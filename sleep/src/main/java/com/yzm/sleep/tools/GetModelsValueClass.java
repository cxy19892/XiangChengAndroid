package com.yzm.sleep.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GetModelsValueClass {

	private Lock lockBaseID = new ReentrantLock();

	public List<ModelsValues> m_pValue = new ArrayList<ModelsValues>();

	public class ModelsValues {
		public long mtime;
		public long mlTotalTime;
		public byte[] mBuf;

		public ModelsValues(byte[] value, long time,long mlTotalTime) {
			this.mBuf = value;
			this.mtime = time;
			this.mlTotalTime=mlTotalTime;
		}
	}

	public void ProcBuff(byte[] buff) {
		lockBaseID.lock();
		m_pValue.clear();

		int iCurrIndex = 0;

		long iDatalen = 0;	
		long iSumNum = 0;

		// ///////////////////包头解析
		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			iSumNum = bytesToInt(temp);
		}
		

		
		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			iDatalen = bytesToInt(temp);
		}

		String strTempString = "";
		// /处理MODEL数据。。。多少天的数据

		long ltime = 0;
		long iBuffLen = 0;
		long lTotalLen=0;
		for (int i = 0; i < iSumNum; i++) {

			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];

			ltime = bytesToInt(temp);

			
			byte[] temp2= new byte[4];
			temp2[0] = buff[iCurrIndex++];
			temp2[1] = buff[iCurrIndex++];
			temp2[2] = buff[iCurrIndex++];
			temp2[3] = buff[iCurrIndex++];

			iBuffLen  = bytesToInt(temp2);
			
			
			
			byte[] temp3 = new byte[4];
			temp3[0] = buff[iCurrIndex++];
			temp3[1] = buff[iCurrIndex++];
			temp3[2] = buff[iCurrIndex++];
			temp3[3] = buff[iCurrIndex++];

			lTotalLen= bytesToInt(temp3);

			byte[] databuff = new byte[((int) iBuffLen + 12)];

			databuff[0] = temp[0];
			databuff[1] = temp[1];
			databuff[2] = temp[2];
			databuff[3] = temp[3];

			databuff[4] = temp2[0];
			databuff[5] = temp2[1];
			databuff[6] = temp2[2];
			databuff[7] = temp2[3];

			for (int j = 12; j < (iBuffLen + 12); j++) {

				databuff[j] = buff[iCurrIndex++];

			}

			m_pValue.add(new ModelsValues(databuff, ltime,lTotalLen));

		}

		lockBaseID.unlock();

	}

	public static long bytesToInt(byte[] bytes) {
		String hex = "";
		String hexTemp = "";
		for (int i = bytes.length - 1; i >= 0; i--) {
			hexTemp = Integer.toHexString(bytes[i] & 0xFF);

			if (hexTemp.length() == 1) {
				hex = hex + '0' + hexTemp;
			} else {
				hex = hex + hexTemp;
			}
			// System.out.print(hex.toUpperCase() );
		}

		long Rst = Long.parseLong(hex, 16);
		return Rst;
	}

}
