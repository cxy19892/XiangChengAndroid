package com.yzm.sleep.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.R;

public class CustomDialog extends AlertDialog implements android.view.View.OnClickListener{

	protected CustomDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public CustomDialog(Context context) {
		super(context);
	}
	
	private Button btnLeft, btnRight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_bluetooth_tip);
		setCancelable(false);
		btnLeft = (Button)findViewById(R.id.dialog_bluetooth_leftbtn);
		btnRight = (Button)findViewById(R.id.dialog_bluetooth_rightbtn);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
	}

	public void setTitle(String title) {
		((TextView)findViewById(R.id.dialog_bluetooth_title)).setText(title);
	}

	public void setSub(String sub) {
		((TextView)findViewById(R.id.dialog_bluetooth_sub)).setText(sub);
	}
	
	public void setSubGone(){
		((TextView)findViewById(R.id.dialog_bluetooth_sub)).setVisibility(View.GONE);
	}

	public void setOnLeftClickListener(String left, MyOnClickListener onClickListener){
		btnLeft.setText(left);
		this.onClickListenerLeft = onClickListener;
	}

	public void setOnRightClickListener(String left, MyOnClickListener onClickListener){
		btnRight.setText(left);
		this.onClickListenerRight = onClickListener;
	}
	
	public void setOnLeftClickColorfulListener(String left, MyOnClickListener onClickListener, int color){
		btnLeft.setTextColor(color);
		btnLeft.setText(left);
		this.onClickListenerLeft = onClickListener;
	}

	public void setOnRightClickColorfulListener(String left, MyOnClickListener onClickListener, int color){
		btnRight.setTextColor(color);
		btnRight.setText(left);
		this.onClickListenerRight = onClickListener;
	}
	
	private MyOnClickListener onClickListenerLeft;
	private MyOnClickListener onClickListenerRight;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_bluetooth_leftbtn:
			if(onClickListenerLeft != null)
				onClickListenerLeft.Onclick(v);
			break;
		case R.id.dialog_bluetooth_rightbtn:
			if(onClickListenerRight != null)
				onClickListenerRight.Onclick(v);
			break;
		default:
			break;
		}
	}
	
	public interface MyOnClickListener{
		public void Onclick(View v);
	}

}
