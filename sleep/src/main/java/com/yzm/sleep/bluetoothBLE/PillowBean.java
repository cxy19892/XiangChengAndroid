package com.yzm.sleep.bluetoothBLE;

public class PillowBean {
	
	private String UserId;
	private String PillowMac;
	private String PowerValue;
	private String HardVersion;
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getPillowMac() {
		return PillowMac;
	}
	public void setPillowMac(String pillowMac) {
		PillowMac = pillowMac;
	}
	public String getPowerValue() {
		return PowerValue;
	}
	public void setPowerValue(String powerValue) {
		PowerValue = powerValue;
	}
	public String getHardVersion() {
		return HardVersion;
	}
	public void setHardVersion(String hardVersion) {
		HardVersion = hardVersion;
	}
}
