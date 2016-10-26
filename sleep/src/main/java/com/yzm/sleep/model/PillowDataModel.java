package com.yzm.sleep.model;

import java.io.Serializable;

public class PillowDataModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8946988623320446104L;

	private String date;
	private String isUpload;
	private String fileName;
	private String datFileName;
	private String datIsUpload;
	private byte[] bfr;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getIsUpload() {
		return isUpload;
	}
	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getBfr() {
		return bfr;
	}
	public void setBfr(byte[] bfr) {
		this.bfr = bfr;
	}
	public String getDatFileName() {
		return datFileName;
	}
	public void setDatFileName(String datFileName) {
		this.datFileName = datFileName;
	}
	public String getDatIsUpload() {
		return datIsUpload;
	}
	public void setDatIsUpload(String datIsUpload) {
		this.datIsUpload = datIsUpload;
	}
	
	
}
