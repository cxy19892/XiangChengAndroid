package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.ArticleCorrelatGroupBean;

public class RecommendGropuAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private List<ArticleCorrelatGroupBean> groupDatas;
	
	public RecommendGropuAdapter(Context context, int screenWidht,List<ArticleCorrelatGroupBean> groupDatas){
		this.mInflater=LayoutInflater.from(context);
		this.groupDatas=groupDatas;
	}
	
	public void setData(List<ArticleCorrelatGroupBean> groupDatas){
		this.groupDatas=groupDatas;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return groupDatas == null ? 0: groupDatas.size();
	}

	@Override
	public ArticleCorrelatGroupBean getItem(int position) {
		return groupDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArticleCorrelatGroupBean bean = groupDatas.get(position);
		ViewHolder holder=null;
		if(convertView!=null && convertView.getTag()!= null){
			holder=(ViewHolder) convertView.getTag();
		}else{
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_groupheader, null);
			holder.groupName=(TextView) convertView.findViewById(R.id.tvHeaderTitle);
			holder.groupIntro=(TextView) convertView.findViewById(R.id.tvHeaderCont);
			holder.groupIcon=(CircleImageView) convertView.findViewById(R.id.imgroupheaderIcon);
			convertView.findViewById(R.id.view_icon).setVisibility(View.GONE);
			convertView.setTag(holder);
		}
		
		holder.groupName.setText(bean.getG_name());
		holder.groupIntro.setText(bean.getG_intro());
		ImageLoader.getInstance().displayImage(bean.getG_ico(),bean.getG_ico_key(), holder.groupIcon, MyApplication.headPicOptn);
		
		return convertView;
	}

	class ViewHolder{
		CircleImageView groupIcon;
		TextView groupName,groupIntro;
	}
}
