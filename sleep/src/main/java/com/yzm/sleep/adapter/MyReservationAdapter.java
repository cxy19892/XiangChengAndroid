package com.yzm.sleep.adapter;

import java.util.List;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.ReservationBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyReservationAdapter extends BaseAdapter {
	private List<ReservationBean> mOrders;
	private Context mContext;
	public MyReservationAdapter(Context contxt){
		this.mContext = contxt;
	}
	public void setData( List<ReservationBean> orders){
		this.mOrders = orders;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mOrders != null ? mOrders.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_reservation, null);
			holder.tvDoctor = (TextView) convertView.findViewById(R.id.tvDoctor);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvOrderTime);
			holder.right = convertView.findViewById(R.id.right);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		ReservationBean orderBean = mOrders.get(position);
		holder.tvDoctor.setText("预约医生：" +orderBean.getYname());
		holder.tvTime.setText("预约时间：" +orderBean.getYtime());
		if("1".equals(orderBean.getFlag())){
			holder.right.setVisibility(View.VISIBLE);
		}else{
			holder.right.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	
	private class ViewHolder{
		TextView tvDoctor;
		TextView tvTime;
		View right;
	}
}
