package com.yzm.sleep.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.xpillowjni.XpillowInterface;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepData;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.tools.GetModelsValueClass.ModelsValues;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.TimeFormatUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class OrangeWeekData extends View {

	private List<byte[]> dataList;
	private int textSize;
	private Paint paint;
//	private String[] times = {"22:00", "00:00", "02:00", "04:00", "06:00", "08:00", "10:00"};
	private String[] weeks = {"一", "二", "三", "四", "五", "六", "日"};
	private String sleepTime = "00:00", getUpTime = "07:00";
	private List<String> times;
	
	//预设图标颜色
	private int timeColor = 0xff999999;
	private int lineColor = 0xffc7c7cc;
	private int dashLineColor = 0x7fff5722;
	private int AlarTimeColor = 0x7fff5722;
	
	private int awakeColor = 0xffffc107;
	private int shallowColor = 0xff4caf50;
	private int deepColor = 0xff2196f3;
	
	public OrangeWeekData(Context context) {
		super(context);
		init();
	}
	
	public OrangeWeekData(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public OrangeWeekData(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init(){
		paint = new Paint();
	}
	
	/**
	 * 计算最晚入睡时间节点
	 * @param st
	 * @param inSleepTime
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	private String handleTimeLineMaxInSleep(String st, String inSleepTime){
		try {
			boolean go = true;
			while (go) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				long t1 = sdf.parse(st).getTime();
				long t2 = sdf.parse(inSleepTime).getTime();
				if(t2 < t1 && t2 > sdf.parse("10:00").getTime()){
					st = TimeFormatUtil.formatTime(t1 - 2 * 60 * 60 * 1000, "HH:mm");
				}else{
					go = false; 
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return st;
	}
	
	/**
	 * 计算最迟起床时间节点
	 * @param et
	 * @param outSleepTime
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	private String handleTimeLineMaxOutSleep(String et, String outSleepTime){
		try {
			boolean go = true;
			while (go) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				long t3 = sdf.parse(et).getTime();
				long t4 = sdf.parse(outSleepTime).getTime();
				if(t4 > t3){
					et = TimeFormatUtil.formatTime(t3 + 2 * 60 * 60 * 1000, "HH:mm");
				}else{
					go = false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return et;
	}
	
	@SuppressLint("SimpleDateFormat") @Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		int width = getWidth();
		int height = getHeight();
		
		//先获取时间轴应有的时间
		try {
			
			times = new ArrayList<String>();
			String st = "22:00";
			String et = "10:00";
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			if(dataList != null && dataList.size() > 0){
				if(earlyTime != null && lastTime != null){
					st = handleTimeLineMaxInSleep(st, earlyTime);
					et = handleTimeLineMaxOutSleep(et, lastTime);
				}else{
					for (int i = 0; i < dataList.size(); i++) {
						byte[] bfr = dataList.get(i);
						if(bfr != null && bfr.length > 0){
							SleepDataHead dataHead = BluetoothDataFormatUtil.format3(bfr, 10);
							st = handleTimeLineMaxInSleep(st, TimeFormatUtil.formatTime1(dataHead.GoToBedTime, "HH:mm"));
							et = handleTimeLineMaxOutSleep(et, TimeFormatUtil.formatTime1(dataHead.GetUpTime, "HH:mm"));
						}
					}
				}
			}
			long startTime = sdf.parse(st).getTime();
			boolean go = true;
			while(go){
				String time = TimeFormatUtil.formatTime(startTime, "HH:mm");
				times.add(time);
				if(time.equals(et) && times.size() > 6){
					go = false;
					break;
				}
				startTime += 2 * 60 * 60 * 1000;
			}
			
		} catch (Exception e) {
		}
		

		
		//画左边时间轴
		paint.setColor(timeColor);
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setTextSize(textSize);
		int h1 = height/times.size();
		for (int i = 0; i < times.size(); i++) {
			canvas.drawText(times.get(i), 0, height - (h1 * i + textSize), paint);
		}
		
		//画左边的实线
		paint.reset();
		paint.setColor(lineColor);
		paint.setStrokeWidth(1);
		canvas.drawLine(3 * textSize, height - textSize, 3 * textSize, 0, paint);
		
		//画周一到周日
		paint.reset();
		paint.setColor(timeColor);
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setTextSize(textSize);
		int w1 = (width - 4 * textSize) / (weeks.length);
		for (int i = 0; i < weeks.length; i++) {
			canvas.drawText(weeks[i], 4 * textSize + w1 * i, height - 3, paint);
		}
		
		
		//画每天的数据
		if(dataList != null && dataList.size() > 0){
			try {
				boolean noData = true;
				for (int i = 0; i < dataList.size(); i++) {
					byte[] bfr1 = dataList.get(i);
					if(bfr1 != null && bfr1.length > 0){
						noData = false;
						SleepDataHead dataHead1 = BluetoothDataFormatUtil.format3(bfr1, 10);
						String goToBedTime = TimeFormatUtil.formatTime1(dataHead1.GoToBedTime, "HH:mm");
						String getUpTime = TimeFormatUtil.formatTime1(dataHead1.GetUpTime, "HH:mm");
						float hh1 = getIntervelTime(goToBedTime, getUpTime);
						int intervelPix = (int)(h1 * (hh1/2)) ;
						
						byte[] bfr = dataList.get(i);
					
						SleepDataHead dataHead = BluetoothDataFormatUtil.format3(bfr, intervelPix);
						List<SleepData> list = dataHead.m_pValue;
						
						float hh2 = getIntervelTime(times.get(0), goToBedTime);
						int lineY = (int) (height - (h1 * hh2/2 + textSize));
						for (int j = 0; j < list.size(); j++) {
							paint.reset();
							paint.setStrokeWidth(1);
//							（ 1深度 2浅度 3清醒）
							switch (list.get(j).SleepType) {
							case 1:
								paint.setColor(deepColor);
								break;
							case 2:
								paint.setColor(shallowColor);
								break;
							case 3:
								paint.setColor(awakeColor);
								break;
							default:
								paint.setColor(awakeColor);
								break;
							}
							canvas.drawLine(4 * textSize + w1 * i, lineY - j, 4 * textSize + w1 * i + textSize, lineY - j, paint);
						}
					
					}
				}
				if(noData){
					paint.reset();
					paint.setColor(lineColor);
					paint.setStrokeWidth(1);
					for (int i = 0; i < times.size(); i++) {
						canvas.drawLine(3 * textSize, height - (h1 * i + textSize) - textSize * 4/10, width - 3 * textSize, height - (h1 * i + textSize) - textSize * 4/10, paint);
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else{
			paint.reset();
			paint.setColor(lineColor);
			paint.setStrokeWidth(1);
			for (int i = 0; i < times.size(); i++) {
				canvas.drawLine(3 * textSize, height - (h1 * i + textSize) - textSize * 4/10, width - 3 * textSize, height - (h1 * i + textSize) - textSize * 4/10, paint);
			}
		}
		
		//画目标睡觉起床虚线
		paint.reset();
		paint.setColor(dashLineColor);
		paint.setStrokeWidth(1);
		float hour1 = getIntervelTime(times.get(0), sleepTime);
		int dashLineY1 = (int) (height - (h1 * hour1/2 + textSize));
		drawDashLine(canvas, paint, 3 * textSize, width - 3 * textSize, dashLineY1 - textSize * 4/10);
		float hour2 = getIntervelTime(times.get(0), getUpTime);
		int dashLineY2 = (int) (height - (h1 * hour2/2 + textSize));
		drawDashLine(canvas, paint, 3 * textSize, width - 3 * textSize, dashLineY2 - textSize * 4/10);
		paint.reset();
		paint.setColor(AlarTimeColor);
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setTextSize(textSize * 3/4);
		canvas.drawText(sleepTime, width - 2f * textSize, dashLineY1 - textSize * 2/10, paint);
		canvas.drawText(getUpTime, width - 2f * textSize, dashLineY2 - textSize * 2/10, paint);
		
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		int width = right - left;
		textSize = width / 24;
		
	}

	/**
	 *  设置画图数据
	 * @param weekData
	 */
	public void setData(List<byte[]> dataList){
		this.dataList = dataList;
		this.invalidate();
	}
	
	private String earlyTime, lastTime;
	public void setStartTimeAndEndTime(String earlyTime, String lastTime){
		this.earlyTime = earlyTime;
		this.lastTime = lastTime;
	}
	
	/**
	 * 设置最早入睡和最迟起床时间
	 * @param sleepTime
	 * @param getUpTime
	 */
	public void setTargetTime(String sleepTime, String getUpTime){
		this.sleepTime = sleepTime;
		this.getUpTime = getUpTime;
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
	 * 获取间隔时间
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
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
