package com.example.map;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.example.client.MyLog;
import com.example.view.TitleBar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterGuardian extends Activity{

	private Spinner sex;
	int ssex ;
	String TAG = "注册详细信息";
	
	private TitleBar mTitleBarView;
	private EditText name;
	private EditText age;
	private EditText emergency;
	private ProgressDialog progDialog = null;
	private Button btnfinish;
	protected String sendstr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_guardian);
        sex = (Spinner) findViewById(R.id.sex2);
        name = (EditText) findViewById(R.id.nam2);
        age = (EditText) findViewById(R.id.age2);
        emergency = (EditText) findViewById(R.id.guardian_tel2); 
        btnfinish = (Button) findViewById(R.id.finish);
        sex.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					ssex = arg2;
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
        });
        
        btnfinish.setOnClickListener(new OnClickListener() {

			private String sendstring,sendstrin;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyLog.i(TAG, "准备发送数据");
				if (ssex == 0) {
					sendstr = name.getText().toString().trim()+"," //.getBytes("GBK")
							+"男"+","+age.getText().toString().trim()+",";
				}
				else {
					sendstr = name.getText().toString().trim()+"," //.getBytes("GBK")
							+"女"+","+age.getText().toString().trim()+",";
				}
					//以上为发送字符串的样式
					Socket.send(6,sendstr);
					
				Socket.send(7,emergency.getText().toString().trim()+",");
			}
        	
        });
        
        MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(RegisterGuardian.this);

		mTitleBarView=(TitleBar) findViewById(R.id.title_bar);
		initTitleView();
		
		progDialog  = new ProgressDialog(this);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MyActivityManager.popOneActivity(RegisterGuardian.this);
	}

	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setTitleText(R.string.title_guardian);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
