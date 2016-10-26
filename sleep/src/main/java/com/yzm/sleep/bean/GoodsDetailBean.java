package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class GoodsDetailBean implements Serializable {
	
	private String tcid;
	private String tcsalenum;
	private String picture;
	private String picture_key;
	private String tc_title;
	private String ygid;
	private String ygname;
	private String ygaddress;
	private String zjuid;
	private String zjname;
	private String zjaddress;
	
	private String kfuid;
	private String name ;
	private String tx;
	private String tx_key;
	
	private List<ShopCommodityBean> shop_list;
	
	public String getTcid() {
		return tcid;
	}
	public void setTcid(String tcid) {
		this.tcid = tcid;
	}
	public String getTcsalenum() {
		return tcsalenum;
	}
	public void setTcsalenum(String tcsalenum) {
		this.tcsalenum = tcsalenum;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPicture_key() {
		return picture_key;
	}
	public void setPicture_key(String picture_key) {
		this.picture_key = picture_key;
	}
	public String getTc_title() {
		return tc_title;
	}
	public void setTc_title(String tc_title) {
		this.tc_title = tc_title;
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
	public List<ShopCommodityBean> getShop_list() {
		return shop_list;
	}
	public void setShop_list(List<ShopCommodityBean> shop_list) {
		this.shop_list = shop_list;
	}
	public String getKfuid() {
		return kfuid;
	}
	public void setKfuid(String kfuid) {
		this.kfuid = kfuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTx() {
		return tx;
	}
	public void setTx(String tx) {
		this.tx = tx;
	}
	public String getTx_key() {
		return tx_key;
	}
	public void setTx_key(String tx_key) {
		this.tx_key = tx_key;
	}
	
	
}
