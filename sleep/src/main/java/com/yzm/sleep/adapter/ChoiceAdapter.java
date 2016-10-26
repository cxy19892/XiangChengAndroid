package com.yzm.sleep.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.utils.LogUtil;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 精选适配器
 * 
 * @author Administrator
 * 
 */
public class ChoiceAdapter extends BaseAdapter {
	private List<ArticleBean> mExpanlist;
	private Context context;
	private LayoutInflater inflater;
	private int imgSize;

	public ChoiceAdapter(Context context, int screenWidht) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.imgSize = screenWidht;
	}

	public void setData(List<ArticleBean> myExpanlist) {
		this.mExpanlist = myExpanlist;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mExpanlist == null ? 0 : mExpanlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mExpanlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int groupPosition, View convertView, ViewGroup arg2) {
		ArticleBean bean = mExpanlist.get(groupPosition);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.my_item_expand1, null);
			holder.imChoice = (ImageView) convertView
					.findViewById(R.id.imChoice);
			holder.tvChoiceTitle = (TextView) convertView
					.findViewById(R.id.tvChoiceTitle);
			holder.tvChoiceCont = (TextView) convertView
					.findViewById(R.id.tvChoiceCont);
			LayoutParams mParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mParam.width = imgSize;
			mParam.height = imgSize;
			holder.imChoice.setLayoutParams(mParam);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(TextUtils.isEmpty(bean.getT_subject())){
			holder.tvChoiceTitle.setText("香橙话题");
		}else{
			holder.tvChoiceTitle.setText(bean.getT_subject());
		}
		holder.tvChoiceCont.setText(bean.getT_message());
		ImageLoader.getInstance().displayImage(bean.getBjpic(),bean.getBjpickey(),holder.imChoice,MyApplication.choicePicOptn);
		
		return convertView;
	}

	class ViewHolder {
		ImageView imChoice;//
		TextView tvChoiceTitle;//
		TextView tvChoiceCont;//
	}

}
