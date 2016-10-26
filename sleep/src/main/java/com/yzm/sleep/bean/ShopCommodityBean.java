package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShopCommodityBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String shopid = "";//   商品id
	private String sale_price = "";//  （实际售卖价格） 
	private String price = "";// （标价）
	private String title = "";//       商品名字 
	private String salenum = "0";//   售卖数量
	private List<ProductListBean> morepicture = new ArrayList<ProductListBean>();//   产品缩略图，为一个数组：
	private String stock = "0";//    真实库存数量（商品数量-真实的售卖数量）
	private String flag = "";//      是否允许购买（1允许，0不允许），这个根据商品库存；后台设置的购买时间段决定
	private String liaocheng;
	private String kefu;
	
	private boolean isgoods = true;
	private boolean iszj = false;
	private boolean isyg = false;
	private String ygid;   
	private String ygname;
	private String ygaddress;
	private String zjuid;
	private String zjname;
	private String zjaddress;
	
	public String getKefu() {
		return kefu;
	}
	public void setKefu(String kefu) {
		this.kefu = kefu;
	}
	private int buyNum; //购买数量 应用内使用
	
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	
	public String getSale_price() {
		return sale_price;
	}
	
	public String getLiaocheng() {
		return liaocheng;
	}
	public void setLiaocheng(String liaocheng) {
		this.liaocheng = liaocheng;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSalenum() {
		return salenum;
	}
	public void setSalenum(String salenum) {
		this.salenum = salenum;
	}
	
	public List<ProductListBean> getMorepicture() {
		return morepicture;
	}
	public void setMorepicture(List<ProductListBean> morepicture) {
		this.morepicture = morepicture;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public boolean isIsgoods() {
		return isgoods;
	}
	public void setIsgoods(boolean isgoods) {
		this.isgoods = isgoods;
	}
	public String getYgid() {
		return ygid;
	}
	public void setYgid(String ygid) {
		this.ygid = ygid;
	}
	public String getYgname() {
		return ygname;
	}
	public void setYgname(String ygname) {
		this.ygname = ygname;
	}
	public String getYgaddress() {
		return ygaddress;
	}
	public void setYgaddress(String ygaddress) {
		this.ygaddress = ygaddress;
	}
	public String getZjuid() {
		return zjuid;
	}
	public void setZjuid(String zjuid) {
		this.zjuid = zjuid;
	}
	public String getZjname() {
		return zjname;
	}
	public void setZjname(String zjname) {
		this.zjname = zjname;
	}
	public String getZjaddress() {
		return zjaddress;
	}
	public void setZjaddress(String zjaddress) {
		this.zjaddress = zjaddress;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public boolean isIszj() {
		return iszj;
	}
	public void setIszj(boolean iszj) {
		this.iszj = iszj;
	}
	public boolean isIsyg() {
		return isyg;
	}
	public void setIsyg(boolean isyg) {
		this.isyg = isyg;
	}
	
	
}
