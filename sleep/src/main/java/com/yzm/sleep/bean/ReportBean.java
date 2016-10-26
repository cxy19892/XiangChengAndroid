package com.yzm.sleep.bean;

import java.io.Serializable;
/**
 * 举报列表接口  举报信息类
 * @author Administrator
 *
 */
public class ReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String rid;//        举报id
	private String uid;//         举报人uid
	private String username;//   举报人名字
	private String tid;//        被举报的话题id
	private String touid;//        被举报人id
	private String tousername;//   被举报人名字
	private String message;//       被举报的话题内容
	private String dateline ;//      举报时间（格式为：8分钟前）
	private String report;//      举报内容
	
	
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTouid() {
		return touid;
	}
	public void setTouid(String touid) {
		this.touid = touid;
	}
	public String getTousername() {
		return tousername;
	}
	public void setTousername(String tousername) {
		this.tousername = tousername;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	
	
	

}
