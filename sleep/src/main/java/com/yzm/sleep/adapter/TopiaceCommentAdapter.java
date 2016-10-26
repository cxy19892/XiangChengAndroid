package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.ArticleCommentBean;
import com.yzm.sleep.bean.ArticleThumbUpUserBean;
import com.yzm.sleep.sticky.StickyListHeadersAdapter;
import com.yzm.sleep.tools.AnimationUtil;
import com.yzm.sleep.utils.CustomImageLoadingListener;
import com.yzm.sleep.utils.EaseSmileUtils;
import com.yzm.sleep.utils.TimeFormatUtil;

public class TopiaceCommentAdapter extends BaseAdapter implements StickyListHeadersAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<ArticleCommentBean> commentData;
	private ClickListenerAdapter cusListener;
	private List<ArticleThumbUpUserBean> tZanUser;
	private String tzanNum,status;
	private boolean isShowItem;
	
	public interface ClickListenerAdapter{
		public void listItemInClick(int type,int position,ArticleCommentBean bean);
		public void onItemClick(final ArticleCommentBean bean);
		public void onItemLongClick(int position,final ArticleCommentBean bean);
		public void onItemHeadClick(int headPosition);
	}
	
	public TopiaceCommentAdapter(Context context,ClickListenerAdapter cusListener){
		this.context=context;
		this.mInflater=LayoutInflater.from(context);
		this.cusListener=cusListener;
	}

	public void setData(List<ArticleCommentBean> commentData,boolean isShowItem){
		this.commentData=commentData;
		this.isShowItem=isShowItem;
		this.notifyDataSetChanged();
	}
	
	/**
	 * 设置头数据
	 */
	public void setHeadData(boolean isShowItem,List<ArticleThumbUpUserBean> tZanUser,String tzanNum,String status){
		this.isShowItem=isShowItem;
		this.tZanUser=tZanUser;
		this.tzanNum=tzanNum;
		this.status=status;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(isShowItem)
			return commentData == null ? 1: (commentData.size()<=0 ? 1 : commentData.size());
		else
			return commentData == null ? 0: commentData.size();
	}

	@Override
	public ArticleCommentBean getItem(int position) {
		return commentData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(commentData !=null && commentData.size()>0){
			final ArticleCommentBean bean=commentData.get(position);
			ViewHolder holder=null;
			if (convertView!=null && convertView.getTag()!=null) {
				holder=(ViewHolder) convertView.getTag();
			}else{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.item_comments_layout,null);
				holder.userHead=(CircleImageView) convertView.findViewById(R.id.comment_userhead);
				holder.userName=(TextView) convertView.findViewById(R.id.comment_username);
				holder.commentTime=(TextView) convertView.findViewById(R.id.comment_time);
				holder.commentContent=(TextView) convertView.findViewById(R.id.comment_conten);
				holder.userType=(ImageView) convertView.findViewById(R.id.user_type);
				holder.commentReply=(TextView) convertView.findViewById(R.id.comment_reply);
				holder.commentReplyName=(TextView) convertView.findViewById(R.id.comment_replyname);
				convertView.setTag(holder);
			}
			
			holder.userName.setText(bean.getP_author());
			holder.commentTime.setText(TimeFormatUtil.getTimeBeforeCurrentTime(bean.getP_dateline()));
			if ("1".equals(bean.getIs_zj())) 
				holder.userType.setVisibility(View.VISIBLE);
			else
				holder.userType.setVisibility(View.GONE);
			//头像
			ImageLoader.getInstance().displayImage(bean.getTouxiang(), bean.getTouxiang_key(), holder.userHead,
					MyApplication.headPicOptn,new CustomImageLoadingListener(context, holder.userHead, bean.getTouxiang_key()));
			Spannable span;
			if(!TextUtils.isEmpty(bean.getP_touid()) && !TextUtils.isEmpty(bean.getP_toauthor())){
				//  回复  
				holder.commentReply.setVisibility(View.VISIBLE);
				holder.commentReplyName.setVisibility(View.VISIBLE);
				String reply="回复";
				String replyName=" "+bean.getP_toauthor()+" ";
				holder.commentReplyName.setText(replyName);
				String contentMse=reply+replyName+": "+bean.getP_message();
				holder.commentReplyName.setOnClickListener(new MyClickListener(position, bean));
				span=EaseSmileUtils.getSmiledText(context, contentMse);
				span.setSpan(new ForegroundColorSpan(0xff6f9ef6), reply.length(), (reply+replyName).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.commentContent.setText(span);
			}else{
				holder.commentReply.setVisibility(View.GONE);
				holder.commentReplyName.setVisibility(View.GONE);
				// 评论
				span = EaseSmileUtils.getSmiledText(context, bean.getP_message());
				holder.commentContent.setText(span);
			}
			holder.userName.setOnClickListener(new MyClickListener(position, bean));
			holder.userHead.setOnClickListener(new MyClickListener(position, bean));
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					cusListener.onItemClick(bean);
				}
			});
			
			convertView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
//					cusListener.onItemLongClick(position,bean);
					return false;
				}
			});
			
			return convertView;
		}else{
			convertView =new TextView(context);
			convertView.setMinimumHeight(0);
			return convertView;
		}
	}
	
	class ViewHolder{
		CircleImageView userHead;
		TextView userName,commentTime,commentContent,commentReply,commentReplyName;
		ImageView userType;
	}
	
	class MyClickListener implements OnClickListener{
		private int position;
		private ArticleCommentBean bean;
		private HeadHolder holder;
		
		public MyClickListener(int position ,ArticleCommentBean bean){
			this.position=position;
			this.bean=bean;
		}
		
		public MyClickListener(HeadHolder holder,int position ,ArticleCommentBean bean){
			this.position=position;
			this.bean=bean;
			this.holder=holder;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.comment_replyname:
				cusListener.listItemInClick(0,position, bean);
				break;
			case R.id.comment_userhead:
			case R.id.comment_username:
				cusListener.listItemInClick(2,position, bean);
				break;
			case R.id.iv_praise:
				if(holder!=null)
					AnimationUtil.btnClickEffect(holder.ivPraise);
				cusListener.listItemInClick(3,position, bean);
				break;
			case R.id.iv_share:
				cusListener.listItemInClick(4,position, bean);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeadHolder holder=null;
		if(convertView != null && convertView.getTag() != null){
			holder=(HeadHolder) convertView.getTag();
		}else{
			holder=new HeadHolder();
			convertView=mInflater.inflate(R.layout.topice_detail_stickyhead, null);
			holder.heads=(LinearLayout) convertView.findViewById(R.id.rl_head_op);
			holder.ivPraise=(Button) convertView.findViewById(R.id.iv_praise);
			holder.ivShare=(Button) convertView.findViewById(R.id.iv_share);
			holder.praiseTotal=(TextView) convertView.findViewById(R.id.praise_total);
			convertView.setTag(holder);
		}
		
		if(!TextUtils.isEmpty(tzanNum)){
			try {
				if(Integer.parseInt(tzanNum)>0)
					 holder.praiseTotal.setText("有"+tzanNum+"个人点赞...");
				else
					holder.praiseTotal.setText("暂无点赞");
			} catch (Exception e) {
				holder.praiseTotal.setText("暂无点赞");
			}
		}else
			holder.praiseTotal.setText("暂无点赞");
		
		praiseCheck("1".equals(status),holder.ivPraise);
		holder.heads.removeAllViews();
		holder.ivPraise.setOnClickListener(new MyClickListener(holder,0,null));
		holder.ivShare.setOnClickListener(new MyClickListener(holder, 0,null));
		
		for(int i=0 ; i < (tZanUser==null ? 0 : ((tZanUser.size()>3) ? 3:tZanUser.size()));i++){
			ArticleThumbUpUserBean bean=tZanUser.get(i);
			CircleImageView  head=new CircleImageView(context);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(54, 54);
			head.setFocusable(false);
			head.setFocusableInTouchMode(true);
			lp.setMargins(22, 0, 0, 0);
			head.setLayoutParams(lp);
			ImageLoader.getInstance().displayImage(bean.getAuthor_profile(), bean.getAuthor_profile_key(), head, MyApplication.headPicOptn);
			holder.heads.addView(head,i);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cusListener.onItemHeadClick(0);
			}
		});
		
		return convertView;
	}
	
	class HeadHolder{
		LinearLayout heads;
		Button ivPraise,ivShare;
		TextView praiseTotal;
	}

	@Override
	public long getHeaderId(int position) {
		return 0;
	}

	private void praiseCheck(boolean check, Button view) {
		if(view==null)
			return;
		Drawable praiseCheck = context.getResources().getDrawable(
				R.drawable.ic_shequ_zan_pressed);
		Drawable praiseNoCheck = context.getResources().getDrawable(
				R.drawable.ic_shequ_zan_normal);
		praiseCheck.setBounds(0, 0, praiseCheck.getMinimumWidth(),
				praiseCheck.getMinimumHeight());
		praiseNoCheck.setBounds(0, 0, praiseNoCheck.getMinimumWidth(),
				praiseNoCheck.getMinimumHeight());
		if (check) {
			view.setCompoundDrawables(praiseCheck, null, null, null);
		} else {
			view.setCompoundDrawables(praiseNoCheck, null, null, null);
		}
	}
}
