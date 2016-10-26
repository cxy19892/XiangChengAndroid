package com.yzm.sleep.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.AssessmentActivity;
import com.yzm.sleep.adapter.OccupationAdapter;
import com.yzm.sleep.adapter.SelectJobAdapter;
import com.yzm.sleep.bean.OccpationBean;
import com.yzm.sleep.bean.OccpationType;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;

/**
 * 职业
 * 
 * @author Administrator
 * 
 */
public class FragmentProfessional extends Fragment  {
	private ListView types, typeContens;
	private List<OccpationBean> typeData;
	private List<OccpationType> occpationTypes;
	private SelectJobAdapter jobAdapter;
	private OccupationAdapter occupationAdapter;
	private Activity activity;
	private TextView tv_userzhiye;

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
		return inflater.inflate(R.layout.activity_zhiye, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// 初始化控件
		super.onViewCreated(view, savedInstanceState);
		initView(view);

	}

	private void initData() {
		occpationTypes = PreManager.instance().getOccupationTypes();
		if (occpationTypes.size() > 0) {
			occpationTypes.get(0).setSelset(true);
			typeData = occpationTypes.get(0).getType();
		} else {
			typeData = new ArrayList<OccpationBean>();
		}
		occupationAdapter = new OccupationAdapter(activity, occpationTypes);
		jobAdapter = new SelectJobAdapter(activity, typeData);

		types.setAdapter(occupationAdapter);
		typeContens.setAdapter(jobAdapter);
	}

	private void initView(View view) {
		tv_userzhiye = (TextView) view.findViewById(R.id.tv_userzhiye);
		types = (ListView) view.findViewById(R.id.types);
		typeContens = (ListView) view.findViewById(R.id.type_contens);

		types.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < occpationTypes.size(); i++) {
					occpationTypes.get(i).setSelset(false);
				}
				occpationTypes.get(position).setSelset(true);
				occupationAdapter.setDatas(occpationTypes);
				jobAdapter.setDatas(typeData = occpationTypes.get(position).getType());
			}
		});

		typeContens.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				tv_userzhiye.setText(typeData.get(position).getVocation());
				tv_userzhiye.setTag(typeData.get(position).getCode());
//				PreManager.instance().saveUserOccupation(activity,
//						typeData.get(position).getCode());
			}
		});
		initData();
	}

	/**
	 * 是否修改职业
	 * 
	 * @return
	 */
	public boolean isData() {
		String code=tv_userzhiye.getTag()==null? "" : tv_userzhiye.getTag() .toString();
		if(!TextUtils.isEmpty(code) && !"".equals(code) && !code.equals("300000000") ){
			if(!code.equals(PreManager.instance().getUserOccupation(activity)))
				PreManager.instance().setIsUpdata(activity, true);
			((AssessmentActivity)activity).setData("jobcode", code);
			return true;
		}else{
			Util.show(activity, "请选择职业");
			return false;
		}
	}
	public String getProfession(){
		String code=tv_userzhiye.getTag()==null? "" : tv_userzhiye.getTag() .toString();
		return code;
	}

}
