package com.yzm.sleep.activity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.bean.CoffoBean;
import com.yzm.sleep.bean.EnvBean;
import com.yzm.sleep.bean.HomeDataBean;
import com.yzm.sleep.bean.SleepSignDataBean;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.bean.SmokeBean;
import com.yzm.sleep.bean.SportBean;
import com.yzm.sleep.bean.WeightBean;
import com.yzm.sleep.bean.WinBean;
import com.yzm.sleep.model.ChangplanBean;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.AvgTimeResult;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.GetAvgTimeParamItem;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceContinuePlanCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceGetReportCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomDialog;
import com.yzm.sleep.widget.ReportSleepLengthTable;
import com.yzm.sleep.widget.SleepTimePointTable;

/**
 * 睡眠数据分析 报告
 */
@SuppressLint("SimpleDateFormat")
public class SleepDataReportActivity extends BaseActivity {

	private View scView, rl_smokingp, rl_winep, rl_coffeep, rl_weightp, rl_sportp, bottomView;
	private TextView tvExplain, tvLengthExplain, tvXianglv, tvSleepExplain,weight,maxWeight,curWeight, 
			tvGetUpExplain, smokingHint, smokingLater, wineHint, coffeeHint, weightHint, sportHint, tv8;
	private LinearLayout rlSmokingSleepQ, rlWine, rlCoffee, rlSport,rlHuanjing, rlHjMessage;
	private ImageView weightPic;
	private SleepTimePointTable sleepTab, getupTab;
	private ReportSleepLengthTable reportTab;
	private SimpleDateFormat simp;
	private ProgressUtils pro;
	private String dataType = "1";
	private ArrayList<SoftDayData> softList;
	private List<SleepStatusBean> mList;
//	private String sleepDate/* 入睡时间 */, getupDate/* 起床时间 */;
	private int hasDataCount = 0, totalTime = 0, lenghtWDb = 0, xlDab = 0,
			totalXl = 0, compalteGetupCount = 0, compalteSleepCount = 0;
	private CustomDialog dialog;
	private ChangplanBean changplanBean;

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
		setContentView(R.layout.activity_sleepdata_report);
		simp = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		softList = new ArrayList<SoftDayData>();
		mList = new ArrayList<SleepStatusBean>();
//		getupDate = PreManager.instance().getGetupTime_Setting(this);
//		sleepDate = PreManager.instance().getSleepTime_Setting(this);
		initView();
		getReportData();
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.btn_2).setOnClickListener(this);
//		findViewById(R.id.btn_3).setOnClickListener(this);
		bottomView = findViewById(R.id.bottom_view);
		TextView title = (TextView) findViewById(R.id.title);
		title.setTextColor(getResources().getColor(R.color.black));
		ImageView bgImg = (ImageView) findViewById(R.id.bg_img);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.height = getScreenWidth()/2;
		bgImg.setLayoutParams(params);

		title.setText("睡眠报告");
		scView = findViewById(R.id.sc_view);
		tvExplain = (TextView) findViewById(R.id.tv_explain);
		tvLengthExplain = (TextView) findViewById(R.id.tv_length_explain);
		tvXianglv = (TextView) findViewById(R.id.tv_xiaolv);
		reportTab = (ReportSleepLengthTable) findViewById(R.id.report_tab);
		tvSleepExplain = (TextView) findViewById(R.id.tv_sleep_explain);
		sleepTab = (SleepTimePointTable) findViewById(R.id.soft_tab1);
		tvGetUpExplain = (TextView) findViewById(R.id.tv_getup_explain);
		getupTab = (SleepTimePointTable) findViewById(R.id.soft_tab2);
		
		rl_smokingp = findViewById(R.id.rl_smokingp);
		smokingHint = (TextView) findViewById(R.id.smoking_hint);
		smokingLater = (TextView) findViewById(R.id.smoking_later);
		rlSmokingSleepQ = (LinearLayout) findViewById(R.id.rl_smoking_sleep);
		
		rl_winep = findViewById(R.id.rl_winep);
		wineHint = (TextView) findViewById(R.id.wine_hint);
		rlWine = (LinearLayout) findViewById(R.id.rl_wine);
		
		rl_coffeep = findViewById(R.id.rl_coffeep);
		coffeeHint = (TextView) findViewById(R.id.coffee_hint);
		rlCoffee = (LinearLayout) findViewById(R.id.rl_coffee);
		
		rl_weightp = findViewById(R.id.rl_weightp);
		weightHint = (TextView) findViewById(R.id.weight_hint);
		weightPic = (ImageView) findViewById(R.id.weight_pic);
		weight = (TextView) findViewById(R.id.weight);
		maxWeight = (TextView) findViewById(R.id.max_weight);
		curWeight = (TextView) findViewById(R.id.cur_weight);
		tv8 = (TextView) findViewById(R.id.tv8);
		
		rl_sportp = findViewById(R.id.rl_sportp);
		sportHint = (TextView) findViewById(R.id.sport_hint);
		rlSport = (LinearLayout) findViewById(R.id.rl_sport);
		
		rlHuanjing = (LinearLayout) findViewById(R.id.rl_huanjing);
		rlHjMessage = (LinearLayout) findViewById(R.id.rl_huanjing_message);
	}

	private void getReportData(){
		showPro();
		if(checkNetWork(this)){
			new XiangchengMallProcClass(this).getSleepReport(PreManager.instance().getUserId(this), dataType, new InterfaceGetReportCallBack() {
				
				@Override
				public void onSuccess(String icode, ChangplanBean changplanBean,  String topText, String plansleep,
						List<SleepSignDataBean> datas, SmokeBean smokeBean,
						WinBean winBean, CoffoBean coffoBean, WeightBean weightBean,
						SportBean sportBean, EnvBean envBean) {
					cancelPro();
					doCallBackData(changplanBean, topText, plansleep, datas, envBean);
					doCallData(smokeBean, winBean, coffoBean, weightBean, sportBean);
				}
				
				@Override
				public void onError(String icode, String strErrMsg) {
					cancelPro();
					Util.show(SleepDataReportActivity.this, strErrMsg);
				}
			});
		}else{
			Util.show(this, "网络连接不可以");
			cancelPro();
		}
	}

	/**
	 *处理固定显示的数据
	 * @param text
	 * @param datas
	 */
	private void doCallBackData(ChangplanBean changplanBean, String text, String plansleep, List<SleepSignDataBean> datas, EnvBean envBean) {
		this.changplanBean = changplanBean;
		if(changplanBean!= null && "1".equals(changplanBean.getIsshow())){
			bottomView.setVisibility(View.VISIBLE);
		}else{
			bottomView.setVisibility(View.GONE);
		}

		ArrayList<GetAvgTimeParamItem> list1 = new ArrayList<GetAvgTimeParamItem>();
		tvExplain.setText(text);
		for (int i = 0; i < datas.size(); i++) {
			SleepSignDataBean data = datas.get(i);
			SoftDayData softData = new SoftDayData();
			SleepStatusBean bean = new SleepStatusBean();
			bean.setDatestring(data.getDate());
			try {
				softData.setDate(simp.format(new SimpleDateFormat("yyyyMMdd").parse(data.getDate())));
//				int sleepLenght = 0;
//				try {
//					sleepLenght = Integer.parseInt(data.getSleeplong());
//				} catch (Exception e) {
//				}
				if (!TextUtils.isEmpty(data.getSleep()) && !"0".equals(data.getSleep()) 
						&& !TextUtils.isEmpty(data.getWakeup()) && !"0".equals(data.getSleep())) {
					bean.setXiaolv(data.getXiaolv());
					int xl = (int) (new BigDecimal(Double.parseDouble(data
							.getXiaolv()))
							.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
					bean.setSleeplong(data.getSleeplong());
					int total = Integer.parseInt(data.getSleeplong());
					double xl1 = Double.parseDouble(data.getXiaolv());
					bean.setDeepsleep(String.valueOf((int) (xl1 * total)));
					// 效率未达标
					if (xl >= 80)
						xlDab += 1;
					// 睡眠时长达标天数
					if (total < 6.5 * 60 || total > 8.5 * 60)
						lenghtWDb += 1;

					// 总效率
					totalXl += xl;
					// 总睡眠时间长 分钟
					totalTime += total;

					long sleepLong = Long.parseLong(data.getSleep()) * 1000;
					long getupLong = Long.parseLong(data.getWakeup()) * 1000;
					softData.setSleepTime(new SimpleDateFormat("HH:mm").format(sleepLong));
					softData.setGetUpTime(new SimpleDateFormat("HH:mm")
							.format(getupLong));
					softData.setSleepTimeLong(String.valueOf(sleepLong));
					softData.setGetUpTimeLong(String.valueOf(getupLong));
					// 平均时间点
					GetAvgTimeParamItem item = new GetAvgTimeParamItem();
					int inSleepTime = TimeFormatUtil.timeToMiss(softData
							.getSleepTime());
					int outSleepTime = TimeFormatUtil.timeToMiss(softData
							.getGetUpTime());
					item.iInSleepTime = inSleepTime;
					item.iOutSleepTime = outSleepTime;
					list1.add(item);
					// 醒来时间达标天数
					long targetu = TimeFormatUtil.timeToMiss(changplanBean.getUp_wakeup());
					long tempup = outSleepTime - targetu;
					if (tempup >= -15 * 60 && tempup <= 15 * 60)
						compalteGetupCount += 1;
//					if (tempup < -15 * 60 || tempup > 15 * 60){
//						compalteGetupCount += 1;
//					}
					// 入睡时间达标天数
					long targetS = TimeFormatUtil.timeToMiss(changplanBean.getUp_sleep());// 目标入睡时间
					long temp = inSleepTime - targetS;
					if (temp >= 0 && temp <= 30 * 60){
						compalteSleepCount += 1;
					}
					
//					if (temp < 0 || temp > 30 * 60){
//						compalteSleepCount += 1;
//					}
				
					hasDataCount += 1;
				}
			} catch (Exception e) {
			}

			mList.add(bean);
			softList.add(softData);
		}

		AvgTimeResult avgResult = new GetSleepAvgTimeValueClass().getAVG(list1);
		if (avgResult != null && hasDataCount > 0) {
			totalTime = totalTime / hasDataCount;
			String avgLenght = ((totalTime / 60) < 10 ? ("0" + (totalTime / 60))
					: (totalTime / 60))
					+ "小时"
					+ ((totalTime % 60) < 0 ? ("0" + (totalTime % 60))
							: (totalTime % 60)) + "分";
			tvLengthExplain.setText(new StringBuffer().append("平均睡眠时长：")
					.append(avgLenght).append("，").append(lenghtWDb)
					.append("天处于亚健康；").toString());
			
			tvXianglv.setText(new StringBuffer().append("平均睡眠效率：")
					.append((totalXl / hasDataCount)).append("%，")
					.append(xlDab).append("天达标(≥85%)。"));

			int avgInSleepTime = avgResult.iAvgInSleepTime;
			int avgOutSleepTime = avgResult.iAvgOutSleepTime;
			String avgInSleepTimeFormat = ((avgInSleepTime / 3600) < 10 ? "0"
					+ (avgInSleepTime / 3600) : (avgInSleepTime / 3600))
					+ ":"
					+ (((avgInSleepTime / 60) % 60) < 10 ? "0"
							+ ((avgInSleepTime / 60) % 60)
							: ((avgInSleepTime / 60) % 60));
			String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"
					+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
					+ ":"
					+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"
							+ ((avgOutSleepTime / 60) % 60)
							: ((avgOutSleepTime / 60) % 60));
			tvSleepExplain.setText(new StringBuffer().append("平均入睡时间：")
					.append(avgInSleepTimeFormat).append("，")
					.append(compalteSleepCount).append("天达标（最早上床时间后的30分钟内）。").toString());
			tvGetUpExplain.setText(new StringBuffer().append("平均醒来时间：")
					.append(avgOutSleepTimeFormat).append("，")
					.append(compalteGetupCount).append("天达标（固定起床时间前后15分钟内）。").toString());
		} else {
			tvSleepExplain.setText("平均入睡时间：_ _：_ _，0天未达标。");
			tvGetUpExplain.setText("平均醒来时间：_ _:_ _，0天未达标。");
			tvLengthExplain.setText("平均睡眠时长：_ _:_ _，0天处于亚健康，平均睡眠效率：_%，0天未达标。");
		}
		reportTab.setData(mList);
		sleepTab.setData(softList, changplanBean.getUp_sleep(), 0);
		getupTab.setData(softList, changplanBean.getUp_wakeup(), 1);
		
		
		/*环境数据*/
		rlHuanjing.removeAllViews();
		for (int i = 0; i < envBean.getEnv_list().size(); i++) {
			View view = getLayoutInflater().inflate(R.layout.item_huangjing_layout, null);
			TextView name = (TextView) view.findViewById(R.id.name);
			ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress);
			TextView num = (TextView) view.findViewById(R.id.num);
			name.setText(envBean.getEnv_list().get(i).getTitle());
			try{
				progress.setProgress(Integer.parseInt(envBean.getEnv_list().get(i).getNums()) * 100);
			}catch(Exception e){
				progress.setProgress(0);
			}
			num.setText(envBean.getEnv_list().get(i).getNums());
			rlHuanjing.addView(view);
		}
		
		for (int i = 0; i < envBean.getMessage().size(); i++) {
			View view = getLayoutInflater().inflate(R.layout.item_huangjian_msg, null);
			TextView msg = (TextView) view.findViewById(R.id.msg);
			msg.setText(envBean.getMessage().get(i));
			rlHjMessage.addView(view);
		}
		
	}
	
	/**
	 * 处理动态呢显示数据
	 */
	private ImageView iv1, iv2, iv3;
	private TextView tv1,tv2,tv3,tv4,tv5;
	private LinearLayout rl1,rl2;
	private void doCallData(SmokeBean smokeBean, WinBean winBean, CoffoBean coffoBean, WeightBean weightBean, SportBean sportBean){
		if("1".equals(smokeBean.getIsshow())){
			rl_smokingp.setVisibility(View.VISIBLE);
			smokingHint.setText(smokeBean.getMessage());
			iv1 = (ImageView) findViewById(R.id.iv1);
			iv2 = (ImageView) findViewById(R.id.iv2);
			iv3 = (ImageView) findViewById(R.id.iv3);
			tv1 = (TextView) findViewById(R.id.t1);
			tv2 = (TextView) findViewById(R.id.t2);
			tv3 = (TextView) findViewById(R.id.t3);
			tv4 = (TextView) findViewById(R.id.t4);
			tv5 = (TextView) findViewById(R.id.t5);
			rl1 = (LinearLayout) findViewById(R.id.iv4);
			rl2 = (LinearLayout) findViewById(R.id.iv5);
			
			try{
				double smoke_nums = Double.parseDouble(smokeBean.getSmoke_nums());
				int smoke_days = Integer.parseInt(smokeBean.getSmoke_days());
				double smoke_shang_nums = Double.parseDouble(smokeBean.getSmoke_shang_nums());
				int smoke_shang_days = Integer.parseInt(smokeBean.getSmoke_shang_days());
				
				if(smoke_nums <= 0){
					iv1.setImageResource(R.drawable.smokings2_pic);
					tv1.setTextColor(getResources().getColor(R.color.theme_color));
				}else if(0 < smoke_nums && smoke_nums < 20){
					iv2.setImageResource(R.drawable.smokings2_pic);
					tv2.setTextColor(getResources().getColor(R.color.theme_color));
				}else if(20 == smoke_nums){
					iv3.setImageResource(R.drawable.smokings2_pic);
					tv3.setTextColor(getResources().getColor(R.color.theme_color));
				}else if(20 < smoke_nums && smoke_nums <= 40){
					int count = rl1.getChildCount();
					for (int i = 0; i < count; i++) {
						ImageView iv = (ImageView) rl1.getChildAt(i);
						iv.setImageResource(R.drawable.smokings2_pic);
					}
					tv4.setTextColor(getResources().getColor(R.color.theme_color));
				}else{
					int count = rl2.getChildCount();
					for (int i = 0; i < count; i++) {
						ImageView iv = (ImageView) rl2.getChildAt(i);
						iv.setImageResource(R.drawable.smokings2_pic);
					}
					tv5.setTextColor(getResources().getColor(R.color.theme_color));
				}
				
				double bil = (smoke_nums/ smoke_days) / (smoke_shang_nums/smoke_shang_days);
				if(smoke_shang_days >0 && smoke_shang_days > 0){
					if(bil > 1){
						int xl = (int) (new BigDecimal(Double.parseDouble((bil - 1)+""))
								.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
						
						smokingLater.setText("较上周期增加了" +  xl +"%");	
					}else if(bil == 1){
						smokingLater.setText("较上周期不变");
					}else{
						int xl = (int) (new BigDecimal(Double.parseDouble((1 - bil)+""))
						.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
						smokingLater.setText("较上周期减少了"+xl +"%");
					}
				}else{
					smokingLater.setText("较上周期不变");
				}
				
			}catch(Exception e){
				smokingLater.setText("较上周期不变");
			}
	
			int num = 0;
			try{
				num = Integer.parseInt(smokeBean.getShuiqian_cishu());
			}catch(Exception e){}
			rlSmokingSleepQ.removeAllViews();
			for (int i = 0; i < 7; i++) {
				View child = getLayoutInflater().inflate(R.layout.item_smokingq_layout, null);
				ImageView imView = (ImageView) child.findViewById(R.id.image);
				if(i < num)
					imView.setImageResource(R.drawable.smokingq2_pic);
				else
					imView.setImageResource(R.drawable.smokingq1_pic);
					
				rlSmokingSleepQ.addView(child);
			}
		}else
			rl_smokingp.setVisibility(View.GONE);
		
		if("1".equals(winBean.getIsshow())){
			rl_winep.setVisibility(View.VISIBLE);
			wineHint.setText(winBean.getMessage());
			int num = 0;
			try{
				num = Integer.parseInt(winBean.getWin_nums());
			}catch(Exception e){}
			rlWine.removeAllViews();
			for (int i = 0; i < 7; i++) {
				View child = getLayoutInflater().inflate(R.layout.item_wine_layout, null);
				ImageView imView = (ImageView) child.findViewById(R.id.image);
				if(i < num)
					imView.setImageResource(R.drawable.wine2_pic);
				else
					imView.setImageResource(R.drawable.wine1_pic);
					
				rlWine.addView(child);
			}
		}else
			rl_winep.setVisibility(View.GONE);
		
		if("1".equals(coffoBean.getIsshow())){
			rl_coffeep.setVisibility(View.VISIBLE);
			coffeeHint.setText(coffoBean.getMessage());
			int num = 0;
			try{
				num = Integer.parseInt(coffoBean.getCoffo_nums());
			}catch(Exception e){}
			rlCoffee.removeAllViews();
			for (int i = 0; i < 7; i++) {
				View child = getLayoutInflater().inflate(R.layout.item_coffee_layout, null);
				ImageView imView = (ImageView) child.findViewById(R.id.image);
				if(i < num)
					imView.setImageResource(R.drawable.coffee2_pic);
				else
					imView.setImageResource(R.drawable.coffee1_pic);
					
				rlCoffee.addView(child);
			}
		}else
			rl_coffeep.setVisibility(View.GONE);
		
		if("1".equals(weightBean.getIsshow())){
			rl_weightp.setVisibility(View.VISIBLE);
			weightHint.setText(weightBean.getMessage());
			
			try{
				double weightd = Double.parseDouble(weightBean.getWeight1());
				if(weightd > 0){
					weightPic.setImageResource(R.drawable.weight1_pic);
					weight.setText("-" + (weightd) + "KG");
					tv8.setText("本周期减重");
				}else if(weightd < 0){
					weightPic.setImageResource(R.drawable.weight2_pic);
					weight.setText("+" + (0 - weightd) + "KG");
					tv8.setText("本周期增重");
				}else{
					weight.setText("0KG");
					tv8.setText("本周期减重");
				}
			}catch(Exception e){
			}
			curWeight.setText("当前体重" + weightBean.getWeight2() + "kg");
			if(!TextUtils.isEmpty(weightBean.getWeigth3()))
				maxWeight.setText("体重波动" + weightBean.getWeigth3() + "kg" );
			else
				maxWeight.setText("体重波动0kg" );
		}else
			rl_weightp.setVisibility(View.GONE);
		
		if("1".equals(sportBean.getIsshow())){
			rl_sportp.setVisibility(View.VISIBLE);
			sportHint.setText(sportBean.getMessage());
			rlSport.removeAllViews();
			for (int i = 0; i < sportBean.getSport_lists().size(); i++) {
				View view = getLayoutInflater().inflate(R.layout.item_sport_layout, null);
				TextView name = (TextView) view.findViewById(R.id.name);
				name.setText(sportBean.getSport_lists().get(i).getTitle());
				LinearLayout content = (LinearLayout) view.findViewById(R.id.rl_content);
				int num = 0;
				try{
					num = Integer.parseInt(sportBean.getSport_lists().get(i).getNums());
				}catch(Exception e){}
				content.removeAllViews();
				for (int j = 0; j < 7; j++) {
					View child = getLayoutInflater().inflate(R.layout.item_sport_child_layout, null);
					ImageView imView = (ImageView) child.findViewById(R.id.image);
					if(j < num)
						imView.setImageResource(R.drawable.sport2_pic);
					else
						imView.setImageResource(R.drawable.sport1_pic);
						
					content.addView(child);
				}
				rlSport.addView(view);
			}
		}else
			rl_sportp.setVisibility(View.GONE);
		
		
	}
	
//
//	public void continuePlan(){
//		new XiangchengMallProcClass(this).continuePlan(PreManager.instance().getUserId(this), dataType, new InterfaceContinuePlanCallBack() {
//
//			@Override
//			public void onSuccess(String icode, HomeDataBean dataBena) {
//
//				Intent intent = new Intent();
//				intent.setAction(Constant.WEEK_FEED_BACK_SUC);
//				sendBroadcast(intent);
//
//				AppManager.getAppManager().finishActivity();
//			}
//
//			@Override
//			public void onError(String icode, String strErrMsg) {
//				Util.show(SleepDataReportActivity.this, strErrMsg);
//			}
//		});
//	}

	

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 2016 && arg1 == RESULT_OK) {
			if (scView.getVisibility() != View.VISIBLE)
				scView.setVisibility(View.VISIBLE);
			if (dialog != null)
				dialog.dismiss();
		}
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_2:
			startActivity(new Intent(this, ContinuePlansActivity.class)
					.putExtra("change", changplanBean));
//					.putExtra("flag", 2));
			break;
//		case R.id.btn_3:
//			startActivity(new Intent(this, ImprSlpPlanActivity.class).putExtra("flag", 2));
//			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if (keyCode == KeyEvent.KEYCODE_BACK) {
		//
		// return true;
		// }
		return super.onKeyDown(keyCode, event);
	}

}
