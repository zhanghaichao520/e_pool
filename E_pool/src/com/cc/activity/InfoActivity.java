package com.cc.activity;

import java.util.List;

import com.baidu.mapapi.utils.DistanceUtil;
import com.cc.R;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class InfoActivity extends Activity implements OnClickListener {
	private TextView startText;
	private TextView endText;
	private TextView distanceText;
	private EditText countEditText;
	private EditText waittimeEditText;
	private EditText tipsEditText;
	private Button subButton;
	private Button retButton;
	private Button clearButton;
	private CheckBox ispool;
	User myUser = BmobUser.getCurrentUser(User.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custmer);
		init();
		initevent();
	} 

	public void init() {
		// TODO Auto-generated method stub
		startText = (TextView) findViewById(R.id.start_txt);
		endText = (TextView) findViewById(R.id.end_txt);
		distanceText = (TextView) findViewById(R.id.distance_txt);
		waittimeEditText = (EditText) findViewById(R.id.waittime_edt);
		countEditText = (EditText) findViewById(R.id.count_edt);
		tipsEditText = (EditText) findViewById(R.id.tips_edt);
		subButton = (Button) findViewById(R.id.submit);
		retButton = (Button) findViewById(R.id.back);
		clearButton = (Button) findViewById(R.id.clear);
		ispool = (CheckBox) findViewById(R.id.ispool);
	}

	public void initevent() {
		// TODO Auto-generated method stub
		Const.distance = DistanceUtil.getDistance(Const.mStartLocationData, Const.mEndLocationData);
		startText.setText(Const.startString);
		endText.setText(Const.endString);
		distanceText.setText((int) Const.distance + "米");
		retButton.setOnClickListener(this);
		subButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.back:
			Util.startActivity(InfoActivity.this, PoiSearchActivity.class);
			break;
		case R.id.submit:
			submit();
			break;
		case R.id.clear:
			clear();
			break;
		default:
			break;
		}
	}

	private void clear() {
		// TODO Auto-generated method stub
		countEditText.setText("");
		waittimeEditText.setText("");
		tipsEditText.setText("");
	}

	private void submit() {
		// TODO Auto-generated method stub
		if (countEditText.getText().toString().equals("")) {
			Util.toast("请输入乘车人数", InfoActivity.this);
			return;
		}else if(waittimeEditText.getText().toString().equals("")){
			Util.toast("请输入等待时间", InfoActivity.this);
			return;
		}
		if (Integer.parseInt(countEditText.getText().toString())>5) {
			Util.toast("乘车人数最多四人", InfoActivity.this);
			return;
		}
		Order order = new Order();
		order.setStartLat(Const.mStartLocationData.latitude);
		order.setStartLng(Const.mStartLocationData.longitude);
		order.setEndLat(Const.mEndLocationData.latitude);
		order.setEndLng(Const.mEndLocationData.longitude);
		order.setWaittime(waittimeEditText.getText().toString().trim());
		order.setTips(tipsEditText.getText().toString().trim());
		order.setCount(countEditText.getText().toString().trim());
		order.setDistance(Const.distance);
		order.setCarNum("00000");
		order.setIspool(ispool.isChecked());
		order.setName(myUser.getName());
		order.setStart(Const.startString);
		order.setEnd(Const.endString);
		order.setIsFinish(false);
		order.setPrice("0");
		order.setPhone(myUser.getMobilePhoneNumber());
		order.save(new SaveListener<String>() {

			@Override
			public void done(String arg0, BmobException arg1) {
				// TODO Auto-generated method stub
				if (arg1 == null) {
					Util.toast("正在为您匹配车辆..", InfoActivity.this);
				} else {
					Util.toast("failed：" + arg1.getMessage(), InfoActivity.this);
				}
			}
		});
		final ProgressDialog progressDialog;
		progressDialog = ProgressDialog.show(InfoActivity.this, // context
				"拼车中", // title
				"请稍候");
		new AsyncTask<String, Void, String>() {
			boolean flag =true;
			String orderId = "";
			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				while (flag) {
			    	BmobQuery<Order> query = new BmobQuery<Order>();
			    	query.addWhereEqualTo("name", arg0[0]);
			    	
					query.findObjects(new FindListener<Order>() {
						
						@Override
						public void done(List<Order> arg0, BmobException arg1) {
							// TODO Auto-generated method stub
							if (arg1 == null) {
								for (int i = 0; i < arg0.size(); i++) {
									if ((!arg0.get(i).getCarNum().toString().equals("00000"))&&arg0.get(i).getIsFinish()==false) {
										progressDialog.dismiss();
										Util.toast("已为您找到合适的车辆", InfoActivity.this);
										flag = false;
										orderId = arg0.get(0).getObjectId();
										break;
									}
								}
							}
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return orderId;
			}
			protected void onPostExecute(String result) {
				Bundle bundle = new Bundle();
				bundle.putString("orderId", result);
				Util.startActivity(InfoActivity.this, ReturnInfoActivity.class,bundle);
			};
		}.execute(myUser.getName());

	}
}