package com.yzm.sleep.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzm.sleep.R;
import com.yzm.sleep.model.RingtoneInfo;

public class MyLocalRingsAdapter extends BaseAdapter {

	public ArrayList<RingtoneInfo> mRingtoneInfos;
	public Context context;
	private LayoutInflater inflater;
	private ItemClickInterface myClick;
	
	public MyLocalRingsAdapter(Context context, ItemClickInterface myClick) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.myClick = myClick;
	}
	
	public void setData(ArrayList<RingtoneInfo> mRingtoneInfos){
		this.mRingtoneInfos = mRingtoneInfos;
		this.notifyDataSetChanged();
	}
	
	public ArrayList<RingtoneInfo> getData(){
		return this.mRingtoneInfos;
	}
	
	@Override
	public int getCount() {
		if(mRingtoneInfos !=null ){
			return mRingtoneInfos.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		
		return mRingtoneInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RingtoneInfo ringtoneInfo = (RingtoneInfo) getItem(position);
		ViewHold viewHold = null;
		if (convertView == null) {
			viewHold = new ViewHold();
			convertView = inflater.inflate(R.layout.item_of_my_local_ring, null);
			viewHold.tv_item_name = (TextView) convertView.findViewById(R.id.item_tv_music_name);
			viewHold.cb_item_select = (ImageView) convertView.findViewById(R.id.item_cb_select);
			viewHold.lin_item = (LinearLayout) convertView.findViewById(R.id.item_layout_lin);
			convertView.setTag(viewHold);
		}else{
			viewHold = (ViewHold) convertView.getTag();
		}
		
		viewHold.tv_item_name.setText(ringtoneInfo.title);
		if(ringtoneInfo.isselect){
			viewHold.cb_item_select.setBackgroundResource(R.drawable.ic_selected);
			viewHold.tv_item_name.setTextColor(context.getResources().getColor(R.color.ct_color));
		}else{
			viewHold.cb_item_select.setBackgroundResource(R.drawable.ic_normal);
			viewHold.tv_item_name.setTextColor(context.getResources().getColor(R.color.fct_color));
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					myClick.itemClick(mRingtoneInfos.get(position));
			}
		});
		
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if(!mRingtoneInfos.get(position).isdefault){
				myClick.itemLongClick(mRingtoneInfos.get(position));
				}else{
					Toast.makeText(context, "默认铃音不可删除", Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		});
		return convertView;
	}
	
	public class ViewHold{
		TextView tv_item_name;
		ImageView cb_item_select;
		LinearLayout lin_item;
	}
	
	public interface ItemClickInterface{
		public void itemClick(RingtoneInfo ringtoneInfo);
		public void itemLongClick(RingtoneInfo ringtoneInfo);
	}
	
}
