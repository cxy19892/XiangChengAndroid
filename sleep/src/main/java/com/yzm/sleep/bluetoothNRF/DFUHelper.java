package com.yzm.sleep.bluetoothNRF;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.yzm.sleep.utils.FileUtil;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

@SuppressLint("NewApi") public class DFUHelper {
	
//	private Handler mHandler;
	private Context context;
	private DFUCallBack dfucallback;
	private String TAG = "DFUHelper";
	private ArrayList<ExtendedBluetoothDevice> mBluetoothDevices = new ArrayList<ExtendedBluetoothDevice>();
	private BluetoothAdapter mBluetoothAdapter;
	private ListIterator<ExtendedBluetoothDevice> mIterator;
	private final long SCAN_DURATION = 10000;
	private boolean mIsScanning = false;
	private DFUServiceParser mDFUServiceParser;
	private String mac = "";
	private DFUManager mDFUManager;
	private boolean isDFUServiceFound = false;
	private boolean isFileValidated = false;
	private boolean isDeviceConnected = false;
	public  final int NO_TRANSFER = 0;
	public  final int START_TRANSFER = 1;
	public  final int FINISHED_TRANSFER = 2;
	private int mFileTransferStatus = NO_TRANSFER;
	/**
	 * 上传进度
	 */
	private int mPercentage = 0;
	
	public DFUHelper(Handler handler, Context context, DFUCallBack dfucallback){
//		this.mHandler = handler;
		this.context = context;
		this.dfucallback = dfucallback;
		initializeDFUManager();
	}

	public void StartconnectDFU(BluetoothAdapter blutoothadapter,String MAC){
		this.mBluetoothAdapter = blutoothadapter;
		Log.i(TAG, "mIsScanning="+mIsScanning);
		if(!mIsScanning){
			mac = MAC;
			startScan();
		}
	}
	
	
	
	
	private void startScan() {
		Log.i(TAG, "startScan");
		DFUManager.log(TAG, "startScan");
		mBluetoothDevices.clear();
		mBluetoothAdapter.startLeScan(mLEScanCallback);
		mIsScanning = true;
		mhandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (mIsScanning) {
					stopScan();
					System.out.println("stopScan + mBluetoothDevices.size()="+mBluetoothDevices.size());
					if(mBluetoothDevices.size()==0){
						dfucallback.DFUuploadcallback(DFUCallBack.DFU_DEV_NOT_FUND_OR_FUNDTWO, "1not fund the dfu device");
						DFUManager.log(TAG, "没找到设备1");
					}/*else if(mBluetoothDevices.size() >1 ){
						dfucallback.DFUuploadcallback(-1, "2more than one dev finded");
					}*//*else{
						DFUManager.log(TAG, "mac"+mac+"mBluetoothDevices size ="+mBluetoothDevices.size());
						for(ExtendedBluetoothDevice dev : mBluetoothDevices){
							System.out.println("找到设备的mac"+dev.device.getAddress());
//							if(dev.device.getAddress().equals(mac)){//找到了该设备
								DFUManager.log(TAG, "找到设备");
								mDFUManager.connect(context, dev.device);
								break;
//							}else{
//								System.out.println("没找到设备"+mac);
//							}
						}
						//没找到设备 返回提示
//						DFUManager.log(TAG, "没找到设备2"+mac);
//						dfucallback.DFUuploadcallback(-1, "2not fund the dfu device");
					}*/
				}
			}

		}, SCAN_DURATION);
	}

	/**
	 * stop scan if user tap Cancel button
	 */
	@SuppressLint("NewApi") 
	private void stopScan() {
		if (mIsScanning) {
			DFUManager.log(TAG, "stopScan");
			mBluetoothAdapter.stopLeScan(mLEScanCallback);
			mIsScanning = false;
		}
	}

	private void addDevice(BluetoothDevice device, int rssi) {
        boolean deviceFound = false;
        System.out.println("addDevice +name="+device.getName()+"addr="+device.getAddress());
        for (ExtendedBluetoothDevice listDev : mBluetoothDevices) {
            if (listDev.device.getAddress().equals(device.getAddress())) {
                deviceFound = true;
                break;
            }
        }
        if (!deviceFound) {
        	mBluetoothDevices.add(convertDevice(device, rssi));
        	System.out.println("deviceList.add(device);");
        }
        
        mhandler.sendEmptyMessage(100);
    }
	
	/**
	 * convert BluetoothDevice and RSSI to single type ExtendedBluetoothDevice
	 */
	private ExtendedBluetoothDevice convertDevice(BluetoothDevice device, int rssi) {
		return new ExtendedBluetoothDevice(device, rssi);
	}

	private boolean deviceAlreadyExist(BluetoothDevice device) {
		if (!mBluetoothDevices.isEmpty()) {
			for (ExtendedBluetoothDevice bDevice : mBluetoothDevices) {
				if (bDevice.device.getAddress().equals(device.getAddress())) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Callback for scan devices DFUServiceParser class will read and parse the scanRecord and it will find out if scanned device has DFU service in it. If device has DFU service then it will be added
	 * in a list
	 */
	private BluetoothAdapter.LeScanCallback mLEScanCallback = new BluetoothAdapter.LeScanCallback() {
		
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			DFUManager.log(TAG, "onLeScan");
			if (device != null) {
				DFUManager.log(TAG, "Device scanned address: " + device.getAddress() + " name: " + device.getName() + " RSSI: " + rssi);
				mDFUServiceParser = DFUServiceParser.getDFUParser();
				try {
					mDFUServiceParser.decodeDFUAdvData(scanRecord);
					if (mDFUServiceParser.isValidDFUSensor()) {
						DFUManager.log(TAG, "Device scanned address: " + device.getAddress() + " name: " + device.getName() + " RSSI: " + rssi);
//						addDFUDevice(device, rssi);
						if(device.getName() == null||device.getName().equals("ODF")){
							addDevice(device, rssi);
						}else{
							DFUManager.log(TAG, "not the pillow dev");
						}
						
					} else {
						DFUManager.log(TAG, "Not DFU device!");
					}
				} catch (Exception e) {
					DFUManager.logE(TAG, "Invalid data in Advertisement packet " + e.toString());
				}
			}
		}
	};
	
	private void initializeDFUManager() {
		mDFUManager = DFUManager.getDFUManager();
		mDFUManager.setGattCallbacks(DFUmanagercallbacks);
	}
	
	private DFUManagerCallbacks DFUmanagercallbacks = new DFUManagerCallbacks() {
		
		/*
		 * Callbacks from DFUManager class(non-Javadoc)
		 * @see no.nordicsemi.android.nrftoolbox.dfu.DFUManagerCallbacks#onDeviceConnected()
		 */
		@Override
		public void onDeviceConnected() {
			DFUManager.log(TAG, "onDeviceConnected()");
			isDeviceConnected = true;
			
			
		}

		/*
		 * Callbacks from DFUManager class(non-Javadoc)
		 * @see no.nordicsemi.android.nrftoolbox.dfu.DFUManagerCallbacks#onDFUServiceFound()
		 */
		@Override
		public void onDFUServiceFound() {
			DFUManager.log(TAG, "onDFUServiceFound");
			isDFUServiceFound = true;
//			setDFUNameOnView(mDevice.getName());
			//连接上了 之后要做的是 找到文件 然后 upload
			List<String> hexlist = FileUtil.getFilePathFromSD(FileUtil.getHexpath(), "hex");
			if(hexlist.size() == 1){
				Startupload(hexlist.get(0));
			}else{
				dfucallback.DFUuploadcallback(DFUCallBack.DFU_DEV_NOT_FUND_OR_FUNDTWO, "more than one file be find");
			}
			
			
			
		}
		

		/*
		 * Callbacks from DFUManager class(non-Javadoc)
		 * @see no.nordicsemi.android.nrftoolbox.dfu.DFUManagerCallbacks#onDeviceDisconnected()
		 */
		@Override
		public void onDeviceDisconnected() {
			DFUManager.log(TAG, "onDeviceDisconnected()"+mFileTransferStatus);
			isDeviceConnected = false;
			if (mFileTransferStatus == START_TRANSFER) {//传输被打断
				DFUManager.log(TAG, "传输被打断");
//				showFileTransferUnSuccessMessage();
				dfucallback.DFUuploadcallback(DFUCallBack.DFU_DEV_TRANSFER_BEINTERRUPT, "传输被打断file transfer unsuccess ");
//				mDFUManager.disconnect();
			}
			if(mFileTransferStatus == NO_TRANSFER){
				DFUManager.log(TAG, "没有完成数据传输");
				dfucallback.DFUuploadcallback(DFUCallBack.DFU_DEV_TRANSFER_NOT_SUCCESS, "没有完成数据传输");
			}
			if (isFileValidated) {//传输完成
				DFUManager.log(TAG, "传输完成"+mFileTransferStatus);
//				showFileTransferSuccessMessage();
				dfucallback.DFUuploadcallback(DFUCallBack.DFU_DEV_TRANSFER_SUCCESSED, "file transfer successed " );
				isFileValidated = false;
			}
			reset();//重置
		}


		@Override
		public void onFileTransferStarted() { //开始
			DFUManager.log(TAG, "onFileTransferStarted()");
//			showProgressBar();
			mFileTransferStatus = START_TRANSFER;
//			disableConnectButton();//已经开始 不能再次开始
			dfucallback.DFUuploadcallback(DFUCallBack.DFU_DEV_TRANSFER_STARTING, "start to transfer file" );
		}

		/*
		 * Callbacks from DFUManager class(non-Javadoc)
		 * @see no.nordicsemi.android.nrftoolbox.dfu.DFUManagerCallbacks#onFileTranfering(long)
		 */
		@Override
		public void onFileTranfering(long sizeTransfered) {//传输中
			DFUManager.log(TAG, "onFileTransfering(): " + sizeTransfered);
			mPercentage = (int) ((sizeTransfered * 100) / mDFUManager.getFileSize());
//			updateProgressBar();
			dfucallback.DFUuploadcallback(DFUCallBack.DFU_DEV_TRANSFER_PRICENTAGE, mPercentage+"");
		}

	
		@Override
		public void onFileTransferCompleted() {//传输完成
			DFUManager.log(TAG, "onFileTransferCompleted()");
			mFileTransferStatus = FINISHED_TRANSFER;
			dfucallback.DFUuploadcallback(DFUCallBack.DFU_DEV_TRANSFER_COMPLETED, "File Transfer Completed");
//			hideProgressBar();
//			setUploadButtonText(R.string.dfu_action_upload);
		}

		
		@Override
		public void onFileTransferValidation() {
			DFUManager.log(TAG, "onFileTransferValidation()");
			isFileValidated = true;
		}

		
		@Override
		public void onError(String message, int errorCode) {//传输出错
			DFUManager.logE(TAG, "onError() " + message + " ErrorCode: " + errorCode);
			dealWithError(message, errorCode);
		}

		private void dealWithError(String message, int errorCode) {
			if (isDeviceConnected) {
				mDFUManager.systemReset();
			}
//			showErrorMessage(message, errorCode);
		}

		@Override
		public void onDFUServiceNotFound() {
			dfucallback.DFUuploadcallback(DFUCallBack.DFU_DEV_NOT_SUPPORT_UPLOAD, "your device not support upload the smartpillow");
			reset();//重置
		}
	};
	
	private  Handler mhandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 100:
//				this.postDelayed(new Runnable() {
//					public void run() {
						stopScan();
						for(ExtendedBluetoothDevice dev : mBluetoothDevices){
							System.out.println("找到设备的mac"+dev.device.getAddress());
							DFUManager.log(TAG, "找到设备");
							if(mDFUManager!=null){
								mDFUManager.connect(context, dev.device);
								System.out.println("开始连接设备mac"+dev.device.getAddress());
							}
							break;
						}
//					}
//				}, 2000);
				break;

			default:
				break;
			}
			
		}
		
	};
	
	
	private void Startupload(String mFilePath){
		System.out.println("mFilePath="+mFilePath);
		try {
			if (isDFUServiceFound) {
				InputStream hexStream = new FileInputStream(mFilePath);
				mDFUManager.openFile(hexStream);
				isFileValidated = false;
				mDFUManager.enableNotification();
			} else {
				DFUManager.logE(TAG, "DFU device is not connected with phone");
			}
		} catch (FileNotFoundException e) {
			DFUManager.logE(TAG, "An exception occured while opening file" + " " + e);
		} catch (IOException e) {
			DFUManager.logE(TAG, "An exception occured while reading file" + " " + e);
		}
	}
	
	private void reset() {
		mFileTransferStatus = NO_TRANSFER;
		isDFUServiceFound = false;
		mDFUManager.closeFile();
		mDFUManager.closeBluetoothGatt();
		mDFUManager.resetStatus();
	}
	
}
