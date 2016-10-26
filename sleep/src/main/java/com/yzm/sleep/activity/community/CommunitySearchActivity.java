package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.PersonalInfoActivity;
import com.yzm.sleep.adapter.GroupListAdapter;
import com.yzm.sleep.adapter.SearchHistoryAdapter;
import com.yzm.sleep.adapter.TopicListAdapter;
import com.yzm.sleep.adapter.UserListAdapter;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.bean.SearchTopicBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSearchGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSearchGroupTopicCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSearchTopicCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.SearchGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SearchGroupTopicParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SearchTopicParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.GetUserIdParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetUserIdByNicknameCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.SearchedUserNicknameClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.CustomListView;

public class CommunitySearchActivity extends BaseActivity {

	private AutoCompleteTextView serchEdit;
	String serchKey;
	private Button commitBtn;
	private ProgressBar commitpro;
	private TextView tvGroup, tvTopic, tvUser;
	private CustomListView lvGroup, /*lvTopic,*/ lvUser;
	private String keyWd = "";
	private GroupListAdapter mGroupAdapter;
//	private TopicListAdapter mTopicAdapter;
	private UserListAdapter  mUserAdapter;
	List<SearchTopicBean> mTopiclist;
	private View listFooter1, /*listFooter2, */listFooter3, historyFooter;
	private ListView moreResonListv;
	private MaterialRefreshLayout pullToRefreshView;
	private LayoutInflater minflater;
	private int PAGESIZE_20 = 20;
	private List<CommunityGroupBean> mGroupList;
	private List<SearchedUserNicknameClass> mUserlist = new ArrayList<SearchedUserNicknameClass>();
	private int myDataPage = 1;
	private boolean isShowTwoList = true;
	private int SHOW_MORE_DATA_TYPE = 1; 
	private int TotalPage;
	private ArrayAdapter<String> arrayAdapter;
	private String mPageId = "0";
	
	private CustomListView lvHistory;
	private SearchHistoryAdapter mHistoryAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_community_search);
		minflater = LayoutInflater.from(this);
		initViews();
		initEditText();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SearchTopic"); 
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SearchTopic");
		MobclickAgent.onPause(this);
	}

	private void initViews() {
		
		serchEdit = (AutoCompleteTextView) findViewById(R.id.serch_edit);
		findViewById(R.id.back).setOnClickListener(this);
		commitBtn = (Button) findViewById(R.id.serch_commit);
		commitpro = (ProgressBar) findViewById(R.id.serch_Progress);
		tvGroup = (TextView) findViewById(R.id.tv_group);
		tvTopic = (TextView) findViewById(R.id.tv_topic);
		tvUser  = (TextView) findViewById(R.id.tv_user);
		lvGroup = (CustomListView) findViewById(R.id.listv_group);
//		lvTopic = (CustomListView) findViewById(R.id.listv_topic);
		lvUser  = (CustomListView) findViewById(R.id.listv_user);
		lvHistory   = (CustomListView) findViewById(R.id.listv_search_history); 
		
		listFooter1 = minflater.inflate(R.layout.layout_listview_footer, null);
//		listFooter2 = minflater.inflate(R.layout.layout_listview_footer, null);
		listFooter3 = minflater.inflate(R.layout.layout_listview_footer, null);
		historyFooter = minflater.inflate(R.layout.layout_clear_footer, null);
		pullToRefreshView=(MaterialRefreshLayout) findViewById(R.id.pull_refreshview_search);
		moreResonListv = (ListView) findViewById(R.id.lv_serch_listv);
//		lvTopic.addFooterView(listFooter2, null, false);
		lvGroup.addFooterView(listFooter1, null, false);
		lvUser.addFooterView(listFooter3, null, false);
		lvHistory.addFooterView(historyFooter, null, false);
		
		mGroupAdapter = new GroupListAdapter(this);
//		mTopicAdapter = new TopicListAdapter(this);
		mUserAdapter  = new UserListAdapter(this);
		moreResonListv.setVisibility(View.GONE);
		commitBtn.setOnClickListener(this);
		mHistoryAdapter = new SearchHistoryAdapter(this);
		
		lvHistory.setAdapter(mHistoryAdapter);
		initSearchHistoryView();
		
		removeFooter();
		pullToRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				myDataPage = 1;
				mPageId = "0";
				if (checkNetWork(CommunitySearchActivity.this)) {
					
					pullToRefreshView.autoRefresh();
					if(SHOW_MORE_DATA_TYPE == 1){
    					serchGroupInfoList(PAGESIZE_20, myDataPage+"", keyWd, true);
    				}/*else if(2 == SHOW_MORE_DATA_TYPE){
    					serchTopicInfoList(PAGESIZE_20, myDataPage+"", keyWd, true);
    				}*/else{
    					UserSerarch(PAGESIZE_20, myDataPage+"", keyWd, true);
    				}
				} else {
					mHandler.postDelayed(new Runnable() {
						public void run() {
							pullToRefreshView.finishRefresh();
							mGroupList.clear();
		            		mTopiclist.clear();
		            		mUserlist.clear();
		            		if(SHOW_MORE_DATA_TYPE == 1){
		            			mGroupAdapter.setData(mGroupList);
		            		}/*else if(2==SHOW_MORE_DATA_TYPE){
		            			mTopicAdapter.setData(mTopiclist);
		            		}*/else{
		            			mUserAdapter.setData(mUserlist);
		            		}
		            		removeFooter();
		        			pullToRefreshView
		        					.addListViewState(moreResonListv, Constant.NO_NET);
						}
					}, 300);
					
				}
				
			}
		});
		pullToRefreshView.setOnClickRequestListener(new OnClickRequestListener() {
			
			@Override
			public void setRequest() {
				pullToRefreshView.autoRefresh();
			}
		});
		
		moreResonListv.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					break;
				default:
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount; 
	            if(lastItem == totalItemCount) { 
	            	if(moreResonListv.getChildCount()>0){
	            		View lastItemView=(View) moreResonListv.getChildAt(moreResonListv.getChildCount()-1); 
		                if ((moreResonListv.getBottom())==lastItemView.getBottom()) { 
		                	if (myDataPage < TotalPage) {
		                		myDataPage += 1;
		    					
		                		if(SHOW_MORE_DATA_TYPE == 1){
		        					serchGroupInfoList(PAGESIZE_20, myDataPage+"", keyWd, false);
		        				}/*else if(2 == SHOW_MORE_DATA_TYPE){
		        					serchTopicInfoList(PAGESIZE_20, myDataPage+"", keyWd, false);
		        				}*/else{
		        					UserSerarch(PAGESIZE_20, myDataPage+"", keyWd, false);
		        				}
		    				}
		                } 
	            	}
	            }
				
			}
		});
		
		
		moreResonListv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 int index = arg2 - moreResonListv.getHeaderViewsCount();
				 if(index >= 0){
					 if(SHOW_MORE_DATA_TYPE==1){
						 Intent intentToGroup = new Intent(CommunitySearchActivity.this, TeamDetailsActivity.class);
						 CommunityGroupBean mCommunityGroupBean = mGroupList.get(index);
						 mCommunityGroupBean.setIscode(true);
						 intentToGroup.putExtra("bean", mCommunityGroupBean);


						 startActivity(intentToGroup);
					 }else if(SHOW_MORE_DATA_TYPE == 2){
						 Intent intentToTopic = new Intent(CommunitySearchActivity.this, CommunityTopiceDetailActivity.class);
						 intentToTopic.putExtra("topices_id", mTopiclist.get(index).getTid());
						 intentToTopic.putExtra("g_type", mTopiclist.get(index).getG_type());
						 startActivity(intentToTopic);
					 }else{
						 SearchedUserNicknameClass friend = mUserlist.get(index);
						 Intent intent = new Intent(CommunitySearchActivity.this, PersonalInfoActivity.class);//跳转到个人主页界面
						 intent.putExtra("user_id", friend.uid);
						 startActivity(intent);
					 }
				 }
				
			}
		});
		
		serchEdit.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){  

					InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  

					if(imm.isActive()){  

						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
						if(checkNetWork(CommunitySearchActivity.this)){
							serchKey = serchEdit.getText().toString();
							if(!TextUtils.isEmpty(serchKey)){
								SHOW_MORE_DATA_TYPE = 0;
//								keyWd = Util.StringFilter(serchKey);
								keyWd = serchKey;
								isShowTwoList = true;
								mPageId = "0";
								initViewFragment(keyWd);
							}else{
								toastMsg("请填入搜索内容");
							}
						}else{
							toastMsg("请检查您的网络");
						}
					}  

					return true;  

				} 
				return false;
			}

		});
		
		historyFooter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearSearchHistory();
			}
		});
	}
	
	
	private void removeFooter(){
		pullToRefreshView.addListViewState(moreResonListv, -1);
	}
	
	private void initEditText(){
		serchEdit.requestFocus();
		serchEdit.setDropDownHeight(getScreenHeight()/3);
		//显示剩余输入量
		serchEdit.addTextChangedListener(new Watcher());
		serchEdit.setDropDownBackgroundResource(R.color.cbg_color);//drawable.white);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.serch_commit://搜索按钮
			if(checkNetWork(CommunitySearchActivity.this)){
				serchKey = serchEdit.getText().toString();
				if(!TextUtils.isEmpty(serchKey)){
					SHOW_MORE_DATA_TYPE = 0;
//					keyWd = Util.StringFilter(serchKey);
					keyWd = serchKey;
					isShowTwoList = true;
					mPageId = "0";
					initViewFragment(keyWd);
				}else{
					toastMsg("请填入搜索内容");
				}
			}else{
				toastMsg("请检查您的网络");
			}
			
			break;
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		default:
			break;
		}
	}
	
	
	private void initViewFragment(String keyWd) {
		
		removeFooter();
		moreResonListv.setVisibility(View.GONE);
		if(null != keyWd){
			commitpro.setVisibility(View.VISIBLE);
			commitBtn.setVisibility(View.GONE);
			myDataPage = 1;
			serchGroupAndTopicList(keyWd);
			UserSerarch(5, myDataPage+"", keyWd, true);
			addSearchWDTolist(keyWd);
			addSearchWDToHistory(keyWd);
			lvHistory.setVisibility(View.GONE);
		}
		listFooter1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SHOW_MORE_DATA_TYPE = 1;
				getMoreData(1);
				
			}
		});
		/*listFooter2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SHOW_MORE_DATA_TYPE = 2;
				getMoreData(2);
			}
		});*/
		listFooter3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SHOW_MORE_DATA_TYPE = 3;
				getMoreData(3);
			}
		});
	}
	
	private void getMoreData(int dataType){
		isShowTwoList = false;
		mGroupList.clear();
		mTopiclist.clear();
		mUserlist.clear();
		tvGroup.setVisibility(View.GONE);
		tvTopic.setVisibility(View.GONE);
		tvUser.setVisibility(View.GONE);
		lvGroup.setVisibility(View.GONE);
//		lvTopic.setVisibility(View.GONE);
		lvUser.setVisibility(View.GONE);
		moreResonListv.setVisibility(View.VISIBLE);
		if(1 == dataType){
			moreResonListv.setAdapter(mGroupAdapter);
			serchGroupInfoList(PAGESIZE_20, myDataPage+"", keyWd, false);
			
		}/*else if(2== dataType){
			moreResonListv.setAdapter(mTopicAdapter);
			serchTopicInfoList(PAGESIZE_20, myDataPage+"", keyWd, false);
		}*/else{
			moreResonListv.setAdapter(mUserAdapter);
			UserSerarch(PAGESIZE_20, myDataPage+"", keyWd, false);
		}
	}


	class SearchItemClick implements OnItemClickListener{

		private int mParent;
		public SearchItemClick(int parent){
			mParent = parent;
		}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			isShowTwoList = false;
			if(mParent == 1){
				Intent intentToGroup = new Intent(CommunitySearchActivity.this, TeamDetailsActivity.class);
				
				CommunityGroupBean mCommunityGroupBean = mGroupList.get(arg2);
				mCommunityGroupBean.setIscode(true);
				intentToGroup.putExtra("bean", mCommunityGroupBean);
				startActivity(intentToGroup);
			}else if(mParent == 2){
				Intent intentToTopic = new Intent(CommunitySearchActivity.this, CommunityTopiceDetailActivity.class);
				intentToTopic.putExtra("topices_id", mTopiclist.get(arg2).getTid());
				intentToTopic.putExtra("g_type", mTopiclist.get(arg2).getG_type());
				startActivity(intentToTopic);
			}else{
				SearchedUserNicknameClass friend = mUserlist.get(arg2);
				Intent intent = new Intent(CommunitySearchActivity.this, PersonalInfoActivity.class);//跳转到个人主页界面
				intent.putExtra("user_id", friend.uid);
				startActivity(intent);
			}
			
		}
	}
	
	private void serchGroupAndTopicList(String key){
		SearchGroupTopicParamClass mParam = new SearchGroupTopicParamClass();
		mParam.xckey = key;
		mParam.my_int_id = (PreManager.instance().getUserId(CommunitySearchActivity.this).equals("") ? "0" : PreManager.instance().getUserId(CommunitySearchActivity.this));
		new CommunityProcClass(CommunitySearchActivity.this).searchGroupTopic(mParam, new InterfaceSearchGroupTopicCallBack() {
			
			@Override
			public void onSuccess(int icode, List<CommunityGroupBean> groupList,
					List<SearchTopicBean> topicList) {
				doSerchListsCallBackData(icode, groupList, topicList);;
				commitpro.setVisibility(View.GONE);
				commitBtn.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				commitpro.setVisibility(View.GONE);
				commitBtn.setVisibility(View.VISIBLE);
				mGroupList = new ArrayList<CommunityGroupBean>();
				mTopiclist = new ArrayList<SearchTopicBean>();
				mHandler.sendEmptyMessage(0);
			}
		});
	}
	private void doSerchListsCallBackData(int icode, List<CommunityGroupBean> groupList,
			List<SearchTopicBean> topicList){
		
		mGroupList = groupList;
		mTopiclist = topicList;
		mHandler.sendEmptyMessage(0);
	}
	
	private void serchGroupInfoList(int pageSize , String page , String key, final boolean isrefresh){
		SearchGroupParamClass mParam = new SearchGroupParamClass();
		mParam.page = page;
		mParam.pagesize = pageSize+"";
		mParam.xckey = key;
		mParam.page_id = mPageId;
		mParam.my_int_id = (PreManager.instance().getUserId(CommunitySearchActivity.this).equals("") ? "0" : PreManager.instance().getUserId(CommunitySearchActivity.this));
		new CommunityProcClass(CommunitySearchActivity.this).searchGroup(mParam, new InterfaceSearchGroupCallBack() {
			
			@Override
			public void onSuccess(int icode, List<CommunityGroupBean> list, int totalPage, String page_id) {
				mPageId = page_id;
				if(isrefresh)
					pullToRefreshView.finishRefresh();
				doSerchGroupCallBackData(icode, list, totalPage, isrefresh);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				pullToRefreshView.finishRefresh();
				if(mGroupList.size() <=0){
					removeFooter();
				}
			}
		});
	}
	
	private void doSerchGroupCallBackData(int icode, List<CommunityGroupBean> list, int totalPage, boolean isrefresh){
		this.TotalPage = totalPage;
		if(list != null && list.size()>0){
			if(isShowTwoList){
				mGroupList = list;
			}else{
				if(isrefresh){
					mGroupList = list;
				}else{
					mGroupList.addAll(list);
				}
			}
		}
		
		if (myDataPage >= TotalPage) {
			removeFooter();
			pullToRefreshView
					.addListViewState(moreResonListv, Constant.NO_MORE);
		} else {
			removeFooter();
			pullToRefreshView
					.addListViewState(moreResonListv, Constant.LOADING);
		}
		
		mGroupAdapter.setData(mGroupList);
	}
	
	
	/*private void serchTopicInfoList(int pageSize , String page , String key, final boolean isrefresh){
		SearchTopicParamClass mParam = new SearchTopicParamClass();
		mParam.page = page;
		mParam.pagesize = pageSize+"";
		mParam.xckey = key;
		mParam.page_id = mPageId;
		new CommunityProcClass(CommunitySearchActivity.this).searchTopic(mParam, new InterfaceSearchTopicCallBack() {
			
			@Override
			public void onSuccess(int icode, List<SearchTopicBean> list, int totalPage,String page_id) {
				// TODO Auto-generated method stub
				mPageId = page_id;
				if(isrefresh)
				pullToRefreshView.finishRefresh();
				doSerchTopicCallBackData(icode, list, totalPage, isrefresh);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				pullToRefreshView.finishRefresh();
				if(mTopiclist.size() <=0){
					removeFooter();
				}
			}
		});
	}
	
	private void doSerchTopicCallBackData(int icode, List<SearchTopicBean> list, int totalPage, boolean isrefresh){
		this.TotalPage = totalPage;
		if(list != null && list.size()>0){
			if(isShowTwoList){
				mTopiclist = list;
			}else{
				if(isrefresh){
					mTopiclist = list;
				}else{
					mTopiclist.addAll(list);
				}
			}
		}
		
		if (myDataPage >= TotalPage) {
			removeFooter();
			pullToRefreshView
					.addListViewState(moreResonListv, Constant.NO_MORE);
		} else {
			removeFooter();
			pullToRefreshView
					.addListViewState(moreResonListv, Constant.LOADING);
		}
		
		mTopicAdapter.setData(mTopiclist);
	}*/
	
	
	/**
	 * 搜索好友--调用user_search.php接口
	 * @param search_key_word
	 * 搜索关键词
	 * @param my_int_id
	 * 用户本人的内部ID号
	 */
	private void UserSerarch(int pageSize , String page , String key, final boolean isrefresh){
		GetUserIdParamClass getUserIdParamClass = new GetUserIdParamClass();
		getUserIdParamClass.key = key;
		getUserIdParamClass.page= page;
		getUserIdParamClass.pagesize = String.valueOf(pageSize);
		new XiangchengProcClass(this).GetUserIdByNickname(getUserIdParamClass, new InterfaceGetUserIdByNicknameCallback() {
			
			@Override
			public void onSuccess(int icode, int totalPage,
					List<SearchedUserNicknameClass> userList) {
				pullToRefreshView.finishRefresh();
				TotalPage = totalPage;
				listFooter3.setVisibility(View.GONE);
				if(isShowTwoList){
					if(userList.size()>5){
						for(int u = 0 ; u < 5 ; u++){
							mUserlist.add(userList.get(u));
						}
						listFooter3.setVisibility(View.VISIBLE);
					}else{
						mUserlist = userList;
						listFooter3.setVisibility(View.GONE);
					}
					mHandler.sendEmptyMessage(1);
				}else{
					if(isrefresh){
						mUserlist = userList;
					}else{
						mUserlist.addAll(userList);
					}
				if (myDataPage >= TotalPage) {
					removeFooter();
					pullToRefreshView
							.addListViewState(moreResonListv, Constant.NO_MORE);
				} else {
					removeFooter();
					pullToRefreshView
							.addListViewState(moreResonListv, Constant.LOADING);
				}
				mUserAdapter.setData(mUserlist);
				}
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				pullToRefreshView.finishRefresh();
				mUserlist.clear();
				if(mUserlist.size() <=0){
					removeFooter();
				}
				mHandler.sendEmptyMessage(1);
				
			}
		});
		/*SearchNickNameParamClass searchNickNameParamClass = new SearchNickNameParamClass();
		searchNickNameParamClass.my_int_id = (PreManager.instance().getUserId(CommunitySearchActivity.this).equals("") ? "0" : PreManager.instance().getUserId(CommunitySearchActivity.this));
		searchNickNameParamClass.search_key_word = key;
		new UserManagerProcClass(this).SearchByNickName(searchNickNameParamClass, new InterfaceSearchNickNameCallBack() {
			

			@Override
			public void onSuccess(int iCode, List<OwerSearchedUserRstListClass> m_list) {
				pullToRefreshView.setRefreshing(false);
				mUserlist.clear();
				listFooter3.setVisibility(View.GONE);
				if(isShowTwoList){
					if(m_list.size()>5){
						for(int u = 0 ; u < 5 ; u++){
							mUserlist.add(m_list.get(u));
						}
						listFooter3.setVisibility(View.VISIBLE);
					}else{
						mUserlist = m_list;
						listFooter3.setVisibility(View.GONE);
					}
					mHandler.sendEmptyMessage(1);
				}else{
				mUserlist = m_list;
				removeFooter();
				pullToRefreshView
						.addListViewState(moreResonListv, Constant.NO_MORE);
				}
				mUserAdapter.setData(mUserlist);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				pullToRefreshView.setRefreshing(false);
				mUserlist.clear();
				if(mUserlist.size() <=0){
					removeFooter();
				}
				mHandler.sendEmptyMessage(1);
			}
		});*/
	}
	
	
	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
			if(!CommunitySearchActivity.this.isFinishing()){
				
				if(mGroupList.size() > 0){
					tvGroup.setVisibility(View.VISIBLE);
					lvGroup.setVisibility(View.VISIBLE);
//					lvGroup.removeFooterView(listFooter1);
					listFooter1.setVisibility(View.GONE);
					mGroupAdapter = new GroupListAdapter(CommunitySearchActivity.this);
					lvGroup.setAdapter(mGroupAdapter);
					tvGroup.setText(Html.fromHtml("相关"+(keyWd.length()>10?keyWd.substring(0, 10)+"...":keyWd)+"小组"));//"相关<font color=#FFA500>"+(keyWd.length()>10?keyWd.substring(0, 10)+"...":keyWd)+"</font>的小组"
					lvGroup.setOnItemClickListener(new SearchItemClick(1));
//					lvGroup.addFooterView(listFooter1, null, false);
					listFooter1.setVisibility(View.VISIBLE);
					
					mGroupAdapter.setData(mGroupList);
				}else{
					tvGroup.setVisibility(View.GONE);
					lvGroup.setVisibility(View.GONE);
				}
				/*if(mTopiclist.size() > 0){
					mTopicAdapter.setData(mTopiclist);
					tvTopic.setVisibility(View.VISIBLE);
					lvTopic.setVisibility(View.VISIBLE);
//					lvTopic.removeFooterView(listFooter2);
					listFooter2.setVisibility(View.GONE);
					mTopicAdapter = new TopicListAdapter(CommunitySearchActivity.this);
					lvTopic.setAdapter(mTopicAdapter);
					tvTopic.setText(Html.fromHtml("有关"+(keyWd.length()>10?keyWd.substring(0, 10)+"...":keyWd)+"话题"));
					lvTopic.setOnItemClickListener(new SearchItemClick(2));
//					lvTopic.addFooterView(listFooter2, null, false);
					listFooter2.setVisibility(View.VISIBLE);
					mTopicAdapter.setData(mTopiclist);
				}else{
					tvTopic.setVisibility(View.GONE);
					lvTopic.setVisibility(View.GONE);
				}*/
				
				
				if(mGroupList.size() == 0 && mTopiclist.size() == 0 && (mUserlist !=null && mUserlist.size() == 0)){
					tvGroup.setVisibility(View.VISIBLE);
					tvGroup.setText(Html.fromHtml("没有发现相关"+(keyWd.length()>10?keyWd.substring(0, 10)+"...":keyWd)+"的信息"));
				}
			}
			}else{
				if(mUserlist.size() > 0){
					tvUser.setVisibility(View.VISIBLE);
					lvUser.setVisibility(View.VISIBLE);
					listFooter3.setVisibility(View.GONE);
					mUserAdapter = new UserListAdapter(CommunitySearchActivity.this);
					lvUser.setAdapter(mUserAdapter);
					tvUser.setText(Html.fromHtml("有关"+(keyWd.length()>10?keyWd.substring(0, 10)+"...":keyWd)+"用户"));
					lvUser.setOnItemClickListener(new SearchItemClick(3));
					listFooter3.setVisibility(View.VISIBLE);
					mUserAdapter.setData(mUserlist);
					if(mGroupList !=null && mTopiclist !=null && mGroupList.size() == 0 && mTopiclist.size() == 0){
						tvGroup.setVisibility(View.GONE);
					}
				}else{
					if(mGroupList !=null && mTopiclist !=null && mGroupList.size() == 0 && mTopiclist.size() == 0){
						tvGroup.setVisibility(View.VISIBLE);
						tvGroup.setText(Html.fromHtml("没有发现相关"+(keyWd.length()>10?keyWd.substring(0, 10)+"...":keyWd)+"的信息"));
					}
					tvUser.setVisibility(View.GONE);
					lvUser.setVisibility(View.GONE);
				}
			}
		}
		
	}; 
	/**
	 * 监听输入字符 用于限定字数
	 * @author Administrator
	 *
	 */
	class Watcher implements TextWatcher{
		/**EditText内容发生变化，才让他执行setText函数，如果没有发生变化，就不执行监听器里的setText函数    不然会导致内存溢出*/
        private boolean isChanged = false;
		
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
            // TODO Auto-generated method stub
            if(isChanged){
                return ;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        	arrayAdapter = new ArrayAdapter<String>(CommunitySearchActivity.this, R.layout.item_textview_serch,Constant.SEARCHWD == null?Constant.SEARCHWD = new ArrayList<String>():Constant.SEARCHWD);
    		serchEdit.setAdapter(arrayAdapter);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if(isChanged){
                return ;
            }
            isChanged = true;
            int number = s.length();
            if(number == 0){
            	tvGroup.setVisibility(View.GONE);
        		tvTopic.setVisibility(View.GONE);
        		tvUser.setVisibility(View.GONE);
        		lvGroup.setVisibility(View.GONE);
//        		lvTopic.setVisibility(View.GONE);
        		lvUser.setVisibility(View.GONE);
        		moreResonListv.setVisibility(View.GONE);
        		if(mGroupList != null){
        			mGroupList.clear();
        		}
        		if(mTopiclist != null)
        		mTopiclist.clear();
        		if(mUserlist != null)
        			mUserlist.clear();
        		
        		initSearchHistoryView();
        		keyWd = "";
            }
			isChanged = false;
        }
        
    }
	
	
	private void addSearchWDTolist(String keywd){
		if(null == Constant.SEARCHWD){
			Constant.SEARCHWD = new ArrayList<String>();
		}
		for(String str : Constant.SEARCHWD){
			if(str.equals(keywd)){
				return;
			}
		}
		Constant.SEARCHWD.add(keywd);
	}
	
	private void addSearchWDToHistory(String keywd){
		String keyS = PreManager.instance().getCommunitySearchKey(this);
		if(TextUtils.isEmpty(keyS)){
			PreManager.instance().saveCommunitySearchKey(this, keywd);
		}else{
			try {
				String[] keyarr = keyS.split(",");
				for (String string : keyarr) {
					if(string.endsWith(keywd)){
						return;
					}
				}
				PreManager.instance().saveCommunitySearchKey(this, keyS+","+keywd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//清除搜索历史
		private void clearSearchHistory() {
			PreManager.instance().saveCommunitySearchKey(this, "");
			initSearchHistoryView();
		}
		//获取搜索历史
		private List<String> getSearchHistory(){
			List<String> keylistList = new ArrayList<String>();
			String keyWD = PreManager.instance().getCommunitySearchKey(this);
			if(TextUtils.isEmpty(keyWD)){
				return keylistList;
			}else{
				try {
					String[] keylist = keyWD.split(",");
					if(keylist.length > 4){
						for (int i = keylist.length-1; i > (keylist.length -5) ; i--) {
							keylistList.add(keylist[i]);
						}
					}else{
						for (int i = keylist.length-1 ; i >= 0 ; i--) {
							keylistList.add(keylist[i]);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return keylistList;
			}
		}
		//
		private void initSearchHistoryView(){
			List<String> historyStrings = getSearchHistory();
			if(historyStrings.size() == 0){
				lvHistory.setVisibility(View.GONE);
			}else{
				lvHistory.setVisibility(View.VISIBLE);
				mHistoryAdapter.setData(historyStrings);
				lvHistory.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						keyWd = getSearchHistory().get(position);
						serchEdit.setText(keyWd);
						Editable etext = serchEdit.getText();
						Selection.setSelection(etext, etext.length());
						initViewFragment(keyWd);
					}
				});
			}
		}
}
