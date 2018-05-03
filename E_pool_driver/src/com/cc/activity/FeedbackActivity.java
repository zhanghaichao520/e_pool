package com.cc.activity;

import com.cc.R;
import com.cc.data.Feedback;
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
import cn.bmob.v3.Bmob;
import cn.bmob.v3.b.thing;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedbackActivity extends SlidingFragmentActivity implements OnClickListener {
	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	private Fragment newContent = null;
	private String title = null;
	private EditText text_edt;
	private EditText contact_edt;
	private Button send_btn;
	private Button clear_btn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 鏃犳爣棰�
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initSlidingMenu(savedInstanceState);
		initevent();

	}


	public void initevent() {
		// TODO Auto-generated method stub
		topButton = (ImageView) findViewById(R.id.topButton);
		topTextView = (TextView) findViewById(R.id.topTv);
		text_edt = (EditText) findViewById(R.id.feedmessage);
		contact_edt =(EditText) findViewById(R.id.contact);
		send_btn = (Button) findViewById(R.id.submit_feed);
		clear_btn = (Button) findViewById(R.id.clear);
		
		topTextView.setText("反馈");
		
		send_btn.setOnClickListener(this);
		topButton.setOnClickListener(this);
		clear_btn.setOnClickListener(this);
	}
	public void submitFeed() {
		Feedback feedback = new Feedback();
		feedback.setId(contact_edt.getText().toString());
		feedback.setText(text_edt.getText().toString());
		if (text_edt.getText().toString().equals("")) {
			Util.toast("反馈内容不能为空", FeedbackActivity.this);
			return;
		}
		feedback.save(new SaveListener<String>() {

			@Override
			public void done(String arg0, BmobException arg1) {
				// TODO Auto-generated method stub
				  if(arg1==null){
						Util.toast("反馈成功", FeedbackActivity.this);
			        }else{
						Util.toast("反馈失败：" + arg1.getMessage(), FeedbackActivity.this);
			        }
			}

		});
	}
	public void clearInput() {
		text_edt.setText("");
		contact_edt.setText("");
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
		case R.id.submit_feed:
			submitFeed();
		case R.id.clear:
			clearInput();
		default:
			break;
		}
	}
}
