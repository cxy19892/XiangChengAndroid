package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.BaseFragmentPagerAdapter;
import com.yzm.sleep.fragment.FragmentPageGetupTimeTrend;
import com.yzm.sleep.fragment.FragmentPageSleepLengthTrend;
import com.yzm.sleep.fragment.FragmentPageSleepTimeTrend;
import com.yzm.sleep.indicator.UnderlinePageIndicator;
import com.yzm.sleep.model.CalenderSelectDialog;
import com.yzm.sleep.model.CalenderSelectDialog.SelectDayDateListener;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.widget.LazyFragment;
import com.yzm.sleep.wxpay.Constants;

/**
 * 睡眠趋势
 * @author
 *
 */
@SuppressLint("SimpleDateFormat") 
public class SleepTrendActivity extends BaseActivity implements OnPageChangeListener{
	
	private Activity activity;
	private TextView tvTab1, tvTab2, tvTab3;
	private BaseFragmentPagerAdapter mAdapter;
	private ViewPager viewPager;
	private Button btnCalendar; //Tag 为1 日历, 2更多
	private CalenderSelectDialog caDialog;
	private List<Fragment> fragments;
	private FragmentPageSleepLengthTrend page1;
	private FragmentPageSleepTimeTrend page2;
	private FragmentPageGetupTimeTrend page3;
	private SimpleDateFormat simp;
	private String currentday;
	private LinearLayout linTable1, linTable2, linTable3;
	private ImageView imgTable1, imgTable2, imgTable3, imgCorner1, imgCorner2, imgCorner3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleeptrend);
		this.activity = this;
		simp = new SimpleDateFormat("yyyy-MM-dd");
		fragments = new ArrayList<Fragment>();
		currentday = TimeFormatUtil.isToday("yyyy-MM-dd");
		initView();
	}
	
	private void initView(){
		findViewById(R.id.back).setOnClickListener(this);
		btnCalendar = (Button) findViewById(R.id.btn_calendar);
		btnCalendar.setOnClickListener(this);
		btnCalendar.setTag("1");

		linTable1 = (LinearLayout) findViewById(R.id.tabl_1);
		linTable2 = (LinearLayout) findViewById(R.id.tabl_2);
		linTable3 = (LinearLayout) findViewById(R.id.tabl_3);

		imgTable1 = (ImageView) findViewById(R.id.tabl_1_img);
		imgTable2 = (ImageView) findViewById(R.id.tabl_2_img);
		imgTable3 = (ImageView) findViewById(R.id.tabl_3_img);

		imgCorner1= (ImageView) findViewById(R.id.tabl_1_corner);
		imgCorner2= (ImageView) findViewById(R.id.tabl_2_corner);
		imgCorner3= (ImageView) findViewById(R.id.tabl_3_corner);

		tvTab1 = (TextView) findViewById(R.id.title_tab1);
		tvTab2 = (TextView) findViewById(R.id.title_tab2);
		tvTab3 = (TextView) findViewById(R.id.title_tab3);
		linTable1.setOnClickListener(this);
		linTable2.setOnClickListener(this);
		linTable3.setOnClickListener(this);
		Bundle bundle = new Bundle();
		bundle.putString("dayDate", currentday);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		page1 = new FragmentPageSleepLengthTrend();
		page1.setArguments(bundle);
		page2 = new FragmentPageSleepTimeTrend();
		page2.setArguments(bundle);
		page3 = new FragmentPageGetupTimeTrend();
		page3.setArguments(bundle);
		
		fragments.add(page1);
		fragments.add(page2);
		fragments.add(page3);
		
		mAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setOnPageChangeListener(this);
//		UnderlinePageIndicator indicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
//		indicator.setViewPager(viewPager);
//		indicator.setFades(false);
//		indicator.setSelectedColor(getResources().getColor(R.color.theme_color));
//		indicator.setOnPageChangeListener(this);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Constant.screenWidht/6, Constant.screenWidht/6);
		imgTable1.setLayoutParams(lp);
		imgTable2.setLayoutParams(lp);
		imgTable3.setLayoutParams(lp);
	}
	
	/**
	 * 显示日历选择控件
	 */
	private void showCalendar(){
		caDialog = new CalenderSelectDialog(this,getScreenWidth(), currentday, new SelectDayDateListener() {
			@Override
			public void selectday(String dayDate) {
				try {
					long day = simp.parse(dayDate).getTime();
					Calendar calendar=Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					if(day <= simp.parse(simp.format(calendar.getTime())).getTime()){
						currentday =dayDate; 
						page1.setDayDate(dayDate);
						page2.setDayDate(dayDate);
						page3.setDayDate(dayDate);
					}else
						Util.show(activity, "不能玩穿越");
					
				} catch (Exception e) {
				}
			}
		});
		caDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
				AppManager.getAppManager().finishActivity();
				break;
			case R.id.btn_calendar:
				if("1".equals(btnCalendar.getTag().toString()))
					showCalendar();
				break;
			case R.id.tabl_1:
				viewPager.setCurrentItem(0);
				break;
			case R.id.tabl_2:
				viewPager.setCurrentItem(1);
				break;
			case R.id.tabl_3:
				viewPager.setCurrentItem(2);
				break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int position) {
		
	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		if (position == 0) {
			tvTab1.setTextColor(getResources().getColor(R.color.white));
			tvTab2.setTextColor(getResources().getColor(R.color.dark_blue));
			tvTab3.setTextColor(getResources().getColor(R.color.dark_blue));
			imgTable1.setImageResource(R.drawable.ic_sleep_lenth_select);
			imgTable2.setImageResource(R.drawable.ic_fall_sleep_normal);
			imgTable3.setImageResource(R.drawable.ic_getup_normal);

			imgCorner1.setVisibility(View.VISIBLE);
			imgCorner2.setVisibility(View.INVISIBLE);
			imgCorner3.setVisibility(View.INVISIBLE);
		} else if (position == 1) {
			tvTab1.setTextColor(getResources().getColor(R.color.dark_blue));
			tvTab2.setTextColor(getResources().getColor(R.color.white));
			tvTab3.setTextColor(getResources().getColor(R.color.dark_blue));
			imgTable1.setImageResource(R.drawable.ic_sleep_lenth_normal);
			imgTable2.setImageResource(R.drawable.ic_fall_sleep_select);
			imgTable3.setImageResource(R.drawable.ic_getup_normal);
			imgCorner1.setVisibility(View.INVISIBLE);
			imgCorner2.setVisibility(View.VISIBLE);
			imgCorner3.setVisibility(View.INVISIBLE);
		} else {
			tvTab1.setTextColor(getResources().getColor(R.color.dark_blue));
			tvTab2.setTextColor(getResources().getColor(R.color.dark_blue));
			tvTab3.setTextColor(getResources().getColor(R.color.white));
			imgTable1.setImageResource(R.drawable.ic_sleep_lenth_normal);
			imgTable2.setImageResource(R.drawable.ic_fall_sleep_normal);
			imgTable3.setImageResource(R.drawable.ic_getup_select);
			imgCorner1.setVisibility(View.INVISIBLE);
			imgCorner2.setVisibility(View.INVISIBLE);
			imgCorner3.setVisibility(View.VISIBLE);
		}
	}
}
