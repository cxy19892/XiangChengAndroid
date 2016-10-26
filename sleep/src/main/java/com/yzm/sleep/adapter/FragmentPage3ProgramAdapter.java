package com.yzm.sleep.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.CommunityEventBean;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
/**
 * 活动适配器
 * @author Administrator
 *
 */
public class FragmentPage3ProgramAdapter extends BaseAdapter{
	private int screenWidht;
	private Activity mConttext;
	private LayoutInflater inflater;
	private List<CommunityEventBean> progList;
	public ViewHolder hoder = null;
	public FragmentPage3ProgramAdapter(Activity mConttext,int screenWidht){
		this.mConttext = mConttext;
		this.screenWidht = screenWidht;
		inflater = LayoutInflater.from(mConttext);
	}
	
	public void setData(List<CommunityEventBean> progList){
		this.progList = progList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return progList == null ? 0 : progList.size();
	}

	@Override
	public Object getItem(int position) {
		return progList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommunityEventBean bean = progList.get(position);
		if(convertView==null){
			hoder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_program, null);
			hoder.improgran = (ImageView) convertView.findViewById(R.id.improGram);
			hoder.tvProgramNuber = (TextView) convertView.findViewById(R.id.tvProgramNuber);
			LayoutParams lp = (LayoutParams) hoder.improgran.getLayoutParams();
			lp.width = screenWidht;
			lp.height = lp.width * 1/2;
			hoder.improgran.setLayoutParams(lp);
			convertView.setTag(hoder);
		}else{
			hoder = (ViewHolder) convertView.getTag();
			clearCache(hoder);
		}
		hoder.tvProgramNuber.setText("参与人数:"+bean.getNumbers());
		ImageLoader.getInstance().displayImage(
				bean.getPicture(),
				bean.getPicturekey(),
				hoder.improgran, MyApplication.defaultOption);
		
		
		
		return convertView;
	}

	public class ViewHolder{
		ImageView improgran;
		TextView tvProgramNuber;
	}
	private void clearCache(ViewHolder holder) {
		holder.tvProgramNuber.setText("");
		holder.improgran.setImageResource(0);
	}
}
