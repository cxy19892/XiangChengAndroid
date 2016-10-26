package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.CalendarAdapter;
import com.yzm.sleep.bean.DateBean;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataParams;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomGridView;

/**
 * 月数据分析
 * 
 * @author tianxun
 * @pramas type 0入睡时长 ;1入睡时间;2醒来时间
 */
@SuppressLint("SimpleDateFormat")
public class MonthDataAnalysisActivity extends BaseActivity implements OnTouchListener{
	private GestureDetector gestureDetector;
	private TextView tvYearMonth;
	private CustomGridView calenderGridview0,calenderGridview1,calenderGridview2;
	private CalendarAdapter mAdapter0,mAdapter1,mAdapter2;
	private ViewFlipper viewFlipper;
	private String type, dataType;
	// 动画
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private HashMap<String,List<DateBean>> cache;
	private ProgressUtils pro;
	private SimpleDateFormat ymSdf, ymdSdf;//年
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monthdata_analysis);
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		ymSdf = new SimpleDateFormat("yyyyMM");
		ymdSdf = new SimpleDateFormat("yyyy-MM-dd");
		title.setText("更多");
		initView();
	}

	private void initView() {
		cache=new HashMap<String, List<DateBean>>();
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this,R.anim.slide_right_out);
		gestureDetector=new GestureDetector(this,new MyOnGestureListener());
		
		findViewById(R.id.but_before).setOnClickListener(this);
		tvYearMonth = (TextView) findViewById(R.id.tv_year_month);
		findViewById(R.id.but_later).setOnClickListener(this);
		viewFlipper=(ViewFlipper) findViewById(R.id.viewfilpper);
		calenderGridview0=(CustomGridView) findViewById(R.id.calender_gridview0);
		calenderGridview1=(CustomGridView) findViewById(R.id.calender_gridview1);
		calenderGridview2=(CustomGridView) findViewById(R.id.calender_gridview2);
		TextView aimeType1=(TextView) findViewById(R.id.aim_type1);
		TextView aimeType2=(TextView) findViewById(R.id.aim_type2);
		if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(this)))
			dataType = "1";
		else
			dataType = "2";
		
		if("0".equals(type=getIntent().getStringExtra("type"))){
			aimeType1.setText("健康");
			aimeType2.setText("亚健康");
		}else{
			aimeType1.setText("达标");
			aimeType2.setText("未达标");
		}
		
		calenderGridview0.setOnTouchListener(this);
		calenderGridview1.setOnTouchListener(this);
		calenderGridview2.setOnTouchListener(this);
		mAdapter0=new CalendarAdapter(this,getScreenWidth(),false);
		mAdapter1=new CalendarAdapter(this,getScreenWidth(),false);
		mAdapter2=new CalendarAdapter(this,getScreenWidth(),false);
		
		calenderGridview0.setAdapter(mAdapter0);
		calenderGridview1.setAdapter(mAdapter1);
		calenderGridview2.setAdapter(mAdapter2);
		setGridItemClickListener();
		viewFlipper.showNext();
		
		try {
			Calendar ca=Calendar.getInstance();
			ca.add(Calendar.DAY_OF_MONTH, -1);
			String dateYearMonth=new SimpleDateFormat("yyyy年MM月").format(ca.getTime());
			tvYearMonth.setText(dateYearMonth);
			setAdapterDatas(mAdapter1,new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime()));
		} catch (Exception e) {}
	}
	
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
	
	/**
	 * 设置数据
	 * @param adapter
	 * @param dayDate
	 */
	private void setAdapterDatas(final CalendarAdapter adapter,String dayDate){
		showPro();
		final List<DateBean> datedayList=new ArrayList<DateBean>();
		String monthFirstDay=CalenderUtil.getCurrentMonthFirstDay(dayDate);
		final List<DateBean> lastList=CalenderUtil.getDateList(monthFirstDay,CalenderUtil.getWeekDay(CalenderUtil.getCurrentMonthFirstDay(dayDate)),true);
		final List<DateBean> currentList =CalenderUtil.getCurrentMonthDayList(dayDate);
		datedayList.addAll(lastList);
		datedayList.addAll(currentList);
		int temp=42-datedayList.size();
		String monthLastDay=CalenderUtil.getCurrentMonthLastDay(dayDate);
		final List<DateBean> nextList =CalenderUtil.getDateList(monthLastDay,temp,false);
		datedayList.addAll(nextList);
		adapter.setDate(datedayList);
		String paramsDate = dayDate;
		int dats =0;
		try {
			String currentDay = ymSdf.format(new Date());
			String day = ymSdf.format(ymdSdf.parse(dayDate));
			if(currentDay.equals(day)){
				paramsDate = dayDate;
				dats = lastList.size() + isExist(currentList, dayDate) + 1;
			}else{
				paramsDate = nextList.get(nextList.size()-1).getDate();
				Calendar ca=Calendar.getInstance();
				ca.add(Calendar.DAY_OF_MONTH, -1);
				long day1 = ymdSdf.parse(paramsDate).getTime();
				long current =ymdSdf.parse(ymdSdf.format(ca.getTime())).getTime();
				if(day1 > current){
					paramsDate = ymdSdf.format(ca.getTime());
					dats = lastList.size() + currentList.size() + isExist(nextList, paramsDate) + 1;
				}else{
					dats = 42;
				}
			}
		} catch (Exception e) {
		}
		
		GetSignInDataParams mParams= new GetSignInDataParams();
		mParams.my_int_id=PreManager.instance().getUserId(this);
		mParams.type = dataType;
		mParams.date = paramsDate.replace("-", "");
		mParams.days = String.valueOf(dats);
		new XiangchengMallProcClass(this).getSignInData(mParams, new GetSignInDataCallBack() {
			
			@Override
			public void onSuccess(int icode, List<SleepStatusBean> list) {
				cancelPro();
				if (list!=null && list.size()>0) 
					doAnalysisCallBackData(adapter, list, datedayList);
				else
					cache.put(tvYearMonth.getText().toString(), datedayList);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				cancelPro();
				cache.put(tvYearMonth.getText().toString(), datedayList);
			}
		});

	}
	
	private void doAnalysisCallBackData(final CalendarAdapter adapter, List<SleepStatusBean> list, final List<DateBean> termpList){
		
		for (SleepStatusBean dateBean : list) {
			if("1".equals(type)){//入睡
				try {
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMdd");
					SimpleDateFormat sdf3=new SimpleDateFormat("HH:mm");
					int index=isExist(termpList, sdf.format(sdf1.parse(dateBean.getDatestring()).getTime()));
					
					//统计入睡达标的天数
					long targets=sdf3.parse(PreManager.instance().getSleepTime_Setting(this)).getTime();//目标入睡时间
					int targetsM=(int) (targets/(60 * 1000));
					long sleepTimes=sdf3.parse(sdf3.format(Long.parseLong(dateBean.getSleep())*1000)).getTime();//实际入睡时间
					int sleepTimeM=(int) (sleepTimes/(60 * 1000));
					int temp=sleepTimeM-targetsM;
					if(index>-1){
						if(temp >= 0 && temp <= 30)
							termpList.get(index).setState("1");
						else
							termpList.get(index).setState("0");
					}
					
				} catch (Exception e) {
				}
			}else if("2".equals(type)){//起床
				try {
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMdd");
					SimpleDateFormat sdf3=new SimpleDateFormat("HH:mm");
					int index=isExist(termpList,sdf.format(sdf1.parse(dateBean.getDatestring()).getTime()));
					
					long targets=sdf3.parse(PreManager.instance().getGetupTime_Setting(this)).getTime();//目标醒来时间
					int targetsM=(int) (targets/(60 * 1000));
					long sleepTimes=sdf3.parse(sdf3.format(Long.parseLong(dateBean.getWakeup())*1000)).getTime();//实际醒来时间
					int sleepTimeM=(int) (sleepTimes/(60 * 1000));
					int temp=sleepTimeM-targetsM;
					if(index>-1){
						if(temp >= -15 && temp <= 15)
							termpList.get(index).setState("1");
						else
							termpList.get(index).setState("0");
					}
				} catch (Exception e) {
				}
			}else{}
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				adapter.setDate(termpList);
				cache.put(tvYearMonth.getText().toString(), termpList);
			}
		}, 300);
	}

	private int isExist(List<DateBean> list,String dayDate){
		int size = list.size();
		for(int i=0;i< size;i++){
			if(list.get(i).getDate().equals(dayDate)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 设置gridView的点击事件
	 */
	private void setGridItemClickListener(){
		calenderGridview0.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				pinionSelect(mAdapter0.getDate().get(position).getDate());
			}
		});
		
		calenderGridview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				pinionSelect(mAdapter1.getDate().get(position).getDate());
			}
		});
		
		calenderGridview2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				pinionSelect(mAdapter2.getDate().get(position).getDate());
			}
		});
	}
	
	/**
	 * 根据选择的日期判断选择上一月 还是下一月
	 * @param dayDate 选择的日期
	 */
	private void pinionSelect(String dayDate){
		try {
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy年MM月");
			String currentYM=tvYearMonth.getText().toString();
			long currentYMLong=sdf2.parse(currentYM).getTime();
			long clickYMLong=sdf2.parse(sdf2.format(sdf1.parse(dayDate).getTime())).getTime();
			if(clickYMLong < currentYMLong)
				selectLastMonth();
			else if(clickYMLong > currentYMLong)
				selectNextMonth();
		} catch (Exception e) {
		}
	}
	
	/**
	 * 选择上一月
	 */
	private void selectLastMonth(){
		viewFlipper.setInAnimation(slideRightIn);
		viewFlipper.setOutAnimation(slideRightOut);
		viewFlipper.showPrevious();
		int postion=viewFlipper.getDisplayedChild();
		String yearMonth=tvYearMonth.getText().toString();
		try {
			SimpleDateFormat sdfYM = new SimpleDateFormat("yyyy年MM月");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date(sdfYM.parse(yearMonth).getTime()));
			cal.add(Calendar.MONTH,-1);
			tvYearMonth.setText(sdfYM.format(cal.getTime()));
			List<DateBean> list=cache.get(tvYearMonth.getText().toString());
			if(postion==0){
				if(list!=null && list.size()>0)
					mAdapter0.setDate(list);
				else
					setAdapterDatas(mAdapter0,sdf.format(cal.getTime()));
				
				mAdapter1.setDate(null);
				mAdapter2.setDate(null);
			}else if(postion==1){
				if(list!=null && list.size()>0)
					mAdapter1.setDate(list);
				else
					setAdapterDatas(mAdapter1,sdf.format(cal.getTime()));
				mAdapter0.setDate(null);
				mAdapter2.setDate(null);
			}else{
				if(list!=null && list.size()>0)
					mAdapter2.setDate(list);
				else
					setAdapterDatas(mAdapter2,sdf.format(cal.getTime()));
				mAdapter1.setDate(null);
				mAdapter0.setDate(null);
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * 选择下一月
	 */
	private void selectNextMonth(){
		viewFlipper.setInAnimation(slideLeftIn);
		viewFlipper.setOutAnimation(slideLeftOut);
		viewFlipper.showNext();
		int postion=viewFlipper.getDisplayedChild();
		String yearMonth=tvYearMonth.getText().toString();
		try {
			SimpleDateFormat sdfYM = new SimpleDateFormat("yyyy年MM月");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date(sdfYM.parse(yearMonth).getTime()));
			cal.add(Calendar.MONTH,1);
			tvYearMonth.setText(sdfYM.format(cal.getTime()));
			List<DateBean> list=cache.get(tvYearMonth.getText().toString());
			if(postion==0){
				if(list!=null && list.size()>0)
					mAdapter0.setDate(list);
				else
					setAdapterDatas(mAdapter0,sdf.format(cal.getTime()));
				mAdapter1.setDate(null);
				mAdapter2.setDate(null);
			}else if(postion==1){
				if(list!=null && list.size()>0)
					mAdapter1.setDate(list);
				else
					setAdapterDatas(mAdapter1,sdf.format(cal.getTime()));
				mAdapter0.setDate(null);
				mAdapter2.setDate(null);
			}else{
				if(list!=null && list.size()>0)
					mAdapter2.setDate(list);
				else
					setAdapterDatas(mAdapter2,sdf.format(cal.getTime()));
				mAdapter0.setDate(null);
				mAdapter1.setDate(null);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.but_before:
			selectLastMonth();
			break;
		case R.id.but_later:
			selectNextMonth();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	
	class MyOnGestureListener implements OnGestureListener{

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e2.getX() - e1.getX() > 120) {			 
				selectLastMonth();
				return true;
			} else if (e2.getX() - e1.getX() < -120) {		 
				selectNextMonth();
				return true;
			}
			return true;
		}
		
	}

}
