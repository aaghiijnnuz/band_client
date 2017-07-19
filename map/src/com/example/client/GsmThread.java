package com.example.client;

import java.util.StringTokenizer;

import com.show.api.ShowApiRequest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class GsmThread extends Thread{

	private String TAG = "易源数据";
	private Handler hGSM;
	private Context ctx;

	public GsmThread (Handler handlerGSM,Context context){
		hGSM = handlerGSM;
		ctx = context;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		/*				String res=new ShowApiRequest()
        .addTextPara("mcc","460")
        .addTextPara("mnc","0")
        .addTextPara("lac","9441")
        .addTextPara("cell","56125")
        .addTextPara("coord","wgs84")
        .post();*/
/*		String res=new ShowApiRequest()
        .addTextPara("mcc","460")
        .addTextPara("mnc","1")
        .addTextPara("lac","20859")
        .addTextPara("ci","50331")
        .addTextPara("coord","wgs84")
        .post();
		MyLog.i(TAG , res);	
		Message msg = hGSM.obtainMessage();
		msg.obj = res;
		if(gsm_success(res) == 0)
			msg.what = 1;
		else
			msg.what = 0;
		hGSM.sendMessage(msg);
*/
		

String res=new ShowApiRequest()         
.post();
MyLog.e(TAG , res);

	}
	
	public static int gsm_success (String s){
		String l = null;
		String p = null;
		int f;
		StringTokenizer success = new StringTokenizer(s,",");
		l = success.nextToken();
		StringTokenizer remove = new StringTokenizer(l,":");
		for(int i = 0;i <= 1;i++)
		{
			p = remove.nextToken();
		}
		f = Integer.parseInt(p);
		return f;
	}


}
