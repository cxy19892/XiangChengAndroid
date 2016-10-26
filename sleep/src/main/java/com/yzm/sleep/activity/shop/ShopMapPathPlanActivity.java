package com.yzm.sleep.activity.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.bean.ClinicBean;
import com.yzm.sleep.utils.Util;

/**
 * 商店路线规划
 * @author tianxun
 * @params ClinicBean clinic
 */
public class ShopMapPathPlanActivity extends BaseActivity implements OnGetRoutePlanResultListener, OnCheckedChangeListener{
	private MapView mapView;
	private BaiduMap mBaiduMap;
	private RoutePlanSearch mSearch;
	private LocationClient mLocClient;
	private BDLocation mLocation;
	private boolean isFirstLoc = true;
	private PlanNode startPo, endPo;
	private ClinicBean clinic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mappathplan);
		findViewById(R.id.back).setOnClickListener(this);
		RadioGroup group = (RadioGroup) findViewById(R.id.group);
		group.setOnCheckedChangeListener(this);
		clinic = (ClinicBean) getIntent().getSerializableExtra("bean");
		mapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mapView.getMap();
		mBaiduMap.setMaxAndMinZoomLevel(20, 5);
		mapView.showZoomControls(true);
		//开启交通图   
		mBaiduMap.setTrafficEnabled(true);
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		try {
			double x =Double.parseDouble(clinic.getLocation_x()); //经度
			double y =Double.parseDouble(clinic.getLocation_y()); //纬度
			LatLng endll= new LatLng(y, x); 
			endPo = PlanNode.withLocation(endll);
		} catch (Exception e) {
		}
		
		locationData();
	}
	
	/**
	 * 定位
	 */
	private void locationData(){
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
		// 当不需要定位图层时关闭定位图层  
		mBaiduMap.setMyLocationEnabled(false);
		mBaiduMap.setBuildingsEnabled(true);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
	}
	
	/**
	 * 定位监听
	 */
	private BDLocationListener myListener = new BDLocationListener(){
		@Override
		public void onReceiveLocation(BDLocation location) {
			 if(mLocClient != null)
				mLocClient.stop();
			 if(location == null){
				Util.show(ShopMapPathPlanActivity.this, "定位失败");
				return;
			 }
			 if(mBaiduMap == null)
				 return;
			 mLocation = location; 
		     MyLocationData locData = new MyLocationData.Builder()
             .accuracy(location.getRadius()) // 此处设置开发者获取到的方向信息，顺时针0-360
             .direction(100).latitude(location.getLatitude())
             .longitude(location.getLongitude()).build();
		     mBaiduMap.setMyLocationData(locData);
		     //移动到指定位置
		     if(isFirstLoc){
		    	 isFirstLoc = false;
		    	 LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
		    	 BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);  
		    	 //构建MarkerOption，用于在地图上添加Marker  
		    	 OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);  
		    	 //在地图上添加Marker，并显示  
                 MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll); 
                 if(u ==null )
                	 return;
                 try {
                	 mBaiduMap.animateMapStatus(u); //以动画的方式更新地图
                     mBaiduMap.addOverlay(option);
                     startPo = PlanNode.withLocation(ll);
                     drive();
				 } catch (Exception e) {
				 }
		     }
		    
		}
	};
	
	/**
	 * 公交
	 */
	private void bus(){
		try {
			if(!Util.checkNetWork(this)){
				Util.show(this, "请检查网络");
				return;
			}
			
			if(startPo == null || mLocation == null || endPo==null)
				return;
			 mBaiduMap.clear();
			 mSearch.transitSearch((new TransitRoutePlanOption())
					  .from(startPo)
					  .city(mLocation.getCity())  
					  .to(endPo));
		} catch (Exception e) {
		}
	}
	
	/**
	 * 驾车
	 */
	private void drive(){
		try {
		
			if(!Util.checkNetWork(this)){
				Util.show(this, "请检查网络");
				return;
			}
			
			if(startPo == null || endPo==null || this.isFinishing())
				return;
			
			 mBaiduMap.clear();
			 mSearch.drivingSearch((new DrivingRoutePlanOption())
	                 .from(startPo)
	                 .to(endPo));
			
		} catch (Exception e) {
		}
	}
	
	/**
	 * 步行
	 */
	private  void walk(){
		try {
			
			if(!Util.checkNetWork(this)){
				Util.show(this, "请检查网络");
				return;
			}
			
			if(startPo == null || endPo==null)
				return;
			mBaiduMap.clear();
			mSearch.walkingSearch((new WalkingRoutePlanOption())
					 .from(startPo)
	                 .to(endPo));
		
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.by_bus:
			bus();
			break;
		case R.id.by_drive:
			drive();
			break;
		case R.id.by_walk:
			walk();
			break;
		default:
			break;
		}
	}

	
   private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }
   
   private class MyTransitRouteOverlay extends TransitRouteOverlay {

       public MyTransitRouteOverlay(BaiduMap baiduMap) {
           super(baiduMap);
       }

       @Override
       public BitmapDescriptor getStartMarker() {
           return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
       }

       @Override
       public BitmapDescriptor getTerminalMarker() {
           return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
       }
   }
   

   //定制RouteOverly
   private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

       public MyDrivingRouteOverlay(BaiduMap baiduMap) {
           super(baiduMap);
       }

       @Override
       public BitmapDescriptor getStartMarker() {
           return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
       }

       @Override
       public BitmapDescriptor getTerminalMarker() {
           return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
       }
   }

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null ){
			 Util.show(ShopMapPathPlanActivity.this, "抱歉，未找到结果");
			 loacMy();
			 return;
		}
		else{
			if(result.error == SearchResult.ERRORNO.NETWORK_ERROR || result.error == SearchResult.ERRORNO.NETWORK_TIME_OUT){
				 Util.show(ShopMapPathPlanActivity.this, "请检查网络");
				 loacMy();
				 return;
			}else if(result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND){
				 Util.show(ShopMapPathPlanActivity.this, "没有找到检索结果");
				 loacMy();
				 return;
			}else if(result.error == SearchResult.ERRORNO.ST_EN_TOO_NEAR){
				 Util.show(ShopMapPathPlanActivity.this, "起终点太近");
				 loacMy();
				 return;
			}else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            result.getSuggestAddrInfo();
	            loacMy();
	            return;
	        }else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	        	 DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
	             mBaiduMap.setOnMarkerClickListener(overlay);
	             overlay.setData(result.getRouteLines().get(0));
	             overlay.addToMap();
	             overlay.zoomToSpan();
	        }
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (result == null ){
			 Util.show(ShopMapPathPlanActivity.this, "抱歉，未找到结果");
		}
		else{
			if(result.error == SearchResult.ERRORNO.NETWORK_ERROR || result.error == SearchResult.ERRORNO.NETWORK_TIME_OUT){
				 Util.show(ShopMapPathPlanActivity.this, "请检查网络");
				 loacMy();
				 return;
			}else if(result.error == SearchResult.ERRORNO.NOT_SUPPORT_BUS){
				 Util.show(ShopMapPathPlanActivity.this, "该城市不支持公交搜索");
				 loacMy();
				 return;
			}else if(result.error == SearchResult.ERRORNO.NOT_SUPPORT_BUS_2CITY){
				 Util.show(ShopMapPathPlanActivity.this, "不支持跨城市公交");
				 loacMy();
				 return;
			}else if(result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND){
				 Util.show(ShopMapPathPlanActivity.this, "没有找到检索结果");
				 loacMy();
				 return;
			}else if(result.error == SearchResult.ERRORNO.ST_EN_TOO_NEAR){
				 Util.show(ShopMapPathPlanActivity.this, "起终点太近");
				 loacMy();
				 return;
			}else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            result.getSuggestAddrInfo();
	            loacMy();
				return;
	        }else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	        	 TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
	             mBaiduMap.setOnMarkerClickListener(overlay);
	             overlay.setData(result.getRouteLines().get(0));
	             overlay.addToMap();
	             overlay.zoomToSpan();
	        }
		}
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null ){
			 Util.show(ShopMapPathPlanActivity.this, "抱歉，未找到结果");
			 loacMy();
			 return;
		}
		else{
			if(result.error == SearchResult.ERRORNO.NETWORK_ERROR || result.error == SearchResult.ERRORNO.NETWORK_TIME_OUT){
				 Util.show(ShopMapPathPlanActivity.this, "请检查网络");
				 loacMy();
				 return;
			}
			else if(result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND){
				 Util.show(ShopMapPathPlanActivity.this, "没有找到检索结果");
				 loacMy();
				 return;
			}else if(result.error == SearchResult.ERRORNO.ST_EN_TOO_NEAR){
				 Util.show(ShopMapPathPlanActivity.this, "起终点太近");
				 loacMy();
				 return;
			}else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            result.getSuggestAddrInfo();
	            loacMy();
				return;
	        }else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
	            mBaiduMap.setOnMarkerClickListener(overlay);
	            overlay.setData(result.getRouteLines().get(0));
	            overlay.addToMap();
	            overlay.zoomToSpan();
	        }
		}
	}
	
	
	private void loacMy(){
		if (mLocation ==null || mBaiduMap ==null) 
			return;
		
		 MyLocationData locData = new MyLocationData.Builder()
         .accuracy(mLocation.getRadius()) // 此处设置开发者获取到的方向信息，顺时针0-360
         .direction(100).latitude(mLocation.getLatitude())
         .longitude(mLocation.getLongitude()).build();
		 
	     LatLng ll = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
    	 BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);  
    	 //构建MarkerOption，用于在地图上添加Marker  
    	 OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);  
		 //在地图上添加Marker，并显示  
		 mBaiduMap.addOverlay(option);
	     mBaiduMap.setMyLocationData(locData);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		try {
			mLocClient.unRegisterLocationListener(myListener);
		} catch (Exception e) {
		}
		mapView.onPause();
		MobclickAgent.onPageEnd("Service_Institute_Intro_Navigate"); 
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		MobclickAgent.onPageStart("Service_Institute_Intro_Navigate");
		MobclickAgent.onResume(this); 
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mLocClient != null)
			mLocClient.stop();
		mSearch.destroy();
		mapView.onDestroy();
	}

}
