package com.yzm.sleep.model;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;

public class MyDialog extends Dialog {

	private Window window;
	public View view;
	private Context context;

	/**
	 * 
	 * @param context
	 * @param x
	 * @param y
	 * @param layoutResId
	 * @param animationStyle
	 * @param gravityId
	 * @param width
	 *            弹出框宽度占屏幕的比例,如果为0则去wrap值
	 * @param height
	 *            弹出框高度占屏幕的比例，如果为0则去wrap值
	 */
	public MyDialog(Context context, int x, int y, int layoutResId,
			int animationStyle, int gravityId, float width, float height) {
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		windowDisplay(x, y, animationStyle, gravityId, layoutResId, width,
				height);
		setCanceledOnTouchOutside(false);
	}

	public void windowDisplay(int x, int y, int animationStyle, int gravityId,
			int layoutResId, float width, float height) {
		window = this.getWindow();
		this.view = LayoutInflater.from(this.context)
				.inflate(layoutResId, null);
		window.setContentView(this.view);
		if (animationStyle != 0) {
			window.setWindowAnimations(animationStyle);
		}
		window.setBackgroundDrawable(MyApplication.instance()
				.getResources().getDrawable(R.color.transparent));
		if (width == 0 && height != 0) {
			window.setLayout(LayoutParams.WRAP_CONTENT,
					(int) (Constant.screenHeight * height));
		} else if (width != 0 && height == 0) {
			window.setLayout((int) (Constant.screenWidht * width),
					LayoutParams.WRAP_CONTENT);
		} else if (width != 0 && height != 0) {
			window.setLayout((int) (Constant.screenWidht * width),
					(int) (Constant.screenHeight * height));
		} else if (width == 0 && height == 0) {
			window.setLayout(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}

		window.setGravity(gravityId);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = x;
		wl.y = y;
		window.setAttributes(wl);
	}

}
