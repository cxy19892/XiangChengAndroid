package com.yzm.sleep.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.community.TeamDetailsActivity;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;

public class GroupHeadViewAdapter extends BaseAdapter{
	private Activity mContext;
	private LayoutInflater inflater;
	private List<CommunityGroupBean> groupList;
	private int screenWidht;
	private int pos;
	public GroupHeadViewAdapter(Activity mContext,int screenWidht,int pos){
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		this.screenWidht = screenWidht;
		this.pos = pos;
	}
	
	public void setData(List<CommunityGroupBean> groupList){
		this.groupList = groupList;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return groupList==null ? 0:groupList.size();
	}

	@Override
	public Object getItem(int position) {
		return groupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		CommunityGroupBean bean = groupList.get(position);
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_groupheader, null);
			holder.tvHeaderCont = (TextView) convertView.findViewById(R.id.tvHeaderCont);
			holder.tvHeaderTitle = (TextView) convertView.findViewById(R.id.tvHeaderTitle);
			holder.imgroupheaderIcon = (CircleImageView) convertView.findViewById(R.id.imgroupheaderIcon);
			holder.reHeader = (RelativeLayout) convertView.findViewById(R.id.reHeader);
			holder.view_icon = (View) convertView.findViewById(R.id.view_icon);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			clearCache(holder);
		}
		if(position == 0 && pos==2){
			holder.view_icon.setVisibility(View.VISIBLE);
		}else{
			holder.view_icon.setVisibility(View.GONE);
		}
		holder.tvHeaderCont.setText(bean.getG_intro());
		holder.tvHeaderTitle.setText(bean.getG_name());
		LogUtil.i("masong", position+":"+bean
				.getG_ico());
		LogUtil.i("masong1", position+":"+bean.getG_ico_key());
		ImageLoader.getInstance().displayImage(bean
				.getG_ico(), bean.getG_ico_key(), holder.imgroupheaderIcon, MyApplication.headPicOptn);
//		
//		FileUtil.displayImage(holder.imgroupheaderIcon, bean
//				.getG_ico(), bean.getG_ico_key());
		
		return convertView;
	}
	class ViewHolder {
		TextView tvHeaderCont;
		TextView tvHeaderTitle;
		RelativeLayout reHeader;
		CircleImageView imgroupheaderIcon;
		View view_icon;
	}
	private void clearCache(ViewHolder holder) {
		holder.tvHeaderCont.setText("");
		holder.tvHeaderTitle.setText("");
		holder.imgroupheaderIcon.setImageResource(0);

	}
}
