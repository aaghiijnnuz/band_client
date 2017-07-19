package com.example.client;

import com.example.map.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class link extends Service   {


private String TAG = "心跳包";
private boolean run = true;

@Override
public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	new Thread(new Runnable() {

		@Override
        public void run() {
			while(run){
				 while (Socket.link == 1) {
		                try {
		                    Thread.sleep(1000*11);
		                    Socket.send(3, "");
							MyLog.i(TAG , "开始发送心跳包");
		                } catch (InterruptedException e) {
		                	MyLog.i(TAG, "心跳包发送失败");
		                }
		            }
		        }
			}
           
    }).start();

}




@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	run = false;
}




@Override
public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
}


}
