package com.yzm.sleep.bluetoothBLE;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.yzm.sleep.bluetoothBLE.AgreementDev.Errors;
import com.yzm.sleep.bluetoothBLE.AutoControl.DevStatus;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownLoadRomInfoClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetDownLoadRomInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownLoadRomCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareDataCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareDataParamClass;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepDataProClass;
import com.yzm.sleep.utils.StringUtil;


/**
 * 在使用的时候 PillowServer 传入指定的参数
 * 在退出的地方调用 Stop_server 信息通过PillowCallback返回返回message格式的信息
 * @author Administrator
 *
 */
public class CopyOfPillowHelper {

	AgreementDev m_agreenAgreementDev = new AgreementDev();
	AutoControl m_AutoControl = new AutoControl();
	boolean m_AutoControlBoolen = false;
	
	private Context activity;
	/** 1 代表 同步 2 代表 出厂  3 代表 空中*/
//	int in_iSelect = 0;
	int m_iSelect = 0;
	private String sensitive = "6,2,2";
	private String TAG = "PillowServer";
	private final int SENDDELAY = 10;
	private String mac = null;
	private byte[] buffgetdate = null;
	private UartService mService = null;
	private PillowBean bund_pillow = null;
	private PillowCallback callback = null;
//	private Thread m_runableRunnable = null;
//	private BluetoothAdapter mBluetoothAdapter = null;
	
	private boolean ISDEBUG = false;
	private boolean m_bProcData = true;
	private TimerTask keepalive_timetask = null;
	private Timer keepalive_timer = null;
	private static CopyOfPillowHelper instance = null;
	
	/**
	 * 获取已经存在的 PillowHelper
	 * @return
	 */
	public static CopyOfPillowHelper getinstance(){
		if(null != instance){
			return instance;
		}
		return null;
	}
	public static CopyOfPillowHelper Getinstance(Context context, PillowCallback callback, String sensitive){
		if (null == instance){
			instance = new CopyOfPillowHelper(context, callback, sensitive);
		}
		return instance;
	}
	
	public CopyOfPillowHelper(Context activity, PillowCallback callback, String sensitive){
		this.activity = activity;
		
		this.callback = callback;
		
		this.sensitive = sensitive;
		LogUtil.e("chen", "sensitive"+sensitive);
		m_runableAutoRunnable.start();
		
		service_init(activity);
	}

	
	
	public void startConnectDev(int in_iSelect, final String macaddr){
		this.m_iSelect = in_iSelect;
		this.mac = macaddr;
		PillowHelperLogs("StartSerch"+macaddr);
		if(null != mService){
			boolean isconnected = mService.connect(macaddr);//连接设备
			LogUtil.d("chen", isconnected?"链接成功":"连接失败");
		}
		bund_pillow = new PillowBean();
		bund_pillow.setPillowMac(macaddr);
	}
	
	/**
	 * 初始化 severce和广播
	 * 绑定service
	 * 注册广播
	 * @param act
	 */
	private void service_init(Context act) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		PillowHelperLogs("service_init");
		Intent bindIntent = new Intent(act, UartService.class);
		
		act.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

		@SuppressWarnings("deprecation")
		String strMsg = "Product Model: " + android.os.Build.MODEL + ","
				+ android.os.Build.VERSION.SDK_INT + ","
				+ android.os.Build.VERSION.RELEASE;
		System.out.print(strMsg);
		// > 18

		LocalBroadcastManager.getInstance(act).registerReceiver(//注册广播
				UARTStatusChangeReceiver, makeGattUpdateIntentFilter());

	}
	/**
	 * 反注册广播
	 * 解绑service
	 * @param act activity
	 */
	public void Stop_server(Context act){
		try {
			cancleDevUpload();
			LocalBroadcastManager.getInstance(act).unregisterReceiver(
					UARTStatusChangeReceiver);
		} catch (Exception ignore) {
			PillowHelperLogs(ignore.toString());
		}finally{
		m_bProcData = false;
		try {
			act.unbindService(mServiceConnection);
		} catch (Exception e1) {
			// 开启和结束不是同一个对象调用，会发生在
			e1.printStackTrace();
		}
		if(mService!=null){
			try {
				mService.stopSelf();
			} catch (Exception e) {
				System.out.println("stop mService error");
				e.printStackTrace();
			}finally{
				mService = null;
			}
		}
		instance = null;
		}
	}
	
	
	// UART service connected/disconnected
		private ServiceConnection mServiceConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className,
					IBinder rawBinder) {
				mService = ((UartService.LocalBinder) rawBinder).getService();
				PillowHelperLogs("onServiceConnected mService= " + mService);
				if (!mService.initialize()) {
					//提示当前设备不支持蓝牙功能
					PillowHelperLogs("Unable to initialize Bluetooth");
				}
			}

			public void onServiceDisconnected(ComponentName classname) {
				mService = null;
			}
		};
	

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
		intentFilter.addAction(UartService.BLUETOOTHGATT_CLOSE);
		return intentFilter;
	}
	
	@SuppressWarnings("static-access")
	public void Startbund(){
		m_AutoControl.Init();
		m_agreenAgreementDev.init();
		m_AutoControl.SetSendInter(mService);
		m_AutoControlBoolen = true;
		m_AutoControl.SendData((byte) 0x18, "xc123321");
	}
	private Runnable runs = new Runnable() {
		
		@Override
		public void run() {
			m_AutoControl.Init();
			m_agreenAgreementDev.init();
			m_AutoControl.SetSendInter(mService);
			m_AutoControlBoolen = true;
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_AutoControl.SendData((byte) 0x18, "xc123321");
		}
	};
	
	private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			PillowHelperLogs(action);
			final Intent mIntent = intent;
			// *********************//
			if (action.equals(UartService.ACTION_GATT_CONNECTED)) {//连接到GATT服务端
				PillowHelperLogs("UART_CONNECT_MSG");
				mHandler.sendEmptyMessage(9010);
			}
			// *********************//
			else if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {//未连接GATT服务端.

				PillowHelperLogs("ACTION_GATT_DISCONNECTED");
				m_AutoControlBoolen = false;
				mHandler.sendEmptyMessage(9011);
			}
			// *********************//
			else if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {//未发现GATT服务.
				mService.enableTXNotification();
				PillowHelperLogs("ACTION_GATT_SERVICES_DISCOVERED");
				mHandler.sendEmptyMessage(9012);
			}
			// *********************//
			else if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {//读到数据
				final byte[] txValue = intent
						.getByteArrayExtra(UartService.EXTRA_DATA);
				m_agreenAgreementDev.AddRecvData(txValue);
			}
			// *********************//
			else if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)) {
				PillowHelperLogs("DEVICE_DOES_NOT_SUPPORT_UART");
				mHandler.sendEmptyMessage(9013);
			}else if(action.equals(UartService.BLUETOOTHGATT_CLOSE)){
				PillowHelperLogs("BLUETOOTHGATT_CLOSE");
				mHandler.sendEmptyMessage(9016);
			}
		}
	};
	
	Thread m_runableAutoRunnable =  new Thread(new Runnable() {
		String text = "";

		@Override
		public void run() {
			while (m_bProcData) {

				if(!m_AutoControlBoolen)
				{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				try {
				m_agreenAgreementDev.ProcData(); 
				long lCurrTime = System.currentTimeMillis();
//				try {  移到上面去 m_agreenAgreementDev.ProcData(); 可能产生IllegalMonitorStateException                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
					if (Errors.None == m_agreenAgreementDev.Proc()) {
						m_AutoControl.ProcPack(m_agreenAgreementDev.toData);

						if (m_AutoControl.g_StringIndex == DevStatus.PASS_CHECK_FAIL_0x17)// 密码失败
						{
							m_AutoControlBoolen = false;
							Message msg = new Message();
							msg.what = 9000;
							msg.obj = "接受密码校验结果，密码错误，流程退出！0x02";
							mHandler.sendMessage(msg);
							break;
						} else if (m_AutoControl.g_StringIndex == DevStatus.PASS_CHECK_SUCCESS_0x10) {
							Message msg = new Message();
							msg.what = 9001;
							msg.obj = "接受密码校验结果：密码成功！ 0x02";
							mHandler.sendMessage(msg);
							Thread.sleep(SENDDELAY);
							if(m_iSelect==2)//绑定
							{
								m_AutoControl.SendData((byte) 0x38, ""); // 发�?�绑�?
							}else if(m_iSelect==1||m_iSelect == 3)
							{
								m_AutoControl.SendData((byte)0x28 ,sensitive); // 发�?�同�?
							}
						} else if (m_AutoControl.g_StringIndex == DevStatus.ID_CHANGE_SUCCESS_0x30) {
							Message msg = new Message();
							msg.what = 9003;
							msg.obj = "更换绑定	设备到手�?	更换绑定成功";
							mHandler.sendMessage(msg);						

							System.out.println("加入的新流程。流程控制含义，灭灯。");

						}else if(m_AutoControl.g_StringIndex == DevStatus.ID_Bound_SUCCESS_0x70) {


							Message msg = new Message();
							msg.what = 9000;
							msg.obj = "更换绑定	设备到手�?	更换绑定成功";
							mHandler.sendMessage(msg);
							m_AutoControl.SendData((byte)0x28 ,sensitive); // 发�?�同�?

							System.out.println("发�?�手机同�?");
						} else if (m_AutoControl.g_StringIndex == DevStatus.ID_CHANGE_FAIL_0x00) {

							// 此步是为了发送用户ID�?
							Message msg = new Message();
							msg.what = 9001;
							msg.obj = "接受密码校验结果：更换绑定失�?";
							mHandler.sendMessage(msg);

						} else if (m_AutoControl.g_StringIndex == DevStatus.DATA_POWER_0x22 ){

							// 数据同步数据�?�?
							bund_pillow.setPowerValue(String. valueOf(m_AutoControl.g_ADValue));
							Message msg = new Message();
							msg.what = 9001;
							String strValue="AD数据接收   0x06---->value="+String.valueOf(m_AutoControl.g_ADValue);	
							System.out.println(strValue);
							msg.obj = strValue;
							mHandler.sendMessage(msg);


						}		
						else if (m_AutoControl.g_StringIndex == DevStatus.DATA_DEVTYPE_0x23) {

							// 数据版本�?
							bund_pillow.setHardVersion( m_AutoControl. g_DevVersion);

							Message msg = new Message();
							msg.what = 9001;
							msg.obj = "服务器版本号   :\n"+m_AutoControl.g_DevVersion;
							System.out.println(msg.obj);
							mHandler.sendMessage(msg);

						}
						else if (m_AutoControl.g_StringIndex == DevStatus.DATA_DATASUM_0x21) {

							//数据包�?�数
							Message msg = new Message();
							msg.what = 90061;
							msg.obj = "数据接收�?�?: 数据包个�?"+String.valueOf(m_AutoControl.m_Total);
							System.out.println(msg.obj);
							mHandler.sendMessage(msg);

						}	else if (m_AutoControl.g_StringIndex == DevStatus.AIRUPDATA_SUCCESS_0x60) {

							//数据包�?�数
							Message msg = new Message();
							msg.what = 9001;
							msg.obj = "�?始空中升级�?�设备重启ing..";
							System.out.println(msg.obj);
							mHandler.sendMessage(msg);

						}  else if (m_AutoControl.g_StringIndex == DevStatus.DATA_DATAPACK_0x20) {
							// 数据同步数据�?�?
							Message msg = new Message();
							msg.what = 90062;					

							byte[] buffsend=m_AutoControl.getM_bDataList();			

							Bundle bundle =new Bundle();
							bundle.putByteArray("RECV", buffsend);							
							msg.setData(bundle);	

							msg.obj = String.valueOf(m_AutoControl.g_Precent);
							mHandler.sendMessage(msg);
							System.out.println(msg.obj);
							// 数据同步ing

						} else if (m_AutoControl.g_StringIndex == DevStatus.DATA_OVER_0x24) {
							//同步完成�?
							Message msg = new Message();
							msg.what = 90063;
							String strValue = "数据同步完成......   0x06   百分�? �? "
									+ String.valueOf(m_AutoControl.g_Precent)
									+ "%";
							msg.obj = String.valueOf(m_AutoControl.g_Precent) + "%";
							mHandler.sendMessage(msg);
							System.out.println(msg.obj);
//							Thread.sleep(SENDDELAY);
							LogUtil.i("huang", "检查是否有新版本" + m_iSelect);
							mHandler.sendEmptyMessage(9015);
						}else if (m_AutoControl.g_StringIndex ==DevStatus.TIME_CHECK_SUCCESS_0x40) {

							// 出厂设置成功流程结束
							m_AutoControlBoolen = false;
							Message msg = new Message();
							msg.what = 9001;
							msg.obj = "时间校验成功"+m_AutoControl.g_Precent;
							mHandler.sendMessage(msg);

						}  else if (m_AutoControl.g_StringIndex == DevStatus.RESET_Y_0x0c) {
							// 出厂设置成功流程结束
							m_AutoControlBoolen = false;
							Message msg = new Message();
							msg.what = 9000;
							msg.obj = "接收回复出厂设置 �?0x0c 流程结束";
							mHandler.sendMessage(msg);
						} 
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					m_AutoControlBoolen = false;
					Message msg = new Message();
					msg.what = 9000;
					msg.obj = "流程异常,流程结束";
					mHandler.sendMessage(msg);

				}
			}

		}});

	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 9000) {//接受密码校验结果，密码错误，流程退出！0x02


			}else if (msg.what == 90061)//数据接收�?�?: 数据包个�?
			{
				
			}		
			else if (msg.what == 90062) //同步数据百分比
			{
			
				
				byte[] buffsend = 	(byte[]) msg.getData().getByteArray("RECV");
				if(buffgetdate == null){
					buffgetdate = new byte[buffsend.length];
					System.arraycopy(buffsend, 0, buffgetdate, 0, buffsend.length);
				}else{
					byte[] buffgetdatetemp =  new byte[buffsend.length+buffgetdate.length];
					System.arraycopy(buffgetdate, 0, buffgetdatetemp, 0, buffgetdate.length);
					System.arraycopy(buffsend, 0, buffgetdatetemp, buffgetdate.length, buffsend.length);
					buffgetdate = buffgetdatetemp;
				}
				Message msgsend = new Message();
				msgsend.what = 9008;
				msgsend.obj = msg.obj;
            	callback.getPillowcallback(msgsend);
			
			}else if (msg.what == 90063) {//数据同步完成  0x06 
			
				PillowHelperLogs("data:"+"数据同步完成......   0x06   百分只百 "
						+ String.valueOf(m_AutoControl.g_Precent)+ "%");
				PreManager.instance().setBundbluetoothPillow(activity, bund_pillow.getPillowMac());
				PreManager.instance().setBluetoothPillowBatteryValue(activity, bund_pillow.getPowerValue());
				PreManager.instance().setBluetoothPillowFirmwareVersion(activity, bund_pillow.getHardVersion());
				sendMessageBack(9009, "100%");
				if(callback != null)
					callback.pillowData(buffgetdate);
				//上传设备信息到服务器
				UpDEVdata2server(bund_pillow);
			} else if (msg.what == 9001) {
				System.out.println(msg.obj.toString());
				
			} else if (msg.what == 9003) {

				sendMessageBack(9020, "更换绑定,更换绑定成功");
				System.out.println("加入的新流程。流程控制含义，灭灯。");
				Start_Sendkeepalivemsg();
			}else if(msg.what == 9010){
				Message msgsend = new Message();
				msgsend.what = 8007;
				msgsend.obj = "UART_CONNECT_MSG";
            	callback.getPillowcallback(msgsend);
            	mHandler.postDelayed(runs, 1800);
			}else if(msg.what == 9011){
				Message msgsend = new Message();
				msgsend.what = 8004;
				msgsend.obj = "ACTION_GATT_DISCONNECTED";
            	callback.getPillowcallback(msgsend);
            	if(mService!= null)
            		mService.close();
			}else if(msg.what == 9012){
				Message msgsend = new Message();
				msgsend.what = 8005;
				msgsend.obj = "ACTION_GATT_SERVICES_DISCOVERED";
            	callback.getPillowcallback(msgsend);
			}else if(msg.what == 9013){
				if(null != mService){
					mService.disconnect();
					Message msgsend = new Message();
					msgsend.what = 8006;
					msgsend.obj = "DEVICE_DOES_NOT_SUPPORT_UART";
	            	callback.getPillowcallback(msgsend);
				}
			}else if(msg.what == 9014){
				System.out.println("传给 ui线程-------------数据包接收结束");
				sendMessageBack(9009, "100%");
			}else if(msg.what == 9015){
				Start_Sendkeepalivemsg();
				getnewversion(bund_pillow.getHardVersion());
			}else if(msg.what == 9016){
				sendMessageBack(9021, "");
			}
		}
		
	};
	private void sendMessageBack(int msgwhat, String msgobj){
		Message msg = new Message();
		msg.what = msgwhat;
		msg.obj = msgobj;
		callback.getPillowcallback(msg);
	}
	
	
	private void PillowHelperLogs(String msg){
		if(ISDEBUG){
			Log.d(TAG, msg);
		}
	}
	
	
	//====================================================================================================

	private void getnewversion(final String the_DEV_version){
			if(PreManager.getNetType(activity) == -1){
				cancleDevUpload();
				LogUtil.i("huang", "界面关闭了-->取消");
				return;
			}
			final GetDownLoadRomInfoParamClass mParam = new GetDownLoadRomInfoParamClass();
			mParam.version = the_DEV_version.split("_").length>=0?the_DEV_version.split("_")[0]:the_DEV_version;//mypillow.getHardVersion();
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new SleepDataProClass(activity).GetDownloadNewRomUrl(mParam, new InterfaceDownLoadRomCallBack(){
						//
							@Override
							public void onError(int icode, String strErrMsg) {
								LogUtil.i("huang", "获取rom包失败");
								//提示 获取rom包失败
								System.out.println(strErrMsg);
								//发送时间同步 然后结束
								cancleDevUpload();
							}
				
							@Override
							public void onSuccess(int iCode, final DownLoadRomInfoClass mResClass) {
								//1判断文件夹里面有没有文件
								//2如果没有 用得到的url下载到一个文件夹下面 下载完毕后升级
								//3如果有则直接进行升级
								//下载完毕之后发送升级命令 让设备重启 ，然后就是 空中升级的流程了 ；停止心跳命令的发送
								//提示需要升级
								LogUtil.i("huang", "获取rom包成功");
								System.out.println(mResClass.rom_url+"::"+mResClass.version_new);
								if(StringUtil.isNeedUpdate(the_DEV_version.split("_").length>=0?the_DEV_version.split("_")[0]:the_DEV_version, mResClass.version_new)){
									sendMessageBack(9016, "");
								}else{
									cancleDevUpload();
								}
							}
						});
				}
			}, 10);
			
		}
	
	//外部调用取消升级
	public void cancleDevUpload(){
		m_AutoControl.SendData((byte) 0x48, "");
		Stop_Sendkeepalivemsg();
	}
	/**
	 * 继续绑定流程
	 */
	public void continuToBund(){
		Stop_Sendkeepalivemsg();
		
		m_AutoControl.SendData((byte) 0x78,"");
	}
	/**
	 * 退出当前的自动流程
	 */
	public void cancleAutoControl(){
		Stop_Sendkeepalivemsg();
		m_AutoControlBoolen = false;
	}
	
	//外部调用开始升级
	public void ensureDevUpload(){
		Stop_Sendkeepalivemsg();
		m_AutoControl.SendData((byte) 0x68, ""); 
	}
	
	private void Start_Sendkeepalivemsg(){
		LogUtil.i("huang", "发送心跳");
		PillowHelperLogs("Start_Sendkeepalivemsg");
		if(keepalive_timetask == null ){
		keepalive_timetask = new TimerTask() {
			
			@Override
			public void run() {
				PillowHelperLogs("Start timertask");
				m_AutoControl.SendData((byte) 0x58, "");
			}
		};
		}
		if(keepalive_timer == null ){
			keepalive_timer = new Timer();
			keepalive_timer.schedule(keepalive_timetask, 10, 1500);
		}
	}
	
	private void Stop_Sendkeepalivemsg(){
		if(keepalive_timetask !=null){
			keepalive_timetask.cancel();
		}
		if(keepalive_timer != null){
			keepalive_timer.cancel();
		}
		keepalive_timer = null;
		keepalive_timetask = null;
	}
	
	
	private void UpDEVdata2server(PillowBean pillow){
		final UploadHardwareDataParamClass mParam = new UploadHardwareDataParamClass();
		mParam.my_int_id    = PreManager.instance().getUserId(activity);
		mParam.version      = pillow.getHardVersion();
		mParam.mac_address  = pillow.getPillowMac();
		mParam.battery_data = pillow.getPowerValue();
		new SleepDataProClass(activity).UploadHardwareData(mParam, new InterfaceUploadHardwareDataCallBack() {
			
			@Override
			public void onSuccess(int iCode, String strSuccMsg) {
				System.out.println("上传设备信息到服务器成功");
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
						System.out.println("上传设备信息到服务器失败");
					}
				});
	}

}
