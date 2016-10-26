package com.yzm.sleep.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.ProductListBean;
import com.yzm.sleep.utils.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ComboAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private int  imageHeight;
	private List<ProductListBean> datas;
	
	public ComboAdapter(Context context, List<ProductListBean> datas){
		this.mInflater= LayoutInflater.from(context);
		this.imageHeight= (Constant.screenWidht - Util.Dp2Px(context, 80))/3;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : (datas.size() > 3 ? 3 : datas.size());
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
		ViewHolder holder =null;
		ProductListBean bean = datas.get(position);
		if(convertView != null && convertView.getTag() !=null){
			holder=(ViewHolder) convertView.getTag();
		}else{
			holder=new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_combo_list, null);
			holder.pic = (ImageView) convertView.findViewById(R.id.iv_pic);
			holder.title = (TextView) convertView.findViewById(R.id.pic_title);
			holder.num = (TextView) convertView.findViewById(R.id.pic_num);
			RelativeLayout.LayoutParams params = (LayoutParams) holder.pic.getLayoutParams();
			params.height = imageHeight;
			holder.pic.setLayoutParams(params);
			convertView.setTag(holder);
		}
		
		ImageLoader.getInstance().displayImage(bean.getImg(), bean.getImg_key(), holder.pic, MyApplication.choicePicOptn);
		holder.title.setText(bean.getImg_title());
		holder.num.setText("x"+bean.getImg_num());
		return convertView;
	}
	
	class ViewHolder{
		TextView title, num;
		ImageView pic;
	}

}
