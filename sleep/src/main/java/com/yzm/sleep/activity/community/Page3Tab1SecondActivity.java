package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceKnowCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.KnowListParams;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * params type 2.生活习惯；3.睡眠环境
 */
public class Page3Tab1SecondActivity extends BaseActivity {
	
	private String type, term_id; //1.生活习惯； 2.睡眠环境
	private HorizontalScrollView hsvView;
	private LinearLayout rlTitle;
	private MaterialRefreshLayout myRefresh;
	private Page3Tab1SecondAdapter mAdapter;
	private ListView mLisView;
	private boolean isLoadTag;
	private int dataPage =1, totalPage = 3;
	private List<ClassifyTagBean> termList;
	private List<ArticleBean> articleList;
	private int tagType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page3tab1_second);
		term_id = type = getIntent().getStringExtra("type");
		termList = new ArrayList<ClassifyTagBean>();
		articleList = new ArrayList<ArticleBean>();
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = ((TextView)findViewById(R.id.title));
		if("2".equals(type))
			title.setText("生活习惯");
		if("3".equals(type))
			title.setText("睡眠环境");
		
		Button right = (Button) findViewById(R.id.btn_title_right);
		Drawable drawable = getResources().getDrawable(R.drawable.community_group__search_btn);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		right.setCompoundDrawables(drawable, null, null, null);
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		hsvView = (HorizontalScrollView) findViewById(R.id.hsv_view);
		rlTitle = (LinearLayout) findViewById(R.id.rl_title);
		myRefresh = (MaterialRefreshLayout) findViewById(R.id.my_refresh);
		mLisView = (ListView) findViewById(R.id.my_listview);
		mAdapter = new Page3Tab1SecondAdapter(this);
		mLisView.setAdapter(mAdapter);
		myRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				if(Util.checkNetWork(Page3Tab1SecondActivity.this)){
					getDatas();
				}else{
					Util.show(Page3Tab1SecondActivity.this, "请检查网络设置");
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
					startActivity(new Intent(Page3Tab1SecondActivity.this, KnowledgeDetailActivity.class)
					.putExtra("pic_url", articleList.get(index).getBjpic())
					.putExtra("object_id", articleList.get(index).getObject_id()));
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
				if(!isLoadTag)
					initRlTitle(term_list);
				
				doCallDataArticle(article_list, totalpage);
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				Util.show(Page3Tab1SecondActivity.this, strErrMsg);
				hsvView.setVisibility(View.INVISIBLE);
				myRefresh.finishRefresh();
			}
		});
	}
	
	/**
	 * 
	 */
	private void initRlTitle(List<ClassifyTagBean> term_list){
		this.termList = term_list;
		LayoutParams mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mParams.width = getScreenWidth()/5;
		for (int i = 0; i < term_list.size(); i++) {
			View view = getLayoutInflater().inflate(R.layout.item_rltitle_layout, null);
			TextView name = ((TextView)view.findViewById(R.id.title_name));
			name.setText(term_list.get(i).getTerm_name());
			name.setLayoutParams(mParams);
			view.setTag(i);
			if(i == 0){
				name.setTextColor(getResources().getColor(R.color.t_color));
				view.findViewById(R.id.title_tip).setVisibility(View.VISIBLE);;
			}else{
				name.setTextColor(getResources().getColor(R.color.theme_color));
				view.findViewById(R.id.title_tip).setVisibility(View.GONE);
			}
			
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tagType = Integer.parseInt(String.valueOf(v.getTag()));
					term_id = termList.get(tagType).getTerm_id();
					if(tagType >= rlTitle.getChildCount()/2){
						hsvView.smoothScrollBy(getScreenWidth()/5, 0);
					}else{
						hsvView.smoothScrollBy(-getScreenWidth()/5, 0);
					}
					for(int i =0; i < rlTitle.getChildCount(); i++){
						rlTitle.getChildAt(i).findViewById(R.id.title_tip).setVisibility(View.GONE);
						((TextView)rlTitle.getChildAt(i).findViewById(R.id.title_name)).setTextColor(getResources().getColor(R.color.theme_color));
					}
					((TextView)rlTitle.getChildAt(tagType).findViewById(R.id.title_name)).setTextColor(getResources().getColor(R.color.t_color));
					rlTitle.getChildAt(tagType).findViewById(R.id.title_tip).setVisibility(View.VISIBLE);
					dataPage = 1;
					getDatas();
				}
			});
			rlTitle.addView(view);
		}
		hsvView.setVisibility(View.VISIBLE);
		this.isLoadTag = true;
	}
	
	private void doCallDataArticle(List<ArticleBean> article_list, int totalpage){
		
		this.totalPage = totalpage;
		if(article_list != null){
			if(dataPage == 1)
				articleList = article_list;
			else
				articleList.addAll(article_list);
			
			if(dataPage < this.totalPage) 
				myRefresh.addListViewState(mLisView, Constant.LOADING);
			else
				myRefresh.addListViewState(mLisView, Constant.NO_MORE);
				
		}
		if(articleList.size() <= 0)
			myRefresh.addListViewState(mLisView, Constant.NO_DATA);
		
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
