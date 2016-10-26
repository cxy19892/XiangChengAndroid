package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.BaseFragmentPagerAdapter;
import com.yzm.sleep.fragment.FragmentMyReservation;
import com.yzm.sleep.fragment.FragmentShopOrderList;
import com.yzm.sleep.indicator.UnderlinePageIndicator;

public class OrdersActivity extends BaseActivity implements OnPageChangeListener {

	private Button back;
	private TextView title;
	private ViewPager myvPager;
	private TextView order_a, order_b;
	private FragmentMyReservation FragmentT1;
	private FragmentShopOrderList FragmentT2;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		initview();
	}

	private void initview() {
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		back.setOnClickListener(this);
		title.setText("我的订单");
		
		myvPager = (ViewPager) findViewById(R.id.vp_fragment_orders);
		order_a  = (TextView) findViewById(R.id.tv_order_reservation);
		order_b  = (TextView) findViewById(R.id.tv_order_shop);
		order_a.setOnClickListener(this);
		order_b.setOnClickListener(this);
		order_a.setText("预约订单");
		order_b.setText("购买订单");
		
		List<Fragment> fragments = getFragments();
        BaseFragmentPagerAdapter ama = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        myvPager.setAdapter(ama);
        
		UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.order_f_indicator);
		indicator.setViewPager(myvPager);
		indicator.setFades(false);
		indicator.setSelectedColor(getResources().getColor(R.color.theme_color));
		indicator.setOnPageChangeListener(this);
		order_a.setTextColor(getResources().getColor(R.color.theme_color));
		order_b.setTextColor(getResources().getColor(R.color.onet_color));
		myvPager.setCurrentItem(0);
	}
	
	private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();
        FragmentT1 = new FragmentMyReservation();
        FragmentT2 = new FragmentShopOrderList();
            fList.add(FragmentT1);
            fList.add(FragmentT2);
        return fList;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_order_reservation:
			myvPager.setCurrentItem(0);
			break;
		case R.id.tv_order_shop:
			myvPager.setCurrentItem(1);
			break;
		case R.id.back:
			AppManager.getAppManager().finishActivity();
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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().finishActivity();
			return true;
		}else
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			order_a.setTextColor(getResources().getColor(R.color.theme_color));
			order_b.setTextColor(getResources().getColor(R.color.onet_color));
			break;
		case 1:
			order_a.setTextColor(getResources().getColor(R.color.onet_color));
			order_b.setTextColor(getResources().getColor(R.color.theme_color));
			break;
		default:
			break;
		}
		
	}
	
	
	

}
