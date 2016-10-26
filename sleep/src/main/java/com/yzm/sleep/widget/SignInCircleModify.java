package com.yzm.sleep.widget;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.render.ShareDataBean;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;

public class SignInCircleModify extends View{

	private Paint mPaint;
	private RectF oval, oval1, oval2, oval3;
	private float textSize = 20;
	private final int BTN_COLOR = 0xff8745ff;
	private boolean isBindPillow = false;
	private boolean isCrop = false;
	private boolean isEvaluated = false;
	
	public SignInCircleModify(Context context) {
		super(context);
		initView();
	}
	
	public SignInCircleModify(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public SignInCircleModify(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	
	/** 改变入睡和起床时间的监听*/
	private OnClickChangeTime onClickChangeTime;
	/** 是否同步数据*/
	private boolean isSync = false;
	
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
	
	/**
	 * 是否有进行睡眠评估
	 * @param isEvaluated
	 */
	public void setEvaluatedSleep(boolean isEvaluated){
		this.isEvaluated = isEvaluated;
	}
	
	private String sleepTimeStr, wakeTimeStr, dateStr;
	private long sleepTimelong = 0l;
	private String perStr = "睡眠效率：--";
	private String sleepLenth = "--小时--分";
	
	@SuppressLint("SimpleDateFormat") 
	public void setData(long sleepTime, long wakeTime, int sleeplong, String dateStr, final float per){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		this.dateStr = dateStr;
		try {
			if(sleepTime != 0 && wakeTime != 0){
				this.sleepTimeStr = sdf.format(new Date(sleepTime));
				this.wakeTimeStr = sdf.format(new Date(wakeTime));
				sleepTimelong = sleeplong;
				if(sleepTimelong<=0){
					sleepLenth = "--小时--分";
				}else{
					int hour = (int) sleepTimelong/60;
					int min = (int) sleepTimelong% 60;
					sleepLenth = hour+"小时" + (min >= 10 ? min : ("0"+min)) +"分";
				}
			}else{
				this.sleepTimeStr = null;
				this.wakeTimeStr = null;
				this.sleepLenth = "--小时--分";
				this.sleepTimelong = 0l;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(per == 0){
			perStr = "睡眠效率：--%";
		}else
			perStr = "睡眠效率：" + new BigDecimal(per*100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + "%";//睡眠效率直接显示了，环使用睡眠时长画
		//newPer == 睡眠时长/预定睡眠时长 
		final float newPer = new BigDecimal(per).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		if(!isCrop){
			this.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(per == 0){
						if(anim != null){
							anim.cancel();
						}
						crrenDegrees = (int) (270 * newPer);
						SignInCircleModify.this.invalidate();
					}else{
						int ds = (int) (270 * newPer);
						setDegrees(ds);
					}
				}
			}, 100);
			
		}else{
			crrenDegrees = (int) (270 * newPer);
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
		mPaint.setColor(0xff484A5E);
		mPaint.setStrokeWidth(textSize * 1/2);
		mPaint.setAntiAlias(true);
	    canvas.drawArc(oval, 135, 270, false, mPaint);//小弧形  
	    
	    mPaint.reset();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(0xff717486);
		mPaint.setStrokeWidth(textSize * 1/2 * 2/9);
		mPaint.setAntiAlias(true);
	    canvas.drawArc(oval1, 135, 270, false, mPaint);//小弧形  
	    
	    mPaint.reset();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(0xff40415A);
		mPaint.setStrokeWidth(textSize * 1/2 * 1/9);
		mPaint.setAntiAlias(true);
	    canvas.drawArc(oval2, 135, 270, false, mPaint);//小弧形  
	    
	    int color1 = 0xff24243f;
	    int color2 = 0xff40415A;
	    int color3 = 0xff30304c;
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
	    if(TextUtils.isEmpty(sleepTimeStr) && TextUtils.isEmpty(wakeTimeStr)){
		    canvas.drawText("00:00", oval2.left - 2.6f * textSize, oval2.bottom - 1.0f * textSize, mPaint);
		    canvas.drawText("00:00", oval2.right , oval2.bottom - 1.0f * textSize, mPaint);
	    }else{
	    	canvas.drawText(sleepTimeStr, oval2.left - 2.6f * textSize, oval2.bottom - 1.0f * textSize, mPaint);
		    canvas.drawText(wakeTimeStr, oval2.right , oval2.bottom - 1.0f * textSize, mPaint);
	    }
	    
	    
	    //是否睡眠评估，是的话，进入睡眠评估
	    if(isEvaluated){
	    	mPaint.reset();
	    	mPaint.setAntiAlias(true);
	    	float width = oval.right - oval.left;
	    	float height = oval.bottom - oval.top;
	    	RectF rect = new RectF();
	    	rect.set(oval.left + width/2 - 4.5f * textSize, oval.top + height/2 - 1.5f * textSize, oval.left + width/2 + 4.5f * textSize, oval.top + height/2 + 1.5f * textSize);
	    	mPaint.setColor(BTN_COLOR);
		    mPaint.setStrokeWidth(textSize * 2.5f);
		    mPaint.setStrokeCap(Paint.Cap.ROUND);  
		    canvas.drawRoundRect(rect, 5, 5, mPaint);
	    	
	    	mPaint.setColor(0xffffffff);
	    	mPaint.setTextAlign(Paint.Align.CENTER);
		    mPaint.setTextSize(textSize * 1.8f);
		    canvas.drawText("睡眠评估", (oval.right - oval.left) * 1/2 + oval.left, oval.top + height/2 + 0.6f * textSize, mPaint);
		    
		    return;
	    }
	    
	    
	    if(isBindPillow){ //绑定了硬件，还未同步数据
	    	if(!isSync){
	    		mPaint.reset();
		    	mPaint.setAntiAlias(true);
		    	float width = oval.right - oval.left;
		    	float height = oval.bottom - oval.top;
		    	RectF rect = new RectF();
		    	rect.set(oval.left + width/2 - 4.5f * textSize, oval.top + height/2 - 1.5f * textSize, oval.left + width/2 + 4.5f * textSize, oval.top + height/2 + 1.5f * textSize);
		    	mPaint.setColor(BTN_COLOR);
			    mPaint.setStrokeWidth(textSize * 2.5f);
			    mPaint.setStrokeCap(Paint.Cap.ROUND);  
			    canvas.drawRoundRect(rect, 5, 5, mPaint);
		    	
		    	mPaint.setColor(0xffffffff);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
			    mPaint.setTextSize(textSize * 1.8f);
			    canvas.drawText("同步数据", (oval.right - oval.left) * 1/2 + oval.left, oval.top + height/2 + 0.6f * textSize, mPaint);
	    	}else{
	    		mPaint.reset();
	    		mPaint.setTextSize(2.0f * textSize);
	    		mPaint.setAntiAlias(true);
	    		mPaint.setColor(0xffE3E5F9);
			    canvas.drawText(sleepLenth, (oval.right - oval.left) * 1/2 + oval.left - 5.0f * textSize, oval.top + (oval.bottom - oval.top) * 1/3, mPaint);
			    
			    mPaint.setColor(0xff868898);
			    mPaint.setTextSize(1.5f * textSize);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
		    	
//		    	String perStr = (int) (new BigDecimal(crrenDegrees/270f).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100) + "%";
		    	
		    	canvas.drawText(perStr, oval.left + (oval.right - oval.left) * 1/2, oval.top + (oval.bottom - oval.top) * 1/2 + textSize, mPaint);
	    	}
	    }else{  //未绑定硬件
	    	
		    if(!TextUtils.isEmpty(sleepTimeStr) && !TextUtils.isEmpty(wakeTimeStr)){
		    	mPaint.reset();
		    	mPaint.setAntiAlias(true);
		    	mPaint.setTextSize(2.0f * textSize);
		    	mPaint.setColor(0xffE3E5F9);
			    canvas.drawText(sleepLenth, (oval.right - oval.left) * 1/2 + oval.left - 4.5f * textSize, oval.top + (oval.bottom - oval.top) * 1/2, mPaint);
			    
			    mPaint.setColor(0xff868898);
			    mPaint.setTextSize(1.2f * textSize);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
		    	
//		    	String perStr = (int) (new BigDecimal(crrenDegrees/270f).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100) + "%";
		    	canvas.drawText(perStr, oval.left + (oval.right - oval.left) * 1/2, oval.top + (oval.bottom - oval.top) * 3/5 + textSize, mPaint);
		    }else{
		    	mPaint.reset();
		    	mPaint.setAntiAlias(true);
		    	float width = oval.right - oval.left;
		    	float height = oval.bottom - oval.top;
		    	RectF rect = new RectF();
		    	rect.set(oval.left + width/2 - 4.5f * textSize, oval.top + height/2 - 1.5f * textSize, oval.left + width/2 + 4.5f * textSize, oval.top + height/2 + 1.5f * textSize);
		    	mPaint.setColor(BTN_COLOR);
			    mPaint.setStrokeWidth(textSize * 2.5f);
			    mPaint.setStrokeCap(Paint.Cap.ROUND);  
			    canvas.drawRoundRect(rect, 5, 5, mPaint);
		    	
		    	mPaint.setColor(0xffffffff);
		    	mPaint.setTextAlign(Paint.Align.CENTER);
			    mPaint.setTextSize(textSize * 1.8f);
			    canvas.drawText("每日签到", (oval.right - oval.left) * 1/2 + oval.left, oval.top + height/2 + 0.6f * textSize, mPaint);
		    }
	    }
	    
	    	
	  //最后画睡眠度圆弧
	    float cx = oval.left + (oval.right - oval.left)/2;
		float cy = oval.top + (oval.bottom - oval.top)/2;
		Shader shader = new SweepGradient(cx, cy, new int[]{Color.RED, Color.YELLOW, Color.GREEN}, new float[]{0.15f, 0.40f, 0.75f});
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
	        mPaint.setColor(Color.RED);
			canvas.drawArc(oval, 135, 1, false, mPaint);//小弧形 
			mPaint.setShader(null);
		}
		if(crrenDegrees == 270){
			mPaint.reset();
	        mPaint.setStyle(Paint.Style.STROKE);
	        mPaint.setStrokeWidth(textSize * 1/2);
	        mPaint.setAntiAlias(true);
	        mPaint.setStrokeCap(Paint.Cap.ROUND);
	        mPaint.setColor(Color.GREEN);
			canvas.drawArc(oval, 45, 1, false, mPaint);//小弧形 
			mPaint.setShader(null);
		}
			
	}
	
	/**
	 * 初始角度
	 */
	private int crrenDegrees;
	private ObjectAnimator anim;

	private void setDegrees(int degrees){
		anim = ObjectAnimator.ofFloat(this, "alpha", 0f, degrees);
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
	    if(sleepTimeStr == null || wakeTimeStr == null)
	    	return null;
    	else {
    		ShareDataBean bean = new ShareDataBean();
    		bean.setSleepTime(sleepTimeStr);
    		bean.setGetUpTime(wakeTimeStr);
    		bean.setDate(dateStr);
    		bean.setSleepLength("");
    		bean.setTargetSleepLength("");
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
			if(downY > (oval.top + (oval.bottom - oval.top)/2 - 1.5f * textSize) && downY < (oval.top + (oval.bottom - oval.top)/2 + 1.5f * textSize)){
				if(downX > (oval.left + (oval.right - oval.left)/2 - 4.5f * textSize) && downX < (oval.left + (oval.right - oval.left)/2 + 4.5f * textSize)){
					if(isEvaluated){
						onClickChangeTime.onEvaluatedSleep();
					}else{
						if(isBindPillow){
							if(!isSync)
								onClickChangeTime.onClickSyncData();
							else{
								onClickChangeTime.onClickShowDaySleep();
							}
						}else{
							if (TextUtils.isEmpty(sleepTimeStr) || TextUtils.isEmpty(wakeTimeStr)) 
								onClickChangeTime.onClickPunch();
							else{
								onClickChangeTime.onClickShowDaySleep();
							}
						}
					}
				}else{
					onClickChangeTime.onClickShowDaySleep();
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
	 * 获取睡眠时间长度(返回HH:mm时间格式)
	 * @param dayData
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	private String getRealTimeString(SoftDayData dayData){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date d1 = sdf.parse(dayData.getGetUpTime());
			Date d2 = sdf.parse(dayData.getSleepTime());
			float hour = (float)(d1.getTime() - d2.getTime())/(1000 * 60 * 60);
			if(hour < 0)
				hour += 24;
			int h = (int)hour;
			float mm = (60 * (hour - h));
			int m = Integer.parseInt(new BigDecimal(mm).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			String hourString = "00";
			String minString = "00";
			if(h < 10)
				hourString = "0" + String.valueOf(h);
			else
				hourString = String.valueOf(h);
			if(m < 10)
				minString = "0" + String.valueOf(m);
			else 
				minString = String.valueOf(m);
			return hourString + ":" + minString;
		} catch (Exception e) {
		}
		return "00:00";
	}
	
	@SuppressLint("SimpleDateFormat") 
	private String getRealTimeString2(SoftDayData dayData){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date d1 = sdf.parse(dayData.getGetUpTime());
			Date d2 = sdf.parse(dayData.getSleepTime());
			double hour = (float)(d1.getTime() - d2.getTime())/(1000 * 60 * 60);
			if(hour < 0)
				hour += 24;
			return String.valueOf(hour);
		} catch (Exception e) {
		}
		return "0";
	}
	
	
	public void setOnClickChangeTime(OnClickChangeTime onClickChangeTime){
		this.onClickChangeTime = onClickChangeTime;
	}
	
	public interface OnClickChangeTime{
		void onClickSyncData();
		void onClickPunch();
		void onClickShowDaySleep();
		void onEvaluatedSleep();
	}
}
