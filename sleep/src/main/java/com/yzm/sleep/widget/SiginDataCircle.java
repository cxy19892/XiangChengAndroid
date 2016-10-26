package com.yzm.sleep.widget;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yzm.sleep.R;
import com.yzm.sleep.model.ModifySignInResult.Report;
import com.yzm.sleep.render.ShareDataBean;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.TimeFormatUtil;

public class SiginDataCircle extends View{

	private Paint mPaint;
	private RectF oval, oval1, oval2, oval3;
	private float textSize = 20;
	private final int BTN_COLOR = 0xff6f9ef6;
	private final int CIR_COLOR_OUTER = 0xff9fa5b7;//ff717486;中间环形的底色
	private final int CIR_COLOR_MIDLE = 0xff9fa5b7;//ff717486;外部圆环的底色
	private final int CIR_COLOR_INNER = 0xff9fa5b7;//ff717486;内部圆环的底色
	private boolean isBindPillow = false;
	private boolean isCrop = false;
//	private boolean isEvaluated = true;
	
	public SiginDataCircle(Context context) {
		super(context);
		settingSleepTime = PreManager.instance().getSleepTime_Setting(context);
		settingGetUpTime = PreManager.instance().getGetupTime_Setting(context);
		initView();
	}
	
	public SiginDataCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		settingSleepTime = PreManager.instance().getSleepTime_Setting(context);
		settingGetUpTime = PreManager.instance().getGetupTime_Setting(context);
		initView();
	}
	
	public SiginDataCircle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		settingSleepTime = PreManager.instance().getSleepTime_Setting(context);
		settingGetUpTime = PreManager.instance().getGetupTime_Setting(context);
		initView();
	}

	
	/** 闹钟入睡时间*/
	private String alarmSleepTime;
	/** 闹钟起床时间*/
	private String alarmGetUpTime;
	/** 日数据*/
	private Report ReportData;
	/** 改变入睡和起床时间的监听*/
	private OnClickChangeTime onClickChangeTime;
	/** 是否同步数据*/
	private boolean isSync = false;
	
	private String settingSleepTime, settingGetUpTime;
	
	public void initView(){
		//以下2个mPaint是为了适配三星机型
		mPaint = new Paint();
		oval = new RectF();
		oval1 = new RectF(); 
		oval2 = new RectF(); 
		oval3 = new RectF(); 
		
	}
	
	/**
	 * 设置是否绑定枕头
	 * @param isBindPillow
	 */
	public void setIsBindPillow(boolean isBindPillow){
		this.isBindPillow = isBindPillow;
	}
	
	/**
	 * 设置数据是否同步
	 * @param isSync
	 */
	public void setDataIsSync(boolean isSync){
		this.isSync = isSync;
		this.invalidate();
	}
	
//	public void setEvaluatedSleep(boolean isEvaluated){
//		this.isEvaluated = isEvaluated;
//	}
	
	private String perStr = "睡眠效率-- --";
	private String sleepLenth = "0小时00分钟";
	private String isPerReach = "增加数据算效率";
	
	public void setData(String alarmSleepTime, String alarmGetUpTime, Report dayData){
		this.alarmSleepTime = alarmSleepTime;
		this.alarmGetUpTime = alarmGetUpTime;
		this.ReportData = dayData;
		float efficient = 0f;
		if(dayData != null){
			if(dayData.getSleeplong() <= 0){
				sleepLenth = "0小时00分钟";
			}else{
				int hour = (int) dayData.getSleeplong() / 60;
				int min = (int) dayData.getSleeplong() % 60;
				sleepLenth = hour+"小时" + (min >= 10 ? min : ("0"+min)) +"分钟";
			}
//			float efficient = 0f;
			try {
				efficient = Float.parseFloat(dayData.getXiaolv());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			float myEffic = new BigDecimal(efficient).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			if(myEffic == 0){
				perStr = "睡眠效率-- --";
				isPerReach = "增加数据算效率";
			}else{
				if(myEffic >= 0.85){
					isPerReach = "效率达标";
				}else{
					isPerReach = "效率未达标";
				}
				perStr = "睡眠效率" + new BigDecimal(efficient*100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + "%";//睡眠效率直接显示了，环使用睡眠时长画
			}
			
		}
		
		
		if(!isCrop){
			final float per = new BigDecimal(efficient).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();//getRealTime(dayData);
			if(per == 0){
				crrenDegrees = (int) (270 * per);
				this.invalidate();
				return;
			}
			this.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int ds = (int) (270 * per);
					if(ds == 0){
						crrenDegrees = ds;
						SiginDataCircle.this.invalidate();
					}else
						setDegrees(ds);
				}
			}, 500);
			
		}else{
			float per = getRealTime(dayData);
			crrenDegrees = (int) (270 * per);
			this.invalidate();
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		int width = right - left;
		
		textSize = width / 24;
		
		oval.left = width * 1/6;
		oval.right = width * 5/6;
		oval.top = width * 1/6;
		oval.bottom = width * 5/6;
		
		float translant1 = 0.6f * textSize;
		oval1.left = width * 1/6 - translant1;
		oval1.right = width * 5/6 + translant1;
		oval1.top = width * 1/6 - translant1;
		oval1.bottom = width * 5/6 + translant1;
		
		float translant2 = 0.9f * textSize;
		oval2.left = width * 1/6 + translant2;
		oval2.right = width * 5/6 - translant2;
		oval2.top = width * 1/6 + translant2;
		oval2.bottom = width * 5/6 - translant2;
		
		float translant3 = 1.2f * textSize;
		oval3.left = width * 1/6 + translant3;
		oval3.right = width * 5/6 - translant3;
		oval3.top = width * 1/6 + translant3;
		oval3.bottom = width * 5/6 - translant3;
	}

	@SuppressLint({ "DrawAllocation", "SimpleDateFormat" }) 
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//先画底色
		mPaint.reset();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(CIR_COLOR_OUTER);
		mPaint.setStrokeWidth(textSize * 1/2);
		mPaint.setAntiAlias(true);
	    canvas.drawArc(oval, 135, 270, false, mPaint);//小弧形  
	    
	    mPaint.reset();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(CIR_COLOR_MIDLE);
		mPaint.setStrokeWidth(textSize * 1/2 * 2/9);
		mPaint.setAntiAlias(true);
	    canvas.drawArc(oval1, 135, 270, false, mPaint);//小弧形  
	    
	    mPaint.reset();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(CIR_COLOR_INNER);
		mPaint.setStrokeWidth(textSize * 1/2 * 1/9);
		mPaint.setAntiAlias(true);
	    canvas.drawArc(oval2, 135, 270, false, mPaint);//小弧形  
	    
	    int color1 = 0xff2d3b8b;//0xff24243f;
	    int color2 = CIR_COLOR_OUTER;//0xff40415A;
	    int color3 = 0xff2d3b8b;//0xff30304c;
	    mPaint.reset();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(color2);
		mPaint.setStrokeWidth(textSize * 1/2 * 6/9);
		mPaint.setAntiAlias(true);
	    canvas.drawArc(oval3, 135, 270, false, mPaint);//小弧形  
	    
	    mPaint.reset();
		mPaint.setStyle(Paint.Style.STROKE);
		if(isCrop)
	    	mPaint.setColor(color3);
		else
			mPaint.setColor(color1);
		mPaint.setStrokeWidth(textSize * 1/2);
		mPaint.setAntiAlias(true);
	    for (int i = 0; i < 11; i++) {
	    	canvas.drawArc(oval3, 135 + (26f * i) + i - 13, 26f, false, mPaint);//小弧形  
		}
	    
	    
	    mPaint.reset();
	    mPaint.setAntiAlias(true);
	    mPaint.setTextSize(textSize);
	    mPaint.setColor(0xff888888);
	    String sleepTimeString = "00:00";
	    String getUpTimeString = "00:00";
	    if(ReportData != null && !TextUtils.isEmpty(ReportData.getSleep()) && !TextUtils.isEmpty(ReportData.getWakeup())){
	    	long sleeplong= 0l;
	    	long wakelong = 0l;
	    	try {
				sleeplong = Long.parseLong(ReportData.getSleep());
				wakelong  = Long.parseLong(ReportData.getWakeup());
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	sleepTimeString = (ReportData.getSleep() == null ? "00:00" : TimeFormatUtil.formatTime1(sleeplong, "HH:mm"));
	    	getUpTimeString = (ReportData.getWakeup() == null ? "00:00" : TimeFormatUtil.formatTime1(wakelong, "HH:mm"));
	    }else{
	    	sleepTimeString = "00:00";
	    	getUpTimeString = "00:00";
	    }
	    sleepTimeString = (TextUtils.isEmpty(sleepTimeString) ? "00:00" : sleepTimeString);
	    getUpTimeString = (TextUtils.isEmpty(getUpTimeString) ? "00:00" : getUpTimeString);
	    canvas.drawText(sleepTimeString, oval2.left - 2.6f * textSize, oval2.bottom - 1.0f * textSize, mPaint);
//	    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.yueliang), oval2.left - 3.6f * textSize, oval2.bottom - 1.8f * textSize, mPaint);
	    canvas.drawBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.yueliang),(int)textSize,(int)textSize), oval2.left - 3.6f * textSize, oval2.bottom - 1.8f * textSize, mPaint);
	    
	    canvas.drawText(getUpTimeString, oval2.right , oval2.bottom - 1.0f * textSize, mPaint);
//	    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.taiyang), oval2.right + 2.6f * textSize, oval2.bottom - 1.8f * textSize, mPaint);
	    canvas.drawBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.taiyang),(int)textSize,(int)textSize), oval2.right + 2.6f * textSize, oval2.bottom - 1.8f * textSize, mPaint);
	    
	    
	    if(isBindPillow){ //绑定了硬件，还未同步数据
	    	if(!isSync){
	    		mPaint.reset();
		    	mPaint.setAntiAlias(true);
		    	float width = oval.right - oval.left;
			    canvas.drawBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.icon_tongbu)), oval.left + (int)(width * 0.3), oval.top + (int)(width * 0.3), mPaint);
	    	}else{
	    		mPaint.reset();
		    	mPaint.setAntiAlias(true);
		    	mPaint.setTextSize(1.4f * textSize);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
		    	mPaint.setColor(getResources().getColor(R.color.fct_color));
			    canvas.drawText(sleepLenth, oval.left+(oval.right - oval.left) * 1/2, oval.top + (oval.bottom - oval.top) * 1/3 + textSize, mPaint);
			    
			    mPaint.setColor(getResources().getColor(R.color.white));
			    mPaint.setTextSize(1.8f * textSize);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
		    	canvas.drawText(perStr, oval.left + (oval.right - oval.left) * 1/2, oval.top + (oval.bottom - oval.top) * 1/2 + textSize, mPaint);
			    
		    	mPaint.setColor(getResources().getColor(R.color.fct_color));
			    mPaint.setTextSize(1.2f * textSize);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
		    	canvas.drawText(isPerReach, oval.left + (oval.right - oval.left) * 1/2 - textSize/2 , oval.top + (oval.bottom - oval.top) * 3/5 + (1.4f * textSize), mPaint);
//		    	canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.explain_icon),
//		    			oval.left + (oval.right - oval.left) * 1/2 - textSize/2 + isPerReach.length() * 0.6f *textSize , oval.top + (oval.bottom - oval.top) * 3/5 + 0.5f * textSize, mPaint);
		    	canvas.drawBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.explain_icon),(int)textSize, (int)textSize), oval.left + (oval.right - oval.left) * 1/2 - textSize/4 + isPerReach.length() * 0.6f *textSize , oval.top + (oval.bottom - oval.top) * 3/5 + 0.5f * textSize, mPaint);
		    	
	    	}
	    }else{  //未绑定硬件
	    	
		    if(ReportData != null && !TextUtils.isEmpty(ReportData.getSleep()) && !TextUtils.isEmpty(ReportData.getWakeup())){
		    	mPaint.reset();
		    	mPaint.setAntiAlias(true);
		    	mPaint.setTextSize(1.4f * textSize);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
		    	mPaint.setColor(getResources().getColor(R.color.fct_color));
			    canvas.drawText(sleepLenth, oval.left+(oval.right - oval.left) * 1/2 , oval.top + (oval.bottom - oval.top) * 1/3 + textSize, mPaint);
			    
			    mPaint.setColor(getResources().getColor(R.color.white));
			    mPaint.setTextSize(1.8f * textSize);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
		    	canvas.drawText(perStr, oval.left + (oval.right - oval.left) * 1/2, oval.top + (oval.bottom - oval.top) * 1/2 + textSize, mPaint);

			    mPaint.setColor(getResources().getColor(R.color.fct_color));
			    mPaint.setTextSize(1.2f * textSize);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
		    	canvas.drawText(isPerReach, oval.left + (oval.right - oval.left) * 1/2 - textSize/2 , oval.top + (oval.bottom - oval.top) * 3/5 + (1.4f * textSize), mPaint);
//		    	canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.explain_icon),
//		    			oval.left + (oval.right - oval.left) * 1/2 - textSize/2 + isPerReach.length() * 0.6f *textSize , oval.top + (oval.bottom - oval.top) * 3/5 + 0.5f * textSize, mPaint);
		    	canvas.drawBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.explain_icon),(int) textSize, (int)textSize), oval.left + (oval.right - oval.left) * 1/2 - textSize/4 + isPerReach.length() * 0.6f *textSize , oval.top + (oval.bottom - oval.top) * 3/5 + 0.5f * textSize, mPaint);
			    
		    }else{
		    	mPaint.reset();
		    	mPaint.setAntiAlias(true);
		    	float width = oval.right - oval.left;
		    	canvas.drawBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.icon_qiandao)), oval.left + (int)(width * 0.3), oval.top + (int)(width * 0.3), mPaint);
		    }
	    }
	    
	    	
	  //最后画睡眠度圆弧
	    float cx = oval.left + (oval.right - oval.left)/2;
		float cy = oval.top + (oval.bottom - oval.top)/2;
		Shader shader = new SweepGradient(cx, cy, new int[]{0xff285aa3, 0xff5fd0e0}, new float[]{0.15f, 0.50f});
		Matrix matrix = new Matrix();
        matrix.setRotate(135, cx, cy);
        shader.setLocalMatrix(matrix);

        
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(textSize * 1/2);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setShader(shader);
		canvas.drawArc(oval, 135, crrenDegrees, false, mPaint);//小弧形 
		mPaint.setShader(null);
		
		//适配机型，有些机型，设置了shader就不出现圆头
		if(crrenDegrees > 1){
			mPaint.reset();
	        mPaint.setStyle(Paint.Style.STROKE);
	        mPaint.setStrokeWidth(textSize * 1/2);
	        mPaint.setAntiAlias(true);
	        mPaint.setStrokeCap(Paint.Cap.ROUND);
	        mPaint.setColor(0xff285aa3);
			canvas.drawArc(oval, 135, 1, false, mPaint);//小弧形 
			mPaint.setShader(null);
		}
		if(crrenDegrees == 270){
			mPaint.reset();
	        mPaint.setStyle(Paint.Style.STROKE);
	        mPaint.setStrokeWidth(textSize * 1/2);
	        mPaint.setAntiAlias(true);
	        mPaint.setStrokeCap(Paint.Cap.ROUND);
	        mPaint.setColor(0xff5fd0e0);
			canvas.drawArc(oval, 45, 1, false, mPaint);//小弧形 
			mPaint.setShader(null);
		}
			
	}
	
	/**
	 * 初始角度
	 */
	private int crrenDegrees;

	private void setDegrees(int degrees){
		ObjectAnimator anim = ObjectAnimator.ofFloat(this, "alpha", 0f, degrees);
		anim.setDuration(600);
		anim.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				crrenDegrees = 0;
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		anim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator value) {
				// TODO Auto-generated method stub
				float f = (Float) value.getAnimatedValue();
				crrenDegrees = (int) f;
				invalidate();
			}
		});
		anim.start();
	}
	
	
	/**
	 * 获取分享的数据
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public ShareDataBean getShareData(){
		String sleepTimeString = "00:00";
	    String getUpTimeString = "00:00";
	    String dateString = "";
	    if(ReportData != null){
	    	if(ReportData != null && !TextUtils.isEmpty(ReportData.getSleep()) && !TextUtils.isEmpty(ReportData.getWakeup())){
	    		long sleeplong= 0l;
		    	long wakelong = 0l;
		    	try {
					sleeplong = Long.parseLong(ReportData.getSleep());
					wakelong  = Long.parseLong(ReportData.getWakeup());
					sleepTimeString = TimeFormatUtil.formatTime1(sleeplong, "HH:mm");
			    	getUpTimeString = TimeFormatUtil.formatTime1(wakelong, "HH:mm");
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
	    	}
	    		dateString = "昨晚";
	    }else{
	    	sleepTimeString = "--:--";
	    	getUpTimeString = "--:--";
	    }
	    sleepTimeString = (TextUtils.isEmpty(sleepTimeString) ? "--:--" : sleepTimeString);
	    getUpTimeString = (TextUtils.isEmpty(getUpTimeString) ? "--:--" : getUpTimeString);
	    if(sleepTimeString.equals("--:--"))
	    	return null;
    	else {
    		ShareDataBean bean = new ShareDataBean();
    		bean.setSleepTime(sleepTimeString);
    		bean.setGetUpTime(getUpTimeString);
    		bean.setDate(TimeFormatUtil.getTodayTime());
    		bean.setSleepLength(ReportData.getSleeplong()/60+":"+ReportData.getSleeplong() % 60);
    		bean.setTargetSleepLength(getPlantTime(alarmSleepTime, alarmGetUpTime));
    		bean.setBeautyTime("");
    		return bean;
		}
	    
	}
	
	public void crop(boolean isCrop){
		this.isCrop = isCrop;
	}
	
	private float downX, downY;
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();
			break;

		case MotionEvent.ACTION_UP:
			downX = event.getX();
			downY = event.getY();
			if(onClickChangeTime == null)
				return true;
			if(downY > (oval.top + (oval.bottom - oval.top)*0.3) && downY < (oval.bottom - (oval.bottom - oval.top)*0.3)){
				if(downX > (oval.left + (oval.right - oval.left)*0.3) && downX < (oval.right - (oval.right - oval.left)*0.3)){
//					if(isEvaluated){
//						onClickChangeTime.onEvaluatedSleep();
//					}else{
						if(isBindPillow){
							if(!isSync)
								onClickChangeTime.onClickSyncData();
							else{
								if(downX > oval.left + (oval.right - oval.left) * 1/5 && downX < oval.right - (oval.right - oval.left) * 1/5 && 
										downY > oval.top + (oval.bottom - oval.top) * 3/5 && downY < oval.top + (oval.bottom - oval.top) * 4/5 + textSize){
									onClickChangeTime.onClickDialog();
								}else
								onClickChangeTime.onClickShowDaySleep();
							}
						}else{
							if (!isSync) 
								onClickChangeTime.onClickPunch();
							else{
								if(downX > oval.left + (oval.right - oval.left) * 1/5 && downX < oval.right - (oval.right - oval.left) * 1/5 && 
										downY > oval.top + (oval.bottom - oval.top) * 3/5 && downY < oval.top + (oval.bottom - oval.top) * 4/5 + textSize){
									onClickChangeTime.onClickDialog();
								}else
									onClickChangeTime.onClickShowDaySleep();
							}
					}
				}else{
					onClickChangeTime.onClickShowDaySleep();
				}
			}else if(downX > oval.left + (oval.right - oval.left) * 1/5 && downX < oval.right - (oval.right - oval.left) * 1/5 && 
					downY > oval.top + (oval.bottom - oval.top) * 3/5 && downY < oval.top + (oval.bottom - oval.top) * 4/5 + textSize){
				if(isBindPillow){
					if(!isSync){
						onClickChangeTime.onClickShowDaySleep();
					}else{
						onClickChangeTime.onClickDialog();
					}
				}else{
					if(ReportData != null && !TextUtils.isEmpty(ReportData.getSleep()) && !TextUtils.isEmpty(ReportData.getWakeup())){
						onClickChangeTime.onClickDialog();
					}else{
						onClickChangeTime.onClickShowDaySleep();
					}
				}
			}else {
				onClickChangeTime.onClickShowDaySleep();
			}
			downX = 0;
			downY = 0;
			break;
			
		default:
			break;
		}
		return true;
	}

	/**
	 * 获取目标睡眠时间
	 * @param sleepTime
	 * @param getUpTime
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	private String getPlantTime(String alarmSleepTime, String alarmGetUpTime){
		if(TextUtils.isEmpty(alarmSleepTime) || TextUtils.isEmpty(alarmGetUpTime)){
			alarmSleepTime = settingSleepTime;
			alarmGetUpTime = settingGetUpTime;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date d1 = sdf.parse(alarmSleepTime);
			Date d2 = sdf.parse(alarmGetUpTime);
			double hour = (float)(d2.getTime() - d1.getTime())/(1000 * 60 * 60);
			DecimalFormat df = new DecimalFormat("#.#");  
			if(hour < 0)
				hour += 24;
			return df.format(hour);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 获取实际睡眠长度(返回的值为度数)
	 * @param dayData
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	private float getRealTime(Report dayData){
		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//			Date d1 = sdf.parse(dayData.getWakeup());
//			Date d2 = sdf.parse(dayData.getSleep());
			double hour = ((double)dayData.getSleeplong())/60;//(float)(d1.getTime() - d2.getTime())/(1000 * 60 * 60);
			if(hour < 0)
				hour += 24;
			double totalHourLength = Double.parseDouble(getPlantTime(alarmSleepTime, alarmGetUpTime));
			float per = (float) (hour / totalHourLength);
			if(per > 1)
				per = 1.0f;
			return per;
		} catch (Exception e) {
		}
		return 0.0f;
	}
	
	/**
	 * 获取睡眠时间长度(返回HH:mm时间格式)
	 * @param dayData
	 * @return
	 */
//	@SuppressLint("SimpleDateFormat") 
//	private String getRealTimeString(Report dayData){
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//			Date d1 = sdf.parse(dayData.getGetUpTime());
//			Date d2 = sdf.parse(dayData.getSleepTime());
//			float hour = (float)(d1.getTime() - d2.getTime())/(1000 * 60 * 60);
//			if(hour < 0)
//				hour += 24;
//			int h = (int)hour;
//			float mm = (60 * (hour - h));
//			int m = Integer.parseInt(new BigDecimal(mm).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
//			String hourString = "00";
//			String minString = "00";
//			if(h < 10)
//				hourString = "0" + String.valueOf(h);
//			else
//				hourString = String.valueOf(h);
//			if(m < 10)
//				minString = "0" + String.valueOf(m);
//			else 
//				minString = String.valueOf(m);
//			return hourString + ":" + minString;
//		} catch (Exception e) {
//		}
//		return "00:00";
//	}
	
//	@SuppressLint("SimpleDateFormat") 
//	private String getRealTimeString2(SoftDayData dayData){
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//			Date d1 = sdf.parse(dayData.getGetUpTime());
//			Date d2 = sdf.parse(dayData.getSleepTime());
//			double hour = (float)(d1.getTime() - d2.getTime())/(1000 * 60 * 60);
//			if(hour < 0)
//				hour += 24;
//			return String.valueOf(hour);
//		} catch (Exception e) {
//		}
//		return "0";
//	}
	
	/**
	 *  根据屏幕大小从资源获取相应的bitmap
	 * @param drawable
	 * @return
	 */
	private Bitmap drawableToBitmap(Drawable drawable) {  
        // 取 drawable 的颜色格式  
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                : Bitmap.Config.RGB_565;  
        // 建立对应 bitmap  
        Bitmap bitmap = Bitmap.createBitmap((int)(oval.right - oval.left)/5*2, (int)(oval.right - oval.left)/5*2, config);  
        // 建立对应 bitmap 的画布  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, (int)(oval.right - oval.left)/5*2, (int)(oval.right - oval.left)/5*2);  
        // 把 drawable 内容画到画布中  
        drawable.draw(canvas);  
        return bitmap;  
    }  
	
	private Bitmap drawableToBitmap(Drawable drawable, int with, int hight) {  
        // 取 drawable 的颜色格式  
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                : Bitmap.Config.RGB_565;  
        // 建立对应 bitmap  
        Bitmap bitmap = Bitmap.createBitmap((int)with, (int)hight, config);  
        // 建立对应 bitmap 的画布  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, (int)with, (int)hight);  
        // 把 drawable 内容画到画布中  
        drawable.draw(canvas);  
        return bitmap;  
    }
	
	public void setOnClickChangeTime(OnClickChangeTime onClickChangeTime){
		this.onClickChangeTime = onClickChangeTime;
	}
	
	public interface OnClickChangeTime{
		void onClickSyncData();
		void onClickPunch();
		void onClickShowDaySleep();
		void onEvaluatedSleep();
		void onClickDialog();
	}
}
