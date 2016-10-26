package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.CitysBean;
import com.yzm.sleep.utils.Util;

public class OpenCityAdapter extends BaseAdapter {

	private List<CitysBean> citylist;
	private LayoutInflater mInflater;
	private int height;
	private String currentCity;
	private Context mContext;
	
	public OpenCityAdapter(Context mContext){
		this.mInflater = LayoutInflater.from(mContext);
		height = Util.Dp2Px(mContext, 50);
		this.mContext = mContext;
	}
	
	public void setData(List<CitysBean> citylist, String currentCity){
		this.currentCity = currentCity;
		this.citylist =citylist;
		this.notifyDataSetChanged();
	}
	
	public List<CitysBean> getData(){
		return citylist;
	}
	
	@Override
	public int getCount() {
		return citylist == null ? 0 : citylist.size();
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
		ViewHolder holder = null;
		if(convertView != null && convertView.getTag() != null)
			holder = (ViewHolder) convertView.getTag();
		else{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_opencity,null);
			holder.name = (TextView) convertView.findViewById(R.id.city_name);
			LinearLayout.LayoutParams param = (LayoutParams) holder.name.getLayoutParams();
			param.height = height;
			holder.name.setLayoutParams(param);
			convertView .setTag(holder);
		}
		
		if(currentCity.equals(citylist.get(position).getCity()))
			holder.name.setTextColor(mContext.getResources().getColor(R.color.onet_color));
		else
			holder.name.setTextColor(mContext.getResources().getColor(R.color.ct_color));
		
		holder.name.setText(citylist.get(position).getCity());
		
		return convertView;
	}

	class ViewHolder{
		TextView name;
	}
}
