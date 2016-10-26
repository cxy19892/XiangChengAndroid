package com.yzm.sleep.widget;

import com.yzm.sleep.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RemindputdevintoPillow extends AlertDialog implements android.view.View.OnClickListener {
	
	private Button btn_ok;

	protected RemindputdevintoPillow(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public RemindputdevintoPillow(Context context) {
		super(context);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_pillow_start2use_tips);
		btn_ok = (Button)findViewById(R.id.dialog_pillow_use_ok);
		btn_ok.setOnClickListener(this);
	}
	
	public void setTitle(String title) {
		((TextView)findViewById(R.id.dialog_pillow_title)).setText(title);
	}

	public void setSub(String sub) {
		((TextView)findViewById(R.id.dialog_pillow_sub)).setText(sub);
	}

	public void setOnOKClickListener(String left, MyOnClickListener onClickListener){
		btn_ok.setText(left);
		this.onClickListenerok = onClickListener;
	}
	
	private MyOnClickListener onClickListenerok;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_pillow_use_ok:
			if(onClickListenerok != null)
				onClickListenerok.Onclick(v);
			break;
		default:
			break;
		}
	}
	
	public interface MyOnClickListener{
		public void Onclick(View v);
	}

	
}
