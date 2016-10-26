package com.yzm.sleep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yzm.sleep.utils.SleepUtils;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 * 
 * @author Enid
 * 
 */
@SuppressLint("DrawAllocation")
public class PalyRoundProgressBar extends View {
	/**画笔对象的引用*/
	private Paint paint;
	/** 圆环的颜色*/
	private int roundColor;
	private int roundProgressColor;
	/**中间进度百分比的字符串的颜色*/
	private int textColor;
	/**中间进度百分比的字符串的字体 */
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

	public static final int STROKE = 0;
	public static final int FILL = 1;
	Context context;
	private Paint paint16;
	
	private float mDensity;

	public PalyRoundProgressBar(Context context) {
		this(context, null);
		this.context = context;
		getDensity();
	}

	public PalyRoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
		getDensity();
	}

	public PalyRoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		getDensity();

		paint = new Paint();
		paint16 = new Paint();

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBar);

		// 获取自定义属性和默认值
		roundColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_roundColor,
				Color.parseColor("#FFFFFF"));
		roundProgressColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_roundProgressColor,
				Color.parseColor("#F3334B"));
		textColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_textColor, Color.parseColor("#3b3b3b"));
		textSize = mTypedArray.getDimension(
				R.styleable.RoundProgressBar_textSize, SleepUtils.dipToPx(context, 48));
		roundWidth = mTypedArray.getDimension(
				R.styleable.RoundProgressBar_roundWidth, (float) (2.53 * mDensity));
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(
				R.styleable.RoundProgressBar_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

		mTypedArray.recycle();
	}
	
	private void getDensity(){
		mDensity = getContext().getResources().getDisplayMetrics().density;

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));//给canvas加抗锯齿
		int centre = getWidth() / 2; // 获取圆心的x坐标
		int radius = (int) (centre - roundWidth / 2 - 6); // 圆环的半径
		 
		
		RectF oval = new RectF(centre - radius, centre - radius, centre+ radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
		
		/*画白色圆环*/
		paint.setColor(roundColor); // 设置进度的颜色
		paint.setStyle(Paint.Style.STROKE);//设置圆环为空心
		paint.setAntiAlias(true);  
		canvas.drawArc(oval, 0, 360 , false, paint); 
		

		/* 画圆弧 ，画圆环的进度 */
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setColor(roundProgressColor); // 设置进度的颜色
		paint.setStyle(Paint.Style.STROKE);//设置圆环为空心
		canvas.drawArc(oval, -90, 360 * progress / max, false, paint); // 根据进度画圆弧
			
	}


	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
	public void setMax(int max) {
		this.max = max;
	}


	public  void setProgress(int progress) {
		
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
		}
	}

}
