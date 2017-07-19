package com.example.map;

import com.example.map.R;
import com.example.map.util.ToastUtil;
import com.example.view.TitleBar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.client.*;

/**设置界面的处理
 * @author 黄子健
 *
 */
public class RegisterNumber extends Activity {
	private Button btnnext;
	private EditText account;
	private EditText password;
	private EditText number;
	private EditText nam;	
	private String TAG="=Setting=";
	private String sendstr = "";
	Handler mhandlerSend;
	private Context ctx;
	Handler mhandler;
	
	private TitleBar mTitleBarView;
	
	private int process;
	SocThread socketThread;
	private ProgressDialog progDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.register_number);
		btnnext = (Button) findViewById(R.id.next);
		account = (EditText) findViewById(R.id.rr_telephone);
		password = (EditText) findViewById(R.id.rr_password);
		number = (EditText) findViewById(R.id.rr_number);
		
        
		super.onCreate(savedInstanceState);
        ctx = RegisterNumber.this;
        progDialog = new ProgressDialog(this);
        
        MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(RegisterNumber.this);
		
		btnnext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RegisterNumber.this, RegisterGuardian.class);
				MyLog.i(TAG, "跳转至注册界面");
				ctx.startActivity(intent);
				// 发送数据
/*				startSocket();
				showDialog();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
*/				MyLog.i(TAG, "准备发送数据");
				sendstr = account.getText().toString().trim()+","
				+password.getText().toString().trim()+","
				+number.getText().toString().trim()+",";
				//以上为发送字符串的样式
				Socket.send(4,account.getText().toString().trim()+",");
				Socket.send(5,sendstr);
			}
		});
		mTitleBarView=(TitleBar) findViewById(R.id.title_bar);
		initTitleView();

	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setTitleText(R.string.title_phoneNumber);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	/**socket开始
	 * 
	 */
	public void startSocket() {
		if(Socket.socket_run != 1)
		{
			Intent startIntent = new Intent(this, Socket.class);  
		    startService(startIntent);
		    Intent startIntent1 = new Intent(this, link.class);  
		    startService(startIntent1);
			MyLog.d(TAG, "Socket已开始");
		}
		
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MyActivityManager.popOneActivity(RegisterNumber.this);
	}
	
	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在处理");
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
}

