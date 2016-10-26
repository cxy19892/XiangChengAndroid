package com.yzm.sleep.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.community.ClassifyByTagActivity;
import com.yzm.sleep.activity.community.CommunityTopiceDetailActivity;
import com.yzm.sleep.activity.community.ProgramDetailsActivity;
import com.yzm.sleep.activity.community.TeamDetailsActivity;
import com.yzm.sleep.adapter.ChoiceAdapter;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.CommunityBarnerBean;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.bean.PushClasifyBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.scrollpage.BaseSliderView;
import com.yzm.sleep.scrollpage.BaseSliderView.OnSliderClickListener;
import com.yzm.sleep.scrollpage.SliderLayout;
import com.yzm.sleep.scrollpage.TextSliderView;
import com.yzm.sleep.scrollpage.ZoomOutSlideTransformer;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.HandPickArticleListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceHandPickArticleListCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.LazyFragment;

/**
 * 精选
 * 
 * @author Administrator
 * 
 */
public class FragmentPage3Tab1Choice extends LazyFragment implements
		OnClickListener, OnSliderClickListener {
	private Activity activity;
	private MaterialRefreshLayout mRefresh;
	private ListView mElistview;
	private int screenWidht;
	private ChoiceAdapter mAdapter;
//	private ClassifyByTagAdapter mAdapter;
	private int page = 0;// 页数
	private List<ArticleBean> articleList;
	private List<CommunityBarnerBean> barnerList;// 封面集合
	private int total_page;
	private boolean isLoading = false;
	private SliderLayout mDemoSlider;
	private View headView;
	private LinearLayout tagParent;
	private TextView tvTag;
	private LayoutInflater mInflater;

	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		screenWidht = bundle.getInt("screenWidth", 0);
		barnerList = new ArrayList<CommunityBarnerBean>();
		articleList = new ArrayList<ArticleBean>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflater = inflater;
		headView = inflater.inflate( R.layout.head_fragment_choice_layout, null);
		return inflater.inflate(R.layout.fragment_page3_tab1_my, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextView textView = new TextView(activity);
		textView.setHeight(0);
		mRefresh = (MaterialRefreshLayout) view.findViewById(R.id.fragment_mian_refresh);
		mElistview = (ListView) view.findViewById(R.id.my_expanlist);
		mElistview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mElistview.addFooterView(textView);
		initHeaderView();
//		mAdapter = new ClassifyByTagAdapter(activity, (screenWidht - Util.Dp2Px(activity, 85))/4);
		mAdapter = new ChoiceAdapter(activity, (screenWidht - Util.Dp2Px(activity, 85))/4);
		mElistview.setAdapter(mAdapter);
		setListener();
		isPrepared = true;
		lazyLoad();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Community_Selected");
		MobclickAgent.onResume(activity); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Community_Selected"); 
		MobclickAgent.onPause(activity);
	}

	private void initHeaderView() {
		if(mDemoSlider == null){
			mDemoSlider = (SliderLayout) headView.findViewById(R.id.sliderlayout);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.width = screenWidht;
			lp.height = lp.width * 1 / 2;
			mDemoSlider.setLayoutParams(lp);
			// 幻灯片切换方式
			mDemoSlider.setPagerTransformer(true, new ZoomOutSlideTransformer());
			// 指示符位置
			mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
		}
		tvTag = (TextView)headView.findViewById(R.id.tv_tag);
		tagParent = (LinearLayout)headView.findViewById(R.id.tag_parent);
		mElistview.addHeaderView(headView, null,false);
	}
	
	private void setListener(){
		mElistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position - mElistview.getHeaderViewsCount();
				if (articleList != null && articleList.size() > 0) {
					Intent intent;
					if (PreManager.instance().getIsLogin(activity)) {
//						intent = new Intent(activity, CommunityTopiceDetailActivity.class);
//						intent.putExtra("topices_id", articleList.get(index) .getTid());
//						intent.putExtra("topices_title", articleList.get(index).getT_subject());
//						intent.putExtra("is_choiceness", true);
						ArticleBean bean = articleList.get(index);
						intent =new Intent(activity, CommunityTopiceDetailActivity.class);
						intent.putExtra("topices_id", bean.getTid());
						intent.putExtra("topices_title", bean.getT_subject());
						intent.putExtra("is_choiceness", "2".equals(bean.getListtype()));
						intent.putExtra("author_id", bean.getUid());
					} else {
						intent = new Intent(activity, LoginActivity.class);
					}
					startActivity(intent);
				}
			}
		});

		mElistview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (mElistview.getChildCount() > 0) {
						View lastItemView = (View) mElistview .getChildAt(mElistview.getChildCount() - 1);
						if ((mElistview.getBottom()) == lastItemView.getBottom()) {
							if (page < total_page && !isLoading) {
								isLoading = true;
								page++;
								HttpChoiceData(false);
							}
						}
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView listview,
					int scrollState) {
			}
		});

		mRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				page = 1;
				HttpChoiceData(true);
			}
		});

		mRefresh.setOnClickRequestListener(new OnClickRequestListener() {

			@Override
			public void setRequest() {
				mRefresh.autoRefresh();
			}
		});
	}

	/**
	 * 重新构造推荐列表
	 */
	private void reloadSliderView() {
		try {
			for (int i = 0; i < barnerList.size(); i++) {
				TextSliderView textSliderView = new TextSliderView(activity);
				String imgUrl = "";
				String imgKey = "";
				if (barnerList.get(i).getPicture() != null
						&& !barnerList.get(i).getPicture().equals("")) {
					imgUrl = barnerList.get(i).getPicture();
					imgKey = barnerList.get(i).getPicturekey();
				}
				// 初始化幻灯片页面
				textSliderView.groupId(barnerList.get(i).getUrlid())
						.urltype(barnerList.get(i).getUrltype())
						.title(barnerList.get(i).getTitle())
						.intro(barnerList.get(i).getIntro()).description("")
						.setImageInfo(imgUrl, imgKey)
						.setOnSliderClickListener(this);
				mDemoSlider.addSlider(textSliderView);
			}
		} catch (Exception e) {
		}
	}

	private void HttpChoiceData(final boolean isRefresh) {
		// 请求网络之前先判断网络是否可以用
		if (!Util.checkNetWork(activity)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mRefresh.finishRefresh();
					mRefresh.addListViewState(mElistview, -1);
					if (barnerList.size() == 0) {
						mRefresh.addListViewState(mElistview, Constant.NO_NET);
					} else {
						mAdapter.setData(articleList);
					}
				}
			}, 300);
			return;
		}
		HandPickArticleListParamClass handPickArticleListParamClass = new HandPickArticleListParamClass();
		handPickArticleListParamClass.my_int_id = PreManager.instance()
				.getUserId(activity);
		handPickArticleListParamClass.page = page + "";
		handPickArticleListParamClass.pagesize = "10";
		new XiangchengProcClass(activity).handPickArticle(
				handPickArticleListParamClass,
				new InterfaceHandPickArticleListCallback() {

					@Override
					public void onSuccess(int icode, List<CommunityBarnerBean> barner_list, List<PushClasifyBean> pushList,
							List<ArticleBean> article_list, int totalpage) {
						if(isRefresh)
							mRefresh.finishRefresh();
						isLoading = false;
						doCallbackData(barner_list, pushList, article_list, isRefresh, totalpage);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						if(isRefresh)
							mRefresh.finishRefresh();
						isLoading = false;
						mRefresh.addListViewState(mElistview, -1);
						Util.show(activity, strErrMsg);
					}
				});

	}

	private void doCallbackData(List<CommunityBarnerBean> barner_list, List<PushClasifyBean> pushList, List<ArticleBean> article_list, boolean isRefresh, int totalpage) {
		this.total_page = totalpage;
		if(isRefresh){
			if (barner_list.size() > 0) {
				if (barnerList.size() == 0) {
					barnerList.addAll(barner_list);
					reloadSliderView();
				}
			}
			addPushList(pushList);
		}
		
		if(article_list != null){
			if(isRefresh)
				this.articleList = article_list;
			else
				this.articleList.addAll(article_list);
			
			if(page < this.total_page)
				mRefresh.addListViewState(mElistview, Constant.LOADING);
			else
				mRefresh.addListViewState(mElistview, Constant.NO_MORE);
		}
		
		if (this.articleList.size() <= 0) 
			mRefresh.addListViewState(mElistview, Constant.NO_DATA);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mAdapter.setData(articleList);
			}
		}, 200);
	}
	
	private void addPushList(List<PushClasifyBean> pushList){
		if(pushList != null && pushList.size() >0){
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			int screenSize = (screenWidht - Util.Dp2Px(activity, 85))/4;
			lp.width = screenSize;
			lp.height = screenSize;
			tvTag.setVisibility(View.VISIBLE);
			tagParent.removeAllViews();
			int size = pushList.size();
			for (int i = 0; i < size; i++) {
				final PushClasifyBean bean = pushList.get(i);
				View view = this.mInflater.inflate(R.layout.item_pushtag, null);
				ImageView tagImg = (ImageView)view.findViewById(R.id.tag_img);
				TextView tagName = (TextView) view.findViewById(R.id.tag_name);
				tagName.setText(bean.getTagname());
				tagImg.setLayoutParams(lp);
				ImageLoader.getInstance().displayImage(bean.getTagpicture(), bean.getTagpicture_key(), tagImg, MyApplication.choicePicOptn);
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(activity, ClassifyByTagActivity.class).putExtra("bean", bean));
					}
				});
				tagParent.addView(view);
			}
		}
	}

	// 标志位，标志已经初始化完成。
	private boolean isPrepared;
	private boolean isFirst = true;
	private boolean isRequest = false;

	/**
	 * 每次滑动到当前页面。就会调用的方法
	 */
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
						mRefresh.autoRefresh();
					} else {
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								mRefresh.finishRefresh();
								mRefresh.addListViewState(mElistview,
										Constant.NO_NET);
							}
						}, 300);
					}
				}
			}, 100);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		mDemoSlider.stopAutoCycle();
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		String grid;
		String uyType;
		if (slider.getGruopId() == null || slider.getGruopId().equals("")) {
			grid = "";
		} else {
			grid = slider.getGruopId();
		}
		if (slider.getType() == null || slider.getType().equals("")) {
			uyType = "";
		} else {
			uyType = slider.getType();
		}

		Intent intent = null;
		if (PreManager.instance().getIsLogin(activity)) {
			if (uyType.equals("1")) {// 跳转到话题
				intent = new Intent(activity,
						CommunityTopiceDetailActivity.class);
				intent.putExtra("topices_id", grid);
				intent.putExtra("topices_title", slider.gettitle());
				intent.putExtra("author_id", "");
			} else if (uyType.equals("2")) {// 跳转到文章
				intent = new Intent(activity,
						CommunityTopiceDetailActivity.class);
				intent.putExtra("topices_id", grid);
				intent.putExtra("topices_title", slider.gettitle());
				intent.putExtra("is_choiceness", true);
				intent.putExtra("author_id", "");
			} else if (uyType.equals("3")) {// 跳转到小组
				intent = new Intent(activity, TeamDetailsActivity.class);
				CommunityGroupBean bean = new CommunityGroupBean();
				bean.setGid(grid);
				bean.setIscode(true);
				intent.putExtra("bean", bean);
			} else if (uyType.equals("4")) {// 跳转到活动详情
				intent = new Intent(activity, ProgramDetailsActivity.class);
				intent.putExtra("id", grid);
			} else {
				intent = new Intent(activity,
						CommunityTopiceDetailActivity.class);
				intent.putExtra("topices_id", grid);
				intent.putExtra("topices_title", slider.gettitle());
				intent.putExtra("author_id", "");
			}
		} else {
			intent = new Intent(activity, LoginActivity.class);
		}
		if (intent != null)
			startActivity(intent);
	}
}
