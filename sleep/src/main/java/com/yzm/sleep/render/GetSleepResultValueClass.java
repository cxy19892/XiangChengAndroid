package com.yzm.sleep.render;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class GetSleepResultValueClass {

	private Lock lockBaseID = new ReentrantLock();

	public SleepDataHead g_data = new SleepDataHead();

	public class SleepData {
		public int index; // 序号 int
		public long X_Time; // 时间 long (可转为 2015-06-01 23:00:00)；
		public int Y_SleepValue; // 高度值 int
		public int SleepType; // 深浅度标志 int （ 1深度 2浅度 3清醒）
		public int SleepBak1; // 备用字段 字符串
		public int SleepBak2; // 备用字段 字符串

		public SleepData(int index, long X_Time, int Y_SleepValue,
				int SleepType, int SleepBak1, int SleepBak2) {
			this.index = index;
			this.X_Time = X_Time;
			this.Y_SleepValue = Y_SleepValue;
			this.SleepType = SleepType;
			this.SleepBak1 = SleepBak1;
			this.SleepBak2 = SleepBak2;
		}
	}

	public class SleepDataHead{
		public long XStart; // 起始时间 long （可转为 2015-06-02 08:00)
		public long XStop; // 结束时间;long (可转为 2015-06-02 08:00)
		public int YMax; // Y轴最大量化值 int ;
		public long InSleepTime; // 入睡时刻 long;
		public long OutSleepTime; // 醒来时刻 long;
		public int TotalSleepTime; // 睡眠时长 int;（单位分钟）
		public int Deep_Sleep; // 深度睡眠总时长（单位分钟） int
		public int Shallow_Sleep; // 浅度睡眠总时长（单位分钟）int
		public int AwakeTime_Sleep; // 清醒时长（单位分钟）int
		public int OnBed; // 在床时长（单位分钟）int
		public int ToSleep; // 入睡速度（单位分钟）intkeyi
		public int AwakeCount; // 清醒次数 int
		public int AwakeNoGetUpCount; // 赖床时间 int
		public long GoToBedTime; // 上床时间 long (可转为 2015-06-01 23:00)；
		public long GetUpTime; // 起床时间 long (可转为 2015-06-01 23:00)；
		public int ListLength; // 每日数据集条数
		public int SleepBak1; // 备用字段 字符串
		public int SleepBak2; // 备用字段 字符串

		public List<SleepData> m_pValue = new ArrayList<SleepData>();

	}

	public void ProcBuff(byte[] buff) {
		lockBaseID.lock();

		int iCurrIndex = 0;

		long iDatalen = 0;
		long iSumNum = 0;

		// ///////////////////每日数据解析
		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.XStart = bytesToInt(temp);
		}

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.XStop = bytesToInt(temp);
		}

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.YMax = (int) bytesToInt(temp);
		}

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.InSleepTime = bytesToInt(temp);
		}

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.OutSleepTime = bytesToInt(temp);
		}

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.TotalSleepTime = (int) bytesToInt(temp);
		}

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.Deep_Sleep = (int) bytesToInt(temp);
		}

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.Shallow_Sleep = (int) bytesToInt(temp);
		}

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.AwakeTime_Sleep = (int) bytesToInt(temp);
		}

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.OnBed = (int) bytesToInt(temp);
		}
		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.ToSleep = (int) bytesToInt(temp);
		}
		;

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.AwakeCount = (int) bytesToInt(temp);
		}
		;
		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.AwakeNoGetUpCount = (int) bytesToInt(temp);
		}
		;
		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.GoToBedTime = bytesToInt(temp);
		}
		;

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.GetUpTime = bytesToInt(temp);
		}
		; // 上床时间 long (可转为 2015-06-01 23:00)；

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.ListLength = (int) bytesToInt(temp);
		}
		; // 上床时间 long (可转为 2015-06-01 23:00)；

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.SleepBak1 = (int) bytesToInt(temp);
		}
		;

		for (int i = 0; i < 1; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];
			g_data.SleepBak2 = (int) bytesToInt(temp);
		}
		;

		int index; // 序号 int
		long X_Time; // 时间 long (可转为 2015-06-01 23:00:00)；
		int Y_SleepValue; // 高度值 int
		int SleepType; // 深浅度标志 int （ 1深度 2浅度 3清醒）
		int SleepBak1; // 备用字段 字符串
		int SleepBak2; // 备用字段 字符串

		for (int i = 0; i < g_data.ListLength; i++) {
			byte[] temp = new byte[4];
			temp[0] = buff[iCurrIndex++];
			temp[1] = buff[iCurrIndex++];
			temp[2] = buff[iCurrIndex++];
			temp[3] = buff[iCurrIndex++];

			index = (int) bytesToInt(temp);

			byte[] temp2 = new byte[4];
			temp2[0] = buff[iCurrIndex++];
			temp2[1] = buff[iCurrIndex++];
			temp2[2] = buff[iCurrIndex++];
			temp2[3] = buff[iCurrIndex++];

			X_Time = bytesToInt(temp2);

			byte[] temp3 = new byte[4];
			temp3[0] = buff[iCurrIndex++];
			temp3[1] = buff[iCurrIndex++];
			temp3[2] = buff[iCurrIndex++];
			temp3[3] = buff[iCurrIndex++];

			Y_SleepValue = (int) bytesToInt(temp3);

			byte[] temp4 = new byte[4];
			temp4[0] = buff[iCurrIndex++];
			temp4[1] = buff[iCurrIndex++];
			temp4[2] = buff[iCurrIndex++];
			temp4[3] = buff[iCurrIndex++];

			SleepType = (int) bytesToInt(temp4);

			byte[] temp5 = new byte[4];
			temp5[0] = buff[iCurrIndex++];
			temp5[1] = buff[iCurrIndex++];
			temp5[2] = buff[iCurrIndex++];
			temp5[3] = buff[iCurrIndex++];

			SleepBak1 = (int) bytesToInt(temp5);

			byte[] temp6 = new byte[4];
			temp6[0] = buff[iCurrIndex++];
			temp6[1] = buff[iCurrIndex++];
			temp6[2] = buff[iCurrIndex++];
			temp6[3] = buff[iCurrIndex++];

			SleepBak2 = (int) bytesToInt(temp6);

			g_data.m_pValue.add(new SleepData(index, X_Time, Y_SleepValue,
					SleepType, SleepBak1, SleepBak2));

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
