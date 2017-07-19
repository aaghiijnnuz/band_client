package com.example.map;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.amap.api.services.core.LatLonPoint;
import com.example.client.MyLog;
import com.example.client.SocThread;
import com.example.client.GsmThread;
import com.example.client.link;
import com.example.map.util.ToastUtil;
import com.example.util.DisposeString;
import com.show.api.ShowApiRequest;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;

/**service 后台运行
 * @author mr huang
 *
 */
public class Socket extends Service 
{
	private static String TAG = "service";
	SocThread socketThread;
	Handler mhandler;
	Handler mhandlerSend;
	private static Context ctx;
	private int title;
	private int err;
	public static String log;
	public static int socket_run = 0; 
	public static int warn = 0;
	
	double lat,lon;
	private link linker;
	private static GsmThread GSMThread;
	private Handler mhandlerGsm;
	private static LatLonPoint latLonPoint;
	public static int link = 0;
	private static String str1;
	private static String str2;
	private static String str3;

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		ctx = Socket.this;
		SocThread.isRun = true;
		super.onCreate();
		final Builder builder1 = new Builder(getApplicationContext()); //getApplicationContext()
		builder1
		.setTitle("SOS")
		.setMessage("老人跌倒")
		.setPositiveButton("保持密切跟踪状态", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.setNeutralButton("消除报警", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				warn = 0;
				//还有代码返回手环……
			}
		})
		.setNegativeButton("马上查看位置", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 跳转至地图界面
				if (Map.running == 0)
				{
					Intent dialogIntent = new Intent(ctx, Main.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //FLAG_ACTIVITY_NEW_TASK
                    ctx.startActivity(dialogIntent);
				MyLog.i(TAG, "跳转至地图界面");
				}
			}
		});
		/**
		*创建Notification,任务栏运行
		*/
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle("快速查看手环位置");
		builder.setContentText("点击此按钮可快速查看被监护人的位置动态");
		Intent intent = new Intent(this, Map.class);
		PendingIntent pendingIntent = PendingIntent.getActivity
		(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		final Notification notification = builder.build();
		//以下语句供测试上述语句的相关功能
//		startForeground(1, notification);
//		Dialog dialog=builder1.create();
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//		dialog.show();
//		warn = 1;
		mhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					MyLog.i(TAG, "mhandler接收到msg=" + msg.what);
					if (msg.obj != null) {
						String s = msg.obj.toString();
						if (s.trim().length() > 0) {
							MyLog.i(TAG, "mhandler接收到obj=" + s);
							title = DisposeString.get_title(s);
							switch (title){
							case 20:
								SocThread.Send(s);
								MyLog.i(TAG, "跳转到地图界面");
								// 跳转至地图界面
								Intent dialogIntent = new Intent(ctx, Main.class);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //FLAG_ACTIVITY_NEW_TASK
                                ctx.startActivity(dialogIntent);
                        		//启动到前台
                        		startForeground(1, notification);
                        		link = 1;
                        		socket_run = 1;
								break;
							case 27:
								err = DisposeString.get_err(s);
								switch(err){
								case 1:
									ToastUtil.show(ctx,"账号不存在");
									break;
								case 2:
									ToastUtil.show(ctx,"密码错误");
									break;
								case 3:
									ToastUtil.show(ctx, "重复注册");
									break;
								}	
								break;
							case 28:
								ToastUtil.show(Socket.this,"注册成功,请返回登陆界面重新登陆");
								Intent dialogIntent2 = new Intent(ctx, Log_in.class);
                                dialogIntent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //FLAG_ACTIVITY_NEW_TASK
                                ctx.startActivity(dialogIntent2);
                                break;
							case 12:
								try
								{
									lat = DisposeString.gps_latitude(s);
									lon = DisposeString.gps_longitude(s);
									latLonPoint = new LatLonPoint(lat, lon);
									MyLog.i("GPS", "GPS坐标："+latLonPoint.toString());
									Map.Save_latlonpoint(latLonPoint);
								}
								catch (Exception ee)
								{
									MyLog.i("GPS", "GPS数据获取错误");
								}
								break;
							case 13:
								MyLog.i("跌倒判断", "跌倒");
								//if (warn == 0)
								{
									warn = 1;
									// 跳转至报警界面
									Dialog dialog=builder1.create();
									dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
									dialog.show();
									MyLog.d(TAG, "显示报警界面");
								}
								try
								{
									lat = DisposeString.gps_latitude(s);
									lon = DisposeString.gps_longitude(s);
									latLonPoint = new LatLonPoint(lat, lon);
									MyLog.i("GPS", "GPS坐标："+latLonPoint.toString());
									Map.Save_latlonpoint(latLonPoint);
								}
								catch (Exception ee)
								{
									MyLog.i("GPS", "GPS数据获取错误");
								}
								break;
							case 18:
								MyLog.i(TAG, "心率");
								HeartRate.save_rate(DisposeString.get_heartrate(s));
								break;
							}
							}
						} else {
							Log.i(TAG, "加载出现异常");
						}
				} catch (Exception ee) {
					MyLog.i(TAG, "没有数据返回");
					ee.printStackTrace();
				}
			}
		};
		//以下为管理发送程序
		mhandlerSend = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					MyLog.i(TAG, "mhandlerSend接收到msg.what=" + msg.what);
					String s = msg.obj.toString();
					if (msg.what == 1) {
						MyLog.d(TAG, "APP："+ s + "发送成功");
					} else {
						MyLog.d(TAG, "APP："+ s + "发送失败");
						ToastUtil.show(ctx,"请检查您的网络状态");
					}
				} catch (Exception ee) {
					MyLog.i(TAG, "加载过程出现异常");
					ee.printStackTrace();
				}
			}
		};
		mhandlerGsm = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what == 1){
					MyLog.d(TAG, "GSM："+ msg.obj + "成功定位");
					String s = msg.obj.toString();
					MyLog.e(TAG , s);
					lat = DisposeString.gsm_latitude(s);
					lon = DisposeString.gsm_longitude(s);
					latLonPoint = new LatLonPoint(lat, lon);
					MyLog.i("GSM", "GSM坐标："+latLonPoint.toString());
					Map.Save_latlonpoint(latLonPoint);
				}
				else {
					MyLog.d(TAG, "GSM："+ msg.obj + "定位失败");
					ToastUtil.show(ctx,"定位失败");
				}
			}
		};

		socketThread = new SocThread(mhandler, mhandlerSend, ctx);
		socketThread.start();
		
		GSMThread = new GsmThread(mhandlerGsm,ctx);
//    	link = 1;

		MyLog.d(TAG, "service开启");
		
		String s = "王";
		try {
			byte[] utf8 = s.getBytes("gbk");
			System.out.println(Arrays.toString(utf8));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MyLog.d(TAG, "service关闭");
		super.onDestroy();
		socketThread.isRun = false;
		socketThread.close();
		GSMThread = null;
		socketThread = null;
		link = 0;
		socket_run = 0;
		SocThread.SocThreadRUN = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		return super.onStartCommand(intent, flags, startId);
	}
	
    public static void send(int a,String s) 
    {
    	SocThread.SocThreadRUN = true;
    	switch(a){
    	case 1:
    		MyLog.d(TAG,"登录包"+ s);
    		log = s ;
    		SocThread.Send("E22,"+s);
    		
    		// 仅供测试：跳转至地图界面(耐心等待，可能失败）
//			Intent dialogIntent = new Intent(ctx, Main.class);   
//			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //FLAG_ACTIVITY_NEW_TASK
//			ctx.startActivity(dialogIntent);
    		break;
    	case 2:
    		MyLog.d(TAG, "数据获取+GPS");
//    		SocThread.Send("E23,"+"1");
//    		GSMThread.start();
//    		latLonPoint = new LatLonPoint(23.045028994137862, 113.4040347864406);
//    		MainActivity.Save_latlonpoint(latLonPoint);
    		break;
    	case 3:
    		MyLog.d(TAG, "心跳包");
    		SocThread.Send("E24,");
    		break;
    	case 4:
    		MyLog.d(TAG, "新建账户");
    		str1 = s;
    		break;
    	case 5:
    		MyLog.d(TAG, "新建账户 2");
    		str2 = s;
    		break;
    	case 6:
    		MyLog.d(TAG, "新建账户 3");
    		str3 = s;
    		break;
    	case 7:
    		MyLog.d(TAG, "新建账户 4");
    		SocThread.Send("E21,"+str2+str3+str1+"cancel"+","+s);
    		break;


    	}
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}