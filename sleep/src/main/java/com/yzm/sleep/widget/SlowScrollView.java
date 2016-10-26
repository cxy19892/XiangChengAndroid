package com.yzm.sleep.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class SlowScrollView extends ScrollView {

	public SlowScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SlowScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SlowScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
     * 滑动事件
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 4);
    }

}
