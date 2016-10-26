package com.yzm.sleep.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.DateBean;

public class CalendarAdapter extends BaseAdapter {
	private Context mContext;
	private List<DateBean> mList;
	private int width;
	private boolean hasSelect;
	private String selectDayDate;
	
	public CalendarAdapter(Context context,int screenWidth,boolean hasSelect){
		this.mContext=context;
		this.width = (screenWidth - Dp2Px(context, 50))/7;
		this.hasSelect=hasSelect;
	}
	
	/**
	 * 
	 * @param mList
	 */
	public void setDate(List<DateBean> mList){
		this.mList=mList;
		this.notifyDataSetChanged();
	}
	
	public List<DateBean> getDate(){
		return this.mList;
	}
	
	public void setDate(List<DateBean> mList,String dayDate){
		this.mList=mList;
		this.selectDayDate=dayDate;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
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
	@SuppressLint("SimpleDateFormat") 
	public View getView(int position, View convertView, ViewGroup parent) {
		DateBean bean=mList.get(position);
		ViewHolder holder=null;
		if(convertView!=null && convertView.getTag()!=null){
			holder=(ViewHolder) convertView.getTag();
		}else{
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_gridview_calendar, null);
			holder.mTextView=(TextView) convertView.findViewById(R.id.item_text);
			holder.calenderP=(RelativeLayout) convertView.findViewById(R.id.item_calender_p);
			RelativeLayout.LayoutParams param=(LayoutParams) holder.calenderP.getLayoutParams();
			param.width=width;
			param.height=width;
			holder.calenderP.setLayoutParams(param);
			convertView.setTag(holder);
		}
		
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String str=new SimpleDateFormat("d").format(sdf.parse(bean.getDate()).getTime());
			holder.mTextView.setText(str);
			if(hasSelect){
				if(bean.getDate().equals(selectDayDate)){
					holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.cbg_color));
					holder.mTextView.setBackgroundResource(R.drawable.item_calender_type1);
				}else{
					holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.ct_color));
					holder.mTextView.setBackgroundColor(0x00000000);
				}
			}else{
				if (TextUtils.isEmpty(bean.getState())) {
					holder.mTextView.setTextColor(0xffffffff);
				}else{
					if("1".equals(bean.getState())){
						holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.cbg_color));
						holder.mTextView.setBackgroundResource(R.drawable.item_calender_type1);
					}else if("0".equals(bean.getState())){
						holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.ct_color));
						holder.mTextView.setBackgroundResource(R.drawable.item_calender_type2);
					}else{
						holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.ct_color));
						holder.mTextView.setBackgroundColor(0x00000000);
					}
				}
			}
		} catch (Exception e) {
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView mTextView;
		RelativeLayout calenderP;
	}

	/**
	 * dp格式的长度转为px格式的长度
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int Dp2Px(Context context, float dp) { 			
		final float scale = context.getResources().getDisplayMetrics().density; 			
		return (int) (dp * scale + 0.5f); 			
	}
}
