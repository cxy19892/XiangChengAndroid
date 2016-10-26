package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShopOrderBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String shopid = "";//       商品id
	private List<ProductListBean> morepicture = new ArrayList<ProductListBean>();//   产品缩略图，为一个数组：
	private String title = "";//            商品名字 
	private String out_trade_no = "";//        (订单号)
	private String buy_num = "";//        (购买数量)
	private String count_price = "";//     （实际付款，已付款） 
	private String youhui_price = "";//     (优惠金额)
	private String kefu="";
	private String flag;
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	
	public List<ProductListBean> getMorepicture() {
		return morepicture;
	}
	public void setMorepicture(List<ProductListBean> morepicture) {
		this.morepicture = morepicture;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getBuy_num() {
		return buy_num;
	}
	public void setBuy_num(String buy_num) {
		this.buy_num = buy_num;
	}
	public String getCount_price() {
		return count_price;
	}
	public void setCount_price(String count_price) {
		this.count_price = count_price;
	}
	public String getYouhui_price() {
		return youhui_price;
	}
	public void setYouhui_price(String youhui_price) {
		this.youhui_price = youhui_price;
	}
	public String getKefu() {
		return kefu;
	}
	public void setKefu(String kefu) {
		this.kefu = kefu;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
