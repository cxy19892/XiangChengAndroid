package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.DoctorBean;
/**
 * 专家适配器
 * @author Administrator
 *
 */
public class FragmentPage2Tab2ExpertsAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater inflater;
	private List<DoctorBean> list;
	public FragmentPage2Tab2ExpertsAdapter(Context mContext){
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}
	
	public void setData(List<DoctorBean> list){
		this.list = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoder hoder = null;
		if(convertView == null){
			hoder = new ViewHoder();
			convertView = inflater.inflate(R.layout.item_fragmentpage3experts, null);
			hoder.img_medical = (CircleImageView) convertView.findViewById(R.id.imgexperts);
			hoder.tv_expertstype = (TextView) convertView.findViewById(R.id.tv_expertstype);
			hoder.tv_expertsName = (TextView) convertView.findViewById(R.id.tv_expertsName);
			hoder.tv_expertsConte = (TextView) convertView.findViewById(R.id.tv_expertsConte);
			hoder.view_experts = (View) convertView.findViewById(R.id.view_experts);
			convertView.setTag(hoder); 
		}else{
			hoder = (ViewHoder) convertView.getTag();
			clearCache(hoder);
		}
		if(position != 0){
			hoder.view_experts.setVisibility(View.GONE);
		}
		hoder.tv_expertsName.setText(list.get(position).getName());
		hoder.tv_expertstype.setText(list.get(position).getZhicheng() +" | "+ list.get(position).getDanwei());
		hoder.tv_expertsConte.setText(list.get(position).getIntro());
		
		ImageLoader.getInstance().displayImage(
				list.get(position).getProfile(),
				list.get(position).getProfile_key(),
				hoder.img_medical, MyApplication.headPicOptn);
		
		return convertView;
	}

	public class ViewHoder{
		TextView tv_expertsName;
		TextView tv_expertstype;
		CircleImageView img_medical;
		TextView tv_expertsConte;
		View view_experts;
	}
	private void clearCache(ViewHoder holder) {
		holder.tv_expertsName.setText("");
		holder.tv_expertstype.setText("");
		holder.img_medical.setImageResource(0);
		holder.tv_expertsConte.setText("");
	}
}
