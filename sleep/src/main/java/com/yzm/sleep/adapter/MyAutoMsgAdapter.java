package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.AutoMsgBean;
import com.yzm.sleep.bean.ContentAttachment;
import com.yzm.sleep.utils.EaseSmileUtils;
import com.yzm.sleep.utils.Util;

public class MyAutoMsgAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<AutoMsgBean> datas;
	private Context context;
	public MyAutoMsgAdapter(Context context,List<AutoMsgBean> datas){
		this.context = context;
		this.mInflater=LayoutInflater.from(context);
		this.datas=datas;
	}
	
	/**
	 * 设置数据
	 * @param datas
	 */
	public void setDatas(List<AutoMsgBean> datas){
		this.datas=datas;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas==null ? 0: datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		AutoMsgBean messageBean=datas.get(position);
		ViewHolder holder=null;
		if(contentView!=null&&contentView.getTag()!=null){
			holder=(ViewHolder) contentView.getTag();
			clearCache(holder);
		}else{
			holder=new ViewHolder();
			contentView=mInflater.inflate(R.layout.item_my_message_list, null);
			holder.userHead=(CircleImageView) contentView.findViewById(R.id.item_mymessage_user_head_img);
			holder.userNickname=(TextView) contentView.findViewById(R.id.item_mymessage_user_nickname);
			holder.time=(TextView) contentView.findViewById(R.id.item_mymessage_time);
			holder.content=(TextView) contentView.findViewById(R.id.item_mymessage_content);
			holder.topiceContent=(TextView) contentView.findViewById(R.id.topice_content);
			holder.topiceImage=(ImageView) contentView.findViewById(R.id.topice_image);
			holder.ivZan=(ImageView) contentView.findViewById(R.id.iv_zan);
			contentView.setTag(holder);
		}
		ImageLoader.getInstance().displayImage(messageBean.getUser_profile(),messageBean.getUser_profile_key(), holder.userHead,MyApplication.headPicOptn);
		holder.userNickname.setText(messageBean.getNickname());
		try{
			long time=Long.parseLong(messageBean.getDateline());
			holder.time.setText(Util.getTime(time*1000));
		}catch(Exception e){
			holder.time.setText(messageBean.getDateline());
		}
		if("2".equals(messageBean.getType())){
			holder.content.setVisibility(View.GONE);
			holder.ivZan.setVisibility(View.VISIBLE);
		}else{
			holder.content.setVisibility(View.VISIBLE);
			holder.ivZan.setVisibility(View.GONE);
			holder.content.setText(EaseSmileUtils.getSmiledText(context,messageBean.getNotice()));
		}
		
		if(messageBean.getImages()!=null &&messageBean.getImages().size()>0){
			ContentAttachment bean = messageBean.getImages().get(0);
			holder.topiceContent.setVisibility(View.GONE);
			holder.topiceImage.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(bean.getContent_attachment_sl(), bean.getT_attachment_key_sl(),holder.topiceImage, MyApplication.choicePicOptn);
			
		}else {
			holder.topiceContent.setVisibility(View.VISIBLE);
			holder.topiceImage.setVisibility(View.GONE);
			holder.topiceContent.setText(messageBean.getThread_message());
		}
		return contentView;
	}
	
	class ViewHolder{
		CircleImageView userHead;
		TextView userNickname,time,content,topiceContent;
		ImageView topiceImage,ivZan;
	}
	
	private void clearCache(ViewHolder holder){
		holder.userHead.setImageResource(0);
		holder.time.setText("");
		holder.content.setText("");
		holder.topiceContent .setText("");
		holder.userNickname.setText("");
		holder.topiceImage .setImageResource(0);
	}

}
