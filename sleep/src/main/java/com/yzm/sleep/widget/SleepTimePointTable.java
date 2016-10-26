package com.yzm.sleep.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.xpillowjni.XpillowInterface;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.utils.TimeFormatUtil;

@SuppressLint("SimpleDateFormat") 
public class SleepTimePointTable extends View {

	private Paint mPaint;
	private float width, height, ts;
	private final int TEXT_COLOR = 0xff61637b;
	private final int BULE_COLOR = 0xff6f9ef6;
	private final int BACK_COLOR = 0xff6f9ef6;
	private final int COLOR1= 0xfffd823a;
	
	public SleepTimePointTable(Context context) {
		super(context);
		init();
	}

	public SleepTimePointTable(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public SleepTimePointTable(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init(){
		mPaint = new Paint();
		setLayerType(View.LAYER_TYPE_HARDWARE, null);  
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getWidth();
		height = getHeight();
		ts = width /30; 
	}

	private List<SoftDayData> mList;
	private String targetTime;
	private int inOrOut;
	
	/**
	 *   入睡/起床点的图标数据
	 * @param mList
	 * @param targetTime  目标入睡时间/起床时间   下面的对应0和1
	 * @param inOrOut     0是入睡点， 1是起床点
	 */
	public void setData(List<SoftDayData> mList, String targetTime, int inOrOut){
		this.mList = mList;
		this.targetTime = targetTime;
		this.inOrOut = inOrOut;
//		this.postInvalidate();//invalidate();
	}
	
	
	
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(mList == null || targetTime == null)
			return;
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
		List<String> times = getTimes(targetTime);
		float timesTotalLength = 0.0f;
		float targetLength = 0.0f;
		try {
			timesTotalLength = getIntervelTime(times.get(0), times.get(times.size() - 1));
			targetLength = getIntervelTime(times.get(0), targetTime);
		} catch (Exception e) {
		}
		
		float timeStartY = ts * 2;
		float totalY = height - 2 * timeStartY;
		float childYLength = totalY / (times.size() - 1);
		
		//先绘制左边的时间点
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(TEXT_COLOR);
		mPaint.setTextSize(ts);
		mPaint.setTextAlign(Paint.Align.LEFT);
		for (int i = 0; i < times.size(); i++) {
			canvas.drawText(times.get(i), ts, height - timeStartY - (childYLength * i), mPaint);
		}
		
		//接着绘制最近7天的日期
		float dateStartX = 5 * ts;
		float totalX = width - 2 * timeStartY;
		float childXLength = totalX / mList.size();
		mPaint.setTextSize(ts);
		mPaint.setTextAlign(Paint.Align.CENTER);
		for (int i = 0; i < mList.size(); i++) {
			try {
				canvas.drawText(sdf2.format(sdf1.parse(mList.get(i).getDate())), dateStartX + childXLength * i, height - 5, mPaint);
			} catch (Exception e) {
			}
		}
		
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(TEXT_COLOR);
		mPaint.setStrokeWidth(1);
		float y = height - timeStartY - ts/2 - (totalY * (targetLength / timesTotalLength));
		drawDashLine(canvas, mPaint, dateStartX - ts, dateStartX + childXLength * (mList.size() - 1), y);
		for (int i = 0; i < mList.size(); i++) {
			mPaint.setStyle(Style.STROKE);
			mPaint.setColor(BULE_COLOR);
			canvas.drawCircle(dateStartX + childXLength * i, y, ts/2, mPaint);
			mPaint.setColor(BULE_COLOR);
			mPaint.setStyle(Style.STROKE);
			canvas.drawCircle(dateStartX + childXLength * i, y, ts/2 , mPaint);
		}
		
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(BULE_COLOR);
		mPaint.setStrokeWidth(1);
		
		float startX = dateStartX;
		int lineCount = (int) (childXLength * (mList.size() - 1));
		
		List<Double> points ;
		if(inOrOut == 0)
			points = getAllInSleepLength(times);
		else
			points = getAllOutSleepLength(times);
		double[] dd = new double[points.size()];
		for (int i = 0; i < dd.length; i++) {
			dd[i] = points.get(i);
		}
		double[] ff = new XpillowInterface().GetDrawData(10, dd, lineCount);
		float startY = height - timeStartY - ts/2;
		
		LinearGradient shader;
		float x_last = 0 ,y_last =0 ;
		for (int i = 0; i < lineCount; i++) { 
			shader = new LinearGradient(startX + i, startY, startX + i, startY - (totalY * (float)ff[i]), 
					0x006f9ef6, 
					Color.argb((int)(ff[i] * 127f), 111, 158, 246), 
					TileMode.MIRROR); 
			mPaint.setStrokeWidth(1);
			mPaint.setShader(shader);
			canvas.drawLine(startX + i, startY, startX + i, startY - (totalY * (float)ff[i]), mPaint);
			mPaint.setShader(null);
			shader = null;
			
			mPaint.reset();
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(3);
			mPaint.setAntiAlias(true);
			mPaint.setColor(BULE_COLOR);
			
			if(i==0)
			{
				x_last=startX + i;
				y_last= startY - (totalY * (float)ff[i]);
				canvas.drawPoint(startX + i, startY - (totalY * (float)ff[i]), mPaint);
			}else {
				
				mPaint.setStrokeWidth((float) 3.0);              //设置线宽  
			    canvas.drawLine(x_last, y_last, startX + i, startY - (totalY * (float)ff[i]), mPaint);        //绘制直线  
				x_last=startX + i;
				y_last= startY - (totalY * (float)ff[i]);
			    
			}
			
		}
		
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth((float) 3.0); 
		mPaint.setStyle(Style.STROKE);
		for (int i = 0; i < dd.length; i++) {
			if(TextUtils.isEmpty(mList.get(i).getGetUpTime()))	
				continue;
			long target = TimeFormatUtil.timeToMiss(targetTime);
			
			if(inOrOut == 0){//入睡
				int inSleepTime = TimeFormatUtil.timeToMiss(mList.get(i).getSleepTime());
				mPaint.setColor(BACK_COLOR);
				long temp = inSleepTime - target;
				if (temp >= 0 && temp <= 30 * 60){
					mPaint.setColor(BACK_COLOR);
				}else{
					mPaint.setColor(COLOR1);
				}
			}else{
				int outSleepTime = TimeFormatUtil.timeToMiss(mList.get(i).getGetUpTime());
				long tempup = outSleepTime - target;
				if (tempup >= -15 * 60 && tempup <= 15 * 60){
					mPaint.setColor(BACK_COLOR);
				}else{
					mPaint.setColor(COLOR1);
				}
			}
			
			canvas.drawCircle(startX + childXLength * i, startY - (totalY * (float)(dd[i]/10f)), ts/2, mPaint);
			
//			mPaint.setColor(BULE_COLOR);
//			canvas.drawCircle(startX + childXLength * i, startY - (totalY * (float)(dd[i]/10f)), ts/2 - 4, mPaint);
		}
	}


	/**
	 * 获取数据在表上的位置
	 * @param times
	 * @return
	 */
	private List<Double> getAllInSleepLength(List<String> times) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
		List<Double> list = new ArrayList<Double>();
		
		//先判断左边的时间轴是否有跨天
		boolean isDay2Day = false;
		try {
			if((sdfTime.parse(times.get(times.size() - 1)).getTime() -  sdfTime.parse(times.get(0)).getTime()) < 0){
				isDay2Day = true;
			}
		} catch (Exception e) {
		}
		try {
			float timesTotalLength = getIntervelTime(times.get(0), times.get(times.size() - 1));
			for (int i = 0; i < mList.size(); i++) {
				String date = mList.get(i).getDate();
				String insleepTime = mList.get(i).getSleepTime();
				if(TextUtils.isEmpty(insleepTime)){
					list.add(0d);
					continue;
				}
				float t = getIntervelTime(times.get(0), insleepTime);
				
				float per = t/timesTotalLength;
				if(per > 1){
					long insleepTimeLong = Long.valueOf(mList.get(i).getSleepTimeLong());
					if(isDay2Day){//有跨天的情况
						if(sdfDate.format(new Date(insleepTimeLong)).equals(date))
							per = 0;  //入睡时间点跟数据属于同一天
						else
							per = 1;  //入睡时间点跟数据不属于同一天
					}else{ //没有跨天的情况
						if((sdfTime.parse(times.get(0)).getTime() -  sdfTime.parse("12:00").getTime()) > 0){ //昨晚的时间轴，而且没有跨天
							if(sdfDate.format(new Date(insleepTimeLong)).equals(date)){ //入睡时间点跟数据属于同一天
								if((sdfTime.parse(insleepTime).getTime() -  sdfTime.parse(times.get(times.size() - 1)).getTime()) > 0)
									per = 1; 
								else
									per = 0;
							}
							else
								per = 1;
						}else{
							if(sdfDate.format(new Date(insleepTimeLong)).equals(date)){ //入睡时间点跟数据属于同一天
								if((sdfTime.parse(insleepTime).getTime() -  sdfTime.parse(times.get(times.size() - 1)).getTime()) > 0)
									per = 1; 
								else
									per = 0;
							}
							else
								per = 0;
						}
							
					}
				}
				list.add((double)(per * 10f));
			}
		} catch (Exception e) {
		}
		return list;
	}

	
	/**
	 * 获取数据在表上的位置
	 * @param times
	 * @return
	 */
	private List<Double> getAllOutSleepLength(List<String> times) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
		List<Double> list = new ArrayList<Double>();
		
		//先判断左边的时间轴是否有跨天
		boolean isDay2Day = false;
		try {
			if((sdfTime.parse(times.get(times.size() - 1)).getTime() -  sdfTime.parse(times.get(0)).getTime()) < 0){
				isDay2Day = true;
			}
		} catch (Exception e) {
		}
		try {
			float timesTotalLength = getIntervelTime(times.get(0), times.get(times.size() - 1));
			for (int i = 0; i < mList.size(); i++) {
				String date = mList.get(i).getDate();
				String outSleeepTime = mList.get(i).getGetUpTime();
				if(TextUtils.isEmpty(outSleeepTime)){
					list.add(0d);
					continue;
				}
				float t = getIntervelTime(times.get(0), outSleeepTime);
				
				float per = t/timesTotalLength;
				if(per > 1){
					long outsleepTimeLong = Long.valueOf(mList.get(i).getGetUpTimeLong());
					if(isDay2Day){//有跨天的情况
						if(sdfDate.format(new Date(outsleepTimeLong)).equals(date))
							per = 0;  
						else
							per = 1;  
					}else{ //没有跨天的情况
						if((sdfTime.parse(times.get(0)).getTime() -  sdfTime.parse("12:00").getTime()) > 0){ //昨晚的时间轴，而且没有跨天
							if(sdfDate.format(new Date(outsleepTimeLong)).equals(date)){
								if((sdfTime.parse(outSleeepTime).getTime() -  sdfTime.parse(times.get(times.size() - 1)).getTime()) > 0)
									per = 1; 
								else
									per = 0;
							}
							else
								per = 1;
						}else{
							if(!sdfDate.format(new Date(outsleepTimeLong)).equals(date)){
								if((sdfTime.parse(outSleeepTime).getTime() -  sdfTime.parse(times.get(times.size() - 1)).getTime()) > 0)
									per = 1; 
								else
									per = 0;
							}
							else
								per = 1;
						}
							
					}
				}
				list.add((double)(per * 10f));
			}
		} catch (Exception e) {
		}
		return list;
	}
	
	/**
	 * 画横向虚线
	 * @param canvas
	 * @param paint
	 * @param startX
	 * @param stopX
	 * @param y
	 */
	private void drawDashLine(Canvas canvas, Paint paint, float startX, float stopX, float y){
		float dashWith = 10;
		float newX = startX - 5;
		boolean go = true;
		while (go) {
			newX += 5;
			canvas.drawLine(newX, y, newX + dashWith, y, paint);
			float nowX = newX + dashWith;
			if(nowX > stopX)
				go = false;
			else 
				newX = nowX;
		}
	}
	
	/**
	 * 根据目标时间点获取左边的时间轴
	 * @param time
	 * @return
	 */
	private List<String> getTimes(String time){
		List<String> times = new ArrayList<String>();
		try {
			int hour = 0;
			String[] hm = time.split(":");
			String newTime = "";
			if(Integer.parseInt(hm[1]) == 0 || Integer.parseInt(hm[1]) == 30){
				newTime = time;
			}else{
				if(Integer.parseInt(hm[1]) > 0 && Integer.parseInt(hm[1]) < 30)
					hour = Integer.parseInt(hm[0]);
				else
					hour = Integer.parseInt(hm[0]) + 1;
				hour = (hour == 24 ? 0 : hour);
				if(hour < 10)
					newTime = "0" + hour + ":00";
				else
					newTime = hour + ":00";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			long tl = sdf.parse(newTime).getTime();;
			long halfHour = 60 * 60 * 1000;
			for (int i = 0; i < 7; i++) {
				long t = tl - halfHour * 3 + halfHour * i;
				times.add(sdf.format(new Date(t)));
			}
		} catch (Exception e) {
		}
		
		return times;
	}

	/**
	 * 获取时间间隔
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private float getIntervelTime(String startTime, String endTime){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			long st = sdf.parse(startTime).getTime();
			long et = sdf.parse(endTime).getTime();
			long intervel = (et - st) >= 0 ? (et - st) : (et - st + 24 * 60 * 60 * 1000); 
			float intervelHour = intervel /  (float)(60 * 60 * 1000);
			
			return intervelHour;
		} catch (Exception e) {
		}
		return 0.0f;
	}
	
}
