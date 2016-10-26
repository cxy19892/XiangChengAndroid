package com.yzm.sleep.activity.community;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.PersonalInfoActivity;
import com.yzm.sleep.activity.ShareActivity;
import com.yzm.sleep.adapter.CommunityGridViewAdapter;
import com.yzm.sleep.adapter.RecommendGropuAdapter;
import com.yzm.sleep.adapter.TopiaceCommentAdapter;
import com.yzm.sleep.adapter.TopiaceCommentAdapter.ClickListenerAdapter;
import com.yzm.sleep.bean.ArticleCommentBean;
import com.yzm.sleep.bean.ArticleCorrelatGroupBean;
import com.yzm.sleep.bean.ArticleDetailBean;
import com.yzm.sleep.bean.ArticleImageBean;
import com.yzm.sleep.bean.ArticleThumbUpUserBean;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.bean.ShareClassParam;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.sticky.StickyListHeadersListView;
import com.yzm.sleep.sticky.StickyListHeadersListView.OnHeaderClickListener;
import com.yzm.sleep.tools.AnimationUtil;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.CustomImageLoadingListener;
import com.yzm.sleep.utils.CustomTextWatcher;
import com.yzm.sleep.utils.InterFaceUtilsClass.DeleteCommentParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetCommunityTopicDetailParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDeleteCommentCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetCommunityTopicDetailCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceTopicDeleteCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceTopicPraiseCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceTopicReplyPostNewCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.TopicDeleteParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.TopicPraiseParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.TopicReplyPostNewParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ArticleCommentListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ArticleDetailMsgParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceArticleCommentListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceArticleDetailMsgCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.CustomGridView;
import com.yzm.sleep.widget.CustomListView;
import com.yzm.sleep.widget.EaseEmojiconMenu;
import com.yzm.sleep.widget.EaseEmojiconMenuBase.EaseEmojiconMenuListener;

/**
 * 话题详情
 * 
 * @author tianxun
 * @params String topices_id; 话题id
 * @params String topices_title; 话题 标题
 * @params boolean is_choiceness  true文章 false 话题
 * @params String author_id 作者id
 */
@SuppressWarnings("deprecation")
public class CommunityTopiceDetailActivity extends BaseActivity implements
		ClickListenerAdapter, OnTouchListener {
	private Activity activity;
	private TextView title;
	private RelativeLayout rlOp, rlInput;
	private Button opPraise;
	private ImageButton btnFace;
	private EditText etInput;
	private EaseEmojiconMenu emojicon;
	private ClipboardManager clipboard;
	private MaterialRefreshLayout pullrView;
	private StickyListHeadersListView lvComments;
	private String topicesId,titleS;
	private View headView, noMoreView, loadingView, noNetView, delete;
	private TopiaceCommentAdapter commentAdapter;
	private CommunityGridViewAdapter imageAdapter;
	private List<ArticleCorrelatGroupBean> groupDatas;
	private List<ArticleCommentBean> commentBeans;
	private int dataPage =1, totalPage = 0;
	private String page_id = "0", tempReplayUId = "", tempReplayUName = "",tempRelayPId="";
	private ArticleDetailBean detailBean;
	private ProgressUtils pro;
	private MyAlertDialog commentDialog;
	private WebView content;
	private List<String> imagesUrl;
	private boolean isChoiceness, isShowInput=false, isGotoTop=false, isGotoIndex=false, isComment = true, isLoadSuc=false, isLoading = false; 
	private boolean hasData = false;
	private ImageButton ibMore, gotoTop;
	private int visibleCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_topic_detail);
		activity = this;
		title = (TextView) findViewById(R.id.title);
		isChoiceness=getIntent().getBooleanExtra("is_choiceness", false);
//		titleS = (TextUtils.isEmpty(getIntent().getStringExtra("topices_title")))? "香橙推荐" : getIntent().getStringExtra("topices_title");
//		title.setText(titleS.equals("香橙推荐") ? "话题详情" : titleS);
		topicesId = getIntent().getStringExtra("topices_id");
		initView();
	}

	private void initView() {
		ibMore = (ImageButton) findViewById(R.id.ib_more);
		
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		headView = getLayoutInflater().inflate(
				R.layout.topices_detail_head_layout, null);
		loadingView = getLayoutInflater().inflate(
				R.layout.listview_loading_footer, null);
		noNetView = getLayoutInflater().inflate(R.layout.layout_no_net, null);
	
		noMoreView = getLayoutInflater().inflate(
				R.layout.listview_footer_nomore, null);

		noNetView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pullrView.autoRefresh();
			}
		});
		delete=findViewById(R.id.delete);
		rlOp = (RelativeLayout) findViewById(R.id.rl_op);
		rlOp.findViewById(R.id.op_comment).setOnClickListener(this);
		opPraise = (Button) rlOp.findViewById(R.id.op_praise);
		opPraise.setOnClickListener(this);
		gotoTop=(ImageButton) findViewById(R.id.goto_top);
		gotoTop.setOnClickListener(this);
		findViewById(R.id.op_share).setOnClickListener(this);
		rlInput = (RelativeLayout) findViewById(R.id.rl_input);
		etInput = (EditText) rlInput.findViewById(R.id.edt_mesage);
		btnFace = (ImageButton) rlInput.findViewById(R.id.btn_face);
		btnFace.setOnClickListener(this);
		rlInput.findViewById(R.id.btn_sendmesage).setOnClickListener(this);
		emojicon = (EaseEmojiconMenu) rlInput.findViewById(R.id.comment_emojicon);
		emojicon.setVisibility(View.GONE);
		pullrView = (MaterialRefreshLayout) findViewById(R.id.pull_refreshview);
		lvComments = (StickyListHeadersListView) findViewById(R.id.lv_comments);
		lvComments.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		lvComments.addFooterView(loadingView, null, false);
		lvComments.addHeaderView(headView, null, false);
		commentAdapter = new TopiaceCommentAdapter(activity, this);
		lvComments.setAdapter(commentAdapter);
		headView.setVisibility(View.GONE);
		lvComments.setOnTouchListener(this);
		etInput.setOnTouchListener(this);
		setListener();
		lvComments.setStickyHeaderTopOffset(0);
		lvComments.setFastScrollEnabled(false);
		lvComments.removeFooterView(loadingView);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				pullrView.autoRefresh();
			}
		}, 300);
	}

	/**
	 * 初始化webview
	 * @param htm
	 */
	@SuppressLint("SetJavaScriptEnabled") 
	private void initWebView(String htm) {
		content = (WebView) headView.findViewById(R.id.topice_webpage);
		WebSettings setting = content.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setSupportZoom(false);
		setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		setting.setRenderPriority(RenderPriority.HIGH); //提高渲染速度
		setting.setBlockNetworkImage(true); //将图片渲染放在渲染完成之后
		content.loadDataWithBaseURL(null, htm, "text/html", "UTF-8", null);
		content.addJavascriptInterface(new JavascriptInterface(activity), "imagelistner");
		content.setWebViewClient(new CustomWebClient());
		content.setVisibility(View.VISIBLE);
		
	}

	private void setListener() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.op_comment).setOnClickListener(this);
		findViewById(R.id.op_praise).setOnClickListener(this);
		findViewById(R.id.op_share).setOnClickListener(this);
		emojicon.setEmojiconMenuListener(new EaseEmojiconMenuListener() {
			@Override
			public void onExpressionClicked(CharSequence emojiContent) {
				etInput.append(emojiContent);
			}

			@Override
			public void onDeleteImageClicked() {
				if (!TextUtils.isEmpty(etInput.getText())) {
					KeyEvent event = new KeyEvent(0, 0, 0,
							KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
							KeyEvent.KEYCODE_ENDCALL);
					etInput.dispatchKeyEvent(event);
				}
			}
		});
		
		//设置内容改变监听  一次性超过7个字符  有可能出现表情 则需要处理
		etInput.addTextChangedListener(new CustomTextWatcher(activity, etInput, null));

		pullrView.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				dataPage = 1;
				page_id = "0";
				if(checkNetWork(activity)){
					if (isChoiceness) 
						getArticleDetail();
					else
						getTopicesDetail();
				}else{
					new Handler().postDelayed(new Runnable() {
						public void run() {
							Util.show(activity, "网络连接错误");
							pullrView.finishRefresh();
							if(!hasData){
								removeAllFooter();
								lvComments.addFooterView(noNetView, null, true);
								rlInput.setVisibility(View.GONE);
								rlOp.setVisibility(View.GONE);
							}
						}
					},500); 
				}
			}
		});

		lvComments.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				isComment = true;
				tempReplayUId = "";
				tempReplayUName = "";
				etInput.setHint("说点什么呗");
				
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					gotoTop.setVisibility(View.GONE);
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					gotoTop.setVisibility(View.GONE);
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					
					if(lvComments.getFirstVisiblePosition() > 0)
						gotoTop.setVisibility(View.VISIBLE);
					else
						gotoTop.setVisibility(View.GONE);
					
					if(hasData){
						if(visibleCount <= 1){
							isShowInput=false;
							AnimationUtil.rotationForChangeAnimation(rlInput, rlOp);
						}
						else{
							AnimationUtil.rotationForChangeAnimation(rlOp, rlInput);
							isShowInput=true;
						}
					}
					break;
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				visibleCount=visibleItemCount;
				if(isGotoTop){ // 到顶部
					if(firstVisibleItem <= 0)
						isGotoTop=false;
					
					if (android.os.Build.VERSION.SDK_INT >= 8) 
						lvComments.smoothScrollToPosition(0);
					else 
						lvComments.setSelection(0);
				}
				
				if(isGotoIndex){ // 到指定位置
					if(visibleItemCount >= 2)
						isGotoIndex=false;
					else{
						if (android.os.Build.VERSION.SDK_INT >= 8) 
							lvComments.smoothScrollToPosition(1);
						else 
							lvComments.setSelection(1);
					}
				}
				
				if(isLoadSuc){
					if(visibleCount <= 1 && !isShowInput){
						if(rlOp.getVisibility() != View.VISIBLE)
							AnimationUtil.rotationForChangeAnimation(rlInput, rlOp);
					}else{
						if(rlInput.getVisibility() != View.VISIBLE)
							AnimationUtil.rotationForChangeAnimation(rlOp, rlInput);
					}
					// 判断是否加载
					int lastItem = firstVisibleItem + visibleItemCount;
					if (lastItem == totalItemCount && !isLoading) {
						if (dataPage < totalPage) {
							dataPage += 1;
							isLoading = true;
							getCommentList();
						}
					}
				}
			}
		});
	
		//点击 到 点赞列表
		lvComments.setOnHeaderClickListener(new OnHeaderClickListener() {
			@Override
			public void onHeaderClick(StickyListHeadersListView l, View header,int itemPosition, long headerId, boolean currentlySticky) {
				Intent intent = new Intent(activity,GroupThumbActivity.class);
				intent.putExtra("tid", detailBean.getTid());
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 获取话题详情
	 */
	private void getTopicesDetail(){
		GetCommunityTopicDetailParamClass mParam = new GetCommunityTopicDetailParamClass();
		mParam.tid = topicesId;
		mParam.my_int_id = PreManager.instance().getUserId(activity);
		new CommunityProcClass(this).getCommunityTopicDetail(mParam,new InterfaceGetCommunityTopicDetailCallBack() {

					@Override
					public void onSuccess(int icode, ArticleDetailBean info) {
						pullrView.finishRefresh();
						if(info!=null)
							doDetailData(info);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						hasData = true; 
						lvComments.removeFooterView(loadingView);
						pullrView.finishRefresh();
						title.setText("话题详情");
						headView.setVisibility(View.GONE);
						delete.setVisibility(View.VISIBLE);
						if(commentBeans != null)
							commentBeans.clear();
						commentAdapter.setData(null, false);
						titleS = (TextUtils.isEmpty(getIntent().getStringExtra("topices_title")))? "香橙推荐" : getIntent().getStringExtra("topices_title");
						title.setText(titleS.equals("香橙推荐") ? "话题详情" : titleS);
					}

				});
	}

	/**
	 * 获取文章详情
	 */
	private void getArticleDetail() {
		ArticleDetailMsgParamClass mParams = new ArticleDetailMsgParamClass();
		mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.tid = topicesId;
		new XiangchengProcClass(getApplicationContext()).articleDetailMsg(
				mParams, new InterfaceArticleDetailMsgCallback() {

					@Override
					public void onSuccess(int icode, ArticleDetailBean article) {
						pullrView.finishRefresh();
						if (article != null)
							doDetailData(article);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						hasData = true;
						lvComments.removeFooterView(loadingView);
						pullrView.finishRefresh();
						headView.setVisibility(View.GONE);
						delete.setVisibility(View.VISIBLE);
						if(commentBeans != null)
							commentBeans.clear();
						commentAdapter.setData(null, false);
						titleS = (TextUtils.isEmpty(getIntent().getStringExtra("topices_title")))? "香橙推荐" : getIntent().getStringExtra("topices_title");
						title.setText(titleS.equals("香橙推荐") ? "话题详情" : titleS);
					}
				});
	}
	
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void doDetailData(ArticleDetailBean detail) {
		this.detailBean = detail;
		this.hasData= true;
		if (PreManager.instance().getUserId(activity).equals(detailBean.getUid()) && !isChoiceness) {
			ibMore.setVisibility(View.VISIBLE);
			ibMore.setOnClickListener(this);
		}
		
		TextView topiceTitle = (TextView) headView.findViewById(R.id.topice_title);
		CircleImageView head = (CircleImageView) headView.findViewById(R.id.iv_userhead);
		TextView author = (TextView) headView.findViewById(R.id.author);
		TextView postTime = (TextView) headView.findViewById(R.id.post_time);
		if (!TextUtils.isEmpty(detailBean.getT_subject())) {
			topiceTitle.setText(detailBean.getT_subject());
			title.setText(detailBean.getT_subject());	
		}else{
			topiceTitle.setText("话题详情");
			title.setText("话题详情");
		}
		
		if("1".equals(detailBean.getIs_zj()))
			headView.findViewById(R.id.user_type).setVisibility(View.VISIBLE);
		else
			headView.findViewById(R.id.user_type).setVisibility(View.GONE);
	
		ImageLoader.getInstance().displayImage(detailBean.getAuthor_profile(),
				detailBean.getAuthor_profile_key(),head,
				MyApplication.headPicOptn,new CustomImageLoadingListener(activity, head, detailBean.getAuthor_profile_key()));
		head.setOnClickListener(this);
		author.setText(detailBean.getT_author());
		
		postTime.setText(TimeFormatUtil.getTimeBeforeCurrentTime(detailBean.getT_dateline()));
		
		TextView readRecordNum = (TextView) headView.findViewById(R.id.read_record_num);
		readRecordNum.setText("阅读数  " + detailBean.getT_view());
		readRecordNum.setVisibility(View.VISIBLE);
		praiseCheck("1".equals(detailBean.getStatus()), opPraise);
		
		if (isChoiceness) {
			initWebView(detailBean.getT_message());
		}else{
			ImageView imageView=(ImageView) headView.findViewById(R.id.topic_content_image);
			CustomGridView imagsView=(CustomGridView) headView.findViewById(R.id.cust_grid);
			TextView contentTextView=(TextView) headView.findViewById(R.id.topic_content);
			contentTextView.setText(detailBean.getIntro());
			contentTextView.setVisibility(View.VISIBLE);
			if(detailBean.getImages()!=null && detailBean.getImages().size()> 0){
				if(imagesUrl==null){
					imagesUrl=new ArrayList<String>();
				}
				imagesUrl.add(detailBean.getImages().get(0).getContent_attachment_sl());
				
				if(detailBean.getImages().size() > 1){
					imageView.setVisibility(View.GONE);
					imagsView.setVisibility(View.VISIBLE);
					imageAdapter = new CommunityGridViewAdapter(this, getScreenWidth());
					imagsView.setAdapter(imageAdapter);
					imageAdapter.setData(detailBean.getImages());
				}else{
					imagsView.setVisibility(View.GONE);
					imageView.setVisibility(View.VISIBLE);
					ArticleImageBean bean = detailBean.getImages().get(0);
					ImageLoader.getInstance().displayImage(bean.getContent_attachment_sl(),bean.getT_attachment_key_sl(), imageView,MyApplication.defaultOption);
					imageView.setOnClickListener(this);
				}
			}else{
				imageView.setVisibility(View.GONE);
				imagsView.setVisibility(View.GONE);
			}
			commentAdapter.setHeadData(true,detailBean.getT_zan_user(),detailBean.getT_zans(), detailBean.getStatus());
			removeAllFooter();
			lvComments.addFooterView(loadingView, null, false);
			isLoadSuc=true;
			getCommentList();
		}
		
		rlOp.setVisibility(View.VISIBLE);
		headView.findViewById(R.id.view_v).setVisibility(View.VISIBLE);
		headView.setVisibility(View.VISIBLE);
	}

	private void praiseCheck(boolean check, Button view) {
		if (view == null)
			return;
		Drawable praiseCheck = getResources().getDrawable(
				R.drawable.ic_shequ_zan_pressed);
		Drawable praiseNoCheck = getResources().getDrawable(
				R.drawable.ic_shequ_zan_normal);
		praiseCheck.setBounds(0, 0, praiseCheck.getMinimumWidth(),
				praiseCheck.getMinimumHeight());
		praiseNoCheck.setBounds(0, 0, praiseNoCheck.getMinimumWidth(),
				praiseNoCheck.getMinimumHeight());
		if (check) 
			view.setCompoundDrawables(praiseCheck, null, null, null);
		else 
			view.setCompoundDrawables(praiseNoCheck, null, null, null);
		
	}

	/**
	 * 删除自己的评论
	 */
	private void deleteComment(final int position, String commentId) {
		showPro();
		DeleteCommentParamClass mParam = new DeleteCommentParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(activity);
		mParam.pid = commentId;
		new CommunityProcClass(this).deleteComment(mParam,
				new InterfaceDeleteCommentCallBack() {

					@Override
					public void onError(int icode, String strErrMsg) {
						cancelPro();
						Util.show(activity, strErrMsg);
					}

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						cancelPro();
						Util.show(activity, strSuccMsg);
						commentBeans.remove(position);
						if (commentBeans.size() <= 0) {
							removeAllFooter();
							lvComments.addFooterView(noMoreView, null, false);
						}

						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								commentAdapter.setData(commentBeans,true);
							}
						}, 200);
					}
				});
	}

	/**
	 * 获取评论列表
	 * @param isRefrensh
	 */
	private void getCommentList() {
		ArticleCommentListParamClass mParam = new ArticleCommentListParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(activity);
		mParam.tid = topicesId;
		mParam.pagesize = String.valueOf(10);
		mParam.page = String.valueOf(dataPage);
		mParam.page_id = page_id;

		new XiangchengProcClass(activity).articleCommentList(mParam,
				new InterfaceArticleCommentListCallback() {

					@Override
					public void onSuccess(int icode, List<ArticleCommentBean> list, int totalpage, String page_id) {
						isLoading = false;
						doCommentData(list, totalpage, page_id);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						isLoading = false;
						Util.show(activity, strErrMsg);
					}
				});
	}

	private void doCommentData(List<ArticleCommentBean> list, int totalpage,String page_id) {
		this.totalPage = totalpage;
		this.page_id = page_id;
		if (list != null) {
			if (dataPage == 1)
				commentBeans = list;
			else
				commentBeans.addAll(list);
			removeAllFooter();
			if (dataPage >= totalPage) 
				lvComments.addFooterView(noMoreView, null, false);
			else 
				lvComments.addFooterView(loadingView, null, false);
		} else {
			removeAllFooter();
			lvComments.addFooterView(noMoreView, null, false);
		}
		
		commentAdapter.setData(commentBeans,true);
	}

	/**
	 * 移除所以footer
	 */
	private void removeAllFooter() {
		lvComments.removeFooterView(loadingView);
		lvComments.removeFooterView(noNetView);
		lvComments.removeFooterView(noMoreView);
	}

	/**
	 * 发送评论 回复
	 * 
	 * @param msg
	 */
	private void postComment(final String msg) {
		if (!Util.checkNetWork(this)) {
			Util.show(this, "网络连接失败");
			return;
		}
		showPro();
		TopicReplyPostNewParamClass mParam = new TopicReplyPostNewParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(activity);
		mParam.p_message = msg;
		mParam.tid = detailBean.getTid();
		if (isComment)
			mParam.p_touid = "";
		else{
			mParam.p_touid = tempReplayUId;
			mParam.pid=tempRelayPId;
		}
		new CommunityProcClass(activity).topicReplyPost(mParam,
				new InterfaceTopicReplyPostNewCallBack() {

					@Override
					public void onSuccess(int icode, String strSuccMsg, String pid) {
						cancelPro();
						Util.show(activity, "评论成功");
						ArticleCommentBean commentBean = new ArticleCommentBean();
						commentBean.setPid(pid);
						commentBean.setIs_zj(PreManager.instance()
								.getUserIsExpert(getApplicationContext()));
						commentBean.setP_uid(PreManager.instance().getUserId(
								activity));
						commentBean.setP_author(PreManager.instance()
								.getUserNickname(activity));
						commentBean.setP_dateline(String
								.valueOf((int) (new Date().getTime() / 1000)));
						commentBean.setTouxiang(PreManager.instance()
								.getUserProfileUrl(activity));
						commentBean.setTouxiang_key(PreManager.instance()
								.getUserProfileKey(activity));
						commentBean.setP_message(msg);

						if (isComment) {
							commentBean.setP_touid("");
							commentBean.setP_toauthor("");
						} else {
							commentBean.setP_touid(tempReplayUId);
							commentBean.setP_toauthor(tempReplayUName);
						}
						if (commentBeans == null)
							commentBeans = new ArrayList<ArticleCommentBean>();
						commentBeans.add(0, commentBean);
						
						if (dataPage>= totalPage) {
							removeAllFooter();
							lvComments.addFooterView(noMoreView, null,false);
						}
						
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								commentAdapter.setData(commentBeans,true);
							}
						}, 200);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						cancelPro();
						Util.show(activity, "评论失败");
					}
				});
	}

	/**
	 * 点赞
	 */
	private void praise() {
		if (!Util.checkNetWork(this)) {
			Util.show(this, "网络连接失败");
			return;
		}
		if ("1".equals(this.detailBean.getStatus())) {
			Util.show(activity, "已经点赞");
			return;
		}
		
		try {
			detailBean.setT_zans(String.valueOf(Integer.parseInt(detailBean.getT_zans()) + 1));
			detailBean.setStatus("1");
			praiseCheck(true, opPraise);
		} catch (Exception e) {
		}
		
		ArticleThumbUpUserBean bean = new ArticleThumbUpUserBean();
		bean.setUid(PreManager.instance().getUserId(activity));
		bean.setAuthor_profile(PreManager.instance().getUserProfileUrl(activity));
		bean.setAuthor_profile_key(PreManager.instance().getUserProfileKey(activity));
		bean.setNickname(PreManager.instance().getUserNickname(activity));
		List<ArticleThumbUpUserBean> zanList = detailBean.getT_zan_user();
		if (zanList == null)
			zanList = new ArrayList<ArticleThumbUpUserBean>();
		zanList.add(0, bean);
		detailBean.setT_zan_user(zanList);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				commentAdapter.setHeadData(true,detailBean.getT_zan_user(),detailBean.getT_zans(), "1");
			}
		}, 500);
		
		TopicPraiseParamClass mParam = new TopicPraiseParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(activity);
		mParam.tid = detailBean.getTid();
		new CommunityProcClass(this).topicPraise(mParam,
				new InterfaceTopicPraiseCallBack() {
					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						commentAdapter.setHeadData(true,detailBean.getT_zan_user(),detailBean.getT_zans(), "1");
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						praiseCheck(false, opPraise);
						Util.show(activity, "点赞失败");
						List<ArticleThumbUpUserBean> zanList = detailBean.getT_zan_user();
						if(zanList !=null && zanList.size() > 0){
							int index = getPraiseIndex(zanList, PreManager.instance().getUserId(activity));
							if(index >= 0){
								zanList.remove(index);
								try {
									detailBean.setT_zans(String.valueOf(Integer.parseInt(detailBean.getT_zans()) - 1));
									detailBean.setStatus("0");
									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											commentAdapter.setHeadData(true,detailBean.getT_zan_user(),detailBean.getT_zans(), "0");
										}
									}, 200);
								} catch (Exception e) {
								}
							}
						}
					}
				});

	}
	
	/**
	 * 根据id判断
	 * @param zanList
	 * @param uid
	 * @return
	 */
	private int getPraiseIndex(List<ArticleThumbUpUserBean> zanList, String uid){
		int  size = zanList.size();
		for(int i =0; i < size; i++){
			if(uid.equals(zanList.get(i).getUid())){
				return i;
			}
		}
		return -1;
	}

	/**
	 * 分享话题
	 */
	private void shareTopice() {
		String title=(TextUtils.isEmpty(detailBean.getT_subject())) ? titleS: detailBean.getT_subject();
		String url=URLUtil.BASEURL+"/share/hd/articledetail.php?tid="+detailBean.getTid()+"&uid="+PreManager.instance().getUserId(activity);
		String intro=detailBean.getIntro();
		String summary=intro.length() > 40 ? intro.substring(0,40) :intro;
		
		Intent intent = new Intent(this,ShareActivity.class);
		ShareClassParam param = new ShareClassParam();
		param.targetUrl = url;
		param.shareTitle = title;
		param.shareSummary = summary;
		if(imagesUrl!=null && !imagesUrl.isEmpty())
			param.sharePictureUrl = imagesUrl.get(0);
		else
			param.sharePictureUrl = "";
		intent.putExtra("from", Constant.SHARE_FROM_COMMUNITY_EVENT);
		intent.putExtra("shareData", param);
		startActivity(intent);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Util.hideToast();
			hideKeyboardB(etInput);
			return  super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			Util.hideToast();
			hideKeyboardB(etInput);
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.op_comment:
			isGotoIndex=true;
			isShowInput=true;
			showKeyboard(etInput);
			AnimationUtil.rotationForChangeAnimation(rlOp, rlInput);
			if (android.os.Build.VERSION.SDK_INT >= 8) 
				lvComments.smoothScrollToPosition(1);
			else 
				lvComments.setSelection(1);
			break;
		case R.id.iv_praise:
		case R.id.op_praise:
			if (detailBean != null) {
				AnimationUtil.btnClickEffect(opPraise);
				praise();
			}
			break;
		case R.id.iv_share:
		case R.id.op_share:
			if (detailBean != null)
				shareTopice();
			break;
		case R.id.btn_sendmesage:
			String msg = etInput.getText() == null ? "" : etInput.getText().toString();
			if (TextUtils.isEmpty(msg)) {
				Util.show(activity, "请输入内容");
				return;
			}
			if (detailBean != null)
				postComment(msg);
			etInput.setText("");
			emojicon.setVisibility(View.GONE);
			hideKeyboardB(etInput);
			break;
		case R.id.btn_face:
			hideKeyboardB(etInput);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if (emojicon.getVisibility() != View.VISIBLE)
						emojicon.setVisibility(View.VISIBLE);
					else
						emojicon.setVisibility(View.GONE);
				}
			}, 200);

			break;
		case R.id.iv_userhead:
			startActivity(new Intent(activity, PersonalInfoActivity.class).putExtra("user_id", detailBean.getUid()));
			break;
		case R.id.topic_content_image:
			if (detailBean!=null && !detailBean.getImages().isEmpty()) {
				startActivity(new Intent(activity,ImageDetailActivity.class).putExtra("img_list", (Serializable) detailBean.getImages()));
			}
			break;
		case R.id.ib_more:
			moreOperator();
			break;
		case R.id.goto_top:
			isGotoTop=true;
			if (android.os.Build.VERSION.SDK_INT >= 8) 
				lvComments.smoothScrollToPosition(0);
			else 
				lvComments.setSelection(0);
			break;
		default:
			break;
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled") 
	class CustomWebClient extends WebViewClient{
		public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            return super.shouldOverrideUrlLoading(view, url);  
        }  
  
        @Override  
        public void onPageFinished(WebView view, String url) {  
            view.getSettings().setJavaScriptEnabled(true);  
            super.onPageFinished(view, url);
            new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					  if (detailBean.getConnect_group() != null && detailBean.getConnect_group().size() > 0) {
							View view = headView.findViewById(R.id.rl_related_title);
							view.setVisibility(View.VISIBLE);
							CustomListView relatedGroups = (CustomListView) headView.findViewById(R.id.related_groups);
							groupDatas = detailBean.getConnect_group();
							relatedGroups.setAdapter(new RecommendGropuAdapter(activity, getScreenWidth(), groupDatas));
							relatedGroups.setVisibility(View.VISIBLE);
							relatedGroups.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
									Intent mIntent=new Intent(activity,TeamDetailsActivity.class);
									ArticleCorrelatGroupBean group = groupDatas.get(position);
									CommunityGroupBean bean =new CommunityGroupBean();
									bean.setG_ico(group.getG_ico());
									bean.setG_ico_key(group.getG_ico_key());
									bean.setG_name(group.getG_name());
									bean.setGid(group.getGid());
									mIntent.putExtra("bean", bean);
									startActivity(mIntent);
								}
							});
						} else 
							headView.findViewById(R.id.rl_related_title).setVisibility(View.GONE);
					  isLoadSuc=true;
			          commentAdapter.setHeadData(true,detailBean.getT_zan_user(),detailBean.getT_zans(), detailBean.getStatus());
			          getCommentList();
				}
			}, 1000);
            addImageClickListner();
        	view.getSettings().setBlockNetworkImage(false);
        }  
  
        @Override  
        public void onPageStarted(WebView view, String url, Bitmap favicon) {  
            view.getSettings().setJavaScriptEnabled(true);  
            super.onPageStarted(view, url, favicon);  
        }  
  
        @Override  
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
            super.onReceivedError(view, errorCode, description, failingUrl);  
        }  
	}
	
    // 注入js函数监听  
    private void addImageClickListner() {  
    	content.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "	   window.imagelistner.imagsUrl( objs[i].src);"
                + "    objs[i].onclick=function()  " + "    {  "
                + "       window.imagelistner.openImage(this.src);  "
                + "    }" 
                + "}" 
                + "})()"); 
    }  
    
    // js通信接口  
    private class JavascriptInterface {  
        public JavascriptInterface(Context context) {  
        }  
  
        @android.webkit.JavascriptInterface
        public void openImage(String img) { 
        }  
        
        @android.webkit.JavascriptInterface
        public void imagsUrl(String img) {  
        	if(imagesUrl==null){
        		imagesUrl=new ArrayList<String>();
        	}
        	imagesUrl.add(img);
        }  
    } 

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.lv_comments:
			emojicon.setVisibility(View.GONE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					hideKeyboardB(etInput);
				}
			}, 200);
			return false;
		case R.id.edt_mesage:
			isShowInput=true;
			emojicon.setVisibility(View.GONE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					showKeyboard(etInput);
				}
			}, 200);
			return false;
		default:
			break;
		}
		return false;
	}

	@Override
	public void listItemInClick(int type, int position, ArticleCommentBean bean) {
		final Intent mIntent = new Intent(activity, PersonalInfoActivity.class);
		switch (type) {
		case 0:
			mIntent.putExtra("user_id", bean.getP_touid());
			break;
		case 1:
		case 2:
			mIntent.putExtra("user_id", bean.getP_uid());
			break;
		case 3:
			if (detailBean != null)
				praise();
			break;
		case 4:
			if (detailBean != null)
				shareTopice();
			break;
		
		default:
			break;
		}
		if (mIntent != null && type <= 2){
			hideKeyboardB(etInput);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(mIntent);
				}
			}, 200);
		}
	}
	
	@Override
	public void onItemClick(final ArticleCommentBean bean) {
		if (PreManager.instance().getUserId(activity).equals(bean.getP_uid())) {
			isComment = true;
			tempReplayUId = "";
			tempReplayUName = "";
			tempRelayPId = "";
			etInput.setText("");
			etInput.setHint("说点什么呗");
			showDialog(bean);
		} else {
			isComment = false;
			isShowInput=true;
			tempReplayUId = bean.getP_uid();
			tempReplayUName = bean.getP_author();
			tempRelayPId = bean.getPid();
			etInput.setHint("回复 " + bean.getP_author());
			if(emojicon.getVisibility()!=View.VISIBLE)
				showKeyboard(etInput);
		}
	}
	
	private void showDialog(final ArticleCommentBean bean){
		final int index=getIndext(bean.getPid());
		if (index < 0) {
			return;
		}
		commentDialog  = new MyAlertDialog(this,R.style.bottom_animation);
		commentDialog.setCanceledOnTouchOutside(true);
		commentDialog.show();
		commentDialog.setTV1("",View.GONE);
		commentDialog.setTV2("", null, View.GONE);
		
		String userId=PreManager.instance().getUserId(activity);
		String pUserId=bean.getP_uid();
		commentDialog.setTV3("复制", new MyOnClickListener() {
			@Override
			public void Onclick(View v) {
				clipboard.setText(bean.getP_message());
				if(commentDialog!=null){
					commentDialog.dismiss();
					commentDialog = null;
				}
			}
		}, View.VISIBLE);
		
		commentDialog.setTVbottom("取消", new MyOnClickListener() {
			
			@Override
			public void Onclick(View v) {
				if(commentDialog!=null){
					commentDialog.dismiss();
					commentDialog = null;
				}
			}
		}, View.VISIBLE);
		
		commentDialog.setTV4("删除", new MyOnClickListener() {
			
			@Override
			public void Onclick(View v) {
				deleteComment(index, bean.getPid());
				if(commentDialog!=null){
					commentDialog.dismiss();
					commentDialog = null;
				}
			}
		}, (userId.equals(pUserId))? View.VISIBLE : View.GONE);
	}
	
	/**
	 * 获取评论在所在位置
	 * @param id
	 * @return
	 */
	private int getIndext(String id){
		int size=commentBeans.size();
		for(int i =0; i< size; i++){
			if(commentBeans.get(i).getPid().equals(id)){
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void onItemLongClick(final int index,final ArticleCommentBean bean) {
		commentDialog  = new MyAlertDialog(this,R.style.bottom_animation);
		commentDialog.setCanceledOnTouchOutside(true);
		commentDialog.show();
		commentDialog.setTV1("",View.GONE);
		commentDialog.setTV2("", null, View.GONE);
		
		String userId=PreManager.instance().getUserId(activity);
		String pUserId=bean.getP_uid();
		commentDialog.setTV3("复制", new MyOnClickListener() {
			@Override
			public void Onclick(View v) {
				clipboard.setText(bean.getP_message());
				if(commentDialog!=null){
					commentDialog.dismiss();
					commentDialog = null;
				}
			}
		}, View.VISIBLE);
		
		commentDialog.setTVbottom("取消", new MyOnClickListener() {
			
			@Override
			public void Onclick(View v) {
				if(commentDialog!=null){
					commentDialog.dismiss();
					commentDialog = null;
				}
			}
		}, View.VISIBLE);
		
		commentDialog.setTV4("删除", new MyOnClickListener() {
			
			@Override
			public void Onclick(View v) {
				deleteComment(index, bean.getPid());
				if(commentDialog!=null){
					commentDialog.dismiss();
					commentDialog = null;
				}
			}
		}, (userId.equals(pUserId))? View.VISIBLE : View.GONE);
	
	}
	
	@Override //跳转点赞
	public void onItemHeadClick(int headPosition) {
		Intent intent = new Intent(activity,GroupThumbActivity.class);
		intent.putExtra("tid", detailBean.getTid());
		startActivity(intent);
	}
	
	/**
	 * 更多操作
	 */
	private  MyAlertDialog operatorDialog;
	private void moreOperator() {
		operatorDialog  = new MyAlertDialog(this,
				R.style.bottom_animation);
		operatorDialog.show();
		operatorDialog.setTV1("",View.GONE);
		operatorDialog.setTV2("删除话题", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				operatorDialog.dismiss();
				deltetTopic();
			}
		}, View.VISIBLE);
		operatorDialog.setTV3("", null, View.GONE);
		operatorDialog.setTV4("", null, View.GONE);
		operatorDialog.setTVbottom("取消", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				if(operatorDialog!=null){
					operatorDialog.dismiss();
					operatorDialog = null;
				}
			}

		}, View.VISIBLE);
	}
	
	private void deltetTopic(){
		TopicDeleteParamClass mParam = new TopicDeleteParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(CommunityTopiceDetailActivity.this);
		mParam.tid = topicesId;
		new CommunityProcClass(CommunityTopiceDetailActivity.this).topicDelete(mParam, new InterfaceTopicDeleteCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				Util.show(CommunityTopiceDetailActivity.this, "删除成功");
				Intent intent = new Intent();
				intent.putExtra("tid", topicesId);
				setResult(Constant.RESULTCODE, intent);
				AppManager.getAppManager().finishActivity();
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				
			}
		});
	}
}
