package com.yzm.sleep.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataParams;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.LazyFragment;
import com.yzm.sleep.widget.OrangeSleepLengthTable;
import com.yzm.sleep.widget.SoftSleepLengthTable;

/**
 * 睡眠时长 趋势
 * @author 
 */
@SuppressLint("SimpleDateFormat") 
public class FragmentPageSleepLengthTrend extends LazyFragment {

	private View scView, noNetView;
	private TextView textHint, sleepStatus, efficiency, sleepLength, wakeLength, morningLength,
					sleep, course, morning, deepsleepLength, qiansleepLength, noNetText;
	private SoftSleepLengthTable softView;
	private OrangeSleepLengthTable orangTabView;
	private List<SoftDayData> softDatas;
	private List<SleepStatusBean> list;
	private Activity activity;
	private String dataType = "1"; //数据类型  1.软件 ; 2.硬件
	private ProgressUtils pro;
	private String dayDate;//日期 格式yyyy-MM-dd
	private boolean loading = true, isInit; //是否需要重新加载

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
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sleeplength_trend, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}
	
	private void initView(View view){
		if("1".equals(dataType)){
			view.findViewById(R.id.rl_qiansleep).setVisibility(View.GONE);
			view.findViewById(R.id.rl_deepsleep).setVisibility(View.GONE);
		}else if("2".equals(dataType)){
			view.findViewById(R.id.rl_qiansleep).setVisibility(View.VISIBLE);
			view.findViewById(R.id.rl_deepsleep).setVisibility(View.VISIBLE);
		}
		
		scView = view.findViewById(R.id.sv_view);
		softView = (SoftSleepLengthTable) view.findViewById(R.id.softsleeplengthform);
		orangTabView = (OrangeSleepLengthTable) view.findViewById(R.id.orangsleeplengthform);
		efficiency = (TextView) view.findViewById(R.id.sleep_efficiency);
		sleep = (TextView) view.findViewById(R.id.sleep);
		sleepLength = (TextView) view.findViewById(R.id.sleep_length);
		course = (TextView) view.findViewById(R.id.sleep_course);
		wakeLength = (TextView) view.findViewById(R.id.wake_length);
		morning = (TextView) view.findViewById(R.id.sleep_morning);
		morningLength = (TextView) view.findViewById(R.id.morning_length);
		sleepStatus = (TextView) view.findViewById(R.id.sleep_status);
		deepsleepLength = (TextView) view.findViewById(R.id.deepsleep_lenght);
		qiansleepLength = (TextView) view.findViewById(R.id.qiansleep_lenght);
		textHint = (TextView) view.findViewById(R.id.text_hint);
		noNetView = view.findViewById(R.id.nonet);
		noNetText = (TextView) noNetView.findViewById(R.id.text);

		if(!Util.checkNetWork(activity)){
			view.findViewById(R.id.rl_tab).setVisibility(View.GONE);
			view.findViewById(R.id.rl_res).setVisibility(View.GONE);
			noNetView.setVisibility(View.VISIBLE);
			noNetText.setText("请检查网络设置");
		}else{
			view.findViewById(R.id.rl_tab).setVisibility(View.VISIBLE);
			view.findViewById(R.id.rl_res).setVisibility(View.VISIBLE);
			view.findViewById(R.id.nonet).setVisibility(View.GONE);
			textHint.setVisibility(View.VISIBLE);
			textHint.setText("睡眠效率=睡着时长/躺在床上的时长");
			if("2".equals(dataType))
				orangTabView.setVisibility(View.VISIBLE);
			else
				softView.setVisibility(View.VISIBLE);
		}
		isInit = true;
		lazyLoad();
		setListener();
	}
	
	private void setListener(){
		softView.setOnClickPosition(new SoftSleepLengthTable.SleepLengthSelect() {
			
			@Override
			public void select(int position) {
				modifiShowData(position);
			}
		});
		
		orangTabView.setOnClickPosition(new OrangeSleepLengthTable.SleepLengthSelect() {
			
			@Override
			public void select(int position) {
				modifiShowData(position);
			}
		});
	}
	
	private void modifiShowData(int position) {
		try {
			int xv = (int) (new BigDecimal(Double.parseDouble(list.get(position).getXiaolv())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
			efficiency.setText(xv+"%");
			if(xv >= 85)
				sleepStatus.setText("达标");
			else
				sleepStatus.setText("不达标");
		} catch (Exception e) {
			efficiency.setText("0%");
			sleepStatus.setText("不达标");
		}
		try {
			sleep.setText(list.get(position).getSleep1());
			course.setText(list.get(position).getSleep2());
			morning.setText(list.get(position).getSleep3());
			if(!TextUtils.isEmpty(list.get(position).getSleepspeed()))
				sleepLength.setText(list.get(position).getSleepspeed()+"分钟");
			else
				sleepLength.setText("无数据");
			if(!TextUtils.isEmpty(list.get(position).getWakelong()))
				wakeLength.setText(list.get(position).getWakelong() + "分钟");
			else
				wakeLength.setText("无数据");
			if(!TextUtils.isEmpty(list.get(position).getWakezao()))
				morningLength.setText(list.get(position).getWakezao() + "分钟");
			else
				morningLength.setText("无数据");
			if("2".equals(dataType)){
				SleepStatusBean bean = list.get(position);
				float deepLength = new BigDecimal(Double.parseDouble(bean.getDeepsleep())/60).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
				float totalLength = new BigDecimal(Double.parseDouble(bean.getSleeplong())/60).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
				deepsleepLength.setText(deepLength + "h");
				qiansleepLength.setText((totalLength*10 - deepLength*10)/10 + "h");
			} 
		} catch (Exception e) {
			sleep.setText("无数据");
			course.setText("无数据");
			morning.setText("无数据");
			sleepLength.setText("无数据");
			wakeLength.setText("无数据");
			morningLength.setText("无数据");
			if("2".equals(dataType)){
				deepsleepLength.setText("无数据");
				qiansleepLength.setText("无数据");
			}
		}
	}
	
	/**
	 * 设置日期
	 * @param dayDate
	 */
	public void setDayDate(String dayDate){
		this.dayDate = dayDate;
		this.loading = true;
		if(isInit)
			lazyLoad();
	}
	
	/**
	 * 获取数据
	 * @param
	 */
	private void getDatas(){
		LogUtil.d("chen", "SleeplengthTime getDatas");
		if(!Util.checkNetWork(activity)){
			Util.show(activity, "网路连接错误");
			try{
				noNetView.setVisibility(View.VISIBLE);
				noNetText.setText("请检查网络设置");
			}catch (Exception e){}

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

				doCallbBackData(dayDate, list);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				LogUtil.d("chen", "SleeplengthTime getDatas onError");
				cancelPro();
				Util.show(activity, strErrMsg);
			}
		});
	}
	
	private int findIndex(String date, List<SleepStatusBean> list){
		for (int i = 0; i < list.size(); i++) {
			if(date.equals(list.get(i).getDatestring())){
				return i;
			}
		}
		return -1;
	}
	
	private void doCallbBackData(String date, List<SleepStatusBean> mlist){
		this.loading = false;
		if(scView.getVisibility() != View.VISIBLE)
			scView.setVisibility(View.VISIBLE);
		noNetView.setVisibility(View.GONE);
		list = new ArrayList<SleepStatusBean>();
		softDatas = new ArrayList<SoftDayData>();
		String[] dates = CalenderUtil.getSevenDay(date, 7);
		for(int i=0; i < dates.length; i++){
			String str= dates[i];
			SoftDayData softData = new SoftDayData();
			softData.setDate(str);
			int index = findIndex(str.replace("-", ""), mlist);
			if(index >= 0){
				try {
					float sleepLength = Float.parseFloat(mlist.get(index).getSleeplong());
					int lenght = (int)((sleepLength/60)*10);
					softData.setSleepLength(lenght/10f);
				} catch (Exception e) {
					softData.setSleepLength(0f);
				}
				list.add(mlist.get(index));
			}else{
				SleepStatusBean bean= new SleepStatusBean();
				bean.setDatestring(str.replace("-", ""));
				bean.setXiaolv("0");
				bean.setDeepsleep("0");
				bean.setQiansleep("0");
				bean.setSleeplong("0");
				bean.setSleep1("无数据");
				bean.setSleep2("无数据");
				bean.setSleep3("无数据");
				list.add(bean);
				softData.setSleepLength(0f);
			}
			softDatas.add(softData);
		}


		if("2".equals(dataType))
			orangTabView.setData(list);
		else
			softView.setData(softDatas);

		modifiShowData(list.size() - 1);
		LogUtil.d("chen", "SleeplengthTime initDatas");
		cancelPro();
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
