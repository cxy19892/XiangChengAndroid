package com.yzm.sleep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
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
public class NewRoundProgressBar extends View {
	/** 画笔对象的引用 */
	private Paint paint;
	/** 圆环的颜色 */
	private int roundColor;
	private int roundProgressColor;
	/** 中间进度百分比的字符串的颜色 */
	private int textColor;
	/** 中间进度百分比的字符串的字体 */
	private float textSize;
	/** 圆环的宽度 */
	private float roundWidth;
	/** 最大进度 */
	private int max;
	/** 当前进度 */
	private int progress;
	/** 进度的风格，实心或者空心 */
	private int style;
	private String text = "";

	public static final int STROKE = 0;
	public static final int FILL = 1;
	Context context;
	private float mDensity;

	public NewRoundProgressBar(Context context) {
		this(context, null);
		this.context = context;
		getDensity();
	}

	public NewRoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
		getDensity();
	}

	public NewRoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		getDensity();

		paint = new Paint();
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBar);

		// 获取自定义属性和默认值
		roundColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_roundColor,
				Color.parseColor("#6f9ef6"));
		roundProgressColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_roundProgressColor,
				Color.parseColor("#6f9ef6"));
		textColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_textColor,
				Color.parseColor("#6f9ef6"));
		textSize = mTypedArray.getDimension(
				R.styleable.RoundProgressBar_textSize,
				SleepUtils.dipToPx(context, 48));
		roundWidth = mTypedArray.getDimension(
				R.styleable.RoundProgressBar_roundWidth,
				(float) (2.53 * mDensity));
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);

		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

		mTypedArray.recycle();
	}

	private void getDensity() {
		mDensity = getContext().getResources().getDisplayMetrics().density;

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));// 给canvas加抗锯齿
		int centre = getWidth() / 2; // 获取圆心的x坐标
		int radius = (int) (centre - roundWidth / 2 - 6); // 圆环的半径

		/* 画白色实心圆 */
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setColor(Color.parseColor("#ffffff")); // 设置进度的颜色
		paint.setStyle(Paint.Style.FILL);// 设置圆环为实心
		paint.setAntiAlias(true);
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
		canvas.drawArc(oval, 0, 360, true, paint);

		/* 画灰色空心圆 */
		paint.setColor(roundColor); // 设置进度的颜色 Color.parseColor("#D2D2D2")
		paint.setStyle(Paint.Style.STROKE);// 设置圆环为空心
		canvas.drawArc(oval, 0, 360, false, paint);

		/* 画圆弧 ，画圆环的进度 */

		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setColor(Color.parseColor("#6f9ef6")); // 设置进度的颜色
		paint.setStyle(Paint.Style.STROKE);// 设置圆环为空心
		paint.setAntiAlias(true);
		Shader shader = new SweepGradient(centre, centre, new int[]{PURPLE_START_COLOR, PURPLE_END_COLOR}, null);
		Matrix matrix = new Matrix();
		matrix.setRotate(-90, centre, centre);
		shader.setLocalMatrix(matrix);
		paint.setShader(shader);
		canvas.drawArc(oval, -90, 360 * progress / max, false, paint); // 根据进度画圆弧
		paint.setShader(null);
		
		/* 画进度百分比 */
		paint.setStrokeWidth(roundWidth/4);
		paint.setStyle(Style.FILL);
		paint.setColor(textColor);
		paint.setTextSize(getResources().getDimension(
				R.dimen.textsize_round_minute));
		float textWidth = paint.measureText(text);
		if (style == STROKE) {
			paint.setTextSize(SleepUtils.dipToPx(context, 16));
			paint.setColor(Color.parseColor("#ff6f9ef6"));
			textWidth = paint.measureText(text);// 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
			canvas.drawText(text, centre - textWidth / 2,
					centre + textSize / 8, paint); 
		}

	}

	public int getMax() {
		return max;
	}

	private final int PURPLE_START_COLOR = 0x7f6f9ef6;
	private final int PURPLE_END_COLOR = 0xff6f9ef6;
	
	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
	public void setMax(int max) {
		// if (max < 0) {
		// throw new IllegalArgumentException("max not less than 0");
		// }
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

	public void setProgress(int progress) {

		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
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

	/**
	 * 设置中心文字
	 * 
	 * @param hour
	 */
	public void setCenterText(String text) {
		this.text = text;
		postInvalidate();
	}

}
