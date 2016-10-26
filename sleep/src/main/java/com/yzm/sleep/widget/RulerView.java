package com.yzm.sleep.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * 卷尺控件类。由于时间比较紧，只有下班后有时间，因此只实现了基本功能。<br>
 * 细节问题包括滑动过程中widget边缘的刻度显示问题等<br>
 *
 * @author ttdevs
 */

public class RulerView extends View {

    public interface OnValueChangeListener {
        public void onValueChange(int value);
    }
    //刻度开始位置距离ruler顶端的距离
    public static final int SCALE_BLANK = 12;


    // 刻度精度
    public static final int MOD_TYPE_HALF = 4;
    public static final int MOD_TYPE_ONE = 8;

    // 刻度间隔距离
    private static final int ITEM_HALF_DIVIDER = 20;
    private static final int ITEM_ONE_DIVIDER = 20;

    // 刻度长短
    private static final int ITEM_MAX_HEIGHT = 26;
    private static final int ITEM_MIN_HEIGHT = 16;

    private static final int TEXT_SIZE = 18;

//    private static final int SCALE_PADDINGTOP = 6;

    private int mType = 1;
    private float mDensity;
    private int mValue = 32, mMaxValue = 95, mMinValue = 0,
            mModType = MOD_TYPE_HALF, mLineDivider = ITEM_HALF_DIVIDER;

    private int mLastX, mMove;
    private int mWidth, mHeight;

    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private OnValueChangeListener mListener;

 
    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new Scroller(getContext());
        mDensity = getContext().getResources().getDisplayMetrics().density;

        mMinVelocity = ViewConfiguration.get(getContext())
                .getScaledMinimumFlingVelocity();

        // setBackgroundResource(R.drawable.bg_wheel);
//		setBackgroundDrawable(createBackground());
    }


    /**
     *
     * 考虑可扩展，但是时间紧迫，只可以支持两种类型效果图中两种类型
     *
     * @param
     *            //初始值
     * @param maxValue
     *            最大值
     * @param model
     *            刻度盘精度：<br>
     *            {@link }<br>
     *            {@link }<br>
     */
    public void initViewParam(int defaultValue, int maxValue, int model) {
        switch (model) {
            case MOD_TYPE_HALF:
                mModType = MOD_TYPE_HALF;
                mLineDivider = ITEM_HALF_DIVIDER;
                mValue = defaultValue * 2;
                mMaxValue = maxValue * 2;
                break;
            case MOD_TYPE_ONE:
                mModType = MOD_TYPE_ONE;
                mLineDivider = ITEM_ONE_DIVIDER;
                mValue = defaultValue;
                mMaxValue = maxValue;
                break;

            default:
                break;
        }
        invalidate();

        mLastX = 0;
        mMove = 0;
        notifyValueChange();
    }

    /**
     * 设置用于接收结果的监听器
     *
     * @param listener
     */
    public void setValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    public void setValue(int mMaxValue, int mMinValue, int mValue, int mtype){
    	this.mValue = mValue;
    	this.mMaxValue = mMaxValue;
    	this.mMinValue = mMinValue;
    	this.mType = mtype;
    	notifyValueChange();
    }

    /**
     * 获取当前刻度值
     *
     * @return
     */
    public int getValue() {
        return mValue;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawScaleLine(canvas);
        // drawWheel(canvas);
        drawMiddleLine(canvas);
    }

    private void drawBg(Canvas canvas) {
        RectF bgRectF = new RectF(0, 15 * mDensity, mWidth, mHeight);
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#7fedeced"));
        canvas.drawRect(bgRectF, bgPaint);
    }

    /**
     * 从中间往两边开始画刻度线
     *
     * @param canvas
     */
    private void drawScaleLine(Canvas canvas) {
        canvas.save();

        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(5);
        linePaint.setColor(Color.parseColor("#8F8F8F"));

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(TEXT_SIZE * mDensity);
        textPaint.setColor(Color.parseColor("#00000000"));


        canvas.drawLine(0, mHeight/* - (25 * mDensity)*/, mWidth, mHeight/* - (25 * mDensity)*/, linePaint);

        int width = mWidth, drawCount = 0;
        float xPosition = 0, textWidth = Layout.getDesiredWidth("0", textPaint);

        for (int i = 0; drawCount <= 4 * width; i++) {
//            int numSize = String.valueOf(mValue + i).length();
            //向左画
            xPosition = (width / 2 - mMove) + i * mLineDivider * mDensity;
            if (xPosition + getPaddingRight() < mWidth) {
                if ((mValue + i) % mModType == 0) {// 画长刻度1930、1940等处
                    canvas.drawLine(xPosition, mHeight/* - (25 * mDensity)*/, xPosition, mHeight/* - (25 * mDensity)*/ - mDensity * ITEM_MAX_HEIGHT, linePaint);/*getPaddingTop() + SCALE_PADDINGTOP, xPosition,
                            mDensity * ITEM_MAX_HEIGHT, linePaint);*/

                    if (mValue + i <= mMaxValue && mValue + i >= mMinValue) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(
                                        ((mValue + i)/4 > 9 ? (mValue + i)/4 : "0"+(mValue + i)/4) +":"+ ((mValue + i)%4 == 0 ? "00" : ((mValue + i)%4) *15),
                                        countLeftStart(mValue + i, xPosition-14,
                                                textWidth),
                                        mHeight/* - (25 * mDensity)*/ - mDensity * ITEM_MIN_HEIGHT - textWidth, textPaint);
                                break;
                            case MOD_TYPE_ONE:
                                // canvas.drawText(String.valueOf(mValue + i),
                                // xPosition - (textWidth * numSize / 2),
                                // getHeight() - textWidth, textPaint);
                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    canvas.drawLine(xPosition, mHeight/* - (25 * mDensity)*/, xPosition, mHeight/* - (25 * mDensity)*/ - mDensity * ITEM_MIN_HEIGHT, linePaint);/*getPaddingTop() + SCALE_PADDINGTOP, xPosition,
                            mDensity * ITEM_MIN_HEIGHT, linePaint);*/
                }
            }
            //向右画
            xPosition = (width / 2 - mMove) - i * mLineDivider * mDensity;
            if (xPosition - getPaddingLeft() > 0) {
                if ((mValue - i) % mModType == 0) {
                    canvas.drawLine(xPosition, mHeight/* - (25 * mDensity)*/, xPosition, mHeight/* - (25 * mDensity)*/ - mDensity * ITEM_MAX_HEIGHT, linePaint);/*(xPosition, getPaddingTop() + SCALE_PADDINGTOP, xPosition,
                            mDensity * ITEM_MAX_HEIGHT, linePaint);*/

                    if (mValue - i >= mMinValue && mValue - i <= mMaxValue) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(
                                        ((mValue - i)/4 > 9 ? (mValue - i)/4 : "0"+(mValue - i)/4) +":"+ ((mValue - i)%4 == 0 ? "00" : ((mValue - i)%4) *15),
                                        countLeftStart(mValue - i, xPosition-14,
                                                textWidth),
                                        mHeight/* - (25 * mDensity)*/ - mDensity * ITEM_MIN_HEIGHT - textWidth, textPaint);
                                break;
                            case MOD_TYPE_ONE:
                                // canvas.drawText(String.valueOf(mValue - i * 10),
                                // xPosition - (textWidth * numSize / 2),
                                // getHeight() - textWidth, textPaint);
                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    canvas.drawLine(xPosition, mHeight/* - (25 * mDensity)*/, xPosition, mHeight/* - (25 * mDensity)*/ - mDensity * ITEM_MIN_HEIGHT, linePaint);/*(xPosition, getPaddingTop() + SCALE_PADDINGTOP, xPosition,
                            mDensity * ITEM_MIN_HEIGHT, linePaint);*/
                }
            }
            Paint bgPaint = new Paint();
            bgPaint.setColor(Color.BLACK);
            bgPaint.setStrokeWidth(28 * mDensity);
            bgPaint.setAntiAlias(true);
            bgPaint.setStyle(Paint.Style.STROKE);
            bgPaint.setStrokeCap(Cap.ROUND);
            
            TextPaint ovalPaint = new TextPaint();
            ovalPaint.setColor(Color.WHITE);
            ovalPaint.setTextSize(22 * mDensity);
            ovalPaint.setStrokeWidth(5);
            ovalPaint.setAntiAlias(true);
            mValue = mValue > mMaxValue ? mMinValue : mValue;
        	mValue = mValue < mMinValue ? mMaxValue : mValue;
            if(mType == 1){
            	
            	canvas.drawLine(mWidth / 2 - Layout.getDesiredWidth("00:00", ovalPaint) / 2, 16 * mDensity, mWidth / 2 + Layout.getDesiredWidth("00:00", ovalPaint) / 2, 16 * mDensity, bgPaint);
            	canvas.drawText((mValue/4 > 9 ? mValue/4 : "0"+mValue/4) +":"+ (mValue%4 == 0 ? "00" : (mValue%4) *15) , mWidth / 2 - Layout.getDesiredWidth("00:00", ovalPaint) / 2, 25 * mDensity, ovalPaint);
            }else{
            	canvas.drawLine(mWidth / 2 - Layout.getDesiredWidth("00小时00分", ovalPaint) / 2, 16 * mDensity, mWidth / 2 + Layout.getDesiredWidth("00小时00分", ovalPaint) / 2, 16 * mDensity, bgPaint);
            	canvas.drawText((mValue/4 > 9 ? mValue/4 : "0"+mValue/4) +"小时"+ (mValue%4 == 0 ? "00" : (mValue%4) *15) +"分" , mWidth / 2 - Layout.getDesiredWidth("00小时00分", ovalPaint) / 2, 25 * mDensity, ovalPaint);
            }
            drawCount += 2 * mLineDivider * mDensity;
        }

        canvas.restore();
    }

    /**
     * 计算没有数字显示位置的辅助方法
     *
     * @param value
     * @param xPosition
     * @param textWidth
     * @return
     */
    private float countLeftStart(int value, float xPosition, float textWidth) {
        float xp = 0f;
        if (value < 20) {
            xp = xPosition - (textWidth * 1 / 2);
        } else {
            xp = xPosition - (textWidth * 2 / 2);
        }
        return xp;
    }

    /**
     * 画中间的蓝色指示线、阴影等。指示线两端简单的用了两个矩形代替
     *
     * @param canvas
     */
    private void drawMiddleLine(Canvas canvas) {

        canvas.save();
        //橙色色指示线
        Paint orangePaint = new Paint();
        orangePaint.setStrokeWidth(3);
        orangePaint.setColor(Color.parseColor("#ff6f9ef6"));
        canvas.drawLine(mWidth / 2, mHeight/* - (25 * mDensity)*/, mWidth / 2, mHeight/4, orangePaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int xPosition = (int) event.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mScroller.forceFinished(true);

                mLastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker(event);
                return false;
            // break;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private void countVelocityTracker(MotionEvent event) {
        mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = mVelocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE,
                    Integer.MAX_VALUE, 0, 0);
        }
    }

    private void changeMoveAndValue() {
        int tValue = (int) (mMove / (mLineDivider * mDensity));
        if (Math.abs(tValue) > 0) {
            
            
//            if (mValue <= mMinValue || mValue > mMaxValue) {
//                mValue = mValue <= 0 ? 0 : mMaxValue;
//                mMove = 0;
//                mScroller.forceFinished(true);
//            }
        	
        	mMove -= tValue * mLineDivider * mDensity;
            if(mValue < mMinValue && mMove <0 ){
            	mValue = mMaxValue;
            }else if(mValue > mMaxValue && mMove > 0){
            	mValue = mMinValue;
            }else {
            	mValue += tValue;
			}
            	
            

            notifyValueChange();
        }
        postInvalidate();
    }

    private void countMoveEnd() {
        int roundMove = Math.round(mMove / (mLineDivider * mDensity));
        mValue = mValue + roundMove;
        mValue = mValue <= 0 ? 0 : mValue;
        mValue = mValue > mMaxValue ? mMaxValue : mValue;

        mLastX = 0;
        mMove = 0;

        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            if (mModType == MOD_TYPE_ONE) {
                mListener.onValueChange(mValue);
            }
            if (mModType == MOD_TYPE_HALF) {
                mListener.onValueChange(mValue);
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX()) { // over
                countMoveEnd();
            } else {
                int xPosition = mScroller.getCurrX();
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }
}