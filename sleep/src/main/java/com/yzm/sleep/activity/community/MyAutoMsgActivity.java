package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.yzm.sleep.adapter.MyAutoMsgAdapter;
import com.yzm.sleep.bean.AutoMsgBean;
import com.yzm.sleep.model.TopicesOperateDialog;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.ClearAutoListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetAutoListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceClearAutoLisCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetAutoLisCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;

/**
 * 我的消息列表
 * @author tianxun
 */
public class MyAutoMsgActivity extends BaseActivity {
	
	private ListView messageListView;
	private MaterialRefreshLayout pullToRefreshView;
	
	private MyAutoMsgAdapter adapter;
	private int dataPage = 1, totalPage = 10;
	private List<AutoMsgBean> messages;
	private TopicesOperateDialog opDialog;
	private String login_user_id,page_id="0";
	private ProgressUtils pro;
	private View viewNotData;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_my_message);   
		login_user_id=PreManager.instance().getUserId(this);
		messages=new ArrayList<AutoMsgBean>(); 
		initTitle();
		initView();
	}
	
	private void initTitle(){
		viewNotData = LayoutInflater.from(MyAutoMsgActivity.this).inflate(R.layout.layout_found_notdata, null);
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("消息");
		Button right=(Button) findViewById(R.id.btn_title_right);
		right.setText("清空");
		right.setVisibility(View.VISIBLE);
		right.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		right.setOnClickListener(this);
	}
	
	private void initView(){
		messageListView=(ListView) findViewById(R.id.lv_mymessages);
		messageListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		pullToRefreshView=(MaterialRefreshLayout) findViewById(R.id.pull_refreshview_message);
		messageListView.setFooterDividersEnabled(false);
		TextView h=new TextView(this);
		h.setHeight(0);
		messageListView.addHeaderView(h,null,false);
		adapter=new MyAutoMsgAdapter(this,messages);
		messageListView.setAdapter(adapter);
		messageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				int realPosition = position - messageListView.getHeaderViewsCount();
				if(realPosition >= 0){
					Intent intent=new Intent(MyAutoMsgActivity.this,CommunityTopiceDetailActivity.class);
					intent.putExtra("topices_id", messages.get(realPosition).getTid());
					intent.putExtra("topices_title;", messages.get(realPosition).getT_subject());
					intent.putExtra("is_choiceness", messages.get(realPosition).getIswenzhang().equals("1"));
					startActivity(intent);
				}
			}
		});
		
		pullToRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				// 下拉的时候数据重置
				if(checkNetWork(MyAutoMsgActivity.this) ){
					getMessages(true);
				}else{
					pullToRefreshView.finishRefresh();
					toastMsg("网络连接失败");
				}
			}
		});
		
		pullToRefreshView.setOnClickRequestListener(new OnClickRequestListener() {
			
			@Override
			public void setRequest() {
				pullToRefreshView.autoRefresh();
			}
		});
		
		messageListView.setOnScrollListener(new OnScrollListener() {
			@Override 
	        public void onScroll(AbsListView listView, int firstVisibleItem,int visibleItemCount, int totalItemCount) { 
	            int lastItem = firstVisibleItem + visibleItemCount; 
	            if(lastItem == totalItemCount) { 
	            	if(messageListView.getChildCount()>0){
	            		View lastItemView=(View) messageListView.getChildAt(messageListView.getChildCount()-1); 
		                if ((messageListView.getBottom())==lastItemView.getBottom()) { 
		                	if (dataPage < totalPage) {
		    					dataPage += 1;
		    					getMessages(false);
		    				} 
		                } 
	            	}
	            } 
	        } 
	   
	        @Override 
	        public void onScrollStateChanged(AbsListView listview, int scrollState) { 
	        	
	        } 
		});
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(Util.checkNetWork(MyAutoMsgActivity.this)){
					pullToRefreshView.autoRefresh();
					getMessages(true);
				}else{
					messageListView.removeFooterView(viewNotData);
					pullToRefreshView.addListViewState(messageListView,-1);
					pullToRefreshView.addListViewState(messageListView, Constant.NO_NET);
				}	
			}
		}, 100);
	}
	
	/**
	 * 获取数据
	 */
	private void getMessages(final boolean isRefresh){
		GetAutoListParamClass mParam=new GetAutoListParamClass();
		if(isRefresh){
			dataPage=1;
			page_id="0";
		}
		mParam.my_int_id=login_user_id;
		mParam.page=dataPage+"";
		mParam.pagesize="10";
		mParam.page_id=page_id;
		new CommunityProcClass(this).getAutoList(mParam, new InterfaceGetAutoLisCallBack() {
			
			@Override
			public void onSuccess(int icode, List<AutoMsgBean> list, int totalpage,String page_id) {
				messageListView.removeFooterView(viewNotData);
				if(isRefresh){
					pullToRefreshView.finishRefresh();
				}
				pullToRefreshView.addListViewState(messageListView,Constant.NO_MORE);
				doAnalysisCallBackData(icode,list,totalpage,page_id,isRefresh);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				if(isRefresh){
					pullToRefreshView.finishRefresh();
				}
				if(messages.size()<=0){
					pullToRefreshView.addListViewState(messageListView,-1);
					messageListView.removeFooterView(viewNotData);
					messageListView.addFooterView(viewNotData,null,false);
				}
			}
		});
	}

	private void doAnalysisCallBackData(int icode, List<AutoMsgBean> list, int totalpage,String page_id,boolean isRefresh){
		this.totalPage=totalpage;
		this.page_id=page_id;
		if(list!=null && list.size()>0){
			messageListView.setDividerHeight(1);
			if(isRefresh){
				messages=list;
			}else{
				messages.addAll(list);
			}
			messageListView.removeFooterView(viewNotData);
			pullToRefreshView.addListViewState(messageListView,-1);
			if(dataPage >=totalPage){
				pullToRefreshView.addListViewState(messageListView,Constant.NO_MORE);
			}else{
				pullToRefreshView.addListViewState(messageListView,Constant.LOADING);
			}
			adapter.setDatas(messages);
		}else{
		}
		if(isRefresh){
			if(messages.size()<=0){
				pullToRefreshView.addListViewState(messageListView,-1);
				messageListView.removeFooterView(viewNotData);
				messageListView.addFooterView(viewNotData,null,false);
			}
		}
		
	}
	
	/**
	 * 清空
	 */
	private void deleteAll(){
		ClearAutoListParamClass mParam=new ClearAutoListParamClass();
		mParam.my_int_id=login_user_id;
		new CommunityProcClass(this).clearAutoList(mParam, new InterfaceClearAutoLisCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				cancelPro();
				messages.clear();
				adapter.setDatas(messages);
				pullToRefreshView.addListViewState(messageListView,-1);
				pullToRefreshView.addListViewState(messageListView, Constant.NO_DATA);
				messageListView.setDividerHeight(0);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				cancelPro();
				toastMsg("清除失败");
			}
		});
	}
	
	/**
	 * 显示进度
	 */
	private void showPro(){
		if(pro==null){
			pro = new ProgressUtils(this);
		}
		pro.show();
	}
	
	/**
	 * 取消进度
	 */
	private void cancelPro(){
		if(pro!=null){
			pro.dismiss();
			pro=null;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			if(messages.size()>0){
				opDialog=TopicesOperateDialog.getInstance(this, false,"清空全部", this);
			}else{
				toastMsg("你还没有消息");
			}
			break;
		case R.id.btn_report:
			showPro();
			deleteAll();
			break;
		case R.id.btn_cancel:
			break;
		default:
			break;
		}
		if(opDialog!=null && v.getId()!=R.id.btn_title_right){
			opDialog.dismiss();
			opDialog=null;
		}
	}
}
