package com.yzm.sleep.utils;

import com.yzm.sleep.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class PunchTipDialog extends AlertDialog implements android.view.View.OnClickListener{

	public PunchTipDialog(Context context) {
		super(context, R.style.MyDialogStyle);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_punch_tip);
		findViewById(R.id.punch_tip_enter).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.punch_tip_enter:
			dismiss();
			break;
		default:
			break;
		}
	}

	
	
}
