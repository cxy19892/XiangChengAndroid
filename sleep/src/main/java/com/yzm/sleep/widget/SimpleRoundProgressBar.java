package com.yzm.sleep.widget;


import com.yzm.sleep.R;
import com.yzm.sleep.utils.LogUtil;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/** 
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度 
 * @author xiaanming 
 * 
 */  
public class SimpleRoundProgressBar extends View {  
    /** 
     * 画笔对象的引用 
     */  
    private Paint paint;  
      
    /** 
     * 圆环的颜色 
     */  
    private int roundColor = R.color.cbg_color;  
      
    /** 
     * 圆环进度的颜色 
     */  
    private int roundProgressColor = R.color.theme_color;  
      
    /** 
     * 中间进度百分比的字符串的颜色 
     */  
    private int textColor = R.color.onet_color;  
      
    /** 
     * 中间进度百分比的字符串的字体 
     */  
    private float textSize = 12;  
      
    /** 
     * 圆环的宽度 
     */  
    private float roundWidth = 10;  
      
    /** 
     * 最大进度 
     */  
    private int max = 100000;  
      
    /** 
     * 当前进度 
     */  
    private int progress = 0;  
    
    
    
    public SimpleRoundProgressBar(Context context) {  
        this(context, null);  
    }  
  
    public SimpleRoundProgressBar(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
      
    public SimpleRoundProgressBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);       
        paint = new Paint();  
    }  
      
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
          
        /** 
         * 画最外层的大圆环 
         */  
        int centre = getWidth()/2; //获取圆心的x坐标  
        int radius = (int) (centre - roundWidth/2); //圆环的半径  
        paint.setColor(roundColor); //设置圆环的颜色  
        paint.setStyle(Paint.Style.STROKE); //设置空心  
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度  
        paint.setAntiAlias(true);  //消除锯齿   
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环  
        /** 
         * 画圆弧 ，画圆环的进度 
         */  
          
        //设置进度是实心还是空心  
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度  
        paint.setColor(roundProgressColor);  //设置进度的颜色  
        RectF oval = new RectF(centre - radius, centre - radius, centre  
                + radius, centre + radius);//用于定义的圆弧的形状和大小的界限  
        paint.setStyle(Paint.Style.STROKE);  
//        canvas.drawArc(oval, -90, 360 * progress / max, false, paint);  //根据进度画圆弧  
        canvas.drawArc(oval, -90, 360 * per, false, paint);
//        LogUtil.d("chen", "progress = "+ 360 * progress / max);
    }  
      
      
    public synchronized int getMax() {  
        return max;  
    }  
  
    /** 
     * 设置进度的最大值 
     * @param max 
     */  
    public synchronized void setMax(int max) {  
        if(max < 0){  
            throw new IllegalArgumentException("max not less than 0");  
        }  
        this.max = max;  
    }  
  
    /** 
     * 获取进度.需要同步 
     * @return 
     */  
    public synchronized int getProgress() {  
        return progress;  
    }
    
    private float per;
    
    public synchronized void setProgressf(float per){
    	if(per > 1)
    		per = 1;
    	else if(per < 0)
    		per = 0;
    	
    	this.per = per;
    	postInvalidate();  
    }
  
    /** 
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 
     * 刷新界面调用postInvalidate()能在非UI线程刷新 
     * @param progress 
     */  
    public synchronized void setProgress(int progress) {  
        if(progress < 0){  
            throw new IllegalArgumentException("progress not less than 0");  
        }  
        if(progress > max){  
            progress = max;  
        }  
        if(progress <= max){  
            this.progress = progress;  
            this.per = progress/max;
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
  
    public void setRoundColor(int roundColor){
    	this.roundColor = roundColor;
    }
    
    public void setRoundProgressColor(int roundProgressColor){
    	this.roundProgressColor = roundProgressColor;
    }
  
}  