package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.adapter.Page3Tab1SecondAdapter;
import com.yzm.sleep.adapter.SearchHistoryAdapter;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.keywordBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceKnowledgeKeysCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceKnowledgeSearchCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.AutoNewLineLinearLayout;
import com.yzm.sleep.widget.CustomListView;

public class KnowledgeSearchActivity extends BaseActivity {

	
	private EditText serchEdit;
	String serchKey;
	private Button commitBtn;
	private ProgressBar commitpro;
	private TextView tvKnowledge;
	private CustomListView lvHistory;
	private String keyWd = "";
	private Page3Tab1SecondAdapter mAdapter;
	private SearchHistoryAdapter mHistoryAdapter;
	private View historyFooter;
	private ListView moreResonListv;
	private MaterialRefreshLayout pullToRefreshView;
	private LayoutInflater minflater;
	private int PAGESIZE_20 = 20;
	private int myDataPage = 1;
	private int TotalPage;
	private AutoNewLineLinearLayout SearchLayberLayout;
	private LinearLayout SearchLabelLin;
	private List<keywordBean> SearchLabels;
	private List<ArticleBean> dataList = new ArrayList<ArticleBean>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_kownledge_search);
		minflater = LayoutInflater.from(this);
		initViews();
		initEditText();
		getHostSearchLabel();
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
		
		serchEdit = (EditText) findViewById(R.id.serch_edit);
		findViewById(R.id.back).setOnClickListener(this);
		commitBtn = (Button) findViewById(R.id.serch_commit);
		commitpro = (ProgressBar) findViewById(R.id.serch_Progress);
		lvHistory   = (CustomListView) findViewById(R.id.listv_search_history); 
		tvKnowledge = (TextView) findViewById(R.id.tv_kownledge);
		historyFooter = minflater.inflate(R.layout.layout_clear_footer, null);
		pullToRefreshView=(MaterialRefreshLayout) findViewById(R.id.pull_refreshview_search);
		moreResonListv = (ListView) findViewById(R.id.lv_serch_listv);
		SearchLayberLayout = (AutoNewLineLinearLayout) findViewById(R.id.search_layout_label);
		SearchLabelLin = (LinearLayout) findViewById(R.id.search_labe_lin);
		lvHistory.addFooterView(historyFooter, null, false);
		mAdapter = new Page3Tab1SecondAdapter(this);
		mHistoryAdapter = new SearchHistoryAdapter(this);
		
		lvHistory.setAdapter(mHistoryAdapter);
		moreResonListv.setAdapter(mAdapter);
		initSearchHistoryView();
		moreResonListv.setVisibility(View.GONE);
		SearchLabelLin.setVisibility(View.GONE);
		commitBtn.setOnClickListener(this);
		removeFooter();
		pullToRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				myDataPage = 1;
				if (checkNetWork(KnowledgeSearchActivity.this)) {
					
					pullToRefreshView.autoRefresh();
					serchInfoList(PAGESIZE_20, myDataPage+"", keyWd, true);
				} else {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							pullToRefreshView.finishRefresh();
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
		                		serchInfoList(PAGESIZE_20, myDataPage+"", keyWd, false);
		        				
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
					 Intent intent = new Intent();
					 intent.setClass(KnowledgeSearchActivity.this, KnowledgeDetailActivity.class);
					 intent.putExtra("object_id", dataList.get(index).getObject_id());
					 startActivity(intent);
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
						if(checkNetWork(KnowledgeSearchActivity.this)){
							serchKey = serchEdit.getText().toString();
							if(!TextUtils.isEmpty(serchKey)){
								keyWd = serchKey;
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
		//显示剩余输入量
		serchEdit.addTextChangedListener(new Watcher());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.serch_commit://搜索按钮
			if(checkNetWork(KnowledgeSearchActivity.this)){
				serchKey = serchEdit.getText().toString();
				if(!TextUtils.isEmpty(serchKey)){
					keyWd = serchKey;
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
		moreResonListv.setVisibility(View.VISIBLE);
		if(null != keyWd){
			commitpro.setVisibility(View.VISIBLE);
			commitBtn.setVisibility(View.GONE);
			myDataPage = 1;
			serchInfoList(PAGESIZE_20, myDataPage+"", keyWd, true);
			addSearchWDTolist(keyWd);
			addSearchWDToHistory(keyWd);
			lvHistory.setVisibility(View.GONE);
			SearchLabelLin.setVisibility(View.GONE);
		}
	}
	//清除搜索历史
	private void clearSearchHistory() {
		PreManager.instance().saveSearchKey(this, "");
		initSearchHistoryView();
	}
	//获取搜索历史
	private List<String> getSearchHistory(){
		List<String> keylistList = new ArrayList<String>();
		String keyWD = PreManager.instance().getSearchKey(this);
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
	
	private void initSearchLabels(){
		addLabel(SearchLabels);
	}
	
	private void getHostSearchLabel(){
		new XiangchengMallProcClass(KnowledgeSearchActivity.this).getKnowKeys(PreManager.instance().getUserId(this), new InterfaceKnowledgeKeysCallBack() {
			
			@Override
			public void onSuccess(String icode, List<keywordBean> Keylist) {
				SearchLabels = Keylist;
				initSearchLabels();
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				SearchLabels = new ArrayList<keywordBean>();
				for(int i = 0 ; i < 10 ; i++){
					keywordBean keys = new keywordBean();
					keys.post_keywords = "失眠"+i;
					SearchLabels.add(keys);
				}
				initSearchLabels();
				
			}
		});
	}
	
	
	private void serchInfoList(int pageSize , String page , String key, final boolean isrefresh){
		new XiangchengMallProcClass(KnowledgeSearchActivity.this).getKnowSearch(key, page, pageSize, new InterfaceKnowledgeSearchCallBack() {
			
			@Override
			public void onSuccess(String icode, List<ArticleBean> article_list,
					String totalpage) {
				TotalPage = Integer.parseInt(totalpage);
				pullToRefreshView.finishRefresh();
				doSerchCallBackData(article_list, isrefresh);
				commitpro.setVisibility(View.GONE);
				commitBtn.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				commitpro.setVisibility(View.GONE);
				commitBtn.setVisibility(View.VISIBLE);
				pullToRefreshView.finishRefresh();
				if(dataList.size() <=0){
					removeFooter();
				}
				toastMsg(strErrMsg);
			}
		});
	}
	
	private void doSerchCallBackData(List<ArticleBean> list, boolean isrefresh){
		if(list != null && list.size()>0){
			if(isrefresh){
				dataList = list;
			}else{
				dataList.addAll(list);
			}
		}
		if(dataList !=null && dataList.size() == 0){
			tvKnowledge.setVisibility(View.VISIBLE);
			tvKnowledge.setText(Html.fromHtml("没有发现有关"+(keyWd.length()>10?keyWd.substring(0, 10)+"...":keyWd)+"的信息"));
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
		
		mAdapter.setDatas(dataList);
	}
	
	
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
            	if(tvKnowledge.getVisibility() == View.VISIBLE)
            		tvKnowledge.setVisibility(View.GONE);
        		moreResonListv.setVisibility(View.GONE);
        		initSearchLabels();
        		initSearchHistoryView();
        		if(dataList != null){
        			dataList.clear();
        		}
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
		String keyS = PreManager.instance().getSearchKey(this);
		if(TextUtils.isEmpty(keyS)){
			PreManager.instance().saveSearchKey(this, keywd);
		}else{
			try {
				String[] keyarr = keyS.split(",");
				for (String string : keyarr) {
					if(string.endsWith(keywd)){
						return;
					}
				}
				PreManager.instance().saveSearchKey(this, keyS+","+keywd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 添加标签
	 * @param labels
	 */

	private void addLabel(List<keywordBean> labels){
		 //添加水平显示的textView 
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(4, 4, 50, 4);
		
		SearchLayberLayout.removeAllViews();
		if(labels == null || labels.size() == 0){
			SearchLabelLin.setVisibility(View.GONE);
		}else{
			SearchLabelLin.setVisibility(View.VISIBLE);
			for (int i = 0; i < labels.size(); i++) {
				TextView text1= new TextView(this);
				text1.setOnClickListener(new LabelOnClickListener(labels.get(i).post_keywords));
				text1.setText(labels.get(i).post_keywords);
				text1.setTextColor(getResources().getColor(R.color.ct_color));
				text1.setBackgroundResource(R.drawable.label_bg);
				text1.setLayoutParams(lp);
				text1.setPadding(25, 10, 25, 10);
				text1.setTextColor(getResources().getColor(R.color.ct_color));
				text1.setTextSize(14);
				LinearLayout linearLayout = new LinearLayout(this);
				linearLayout.setOrientation(LinearLayout.HORIZONTAL);
				linearLayout.setGravity(Gravity.CENTER_VERTICAL);
				linearLayout.addView(text1);
				SearchLayberLayout.addView(linearLayout);
			}	
		}
	}
	
	class LabelOnClickListener implements OnClickListener {
		String SearchTv;
		public LabelOnClickListener(String text) {
			SearchTv = text;
		}

		@Override
		public void onClick(View view) {
			keyWd = SearchTv;
			serchEdit.setText(keyWd);
			Editable etext = serchEdit.getText();
			Selection.setSelection(etext, etext.length());
			initViewFragment(keyWd);
		}

	}
	
}
