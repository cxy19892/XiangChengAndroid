package com.yzm.sleep.model;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.utils.Util;

public class SelectLCDialog extends Dialog {

	private TextView lcNum;
	private int num = 1;
	private int maxNum;
	private IntClickBuyListener listener;
	private Context context;
	
	public interface IntClickBuyListener{
		public void clickBuy(int lcNum);
	}
	
	public SelectLCDialog(Context context, int maxNum, IntClickBuyListener listener) {
		super(context, R.style.pop_dialog);
		this.context = context;
		this.maxNum = maxNum;
		this.listener  =listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_lc);
		this.getWindow().setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL);
		this.getWindow().setWindowAnimations(R.style.bottom_animation);
		
		lcNum = (TextView) findViewById(R.id.lc_num);
		lcNum.setText(String.valueOf(num));
		findViewById(R.id.lc_minus).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(num <= 1)
					return;
				num --;
				lcNum.setText(String.valueOf(num));
			}

		});
		
		findViewById(R.id.lc_add).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (num >= maxNum) {
					Util.show(context, "不能再多了");
					return;
				}
				num ++;
				lcNum.setText(String.valueOf(num));
			}
		});
		
		findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener == null)
					return;
				listener.clickBuy(num);
			}
		});
	}
	

}
