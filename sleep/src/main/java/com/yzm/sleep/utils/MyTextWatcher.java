package com.yzm.sleep.utils;

import com.yzm.sleep.activity.alar.RemindAlarmEditActivity;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 监听edittext 的类
 * 1用于限制edittext 输入字符的长度   
 * 2用于禁止emoji表情的输入
 * @author chen
 *
 */
public class MyTextWatcher  implements TextWatcher {
	//输入表情前的光标位置
	private int cursorPos;    
	//输入表情前EditText中的文本
	private String inputAfterText;    
	//是否重置了EditText的内容
	private boolean resetText;  
	private CharSequence temp , temp1;
	private int selectionStart;
	private Context mContext;
	private EditText mEd;
	private int limitLenth = 10;
	private TextWatcherCallBack mTextWatcherCallBack;

	/**
	 * 用于限制edittext 输入字符的长度  的回调 
	 * @author chen
	 *
	 */
	public interface TextWatcherCallBack{
		/**
		 * 返回剩余的可输入字符数
		 * @param num
		 */
		public void onTextNumsback(int num);
	}

	/**
	 * 监听edittext 的类
	 * @param ed  被监听的edittext
	 * @param mcontext  上下文对象不可以传null 用于弹出toast提示
	 * @param limitLenth  限制的最大输入数
	 * @param mTextWatcherCallBack  回调接口  不需要返回剩余字符长度的可以为null
	 */
	public MyTextWatcher(EditText ed, Context mcontext, int limitLenth, TextWatcherCallBack mTextWatcherCallBack){
		this.mEd = ed;
		this.mContext = mcontext;
		this.limitLenth = limitLenth;
		this.mTextWatcherCallBack = mTextWatcherCallBack;
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if (!resetText) {
			cursorPos = mEd.getSelectionEnd();
			// 这里用s.toString()而不直接用s是因为如果用s，
			// 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
			// inputAfterText也就改变了，那么表情过滤就失败了
			inputAfterText= s.toString();
		}
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before,
			int count) {
		temp1 = s;
		if (!resetText) {
			try {
				if (count >= 2 && (cursorPos + count)<=s.length()) {//表情符号的字符长度最小为2
					temp = s.subSequence(cursorPos, cursorPos + count);
					if (Util.containsEmoji(temp.toString())) {
						resetText = true;
						showTextToast("不支持输入表情符号");
						//是表情符号就将文本还原为输入表情符号之前的内容
						mEd.setText(inputAfterText);
						temp = mEd.getText();
						if (temp instanceof Spannable) {
							Spannable spanText = (Spannable) temp;
							Selection.setSelection(spanText, temp.length());
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			resetText = false;
		}
		
	}

	@Override
	public void afterTextChanged(Editable s) {//如果不限制字数这个方法可以不用实现
		if(resetText){
			return ;
		}
		resetText = true;
		int number = limitLenth - s.length();
		if(number >-1){
			try {
				if(null != mTextWatcherCallBack){
					mTextWatcherCallBack.onTextNumsback(number);
					
				}
				selectionStart = mEd.getSelectionStart();
				if (temp1.length() > limitLenth) {
//					s.delete(selectionStart - 1, cursorPos);
					if(number <= 0){
						showTextToast("限制输入"+limitLenth+"个字符");
					}
					int tempSelection = cursorPos;
					mEd.setText(s.toString().substring(0, limitLenth));
					mEd.setSelection(tempSelection);//设置光标在最后
					
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}else{//当用户使用复制粘贴的时候不能使用上面的方法
			try {
				if(null != mTextWatcherCallBack){
					mTextWatcherCallBack.onTextNumsback(0);
					
				}
				if ((temp1.length()+inputAfterText.length()) > limitLenth) {
					if(number <= 0){
						showTextToast("限制输入"+limitLenth+"个字符");
					}
					selectionStart = mEd.getSelectionStart();
					cursorPos = mEd.getSelectionEnd();
//					s.delete(selectionStart-1, cursorPos);
					mEd.setText(s.toString().substring(0, limitLenth));
					mEd.setSelection(limitLenth);//设置光标在最后
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		resetText = false;
		
	} 
	
	private Toast mytoast;
	private Handler mhandler = new Handler();
	private void showTextToast(String msg) {
        if (mytoast == null) {
        	mytoast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
//        	mytoast.setGravity(Gravity.BOTTOM, 0, 0);
        	mhandler.postDelayed(new Runnable() {
    			
    			@Override
    			public void run() {
    				if(null != mytoast){
    					mytoast.cancel();
    					mytoast = null;
    				}
    			}
    		}, 2000);
        	mytoast.show();
        }
        
    }
//	
//	private void toastCenter(String toastStr){
//		Toast toast=Toast.makeText(mContext, toastStr,
//				Toast.LENGTH_SHORT); 
//		 toast.setGravity(Gravity.CENTER, 0, 0);  
//	     toast.show(); 
//	}
}
