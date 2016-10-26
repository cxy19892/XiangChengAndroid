package com.yzm.sleep.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.adapter.ClinicDoctorAdapter;
import com.yzm.sleep.bean.DoctorBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.HosDoctorsParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHosDoctorsCallback;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
/**
 * 医馆的名医列表
 * @author Administrator
 *
 */
public class HosdoctorListActivity extends BaseActivity {
	
	ListView docListView;
	TextView titleTv;
	private ClinicDoctorAdapter adapter;
	private List<DoctorBean> mList;
	private MaterialRefreshLayout mRefresh;
	private int Page = 1;
	private int totalPage;
	private String ygid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hos_doctors_list);
		mList = new ArrayList<DoctorBean>();
		ygid = getIntent().getStringExtra("ygid");
		initViews();
		initViewOncli();
		mRefresh.autoRefresh();
	}

	private void initViews() {
		mRefresh = (MaterialRefreshLayout) findViewById(R.id.hosdoc_refresh);
		docListView = (ListView) findViewById(R.id.my_hosdoc_listv);
		titleTv= (TextView) findViewById(R.id.title);
		findViewById(R.id.back).setOnClickListener(this);
		titleTv.setText(getResources().getString(R.string.string_hospital_doctor));
		adapter = new ClinicDoctorAdapter(this);
		docListView.setAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Service_Institute_Intro_Expert");
		MobclickAgent.onResume(this); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Service_Institute_Intro_Expert"); 
		MobclickAgent.onPause(this);
	}
	
	private void initViewOncli() {
		/**
		 * 没网络的时候：点击屏幕刷洗
		 */
		mRefresh.setOnClickRequestListener(new OnClickRequestListener() {

			@Override
			public void setRequest() {
				mRefresh.autoRefresh();
			}
		});
		/**
		 * 下啦刷新
		 */

		mRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				Page = 1;

				if (checkNetWork(HosdoctorListActivity.this)) {
					getHosDoctors(ygid, Page, true);
				} else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							mRefresh.finishRefresh();
							Util.show(HosdoctorListActivity.this, "请检查您的网络");
							mRefresh.addListViewState(docListView, -1);
							if(mList.size()==0){
								mRefresh
								.addListViewState(docListView, Constant.NO_NET);
							}
						}
					}, 300);
				}
			}
		});

		/**
		 * 上啦加载更多
		 */
		docListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (docListView.getChildCount() > 0) {
						View lastItemView = (View) docListView
								.getChildAt(docListView.getChildCount() - 1);
						if ((docListView.getBottom()) == lastItemView
								.getBottom()) {
							if (Page < totalPage) {
								mRefresh.addListViewState(docListView,
										Constant.LOADING);
								getHosDoctors(ygid, Page+=1, false);
							}
						}
					}
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView listview,
					int scrollState) {
			}
		});

	}
	
	
	private void getHosDoctors(String ygid, int page, final boolean isRefresh){
		HosDoctorsParamClass mparams = new HosDoctorsParamClass();
		mparams.page = page+"";
		mparams.pagesize = "10";
		mparams.ygid = ygid;
		new XiangchengMallProcClass(HosdoctorListActivity.this).getHosDoctors(mparams, new InterfaceHosDoctorsCallback() {
			
			@Override
			public void onSuccess(int icode, List<DoctorBean> doctorslist, int totalpage) {
				mRefresh.addListViewState(docListView, -1);
				if(isRefresh){
					mList = doctorslist;
				}else {
					mList.addAll(doctorslist);
				}
				if (Page >= totalpage) {
					
					mRefresh
							.addListViewState(docListView, Constant.NO_MORE);
				} else {
//					removeFooter();
					mRefresh
							.addListViewState(docListView, Constant.LOADING);
				}
				totalPage = totalpage; 
				adapter.setData(mList);
				mRefresh.finishRefresh();
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				mRefresh.finishRefresh();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().finishActivity();
			return true;
		}else
		return super.onKeyDown(keyCode, event);
	}

	
}
