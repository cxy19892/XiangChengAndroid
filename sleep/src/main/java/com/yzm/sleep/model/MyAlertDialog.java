package com.yzm.sleep.model;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yzm.sleep.R;

public class MyAlertDialog extends AlertDialog implements android.view.View.OnClickListener {

	public View view;
	private TextView tv_1, tv_2, tv_3, tv_4, tv_bottom;
	private MyOnClickListener tv1ClickListener, tv2ClickListener, tv3ClickListener, tv4ClickListener, tv5ClickListener;
	private int animationStyle;
	protected MyAlertDialog(Context context) {
		super(context, R.style.pop_dialog);
	}
	
	public MyAlertDialog(Context context, int animationStyle) {
		super(context,R.style.pop_dialog);
		this.animationStyle = animationStyle;
	}
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popupwindow_botton_for_four);
		this.getWindow().setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL);
		if (animationStyle != 0) {
			this.getWindow().setWindowAnimations(animationStyle);
		}
		setCanceledOnTouchOutside(true);
		initViews();
	}

	private void initViews(){
		tv_1 = (TextView) findViewById(R.id.pop_tv_top);
		tv_2 = (TextView) findViewById(R.id.pop_tv_secend);
		tv_3 = (TextView) findViewById(R.id.pop_tv_thrid);
		tv_4 = (TextView) findViewById(R.id.pop_tv_four);
		tv_bottom = (TextView) findViewById(R.id.pop_tv_bottom);
		
//		tv_1.setOnClickListener(this);
		tv_2.setOnClickListener(this);
		tv_3.setOnClickListener(this);
		tv_4.setOnClickListener(this);
		tv_bottom.setOnClickListener(this);
	}

	public void setTV1(String strtv1){
		tv_1.setBackgroundResource(R.color.cbg_color);
		tv_1.setText(strtv1);
	}
	public void setTV1(String strtv1,int isvisiable){
		tv_1.setVisibility(isvisiable);
		tv_1.setBackgroundResource(R.color.cbg_color);
		tv_1.setText(strtv1);
	}
	public void setTV2(String strtv2, MyOnClickListener ClickListener, int isvisiable){
		tv_2.setVisibility(isvisiable);
		this.tv2ClickListener = ClickListener;
		tv_2.setText(strtv2);
	}
	public void setTV3(String strtv3, MyOnClickListener ClickListener, int isvisiable){
		tv_3.setVisibility(isvisiable);
		this.tv3ClickListener = ClickListener;
		tv_3.setText(strtv3);
	}
	public void setTV4(String strtv4, MyOnClickListener ClickListener, int isvisiable){
		tv_4.setVisibility(isvisiable);
		this.tv4ClickListener = ClickListener;
		tv_4.setText(strtv4);
	}
	public void setTVbottom(String strtvbottom, MyOnClickListener ClickListener, int isvisiable){
		tv_bottom.setVisibility(isvisiable);
		this.tv5ClickListener = ClickListener;
		tv_bottom.setText(strtvbottom);
	}
	
	
	public interface MyOnClickListener{
		public void Onclick(View v);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_tv_top:
			if(null != tv1ClickListener){
				tv1ClickListener.Onclick(v);
			}
			break;
		case R.id.pop_tv_secend:
			if(null != tv2ClickListener){
				tv2ClickListener.Onclick(v);
			}
					break;
		case R.id.pop_tv_thrid:
			if(null != tv3ClickListener){
				tv3ClickListener.Onclick(v);
			}
			break;
		case R.id.pop_tv_four:
			if(null != tv4ClickListener){
				tv4ClickListener.Onclick(v);
			}
			break;
		case R.id.pop_tv_bottom:
			if(null != tv5ClickListener){
				tv5ClickListener.Onclick(v);
			}
			break;
		default:
			break;
		}
		
	}
}
