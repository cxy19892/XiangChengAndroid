package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.FirstRecommentGroupBean;
import com.yzm.sleep.bean.GroupMessageBean;

public class HotGroupAdapter extends BaseAdapter {
	private Context mContxt;
	private List<FirstRecommentGroupBean> data;

	public void setData(List<FirstRecommentGroupBean> groupInfos) {
		this.data = groupInfos;
		notifyDataSetChanged();
	}

	public HotGroupAdapter(Context context) {
		this.mContxt = context;
	}

	@Override
	public int getCount() {
		if (data == null) {
			return 0;
		}
		return data.size() > 3 ? 3 : data.size();
	}

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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContxt).inflate(
					R.layout.item_group_gridview, null);
			viewHolder = new ViewHolder();
			viewHolder.iv = (CircleImageView) convertView
					.findViewById(R.id.imageView);
			viewHolder.tv = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			ImageLoader.getInstance().displayImage(
					data.get(position).getG_ico(),
					data.get(position).getG_ico_key(), viewHolder.iv,
					MyApplication.headPicOptn);
			String g_name = data.get(position).getG_name();
			if (g_name.length() ==7) {
				g_name = g_name +" ";
			}
			viewHolder.tv.setText(g_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	private class ViewHolder {
		private CircleImageView iv;
		private TextView tv;
	}

}
