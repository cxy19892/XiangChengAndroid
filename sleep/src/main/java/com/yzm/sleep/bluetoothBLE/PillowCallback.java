package com.yzm.sleep.bluetoothBLE;

import android.os.Message;

public interface PillowCallback {
	/**
	 * msg.what = 
	 * 8000:传入数据有误，提示需要先绑定在同步<br>
	 * 8001:没有找到设备<br>
	 * 8002:找到了多个设备<br>
	 * 8003:找到设备开始链接<br>
	 * 8004:未连接GATT服务端<br>
	 * 8005:未发现GATT服务<br>
	 * 8006:DEVICE_DOES_NOT_SUPPORT_UART<br>
	 * 9000:密码校验失败<br>
	 * 9001:密码校验成功<br>
	 * 9002:更换绑定成功<br>
	 * 9003:更滑绑定失败<br>
	 * 9004:电池电量返回 msg.obj<br>
	 * 9005:固件版本返回 msg.obj<br>
	 * 9006:数据包个数     msg.obj<br>
	 * 9007:固件收到发送的升级命令返回<br>
	 * 9008:数据包接受中 百分比 <br>
	 * 9009:数据包接收结束<br>
	 * 9010:时间同步完成<br>
	 * 9011:恢复出厂设置完成<br>
	 * 9012:异常结束<br>
	 * @param msg
	 */
	public void getPillowcallback(Message msg);

	public void getPillowError(int errorcode, String errormsg);
	
	public void pillowData(byte[] buffer);
}
