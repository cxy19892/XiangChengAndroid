package com.yzm.sleep.widget;

import com.yzm.sleep.utils.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class CustomGallery extends Gallery {

	public CustomGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		int kEvent;
		if (isScrollingLeft(e1, e2)) {
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);

		return false;

	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	
	private boolean isScrolling = false;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(onListener != null){
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if(!isScrolling){
					isScrolling = true;
					onListener.pageScrollStart();
				}
				break;
			case MotionEvent.ACTION_UP:
				if(isScrolling){
					onListener.pageScrollEnd();
					isScrolling = false;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				if(isScrolling){
					onListener.pageScrollEnd();
					isScrolling = false;
				}
				break;
			default:
				break;
			}
		}
		
		return super.onTouchEvent(event);
	}




	private OnPageScrollListener onListener;
	
	public void setOnPageScrollListener(OnPageScrollListener onListener){
		this.onListener = onListener;
	}
	
	public interface OnPageScrollListener{
		public void pageScrollStart();
		public void pageScrollEnd();
	}
}
