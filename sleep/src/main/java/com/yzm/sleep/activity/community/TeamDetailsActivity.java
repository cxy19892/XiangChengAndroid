package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.adapter.FragmentPage3FoundAdapter;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.GroupHotNewListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGroupHotNewListCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.StickyLayout;
import com.yzm.sleep.widget.StickyLayout.OnGiveUpTouchEventListener;

/**
 * 小组子界面
 * 
 * CommunityGroupBean bean 小组实体类
 * 
 * @author Administrator
 * 
 */
public class TeamDetailsActivity extends BaseActivity implements
		OnCheckedChangeListener, OnGiveUpTouchEventListener {
	private CircleImageView team_group_icon;
	private TextView team_groupName;
	private ListView mListView;
	private List<ArticleBean> hotList;
	private List<ArticleBean> freshList;
	private Button btnTop;
	private int page = 0;
	private int tota_lpage;
	private String type = "1";

	private FragmentPage3FoundAdapter mAdapter;

	private Button btnPush;
	private CommunityGroupBean bean;

	private View viewLoding;
	private View noMore;
	private View View_notnew;
	private View headerLine;
	private RadioGroup rg;
	private boolean isHttpCode = true;
	private RelativeLayout sticky_header_rela;
	private int type_Poss1, type_poss2;// 分别记录个集合滑动的位置

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team);
		bean = (CommunityGroupBean) getIntent().getSerializableExtra("bean");

		((StickyLayout) findViewById(R.id.sticky_layout))
				.setOnGiveUpTouchEventListener(this);
		initView();
	}

	private void initView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		noMore = inflater.inflate(R.layout.listview_footer_nomore, null);
		viewLoding = inflater.inflate(R.layout.view_loding, null);
		View_notnew = inflater.inflate(R.layout.layout_no_net, null);

		sticky_header_rela = (RelativeLayout) findViewById(R.id.sticky_header_rela);
		sticky_header_rela.setOnClickListener(this);

		hotList = new ArrayList<ArticleBean>();
		freshList = new ArrayList<ArticleBean>();
		((Button) findViewById(R.id.back)).setOnClickListener(this);

		rg = (RadioGroup) findViewById(R.id.group_list_sort);
		headerLine = findViewById(R.id.group_sort_line);
		rg.setOnCheckedChangeListener(this);

		btnTop = (Button) findViewById(R.id.but_groupzd);
		btnTop.setOnClickListener(this);
		btnPush = (Button) findViewById(R.id.btn_title_right2);
		btnPush.setVisibility(View.VISIBLE);
		btnPush.setText("发布");
		btnPush.setOnClickListener(this);
		team_group_icon = (CircleImageView) findViewById(R.id.team_group_icon);
		team_groupName = (TextView) findViewById(R.id.team_groupName);
		mListView = (ListView) findViewById(R.id.my_expanlist);
		mAdapter = new FragmentPage3FoundAdapter(TeamDetailsActivity.this,
				getScreenWidth(), -1);
		mListView.addFooterView(noMore, null, false);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position - mListView.getHeaderViewsCount();
				Intent intent;
				if (PreManager.instance().getIsLogin(TeamDetailsActivity.this)) {
					ArticleBean bean = mAdapter.getData().get(index);
					intent = new Intent(TeamDetailsActivity.this,
							CommunityTopiceDetailActivity.class);
					intent.putExtra("topices_id", bean.getTid());
					intent.putExtra("topices_title", bean.getT_subject());
					intent.putExtra("author_id", bean.getUid());
					TeamDetailsActivity.this
							.startActivityForResult(intent, 201);
				} else {
					intent = new Intent(TeamDetailsActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}

			}
		});
		mListView.setAdapter(mAdapter);
		mListView.removeFooterView(noMore);
		if (bean != null && !bean.isIscode()) {
			team_groupName.setText(bean.getG_name());
			ImageLoader.getInstance().displayImage(bean.getG_ico(),
					bean.getG_ico_key(), team_group_icon,
					MyApplication.headPicOptn);
		}
		if (bean != null) {
			((TextView) findViewById(R.id.title)).setText(bean.getG_name());
		}
		View_notnew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showProgress(1);
				HttpMyData(false, type);
			}
		});

		/**
		 * 上啦加载更多
		 */
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}

			@Override
			public void onScrollStateChanged(AbsListView listview,
					int scrollState) {
				if (type.equals("1")) {
					type_Poss1 = mListView.getFirstVisiblePosition();
				} else {
					type_poss2 = mListView.getFirstVisiblePosition();
				}
				// 判断是否滚动到底部
				if (listview.getLastVisiblePosition() == listview.getCount() - 1) {
					// 加载更多功能的代码
					if (page < tota_lpage && isHttpCode) {
						showProgress(1);
						isHttpCode = false;
						HttpMyData(true, type);
					}
				}
				switch (scrollState) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
					if (type.equals("1")) {
						if (hotList.size() > 0) {
							if (mListView.getFirstVisiblePosition() >= 1) {
								btnTop.setVisibility(View.VISIBLE);
							}
							// 判断滚动到顶部
							if (mListView.getFirstVisiblePosition() == 0) {
								btnTop.setVisibility(View.GONE);
							}
						}
					} else {
						if (hotList.size() > 0) {
							if (mListView.getFirstVisiblePosition() >= 1) {
								btnTop.setVisibility(View.VISIBLE);
							}
							// 判断滚动到顶部
							if (mListView.getFirstVisiblePosition() == 0) {
								btnTop.setVisibility(View.GONE);
							}
						}
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
					break;
				default:
					break;
				}
			}
		});
		showProgress(1);
		HttpMyData(false, type);
	}

	//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_right2:// 发布
			if (bean != null) {
				Intent in = new Intent(TeamDetailsActivity.this,
						PostTopicsActivity.class);
				in.putExtra("groupName", bean.getG_name());
				in.putExtra("groupId", bean.getGid());
				startActivityForResult(in, 1);
			} else {
				ToastManager.getInstance(getApplicationContext()).show(
						"获取小组信息失败");
			}
			break;
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.sticky_header_rela:
			if (bean != null) {
				Intent intent = new Intent(TeamDetailsActivity.this,
						GroupInfoActivity.class);
				intent.putExtra("groupId", bean.getGid());
				startActivity(intent);
			} else {
				ToastManager.getInstance(getApplicationContext()).show(
						"获取小组信息失败");
			}
			break;
		case R.id.but_groupzd:
			if (android.os.Build.VERSION.SDK_INT >= 8) {
				mListView.smoothScrollToPosition(0);
			} else {
				mListView.setSelection(0);
			}
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			type = "2";
			rg.check(R.id.group_fresh);
			headerLine.animate().translationX(headerLine.getWidth())
					.setDuration(200).start();
			HttpMyData(false, type);
		} else if (requestCode == 201 && resultCode == Constant.RESULTCODE) {
			if (type.equals("1")) {
				String id = data.getStringExtra("tid");
				if (id != null && !id.equals("")) {
					for (int i = 0; i < hotList.size(); i++) {
						if (id.equals(hotList.get(i).getTid())) {
							hotList.remove(i);
						}
					}
					mAdapter.setData(hotList);
				}
				rg.check(R.id.group_hottest);
				headerLine.animate().translationX(0).setDuration(200).start();
			} else {
				String id = data.getStringExtra("tid");
				if (id != null && !id.equals("")) {
					for (int i = 0; i < freshList.size(); i++) {
						if (id.equals(freshList.get(i).getTid())) {
							freshList.remove(i);
						}
					}
					mAdapter.setData(freshList);
				}
				rg.check(R.id.group_fresh);
				headerLine.animate().translationX(headerLine.getWidth())
						.setDuration(200).start();
			}
		}
	}

	private void showProgress(int pos) {
		mListView.removeFooterView(viewLoding);
		mListView.removeFooterView(noMore);
		mListView.removeFooterView(View_notnew);
		if (pos == 1) {
			mListView.addFooterView(viewLoding, null, false);
		} else if (pos == 2) {
			mListView.addFooterView(noMore, null, false);
		} else if (pos == 3) {
			mListView.addFooterView(View_notnew, null, false);
		}
	}

	/**
	 * 
	 * @param chd
	 * @param type
	 *            1 = 最热 2 = 最新
	 */
	private void HttpMyData(final boolean chd, final String type) {
		if (chd) {
			page++;
		} else {
			page = 1;
		}
		// 请求网络之前先判断网络是否可以用
		if (!Util.checkNetWork(this)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					showProgress(-1);
					Util.show(TeamDetailsActivity.this, "请检查您的网络");
					if (type.equals("1") && hotList.size() > 0) {
						mAdapter.setData(hotList);
					} else if (type.equals("2") && freshList.size() > 0) {
						mAdapter.setData(freshList);
					} else {
						showProgress(3);
					}
				}
			}, 300);
			return;
		}
		GroupHotNewListParamClass articleListParamClass = new GroupHotNewListParamClass();
		articleListParamClass.page = page + "";
		articleListParamClass.my_int_id = PreManager.instance().getUserId(
				TeamDetailsActivity.this);
		articleListParamClass.pagesize = 10 + "";
		articleListParamClass.gid = bean.getGid();
		articleListParamClass.flag = type;
		new XiangchengProcClass(TeamDetailsActivity.this).groupHotNewList(
				articleListParamClass, new InterfaceGroupHotNewListCallback() {

					@Override
					public void onSuccess(int icode,
							CommunityGroupBean group_info,
							List<ArticleBean> article_list, int totalpage) {
						showProgress(-1);
						tota_lpage = totalpage;
						setData(article_list, chd, group_info);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						showProgress(-1);
						if (!Util.checkNetWork(TeamDetailsActivity.this)) {
							mAdapter.setData(hotList);
						} else {
							Util.show(TeamDetailsActivity.this, strErrMsg);
						}
					}
				});
	}

	private void setData(List<ArticleBean> article_list, boolean chd,
			CommunityGroupBean beans) {
		if (bean.isIscode()) {
			team_groupName.setText(beans.getG_name());
			ImageLoader.getInstance().displayImage(beans.getG_ico(),
					beans.getG_ico_key(), team_group_icon,
					MyApplication.headPicOptn);
			((TextView) findViewById(R.id.title)).setText(beans.getG_name());
		}
		mListView.removeFooterView(noMore);
		if (type.equals("1")) {
			if (chd) {
				hotList.addAll(article_list);
			} else {
				hotList.clear();
				hotList.addAll(article_list);
			}
			if (hotList != null) {
				mAdapter.setData(hotList);
			}
		} else {
			if (chd) {
				freshList.addAll(article_list);
			} else {
				freshList.clear();
				freshList.addAll(article_list);
			}
			if (freshList != null) {
				mAdapter.setData(freshList);
			}
		}
		if (page >= tota_lpage) {
			if (type.equals("1") && hotList.size() > 1) {
				showProgress(2);
			} else if (type.equals("2") && freshList.size() > 1) {
				showProgress(2);
			}
		} else {
			isHttpCode = true;
		}
	}

	@Override
	public boolean giveUpTouchEvent(MotionEvent event) {
		if (mListView.getFirstVisiblePosition() == 0) {
			View view = mListView.getChildAt(0);
			if (view != null && view.getTop() >= 0) {
				return true;
			}
		}
		return false;
	}

	@SuppressLint("NewApi")
	@Override
	public void onCheckedChanged(RadioGroup rGroup, int btnId) {
		switch (btnId) {
		case R.id.group_hottest: // 选择最热
			headerLine.animate().translationX(0).setDuration(200).start();
			nowkData(true);
			break;

		case R.id.group_fresh: // 选择最新
			headerLine.animate().translationX(headerLine.getWidth())
					.setDuration(200).start();
			nowkData(false);
			break;

		default:
			break;
		}
	}

	/**
	 * 切换时判断是否有数据在请求网络
	 * 
	 * @param isnowk
	 */
	private void nowkData(boolean isnowk) {
		if (isnowk) {
			btnTop.setVisibility(View.GONE);
			if (hotList.size() == 0) {
				mAdapter.setData(hotList);
				type = "1";
				showProgress(1);
				HttpMyData(false, type);
			} else {
				mAdapter.setData(hotList);
				mListView.setSelection(type_Poss1);
			}
		} else {
			btnTop.setVisibility(View.GONE);
			if (freshList.size() == 0) {
				mAdapter.setData(freshList);
				showProgress(1);
				type = "2";
				HttpMyData(false, type);
			} else {
				mAdapter.setData(freshList);
				mListView.setSelection(type_poss2);
			}
		}

	}
}
