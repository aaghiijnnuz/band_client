package com.example.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import com.example.util.Data;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**socket连接线程
 * @author 29006
 *
 */
public class SocThread extends Thread {
	private static String ip = "120.24.83.227"; //121.42.189.114
	private static int port = 9990; //9990
	private static String TAG = "socket thread";
	private static int timeout = 10000;

	public static Socket client = null;
	static PrintWriter out;
	static BufferedReader in;
	public static boolean isRun = true;
	Handler inHandler;
	static Handler outHandler;
	Context ctx;
	private static String TAG1 = "===Send===";
	SharedPreferences sp;
	public static boolean SocThreadRUN = false;
	
	public static int sending = 0;

	public SocThread(Handler handlerin, Handler handlerout, Context context) {
		inHandler = handlerin;
		outHandler = handlerout;
		ctx = context;
		MyLog.i(TAG, "创建线性socket");
	}

	/**
	 * 连接socket服务器
	 */
	public static void conn() {

		try {
			Log.i(TAG, "连接中");
			client = new Socket(ip, port);
			client.setSoTimeout(timeout);// 设置阻塞时间
			MyLog.i(TAG, "连接成功");
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					client.getOutputStream())), true);
			MyLog.i(TAG, "输入输出流获取成功");
		} catch (UnknownHostException e) {
			MyLog.i(TAG, "连接错误UnknownHostException 重新获取");
			e.printStackTrace();
			conn();
		} catch (IOException e) {
			MyLog.i(TAG, "连接服务器io错误");
			e.printStackTrace();
		} catch (Exception e) {
			MyLog.i(TAG, "连接服务器错误Exception" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 实时接收数据
	 */
	@Override
	public void run() {
		MyLog.i(TAG, "线性socket开始运行");
		conn();
		MyLog.i(TAG, "1.run开始");
		String line = "";
		while (isRun) {
			while(SocThreadRUN){
				try {
					if (client != null) {
						MyLog.i(TAG, "2.检测数据");
							while ((line= in.readLine())  != null) {
								MyLog.i(TAG, "3.getdata" + line + " len=" + line.length());
								MyLog.i(TAG, "4.start set Message");
								Message msg = inHandler.obtainMessage();
								msg.obj = line;
								inHandler.sendMessage(msg);// 结果返回给UI处理
								MyLog.i(TAG1, "5.send to handler");}
							if (line == null)
							{
								MyLog.i(TAG, "掉线，重新连接");			
								conn();
								Send("E22,"+com.example.map.Socket.log);
							}
					} else {
						MyLog.i(TAG, "没有可用连接");
						conn();
					}
				} catch (Exception e) {
					MyLog.i(TAG, "数据接收错误" + e.getMessage());
					e.printStackTrace();
				}
			}
			
		}
	}
	
	/**
	 * 发送数据
	 * 
	 * @param mess
	 */
	public static void Send(String mess) {
		try {
			if (client != null) {
				MyLog.i(TAG1, "发送" + mess + "至"
						+ client.getInetAddress().getHostAddress() + ":"
						+ String.valueOf(client.getPort()));
				out.println(mess);
				out.flush();
				MyLog.i(TAG1, "发送成功");
				Message msg = outHandler.obtainMessage();
				msg.obj = mess;
				msg.what = 1;
				outHandler.sendMessage(msg);// 结果返回给UI处理
				sending = 0;
			} else {
				MyLog.i(TAG, "client 不存在");
				Message msg = outHandler.obtainMessage();
				msg.obj = mess;
				msg.what = 0;
				outHandler.sendMessage(msg);// 结果返回给UI处理
				MyLog.i(TAG, "连接不存在重新连接");
				conn();
			}

		} catch (Exception e) {
			MyLog.i(TAG1, "send error");
			sending = 1;
			e.printStackTrace();
		} finally {
			MyLog.i(TAG1, "发送完毕");

		}
	}

	/**
	 * 关闭连接
	 */
	public void close() {
		try {
			if (client != null) {
				MyLog.i(TAG, "close in");
				in.close();
				MyLog.i(TAG, "close out");
				out.close();
				MyLog.i(TAG, "close client");
				client.close();
			}
		} catch (Exception e) {
			MyLog.i(TAG, "close err");
			e.printStackTrace();
		}

	}
}
