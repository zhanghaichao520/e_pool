package com.cc.activity;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.sensor.MyOrientationListener;
import com.baidu.mapapi.sensor.MyOrientationListener.onOrientationListener;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.map.C;
import com.cc.R;
import com.cc.data.Const;
import com.cc.data.MarkInfo;
import com.cc.data.Order;
import com.cc.data.User;
import com.cc.list.LeftFragment;
import com.cc.util.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BasemapActivity extends SlidingFragmentActivity
		implements OnClickListener, OnMapClickListener, OnMarkerClickListener, OnGetGeoCoderResultListener {
	private ImageView myLocation;
	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	private TextView mylocation_txt;
	private TextView mystart_txt;
	MapView mMapView = null;
	BaiduMap myBaiduMap = null;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private boolean isFirstLoc = true;// 设置一个标记，查看是否是第一次
	private EditText searEditText;
	private ImageButton start_go;
	// 修改默认View相关
	private View defaultBaiduMapScaleButton, defaultBaiduMapLogo, defaultBaiduMapScaleUnit;
	// 基本地图类型,实时交通，及覆盖物相关
	private ImageView mapRoad;
	private ImageView mapType;
	private String[] types = { "普通地图", "卫星地图", "热力地图(已关闭)" };
	private float current;// 放大或缩小的比例系数
	private ImageView expandMap;// 放大地图控件
	private ImageView narrowMap;// 缩小地图
	private ImageView addMarks;// 添加覆盖物控件
	private BitmapDescriptor myMarks;
	private List<MarkInfo> markInfoList;
	private LinearLayout markLayout;
	// 定位相关
	private LocationClient myLocationClient;// 专门用于监听位置的客户端对象
	private double latitude, longtitude;// 经纬度
	private BitmapDescriptor myBitmapLocation;// 定位的自定义图标
	public String locationTextString;// 定义的位置的信息
	private TextView locationText;// 显示定位信息的TextView控件
	private MyOrientationListener myOrientationListener;
	private float myCurrentX;
	private ImageView selectLocationMode;
	private String[] LocationModeString = { "罗盘模式", "普通模式", "跟随模式", "3D俯视模式(已关闭)" };
	GeoCoder mSearch = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, "90bbf929c33c29739941e5e3f249b39a");
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_basemap);
		initSlidingMenu(savedInstanceState);
		initevent();
		initMap();
	}

	public void autoScan() {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>() {
			boolean flag = true;
			User myUser = BmobUser.getCurrentUser(User.class);
			private LatLng passengerStart;
			private LatLng passengerEnd;

			private double currentdis = 9999999.0;
			private int currentOrder;
			private int currentNum = 0;
			private Order passenger1 = new Order();

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				while (flag) {
					flag = false;
					BmobQuery<Order> query = new BmobQuery<Order>();
					query.addWhereEqualTo("carNum", "00000");
					query.findObjects(new FindListener<Order>() {

						@Override
						public void done(List<Order> arg0, BmobException arg1) {
							// TODO Auto-generated method stub
							List<Order> res;
							double dis;
							currentNum = 0;
							if (arg1 == null) {
								Order order = new Order();
								for (int i = 0; i < arg0.size(); i++) {
									order = arg0.get(i);
									passengerStart = new LatLng(order.getStartLat(), order.getStartLng());
									dis = DistanceUtil.getDistance(passengerStart, Const.mStartLocationData);
									if (dis > 2000.0) {
										continue;
									}
									if (dis < currentdis) {
										currentdis = dis;
										currentOrder = i;
									}
								}
								if (currentdis == 9999999.0) {
									flag = true;
									// Util.toast("周围没有乘客，去其他地方转转吧",
									// ReturnInfoActivity.this);
								}

							} else {
								flag = true;
								// Util.toast("周围没有乘客，去其他地方转转吧" +
								// arg1.getMessage(), ReturnInfoActivity.this);
							}
						}
					});
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				if (Const.switcher) {
					if (Const.isSetStart) {
						Util.startActivity(BasemapActivity.this, ReturnInfoActivity.class);

					} else {
						Const.mStartLocationData = Const.mLastLocationData;
						addStartInfoOverlay(Const.mLastLocationData);
						Const.isSetStart = true;
						Util.startActivity(BasemapActivity.this, ReturnInfoActivity.class);
					}

				}
			};
		}.execute();
 	}

	public void initevent() {
		// TODO Auto-generated method stub
		topButton = (ImageView) findViewById(R.id.topButton);
		myLocation = (ImageView) findViewById(R.id.my_location);
		topTextView = (TextView) findViewById(R.id.topTv);
		mylocation_txt = (TextView) findViewById(R.id.mylocation_text);
		mystart_txt = (TextView) findViewById(R.id.mystart_text);
		mapRoad = (ImageView) findViewById(R.id.road_condition);
		mapType = (ImageView) findViewById(R.id.map_type);
		expandMap = (ImageView) findViewById(R.id.add_scale);
		narrowMap = (ImageView) findViewById(R.id.low_scale);
		addMarks = (ImageView) findViewById(R.id.map_marker);
		markLayout = (LinearLayout) findViewById(R.id.mark_layout);
		selectLocationMode = (ImageView) findViewById(R.id.map_location);
		start_go = (ImageButton) findViewById(R.id.start_go);
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		topButton.setOnClickListener(this);
		myLocation.setOnClickListener(this);
		mapRoad.setOnClickListener(this);
		mapType.setOnClickListener(this);
		expandMap.setOnClickListener(this);
		narrowMap.setOnClickListener(this);
		addMarks.setOnClickListener(this);
		selectLocationMode.setOnClickListener(this);
		start_go.setOnClickListener(this);
	}

	public void initMap() {
		// TODO Auto-generated method stub
		mMapView = (MapView) findViewById(R.id.map_view_test);
		myBaiduMap = mMapView.getMap();
		// 定位相关
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(20.5f);
		myBaiduMap.animateMapStatus(msu);
		initLocation();
		// 设置起点
		myBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng arg0) {
				// TODO Auto-generated method stub
				Const.mStartLocationData = arg0;
				addStartInfoOverlay(arg0);
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0));
				Const.isSetStart = true;
			}
		});

	}

	private void addStartInfoOverlay(LatLng startInfo) {
		// TODO Auto-generated method stub
		myBaiduMap.clear();
		OverlayOptions options = new MarkerOptions().position(startInfo)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_st)).zIndex(5);
		myBaiduMap.addOverlay(options);
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(false);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
		mLocationClient.setLocOption(option);
		useLocationOrientationListener();// 调用方向传感器

	}

	/**
	 * 
	 * 定位结合方向传感器，从而可以实时监测到X轴坐标的变化，从而就可以检测到
	 * 定位图标方向变化，只需要将这个动态变化的X轴的坐标更新myCurrentX值， 最后在MyLocationData
	 * data.driection(myCurrentX);
	 */
	private void useLocationOrientationListener() {
		myOrientationListener = new MyOrientationListener(BasemapActivity.this);
		myOrientationListener.setMyOrientationListener(new onOrientationListener() {
			@Override
			public void onOrientationChanged(float x) {// 监听方向的改变，方向改变时，需要得到地图上方向图标的位置
				myCurrentX = x;
				// System.out.println("方向:x---->"+x);
			}
		});
	}

	/**
	 * 
	 * 选择地图的类型
	 */
	private void selectMapType() {
		AlertDialog.Builder builder = new AlertDialog.Builder(BasemapActivity.this);
		builder.setIcon(R.drawable.icon).setTitle("请选择地图的类型").setItems(types, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String select = types[which];
				if (select.equals("普通地图")) {
					myBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
				} else if (select.equals("卫星地图")) {
					myBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
				} else if (select.equals("热力地图(已关闭)") || select.equals("热力地图(已打开)")) {
					if (myBaiduMap.isBaiduHeatMapEnabled()) {
						myBaiduMap.setBaiduHeatMapEnabled(false);
						Toast.makeText(BasemapActivity.this, "热力地图已关闭", 0).show();
						types[which] = "热力地图(已关闭)";
					} else {
						myBaiduMap.setBaiduHeatMapEnabled(true);
						Toast.makeText(BasemapActivity.this, "热力地图已打开", 0).show();
						types[which] = "热力地图(已打开)";
					}
				}
			}
		}).show();
	}

	/**
	 * 自定义定位图标
	 */
	private void changeLocationIcon() {
		myBitmapLocation = BitmapDescriptorFactory.fromResource(R.drawable.calibration_arrow);// 引入自己的图标
		if (isFirstLoc) {// 表示第一次定位显示普通模式
			MyLocationConfiguration config = new MyLocationConfiguration(
					com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, true, myBitmapLocation);
			myBaiduMap.setMyLocationConfigeration(config);
		}
	}

	/**
	 * 
	 * 获得最新定位的位置,并且地图的中心点设置为我的位置
	 */
	private void getMyLatestLocation(double lat, double lng) {
		LatLng ll = new LatLng(lat, lng);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		myBaiduMap.animateMapStatus(u);
	}

	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftFragment()).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.road_condition:// 是否打开实时交通
			switchRoadCondition();
			break;
		case R.id.topButton:
			toggle();
			break;
		case R.id.my_location:
			getMyLatestLocation(latitude, longtitude);
			break;
		case R.id.map_type:// 选择地图的类型
			selectMapType();
			break;
		case R.id.add_scale:// 放大地图比例
			expandMapScale();
			break;
		case R.id.low_scale:// 缩小地图比例
			narrowMapScale();
			break;
		case R.id.map_marker:// 添加覆盖物
			addMapMarks();
			break;
		case R.id.map_location:// 选择定位模式
			selectLocation();
			break;
		case R.id.start_go:
			start_go();
		default:
			break;
		}
	}

	public void start_go() {
		// TODO Auto-generated method stub
		if (Const.switcher) {
			Const.switcher = false;
			Util.toast("自动接单已关闭", BasemapActivity.this);
		} else {
			Const.switcher = true;
			autoScan();
			Util.toast("自动接单已开启", BasemapActivity.this);
		}

	}

	/**
	 * 
	 * 选择定位的模式
	 */
	private void selectLocation() {
		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
		builder2.setIcon(R.drawable.track_collect_running).setTitle("请选择定位的模式")
				.setItems(LocationModeString, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String mode = LocationModeString[which];
						if (mode.equals("罗盘模式")) {
							MyLocationConfiguration config = new MyLocationConfiguration(
									com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.COMPASS, true,
									myBitmapLocation);
							myBaiduMap.setMyLocationConfigeration(config);
						} else if (mode.equals("跟随模式")) {
							MyLocationConfiguration config = new MyLocationConfiguration(
									com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING, true,
									myBitmapLocation);
							myBaiduMap.setMyLocationConfigeration(config);
						} else if (mode.equals("普通模式")) {
							MyLocationConfiguration config = new MyLocationConfiguration(
									com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, true,
									myBitmapLocation);
							myBaiduMap.setMyLocationConfigeration(config);
						} else if (mode.equals("3D俯视模式(已关闭)") || mode.equals("3D俯视模式(已打开)")) {
							if (mode.equals("3D俯视模式(已打开)")) {
								UiSettings mUiSettings = myBaiduMap.getUiSettings();
								mUiSettings.setCompassEnabled(true);
								LocationModeString[which] = "3D俯视模式(已关闭)";
								toast("3D模式已关闭");
							} else {
								MyLocationConfiguration config = new MyLocationConfiguration(
										com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.COMPASS, true,
										myBitmapLocation);
								myBaiduMap.setMyLocationConfigeration(config);
								MyLocationConfiguration config2 = new MyLocationConfiguration(
										com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, true,
										myBitmapLocation);
								myBaiduMap.setMyLocationConfigeration(config2);
								LocationModeString[which] = "3D俯视模式(已打开)";
								toast("3D模式已打开");
							}
						}
					}
				}).show();
	}

	/**
	 * 
	 * 添加覆盖物
	 */
	private void addMapMarks() {
		initMarksData();
		myBaiduMap.clear();// 先清除一下图层
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		myMarks = BitmapDescriptorFactory.fromResource(R.drawable.mark);// 引入自定义的覆盖物图标，将其转化成一个BitmapDescriptor对象
		// 遍历MarkInfo的List一个MarkInfo就是一个Mark
		for (int i = 0; i < markInfoList.size(); i++) {
			// 经纬度对象
			latLng = new LatLng(markInfoList.get(i).getLatitude(), markInfoList.get(i).getLongitude());// 需要创建一个经纬对象，通过该对象就可以定位到处于地图上的某个具体点
			// 图标
			options = new MarkerOptions().position(latLng).icon(myMarks).zIndex(6);
			marker = (Marker) myBaiduMap.addOverlay(options);// 将覆盖物添加到地图上
			Bundle bundle = new Bundle();// 创建一个Bundle对象将每个mark具体信息传过去，当点击该覆盖物图标的时候就会显示该覆盖物的详细信息
			bundle.putSerializable("mark", markInfoList.get(i));
			marker.setExtraInfo(bundle);
		}
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);// 通过这个经纬度对象，地图就可以定位到该点
		myBaiduMap.animateMapStatus(msu);
	}

	/**
	 * 
	 * 初始化覆盖物信息数据
	 */
	private void initMarksData() {
		markInfoList = new ArrayList<MarkInfo>();
		markInfoList.add(new MarkInfo(32.079254, 118.787623, R.drawable.pic1, "英伦贵族小旅馆", "距离209米", 1888));
		markInfoList.add(new MarkInfo(32.064355, 118.787624, R.drawable.pic2, "沙井国际高级会所", "距离459米", 388));
		markInfoList.add(new MarkInfo(28.7487420000, 115.8748860000, R.drawable.pic4, "华东交通大学南区", "距离5米", 888));
		markInfoList.add(new MarkInfo(28.7534890000, 115.8767960000, R.drawable.pic3, "华东交通大学北区", "距离10米", 188));
		myBaiduMap.setOnMarkerClickListener(this);
		myBaiduMap.setOnMapClickListener(this);
	}

	/**
	 * 
	 * 放大地图的比例
	 */
	private void narrowMapScale() {
		current -= 0.5f;
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f + current);
		myBaiduMap.animateMapStatus(msu);
	}

	/**
	 *
	 * 缩小地图的比例
	 */
	private void expandMapScale() {
		current += 0.5f;
		MapStatusUpdate msu2 = MapStatusUpdateFactory.zoomTo(15.0f + current);
		myBaiduMap.animateMapStatus(msu2);
	}

	/**
	 * 
	 * 是否打开实时交通
	 */
	private void switchRoadCondition() {
		if (myBaiduMap.isTrafficEnabled()) {// 如果是开着的状态，当点击后，就会出关闭状态
			myBaiduMap.setTrafficEnabled(false);
			mapRoad.setImageResource(R.drawable.main_icon_roadcondition_off);
		} else {// 如果是的关闭的状态，当点击后，就会处于开启的状态
			myBaiduMap.setTrafficEnabled(true);
			mapRoad.setImageResource(R.drawable.main_icon_roadcondition_on);
		}
	}

	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			MyLocationData data = new MyLocationData.Builder().accuracy(location.getRadius())// 精度半径
					.direction(myCurrentX)// 方向
					.latitude(location.getLatitude())// 经度
					.longitude(location.getLongitude())// 纬度
					.build();
			myBaiduMap.setMyLocationData(data);
			// 配置自定义的定位图标,需要在紧接着setMyLocationData后面设置
			// 调用自定义定位图标
			changeLocationIcon();
			latitude = location.getLatitude();// 得到当前的经度
			longtitude = location.getLongitude();// 得到当前的纬度
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			Const.mLastLocationData = latLng;
			if (isFirstLoc) {
				isFirstLoc = false;
				getMyLatestLocation(location.getLatitude(), location.getLongitude());
				mylocation_txt.setText(location.getAddrStr());
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 开启定位图层
		myBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted()) {

			mLocationClient.start();
		}
		// 开启方向传感器
		myOrientationListener.start();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 开启定位图层
		myBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		myOrientationListener.stop();
	}

	/**
	 * 
	 * 覆盖物的点击事件
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		Bundle bundle = marker.getExtraInfo();
		MarkInfo MyMarker = (MarkInfo) bundle.getSerializable("mark");
		ImageView iv = (ImageView) markLayout.findViewById(R.id.mark_image);
		TextView distanceTv = (TextView) markLayout.findViewById(R.id.distance);
		TextView nameTv = (TextView) markLayout.findViewById(R.id.name);
		TextView zanNumsTv = (TextView) markLayout.findViewById(R.id.zan_nums);
		iv.setImageResource(MyMarker.getImageId());
		distanceTv.setText(MyMarker.getDistance() + "");
		nameTv.setText(MyMarker.getName());
		zanNumsTv.setText(MyMarker.getZanNum() + "");
		// 初始化一个InfoWindow
		initInfoWindow(MyMarker, marker);
		markLayout.setVisibility(View.VISIBLE);
		return true;
	}

	/**
	 *
	 * 初始化出一个InfoWindow
	 * 
	 */
	private void initInfoWindow(MarkInfo MyMarker, Marker marker) {
		// TODO Auto-generated method stub
		InfoWindow infoWindow;
		// InfoWindow中显示的View内容样式，显示一个TextView
		TextView infoWindowTv = new TextView(BasemapActivity.this);
		infoWindowTv.setBackgroundResource(R.drawable.location_tips);
		infoWindowTv.setPadding(30, 20, 30, 50);
		infoWindowTv.setText(MyMarker.getName());
		infoWindowTv.setTextColor(Color.parseColor("#FFFFFF"));

		final LatLng latLng = marker.getPosition();
		Point p = myBaiduMap.getProjection().toScreenLocation(latLng);// 将地图上的经纬度转换成屏幕中实际的点
		p.y -= 47;// 设置屏幕中点的Y轴坐标的偏移量
		LatLng ll = myBaiduMap.getProjection().fromScreenLocation(p);// 把修改后的屏幕的点有转换成地图上的经纬度对象
		/**
		 * 
		 * 实例化一个InfoWindow的对象 public InfoWindow(View view,LatLng position, int
		 * yOffset)通过传入的 view 构造一个 InfoWindow,
		 * 此时只是利用该view生成一个Bitmap绘制在地图中，监听事件由开发者实现。 参数: view - InfoWindow 展示的
		 * view position - InfoWindow 显示的地理位置 yOffset - InfoWindow Y 轴偏移量
		 */
		infoWindow = new InfoWindow(infoWindowTv, ll, 10);
		myBaiduMap.showInfoWindow(infoWindow);// 显示InfoWindow
	}

	@Override
	public void onMapClick(LatLng arg0) {// 表示点击地图其他的地方使得覆盖物的详情介绍的布局隐藏，但是点击已显示的覆盖物详情布局上，则不会消失，因为在详情布局上添加了Clickable=true
		// 由于事件的传播机制，因为点击事件首先会在覆盖物布局的父布局(map)中,由于map是可以点击的，map则会把点击事件给消费掉，如果加上Clickable=true表示点击事件由详情布局自己处理，不由map来消费
		markLayout.setVisibility(View.GONE);
		myBaiduMap.hideInfoWindow();// 隐藏InfoWindow
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

	public void toast(String str) {
		Toast.makeText(BasemapActivity.this, str, 0).show();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		Const.startString = arg0.getSematicDescription();
		mystart_txt.setText(Const.startString + "");
		Util.toast("起点：" + arg0.getAddress(), BasemapActivity.this);
	}
}
