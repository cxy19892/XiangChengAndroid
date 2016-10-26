package com.yzm.sleep.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.shop.GoodsDetailActivity;
import com.yzm.sleep.adapter.FragmentPage2TabStoreAdapter;
import com.yzm.sleep.bean.TaocanBean;
import com.yzm.sleep.model.SellEndDialog;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceTaocanListCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.TaocanMsgParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.LazyFragment;

/**
 * 商城
 * @author tianxun
 */
public class FragmentPage2Tab3Store extends LazyFragment {
	private Activity activity;
	private int screenWidht;
	private int dataPage = 1,totalPage=0;
	private MaterialRefreshLayout refrenshView;
	private ListView mList;
	private boolean isPrepared, isFirst = true, isRequest = false;
	private List<TaocanBean> goodsList; 
	private FragmentPage2TabStoreAdapter mAdapter;
	private SellEndDialog dialog;
	
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_page2tab3_store, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		refrenshView=(MaterialRefreshLayout) view.findViewById(R.id.fragment_refresh);
		mList=(ListView) view.findViewById(R.id.lv_goods_list);
		TextView textView=new TextView(activity);
		textView.setHeight(0);
		mList.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mList.addFooterView(textView, null, false);
		mList.addHeaderView(textView, null, false);
		
		mAdapter = new FragmentPage2TabStoreAdapter(activity, screenWidht);
		mList.setAdapter(mAdapter);
		goodsList = new ArrayList<TaocanBean>();
		
		refrenshView.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				if(Util.checkNetWork(activity)){
					dataPage = 1;
					getGoodsList(true);
				}else{
					new Handler().postDelayed(new Runnable() {
						public void run() {
							refrenshView.finishRefresh();
							if(goodsList.size() <= 0)
								refrenshView.addListViewState(mList, Constant.NO_NET);
							else
								Util.show(activity, "网路连接错误");
						}
					}, 500);
				}
			}
		});
		
		refrenshView.setOnClickRequestListener(new OnClickRequestListener() {
			
			@Override
			public void setRequest() {
				refrenshView.autoRefresh();
			}
		});
		
		mList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// 判断是否加载
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (dataPage < totalPage) {
						if (Util.checkNetWork(activity)) {
							dataPage += 1;
							getGoodsList(false);
						}else{
							Util.show(activity, "网路连接错误");
						}
					}
				}
			}
		});
		
		mList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int index=position - mList.getHeaderViewsCount();
				if(index >= 0){
					TaocanBean bean = goodsList.get(index);
					if("1".equals(bean.getTc_status())){
						AppManager.getAppManager().finishActivity(GoodsDetailActivity.class);
						activity.startActivity(new Intent(activity, GoodsDetailActivity.class)
						.putExtra("goods_id", bean.getTcid()).putExtra("kefu", bean.getKefu())
						.putExtra("fromshop", true));
					}
					else{
						if(dialog == null)
							dialog = new SellEndDialog(activity);
						dialog.show();
					}
				}
			}
			
		});
		
		isPrepared = true;
		lazyLoad();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Service_Store");
		MobclickAgent.onResume(activity); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Service_Store"); 
		MobclickAgent.onPause(activity);
	}
	
	/**
	 * 获取数据
	 */
	private void getGoodsList(final boolean isRefrensh){
		TaocanMsgParamClass mParam=new TaocanMsgParamClass();
		if (PreManager.instance().getIsLogin(activity)) 
			mParam.my_int_id=PreManager.instance().getUserId(activity);
		
		mParam.page=String.valueOf(dataPage);
		mParam.pagesize= String.valueOf(20);
		new XiangchengMallProcClass(activity).taocanList(mParam, new InterfaceTaocanListCallback(){

			@Override
			public void onSuccess(int icode, List<TaocanBean> tc_list, int totalpage) {
				if (isRefrensh) {
					refrenshView.finishRefresh();
				}
				doGoodsListData(isRefrensh, tc_list, totalpage);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				if(isRefrensh){
					refrenshView.finishRefresh();
				}
				if(goodsList != null && goodsList.size() < 0){
					refrenshView.addListViewState(mList, Constant.NO_DATA);
				}
			}
			
		});
	}
	
	private void doGoodsListData(boolean isRefrensh, List<TaocanBean> tc_list, int totalpage){
		this.totalPage=totalpage;
		if(tc_list != null){
			if(isRefrensh)
				this.goodsList = tc_list;
			else{
				this.goodsList.addAll(tc_list);
			}
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mAdapter.setData(goodsList);
				if(dataPage < totalPage )
					refrenshView.addListViewState(mList, Constant.LOADING);
				else
					refrenshView.addListViewState(mList, Constant.NO_MORE);
			}
		}, 200);
		
		if(goodsList.size() <= 0){
			refrenshView.addListViewState(mList, Constant.NO_DATA);
		}
	}
	
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
						refrenshView.autoRefresh();
					} else {
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								refrenshView.finishRefresh();
								refrenshView.addListViewState(mList,Constant.NO_NET);
							}
						}, 300);
					}
				}
			}, 100);
		}

	}

}
