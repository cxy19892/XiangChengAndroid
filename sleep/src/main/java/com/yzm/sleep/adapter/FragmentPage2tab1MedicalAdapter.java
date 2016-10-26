package com.yzm.sleep.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.FragmentPage3GroupAdapter.ViewHolder;
import com.yzm.sleep.bean.ClinicBean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
/**
 * 医馆适配器
 * @author Administrator
 *
 */
public class FragmentPage2tab1MedicalAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
	private int screenWidht;
	private List<ClinicBean> beanList;
	public DisplayImageOptions choicePicOptn; //默认图片加载模式，包括大图加载等模式
	
	public FragmentPage2tab1MedicalAdapter(Context mContext,int screenWidht){
		this.mContext = mContext;
		this.screenWidht = screenWidht;
		inflater = LayoutInflater.from(mContext);
		choicePicOptn = new DisplayImageOptions.Builder()
		.showImageOnLoading(new ColorDrawable(mContext.getResources().getColor(R.color.cbg_color))) 
		.showImageForEmptyUri(new ColorDrawable(mContext.getResources().getColor(R.color.cbg_color)))
		.showImageOnFail(new ColorDrawable(mContext.getResources().getColor(R.color.cbg_color)))
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中 
		.cacheOnDisk(true)
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示  
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
		.resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位  
		.displayer(new RoundedBitmapDisplayer(6))//设置图片的圆角
		.build();//构建完成  
	}
	
	public void setData(List<ClinicBean> beanList){
		this.beanList = beanList;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beanList!=null ? beanList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return beanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoder hoder = null;
		if(convertView == null){
			hoder = new ViewHoder();
			convertView = inflater.inflate(R.layout.item_fragmentpage3medical, null);
			hoder.img_medical = (ImageView) convertView.findViewById(R.id.img_medical);
			hoder.tv_medicalCont = (TextView) convertView.findViewById(R.id.tv_medicalCont);
			hoder.tv_medicalName = (TextView) convertView.findViewById(R.id.tv_medicalName);
			
			LayoutParams lp = (LayoutParams) hoder.img_medical.getLayoutParams();
			lp.width = screenWidht - 20;
			lp.height = lp.width * 1/2;
			hoder.img_medical.setLayoutParams(lp);
			
			convertView.setTag(hoder);
		}else{
			hoder = (ViewHoder) convertView.getTag();
			clearCache(hoder);
		}
		
		//http://f1.topit.me/1/a6/c8/1188974892510c8a61o.jpg
		hoder.tv_medicalCont.setText(beanList.get(position).getAddress());
		hoder.tv_medicalName.setText(beanList.get(position).getName());
		ImageLoader.getInstance().displayImage(
				beanList.get(position).getPicture(),
				beanList.get(position).getPicture_key(),
				hoder.img_medical, choicePicOptn);
		
		return convertView;
	}

	public class ViewHoder{
		TextView tv_medicalName;
		TextView tv_medicalCont;
		ImageView img_medical;
		
	}
	private void clearCache(ViewHoder holder) {
		holder.tv_medicalName.setText("");
		holder.tv_medicalCont.setText("");
		holder.img_medical.setImageResource(0);
	}
}
