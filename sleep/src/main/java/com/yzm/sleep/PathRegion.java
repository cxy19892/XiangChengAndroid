package com.yzm.sleep;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.model.SleepDistributionInfo;
import com.yzm.sleep.utils.HLog;
import com.yzm.sleep.utils.SleepUtils;

public class PathRegion extends View {
	private static final String TAG = "PathRegion";
	Context context;
	private static final int TEXT_SIZE = 10;
	private static final int TAG_TEXT_SIZE = 20;
	/** X方向刻度间隔距离 */
	private float line_divider_x = 0;
	/** Y方向刻度间隔距离 */
	private  float line_divider_y = 0;
	/** 刻度长短 */
	private static final int ITEM_HEIGHT = 5;
	/** 刻度精度 */
	private static final int MOD_TYPE = 10;
	/** 区域图距离底部的距离 */
	private static final int MARGIN_BOTTOM = 0;
	private float mDensity;
	private int mMinVelocity;
	private int mWidth, mHeight;
	private int mLastX, mMove;

	private Path path = new Path();
	private Region region = new Region();
	/** 单位小时在X方向上的长度 */
	private float unit_hour_length;
	/** 单位加速度在Y方向上的长度 */
	private float unit_acce_length;
	/** 睡眠分布数据 */
	private ArrayList<SleepDistributionInfo> dists;
	/** 画面积图的区域高 */
	private int height;
	/** 预设的睡眠时刻 */
	private String sleep_time_setting = DataUtils.dealData((int)(SleepInfo.SET_STARTTIME / 60)) + ":"+DataUtils.dealData((int) (SleepInfo.SET_STARTTIME % 60));;
	/** 预设起床时刻 */
	private String getup_time_setting = DataUtils.dealData((int)(SleepInfo.SET_ENDTIME / 60)) + ":"+DataUtils.dealData((int) (SleepInfo.SET_ENDTIME % 60));
	/** 实际预眠时刻 */
	private String sleep_time = "00:00";
	/** 实际起床时刻 */
	private String getup_time = "00:00";
	/** 实际预眠时刻加速度 */
	private float sleep_acce;
	/** 实际起床时刻加速度 */
	private float getup_acce;
	/** 睡觉时刻预设值 */
	private float set_hour_value_sleep;
	/** 起床时刻预设值 */
	private float set_hour_value_getup;
	/** 睡觉时刻显示值 */
	private float show_hour_value_sleep;
	/** 起床时刻显示值 */
	private float show_hour_value_getup;
	/** 零点之前基本小时值 */
	private float show_basictime_value = 0;
	private float time_value_total;
	/**加速度的最大值*/
	private float acceMax = 10;
	/** 是否有数据存在*/
	private boolean isDataExits = false;
	private boolean isNextDay = false;
	private boolean isSetTimeOut = false;
	/**入睡起床时间是否更新*/
	private boolean isUpdate = false;
	/**原始入睡时间*/
	private String orgSleepTime = "00:00";
	/**原始起床时间*/
	private String orgUpTime = "00:00";
	float sleep_time_dot_x = 0;
	float sleep_time_tag_x = 0;
	float sleep_time_text_x = 0;
	float sleep_time_dot_y = 0;
	float sleep_time_tag_y = 0 ;
	float sleep_time_text_y = 0;
	private TextPaint tag_paint;
	private Bitmap tag_bc;
	private Bitmap tag_dot;
	private static final int TO_UP = 50;

	public PathRegion(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mDensity = getContext().getResources().getDisplayMetrics().density;
		mMinVelocity = ViewConfiguration.get(getContext())
				.getScaledMinimumFlingVelocity();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		mWidth = getWidth();
		mHeight = getHeight();
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		init();
		set_hour_value_sleep = SleepUtils.getHours(sleep_time_setting);
		set_hour_value_getup = SleepUtils.getHours(getup_time_setting);
		show_hour_value_sleep = set_hour_value_sleep - 1;
		show_hour_value_getup = set_hour_value_getup + 3;
		if(show_hour_value_sleep < 0){
			show_hour_value_sleep = 24 + show_hour_value_sleep;
		}
		if (show_hour_value_getup > 23) {
			show_hour_value_sleep = show_hour_value_getup - 24;
		}
		
		count_scaleine_x_y();
		drawScaleLine(canvas);
		if(this.isDataExits && !TextUtils.isEmpty(orgSleepTime) && !TextUtils.isEmpty(orgUpTime)) {//数据存在且原始入睡时刻和起床时刻均不为空时
			count_width_value1();
			drawPathRegion(canvas);
//			if(TextUtils.isEmpty(orgSleepTime)||TextUtils.isEmpty(orgUpTime)){
//				drawTag(canvas,sleep_time,sleep_time,sleep_acce,true);
//				drawTag(canvas,getup_time,getup_time,getup_acce,false);
//			}else{
//				drawTag(canvas,orgSleepTime,sleep_time,sleep_acce,true);
//				drawTag(canvas,orgUpTime,getup_time,getup_acce,false);
//			}
			drawTag(canvas,orgSleepTime,sleep_time,sleep_acce,true);
			drawTag(canvas,orgUpTime,getup_time,getup_acce,false);
		}
	}


	/**
	 * 计算某时刻在x方向上的长度
	 */
	private void count_width_value1() {
//		acceMax = (float) ((acceMax > SleepInfo.ALERATE_DATA) ? acceMax : SleepInfo.ALERATE_DATA);
		unit_acce_length = height / (acceMax * 10 / 6);
		float show_hour_value = 0;// 时间小时显示值 
		SleepDistributionInfo dist = null;
		boolean isNextDay = false;
		for (int i = 0; i < dists.size(); i++) {
			dist = dists.get(i);
			float time_value_original = getHour(dist.time)
					+ getMinute(dist.time) / 60F;// 原始时间小时值
			
			if (show_hour_value_sleep > 12 && show_hour_value_getup <= 12) {		//show_basictime_value 不为0
				if(time_value_original > 12){
					show_hour_value = show_basictime_value - (24 - time_value_original);
				}else{
					show_hour_value = show_basictime_value + time_value_original;
				}
			}else{		//show_basictime_value 等于0
				show_hour_value = time_value_original - show_hour_value_sleep;
			}
			
//			if (time_value_original > 12) {// 显示时间数大于12小时
//				if (isNextDay) {
//					show_hour_value = show_basictime_value + time_value_original;
//				} else {
//					show_hour_value = show_basictime_value - (24 - time_value_original);
//				}
//			} else {// 开始显示时间数小于12小时
//				isNextDay = true;
//				show_hour_value = show_basictime_value + time_value_original;
//			}
			
			
			dist.time_value = unit_hour_length * show_hour_value;
		}
	}
	/**
	 * 计算x y方向格子数及每格的长度
	 */
	private void count_scaleine_x_y(){
		// 根据设置时间计算显示时间对应的x方向值
		time_value_total = 0;
		height = mHeight - MARGIN_BOTTOM;
	
		if (show_hour_value_sleep > 12 && show_hour_value_getup <= 12) {// 开始显示时间数大于12并且结束显示时间小于或等于12------
			show_basictime_value = 24 - show_hour_value_sleep;
			time_value_total = show_basictime_value + show_hour_value_getup;
		} else {// 开始显示时间数小于12
			time_value_total =  show_hour_value_getup - show_hour_value_sleep;
		}

		unit_hour_length = mWidth / time_value_total;

	}

	/**
	 * 画刻度标记 方格背景
	 * 
	 * @param canvas
	 */
	private void drawScaleLine(Canvas canvas) {
		HLog.i(TAG, "drawScaleLine");
		canvas.save();
		Paint linePaint = new Paint();
		linePaint.setColor(Color.parseColor("#B4B4B4"));
		linePaint.setStrokeWidth(3);

		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(16 * mDensity);//dipToPx
		
		textPaint.setColor(Color.parseColor("#B4B4B4"));
		textPaint.setTypeface(Typeface.DEFAULT); // 设置字体

		float xPosition = 0, yPosition = 0, textWidth = Layout.getDesiredWidth(
				"0", textPaint);
		// 画方格背景纵向线
		line_divider_x = unit_hour_length / 2;// 一格半个小时
		line_divider_x = SleepUtils.pxToDip(context, line_divider_x);
		float len = time_value_total * 2 + 1;
		for (int i = 0; i < len; i++) {
			xPosition = (0 + mMove) + i * line_divider_x * mDensity;
			if (xPosition + getPaddingLeft() <= mWidth) {
				linePaint.setColor(Color.parseColor("#E1E1E1"));
				canvas.drawLine(xPosition - 1, height, xPosition - 1, 0, linePaint);
			}
		}

		/* 画方格背景横向线 */
		line_divider_y = height / 10;
		line_divider_y = SleepUtils.pxToDip(context, line_divider_y);
		for (int i = 0; i < len; i++) {
			yPosition = (0 + mMove) + i * line_divider_y * mDensity;
			if (yPosition + getPaddingTop() < height
					|| yPosition + getPaddingTop() == height) {
				canvas.drawLine(0, yPosition - 1, mWidth, yPosition - 1, linePaint);
			}
		}

		/* 画X方向时间 */
//		String sleep_time_show = "00:00";
//		String getup_time_show = "00:00";

//		if (getHour(sleep_time_setting) == 0) {
//			sleep_time_show = "23:" + format(getMinute(sleep_time_setting));
//		} else {
//			sleep_time_show = format(getHour(sleep_time_setting) - 1) + ":"
//					+ format(getMinute(sleep_time_setting));
//		}
//
//		if (getHour(getup_time_setting) > 20) {
//			getup_time_show = format(getHour(getup_time_setting) + 3 - 24)
//					+ ":" + format(getMinute(getup_time_setting));
//		}else{//预设起床时间小于20或等于20时，显示结束睡眠时间值------
//			getup_time_show = format(getHour(getup_time_setting) + 3) + ":"
//					+ format(getMinute(getup_time_setting));
//		}

		if(!isSetTimeOut){//重置设置时间没有超出上一次设置时间的范围
//			canvas.drawText(sleep_time_show, 0, mHeight, textPaint);//画起始时间文本
//			canvas.drawText(getup_time_show,mWidth - Layout.getDesiredWidth(getup_time_show, textPaint),mHeight - 4 , textPaint);//画结束时间文本 mHeight
		}
		canvas.restore();
	}

	public String format(int i) {
		if (i < 10) {
			return "0" + i;
		} else {
			return i + "";
		}
	}

	/**
	 * 画睡眠分布面积区域
	 * 
	 * @param canvas
	 */
	private void drawPathRegion(Canvas canvas) {
		canvas.save();
		path.moveTo(0, height);
		path.lineTo(0, height - TO_UP);
		SleepDistributionInfo dist = null;
		for (int i = 0,len = dists.size(); i < len; i++) {
			dist = dists.get(i);
			if(i == 0){
				path.lineTo(dist.time_value, height - TO_UP);
			}else if(i == dists.size()-1){
				path.lineTo(dist.time_value + 10, height - TO_UP);
			}else{
				path.lineTo(dist.time_value, mHeight - MARGIN_BOTTOM - dist.accelerate_value
						* unit_acce_length - TO_UP);
			}
		}
		path.lineTo(mWidth, height - TO_UP);
		path.lineTo(mWidth, height);
		path.close();
		
		
		// 构造一个区域对象，左闭右开的。
//		RectF r = new RectF();
		// 计算控制点的边界
//		path.computeBounds(r, true);
		// 设置区域路径和剪辑描述的区域
//		region.setPath(path, new Region((int) r.left, (int) r.top,
//				(int) r.right, (int) r.bottom));
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#DCDCDC"));
		paint.setStyle(Paint.Style.FILL);
		// 设置抗锯齿。
		paint.setAntiAlias(true);
		// paint.setFilterBitmap(true);
		canvas.drawPath(path, paint);

		canvas.restore();
	}
	void init(){
		tag_paint = new TextPaint();
		tag_paint.setColor(Color.WHITE);
		tag_paint.setTextSize(SleepUtils.dipToPx(context, 18));
		tag_bc = BitmapFactory.decodeResource(getResources(),R.drawable.tag_bc);
		tag_dot = BitmapFactory.decodeResource(getResources(),R.drawable.tag_dot);
	}

	/**
	 * 画睡觉时刻、起床时刻的标签
	 * @param isDown
	 * 			isDown为true画睡点标签，为false是画起点标签
	 * @param canvas
	 */
	private void drawTag(Canvas canvas,String time,String newTime,float acce,boolean isDown) {
		canvas.save();
		float time_hours = SleepUtils.getHours(time);
		if(time_hours > 12){
			if(isNextDay ){
				sleep_time_dot_x = (SleepUtils.getHours(time) + show_basictime_value) * unit_hour_length - tag_dot.getWidth() / 2;
				sleep_time_tag_x = (SleepUtils.getHours(time) + show_basictime_value) * unit_hour_length - tag_bc.getWidth() / 2;
			}else{
				sleep_time_dot_x = (show_basictime_value - (24-SleepUtils.getHours(time))) * unit_hour_length - tag_dot.getWidth() / 2; 
				sleep_time_tag_x = (show_basictime_value - (24 -SleepUtils.getHours(time))) * unit_hour_length - tag_bc.getWidth() / 2;
			}
		}else{
			sleep_time_dot_x = (SleepUtils.getHours(time) + show_basictime_value) * unit_hour_length - tag_dot.getWidth() / 2;
			sleep_time_tag_x = (SleepUtils.getHours(time) + show_basictime_value) * unit_hour_length - tag_bc.getWidth() / 2;
			isNextDay  = true;
		}
			
		
		if(isDown){//画入睡时刻标签
			sleep_time_dot_y = mHeight - MARGIN_BOTTOM - acce * unit_acce_length - TO_UP;
			sleep_time_tag_y = sleep_time_dot_y + tag_dot.getHeight() ;
			if(sleep_time_dot_x < tag_bc.getWidth() / 2){//如果标签超出画图范围左侧
				sleep_time_tag_x = sleep_time_dot_x + tag_dot.getWidth();
				sleep_time_tag_y = sleep_time_dot_y - (tag_bc.getHeight() - tag_dot.getHeight())/2;
			}
			if(sleep_time_dot_y > mHeight - tag_bc.getHeight() - MARGIN_BOTTOM){//如果标签超出画图范围底部
				sleep_time_tag_y = sleep_time_dot_y - tag_bc.getHeight() ;
			}
		}else {//画起床时刻标签
			sleep_time_dot_y = mHeight - MARGIN_BOTTOM - acce * unit_acce_length - TO_UP;
			sleep_time_tag_y = sleep_time_dot_y - tag_bc.getHeight() ;
			if(sleep_time_dot_x > mWidth -  tag_bc.getWidth() / 2){//如果标签超出画图范围右侧
				sleep_time_tag_x = sleep_time_dot_x - tag_bc.getWidth();
				sleep_time_tag_y = sleep_time_dot_y - (tag_bc.getHeight() - tag_dot.getHeight())/2;
			}
			if(sleep_time_dot_y > mHeight - tag_bc.getHeight() - MARGIN_BOTTOM){//如果标签超出画图范围底部
				sleep_time_tag_y = sleep_time_dot_y - tag_bc.getHeight() ;
			}
		}
		sleep_time_text_y = sleep_time_tag_y + tag_bc.getHeight() / 3 * 2 + 4;
		sleep_time_text_x = sleep_time_tag_x + (tag_bc.getWidth()-Layout.getDesiredWidth(time+"睡",tag_paint)) / 2;
		
		canvas.drawBitmap(tag_dot, sleep_time_dot_x, sleep_time_dot_y, null);
		canvas.drawBitmap(tag_bc, sleep_time_tag_x, sleep_time_tag_y, null);
		
		if(isDown){
			if(isUpdate){
				canvas.drawText(newTime+"睡", sleep_time_text_x, sleep_time_text_y, tag_paint);
			}else{
				canvas.drawText(time+"睡", sleep_time_text_x, sleep_time_text_y, tag_paint);
			}
		}else {
			if(isUpdate){
				canvas.drawText(newTime+"起", sleep_time_text_x, sleep_time_text_y, tag_paint);
			}else{
				canvas.drawText(time+"起", sleep_time_text_x, sleep_time_text_y, tag_paint);
			}
		}
		
		canvas.restore();
	}


	public int getHour(String s) {
		int hour = 0;
		try{
			hour = Integer.parseInt(s.substring(0, s.indexOf(":")));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return hour;
	}

	public int getMinute(String s) {
		return Integer.parseInt(s.substring(s.indexOf(":") + 1));
	}

	/**
	 * 设置用户预设的睡眠时刻
	 * 
	 * @param s
	 */
	public void setSleepTime_Setting(String s) {
		this.sleep_time_setting = s;
	}

	/**
	 * 设置用户预设的起床时刻
	 * 
	 * @param s
	 */
	public void setGetupTime_Setting(String s) {
		this.getup_time_setting = s;
	}

	/**
	 * 设置实际睡眠时刻
	 * 
	 * @param s
	 */
	public void setSleepTime(String s) {
		this.sleep_time = s;
	}

	/**
	 * 设置实际起床时刻
	 * 
	 * @param s
	 */
	public void setGetupTime(String s) {
		this.getup_time = s;
	}

	/**
	 * 设置实际睡眠时刻加速度
	 * 
	 * @param f
	 */
	public void setSleepAcce(float sleep_acce) {
		this.sleep_acce = sleep_acce;
		HLog.i(TAG, "睡眠时刻的加速度为：" + sleep_acce);
	}

	/**
	 * 设置实际起床时刻加速度
	 */
	public void setGetupAcce(float getup_acce) {
		this.getup_acce = getup_acce;
		HLog.i(TAG, "起床时刻的加速度为：" + getup_acce);
	}

	/**
	 * 设置睡眠分布数据
	 * 
	 * @param dists
	 */
	public void setDistributionInfo(ArrayList<SleepDistributionInfo> dists) {
		this.dists = dists;
	}
	/**
	 * 设置加速度最大值
	 * @param acceMax
	 */
	public void setAcceMax(float acceMax){
		this.acceMax = acceMax;
	}
	
	/**
	 * 设置是否有数据
	 * @return
	 */
	public boolean isDataExits() {
		return isDataExits;
	}

	public void setDataExits(boolean isDataExits) {
		this.isDataExits = isDataExits;
	}
	
	public void setIsSetTimeOut(boolean isSetTimeOut){
		this.isSetTimeOut = isSetTimeOut;
	}
	/**
	 * 设置入睡起床时间是否更新
	 * @param isUpdate
	 */
	public void setHaveUpdate(boolean isUpdate){
		this.isUpdate = isUpdate;
	}
	public void setOrglSleepTime(String orgSleepTime){
		this.orgSleepTime = orgSleepTime;
	}
	public void setOrgUpTime(String orgUpTime){
		this.orgUpTime = orgUpTime;
	}
}
