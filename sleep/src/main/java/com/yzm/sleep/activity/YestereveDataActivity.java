package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.bean.UserSleepDataBean;
import com.yzm.sleep.model.CalenderSelectDialog;
import com.yzm.sleep.model.CalenderSelectDialog.SelectDayDateListener;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.model.SleepTargetDialog;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.DateOperaUtil;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceQueryUserSleepDataCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.QueryUserSleepDataParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 昨晚数据 软件数据
 * @author tianxun
 */
@SuppressLint("SimpleDateFormat") 
public class YestereveDataActivity extends BaseActivity {
	private TextView sleepLength,sleepTime,getupTime,relative1T1,relative1T2,relative1T3,relative1T4,relative1T5,text0two,text1one,text1two,text1th,text2one,text2two,text2th,
	relative2T1,relative2T2,relative2T3,relative2T4,relative2T5,relative3T1,relative3T2,relative3T3,relative3T4,relative3T5,title;
	private SeekBar relativeSeekBar1,relativeSeekBar2,relativeSeekBar3,sbTimeValue21,sbTimeValue31;
	private int countTime=4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yestereve_data);
		findViewById(R.id.back).setOnClickListener(this);
		title=(TextView) findViewById(R.id.title);
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setCompoundDrawables(null, null, null, null);
		right.setText("日期");
		right.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
		initView();
		initAim();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			title.setTag(sdf.format(calendar.getTime()));
			title.setText(new SimpleDateFormat("MM月dd日").format(calendar.getTime()));
			getYestereveSleepData(sdf.format(calendar.getTime()));
		} catch (Exception e) {
			sleepLength.setText("睡眠时长：00小时00分");
			sleepTime.setText("入睡时间：--:--");
			getupTime.setText("醒来时间：--:--");
			relativeSeekBar1.setVisibility(View.INVISIBLE);
			relativeSeekBar2.setVisibility(View.INVISIBLE);
			relativeSeekBar3.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("App_LastNightDetail"); 
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("App_LastNightDetail");
		MobclickAgent.onPause(this);
	}

	private void initView() {
		sleepLength=(TextView) findViewById(R.id.sleept_lenght);
		findViewById(R.id.sleep_target).setOnClickListener(this);
		sleepTime=(TextView) findViewById(R.id.sleep_time);
		getupTime=(TextView) findViewById(R.id.getup_time);
		View relative1=findViewById(R.id.relative_1);
		View relative2=findViewById(R.id.relative_2);
		View relative3=findViewById(R.id.relative_3);
		
		relative1T1=(TextView) relative1.findViewById(R.id.time1);
		relative1T2=(TextView) relative1.findViewById(R.id.time2);
		relative1T3=(TextView) relative1.findViewById(R.id.time3);
		relative1T4=(TextView) relative1.findViewById(R.id.time4);
		relative1T5=(TextView) relative1.findViewById(R.id.time5);
		text0two=(TextView) relative1.findViewById(R.id.text_two);
		relativeSeekBar1=(SeekBar) relative1.findViewById(R.id.sb_time_value);
		relativeSeekBar1.setEnabled(false);
		relativeSeekBar1.setProgress(countTime*60*3);
		
		relative1T1.setText("偏短");
		relative1T3.setText("健康");
		relative1T5.setText("偏长");
		
		relative2T1=(TextView) relative2.findViewById(R.id.time1);
		relative2T2=(TextView) relative2.findViewById(R.id.time2);
		relative2T3=(TextView) relative2.findViewById(R.id.time3);
		relative2T4=(TextView) relative2.findViewById(R.id.time4);
		relative2T5=(TextView) relative2.findViewById(R.id.time5);
		text1one=(TextView) relative2.findViewById(R.id.text_one);
		text1two=(TextView) relative2.findViewById(R.id.text_two);
		text1th=(TextView) relative2.findViewById(R.id.text_th);
		relativeSeekBar2=(SeekBar) relative2.findViewById(R.id.sb_time_value);
		relativeSeekBar2.setProgress(countTime*60*3);
		relativeSeekBar2.setEnabled(false);
		sbTimeValue21=(SeekBar) relative2.findViewById(R.id.sb_time_value1);
		sbTimeValue21.setVisibility(View.GONE);
		sbTimeValue21.setEnabled(false);
		
		relative2T1.setText("偏早");
		relative2T3.setText("正常");
		relative2T5.setText("偏晚");
		
		relative3T1=(TextView) relative3.findViewById(R.id.time1);
		relative3T2=(TextView) relative3.findViewById(R.id.time2);
		relative3T3=(TextView) relative3.findViewById(R.id.time3);
		relative3T4=(TextView) relative3.findViewById(R.id.time4);
		relative3T5=(TextView) relative3.findViewById(R.id.time5);
		text2one=(TextView) relative3.findViewById(R.id.text_one);
		text2two=(TextView) relative3.findViewById(R.id.text_two);
		text2th=(TextView) relative3.findViewById(R.id.text_th);
		relativeSeekBar3=(SeekBar) relative3.findViewById(R.id.sb_time_value);
		relativeSeekBar3.setProgress(countTime*60*3);
		relativeSeekBar3.setEnabled(false);
		sbTimeValue31=(SeekBar) relative3.findViewById(R.id.sb_time_value1);
		sbTimeValue31.setVisibility(View.GONE);
		sbTimeValue31.setEnabled(false);
		
		relative3T1.setText("偏早");
		relative3T3.setText("正常");
		relative3T5.setText("偏晚");
	}
	
	private void initAim(){
		try {
			float sleepSetTimeLength=PreManager.instance().getRecommendTarget(this);
			//设置睡眠时长
			relative1T2.setText((sleepSetTimeLength-1)+"h");
			relative1T4.setText((sleepSetTimeLength+1)+"h");
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String setGetUpTime=PreManager.instance().getGetupTime_Setting(this);
			String setSleepTime=PreManager.instance().getSleepTime_Setting(this);
			int seekBarP=getScreenWidth()-Util.Dp2Px(this, 20f);
			
			//时长目标
			LinearLayout.LayoutParams text0twoP = (LayoutParams) text0two.getLayoutParams();
			text0twoP.width=seekBarP/3-Util.Dp2Px(this, 2f);
			text0two.setLayoutParams(text0twoP);
			
			//计算入睡目标位置
			String times=new SimpleDateFormat("HH").format(sdf.parse(setSleepTime).getTime()-60*60*1000);
			relative2T2.setText(times+":00");
			String times2=new SimpleDateFormat("HH").format(sdf.parse(setSleepTime).getTime()+60*60*1000);
			relative2T4.setText(times2+":00");
			String mins=new SimpleDateFormat("mm").format(sdf.parse(setSleepTime).getTime());
			int mins1=Integer.parseInt(mins);
			int tempaimPros= (60+mins1)*(countTime/2);
			sbTimeValue21.setProgress(((countTime*60)+tempaimPros)>700?700:((countTime*60)+tempaimPros));
			
			//计算目标段
			int temSm=mins1-15;
			if(temSm < 0){
				temSm=60-15;
			}else{
				temSm=temSm+60;
			}
			
			int temSt=mins1+15;
			int tempTow=0;
			if(temSt>60){
				tempTow=(countTime/2)*(60-mins1+15)+(temSt-60);
			}else{
				tempTow=(countTime/2)*(30);
			}
			
			int temtwo=(int) (seekBarP*(tempTow/((float)(countTime*60*3))));
			LinearLayout.LayoutParams mPrams1two=(LayoutParams) text1two.getLayoutParams();
			mPrams1two.width=temtwo;
			text1two.setLayoutParams(mPrams1two);
			
			int temOne=(temSm*(countTime/2))+countTime*60;
			LinearLayout.LayoutParams mPrams1One=(LayoutParams) text1one.getLayoutParams();
			mPrams1One.weight=temOne/(float)((countTime*60*3)-tempTow);
			text1one.setLayoutParams(mPrams1One);
			
			LinearLayout.LayoutParams mPrams1Th=(LayoutParams) text1th.getLayoutParams();
			mPrams1Th.weight=1-(temOne/(float)((countTime*60*3)-tempTow));
			text1th.setLayoutParams(mPrams1Th);
			
			//计算起床目标位置
			String timeq=new SimpleDateFormat("HH").format(sdf.parse(setGetUpTime).getTime()-60*60*1000);
			relative3T2.setText(timeq+":00");
			String timeq2=new SimpleDateFormat("HH").format(sdf.parse(setGetUpTime).getTime()+60*60*1000);
			relative3T4.setText(timeq2+":00");
			String minq=new SimpleDateFormat("mm").format(sdf.parse(setGetUpTime).getTime());
			int minq1=Integer.parseInt(minq);
			int tempaimProq= (60+minq1)*(countTime/2);
			sbTimeValue31.setProgress(((countTime*60)+tempaimProq)>700?700:((countTime*60)+tempaimProq));
			
			//计算起床目标段
			int temgt=minq1+15;
			int tempgTow=0;
			if(temgt>60){
				tempgTow=(countTime/2)*(60-mins1+15)+(temgt-60);
			}else{
				tempgTow=(countTime/2)*(30);
			}
			int temgtwo=(int) (seekBarP*(tempgTow/((float)(countTime*60*3))));
			LinearLayout.LayoutParams mPrams2two=(LayoutParams) text2two.getLayoutParams();
			mPrams2two.width=temgtwo;
			text2two.setLayoutParams(mPrams2two);
			
			int temgm=minq1-15;
			if(temgm<0){
				temgm=60-15;
			}else{
				temgm=temgm+60;
			}
			
			int temTwo=(temgm*(countTime/2))+countTime*60;
			LinearLayout.LayoutParams mPrams2One=(LayoutParams) text2one.getLayoutParams();
			mPrams2One.weight=temTwo/(float)((countTime*60*3)-tempgTow);
			text2one.setLayoutParams(mPrams2One);
			
			LinearLayout.LayoutParams mPrams2th=(LayoutParams) text2th.getLayoutParams();
			mPrams2th.weight=1-(temTwo/(float)((countTime*60*3)-tempgTow));
			text2th.setLayoutParams(mPrams2th);
		} catch (Exception e) {
		}
	}

	/**
	 * 获取指定日期昨晚的睡眠数据
	 * @param dayDate
	 */
	private void getYestereveSleepData(String dayDate){
		try {
			relative1T1.setTextColor(0xff7F7D92);
			relative1T3.setTextColor(0xff7F7D92);
			relative1T5.setTextColor(0xff7F7D92);
			
			relative2T1.setTextColor(0xff7F7D92);
			relative2T3.setTextColor(0xff7F7D92);
			relative2T5.setTextColor(0xff7F7D92);
			
			relative3T1.setTextColor(0xff7F7D92);
			relative3T3.setTextColor(0xff7F7D92);
			relative3T5.setTextColor(0xff7F7D92);
		
			SoftDayData data= SleepUtils.getDaySleepData(this, dayDate, "1");
			if(data!=null &&!TextUtils.isEmpty(data.getDate()))
				processData(data);
			else
				getNetWorkSleepDate(dayDate);
			
		} catch (Exception e) {
			sleepLength.setText("睡眠时长：00小时00分");
			sleepTime.setText("入睡时间：--:--");
			getupTime.setText("醒来时间：--:--");
			relativeSeekBar1.setVisibility(View.INVISIBLE);
			relativeSeekBar2.setVisibility(View.INVISIBLE);
			relativeSeekBar3.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 获取网络上的睡眠数据
	 * @param dayDate
	 */
	private void getNetWorkSleepDate(String dayDate){
		QueryUserSleepDataParamClass mParams=new QueryUserSleepDataParamClass();
		mParams.my_int_id=PreManager.instance().getUserId(this);
		mParams.sdate=dayDate.replace("-", "");
		mParams.edate=dayDate.replace("-", "");
		new XiangchengProcClass(this).queryUserSleepData(mParams, new InterfaceQueryUserSleepDataCallback() {
			
			@Override
			public void onSuccess(int icode, List<UserSleepDataBean> datas) {
				SoftDayData sleepData=new SoftDayData();
				if(datas!=null && datas.size()>0){
					UserSleepDataBean bean =datas.get(0);
					try {
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMdd");
						SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMddHHmm");
						SimpleDateFormat sdf3=new SimpleDateFormat("HH:mm");
						sleepData.setDate(sdf.format(sdf1.parse(bean.getDate()).getTime()));
						sleepData.setSleepTime(sdf3.format(sdf2.parse(bean.getSleep_point()).getTime()));
						sleepData.setGetUpTime(sdf3.format(sdf2.parse(bean.getWakeup_point()).getTime()));
					} catch (Exception e) {
					}
					processData(sleepData);
				}else{
					sleepLength.setText("睡眠时长：00小时00分");
					sleepTime.setText("入睡时间：--:--");
					getupTime.setText("醒来时间：--:--");
					relativeSeekBar1.setVisibility(View.INVISIBLE);
					relativeSeekBar2.setVisibility(View.INVISIBLE);
					relativeSeekBar3.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				sleepLength.setText("睡眠时长：00小时00分");
				sleepTime.setText("入睡时间：--:--");
				getupTime.setText("醒来时间：--:--");
				relativeSeekBar1.setVisibility(View.INVISIBLE);
				relativeSeekBar2.setVisibility(View.INVISIBLE);
				relativeSeekBar3.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	/**
	 * 处理现实数据 做显示
	 * @param sleepDta
	 */
	private void processData(SoftDayData sleepData){
		try {
			relativeSeekBar1.setVisibility(View.VISIBLE);
			relativeSeekBar2.setVisibility(View.VISIBLE);
			relativeSeekBar3.setVisibility(View.VISIBLE);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String setGetUpTime=PreManager.instance().getGetupTime_Setting(this);
			String setSleepTime=PreManager.instance().getSleepTime_Setting(this);

			String times=new SimpleDateFormat("HH").format(sdf.parse(setSleepTime).getTime()-60*60*1000);
			relative2T2.setText(times+":00");
			String times2=new SimpleDateFormat("HH").format(sdf.parse(setSleepTime).getTime()+60*60*1000);
			relative2T4.setText(times2+":00");
			
			String timeq=new SimpleDateFormat("HH").format(sdf.parse(setGetUpTime).getTime()-60*60*1000);
			relative3T2.setText(timeq+":00");
			String timeq2=new SimpleDateFormat("HH").format(sdf.parse(setGetUpTime).getTime()+60*60*1000);
			relative3T4.setText(timeq2+":00");
			
			if(sleepData!=null&&!TextUtils.isEmpty(sleepData.getSleepTime())&&!TextUtils.isEmpty(sleepData.getGetUpTime())){
				float sleepTimeLength=countLengthByTime(sleepData.getSleepTime(),sleepData.getGetUpTime());
				
				if (!TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(this))) {
					
					PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(this, TimeFormatUtil.getYesterDay(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "yyyy-MM-dd"));
					if (pModel != null) {
						if (pModel.getBfr() != null) {
							SleepDataHead datahead = BluetoothDataFormatUtil.format3(pModel.getBfr(), 10);
							pModel.getBfr();
							sleepData.setTotalSleepTime(datahead.TotalSleepTime);
						}
					}
				}
				
				sleepLength.setText("睡眠时长："+SleepUtils.getSleepLengthString(this,sleepData));
				if(!TextUtils.isEmpty(sleepData.getSleepTime()))
					sleepTime.setText("入睡时间："+sleepData.getSleepTime());
				else
					sleepTime.setText("入睡时间：--:--");
			
				if(!TextUtils.isEmpty(sleepData.getGetUpTime()))
					getupTime.setText("醒来时间："+sleepData.getGetUpTime());
				else
					getupTime.setText("醒来时间：--:--");
				
				float sleepSetTimeLength=PreManager.instance().getRecommendTarget(this);
				
				//设置睡眠时长
				relative1T2.setText((sleepSetTimeLength-1)+"h");
				relative1T4.setText((sleepSetTimeLength+1)+"h");
				try {
					relative1T1.setTextColor(0xff7F7D92);
					relative1T3.setTextColor(0xff7F7D92);
					relative1T5.setTextColor(0xff7F7D92);
					if(sleepTimeLength < (sleepSetTimeLength-1)){
						relative1T1.setTextColor(0xff4471FF);
						float tempTime =(sleepSetTimeLength-1)-sleepTimeLength;
						int tempPro= (int) (tempTime*60);
						relativeSeekBar1.setProgress((countTime*60-tempPro)<25?25:(countTime*60-tempPro));
						relativeSeekBar1.setThumb(getResources().getDrawable(R.drawable.index_icon1));
					}else if(sleepTimeLength > (sleepSetTimeLength+1)){
						relative1T5.setTextColor(0xff4471FF);
						float tempTime = sleepTimeLength-(sleepSetTimeLength+1);
						int tempPro= (int) (tempTime*60);
						relativeSeekBar1.setProgress(((countTime*60*2)+tempPro)>695?695:((countTime*60*2)+tempPro));	
						relativeSeekBar1.setThumb(getResources().getDrawable(R.drawable.index_icon1));
					}else{
						relative1T3.setTextColor(0xff8745ff);
						float tempTime = sleepTimeLength-(sleepSetTimeLength-1);
						int tempPro= (int) (tempTime*(60*countTime/2));
						relativeSeekBar1.setProgress(((countTime*60)+tempPro)>695?695:((countTime*60)+tempPro));
						relativeSeekBar1.setThumb(getResources().getDrawable(R.drawable.index_icon));
					}
				} catch (Exception e) {
				}
				
				SoftDayData aimData=new SoftDayData();
				aimData.setDate(title.getTag().toString());
				aimData.setSleepTime(setSleepTime);
				aimData.setGetUpTime(setGetUpTime);
				long sleepTimeL=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(title.getTag().toString()+" "+setSleepTime).getTime();
				aimData.setSleepTimeLong(String.valueOf(sleepTimeL));
				long getTimeL=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(title.getTag().toString()+" "+setGetUpTime).getTime();
				aimData.setGetUpTimeLong(String.valueOf(getTimeL));
				aimData=DateOperaUtil.comperaDate(aimData);
				
				//设置入睡时间
				try {
					relative2T1.setTextColor(0xff7F7D92);
					relative2T3.setTextColor(0xff7F7D92);
					relative2T5.setTextColor(0xff7F7D92);
					
					long sleepLong = Long.parseLong(sleepData.getSleepTimeLong());
					long tempSleepTimeL =Long.parseLong(aimData.getSleepTimeLong());
					long min=0;
					try {
						String[] time = setSleepTime.split(":");
						min=Integer.parseInt(time[1]) * 60 * 1000;
					} catch (Exception e) {
					}
					
					long aimSleepm = tempSleepTimeL - 60 * 60 * 1000 -min;
					long aimSleepM = tempSleepTimeL + 60 * 60 * 1000 - min;
					
					//tempSleepTimeL  1446913800000   sleepLong 1446915660000  aimSleepM 1446917400000
					if(sleepLong < aimSleepm){
						int tempPro= (int) ((aimSleepm-sleepLong)/( 60 * 1000));
						relativeSeekBar2.setProgress((countTime*60-tempPro)<25? 25:(countTime*60-tempPro));
					}else if(sleepLong > aimSleepM){
						int tempPro= (int) ((sleepLong-aimSleepM)/(60 * 1000));
						relativeSeekBar2.setProgress(((countTime*60*2)+tempPro)>685?685:((countTime*60*2)+tempPro));
					}else{
						//TODO
						int tempPro= (int) (((sleepLong-aimSleepm)/(60 * 1000))*(countTime/2));
						relativeSeekBar2.setProgress(((countTime*60)+tempPro));
					}
					
					if(sleepLong - tempSleepTimeL < -(15 * 60 * 1000)){
						relativeSeekBar2.setThumb(getResources().getDrawable(R.drawable.index_icon1));
						relative2T1.setTextColor(0xff4471FF);
					}else if(sleepLong - tempSleepTimeL > (15 * 60 * 1000)){
						relative2T5.setTextColor(0xff4471FF);
						relativeSeekBar2.setThumb(getResources().getDrawable(R.drawable.index_icon1));
					}else{
						relative2T3.setTextColor(0xff8745ff);
						relativeSeekBar2.setThumb(getResources().getDrawable(R.drawable.index_icon));
					}
				} catch (Exception e) {}
				//设置醒来时间
				try {
					long getUpLong=Long.parseLong(sleepData.getGetUpTimeLong());
					long tempGetTimeL=Long.parseLong(aimData.getGetUpTimeLong());
					long min= 0 ;
					try {
						String[] time = setGetUpTime.split(":");
						min = Integer.parseInt(time[1]) * 60 *1000;
					} catch (Exception e) {
					}
					long aimGetUpm = tempGetTimeL - 60*60*1000 - min;
					long aimGetUpM = tempGetTimeL + 60*60*1000 - min;
					
					if(getUpLong < aimGetUpm){
						int tempPro= (int) ((aimGetUpm - getUpLong)/(60 * 1000));
						relativeSeekBar3.setProgress((countTime*60 - tempPro) < 25 ? 25:(countTime*60-tempPro));
					}else if(getUpLong > aimGetUpM){
						int tempPro= (int) ((getUpLong - aimGetUpM)/(60 * 1000));
						relativeSeekBar3.setProgress(((countTime*60*2) + tempPro)> 695 ? 695:((countTime*60*2)+tempPro));	
					}else{
						int tempPro= (int) ((getUpLong-aimGetUpm)/(60 * 1000)*(countTime/2));
						relativeSeekBar3.setProgress(((countTime*60)+tempPro));
					}
					
					if(getUpLong - tempGetTimeL < -(15 * 60 * 1000)){
						relativeSeekBar3.setThumb(getResources().getDrawable(R.drawable.index_icon1));
						relative3T1.setTextColor(0xff4471FF);
					}else if(getUpLong - tempGetTimeL > (15 * 60 * 1000)){
						relativeSeekBar3.setThumb(getResources().getDrawable(R.drawable.index_icon1));
						relative3T5.setTextColor(0xff4471FF);
					}else{
						relativeSeekBar3.setThumb(getResources().getDrawable(R.drawable.index_icon));
						relative3T3.setTextColor(0xff8745ff);
					}
				
				} catch (Exception e) {
				}
				
			}else{
				sleepLength.setText("睡眠时长：00小时00分");
				sleepTime.setText("入睡时间：--:--");
				getupTime.setText("醒来时间：--:--");
				relativeSeekBar1.setVisibility(View.INVISIBLE);
				relativeSeekBar2.setVisibility(View.INVISIBLE);
				relativeSeekBar3.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			sleepLength.setText("睡眠时长：00小时00分");
			sleepTime.setText("入睡时间：--:--");
			getupTime.setText("醒来时间：--:--");
			relativeSeekBar1.setVisibility(View.INVISIBLE);
			relativeSeekBar2.setVisibility(View.INVISIBLE);
			relativeSeekBar3.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 根据起床时间和入睡时间，计算睡眠时长
	 * @param alarmSleepTime  入睡时间（开始时间）
	 * @param alarmGetUpTime  起床时间（结束时间）
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public float countLengthByTime(String sleepTime, String getUpTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			long sleepTimeLong = sdf.parse(sleepTime).getTime();
			long getUpTimeLong = sdf.parse(getUpTime).getTime();
			long totalTime = getUpTimeLong - sleepTimeLong;
			if(totalTime < 0)
				totalTime += 24 * 60 * 60 * 1000;
			return totalTime/((float)60 * 60 * 1000);
		} catch (Exception e) {
		}
		return 0;
	}

	/**获取睡眠时间长度(返回HH:mm时间格式)@param dayData */
	@SuppressLint("SimpleDateFormat") 
	private String getRealTimeString(SoftDayData dayData){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date d1 = sdf.parse(dayData.getGetUpTime());
			Date d2 = sdf.parse(dayData.getSleepTime());
			double hour = (float)(d1.getTime() - d2.getTime())/(1000 * 60 * 60);
			if(hour < 0)
				hour += 24;
			int h = (int)hour;
			int m = (int) (60 * (hour - h));
			String hourString = "00";
			String minString = "00";
			if(h < 10)
				hourString = "0" + String.valueOf(h);
			else
				hourString = String.valueOf(h);
			if(m < 10)
				minString = "0" + String.valueOf(m);
			else 
				minString = String.valueOf(m);
			return hourString + ":" + minString;
		} catch (Exception e) {
		}
		return "00:00";
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppManager.getAppManager().finishActivity();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.sleep_target:
			SleepTargetDialog dialog=new SleepTargetDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			break;
		case R.id.btn_title_right:
			new CalenderSelectDialog(this,getScreenWidth(),title.getTag().toString(),new SelectDayDateListener() {
				
				@Override
				public void selectday(String dayDate) {
					title.setTag(dayDate);
					try {
						long day = new SimpleDateFormat("yyyy-MM-dd").parse(dayDate).getTime();
						Calendar calendar=Calendar.getInstance();
						calendar.setTime(new Date());
						String currentYear=new SimpleDateFormat("yyyy").format(calendar.getTime());
						String selectYear=new SimpleDateFormat("yyyy").format(day);
						if(currentYear.equals(selectYear)){
							title.setText(new SimpleDateFormat("MM月dd日").format(day));
						}else
							title.setText(dayDate);
					} catch (Exception e) {
						title.setText(dayDate);
					}
					getYestereveSleepData(dayDate);
				}
			}).show();
			break;
		default:
			break;
		}
	}

}
