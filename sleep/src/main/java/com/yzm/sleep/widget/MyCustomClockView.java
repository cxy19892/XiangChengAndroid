package com.yzm.sleep.widget;

import com.yzm.sleep.utils.LogUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/3/7.
 */
public class MyCustomClockView extends View {
    private int width;
    private int height;
    private Paint mPaint;
    private int minute;

    public MyCustomClockView(Context context) {
        super(context);
    }

    public MyCustomClockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setData(int minute){
        this.minute = minute;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //画圆
        mPaint.setColor(0xff000000);
        RectF oval1 = new RectF(25 , 25 , width - 25, height - 25);
        canvas.drawArc(oval1, 0, 360, true, mPaint);
        float startOval = 0;
        float stopOval = 360f/ (12f * 60f) * minute;
        LogUtil.i("tx", stopOval + "");
        //画圆弧
        mPaint.setColor(0xff78C6DA);
        RectF oval = new RectF(5, 5, width - 5, height - 5);
        canvas.drawArc(oval, startOval - 90, stopOval , true, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left-4;
        height= bottom - top-4;
        width = height = height > width ? width : height;
    }
}
