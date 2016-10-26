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
import android.view.View;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.imageSelect.Folder;

@SuppressLint("SimpleDateFormat") 
public class ReportSleepLengthTable extends View {

	private final int BULE_COLOR = 0xff9EAAFC;
	private final int PURPLE_COLOR = 0xff6f9ef6;
	private final int TEXT_COLOR = 0xffffffff;
	private final int WIHTE_COLOR = 0xffe0e0f6;
	
	private final int TEXT_COLOR1 =  0xff000000;
	private final int TEXT_COLOR2= 0xfffd823a;
	private final int BG_COLOR1 = 0xff7b96de;
	private final int BG_COLOR2 = 0xff91dced;
	
	private int width, height;
	private Paint mPaint;
	private float ts;
	
	public ReportSleepLengthTable(Context context) {
		super(context);
		init();
	}
	
	public ReportSleepLengthTable(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public ReportSleepLengthTable(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init(){
		mPaint = new Paint();
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(mList == null)
			return;
		
		int daysNum = mList.size();
		int x_length = width / (daysNum +1);
		
		//先画日期
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setColor(getResources().getColor(R.color.fct_color));
		mPaint.setTextSize(ts);
		mPaint.setTextAlign(Paint.Align.CENTER);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < daysNum; i++) {
			try {
				String date = new SimpleDateFormat("MM/dd").format(sdf.parse(mList.get(i).getDatestring()));
				canvas.drawText(date, x_length * (i + 1) , height, mPaint);
			} catch (Exception e) {
			}
		}
		
//		//顶部线条   + x_length/4
//		canvas.drawLine(x_length, 0f, (float)width, 0.5f, mPaint);
//		mPaint.reset();
//		mPaint.setAntiAlias(true);
		
		//起始y轴点
		float startY = height - (1.5f * ts);
		//最大长度
		float maxLength = height - 4 * (1.5f * ts);
//		
//		//底部线条
//		mPaint.setColor(getResources().getColor(R.color.fct_color));
//		canvas.drawLine(x_length, startY, (float)width, startY + 0.5f, mPaint);
//		mPaint.reset();
//		mPaint.setAntiAlias(true);
		
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
				deepHours.add(0.0f);
			}
		}
		
			//再画柱状图
			for (int i = 0; i < daysNum; i++) {
				mPaint.reset();
				mPaint.setAntiAlias(true);
				
				mPaint.setStyle(Style.FILL);
				mPaint.setStrokeWidth(3.0f * ts);
				mPaint.setColor(BG_COLOR2);
				//最大时长
				float per = 0.0f;
				if(maxSleepLength != 0)
					per = hours.get(i) / maxSleepLength;
				float lineHeight = startY - maxLength * per;
				canvas.drawLine(x_length * (i + 1), startY, x_length * (i + 1), lineHeight, mPaint);
				//半圆
				RectF oval = new RectF();
				oval.left = x_length * (i + 1) - 1.5f * ts; 
				oval.right = x_length * (i + 1) + 1.5f * ts; 
				oval.bottom = lineHeight + 1.5f * ts + 2 ;
				oval.top = lineHeight - 1.5f * ts;
				canvas.drawArc(oval, 180, 180, true, mPaint);
				mPaint.reset();
				mPaint.setAntiAlias(true);
			
				//入睡时长
				mPaint.setStyle(Style.FILL);
				mPaint.setStrokeWidth(3.0f * ts);
				mPaint.setColor(BG_COLOR1);
				float per1 = 0.0f;
				if(maxSleepLength > 0){
					per1 = deepHours.get(i) / maxSleepLength;
				
				if(deepHours.get(i) > 0){	
					float lineHeight1 = startY - maxLength * per1;
					canvas.drawLine(x_length * (i + 1), startY, x_length * (i + 1), lineHeight1, mPaint);
					//半圆
					RectF oval1 = new RectF();
					oval1.left = x_length * (i + 1) - 1.5f * ts; 
					oval1.right = x_length * (i + 1) + 1.5f * ts; 
					oval1.bottom = lineHeight1 + 1.5f * ts + 2 ;
					oval1.top = lineHeight1 - 1.5f * ts;
					canvas.drawArc(oval1, 180, 180, true, mPaint);
					mPaint.reset();
					mPaint.setAntiAlias(true);
				}
				
				//效率
				mPaint.setColor(getResources().getColor(R.color.ct_color));
				mPaint.setTextSize(ts);
				mPaint.setTextAlign(Paint.Align.CENTER);
				
				int xl = 0;
				try {
					 xl = (int) (new BigDecimal(Double.parseDouble(mList.get(i).getXiaolv())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
				} catch (Exception e) {
				}
				
				if(xl >= 80){
					mPaint.setColor(TEXT_COLOR1);
				}else{
					mPaint.setColor(TEXT_COLOR2);
				}
				
				canvas.drawText(xl + "%", x_length * (i + 1), 1.5f * ts, mPaint);
				mPaint.reset();
				mPaint.setAntiAlias(true);
			}
		}
	}

	private List<SleepStatusBean> mList;
	public void setData(List<SleepStatusBean> mList){
		this.mList = mList;
		this.invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getWidth();
		height = getHeight();
		ts = width /30;
	}
	
}
