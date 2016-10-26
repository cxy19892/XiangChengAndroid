package com.yzm.sleep.model;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.EnvironmentBean;

public class BedroomSuggestDialog extends AlertDialog {
    
	private EnvironmentBean data;
	private LinearLayout rlSu, rlpic;
	private TextView tvSu;
	private TextView title;
	private ImageView pic, suggestimg;
	private boolean isDismiss = true, closeAnim;
	private View view;
	
    public BedroomSuggestDialog(Context context) {
        super(context);
    }
    
    public BedroomSuggestDialog(Context context,int theme) {
        super(context, theme);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = getLayoutInflater().inflate(R.layout.dialog_bedroom_suggest, null);
    	rlSu = (LinearLayout)view.findViewById(R.id.rl_su);
 		tvSu = (TextView)view.findViewById(R.id.tv_suggest);
 		rlpic = (LinearLayout) view.findViewById(R.id.rl_pic);
 		pic = (ImageView) view.findViewById(R.id.pic);
 		title = (TextView) view.findViewById(R.id.title);
 		suggestimg = (ImageView) view.findViewById(R.id.img_suggest);
 		
 		getWindow().setLayout((int) (Constant.screenWidht * 0.8),(int) (Constant.screenHeight * 0.6));
 		
 		setContentView(view);
 		tvSu.setText(data.getSuggest());
 		title.setText(data.getTitle());
 		if("1".equals(data.getFlag())){
 			suggestimg.setImageResource(R.drawable.ic_status_ok);
 		}else{
 			suggestimg.setImageResource(R.drawable.ic_status_po);
 		}
 		
		ImageLoader.getInstance().displayImage(data.getPicture(), pic, MyApplication.defaultOption);
		
 		this.setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				startAnim();
			}
		});
 		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BedroomSuggestDialog.this.dismiss();
			}
		});
		
	}
	
	public void setData(EnvironmentBean data){
		this.data = data;
	}
    
    private void startAnim(){
    	AnimatorSet mAnimatorSet = new AnimatorSet();
    	ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotationY", 180, 0);
    	ObjectAnimator objectAnimator2 =  ObjectAnimator.ofFloat(view, "scaleY", 0.3f, 0.9f);
    	ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(view, "scaleX", 0.3f, 0.9f);
    	objectAnimator2.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				isDismiss = false;
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				isDismiss = true;
			}
			@Override
			public void onAnimationCancel(Animator animation) {
				isDismiss = true;
			}
		});
    	
    	objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (Float) animation.getAnimatedValue();
				if(value < 90){
					rlpic.setVisibility(View.GONE);
					rlSu.setVisibility(View.VISIBLE);
				}
			}
		});
    	
    	mAnimatorSet.play(objectAnimator).with(objectAnimator2).with(objectAnimator3);
    	mAnimatorSet.setDuration(500);
    	mAnimatorSet.start();
    }

    private void closeAnim(){
    	AnimatorSet mAnimatorSet = new AnimatorSet();
    	ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotationY", 0, 180);
    	ObjectAnimator objectAnimator2 =  ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 0.2f);
    	ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 0.2f);
    	objectAnimator2.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				closeAnim = true;
				dismiss();
			}
			@Override
			public void onAnimationCancel(Animator animation) {
				closeAnim = true;
				dismiss();
			}
		});
    	
    	objectAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (Float) animation.getAnimatedValue();
				if(value >= 90){
					rlpic.setVisibility(View.VISIBLE);
					rlSu.setVisibility(View.GONE);
				}
			}
		});
    	
    	mAnimatorSet.play(objectAnimator).with(objectAnimator2).with(objectAnimator3);
    	mAnimatorSet.setDuration(500);
    	mAnimatorSet.start();
    }
    
	@Override
	public void dismiss() {
		if(!isDismiss){
			return;
		}
		if(!closeAnim){
			closeAnim();
			return;
		}
		
		super.dismiss();
	}
    
    
}
