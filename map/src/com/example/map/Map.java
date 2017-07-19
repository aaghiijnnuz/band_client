package com.example.map;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.DPoint;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.CoordinateConverter.CoordType;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.example.client.MyLog;
import com.example.client.SocThread;
import com.example.map.R;
import com.example.map.util.AMapUtil;
import com.example.map.util.ToastUtil;
import com.example.util.DisposeString;
import com.example.view.TitleBar;
import com.show.api.ShowApiRequest;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**主类和地图调用
 * @author 29006
 *
 */
public class Map extends Activity implements
OnGeocodeSearchListener{
	private static ProgressDialog progDialog = null;
	private static GeocodeSearch geocoderSearch;
	private String addressName;
	private AMap aMap;
	private MapView mapView;
	private Marker regeoMarker;
	double lat,lon;
	private static LatLonPoint latLonPoint;
	boolean f;
	
	private Context ctx;
	private static  String TAG = "地图";
	private TitleBar mTitleBarView;
	
	public static int running = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		ctx = Map.this;
/*		if(Socket.warn == 1)
		{
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog
			        .setTitle("SOS")
					.setMessage("老人跌倒")
					.setPositiveButton("进入查看老人位置", new DialogInterface.OnClickListener() {
						
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

		running = 1;*/
		mTitleBarView=(TitleBar) findViewById(R.id.title_bar);
		initTitleView();
//		getAddress(latLonPoint);
		
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.VISIBLE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setBtnRight(R.drawable.refresh);
		mTitleBarView.setTitleText(R.string.title_map);
		if(Socket.warn == 1)
		mTitleBarView.setBackgroundColor(Color.parseColor("#e24949"));
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					Socket.send(2,"");
//					latLonPoint = new LatLonPoint(23.044153, 113.389374);
					getAddress(latLonPoint);
				}
				catch (Exception ee)
				{
					MyLog.i("GPS", "信息请求失败");
					dismissDialog();
					ToastUtil.show(Map.this,"信息请求失败");
				}
			}
			
		});
	}

	
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		}
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);

	}
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(Map.this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "地图界面退出");
		MyActivityManager.popOneActivity(Map.this);
		//stopSocket();
	}
	
	
	
	/**
	 * 显示进度条对话框
	 */
	public static  void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
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


	/**
	 * 响应逆地理编码
	 */
	public static  void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		MyLog.i(TAG, "开始更新地图：GPS坐标："+latLonPoint.toString());
			RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
					GeocodeSearch.GPS);// 第一个参数表示一个Latlng，第二参数表示范围多少米（原参数200），第三个参数表示是火星坐标系还是GPS原生坐标系
			geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}


	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 1000) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				CoordinateConverter converter  = new CoordinateConverter(); 
				// CoordType.GPS 待转换坐标类型
				converter.from(CoordType.GPS); 
				// sourceLatLng待转换坐标点 DPoint类型
				converter.coord(AMapUtil.convertToLatLng(latLonPoint)); 
				// 执行转换操作
				LatLng desLatLng = converter.convert();
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						desLatLng, 15)); //  AMapUtil.convertToLatLng(latLonPoint)
				regeoMarker.setPosition(desLatLng);
				ToastUtil.show(Map.this, addressName);
			} else {
				ToastUtil.show(Map.this, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(Map.this, rCode);
			MyLog.e(TAG, "更新地图：error "+rCode);
		}
	}


	/**无需使用
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	/**将地址坐标保存在本地
	 * @param latlonPoint
	 */
	public static void Save_latlonpoint (LatLonPoint latlonPoint)
	{
		latLonPoint = latlonPoint;
		getAddress(latLonPoint);
	}


	}

