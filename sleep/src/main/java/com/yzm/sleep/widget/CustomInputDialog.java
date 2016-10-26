package com.yzm.sleep.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.utils.Util;

public class CustomInputDialog extends Dialog implements
		android.view.View.OnClickListener {
	private TextView tvTitle, tvTag;
	private ImageButton cancleBtn, confirmBtn;
	private EditText editText;
	private String title, tag, current;
	private InputCallBack mCallback;
	private Context mContext;
	/** 输入框小数的位数 */
	private static final int DECIMAL_DIGITS = 1;
	private InputMethodManager imm;

	protected CustomInputDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	public CustomInputDialog(Context context, InputCallBack callback) {
		super(context, R.style.MyDialogStyle);
		this.mCallback = callback;
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_input_edit);
		this.getWindow().setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
		this.getWindow().setWindowAnimations(R.style.bottom_animation);
		this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(true);
		initViews();
	}

	public void setData(InputClass inputClass) {
		this.title = inputClass.title;
		this.tag = inputClass.tag;
		this.current = inputClass.current;
	}

	private void initViews() {
		tvTitle = (TextView) findViewById(R.id.tv_roll_title);
		cancleBtn = (ImageButton) findViewById(R.id.roll_btn_cancle);
		confirmBtn = (ImageButton) findViewById(R.id.roll_btn_confirm);
		editText = (EditText) findViewById(R.id.editText);
		tvTag = (TextView) findViewById(R.id.tvTag);
		cancleBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		editText.setFilters(new InputFilter[] { lengthfilter });
		tvTitle.setText(title);
		tvTag.setText(tag);
		editText.setText(current);
		editText.setSelection(current.length());
		editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				current = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s.toString())) {
					try {
						double tem1 = Double.parseDouble(s.toString());
						if (tem1 > 200) {
							Util.show(mContext, "超过了我们地记录了");
							editText.setText("");
						}
						
					} catch (Exception e) {
						editText.setText("");
					}
				}
			}
		});
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				showKeyboard(editText);
			}
		}, 200);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.roll_btn_cancle) {
			cancel();
		}

		if (v.getId() == R.id.roll_btn_confirm) {
			getWeightValue();
		}
	}

	private void getWeightValue() {
		String weight = "0";
		if (mCallback != null) {
			String trim = editText.getText().toString().trim();
			if(TextUtils.isEmpty(trim)){
				Util.show(mContext, "体重不能为空");
				return;
			}
			weight = trim;
			if (trim.length() >= 2) {
				if (!trim.startsWith("0.") && trim.startsWith("0")) {
					weight = trim.substring(1);
				}
				
				if ("0.".endsWith(trim)) {
					weight = "0";
				}
			}
			mCallback.setResault(weight);
			cancel();
		}
	}
	@Override
	public void cancel() {
		hideKeyboardB(editText);
		super.cancel();
	}

	public interface InputCallBack {
		public void setResault(String s);
	}

	public static class InputClass {
		public String title;
		public String tag;
		public String current;
	}

	/**
	 * 设置小数位数控制
	 */
	InputFilter lengthfilter = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			// 删除等特殊字符，直接返回
			if ("".equals(source.toString())) {
				return null;
			}
			String dValue = dest.toString();
			String[] splitArray = dValue.split("\\.");
			if (splitArray.length > 1) {
				String dotValue = splitArray[1];
				int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
				if (diff > 0) {
					return source.subSequence(start, end - diff);
				}
			}
			return null;
		}
	};
	
	/**
	 * 显示键盘
	 * @param inputView 接收输入的view
	 */
	private void showKeyboard(EditText inputView){
		inputView.setFocusable(true);
		inputView.setFocusableInTouchMode(true);
		inputView.requestFocus();
		if(null==imm){
			imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		imm.showSoftInput(inputView, InputMethodManager.SHOW_FORCED);
	}
	/**
	 * 隐藏键盘
	 * @param inputView
	 */
	public boolean hideKeyboardB(EditText inputView){
		if(null==imm)
			return true;
		try{
			imm.hideSoftInputFromWindow(inputView.getWindowToken(), 0);
		}catch(Exception e){
			
		}
		return true;
	}

}
