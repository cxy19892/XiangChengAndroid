package com.yzm.sleep.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.shop.GoodsDetailActivity;
import com.yzm.sleep.activity.shop.PackageDetailsActivity;
import com.yzm.sleep.activity.shop.ShopOrderListActivity;
import com.yzm.sleep.adapter.HosShopProAdapter;
import com.yzm.sleep.bean.ShopOrderBean;
import com.yzm.sleep.bean.TaocanBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.DoctorProParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.HosDoctorsParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHosProAndSerCallback;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
/**
 * 产品服务列表
 * @author Administrator<br>
 * type 1: 医馆的产平和服务<br>
 * type 2： 医生的产品和服务<br>
 */
public class HosShopProlistActivity extends BaseActivity {
	
	ListView proListView;
	TextView titleTv;
	private HosShopProAdapter adapter;
	private List<TaocanBean> mList;
	private MaterialRefreshLayout mRefresh;
	private int Page = 1;
	private int totalPage;
	private String ygid;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hos_doctors_list);
		mList = new ArrayList<TaocanBean>();
		type = getIntent().getIntExtra("type", 1);
		ygid = getIntent().getStringExtra("ygid");
		initViews();
		initViewOncli();
		mRefresh.autoRefresh();
	}

	private void initViews() {
		mRefresh = (MaterialRefreshLayout) findViewById(R.id.hosdoc_refresh);
		proListView = (ListView) findViewById(R.id.my_hosdoc_listv);
		titleTv= (TextView) findViewById(R.id.title);
		findViewById(R.id.back).setOnClickListener(this);
		titleTv.setText("产品服务");
		adapter = new HosShopProAdapter(this);
		proListView.setAdapter(adapter);
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

				if (checkNetWork(HosShopProlistActivity.this)) {
					getHosDoctors(ygid, Page, true);
				} else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							mRefresh.finishRefresh();
							Util.show(HosShopProlistActivity.this, "请检查您的网络");
							mRefresh.addListViewState(proListView, -1);
							if(mList.size()==0){
								mRefresh
								.addListViewState(proListView, Constant.NO_NET);
							}
						}
					}, 300);
				}
			}
		});

		/**
		 * 上啦加载更多
		 */
		proListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (proListView.getChildCount() > 0) {
						View lastItemView = (View) proListView
								.getChildAt(proListView.getChildCount() - 1);
						if ((proListView.getBottom()) == lastItemView
								.getBottom()) {
							if (Page < totalPage) {
								mRefresh.addListViewState(proListView,
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

		proListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 int index = position - proListView.getHeaderViewsCount();
                 if(index >= 0){
					TaocanBean mTaocanBean = adapter.getData().get(index);
					startActivity(new Intent(HosShopProlistActivity.this, GoodsDetailActivity.class)
					.putExtra("goods_id", mTaocanBean.getTcid()).putExtra("kefu", mTaocanBean.getKefu()));
                 }
			}
		});
	}
	
	
	private void getHosDoctors(String ygid, int page, final boolean isRefresh){
		HosDoctorsParamClass mparams = new HosDoctorsParamClass();
		mparams.page = page+"";
		mparams.pagesize = "10";
		mparams.ygid = ygid;
		new XiangchengMallProcClass(HosShopProlistActivity.this).getHosShopPro(type, mparams, new InterfaceHosProAndSerCallback() {
			
			@Override
			public void onSuccess(int icode, List<TaocanBean> taocanlist, int totalpage) {
				mRefresh.addListViewState(proListView, -1);
				if(isRefresh){
					mList = taocanlist;
				}else {
					mList.addAll(taocanlist);
				}
				if (Page >= totalpage) {
					
					mRefresh
							.addListViewState(proListView, Constant.NO_MORE);
				} else {
//					removeFooter();
					mRefresh
							.addListViewState(proListView, Constant.LOADING);
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
