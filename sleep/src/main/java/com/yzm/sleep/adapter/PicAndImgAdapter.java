package com.yzm.sleep.adapter;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.BePushBean;
import com.yzm.sleep.utils.Util;

public class PicAndImgAdapter extends BaseAdapter {

	private int Screenwith;
	private List<BePushBean> mlistBeans;
	private LayoutInflater inflater;
	private int Columns;
	private Context mcontext;
	
	private DisplayImageOptions choicePicOptn;
	
	public PicAndImgAdapter(Context mcontext, int Screenwith, int Columns){
		inflater = LayoutInflater.from(mcontext);
		this.Screenwith = Screenwith;
		this.Columns = Columns;
		this.mcontext = mcontext;
		if(choicePicOptn == null){
			choicePicOptn = new DisplayImageOptions.Builder()  
			.showImageOnLoading(new ColorDrawable(mcontext.getResources().getColor(R.color.cbg_color))) //设置图片在下载期间显示的图片  
			.showImageForEmptyUri(new ColorDrawable(mcontext.getResources().getColor(R.color.cbg_color)))//设置图片Uri为空或是错误的时候显示的图片  
			.showImageOnFail(new ColorDrawable(mcontext.getResources().getColor(R.color.cbg_color)))  //设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)//设置下载的图片是否缓存在内存中 
			.cacheOnDisk(true)
			.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示  
			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
			.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
			.build();//构建完成  
		}
	}
	
	public void setData(List<BePushBean> mlistBeans){
		this.mlistBeans = mlistBeans;
		notifyDataSetChanged();
	}
	
	public List<BePushBean> getData(){
		return  mlistBeans;
	}
	
	@Override
	public int getCount() {
		if(mlistBeans != null){
			return mlistBeans.size();
		}else
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlistBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		BePushBean bean = mlistBeans.get(position);
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_pic_text_vertical, null);
			holder.itemtv = (TextView) convertView.findViewById(R.id.item_tv);
			holder.itemimg = (ImageView) convertView.findViewById(R.id.item_img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			clearCache(holder);
		}
		holder.itemtv.setText(bean.getTitle());
		ImageLoader.getInstance().displayImage(bean
				.getPicture(), holder.itemimg, choicePicOptn);/*displayImage(bean
				.getPicture(), bean.getPicture_key(), holder.itemimg, MyApplication.headPicOptn);*/
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams((Screenwith-Util.Dp2Px(mcontext, 20))/Columns,LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((Screenwith-Util.Dp2Px(mcontext, 20))/(Columns+2),(Screenwith-Util.Dp2Px(mcontext, 20))/(Columns+2));
		holder.itemimg.setLayoutParams(lp2);
		convertView.setLayoutParams(lp);
		return convertView;
	}
	class ViewHolder {
		TextView itemtv;
		ImageView itemimg;
	}
	private void clearCache(ViewHolder holder) {
		holder.itemtv.setText("");
		holder.itemimg.setImageResource(0);

	}
}
