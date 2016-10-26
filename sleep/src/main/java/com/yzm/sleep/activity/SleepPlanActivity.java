package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.SleepPlanAdapter;
import com.yzm.sleep.bean.PlanBean;
import com.yzm.sleep.utils.InterfaceMallUtillClass.EditPlanListCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.EditPlanListParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetPlanListCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetPlanListParams;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 七日计划
 * @author tianuxn
 */
@SuppressLint("SimpleDateFormat") 
public class SleepPlanActivity extends BaseActivity{
	
	private View noNet, onData;
	private ListView mListView;
	private SleepPlanAdapter mAdapter;
	private Button right;
	private Context context;
	private List<PlanBean> showList, mPlanList;
	private List<PlanBean> listT1, listT2, listT3, listT4; //修改项
	private RadioButton type1, type2, type3, type4;
	private View linearlayout;
	private String strPlan;
	private Gson gson;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		setContentView(R.layout.activity_sleep_plan);
		gson = new Gson();
		listT1 = new ArrayList<PlanBean>();
		listT2 = new ArrayList<PlanBean>();
		listT3 = new ArrayList<PlanBean>();
		listT4 = new ArrayList<PlanBean>();
		showList = new ArrayList<PlanBean>();
		mPlanList = new ArrayList<PlanBean>();
		initView();
	}
	
	private void initView(){
		linearlayout = findViewById(R.id.linearlayout);
		onData = findViewById(R.id.no_data);
		noNet = getLayoutInflater().inflate(R.layout.layout_no_net, null);
		((TextView)noNet.findViewById(R.id.text)).setText("请检查网路设置");
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("7日计划");
		right = (Button) findViewById(R.id.btn_title_right);
		right.setText("修改");
		right.setOnClickListener(this);
		right.setCompoundDrawables(null, null, null, null);
		right.setVisibility(View.VISIBLE);
		type1 = (RadioButton) findViewById(R.id.type1);
		type1.setOnClickListener(this);
		type2 = (RadioButton) findViewById(R.id.type2);
		type2.setOnClickListener(this);
		type3 = (RadioButton) findViewById(R.id.type3);
		type3.setOnClickListener(this);
		type4 = (RadioButton) findViewById(R.id.type4);
		type4.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.addHeaderView(noNet, null, false);
		mAdapter = new SleepPlanAdapter(context);
		mListView.removeHeaderView(noNet);
		mListView.setAdapter(mAdapter);
		if(Util.checkNetWork(context)){
			linearlayout.setVisibility(View.VISIBLE);
			mListView.removeHeaderView(noNet);
			getPlanList();
		}else{
			mListView.addHeaderView(noNet, null, false);
			mAdapter.setData(new ArrayList<PlanBean>());
			linearlayout.setVisibility(View.GONE);
		}
	}
	
	private int select_type;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			if(mAdapter.getOpenSe()){
				restListData();
				right.setText("修改");
			}else
				AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			if(!Util.checkNetWork(context)){
				Util.show(context, "检查网路设置");
				return;
			}
			
			if(mAdapter.getOpenSe()){
				modifyPlan();
				right.setText("修改");
			}else{
				mAdapter.openSe(true);
				switchType(select_type);
				right.setText("确定");
			}
			break;
		case R.id.type1:
			switchType(1);
			break;
		case R.id.type2:
			switchType(2);
			break;
		case R.id.type3:
			switchType(3);
			break;
		case R.id.type4:
			switchType(4);
			break;
		default:
			break;
		}
	}

	private void getPlanList(){
		showPro();
		GetPlanListParams mParams = new GetPlanListParams();
		Calendar calendar =Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		mParams.my_int_id = PreManager.instance().getUserId(context);
		mParams.date = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
		
		new XiangchengMallProcClass(context).getPlanList(mParams, new GetPlanListCallBack() {
			
			@Override
			public void onSuccess(int icode, List<PlanBean> mlist) {
				cancelPro();
				strPlan = gson.toJson(mlist);
				classifyData(mlist);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				cancelPro();
				Util.show(context, strErrMsg);
			}
		});
	}
	
	/**
	 * 分类数据
	 * @param mlist
	 */
	private void classifyData(List<PlanBean> mlist){
		this.mPlanList = mlist;
		for (PlanBean planBean : mlist) {
			if("1".equals(planBean.getType())){
				listT1.add(planBean);
			}else if("2".equals(planBean.getType())){
				listT2.add(planBean);
			}else if("3".equals(planBean.getType())){
				listT3.add(planBean);
			}else if("4".equals(planBean.getType())){
				listT4.add(planBean);
			}
		}
		
		switchType(1);
		List<PlanBean> listTemp = new ArrayList<PlanBean>();
		for (PlanBean planBean : listT1) {
			if("1".equals(planBean.getIsshow()))
				listTemp.add(planBean);
		}
		
		mAdapter.setData(listTemp);
		if(listTemp.size() > 0)
			onData.setVisibility(View.GONE);
		else
			onData.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 点击返回
	 * @param type
	 */
	private void restListData(){
		mAdapter.openSe(false);
		listT1.clear();
		listT2.clear();
		listT3.clear();
		listT4.clear();
		try {
			this.mPlanList = gson.fromJson(strPlan, new TypeToken<List<PlanBean>>() {}.getType());
		} catch (Exception e) {
		}
		
		if(this.mPlanList == null || this.mPlanList.size() < 0)
			return;
		
		for (PlanBean planBean : this.mPlanList) {
			if("1".equals(planBean.getType())){
				listT1.add(planBean);
			}else if("2".equals(planBean.getType())){
				listT2.add(planBean);
			}else if("3".equals(planBean.getType())){
				listT3.add(planBean);
			}else if("4".equals(planBean.getType())){
				listT4.add(planBean);
			}
		}
		switchType(select_type);
	}
	
	/**
	 * 类型选择
	 * @param type
	 */
	private void switchType(int type){
		select_type = type;
		type1.setChecked(false);
		type2.setChecked(false);
		type3.setChecked(false);
		type4.setChecked(false);
		switch (type) {
		case 1:
			type1.setChecked(true);
			if(mAdapter.getOpenSe()){
				mAdapter.setData(listT1);
				if(listT1.size() > 0)
					onData.setVisibility(View.GONE);
				else
					onData.setVisibility(View.VISIBLE);
			}
			else{
				List<PlanBean> listTemp = new ArrayList<PlanBean>();
				for (PlanBean planBean : listT1) {
					if("1".equals(planBean.getIsshow()))
						listTemp.add(planBean);
				}
				mAdapter.setData(listTemp);
				if(listTemp.size() > 0)
					onData.setVisibility(View.GONE);
				else
					onData.setVisibility(View.VISIBLE);
			}
			break;
		case 2:
			type2.setChecked(true);
			if(mAdapter.getOpenSe()){
				mAdapter.setData(listT2);
				if(listT2.size() > 0)
					onData.setVisibility(View.GONE);
				else
					onData.setVisibility(View.VISIBLE);
			}
			else{
				List<PlanBean> listTemp = new ArrayList<PlanBean>();
				for (PlanBean planBean : listT2) {
					if("1".equals(planBean.getIsshow()))
						listTemp.add(planBean);
				}
				mAdapter.setData(listTemp);
				if(listTemp.size() > 0)
					onData.setVisibility(View.GONE);
				else
					onData.setVisibility(View.VISIBLE);
			}
			break;
		case 3:
			type3.setChecked(true);
			if(mAdapter.getOpenSe()){
				mAdapter.setData(listT3);
				if(listT3.size() > 0)
					onData.setVisibility(View.GONE);
				else
					onData.setVisibility(View.VISIBLE);
			}
			else{
				List<PlanBean> listTemp = new ArrayList<PlanBean>();
				for (PlanBean planBean : listT3) {
					if("1".equals(planBean.getIsshow()))
						listTemp.add(planBean);
				}
				mAdapter.setData(listTemp);
				if(listTemp.size() > 0)
					onData.setVisibility(View.GONE);
				else
					onData.setVisibility(View.VISIBLE);
			}
			break;
		case 4:
			type4.setChecked(true);
			if(mAdapter.getOpenSe()){
				mAdapter.setData(listT4);
				if(listT4.size() > 0)
					onData.setVisibility(View.GONE);
				else
					onData.setVisibility(View.VISIBLE);
			}else{
				List<PlanBean> listTemp = new ArrayList<PlanBean>();
				for (PlanBean planBean : listT4) {
					if("1".equals(planBean.getIsshow()))
						listTemp.add(planBean);
				}
				mAdapter.setData(listTemp);
				if(listTemp.size() > 0)
					onData.setVisibility(View.GONE);
				else
					onData.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}
	
	
	private ProgressUtils pro;
	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}
	
	/**
	 * 提交
	 */
	private void modifyPlan(){
		StringBuffer buffer = new StringBuffer();
		final List<PlanBean> temp = new ArrayList<PlanBean>();
		temp.addAll(listT1);
		temp.addAll(listT2);
		temp.addAll(listT3);
		temp.addAll(listT4);
		
		buffer.append("{");
		int size = temp.size();
		showList.clear();
		for (int i = 0; i < size; i++) {
			PlanBean bean = temp.get(i);
			if("1".equals(bean.getIsshow()))
				showList.add(bean);
			buffer.append("\""+bean.getPlanid()+"\"");
			buffer.append(":");
			buffer.append(bean.getIsshow());
			if(i < size - 1)
				buffer.append(",");
		}
		buffer.append("}");
		showPro();
		EditPlanListParams mParams = new  EditPlanListParams ();
		mParams.my_int_id = PreManager.instance().getUserId(context);
		mParams.content = buffer.toString();
		new XiangchengMallProcClass(context).modifyPlan(mParams, new EditPlanListCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				cancelPro();
				Util.show(context, strSuccMsg);
				Gson gson = new Gson();
				gson.toJson(showList);
				strPlan = gson.toJson(temp);
				restListData();
				PreManager.instance().saveSleepPlan(context, gson.toJson(showList));
				sendBroadcast(new Intent().setAction(Constant.MODIFY_PLAN));
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				cancelPro();
				Util.show(context, strErrMsg);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mAdapter.getOpenSe()){
				restListData();
				right.setText("修改");
			}else
				AppManager.getAppManager().finishActivity();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	
}
