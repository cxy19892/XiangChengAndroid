package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.LifeHabitBean.CustomerItem;

public class HabitGridAdapter extends BaseAdapter {
	
	private List<CustomerItem> datas;
	private LayoutInflater mInflater;
	private InterfaceOnClick listerner;
	
	public interface InterfaceOnClick{
		public void onClick(int position, String flag);
	}

	public HabitGridAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
	}
	
	public void setListener(InterfaceOnClick listerner){
		this.listerner = listerner;
		this.notifyDataSetChanged();
	}
	
	public void setDatas(List<CustomerItem> datas){
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
		CustomerItem data = datas.get(position);
		ViewHolder holder = null;
		if(convertView != null && convertView.getTag() != null)
			holder = (ViewHolder) convertView.getTag();
		else{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_habit_layout, null);
			holder.rbtn = (TextView) convertView.findViewById(R.id.radiobtn);
			convertView.setTag(holder);
		}
		
		holder.rbtn.setText(data.getTitle());
		if("1".equals(data.getFlag()))
			holder.rbtn.setBackgroundResource(R.drawable.custom_round_select);
		else
			holder.rbtn.setBackgroundResource(R.drawable.custom_round_normal);
		holder.rbtn.setOnClickListener(new MyCliclListener(position, data.getFlag()));
		return convertView;
	}

	class ViewHolder{
		TextView rbtn;
	}

	class MyCliclListener implements OnClickListener{

		private int position;
		private String flag;
		
		public MyCliclListener(int position, String flag){
			this.position = position;
			this.flag = flag;
		}
		
		@Override
		public void onClick(View v) {
			if(listerner != null)
				listerner.onClick(position, flag);
		}
		
	}
	
}
