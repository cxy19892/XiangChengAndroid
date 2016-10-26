package com.yzm.sleep.adapter;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.ComfortBean;
import com.yzm.sleep.bean.ComfortResult;
import com.yzm.sleep.bean.ComfortShowsBean;
import com.yzm.sleep.bean.ComfortStrBean;
import com.yzm.sleep.utils.LogUtil;

public class ComfortAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<ComfortShowsBean> list;
	int  screenHight;
	lastPagekListener mLastPageListener;
	ComfortResult mComfortResult;
	int[] bottomBg = {R.drawable.ic_zhi_blue_bottom, R.drawable.ic_zhi_yellow_bottom, R.drawable.ic_zhi_green_bottom, R.drawable.ic_zhi_red_bottom, R.drawable.ic_zhi_purple_bottom};
	int[] firstBg  = {R.drawable.ic_zhi_blue1, R.drawable.ic_zhi_yellow1, R.drawable.ic_zhi_green1, R.drawable.ic_zhi_red1, R.drawable.ic_zhi_purple1}; 
	int[] contentBg= {R.drawable.ic_zhi_blue2, R.drawable.ic_zhi_yellow2, R.drawable.ic_zhi_green2, R.drawable.ic_zhi_red2, R.drawable.ic_zhi_purple2}; 
	int[] slidingBg= {R.drawable.ic_zhi_blue3, R.drawable.ic_zhi_yellow3, R.drawable.ic_zhi_green3, R.drawable.ic_zhi_red3, R.drawable.ic_zhi_purple3}; 
	
	public ComfortAdapter(LayoutInflater inflater,  int screenHight){
		this.inflater = inflater;
		this.screenHight = screenHight;
	}
	
	public void setData(ComfortResult comfortResult){
		mComfortResult = comfortResult;
		this.list = comfortResult.getList();
		notifyDataSetChanged();
	}
	
	public ComfortResult getData(){
		return this.mComfortResult;
	}
	@Override
	public int getCount() {
		if(list != null)
			return list.size();
		else
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
	int downX;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holdler holder;
		if(convertView == null){
		convertView = null;
			holder = new Holdler();
			convertView = inflater.inflate(R.layout.item_curl_page, null, false);
			holder.relContent = (RelativeLayout) convertView.findViewById(R.id.rel_content);
			holder.relLast    = (RelativeLayout) convertView.findViewById(R.id.rel_bottom);
			holder.tvLast     = (TextView) convertView.findViewById(R.id.tv_bottom);
			holder.list = new ArrayList<ComfortShowsBean>();
			holder.imgLast = (ImageView) convertView.findViewById(R.id.writimg);
			convertView.setTag(holder);
		}else{
			holder = (Holdler) convertView.getTag();
		}
		holder.relLast.setBackgroundResource(bottomBg[position%5]);
		holder.list = list;
		holder.tvLast.setText(list.get(position).getLaString());
//		if(list.get(position).getLaString().equals("这些话都没有安慰到我，我要自己写")){
//			holder.imgLast.setVisibility(View.VISIBLE);
//		}else{
//			holder.imgLast.setVisibility(View.GONE);
//		}
		boolean isReadAll = true;
		for(ComfortStrBean strBean : list.get(position).getContentList()){
			if(!strBean.isIsread()){
				isReadAll = false;
			}
		}
		if(isReadAll){
			holder.relLast.setOnTouchListener(new OnTouchListener() {//滑动不能进入下一页，点击才能进入下一页
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case  MotionEvent.ACTION_DOWN:
						downX = (int) event.getX();
						break;
					case  MotionEvent.ACTION_MOVE:
					
						break;
					case  MotionEvent.ACTION_UP:
						if(Math.abs(event.getX() - downX) > 20){
							break;
						}else{
							if(mLastPageListener != null){
								mLastPageListener.lastPageCallBack(position, holder.tvLast.getText().toString());
							}
						}
						break;
					default :
							break;
					}
					return true;
				}
			});
		}else{
			holder.relLast.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
			});
		}
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,(screenHight-100)/5);
		addItem(holder.relContent, list.get(position).getContentList(), position, lp);
		convertView.setLayoutParams(lp);
		return convertView;
	}
	
	public class Holdler{
		RelativeLayout relLast;
		TextView tvLast;
		RelativeLayout relContent;
		public List<ComfortShowsBean> list;
		ImageView imgLast;
	}
	
	private void addItem(RelativeLayout contentLin , List<ComfortStrBean> list, int position, AbsListView.LayoutParams lp){
		
		if(contentLin.getChildCount()!=0){
			contentLin.removeAllViews();
		}
		for (int i = list.size()-1; i >= 0 ; i--) {
			if (!list.get(i).isIsread()) {
				View view  = inflater.inflate(R.layout.item_comfrot_scroll, null);
				TextView tView = (TextView) view.findViewById(R.id.item_tv);
				ImageView img  = (ImageView) view.findViewById(R.id.item_img);
				ImageView img2 = (ImageView) view.findViewById(R.id.item_img2);
				TextView guide = (TextView) view.findViewById(R.id.item_guide);
				if(i == 0){
					img.setImageResource(firstBg[position%5]);
					guide.setVisibility(View.VISIBLE);
				}else{
					img.setImageResource(contentBg[position%5]);
					guide.setVisibility(View.GONE);
				}
				img2.setImageResource(slidingBg[position%5]);
				tView.setText(list.get(i).getContent());
				view.setId(position * 10000 + i);
				view.setLayoutParams(lp);
				contentLin.addView(view);
			}
		}
	}
	
	public interface lastPagekListener {
		public void lastPageCallBack(int position, String text);
	}
	
	public void setLastPageClickListener(lastPagekListener ClickListener){
		this.mLastPageListener = ClickListener;
	}

}
