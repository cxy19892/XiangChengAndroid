package com.yzm.sleep.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.xpillowjni.XpillowInterface;
import com.yzm.sleep.Constant;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.render.GetModelsValueClass;
import com.yzm.sleep.render.GetSleepResultValueClass;
import com.yzm.sleep.render.GetModelsValueClass.ModelsValues;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;

public class BluetoothDataFormatUtil {

	/**
	 * 蓝牙数据转换成结果集  
	 * @param b
	 * @param count
	 * @return
	 */
	public static List<ModelsValues> format1(byte[] bluedata){
		if(bluedata == null)
			return null;
		XpillowInterface xInterface = new XpillowInterface();
		byte[] para = PillowDataOpera.readPillowProfile();
		if(para != null)
			xInterface.SetPara(para, para.length);
		else 
			xInterface.SetPara(Constant.profile, Constant.profile.length);
		byte[] arr = xInterface.GetModels(bluedata, bluedata.length);
		GetModelsValueClass valueClass = new GetModelsValueClass();
		valueClass.ProcBuff(arr);
		List<ModelsValues> listModels = valueClass.m_pValue;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		for (ModelsValues modelsValues : listModels) {
//			try {
//				LogUtil.i("huang", "mtime = " + sdf.format(new Date(modelsValues.mtime * 1000)));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				LogUtil.i("huang", "mlTotalTime = " + sdf.format(new Date(modelsValues.mlTotalTime * 1000)));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			LogUtil.i("huang", "-------------------------------------");
//		}
		
		return listModels;
	}
	
	/**
	 *  蓝牙数据转换成实体类
	 * @param b
	 * @param count
	 * @return
	 */
	public static SleepDataHead format2(ModelsValues values, long count){
		byte[] arr_day = new XpillowInterface().GetSleepResult(values.mBuf, count);
		GetSleepResultValueClass valueClass2 = new GetSleepResultValueClass();
		valueClass2.ProcBuff(arr_day);
		SleepDataHead dataHead = valueClass2.g_data;
		return dataHead;
	}
	
	/**
	 *  处理每天的model数据
	 * @param b
	 * @param count
	 * @return
	 */
	public static SleepDataHead format3(byte[] values, long count){
		XpillowInterface xInterface = new XpillowInterface();
		byte[] para = PillowDataOpera.readPillowProfile();
		if(para != null)
			xInterface.SetPara(para, para.length);
		else 
			xInterface.SetPara(Constant.profile, Constant.profile.length);
		byte[] arr_day = xInterface.GetSleepResult(values, count);
		GetSleepResultValueClass valueClass2 = new GetSleepResultValueClass();
		valueClass2.ProcBuff(arr_day);
		SleepDataHead dataHead = valueClass2.g_data;
		return dataHead;
	}
}
