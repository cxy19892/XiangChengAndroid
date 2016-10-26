package com.yzm.sleep.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.activity.shop.PackageDetailsActivity;
import com.yzm.sleep.bean.ShopCommodityBean;
import com.yzm.sleep.widget.CustomGridView;

/**
 * 套餐列表
 * @author tianxun
 */
public class GoodsComboAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
    private List<ShopCommodityBean> listData;
    private IntClickBuyListenter buyListenter;
    private Context context;
    
    public interface IntClickBuyListenter{
    	public void onClickBuy(ShopCommodityBean bean );
    	public void onClickAsk(ShopCommodityBean bean );
    }
    
    public GoodsComboAdapter(Context context, int screenWidht,IntClickBuyListenter buyListenter){
    	this.mInflater=LayoutInflater.from(context);
    	this.buyListenter=buyListenter;
    	this.context = context ;
    }
    
    public void setData(List<ShopCommodityBean> listData){
    	this.listData = listData;
    	this.notifyDataSetChanged();
    }
	
	@Override
	public int getCount() {
		return listData == null ? 0 : listData.size();
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
		ViewHolder holder=null;
		final ShopCommodityBean bean = listData.get(position);
		if(convertView != null && convertView.getTag() != null){
			holder=(ViewHolder) convertView.getTag();
			clearCache(holder);
		}else{
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_goodscombo_list, null);
			holder.goodsTitle = (TextView) convertView.findViewById(R.id.goods_title);
			holder.goodsPrice = (TextView) convertView.findViewById(R.id.goods_pice);
			holder.cvComboList = (CustomGridView) convertView.findViewById(R.id.cv_combo_list);
			holder.btnBuy = (Button) convertView.findViewById(R.id.btn_buy);
			holder.btnAsk = (Button) convertView.findViewById(R.id.btn_ask);
			holder.goodsRaL = (RelativeLayout) convertView.findViewById(R.id.goods_rel);
			holder.zjRaL = (RelativeLayout) convertView.findViewById(R.id.zj_rel);
			holder.ygRaL = (RelativeLayout) convertView.findViewById(R.id.yg_rel);
			holder.ygAddr = (TextView) convertView.findViewById(R.id.tv_ygaddr);
			holder.ygName = (TextView) convertView.findViewById(R.id.tv_ygname);
			holder.zjName = (TextView) convertView.findViewById(R.id.tv_zjname);
			holder.zjAddr = (TextView) convertView.findViewById(R.id.tv_zjaddr);
			convertView.setTag(holder);
		}
		
		if(bean.isIsgoods()){
			holder.goodsRaL.setVisibility(View.VISIBLE);
			holder.zjRaL.setVisibility(View.GONE);
			holder.ygRaL.setVisibility(View.GONE);
			if(bean.getMorepicture() != null){
				holder.cvComboList.setVisibility(View.VISIBLE);
				holder.cvComboList.setAdapter(new ComboAdapter(context, bean.getMorepicture()));
				holder.cvComboList.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP){
							context.startActivity(new Intent(context, PackageDetailsActivity.class)
							.putExtra("shopid", bean.getShopid())
							.putExtra("flag", bean.getFlag()));
						}
						return true;
					}
				});
			}else
				holder.cvComboList.setVisibility(View.GONE);

			holder.goodsTitle.setText(bean.getTitle());
			holder.goodsPrice.setText(bean.getPrice());

			if("1".equals(bean.getFlag())){
				holder.btnBuy.setEnabled(true);
				holder.btnBuy.setText("立即购买");
				holder.btnBuy.setBackgroundResource(R.drawable.custom_button_bg);
			}else{
				holder.btnBuy.setEnabled(false);
				holder.btnBuy.setText("售罄");
				holder.btnBuy.setBackgroundResource(R.drawable.custom_bgbtnborder);
			}

			holder.btnBuy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if(buyListenter != null){
						buyListenter.onClickBuy(bean);
					}
				}
			});

			holder.btnAsk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(buyListenter != null){
						buyListenter.onClickAsk(bean);
					}

				}
			});
		}
		if(bean.isIsyg()){
			holder.goodsRaL.setVisibility(View.GONE);
			holder.zjRaL.setVisibility(View.GONE);
			holder.ygRaL.setVisibility(View.VISIBLE);
			holder.ygName.setText(bean.getYgname());
			holder.ygAddr.setText(bean.getYgaddress());
		}
		if(bean.isIszj()){
			holder.goodsRaL.setVisibility(View.GONE);
			holder.zjRaL.setVisibility(View.VISIBLE);
			holder.ygRaL.setVisibility(View.GONE);
			holder.zjName.setText(bean.getZjname());
			holder.zjAddr.setText(bean.getZjaddress());
		}
		
		
		return convertView;
	}
	
	private void clearCache(ViewHolder holder){
		holder.cvComboList.setAdapter(null);
		holder.goodsPrice.setText("");
		holder.goodsTitle.setText("");
	}

	class ViewHolder{
		TextView goodsTitle, goodsPrice;
		Button btnBuy;
		Button btnAsk;
		CustomGridView cvComboList;
		RelativeLayout goodsRaL;
		RelativeLayout zjRaL;
		RelativeLayout ygRaL;
		TextView zjName, zjAddr, ygName, ygAddr;
	}
}
