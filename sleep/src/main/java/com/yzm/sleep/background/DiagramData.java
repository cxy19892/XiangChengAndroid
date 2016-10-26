package com.yzm.sleep.background;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;

public class DiagramData implements Comparable<DiagramData>{
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private String date;
	private String time;
	private float acce;
	
	
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public float getAcce() {
		return acce;
	}


	public void setAcce(float acce) {
		this.acce = acce;
	}


	@Override
	public int compareTo(DiagramData data) {
		// TODO Auto-generated method stub
		try {
			long thistime = sdf.parse(this.time).getTime();
			long datatime = sdf.parse(data.getTime()).getTime();
			if(thistime < datatime){
				return -1;
			}else if (thistime > datatime) {
				return 1;
			}else {
				return 0;
			}	
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
