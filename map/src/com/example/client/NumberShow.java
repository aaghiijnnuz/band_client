package com.example.client;

import com.example.view.Digital;

import android.os.Handler;

public class NumberShow extends Thread{
	
	private int value;
	private int rate;
	private Digital digitalView;
	Handler showhandler;

	public NumberShow (Handler show) {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
	}

}
