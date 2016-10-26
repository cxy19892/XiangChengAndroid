package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.ReleaseEditActivity;
import com.yzm.sleep.bean.ComfortBean;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfacePerceiveCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 认知重构 
 */
public class PerceiveResetActivity extends BaseActivity {
	
	private View rlNousecontent, rlContent, rlOp, rlType1, rlView, noNet;
	private TextView tvTitle, tvContent;
	private List<ComfortBean> comfortBeans;
	private int currentPo;
	private boolean isStart;
	private RelativeLayout btnBtn3;
	private ProgressUtils pro;
	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perceivereset);
		comfortBeans = new ArrayList<ComfortBean>();
		findViewById(R.id.rl_title).setBackgroundColor(getResources().getColor(R.color.white));
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("认知重构");
		noNet = findViewById(R.id.no_net);
		noNet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				noNet.setVisibility(View.GONE);
				getData();
			}
		});
//		((TextView)noNet.findViewById(R.id.text)).setText("检查网路连接");
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setText("换一个");
		right.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
		findViewById(R.id.btn_start).setOnClickListener(this);
		findViewById(R.id.btn_btn1).setOnClickListener(this);
		findViewById(R.id.btn_btn2).setOnClickListener(this);
		btnBtn3 = (RelativeLayout) findViewById(R.id.btn_btn3);
		btnBtn3.setOnClickListener(this);
		rlView = findViewById(R.id.rl_view);
		rlNousecontent = findViewById(R.id.rl_nousecontent);
		rlContent = findViewById(R.id.rl_content);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvContent = (TextView) findViewById(R.id.tv_content);
		tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
		rlOp = findViewById(R.id.rl_op);
		rlType1 = findViewById(R.id.rl_type1);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		Button btnStart = (Button) findViewById(R.id.btn_start);
		params.width = getScreenWidth() * 2 / 3;
		params.height= params.width;
		btnStart.setLayoutParams(params);
		getData();
	}
	
	/**
	 * 获取数据
	 */
	private void getData(){
		showPro();
		if(checkNetWork(this)){
			noNet.setVisibility(View.GONE);
			new XiangchengMallProcClass(this).perceive(PreManager.instance().getUserId(this), new InterfacePerceiveCallBack() {
				@Override
				public void onSuccess(String icode, List<ComfortBean> datas) {
					cancelPro();
					findViewById(R.id.rl_type1).setVisibility(View.VISIBLE);
					findViewById(R.id.rl_start).setVisibility(View.VISIBLE);
					comfortBeans = datas;
					rlView.setBackgroundColor(getResources().getColor(R.color.theme_color));
					tvTitle.setText(comfortBeans.get(currentPo).content.get(0));
				}

				@Override
				public void onError(String icode, String strErrMsg) {
					cancelPro();
					Util.show(PerceiveResetActivity.this, strErrMsg);
					findViewById(R.id.rl_type1).setVisibility(View.GONE);
					findViewById(R.id.rl_start).setVisibility(View.GONE);
				}
			});
		}else{
			Util.show(this, "网络连接不可以");
			cancelPro();
			noNet.setVisibility(View.VISIBLE);
			findViewById(R.id.rl_type1).setVisibility(View.GONE);
			findViewById(R.id.rl_start).setVisibility(View.GONE);
		}
	}
	


	@Override
	protected void onActivityResult(int arg0, int arg1, Intent intent) {
		super.onActivityResult(arg0, arg1, intent);
		if(arg0 == Constant.EDITPERCEIVER_REQUSTCODE && arg1 == RESULT_OK){
			tvContent.setText(intent.getStringExtra("context"));
			comfortBeans.get(currentPo).setSelsectUse(true);
			comfortBeans.get(currentPo).content.add(intent.getStringExtra("context"));
			comfortBeans.get(currentPo).setSelectPosition(comfortBeans.get(currentPo).getSelectPosition() + 1);
			rlNousecontent.setVisibility(View.GONE);
			rlContent.setVisibility(View.VISIBLE);
			rlOp.setVisibility(View.GONE);
			rlType1.setVisibility(View.GONE);
		}
	}

	@SuppressLint("NewApi") 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			try {
				if(isStart){
					rlContent.setVisibility(View.VISIBLE);
					rlType1.setVisibility(View.VISIBLE);
					rlOp.setVisibility(View.VISIBLE);
					rlNousecontent.setVisibility(View.GONE);
				}
				if(currentPo < (comfortBeans == null ? 0 : comfortBeans.size() - 1)){
					tvTitle.setText(comfortBeans.get(++currentPo).content.get(0));
					comfortBeans.get(currentPo).setSelectPosition(comfortBeans.get(currentPo).getSelectPosition());
					tvContent.setText(comfortBeans.get(currentPo).content.get(comfortBeans.get(currentPo).getSelectPosition()));
				}else{ 
					currentPo= 0;
					tvTitle.setText(comfortBeans.get(0).content.get(0));
					comfortBeans.get(currentPo).setSelectPosition(comfortBeans.get(currentPo).getSelectPosition());
					tvContent.setText(comfortBeans.get(currentPo).content.get(comfortBeans.get(currentPo).getSelectPosition()));
				}
				
				if(comfortBeans.get(currentPo).isSelsectUse()){
					rlType1.setVisibility(View.GONE);
					rlOp.setVisibility(View.GONE);
				}else{
					rlType1.setVisibility(View.VISIBLE);
					rlOp.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
			}
				
			break;
		case R.id.btn_btn1: //没有用
			if(comfortBeans.get(currentPo).getSelectPosition() < comfortBeans.get(currentPo).content.size() - 1){
				comfortBeans.get(currentPo).setSelectPosition(comfortBeans.get(currentPo).getSelectPosition()+1);
				tvContent.setText(comfortBeans.get(currentPo).content.get(comfortBeans.get(currentPo).getSelectPosition()));
			}else{
				rlContent.setVisibility(View.GONE);
				rlNousecontent.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_btn2://有用
			comfortBeans.get(currentPo).setSelsectUse(true);
			startAnim();
			break;
		case R.id.btn_btn3:
			btnBtn3.setEnabled(false);
			startActivityForResult(new Intent(this, ReleaseEditActivity.class)
			.putExtra("types", 15)
			.putExtra("title", "认知重构"), Constant.EDITPERCEIVER_REQUSTCODE); 
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					btnBtn3.setEnabled(true);
				}
			}, 800);
			break;
		case R.id.btn_start:
			isStart = true;
			tvContent.setText(comfortBeans.get(currentPo).content.get(comfortBeans.get(currentPo).getSelectPosition()));
			findViewById(R.id.rl_start).setVisibility(View.GONE);
			findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void startAnim(){
		ScaleAnimation scalAnim1 = new ScaleAnimation(1f, 1f, 1f, 0f, AnimationSet.RELATIVE_TO_SELF, 1f, AnimationSet.RELATIVE_TO_SELF, 1f);
		TranslateAnimation tranAnim1 = new TranslateAnimation(0, 0, 0, -rlType1.getHeight());
		AnimationSet openAnim1 = new AnimationSet(false);
		openAnim1.addAnimation(scalAnim1);
		openAnim1.addAnimation(tranAnim1);
		openAnim1.setDuration(300);
		openAnim1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				rlType1.setVisibility(View.GONE);
				rlOp.setVisibility(View.GONE);
				
			}
		});
		rlType1.startAnimation(openAnim1);
	}
}
