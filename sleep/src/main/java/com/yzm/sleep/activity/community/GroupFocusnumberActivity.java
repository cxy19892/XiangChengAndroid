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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.PersonalInfoActivity;
import com.yzm.sleep.bean.AccessAttentionUserBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.GroupAccessAttentionUserListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGroupAccessAttentionUserListCallback;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 关注人列表
 * 
 * @author Administrator
 * 
 */
public class GroupFocusnumberActivity extends BaseActivity {
	private MaterialRefreshLayout mRefresh;
	private ListView mElistview;
	private FocusnumAdapter mAdapter;
	private int page = 0;
	private String gid;
	private String flag;
	private int tota_lpage = 1;
	private boolean isHttpCode = false;
	private List<AccessAttentionUserBean> userList;
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_focusnmber);
		gid = getIntent().getStringExtra("gid");
		flag = getIntent().getStringExtra("flag");
		initView();
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title);
		if ("1".equals(flag)) {
			title.setText("访问过的小伙伴");
		} else {
			title.setText("已关注的小伙伴");
		}
		userList = new ArrayList<AccessAttentionUserBean>();
		((Button) findViewById(R.id.back)).setOnClickListener(this);
		// propessbarTeam = (RelativeLayout) findViewById(R.id.propessbarTeam);
		TextView textView = new TextView(GroupFocusnumberActivity.this);
		textView.setHeight(0);
		mRefresh = (MaterialRefreshLayout) findViewById(R.id.focu_mian_refresh);
		mElistview = (ListView) findViewById(R.id.focu_expanlist);
		mElistview.setDivider(null);
		mElistview.setHeaderDividersEnabled(false);
		mElistview.setFooterDividersEnabled(false);
		mElistview.setVerticalScrollBarEnabled(true);
		mElistview.setDividerHeight(0);
		mElistview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		mElistview.addHeaderView(textView);
		mAdapter = new FocusnumAdapter();
		mElistview.setAdapter(mAdapter);
		isHttpCode = true;
		mElistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position  - mElistview.getHeaderViewsCount();
				Intent intent = new Intent(GroupFocusnumberActivity.this,
						PersonalInfoActivity.class);
				intent.putExtra("user_id", userList.get(index).getUid());
				startActivity(intent);
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
								mRefresh.addListViewState(mElistview, 0);
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
					Util.show(GroupFocusnumberActivity.this, "请检查您的网络");
					mAdapter.notifyDataSetChanged();
				}
			}, 300);
			return;
		}
		// 获取列表数据
		GroupAccessAttentionUserListParamClass accessAttentionUserListParamClass = new GroupAccessAttentionUserListParamClass();
		accessAttentionUserListParamClass.flag = flag;
		accessAttentionUserListParamClass.gid = gid;
		accessAttentionUserListParamClass.page = page + "";
		accessAttentionUserListParamClass.pagesize = 10 + "";
		new XiangchengProcClass(GroupFocusnumberActivity.this)
				.groupAccessAttentionUserList(
						accessAttentionUserListParamClass,
						new InterfaceGroupAccessAttentionUserListCallback() {

							@Override
							public void onSuccess(int icode,
									List<AccessAttentionUserBean> user_list,
									int totalpage) {
								mRefresh.addListViewState(mElistview, -1);
								mRefresh.finishRefresh();
								tota_lpage = totalpage;
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
								if (userList.size() > 0) {
									if (page >= tota_lpage) {
										isHttpCode = false;
										mRefresh.addListViewState(mElistview,
												-1);
										mRefresh.addListViewState(mElistview,
												Constant.NO_MORE);
									} else {
										isHttpCode = true;
									}
								}
							}

							@Override
							public void onError(int icode, String strErrMsg) {
								mRefresh.finishRefresh();
								mRefresh.addListViewState(mElistview, -1);
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
			inflater = LayoutInflater.from(GroupFocusnumberActivity.this);
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
				holder.tvGroupUserType = (ImageView) convertview.findViewById(R.id.tvGroupUserType);
				convertview.setTag(holder);
			} else {
				holder = (ViewHolder) convertview.getTag();
			}

			holder.fo_groupName.setText(userList.get(position).getName());
			LogUtil.i("masong", userList.get(position).getDateline());
			holder.fo_time.setText(TimeFormatUtil.getTimeBeforeCurrentTime(userList.get(position).getDateline()));
			ImageLoader.getInstance().displayImage(userList.get(position).getAuthor_profile(), userList.get(position).getAuthor_profile_key(), holder.fo_group_icon, MyApplication.headPicOptn);
			if(userList.get(position).getIs_zj().equals("1")){
				holder.tvGroupUserType.setVisibility(View.VISIBLE);
			}else{
				holder.tvGroupUserType.setVisibility(View.GONE);
			}
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
