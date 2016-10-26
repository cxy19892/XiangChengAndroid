package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.DoctorBean;
import com.yzm.sleep.utils.LogUtil;

public class ClinicDoctorAdapter extends BaseAdapter {
	private Context mContext;
	private List<DoctorBean> mData;

	public ClinicDoctorAdapter(Context context) {
		this.mContext = context;
	}

	public void setData(List<DoctorBean> data) {
		this.mData = data;
		notifyDataSetInvalidated();
	}

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		DoctorBean clinicDoctor = mData.get(position);
		ViewHoldr holder = null;
		if (convertView == null) {
			holder = new ViewHoldr();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_clinic_doctor, null);
			holder.doctorHeader = (CircleImageView) convertView
					.findViewById(R.id.civDoctorHead);
			holder.ivUserType = (ImageView) convertView
					.findViewById(R.id.ivUserType);
			holder.tvDoctor = (TextView) convertView
					.findViewById(R.id.tvDoctor);
			holder.tvZhichen = (TextView) convertView
					.findViewById(R.id.tvZhichen);
			holder.tvDoctorIntro = (TextView) convertView
					.findViewById(R.id.tvDoctorIntro);
			holder.btnMore = (Button) convertView.findViewById(R.id.btnMore);
			convertView.setTag(holder);
		} else {
			holder = (ViewHoldr) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(clinicDoctor.getProfile(),
				clinicDoctor.getProfile_key(), holder.doctorHeader, MyApplication.defaultOption);
//		holder.ivUserType
		holder.tvDoctor.setText(clinicDoctor.getName());
		holder.tvZhichen.setText(clinicDoctor.getZhicheng()+" | "+ clinicDoctor.getDanwei());
		
		holder.tvDoctorIntro.getViewTreeObserver().addOnPreDrawListener(new MyRunnable(holder, position));
		holder.tvDoctorIntro.setText(clinicDoctor.getIntro());
		holder.btnMore.setOnClickListener(new MyOnClickListener(holder, position));
		return convertView;
	}

	private class ViewHoldr {
		CircleImageView doctorHeader;
		ImageView ivUserType;
		TextView tvDoctor;
		TextView tvZhichen;
		TextView tvDoctorIntro;
		Button btnMore;
	}
	
	class MyRunnable implements OnPreDrawListener{
		ViewHoldr rHolder;
		int rPosition;
		public MyRunnable(ViewHoldr holder,int position){
			this.rHolder = holder;
			this.rPosition = position;
		}
		@Override
		public boolean onPreDraw() {
			int lineCount;
			if(rHolder.tvDoctorIntro.getTag() != null)
				lineCount = (Integer) rHolder.tvDoctorIntro.getTag();
			else{
				lineCount = rHolder.tvDoctorIntro.getLineCount();
				rHolder.tvDoctorIntro.setTag(lineCount);
			}
			
			if (lineCount >= 6) {
				rHolder.btnMore.setVisibility(View.VISIBLE);
				if (mData.get(rPosition).isMore()) {
					rHolder.btnMore.setText("收起");
					rHolder.tvDoctorIntro.setMaxLines(Integer.MAX_VALUE);
				}else{
					rHolder.btnMore.setText("查看更多");
					rHolder.tvDoctorIntro.setMaxLines(5);
				}
			} else {
				rHolder.btnMore.setVisibility(View.GONE);
			}
			return true;
		}
		
	}
	
	private class MyOnClickListener implements OnClickListener{
		ViewHoldr yHolder;
		int yPosition;
		public MyOnClickListener(ViewHoldr holder,int position){
			this.yHolder = holder;
			this.yPosition = position;
		}

		@Override
		public void onClick(View v) {
			mData.get(yPosition).setMore(!mData.get(yPosition).isMore());
			if (mData.get(yPosition).isMore()) {
				yHolder.btnMore.setText("收起");
				yHolder.tvDoctorIntro.setMaxLines(Integer.MAX_VALUE);
			}else{
				yHolder.btnMore.setText("查看更多");
				yHolder.tvDoctorIntro.setMaxLines(5);
			}
		}
		
	}

}
