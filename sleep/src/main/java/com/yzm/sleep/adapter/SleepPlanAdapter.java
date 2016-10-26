package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.PlanBean;
import com.yzm.sleep.utils.Util;

public class SleepPlanAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private boolean openSelect;
	private List<PlanBean> datas;
	
	public SleepPlanAdapter(Context context){
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	public void setData(List<PlanBean> datas){
		this.datas = datas;
		this.notifyDataSetChanged();
	}
	
	public void openSe(boolean open){
		this.openSelect = open;
		this.notifyDataSetChanged();
	}
	
	public boolean getOpenSe(){
		return this.openSelect;
	}
	
	/**
	 * 获取数据
	 * @return
	 */
	public List<PlanBean> getListData(){
		return datas;
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
		PlanBean bean = datas.get(position);
		if(convertView != null && convertView.getTag() != null)
			holder = (ViewHolder) convertView.getTag();
		else{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_sleep_plan_layout, null);
			holder.rab = (CheckBox) convertView.findViewById(R.id.rab);
			holder.text = (TextView) convertView.findViewById(R.id.textview);
			convertView.setTag(holder);
		}

		if(openSelect){ //修改状态
			convertView.setClickable(true);
			holder.rab.setVisibility(View.VISIBLE);
			if("1".equals(bean.getIsshow()))
				holder.rab.setChecked(true);
			else
				holder.rab.setChecked(false);
			holder.rab.setOnCheckedChangeListener(new MyCheckedChangeListener(holder, position, openSelect));
			convertView.setOnClickListener(new MyClickListener(holder, position, openSelect));
		}else{    //显示状态
			holder.rab.setVisibility(View.GONE);
			convertView.setClickable(false);
		}
		holder.text.setText(bean.getName());
		return convertView;
	}

	class ViewHolder{
		TextView text;
		CheckBox rab;
	}
	
	class MyCheckedChangeListener implements OnCheckedChangeListener{

		private ViewHolder holder; 
		private int position;
		private boolean canClick;
		
		public MyCheckedChangeListener(ViewHolder holder, int position, boolean canClisck){
			this.canClick = canClisck;
			this.position = position;
			this.holder = holder;
		}
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(!this.canClick)
				return;
			if(!Util.checkNetWork(mContext)){
				Util.show(mContext, "检查网路设置");
				holder.rab.setChecked(!isChecked);
				return;
			}
			holder.rab.setChecked(isChecked);
			datas.get(position).setIsshow(isChecked ? "1" : "0");
		}
		
	}
	
	class MyClickListener implements OnClickListener{
		private ViewHolder holder; 
		private boolean canClick;
		
		public MyClickListener(ViewHolder holder, int position, boolean canClisck){
			this.canClick = canClisck;
			this.holder = holder;
		}
		
		@Override
		public void onClick(View v) {
			
			if(!Util.checkNetWork(mContext)){
				Util.show(mContext, "检查网路设置");
				return;
			}
			if(canClick){
				if(holder.rab.isChecked()){
					holder.rab.setChecked(false);
//					datas.get(position).setIsshow("0");
				}else{
					holder.rab.setChecked(true);
//					datas.get(position).setIsshow("1");
				}
			}
		}
	}
	
	
}
