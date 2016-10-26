package com.yzm.sleep.model;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.yzm.sleep.Constant;
import com.yzm.sleep.R;

public class myProsessDialog extends AlertDialog {
	String msg;
	
	protected myProsessDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public myProsessDialog(Context context, String msg){
		super(context);
		this.msg = msg;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_prosess_layout);
		this.getWindow().setGravity(Gravity.CENTER);
		this.getWindow().setLayout((int) (Constant.screenWidht * 0.5f),LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(true);
		if(!TextUtils.isEmpty(msg)){
			((TextView)findViewById(R.id.dialog_tv_message)).setText(msg);
		}
	}
	
	

}
