package com.yzm.sleep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepIndexCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.SimpleRoundProgressBar;

/**
 * 睡眠评估所有结果显示界面
 * @author chen
 * 
 */
public class EstimateBaseActivity extends BaseActivity {
	TextView tvScore;
	Button tiRightBtn;
	private RelativeLayout rel1, rel2, rel3, rel4;
	private TextView tips1, tips2, tips3, tips4;
	private TextView con1, con2, con3, con4;
	private SimpleRoundProgressBar bar;
	private EstimateBaseActivity activity;
	private LinearLayout contentLin , nonetLin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_estimate_base);
		((TextView)findViewById(R.id.title)).setText("睡眠评估");
		findViewById(R.id.back).setOnClickListener(this);
		tiRightBtn = (Button) findViewById(R.id.btn_title_right);
		tiRightBtn.setVisibility(View.VISIBLE);
		tiRightBtn.setOnClickListener(this);
		tiRightBtn.setText("重评");
		activity = this;
		initviews();
		if(checkNetWork(activity)){
			contentLin.setVisibility(View.VISIBLE);
			nonetLin.setVisibility(View.GONE);
			getEstimateData();
		}else{
			contentLin.setVisibility(View.GONE);
			nonetLin.setVisibility(View.VISIBLE);
			refreshDate();
		}
		sendBroadcast(new Intent(Constant.PINGGU_DEAL_ACTION));//通知首页评估完成
	}

	private void initviews() {
		tvScore = (TextView) findViewById(R.id.esti_tv_scor);
		rel1 = (RelativeLayout) findViewById(R.id.esti_rel_1);
		rel2 = (RelativeLayout) findViewById(R.id.esti_rel_2);
		rel3 = (RelativeLayout) findViewById(R.id.esti_rel_3);
		rel4 = (RelativeLayout) findViewById(R.id.esti_rel_4);
		contentLin = (LinearLayout) findViewById(R.id.content_lin);
		nonetLin   = (LinearLayout) findViewById(R.id.no_net_lin);
		bar = (SimpleRoundProgressBar) findViewById(R.id.esti_progress);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((getScreenWidth()-Util.Dp2Px(this, 44))/2, (getScreenWidth()-Util.Dp2Px(this, 44))/2);
		lp.leftMargin = Util.Dp2Px(this, 1);
		lp.rightMargin = Util.Dp2Px(this, 1);
		lp.bottomMargin = Util.Dp2Px(this, 1);
		lp.topMargin = Util.Dp2Px(this, 1);
		
		rel1.setLayoutParams(lp);
		rel2.setLayoutParams(lp);
		rel3.setLayoutParams(lp);
		rel4.setLayoutParams(lp);
		
		tips1 = (TextView) findViewById(R.id.esti_rel_1_tv_tip);
		tips2 = (TextView) findViewById(R.id.esti_rel_2_tv_tip);
		tips3 = (TextView) findViewById(R.id.esti_rel_3_tv_tip);
		tips4 = (TextView) findViewById(R.id.esti_rel_4_tv_tip);
		
		con1 = (TextView) findViewById(R.id.esti_rel_1_tv_con);
		con2 = (TextView) findViewById(R.id.esti_rel_2_tv_con);
		con3 = (TextView) findViewById(R.id.esti_rel_3_tv_con);
		con4 = (TextView) findViewById(R.id.esti_rel_4_tv_con);
		
		tips1.setText("生活习惯");
		tips2.setText("睡眠规律");
		tips3.setText("卧室环境");
		tips4.setText("心理活动");
		
		rel1.setOnClickListener(this);
		rel2.setOnClickListener(this);
		rel3.setOnClickListener(this);
		rel4.setOnClickListener(this);
	}
	
	private void getEstimateData(){
		new XiangchengMallProcClass(this).getSleepIndex(PreManager.instance().getUserId(this), new InterfaceSleepIndexCallback() {
			
			@Override
			public void onSuccess(int icode, String sleep_pg, String shxg_pg,
					String smgl_pg, String smhj_pg, String xlhd_pg) {
				PreManager.instance().saveSMPGResult(activity, sleep_pg);
				if(!TextUtils.isEmpty(smgl_pg)){//PreManager.instance().getSMGLPGRsult(activity)
					PreManager.instance().saveSMGLPGResult(activity, smgl_pg);
				}
				if(!TextUtils.isEmpty(shxg_pg)){//PreManager.instance().getSHXGPGRsult(activity)
					PreManager.instance().saveSHXGPGResult(activity, shxg_pg);
				}
				if(!TextUtils.isEmpty(smhj_pg)){//PreManager.instance().getSMHJPGRsult(activity)
					PreManager.instance().saveSMHJPGResult(activity, smhj_pg);
				}
				if(!TextUtils.isEmpty(xlhd_pg)){//PreManager.instance().getXLHDPGRsult(activity))
					PreManager.instance().saveXLHDPGResult(activity, xlhd_pg);
				}
				refreshDate();
				if(!TextUtils.isEmpty(sleep_pg)){
					PreManager.instance().saveIsCompleteSleepPg(EstimateBaseActivity.this, true);
					EstimateBaseActivity.this.sendBroadcast(new Intent(
							Constant.RECEVER_USER_BIRTHDAY_UPDATE));
				}
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				toastMsg(strErrMsg);
				refreshDate();
			}
		});
	}
	/**
	 * 刷新显示
	 */
	private void refreshDate(){
		
		String sleep_pg = PreManager.instance().getSMPGRsult(activity);
		int progress = 0;
		if(StringUtil.isNumber(sleep_pg)){
			progress = Integer.parseInt(sleep_pg);
		}
		if(0 > progress){
			progress = 0;
		}else if(100 < progress){
			progress = 100;
		}
		bar.setRoundColor(getResources().getColor(R.color.fct_color_7f));
		bar.setRoundProgressColor(getResources().getColor(R.color.theme_color));
		bar.setMax(100);
		bar.setRoundWidth(8);
		bar.setProgressf((float)progress/100);
		
		tvScore.setText(sleep_pg);
		
		String shxg_pg = PreManager.instance().getSHXGPGRsult(activity);
		String smgl_pg = PreManager.instance().getSMGLPGRsult(activity);
		String smhj_pg = PreManager.instance().getSMHJPGRsult(activity);
		String xlhd_pg = PreManager.instance().getXLHDPGRsult(activity);
		
		con1.setText(TextUtils.isEmpty(shxg_pg)?"":shxg_pg);
		con2.setText(TextUtils.isEmpty(smgl_pg)?"":smgl_pg);
		con3.setText(TextUtils.isEmpty(smhj_pg)?"":smhj_pg);
		con4.setText(TextUtils.isEmpty(xlhd_pg)?"":xlhd_pg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_right:
			Intent intentRedid = new Intent(EstimateBaseActivity.this, EstimateWebActivity.class);
			intentRedid.putExtra("type", "0");
			startActivity(intentRedid);
			break;
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.esti_rel_1:
			Intent intent = null;
			/*if(con1.getText().toString().equals("评估")){
				intent = new Intent(EstimateBaseActivity.this, EstimateWebActivity.class);
				intent.putExtra("url", Constant.SLEEP_SHXG_URL+"uid="+PreManager.instance().getUserId(EstimateBaseActivity.this)+"#");
				intent.putExtra("type", "1");
			}else {*/
				intent = new Intent(EstimateBaseActivity.this, EstiRresultsWebActivity.class);
				intent.putExtra("type", "1");
				startActivity(intent);
			break;
		case R.id.esti_rel_2://1生活习惯评估；2睡眠规律评估；3睡眠环境评估；4心理活动
			Intent intent2 = null;
			/*if(con2.getText().toString().equals("评估")){
				intent2 = new Intent(EstimateBaseActivity.this, EstimateWebActivity.class);
//				intent2.putExtra("url", Constant.SLEEP_SMGL_URL+"uid="+PreManager.instance().getUserId(EstimateBaseActivity.this)+"#");
				intent2.putExtra("type", "2");
			}else {*/
				intent2 = new Intent(EstimateBaseActivity.this, EstiRresultsWebActivity.class);
				intent2.putExtra("type", "2");
				startActivity(intent2);
			break;
		case R.id.esti_rel_3:
			Intent intent3 = null;
			/*if(con3.getText().toString().equals("评估")){
				intent3 = new Intent(EstimateBaseActivity.this, EstimateWebActivity.class);
//				intent3.putExtra("url", Constant.SLEEP_SMHJ_URL+"uid="+PreManager.instance().getUserId(EstimateBaseActivity.this)+"#");
				intent3.putExtra("type", "3");
			}else {*/
				intent3 = new Intent(EstimateBaseActivity.this, EstiRresultsWebActivity.class);
				intent3.putExtra("type", "3");
				startActivity(intent3);
			break;
		case R.id.esti_rel_4:
			Intent intent4 = null;
			/*if(con4.getText().toString().equals("评估")){
				intent4 = new Intent(EstimateBaseActivity.this, EstimateWebActivity.class);
//				intent4.putExtra("url", Constant.SLEEP_XLHD_URL+"uid="+PreManager.instance().getUserId(EstimateBaseActivity.this)+"#");
				intent4.putExtra("type", "4");
			}else {*/
				intent4 = new Intent(EstimateBaseActivity.this, EstiRresultsWebActivity.class);
				intent4.putExtra("type", "4");
				startActivity(intent4);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshDate();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
}
