package com.yzm.sleep.utils;

import com.yzm.sleep.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class ProgressUtils extends ProgressDialog{

	public ProgressUtils(Context context) {
		super(context, R.style.add_dialog);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_dialog);
	}
}
