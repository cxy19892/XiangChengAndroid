package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.adapter.Page3Tab1SecondAdapter;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.ClassifyTagBean;
import com.yzm.sleep.bean.LeftBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceKnowCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.KnowListParams;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * params type 睡眠常识为4； 睡眠规律为5；心理调节为6；
 */
public class KnowledgeListActivity extends BaseActivity {
	
	private String type, term_id; //1.生活习惯； 2.睡眠环境
	private MaterialRefreshLayout myRefresh;
	private Page3Tab1SecondAdapter mAdapter;
	private ListView mLisView;
	private int dataPage =1, totalPage = 3;
	private List<ArticleBean> articleList;
	private int tagType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowledge_list);
		term_id = type = getIntent().getStringExtra("type");
		articleList = new ArrayList<ArticleBean>();
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = ((TextView)findViewById(R.id.title));
		if("4".equals(type))
			title.setText("睡眠常识");
		if("5".equals(type))
			title.setText("睡眠规律");
		if("6".equals(type))
			title.setText("心理调节");
		
		Button right = (Button) findViewById(R.id.btn_title_right);
		Drawable drawable = getResources().getDrawable(R.drawable.community_group__search_btn);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		right.setCompoundDrawables(drawable, null, null, null);
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		myRefresh = (MaterialRefreshLayout) findViewById(R.id.my_refresh);
		mLisView = (ListView) findViewById(R.id.my_listview);
		mAdapter = new Page3Tab1SecondAdapter(this);
		mLisView.setAdapter(mAdapter);
		myRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				if(Util.checkNetWork(KnowledgeListActivity.this)){
					getDatas();
				}else{
					myRefresh.addListViewState(mLisView, Constant.NO_NET);
					myRefresh.finishRefreshing();
				}
			}
		});
		
		mLisView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount; 
	            if(lastItem == totalItemCount) { 
	            	if(mLisView.getChildCount()>0){
	            		View lastItemView=(View) mLisView.getChildAt(mLisView.getChildCount()-1); 
		                if ((mLisView.getBottom())==lastItemView.getBottom()) { 
		                	if (dataPage < totalPage) {
		    					dataPage += 1;
		    					getDatas();
		    				} 
		                } 
	            	}
	            } 
			}
		});
		
		mLisView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int index = position - mLisView.getHeaderViewsCount();
				if(index >=0 ){
					Intent intent = new Intent();
					intent.setClass(KnowledgeListActivity.this, KnowledgeDetailActivity.class);
					intent.putExtra("object_id", mAdapter.getDatas().get(index).getObject_id());
					intent.putExtra("pic_url", mAdapter.getDatas().get(index).getBjpic());
					startActivity(intent);
				}
			}
		});
		
		myRefresh.autoRefresh();
		
	}
	
	private void getDatas(){
		KnowListParams params = new KnowListParams();
		params.my_int_id = PreManager.instance().getUserId(this);
		params.term_id = term_id;
		params.page = String.valueOf(dataPage);
		params.pagesize = "20";
		new XiangchengMallProcClass(this).getknowList(params, new InterfaceKnowCallBack() {
			
			@Override
			public void onSuccess(String icode, List<ClassifyTagBean> term_list, List<ArticleBean> article_list, int totalpage) {
				myRefresh.finishRefresh();
				
				doCallDataArticle(article_list, totalpage);
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				Util.show(KnowledgeListActivity.this, strErrMsg);
				myRefresh.finishRefresh();
			}
		});
	}
	
	/**
	 * 根据标签获取数据
	 */
	private void doCallDataArticle(List<ArticleBean> article_list, int totalpage){
		
		this.totalPage = totalpage;
		if(article_list != null){
			if(dataPage == 1)
				articleList = article_list;
			else
				articleList.addAll(article_list);
		}
		if(totalPage == dataPage){
			if(articleList.size() == 0){
				myRefresh.addListViewState(mLisView, Constant.NO_DATA);
			}else
			myRefresh.addListViewState(mLisView, Constant.NO_MORE);
		}
		mAdapter.setDatas(articleList);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			startActivity(new Intent(this, KnowledgeSearchActivity.class));
		default:
			break;
		}
	}

}
