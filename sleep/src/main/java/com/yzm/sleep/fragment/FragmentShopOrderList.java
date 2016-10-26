package com.yzm.sleep.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.shop.PackageDetailsActivity;
import com.yzm.sleep.adapter.OrderListAdapter;
import com.yzm.sleep.bean.ShopOrderBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.BottomPopDialog;
import com.yzm.sleep.utils.BottomPopDialog.PopDialogClickListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceMyOrderListCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.MyOrderListParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

public class FragmentShopOrderList extends Fragment implements PopDialogClickListener, OnItemClickListener{
//	private Button back;
//	private TextView title;
//	private Button rightbtn;
	private ListView listV;
	private MaterialRefreshLayout pullToRefreshView;
	private int myDataPage = 1;
	private int TotalPage;
	private List<ShopOrderBean> orderList = new ArrayList<ShopOrderBean>();
	private OrderListAdapter mAdapter;
	private BottomPopDialog popDialog;
	private String telnumber="";
	private View noDataView;
	private Activity activity;
	private int screenWidht;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_shop_order_list, container, false);
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		getScreenWith();
		initView(view);
		pullToRefreshView.autoRefresh();//正式使用的时候网上取数据
	}
	
		private void initView(View view) {
			listV = (ListView) view.findViewById(R.id.order_list);
			pullToRefreshView=(MaterialRefreshLayout) view.findViewById(R.id.pull_refreshview_order);
			mAdapter = new OrderListAdapter(activity, screenWidht);
			
			noDataView = LayoutInflater.from(activity).inflate(
					R.layout.no_comment_data_layout, null);
			noDataView.findViewById(R.id.no_data_view).setVisibility(View.GONE);
			((TextView) noDataView.findViewById(R.id.no_data_txt)).setText("暂无订单记录");
//			noDataView.setVisibility(View.GONE);
			listV.addFooterView(noDataView, null, false);
			
			removeFooter();
			
			listV.setOnItemClickListener(this);
			
			pullToRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
				
				@Override
				public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
					myDataPage = 1;
					if (Util.checkNetWork(activity)) {
						getMyOrderListData(myDataPage, true);
					} else {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
//								listV.removeFooterView(noDataView);
//								noDataView.setVisibility(View.GONE);
								pullToRefreshView.finishRefresh();
								Util.show(activity, "请检查您的网络");
			            		removeFooter();
								if(orderList.size()==0){
									pullToRefreshView
		        					.addListViewState(listV, Constant.NO_NET);
								}
							}
						}, 300);
					}
				}
			});
			
			listV.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					
					
				}
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					int lastItem = firstVisibleItem + visibleItemCount; 
		            if(lastItem == totalItemCount) { 
		            	if (Util.checkNetWork(activity)) {
		            		if(listV.getChildCount()>0){
		            			View lastItemView=(View) listV.getChildAt(listV.getChildCount()-1); 
		            			if ((listV.getBottom())==lastItemView.getBottom()) { 
		            				if (myDataPage < TotalPage) {
		            					myDataPage += 1;
		            					getMyOrderListData(myDataPage, false);
		            				}
		            			} 
		            		}
		            	}
		            }
				}
			});
			
			pullToRefreshView.setOnClickRequestListener(new OnClickRequestListener() {
				
				@Override
				public void setRequest() {
					pullToRefreshView.autoRefresh();
					getMyOrderListData(myDataPage, true);
				}
			});
			
			listV.setAdapter(mAdapter);
		}
		
		private void getScreenWith(){
			InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE); 
			WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			screenWidht = dm.widthPixels;
		}
		
		
		private void getMyOrderListData(int page, final boolean isRefresh){
			MyOrderListParamClass mparam = new MyOrderListParamClass();
			mparam.my_int_id = PreManager.instance().getUserId(activity);
			mparam.page = page+"";
			mparam.pagesize = "10";
			new XiangchengMallProcClass(activity).myOrderList(mparam, new InterfaceMyOrderListCallback() {
				
				@Override
				public void onSuccess(int icode, List<ShopOrderBean> order_list,
						String kefu1, int totalpage) {
					removeFooter();
					TotalPage = totalpage;
					if(isRefresh){
						orderList = order_list;
					}else {
						orderList.addAll(order_list);
					}
					if(orderList.size()>0){
						if (myDataPage >= TotalPage) {
							
							pullToRefreshView
									.addListViewState(listV, Constant.NO_MORE);
						} else {
//							removeFooter();
							pullToRefreshView
									.addListViewState(listV, Constant.LOADING);
						}
					}else{
						listV.addFooterView(noDataView, null, false);
//						noDataView.setVisibility(View.VISIBLE);
					}
						telnumber = kefu1;
					
					mAdapter.SetDate(orderList);
					pullToRefreshView.finishRefresh();
				}
				
				@Override
				public void onError(int icode, String strErrMsg) {
//					listV.removeFooterView(noDataView);
//					noDataView.setVisibility(View.GONE);
					Util.show(activity, strErrMsg);
					removeFooter();
					pullToRefreshView.finishRefresh();
					mAdapter.SetDate(orderList);
				}
			});
		}
		
		
		private void removeFooter(){
			pullToRefreshView.addListViewState(listV, -1);
			try {
				listV.removeFooterView(noDataView);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.btn_title_right2:
//				if(!TextUtils.isEmpty(telnumber)){
//					if(popDialog == null){
//						popDialog = new BottomPopDialog(activity, this);
//					}
//					popDialog.show();
//					popDialog.setShowViews(1, "确定拨打客服电话\n"+telnumber, "");
//				}else{
//					Util.show(activity, "获取服务电话失败");
//				}
//				break;
//			default:
//				break;
//			}
//		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
		}


		@Override
		public void PopDialogClick(int clickid) {
			if(1 == clickid){
				Util.show(activity, "拨打客服电话");
				if(StringUtil.isNumber(telnumber.replace("-", ""))){
					 try {
							Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telnumber));  
							 startActivity(intent);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
				}else{
					Util.show(activity, "无法识别号码");
				}
			}
			if(isValidContext(activity)){
				if(popDialog!=null){
					popDialog.cancel();
				}
			}
		}
		
		/**
		 * 查询activity是否存在
		 * @param c
		 * @return
		 */
		@SuppressLint("NewApi") 
		private boolean isValidContext (Context c){  
	        
	        Activity a = (Activity)c;  
	          
	        if (a.isDestroyed() || a.isFinishing()){  
	            return false;  
	        }else{  
	            return true;  
	        }  
	    }

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ShopOrderBean selsectShopOrderBean = mAdapter.getData().get(position);
			if(selsectShopOrderBean.getFlag().equals("1")){
				Intent intent2Detail = new Intent(activity, PackageDetailsActivity.class);
				intent2Detail.putExtra("shopid", selsectShopOrderBean.getShopid());
				intent2Detail.putExtra("flag", selsectShopOrderBean.getFlag());
				startActivity(intent2Detail);
			}else{
				Util.show(activity, "该商品已下架");
			}
		}

}
