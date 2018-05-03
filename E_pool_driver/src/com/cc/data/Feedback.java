package com.cc.data;

import cn.bmob.v3.BmobObject;

public class Feedback extends BmobObject{
	String Id;
	String Text;

	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	
}
