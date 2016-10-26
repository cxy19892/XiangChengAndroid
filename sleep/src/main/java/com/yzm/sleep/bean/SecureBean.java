package com.yzm.sleep.bean;

import java.io.Serializable;

public class SecureBean implements Serializable{
	
	private String kefuuid;// 客服UID     
	private String uid;//  报名用户UID      
	private String safenums;//  为保单号    
	private String name;//   姓名   
	private String age;// 年龄    
	private String sex;//  性别 （01为男；02为女 ）     
	private String phone;//  电话     
	private String occu;// 职业      
	private String zhuangtai;//    状态   
	private String xinli;//     心理
	
	public String getKefuuid() {
		return kefuuid;
	}
	public void setKefuuid(String kefuuid) {
		this.kefuuid = kefuuid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getSafenums() {
		return safenums;
	}
	public void setSafenums(String safenums) {
		this.safenums = safenums;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getZhuangtai() {
		return zhuangtai;
	}
	public void setZhuangtai(String zhuangtai) {
		this.zhuangtai = zhuangtai;
	}
	public String getXinli() {
		return xinli;
	}
	public void setXinli(String xinli) {
		this.xinli = xinli;
	}
	public String getOccu() {
		return occu;
	}
	public void setOccu(String occu) {
		this.occu = occu;
	}
	public String getInfoString() {
		StringBuffer sb = new StringBuffer();
		sb.append("保单号:");
		sb.append(safenums);
		sb.append("\n");
		sb.append("姓名 :");
		sb.append(name);
		sb.append("\n");
		sb.append("年龄:");
		sb.append(age);
		sb.append("\n");
		sb.append("性别:");
		sb.append(sex.equals("01")?"男":"女");
		sb.append("\n");
		sb.append("电话:");
		sb.append(phone);
		sb.append("\n");
		sb.append("职业:");
		sb.append(occu);
		sb.append("\n");
		sb.append("状态:");
		sb.append(zhuangtai);
		sb.append("\n");
		sb.append("心理:");
		sb.append(xinli);
		sb.append("\n");
		
//		JSONObject infojson = new JSONObject();
//		try {
//			infojson.put("uid", uid);
//			infojson.put("safenums", safenums);
//			infojson.put("name", name);
//			infojson.put("age", age);
//			infojson.put("sex", sex);
//			infojson.put("phone", phone);
//			infojson.put("occu", occu);
//			infojson.put("zhuangtai", zhuangtai);
//			infojson.put("xinli", xinli);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return sb.toString();
	}
	
	
}
