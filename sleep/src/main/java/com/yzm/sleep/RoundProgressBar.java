package com.yzm.sleep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.yzm.sleep.utils.SleepUtils;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 */
@SuppressLint("DrawAllocation")
public class RoundProgressBar extends View {
	/**画笔对象的引用*/
//	private Paint paint;
	/** 圆环的颜色*/
	private int roundColor;
	/**圆环进度的颜色*/
	private int roundProgressColor;
	/**中间进度百分比的字符串的颜色*/
	private int textColor;
	/**中间进度百分比的字符串的字体*/
	private float textSize;
	/**圆环的宽度*/
	private float roundWidth;
	/**最大进度*/
	private int max;
	/**当前进度*/
	private int progress;
	/**是否显示中间的进度*/
	private boolean textIsDisplayable;
	/**进度的风格，实心或者空心*/
	private int style;
	/**数据是否存在*/
	private boolean isDataExits = false;
	/**是否完成8小时睡眠目标*/
	private boolean isAchieve;
	private String healthHour;

	public static final int STROKE = 0;
	public static final int FILL = 1;
	Context context;

	private int centre;

	private float centre_y;

	private int radius = 0;

	private int radius_in = 0;
	
	private String hour = "0";
	
	private String minute = "0";

	public RoundProgressBar(Context context) {
		this(context, null);
		this.context = context;
	}

	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
	}

	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				
				R.styleable.RoundProgressBar);

		// 获取自定义属性和默认值
		roundColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_roundColor,
				Color.parseColor("#DDDDDD"));
		roundProgressColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_roundProgressColor,
				Color.parseColor("#DDDDDD"));
		textColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_textColor, Color.parseColor("#3b3b3b"));
		textSize = mTypedArray.getDimension(
				R.styleable.RoundProgressBar_textSize, SleepUtils.dipToPx(context, 58));
		roundWidth = mTypedArray.getDimension(
				R.styleable.RoundProgressBar_roundWidth,getResources().getDimension(R.dimen.sleep_round_width));
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(
				R.styleable.RoundProgressBar_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

		mTypedArray.recycle();
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		roundWidth = getResources().getDimension(R.dimen.sleep_round_width);
		centre = getWidth()/2;
		centre_y = getWidth() / 2 - 30;
		radius = (int) (centre_y - roundWidth / 2 - 20); // 圆环的半径
		radius_in = (int) (radius - roundWidth / 2 - roundWidth / 4);
		
		
		if(isAchieve){
			roundWidth = roundWidth + 3.6f;
			centre_y = getWidth() / 2 ;
			radius = (int) (centre_y - roundWidth / 2 + 0.6); // 圆环的半径
		}

		drawArc(canvas);
		drawText(canvas,isDataExits);
	}
	
	/**
	 * 画圆弧 ，画圆环的进度
	 * @param canvas
	 */
	public void drawArc(Canvas canvas){
		Paint sweepPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//梯度渐变画笔
		sweepPaint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		sweepPaint.setStyle(Paint.Style.STROKE);// 设置进度是空心
		sweepPaint.setAntiAlias(true);//设置抗锯齿
		//设置弧形进度渐变色
		int[] colorArrs = new int[] { 0xFFFFCC33, 0xFF339933, 0xFF33BBEE,0xFFFF6655, 0xFFFFCC33 };
		SweepGradient shader = new SweepGradient(centre, centre, colorArrs,null);
		shader.setLocalMatrix(null);
		RectF oval = new RectF(centre - radius, centre_y - radius, centre + radius, centre_y + radius); // 用于定义的圆弧的形状和大小的界限
		sweepPaint.setShader(shader);
		if(progress < max){
			canvas.drawArc(oval, -90, 360 * progress / max, false, sweepPaint); // 根据进度画圆弧
		}else {
		}
		
		canvas.save();
		if(isDataExits && progress < max){
			//是进度起始带有弧度 在起始画两个小圆
			sweepPaint.setStrokeWidth(roundWidth / 2); 
			canvas.restore();
			canvas.drawCircle(
					(float) (centre + radius * Math.cos(-90 * 3.14 / 180)),
					(float) (centre_y + radius * Math.sin(-90 * 3.14 / 180)),
					roundWidth / 4, 
					sweepPaint);// 小圆
			canvas.save();
			canvas.restore();
			canvas.drawCircle(
					(float) (centre + radius * Math.cos((progress * 360.0f / max - 90) * 3.14 / 180)),
					(float) (centre_y + radius * Math.sin((progress * 360.0f / max - 90) * 3.14 / 180)),
					roundWidth / 4,
					sweepPaint);// 小圆
		}
		sweepPaint.setShader(null);
		canvas.save();
	}
	
	/**
	 * 画显示文字
	 * @param canvas
	 */
	public void drawText(Canvas canvas, boolean isDataExits){
		canvas.restore();
		TextPaint paint88 = new TextPaint();
		TextPaint paint68 = new TextPaint();
		TextPaint paint32 = new TextPaint();
		paint88.setColor(textColor);
		paint68.setColor(textColor);
		paint32.setColor(Color.parseColor("#acb0b2"));
		paint88.setAntiAlias(true);
		paint68.setAntiAlias(true);
		paint32.setAntiAlias(true);
		paint88.setTextSize(getResources().getDimension(R.dimen.textsize_round_hour));
		paint68.setTextSize(getResources().getDimension(R.dimen.textsize_round_minute));
		paint32.setTextSize(getResources().getDimension(R.dimen.textsize_round_tag));
//		paint88.setTypeface(TypefaceUtil.getTypeHiraginoSans(context));
//		paint68.setTypeface(TypefaceUtil.getTypeHiraginoSans(context));
//		paint32.setTypeface(TypefaceUtil.getTypeHiraginoSans(context));
//		paint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体

		float text_hour_x = paint88.measureText(hour) +
				paint68.measureText(minute) +
				paint32.measureText("h") +
				paint32.measureText("m"); // 测量字体宽度确定画小时数开始的x值
//		if(!isDataExits){
//			hour = "0";
//			minute = "0";
//		}
		if (textIsDisplayable && style == STROKE) {
			canvas.drawText(hour, centre - text_hour_x / 2, centre + textSize / 10, paint88); // 画小时数
			canvas.drawText("h", centre - text_hour_x / 2 + paint88.measureText(hour), centre + textSize / 10, paint32); // 画小时符号
			canvas.drawText(minute, centre - text_hour_x / 2 + paint88.measureText(hour) + paint32.measureText("h") * 3 / 2, centre + textSize / 10, paint68); // 画分钟数
			canvas.drawText("m", centre - text_hour_x / 2 + paint88.measureText(hour) + paint32.measureText("h") * 3 / 2 + paint68.measureText(minute), centre + textSize / 10, paint32); // 画分符号

			paint32.setColor(Color.parseColor("#acb0b2"));
			String text_top = getResources().getString(R.string.layout_day_sleep_s);
			String text_bottom = getResources().getString(R.string.layout_day_sleep_health);
			text_bottom  = text_bottom + (healthHour == null ? "8" : healthHour) + "h";
			float text_top_width = paint32.measureText(text_top);
			float text_bottom_width = paint32.measureText(text_bottom);
			
			canvas.drawText(text_top, centre - text_top_width / 2, centre - radius_in / 2 - 8, paint32);// 画顶部提示
			canvas.drawText(text_bottom, centre - text_bottom_width / 2, centre + radius_in / 2, paint32);// 画底部提示
			
		}
		
		canvas.save();

	}

	public int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
	public void setMax(int max) {
//		if (max < 0) {
//			throw new IllegalArgumentException("max not less than 0");
//		}
		this.max = max;
	}

	/**
	 * 获取进度.需要同步
	 * 
	 * @return
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新  synchronized
	 * 
	 * @param progress
	 */
	public  void setProgress(int progress) {
//		if (progress < 0) {
//			throw new IllegalArgumentException("progress not less than 0");
//		}
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
//			postInvalidate();
		}

	}

	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}
	
	public void setIsAchieve(boolean isAchieve){
		this.isAchieve = isAchieve;
	}
	
	public void setHour(String hour){
		this.hour = hour;
	}
	
	public void setMinute(String minute){
		this.minute = minute;
	}
	
	/**
	 * 设置是否有数据
	 * @return
	 */
	public void setDataExits(boolean isDataExits) {
		this.isDataExits = isDataExits;
	}
	public void setHealthHour(String healthHour) {
		this.healthHour = healthHour;
	}
}
