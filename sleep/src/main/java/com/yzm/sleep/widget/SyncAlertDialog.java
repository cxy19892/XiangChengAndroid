package com.yzm.sleep.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.LogUtil;

public class SyncAlertDialog extends AlertDialog implements android.view.View.OnClickListener {


	private int animationStyle;
	private Context mContext;
	private RelativeLayout relConnect, relSync, relFailed, relUpload, relBund;
	private Button giveUpBtn, reTryBtn, startUploadBtn, cancleUploadBtn, confirmBund, cancleBund;
	private ImageView closeBtn;
	private TextView processValue;
	private SimpleRoundProgressBar mProgressBar;
	private MySyncOnClickListener mlistener;
	private ImageView imgv1, imgv2, imgv3, imgv1_, imgv2_, imgv3_;
	private TextView bundTips;
	private LinearLayout bundBtnLin, bundImgLin;
	
	protected SyncAlertDialog(Context context) {
		super(context, R.style.select_dialog);
		// TODO Auto-generated constructor stub
	}
	
	public SyncAlertDialog(Context context, int animationStyle, MySyncOnClickListener mlistener) {
		super(context, R.style.select_dialog);
		this.mContext = context;
		this.animationStyle = animationStyle;
		this.mlistener = mlistener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.sync_pillow_data_pop);
		getWindow().setWindowAnimations(animationStyle);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Constant.screenHeight * 0.4));
        getWindow().setBackgroundDrawable(new ColorDrawable(R.color.bg_color));
        getWindow().setGravity(Gravity.BOTTOM);
		setCanceledOnTouchOutside(false);
		initViews();
		
		startPointAnim();
	}

	private void initViews(){
		relConnect = (RelativeLayout) findViewById(R.id.rel_search_dev);
		relSync = (RelativeLayout) findViewById(R.id.rel_sync_dev);
		relFailed = (RelativeLayout) findViewById(R.id.rel_sync_faild);
		relUpload = (RelativeLayout) findViewById(R.id.rel_sync_upload);
		relBund   = (RelativeLayout) findViewById(R.id.rel_bund_pillow_dev);
		giveUpBtn = (Button) findViewById(R.id.btn_give_up);
		reTryBtn = (Button) findViewById(R.id.btn_re_try);
		closeBtn = (ImageView) findViewById(R.id.imgv_close_sync);
		processValue = (TextView) findViewById(R.id.tv_sync_value);
		startUploadBtn = (Button) findViewById(R.id.btn_start_upload);
		cancleUploadBtn= (Button) findViewById(R.id.btn_cancle_upload);
		confirmBund = (Button) findViewById(R.id.btn_start_bund);
		cancleBund  = (Button) findViewById(R.id.btn_cancle_bund);
		imgv1 = (ImageView) findViewById(R.id.imgv_1);
		imgv2 = (ImageView) findViewById(R.id.imgv_2);
		imgv3 = (ImageView) findViewById(R.id.imgv_3);
		imgv1_ = (ImageView) findViewById(R.id.imgv_1_bunding);
		imgv2_ = (ImageView) findViewById(R.id.imgv_2_bunding);
		imgv3_ = (ImageView) findViewById(R.id.imgv_3_bunding);
		bundTips = (TextView) findViewById(R.id.tv_bund_tips);
		bundBtnLin = (LinearLayout) findViewById(R.id.lin_bund_btn);
		bundImgLin = (LinearLayout) findViewById(R.id.lin_bund_waiting);
		giveUpBtn.setOnClickListener(this);
		reTryBtn.setOnClickListener(this);
		closeBtn.setOnClickListener(this);
		startUploadBtn.setOnClickListener(this);
		cancleUploadBtn.setOnClickListener(this);
		confirmBund.setOnClickListener(this);
		cancleBund.setOnClickListener(this);
		mProgressBar = (SimpleRoundProgressBar) findViewById(R.id.sync_simple_round_progressBar);
		mProgressBar.setRoundWidth(12);
		mProgressBar.setRoundColor(mContext.getResources().getColor(R.color.fct_color_7f));
		mProgressBar.setRoundProgressColor(mContext.getResources().getColor(R.color.theme_color));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_give_up:
			mlistener.OnSyncClick(1);
			break;
		case R.id.btn_re_try:
			mlistener.OnSyncClick(2);
			break;
		case R.id.imgv_close_sync:
			mlistener.OnSyncClick(0);
			break;
		case R.id.btn_cancle_upload:
			mlistener.OnSyncClick(3);
			break;
		case R.id.btn_start_upload:
			mlistener.OnSyncClick(4);
			break;
		case R.id.btn_start_bund:
			mlistener.OnSyncClick(5);
			break;
		case R.id.btn_cancle_bund:
			mlistener.OnSyncClick(6);
			break;
		default:
			break;
		}
	}
	
	public boolean isShowBundCancle(){
		return relBund.getVisibility() == View.VISIBLE;
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if(relConnect.getVisibility() == View.VISIBLE){
					imgv1.setImageResource(R.drawable.custom_circle_point_theme);
					imgv2.setImageResource(R.drawable.custom_circle_point_7e7c91);
					imgv3.setImageResource(R.drawable.custom_circle_point_7e7c91);
				}
				if(relBund.getVisibility() == View.VISIBLE){
					imgv1_.setImageResource(R.drawable.custom_circle_point_theme);
					imgv2_.setImageResource(R.drawable.custom_circle_point_7e7c91);
					imgv3_.setImageResource(R.drawable.custom_circle_point_7e7c91);
				}
				break;
			case 1:
				if(relConnect.getVisibility() == View.VISIBLE){
					imgv1.setImageResource(R.drawable.custom_circle_point_7e7c91);
					imgv2.setImageResource(R.drawable.custom_circle_point_theme);
					imgv3.setImageResource(R.drawable.custom_circle_point_7e7c91);
				}
				
				if(relBund.getVisibility() == View.VISIBLE){
					imgv1_.setImageResource(R.drawable.custom_circle_point_7e7c91);
					imgv2_.setImageResource(R.drawable.custom_circle_point_theme);
					imgv3_.setImageResource(R.drawable.custom_circle_point_7e7c91);
				}
				break;
			case 2:
				if(relConnect.getVisibility() == View.VISIBLE){
					imgv1.setImageResource(R.drawable.custom_circle_point_7e7c91);
					imgv2.setImageResource(R.drawable.custom_circle_point_7e7c91);
					imgv3.setImageResource(R.drawable.custom_circle_point_theme);
				}
				if(relBund.getVisibility() == View.VISIBLE){
					imgv1_.setImageResource(R.drawable.custom_circle_point_7e7c91);
					imgv2_.setImageResource(R.drawable.custom_circle_point_7e7c91);
					imgv3_.setImageResource(R.drawable.custom_circle_point_theme);
				}
				break;
			default:
				break;
			}
		}
		
	};
	
	int CycleValue = 0;
	TimerTask task = null;
	Timer mtimer = null;
	private void startPointAnim(){
		if(null == task){
		task = new TimerTask() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(CycleValue % 3);
				CycleValue += 1;
				if(CycleValue > 99){
					CycleValue = 0;
				}
			}
		};
		}
		if(mtimer == null){
			mtimer = new Timer();
		}
		mtimer.schedule(task, 100, 500);
	}
	/**
	 * 切换视图
	 * @param value<br> 0:连接设备 <br> 1:同步数据 <br> 2:同步或者连接失败 <br> 3:升级提示 <br> 4:绑定提示
	 */
	public void updataViews(int value){
		switch(value){
		case 0:
			relConnect.setVisibility(View.VISIBLE);
			relSync.setVisibility(View.GONE);
			relFailed.setVisibility(View.GONE);
			closeBtn.setVisibility(View.VISIBLE);
			relBund.setVisibility(View.GONE);
			break;
		case 1:
			relConnect.setVisibility(View.GONE);
			relSync.setVisibility(View.VISIBLE);
			relFailed.setVisibility(View.GONE);
			closeBtn.setVisibility(View.GONE);
			relBund.setVisibility(View.GONE);
			break;
		case 2:
			relConnect.setVisibility(View.GONE);
			relSync.setVisibility(View.GONE);
			relFailed.setVisibility(View.VISIBLE);
			closeBtn.setVisibility(View.GONE);
			relBund.setVisibility(View.GONE);
			break;
		case 3:
			relConnect.setVisibility(View.GONE);
			relSync.setVisibility(View.GONE);
			relFailed.setVisibility(View.GONE);
			relUpload.setVisibility(View.VISIBLE);
			closeBtn.setVisibility(View.GONE);
			relBund.setVisibility(View.GONE);
			break;
		case 4:
			relConnect.setVisibility(View.GONE);
			relSync.setVisibility(View.GONE);
			relFailed.setVisibility(View.GONE);
			relUpload.setVisibility(View.GONE);
			closeBtn.setVisibility(View.GONE);
			relBund.setVisibility(View.VISIBLE);
			bundImgLin.setVisibility(View.GONE);
			bundBtnLin.setVisibility(View.VISIBLE);
			bundTips.setText(R.string.tips_getdev_and_ask_confirm);
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * -1取消
	 * @param time
	 */
	public synchronized void upDataBundtimerBtn(int time){
		if(relBund.getVisibility() == View.VISIBLE){
			cancleBund.setText("取消  "+time);
		}
		if(time == -1){
			cancleBund.setText("取消  ");
		}
	}
	
	public void showWaitingView(){
		if(relBund.getVisibility() == View.VISIBLE){
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.translatexanimator);
			Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.translatexanimator2); 
			bundBtnLin.startAnimation(animation);
			bundImgLin.setVisibility(View.VISIBLE);
			bundBtnLin.setVisibility(View.GONE);
			bundImgLin.startAnimation(animation2);
			
			bundTips.setText("数据交互中");
		}
	}
	
	
	@Override
	public void dismiss() {
		super.dismiss();
		LogUtil.d("chen", "mysyncDialog dismiss");
		if(mtimer != null){
			mtimer.cancel();
			mtimer = null;
		}
		if(task != null){
			task.cancel();
			task = null;
		}
	}
	

	/**
	 * 更新进度条
	 * @param process 进度 最大值为 100 
	 */
	public void updataSyncValue(int process){
		if(process>100){
			process =100;
		}
		mProgressBar.setMax(100);
		mProgressBar.setProgressf((float)process/100);
		processValue.setText(process+"%");
		if(process == 100){
			mProgressBar.setProgressf(1f);
		}
	}

	public interface MySyncOnClickListener{
		/**
		 * 同步dialog的返回信息
		 * @param value <br> 0: 点击关闭按钮  <br> 1: 点击放弃 <br> 2: 点击重试 <br> 3:点击不升级 <br> 4:点击升级 <br> 5:绑定设备 <br> 6:取消绑定设备
		 */
		public void OnSyncClick(int value);
	}
}
