package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.OccpationBean;

public class SelectJobAdapter extends BaseAdapter {

	private List<OccpationBean> datas;
	private LayoutInflater mInflater;

	public SelectJobAdapter(Context context, List<OccpationBean> datas) {
		this.mInflater = LayoutInflater.from(context);
		this.datas = datas;
	}

	public void setDatas(List<OccpationBean> datas) {
		this.datas = datas;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OccpationBean bean = datas.get(position);
		ViewHolder holder = null;
		if (convertView != null && convertView.getTag() != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.select_list_item, null);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}
		holder.text.setText(bean.getVocation());
		return convertView;
	}

	class ViewHolder {
		TextView text;
	}

}
