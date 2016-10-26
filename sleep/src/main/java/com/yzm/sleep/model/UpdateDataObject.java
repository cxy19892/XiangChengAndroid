package com.yzm.sleep.model;

public class UpdateDataObject {

	private String my_int_id;
	private String date_of_data;	//按照yyyymmdd的格式
	private String sleep_point;		//按照yyyymmddhhmm的格式
	private String wakeup_point;	//按照yyyymmddhhmm的格式
	private String user_location_x;
	private String user_location_y;
	private String sleep_duration;	//以小时为单位显示小数，如7.67（小时）
	private String my_int_occupation;	//非空，值为2000000xx
	public String getMy_int_id() {
		return my_int_id;
	}
	public void setMy_int_id(String my_int_id) {
		this.my_int_id = my_int_id;
	}
	public String getDate_of_data() {
		return date_of_data;
	}
	public void setDate_of_data(String date_of_data) {
		this.date_of_data = date_of_data;
	}
	public String getSleep_point() {
		return sleep_point;
	}
	public void setSleep_point(String sleep_point) {
		this.sleep_point = sleep_point;
	}
	public String getWakeup_point() {
		return wakeup_point;
	}
	public void setWakeup_point(String wakeup_point) {
		this.wakeup_point = wakeup_point;
	}
	public String getUser_location_x() {
		return user_location_x;
	}
	public void setUser_location_x(String user_location_x) {
		this.user_location_x = user_location_x;
	}
	public String getUser_location_y() {
		return user_location_y;
	}
	public void setUser_location_y(String user_location_y) {
		this.user_location_y = user_location_y;
	}
	public String getSleep_duration() {
		return sleep_duration;
	}
	public void setSleep_duration(String sleep_duration) {
		this.sleep_duration = sleep_duration;
	}
	public String getMy_int_occupation() {
		return my_int_occupation;
	}
	public void setMy_int_occupation(String my_int_occupation) {
		this.my_int_occupation = my_int_occupation;
	}
	
	
}
