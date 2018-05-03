package com.cc.activity;

import com.cc.R;
import com.cc.data.User;
import com.cc.list.LeftFragment;
import com.cc.util.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PersonalActivity extends SlidingFragmentActivity implements OnClickListener {

	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	private TextView nickname_txt;
	private TextView name_txt;
	private TextView phone_txt;
	private TextView email_txt;
	private TextView nameSet_btn;
	private TextView phoneSet_btn;
	private TextView emailSet_btn;
	User myUser = BmobUser.getCurrentUser(User.class);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 鏃犳爣棰?
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		initSlidingMenu(savedInstanceState);
		initevent();

	}

	public void initevent() {
		// TODO Auto-generated method stub
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		topTextView.setText("个人中心");
		nickname_txt = (TextView) findViewById(R.id.setNickname);
		name_txt = (TextView) findViewById(R.id.setName);
		phone_txt = (TextView) findViewById(R.id.setPhone);
		email_txt = (TextView) findViewById(R.id.setEmail);
		nameSet_btn = (TextView) findViewById(R.id.nameSet_button);
		phoneSet_btn = (TextView) findViewById(R.id.phoneSet_button);
		emailSet_btn = (TextView) findViewById(R.id.emailSet_button);
		nameSet_btn.setOnClickListener(this);
		phoneSet_btn.setOnClickListener(this);
		emailSet_btn.setOnClickListener(this);
		nickname_txt.setText(myUser.getName());
		name_txt.setText(myUser.getUsername());
		phone_txt.setText(myUser.getMobilePhoneNumber());
		email_txt.setText(myUser.getEmail());
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
		case R.id.nameSet_button:
			updataName();
			break;
		case R.id.phoneSet_button:
			updataPhone();
			break;
		case R.id.emailSet_button:
			updataEmail();
			break;
		default:
			break;
		}
	}

	private void updataEmail() {
		// TODO Auto-generated method stub
		final EditText inputServer = new EditText(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更改邮箱").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("取消",
				null);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				BmobUser newUser = new BmobUser();
				newUser.setEmail(inputServer.getText().toString().trim());
				newUser.update(myUser.getObjectId(), new UpdateListener() {

					@Override
					public void done(BmobException arg0) {
						// TODO Auto-generated method stub
						if (arg0==null) {
							Util.toast("修改成功", PersonalActivity.this);
							email_txt.setText(inputServer.getText().toString().trim());
						}else{
							Util.toast("failed:"+arg0.getMessage(), PersonalActivity.this);
						}
					}
				});
			}
		});
		builder.show();
	}

	private void updataPhone() {
		// TODO Auto-generated method stub
		final EditText inputServer = new EditText(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更改手机").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("取消",
				null);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				BmobUser newUser = new BmobUser();
				newUser.setMobilePhoneNumber(inputServer.getText().toString().trim());
				newUser.update(myUser.getObjectId(), new UpdateListener() {

					@Override
					public void done(BmobException arg0) {
						// TODO Auto-generated method stub
						if (arg0==null) {
							Util.toast("修改成功", PersonalActivity.this);
							phone_txt.setText(inputServer.getText().toString().trim());
						}else{
							Util.toast("failed:"+arg0.getMessage(), PersonalActivity.this);
						}
					}
				});
			}
		});
		builder.show();
	}

	private void updataName() {
		// TODO Auto-generated method stub
		final EditText inputServer = new EditText(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更改昵称").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("取消",
				null);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				BmobUser newUser = new BmobUser();
				newUser.setUsername(inputServer.getText().toString().trim());
				newUser.update(myUser.getObjectId(), new UpdateListener() {

					@Override
					public void done(BmobException arg0) {
						// TODO Auto-generated method stub
						if (arg0==null) {
							Util.toast("修改成功", PersonalActivity.this);
							name_txt.setText(inputServer.getText().toString().trim());
						}else{
							Util.toast("failed:"+arg0.getMessage(), PersonalActivity.this);
						}
					}
				});
			}
		});
		builder.show();
	}

}
