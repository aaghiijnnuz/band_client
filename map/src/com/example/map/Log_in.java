package com.example.map;

import com.example.client.MyLog;
import com.example.client.SocThread;
import com.example.client.link;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.map.R;
import com.example.map.util.ToastUtil;
import com.example.util.DisposeString;

import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

/**登录界面
 * @author mr huang
 *
 */
public class Log_in extends Activity implements OnClickListener
{
	private String TAG = "登录";
	private EditText account;
	private EditText password;
	private SharedPreferences sp;  
	private String sendstr = "";
	private String userNameValue,passwordValue;
	private ProgressDialog progDialog = null;
	public static int process = 0;
	private Context ctx;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.log_in);
		account = (EditText) findViewById(R.id.name2);
		Button log = (Button) findViewById(R.id.log);
		log.setOnClickListener(this);
		password = (EditText) findViewById(R.id.password2);
		Button login = (Button) findViewById(R.id.login);
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
		login.setOnClickListener(this);
		super.onCreate(savedInstanceState);
		ctx = Log_in.this;
		MyLog.d(TAG, "创建完成");

		progDialog = new ProgressDialog(this);
		
		if(Socket.socket_run == 1)
		{
			Intent dialogIntent = new Intent(ctx, Main.class);   
//			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //FLAG_ACTIVITY_NEW_TASK
			ctx.startActivity(dialogIntent);
		}
		      
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(Log_in.this);
		
		startSocket();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.log:
			// 发送数据
			MyLog.i(TAG, "准备发送数据");
			sendstr = account.getText().toString().trim()+","+password.getText().toString().trim()+",";
			Socket.send(1,sendstr);
			process = 1;
			break;
		case R.id.login:
			// 跳转至注册界面
//			Socket.send(2,"");
			Intent intent = new Intent();
			intent.setClass(Log_in.this, RegisterNumber.class);
			MyLog.i(TAG, "跳转至注册界面");
			ctx.startActivity(intent);// 打开新界面
			break;
		default:
			break;
		}
	}

/**socket开始
 * 
 */
public void startSocket() {
	Intent startIntent = new Intent(this, Socket.class);  
    startService(startIntent);
    Intent startIntent1 = new Intent(this, link.class);  
    startService(startIntent1);
	MyLog.d(TAG, "Socket已开始");
}

/**socket停止
 * 
 */
private void stopSocket() {
	Intent stopIntent1 = new Intent(this, link.class);  
	stopService(stopIntent1); 
	Intent stopIntent = new Intent(this, Socket.class);  
	stopService(stopIntent);   
	MyLog.i(TAG, "Socket已终止");
}

/**
 * 显示进度条对话框
 */
public void showDialog() {
	progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progDialog.setIndeterminate(false);
	progDialog.setCancelable(true);
	progDialog.setMessage("正在验证");
	progDialog.show();
}

/**
 * 隐藏进度条对话框
 */
public void dismissDialog() {
	if (progDialog != null) {
		progDialog.dismiss();
	}
}

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	new AlertDialog.Builder(this).setTitle("确定退出吗？")  
    .setIcon(android.R.drawable.ic_dialog_info)  
    .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
  
        @Override  
        public void onClick(DialogInterface dialog, int which) {  
        // 点击“确认”后的操作  
 //       	if(process >= 1)
        	stopSocket();
        	Log_in.this.finish();
        	MyActivityManager.popOneActivity(Log_in.this);
        }  
    })  
    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
		}
	}).show();  
}

@Override
protected void onStop() {
	// TODO Auto-generated method stub
//	unbindService(connection);
	super.onStop();
}


}
