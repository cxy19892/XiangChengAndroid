package com.yzm.sleep.bluetoothNRF;

public interface DFUCallBack {
	public static final int DFU_DEV_NOT_FUND_OR_FUNDTWO = -1;
	public static final int DFU_DEV_TRANSFER_BEINTERRUPT= -2;
	public static final int DFU_DEV_TRANSFER_NOT_SUCCESS= -3;
	public static final int DFU_DEV_NOT_SUPPORT_UPLOAD  = -4;
	public static final int DFU_DEV_TRANSFER_SUCCESSED  = 10;
	public static final int DFU_DEV_TRANSFER_STARTING   = 11;
	public static final int DFU_DEV_TRANSFER_PRICENTAGE = 12;
	public static final int DFU_DEV_TRANSFER_COMPLETED  = 13;
	
	public void DFUuploadcallback(int dfucode, String dfumsg); 

}
