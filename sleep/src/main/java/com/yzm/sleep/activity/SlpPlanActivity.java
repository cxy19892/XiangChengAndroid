package com.yzm.sleep.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.SleepCaseBean;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceGetSleepplanCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.MyClockPro;

public class SlpPlanActivity extends BaseActivity {

	private TextView startTime, stopTime, timeLenth, stateName, stateSuggest;
	private TextView title;
	private Button resetbtn, nextBtn, lastBtn;
	/** 0 : 需要请求当前阶段， 1：当前阶段已经存在*/
	private int type = 0;
	private List<SleepCaseBean> myplanlist = null;
	private SleepCaseBean CurrentCase;
	private MyClockPro myClockPro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slp_plan);
		initview();
		CurrentCase = (SleepCaseBean) getIntent().getSerializableExtra("plan");
		if(CurrentCase != null){
			type = 1;
			setInitData(CurrentCase);
		}else{
			type = 0;
		}
		getSleepPlan();
	}
	
	private void initview(){
		startTime = (TextView) findViewById(R.id.tv_start_time);
		stopTime = (TextView) findViewById(R.id.tv_stop_time);
		timeLenth = (TextView) findViewById(R.id.tv_state_time);
		stateName = (TextView) findViewById(R.id.tv_state_name);
		stateSuggest = (TextView) findViewById(R.id.tv_state_suggest);
		nextBtn = (Button) findViewById(R.id.btn_next);
		lastBtn = (Button) findViewById(R.id.btn_last);
		myClockPro = (MyClockPro) findViewById(R.id.myclock_pro);
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		resetbtn = (Button) findViewById(R.id.btn_title_right);
		
		
		resetbtn.setText("重置");
		resetbtn.setVisibility(View.VISIBLE);
		nextBtn.setVisibility(View.GONE);
		lastBtn.setVisibility(View.GONE);
		resetbtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		lastBtn.setOnClickListener(this);
	}
	
	private void setInitData(SleepCaseBean caseBane){
		startTime.setText(caseBane.getStart_time());
		stopTime.setText(caseBane.getEnd_time());
//		timeLenth.setText(getLenth(caseBane.getShichang()));
		stateSuggest.setText(caseBane.getSuggest());
		title.setText(caseBane.getTitle());
		String [] start = caseBane.getStart_time().split(":");
		String [] stop = caseBane.getEnd_time().split(":");
		
		int starthou = 0, startmin=0, stophour=0, stopmin=0;
		try {
			starthou = Integer.parseInt(start[0]);
			startmin = Integer.parseInt(start[1]);
			stophour = Integer.parseInt(stop[0]);
			stopmin = Integer.parseInt(stop[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		myClockPro.setTime(starthou, startmin, stophour , stopmin, true);
	}
	
//	private void setInitData(SleepCaseBean caseBane, SleepCaseBean nextCase, SleepCaseBean lastCase){
//		startTime.setText(caseBane.getStart_time() + "\n" + lastCase.getTitle());
//		stopTime.setText(caseBane.getEnd_time()  + "\n" + nextCase.getTitle());
////		timeLenth.setText(getLenth(caseBane.getShichang()));
//		stateSuggest.setText(caseBane.getSuggest());
//		title.setText(caseBane.getTitle());
//		String [] start = caseBane.getStart_time().split(":");
//		String [] stop = caseBane.getEnd_time().split(":");
//		
//		int starthou = 0, startmin=0, stophour=0, stopmin=0;
//		try {
//			starthou = Integer.parseInt(start[0]);
//			startmin = Integer.parseInt(start[1]);
//			stophour = Integer.parseInt(stop[0]);
//			stopmin = Integer.parseInt(stop[1]);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		myClockPro.setTime(starthou, startmin, stophour , stopmin, true);
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity(SlpPlanActivity.class);
			break;
		case R.id.btn_title_right:
			Intent intent = new Intent();
			intent.setClass(SlpPlanActivity.this, ImprSlpPlanActivity.class);
			intent.putExtra("flag", 2);
			startActivity(intent);
			AppManager.getAppManager().finishActivity(SlpPlanActivity.class);
			break;
		case R.id.btn_next:
			if(myplanlist != null){
				SleepCaseBean tempBean = getNextOrLastPlan(myplanlist, CurrentCase, 1);
				if(tempBean != null){
					CurrentCase = tempBean;
					setInitData(CurrentCase);
//					setInitData(CurrentCase, getNextOrLastPlan(myplanlist, CurrentCase, 1), getNextOrLastPlan(myplanlist, CurrentCase, -1));
					tempBean = null;
				}
			}else{
				getSleepPlan();
			}
			break;
		case R.id.btn_last:
			if(myplanlist != null){
				SleepCaseBean tempBean = getNextOrLastPlan(myplanlist, CurrentCase, -1);
				if(tempBean != null){
					CurrentCase = tempBean;
					setInitData(CurrentCase);
//					setInitData(CurrentCase, getNextOrLastPlan(myplanlist, CurrentCase, 1), getNextOrLastPlan(myplanlist, CurrentCase, -1));
					tempBean = null;
				}
			}else{
				getSleepPlan();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
	
	private void getSleepPlan(){
		new XiangchengMallProcClass(this).getSleepPlanById(PreManager.instance().getUserId(this), new InterfaceGetSleepplanCallBack() {
			
			@Override
			public void onSuccess(String icode, List<SleepCaseBean> planlist) {
				nextBtn.setVisibility(View.VISIBLE);
				lastBtn.setVisibility(View.VISIBLE);
				myplanlist = planlist;
				if(type == 0){
					CurrentCase = getCurrentPlan(planlist);
					if(CurrentCase != null){
						setInitData(CurrentCase);
//						setInitData(CurrentCase, getNextOrLastPlan(myplanlist, CurrentCase, 1), getNextOrLastPlan(myplanlist, CurrentCase, -1));
					}
				}
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				
				
			}
		});
	}
	
	private String getLenth(int second){
		return second / 3600 +"小时" + (second/60) % 60 + "分";
	}
	
	/**
	 * 获取当前阶段
	 * @param datas
	 */
	private SleepCaseBean getCurrentPlan(List<SleepCaseBean> datas){
		for (SleepCaseBean sleepCaseBean : datas) {
			if(TimeFormatUtil.isBetweenTime(sleepCaseBean.getStart_time(), sleepCaseBean.getEnd_time())){
				return sleepCaseBean;
			}
		}
		return null;
	}
	/**
	 * 
	 * @param datas 计划列表
	 * @param CurrentPlan 当前阶段
	 * @param state -1 上一阶段， 1下一阶段
	 * @return
	 */
	private SleepCaseBean getNextOrLastPlan(List<SleepCaseBean> datas, SleepCaseBean CurrentPlan, int state){
		for (SleepCaseBean tempCaseBean : datas) {
			if(state == 1 && tempCaseBean.getStart_time().equals(CurrentPlan.getEnd_time())){
				return tempCaseBean;
			}
			if(state == -1 && tempCaseBean.getEnd_time().equals(CurrentPlan.getStart_time())){
				return tempCaseBean;
			}
		}
		return null;
	}
	
	
}
