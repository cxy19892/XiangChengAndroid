package com.yzm.sleep.tools;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;

public class AnimationUtil {

	private final static String ALPHA = "alpha";
	private final static String ROTATION_X = "rotationX";
	private final static String SCALE_Y = "scaleY";
	private final static String PIVOT_Y = "pivotY";
	
	private static boolean isLoading = false;
	
	/**
	 *  旋转切换的动画
	 * @param view1  被旋转的控件
	 * @param view2  旋转后展示的控件
	 */
	public static void rotationForChangeAnimation(final View view1, final View view2){
		if(!isLoading){
			PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(ALPHA, 1f,  
	                0f);  
	        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(ROTATION_X, 0.0f,  
	                180f);  
	        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view1, pvhX, pvhY).setDuration(500); 
			anim.start();  
			anim.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator arg0) {
					isLoading = true;
				}
				
				@Override
				public void onAnimationRepeat(Animator arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator arg0) {
					PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(ALPHA, 
			                0f, 1f);  
			        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(ROTATION_X,
			                180f, 0.0f);  
			        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view1, pvhX, pvhY).setDuration(1); 
					anim.start(); 
					view1.setVisibility(View.GONE);
					view2.setVisibility(View.VISIBLE);
					isLoading = false;
				}
				
				@Override
				public void onAnimationCancel(Animator arg0) {
				}
			});
		}
		
	}
	
	/**
	 *  渐隐还是渐显
	 * @param view
	 * @param VisibleOrGone true 是显示，false是隐藏
	 */
	public static void hideOrShow(final View view, boolean VisibleOrGone){
		 ObjectAnimator anim = ObjectAnimator.ofFloat(view, ALPHA, 1f, 0f).setDuration(500);
         if(VisibleOrGone)
        	 anim = ObjectAnimator.ofFloat(view, ALPHA, 0.0f, 1.0f).setDuration(500);
		 anim.start(); 
	}
	
	
	/**
	 *  根据控件的高度从下往上缩小
	 * @param view1
	 * @param height
	 */
	public static void scaleByY1(final View view1, final int height){
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleY", 1.0f,  
                0.0f);  
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("pivotY", 0.0f);  
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view1, pvhX, pvhY).setDuration(500); 
        anim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				LayoutParams lp = view1.getLayoutParams();
				lp.height = (int) (height * cVal);
				view1.setLayoutParams(lp);
				if(cVal == 0.0f)
					view1.setVisibility(View.GONE);
			}
		});
		anim.start();
	}
	
	/**
	 *  根据控件的高度从上往下放大
	 * @param view1
	 * @param height
	 */
	public static void scaleByY2(final View view1, final int height){
		view1.setVisibility(View.VISIBLE);
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleY", 0.0f,  
                1.0f);  
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("pivotY", 0.0f);  
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view1, pvhX, pvhY).setDuration(500); 
        anim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				LayoutParams lp = view1.getLayoutParams();
				lp.height = (int) (height * cVal);
				view1.setLayoutParams(lp);
			}
		});
		anim.start();
	}
	
	/**
	 *  按钮点击后膨胀效果
	 * @param view
	 * @param VisibleOrGone true 是显示，false是隐藏
	 */
	public static void btnClickEffect(View view){
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,  
                0f, 1f);  
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,  
                2.0f, 1f);  
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,  
                2.0f, 1f);
	    ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(500); 
	    anim.setInterpolator(new DecelerateInterpolator());
		anim.start();  
	}
}
