package com.yzm.sleep.activity.pillow;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;

public class PillowDataActivity extends BaseActivity{

	private TextView tvPullDown;
	private int screenHeight;
	private float startY = 0.0f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showdata);
		tvPullDown = (TextView) findViewById(R.id.pillow_data_pull);
		screenHeight = getScreenHeight();
		
//		getSupportFragmentManager().beginTransaction().replace(R.id.pillow_data_content, new DayFragment()).commit();
		 
//		tvPullDown.setOnTouchListener(new OnTouchListener() {
//			
//			
//			@SuppressLint("NewApi")
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				int action = event.getAction();  
//			    switch(action){  
//			    case MotionEvent.ACTION_DOWN:
//			    	startY = event.getRawY();
//			        break;  
//			    case MotionEvent.ACTION_MOVE: 
//			    	float moveY = event.getRawY();
//			    	int distance = (int) (moveY - startY);
//			    	v.scrollTo(0, - distance);
//			        break;  
//			    case MotionEvent.ACTION_UP:  
//			    	float endY = event.getRawY();
//			    	if((endY - startY) > 200 ){
//			    		scrollToAnimation(v, endY, screenHeight - v.getHeight());
//			    	}else{
//			    		scrollToAnimation(v, endY, 0.0f);
//			    	}
//			        break;                
//			    }  
//			    return true;     
//		    }
//		});
		
	}

	@SuppressLint("NewApi")
	private void scrollToAnimation(View v, float start, float end){
		System.out.println("开始动画");
//		TranslateAnimation animation = new TranslateAnimation(
//				0, 
//				0,
//				start, 
//				end);
//		animation.setDuration(1000);
//		v.startAnimation(animation);
		ValueAnimator anim= ValueAnimator.ofInt(0, 40);  
		anim.setDuration(40);  
		anim.start(); 
	}
	
}
