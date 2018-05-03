package com.cc.activity;

import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.cc.R;
import com.cc.algorithm.Compute;
import com.cc.data.Const;
import com.cc.data.Order;
import com.cc.data.User;
import com.cc.util.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ReturnInfoActivity extends Activity implements OnClickListener {
	private TextView startText1;
	private TextView endText1;
	private TextView distanceText1;
	private TextView passengernameText1;
	private TextView passengerphoneText1;
	private TextView startText2;
	private TextView endText2;
	private TextView distanceText2;
	private TextView passengernameText2;
	private TextView passengerphoneText2;
	private Button retButton;
	private ScrollView group2;
	private TextView custmer2;
	private TextView routeText;
	private TextView priceText;

	User myUser = BmobUser.getCurrentUser(User.class);
	private LatLng passengerStart;
	private LatLng passengerEnd;
	
	private double currentdis = 9999999.0;
	private int currentOrder;
	private int currentNum=0;
	private Order passenger1 = new Order();
	private Order passenger2 = new Order();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_returninfo);
		init();
		readInfo();
		Util.toast("接单成功，自动接单已关闭", ReturnInfoActivity.this);
		Const.switcher = false;
	}

	private void readInfo() {
		// TODO Auto-generated method stub
		final ProgressDialog progressDialog;
		progressDialog = ProgressDialog.show(ReturnInfoActivity.this, // context
				"搜索中", // title
				"请稍后");
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				BmobQuery<Order> query = new BmobQuery<Order>();
				query.addWhereEqualTo("carNum", "00000");
				query.findObjects(new FindListener<Order>() {
					
					@Override
					public void done(List<Order> arg0, BmobException arg1) {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
						List<Order> res;
						double dis;
						currentNum=0;
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
								Util.toast("周围没有乘客，去其他地方转转吧", ReturnInfoActivity.this);
								Util.startActivity(ReturnInfoActivity.this, BasemapActivity.class);
								return;
							}
							passenger1 = arg0.get(currentOrder);
							int temp = currentOrder;
							currentdis = 9999999.0;
							currentNum = Integer.parseInt(passenger1.getCount());
							for (int i = 0; i < arg0.size(); i++) {
								if (temp==i) {
									continue;
								}
								order = arg0.get(i);
								if (currentNum+Integer.parseInt(order.getCount())>5) {
									continue;
								}
								LatLng p1End = new LatLng(passenger1.getEndLat(), passenger1.getEndLng());
								passengerStart = new LatLng(order.getStartLat(), order.getStartLng());
								passengerEnd = new LatLng(order.getEndLat(), order.getEndLng());
								dis = DistanceUtil.getDistance(passengerStart, Const.mStartLocationData);

								double p1ToP2Dis_End = DistanceUtil.getDistance(p1End, passengerStart);
								double p2Dis = DistanceUtil.getDistance(passengerStart, passengerEnd);
								if (dis > 2000.0) {
									continue;
								}
								if (p1ToP2Dis_End>p2Dis) {
									continue;
								}
								if (dis < currentdis) {
									currentdis = dis;
									currentOrder = i;
								}
							}
							if (currentOrder!=temp&&currentdis!=9999999.0) {
								passenger2 = arg0.get(currentOrder);
								if (passenger1.getIspool()&&passenger2.getIspool()) {
									setRoute();
									setPassenger1(passenger1);
									setPassenger2(passenger2);
								}
								if (passenger1.getIspool()==true&&passenger2.getIspool()==false) {
									//价格
									String priceString = "价格：";
									int price;
									price = (int) (passenger1.getDistance()*Const.pricePerKm);
									priceString += price+"元";
									priceText.setText(priceString);
									passenger1.setPrice(price+"");
									setPassenger1(passenger1);
									custmer2.setVisibility(View.INVISIBLE);
									group2.setVisibility(View.INVISIBLE);
									routeText.setVisibility(View.INVISIBLE);
								}
								if (passenger1.getIspool()==false&&passenger2.getIspool()==true) {
									//价格
									String priceString = "价格：";
									int price;
									price = (int) (passenger2.getDistance()*Const.pricePerKm);
									priceString += price+"元";
									priceText.setText(priceString);
									passenger2.setPrice(price+"");
									setPassenger1(passenger2);
									custmer2.setVisibility(View.INVISIBLE);
									group2.setVisibility(View.INVISIBLE);
									routeText.setVisibility(View.INVISIBLE);
								}
							}else {
								//价格
								String priceString = "价格：";
								int price;
								price = (int) (passenger1.getDistance()*Const.pricePerKm);
								priceString += price+"元";
								priceText.setText(priceString);
								passenger1.setPrice(price+"");
								setPassenger1(passenger1);
								custmer2.setVisibility(View.INVISIBLE);
								group2.setVisibility(View.INVISIBLE);
								routeText.setVisibility(View.INVISIBLE);
							}
						} else {
							Util.toast("周围没有乘客，去其他地方转转吧" + arg1.getMessage(), ReturnInfoActivity.this);
							Util.startActivity(ReturnInfoActivity.this, BasemapActivity.class);
						}
					}


					private void setRoute() {
						// TODO Auto-generated method stub
						String route = "路线：";
						String priceString = "价格：";
						int price1;
						int price2;
						route += passenger1.getStart();
						route += "->";
						route += passenger2.getStart();
						route += "->";
						LatLng p1Start = new LatLng(passenger1.getStartLat(), passenger1.getStartLng());
						LatLng p2Start = new LatLng(passenger2.getStartLat(), passenger2.getStartLng());
						LatLng p1End = new LatLng(passenger1.getEndLat(), passenger1.getEndLng());
						LatLng p2End = new LatLng(passenger2.getEndLat(), passenger2.getEndLng());
						price1 = (int) (DistanceUtil.getDistance(p1Start, p2Start)*Const.pricePerKm*0.95);
						if (DistanceUtil.getDistance(p2Start, p1End)<DistanceUtil.getDistance(p2Start, p2End)) {
							route += passenger1.getEnd();
							route += "->";
							route += passenger2.getEnd();
							//价格
							price1 += DistanceUtil.getDistance(p2Start, p1End)*Const.pricePerKm*0.65;
							price2 = (int) (DistanceUtil.getDistance(p2Start, p1End)*Const.pricePerKm*0.65);
							price2 += DistanceUtil.getDistance(p1End, p2End)*Const.pricePerKm*0.95;

						}else {
							route += passenger2.getEnd();
							route += "->";
							route += passenger1.getEnd();
							//价格
							price1 += DistanceUtil.getDistance(p2Start, p1End)*Const.pricePerKm*0.65;
							price2 = (int) (DistanceUtil.getDistance(p2Start, p2End)*Const.pricePerKm*0.65);
							price1 += DistanceUtil.getDistance(p2End, p1End)*Const.pricePerKm*0.95;
						}
						routeText.setText(route);
						priceString +=price1+price2+"元";
						priceText.setText(priceString);
						passenger1.setPrice(price1+"");
						passenger2.setPrice(price2+"");
					}
				});

				return null;
			}

		}.execute();

	}
	private void setPassenger1(Order passenger) {
		startText1.setText(passenger.getStart() + "");
		endText1.setText(passenger.getEnd() + "");
		distanceText1.setText((int) passenger.getDistance().doubleValue() + "米");
		passengernameText1.setText(passenger.getName());
		passengerphoneText1.setText(passenger.getPhone());
		Order order = new Order();
		order.setCarNum(myUser.getCarNum());
		order.setPrice(passenger1.getPrice());
		order.update(passenger.getObjectId(),new UpdateListener() {
			
			@Override
			public void done(BmobException arg0) {
				// TODO Auto-generated method stub
				if (arg0==null) {
					Util.toast("乘客1信息读取成功", ReturnInfoActivity.this);
				}else {
					Util.toast("信息读取失败"+arg0.getMessage(), ReturnInfoActivity.this);

				}
			}
		});
	}
	private void setPassenger2(Order passenger) {
		startText2.setText(passenger.getStart() + "");
		endText2.setText(passenger.getEnd() + "");
		distanceText2.setText((int) passenger.getDistance().doubleValue() + "米");
		passengernameText2.setText(passenger.getName());
		passengerphoneText2.setText(passenger.getPhone());
		Order order = new Order();
		order.setCarNum(myUser.getCarNum());
		order.setPrice(passenger2.getPrice());
		order.update(passenger.getObjectId(),new UpdateListener() {
			
			@Override
			public void done(BmobException arg0) {
				// TODO Auto-generated method stub
				if (arg0==null) {
					Util.toast("乘客2信息读取成功", ReturnInfoActivity.this);
				}else {
					Util.toast("信息读取失败"+arg0.getMessage(), ReturnInfoActivity.this);

				}
			}
		});
	}
	private void init() {
		// TODO Auto-generated method stub
		startText1 = (TextView) findViewById(R.id.r_start_txt);
		endText1 = (TextView) findViewById(R.id.r_end_txt);
		distanceText1 = (TextView) findViewById(R.id.r_distance_txt);
		passengernameText1 = (TextView) findViewById(R.id.divername_txt);
		passengerphoneText1 = (TextView) findViewById(R.id.diverphone_txt);

		startText2 = (TextView) findViewById(R.id.r_start_txt2);
		endText2 = (TextView) findViewById(R.id.r_end_txt2);
		distanceText2 = (TextView) findViewById(R.id.r_distance_txt2);
		passengernameText2 = (TextView) findViewById(R.id.divername_txt2);
		passengerphoneText2 = (TextView) findViewById(R.id.diverphone_txt2);

		retButton = (Button) findViewById(R.id.r_back);
		retButton.setOnClickListener(this);
		
		group2 = (ScrollView) findViewById(R.id.group2);
		custmer2 = (TextView) findViewById(R.id.custmer2);		
		routeText = (TextView) findViewById(R.id.route);
		priceText= (TextView) findViewById(R.id.price);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.r_back:
			Util.startActivity(ReturnInfoActivity.this, BasemapActivity.class);
			break;

		default:
			break;
		}
	}
}
