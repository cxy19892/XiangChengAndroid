package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.bean.SleepCaseBean;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.model.RollPickerDialog.RollCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceImproveSleepCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 生成睡眠改善方案
 */
public class ImprSleepPlanActivity extends BaseActivity {

	private RelativeLayout relProberm1, relProberm2, relProberm3, relProberm4;
	private TextView tvProberm1, tvProberm2, tvProberm3, tvProberm4;
//	private ImageView imgSelect1, imgSelect2, imgSelect3, imgSelect4;
	private Button btnCommit;
	private TextView wakeupTime, sleepLenth;
	private StringBuffer sBuffer;
	private RollPickerDialog dialog;
	private String wakeTime;
	private int sleepLen = 0;
	private ArrayList<String> listH, listM;
	private String length1 = "", length2 = "";
	
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
		setContentView(R.layout.activity_impr_sleep_plan);
		initviews();
		inidDialogData();
		getSpTime();
	}

	private void initviews() {
		listM = new ArrayList<String>();
		listH = new ArrayList<String>();
		
		relProberm1 = (RelativeLayout) findViewById(R.id.rel_sleep_prog1);
		relProberm2 = (RelativeLayout) findViewById(R.id.rel_sleep_prog2);
		relProberm3 = (RelativeLayout) findViewById(R.id.rel_sleep_prog3);
		relProberm4 = (RelativeLayout) findViewById(R.id.rel_sleep_prog4);
		tvProberm1 = (TextView) findViewById(R.id.tv_sleep_prog1);
		tvProberm2 = (TextView) findViewById(R.id.tv_sleep_prog2);
		tvProberm3 = (TextView) findViewById(R.id.tv_sleep_prog3);
		tvProberm4 = (TextView) findViewById(R.id.tv_sleep_prog4);
//		imgSelect1 = (ImageView) findViewById(R.id.img_sleep_prog1);
//		imgSelect2 = (ImageView) findViewById(R.id.img_sleep_prog2);
//		imgSelect3 = (ImageView) findViewById(R.id.img_sleep_prog3);
//		imgSelect4 = (ImageView) findViewById(R.id.img_sleep_prog4);
		wakeupTime = (TextView) findViewById(R.id.tv_wakeup_time);
		sleepLenth = (TextView) findViewById(R.id.tv_sleep_lenth);
		btnCommit = (Button) findViewById(R.id.btn_improve_commit);
		((TextView)findViewById(R.id.title)).setText("7日睡眠改善方案");//7日睡眠改善方案
		findViewById(R.id.back).setOnClickListener(this);
		
		tvProberm1.setText("入睡困难");
		tvProberm2.setText("中途易醒");
		tvProberm3.setText("早醒");
		tvProberm4.setText("都没有");
		tvProberm1.setTag("");
		tvProberm2.setTag("");
		tvProberm3.setTag("");
		tvProberm4.setTag("");
		wakeupTime.setText("选择");
		sleepLenth.setText("选择");
		
		relProberm1.setOnClickListener(this);
		relProberm2.setOnClickListener(this);
		relProberm3.setOnClickListener(this);
		relProberm4.setOnClickListener(this);
		wakeupTime.setOnClickListener(this);
		sleepLenth.setOnClickListener(this);
		btnCommit.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().finishActivity();
			return false;
		}else
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_sleep_prog1:
			if(TextUtils.isEmpty(tvProberm1.getTag().toString())){
				tvProberm1.setTag("入睡困难");
				tvProberm1.setTextColor(getResources().getColor(R.color.white));
//				imgSelect1.setVisibility(View.VISIBLE);
				relProberm1.setBackgroundResource(R.drawable.custom_round_select_small);
				tvProberm4.setTag("");
//				imgSelect4.setVisibility(View.GONE);
				relProberm4.setBackgroundResource(R.drawable.custom_round_normal_small);
			}else{
				tvProberm1.setTag("");
//				imgSelect1.setVisibility(View.GONE);
				tvProberm1.setTextColor(getResources().getColor(R.color.ct_color));
				relProberm1.setBackgroundResource(R.drawable.custom_round_normal_small);
			}
			break;
		case R.id.rel_sleep_prog2:
			if(TextUtils.isEmpty(tvProberm2.getTag().toString())){
				tvProberm2.setTag("中途易醒");
//				imgSelect2.setVisibility(View.VISIBLE);
				tvProberm2.setTextColor(getResources().getColor(R.color.white));
				relProberm2.setBackgroundResource(R.drawable.custom_round_select_small);
				tvProberm4.setTag("");
//				imgSelect4.setVisibility(View.GONE);
				relProberm4.setBackgroundResource(R.drawable.custom_round_normal_small);
			}else{
				tvProberm2.setTag("");
//				imgSelect2.setVisibility(View.GONE);
				tvProberm2.setTextColor(getResources().getColor(R.color.ct_color));
				relProberm2.setBackgroundResource(R.drawable.custom_round_normal_small);
			}
			break;
		case R.id.rel_sleep_prog3:
			if(TextUtils.isEmpty(tvProberm3.getTag().toString())){
				tvProberm3.setTag("早醒");
//				imgSelect3.setVisibility(View.VISIBLE);
				tvProberm3.setTextColor(getResources().getColor(R.color.white));
				relProberm3.setBackgroundResource(R.drawable.custom_round_select_small);
				tvProberm4.setTag("");
//				imgSelect4.setVisibility(View.GONE);
				relProberm4.setBackgroundResource(R.drawable.custom_round_normal_small);
			}else{
				tvProberm3.setTag("");
//				imgSelect3.setVisibility(View.GONE);
				tvProberm3.setTextColor(getResources().getColor(R.color.ct_color));
				relProberm3.setBackgroundResource(R.drawable.custom_round_normal_small);
			}
			break;
		case R.id.rel_sleep_prog4:
			if(TextUtils.isEmpty(tvProberm4.getTag().toString())){
				tvProberm4.setTag("都没有");
//				imgSelect4.setVisibility(View.VISIBLE);
//				imgSelect3.setVisibility(View.GONE);
//				imgSelect2.setVisibility(View.GONE);
//				imgSelect1.setVisibility(View.GONE);
				tvProberm4.setTextColor(getResources().getColor(R.color.white));
				tvProberm1.setTextColor(getResources().getColor(R.color.ct_color));
				tvProberm2.setTextColor(getResources().getColor(R.color.ct_color));
				tvProberm3.setTextColor(getResources().getColor(R.color.ct_color));
				relProberm1.setBackgroundResource(R.drawable.custom_round_normal_small);
				relProberm2.setBackgroundResource(R.drawable.custom_round_normal_small);
				relProberm3.setBackgroundResource(R.drawable.custom_round_normal_small);
				relProberm4.setBackgroundResource(R.drawable.custom_round_select_small);
				tvProberm3.setTag("");
				tvProberm2.setTag("");
				tvProberm1.setTag("");
			}else{
				tvProberm4.setTag("");
//				imgSelect4.setVisibility(View.GONE);
				tvProberm4.setTextColor(getResources().getColor(R.color.ct_color));
				relProberm4.setBackgroundResource(R.drawable.custom_round_normal_small);
			}
			break;
		case R.id.tv_wakeup_time:
			dialog = new RollPickerDialog(this, new RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,
						String select3) {
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
					wakeTime = (select1>=10? select1 : "0"+select1)+":"+ (select2 >= 10 ? select2 : "0"+select2);
					wakeupTime.setText((select1>=10? select1 : "0"+select1)+":"+ (select2 >= 10 ? select2 : "0"+select2));
				}
			});
			
			String timeStr = wakeupTime.getText().toString();
			int hourset=0,miunset=0;
			if(!timeStr.equals("选择")){
				try {
					hourset = Integer.parseInt(timeStr.split(":")[0]);
					miunset = Integer.parseInt(timeStr.split(":")[1]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			dialog.SetData(1, "起床时间", new int[]{0, 23}, hourset, new int[]{0, 59}, miunset, null, 0);
			dialog.show();
			break;
		case R.id.tv_sleep_lenth:
			dialog = new RollPickerDialog(this, new RollCallBack() {
				
				@Override
				public void setResaultRollString(String select1, String select2,
						String select3) {
					String temp = select1 + select2;
					if(!temp.equals(length1 + length2)){
						length1 = select1;
						length2 = select2;
						sleepLenth.setText(length1 + length2);
					}
					try {
						String sleepLength = new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH小时mm分").parse(length1 + length2).getTime());
						int sleepMinH=Integer.parseInt(sleepLength.split(":")[0]);
						int sleepMinM=Integer.parseInt(sleepLength.split(":")[1]);
						sleepLen = sleepMinH*60 + sleepMinM;
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
//					sleepLen = (select1 * 60 + select2);
//					sleepLenth.setText((select1 > 10 ? select1 : "0"+select1)+"小时"+ (select2 > 10 ? select2 : "0"+select2)+"分");
				}
			});
			
			String lenthStr = sleepLenth.getText().toString();
			int hour=0,min=0;
			if(!lenthStr.equals("选择")){
				for (int i = 0; i < listH.size(); i++) {
					if(length1.equals(listH.get(i))){
						hour = i;
						break;
					}
				}
				for (int i = 0; i < listM.size(); i++) {
					if(length2.equals(listM.get(i))){
						min = i;
						break;
					}
				}
			}
			dialog.SetStrDate(2, "睡眠时长", listH, hour, listM, min, null, 0);
			dialog.show();
			break;
		case R.id.btn_improve_commit:
			
			if(TextUtils.isEmpty(wakeTime)){
				toastMsg("请设置起床时间");
			}else if(sleepLen == 0){
				toastMsg("请设置睡眠时长");
			}else {
				sBuffer = new StringBuffer();
				if(!TextUtils.isEmpty(tvProberm1.getTag().toString())){
					sBuffer.append(tvProberm1.getTag().toString());
					sBuffer.append(",");
				}
				if(!TextUtils.isEmpty(tvProberm2.getTag().toString())){
					sBuffer.append(tvProberm2.getTag().toString());
					sBuffer.append(",");
				}
				if(!TextUtils.isEmpty(tvProberm3.getTag().toString())){
					sBuffer.append(tvProberm3.getTag().toString());
					sBuffer.append(",");
				}
				if(!TextUtils.isEmpty(tvProberm4.getTag().toString())){
					sBuffer.append(tvProberm4.getTag().toString());
					sBuffer.append(",");
				}
				if(sBuffer.length()>0){
					sBuffer.deleteCharAt(sBuffer.length()-1);  
				}
				creatSleepImprovePlan(wakeTime, sleepLen, sBuffer.toString());
				setSPtime(wakeTime, sleepLen);
			}
			break;
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}
	
	private void inidDialogData(){
		for (int i = 1; i < 13; i++) 
			listH.add((i < 10 ? "0"+i : i)+"小时");
		for (int i = 0; i < 60; i++) 
			listM.add((i < 10 ? "0"+i : i)+"分");
	}
	
	
	/**
	 * 提交数据 生成睡眠方案
	 * @param wakeTime
	 * @param sleepLen
	 * @param Proberms
	 */
	private void creatSleepImprovePlan(String wakeTime, int sleepLen, String Proberms){
		showPro();
		new XiangchengMallProcClass(this).ImproveSleep(PreManager.instance().getUserId(this), wakeTime, sleepLen, Proberms, new InterfaceImproveSleepCallBack() {
			
			@Override
			public void onSuccess(String icode, List<SleepCaseBean> datas) {
				cancelPro();
				startActivity(new Intent(ImprSleepPlanActivity.this, SleepCaseActivity.class));
				sendBroadC();
				AppManager.getAppManager().finishActivity();
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				cancelPro();
				Util.show(ImprSleepPlanActivity.this, strErrMsg);
			}
		});
	}
	
	private void setSPtime(String getuptime, int sleeplenth){
		int hour = 0, min = 0;
		int startTime = 0;
		try {
			hour = Integer.parseInt(getuptime.split(":")[0]);
			min  = Integer.parseInt(getuptime.split(":")[1]);
			startTime = hour*60+min - sleeplenth;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		int endTime = hour*60+min;
		if(startTime < 0){
			startTime = 24*60+startTime;
		}
		SharedPreferences sp = getSharedPreferences(SleepInfo.SLEEP_SETTIME, MODE_APPEND);
		Editor edit = sp.edit();
		edit.putString(SleepInfo.STARTTIME, startTime+"");
		edit.putString(SleepInfo.ENDTIME, endTime + "");
		edit.putString(SleepInfo.SLEEP_LENTH, sleeplenth+"");
		edit.commit();
	}
	
	
	private void getSpTime(){
		SharedPreferences sp =getSharedPreferences(
				SleepInfo.SLEEP_SETTIME, MODE_APPEND);
		String startTime = sp.getString(SleepInfo.STARTTIME, "");
		String endTime = sp.getString(SleepInfo.ENDTIME, "");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void sendBroadC(){
		Intent intent = new Intent();
		intent.setAction(Constant.SLEEP_PLAN_IMPROVE_ACTION);
		sendBroadcast(intent);
	}
	
}
