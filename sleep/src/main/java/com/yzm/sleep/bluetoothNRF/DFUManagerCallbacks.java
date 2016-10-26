package com.yzm.sleep.bluetoothNRF;

public interface DFUManagerCallbacks {

	public void onDeviceConnected();

	public void onDFUServiceFound();
	//add by chenbo
	public void onDFUServiceNotFound();

	public void onDeviceDisconnected();

	public void onFileTransferStarted();

	public void onFileTranfering(long sizeTransfered);

	public void onFileTransferCompleted();

	public void onFileTransferValidation();

	public void onError(String message, int errorCode);

}
