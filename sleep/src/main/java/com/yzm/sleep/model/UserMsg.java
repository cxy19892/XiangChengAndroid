package com.yzm.sleep.model;

import java.util.ArrayList;

public class UserMsg extends ResultMsg{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserInfo user_info = new UserInfo();
	
	public String int_id ="";
	
	public ArrayList<FriendsNearbyInfo> friend_list = new ArrayList<FriendsNearbyInfo>();
	
	public ArrayList<FriendsNearbyInfo> nearby_list = new ArrayList<FriendsNearbyInfo>();
	
	public ArrayList<FriendsNearbyInfo> request_list = new ArrayList<FriendsNearbyInfo>();
	
	public DailyRankInfo daily_rank = new DailyRankInfo();    
	
	
}
