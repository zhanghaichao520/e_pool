package com.cc.activity;

import com.cc.R;
import com.cc.data.User;
import com.cc.util.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegistActivity extends Activity implements OnClickListener {
	private Button back_btn;
	private Button regist_btn;
	private Button clear_btn;
	private EditText userid_edt;
	private EditText name_edt;
	private EditText password_edt;
	private EditText e_mail_edt;
	private EditText phone_edt;
	private EditText repwd_edt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initevent();
	}

	public void initevent() {
		// TODO Auto-generated method stub
		back_btn = (Button) findViewById(R.id.back);
		regist_btn = (Button) findViewById(R.id.ok);
		clear_btn = (Button) findViewById(R.id.cancel);
		userid_edt = (EditText) findViewById(R.id.input_userid);
		name_edt = (EditText) findViewById(R.id.input_name);
		password_edt = (EditText) findViewById(R.id.input_pwd);
		e_mail_edt = (EditText) findViewById(R.id.input_email);
		phone_edt = (EditText) findViewById(R.id.input_phone);
		repwd_edt = (EditText) findViewById(R.id.input_repwd);
		back_btn.setOnClickListener(this);
		regist_btn.setOnClickListener(this);
		clear_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.back:
			Util.startActivity(RegistActivity.this, LoginActivity.class);
			break;
		case R.id.cancel:
			clearInfo();
			break;
		case R.id.ok:
			registUser();
			break;
		default:
			break;
		}
	}

	public void registUser() {
		// TODO Auto-generated method stub
		if (password_edt.getText().toString().equals(repwd_edt.getText().toString())) {
			final User user = new User();
			final ProgressDialog progressDialog;
			progressDialog = ProgressDialog.show(this, // context
					"注册中", // title
					"请稍后", // message
					true);
			user.setUsername(userid_edt.getText().toString());
			user.setName(name_edt.getText().toString());
			user.setPassword(password_edt.getText().toString());
			user.setEmail(e_mail_edt.getText().toString());
			user.setMobilePhoneNumber(phone_edt.getText().toString());
			user.signUp(new SaveListener<User>() {
				@Override
				public void done(User arg0, BmobException arg1) {
					// TODO Auto-generated method stub
					progressDialog.dismiss();
					if (arg1 == null) {
						Util.toast("注册成功,请去邮箱激活账户", RegistActivity.this);
						Util.startActivity(RegistActivity.this, LoginActivity.class);
					} else {
						Util.toast("failed：" + arg1.getMessage(), RegistActivity.this);
					}
				}
			});
		} else {
			Util.toast("两次密码输入不匹配，请重新输入", RegistActivity.this);
		}

	}

	public void clearInfo() {
		// TODO Auto-generated method stub
		Util.toast("clear success", RegistActivity.this);
		userid_edt.setText("");
		password_edt.setText("");
		repwd_edt.setText("");
		name_edt.setText("");
		e_mail_edt.setText("");
		phone_edt.setText("");
	}
}
