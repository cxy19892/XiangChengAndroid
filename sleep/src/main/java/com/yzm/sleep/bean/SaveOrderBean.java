package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveOrderBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String realname = "";//    收货人
	private String phone = "";//     收货人手机
	private String address = "";//      收货地址
	private String out_trade_no = "";//    订单号
	private String shopid = "";//        商品id
	private List<ProductListBean> morepicture = new ArrayList<ProductListBean>();//  产品缩略图，为一个数组：
	private String title = "";//         商品名字 
	private String buy_num = "0";//       (购买数量)
	private String price = "";//          商品金额
	private String youhui_price = "0";//    (优惠金额) 
	private String count_price = "0";//    （实付款）
	
	private String kefu;
	
	public String getKefu() {
		return kefu;
	}
	public void setKefu(String kefu) {
		this.kefu = kefu;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getYouhui_price() {
		return youhui_price;
	}
	public void setYouhui_price(String youhui_price) {
		this.youhui_price = youhui_price;
	}
	public String getCount_price() {
		return count_price;
	}
	public void setCount_price(String count_price) {
		this.count_price = count_price;
	}
	
	

}
