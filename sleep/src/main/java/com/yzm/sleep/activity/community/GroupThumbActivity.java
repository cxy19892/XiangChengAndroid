package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.yzm.sleep.bean.ThumbUpUserBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ArticleThumbUpUserParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceArticleThumbUpUserCallback;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 点赞人列表
 * 
 * tid 文章ID
 * 
 * @author Administrator
 * 
 */
public class GroupThumbActivity extends BaseActivity {
	private MaterialRefreshLayout mRefresh;
	private ListView mElistview;
	private FocusnumAdapter mAdapter;
	private int page = 0;
	private String tid;
	private int tota_lpage = 1;
	private boolean isHttpCode = false;
	private List<ThumbUpUserBean> userList;
	private RelativeLayout propessbarTeam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_focusnmber);
		tid = getIntent().getStringExtra("tid");
		initView();
	}

	private void initView() {
		userList = new ArrayList<ThumbUpUserBean>();
		((Button) findViewById(R.id.back)).setOnClickListener(this);
		((TextView) findViewById(R.id.title)).setText("点赞人");
		TextView textView = new TextView(GroupThumbActivity.this);
		textView.setHeight(0);
		propessbarTeam = (RelativeLayout) findViewById(R.id.propessbarTeam);
		mRefresh = (MaterialRefreshLayout) findViewById(R.id.focu_mian_refresh);
		mElistview = (ListView) findViewById(R.id.focu_expanlist);
		mElistview.addHeaderView(textView);
		mElistview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mAdapter = new FocusnumAdapter();
		mElistview.setAdapter(mAdapter);
		isHttpCode = true;
		mElistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position - mElistview.getHeaderViewsCount();
				if(index >= 0){
					Intent intent = new Intent(GroupThumbActivity.this, PersonalInfoActivity.class);
					intent.putExtra("user_id", userList.get(index).getUid());
					startActivity(intent);
				}
			}
		});
		/**
		 * 下啦刷新
		 */
		mRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				HttpExpendData(false);
			}
		});
		/**
		 * 上啦加载更多
		 */
		mElistview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (mElistview.getChildCount() > 0) {
						View lastItemView = (View) mElistview
								.getChildAt(mElistview.getChildCount() - 1);
						if ((mElistview.getBottom()) == lastItemView
								.getBottom()) {
							if (page < tota_lpage && isHttpCode) {
								propessbarTeam.setVisibility(View.VISIBLE);
								isHttpCode = false;
								HttpExpendData(true);
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

		mRefresh.setOnClickRequestListener(new OnClickRequestListener() {

			@Override
			public void setRequest() {
				mRefresh.autoRefresh();
			}
		});
		mRefresh.autoRefresh();

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

	/**
	 * 访问网络加载数据(获取列表数据)
	 * 
	 * @param pageSize
	 *            每一页数量
	 * @param chd
	 *            (是刷新还是加载) true = 加载 、、、、、 falus 刷新
	 */
	private void HttpExpendData(final boolean chd) {
		if (chd) {
			page++;
		} else {
			page = 1;
		}
		// 请求网络之前先判断网络是否可以用
		if (!Util.checkNetWork(this)) {
			mRefresh.finishRefresh();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					isHttpCode = true;
					userList.clear();// 清除数据
					mRefresh.addListViewState(mElistview, -1);
					mRefresh.addListViewState(mElistview, Constant.NO_NET);
					if (!chd) {
						mRefresh.finishRefresh();
					}
					Util.show(GroupThumbActivity.this, "请检查您的网络");
					mAdapter.notifyDataSetChanged();
				}
			}, 300);
			return;
		}
		// 获取列表数据
		ArticleThumbUpUserParamClass accessAttentionUserListParamClass = new ArticleThumbUpUserParamClass();
		accessAttentionUserListParamClass.tid = tid;
		accessAttentionUserListParamClass.page = page + "";
		accessAttentionUserListParamClass.pagesize = 10 + "";
		new XiangchengProcClass(GroupThumbActivity.this)
				.articleThumbUpUserLists(accessAttentionUserListParamClass,
						new InterfaceArticleThumbUpUserCallback() {
							@Override
							public void onSuccess(int icode,
									List<ThumbUpUserBean> user_list,
									int totalpage) {
								mRefresh.finishRefresh();
								tota_lpage = totalpage;
								if (page >= totalpage) {
									isHttpCode = false;
									mRefresh.addListViewState(mElistview,
											Constant.NO_MORE);
								} else {
									isHttpCode = true;
								}
								if (user_list != null && user_list.size() > 0) {
									if (chd) {
										userList.addAll(user_list);
									} else {
										userList.clear();
										userList.addAll(user_list);
									}
									mAdapter.notifyDataSetChanged();
								} else {
									if (userList.size() == 0) {
										mRefresh.addListViewState(mElistview,
												Constant.NO_DATA);
									}
								}
								propessbarTeam.setVisibility(View.GONE);
							}

							@Override
							public void onError(int icode, String strErrMsg) {
								mRefresh.finishRefresh();
								propessbarTeam.setVisibility(View.GONE);
								if (userList.size() > 0) {
									toastMsg(strErrMsg);
								} else {
									mRefresh.addListViewState(mElistview,
											Constant.NO_DATA);
								}
							}
						});
	}

	private class FocusnumAdapter extends BaseAdapter {
		LayoutInflater inflater;

		public FocusnumAdapter() {
			inflater = LayoutInflater.from(GroupThumbActivity.this);
		}

		@Override
		public int getCount() {
			return userList.size();
		}

		@Override
		public Object getItem(int position) {
			return userList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertview, ViewGroup parent) {
			ViewHolder holder;
			if (convertview == null) {
				holder = new ViewHolder();
				convertview = inflater.inflate(R.layout.item_focusnumber, null);
				holder.fo_group_icon = (CircleImageView) convertview
						.findViewById(R.id.fo_group_icon);
				holder.fo_groupName = (TextView) convertview
						.findViewById(R.id.fo_groupName);
				holder.fo_time = (TextView) convertview
						.findViewById(R.id.fo_time);
				holder.tvGroupUserType = (ImageView) convertview
						.findViewById(R.id.tvGroupUserType);

				convertview.setTag(holder);
			} else {
				holder = (ViewHolder) convertview.getTag();
			}

			holder.fo_groupName.setText(userList.get(position).getName());
			try {
				holder.fo_time.setText(TimeFormatUtil
						.getTimeBeforeCurrentTime(userList.get(position)
								.getZ_dateline()));
			} catch (Exception e) {

			}
			if (userList.get(position).getIs_zj().equals("1")) {
				holder.tvGroupUserType.setVisibility(View.VISIBLE);
			} else {
				holder.tvGroupUserType.setVisibility(View.GONE);
			}

			ImageLoader.getInstance().displayImage(
					userList.get(position).getAuthor_profile(),
					userList.get(position).getAuthor_profile_key(),
					holder.fo_group_icon, MyApplication.headPicOptn);
			return convertview;
		}

	}

	public class ViewHolder {
		CircleImageView fo_group_icon;
		TextView fo_groupName;
		TextView fo_time;
		ImageView tvGroupUserType;
	}
}
