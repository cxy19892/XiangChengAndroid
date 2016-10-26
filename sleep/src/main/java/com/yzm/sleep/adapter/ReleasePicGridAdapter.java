package com.yzm.sleep.adapter;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.ReleasePicbean;
import com.yzm.sleep.utils.Util;

public class ReleasePicGridAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private ReleasePicbean mReleasePicbean;
	private int picwidth = 78;
	private Context context;
	private ImageUploadLisenner mImageUploadLisenner;
	
	public ReleasePicGridAdapter(Context context,int Screenwidth) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.picwidth = (Screenwidth-Util.Dp2Px(context, 28))/3;
	}
	
	public void setData(ReleasePicbean releasePicbean){
		this.mReleasePicbean = releasePicbean;
		notifyDataSetChanged();
	}
	
	public void setLisenner(ImageUploadLisenner imageUploadLisenner){
		this.mImageUploadLisenner = imageUploadLisenner;
	}
	
	public ReleasePicbean getData(){
		return this.mReleasePicbean;
	}

	public int getCount() {
		return mReleasePicbean == null ? 0 : (mReleasePicbean.getUpLoadImags().size());//SelectImgs().size());
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public void setSelectedPosition(int position) {
//		if(getCount()==10){
		selectedPosition = position;
//		}else{
//			selectedPosition = position-1;
//		}
		
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		System.gc();
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_published_grida,
					parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			holder.imagedel = (ImageView) convertView.findViewById(R.id.item_grida_image_del);
			holder.percenttv = (TextView) convertView.findViewById(R.id.item_grida_tv_percent);
			LayoutParams lp = convertView.getLayoutParams();
			lp.height = this.picwidth;
			lp.width  = this.picwidth;
			convertView.setLayoutParams(lp);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(parent.getChildCount() == position)  
		{
			if("add".equals(mReleasePicbean.getUpLoadImags().get(position).getKey())){
				holder.image.setImageResource(R.drawable.btn_add_pic_click);/*(BitmapFactory.decodeResource(
					context.getResources(), ));*/
				holder.imagedel.setVisibility(View.GONE);
				holder.percenttv.setVisibility(View.GONE);
				if(position == 0/*mReleasePicbean.getUpLoadImags().size()*/){
					holder.image.setOnClickListener(mImageUploadLisenner.ClickLisenner(3, holder.image, null));
				}
			}else {
				holder.image.setOnClickListener(null);
				holder.imagedel.setVisibility(View.VISIBLE);

				//				ImageLoader.getInstance(1,Type.LIFO).loadImage(mReleasePicbean.getSelectImgs().get(position), holder.image);

				String strPath = mReleasePicbean.getUpLoadImags().get(position).getPath();
				String Path = "";
				if(getCount() == 9 && !"add".equals(mReleasePicbean.getUpLoadImags().get(0).getKey())){
					Path = (strPath==null || "".equals(strPath))?mReleasePicbean.getSelectImgs().get(position):strPath;
				}else if(!"add".equals(mReleasePicbean.getUpLoadImags().get(0).getKey())){
					Path = (strPath==null || "".equals(strPath))?mReleasePicbean.getSelectImgs().get(position):strPath;
				}else{
					Path = (strPath==null || "".equals(strPath))?mReleasePicbean.getSelectImgs().get(position-1):strPath;
				}
				
//								holder.image.setImageBitmap(ImageCompressUtil.readBitmapFromSD(Path, 4));
//				try {
//					Picasso.with(context)
//					.load(new File(Path))
//					.placeholder(R.drawable.default_choice_icon)
//					//.error(R.drawable.default_error)
//					.resize(this.picwidth, this.picwidth)
//					.centerCrop()
//					.into(holder.image);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				ImageLoader.getInstance().displayImage("file://"+Path, holder.image, MyApplication.defaultOption, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						LayoutParams params = arg1.getLayoutParams();  
				        params.height = picwidth; 
				        params.width  = picwidth;
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});//("file://"+Path, holder.image, MyApplication.defaultOption);
				if(mReleasePicbean.getUpLoadImags().get(position).isIscanceled()){
					holder.percenttv.setVisibility(View.VISIBLE);
					holder.percenttv.setBackgroundResource(R.drawable.uploading_failure2);
				}else{
					if(mReleasePicbean.getUpLoadImags().get(position).isIscomplete()){
						holder.percenttv.setVisibility(View.GONE);
					}else{
						holder.percenttv.setVisibility(View.GONE);
//						holder.percenttv.setText((int)(mReleasePicbean.getUpLoadImags().get(position).getPercent()*100)+"%");
					}
				}
				if(mImageUploadLisenner != null && !mReleasePicbean.getUpLoadImags().get(position).isIscomplete()){
					mImageUploadLisenner.ImageUploadCallBack(holder.percenttv, mReleasePicbean.getUpLoadImags().get(position).getKey());
				}
				if(mImageUploadLisenner != null){
					holder.imagedel.setOnClickListener(mImageUploadLisenner.ClickLisenner(1, holder.imagedel, mReleasePicbean.getUpLoadImags().get(position).getKey()));
					holder.image.setOnClickListener(mImageUploadLisenner.ClickLisenner(4, holder.image, mReleasePicbean.getUpLoadImags().get(position).getKey()));
				}

			}
		}
		

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
		public ImageView imagedel;
		public TextView percenttv;
	}
	
	public interface ImageUploadLisenner{
		public void ImageUploadCallBack(TextView tv, String key);
		public OnClickListener ClickLisenner(int type, View view, String key);
	}

}
