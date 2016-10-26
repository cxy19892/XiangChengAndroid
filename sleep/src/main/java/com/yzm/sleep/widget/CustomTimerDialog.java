package com.yzm.sleep.widget;

import com.yzm.sleep.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CustomTimerDialog extends AlertDialog {

	private NumberPicker hourPicker, minPicker;
	private TextView btnCancle, btnCommit;
	
	public CustomTimerDialog(Context context) {
		super(context, R.style.timer_dialog);
		setCanceledOnTouchOutside(false);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_timepicker);
		initView();
	}

	public void setTime(int hourNum, int minNum){
		this.hourNum = hourNum;
		this.minNum = minNum;
	}
	
	private int hourNum = 0;
	private int minNum = 0;
	
	private void initView() {
		hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
		minPicker = (NumberPicker) findViewById(R.id.minutePicker);
		btnCancle = (TextView) findViewById(R.id.timepicker_cacel);
		btnCommit = (TextView) findViewById(R.id.timepicker_submit);
		hourPicker.setMaxValue(23);
		hourPicker.setMinValue(0);
		hourPicker.setFocusable(true);
		hourPicker.setFocusableInTouchMode(true);
		hourPicker.setValue(hourNum);
		minPicker.setMaxValue(59);
		minPicker.setMinValue(0);
		minPicker.setFocusable(true);
		minPicker.setFocusableInTouchMode(true);
		minPicker.setValue(minNum);
		btnCancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(onSelectionListener != null)
					onSelectionListener.cancleClick();
			}
		});
		btnCommit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(onSelectionListener != null){
					int hour = hourPicker.getValue();
					int min = minPicker.getValue();
					String hourString = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
					String minString = min < 10 ? "0" + String.valueOf(min) : String.valueOf(min);
					onSelectionListener.commitClick(hourString + ":" + minString);
				}

			}
		});
	}

	private OnSelectionListener onSelectionListener;
	
	public void setOnSelectionListener(OnSelectionListener onSelectionListener){
		this.onSelectionListener = onSelectionListener;
	}
	
	public interface OnSelectionListener{
		public void cancleClick();
		public void commitClick(String time);
	}
}
