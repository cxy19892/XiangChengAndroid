package com.yzm.sleep.bluetoothNRF;

import android.bluetooth.BluetoothDevice;
/**
 * �ҵ��������豸����
 * @author Administrator
 *
 */
public class ExtendedBluetoothDevice {
	public BluetoothDevice device;
	public int rssi;

	/**
	 * �ҵ��������豸����
	 * @param device
	 * @param rssi
	 */
	public ExtendedBluetoothDevice(BluetoothDevice device, int rssi) {
		this.device = device;
		this.rssi = rssi;
	}
}
