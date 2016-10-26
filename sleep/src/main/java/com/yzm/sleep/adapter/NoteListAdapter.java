package com.yzm.sleep.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.NoteBean;
import com.yzm.sleep.utils.TimeFormatUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter {

	private Context mContext;
	private List<NoteBean> mlist = new ArrayList<NoteBean>();
	private LayoutInflater inflater;
	private boolean isSelect = false;
	public NoteListAdapter(Context context){
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
	}
	
	public void SetData(List<NoteBean> mlist){
		this.mlist = mlist;
		notifyDataSetChanged();
	}
	
	public List<NoteBean> getData(){
		return this.mlist;
	}
	
	public void setIsSelect(boolean select){
		this.isSelect = select;
		notifyDataSetChanged();
	}
	
	public boolean getIsSelect(){
		return isSelect;
	}
	
	@Override
	public int getCount() {
		if(mlist != null){
			return mlist.size();
		}else
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
			convertView = inflater.inflate(R.layout.item_note_list, null);
			holder.itemImg = (ImageView) convertView.findViewById(R.id.img_select_icon);
			holder.itemTitle = (TextView) convertView.findViewById(R.id.tv_note_name);
			holder.itemTime = (TextView) convertView.findViewById(R.id.tv_note_time);
			convertView.setTag(holder);
		}else{
			holder = (HolderView) convertView.getTag();
		}
		long time = 0l;
		try {
			time = Long.parseLong(mlist.get(position).getDateline());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		if(isSelect){
			holder.itemImg.setVisibility(View.VISIBLE);
			if(mlist.get(position).isSelected()){
				holder.itemImg.setImageResource(R.drawable.ic_selected);
			}else{
				holder.itemImg.setImageResource(R.drawable.ic_normal);
			}
		}else{
			holder.itemImg.setVisibility(View.GONE);
		}
		holder.itemTitle.setText(mlist.get(position).getText());
		
		holder.itemTime.setText(TimeFormatUtil.getTimeForYM(time));
		return convertView;
	}
	
	
	public class HolderView{
		public ImageView itemImg;
		public TextView itemTitle;
		public TextView itemTime;
	}

}
