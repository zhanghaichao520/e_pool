package com.cc.data;

import cn.bmob.v3.BmobObject;

public class Order extends BmobObject{
	Double startLat;
	Double startLng;
	Double endLat;
	Double endLng;
	Double distance;
	String count;
	String waittime;
	String tips;
	Boolean ispool;
	String carNum;
	String name;
	String start;
	String end;
	String phone;
	Boolean isFinish;
	String price;
	
	public Boolean getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(Boolean isFinish) {
		this.isFinish = isFinish;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public Double getStartLat() {
		return startLat;
	}
	public void setStartLat(Double startLat) {
		this.startLat = startLat;
	}
	public Double getStartLng() {
		return startLng;
	}
	public void setStartLng(Double startLng) {
		this.startLng = startLng;
	}
	public Double getEndLat() {
		return endLat;
	}
	public void setEndLat(Double endLat) {
		this.endLat = endLat;
	}
	public Double getEndLng() {
		return endLng;
	}
	public void setEndLng(Double endLng) {
		this.endLng = endLng;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getWaittime() {
		return waittime;
	}
	public void setWaittime(String waittime) {
		this.waittime = waittime;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public Boolean getIspool() {
		return ispool;
	}
	public void setIspool(Boolean ispool) {
		this.ispool = ispool;
	}
	
}
