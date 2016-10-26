package com.yzm.sleep.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.EnvironmentBean;

public class BedroomEnvAdapter extends BaseAdapter {
	
	private List<EnvironmentBean> datas;
	
	private LayoutInflater mInflater;
	
	
	public BedroomEnvAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
	}

	public void setDatas(List<EnvironmentBean> datas){
		this.datas = datas;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}
	
	public List<EnvironmentBean> getDatas(){
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		EnvironmentBean data= datas.get(position);
		ViewHolder holder = null;
		if(convertView != null && convertView.getTag() != null){
			holder = (ViewHolder) convertView.getTag();
		}else{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_bedroom_layout, null);
			holder.rlSu = (LinearLayout) convertView.findViewById(R.id.rl_su);
			holder.tvSu = (TextView) convertView.findViewById(R.id.tv_suggest);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.rlPic = (LinearLayout) convertView.findViewById(R.id.rl_pic);
			holder.rlp = (RelativeLayout) convertView.findViewById(R.id.rl_p);
			holder.status = (ImageView) convertView.findViewById(R.id.status);
			convertView.setTag(holder);
		}
		holder.tvSu.setText(data.getSuggest());
		holder.rlSu.setVisibility(View.GONE);
		holder.rlPic.setVisibility(View.VISIBLE);
		holder.title.setText(data.getTitle());
		if("1".equals(data.getFlag()))
			holder.status.setVisibility(View.GONE);
		else
			holder.status.setVisibility(View.VISIBLE);
		
		ImageLoader.getInstance().displayImage(data.getPicture(), holder.pic, MyApplication.defaultOption);
		
		return convertView;
	}
	
	
//	private class MyClickListener implements OnClickListener{
//		private ViewHolder holder = null;
//		private int position;
//		public MyClickListener(int position, ViewHolder holder){
//			this.holder = holder;
//			this.position= position;
//		}
//		
//		@SuppressLint("NewApi") 
//		@Override
//		public void onClick(View v) {
//			if(!"1".equals(datas.get(position).getShowSuggest())){
//				holder.rlSu.animate().rotationY(0.0f).rotationY(180f).setDuration(10).setListener(new AnimatorListener() {
//					@Override
//					public void onAnimationStart(Animator animation) {
//						holder.rlp.setEnabled(false);
//					}
//					@Override
//					public void onAnimationRepeat(Animator animation) {
//					}
//					@Override
//					public void onAnimationEnd(Animator animation) {
//						holder.rlp.animate().rotationY(0.0f).rotationY(180f).setDuration(400).setUpdateListener(new AnimatorUpdateListener() {
//							@Override
//							public void onAnimationUpdate(ValueAnimator animation) {
//								float rot = (Float) animation.getAnimatedValue();
//								if(rot >= 0.5 ){
//									holder.rlPic.setVisibility(View.GONE);
//									holder.rlSu.setVisibility(View.VISIBLE);
//									datas.get(position).setShowSuggest("1");
//								}
//							}
//							}).setListener(new AnimatorListener() {
//								@Override
//								public void onAnimationStart(Animator animation) {
//								}
//								@Override
//								public void onAnimationRepeat(Animator animation) {
//								}
//								@Override
//								public void onAnimationEnd(Animator animation) {
//									holder.rlp.setEnabled(true);
//									holder.rlPic.setVisibility(View.GONE);
//									holder.rlSu.setVisibility(View.VISIBLE);
//								}
//								@Override
//								public void onAnimationCancel(Animator animation) {
//									holder.rlp.setEnabled(true);
//									holder.rlPic.setVisibility(View.GONE);
//									holder.rlSu.setVisibility(View.VISIBLE);
//								}
//							}).start();
//					}
//					
//					@Override
//					public void onAnimationCancel(Animator animation) {
//					}
//				}).start();
//				
//			}else{
//				holder.rlp.animate().rotationY(180f).rotationY(0.0f).setDuration(400).setUpdateListener(new AnimatorUpdateListener() {
//					@Override
//					public void onAnimationUpdate(ValueAnimator animation) {
//						float rot = (Float) animation.getAnimatedValue();
//						if(rot >= 0.5 ){
//							holder.rlPic.setVisibility(View.VISIBLE);
//							holder.rlSu.setVisibility(View.GONE);
//							datas.get(position).setShowSuggest("0");
//						}
//					}
//					}).setListener(new AnimatorListener() {
//						
//						@Override
//						public void onAnimationStart(Animator animation) {
//							holder.rlp.setEnabled(false);
//						}
//						
//						@Override
//						public void onAnimationRepeat(Animator animation) {
//						}
//						
//						@Override
//						public void onAnimationEnd(Animator animation) {
//							holder.rlp.setEnabled(true);
//							holder.rlPic.setVisibility(View.VISIBLE);
//							holder.rlSu.setVisibility(View.GONE);
//						}
//						
//						@Override
//						public void onAnimationCancel(Animator animation) {
//							holder.rlp.setEnabled(true);
//							holder.rlPic.setVisibility(View.VISIBLE);
//							holder.rlSu.setVisibility(View.GONE);
//						}
//					}).start();
//			}
//		}
//	}

	class ViewHolder{
		LinearLayout rlSu;
		TextView tvSu;
		LinearLayout rlPic;
		TextView title;
		ImageView pic, status;
		RelativeLayout rlp;
	}

}
