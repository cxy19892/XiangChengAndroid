package com.yzm.sleep.model;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.yzm.sleep.R;

public class SleepTargetDialog extends Dialog {

	public SleepTargetDialog(Context context) {
		super(context, R.style.select_dialog);
	}

	public SleepTargetDialog(Context context, int animationStyle) {
		super(context, R.style.select_dialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_sleep_target);
		findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
