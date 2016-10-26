package com.yzm.sleep.bean;

import java.io.Serializable;

public class ClassifyTagBean implements Serializable {
	private String term_id; 
	private String term_name;
	
	public String getTerm_id() {
		return term_id;
	}
	public void setTerm_id(String term_id) {
		this.term_id = term_id;
	}
	public String getTerm_name() {
		return term_name;
	}
	public void setTerm_name(String term_name) {
		this.term_name = term_name;
	} 
	
	
}
