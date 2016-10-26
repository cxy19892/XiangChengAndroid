package com.yzm.sleep.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class CustomPopupWindow extends PopupWindow {
	private Activity activity;
	public CustomPopupWindow(Context context,View view, int width, int height) {
		super(context);
		this.activity = (Activity) context;
		this.setContentView(view);
		this.setWidth(width);
		this.setHeight(height);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setBackgroundDrawable(new ColorDrawable(0x00000000));
      
	}

	@Override
	public void dismiss() {
		super.dismiss();
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();  
        params.alpha=1f;
		activity.getWindow().setAttributes(params);
	}
	
}
