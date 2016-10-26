package com.yzm.sleep.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.ImageDetailActivity;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceTopicDeleteCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.TopicDeleteParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.widget.CustomGridView;

/**
 * 我的（适配器）
 * 
 * @author Administrator
 * 
 */
public class FragmentPage3FoundAdapter extends BaseAdapter {

	private List<ArticleBean> artList;
	private Activity mContext;// 上下文
	private LayoutInflater inflater;
	private int sreenWidth;
	private int mFrom = -1;
	private DisplayImageOptions defaultOption;

	public FragmentPage3FoundAdapter(Activity mContext, int screenWidth,
			int from) {
		this.sreenWidth = screenWidth;
		this.mContext = mContext;
		this.mFrom = from;
		inflater = LayoutInflater.from(mContext);
		defaultOption = new DisplayImageOptions.Builder()
				.showImageOnLoading(
						new ColorDrawable(mContext.getResources().getColor(
								R.color.cbg_color))) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(
						new ColorDrawable(mContext.getResources().getColor(
								R.color.cbg_color)))// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(
						new ColorDrawable(mContext.getResources().getColor(
								R.color.cbg_color))) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true).considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.build();// 构建完成
	}

	/**
	 * 刷新列表
	 * 
	 * @param foundList
	 */
	public void setData(List<ArticleBean> foundList) {
		this.artList = foundList;
		this.notifyDataSetChanged();
	}

	public void clearList() {
		List<ArticleBean> foundList = new ArrayList<ArticleBean>();
		this.artList = foundList;
		this.notifyDataSetChanged();
	}

	/**
	 * 
	 */

	public List<ArticleBean> getData() {
		return artList;
	}

	@Override
	public int getCount() {
		return artList == null ? 0 : artList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return artList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(final int arg0, View convertview, ViewGroup arg2) {

		ViewHolder holder = null;
		if (convertview == null) {
			holder = new ViewHolder();
			convertview = inflater.inflate(
					R.layout.item_mypulltorefreshlistview, null);
			holder.imgUrl = (ImageView) convertview.findViewById(R.id.imgUrl);
			holder.tv_contteam = (TextView) convertview
					.findViewById(R.id.tv_contteam);
			holder.my_icon_found = (CircleImageView) convertview
					.findViewById(R.id.my_icon_found);
			holder.cust_grid = (CustomGridView) convertview
					.findViewById(R.id.group_list_grid);
			holder.tv_groupname = (TextView) convertview
					.findViewById(R.id.tv_groupname);
			holder.tvyuedu = (TextView) convertview.findViewById(R.id.tvyuedu);
			holder.tvHotgroup_type = (ImageView) convertview
					.findViewById(R.id.tvHotgroup_type);
			holder.view_color = (View) convertview
					.findViewById(R.id.view_color);
			holder.tvUserName = (TextView) convertview
					.findViewById(R.id.tvUserName);

			holder.btnDelete = (Button) convertview
					.findViewById(R.id.btnDelete);

			LayoutParams lp = (LayoutParams) holder.imgUrl.getLayoutParams();
			lp.width = sreenWidth;
			lp.height = sreenWidth * 1 / 2;
			holder.imgUrl.setLayoutParams(lp);
			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
			clearCache(holder);
		}
		holder.tvUserName.setText(artList.get(arg0).getT_author());
		holder.tv_contteam.setText(artList.get(arg0).getT_message());
		String title = artList.get(arg0).getT_subject();
		if (title.equals("") || title == null) {
			title = "香橙推荐";
		}
		holder.tv_groupname.setText(title);

		holder.tvyuedu.setText(TimeFormatUtil.getTimeBeforeCurrentTime(artList
				.get(arg0).getT_dateline()));
		ImageLoader.getInstance().displayImage(
				artList.get(arg0).getAuthor_profile(),
				artList.get(arg0).getAuthor_profile_key(),
				holder.my_icon_found, MyApplication.headPicOptn);
		if (artList.get(arg0).getIs_zj().equals("1")) {
			holder.tvHotgroup_type.setVisibility(View.VISIBLE);
		} else {
			holder.tvHotgroup_type.setVisibility(View.GONE);
		}

		// // 内容图片是否为NULL
		if (artList.get(arg0).getImages() != null
				&& artList.get(arg0).getImages().size() > 0) {
			if (artList.get(arg0).getImages().size() > 1) {
				holder.cust_grid.setVisibility(View.VISIBLE);
				holder.imgUrl.setVisibility(View.GONE);
				CommunityGridViewAdapter mAdapter = new CommunityGridViewAdapter(
						mContext, sreenWidth);
				holder.cust_grid.setAdapter(mAdapter);
				if (arg0 < 2)
					mAdapter.setDisplayImageOptions(defaultOption);
					mAdapter.setFade(true);
				mAdapter.setData(artList.get(arg0).getImages());
			} else {
				holder.cust_grid.setVisibility(View.GONE);
				holder.imgUrl.setVisibility(View.VISIBLE);
				if (arg0 < 2) {
					ImageLoader.getInstance().displayImage(
							artList.get(arg0).getImages().get(0)
									.getContent_attachment_sl(),
							artList.get(arg0).getImages().get(0)
									.getT_attachment_key_sl(), holder.imgUrl,
							defaultOption);
				} else {
					ImageLoader.getInstance().displayImage(
							artList.get(arg0).getImages().get(0)
									.getContent_attachment_sl(),
							artList.get(arg0).getImages().get(0)
									.getT_attachment_key_sl(), holder.imgUrl,
							MyApplication.defaultOption);
				}
			}

		} else {
			holder.imgUrl.setVisibility(View.GONE);
			holder.cust_grid.setVisibility(View.GONE);
		}
		holder.my_icon_found
				.setOnClickListener(new FragmentPageOnClickListener(arg0));
		holder.imgUrl.setOnClickListener(new FragmentPageOnClickListener(arg0));

		if (mFrom == 0) {
			holder.btnDelete.setVisibility(View.VISIBLE);
		} else {
			holder.btnDelete.setVisibility(View.GONE);
		}
		holder.btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArticleBean articleBean = artList.get(arg0);
				TopicDeleteParamClass mParam = new TopicDeleteParamClass();
				mParam.my_int_id = PreManager.instance().getUserId(mContext);
				mParam.tid = articleBean.getTid();
				new CommunityProcClass(mContext).topicDelete(mParam,
						new InterfaceTopicDeleteCallBack() {

							@Override
							public void onSuccess(int icode, String strSuccMsg) {
								Util.show(mContext, "删除成功");
								notifyDataSetChanged();
								Intent intent = new Intent();
								intent.putExtra("position", arg0);
								intent.setAction("com.yzm.community.TOPIC_DELETE");
								mContext.sendBroadcast(intent);
							}

							@Override
							public void onError(int icode, String strErrMsg) {

							}
						});
			}
		});
		return convertview;
	}

	private class FragmentPageOnClickListener implements OnClickListener {
		int posstion;

		public FragmentPageOnClickListener(int posstion) {
			this.posstion = posstion;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_groupname://
				break;

			case R.id.my_icon_found://
				break;
			case R.id.imgUrl:
				ImageActivity(posstion);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 点击大图
	 * 
	 * @param posstion
	 */
	private void ImageActivity(int posstion) {
		Intent intent = new Intent(mContext, ImageDetailActivity.class);
		intent.putExtra("img_list", (Serializable) artList.get(posstion)
				.getImages());
		intent.putExtra("img_info", 0);
		mContext.startActivity(intent);
	}

	public class ViewHolder {
		ImageView imgUrl;
		TextView tv_contteam;
		CustomGridView cust_grid;
		CircleImageView my_icon_found;
		View view_color;
		TextView tv_groupname;
		TextView tvUserName;
		ImageView tvHotgroup_type;
		TextView tvyuedu;//
		Button btnDelete;
	}

	private void clearCache(ViewHolder holder) {
		holder.imgUrl.setImageResource(0);
		holder.tv_contteam.setText("");
		holder.tv_groupname.setText("");
		holder.cust_grid.setAdapter(null);
		holder.my_icon_found.setImageResource(0);
	}

}
