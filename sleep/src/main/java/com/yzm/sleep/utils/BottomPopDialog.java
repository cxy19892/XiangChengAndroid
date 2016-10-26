package com.yzm.sleep.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.Constant;
import com.yzm.sleep.R;

public class BottomPopDialog extends AlertDialog implements android.view.View.OnClickListener {
//	private Context mContext;
	private PopDialogClickListener mlistener;
	private RelativeLayout relAskCallPhoneNum;
	private Button confrmBtn, cancleBtn;
	private TextView askTips;
	
	protected BottomPopDialog(Context context) {
		super(context, R.style.select_dialog);
		// TODO Auto-generated constructor stub
	}

	public BottomPopDialog(Context context, PopDialogClickListener clickListener){
		super(context, R.style.select_dialog);
//		this.mContext = context;
		this.mlistener = clickListener;
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.dialog_bottom_pop_layout);
		getWindow().setWindowAnimations(R.style.bottom_animation);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Constant.screenHeight * 0.4));
        getWindow().setBackgroundDrawable(new ColorDrawable(R.color.bg_color));
        getWindow().setGravity(Gravity.BOTTOM);
		setCanceledOnTouchOutside(true);
		initViews();
	}



	private void initViews() {
		relAskCallPhoneNum = (RelativeLayout) findViewById(R.id.rel_ask_call_phone_num);
		confrmBtn = (Button) findViewById(R.id.btn_insure);
		cancleBtn = (Button) findViewById(R.id.btn_cancle);
		askTips   = (TextView) findViewById(R.id.tv_tips_ask);
	}
	/**
	 * 根据传入，确定显示的界面
	 * @param viewsid
	 * 1： 显示二选一界面(一条提示两个button) <br>
	 */
	public void setShowViews(int viewsid, String tips1, String tips2){
		switch (viewsid) {
		case 1:
			relAskCallPhoneNum.setVisibility(View.VISIBLE);
			askTips.setText(tips1);
			confrmBtn.setOnClickListener(this);
			cancleBtn.setOnClickListener(this);
			break;

		default:
			break;
		}
	}
	
	public void setShowViewsAndBtn(int viewsid, String tips1, String tips2, String btnCancle, String btnComfrm){
		switch (viewsid) {
		case 1:
			relAskCallPhoneNum.setVisibility(View.VISIBLE);
			askTips.setText(tips1);
			confrmBtn.setText(btnComfrm);
			cancleBtn.setText(btnCancle);
			confrmBtn.setOnClickListener(this);
			cancleBtn.setOnClickListener(this);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_insure:
			if(mlistener!=null){
				mlistener.PopDialogClick(1);
			}
			break;
		case R.id.btn_cancle:
			if(mlistener!=null){
				mlistener.PopDialogClick(0);
			}
			this.cancel();
			break;
		default:
			break;
		}
		
	}
	
	
	public interface PopDialogClickListener{
		public void PopDialogClick(int clickid);
	}
}
