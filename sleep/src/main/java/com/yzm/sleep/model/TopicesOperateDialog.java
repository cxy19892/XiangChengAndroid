package com.yzm.sleep.model;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;

/**
 * 话题操作
 * @author tianxun
 *
 */
public class TopicesOperateDialog extends Dialog {
	
	private Window window;
	public View view;
	private Context context;

	public TopicesOperateDialog(Context context, int x, int y, int layoutResId,
			int animationStyle, int gravityId, float width, float height) {
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		windowDisplay(x, y, animationStyle, gravityId, layoutResId, width,
				height);
		setCanceledOnTouchOutside(false);
	}

	public void windowDisplay(int x, int y, int animationStyle, int gravityId,int layoutResId, float width, float height) {
		window = this.getWindow();
		this.view = LayoutInflater.from(this.context).inflate(layoutResId, null);
		window.setContentView(this.view);
		if (animationStyle != 0) {
			window.setWindowAnimations(animationStyle);
		}
		window.setBackgroundDrawable(MyApplication.instance().getResources().getDrawable(R.color.transparent));
		if (width == 0 && height != 0) {
			window.setLayout(LayoutParams.WRAP_CONTENT,(int) (Constant.screenHeight * height));
		} else if (width != 0 && height == 0) {
			window.setLayout((int) (Constant.screenWidht * width),LayoutParams.WRAP_CONTENT);
		} else if (width != 0 && height != 0) {
			window.setLayout((int) (Constant.screenWidht * width),(int) (Constant.screenHeight * height));
		} else if (width == 0 && height == 0) {
			window.setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		}

		window.setGravity(gravityId);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = x;
		wl.y = y;
		window.setAttributes(wl);
	}
	
	/**
	 * 获取TopicesOperateDialog 实例
	 * @param isSelf
	 */
	private static TopicesOperateDialog opDialog=null;
	public static TopicesOperateDialog getInstance(Context context,boolean isSelf,android.view.View.OnClickListener click){
		Button report = null,delete=null,cancel=null,setTop=null;
		opDialog=new TopicesOperateDialog(context, 0, 0, R.layout.dialog_topices_op, R.style.bottom_animation, Gravity.BOTTOM,  0.96f, 0.0f);
		opDialog.setCanceledOnTouchOutside(true);
		report=(Button) opDialog.findViewById(R.id.btn_report);
		delete=(Button) opDialog.findViewById(R.id.btn_delete);
		cancel=(Button) opDialog.findViewById(R.id.btn_cancel);
		setTop=(Button) opDialog.findViewById(R.id.btn_set_top);
		report.setOnClickListener(click);
		delete.setOnClickListener(click);
		cancel.setOnClickListener(click);
		setTop.setOnClickListener(click);
		setTop.setVisibility(View.GONE);
		if(isSelf){//自己的话题
			report.setVisibility(View.GONE);
		}else {
			delete.setVisibility(View.GONE);
			opDialog.findViewById(R.id.view_v).setVisibility(View.VISIBLE);
		}
		opDialog.show();
		return opDialog;
	}
	public static TopicesOperateDialog getInstance(Context context,boolean isSelf,boolean isManager,android.view.View.OnClickListener click){
		Button report = null,delete=null,cancel=null,setTop=null;
		opDialog=new TopicesOperateDialog(context, 0, 0, R.layout.dialog_topices_op, R.style.bottom_animation, Gravity.BOTTOM,  0.96f, 0.0f);
		opDialog.setCanceledOnTouchOutside(true);
		report=(Button) opDialog.findViewById(R.id.btn_report);
		delete=(Button) opDialog.findViewById(R.id.btn_delete);
		cancel=(Button) opDialog.findViewById(R.id.btn_cancel);
		setTop=(Button) opDialog.findViewById(R.id.btn_set_top);
		report.setOnClickListener(click);
		delete.setOnClickListener(click);
		cancel.setOnClickListener(click);
		setTop.setOnClickListener(click);

		if (isManager) {
			if(isSelf){//自己的话题
				report.setVisibility(View.GONE);
			}
			delete.setVisibility(View.VISIBLE);
			setTop.setVisibility(View.VISIBLE);
		}else{
			setTop.setVisibility(View.GONE);
			if(isSelf){//自己的话题
				report.setVisibility(View.GONE);
			}else {
				delete.setVisibility(View.GONE);
				opDialog.findViewById(R.id.view_v).setVisibility(View.VISIBLE);
			}
		}
		opDialog.show();
		return opDialog;
	}
	public static TopicesOperateDialog getInstance(Context context,String quanxian,android.view.View.OnClickListener click){
		Button report = null,delete=null,cancel=null,setTop=null;
		opDialog=new TopicesOperateDialog(context, 0, 0, R.layout.dialog_topices_op, R.style.bottom_animation, Gravity.BOTTOM,  0.96f, 0.0f);
		opDialog.setCanceledOnTouchOutside(true);
		report=(Button) opDialog.findViewById(R.id.btn_report);
		delete=(Button) opDialog.findViewById(R.id.btn_delete);
		cancel=(Button) opDialog.findViewById(R.id.btn_cancel);
		setTop=(Button) opDialog.findViewById(R.id.btn_set_top);
		report.setOnClickListener(click);
		delete.setOnClickListener(click);
		cancel.setOnClickListener(click);
		setTop.setOnClickListener(click);
		if (quanxian.contains("-1")) {//无任何权限
		}
		if (!quanxian.contains("1")) {//删除权限
			delete.setVisibility(View.GONE);
			opDialog.findViewById(R.id.view_v).setVisibility(View.VISIBLE);
		}
		if (!quanxian.contains("2")) {//话题置顶权限
			setTop.setVisibility(View.GONE);
		}
		if (!quanxian.contains("3")) {//举报话题权限
			report.setVisibility(View.GONE);
		}
		opDialog.show();
		return opDialog;
	}
	
	public static TopicesOperateDialog getInstance(Context context,boolean isSelf,String opText,android.view.View.OnClickListener click){
		Button report = null,delete=null,cancel=null,setTop = null;
		opDialog=new TopicesOperateDialog(context, 0, 0, R.layout.dialog_topices_op, R.style.bottom_animation, Gravity.BOTTOM,  0.96f, 0.0f);
		opDialog.setCanceledOnTouchOutside(true);
		report=(Button) opDialog.findViewById(R.id.btn_report);
		delete=(Button) opDialog.findViewById(R.id.btn_delete);
		cancel=(Button) opDialog.findViewById(R.id.btn_cancel);
		setTop=(Button) opDialog.findViewById(R.id.btn_set_top);
		report.setOnClickListener(click);
		delete.setOnClickListener(click);
		cancel.setOnClickListener(click);
//		setTop.setOnClickListener(click);
		setTop.setVisibility(View.GONE);
		opDialog.findViewById(R.id.view_v1).setVisibility(View.INVISIBLE);
		if(isSelf){
			report.setVisibility(View.GONE);
			delete.setVisibility(View.VISIBLE);
		}else {
			delete.setVisibility(View.GONE);
			report.setText(opText);
			opDialog.findViewById(R.id.view_v).setVisibility(View.INVISIBLE);
		}
		
		opDialog.show();
		return opDialog;
	}
	
}
