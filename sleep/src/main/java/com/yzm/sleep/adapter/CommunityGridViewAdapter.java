package com.yzm.sleep.adapter;

import java.io.Serializable;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.ImageDetailActivity;
import com.yzm.sleep.bean.ArticleImageBean;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 社区九宫格图适配器
 * 
 * @author Administrator
 * 
 */
public class CommunityGridViewAdapter extends BaseAdapter {
	private List<ArticleImageBean> mList;
	private Context mContext;
	private int screenWidth;
	private LayoutInflater inflater;
	private boolean isFade;
	private DisplayImageOptions defaultOption;
	public CommunityGridViewAdapter(Context context, int screenWidth) {
		this.mContext = context;
		this.screenWidth = screenWidth;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<ArticleImageBean> mList) {
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size()> 9 ? 9:mList.size();
		
	}
	
	public void setDisplayImageOptions(DisplayImageOptions displayImageOptions){
		this.defaultOption = displayImageOptions;
	}
	public void setFade(boolean isFade){
		this.isFade = isFade;
	}
	
	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override 
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertview, ViewGroup arg2) {
		ViewHolder holder;
		if (convertview == null) {
			holder = new ViewHolder();
			convertview = inflater.inflate(R.layout.item_gridview, null);
			holder.imageview = (ImageView) convertview
					.findViewById(R.id.grid_imgUrl);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					screenWidth * 2 / 4, screenWidth * 1 / 4);
			lp.addRule(RelativeLayout.CENTER_IN_PARENT);
			holder.imageview.setLayoutParams(lp);
			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();                          
		}
		ArticleImageBean com = mList.get(pos);
		if(isFade){
			ImageLoader.getInstance().displayImage(
					com.getContent_attachment_sl(),
					com.getT_attachment_key_sl(), holder.imageview,
					defaultOption);
		}else{
			ImageLoader.getInstance().displayImage(
					com.getContent_attachment_sl(),
					com.getT_attachment_key_sl(), holder.imageview,
					MyApplication.defaultOption);
		}
			
			holder.imageview.setOnClickListener(new FragmentPageOnClickListener(pos));
		return convertview;
	}

	private class FragmentPageOnClickListener implements OnClickListener {
		int index;

		public FragmentPageOnClickListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			ImageActivity(index);
		}
	}

	/**
	 * 点击大图
	 * 
	 * @param posstion
	 */
	private void ImageActivity(int posstion) {
		Intent intent = new Intent(mContext, ImageDetailActivity.class);
		intent.putExtra("img_list", (Serializable) mList);
		intent.putExtra("position", posstion);
		mContext.startActivity(intent);
	}

	public class ViewHolder {
		ImageView imageview;

	}
}
