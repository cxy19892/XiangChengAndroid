package com.yzm.sleep.adapter;

import java.util.List;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.bean.SearchGroupBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupListAdapter extends BaseAdapter {

	private Context mContext;
	private List<CommunityGroupBean> mList;
	private LayoutInflater inflater;
	
	public GroupListAdapter(Context mContext){
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
	}
	
	public void setData(List<CommunityGroupBean> mList){
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(null != mList){
			return mList.size();
		}else
			return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHold viewHold = null;
		if (convertView == null) {
			viewHold = new ViewHold();
			convertView = inflater.inflate(R.layout.item_textview_serch, null);
			convertView.setTag(viewHold);
		}else{
			viewHold = (ViewHold) convertView.getTag();
		}
		((TextView) convertView).setText(mList.get(arg0).getG_name());
		return convertView;
	}

	public class ViewHold{
		TextView textV;
	}
}
