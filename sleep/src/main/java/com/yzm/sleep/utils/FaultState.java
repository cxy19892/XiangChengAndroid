package com.yzm.sleep.utils;

public enum FaultState {
	A("0"),
	B("1"),
	C("2-1"),
	D("2-2"),
	E("3-1"),
	F("3-2"),
	G("3-3"),
	H("4-1"),
	I("4-2"),
//	J("4-2-2"),
	K("4-3"),
	L("4-4"),
	M("4-5"),
	N("4-6"),
	O("5-1"),
	P("5-2"),
	Q("5-3"),
	R("5-4"),
	S("5-5"),
	T("5-6");
	
	private String state;
	private FaultState(String state){
		this.state = state;
	}
	
	public String getState(){
		return state;
	}
}
