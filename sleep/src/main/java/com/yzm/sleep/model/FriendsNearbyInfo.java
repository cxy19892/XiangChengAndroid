package com.yzm.sleep.model;

import java.io.Serializable;

public class FriendsNearbyInfo implements Serializable{
	private static final long serialVersionUID = 1L;
    public String int_id = "";
	public String nickname = "";
	public String profile = "";
	/**睡眠时长*/
	public String sleep = "";
	/**睡觉时间 HH：mm*/
	public String sleepTime = "";
	/**起床时间 HH：mm*/
	public String upTime = "";
	/**来自平台*/
	public String platfrom = "";
	public String age = "";
	/**性别 01-男 ， 02-女*/
	public String gender = "";
	public String occupation = "";
	
	public String profile_key;
	
	
	public String profile_suolue;
	public String profile_key_suolue;
	
}
