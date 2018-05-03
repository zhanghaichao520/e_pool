package com.cc.algorithm;

import java.util.List;
import java.util.Map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.cc.data.Const;
import com.cc.data.Order;

public class Compute {
	private List<Order> res = null;
	private LatLng passengerStart;
	private double currentdis = 9999999.0;
	private int currentOrder;
	private int count = 2;
	public Compute(List<Order> list) {
		// TODO Auto-generated constructor stub
		Order order = new Order();
		double dis;
		while (count!=0) {
			for (int i = 0; i < list.size(); i++) {
				order = list.get(i);
				passengerStart = new LatLng(order.getStartLat(), order.getStartLng());
				dis = DistanceUtil.getDistance(passengerStart, Const.mStartLocationData);
				if (dis<currentdis) {
					currentdis = dis;
					currentOrder = i;
				}
			}
			res.add(list.get(currentOrder));
			list.remove(currentOrder);
			count--;
		}
	}
	public List<Order> getResult() {
		return res;
	}
}
