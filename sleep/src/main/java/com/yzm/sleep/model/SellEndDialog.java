package com.yzm.sleep.model;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.yzm.sleep.Constant;
import com.yzm.sleep.R;

public class SellEndDialog extends AlertDialog {

	public SellEndDialog(Context context) {
		super(context, R.style.pop_dialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_center_layout);
		this.getWindow().setGravity(Gravity.CENTER);
		this.getWindow().setLayout((int) (Constant.screenWidht * 0.86f),LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(true);
		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
	}

}
