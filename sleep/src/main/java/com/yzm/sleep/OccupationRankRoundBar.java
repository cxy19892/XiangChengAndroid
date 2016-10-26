package com.yzm.sleep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 * 
 * @author Enid
 * 
 */
@SuppressLint("DrawAllocation")
public class OccupationRankRoundBar extends View {
	/**画笔对象的引用*/
	private Paint paint;
	/**中间进度百分比的字符串的字体 */
	private float textSize;
	/**圆环的宽度*/
	private float roundWidth;
	/**最大进度*/
	private int max = 100;
	/**当前进度*/
	private int progress;

	public static final int STROKE = 0;
	public static final int FILL = 1;
	Context context;
	public OccupationRankRoundBar(Context context) {
		this(context, null);
		this.context = context;
	}

	public OccupationRankRoundBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
	}

	public OccupationRankRoundBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@SuppressLint({ "DrawAllocation", "ResourceAsColor" })
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int mWidth = getWidth();
		int mHeight = getHeight();
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));//给canvas加抗锯齿
		int centre = getWidth() / 2; // 获取圆心的x坐标
		roundWidth = getResources().getDimension(R.dimen.occupation_round_width);
		int radius = (int) (centre - roundWidth * 2.4); // 圆环的半径
		
		
		//画橙色渐变弧形
		paint = new Paint();
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setColor(Color.parseColor("#FFCC33")); // 设置进度的颜色
		paint.setStyle(Paint.Style.STROKE);//设置圆环为空心
		paint.setAntiAlias(true);  
		RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
		canvas.drawArc(oval, -90, 360 * progress / max, false, paint); // 根据进度画圆弧
			
	
		
//		 // 随ProgressBar移动的圆。
//		paint.setStyle(Paint.Style.FILL);
//		canvas.drawCircle(
//                (float) (centre + radius * Math.cos((progress * 360.0f / max - 90)* 3.14 / 180)),
//                (float) (centre + radius * Math.sin((progress * 360.0f / max - 90)* 3.14 / 180)),
//                roundWidth * 3 / 2,
//                paint);// 小圆
		 
		//画时刻值背景图片
		Bitmap round_bc = BitmapFactory.decodeResource(getResources(),R.drawable.occupation_round_bc);
		int round_bc_width = round_bc.getWidth();
		int round_bc_height = round_bc.getHeight();
		canvas.drawBitmap(round_bc, (mWidth - round_bc_width)/2, (mHeight - round_bc_height)/2 ,null);
		
		//弧形开始图案
//		Bitmap round_start = BitmapFactory.decodeResource(getResources(),R.drawable.start);
//		int round_start_width = centre - 8;
//		int round_start_height  = centre - radius - round_start.getWidth() / 2;
//		canvas.drawBitmap(round_start, round_start_width, round_start_height ,null);
		
		//弧形结束图案
//		Bitmap round_end = BitmapFactory.decodeResource(getResources(),R.drawable.end);
//		float round_end_width = (float) (centre + radius * Math.cos((progress * 360.0f / max - 90)* 3.14 / 180) - round_end.getWidth() / 2);
//		float round_end_height  = (float) (centre + radius * Math.sin((progress * 360.0f / max - 90)* 3.14 / 180) - round_end.getWidth() / 2);
//		canvas.drawBitmap(round_end, round_end_width, round_end_height ,null);
		
		
		//文字
		TextPaint  textPaint = new TextPaint();
		textSize = (getResources().getDimension(R.dimen.textsize_round_hour));
		textPaint.setAntiAlias(true);  
		textPaint.setTextSize(textSize);
		textPaint.setColor(Color.parseColor("#FF6655"));
		canvas.drawText(progress +"%", centre - textPaint.measureText(progress +"%") / 2, centre + textSize / 4
				, textPaint);//击败概率
		textPaint.setTextSize((getResources().getDimension(R.dimen.textsize_round_tag)));
		textPaint.setColor(Color.parseColor("#3d3d3d"));
		String text_top ="您击败了";
		String text_bottom ="同职业用户";
		float text_top_width = textPaint.measureText(text_top);
		float text_bottom_width = textPaint.measureText(text_bottom);
		canvas.drawText(text_top, centre - text_top_width / 2, centre - radius / 2 - 5, textPaint);// 画顶部提示
		canvas.drawText(text_bottom, centre - text_bottom_width / 2, centre + radius / 2 + 10, textPaint);// 画底部提示
	}
	

	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
//	public synchronized void setMax(int max) {
//		if (max < 0) {
//			throw new IllegalArgumentException("max not less than 0");
//		}
//		this.max = max;
//	}

	/**
	 * 获取进度.需要同步
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
		}

	}


}
