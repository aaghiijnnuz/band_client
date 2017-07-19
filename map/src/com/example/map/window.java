package com.example.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class window extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final Window win = getWindow();  
	     win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED  
	             | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD  
	             | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON  
	             | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); 
	}

}
