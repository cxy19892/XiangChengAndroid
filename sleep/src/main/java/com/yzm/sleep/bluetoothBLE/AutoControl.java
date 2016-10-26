package com.yzm.sleep.bluetoothBLE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.R.integer;

import com.yzm.sleep.bluetoothBLE.AgreementDev.tsPacketOfAppInfo;


//import com.nrfUARTv2.AgreementDev.tsPacketOfAppInfo;

public class AutoControl {

	//设备命令接收�?
	 int CMD_TestUse_0x08 =0x08;
	 int CMD_PasswordCheck_0x18 =0x18;
	 int CMD_DataTrans_0x28 =0x28;
	public int CMD_ChangeID_0x38 =0x38;
	public int CMD_TimeCheck_0x48 =0x48;
	public int CMD_KeepOnline_0x58 =0x58;	
	public int CMD_AirUpdate_0x68 =0x68;
	public int CMD_CheckBound_0x78 =0x78;
	
	public int DevRetrun_TestUse_0x00 =0x00;
	public int DevRetrun_PasswordCheck_Sucess_0x10 =0x10;
	public int DevRetrun_PasswordCheck_Fail_0x17 =0x17;
	public int DevRetrun_DataTrans_Data_0x20 =0x20;
	public int DevRetrun_DataTrans_DataLength_0x21 =0x21;
	public int DevRetrun_DataTrans_Power_0x22 =0x22;
	public int DevRetrun_DataTrans_DevVer_0x23 =0x23; 
	public int DevRetrun_DataTrans_Over_0x24 =0x24; 
	public int DevRetrun_ChangeID_Success_0x30 =0x30;
	public int DevRetrun_TimeCheck_Success_0x40 =0x40;
	public int DevRetrun_KeepOnline_Return_0x50 =0x50;	
	public int DevRetrun_AirUpdate_Recv_0x60=0x60;
	public int DevRetrun_CheckBound_Recv_0x70 =0x70;
	
	
	
	private UartService mService = null;
	
	private  Lock  m_buffLock = new ReentrantLock();
	
	
	public void SetSendInter(UartService mServic )
	{
		mService=mServic;		
	};
	
	public static enum DevStatus {
		/** 设备状�?�未�? */
		NONE_0x00,
		/** 用于测试使用，可以发送任意数�? */
		DEV_NONE_0x00,
		/** 密码校验结果：校验成�?*/
		PASS_CHECK_SUCCESS_0x10,
		/** 密码校验结果：校验失�? */
		PASS_CHECK_FAIL_0x17,
		
		/**接受密码校验结果�? 密码流程异常 */
		PASS_CHECK_OHTER_0X00,
		
		
		/**更换绑定成功信息已收�? �? 更换绑定成功 */
		ID_CHANGE_SUCCESS_0x30,
		
		/**按钮确认以后，灭灯过程确认*/
		ID_Bound_SUCCESS_0x70,
		
		/** 更换绑定成功失败，流程异�?*/
		ID_CHANGE_FAIL_0x00,
		
		/** 接受服务器时间已收到：时间同步成�? */
		TIME_CHECK_SUCCESS_0x40,
		
		/** 接受服务器时间流程异�? */
		TIME_CHECK_FAIL_0x00,
		
		/** 保持通信收到 */
		KEEPCONNECT_SUCCESS_0x50,
		
		/** 保持通信收到 流程异常*/
		KEEPCONNECT_SUCCESS_0x00,
		
		/** 接收回复出厂设置 */
		RESET_Y_0x0c,
			
		/** 空中升级收到 */
		AIRUPDATA_SUCCESS_0x60,
			
		/** 电量数据接收�? */
		DATA_POWER_0x22,
		
		/** 设备版本�? */
		DATA_DEVTYPE_0x23,
		
		/** 设备版本�? */
		DATA_DATASUM_0x21,
		
		/** 数据包接收中 */
		DATA_DATAPACK_0x20,
		
		/** 数据接收结束 */
		DATA_OVER_0x24,
	
		
	}



	boolean m_bStartPack = false;

	public DevStatus g_StringIndex;

	public long g_lastSendTime = 0;// System.currentTimeMillis();

	public float g_Precent = 0;

	public float g_ADValue=0;
	
	public String g_DevVersion="";
	
	public long m_Total = 0;

	private long m_currRecvIndex = 0;

	public List<byte[]> m_bDataList = new ArrayList<byte[]>();

	class FlagStat {
		public int iTestFlag;
		public int iPassWordFlag;
		public int IDCheckFlag;
		public int DataCommitFlag;
		public int TimeChangeFlag;
		public int IDChangeFlag;
		public int ReSetFlag;

		FlagStat() {
			this.iTestFlag = 0;
			this.iPassWordFlag = 0;
			this.IDCheckFlag = 0;
			this.DataCommitFlag = 0;
			this.TimeChangeFlag = 0;
			this.IDChangeFlag = 0;
			this.ReSetFlag = 0;

		}
	}

	class CmdBuff {

		int m_CMD;
		byte[] m_Buff;

		CmdBuff() {
			this.m_Buff = null;
			this.m_CMD = 0x00;
		}

	}

	public FlagStat g_flagFlagStat = new FlagStat();

	public void Init() {
		g_StringIndex = DevStatus.NONE_0x00; // 流程进行步骤

		g_lastSendTime = 0;// System.currentTimeMillis();

		g_flagFlagStat.iTestFlag = 0;
		g_flagFlagStat.iPassWordFlag = 0;
		g_flagFlagStat.IDCheckFlag = 0;
		g_flagFlagStat.DataCommitFlag = 0;
		g_flagFlagStat.TimeChangeFlag = 0;
		g_flagFlagStat.IDChangeFlag = 0;
		g_flagFlagStat.ReSetFlag = 0;

		m_Total = 0;

		m_currRecvIndex = 0;
	}

	public void SendData(byte cmd, String strData) {

		System.out.println("SendData = "+cmd);
		String message = strData;
		
		byte[] value = null;
		short year;
		byte month;
		byte day;
		byte hour;
		byte minute;
		byte second;

		try {
			// send data to service

			tsPacketOfAppInfo dataBuff = new tsPacketOfAppInfo();
			dataBuff.u8PacketNO = 0;
			dataBuff.u8CMD = cmd;

			if (dataBuff.u8CMD == CMD_TestUse_0x08) {

				value = message.getBytes("UTF_8");

			}else if (dataBuff.u8CMD == CMD_PasswordCheck_0x18) {
				
				value = message.getBytes("UTF_8");

			}else if (dataBuff.u8CMD == CMD_DataTrans_0x28) {
				value = new byte[5];
//				value[0] = (byte) 0x00;
				
				System.out.println(strData+"--------");
			String []strValueString =strData.split(",");
			
		
			
			value[0]=Integer.valueOf(strValueString[0]).byteValue();
			value[1]=',';
			value[2]=Integer.valueOf(strValueString[1]).byteValue();
			value[3]=',';
			value[4]=Integer.valueOf(strValueString[2]).byteValue();
				//value = message.getBytes("UTF_8");
				
			} else if (dataBuff.u8CMD == CMD_ChangeID_0x38) {

				value = new byte[1];
				value[0] = (byte) 0x00;

			} else if (dataBuff.u8CMD == CMD_TimeCheck_0x48) {

				Calendar c = Calendar.getInstance();
				year = (short) c.get(Calendar.YEAR);
				month = (byte) (c.get(Calendar.MONTH)+1);//Calendar.MONTH 得到的是0-11的范围

				day = (byte) c.get(Calendar.DAY_OF_MONTH);

				hour = (byte) c.get(Calendar.HOUR_OF_DAY);

				minute = (byte) c.get(Calendar.MINUTE);

				second = (byte) c.get(Calendar.SECOND);

				value = new byte[7];
				byte[] year1;
				year1 = AgreementDev.getBytes(year, false);
				value[0] = year1[0];
				value[1] = year1[1];
				value[2] = month;
				value[3] = day;
				value[4] = hour;
				value[5] = minute;
				value[6] = second;

			}else if (dataBuff.u8CMD == CMD_KeepOnline_0x58) {

				value = new byte[1];
				value[0] = (byte) 0x00;

			}else if (dataBuff.u8CMD == CMD_AirUpdate_0x68) {

				value = new byte[1];
				value[0] = (byte) 0x00;

			}else if (dataBuff.u8CMD == CMD_CheckBound_0x78) {

				value = new byte[1];
				value[0] = (byte) 0x00;

			}
//			Log.e("u8CMD", "发送的命令："+dataBuff.u8CMD);
			dataBuff.pData = value;

			byte buff[] = AgreementDev.LoadApp((byte) 0, (byte) 0,
					dataBuff.u8CMD, dataBuff);

			
			if(mService!=null)
			{
				g_lastSendTime=System.currentTimeMillis();
				System.out.print("send msg to ....");
				System.out.print("\n"+buff.toString());
				mService.writeRXCharacteristic(buff);
				
			}
		

		} catch (UnsupportedEncodingException e) {
			// TODO Auto_generated catch block
			e.printStackTrace();
		}


	}

	public DevStatus  ProcPack(tsPacketOfAppInfo returnbuff) {

//		System.out.println("AutoControl DevStatus = "+returnbuff.u8CMD);
		
		if (returnbuff.u8CMD == DevRetrun_TestUse_0x00) {
			
			g_StringIndex=DevStatus.DEV_NONE_0x00;
			
		} else if (returnbuff.u8CMD == DevRetrun_PasswordCheck_Sucess_0x10) {
		
				g_StringIndex = DevStatus.PASS_CHECK_SUCCESS_0x10;
	

		} else if (returnbuff.u8CMD == DevRetrun_PasswordCheck_Fail_0x17) {

				g_StringIndex = DevStatus.PASS_CHECK_FAIL_0x17;
		
		} else if (returnbuff.u8CMD == DevRetrun_DataTrans_Data_0x20) {

			g_StringIndex = DevStatus.DATA_DATAPACK_0x20;
			
			m_currRecvIndex++;			
			m_buffLock.lock();
			m_bDataList.add(returnbuff.pData);
			m_buffLock.unlock();
			
			g_Precent =  ((float)m_currRecvIndex / m_Total) * 100;

		//	System.out.println("%%");
			
		
		} else if (returnbuff.u8CMD == DevRetrun_DataTrans_Over_0x24) {

			g_StringIndex = DevStatus.DATA_OVER_0x24;
			
			m_currRecvIndex = 0;
			m_Total = 0;
			g_Precent = 100;
			
		} else if (returnbuff.u8CMD == DevRetrun_DataTrans_DataLength_0x21) {

			g_StringIndex = DevStatus.DATA_DATASUM_0x21;
			
			m_buffLock.lock();
			
			m_bDataList.clear();
			
			m_buffLock.unlock();
						
			
			m_Total = AgreementDev.bytesToInt(returnbuff.pData);
			
			System.out.println(String.valueOf(m_Total));
			g_Precent = 0;
			
		} else if (returnbuff.u8CMD == DevRetrun_DataTrans_Power_0x22) {

				g_StringIndex = DevStatus.DATA_POWER_0x22;
				m_Total = 0;
				m_currRecvIndex = 0;
				System.out.println("DATA_POWER_0x22");

				g_ADValue =  AgreementDev.bytesToSignLong(returnbuff.pData);
				System.out.println( "获取电池电量的返回！"+g_ADValue);

		}else if (returnbuff.u8CMD == DevRetrun_DataTrans_DevVer_0x23) {
			
			g_StringIndex = DevStatus.DATA_DEVTYPE_0x23;
			//设备版本信息未处理�??
			
			g_DevVersion  =  String.format("%s", returnbuff.pData);
			System.out.println("1版本信息"+g_ADValue);
			try {
				g_DevVersion = new String(returnbuff.pData, "GB2312");
//				System.out.println("2版本信息"+s1);
//				String s2 = new String(returnbuff.pData, "ISO8859-1");
//				System.out.println("3版本信息"+s2);
//				String s3 = new String(returnbuff.pData, "Unicode");
//				System.out.println("4版本信息"+s3);
//				String s4 = new String(returnbuff.pData, "UTF-8");
//				System.out.println("5版本信息"+s4);
//				String s5 = new String(returnbuff.pData, "GBK");
//				System.out.println("6版本信息"+s5);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if (returnbuff.u8CMD == DevRetrun_ChangeID_Success_0x30) {
			
			g_StringIndex = DevStatus.ID_CHANGE_SUCCESS_0x30;
			
		}else if (returnbuff.u8CMD == DevRetrun_CheckBound_Recv_0x70) {
			
			g_StringIndex = DevStatus.ID_Bound_SUCCESS_0x70;
		}else if (returnbuff.u8CMD == DevRetrun_TimeCheck_Success_0x40) {
			
			g_StringIndex = DevStatus.TIME_CHECK_SUCCESS_0x40;
			byte[] g_bufftemp= returnbuff.pData;
			
			int syear = 0;
			int imonth = 0;
			int iday = 0;
			int ihour = 0;
			int iminute = 0;
			int isecond = 0;

			byte[] yearbuff = new byte[2];
			yearbuff[0] = g_bufftemp[0];
			yearbuff[1] = g_bufftemp[1];
			syear = AgreementDev.bytesToInt(yearbuff);
			imonth = (int) g_bufftemp[2];
			iday = (int) g_bufftemp[3];
			ihour = (int) g_bufftemp[4];
			iminute = (int) g_bufftemp[5];
			isecond = (int) g_bufftemp[6];

			String strTime = String.format(
					"%d-%d-%d  %d:%d:%d", syear,
					imonth, iday, ihour, iminute,
					isecond);
			
		}else if (returnbuff.u8CMD ==DevRetrun_KeepOnline_Return_0x50 ) {
			
			g_StringIndex = DevStatus.KEEPCONNECT_SUCCESS_0x50;
			
		}else if (returnbuff.u8CMD ==DevRetrun_AirUpdate_Recv_0x60 ) {
			
			g_StringIndex = DevStatus.AIRUPDATA_SUCCESS_0x60;
			///空中升级收到命令。后续操作需要后面处理�??
		}				
			
		return g_StringIndex;

	}

	public byte[]  getM_bDataList() {
		m_buffLock.lock();		
		byte [] buff=null;		
		if(m_bDataList.size()>0)
			buff=m_bDataList.remove(0);
		m_buffLock.unlock();
		return  buff;
	}


	
	

}
