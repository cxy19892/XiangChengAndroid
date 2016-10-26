package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.TaocanBean;
import com.yzm.sleep.utils.Util;

public class FragmentPage2TabStoreAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<TaocanBean> goodsList;
	private int imageheight;
	public DisplayImageOptions choicePicOptn; //默认图片加载模式，包括大图加载等模式
	
	public FragmentPage2TabStoreAdapter(Context context, int screenWidht){
		this.mInflater=LayoutInflater.from(context);
		this.imageheight = (screenWidht - Util.Dp2Px(context,24))/2; 
		
		choicePicOptn = new DisplayImageOptions.Builder()
		.showImageOnLoading(new ColorDrawable(context.getResources().getColor(R.color.cbg_color))) 
		.showImageForEmptyUri(new ColorDrawable(context.getResources().getColor(R.color.cbg_color)))
		.showImageOnFail(new ColorDrawable(context.getResources().getColor(R.color.cbg_color)))
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中 
		.cacheOnDisk(true)
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示  
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
		.resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位  
		.displayer(new RoundedBitmapDisplayer(6))//设置图片的圆角
		.build();//构建完成  
	}
	
	public void setData(List<TaocanBean> goodsList){
		this.goodsList = goodsList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return goodsList == null ? 0 : goodsList.size();
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
		TaocanBean bean = goodsList.get(position);
		ViewHolder holder = null;
		if(convertView != null && convertView.getTag() !=null){
			holder = (ViewHolder) convertView.getTag();
			clearCache(holder);
		}else{
			holder=new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_goods_list, null);
			holder.goodsImg = (ImageView) convertView.findViewById(R.id.item_goodsimg);
			holder.goodsTitle = (TextView) convertView.findViewById(R.id.item_goods_title);
			holder.goodsSell = (TextView) convertView.findViewById(R.id.item_goods_sell);
			holder.bottom = convertView.findViewById(R.id.view_botten);
			RelativeLayout.LayoutParams params = (LayoutParams) holder.goodsImg.getLayoutParams();
			params.height = imageheight;
			holder.goodsImg.setLayoutParams(params);
			convertView.setTag(holder);
		}
		
		if(getCount() > 3 && position == getCount() -1 )
			holder.bottom.setVisibility(View.VISIBLE);
		else
			holder.bottom.setVisibility(View.GONE);
		
		ImageLoader.getInstance().displayImage(bean.getPicture(), bean.getPicture_key(), holder.goodsImg, choicePicOptn);
		
		holder.goodsTitle.setText(bean.getTitle());
		if("1".equals(bean.getTc_status())){
			holder.goodsSell.setText("已售：" + bean.getTcsalenum());
		}else{
			holder.goodsSell.setText("售罄");
		}
		
		return convertView;
	}

	class ViewHolder{
		ImageView goodsImg;
		TextView goodsTitle, goodsSell;
		View bottom;
	}
	
	private void clearCache(ViewHolder holder){
		holder.goodsImg.setImageResource(0);
		holder.goodsTitle.setText("");
		holder.goodsSell.setText("已售：0");
	}

}
