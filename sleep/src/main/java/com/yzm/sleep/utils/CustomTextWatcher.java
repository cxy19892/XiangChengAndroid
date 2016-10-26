package com.yzm.sleep.utils;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {
	// 输入表情前EditText中的文本
	// 是否重置了EditText的内容
	private TextWatcherCallBack mTextWatcherCallBack;
	private EditText mEditText;
	private int  temp=0, selectIndex=0 ,changNum=0;
	private Context context;

	/**
	 * 用于限制edittext 输入字符的长度 的回调
	 * 
	 * @author chen
	 * 
	 */
	public interface TextWatcherCallBack {
		/**
		 * 输入的字数
		 */
		public void onTextNumsBack(int num, String text);
	}

	/**
	 * 监听edittext 的类
	 * 
	 * @param ed
	 *            被监听的edittext
	 * @param mcontext
	 *            上下文对象不可以传null 用于弹出toast提示
	 * @param mTextWatcherCallBack
	 */
	public CustomTextWatcher(Context context, EditText ed, TextWatcherCallBack mTextWatcherCallBack) {
		this.context=context;
		this.mEditText=ed;
		this.mTextWatcherCallBack = mTextWatcherCallBack;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		this.temp=count;
		this.selectIndex=mEditText.getSelectionStart();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		this.changNum = count - this.temp;
		if(count - this.temp >= 7){
			mEditText.setText(EaseSmileUtils.getSmiledText(context, s));
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		int num = 0;
		if (s != null) {
			num = s.toString().length();
		}
		
		int index= this.selectIndex + (this.changNum);
		if(index < 0)
			index = 0;
		Selection.setSelection(s, index);
		
		if (mTextWatcherCallBack != null) 
			mTextWatcherCallBack.onTextNumsBack(num, s.toString());
		
	}
}
