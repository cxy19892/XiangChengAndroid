package com.yzm.sleep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
@SuppressLint("ClickableViewAccessibility")
public class RulerView extends View {

	public interface OnValueChangeListener {
		public void onValueChange(int value);
	}


	// 刻度精度
	public static final int MOD_TYPE_ONE = 1;
	
	// 刻度间隔距离
	private static final int ITEM_ONE_DIVIDER = 52;

	/**刻度尺颜色*/
	private int scaleColor;

	private float mDensity;
	private int mValue = 1990, mMaxValue = 2020, mMinValue = 1920,
			mModType = MOD_TYPE_ONE, mLineDivider = ITEM_ONE_DIVIDER;

	private int mLastX, mMove;
	private int mWidth, mHeight;

	private int mMinVelocity;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	private OnValueChangeListener mListener;

	@SuppressWarnings("deprecation")
	public RulerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mScroller = new Scroller(getContext());
		mDensity = getContext().getResources().getDisplayMetrics().density;

		mMinVelocity = ViewConfiguration.get(getContext())
				.getScaledMinimumFlingVelocity();

	}

	/**
	 * 
	 * 考虑可扩展，但是时间紧迫，只可以支持两种类型效果图中两种类型
	 * 
	 * @param value
	 *            初始值
	 * @param maxValue
	 *            最大值
	 * @param model
	 *            刻度盘精度：<br>
	 *            {@link MOD_TYPE_HALF}<br>
	 *            {@link MOD_TYPE_ONE}<br>
	 */
	public void initViewParam(int defaultValue, int maxValue, int model) {
		switch (model) {
		case MOD_TYPE_ONE:
			mModType = MOD_TYPE_ONE;
			mLineDivider = ITEM_ONE_DIVIDER;
			mValue = defaultValue * 2;
			mMaxValue = maxValue * 2;
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

	/**
	 * 获取当前刻度值
	 * 
	 * @return
	 */
	public int getValue() {
		return mValue;
	}
	public void setValue(int value) {
		mValue = value;
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
//		mLineDivider = mWidth / 7;
		drawScaleLine(canvas);
		drawMiddleLine(canvas);
	}


	/**
	 * 从中间往两边开始画刻度线
	 * 
	 * @param canvas
	 */
	private void drawScaleLine(Canvas canvas) {
		canvas.save();

		Paint linePaint = new Paint();
		linePaint.setStrokeWidth(10);
		linePaint.setColor(Color.parseColor("#8F8F8F"));

		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(getScaleColor());

		int width = mWidth, drawCount = 0;
		float xPosition = 0, textWidth = Layout.getDesiredWidth("0000", textPaint);
		
		//  画刻度板 底部橙色直线
		linePaint.setColor(getScaleColor());
		canvas.drawLine(0, mHeight, mWidth, mHeight, linePaint);
		float textHeight = 0;

		for (int i = 0; drawCount <= 4 * width; i++) {
			int numSize = String.valueOf(mValue + i).length();
			//向左画
			xPosition = (width / 2 - mMove) + i * mLineDivider * mDensity;
			if (xPosition + getPaddingRight() < mWidth) {
				if ((mValue + i) % mModType == 0) {// 画长刻度处 文字
					if (mValue + i <= mMaxValue && mValue + i >= mMinValue) {
						switch (mModType) {
						case MOD_TYPE_ONE:
							if (mWidth / xPosition == 2) {
								//设置滑到中间的文字大小和位置
								textPaint.setTextSize(24 * mDensity);
								textHeight = mHeight / 5 * 3 + textPaint.getTextSize() / 2 - 4 * mDensity;
							}else {
								//设置没有滑到中间的文字大小和位置
								textPaint.setTextSize(12 * mDensity);
								textHeight = mHeight / 5 * 3 + textPaint.getTextSize() / 2;
							}
							canvas.drawText(
									String.valueOf(mValue + i),
									xPosition - Layout.getDesiredWidth("0000", textPaint) / 2,//countLeftStart(mValue + i, xPosition-14,textWidth)
									textHeight, textPaint);
							break;
						default:
							break;
						}
					}
				}
			}
			//向右画
			xPosition = (width / 2 - mMove) - i * mLineDivider * mDensity;
			if (xPosition - getPaddingLeft() > 0) {
				if ((mValue - i) % mModType == 0) {
					if (mValue - i >= mMinValue && mValue - i <= mMaxValue) {
						switch (mModType) {
						case MOD_TYPE_ONE:
							if (mWidth / xPosition == 2) {
								//设置滑到中间的文字大小和位置
								textPaint.setTextSize(24 * mDensity);
								textHeight = mHeight / 5 * 3 + textPaint.getTextSize() / 2 - 4 * mDensity;
							}else {
								//设置没有滑到中间的文字大小和位置
								textPaint.setTextSize(12 * mDensity);
								textHeight = mHeight / 5 * 3 + textPaint.getTextSize() / 2;
							}
							canvas.drawText(
									String.valueOf(mValue - i),
									xPosition - Layout.getDesiredWidth("0000", textPaint) / 2,
									textHeight, textPaint);
							break;
						default:
							break;
						}
					}
				}
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
	 * 画中间的指示线
	 * 
	 * @param canvas
	 */
	private void drawMiddleLine(Canvas canvas) {
		int indexWidth = 6;
		canvas.save();
		Paint orangePaint = new Paint();
		orangePaint.setStrokeWidth(indexWidth);
		//画灰色指标线
		orangePaint.setColor(Color.parseColor("#d9d9d9"));
		canvas.drawLine(mWidth / 2, mHeight, mWidth / 2, 0, orangePaint);
		//画橙色指标线
		orangePaint.setColor(getScaleColor());
		indexWidth = 10;
		canvas.drawLine(mWidth / 2, mHeight, mWidth / 2, mHeight / 7 * 6, orangePaint);
		//画橙色三角形指标标记
		int point1X = mWidth / 2;
		int point2X = (int) (mWidth / 2 + 15 * mDensity);
		int point3X = (int) (mWidth / 2 - 15 * mDensity);
		int point1Y = (int) (20 * mDensity);
		int point2Y = 0;
		int point3Y = 0;
		Paint paint = new Paint();
		paint.setColor(getScaleColor());
		paint.setStyle(Paint.Style.FILL); 
		paint.setStyle(Paint.Style.FILL_AND_STROKE);

        paint.setAntiAlias(true);

         Path path = new Path();

        path.moveTo(point1X, point1Y);

        path.lineTo(point2X, point2Y);


        path.lineTo(point3X, point3Y);

        path.lineTo(point1X, point1Y);

        path.close(); 

        canvas.drawPath(path, paint);
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
			mValue += tValue;
			mMove -= tValue * mLineDivider * mDensity;
//			if (mValue <= mMinValue || mValue > mMaxValue) {//sss2
//				mValue = mValue <= 0 ? 0 : mMaxValue;
//				mMove = 0;
//				mScroller.forceFinished(true);
//			}
//			System.out.println("sss  mValue=" + mValue);
			if (mValue <= mMinValue) {//sss2
				mValue = mValue <= 0 ? 0 : mMaxValue;
				mMove = 0;
				mScroller.forceFinished(true);
			}else if (mValue >= mMaxValue) {
				mValue = mValue <= 0 ? 0 : mMinValue;
				mMove = 0;
				mScroller.forceFinished(true);
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
	
	private int getScaleColor(){
		return this.scaleColor;
	}
	public void setScaleColor(int colorInt) {
		this.scaleColor = colorInt;
	}
}