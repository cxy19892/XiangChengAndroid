package com.yzm.sleep.adapter;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.NoteBean;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.TimeFormatUtil;

public class NoteRecordAdapter extends BaseAdapter {

	private LayoutInflater mLInflater;
	private List<NoteBean> list;
	private NotesClickInterface mCallback;
	private Context mContext;
	
	public NoteRecordAdapter(Context context, NotesClickInterface callback){
		this.mLInflater = LayoutInflater.from(context);
		this.mCallback  = callback;
		this.mContext = context;
	}
	
	public void setData(List<NoteBean> mNoteList){
		this.list = mNoteList;
		notifyDataSetChanged();
	}
	
	public List<NoteBean> getDate(){
		return this.list;
	}
	@Override
	public int getCount() {
		if(list == null)
			return 0;
		else
			return list.size();
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
		ViewHolder holder = null;
		if(convertView !=null && convertView.getTag() != null)
			holder = (ViewHolder) convertView.getTag();
		else{
			holder = new ViewHolder();
			convertView = mLInflater.inflate(R.layout.item_note_record, null);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvMore = (TextView) convertView.findViewById(R.id.tv_more);
			convertView.setTag(holder);
		}
		holder.tvContent.setText(list.get(position).getText());
		final TextView tvContent = holder.tvContent;
		final TextView tvMore = holder.tvMore;
		holder.tvContent.post(new Runnable() {
			
			@Override
			public void run() {
				if(tvMore.getText().toString().contains("全文")){
					if(tvContent.getLineCount() >= 5){
						tvContent.setMaxLines(4);
						tvMore.setVisibility(View.VISIBLE);
					} else{
						tvMore.setVisibility(View.GONE);
					}
				}
				
			}
		});
		long time = 0l;
		try {
			time = Long.parseLong(list.get(position).getDateline());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		Drawable drawable = null;
		if(TimeFormatUtil.getDatetimeBygetTime(time*1000).equals(TimeFormatUtil.getTodayTime())){
			drawable = mContext.getResources().getDrawable(R.drawable.ic_calendar_blue);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.theme_color));
		}else{
			drawable = mContext.getResources().getDrawable(R.drawable.ic_calendar_gray);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.ct_color));
		}
		holder.tvTime.setCompoundDrawables(drawable, null, null, null);
		holder.tvTime.setCompoundDrawablePadding(20);
		holder.tvTime.setText(TimeFormatUtil.getTimeForYMHm(time));
		holder.tvMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(tvMore.getText().equals("全文")){
					tvContent.setMaxLines(Integer.MAX_VALUE);
					tvMore.setText("收起");
				}else{
					tvContent.setMaxLines(4);
					tvMore.setText("全文");
				}

			}
		});
		holder.tvDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCallback != null){
					mCallback.DeletNotes(list.get(position));
				}
				
			}
		});
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tvTime;
		TextView tvDelete;
		TextView tvContent;
		TextView tvMore;
		
	}
	
	public interface NotesClickInterface{
		public void DeletNotes(NoteBean mNoteBean);
	}

}
