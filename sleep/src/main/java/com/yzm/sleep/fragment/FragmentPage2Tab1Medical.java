package com.yzm.sleep.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.doctor.ClinicDetatilActivity;
import com.yzm.sleep.adapter.FragmentPage2tab1MedicalAdapter;
import com.yzm.sleep.bean.ClinicBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ClinicListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceClinicListCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.LazyFragment;

/**
 * 医馆
 * 
 * @author Administrator
 * 
 */
public class FragmentPage2Tab1Medical extends LazyFragment {
	private Activity activity;
	private MaterialRefreshLayout mRefresh;
	private ListView mElistview;
	private int screenWidht;
	private int page = 1;// 页数
	private int totalPage;
	private FragmentPage2tab1MedicalAdapter mAdapter;
	private List<ClinicBean> beanList;
	private LocationClient mLocClient;
	private String city = "成都";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		screenWidht = bundle.getInt("screenWidth", 0);
		mAdapter = new FragmentPage2tab1MedicalAdapter(activity, screenWidht);
		beanList = new ArrayList<ClinicBean>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_page3_tab1_my, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextView textView = new TextView(activity);
		textView.setHeight(0);
		mRefresh = (MaterialRefreshLayout) view
				.findViewById(R.id.fragment_mian_refresh);
		mElistview = (ListView) view.findViewById(R.id.my_expanlist);
		mElistview.setBackgroundResource(R.color.bg_color);
		mElistview.setDivider(null);
		mElistview.setHeaderDividersEnabled(false);
		mElistview.setDividerHeight(0);
		mElistview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mElistview.addHeaderView(textView);
		mElistview.setAdapter(mAdapter); 
		city = PreManager.instance().getUserLocationCity(activity);
		initViewOncli();
		isPrepared = true;
		lazyLoad();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Service_Institute");
		MobclickAgent.onResume(activity); 
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			mLocClient.unRegisterLocationListener(myListener);
		} catch (Exception e) {
		}
		MobclickAgent.onPageEnd("Service_Institute"); 
		MobclickAgent.onPause(activity);
	}

	public void refresh(String city){
		this.city = city;
		if(mElistview.getChildCount() > 0)
			mElistview.setSelection(0);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				beanList.clear();
				mAdapter.setData(beanList);
				mRefresh.autoRefresh();
				mRefresh.addListViewState(mElistview, -1);
			}
		},100);
	}
	
	private void initViewOncli() {
		mElistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position - mElistview.getHeaderViewsCount();
				if(index >= 0){
					startActivity(new Intent(activity, ClinicDetatilActivity.class).putExtra("id", beanList.get(index).getYgid()));
				}
			}
		});
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
				String longitude = PreManager.instance().getUserLongitude(getActivity());
				String latitude = PreManager.instance().getUserLatitude(getActivity());
				if(TextUtils.isEmpty(city) || TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)){
					mLocClient = new LocationClient(getActivity());
					mLocClient.registerLocationListener(myListener);
					LocationClientOption option = new LocationClientOption();
					option.setOpenGps(true);// 打开gps
					option.setCoorType("bd09ll"); // 设置坐标类型
					option.setAddrType("all");
					option.setScanSpan(1000);
					mLocClient.setLocOption(option);
					mLocClient.start();
				}else{
					if(Util.checkNetWork(activity)){
						page = 1;
						HttpMedicalTask(false);
					}else{
						new Handler().postDelayed(new Runnable() {
							public void run() {
								mRefresh.finishRefresh();
								if(beanList .size() <= 0)
									mRefresh.addListViewState(mElistview, Constant.NO_NET);
								else
									Util.show(activity, "网路连接错误");
										
							}
						}, 300);
					}
				}
			}
		});
		
		/**
		 * 上啦加载更多
		 */
		mElistview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (mElistview.getChildCount() > 0) {
						View lastItemView = (View) mElistview.getChildAt(mElistview.getChildCount() - 1);
						if ((mElistview.getBottom()) == lastItemView .getBottom()) {
							if (page < totalPage) { 
								if (Util.checkNetWork(activity)) {
									page ++;
									HttpMedicalTask(true);
								}else
									Util.show(activity, "网路连接错误");
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
	
	/**
	 * 定位监听
	 */
	private BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if(mLocClient != null)
				mLocClient.stop();
			if(location !=null){
				city = TextUtils.isEmpty(location.getCity()) ? city : location.getCity();
				PreManager.instance().saveUserLocationCity(getActivity(), city.replace("市", ""));
				PreManager.instance().saveUserLatitude(getActivity(), location.getLatitude()+"");
				PreManager.instance().saveUserLongitude(getActivity(), location.getLongitude()+"");
			}
			if(Util.checkNetWork(activity)){
				HttpMedicalTask(false);
			}else{
				mRefresh.addListViewState(mElistview, Constant.NO_NET);
			}
		}
	};
	

	/** 	
	 * 访问网络
	 * 
	 * @param ischd
	 */
	private void HttpMedicalTask(final boolean ischd) {
		ClinicListParamClass clinicListParamClass = new ClinicListParamClass();
		clinicListParamClass.page = String.valueOf(page);
		clinicListParamClass.pagesize = "10";
		clinicListParamClass.city = city.replace("市", "");
		clinicListParamClass.location_x = PreManager.instance().getUserLongitude(activity);
		clinicListParamClass.location_y = PreManager.instance().getUserLatitude(activity);
		 
		new XiangchengProcClass(activity).clinicList(clinicListParamClass,
				new InterfaceClinicListCallback() {

					@Override
					public void onSuccess(int icode, List<ClinicBean> clinicList, int totalpage) {
						if (!ischd) 
							mRefresh.finishRefresh();
						netWorkData(clinicList, totalpage, ischd);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						Util.show(activity, strErrMsg);
						if (!ischd) 
							mRefresh.finishRefresh();
						if (beanList.size() == 0) {
							mRefresh.addListViewState(mElistview, Constant.NO_DATA);
						}
					}
				});

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

	// 标志位，标志已经初始化完成。
	private boolean isPrepared;
	private boolean isFirst = true;
	private boolean isRequest = false;

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible) {
			return;
		}
		if (!isRequest) {
			isRequest = true;
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					if (isFirst) {
						isFirst = false;
					}
					if (Util.checkNetWork(activity)) {
						mRefresh.autoRefresh();
					} else {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								mRefresh.finishRefresh();
								mRefresh.addListViewState(mElistview,Constant.NO_NET);
							}
						}, 300);
					}
				}
			}, 100);
		}

	}

	/**
	 * 处理数据
	 * @param clinicList
	 * @param totalpage
	 * @param ischd 是否是加载更多
	 */
	private void netWorkData(List<ClinicBean> clinicList,int totalpage, boolean ischd) {
		this.totalPage = totalpage;
		if (clinicList != null) {
			if(ischd)
				beanList.addAll(clinicList);
			else
				beanList = clinicList; 
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mAdapter.setData(beanList);
				if(page < totalPage)
					mRefresh.addListViewState(mElistview, Constant.LOADING);
				else{
					if (beanList.size() == 0) {
						mRefresh.addListViewState(mElistview, Constant.NO_DATA);
					}else{
						mRefresh.addListViewState(mElistview, Constant.NO_MORE);
					}
				}
			}
		}, 300);
	}
}
