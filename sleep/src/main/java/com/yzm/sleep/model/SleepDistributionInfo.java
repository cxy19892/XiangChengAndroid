package com.yzm.sleep.model;

import java.io.Serializable;

public class SleepDistributionInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	/**记录加速度时刻*/
	public String time;
	/**时间在X方向距离值*/
	public float time_value;
	/**加速度平方根号值*/
	public float accelerate_value;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public float getTime_value() {
		return time_value;
	}
	public void setTime_value(float time_value) {
		this.time_value = time_value;
	}
	public float getAccelerate_value() {
		return accelerate_value;
	}
	public void setAccelerate_value(float accelerate_value) {
		this.accelerate_value = accelerate_value;
	}
}
