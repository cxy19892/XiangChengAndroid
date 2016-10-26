package com.yzm.sleep.render;

import java.util.List;


public class ItemBean {

	private int length;
	private int sleepType;
	private float per;
	private String time;
	private long startTime;
	private long endTime;
	
//	/** 起始时间*/
//	private long XStart;
//	/** 结束时间*/
//	private long XStop;
//	/** Y轴最大量化值*/
//	private int YMax;
//	/** 入睡时刻*/
//	private long InSleepTime;
//	/** 醒来时刻*/
//	private long OutSleepTime;
//	/** 睡眠总时长 */
//	private int TotalSleepTime;
//	/** 深度睡眠时长*/
//	private int Deep_Sleep;
//	/** 浅度睡眠时长*/
//	private int Shallow_Sleep;
//	/** 清醒时长*/
//	private int AwakeTime_Sleep;
//	/** 在床时长*/
//	private int OnBed;
//	/** 入睡速度*/
//	private int ToSleep;
//	/** 清醒次数*/
//	private int AwakeCount;
//	/** 赖床时间*/
//	private int AwakeNoGetUpCount;
//	/** 上床时间 */
//	private long GoToBedTime;
//	/** 起床时间*/
//	private long GetUpTime;
//	/** 每日数据集*/
//	private List<ListLength> listLength; 
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getSleepType() {
		return sleepType;
	}

	public void setSleepType(int sleepType) {
		this.sleepType = sleepType;
	}

	public float getPer() {
		return per;
	}

	public void setPer(float per) {
		this.per = per;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

//	public long getXStart() {
//		return XStart;
//	}
//
//	public void setXStart(long xStart) {
//		XStart = xStart;
//	}
//
//	public long getXStop() {
//		return XStop;
//	}
//
//	public void setXStop(long xStop) {
//		XStop = xStop;
//	}
//
//	public int getYMax() {
//		return YMax;
//	}
//
//	public void setYMax(int yMax) {
//		YMax = yMax;
//	}
//
//	public long getInSleepTime() {
//		return InSleepTime;
//	}
//
//	public void setInSleepTime(long inSleepTime) {
//		InSleepTime = inSleepTime;
//	}
//
//	public long getOutSleepTime() {
//		return OutSleepTime;
//	}
//
//	public void setOutSleepTime(long outSleepTime) {
//		OutSleepTime = outSleepTime;
//	}
//
//	public int getTotalSleepTime() {
//		return TotalSleepTime;
//	}
//
//	public void setTotalSleepTime(int totalSleepTime) {
//		TotalSleepTime = totalSleepTime;
//	}
//
//	public int getDeep_Sleep() {
//		return Deep_Sleep;
//	}
//
//	public void setDeep_Sleep(int deep_Sleep) {
//		Deep_Sleep = deep_Sleep;
//	}
//
//	public int getShallow_Sleep() {
//		return Shallow_Sleep;
//	}
//
//	public void setShallow_Sleep(int shallow_Sleep) {
//		Shallow_Sleep = shallow_Sleep;
//	}
//
//	public int getAwakeTime_Sleep() {
//		return AwakeTime_Sleep;
//	}
//
//	public void setAwakeTime_Sleep(int awakeTime_Sleep) {
//		AwakeTime_Sleep = awakeTime_Sleep;
//	}
//
//	public int getOnBed() {
//		return OnBed;
//	}
//
//	public void setOnBed(int onBed) {
//		OnBed = onBed;
//	}
//
//	public int getToSleep() {
//		return ToSleep;
//	}
//
//	public void setToSleep(int toSleep) {
//		ToSleep = toSleep;
//	}
//
//	public int getAwakeCount() {
//		return AwakeCount;
//	}
//
//	public void setAwakeCount(int awakeCount) {
//		AwakeCount = awakeCount;
//	}
//
//	public int getAwakeNoGetUpCount() {
//		return AwakeNoGetUpCount;
//	}
//
//	public void setAwakeNoGetUpCount(int awakeNoGetUpCount) {
//		AwakeNoGetUpCount = awakeNoGetUpCount;
//	}
//
//	public long getGoToBedTime() {
//		return GoToBedTime;
//	}
//
//	public void setGoToBedTime(long goToBedTime) {
//		GoToBedTime = goToBedTime;
//	}
//
//	public long getGetUpTime() {
//		return GetUpTime;
//	}
//
//	public void setGetUpTime(long getUpTime) {
//		GetUpTime = getUpTime;
//	}
//	
//	public List<ListLength> getListLength() {
//		return listLength;
//	}
//
//	public void setListLength(List<ListLength> listLength) {
//		this.listLength = listLength;
//	}


	public class ListLength{
		/** 序号*/
		private int index;
		/** 时间*/
		private long X_Time;
		/** 高度值*/
		private int Y_SleepValue;
		/** 深浅度标志*/
		private int SleepType;
		
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public long getX_Time() {
			return X_Time;
		}
		public void setX_Time(long x_Time) {
			X_Time = x_Time;
		}
		public int getY_SleepValue() {
			return Y_SleepValue;
		}
		public void setY_SleepValue(int y_SleepValue) {
			Y_SleepValue = y_SleepValue;
		}
		public int getSleepType() {
			return SleepType;
		}
		public void setSleepType(int sleepType) {
			SleepType = sleepType;
		}
	}
	
}
