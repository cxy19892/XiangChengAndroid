package com.yzm.sleep.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.KnowledgeListActivity;
import com.yzm.sleep.activity.community.KnowledgeSearchActivity;
import com.yzm.sleep.activity.community.Page3Tab1SecondActivity;
import com.yzm.sleep.utils.Util;

public class FragmentPage3Tab1 extends Fragment implements OnClickListener {

	private Activity activity;
	private ImageView headImg;
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_page3_tab1, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		headImg = (ImageView) view.findViewById(R.id.view_head);
		view.findViewById(R.id.knowledge_title_search).setOnClickListener(this);
		view.findViewById(R.id.btn1).setOnClickListener(this);
		view.findViewById(R.id.btn2).setOnClickListener(this);
		view.findViewById(R.id.btn3).setOnClickListener(this);
		view.findViewById(R.id.btn4).setOnClickListener(this);
		headImg.setOnClickListener(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Constant.screenWidht/15*11);
		lp.setMargins(0, Util.Dp2Px(getActivity(), 58), 0, 0);
		headImg.setLayoutParams(lp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_head:
			startActivity(new Intent(activity, KnowledgeListActivity.class).putExtra("type", "4"));
			break;
		case R.id.knowledge_title_search:
			startActivity(new Intent(activity, KnowledgeSearchActivity.class));
			break;
		case R.id.btn1:
			startActivity(new Intent(activity, KnowledgeListActivity.class).putExtra("type", "5"));
			break;
		case R.id.btn2:
			startActivity(new Intent(activity, Page3Tab1SecondActivity.class).putExtra("type", "2"));
			break;
		case R.id.btn3:
			startActivity(new Intent(activity, KnowledgeListActivity.class).putExtra("type", "6"));
			break;
		case R.id.btn4:
			startActivity(new Intent(activity, Page3Tab1SecondActivity.class).putExtra("type", "3"));
			break;

		default:
			break;
		}
	}

}
