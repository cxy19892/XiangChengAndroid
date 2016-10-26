package com.yzm.sleep.widget;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("SimpleDateFormat") 
public class SoftSleepLengthTable extends View {

	private final int BULE_COLOR = 0xff6f9ef6;
	private final int TEXT_COLOR = 0xff7d7d90;
	private final int WIHTE_COLOR = 0xffedeced;
	
	private int width, height;
	private Paint mPaint;
	private float ts;
	
	private int defaultPosition = 0;
	
	public SoftSleepLengthTable(Context context) {
		super(context);
		init();
	}
	
	public SoftSleepLengthTable(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public SoftSleepLengthTable(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init(){
		mPaint = new Paint();
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		if(mList == null)
			return;
		
		int daysNum = mList.size();
		int x_length = width / (daysNum + 1);
		
		//先画日期
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setColor(TEXT_COLOR);
		mPaint.setTextSize(ts);
		mPaint.setTextAlign(Paint.Align.CENTER);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < daysNum; i++) {
			SoftDayData data = mList.get(i);
			if(data == null)
				continue;
			try {
				String date = new SimpleDateFormat("MM/dd").format(sdf.parse(data.getDate()));
				canvas.drawText(date, x_length * (i + 1), height, mPaint);
			} catch (Exception e) {
			}
		}
		
		//起始y轴点
		float startY = height - 1.5f * ts;
		//最大长度
		float maxLength = height - 3.0f * ts;
		
//		List<Float> hours = getWeekSleepLength(mList);
//		float maxSleepLength = maxSleepLength(hours);
		float maxSleepLength = 0.0f;
		List<Float> hours = new ArrayList<Float>();
		for (SoftDayData data : mList) {
			hours.add(data.getSleepLength());
			if(data.getSleepLength() > maxSleepLength)
				maxSleepLength = data.getSleepLength();
		}
		
		//再画柱状图
		for (int i = 0; i < daysNum; i++) {
			
			mPaint.reset();
			mPaint.setAntiAlias(true);
			if(i == defaultPosition)
				mPaint.setColor(BULE_COLOR);
			else
				mPaint.setColor(getResources().getColor(R.color.bg_color));
			mPaint.setStyle(Style.FILL);
			mPaint.setStrokeWidth(3.0f * ts);
			float per = 0.0f;
			if(maxSleepLength != 0)
				per = hours.get(i) / maxSleepLength;
			float lineHeight = startY - maxLength * per;
			canvas.drawLine(x_length * (i + 1), startY, x_length * (i + 1), lineHeight, mPaint);
			
			RectF oval = new RectF();
			oval.left = x_length * (i + 1) - 1.5f * ts; 
			oval.right = x_length * (i + 1) + 1.5f * ts; 
			oval.bottom = lineHeight + 1.5f * ts + 2 ;
			oval.top = lineHeight - 1.5f * ts;
			canvas.drawArc(oval, 180, 180, true, mPaint);
			
			mPaint.reset();
			mPaint.setAntiAlias(true);
			if(i == defaultPosition)
				mPaint.setColor(WIHTE_COLOR);
			else
				mPaint.setColor(getResources().getColor(R.color.ct_color));
			
			mPaint.setTextSize(ts);
			mPaint.setTextAlign(Paint.Align.CENTER);
			if(hours.get(i) > 0)
				canvas.drawText(hours.get(i) + "h", x_length * (i + 1), lineHeight + 0.5f * ts, mPaint);
		}
		
	}

	private float touchX;
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchX = event.getX();
			try {
				int daysNum = mList.size();
				int x_length = width / (daysNum + 1);
				for (int i = 0; i < daysNum; i++) {
					if(touchX >= (x_length * (i + 1) - 1.5f * ts) && touchX <= (x_length * (i + 1) + 1.5f * ts)){
						defaultPosition = i;
						this.postInvalidate();
						if(sleepLengthSelect != null)
							sleepLengthSelect.select(i);
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return false;
	}

	private SleepLengthSelect sleepLengthSelect;
	
	public void setOnClickPosition(SleepLengthSelect sleepLengthSelect){
		this.sleepLengthSelect = sleepLengthSelect;
	}

	public interface SleepLengthSelect{
		void select(int position);
	}
	
	private List<SoftDayData> mList;
	
	/**
	 * 集合不允许有null
	 * SoftDayData里面的3个值必不可少
	 * date  原始格式 yyyy-MM-dd 如果数据库当天没有数据也需要把日期设置进来，
	 * sleeptime long型的时间格式
	 * getupTime long型的时间格式
	 * @param mList
	 */
	public void setData(List<SoftDayData> mList){
		this.mList = mList;
		if(mList != null && mList.size() > 0)
			this.defaultPosition = mList.size() - 1;
		this.postInvalidate();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		width = getWidth();
		height = getHeight();
		ts = width /30;
	}

	
	/**
	 * 获取一周的睡眠总时长
	 * @param list
	 * @return
	 */
	private List<Float> getWeekSleepLength(List<SoftDayData> list){
		List<Float> hours = new ArrayList<Float>();
		for (SoftDayData data : list) {
			if(data == null){
				hours.add(0.0f);
				continue;
			}
			try {
				Long totalLength = Long.parseLong(data.getGetUpTimeLong()) - Long.parseLong(data.getSleepTimeLong());
				float hour = totalLength/3600000f;
				//四舍五入保留小数点后一位
				BigDecimal b = new BigDecimal(hour);  
				hour = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();  
				
				hours.add(hour < 0 ? 0.0f : hour);
			} catch (Exception e) {
				hours.add(0.0f);
			}
		}
		return hours;
	}
	
	/**
	 * 获取最大时间值
	 * @param list
	 * @return
	 */
	private float maxSleepLength(List<Float> list){
		float maxLength = 0.0f;
		for (Float hour : list) {
			if(hour > maxLength)
				maxLength = hour;
		}
		return maxLength;
	}
	
}
