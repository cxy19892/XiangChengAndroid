package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.MsgListBean;
import com.yzm.sleep.utils.CustomImageLoadingListener;
import com.yzm.sleep.utils.EaseSmileUtils;
import com.yzm.sleep.utils.Util;

public class MessageAdapter extends BaseAdapter {

	private List<MsgListBean> messages;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public MessageAdapter(Context context){
		this.mContext=context;
		this.mInflater=LayoutInflater.from(context);
	}
	
	public void setData(List<MsgListBean> messages){
		this.messages=messages;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return messages == null ? 0 : messages.size();
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
		MsgListBean bean=messages.get(position);
		ViewHolder holder=null;
		if(convertView!=null && convertView.getTag()!=null)
			holder=(ViewHolder) convertView.getTag();
		else{
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_message, null);
			holder.tvUsername=(TextView) convertView.findViewById(R.id.tv_username);
			holder.tvMsgContent=(TextView) convertView.findViewById(R.id.tv_message_content);
			holder.tvDate=(TextView) convertView.findViewById(R.id.tv_date);
			holder.hasMsg=(ImageView) convertView.findViewById(R.id.iv_hasnew_mag);
			holder.userHead=(CircleImageView) convertView.findViewById(R.id.iv_userhead);
			holder.userType=(ImageView) convertView.findViewById(R.id.user_type);
			convertView.setTag(holder);
		}
		
		if("1".equals(bean.getMsgListTyp())){
			ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_ms, holder.userHead);
		}else{
			ImageLoader.getInstance().displayImage(bean.getUserUrl(), bean.getUserKey(), holder.userHead, MyApplication.headPicOptn,
					new CustomImageLoadingListener(mContext, holder.userHead, bean.getUserKey()));
		}
		
		holder.tvUsername.setText(bean.getUserName());
		Spannable span = EaseSmileUtils.getSmiledText(mContext, bean.getMessage());
        // 设置内容
		holder.tvMsgContent.setText(span, BufferType.SPANNABLE);
		holder.tvDate.setText(Util.getTime(bean.getDate()));
		
		if(bean.getMsgCount()>0)
			holder.hasMsg.setVisibility(View.VISIBLE);
		else
			holder.hasMsg.setVisibility(View.GONE);
		
		if("1".equals(bean.getType()))
			holder.userType.setVisibility(View.VISIBLE);
		else
			holder.userType.setVisibility(View.GONE);
		
		return convertView;
	}

	class ViewHolder {
		TextView tvUsername, tvMsgContent, tvDate;
		ImageView hasMsg,userType;
		CircleImageView userHead;
	}
}
