package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommodityDetailBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String shopid = "";//   商品id
	private String 	sale_price = "";//  （实际售卖价格） 
	private String price = "";//       （标价）
	private List<ProductListBean> morepicture = new ArrayList<ProductListBean>();//   产品缩略图，为一个数组：
	private String title = "";//      商品名字 
	private String intro = "";//     商品介绍 (富文本)
	private String salenum = "";//   售卖数量
	private String flag = "";//       是否允许购买（1允许，0不允许），这个根据商品库存；后台设置的购买时间段决定
	private String isaddinfo = "";//   是否需要完善收货信息（1需要；0不需要）
	private String liaocheng = "";//    疗程数量（允许购买的最大值）
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getSale_price() {
		return sale_price;
	}
	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getSalenum() {
		return salenum;
	}
	public void setSalenum(String salenum) {
		this.salenum = salenum;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getIsaddinfo() {
		return isaddinfo;
	}
	public void setIsaddinfo(String isaddinfo) {
		this.isaddinfo = isaddinfo;
	}
	public String getLiaocheng() {
		return liaocheng;
	}
	public void setLiaocheng(String liaocheng) {
		this.liaocheng = liaocheng;
	}
	
	

}
