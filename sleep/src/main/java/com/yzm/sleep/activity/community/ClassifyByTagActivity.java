package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.adapter.ChoiceAdapter;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.PushClasifyBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.GetArticleByTagParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetArticleByTagCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * @param bean PushClasifyBean bean;
 * @author Administrator
 */
public class ClassifyByTagActivity extends BaseActivity {

	private PushClasifyBean bean;
	private MaterialRefreshLayout mRefresh;
	private ListView mListView;
	private int dataPage = 1, totalPage = 0; 
	private boolean isLoading;
	private ChoiceAdapter mAdapter;
	private List<ArticleBean> articleList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify_bytag);
		bean = (PushClasifyBean) getIntent().getSerializableExtra("bean");
		articleList = new ArrayList<ArticleBean>();
		initView();
	}
	
	private void initView(){
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText(bean.getTagname());
		mRefresh = (MaterialRefreshLayout) findViewById(R.id.material_refresh);
		mListView = (ListView) findViewById(R.id.listview);
		TextView f = new TextView(this);
		f.setHeight(0);
		mListView.addHeaderView(f, null, false);
		mAdapter = new ChoiceAdapter(this, (getScreenWidth() - Util.Dp2Px(this, 85))/4);
		mListView.setAdapter(mAdapter);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mRefresh.autoRefresh();
			}
		}, 300);
		
		mRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				if (Util.checkNetWork(ClassifyByTagActivity.this)) {
					dataPage = 1;
					getClassifyByTagData(true);
				}else{
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							mRefresh.finishRefresh();
							if(articleList.size() > 0)
								Util.show(ClassifyByTagActivity.this, "网路连接错误");
							else
								mRefresh.addListViewState(mListView, Constant.NO_NET);
						}
					}, 500);
					
				}
			}
		});

		mRefresh.setOnClickRequestListener(new OnClickRequestListener() {

			@Override
			public void setRequest() {
				mRefresh.autoRefresh();
			}
		});
		
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
						View lastItemView = (View) mListView .getChildAt(mListView.getChildCount() - 1);
						if ((mListView.getBottom()) == lastItemView.getBottom()) {
							if(Util.checkNetWork(ClassifyByTagActivity.this)){
								if (dataPage < totalPage && !isLoading) {
									isLoading = true;
									dataPage++;
									getClassifyByTagData(false);
								}
							}else
								Util.show(ClassifyByTagActivity.this, "网路连接错误");
						}
					}
				}
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position - mListView.getHeaderViewsCount();
				if(index >= 0){
					Intent intent =null;
					if(PreManager.instance().getIsLogin(ClassifyByTagActivity.this)){
						ArticleBean bean = articleList.get(index);
						intent =new Intent(ClassifyByTagActivity.this, CommunityTopiceDetailActivity.class);
						intent.putExtra("topices_id", bean.getTid());
						intent.putExtra("topices_title", bean.getT_subject());
						intent.putExtra("is_choiceness", "2".equals(bean.getListtype()));
						intent.putExtra("author_id", bean.getUid());
					}else
						intent = new Intent(ClassifyByTagActivity.this, LoginActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	
	private void getClassifyByTagData(final boolean isRefrensh){
		if(isRefrensh)
			dataPage = 1;
		GetArticleByTagParamClass mParam = new GetArticleByTagParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(this);
		mParam.page = String.valueOf(dataPage);
		mParam.pagesize = "10";
		mParam.tagid = bean.getTagid();
		new XiangchengProcClass(this).getArticleByTag(mParam, new InterfaceGetArticleByTagCallback() {
			
			@Override
			public void onSuccess(int icode, List<ArticleBean> list, int totalpage) {
				isLoading = false;
				if (isRefrensh) 
					mRefresh.finishRefresh();
				doCallBackData(list, totalpage);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				isLoading = false;
				if (isRefrensh) 
					mRefresh.finishRefresh();
				if(articleList.size() <= 0)
					mRefresh.addListViewState(mListView, Constant.NO_DATA);
				else
					Util.show(ClassifyByTagActivity.this, strErrMsg);
			}
		});
	}
	
	private void doCallBackData(List<ArticleBean> list, int totalpage){
		this.totalPage = totalpage;
		if(list != null){
			if(dataPage == 1)
				articleList = list;
			else
				articleList.addAll(list);
			
			if(dataPage < totalPage)
				mRefresh.addListViewState(mListView, Constant.LOADING);
			else
				mRefresh.addListViewState(mListView, Constant.NO_MORE);
		}
		
		if(articleList.size() <= 0)
			mRefresh.addListViewState(mListView, Constant.NO_DATA);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mAdapter.setData(articleList);
			}
		}, 200);
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
	
}
