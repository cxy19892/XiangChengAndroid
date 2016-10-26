package com.yzm.sleep.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.adapter.MyReservationAdapter;
import com.yzm.sleep.bean.ReservationBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceReservationListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ReservationListParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 我的预约界面
 * 
 * @author hetonghua
 * 
 */
public class MyReservationActivity extends BaseActivity {
	private List<ReservationBean> mReservations;
	private MyReservationAdapter mAdapter;
	private MaterialRefreshLayout refreshView;
	private ListView mListView;
	private View noDataView;
	private int dataPage = 1, totalPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_reservation);
		initView();
		mAdapter = new MyReservationAdapter(MyReservationActivity.this);
		mReservations = new ArrayList<ReservationBean>();
		mListView.setAdapter(mAdapter);
		initData();
	}

	private void initData() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshView.autoRefresh();
			}
		}, 300);
	}

	private void initView() {
		((TextView) findViewById(R.id.title)).setText("我的预约");
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity();
			}
		});

		refreshView = (MaterialRefreshLayout) findViewById(R.id.pull_refreshview);

		mListView = (ListView) findViewById(R.id.listView);
		mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (mListView.getChildCount() > 0) {
						View lastItemView = (View) mListView
								.getChildAt(mListView.getChildCount() - 1);
						if ((mListView.getBottom()) == lastItemView.getBottom()) {
							if (dataPage < totalPage) {
								dataPage += 1;
								refreshView.addListViewState(mListView, Constant.LOADING);
								getReservationData(false);
							}
						}
					}
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int iPosition = position - mListView.getHeaderViewsCount();
				if(iPosition >= 0 && "1".equals(mReservations.get(iPosition).getFlag())){
					Intent intent = new Intent(MyReservationActivity.this,ReservationDetailActivity.class);
					intent.putExtra("id", mReservations.get(iPosition).getId());
					startActivity(intent);
				}
			}
		});

		noDataView = LayoutInflater.from(MyReservationActivity.this).inflate(
				R.layout.no_comment_data_layout, null);
		noDataView.findViewById(R.id.no_data_view).setVisibility(View.GONE);
		((TextView) noDataView.findViewById(R.id.no_data_txt)).setText("无预约记录");
		noDataView.setVisibility(View.GONE);
		mListView.addFooterView(noDataView, null, false);

		refreshView.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				dataPage = 1;
				if (checkNetWork(MyReservationActivity.this)) {
					getReservationData(true);
				} else {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							Util.show(MyReservationActivity.this, "网络连接错误");
							removeFooterView();
							refreshView.finishRefresh();
							refreshView.addListViewState(mListView,
									Constant.NO_NET);
						}
					}, 500);
				}

			}
		});

		refreshView.setOnClickRequestListener(new OnClickRequestListener() {

			@Override
			public void setRequest() {
				refreshView.autoRefresh();
			}
		});
	}

	private void getReservationData(final boolean isRefresh) {
		ReservationListParamClass mParams = new ReservationListParamClass();
		mParams.uid = PreManager.instance().getUserId(
				MyReservationActivity.this);
		mParams.page = String.valueOf(dataPage);
		mParams.pagesize = "10";
		new XiangchengProcClass(MyReservationActivity.this).reservationList(
				mParams, new InterfaceReservationListCallback() {

					@Override
					public void onSuccess(int icode,
							List<ReservationBean> reserationList, int totalpage) {
						refreshView.addListViewState(mListView, -1);
						totalPage = totalpage;
						refreshView.finishRefresh();
						removeFooterView();
						if (reserationList != null && reserationList.size() > 0) {
							if (isRefresh) {
								mReservations = reserationList;
							} else {
								mReservations.addAll(reserationList);
							}
							mAdapter.setData(mReservations);
						}

						if (mReservations.size() > 0) {
							if (dataPage >= totalpage) {
								refreshView.addListViewState(mListView,
										Constant.NO_MORE);
							}
						} else {
							mListView.addFooterView(noDataView, null, false);
							noDataView.setVisibility(View.VISIBLE);
							// refreshView.addListViewState(mListView,
							// Constant.NO_DATA);
						}
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						refreshView.addListViewState(mListView, -1);
						removeFooterView();
						refreshView.finishRefresh();
						if (mReservations.size() == 0
								&& !Util.checkNetWork(MyReservationActivity.this)) {
							refreshView.addListViewState(mListView,
									Constant.NO_MORE);
						}
						Util.show(MyReservationActivity.this, strErrMsg);
					}
				});

	}

	private void removeFooterView() {
		mListView.removeFooterView(noDataView);
	}
}
