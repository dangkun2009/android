package com.chenyao.baidumaptest;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {
	private LocationManager locationManager;
	private String provider;
//	百度地图控件  
    private MapView mMapView = null;  
//  百度地图对象  
    private BaiduMap bdMap; 
//	定位相关声明
	public LocationClient locationClient = null;
//	自定义图标
	BitmapDescriptor mCurrentMarker = null;
//	是否首次定位
	boolean isFirstLoc = true;
	
	public BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			bdMap.setMyLocationData(locData);	//设置定位数据
			
			if (isFirstLoc) {
				isFirstLoc = false;
				
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);	//设置地图中心点以及缩放级别
				bdMap.animateMapStatus(u); 
			}
		}
	};
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_main);  
        init();
        locationManager = (LocationManager) getSystemService(Context.
        		LOCATION_SERVICE);
//		 获取所有可用的位置提供器
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else {
//		当没有可用的位置提供器时，弹出Toast提示用户
			Toast.makeText(this, "No location provider to use",
			Toast.LENGTH_SHORT).show();
		//	return;
		}
		bdMap = mMapView.getMap();
		//开启定位图层
        bdMap.setMyLocationEnabled(true);
      
		locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
		locationClient.registerLocationListener(myListener); // 注册监听函数
		this.setLocationOption();	//设置定位参数
		locationClient.start(); // 开始定位
		
//		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mapView);
		
	}
	private void init() {  
        mMapView = (MapView) findViewById(R.id.map_view);  
    } 
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向
		locationClient.setLocOption(option);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume(); 
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();  
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		locationClient.stop();
		bdMap.setMyLocationEnabled(false);
		mMapView.onDestroy();  
        mMapView = null;  
		super.onDestroy();
	}
	private void navigateTo(Location location) {
//		MapController controller = mapView.getController();
//		controller.setZoom(16); // 设置缩放级别
//		GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6),
//		(int) (location.getLongitude() * 1E6));
//		controller.setCenter(point); // 设置地图中心点
		}

}
