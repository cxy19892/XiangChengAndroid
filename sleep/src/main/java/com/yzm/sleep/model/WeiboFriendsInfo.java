package com.yzm.sleep.model;

import java.io.Serializable;
import java.util.ArrayList;

public class WeiboFriendsInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	public ArrayList<String> ids = new ArrayList<String>();
	public String next_cursor = "";
	public String previous_curdor = "";
	public String total_number = "";
}
