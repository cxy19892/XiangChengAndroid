package com.yzm.sleep.adapter;

import java.util.List;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.ShareViewBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareViewAdapter extends BaseAdapter {
	private List<ShareViewBean> data;
	private Context mContext;

	public ShareViewAdapter(Context context, List<ShareViewBean> data) {

		this.data = data;
		this.mContext = context;
	}

	/**
	 * 数据总数
	 */
	@Override
	public int getCount() {

		return data.size();
	}

	/**
	 * 获取当前数据
	 */
	@Override
	public Object getItem(int position) {

		return data.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShareViewBean initClass = data.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_invite_item, null);
			viewHolder = new ViewHolder();
			viewHolder.iv = (ImageView) convertView
					.findViewById(R.id.iv_invite);
			viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_invite);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.iv.setImageResource(initClass.id);
		viewHolder.tv.setText(initClass.type);
		return convertView;
	}

	private class ViewHolder {
		public ImageView iv;
		public TextView tv;
	}

}
