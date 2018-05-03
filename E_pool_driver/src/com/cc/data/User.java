package com.cc.data;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser{

	private String name;
	private int identity;
	private String carNum;
	public User() {
		// TODO Auto-generated constructor stub
		identity = 2;
	}
	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
