package com.example.map;

import com.example.client.MyLog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class Main extends Activity implements OnClickListener{

	private String TAG = "主界面";
	private Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ctx = Main.this;
		RelativeLayout rltlocation = (RelativeLayout) findViewById(R.id.ce_locations);
		rltlocation.setOnClickListener(this);
		RelativeLayout rltheart = (RelativeLayout) findViewById(R.id.ce_heart);
		rltheart.setOnClickListener(this);
		RelativeLayout rltmessage = (RelativeLayout) findViewById(R.id.ce_message);
		rltmessage.setOnClickListener(this);
		RelativeLayout rltset = (RelativeLayout) findViewById(R.id.ce_set);
		rltset.setOnClickListener(this);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(Main.this);
		
		if(Socket.warn == 1)
		{
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog
			        .setTitle("SOS")
					.setMessage("老人跌倒")
					.setPositiveButton("我已经知道", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					})
					.setNegativeButton("消除报警", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Socket.warn = 0;
							//还有代码返回手环……
						}
					}).setCancelable(true).create();
			alertDialog.show();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ce_locations:
			Intent to_location = new Intent();
			to_location.setClass(Main.this, Map.class);
			MyLog.i(TAG, "跳转至地图界面");
			ctx.startActivity(to_location);// 打开新界面
			break;
		case R.id.ce_heart:
			Intent to_heart = new Intent();
			to_heart.setClass(Main.this, HeartRate.class);
			MyLog.i(TAG, "跳转至心跳界面");
			ctx.startActivity(to_heart);// 打开新界面
			break;
		case R.id.ce_message:
			Intent to_message = new Intent();
			to_message.setClass(Main.this, Message.class);
			MyLog.i(TAG, "跳转至健康信息界面");
			ctx.startActivity(to_message);// 打开新界面
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(Socket.warn == 1)
		{
			new AlertDialog.Builder(this).setTitle("确认退出吗")  
			.setMessage("紧急状态下，默认保留您账户的登录状态")
		    .setIcon(android.R.drawable.ic_dialog_info)  
		    .setPositiveButton("是", new DialogInterface.OnClickListener() {  
		  
		        @Override  
		        public void onClick(DialogInterface dialog, int which) {    
		        	Main.this.finish();
		        	MyActivityManager.finishAllActivity();
		        }  
		    })  
		    .setNegativeButton("否",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();  
		}
		else{
			new AlertDialog.Builder(this).setTitle("退出后保留登录状态吗？")  
		    .setIcon(android.R.drawable.ic_dialog_info)  
		    .setPositiveButton("保留", new DialogInterface.OnClickListener() {  
		  
		        @Override  
		        public void onClick(DialogInterface dialog, int which) {    
		        	Main.this.finish();
		        	MyActivityManager.finishAllActivity();
		        }  
		    })  
		    .setNeutralButton("不保留", new DialogInterface.OnClickListener() {  
		  
		        @Override  
		        public void onClick(DialogInterface dialog, int which) {  
			        Main.this.finish();
		        	MyActivityManager.finishAllActivity();
		        		stopSocket();
		        }  
		    })
		    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();  

		}
	}

	protected void stopSocket() {
		// TODO Auto-generated method stub
		Intent stopIntent = new Intent(this, Socket.class);  
		stopService(stopIntent);  
		MyLog.i(TAG , "Socket已终止");
	}
	

}
