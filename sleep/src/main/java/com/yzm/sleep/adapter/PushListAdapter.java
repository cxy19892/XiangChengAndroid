package com.yzm.sleep.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.BePushBean;
import com.yzm.sleep.utils.FileUtil;

public class PushListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<BePushBean> mList;
	public PushListAdapter(Activity mContext){
		inflater = LayoutInflater.from(mContext);
	}
	
	public void setData(List<BePushBean> list){
		this.mList = list;
		notifyDataSetChanged();
	}
	
	public List<BePushBean> getData(){
		return mList;
	}
	@Override
	public int getCount() {
		return mList==null ? 0:mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		BePushBean bean = mList.get(position);
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_groupheader, null);
			holder.tvHeaderCont = (TextView) convertView.findViewById(R.id.tvHeaderCont);
			holder.tvHeaderTitle = (TextView) convertView.findViewById(R.id.tvHeaderTitle);
			holder.imgroupheaderIcon = (CircleImageView) convertView.findViewById(R.id.imgroupheaderIcon);
			holder.reHeader = (RelativeLayout) convertView.findViewById(R.id.reHeader);
			holder.view_icon = (View) convertView.findViewById(R.id.view_icon);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			clearCache(holder);
		}
		holder.view_icon.setVisibility(View.GONE);
		
		holder.tvHeaderCont.setText(bean.getIntro());
		holder.tvHeaderTitle.setText(bean.getTitle() == null ? "" :bean.getTitle());
		ImageLoader.getInstance().displayImage(bean
				.getPicture(), 
				bean.getPicture_key() == null ? "" : bean.getPicture_key(), 
				holder.imgroupheaderIcon, 
				MyApplication.headPicOptn);
		
		return convertView;
	}
	class ViewHolder {
		TextView tvHeaderCont;
		TextView tvHeaderTitle;
		RelativeLayout reHeader;
		CircleImageView imgroupheaderIcon;
		View view_icon;
	}
	private void clearCache(ViewHolder holder) {
		holder.tvHeaderCont.setText("");
		holder.tvHeaderTitle.setText("");
		holder.imgroupheaderIcon.setImageResource(0);

	}
}
