package com.yzm.sleep.tools;

import java.util.Date;

import android.content.Context;

import com.yzm.sleep.background.AnalyzeData;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.JudgFuction;
import com.yzm.sleep.utils.FaultState;
import com.yzm.sleep.utils.PreManager;

public class DealSetTimeUtils {
	private static DealSetTimeUtils instance;
	
	private DealSetTimeUtils() {
		
	}
	
	public static synchronized DealSetTimeUtils getInstance() {
		if(instance == null) {
			instance = new DealSetTimeUtils();
		}
		return instance;
	}
	
	/**
	 * 修改预设时间的机制
	 * @param context
	 * @param date 当前周期
	 * @param setupTime 设置发生时间，单位为毫秒
	 * @param oldt1	单位为分钟
	 * @param oldt2	单位为分钟
	 * @param newt1 单位为分钟
	 * @param newt2 单位为分钟
	 * @throws Exception 
	 */
	public void setTime(Context context,String date,long setupTime,long oldt1,long oldt2,long newt1,long newt2) throws Exception {
		PreManager.instance().saveSetupState(context, "2");
		long currentDayMills = setupTime - DataUtils.getSS(new Date());
		String oldRangeMode = "";
		String newRangeMode = "";
		if(DataUtils.isInRange(currentDayMills, oldt1*60*1000, oldt2*60*1000)) {
			if(AnalyzeData.haveSleep(context, date, DataUtils.getT1(date, oldt1,oldt2), 
									setupTime)) {
				oldRangeMode = "a";
			}else {
				oldRangeMode = "b";
			}
		}else {
			oldRangeMode = "c";
		}
		
		if(DataUtils.isInRange(currentDayMills, newt1*60*1000, newt2*60*1000)) {
			newRangeMode = "a";
		}else {
			newRangeMode = "b";
		}
		
		if("a".equals(oldRangeMode) && "a".equals(newRangeMode)) {
			PreManager.instance().saveFaultState(context, FaultState.O.getState());
			PreManager.instance().saveFaultDate(context, date);
			PreManager.instance().saveRestartTime(context, setupTime+"");
			DataUtils.saveRecordState(context, date, "3");
//			new JudgFuction().judge(context, date,DataUtils.getT1(date, oldt1,oldt2),setupTime);
			new JudgFuction().judge(context, date);
		}
		if("a".equals(oldRangeMode) && "b".equals(newRangeMode)) {
			PreManager.instance().saveFaultState(context, FaultState.P.getState());
			PreManager.instance().saveFaultDate(context, date);
			PreManager.instance().saveRestartTime(context, setupTime+"");
			DataUtils.saveRecordState(context, date, "3");
//			new JudgFuction().judge(context, date,DataUtils.getT1(date, oldt1, oldt2),setupTime);
			new JudgFuction().judge(context, date);
		}
		if("b".equals(oldRangeMode) && "a".equals(newRangeMode)) {
			PreManager.instance().saveFaultState(context, FaultState.Q.getState());
			PreManager.instance().saveFaultDate(context, date);
			PreManager.instance().saveRestartTime(context, setupTime+"");
			DataUtils.deleteData(context, setupTime, date);
			DataUtils.saveRecordState(context, date, "2");
		}
		if("b".equals(oldRangeMode) && "b".equals(newRangeMode)) {
			PreManager.instance().saveFaultState(context, FaultState.R.getState());
			PreManager.instance().saveFaultDate(context, date);
			PreManager.instance().saveRestartTime(context, setupTime+"");
			DataUtils.saveRecordState(context, date, "1");
			DataUtils.deleteData(context, setupTime, date);
		}
		if("c".equals(oldRangeMode) && "a".equals(newRangeMode)) {
			PreManager.instance().saveFaultState(context, FaultState.S.getState());
			PreManager.instance().saveFaultDate(context, date);
			PreManager.instance().saveRestartTime(context, setupTime+"");
			DataUtils.deleteData(context, setupTime, date);
			DataUtils.saveRecordState(context, date, "2");
		}
		if("c".equals(oldRangeMode) && "b".equals(newRangeMode)) {
			PreManager.instance().saveFaultState(context, FaultState.T.getState());
			PreManager.instance().saveFaultDate(context, date);
			PreManager.instance().saveRestartTime(context, setupTime+"");
			DataUtils.saveRecordState(context, date, "1");
		}
		
		PreManager.instance().saveSetupState(context, "1");
	}
	
}
