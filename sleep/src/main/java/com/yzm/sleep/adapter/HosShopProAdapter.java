package com.yzm.sleep.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.TaocanBean;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HosShopProAdapter extends BaseAdapter {

	private Context mcontext;
	private List<TaocanBean> mList;
	private DisplayImageOptions choicePicOptn;
	
	public HosShopProAdapter(Context context){
		this.mcontext = context;
		if(choicePicOptn == null){
			choicePicOptn = new DisplayImageOptions.Builder()  
			.showImageOnLoading(R.drawable.default_choice_icon) //设置图片在下载期间显示的图片  
			.showImageForEmptyUri(R.drawable.default_choice_icon)//设置图片Uri为空或是错误的时候显示的图片  
			.showImageOnFail(R.drawable.default_choice_icon)  //设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)//设置下载的图片是否缓存在内存中 
			.cacheOnDisk(true)
			.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示  
			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
			.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
			.build();//构建完成  
		}
	}
	
	public void setData(List<TaocanBean> list){
		mList = list;
		notifyDataSetChanged();
	}
	
	public List<TaocanBean> getData(){
		return mList;
	}
	
	@Override
	public int getCount() {
		if(mList != null){
			return mList.size();
		}else
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TaocanBean mTaocanBean = mList.get(position);
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
			convertView = LayoutInflater.from(mcontext).inflate(
					R.layout.item_hos_shop_pro, null);
			holder.itemImg = (CircleImageView) convertView.findViewById(R.id.item_icon);
			holder.itemTv  = (TextView) convertView.findViewById(R.id.item_tv);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.itemTv.setText(TextUtils.isEmpty(mTaocanBean.getTitle())?"":mTaocanBean.getTitle());
		ImageLoader.getInstance().displayImage(mTaocanBean.getPicture(), mTaocanBean.getPicture_key(), holder.itemImg, choicePicOptn);
		return convertView;
	}

	
	private class Holder{
		CircleImageView itemImg;
		TextView itemTv;
	}
}
