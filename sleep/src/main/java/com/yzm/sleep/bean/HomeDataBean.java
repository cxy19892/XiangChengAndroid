package com.yzm.sleep.bean;

import java.io.Serializable;
import java.util.List;

public class HomeDataBean implements Serializable {
	private List<PlanListBean> plan_list;//:
	private String nums;// 参与人数nums
	private List<UserBean>	user_list;//:最近参与人信息
	private SleepCaseBean plan;
	private int zongjie_flag;// 是否显示一周总结zongjie_flag（1显示；0不显示）
	private int plan_flag;// 是否设置改善睡眠计划（1已经设置；0没有设置）
	private int data_flag;// 是否需要补充睡眠数据（1需要；0不需要）
	private int feel_flag;// 是否需要补充睡眠总体感觉（1需要；0不需要）
	private int env_flag;// 是否需要补充哪些环境对睡眠影响（1需要；0不需要）
	private int smoke_flag;// 是否需要补充烟（1需要；0不需要）
	private int win_flag;// 是否需要补充酒（1需要；0不需要）
	private int coffo_flag;// 是否需要补充咖啡（1需要；0不需要）
	private int weight_flag;// 是否需要补充减肥（1需要；0不需要）
	private int index_flag; //是否需要跳转到首页补全数据（1需要；0不需要）
	private int sport_flag;
	private int report_before;
	private String sleep;//     睡觉时间     格式：23:30
	private String wakeup;//   起床时间    格式：08:30
	
	public int getIndex_flag() {
		return index_flag;
	}
	public void setIndex_flag(int index_flag) {
		this.index_flag = index_flag;
	}
	public int getPlan_flag() {
		return plan_flag;
	}
	public void setPlan_flag(int plan_flag) {
		this.plan_flag = plan_flag;
	}
	public int getData_flag() {
		return data_flag;
	}
	public void setData_flag(int data_flag) {
		this.data_flag = data_flag;
	}
	public int getFeel_flag() {
		return feel_flag;
	}
	public void setFeel_flag(int feel_flag) {
		this.feel_flag = feel_flag;
	}
	public int getEnv_flag() {
		return env_flag;
	}
	public void setEnv_flag(int env_flag) {
		this.env_flag = env_flag;
	}
	public int getSmoke_flag() {
		return smoke_flag;
	}
	public void setSmoke_flag(int smoke_flag) {
		this.smoke_flag = smoke_flag;
	}
	public int getWin_flag() {
		return win_flag;
	}
	public void setWin_flag(int win_flag) {
		this.win_flag = win_flag;
	}
	public int getCoffo_flag() {
		return coffo_flag;
	}
	public void setCoffo_flag(int coffo_flag) {
		this.coffo_flag = coffo_flag;
	}
	public int getWeight_flag() {
		return weight_flag;
	}
	public void setWeight_flag(int weight_flag) {
		this.weight_flag = weight_flag;
	}
	public List<PlanListBean> getPlan_list() {
		return plan_list;
	}
	public void setPlan_list(List<PlanListBean> plan_list) {
		this.plan_list = plan_list;
	}
	public String getNums() {
		return nums;
	}
	public void setNums(String nums) {
		this.nums = nums;
	}
	public List<UserBean> getUser_list() {
		return user_list;
	}
	public void setUser_list(List<UserBean> user_list) {
		this.user_list = user_list;
	}
	public int getZongjie_flag() {
		return zongjie_flag;
	}
	public void setZongjie_flag(int zongjie_flag) {
		this.zongjie_flag = zongjie_flag;
	}
	public SleepCaseBean getPlan() {
		return plan;
	}
	public void setPlan(SleepCaseBean plan) {
		this.plan = plan;
	}
	public int getSport_flag() {
		return sport_flag;
	}
	public void setSport_flag(int sport_flag) {
		this.sport_flag = sport_flag;
	}
	public int getReport_before() {
		return report_before;
	}
	public void setReport_before(int report_before) {
		this.report_before = report_before;
	}


	public String getWakeup() {
		return wakeup;
	}

	public void setWakeup(String wakeup) {
		this.wakeup = wakeup;
	}

	public String getSleep() {
		return sleep;
	}

	public void setSleep(String sleep) {
		this.sleep = sleep;
	}
}
