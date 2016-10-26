package com.yzm.sleep.fragment;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzm.sleep.NewRoundProgressBar;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.AvgTimeResult;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.GetAvgTimeParamItem;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.GetTimeParamItem;
import com.yzm.sleep.utils.GetTimeParamItem.GetTimeResult;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataParams;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.ColorFullRoundProgressBar;
import com.yzm.sleep.widget.LazyFragment;
import com.yzm.sleep.widget.SleepTimePointTable;

/**
 * 醒来时间点趋势
 * @author
 */
@SuppressLint("SimpleDateFormat") 
public class FragmentPageGetupTimeTrend extends LazyFragment {

	private View scView, noNetView;
	private AvgTimeResult avgResult; //计算平均
	private GetTimeResult getTimeResult;// 最早 最迟  返回数据
	private Activity activity;
	private String dataType = "1"; //数据类型  1.软件 ; 2.硬件
	private SleepTimePointTable sleepTimePointTable;
	private TextView tvTitle,tvTime,tvComplete,tvGoalBefore,tvGoalLater,tvGoalBetween,tipsDesc,tvPro;
	private ColorFullRoundProgressBar progerssbar;
	private String getupDate/*起床时间*/;
	private String dayDate;//日期 格式yyyy-MM-dd
	private boolean loading = true; //是否需要重新加载
	private ProgressUtils pro;
	private TextView tvReach, tvNotReach, noNetText;
	/**
	 * 显示进度
	 */
	private void showPro(){
		if(pro==null){
			pro = new ProgressUtils(activity);
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
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		dayDate = bundle.getString("dayDate");

		getupDate = PreManager.instance().getGetupTime_Setting(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_time_trend, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tvReach= (TextView) view.findViewById(R.id.tv_rech);
		tvNotReach = (TextView) view.findViewById(R.id.tv_not_rech);
		scView = view.findViewById(R.id.sc_view);
		sleepTimePointTable=(SleepTimePointTable) view.findViewById(R.id.soft_week_data_view2);
		tvTitle=(TextView) view.findViewById(R.id.tv_title);
		tvTime=(TextView) view.findViewById(R.id.tv_time);
		progerssbar=(ColorFullRoundProgressBar) view.findViewById(R.id.progerssbar);
		tvComplete=(TextView) view.findViewById(R.id.tv_complete);
		tvComplete.setText("0%");
		tvGoalBefore=(TextView) view.findViewById(R.id.tv_goal_before);
		tvGoalLater=(TextView) view.findViewById(R.id.tv_goal_later);
		tvGoalBetween=(TextView) view.findViewById(R.id.tv_goal_between);
		tipsDesc=(TextView) view.findViewById(R.id.tips_desc);
		tvPro=(TextView) view.findViewById(R.id.tv_pro);
		tvTitle.setText("平均醒来时间:");
		tvReach.setText("醒来时间达标");
		tvNotReach.setText("醒来时间未达标");
		noNetView =view.findViewById(R.id.nonet);
		noNetText = (TextView) noNetView.findViewById(R.id.text);

		if(!Util.checkNetWork(activity)){
			noNetView.setVisibility(View.VISIBLE);
			noNetText.setText("请检查网络设置");
		}else{
			view.findViewById(R.id.nonet).setVisibility(View.GONE);
		}
		lazyLoad();
	}
	
	/**
	 * 获取数据
	 * @param
	 */
	private void getDatas(){
		LogUtil.d("chen", "GetupTime getDatas");
		if(!Util.checkNetWork(activity)){
			Util.show(activity, "检查网络设置");
			noNetView.setVisibility(View.VISIBLE);
			noNetText.setText("请检查网络设置");
			return;
		}
		showPro();
		GetSignInDataParams mParams = new GetSignInDataParams();
		mParams.my_int_id = PreManager.instance().getUserId(activity);
		mParams.date = dayDate.replace("-", "");
		mParams.days = "7";
		mParams.type = dataType;
		new XiangchengMallProcClass(activity).getSignInData(mParams, new GetSignInDataCallBack() {

			@Override
			public void onSuccess(int icode, List<SleepStatusBean> list) {
				loading = false;

				doSignInDatas(list);
			}

			@Override
			public void onError(int icode, String strErrMsg) {
				LogUtil.d("chen", "GetupTime getDatas onError");
				cancelPro();
				loading = false;
				Util.show(activity, strErrMsg);
			}
		});
	}

	/**
	 * 判断 元素 是否在集合里
	 * @param date
	 * @param list
	 * @return 返回 在集合的位置 不在返回-1
	 */
	private int findIndex(String date, List<SleepStatusBean> list){
		for (int i = 0; i < list.size(); i++) {
			if(date.equals(list.get(i).getDatestring())){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 逻辑处理数据
	 * @param
	 */
	private void doSignInDatas(List<SleepStatusBean> mlist){
		if(scView.getVisibility() != View.VISIBLE)
			scView.setVisibility(View.VISIBLE);

		List<SoftDayData> softList = new ArrayList<SoftDayData>();
		int hasDataCount = 0, compalteGetupCount = 0;
		ArrayList<GetAvgTimeParamItem> list1 = new ArrayList<GetAvgTimeParamItem>();
		ArrayList<GetTimeParamItem> tTimelist = new ArrayList<GetTimeParamItem>();
		String[] dates = CalenderUtil.getSevenDay(dayDate, 7);
		for(int i=0; i < dates.length; i++){
			String str= dates[i];
			SoftDayData soft = new SoftDayData();
			
			soft.setDate(str);
			int index = findIndex(str.replace("-", ""), mlist);
			if(index >= 0){
				try {
					SleepStatusBean data = mlist.get(index);
					if(Long.parseLong(data.getWakeup()) > 0 && Long.parseLong(data.getSleep()) > 0 && Long.parseLong(data.getSleeplong()) > 0){
						soft.setSleepTime(new SimpleDateFormat("HH:mm").format(Long.parseLong(data.getSleep()) * 1000));
						soft.setGetUpTime(new SimpleDateFormat("HH:mm").format(Long.parseLong(data.getWakeup()) * 1000));
						soft.setSleepTimeLong(String.valueOf(Long.parseLong(data.getSleep()) * 1000));
						soft.setGetUpTimeLong(String.valueOf(Long.parseLong(data.getWakeup()) * 1000));
					
						//据算平均
						GetAvgTimeParamItem item = new GetAvgTimeParamItem();
						int inSleepTime = TimeFormatUtil.timeToMiss(soft.getSleepTime());
						int outSleepTime = TimeFormatUtil.timeToMiss(soft.getGetUpTime());
						item.iInSleepTime = inSleepTime;
						item.iOutSleepTime = outSleepTime;
						list1.add(item);
						
						//统计起床达标的天数
						long targetu=TimeFormatUtil.timeToMiss(getupDate);
						long tempup=outSleepTime - targetu;
						if(tempup >= -15*60 && tempup <= 15*60)
							compalteGetupCount+=1;
						
						// 提取数据 便于计算最早最迟
						GetTimeParamItem item1 = new GetTimeParamItem();
						item1.iBelongDate = new SimpleDateFormat("yyyy-MM-dd").parse(soft.getDate()).getTime();
						String s = new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(soft.getSleepTimeLong()));
						item1.iInSleepDate = new SimpleDateFormat("yyyy-MM-dd").parse(s).getTime();

						item1.iInSleepTime = new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(Long.parseLong(soft.getSleepTimeLong()))).getTime();
						String s1 = new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(soft.getGetUpTimeLong()));

						item1.iOutSleepDate = new SimpleDateFormat("yyyy-MM-dd").parse(s1).getTime();
						item1.iOutSleepTime = new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(Long.parseLong(soft.getGetUpTimeLong()))).getTime();
						tTimelist.add(item1);
						hasDataCount++;
					}
				} catch (Exception e) {
				}
			}
			softList.add(soft);
		}
		sleepTimePointTable.setData(softList,getupDate, 1);
		sleepTimePointTable.postInvalidate();
		avgResult = new GetSleepAvgTimeValueClass().getAVG(list1);
		getTimeResult = GetTimeParamItem.ProcTimeData(tTimelist);// 最早 最迟  返回数据
		if(avgResult != null){
			int avgOutSleepTime = avgResult.iAvgOutSleepTime;
			String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
					+ ":"+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"+ ((avgOutSleepTime / 60) % 60): ((avgOutSleepTime / 60) % 60));
			tvTime.setText(avgOutSleepTimeFormat);
		}else{
			tvTime.setText("--:--");
		}
		
		if (hasDataCount > 0 && getTimeResult != null) {
//			int per = (int)((((float)compalteGetupCount)/hasDataCount)*100);
			int per = new BigDecimal(((float)compalteGetupCount)/hasDataCount*100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			if(per >= 60){
				progerssbar.setColor(getResources().getColor(R.color.bg_color), 0xffa7bb70);
				tvPro.setTextColor(0xffa7bb70);
			}else{
				progerssbar.setColor(getResources().getColor(R.color.bg_color), 0xFFFF8037);
				tvPro.setTextColor(0xFFFF8037);
			}
			progerssbar.setProgress(per);
			tvPro.setText(per+"%");
			tvComplete.setText("最近7天目标完成"+per+"% ,请继续加油");
			if(getTimeResult.iOutSleepTime_early != 0)
				tvGoalBefore.setText("最早醒来时间点: "+new SimpleDateFormat("HH:mm").format(getTimeResult.iOutSleepTime_early));
			else if(getTimeResult.iOutSleepTime_last != 0){
				tvGoalBefore.setText("最早醒来时间点: "+new SimpleDateFormat("HH:mm").format(getTimeResult.iOutSleepTime_last));
			}else
				tvGoalBefore.setText("最早醒来时间点: --:--");
			if(getTimeResult.iOutSleepTime_last > 0)
				tvGoalLater.setText("最迟醒来时间点: "+new SimpleDateFormat("HH:mm").format(getTimeResult.iOutSleepTime_last));
			else if(getTimeResult.iOutSleepTime_early != 0){
				tvGoalLater.setText("最迟醒来时间点: "+new SimpleDateFormat("HH:mm").format(getTimeResult.iOutSleepTime_early));
			}else
				tvGoalLater.setText("最迟醒来时间点: --:--");
			tvGoalBetween.setText("有"+compalteGetupCount+"天准时醒来");
			tipsDesc.setText("在固定起床时间的前后15分钟醒来，都算准时醒来哦！");
		}else{
			tipsDesc.setText("在固定起床时间的前后15分钟醒来，都算准时醒来哦！");
    		tvComplete.setText("最近7天目标完成0% ,请继续加油");
			tvPro.setText("0%");
			tvGoalBefore.setText("最早醒来时间点: --:--");
			tvGoalLater.setText("最迟醒来时间点: --:--");
			tvGoalBetween.setText("有0天准时醒来");
		}
		cancelPro();
		LogUtil.d("chen", "GetupTime initDatas");
	}
	
	/**
	 * 设置日期
	 * @param dayDate
	 */
	public void setDayDate(String dayDate){
		this.dayDate = dayDate;
		this.loading = true;
		lazyLoad();
	}
	
	@Override
	protected void lazyLoad() {
		if (!this.loading)
			return;
		if(isVisible) {
			getDatas();
		}
	}

}
