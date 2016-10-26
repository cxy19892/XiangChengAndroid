package com.yzm.sleep.widget;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;

@SuppressLint("SimpleDateFormat") 
public class OrangeSleepLengthTable extends View {

	private final int BULE_COLOR = 0xff9EAAFC;
	private final int PURPLE_COLOR = 0xff6f9ef6;
	private final int TEXT_COLOR = 0xffffffff;
	private final int WIHTE_COLOR = 0xffe0e0f6;
	
	private int width, height;
	private Paint mPaint;
	private float ts;

	private int defaultPosition = 0;
	
	public OrangeSleepLengthTable(Context context) {
		super(context);
		init();
	}
	
	public OrangeSleepLengthTable(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public OrangeSleepLengthTable(Context context, AttributeSet attrs,
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
		mPaint.setColor(getResources().getColor(R.color.ct_color));
		mPaint.setTextSize(ts);
		mPaint.setTextAlign(Paint.Align.CENTER);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < daysNum; i++) {
			try {
				String date = new SimpleDateFormat("MM/dd").format(sdf.parse(mList.get(i).getDatestring()));
				canvas.drawText(date, x_length * (i + 1), height, mPaint);
			} catch (Exception e) {
			}
		}
		
		//起始y轴点
		float startY = height - 1.5f * ts;
		//最大长度
		float maxLength = height - 3.5f * ts;
		
//		List<Float> hours = getWeekSleepLength(mList);
//		List<Float> deepHours = getWeekSleepDeepLength(mList);
//		float maxSleepLength = maxSleepLength(mList);
		
		float maxSleepLength = 0.0f;
		List<Float> hours = new ArrayList<Float>();
		List<Float> deepHours = new ArrayList<Float>();
		for (int i = 0; i < mList.size(); i++) {
			try {
				float hour = (Integer.parseInt(mList.get(i).getSleeplong()) / 60f);
				hours.add(new BigDecimal(hour).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
				if(hour > maxSleepLength)
					maxSleepLength = hour;
			} catch (Exception e) {
				e.printStackTrace();
				hours.add(0.0f);
			}
			
			try {
				float deepHour = (Integer.parseInt(mList.get(i).getDeepsleep()) / 60f);
				deepHours.add(new BigDecimal(deepHour).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//再画柱状图
		for (int i = 0; i < daysNum; i++) {
			
			mPaint.reset();
			mPaint.setAntiAlias(true);
			if(defaultPosition == i)
				mPaint.setColor(BULE_COLOR);
			else
				mPaint.setColor(getResources().getColor(R.color.bg_color));
			mPaint.setStyle(Style.FILL);
			mPaint.setStrokeWidth(3.0f * ts);
			float per = 0.0f;
			if(maxSleepLength > 0)
				per = hours.get(i) / maxSleepLength;
			float lineHeight = startY - maxLength * per;
			canvas.drawLine(x_length * (i + 1), startY, x_length * (i + 1), lineHeight, mPaint);
			
			RectF oval = new RectF();
			oval.left = x_length * (i + 1) - 1.5f * ts; 
			oval.right = x_length * (i + 1) + 1.5f * ts; 
			oval.bottom = lineHeight + 1.5f * ts + 2 ;
			oval.top = lineHeight - 1.5f * ts;
			canvas.drawArc(oval, 180, 180, true, mPaint);
			
			if(defaultPosition == i)
				mPaint.setColor(PURPLE_COLOR);
			else
				mPaint.setColor(getResources().getColor(R.color.bg_color));
			float per1 = 0.0f;
			if(maxSleepLength > 0)
				per1 = deepHours.get(i) / maxSleepLength;
			float lineHeight1 = startY - maxLength * per1;
			canvas.drawLine(x_length * (i + 1), startY, x_length * (i + 1), lineHeight1, mPaint);
			
			mPaint.reset();
			mPaint.setAntiAlias(true);
			if(defaultPosition == i)
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

	private class OrangeDayData{
		private String date;
		private int deepSleepLength;
		private int shallowSleepLength;
		
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public int getDeepSleepLength() {
			return deepSleepLength;
		}
		public void setDeepSleepLength(int deepSleepLength) {
			this.deepSleepLength = deepSleepLength;
		}
		public int getShallowSleepLength() {
			return shallowSleepLength;
		}
		public void setShallowSleepLength(int shallowSleepLength) {
			this.shallowSleepLength = shallowSleepLength;
		}
	}
	
	private List<OrangeDayData> m_List;
	private List<SleepStatusBean> mList;
	
	public void setData(List<SleepStatusBean> mList){
		this.mList = mList;
		if(mList != null && mList.size() > 0)
			this.defaultPosition = mList.size() - 1;
		this.postInvalidate();
	}
	
	/**
	 *  设置画图数据
	 * @param weekData
	 */
	public void setData(List<byte[]> list, List<String> dates){
		if(list == null || dates == null || list.size() != dates.size())
			return;
		if(m_List == null)
			m_List = new ArrayList<OrangeDayData>();
		m_List.clear();
		for (int i = 0; i < list.size(); i++) {
			byte[] bfr = list.get(i);
			OrangeDayData data = new OrangeDayData();
			data.setDate(dates.get(i));
			if(bfr != null && bfr.length > 0){
				SleepDataHead dataHead = BluetoothDataFormatUtil.format3(bfr, 10);
				data.setDeepSleepLength(dataHead.Deep_Sleep);
				data.setShallowSleepLength(dataHead.Shallow_Sleep);
			}else{
				data.setDeepSleepLength(0);
				data.setShallowSleepLength(0);
			}
			m_List.add(data);
		}
//		this.invalidate();
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

	private SleepLengthSelect sleepLengthSelect;
	
	public void setOnClickPosition(SleepLengthSelect sleepLengthSelect){
		this.sleepLengthSelect = sleepLengthSelect;
	}

	public interface SleepLengthSelect{
		void select(int position);
	}
	
//	/**
//	 * 获取一周每天的睡眠总时长
//	 * @param list
//	 * @return
//	 */
//	private List<Float> getWeekSleepLength(List<OrangeDayData> list){
//		List<Float> hours = new ArrayList<Float>();
//		for (OrangeDayData data : list) {
//			try {
//				float hour = (data.deepSleepLength + data.shallowSleepLength)/60f;
//				//四舍五入保留小数点后一位
//				BigDecimal b = new BigDecimal(hour);  
//				hour = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();  
//				
//				hours.add(hour < 0 ? 0.0f : hour);
//			} catch (Exception e) {
//				hours.add(0.0f);
//			}
//		}
//		return hours;
//	}
	
//	/**
//	 * 获取一周每天的深度睡眠总时长
//	 * @param list
//	 * @return
//	 */
//	private List<Float> getWeekSleepDeepLength(List<OrangeDayData> list){
//		List<Float> hours = new ArrayList<Float>();
//		for (OrangeDayData data : list) {
//			try {
//				float hour = data.deepSleepLength/60f;
//				//四舍五入保留小数点后一位
//				BigDecimal b = new BigDecimal(hour);  
//				hour = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();  
//				
//				hours.add(hour < 0 ? 0.0f : hour);
//			} catch (Exception e) {
//				hours.add(0.0f);
//			}
//		}
//		return hours;
//	}
	
//	/**
//	 * 获取最大时间值
//	 * @param list
//	 * @return
//	 */
//	private float maxSleepLength(List<OrangeDayData> list){
//		float maxLength = 0.0f;
//		for (OrangeDayData data : list) {
//			int length = data.getDeepSleepLength() + data.getShallowSleepLength();
//			if((length / 60f) > maxLength)
//				maxLength = length / 60f;
//		}
//		return maxLength;
//	}
	
}
