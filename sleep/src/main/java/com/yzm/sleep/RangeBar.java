package com.yzm.sleep;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

import com.yzm.sleep.model.SleepStatistics;
import com.yzm.sleep.model.WeekSleepMsg;
import com.yzm.sleep.utils.HLog;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepUtils;

public class RangeBar extends View {
	Context context;
	public static final String TAG = "RangBar";
	private static final int TEXT_SIZE = 10;
	private static final int line_width = 4;
	private float mDensity;
	private int mMinVelocity;
	/**刻度间隔距离*/
	private float line_divider_y = 0;
	/**刻度长短*/
	private static final int ITEM_HEIGHT = 10;
	/**刻度精度*/
	public static final int MOD_TYPE = 10;
	/**画图标区域距离左右的距离*/
	private float marginX;
	/**画图标区域距离上下的距离*/
	private float marginY ;
	/**画图标区域宽*/
	private int width;
	/**画图标区域高*/
	private float height;
	private int mWidth, mHeight;
	/** 预设的睡眠时刻 */
	private String sleep_time_setting = "00:00";
	/** 预设起床时刻 */
	private String getup_time_setting = "00:00";
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
	/**一个小时在Y方向上占的长度*/
	private float unit_hour_length = 0;
	/**睡眠统计信息*/
	private ArrayList<SleepStatistics> statistics = new ArrayList<SleepStatistics>();
	/**周睡眠最大时长默认为10小时*/
	private float sleepHourMax = 12;
	/**一周中睡眠时刻预设值是否改变*/
	private boolean isSame;
	private WeekSleepMsg weekSleepMsg = new WeekSleepMsg();
	private float text_size_Y;
	private float text_size_X;
	private float text_up_down;
	
	
	public RangeBar(Context context) {
		super(context);
		getDensity();

		// TODO Auto-generated constructor stub
	}
	public RangeBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getDensity();
		mMinVelocity = ViewConfiguration.get(getContext())
				.getScaledMinimumFlingVelocity();
		
	}
	public RangeBar(Context context, AttributeSet attrs, int defStyle) {
		
		// TODO Auto-generated constructor stub
		this(context, attrs);
		getDensity();

//		super(context, attrs, defStyle);
	}
	
	private void getDensity(){
		mDensity = getContext().getResources().getDisplayMetrics().density;

	}
	
	
	/**
	 * 计算Y轴时间与高度的关系
	 */
	private void count_time_value1() {
		float show_hour_value = 0;// 时间在Y方向显示值
		time_value_total = 0;
		show_hour_value_sleep = (float) Math.floor(show_hour_value_sleep);
		show_hour_value_getup = (float) Math.floor(show_hour_value_getup);
		if (show_hour_value_sleep > 12) {// 开始显示时间数大于12
			show_basictime_value = 24 - show_hour_value_sleep;
			time_value_total = show_basictime_value + show_hour_value_getup;
		} else {// 开始显示时间数小于12
			time_value_total =  show_hour_value_getup - show_hour_value_sleep;
		}
		
		if(Math.floor(time_value_total) == time_value_total){
			if(Math.floor(time_value_total) % 2 == 1){
				time_value_total = (float) (Math.floor(time_value_total) + 1);
			}
		}else if(Math.floor(time_value_total) < time_value_total){
			if(Math.floor(time_value_total) % 2 == 1){
				time_value_total = (float) (Math.floor(time_value_total) + 1);
			}else if(Math.floor(time_value_total) % 2 == 0){
				time_value_total = (float) (Math.floor(time_value_total) + 2);
			}
		}
//		time_value_total = (float) (Math.floor(time_value_total) + 2);
		unit_hour_length = height / time_value_total;
		
		HLog.i(TAG,"unit_hour_length:"+ unit_hour_length+",height:"+ height);

		SleepStatistics result = null;
		boolean isNextDay = false;
		for (int i = 0,len = statistics.size(); i < len; i++) {
			result = statistics.get(i);
			if(!result.isEmpty()){
				float time_value_sleep = SleepUtils.getHours(result.sleep_time);// 实际睡觉时刻
				float time_value_getup = SleepUtils.getHours(result.getup_time);// 实际起床时刻
				
				isNextDay = false;
				if (time_value_sleep > 12) {// 显示时间数大于12小时  实际睡眠时间
					if (!isNextDay) {
						show_hour_value = show_basictime_value
								- (24 - time_value_sleep);
						result.sleep_time_value = unit_hour_length * show_hour_value;
					} else {
						show_hour_value = show_basictime_value
								+ time_value_sleep;
						result.sleep_time_value = unit_hour_length * show_hour_value;
					}
				} else {//显示时间数小于12小时
					isNextDay = true;
					show_hour_value = show_basictime_value + time_value_sleep;
					result.sleep_time_value = unit_hour_length * show_hour_value;
				}
				
				float sleep_time_value = result.sleep_time_value;
				
				isNextDay = false;
				
				if (time_value_getup > 12) {// 显示时间数大于12小时  实际起床时间
					if (!isNextDay) {
						show_hour_value = show_basictime_value
								- (24 - time_value_getup);
						result.getup_time_value = unit_hour_length * show_hour_value;
					} else {
						show_hour_value = show_basictime_value
								+ time_value_getup;
						result.getup_time_value = unit_hour_length * show_hour_value;
					}
				} else {// 显示时间数小于12小时
					isNextDay = true;
					show_hour_value = show_basictime_value + time_value_getup;
					result.getup_time_value = unit_hour_length * show_hour_value;
				}
				float getup_time_value = result.getup_time_value;
				
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		mWidth = getWidth();
		mHeight = getHeight();
		marginY = SleepUtils.dipToPx(context, 50);
		height = mHeight - marginY * 2;
		HLog.i(TAG, "mHeight = " + mHeight + ",marginY = " + marginY);
		
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		text_size_X = 18 * mDensity;
		text_size_Y = 36;//p7
		text_size_Y = (float) (13.09 * mDensity);
		text_up_down = SleepUtils.dipToPx(context, 14);
		if(statistics.size() > 0){
			if(isSame){
				count_time_value1();
				drawScaleLine(canvas);
				drawStatisticsBar(canvas);
			}else {
				drawScaleLine1(canvas);
				drawStatisticsBar1(canvas);
			}
		}
	}

	/**
	 * 画刻度线 以及说明文本
	 */
	@SuppressLint("ResourceAsColor")
	private void drawScaleLine(Canvas canvas){
		canvas.save();
		Paint linePaint = new Paint();
		linePaint.setColor(Color.parseColor("#B4B4B4"));
		linePaint.setStrokeWidth(3);
		
		Paint backPaint = new Paint();
		backPaint.setColor(Color.parseColor("#E6E6E6"));
		backPaint.setStrokeWidth(3);
		
		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(text_size_Y);
		textPaint.setColor(Color.parseColor("#B4B4B4"));
		textPaint.setTypeface(Typeface.DEFAULT); // 设置字体
		
		
		marginX = Layout.getDesiredWidth("000:00", textPaint);
		width = (int) (mWidth - marginX);
		
		//画刻度线
		float yPosition = mHeight - marginY, textWidth = Layout.getDesiredWidth("00:00", textPaint);
		line_divider_y = unit_hour_length * 2;//隔2个小时画一次时间
		line_divider_y = SleepUtils.pxToDip(context, line_divider_y);
		
		HLog.i(TAG,"line_divider_y = "+ line_divider_y);
		
		HLog.i(TAG, "time_value_total = " + time_value_total);
		
		int sleep_time_show_hour = getHourValue(show_hour_value_sleep+"");
		int time_show_hour = getHourValue(show_hour_value_sleep+"");
		for (int i = 0,len = (int) (time_value_total / 2 + 1); i < len ; i++) {
			HLog.i(TAG, " 画刻度的次数 i = " + i);
			time_show_hour = sleep_time_show_hour + i * 2;
			if(time_show_hour >= 24){
				time_show_hour = time_show_hour - 24;
			}
			//向上画
			yPosition = (mHeight - marginY) - line_divider_y * i * mDensity;
			if (yPosition >= marginY ) {
					canvas.drawLine(marginX, yPosition, mWidth, yPosition, backPaint);//画背景线
					canvas.drawLine(marginX, yPosition, marginX + ITEM_HEIGHT * mDensity,yPosition, linePaint);//画刻度
					canvas.drawText(format(time_show_hour) + ":00", (marginX - textWidth) / 2, (float) (yPosition + 4.72 * mDensity), textPaint);//画时间刻度值  p7:yPosition + 13
			}
			
			// 画右边竖线上下的短线
			if( i == 0 || i == time_value_total / 2){
				canvas.drawLine(mWidth - 4, yPosition, mWidth - 4 - ITEM_HEIGHT * mDensity, yPosition, linePaint);//画刻度
			}
		}
		
	//	canvas.drawLine(mStartX, mStartY, mStopX ,mStopY, linePaint);//画起床线

				
		linePaint.setStrokeWidth(4);//左右竖线的宽度
		canvas.drawLine(marginX, marginY / 2, marginX, mHeight - marginY / 2, linePaint);//左边竖线
		canvas.drawLine(mWidth - 4 , marginY / 2, mWidth - 4, mHeight - marginY / 2, linePaint);//右边竖线
		
		//画上上下提示文字
		String sleep = "入睡时刻";
		String wakeup = "起床时刻";
//		float text_width = Layout.getDesiredWidth(sleep, textPaint) ;
		textPaint.setTextSize(text_up_down);
		float text_width1 = textPaint.measureText(sleep);
		canvas.drawText(wakeup,marginX + width / 2 - text_width1 / 2, marginY - 10, textPaint);
		canvas.drawText(sleep,marginX + width / 2 - text_width1 / 2,mHeight - marginY / 3 * 2, textPaint);
		
		
		linePaint.setColor(Color.parseColor("#84ccc9"));
		linePaint.setStrokeWidth(5);
		float nStartX=0,nStartY=0,nStopX=0,nStopY=0;
		
		nStartX=marginX;			
		nStopX=marginX+mWidth;
		
		String  strSleepTimeString=PreManager.instance().getSleepTime_Setting(context);
		
		float time_sleep = SleepUtils.getHours(strSleepTimeString);// 实际睡觉时刻	
		
		float timeClacTime=0;
		
		if(time_sleep >sleep_time_show_hour ){
			timeClacTime  =time_sleep- sleep_time_show_hour;
		}else {
			timeClacTime  =24  +time_sleep- sleep_time_show_hour;
		}
		
		
		nStartY=nStopY=(mHeight - marginY)-((timeClacTime/2* line_divider_y *  mDensity));
	
		
		int iCalc=mWidth/20+1;
		
		for(int j=0;j<iCalc;j++)
		{			
			if(j%2==0)
			{
				nStartX=marginX+j*20;
				nStopX=nStartX+20;
				canvas.drawLine(nStartX, nStartY, nStopX ,nStopY, linePaint);//画睡觉线		
			}
	
						
		};
		
        
		float mStartX=0,mStartY=0,mStopX=0,mStopY=0;

		mStartX=marginX;			
		mStopX=marginX+mWidth;
		float timeCalc=0;

		String  strGetUPTimeString=PreManager.instance().getGetupTime_Setting(context);
		
		
		
		float time_getup = SleepUtils.getHours(strGetUPTimeString);// 
		
		
			if(sleep_time_show_hour > time_getup){
				timeCalc  =(24  - sleep_time_show_hour)+time_getup;
			}else {
				timeCalc=time_getup-sleep_time_show_hour;
			}
		
		mStartY=mStopY=(mHeight - marginY)-((timeCalc/2*line_divider_y*mDensity));
	
		int iCalc2=mWidth/20+1;
		
		for(int j2=0;j2<iCalc2;j2++)
		{			
			if(j2%2==0)
			{
				mStartX=marginX+j2*20;
				mStopX=mStartX+20;
				canvas.drawLine(mStartX, mStopY, mStopX ,mStopY, linePaint);//画睡觉线		
			}
	
						
		};
		
	
        
		canvas.restore();
	}
	/**
	 * 预设睡眠时间不同时画背景图
	 * @param canvas
	 */
	private void drawScaleLine1(Canvas canvas){
		canvas.save();
		Paint linePaint = new Paint();
		linePaint.setColor(Color.parseColor("#B4B4B4"));
		linePaint.setStrokeWidth(3);
		
		Paint backPaint = new Paint();
		backPaint.setColor(Color.parseColor("#E6E6E6"));
		backPaint.setStrokeWidth(3);
		
		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(text_size_Y);
		textPaint.setColor(Color.parseColor("#B4B4B4"));
		textPaint.setTypeface(Typeface.DEFAULT); // 设置字体
		
		
		marginX = Layout.getDesiredWidth("024h", textPaint);
		width = (int) (mWidth - marginX);
		//画刻度线
		float yPosition = mHeight - marginY;
		float iValueTotal=sleepHourMax;
		sleepHourMax = sleepHourMax + 1;
		unit_hour_length = height / sleepHourMax;
		line_divider_y = unit_hour_length * 2;//隔2个小时画一次时间
		line_divider_y = SleepUtils.pxToDip(context, line_divider_y);
		
		int n = (int) (sleepHourMax / 2 + 1);
		for (int i = 0; i < n ; i++) {
			//向上画
			yPosition = (mHeight - marginY ) - line_divider_y * i * mDensity;
			if (yPosition >= marginY ) {
					canvas.drawLine(marginX, yPosition, mWidth, yPosition, backPaint);//画背景线
					canvas.drawLine(marginX, yPosition, marginX + ITEM_HEIGHT * mDensity,yPosition, linePaint);//画刻度
					canvas.drawText(i * 2 + "h", (marginX - Layout.getDesiredWidth(i * 2 + "h", textPaint)) / 2 , (float) (yPosition + 4.72 * mDensity), textPaint);//画时间刻度值
			}
			// 画右边竖线上下的短线
			if( i == 0 || i == sleepHourMax / 2){
				canvas.drawLine(mWidth - 4, yPosition, mWidth - 4 - ITEM_HEIGHT * mDensity, yPosition, linePaint);//画刻度
			}
		}
		
		
		/*
		String strSleepTimeString = PreManager.instance().getSleepTime_Setting(
				context);

		float time_sleep = SleepUtils.getHours(strSleepTimeString);// 实际睡觉时刻

		String strGetUPTimeString = PreManager.instance().getGetupTime_Setting(
				context);

		float time_getup = SleepUtils.getHours(strGetUPTimeString);//

		float timeCalc = 0;

		if (time_sleep > time_getup) {
			timeCalc = 24 + time_getup - time_sleep;

		} else {

			timeCalc = time_getup - time_sleep;
		}

		float nStartX = 0, nStartY = 0, nStopX = 0, nStopY = 0;
		
		nStartX=marginX;			
		nStopX=mWidth;
		
		nStartY=nStopY=(mHeight - marginY)-(timeCalc*line_divider_y);	
		
		Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#84ccc9"));
		paint.setStrokeWidth(5);
        Path path = new Path();    
        path.moveTo(nStartX, nStartY);
        path.lineTo(nStopX,nStopY);     
        PathEffect effects = new DashPathEffect(new float[]{20, 10},1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
		
		*/
		
		
		linePaint.setStrokeWidth(4);//左右竖线的宽度
		canvas.drawLine(marginX, marginY / 2, marginX, mHeight-marginY / 2, linePaint);//左边竖线
		canvas.drawLine(mWidth - 4 , marginY / 2, mWidth - 4, mHeight - marginY / 2, linePaint);//右边竖线
		canvas.restore();
	}
	
	
	/**
	 * 画周一到周日的睡觉统计条形图
	 * 将图表区域(mWidth-marginX*2,mHeight-marginTop-marginBottom)
	 * x轴方向均分为24份，图表左右空白、条形图宽占2份，条形图之间空白占一份
	 */
	
	private void drawStatisticsBar(Canvas canvas){
		float unit_width =(width - line_width * 2) / 23.5F;
		float paddingX = unit_width * 2;
		float bar_width = unit_width*2;
		Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);//充满  
        paint.setColor(Color.parseColor("#FE6555"));
        paint.setAntiAlias(true);// 设置画笔的锯齿效果  
        

        /*画条形图*/
        for (int i = 0; i < 7; i++) {
        	if(!statistics.get(i).isEmpty() && statistics.size()>0){
	        	float left = marginX + paddingX + (bar_width+unit_width) * i;
	        	float top = mHeight -  marginY - statistics.get(i).getup_time_value;
	        	float right = marginX + paddingX+(bar_width+unit_width) * i+bar_width;
	        	float bottom = mHeight - marginY - statistics.get(i).sleep_time_value;
	        	RectF oval1 = new RectF(left,top , right, bottom);// 设置个新的长方形  
	        	canvas.drawRoundRect(oval1, 8 * mDensity, 4 * mDensity, paint);//第二个参数是x半径，第三个参数是y半径  
        	}
		}
        
        
        /*画星期文本*/
    	TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(text_size_X);
		textPaint.setColor(Color.parseColor("#B4B4B4"));
		textPaint.setTypeface(Typeface.DEFAULT); // 设置字体
        String[] weeks = {"一","二","三","四","五","六","日"};
        for (int i = 0; i < 7; i++) {
			canvas.drawText(weeks[i], marginX + paddingX + (bar_width + unit_width) * i, mHeight - 2, textPaint);
		}
	}
	
	
	private void drawStatisticsBar1(Canvas canvas){
		float unit_width =(width - line_width*2) / 23.5F;
		float paddingX = unit_width * 2;
		float bar_width = unit_width * 2;
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);//充满  
		paint.setColor(Color.parseColor("#FE6555"));
		paint.setAntiAlias(true);// 设置画笔的锯齿效果  
		
		
		/*画条形图*/
		for (int i = 0; i < 7; i++) {
				if(!statistics.get(i).isEmpty()){
				float left = marginX + paddingX + (bar_width+unit_width) * i;
				float top = mHeight -  marginY - statistics.get(i).getSleep_hour() * unit_hour_length;
				float right = marginX + paddingX + (bar_width+unit_width) * i+bar_width;
				float bottom = mHeight - marginY;
				RectF oval1 = new RectF(left,top , right, bottom);// 设置个新的长方形  
				canvas.drawRoundRect(oval1, 10, 10, paint);//第二个参数是x半径，第三个参数是y半径  
			}
		}
		
		
		/*画星期文本*/
		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(text_size_X);
		textPaint.setColor(Color.parseColor("#B4B4B4"));
		textPaint.setTypeface(Typeface.DEFAULT); // 设置字体
		String[] weeks = {"一","二","三","四","五","六","日"};
		for (int i = 0; i < 7; i++) {
			canvas.drawText(weeks[i], marginX+paddingX + (bar_width+unit_width) * i, mHeight - 2, textPaint);
		}
	}
	
	public int getHourValue(String s) {
		return Integer.parseInt(s.substring(0, s.indexOf(".")));
	}
	public float  getMinutValue(String s) {
		return Integer.parseInt(s.substring(s.indexOf(":") + 1)) / 60F;
	}
	public String format(int i) {
		if (i < 10) {
			return "0" + i;
		} else {
			return i + "";
		}
	}
	
	/**
	 * 设置睡眠分布数据
	 * 
	 * @param dists
	 */
	public void setWeekSleepMsg(WeekSleepMsg weekSleepMsg) {
		this.weekSleepMsg = weekSleepMsg;
		isSame = weekSleepMsg.isSame();
		sleepHourMax = weekSleepMsg.getSleepHourMax();
		statistics = weekSleepMsg.getSleepStatistics();
		SleepStatistics statistic = null;
		if(isSame){
			for(int i = 0 ;i < 7;i++){
				statistic = statistics.get(i);
				if(statistic.getSleepTimeSetting() != null && statistic.getGetupTimeSetting() != null){
					sleep_time_setting = statistics.get(i).getSleepTimeSetting();
					getup_time_setting = statistics.get(i).getGetupTimeSetting();
					break;
				}
			}
		}
		
		HLog.i(TAG, "预设睡眠时间：" + sleep_time_setting+"预设起床时间：" + getup_time_setting + "一周内预设时间是否相同：" + isSame + "睡眠最大时长：" + sleepHourMax);
		set_hour_value_sleep = SleepUtils.getHours(sleep_time_setting);
		set_hour_value_getup = SleepUtils.getHours(getup_time_setting);
		show_hour_value_sleep = set_hour_value_sleep - 1;
		if(show_hour_value_sleep < 0){
			show_hour_value_sleep = 24 + show_hour_value_sleep;
		}
		show_hour_value_getup = set_hour_value_getup + 3;
		if(show_hour_value_getup > 24){
			show_hour_value_getup = show_hour_value_getup - 24;
		}
	}
	

}
