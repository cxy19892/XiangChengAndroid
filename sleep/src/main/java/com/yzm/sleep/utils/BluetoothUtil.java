package com.yzm.sleep.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

public class BluetoothUtil {

	/**
	 *  判断蓝牙设备是否开启
	 * @return
	 */
	public static boolean bluetoothIsOn(Context context) {
		BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
		if (ba != null) {
			if (ba.isEnabled()) 
//				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//				startActivity(intent);
				return true;
		}else{
			ToastManager.getInstance(context).show("对不起，您的手机没有蓝牙设备");
		}
		return false;
	}
	
	
}
