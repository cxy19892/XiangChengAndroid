package com.yzm.sleep.activity.shop;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
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

public class ShopOrderListActivity extends BaseActivity implements PopDialogClickListener, OnItemClickListener{
	private Button back;
	private TextView title;
	private Button rightbtn;
	private ListView listV;
	private MaterialRefreshLayout pullToRefreshView;
	private int myDataPage = 1;
	private int TotalPage;
	private List<ShopOrderBean> orderList = new ArrayList<ShopOrderBean>();
	private OrderListAdapter mAdapter;
	private BottomPopDialog popDialog;
	private String telnumber="";
	private View noDataView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_order_list);
		initView();
//		initData();//没数据的时候自己添加数据
		pullToRefreshView.autoRefresh();//正式使用的时候网上取数据
	}

	private void initView() {
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		rightbtn = (Button) findViewById(R.id.btn_title_right2);
		listV = (ListView) findViewById(R.id.order_list);
		pullToRefreshView=(MaterialRefreshLayout) findViewById(R.id.pull_refreshview_order);
		back.setOnClickListener(this);
		rightbtn.setOnClickListener(this);
		title.setText("我的订单");
		rightbtn.setVisibility(View.VISIBLE);
		rightbtn.setText("客服");
		mAdapter = new OrderListAdapter(this, getScreenWidth());
		
		noDataView = LayoutInflater.from(ShopOrderListActivity.this).inflate(
				R.layout.no_comment_data_layout, null);
		noDataView.findViewById(R.id.no_data_view).setVisibility(View.GONE);
		((TextView) noDataView.findViewById(R.id.no_data_txt)).setText("暂无订单记录");
//		noDataView.setVisibility(View.GONE);
		listV.addFooterView(noDataView, null, false);
		
		removeFooter();
		
		listV.setOnItemClickListener(this);
		
		pullToRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				myDataPage = 1;
				if (checkNetWork(ShopOrderListActivity.this)) {
					getMyOrderListData(myDataPage, true);
				} else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
//							listV.removeFooterView(noDataView);
//							noDataView.setVisibility(View.GONE);
							pullToRefreshView.finishRefresh();
							Util.show(ShopOrderListActivity.this, "请检查您的网络");
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
	            	if (checkNetWork(ShopOrderListActivity.this)) {
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
	
	
	private void getMyOrderListData(int page, final boolean isRefresh){
		MyOrderListParamClass mparam = new MyOrderListParamClass();
		mparam.my_int_id = PreManager.instance().getUserId(ShopOrderListActivity.this);
		mparam.page = page+"";
		mparam.pagesize = "10";
		new XiangchengMallProcClass(this).myOrderList(mparam, new InterfaceMyOrderListCallback() {
			
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
//						removeFooter();
						pullToRefreshView
								.addListViewState(listV, Constant.LOADING);
					}
				}else{
					listV.addFooterView(noDataView, null, false);
//					noDataView.setVisibility(View.VISIBLE);
				}
					telnumber = kefu1;
				
				mAdapter.SetDate(orderList);
				pullToRefreshView.finishRefresh();
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
//				listV.removeFooterView(noDataView);
//				noDataView.setVisibility(View.GONE);
				Util.show(ShopOrderListActivity.this, strErrMsg);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right2:
			if(!TextUtils.isEmpty(telnumber)){
				if(popDialog == null){
					popDialog = new BottomPopDialog(ShopOrderListActivity.this, this);
				}
				popDialog.show();
				popDialog.setShowViews(1, "确定拨打客服电话\n"+telnumber, "");
			}else{
				toastMsg("获取服务电话失败");
			}
			break;
		default:
			break;
		}
		super.onClick(v);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppManager.getAppManager().finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void PopDialogClick(int clickid) {
		if(1 == clickid){
			toastMsg("拨打客服电话");
			if(StringUtil.isNumber(telnumber.replace("-", ""))){
				 try {
						Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telnumber));  
						 startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
			}else{
				Util.show(ShopOrderListActivity.this, "无法识别号码");
			}
		}
		if(isValidContext(ShopOrderListActivity.this)){
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
			Intent intent2Detail = new Intent(ShopOrderListActivity.this, PackageDetailsActivity.class);
			intent2Detail.putExtra("shopid", selsectShopOrderBean.getShopid());
			intent2Detail.putExtra("flag", selsectShopOrderBean.getFlag());
			startActivity(intent2Detail);
		}else{
			Util.show(ShopOrderListActivity.this, "该商品已下架");
		}
	}

}
