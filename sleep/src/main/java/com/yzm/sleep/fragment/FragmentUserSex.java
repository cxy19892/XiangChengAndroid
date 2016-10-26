package com.yzm.sleep.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.jpush.android.api.c;

import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.AssessmentActivity;
import com.yzm.sleep.utils.PreManager;

/**
 * 性别
 * 
 * @author Administrator
 * 
 */
public class FragmentUserSex extends Fragment implements OnClickListener {
	private ImageView im_male, im_female;
	private RelativeLayout re_usernan, re_usernv;
	private String sex = "01";
	private Activity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 初始化非控件对象
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 加载布局，或初始化布局对象
		return inflater.inflate(R.layout.activity_usersex, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// 初始化控件
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		sexIcon(sex);
	}

	private void initView(View view) {
		im_male = (ImageView) view.findViewById(R.id.user_im_male);
		im_female = (ImageView) view.findViewById(R.id.userim_female);
		re_usernan = (RelativeLayout) view.findViewById(R.id.re_usernan);
		re_usernv = (RelativeLayout) view.findViewById(R.id.re_usernv);
		re_usernan.setOnClickListener(this);
		re_usernv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_usernan:
			sexIcon("01");
			break;
		case R.id.re_usernv:
			sexIcon("02");
			break;
		default:
			break;
		}
	}

	/*
	 * 更改ICon (男。女)
	 */
	private void sexIcon(String pos) {
		if (pos.equals("01")) {
			sex = "01";
			im_male.setBackgroundResource(R.drawable.ic_man_selected);
			im_female.setBackgroundResource(R.drawable.ic_woman_normal);
//			PreManager.instance().saveUserGender(activity, sex);
		} else if (pos.equals("02")) {
			sex = "02";
			im_male.setBackgroundResource(R.drawable.ic_man_normal);
			im_female.setBackgroundResource(R.drawable.ic_woman_selected);
//			PreManager.instance().saveUserGender(activity, sex);
		}
	}

	public boolean isData() {
		if (sex != null && sex.length() > 0) {
			if(!sex.equals(PreManager.instance().getUserGender(activity))){
				PreManager.instance().setIsUpdata(activity, true);
			}
			((AssessmentActivity)activity).setData("gender", sex);
//			PreManager.instance().saveUserGender(activity, sex);
			return true;
		} else {
			return false;
		}

	}
	public void setData(String sex){
		sexIcon(sex);
	}
	
	public String getSex(){
		
		return sex;
	}
}
