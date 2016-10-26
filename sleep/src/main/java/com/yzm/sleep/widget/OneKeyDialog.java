package com.yzm.sleep.widget;

import com.yzm.sleep.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class OneKeyDialog extends AlertDialog  implements android.view.View.OnClickListener{

	private Button keybtn;
	private TextView titletv;
	public OneKeyDialog(Context context) {
		super(context);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_onekey_dialog);
		setCancelable(false);
		keybtn = (Button)findViewById(R.id.dialog_btn);
		titletv = (TextView)findViewById(R.id.dialog_text);
		keybtn.setOnClickListener(this);
		setTitle("昨晚枕扣未检测到您的睡眠");
		setButton("我知道了");
	}

	public void setTitle(String title) {
		titletv.setText(title);
	}
	
	public void setMessage(String message) {
		titletv.setText(message);
		titletv.setTextSize(14);
	}

	public void setButton(String btntext){
		keybtn.setText(btntext);
	}
	
	@Override
	public void onClick(View v) {
		if(v == keybtn){
			OneKeyDialog.this.cancel();
		}
	}
}
