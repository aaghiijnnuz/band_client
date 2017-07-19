package com.example.warn;

import com.example.client.MyLog;
import com.example.map.Map;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

/**报警（未完成）
 * @author 黄子健
 *
 */
public class warn 
{
	public void onReceive(Context context, Intent intent) {  
	        MyLog.d("锁屏警告", intent.getAction());  
	        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);  
	        if (km.inKeyguardRestrictedInputMode()) {  
	            Intent alarmIntent = new Intent(context, Map.class);  
	            alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	            context.startActivity(alarmIntent);  
	        }  
	    }  

			
}