package com.example.util;

import java.util.StringTokenizer;

import com.amap.api.services.core.LatLonPoint;

import android.R.integer;

/**字符串解析类
 * @author 黄子健
 *
 */
public class DisposeString {
	
	private static String TAG = "字符串解析";
	
	/**获得纬度GPS
	 * @param 字符串
	 * @return 双精度值
	 */
	public static double gps_latitude (String s)
	{
		double f;
		String l = null;
		StringTokenizer latitude = new StringTokenizer(s,",");
		for(int i=0;i<=3;i++)
		{
			 l = latitude.nextToken();
		}
		f = Double.parseDouble(l);
		l = latitude.nextToken();
//		if(l.equals("S"))
//		{
//			return -((int)(f/100) + (f/100-(int)(f/100)) / 60*100);
//		}
//		else
		{
			return (int)(f/100) + (f/100-(int)(f/100)) / 60*100;
		}
		
	}
	
	/**获得经度GPS
	 * @param 字符串
	 * @return 双精度值
	 */
	public static double gps_longitude (String s)
	{
		double f;
		String l = null;
		StringTokenizer longitude = new StringTokenizer(s,",");
		for(int i=0;i<=4;i++)
		{
			 l = longitude.nextToken();
		}
		f = Double.parseDouble(l);
		l = longitude.nextToken();
//		if(l.equals("W"))
//		{
//			return -((int)(f/100) + (f/100-(int)(f/100))*100/60);
//		}
//		else
		{
			return (int)(f/100) + (f/100-(int)(f/100))*100/60;
		}	
	}
	
	public static double gsm_longitude (String s){
		String l = null;
		double f;
		StringTokenizer divide = new StringTokenizer(s,"\"");
		for(int i=0;i<=17;i++)
		{
			 l = divide.nextToken();
		}
		f = Double.parseDouble(l);
		return f;
	}
	
	public static double gsm_radius (String s){
		String l = null;
		double f;
		StringTokenizer divide = new StringTokenizer(s,"\"");
		for(int i=0;i<=25;i++)
		{
			 l = divide.nextToken();
		}
		f = Double.parseDouble(l);
		return f;
	}
	
	public static double gsm_latitude (String s){
		String l = null;
		double f;
		StringTokenizer divide = new StringTokenizer(s,"\"");
		for(int i=0;i<=29;i++)
		{
			 l = divide.nextToken();
		}
		f = Double.parseDouble(l);
		return f;
	}

	/**判断跌倒
	 * @param 字符串
	 * @return 布尔值
	 */
	public static boolean get_judge (String s)
	{
		String l = null;
		StringTokenizer judge = new StringTokenizer(s,",");
		for(int i=0;i<=0;i++)
		{
			 l = judge.nextToken();
		}
		if(Integer.parseInt(l) == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**读取包头
	 * @param 字符串
	 * @return 整型值
	 */
	public static int get_title (String s)
	{
		String l = null;
		StringTokenizer title = new StringTokenizer(s,",");
		l = title.nextToken();
		return Integer.parseInt(l.substring(1, 3));
	}
	
	/**读取错误码
	 * @param 字符串
	 * @return 整型值
	 */
	public static int get_err (String s)
	{
		String l = null;
		StringTokenizer err = new StringTokenizer(s,",");
		for(int i=0;i<=1;i++)
		{
			 l = err.nextToken();
		}
		return Integer.parseInt(l);
	}
	
	public static int get_heartrate (String s) {
		String l = null;
		StringTokenizer rate = new StringTokenizer(s,",");
		for(int i=0;i<=1;i++) {
			l = rate.nextToken();
		}
		return Integer.parseInt(l);
	}
}

