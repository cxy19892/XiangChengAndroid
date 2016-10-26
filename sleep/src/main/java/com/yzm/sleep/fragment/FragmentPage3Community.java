package com.yzm.sleep.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.community.CommunitySearchActivity;
import com.yzm.sleep.activity.community.KnowledgeSearchActivity;
import com.yzm.sleep.adapter.BaseFragmentPagerAdapter;
import com.yzm.sleep.indicator.UnderlinePageIndicator;
import com.yzm.sleep.utils.PreManager;

public class FragmentPage3Community extends Fragment implements
		OnClickListener, OnPageChangeListener {

	private Activity activity;
	private TextView tvTab1, tvTab2;
	private ImageButton btnMsg;
	private BaseFragmentPagerAdapter mAdapter;
	private ViewPager viewPager;
	private FragmentPage3Tab1 fragmentPage3Tab1;
	private FragmentPage3Tab2Group fragmentPage3Tab2Group;
	private List<Fragment> fragments;
	private Bundle bundle;
	private Class clas = KnowledgeSearchActivity.class;

	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_knowledge, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		btnMsg = (ImageButton) view.findViewById(R.id.community_title_msg);
		tvTab1 = (TextView) view.findViewById(R.id.community_title_tab1);
		tvTab2 = (TextView) view.findViewById(R.id.community_title_tab2);
		btnMsg.setOnClickListener(this);
		view.findViewById(R.id.community_title_search).setOnClickListener(this);
		tvTab1.setOnClickListener(this);       
		tvTab2.setOnClickListener(this);
		viewPager = (ViewPager) view.findViewById(R.id.community_viewpager);
		view.findViewById(R.id.open_city).setVisibility(View.GONE);
		fragments = new ArrayList<Fragment>();

		fragmentPage3Tab1 = new FragmentPage3Tab1();// 精选
		fragmentPage3Tab1.setArguments(bundle);

		fragmentPage3Tab2Group = new FragmentPage3Tab2Group(); // 小组
		fragmentPage3Tab2Group.setArguments(bundle);

		fragments.add(fragmentPage3Tab1);
		fragments.add(fragmentPage3Tab2Group);

		mAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), fragments);
		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(3);

		UnderlinePageIndicator indicator = (UnderlinePageIndicator) view
				.findViewById(R.id.indicator);
		indicator.setViewPager(viewPager);
		indicator.setFades(false);
		indicator.setSelectedColor(getResources()
				.getColor(R.color.theme_color));
		indicator.setOnPageChangeListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.community_title_tab1:
			viewPager.setCurrentItem(0, true);
			break;

		case R.id.community_title_tab2:
			viewPager.setCurrentItem(1, true);
			break;
		case R.id.community_title_search:
			if(clas == KnowledgeSearchActivity.class){
				startActivity(new Intent(activity,
						clas));
			}else{
				if (PreManager.instance().getIsLogin(activity)) {
					MobclickAgent.onEvent(activity, "534");
					startActivity(new Intent(activity,
							clas));
				} else {
					startActivity(new Intent(activity, LoginActivity.class));
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {

	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onPageSelected(int position) {
		if (position == 0) {
			tvTab1.setTextColor(getResources().getColor(R.color.theme_color));
			tvTab2.setTextColor(getResources().getColor(R.color.ct_color));
			clas = KnowledgeSearchActivity.class;
		} else if (position == 1) {
			tvTab1.setTextColor(getResources().getColor(R.color.ct_color));
			tvTab2.setTextColor(getResources().getColor(R.color.theme_color));
			clas = CommunitySearchActivity.class;
		} else {
			tvTab1.setTextColor(getResources().getColor(R.color.ct_color));
			tvTab2.setTextColor(getResources().getColor(R.color.ct_color));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
