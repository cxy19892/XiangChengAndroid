package com.yzm.sleep.widget;

import com.yzm.sleep.utils.LogUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/2/23.
 */
public class MyClockPro extends View {

    private int width;
    private int height;
    private int HourRadius;
//    private int CircRadius;
    private Paint mStartTime;
    private Paint mStopTime;
    private Paint mPaint, mPaintWhite, mCirclePaint, mCirclePaintWhite, mAddCirclePaint, mReducePaint;
    private Paint mScirPaint;
    private TextPaint mTextPaint;
    private int HourColor = 0xff63737b;
    private int sminute = 5;
    private int shour   = 15;
    private int eminute = 5;
    private int ehour   = 15;
    private int changeTime = 0;
    private boolean isShowNum = false;
    private boolean isChanges = false;
    private float mDensity;
    private int smallCircRadius = 10;
    private int normalCircRadius = 20;
    private int TEXTSIZE = 15;
    private float fontHeight;
    private boolean isrun = true;
    private RectF oval;


    public MyClockPro(Context context) {
        super(context);
    }

    public MyClockPro(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDensity = getContext().getResources().getDisplayMetrics().density;

        smallCircRadius = (int)(3 * mDensity);
        mPaint = new Paint();
        mPaint.setColor(HourColor);
        mPaint.setStrokeWidth(2 * mDensity);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mStartTime = new Paint();
        mStartTime.setColor(HourColor);
        mStartTime.setStrokeWidth(2 * mDensity);
        mStartTime.setAntiAlias(true);


        mStopTime = new Paint();
        mStopTime.setColor(HourColor);
        mStopTime.setStrokeWidth(2 * mDensity);
        mStopTime.setAntiAlias(true);

        mTextPaint = new TextPaint();
        // 去锯齿
        mTextPaint.setAntiAlias(true);
        // 防抖动
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(TEXTSIZE * mDensity);
        mTextPaint.setColor(Color.BLACK);

        mScirPaint = new Paint();
        mScirPaint.setColor(HourColor);
        mScirPaint.setAntiAlias(true);
        mScirPaint.setDither(true);
        mScirPaint.setColor(0xff6f9ef6);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(0xaf6f9ef6);

        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaintWhite = new Paint(mCirclePaint);
        mCirclePaintWhite.setColor(0x7fedeced);
        
        mPaintWhite = new Paint(mPaint);
        mPaintWhite.setColor(Color.WHITE);

        Paint.FontMetrics mfontMetrics = mTextPaint.getFontMetrics();
        //计算文字高度
        fontHeight = mfontMetrics.bottom - mfontMetrics.top;

        mAddCirclePaint= new Paint(mCirclePaint);
        mAddCirclePaint.setARGB(0x7f,0x90,0xe0,0xea);

        mReducePaint = new Paint(mCirclePaint);
        mReducePaint.setARGB(0xff, 0xff, 0x59, 0x3c);

        oval = new RectF();

    }


    public void stopRunning(){
        isrun = false;
    }

    /**
     * 传入时间
     * @param
     * @param
     */
    public void setTime(int starthou, int startmin, int stophour, int stopmin, boolean isShowNum){
        this.shour = starthou;
        this.sminute = startmin;
        this.ehour = stophour;
        this.eminute = stopmin;
        this.isShowNum = isShowNum;
        invalidate();
    }


    /**
     * 传入时间
     * @param
     * @param
     */
    public void setChangeTime(int starthou, int startmin, int stophour, int stopmin, int changeTime, boolean isChange){
        this.shour = starthou;
        this.sminute = startmin;
        this.ehour = stophour;
        this.eminute = stopmin;
        this.changeTime = changeTime;
        this.isChanges = isChange;
        if(isChange){
            isrun = true;
            new MyThread().start();
        }
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            int ALPHA = 0x00;
            boolean isReturn = false;
            while(isrun){
                try{
                    Thread.sleep(10);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(isReturn){
                    ALPHA -= 2;
                }else {
                    ALPHA += 2;
                }

                if(ALPHA >= 0xfa){
                    isReturn = true;
                }else if(isReturn && ALPHA <= 0x0a){
                    isReturn = false;
                }


                mAddCirclePaint.setARGB(ALPHA, 0x90,0xe0,0xea);
                mReducePaint.setARGB(ALPHA, 0xff, 0x59, 0x3c);

                postInvalidate();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left-4;
        height= bottom - top-4;
        width = height = height > width ? width : height;
        HourRadius = width / 3;
        
        
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //画数字
        if(isShowNum) {
        	canvas.drawText("12", width / 2 - Layout.getDesiredWidth("0", mTextPaint), fontHeight-1, mTextPaint);

        	canvas.drawText("3", width/2 + (width/2 - fontHeight) /*width - Layout.getDesiredWidth("0", mTextPaint)*/, height / 2 + Layout.getDesiredWidth("0", mTextPaint) / 2, mTextPaint);

        	canvas.drawText("6", width / 2 - Layout.getDesiredWidth("0", mTextPaint) / 2, height, mTextPaint);

        	canvas.drawText("9", Layout.getDesiredWidth("0", mTextPaint), height/2 + Layout.getDesiredWidth("0", mTextPaint)/2, mTextPaint);
        }
        //画小圆点
        for (int i = 3; i <= 12; i+=3) {
            canvas.save();//保存当前画布
            canvas.rotate(360 / 12 * i, width / 2, height / 2);
            canvas.drawCircle(width/2, fontHeight+smallCircRadius, smallCircRadius, mScirPaint);
            canvas.restore();//
        }

        //画出大圆
//        canvas.drawCircle(width / 2, height / 2, CircRadius, mPaint);
        //画出圆中心
        canvas.drawCircle(width / 2, height / 2, smallCircRadius, mPaint);
        //依次旋转画布，画出每个刻度和对应数字
        
            mPaint.setTextSize(width / 12);
            mPaint.setStrokeWidth(2);
            for (int i = 1; i <= 72; i++) {
                if(i % 18 != 0) {
                    canvas.save();//保存当前画布
                    canvas.rotate(360 / 72 * i, width / 2, height / 2);
                    //左起：起始位置x坐标，起始位置y坐标，终止位置x坐标，终止位置y坐标，画笔(一个Paint对象)
                    if(i % 6 != 0)
                    	canvas.drawLine(width / 2, fontHeight, width / 2, fontHeight + smallCircRadius * 3 /2, mPaint);
                    else {
                    	canvas.drawLine(width / 2, fontHeight, width / 2, fontHeight + smallCircRadius*9/2, mPaint);
					}canvas.restore();//
                }
            }

        float StopDegree = (ehour*60+eminute)/12f/60*360;//得到分针旋转的角度
        

        float StartDegree = (shour*60+sminute)/12f/60*360;//得到时钟旋转的角度
        
        //画圆弧
        mCirclePaint.setStrokeWidth(width/5);
        mCirclePaintWhite.setStrokeWidth(width/5);
//        oval = new RectF(width / 2 - (float)(width * Math.sqrt(2d)/ 6), height / 2 - (float)(width * Math.sqrt(2d)/ 6), width / 2 + (float)(width * Math.sqrt(2d)/ 6), height / 2 + (float)(width * Math.sqrt(2d)/ 6));//(TEXTSIZE * mDensity + smallCircRadius, TEXTSIZE * mDensity + smallCircRadius, width - TEXTSIZE * mDensity - smallCircRadius, height - TEXTSIZE * mDensity - smallCircRadius);
        oval.left = width / 2 - (float)(width * Math.sqrt(2d)/ 6);
        oval.top = height / 2 - (float)(width * Math.sqrt(2d)/ 6);
        oval.right = width / 2 + (float)(width * Math.sqrt(2d)/ 6);
        oval.bottom=height / 2 + (float)(width * Math.sqrt(2d)/ 6);
//        canvas.drawRect(width / 2 - HourRadius/2, height / 2 - HourRadius/2, width / 2 + HourRadius/2, height / 2 + HourRadius/2, mStartTime);
        float start = StartDegree - 90f;
        float stop = StopDegree - 90f;
        if(ehour > (shour + 12)){
            canvas.drawArc(oval, start, 360 , false, mCirclePaint);//Arc(oval, hourDegree % 360 - 90, minuteDegree - 90 , false, mCirclePaint);
            //起点时刻
            canvas.save();
            canvas.rotate(StartDegree, width / 2, height / 2);
            canvas.drawLine(width / 2, height / 2 - HourRadius, width / 2, height / 2, mStartTime);
            canvas.restore();
            //终点时刻
            canvas.save();
            canvas.rotate(StopDegree, width / 2, height / 2);
            canvas.drawLine(width / 2, height / 2 - HourRadius, width / 2, height / 2, mStopTime);
            canvas.restore();
        }else {
            float sweepAngle = ehour > (shour + 12) ? (stop - 360 - start) : shour > (ehour + 12) ? (360 * 2 - start + stop) : stop - start;
            if (sweepAngle > 360) {
                canvas.drawArc(oval, start, sweepAngle, false, mCirclePaint);//Arc(oval, hourDegree % 360 - 90, minuteDegree - 90 , false, mCirclePaint);
                //起点时刻
                canvas.save();
                canvas.rotate(StartDegree, width / 2, height / 2);
                canvas.drawLine(width / 2, height / 2 - HourRadius, width / 2, height / 2, mStartTime);
                canvas.restore();
                //终点时刻
                canvas.save();
                canvas.rotate(StopDegree, width / 2, height / 2);
                canvas.drawLine(width / 2, height / 2 - HourRadius, width / 2, height / 2, mStopTime);
                canvas.restore();
            } else {
                canvas.drawArc(oval, start, sweepAngle, false, mCirclePaint);//Arc(oval, hourDegree % 360 - 90, minuteDegree - 90 , false, mCirclePaint);
                canvas.drawArc(oval, stop, 360 - sweepAngle, false, mCirclePaintWhite);
                //起点时刻
                canvas.save();
                canvas.rotate(StartDegree, width / 2, height / 2);
                canvas.drawLine(width / 2 - 2 * mDensity, height / 2 - HourRadius - 5, width / 2 - 2 * mDensity, height / 2 - HourRadius + width / 5, mPaintWhite);
                canvas.restore();
                //终点时刻
                canvas.save();
                canvas.rotate(StopDegree, width / 2, height / 2);
                canvas.drawLine(width / 2 + 2 * mDensity, height / 2 - HourRadius - 5, width / 2 + 2 * mDensity, height / 2 - HourRadius + width / 5, mPaintWhite);
                canvas.restore();

                if(isChanges){
                    if(changeTime > 0){//增加睡眠时长
                        mAddCirclePaint.setStrokeWidth(width / 5);
                        canvas.drawArc(oval, start, -7.8f*changeTime, false, mAddCirclePaint);
                    }else{//缩短睡眠时长
                        mReducePaint.setStrokeWidth(width / 5);
                        canvas.drawArc(oval, start, -7.8f*changeTime, false, mReducePaint);
                    }
                }

                //起点时刻
                canvas.save();
                canvas.rotate(StartDegree, width / 2, height / 2);
                canvas.drawLine(width / 2, height / 2 - HourRadius, width / 2, height / 2, mStartTime);
                canvas.restore();
                //终点时刻
                canvas.save();
                canvas.rotate(StopDegree, width / 2, height / 2);
                canvas.drawLine(width / 2, height / 2 - HourRadius, width / 2, height / 2, mStopTime);
                canvas.restore();
            }
        }
    }

}
