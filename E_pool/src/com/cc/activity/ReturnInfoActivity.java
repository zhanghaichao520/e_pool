package com.cc.activity;

import java.util.List;

import com.baidu.mapapi.utils.DistanceUtil;
import com.cc.R;
import com.cc.data.Const;
import com.cc.data.Order;
import com.cc.data.User;
import com.cc.util.Util;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.http.bean.Init;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ReturnInfoActivity extends Activity implements OnClickListener {
	private TextView startText;
	private TextView endText;
	private TextView distanceText;
	private TextView diverenameText;
	private TextView diverphoneText;
	private TextView carnumText;
	private TextView priceText;
	private Button retButton;
	User myUser = BmobUser.getCurrentUser(User.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_returninfo);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		startText = (TextView) findViewById(R.id.r_start_txt);
		endText = (TextView) findViewById(R.id.r_end_txt);
		distanceText = (TextView) findViewById(R.id.r_distance_txt);
		diverenameText = (TextView) findViewById(R.id.divername_txt);
		diverphoneText = (TextView) findViewById(R.id.diverphone_txt);
		carnumText = (TextView) findViewById(R.id.carnum_txt);
		priceText = (TextView) findViewById(R.id.price_txt);
		retButton = (Button) findViewById(R.id.r_back);
		retButton.setOnClickListener(this);

		startText.setText(Const.startString);
		endText.setText(Const.endString);
		distanceText.setText("大约" + (int) Const.distance + "米");
		findCarNum();
		delOrder();
	}

	private void delOrder() {
		// TODO Auto-generated method stub
		String orderId;
		Bundle bundle = this.getIntent().getExtras();
		orderId = bundle.getString("orderId");
		BmobQuery<Order> query = new BmobQuery<Order>();
		query.getObject(orderId, new QueryListener<Order>() {

			@Override
			public void done(Order arg0, BmobException arg1) {
				// TODO Auto-generated method stub
				if (arg1 == null) {
					priceText.setText(""+arg0.getPrice());
				} else {
					Util.toast("failed：" + arg1.getMessage(),ReturnInfoActivity.this);
				}
			}
		});
		Order order = new Order();
		order.setIsFinish(true);
		order.update(orderId, new UpdateListener() {

			@Override
			public void done(BmobException arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
					Util.toast("订单成功完成", ReturnInfoActivity.this);
				} else {
					Util.toast("failed:" + arg0.getMessage(), ReturnInfoActivity.this);
				}
			}
		});
	}

	private void findCarNum() {
		new AsyncTask<String, Void, String>() {
			String carnum;

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				BmobQuery<Order> query = new BmobQuery<Order>();
				query.addWhereEqualTo("name", arg0[0]);
				query.findObjects(new FindListener<Order>() {
					@Override
					public void done(List<Order> arg0, BmobException arg1) {
						// TODO Auto-generated method stub
						if (arg1 == null) {
							carnum = arg0.get(0).getCarNum().toString();
							carnumText.setText("" + carnum);
							BmobQuery<User> query = new BmobQuery<User>();
							query.addWhereEqualTo("carNum", carnum);
							query.findObjects(new FindListener<User>() {

								@Override
								public void done(List<User> arg0, BmobException arg1) {
									// TODO Auto-generated method stub
									if (arg1 == null) {
										diverenameText.setText(arg0.get(0).getName().toString());
										diverphoneText.setText(arg0.get(0).getMobilePhoneNumber().toString());
									} else {
										Util.toast("failed1:" + arg1.getMessage(), ReturnInfoActivity.this);
									}
								}
							});
						} else {
							Util.toast("failed2" + arg1.getMessage(), ReturnInfoActivity.this);
						}
					}
				});
				return carnum;
			}

		}.execute(myUser.getName());

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.r_back:
			Util.startActivity(ReturnInfoActivity.this, InfoActivity.class);
			break;

		default:
			break;
		}
	}
}
