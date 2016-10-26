package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.R.id;
import com.yzm.sleep.model.SignInResult.Plan;

public class DaysPlanAdapter extends BaseAdapter {

	private List<Plan> planList;
	private LayoutInflater inflater;
	private DayPlanClickCallBack callBack;
	private Context mcontext;
	
	
	public DaysPlanAdapter(Context context, DayPlanClickCallBack callback){
		this.inflater = LayoutInflater.from(context);
		this.callBack = callback;
		this.mcontext = context;
	}
	
	
	public void setData(List<Plan> List){
		this.planList = List;
		notifyDataSetChanged();
	}
	
	public List<Plan>  getdata(){
		return this.planList;
	}
	
	@Override
	public int getCount() {
		if(planList != null){
			return planList.size();
		}else
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(planList != null){
			return planList.get(0);
		}else{
			return 0;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		HolderView holder = null;
		if(convertView == null){
			holder = new HolderView();
			convertView = inflater.inflate(R.layout.item_days_plan, null);
			holder.itemTV = (TextView) convertView.findViewById(R.id.item_days_plan_tv);
			holder.itemDone= (Button) convertView.findViewById(R.id.item_days_plan_done);
			holder.itemPlay= (ImageView)convertView.findViewById(R.id.item_days_plan_btn);
			holder.itemLine = convertView.findViewById(R.id.item_days_plan_lines);
			convertView.setTag(holder);
		}else{
			holder = (HolderView) convertView.getTag();
		}
		if("1".equals(planList.get(position).getIsshow())){
//			holder.itemBtn.setVisibility(View.VISIBLE);
			holder.itemTV.setVisibility(View.VISIBLE);
			holder.itemLine.setVisibility(View.VISIBLE);
			holder.itemTV.setText(planList.get(position).getName());
			
			if("1".equals(planList.get(position).getIsfinish())){//完成
//				holder.itemDone.setText("已完成");
				holder.itemDone.setVisibility(View.VISIBLE);
				holder.itemPlay.setVisibility(View.GONE);
				holder.itemDone.setTextColor(mcontext.getResources().getColor(R.color.ct_color));
				holder.itemDone.setBackgroundResource(0);
			}else{//未完成
//				holder.itemDone.setText("已完成");
				holder.itemDone.setVisibility(View.INVISIBLE);
				holder.itemPlay.setVisibility(View.VISIBLE);
				holder.itemPlay.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(callBack != null){
							callBack.DayPlanClickFinish(planList.get(position));
						}
					}
				});
			}
			holder.itemTV.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(callBack != null){
						callBack.DayPlanClickDetail(planList.get(position));
					}
				}
			});
		}else{
			holder.itemDone.setVisibility(View.GONE);
			holder.itemTV.setVisibility(View.GONE);
			holder.itemLine.setVisibility(View.GONE);
			holder.itemPlay.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	
	
	class HolderView{
		TextView itemTV;
		Button itemDone;
		View itemLine;
		ImageView itemPlay;
	}
	
	public interface DayPlanClickCallBack{
		public void DayPlanClickDetail(Plan plan);
		public void DayPlanClickFinish(Plan plan);
	}

}
