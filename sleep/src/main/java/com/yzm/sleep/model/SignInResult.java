package com.yzm.sleep.model;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignInResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7304741291753296414L;

	private String message;
	private String response;
	private int qiandao;
	private int pinggu;
	private String emotion;
	private String xinqing;
	
	private Report report_data;
	
	private List<Plan> plan_list;
	
	public class Plan{
		private String planid;
		private int urltype;
		private String isshow;
		private String type;
		private String isfinish;
		private String urlid;
		private String name;
		
		public String getPlanid() {
			return planid;
		}
		public void setPlanid(String planid) {
			this.planid = planid;
		}
		public int getUrltype() {
			return urltype;
		}
		public void setUrltype(int urltype) {
			this.urltype = urltype;
		}
		public String getIsshow() {
			return isshow;
		}
		public void setIsshow(String isshow) {
			this.isshow = isshow;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getIsfinish() {
			return isfinish;
		}
		public void setIsfinish(String isfinish) {
			this.isfinish = isfinish;
		}
		public String getUrlid() {
			return urlid;
		}
		public void setUrlid(String urlid) {
			this.urlid = urlid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		
	}
	
	public class Report{
		private String sleeplong;
		private int days;
		private String qiandaoid;
		private String feeling;
		private String wakeup;
		private String report;
		private String xiaolv;
		private String sleep;
		private int report_ok;
		
		public String getSleeplong() {
			return sleeplong;
		}
		public void setSleeplong(String sleeplong) {
			this.sleeplong = sleeplong;
		}
		public int getDays() {
			return days;
		}
		public void setDays(int days) {
			this.days = days;
		}
		public String getQiandaoid() {
			return qiandaoid;
		}
		public void setQiandaoid(String qiandaoid) {
			this.qiandaoid = qiandaoid;
		}
		public String getFeeling() {
			return feeling;
		}
		public void setFeeling(String feeling) {
			this.feeling = feeling;
		}
		public String getWakeup() {
			return wakeup;
		}
		public void setWakeup(String wakeup) {
			this.wakeup = wakeup;
		}
		public String getReport() {
			return report;
		}
		public void setReport(String report) {
			this.report = report;
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

	public List<Plan> getPlan_list() {
		return plan_list;
	}

	public void setPlan_list(List<Plan> plan_list) {
		this.plan_list = plan_list;
	}

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("message", message);
			jsonObject.put("response", response);
			JSONArray jsonAry = new JSONArray();
			if(plan_list != null){
				for (Plan planObj : plan_list) {
					JSONObject jsonObjItem = new JSONObject();
					if(planObj != null){
						jsonObjItem.put("planid", planObj.getPlanid());
						jsonObjItem.put("urltype", planObj.getUrltype());
						jsonObjItem.put("isshow", planObj.getIsshow());
						jsonObjItem.put("type", planObj.getType());
						jsonObjItem.put("isfinish", planObj.getIsfinish());
						jsonObjItem.put("urlid", planObj.getUrlid());
						jsonObjItem.put("name", planObj.getName());
					}
					jsonAry.put(jsonObjItem);
				}
			}
			jsonObject.put("plan_list", jsonAry);
			jsonObject.put("qiandao", qiandao);
			jsonObject.put("pinggu", pinggu);
			JSONObject jsonReport = new JSONObject();
			if(report_data != null){
				jsonReport.put("sleeplong", report_data.getSleeplong());
				jsonReport.put("days", report_data.getDays());
				jsonReport.put("qiandaoid", report_data.getQiandaoid());
				jsonReport.put("feeling", report_data.getFeeling());
				jsonReport.put("wakeup", report_data.getWakeup());
				jsonReport.put("report", report_data.getReport());
				jsonReport.put("xiaolv", report_data.getXiaolv());
				jsonReport.put("sleep", report_data.getSleep());
			}
			jsonObject.put("report_data", jsonReport);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return jsonObject.toString();
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public String getXinqing() {
		return xinqing;
	}

	public void setXinqing(String xinqing) {
		this.xinqing = xinqing;
	}
	
	
}
