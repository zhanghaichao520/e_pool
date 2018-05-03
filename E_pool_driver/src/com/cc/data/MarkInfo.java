package com.cc.data;

import java.io.Serializable;

public class MarkInfo implements Serializable{
	private double latitude;
	private double longitude;
	private int ImageId;
	private String name;
	private String distance;
	private int zanNum;
	public MarkInfo(double latitude, double longitude, int imageId,
			String name, String distance, int zanNum) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		ImageId = imageId;
		this.name = name;
		this.distance = distance;
		this.zanNum = zanNum;
	}
	public MarkInfo() {
		super();
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getImageId() {
		return ImageId;
	}
	public void setImageId(int imageId) {
		ImageId = imageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public int getZanNum() {
		return zanNum;
	}
	public void setZanNum(int zanNum) {
		this.zanNum = zanNum;
	}

}
