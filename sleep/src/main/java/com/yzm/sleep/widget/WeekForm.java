package com.yzm.sleep.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.yzm.sleep.render.GetSleepResultValueClass.SleepData;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.render.ItemArray;
import com.yzm.sleep.render.ItemBean;
import com.yzm.sleep.utils.TimeFormatUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WeekForm extends View {
	
	/** 周几的字体大小*/
	private final int WEEK_TEXT_SIZE = 40;
	/** 左边时间轴的字体大小*/
	private final int TIME_TEXT_SIZE = 24;
	/** 上下多余的时间轴s*/
	private final long excesstime = 3600000;
	
	private Paint paint;
	private String[] weeks = new String[]{"一", "二", "三", "四", "五", "六", "日"};
	private float startSleep, endSleep;
//	private List<ModelsValues> weekData;
	private List<SleepDataHead> weekData;
	private long max_time, min_time;
	
	//实线颜色
	private int line1Color = 0xffCDCDCD;
	//虚线颜色
	private int line2Color = 0xffFFAF95;
	//虚线时间颜色
	private int xuxianTimeColor = 0xffFF5725;
	//时间轴字体颜色
	private int timeColor = 0xff2E2E2E;
	
	public WeekForm(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public WeekForm(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public WeekForm(Context context) {
		super(context);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//画实线
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(line1Color);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);
		canvas.drawLine(70, 0, 70, getHeight(), paint);
		
		startSleep = getHeight() * 9/10;
		endSleep = getHeight() * 2/10;
		//画虚线
		paint.reset();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);  
		paint.setColor(line2Color);
		drawXuLine(canvas, paint, 70, getWidth() - 70, startSleep);
		drawXuLine(canvas, paint, 70, getWidth() - 70, endSleep);
		
		//标注虚线的时间节点
		paint.reset();
		paint.setAntiAlias(true);
		paint.setTextSize(TIME_TEXT_SIZE);
		paint.setColor(xuxianTimeColor);
		float textWidth = paint.getTextSize();
		canvas.drawText("07:15", getWidth() - 70, startSleep, paint);
		canvas.drawText("11:00", getWidth() - 70, endSleep, paint);
		
		paint.setTextSize(WEEK_TEXT_SIZE);
		paint.setColor(timeColor);
		for (int i = 0; i < weeks.length; i++) {
			canvas.drawText(weeks[i], getWidth() / (weeks.length + 1) * i + 80, getHeight() - textWidth, paint);
		}
		
		if(weekData == null)
			return;
		List<ItemArray> list = filterData(weekData);
		// 左边时间轴的最大值和最小值
		getMaxAndMinTime(list);
		paint.reset();
		paint.setAntiAlias(true);
		paint.setTextSize(TIME_TEXT_SIZE);
		paint.setColor(timeColor);
		canvas.drawText(TimeFormatUtil.formatTime1(max_time + excesstime, "HH:mm"), 0, 0 + TIME_TEXT_SIZE, paint);
		canvas.drawText(TimeFormatUtil.formatTime1(min_time - excesstime, "HH:mm"), 0, getHeight() - 2 * WEEK_TEXT_SIZE, paint);
		
		//画每天的数据
		long totalMs = (max_time - min_time) + 2 * excesstime;
		float oneMsLength = (float)(getHeight() - 2 * WEEK_TEXT_SIZE)/totalMs;
		for (int i = 0; i < list.size(); i++) {
			ItemArray array = list.get(i);
			if(array == null)
				continue;
			List<ItemBean> item = array.getList();
			for (int j = 0; j < item.size(); j++) {
				
				paint.reset();
				paint.setAntiAlias(true);
				paint.setStyle(Paint.Style.FILL);
				paint.setStrokeWidth(40);
				if(item.get(j).getSleepType() == 1)
					paint.setColor(0xffEB6100);
				if(item.get(j).getSleepType() == 2)
					paint.setColor(0xffBCD029);
				if(item.get(j).getSleepType() == 3)
					paint.setColor(0xff7ECEF4);
				
				int week = array.getWeek();
				--week;
				float x = getWidth() / 7 * week + 100 - (100 * week/7);
				float startY = (getHeight() - 2 * WEEK_TEXT_SIZE) - (item.get(j).getStartTime() - (min_time - excesstime)) * oneMsLength;
				float endY = (getHeight() - 2 * WEEK_TEXT_SIZE) - (item.get(j).getEndTime() - (min_time - excesstime)) * oneMsLength;
				canvas.drawLine(x, startY, x, endY, paint);
			}
		}
		
	}
	
	/**
	 * 获取最大时间点和最小时间点
	 * @param list
	 */
	private void getMaxAndMinTime(List<ItemArray> list){
		for (int i = 0; i < list.size(); i++) {
			ItemArray array = list.get(i);
			if(array == null)
				continue;
			List<ItemBean> beans = array.getList();
			if(beans.size() > 0){
				ItemBean bean1 = beans.get(0);
				ItemBean bean2 = beans.get(list.get(i).getList().size() - 1);
				if(i== 0)
					min_time = bean1.getStartTime();
				if(bean1.getStartTime() < min_time)
					min_time = bean1.getStartTime();
				if(bean2.getEndTime() > max_time)
					max_time = bean2.getEndTime();
			}
		}
	}
	
	/**
	 *  设置画图数据
	 * @param weekData
	 */
	public void setData(List<SleepDataHead> weekData){
		this.weekData = weekData;
		this.invalidate();
	}
	
	/**
	 *  画虚线
	 * @param canvas
	 * @param mPaint
	 * @param mStartX  开始X位置
	 * @param mStopX   结束X位置
	 * @param mStopY   y轴位置
	 */
	private void drawXuLine(Canvas canvas, Paint mPaint, float mStartX, float mStopX, float mStopY){
		int iCalc2= (getWidth() - 140)/20+1;
		float marginX = mStartX;
		for(int i = 0;i < iCalc2; i++){			
			if(i%2==0){
				mStartX = marginX + i * 20;
				mStopX = mStartX + 20;
				canvas.drawLine(mStartX, mStopY, mStopX ,mStopY, mPaint);//画睡觉线		
			}
		};
	}
	
	/**
	 * 过滤数据
	 * @param weekData
	 * @return
	 */
	private List<ItemArray> filterData(List<SleepDataHead> weekData){
		List<ItemArray> list = new LinkedList<ItemArray>();
		for (int i = 0; i < weekData.size(); i++) {
			SleepDataHead dataHead = weekData.get(i);
			ItemArray array = new ItemArray();
			if(dataHead == null){
				list.add(null);
				continue;
			}
			List<SleepData> dataList = dataHead.m_pValue;
			List<ItemBean> beans = new ArrayList<ItemBean>();
			for (int j = 0; j < dataList.size(); j++) {
				SleepData data = dataList.get(j);
				if(j == 0){
					ItemBean bean = new ItemBean();
					bean.setSleepType(data.SleepType);
					bean.setStartTime(data.X_Time);
					bean.setEndTime(data.X_Time);
					beans.add(bean);
				}else{
					if(data.SleepType == dataList.get(j - 1).SleepType){
						beans.get(beans.size() - 1).setEndTime(data.X_Time);
					}else{
						ItemBean bean = new ItemBean();
						bean.setSleepType(data.SleepType);
						bean.setStartTime(beans.get(beans.size() - 1).getEndTime());
						bean.setEndTime(data.X_Time);
						beans.add(bean);
					}
				}
			}
			array.setList(beans);
			array.setWeek(getWeekOfDate(new Date(dataHead.XStart)));
			list.add(array);
		}
		return list;
	}
	
	 /**
     * 获取当前日期是星期几<br>
     * 
     * @param dt
     * @return w 当前日期是星期几
     */
    private int getWeekOfDate(Date dt) {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w == 0)
			w = 7;
        return w;
    }
	
}
