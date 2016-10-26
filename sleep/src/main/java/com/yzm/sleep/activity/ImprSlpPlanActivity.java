package com.yzm.sleep.activity;

import java.util.List;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.SleepCaseBean;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceNewSleepplanCallBack;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.MyClock;
import com.yzm.sleep.widget.MyClockPro;
import com.yzm.sleep.widget.RulerView;
import com.yzm.sleep.widget.RulerView.OnValueChangeListener;

/**
 * 生成睡眠改善方案
 * 传入参数  int flag 1表示新增； 2表示重置。
 * 
 */
public class ImprSlpPlanActivity extends BaseActivity {

	private RelativeLayout relHaibits, relTime, relLenth;
	private TextView Htips, Ttips, title;
	private CheckBox radioA, radioB, radioC, radioD, radioE, radioF;
	private LinearLayout linA, linB, linC, linD, linE, linF;
	private MyClock myclock;
	private RulerView mRulerView , mRulerView2;
	private int state = 0;
	private Button commit;
	private ImageButton nextBtn;
//	private LinearLayout linTTlth;
//	private TextView tvSlthT;
	private int wakeValue = 32, lenthValue = 34;
	
	private String habit;
	private String waketime;
	private String sleepLenth;
	private int isNewflag;
	private MyClockPro myClockPro;

	private String SleepTime;
	
	private ProgressUtils pro;
	/**
	 * 显示进度
	 */
	private void showPro(){
		if(pro==null){
			pro = new ProgressUtils(this);
		}
		pro.show();
	}
	
	/**
	 * 取消进度
	 */
	private void cancelPro(){
		if(pro!=null){
			pro.dismiss();
			pro=null;
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_impr_slp_plan);
		initviews();
		
		isNewflag = getIntent().getIntExtra("flag", 1);
	}

	private void initviews() {
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		commit= (Button) findViewById(R.id.btn_title_right);
		nextBtn = (ImageButton) findViewById(R.id.ib_right);		
		relHaibits = (RelativeLayout) findViewById(R.id.rel_habit);
		relTime = (RelativeLayout) findViewById(R.id.rel_time);
		relLenth = (RelativeLayout) findViewById(R.id.rel_length);
		mRulerView = (RulerView) findViewById(R.id.myrulerview);
		mRulerView2= (RulerView) findViewById(R.id.myruler_lth);
		Htips = (TextView) findViewById(R.id.habit_tips);
		Ttips = (TextView) findViewById(R.id.tv_tips);
		
		myClockPro = (MyClockPro) findViewById(R.id.myclock_lenth);
		
		linA = (LinearLayout) findViewById(R.id.lin_A);
		linB = (LinearLayout) findViewById(R.id.lin_B);
		linC = (LinearLayout) findViewById(R.id.lin_C);
		linD = (LinearLayout) findViewById(R.id.lin_D);
		linE = (LinearLayout) findViewById(R.id.lin_E);
		linF = (LinearLayout) findViewById(R.id.lin_F);
		
		radioA = (CheckBox) findViewById(R.id.radioButtonA);
		radioB = (CheckBox) findViewById(R.id.radioButtonB);
		radioC = (CheckBox) findViewById(R.id.radioButtonC);
		radioD = (CheckBox) findViewById(R.id.radioButtonD);
		radioE = (CheckBox) findViewById(R.id.radioButtonE);
		radioF = (CheckBox) findViewById(R.id.radioButtonF);
		
		myclock = (MyClock) findViewById(R.id.myclock);
		
//		linTTlth = (LinearLayout) findViewById(R.id.lin_totle_lenth);
//		tvSlthT  = (TextView) findViewById(R.id.tv_sleep_lenth);
		Htips.setText("您最近一周是否有以下习惯？");
		Ttips.setText("您最近一周都几点起床？");
		title.setText("改善睡眠计划");
		nextBtn.setVisibility(View.VISIBLE);
		nextBtn.setOnClickListener(this);
		
		linA.setOnClickListener(this);
		linB.setOnClickListener(this);
		linC.setOnClickListener(this);
		linD.setOnClickListener(this);
		linE.setOnClickListener(this);
		linF.setOnClickListener(this);
		
		radioA.setOnCheckedChangeListener(checkListener(1));
		radioB.setOnCheckedChangeListener(checkListener(2));
		radioC.setOnCheckedChangeListener(checkListener(3));
		radioD.setOnCheckedChangeListener(checkListener(4));
		radioE.setOnCheckedChangeListener(checkListener(5));
		radioF.setOnCheckedChangeListener(checkListener(6));
		
		initState1();
	}
	
	private void initState1(){
		relHaibits.setVisibility(View.VISIBLE);
		relTime.setVisibility(View.GONE);
		relLenth.setVisibility(View.GONE);
	}
	
	private void initState2(){
		relHaibits.setVisibility(View.GONE);
		relTime.setVisibility(View.VISIBLE);
		relLenth.setVisibility(View.GONE);
		commit.setVisibility(View.GONE);
		nextBtn.setVisibility(View.VISIBLE);
		mRulerView.setValueChangeListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(int value) {
				myclock.setTime(value/4, (value%4) * 15, true);
				waketime = (value/4 >=10 ?  value/4 : ("0"+(value/4== 0 ? "0" : value/4)))+ ":" + ((value%4) * 15 > 10 ? (value%4) * 15 : "00");
				wakeValue = value;
			}
		});
		mRulerView.setValue(96, 0, wakeValue, 1);
	}
	
	private void initState3(){
		relHaibits.setVisibility(View.GONE);
		relTime.setVisibility(View.GONE);
		relLenth.setVisibility(View.VISIBLE);
		commit.setText("完成");
		commit.setTextColor(getResources().getColor(R.color.ct_color));
		commit.setVisibility(View.VISIBLE);
		commit.setOnClickListener(this);
		nextBtn.setVisibility(View.GONE);
		myClockPro.setTime(((wakeValue - lenthValue)/4) , (wakeValue - lenthValue)%4 * 15, (wakeValue/4) , wakeValue%4 * 15, false);
		mRulerView2.setValueChangeListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(int value) {
				value = value > 40 ? 40 : value;
				lenthValue = value;
				myClockPro.setTime(((wakeValue - lenthValue)/4) , (wakeValue - lenthValue)%4 * 15, (wakeValue/4) , wakeValue%4 * 15, false);
				sleepLenth = (value < 20 ? 20 : value) * 15 +"";
				
			}
		});
		mRulerView2.setValue(40, 20, lenthValue, 2);
	}
	
	private String getSelected(){
		StringBuffer sB = new StringBuffer();
		if(radioA.isChecked()){
			sB.append("E");
			sB.append(",");
		}
		if(radioB.isChecked()){
			sB.append("A");
			sB.append(",");
		}
		if(radioC.isChecked()){
			sB.append("C");
			sB.append(",");
		}
		if(radioD.isChecked()){
			sB.append("B");
			sB.append(",");
		}
		if(radioE.isChecked()){
			sB.append("D");
			sB.append(",");
		}
		if(radioF.isChecked()){
			sB.append("F");
			sB.append(",");
		}
		if (sB.length() > 0) {
			sB.deleteCharAt(sB.length()-1); 
		}
		return sB.toString();
	}
	
	private OnCheckedChangeListener checkListener(final int type){
		return new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				switch (type) {
				case 1:
					if(radioA.isChecked()){
						radioF.setChecked(false);
					}
					break;
				case 2:
					if(radioB.isChecked()){
						radioF.setChecked(false);
					}
					break;
				case 3:
					if(radioC.isChecked()){
						radioF.setChecked(false);
					}
					break;
				case 4:
					if(radioD.isChecked()){
						radioF.setChecked(false);
					}
					break;
				case 5:
					if(radioE.isChecked()){
						radioF.setChecked(false);
					}
					break;
				case 6:
					if(radioF.isChecked()){
						radioA.setChecked(false);
						radioB.setChecked(false);
						radioC.setChecked(false);
						radioD.setChecked(false);
						radioE.setChecked(false);
					}
					break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(state == 0){
				AppManager.getAppManager().finishActivity();
			}else if(state == 1){
				initState1();
				state = 0;
			}else{
				initState2();
				commit.setText("下一步");
				state = 1;
			}
			return false;
		}else
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.back:
			if(state == 0){
				AppManager.getAppManager().finishActivity();
			}else if(state == 1){
				initState1();
				state = 0;
			}else{
				initState2();
				commit.setText("下一步");
				state = 1;
			}
			break;
		case R.id.ib_right:
			if(state == 0){
				if(!TextUtils.isEmpty(getSelected())){
					habit = getSelected();
					
					initState2();
					state = 1;
				}else{
					toastMsg("至少选择一项");
				}
			}else if(state == 1){
				initState3();
				
				state = 2;
			}
			break;
		case R.id.btn_title_right:
			creatSleepImprovePlan(waketime, sleepLenth, habit, String.valueOf(isNewflag));
			break;
			
		case R.id.radioButtonA:
			radioA.setChecked(!radioA.isChecked());
			radioF.setChecked(false);
			break;
		case R.id.radioButtonB:
			radioB.setChecked(!radioB.isChecked());
			radioF.setChecked(false);
			break;
		case R.id.radioButtonC:
			radioC.setChecked(!radioC.isChecked());
			radioF.setChecked(false);
			break;
		case R.id.radioButtonD:
			radioD.setChecked(!radioD.isChecked());
			radioF.setChecked(false);
			break;
		case R.id.radioButtonE:
			radioE.setChecked(!radioE.isChecked());
			radioF.setChecked(false);
			break;
		case R.id.radioButtonF:
			radioA.setChecked(false);
			radioB.setChecked(false);
			radioC.setChecked(false);
			radioD.setChecked(false);
			radioE.setChecked(false);
			radioF.setChecked(!radioF.isChecked());
			break;
		case R.id.lin_A:
			radioA.setChecked(!radioA.isChecked());
			break;
		case R.id.lin_B:
			radioB.setChecked(!radioB.isChecked());
			break;
		case R.id.lin_C:
			radioC.setChecked(!radioC.isChecked());
			break;
		case R.id.lin_D:
			radioD.setChecked(!radioD.isChecked());
			break;
		case R.id.lin_E:
			radioE.setChecked(!radioE.isChecked());
			break;
		case R.id.lin_F:
			radioF.setChecked(!radioF.isChecked());
			break;
		default:
			break;
		}
	}

	
	
	/**
	 * 提交数据 生成睡眠方案
	 * @param wakeTime
	 * @param sleepLen
	 * @param Proberms
	 */
	private void creatSleepImprovePlan(final String wakeTime, String sleepLen, String Proberms, String flag){
		showPro();

		if(wakeValue - lenthValue < 0){
			int hour = (96 + (wakeValue - lenthValue))/4 ;
			int min  = (96 + (wakeValue - lenthValue))%4 *15;
			SleepTime = (hour <10 ? "0"+(hour == 0 ? "0" : hour) : hour) +  ":" + (min < 10 ? "0" + (min == 0 ? "0" : min) : min);
		}else
			SleepTime = ((wakeValue - lenthValue)/4 < 10 ? "0" + ((wakeValue - lenthValue)/4 == 0 ? "0" : (wakeValue - lenthValue)/4) : (wakeValue - lenthValue)/4) + ":"
				+ ((wakeValue - lenthValue)%4 * 15 < 10 ? "0" +((wakeValue - lenthValue)%4 * 15 == 0 ? "0" : (wakeValue - lenthValue)%4 * 15) : (wakeValue - lenthValue)%4 * 15);
		PreManager.instance().saveSleepTime_Setting(
				this, SleepTime);
		PreManager.instance().saveGetupTime_Setting(this, waketime);

		new XiangchengMallProcClass(this).newSleepPlan(PreManager.instance().getUserId(this), wakeTime, sleepLen, Proberms, flag, new InterfaceNewSleepplanCallBack() {
			
			@Override
			public void onSuccess(String icode, List<SleepCaseBean> caseBane) {
				cancelPro();
				sendBroadC(wakeTime);
//				Intent intent = new Intent(ImprSlpPlanActivity.this, ImprSucActivity.class);

//				startActivity(intent);
				AppManager.getAppManager().finishActivity();
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				cancelPro();
				Util.show(ImprSlpPlanActivity.this, strErrMsg);
			}
		});
	}
	
	private void sendBroadC(String wakeTime){
		Intent intent = new Intent();
		intent.setAction(Constant.IMPROVE_SLEEP_PLAN);
		intent.putExtra("getup", wakeTime);
		intent.putExtra("sleep", SleepTime);
		sendBroadcast(intent);
	}
	
}
