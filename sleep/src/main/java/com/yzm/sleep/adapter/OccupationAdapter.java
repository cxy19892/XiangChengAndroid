package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.OccpationType;

public class OccupationAdapter extends BaseAdapter {
	private List<OccpationType> types;
	private Context context;
	private LayoutInflater mInflater;
	
	public OccupationAdapter(Context context,List<OccpationType> types){
		this.context=context;
		this.types=types;
		this.mInflater=LayoutInflater.from(context);
	}
	
	public void setDatas(List<OccpationType> types){
		this.types=types;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return types.size();
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
		OccpationType bean=types.get(position);
		ViewHolder holder=null;
		if(convertView!=null && convertView.getTag()!=null){
			holder=(ViewHolder) convertView.getTag();
		}else{
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.select_list_item, null);
			holder.text=(TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}
		if(bean.isSelset()){
			holder.text.setBackgroundColor(context.getResources().getColor(R.color.bg_color));
			holder.text.setTextColor(context.getResources().getColor(R.color.onet_color));
		}else{
			holder.text.setBackgroundColor(context.getResources().getColor(R.color.cbg_color));
			holder.text.setTextColor(context.getResources().getColor(R.color.onet_color));
		}
		holder.text.setText(bean.getTypeName());
		return convertView;
	}
	
	class ViewHolder{
		TextView text;
	}
	

}
