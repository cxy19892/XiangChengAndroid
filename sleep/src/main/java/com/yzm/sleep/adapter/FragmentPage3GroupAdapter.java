package com.yzm.sleep.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.community.CommunityTopiceDetailActivity;
import com.yzm.sleep.activity.community.ImageDetailActivity;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.ArticleImageBean;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.widget.CustomGridView;

/**
 * FragmentPage3小组适配器
 * 
 * @author Administrator
 * 
 */
public class FragmentPage3GroupAdapter extends BaseAdapter {
	private List<ArticleBean> foundList;// 发现列表集合
	private Activity mContext;// 上下文
	private LayoutInflater inflater;
	private int sreenWidth;

	public FragmentPage3GroupAdapter(Activity mContext, int screenWidth) {
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		this.sreenWidth = screenWidth;
	}

	public void setData(List<ArticleBean> foundList) {
		this.foundList = foundList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return foundList == null ? 0 : foundList.size();
	}

	@Override
	public Object getItem(int position) {
		return foundList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup parent) {
		ViewHolder holder = null;
		final ArticleBean bean = foundList.get(position);
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.item_group, null);
			holder.tvGroupUserName = (TextView) contentView
					.findViewById(R.id.tvGroupUserName);
			holder.tvGroupNumber = (TextView) contentView
					.findViewById(R.id.tvGroupNumber);
			holder.tvGroupType = (ImageView) contentView
					.findViewById(R.id.tvGroupType);
			holder.tvGroupCont = (TextView) contentView
					.findViewById(R.id.tvGroupCont);
			holder.tvGroupName = (TextView) contentView
					.findViewById(R.id.tvGroupName);
			holder.lin_teamName = (LinearLayout) contentView
					.findViewById(R.id.lin_teamName);
			holder.customGridView = (CustomGridView) contentView
					.findViewById(R.id.gdvGroupgrid);
			holder.imGroupUserIcon = (CircleImageView) contentView
					.findViewById(R.id.imGroupUserIcon);
			holder.lin_grouppage3 = (LinearLayout) contentView.findViewById(R.id.lin_grouppage3);
			holder.imgGroupUrl = (ImageView) contentView
					.findViewById(R.id.imgGroupUrl);
			
			LayoutParams lp = (LayoutParams)holder.imgGroupUrl
					.getLayoutParams();
			lp.width = sreenWidth - 100;
			lp.height = (sreenWidth - 100) / 2;
			lp.setMargins(40, 20, 40, 0);
			holder.imgGroupUrl.setLayoutParams(lp);
			
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
			clearCache(holder);
		}
		if (position == 0) {
			holder.lin_teamName.setVisibility(View.VISIBLE);
		} else {
			holder.lin_teamName.setVisibility(View.GONE);
		}
		
		if(bean.getIs_zj().equals("1")){
			holder.tvGroupType.setVisibility(View.VISIBLE);
		}else{
			holder.tvGroupType.setVisibility(View.GONE);
		}
		
		if (bean.getT_subject() != null && !bean.getT_subject().equals(null)
				&& bean.getT_subject().length() > 0)
			holder.tvGroupName.setText(bean.getT_subject());
		else
			holder.tvGroupName.setText("香橙推荐");
		holder.tvGroupCont.setText(bean.getT_message());
		holder.tvGroupUserName.setText(bean.getT_author());
		holder.tvGroupNumber.setText(TimeFormatUtil.getTimeBeforeCurrentTime(bean.getT_dateline()));
		if (bean.getImages() != null) {
			if (bean.getImages().size() > 1) {
				holder.imgGroupUrl.setVisibility(View.GONE);
				holder.customGridView.setVisibility(View.VISIBLE);
				CommunityGridViewAdapter mAdapter = new CommunityGridViewAdapter(
						mContext, sreenWidth);
				holder.customGridView.setAdapter(mAdapter);
				mAdapter.setData(bean.getImages());
			} else {
				if (bean.getImages() != null && bean.getImages().size() > 0) {
					holder.imgGroupUrl.setVisibility(View.VISIBLE);
					holder.customGridView.setVisibility(View.GONE);
					ImageLoader.getInstance().displayImage(
							bean.getImages().get(0).getContent_attachment_sl(),
							bean.getImages().get(0).getT_attachment_key_sl(),
							holder.imgGroupUrl, MyApplication.defaultOption);
				} else {
					holder.imgGroupUrl.setVisibility(View.GONE);
					holder.customGridView.setVisibility(View.GONE);
				}
			}
		}

		ImageLoader.getInstance().displayImage(bean.getAuthor_profile(),
				bean.getAuthor_profile_key(), holder.imGroupUserIcon,
				MyApplication.headPicOptn);

		holder.imgGroupUrl.setOnClickListener(new FragmentOnCliKListener(position));
		holder.lin_grouppage3.setOnClickListener(new FragmentOnCliKListener(position));
		return contentView;
	}

	public class FragmentOnCliKListener implements OnClickListener{
		int postion;
		
		public FragmentOnCliKListener(int postion){
			
			this.postion = postion;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imgGroupUrl:
				ImageActivity(postion);
				break;
            case R.id.lin_grouppage3:
            	Intent intent;
				if (PreManager.instance().getIsLogin(mContext)) {
					intent = new Intent(mContext,
							CommunityTopiceDetailActivity.class);
					intent.putExtra("topices_id", foundList
							.get(postion).getTid());
					intent.putExtra("topices_title",
							foundList.get(postion).getT_subject());
					intent.putExtra("is_choiceness", "2".equals(foundList.get(postion).getListtype()));
					intent.putExtra("author_id",foundList.get(postion).getUid());
				} else {
					intent = new Intent(mContext, LoginActivity.class);
				}
				mContext.startActivity(intent);
				break;
			default:
				break;
			}
		}
		
	}
	
	class ViewHolder {
		TextView tvGroupUserName;
		TextView tvGroupNumber;
		ImageView imgGroupUrl;
		ImageView tvGroupType;
		CircleImageView imGroupUserIcon;
		TextView tvGroupCont;
		TextView tvGroupName;
		LinearLayout lin_teamName;
		CustomGridView customGridView;
		LinearLayout lin_grouppage3;
	}

	/**
	 * 点击大图
	 * 
	 * @param posstion
	 */
	private void ImageActivity(int posstion) {
		Intent intent = new Intent(mContext, ImageDetailActivity.class);
		intent.putExtra("img_list", (Serializable) foundList.get(posstion)
				.getImages());
		mContext.startActivity(intent);
	}

	private void clearCache(ViewHolder holder) {
		holder.tvGroupUserName.setText("");
		holder.tvGroupName.setText("");
		holder.imgGroupUrl.setImageResource(0);
		holder.tvGroupType.setImageResource(0);
		holder.imGroupUserIcon.setImageResource(0);
		holder.tvGroupCont.setText("");
		holder.tvGroupName.setText("");

	}

	/**
	 * 筛选图片集合。。只需要三张图片
	 * 
	 * @param list
	 * @return
	 */
	private List<ArticleImageBean> screeningImageList(
			List<ArticleImageBean> list) {

		List<ArticleImageBean> imList = new ArrayList<ArticleImageBean>();
		for (int i = 0; i < list.size(); i++) {
			 ArticleImageBean bena;
			 if(i>2){
				 return imList;
			 }
			 bena = list.get(i);
			 imList.add(bena);
		}
		return imList;

	}
}
