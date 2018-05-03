package com.cc.activity;

import com.cc.R;
import com.cc.data.User;
import com.cc.util.Util;

import android.R.bool;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity {
	private EditText Id_edt;
	private EditText Password_edt;
	private Button btn_login;
	private Button btn_custom;
	private TextView regist_txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initevent();
	}
	public void initevent() {
		// TODO Auto-generated method stub
		Id_edt = (EditText) findViewById(R.id.input_userid);
		Password_edt = (EditText) findViewById(R.id.input_pwd);
		regist_txt = (TextView) findViewById(R.id.register_link);
		Id_edt.requestFocus();
		btn_custom = (Button) findViewById(R.id.custom);
		btn_custom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BmobUser.logOut();
				Util.startActivity(LoginActivity.this, BasemapActivity.class);
			}
		});
		btn_login = (Button) findViewById(R.id.login);
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ProgressDialog progressDialog;
				progressDialog = ProgressDialog.show(LoginActivity.this, // context
						"µÇÂ¼ÖÐ", // title
						"ÇëÉÔºó");
				final BmobUser bmobUser = new BmobUser();
				bmobUser.setUsername(Id_edt.getText().toString());
				bmobUser.setPassword(Password_edt.getText().toString());
				bmobUser.login(new SaveListener<User>() {

					@Override
					public void done(User arg0, BmobException arg1) {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
						if (arg1==null) {
							if (arg0.getIdentity()==1) {
								if (bmobUser.getEmailVerified()) {
									Util.toast("µÇÂ¼³É¹¦£¬»¶Ó­Äã:"+arg0.getName(), LoginActivity.this);
									Util.startActivity(LoginActivity.this, BasemapActivity.class);
								}else {
									Util.toast("ÇëÈ¥ÓÊÏä¼¤»îÕË»§", LoginActivity.this);
								}
							}else {
								Util.toast("ÇëÏÂÔØË¾»ú¶ËµÇÂ¼", LoginActivity.this);
							}
							
						}else {
							Util.toast("faile:"+arg1.getMessage(), LoginActivity.this);
						}
					}
				});
			}
		});
		regist_txt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Util.startActivity(LoginActivity.this, RegistActivity.class);
			}
		});
	}
	
}
