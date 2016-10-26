package com.yzm.sleep.bluetoothBLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AgreementDev {

	// public static ArrayList<tsPacketOfDataLink> m_recvList=new
	// ArrayList<AgreementDev.tsPacketOfDataLink>();

	// private static Lock m_lock ;

	public static class tsPacketOfAppInfo {
		public byte u8PacketNO;
		public byte u8CMD;
		public char u16LEN;
		public byte[] pData;

		@Override
		public String toString() {
			return "tsPacketOfAppInfo [u8PacketNO=" + u8PacketNO + ", u8CMD="
					+ u8CMD + ", u16LEN=" + u16LEN + ", pData="
					+ Arrays.toString(pData) + "]";
		}

	}

	public static class tsPacketOfDataLink {
		public byte u8RecAddr;
		public byte u8SendAddr;
		public tsPacketOfAppInfo pPacketOfAppInfo;
		public byte u8Checksum;

		// add for debug
		@Override
		public String toString() {
			return "tsPacketOfDataLink [u8RecAddr=" + u8RecAddr
					+ ", u8SendAddr=" + u8SendAddr + ", pPacketOfAppInfo="
					+ pPacketOfAppInfo.toString() + ", u8Checksum="
					+ u8Checksum + "]";
		}

	}

	public enum Errors {
		TimeOut, Bit8Error, CheckSumError, AddrError, None
	}

	// static public SerialPort comm = new SerialPort();

	private static List<byte[]> m_recvByteList = new ArrayList<byte[]>();
	private static List<byte[]> m_OnePackList = new ArrayList<byte[]>();

	static Lock m_listLock = new ReentrantLock();
	static Lock m_AddLock = new ReentrantLock();

	public static byte STX = (byte) 0x82;
	public static byte ETX = (byte) 0x83;
	static byte IP_address = 0x00;

	// static int APP_DATA_LEN = 5000;
	static int APP_DATATemp_LEN = 5000;

	static private int UnloadIndex = 0;
	static private int UnloadStatus = 0;
	private static int InputBufferMax = APP_DATATemp_LEN;
	static private int m_iCurrTotalLen = 0;
	static private int m_iCurrOnePackLen = 0;

	static private byte[] InputBuffer = new byte[InputBufferMax];
	static byte[] u8DataPool = new byte[APP_DATATemp_LEN];
	static private byte[] tempBuffTotal = new byte[APP_DATATemp_LEN];
	static private byte[] tempBuffTemp = new byte[APP_DATATemp_LEN];
	static private byte[] tempBuffTurnOn = new byte[APP_DATATemp_LEN];

	public static tsPacketOfAppInfo toData = null;

	static boolean m_bRecord = true;

	static public void init() {

		for (int i = 0; i < 5000; i++) {

			InputBuffer[i] = 0;

			u8DataPool[i] = 0;

			tempBuffTotal[i] = 0;

			tempBuffTemp[i] = 0;

			tempBuffTurnOn[i] = 0;
		}

		m_AddLock.lock();
		m_recvByteList.clear();
		m_AddLock.unlock();

		m_listLock.lock();
		m_OnePackList.clear();
		m_listLock.unlock();

		m_iCurrTotalLen = 0;

		m_iCurrOnePackLen = 0;

		m_bRecord = true;

	}

	static public void AddRecvData(byte[] rstbyte) {
		m_AddLock.lock();

		m_recvByteList.add(rstbyte);

		m_AddLock.unlock();
	}

	// /循环处理当前接收的list
	static public Errors Proc() {
		Errors Ereturn = null;
		m_listLock.lock();

		if (m_OnePackList.size() > 0) {

			byte[] recv = m_OnePackList.remove(0);

			Ereturn = UnloadPhyToDataLink(recv);
		}
		m_listLock.unlock();

		return Ereturn;
	}

	public static void ProcData() {

		// byte[]
		// recv={(byte)0x82,0x00,0x00,0x00,0x00,0x02,0x02,0x00,0x11,0x00,0x11,0x26,(byte)0x83,(byte)0x82,0x00,0x00,0x00,0x00,0x04,0x01,0x00,0x11,0x00,0x16,(byte)
		// 0x83,(byte)
		// 0x82,0x00,0x00,0x00,0x00,0x06,0x02,0x00,0x00,0x00,0x00,0x08,(byte)0x83,(byte)0x82,0x00,0x00,0x00,0x00,0x06,0x1E,0x00,0x00,0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x00,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x00,0x0F,0x10,0x11,0x12,0x13,0x14,0x15,0x00,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x40,0x1D,0x57,(byte)0x83,(byte)0x82,0x40,0x00,0x00,0x00,0x06,0x01,0x00,0x7F,0x00,0x06,(byte)0x83};//m_recvByteList.remove(0);

		// byte [] recv ={(byte)
		// 0x82,0x00,0x00,0x00,0x00,0x02,0x02,0x00,0x11,0x00,0x11,0x26,(byte)
		// 0x83,(byte)
		// 0x82,0x00,0x00,0x00,0x00,0x04,0x01,0x00,0x11,0x00,0x16,(byte)
		// 0x83,(byte)
		// 0x82,0x00,0x00,0x00,0x00,0x06,0x02,0x00,0x00,0x00,0x00,0x08,(byte)
		// 0x83,(byte)
		// 0x82,0x10,0x00,0x00,0x00,0x06,0x34,0x00,0x00,0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x00,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x00,0x0F,0x10,0x11,0x12,0x13,0x14,0x15,0x00,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x00,0x1D,0x1E,0x1F,0x20,0x21,0x22,0x23,0x00,0x24,0x25,0x26,0x27,0x28,0x29,0x2A,0x00,0x2B,0x2C,0x2D,0x2E,0x2F,0x30,0x31,0x00,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x00,0x39,0x3A,0x3B,0x3C,0x3D,0x3E,0x3F,0x00,0x40,0x41,0x42,0x43,0x44,0x45,0x46,0x00,0x47,0x48,0x49,0x4A,0x4B,0x4C,0x4D,0x00,0x4E,0x4F,0x50,0x51,0x52,0x53,0x54,0x00,0x55,0x56,0x57,0x58,0x59,0x5A,0x5B,0x00,0x5C,0x5D,0x5E,0x5F,0x60,0x61,0x62,0x00,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x00,0x6A,0x6B,0x6C,0x6D,0x6E,0x6F,0x70,0x00,0x71,0x72,0x73,0x74,0x75,0x76,0x77,0x00,0x78,0x79,0x7A,0x7B,0x7C,0x7D,0x7E,0x7E,0x7F,0x00,0x01,0x02,0x03,0x04,0x05,0x7F,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x7F,0x0D,0x0E,0x0F,0x10,0x11,0x12,0x13,0x7F,0x14,0x15,0x16,0x17,0x18,0x19,0x1A,0x7F,0x1B,0x1C,0x1D,0x1E,0x1F,0x20,0x21,0x7F,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x7F,0x29,0x2A,0x2B,0x2C,0x2D,0x2E,0x2F,0x7C,0x30,0x31,0x32,0x33,0x28,(byte)
		// 0x83,(byte)
		// 0x82,0x40,0x00,0x00,0x00,0x06,0x01,0x00,0x7F,0x00,0x06,(byte) 0x83};

		if (m_recvByteList.size() > 0) {
			m_AddLock.lock();
			if (m_recvByteList.size() > 0) {
				byte[] recv = m_recvByteList.remove(0);

				System.arraycopy(recv, 0, tempBuffTotal, m_iCurrTotalLen,
						recv.length);

				m_iCurrTotalLen += recv.length;

				for (int j = m_iCurrOnePackLen; j < m_iCurrTotalLen; j++) {
					if (tempBuffTotal[j] == STX) {
						m_bRecord = true;
						tempBuffTemp[m_iCurrOnePackLen] = tempBuffTotal[j];

						m_iCurrOnePackLen++;
					} else if (tempBuffTotal[j] != STX && m_bRecord) {

						if (tempBuffTotal[j] == ETX) {
							tempBuffTemp[m_iCurrOnePackLen] = tempBuffTotal[j];

							m_iCurrTotalLen = m_iCurrTotalLen
									- (m_iCurrOnePackLen + 1);
							System.arraycopy(tempBuffTotal,
									m_iCurrOnePackLen + 1, tempBuffTurnOn, 0,
									m_iCurrTotalLen);

							System.arraycopy(tempBuffTurnOn, 0, tempBuffTotal,
									0, m_iCurrTotalLen);

							byte[] newpack = new byte[m_iCurrOnePackLen + 1];

							System.arraycopy(tempBuffTemp, 0, newpack, 0,
									m_iCurrOnePackLen + 1);
							m_OnePackList.add(newpack);
							j = -1;
							m_iCurrOnePackLen = 0;
							m_bRecord = false;

						} else {
							tempBuffTemp[m_iCurrOnePackLen] = tempBuffTotal[j];

							m_iCurrOnePackLen++;
						}

					}
				}
			}
			m_AddLock.unlock();

		} else {
			return;
		}

	}

	// 数据收，当收到STX时开始存，当收到ETX时收结束
	private static Errors UnloadPhyToDataLink(byte[] u8InputData) {
		for (int i = 0; i < u8InputData.length; i++) {
			switch (UnloadStatus) {
			// 状态0 + 找到帧头 --》状态1
			case 0:
				if (u8InputData[i] == STX) {
					UnloadStatus = 1;
				} else {
					UnloadIndex = 0;
				}
				break;
			// 状态1 +找到帧尾 --》状态0
			case 1:
				if (u8InputData[i] != ETX) {
					InputBuffer[UnloadIndex] = u8InputData[i];
					UnloadIndex++;
				} else {
					UnloadStatus = 0;
					// ִ�����ݽ���
					byte[] buf = new byte[UnloadIndex];

					System.arraycopy(InputBuffer, 0, buf, 0, UnloadIndex);

					Errors error = UnloadDataLinkToApp(buf);
					// �������
					UnloadIndex = 0;
				}
				break;
			}
		}
		return Errors.None;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 把正常的byte[]转换成结构体
	static private Errors UnloadApp(tsPacketOfDataLink sPacketOfDataLink) {
		// Student
		// a=StructCopyer.ByteToStructure<Student>(sPacketOfDataLink.pPacketOfAppInfo.pData);
		// m_lock.lock();
		// m_recvList.add(sPacketOfDataLink);
		// m_lock.unlock();
		toData = sPacketOfDataLink.pPacketOfAppInfo;
		return Errors.None;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 协议组包流程

	static public byte[] LoadApp(byte RecAddr, byte PacketNO, byte CMD,
			tsPacketOfAppInfo pData) {
		byte[] AppData = pData.pData;
		byte[] SendData = new byte[7 + AppData.length];
		SendData[0] = RecAddr;
		SendData[1] = IP_address;
		SendData[2] = PacketNO;
		SendData[3] = CMD;
		short buffsShort = (short) AppData.length;

		byte[] buf = getBytes(buffsShort, false);

		SendData[4] = buf[0];
		SendData[5] = buf[1];

		for (int i = 0; i < AppData.length; i++) {
			SendData[i + 6] = AppData[i];
		}
		for (int i = 0; i < SendData.length - 1; i++) {
			SendData[SendData.length - 1] += SendData[i];
		}
		return LoadAppToDataLink(SendData);
	}

	// 把结构体转换成正常的byte[]
	static private byte[] LoadAppToDataLink(byte[] AppData) {
		int LinkDataLength = UpLinkTransform(AppData, u8DataPool);
		return LoadDataLinkToPhy(u8DataPool, LinkDataLength);
	}

	// 把最高位永远不是0的的byte[]加上STX与ETX
	static private byte[] LoadDataLinkToPhy(byte[] LinkData, int LinkDataLength) {
		byte[] DataToSend = new byte[LinkDataLength + 2];
		DataToSend[0] = STX;
		DataToSend[DataToSend.length - 1] = ETX;

		System.arraycopy(LinkData, 0, DataToSend, 1, LinkDataLength);

		return DataToSend;
	}

	// 把正常的byte[]转换成最高位永远不为0的byte[]
	static private int UpLinkTransform(byte[] u8InputData, byte[] u8OutputData) {
		/*****************************************
		 * Function Name:UpLinkTransform Discription :Package the input
		 * data,rules below: 1.Package every 7 Bytes in sequence.Creat a package
		 * header before them.So every normal package is made up of 8 Bytes with
		 * a header at the first position. 2.The header's bit-7 to bit-1 is made
		 * up of the bit-8 of every Packed data.The bit-7 of header eaquals the
		 * last data of this package.And the header's bit-8 is always low.
		 * 3.Each packaed data = data & 0x7f. 4.In this way,each data's bit-8 is
		 * always low.if we recieve a high-bit-8 data but it's not the STX or
		 * ETX. It's must be a wrong data. 5.if the number of a package's data
		 * is below 7,It's also ok to package. Function input: u8InputData[]:
		 * the unpacked input data; u32Len: the length of unpacked data;
		 * u8OutputData[]: the packed output data; Function output: the length
		 * of packed data;
		 ******************************************/
		int u32I = 0, u32J = 0;
		int u32DataIndex = 0;
		byte u32Tmp = 0;
		int u32Len = (int) u8InputData.length;

		for (; u32DataIndex < u32Len; u32DataIndex++) {

			if (u32I++ > 6) {
				u32I = 0;
				u8OutputData[u32J * 8] = u32Tmp;
				u32J++;
				u32Tmp = 0;
				u32DataIndex--;
			} else {
				u32Tmp >>= 1;
				if ((u8InputData[u32DataIndex] & 0x80) != 0) {
					u32Tmp |= 0x40;
				} else {
				}
				u8OutputData[u32J * 8 + u32I] = (byte) (u8InputData[u32DataIndex] & 0x7f);
			}

		}// End for

		if (u32I != 0) {
			u8OutputData[u32J * 8] = u32Tmp;
		}

		return (u32J * 8) + u32I + 1;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////��Э������////////////////////////

	// 把正常的byte[]转换成结构体
	static private Errors UnloadDataLinkToApp(byte[] u8InputData) {
		/*****************************************
		 * Function Name:UnloadDataLinkToApp Discription :transfer the data from
		 * Link layer to Application layer Function input: u8InputData[]: the
		 * receive data Function output:
		 ******************************************/
		DownLinkTransform(u8InputData, u8DataPool);
		tsPacketOfDataLink Rec = new tsPacketOfDataLink();
		Rec.u8RecAddr = u8DataPool[0];
		Rec.u8SendAddr = u8DataPool[1];
		Rec.pPacketOfAppInfo = new tsPacketOfAppInfo();
		Rec.pPacketOfAppInfo.u8PacketNO = u8DataPool[2];
		Rec.pPacketOfAppInfo.u8CMD = u8DataPool[3];

		byte Temp[] = new byte[2];

		for (int i = 0; i < 2; i++) {
			Temp[i] = u8DataPool[4 + i];

		}

		Rec.pPacketOfAppInfo.u16LEN = (char) bytesToInt(Temp); // BitConverter.ToUInt16(u8DataPool,
																// 4);

		Rec.pPacketOfAppInfo.pData = new byte[Rec.pPacketOfAppInfo.u16LEN];
		System.arraycopy(u8DataPool, 6, Rec.pPacketOfAppInfo.pData, 0,
				Rec.pPacketOfAppInfo.u16LEN);
		Rec.u8Checksum = u8DataPool[6 + Rec.pPacketOfAppInfo.u16LEN];

		// add for debug
		// System.out.println(Rec.toString());
		byte Checksum = 0;
		for (int i = 0; i < 6 + Rec.pPacketOfAppInfo.u16LEN; i++) {
			Checksum += u8DataPool[i];
		}
		if (Checksum == Rec.u8Checksum) {
			return UnloadApp(Rec);
		} else
			return Errors.CheckSumError;
	}

	// 把最高位永远不为0的byte[]转换成正常的byte[]
	static private void DownLinkTransform(byte[] u8InputData,
			byte[] u8OutputData) {
		/*****************************************
		 * Function Name:DownLinkTransform Discription :unPackage the input data
		 * Function input: u8InputData[]: the packed input data; u8OutputData[]:
		 * the unpacked output data; Function output:
		 ******************************************/
		int u32I, u32Block;
		int u32DataIndex = 0;
		byte u32J, u32Remainder;
		int u32Len = (int) u8InputData.length;
		u32Block = u32Len >> 3; // u32Block = u32Len / 8
		u32Remainder = (byte) (u32Len % 8); // u32Remainder = u32Len % 8

		for (u32I = 0; u32I < u32Block; u32I++) {
			for (u32J = 0; u32J < 8; u32J++) {
				if (u32J >= 7) {
					continue;
				}
				if (((u8InputData[(u32I << 3) + 0]) & (0x01 << u32J)) != 0) {
					u8OutputData[u32DataIndex] = (byte) (u8InputData[(u32I << 3)
							+ u32J + 1] | 0x80);
				} else {
					u8OutputData[u32DataIndex] = u8InputData[(u32I << 3) + u32J
							+ 1];
				}
				u32DataIndex++;
			}
		}

		if (u32Remainder == 0) {
			return;
		}

		for (u32J = 0; u32J < (u32Remainder - 1); u32J++) {
			if (((u8InputData[(u32Block << 3) + 0]) & (0x01 << (u32J + 8 - u32Remainder))) != 0) {
				u8OutputData[u32DataIndex] = (byte) (u8InputData[(u32Block << 3)
						+ u32J + 1] | 0x80);
			} else {
				u8OutputData[u32DataIndex] = u8InputData[(u32Block << 3) + u32J
						+ 1];
			}
			u32DataIndex++;
		}// End for

		return;
	}

	// ///////////////////////////////////////////////////////////////////����ת��///////////////////////////////////////////////////

	public static int bytesToInt(byte[] bytes) {
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

		int Rst = Integer.parseInt(hex, 16);
		return Rst;
	}

	public static long bytesToSignLong(byte[] buff) {
		int size = buff.length;
		int isNega = ((buff[size - 1] & 0x80) >>> 7);

		byte[] buff2 = new byte[buff.length];

		long value = 0;

		if (isNega > 0) {
			for (int i = 0; i < buff.length; i++)
				buff2[i] = (byte) ~buff[i];
			value = 0 - bytesToLong(buff2) - 1;

		} else {
			value = bytesToLong(buff);
		}
		return value;
	}

	public static long bytesToLong(byte[] bytes) {
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

	public short getShort(byte[] buf, boolean bBigEnding) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 2) {
			throw new IllegalArgumentException("byte array size > 2 !");
		}
		short r = 0;
		if (bBigEnding) {
			for (int i = 0; i < buf.length; i++) {
				r <<= 8;
				r |= (buf[i] & 0x00ff);
			}
		} else {
			for (int i = buf.length - 1; i >= 0; i--) {
				r <<= 8;
				r |= (buf[i] & 0x00ff);
			}
		}

		return r;
	}

	public static byte[] getBytes(short s, boolean bBigEnding) {
		byte[] buf = new byte[2];
		if (bBigEnding)
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (s & 0x00ff);
				s >>= 8;
			}
		else
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (s & 0x00ff);
				s >>= 8;
			}
		return buf;
	}

}
