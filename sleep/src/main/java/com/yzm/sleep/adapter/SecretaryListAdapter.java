package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.CommunityTopiceDetailActivity;
import com.yzm.sleep.activity.community.ProgramDetailsActivity;
import com.yzm.sleep.activity.doctor.ReservationDetailActivity;
import com.yzm.sleep.bean.SecretaryMsgBean;

public class SecretaryListAdapter extends BaseAdapter {

	private List<SecretaryMsgBean> listMsg;
	private Context context;
	private LayoutInflater mInflater;

	public SecretaryListAdapter(Context mContext, List<SecretaryMsgBean> listMsg) {
		this.context = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		this.listMsg = listMsg;
	}

	@Override
	public int getCount() {
		return this.listMsg == null ? 0 : this.listMsg.size();
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
		SecretaryMsgBean bean = listMsg.get(position);
		ViewHolder holder = null;
		if (convertView != null && convertView.getTag() != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_xcsecretary_list,
					null);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.msg_content);
			holder.msgTitle = (TextView) convertView
					.findViewById(R.id.msg_title);
			holder.hasLink = (RelativeLayout) convertView
					.findViewById(R.id.has_link);
			convertView.setTag(holder);
		}

		if (bean.getType()==1 || bean.getType()==2 || bean.getType()==3 || bean.getType()==4) {
			holder.hasLink.setVisibility(View.VISIBLE);
			holder.msgTitle.setText(bean.getTitle());
			holder.hasLink.setOnClickListener(new MyClickListener(bean));
		} else {
			holder.hasLink.setVisibility(View.GONE);
		}
		holder.tvContent.setText(bean.getMsg());
		return convertView;
	}

	class ViewHolder {
		TextView tvContent, msgTitle;
		RelativeLayout hasLink;
	}

	class MyClickListener implements OnClickListener {
		private SecretaryMsgBean bean;

		public MyClickListener(SecretaryMsgBean bean) {
			this.bean = bean;
		}

		@Override
		public void onClick(View v) {
			switch (bean.getType()) {
			case 0:
				//不跳转
				break;
			case 1:
				//活动详情
				try {
					context.startActivity(new Intent(context,ProgramDetailsActivity.class).putExtra("id", bean.getMsgId()).putExtra("title", bean.getTitle()));
				} catch (Exception e) {
				}
				break;
			case 2:
				//话题详情
				try {
					context.startActivity(new Intent(context,CommunityTopiceDetailActivity.class)
					.putExtra("topices_id", bean.getMsgId()).putExtra("topices_title", bean.getTitle()));
				} catch (Exception e) {
				}
				break;
			case 3:
				//文章详情
				try {
					context.startActivity(new Intent(context,CommunityTopiceDetailActivity.class)
					.putExtra("topices_id", bean.getMsgId()).putExtra("topices_title", bean.getTitle()).putExtra("is_choiceness", true));
				} catch (Exception e) {
				}
				break;
			case 4:
				try {
					context.startActivity(new Intent(context,ReservationDetailActivity.class)
					.putExtra("id", bean.getMsgId()));
				} catch (Exception e) {
				}
			default:
				break;
			}
		}

	}

}
