package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.activity.shop.PackageDetailsActivity;
import com.yzm.sleep.bean.ShopOrderBean;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.widget.CustomGridView;

public class OrderListAdapter extends BaseAdapter {

	private List<ShopOrderBean> mlist;
	private Context mcontext;
	private LayoutInflater inflater;
	private int Screenwidth;
	public OrderListAdapter(Context mcontext, int Screenwidth){
		this.mcontext = mcontext;
		inflater = LayoutInflater.from(mcontext);
		this.Screenwidth = Screenwidth;
	}
	
	public synchronized void SetDate(List<ShopOrderBean> list){
		this.mlist = list;
		notifyDataSetChanged();
	}
	
	public List<ShopOrderBean> getData(){
		return mlist;
	}
	
	
	@Override
	public int getCount() {
		if(mlist != null){
			return mlist.size();
		}else
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		ShopOrderBean mShopOrderBean = mlist.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_order_list, null);
			holder.picListv = (LinearLayout) convertView.findViewById(R.id.item_order_lin);
			holder.orderId = (TextView) convertView.findViewById(R.id.order_id);
			holder.countPrice = (TextView) convertView.findViewById(R.id.item_order_price);
			holder.oderState = (TextView) convertView.findViewById(R.id.item_order_state);
			holder.buyNum = (TextView) convertView.findViewById(R.id.order_num);
			holder.picBottomline = convertView.findViewById(R.id.pic_bottom_line);
			
			holder.cvComboList = (CustomGridView) convertView.findViewById(R.id.item_order_grid);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.countPrice.setText(StringUtil.getPrice(mlist.get(position).getCount_price()));
		holder.orderId.setText(mlist.get(position).getOut_trade_no());
		holder.oderState.setText("已付款");
		holder.buyNum.setText("x "+mlist.get(position).getBuy_num());
//		OrderListImageAdapter picAdapter = new OrderListImageAdapter(mcontext,Screenwidth);
//		holder.picListv.setAdapter(picAdapter);
//		picAdapter.SetDate(mShopOrderBean.getMorepicture());
		if(mlist.get(position).getMorepicture() == null || mlist.get(position).getMorepicture().size()==0){
			holder.picListv.setVisibility(View.GONE);
			holder.picBottomline.setVisibility(View.GONE);
		}else{
			holder.picListv.setVisibility(View.VISIBLE);
			holder.picBottomline.setVisibility(View.VISIBLE);
			holder.cvComboList.setFocusable(false);
			holder.cvComboList.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_UP){
						if(mlist.get(position).getFlag().equals("1")){
							Intent intent2Detail = new Intent(mcontext, PackageDetailsActivity.class);
							intent2Detail.putExtra("shopid", mlist.get(position).getShopid());
							intent2Detail.putExtra("flag", mlist.get(position).getFlag());
							mcontext.startActivity(intent2Detail);
						}else{
							Util.show(mcontext, "该商品已下架");
						}
					}
					return true;
				}
			});
			holder.cvComboList.setAdapter(new ComboAdapter(mcontext, mlist.get(position).getMorepicture()));
		}
//		addViews(holder.picListv, mShopOrderBean.getMorepicture());
		return convertView;
	}
	
	
//	private void addViews(LinearLayout linview, List<ProductListBean> pic_list){
//		LayoutInflater inflater = LayoutInflater.from(mcontext);
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((Screenwidth-Util.Dp2Px(mcontext, 50))/3,LayoutParams.WRAP_CONTENT);
//		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((Screenwidth-Util.Dp2Px(mcontext, 50))/3,(Screenwidth-Util.Dp2Px(mcontext, 50))/3);
//		lp.leftMargin = Util.Dp2Px(mcontext, 8);
//		lp.rightMargin = Util.Dp2Px(mcontext, 8);
//		linview.removeAllViews();
//		ImageView molderImg;
//		TextView name;
//		TextView number;
////		CircleImageView headimg;
//		for (int i = 0; i < (pic_list.size() >3 ? 3 : pic_list.size()); i++) {
//			ProductListBean mItemBean = pic_list.get(i);
//			View view  = inflater.inflate(R.layout.item_order_image_list, null);
//			molderImg = (ImageView) view.findViewById(R.id.item_pic_img);
//			name  = (TextView) view.findViewById(R.id.pic_title);
//			number= (TextView) view.findViewById(R.id.pic_num);
//			molderImg.setLayoutParams(lp2);
//			view.setLayoutParams(lp);
//			ImageLoader.getInstance().displayImage(mItemBean.getImg(), mItemBean.getImg_key(), molderImg, MyApplication.choicePicOptn);
//			name.setText(mItemBean.getImg_title());
//			number.setText("x"+mItemBean.getImg_num());
//			linview.addView(view);
//		}
//	}
	
	class ViewHolder{
		LinearLayout picListv;
		TextView orderId;
		TextView countPrice;
		TextView oderState;
		TextView buyNum;
		View picBottomline;
		GridView cvComboList;
	}
}
