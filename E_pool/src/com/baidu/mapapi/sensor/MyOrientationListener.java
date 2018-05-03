package com.baidu.mapapi.sensor;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
public class MyOrientationListener implements SensorEventListener{
	private SensorManager mySensorManager;
	private Sensor mySensor;
	private Context myContext; 
	private float lastX;
	private onOrientationListener myOrientationListener;
	public void start(){//开启方向传感器
		//先通过系统服务来得到传感器管理对象mySensorManager
		mySensorManager=(SensorManager) myContext.getSystemService(Context.SENSOR_SERVICE);
		if (mySensorManager!=null) {//如果传感器管理对象不为空，则可以通过传感器管理对象来获得方向传感器对象
			//获得方向传感器对象
			mySensor=mySensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		if (mySensor!=null) {//如果方向传感器不为空，则给该方向传感器注册监听事件
			mySensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_UI);
		}
	}
	public void stop(){//解除注册方向传感器监听事件
		mySensorManager.unregisterListener(this);
	}
	public MyOrientationListener(Context myContext) {//方向传感器的一个构造器
		super();
		this.myContext = myContext;
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
	//监听方向发生变化
	@Override
	public void onSensorChanged(SensorEvent event) {//精度发生改变的时候
		if (event.sensor.getType()==Sensor.TYPE_ORIENTATION) {//如果是方向传感器
			float x=event.values[SensorManager.DATA_X];//获得传感器的X轴的坐标,可以返回3个值，即X轴的坐标，Y轴坐标，Z轴坐标，我们只需要X轴坐标
			if (Math.abs(x-lastX)>1.0) {//对比本次的X坐标的变化比上一次的变化差大于1.0就说明方向发生改变
				if (myOrientationListener!=null) {//说明主界面已经注册了事件，即不为空，然后产生一个回调将实时变化X轴的坐标传入
					//通过一个回调方法，通知主界面去更新UI
					myOrientationListener.onOrientationChanged(lastX);//就需要把上一次的X轴坐标传入，在MainActivity中的回调方法中去获取即可
				}
			}
			lastX=x;
		}
	}

	public void setMyOrientationListener(onOrientationListener myOrientationListener) {
		this.myOrientationListener = myOrientationListener;
	}
	//写一个接口实现方向改变的监听产生的回调
	public interface onOrientationListener{
		void onOrientationChanged(float x);//回调的方法
	}
}
