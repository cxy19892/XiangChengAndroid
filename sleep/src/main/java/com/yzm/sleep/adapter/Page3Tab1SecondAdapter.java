package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.ArticleBean;

public class Page3Tab1SecondAdapter extends BaseAdapter {
	
	private LayoutInflater mLInflater;
	private List<ArticleBean> datas;
	

	public Page3Tab1SecondAdapter(Context mContext){
		this.mLInflater = LayoutInflater.from(mContext);
	}
	
	public void setDatas(List<ArticleBean> datas){
		this.datas =datas;
		this.notifyDataSetChanged();
	}
	
	public List<ArticleBean> getDatas(){
		return this.datas;
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
		ViewHolder holder = null;
		if(convertView !=null && convertView.getTag() != null)
			holder = (ViewHolder) convertView.getTag();
		else{
			holder = new ViewHolder();
			convertView = mLInflater.inflate(R.layout.item_page3tab1_second, null);
			holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		}
		
		ArticleBean bean = datas.get(position);
		holder.tvTitle.setText(bean.getPost_title());
		ImageLoader.getInstance().displayImage(bean.getBjpic(), bean.getBjpickey(), holder.ivPic, MyApplication.choicePicOptn);
		
		return convertView;
	}

	
	class ViewHolder{
		ImageView ivPic;
		TextView tvTitle;
	}
	
}
