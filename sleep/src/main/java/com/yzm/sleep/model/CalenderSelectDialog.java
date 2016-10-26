package com.yzm.sleep.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yzm.sleep.R;
import com.yzm.sleep.adapter.CalendarAdapter;
import com.yzm.sleep.bean.DateBean;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.widget.CustomGridView;

/**
 * 日历dialog
 * @author tianxun
 */
@SuppressLint("SimpleDateFormat") 
public class CalenderSelectDialog extends AlertDialog implements View.OnClickListener,OnTouchListener{
	private GestureDetector gestureDetector;
	private Context context;
	private TextView tvYearMonth;
	private int screenWidth;
	private SelectDayDateListener dayDateListener;
	private List<DateBean> datedayList;
	private String slectDayDate;
	private CustomGridView calenderGridview0,calenderGridview1,calenderGridview2;
	private CalendarAdapter mAdapter0,mAdapter1,mAdapter2;
	private ViewFlipper viewFlipper;
	// 动画
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	
	public interface SelectDayDateListener{
		public void selectday(String dayDate);
	}

	public CalenderSelectDialog(Context context,int screenWidth,String dayDate,SelectDayDateListener dayDateListener) {
		this(context, screenWidth, dayDateListener);
		this.slectDayDate=dayDate;
	}
	
	public CalenderSelectDialog(Context context,int screenWidth,SelectDayDateListener dayDateListener) {
		super(context, R.style.pop_dialog);
		this.getWindow().setWindowAnimations(R.style.bottom_animation);
		this.context = context;
		this.screenWidth=screenWidth;
		this.dayDateListener=dayDateListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calender_select_dialog);
		this.getWindow().setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
		setCanceledOnTouchOutside(true);
		initViews();
	}

	private void initViews() {
		slideLeftIn = AnimationUtils.loadAnimation(context, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(context, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(context,R.anim.slide_right_out);
		gestureDetector=new GestureDetector(context,new MyOnGestureListener());
		
		findViewById(R.id.but_before).setOnClickListener(this);;
		findViewById(R.id.but_later).setOnClickListener(this);;
		tvYearMonth=(TextView) findViewById(R.id.tv_year_month);
		viewFlipper=(ViewFlipper) findViewById(R.id.viewfilpper);
		calenderGridview0=(CustomGridView) findViewById(R.id.calender_gridview0);
		calenderGridview1=(CustomGridView) findViewById(R.id.calender_gridview1);
		calenderGridview2=(CustomGridView) findViewById(R.id.calender_gridview2);
		calenderGridview0.setOnTouchListener(this);
		calenderGridview1.setOnTouchListener(this);
		calenderGridview2.setOnTouchListener(this);
		mAdapter0=new CalendarAdapter(context,screenWidth,true);
		mAdapter1=new CalendarAdapter(context,screenWidth,true);
		mAdapter2=new CalendarAdapter(context,screenWidth,true);
		
		calenderGridview0.setAdapter(mAdapter0);
		calenderGridview1.setAdapter(mAdapter1);
		calenderGridview2.setAdapter(mAdapter2);
		setGridItemClickListener();
		viewFlipper.showNext();
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
			Calendar ca=Calendar.getInstance();
			ca.setTime(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(slectDayDate).getTime()));
			String dateYearMonth=sdf.format(ca.getTime());
			tvYearMonth.setText(dateYearMonth);
			setAdapterDatas(mAdapter1,new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime()));
		} catch (Exception e) {
		}
	}
	
	private void setAdapterDatas(CalendarAdapter adapter,String dayDate) {
		datedayList=new ArrayList<DateBean>();
		List<DateBean> list=CalenderUtil.getDateList(CalenderUtil.getCurrentMonthFirstDay(dayDate),CalenderUtil.getWeekDay(CalenderUtil.getCurrentMonthFirstDay(dayDate)),true);
		datedayList.addAll(list);
		datedayList.addAll(CalenderUtil.getCurrentMonthDayList(dayDate));
		int temp=42-datedayList.size();
		datedayList.addAll(CalenderUtil.getDateList(CalenderUtil.getCurrentMonthLastDay(dayDate),temp,false));
		adapter.setDate(datedayList,slectDayDate);
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
	
	private void pinionSelect(String dayDate){
		if(dayDateListener!=null && datedayList!=null){
			dayDateListener.selectday(dayDate);
			dismiss();
		}else
			dismiss();
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
			if(postion==0){
				setAdapterDatas(mAdapter0,sdf.format(cal.getTime()));
			}else if(postion==1){
				setAdapterDatas(mAdapter1,sdf.format(cal.getTime()));
			}else{
				setAdapterDatas(mAdapter2,sdf.format(cal.getTime()));
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
			if(postion==0){
				setAdapterDatas(mAdapter0,sdf.format(cal.getTime()));
			}else if(postion==1){
				setAdapterDatas(mAdapter1,sdf.format(cal.getTime()));
			}else{
				setAdapterDatas(mAdapter2,sdf.format(cal.getTime()));
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
