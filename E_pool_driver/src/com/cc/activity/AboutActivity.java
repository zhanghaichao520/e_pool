package com.cc.activity;

import com.cc.R;
import com.cc.data.Const;
import com.cc.list.LeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends SlidingFragmentActivity implements OnClickListener {
	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	private Fragment newContent = null;
	private String title = null;
	private Button use_btn;
	private Button team_btn;
	private Button declare_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 閺冪姵鐖ｆ０锟�
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initSlidingMenu(savedInstanceState);
		initevent();

	}

	public void initevent() {
		// TODO Auto-generated method stub
		topButton = (ImageView) findViewById(R.id.topButton);
		topTextView = (TextView) findViewById(R.id.topTv);
		use_btn = (Button) findViewById(R.id.button_use);
		team_btn = (Button) findViewById(R.id.button_team);
		declare_btn = (Button) findViewById(R.id.button_declare);
		topTextView.setText("关于");
		topButton.setOnClickListener(this);
		use_btn.setOnClickListener(this);
		team_btn.setOnClickListener(this);
		declare_btn.setOnClickListener(this);
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
		case R.id.button_use:
			diaLog("使用手册", Const.use);
			break;
		case R.id.button_team:
			diaLog("团队介绍", Const.team);
			break;
		case R.id.button_declare:
			diaLog("免责申明", Const.declare);
			break;
		default:
			break;
		}
	}

	private void diaLog(String title,String text) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(AboutActivity.this)  
        .setTitle(title)
        .setMessage(text)
        .setPositiveButton("确定", null)
        .show();
	}
}
