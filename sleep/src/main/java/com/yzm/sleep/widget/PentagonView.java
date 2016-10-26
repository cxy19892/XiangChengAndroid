package com.yzm.sleep.widget;

import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.TimeFormatUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class PentagonView extends View {

	private String[] texts = {"在床时间", "赖床时间", "中途醒来", "上床时间", "入睡速度"};
	private float textSize = 30;
	
	public PentagonView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PentagonView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PentagonView(Context context) {
		super(context);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(3);
		
		float width = getWidth() - 8 * textSize;
		float height = getHeight() - 8 * textSize;
		
		canvas.drawLine(width * 1/2 + 4 * textSize,  4 * textSize, width+ 4 * textSize, height * (float)4/(float)9.25 + 4 * textSize, paint);
		canvas.drawLine(width + 4 * textSize, height * (float)4/(float)9.25 + 4 * textSize, width * 4/5 + 4 * textSize, height + 4 * textSize, paint);
		canvas.drawLine(width * 4/5 + 4 * textSize, height + 4 * textSize, width * 1/5 + 4 * textSize, height + 4 * textSize, paint);
		canvas.drawLine(width * 1/5 + 4 * textSize, height + 4 * textSize,  4 * textSize, height * (float)4/(float)9.25 + 4 * textSize, paint);
		canvas.drawLine(4 * textSize, height * (float)4/(float)9.25 + 4 * textSize, width * 1/2 + 4 * textSize, 4 * textSize, paint);
		
		paint.reset();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(6);
		canvas.drawCircle(getWidth() * 1/2, getHeight() * 1/2 + textSize, getWidth() * 1/6, paint);
		
		
		paint.reset();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.DEFAULT);
		paint.setTextSize(textSize);
		String textTemp = "";
		if(isWeekForm){
			textTemp = "平均";
			paint.setTextSize(1.5f * textSize);
			canvas.drawText("达标率", getWidth() * 1/2 - 2.2f * textSize, getHeight() * 1/2 - textSize, paint);
			paint.setTextSize(3 * textSize);
			canvas.drawText("70%", getWidth() * 1/2 - 2.5f * textSize, getHeight() * 1/2 + 3 * textSize, paint);
		}else{
			paint.setTextSize(1.3f * textSize);
			canvas.drawText("睡眠评估", getWidth() * 1/2 - 2.5f * textSize, getHeight() * 1/2 - textSize, paint);
		}
		paint.setTextSize(textSize);
		canvas.drawText(textTemp + texts[0], getWidth() * 1/2 - 2 * textSize, 2 * textSize, paint);
		canvas.drawText(textTemp + texts[1], getWidth() - 4 * textSize, getHeight() * (float)4/(float)9.25, paint);
		canvas.drawText(textTemp + texts[2], getWidth() * 4/5 - 2 * textSize, getHeight() - 4 * textSize, paint);
		if(isWeekForm)
			canvas.drawText(textTemp + texts[3], getWidth() * 1/5 - 4 * textSize, getHeight() - 4 * textSize, paint);
		else
			canvas.drawText(textTemp + texts[3], getWidth() * 1/5 - 2 * textSize, getHeight() - 4 * textSize, paint);
		if(isWeekForm)
			canvas.drawText(textTemp + texts[4], 0, getHeight() * (float)3.5/(float)9.25, paint);
		else
			canvas.drawText(textTemp + texts[4], 0, getHeight() * (float)4/(float)9.25, paint);
		
		if(dataHead == null)
			return;
		String text1 = TimeFormatUtil.minToHour2(dataHead.TotalSleepTime);
		String text2 = TimeFormatUtil.minToHour2(dataHead.AwakeNoGetUpCount);
		String text3 = dataHead.AwakeCount + "次";
		String text4 = TimeFormatUtil.formatTime1(dataHead.GoToBedTime, "HH:mm");
		String text5 = TimeFormatUtil.minToHour2(dataHead.ToSleep);
		canvas.drawText(text1 , getWidth() * 1/2 - 2 * textSize, 3.5f * textSize, paint);
		canvas.drawText(text2 , getWidth() - 4 * textSize, getHeight() * (float)4/(float)9.25 + 1.5f * textSize, paint);
		canvas.drawText(text3 , getWidth() * 4/5 - 2 * textSize, getHeight() - 2.5f * textSize, paint);
		canvas.drawText(text4 , getWidth() * 1/5 - 2 * textSize, getHeight() - 2.5f * textSize, paint);
		if(isWeekForm)
			canvas.drawText(text5 , 0, getHeight() * (float)3.5/(float)9.25 +  1.5f * textSize, paint);
		else 
			canvas.drawText(text5 , 0, getHeight() * (float)4/(float)9.25 +  1.5f * textSize, paint);
		
	}
	
	private SleepDataHead dataHead;
	/** 是否是周视图*/
	private boolean isWeekForm;
	
	/** 输入数据*/
	public void setData(SleepDataHead dataHead, boolean isWeekForm, float textSize){
		this.dataHead = dataHead;
		this.isWeekForm = isWeekForm;
		this.textSize = textSize;
		this.invalidate();
	}
	
}
