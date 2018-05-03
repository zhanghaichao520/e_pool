package com.cc.activity;

import com.cc.R;
import com.cc.list.LeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class EpoolActivity extends SlidingFragmentActivity implements
		OnClickListener {

	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 閺冪姵鐖ｆ０锟�
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poisearch);
		initSlidingMenu(savedInstanceState);
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		topTextView.setText("终点输入");
	}

	private void initSlidingMenu(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

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
