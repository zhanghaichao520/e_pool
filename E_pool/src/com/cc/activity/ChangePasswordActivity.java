package com.cc.activity;

import com.baidu.lbsapi.auth.e;
import com.cc.R;
import com.cc.list.LeftFragment;
import com.cc.util.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePasswordActivity extends SlidingFragmentActivity implements OnClickListener {

	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	private EditText oldpwd_edt;
	private EditText newpwd_edt;
	private EditText confirmpwd_edt;
	private Button submit_btn;
	private String oldpwd;
	private String newpwd;
	private String confirmpwd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 鏃犳爣棰?
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changepassword);
		initSlidingMenu(savedInstanceState);
		initevent();

	}

	public void initevent() {
		// TODO Auto-generated method stub
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		topTextView.setText("修改密码");
		oldpwd_edt = (EditText) findViewById(R.id.oldPwdEdit);
		newpwd_edt = (EditText) findViewById(R.id.newPwdEdit);
		confirmpwd_edt = (EditText) findViewById(R.id.newsurePwdEdit);
		submit_btn = (Button) findViewById(R.id.save);
		submit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				oldpwd = oldpwd_edt.getText().toString().trim();
				newpwd = newpwd_edt.getText().toString().trim();
				confirmpwd = confirmpwd_edt.getText().toString().trim();
				if (newpwd.equals(confirmpwd)) {
					BmobUser.updateCurrentUserPassword(oldpwd, newpwd, new UpdateListener() {
						@Override
						public void done(BmobException arg0) {
							// TODO Auto-generated method stub
							  if(arg0==null){
						            Util.toast("修改密码成功", ChangePasswordActivity.this);
						            Util.startActivity(ChangePasswordActivity.this, LoginActivity.class);
						        }else{
						            Util.toast("failed："+arg0.getMessage(), ChangePasswordActivity.this);
						        }
						}
					});
				}else {
					Util.toast("两次输入的新密码不匹配", ChangePasswordActivity.this);
				}

			}
		});
	}

	private void initSlidingMenu(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}

		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftFragment()).commit();

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(null);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
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
		case R.id.topButton:
			toggle();
			break;
		default:
			break;
		}
	}

}
