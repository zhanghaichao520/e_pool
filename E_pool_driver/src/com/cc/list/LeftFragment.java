package com.cc.list;

import com.cc.R;
import com.cc.activity.LoginActivity;
import com.cc.activity.PersonalActivity;
import com.cc.activity.PoiSearchActivity;
import com.cc.activity.ReturnInfoActivity;
import com.cc.data.Const;
import com.cc.data.User;
import com.cc.activity.AboutActivity;
import com.cc.activity.BasemapActivity;
import com.cc.activity.ChangePasswordActivity;
import com.cc.activity.FeedbackActivity;
import com.cc.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import android.view.ViewGroup;

public class LeftFragment extends Fragment implements OnClickListener{
	private View baseMapView;
	private View ePoolView;
	private View changepwdView;
	private View settingsView;
	private View personalView;
	private View aboutView;
	private View reloginView;
	private View exitView;
	private TextView name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);

		return view;
	}


	public void findViews(View view) {
		baseMapView = view.findViewById(R.id.tvBasemap);
		ePoolView = view.findViewById(R.id.tvEpool);
		settingsView = view.findViewById(R.id.tvMySettings); //反馈
		changepwdView = view.findViewById(R.id.tvChangepassword);
		personalView = view.findViewById(R.id.tvperson);
		aboutView = view.findViewById(R.id.tvabout);
		reloginView = view.findViewById(R.id.relogin);
		exitView = view.findViewById(R.id.exit);
		name =(TextView) view.findViewById(R.id.name);
		User bmobUser = BmobUser.getCurrentUser(User.class);
		if(bmobUser != null){
			// 允许用户使用应用
			name.setText(bmobUser.getName());
		}else{
			//缓存用户对象为空时， 可打开用户注册界面…
			name.setText("未登录");
		}
		baseMapView.setOnClickListener(this);
		ePoolView.setOnClickListener(this);
		changepwdView.setOnClickListener(this);
		settingsView.setOnClickListener(this);
		reloginView.setOnClickListener(this);
		personalView.setOnClickListener(this);
		aboutView.setOnClickListener(this);
		exitView.setOnClickListener(this);
		name.setOnClickListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	public void nameOnclick(){
		if (name.getText().toString().equals("未登录")) {
			Util.startActivity(getActivity(), LoginActivity.class);
		}
	}
	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		Intent intent ;
		switch (v.getId()) {
		case R.id.tvBasemap: // 基础地图
			Util.startActivity(getActivity(), BasemapActivity.class);
			break;
		case R.id.tvEpool:// 接单
			if (Const.isSetStart) {
				Util.startActivity(getActivity(), ReturnInfoActivity.class);
			}else {
				Util.toast("请先设置您想匹配到的乘客位置", getActivity());
			}
			break;
		case R.id.tvperson: // 个人中心
			if (name.getText().toString().equals("未登录")) {
				Util.toast("请先登录", getActivity());
				Util.startActivity(getActivity(), LoginActivity.class);
			}else {
				Util.startActivity(getActivity(), PersonalActivity.class);
			}
			break;
		case R.id.tvChangepassword:
			if (name.getText().toString().equals("未登录")) {
				Util.toast("请先登录", getActivity());
				Util.startActivity(getActivity(), LoginActivity.class);
			}else {
				Util.startActivity(getActivity(), ChangePasswordActivity.class);
			}
			break;
		case R.id.tvMySettings: // 反馈
			Util.startActivity(getActivity(), FeedbackActivity.class);
			break;
		case R.id.tvabout: 
			Util.startActivity(getActivity(), AboutActivity.class);
			break;
		case R.id.relogin:
			BmobUser.logOut();
			intent = new Intent();
			intent.setClass(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			((Activity) getActivity()).finish();
			break;
		case R.id.exit:
			((Activity) getActivity()).finish();
			break;	
		case R.id.name:
			nameOnclick();
		default:
			break;
		}
	}


}
