package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.SleepCaseBean;

public class SleepCaseAdapter extends BaseAdapter {

	private List<SleepCaseBean> datas;
	private LayoutInflater mLinflater;
	private Context mContext;
	private int position;
	
	public SleepCaseAdapter(Context context){
		this.mContext = context;
		this.mLinflater = LayoutInflater.from(context);
	}
	
	public void setDatas(List<SleepCaseBean> datas){
		this.datas = datas;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}
	
	public void selectPo(int position){
		this.position = position;
		this.notifyDataSetChanged();
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
		SleepCaseBean data = datas.get(position);
		ViewHolder holder = null;
		if(convertView != null && convertView.getTag() != null)
			holder = (ViewHolder) convertView.getTag();
		else{
			holder = new ViewHolder();
			convertView = mLinflater.inflate(R.layout.item_sleepcase_layout, null);
			holder.rbBtn = (TextView) convertView.findViewById(R.id.rb_btn);
			convertView.setTag(holder);
		}
		
		if(position == this.position)
			holder.rbBtn.setBackgroundColor(mContext.getResources().getColor(R.color.bg_color));
		else
			holder.rbBtn.setBackgroundColor(mContext.getResources().getColor(R.color.cbg_color));
		
		holder.rbBtn.setText(new StringBuffer().append(data.getStart_time()).append("~").append(data.getEnd_time())
				.append("\n").append(data.getTitle()).toString());
		
		return convertView;
	}
	
	class ViewHolder{
		TextView rbBtn;
	}

}
