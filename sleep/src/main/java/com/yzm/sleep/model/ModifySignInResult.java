package com.yzm.sleep.model;

import java.io.Serializable;

import org.json.JSONObject;

public class ModifySignInResult implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3766185974676084047L;

	private String message;
	private String response;
	private int qiandao;
	private int pinggu;
	private int changesleep;
	private String sleep_time;
	private String wakeup_time;
	
	private Report report_data;
	
	public class Report{
		private String xiaolv;//        睡眠效率     
		private String sleep;//        (入睡时间)    
		private String wakeup;//     （起床时间）    
		private int sleeplong;//   （睡眠时长）
		private int bedlong;//     （在床时长，题8-题1）
		private int report_ok;//   是否弹出反馈（1弹出；0不弹出）
		
		public int getSleeplong() {
			return sleeplong;
		}
		public void setSleeplong(int sleeplong) {
			this.sleeplong = sleeplong;
		}
		
		public String getWakeup() {
			return wakeup;
		}
		public void setWakeup(String wakeup) {
			this.wakeup = wakeup;
		}
		public String getXiaolv() {
			return xiaolv;
		}
		public void setXiaolv(String xiaolv) {
			this.xiaolv = xiaolv;
		}
		public String getSleep() {
			return sleep;
		}
		public void setSleep(String sleep) {
			this.sleep = sleep;
		}
		public int getReport_ok() {
			return report_ok;
		}
		public void setReport_ok(int report_ok) {
			this.report_ok = report_ok;
		}
		public int getBedlong() {
			return bedlong;
		}
		public void setBedlong(int bedlong) {
			this.bedlong = bedlong;
		}
		
		
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getQiandao() {
		return qiandao;
	}

	public void setQiandao(int qiandao) {
		this.qiandao = qiandao;
	}

	public int getPinggu() {
		return pinggu;
	}

	public void setPinggu(int pinggu) {
		this.pinggu = pinggu;
	}

	public Report getReport_data() {
		return report_data;
	}

	public void setReport_data(Report report_data) {
		this.report_data = report_data;
	}


	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("message", message);
			jsonObject.put("response", response);
			jsonObject.put("qiandao", qiandao);
			jsonObject.put("pinggu", pinggu);
			jsonObject.put("changesleep", changesleep);
			jsonObject.put("sleep_time", sleep_time);
			jsonObject.put("wakeup_time", wakeup_time);
			JSONObject jsonReport = new JSONObject();
			if(report_data != null){
				jsonReport.put("sleeplong", report_data.getSleeplong());
				jsonReport.put("wakeup", report_data.getWakeup());
				jsonReport.put("bedlong", report_data.getBedlong());
				jsonReport.put("xiaolv", report_data.getXiaolv());
				jsonReport.put("sleep", report_data.getSleep());
			}
			jsonObject.put("report_data", jsonReport);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return jsonObject.toString();
	}

	public int getChangesleep() {
		return changesleep;
	}

	public void setChangesleep(int changesleep) {
		this.changesleep = changesleep;
	}

	public String getSleep_time() {
		return sleep_time;
	}

	public void setSleep_time(String sleep_time) {
		this.sleep_time = sleep_time;
	}

	public String getWakeup_time() {
		return wakeup_time;
	}

	public void setWakeup_time(String wakeup_time) {
		this.wakeup_time = wakeup_time;
	}

}
