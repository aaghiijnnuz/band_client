package com.example.map;

import com.example.client.MyLog;
import com.example.map.util.ToastUtil;
import com.example.view.Digital;
import com.example.view.TitleBar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class HeartRate extends Activity{

	private TitleBar mTitleBarView;
	private static Digital digitalView;
	private static int rate = 0;
	private int number = 3;
	private boolean heart_run = true;
	
	static CalThread calThread;

	class CalThread extends Thread  {  
		int value = 0;
		int rate = 50;
		private String TAG = "心率数字显示";
		public Handler hearthandler;
		@Override
		public void run() {
			Looper.prepare();
			hearthandler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
				try {
					rate = (Integer) msg.obj;
					while (value < rate) {
						value++;
						digitalView.post(new Runnable() {

							@Override
							public void run() {
								digitalView.setValue(value);
							}
						});
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					while (value > rate) {
						value--;
						digitalView.post(new Runnable() {

							@Override
							public void run() {
								digitalView.setValue(value);
							}
						});
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (Exception ee) {
					MyLog.i(TAG, "加载过程出现异常");
					ee.printStackTrace();
				}
				}
				
			};	
			Looper.loop();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heart_rate);
		mTitleBarView=(TitleBar) findViewById(R.id.title_bar);
		initTitleView();
		digitalView = (Digital) findViewById(R.id.digitalView);
		digitalView.setNumbers(number);
		calThread = new CalThread();  
		calThread.start();
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.VISIBLE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setBtnRight(R.drawable.refresh);
		if(Socket.warn == 1)
			mTitleBarView.setBackgroundColor(Color.parseColor("#e24949"));
		mTitleBarView.setTitleText(R.string.title_heart_rate);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//待以后完善
				save_rate(rate);
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(HeartRate.this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MyActivityManager.popOneActivity(HeartRate.this);
		heart_run = false;
	}
	
	public static void save_rate (int heart_rate) {
		rate = heart_rate;
		Message msg = new Message();
		msg.what = 1;
		msg.obj = heart_rate;
		calThread.hearthandler.sendMessage(msg);
	}

}
