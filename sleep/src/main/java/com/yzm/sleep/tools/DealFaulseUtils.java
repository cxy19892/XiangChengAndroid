package com.yzm.sleep.tools;

import java.text.ParseException;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;

import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.JudgFuction;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.utils.FaultState;
import com.yzm.sleep.utils.PreManager;

public class DealFaulseUtils {

	private static DealFaulseUtils instance;
	
	private DealFaulseUtils() {
		
	}
	
	public static synchronized DealFaulseUtils getInstance() {
		if(instance == null) {
			instance = new DealFaulseUtils();
		}
		return instance;
	}
	
	public void dealBreak(Context context) throws ParseException {
		long downTime = Long.valueOf(PreManager.instance().getDownTime(context));
		long restartTime = System.currentTimeMillis();
		if((restartTime - downTime) < 0) {
			String date = DataUtils.getRecordDate(new Date(restartTime));
			String record_state = DataUtils.getRecordState(context, date);
			if("3".equals(record_state) || "4".equals(record_state)) {
				DataUtils.saveRecordState(context, date, "4");
			}else {
				DataUtils.saveRecordState(context, date, "4");
				MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
				MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
				ContentValues cv = new ContentValues();
				cv.put(MyTabList.TableDay.DIAGRAMDATA.getCloumn(), "");
				cv.put(MyTabList.TableDay.FILEID.getCloumn(), "");
				cv.put(MyTabList.TableDay.HEALTHLENGTH.getCloumn(), "");
				cv.put(MyTabList.TableDay.ORGSLEEPTIME.getCloumn(), "");
				cv.put(MyTabList.TableDay.ORGUPTIME.getCloumn(), "");
				cv.put(MyTabList.TableDay.SLEEPACCE.getCloumn(), "");
				cv.put(MyTabList.TableDay.SLEEPLENGTH.getCloumn(), "");
				cv.put(MyTabList.TableDay.SLEEPTIME.getCloumn(), "");
				cv.put(MyTabList.TableDay.UPACCE.getCloumn(), "");
				cv.put(MyTabList.TableDay.UPTIME.getCloumn(), "");
				operate.update(cv, MyTabList.TableDay.DATE.getCloumn()+" = ?", new String[]{date});
			}
		}else {
			String downDate = DataUtils.getRecordDate(new Date(downTime));
			long nextT1 = DataUtils.getT1(downDate) + 24*60*60*1000;
			String dealDate;
			if(restartTime > nextT1) {
				dealDate = DataUtils.getRecordDate(new Date(restartTime));	
			}else {
				dealDate = downDate;
			}
			String record_date = DataUtils.getRecordState(context, dealDate);
			if("4".equals(record_date)) {
				return;
			}else {
				long t1 = DataUtils.getT1(dealDate);
				long t2 = DataUtils.getT2(dealDate);
				long t10 = t1 + SleepInfo.BEFORE_SLEEP * 60 * 1000;
				long t20 = t2 - SleepInfo.AFTER_SLEEP * 60 * 1000;
				String setStart = DataUtils.dealData((int)(SleepInfo.SET_STARTTIME / 60)) + 
						":" + DataUtils.dealData((int)(SleepInfo.SET_STARTTIME % 60));
				String setEnd = DataUtils.dealData((int)(SleepInfo.SET_ENDTIME / 60)) + 
						":" + DataUtils.dealData((int)(SleepInfo.SET_ENDTIME % 60));
				String downTimeMode; //标记downTime的类型
				if(downTime < t1) {
					downTimeMode = "a";
				}else if(downTime >= t1 && downTime <= t2) {
					downTimeMode = "b";
				}else {
					downTimeMode = "c";
				}
				
				String restartTimeMode; //标记restartTime的类型
				if(restartTime < t1) {
					restartTimeMode = "a";
				}else if(restartTime >= t1 && restartTime <= t2) {
					restartTimeMode = "b";
				}else {
					restartTimeMode = "c";
				}
				
				if("a".equals(downTimeMode) && "b".equals(restartTimeMode)) {
					if(restartTime >= t1 && restartTime <= (t1 + 3*60*60*1000)) {
						PreManager.instance().saveFaultState(context, FaultState.C.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.deleteData(context, restartTime, dealDate);
						DataUtils.saveRecordState(context, dealDate, "2");
					}else {
						PreManager.instance().saveFaultState(context, FaultState.D.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.newData(t10, restartTime, context,dealDate);
						DataUtils.saveRecordState(context, dealDate, "2");
					}
				}
				if("a".equals(downTimeMode) && "c".equals(restartTimeMode)) {
					PreManager.instance().saveFaultState(context, FaultState.B.getState());
					PreManager.instance().saveFaultDate(context, dealDate);
					PreManager.instance().saveDownTime(context, downTime+"");
					PreManager.instance().saveRestartTime(context, restartTime+"");
					DataUtils.saveRecordState(context, dealDate, "4");
					DataUtils.stritSetTime(context,setStart, setEnd, dealDate);
					PreManager.instance().saveFaultState(context, "0");
					PreManager.instance().saveFaultDate(context, "");
					PreManager.instance().saveDownTime(context, "0");
					PreManager.instance().saveRestartTime(context, "0");
				}
				if("b".equals(downTimeMode) && "c".equals(restartTimeMode)) {
					PreManager.instance().saveFaultState(context, "3");
					PreManager.instance().saveFaultDate(context, dealDate);
					PreManager.instance().saveDownTime(context, downTime+"");
					PreManager.instance().saveRestartTime(context, restartTime+"");
					if(downTime >= t1 && downTime < t10) {
						PreManager.instance().saveFaultState(context, FaultState.E.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.newData(downTime, t20, context, dealDate);
						DataUtils.saveRecordState(context, dealDate, "3");
						try {
//							new JudgFuction().judge(context, dealDate,t1,t20);
							new JudgFuction().judge(context, dealDate);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(downTime >= t10 && downTime <= t20) {
						PreManager.instance().saveFaultState(context, FaultState.F.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.newData(downTime, t20, context, dealDate);
						DataUtils.saveRecordState(context, dealDate, "3");
						try {
//							new JudgFuction().judge(context, dealDate,t1,t20);
							new JudgFuction().judge(context, dealDate);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(downTime > t20 && downTime <= t2) {
						PreManager.instance().saveFaultState(context, FaultState.G.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.newData(downTime, t2, context, dealDate);
						DataUtils.saveRecordState(context, dealDate, "3");
						try {
//							new JudgFuction().judge(context, dealDate,t1,t2);
							new JudgFuction().judge(context, dealDate);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if("b".equals(downTimeMode) && "b".equals(restartTimeMode)) {
					String downMode = "";	//标记down所处的区间
					String restartMode = "";	//标记restart所处的区间
					if(downTime > t1 && downTime < t10) {
						downMode = "a";
					}else if(downTime >= t10 && downTime <= t20) {
						downMode = "b";
					}else if(downTime > t20 && downTime < t2) {
						downMode = "c";
					}
					
					if(restartTime > t1 && restartTime < t10) {
						restartMode = "a";
					}else if(restartTime >= t10 && restartTime <= t20) {
						restartMode = "b";
					}else if(restartTime > t20 && restartTime < t2) {
						restartMode = "c";
					}
					
					if("a".equals(downMode) && "a".equals(restartMode)) {
						PreManager.instance().saveFaultState(context, FaultState.H.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.saveRecordState(context, dealDate, "2");
						DataUtils.newData(downTime, restartTime, context, dealDate);
					}
					if("a".equals(downMode) && "b".endsWith(restartMode)) {
						PreManager.instance().saveFaultState(context, FaultState.I.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.newData(downTime, restartTime, context, dealDate);
						DataUtils.saveRecordState(context, dealDate, "2");
//						if((restartTime - downTime) > 2*60*60*1000) {
//							PreManager.instance().saveFaultState(context, FaultState.I.getState());
//							PreManager.instance().saveFaultDate(context, dealDate);
//							PreManager.instance().saveDownTime(context, downTime+"");
//							PreManager.instance().saveRestartTime(context, restartTime+"");
//							DataUtils.newData(downTime, restartTime, context, dealDate);
//							DataUtils.saveRecordState(context, dealDate, "2");	
//						}else {
//							PreManager.instance().saveFaultState(context, FaultState.J.getState());
//							PreManager.instance().saveFaultDate(context, dealDate);
//							PreManager.instance().saveDownTime(context, downTime+"");
//							PreManager.instance().saveRestartTime(context, restartTime+"");
//							DataUtils.deleteData(context, restartTime, dealDate);
//							DataUtils.saveRecordState(context, dealDate, "2");	
//						}
					}
					if("a".equals(downMode) && "c".equals(restartMode)) {
						PreManager.instance().saveFaultState(context, FaultState.K.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.newData(downTime, t20, context, dealDate);
						DataUtils.saveRecordState(context, dealDate, "3");
						try {
//							new JudgFuction().judge(context, dealDate,t1,t20);
							new JudgFuction().judge(context, dealDate);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if("b".equals(downMode) && "b".equals(restartMode)) {
						PreManager.instance().saveFaultState(context, FaultState.L.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.newData(downTime, restartTime, context, dealDate);
						DataUtils.saveRecordState(context, dealDate, "2");
					}
					if("b".equals(downMode) && "c".equals(restartMode)) {
						PreManager.instance().saveFaultState(context, FaultState.M.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.newData(downTime, restartTime, context, dealDate);
						DataUtils.saveRecordState(context, dealDate, "2");
//						try {
////							new JudgFuction().judge(context, dealDate,t1,t20);
//							new JudgFuction().judge(context, dealDate);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
					}
					if("c".equals(downMode) && "c".equals(restartMode)) {
						PreManager.instance().saveFaultState(context, FaultState.N.getState());
						PreManager.instance().saveFaultDate(context, dealDate);
						PreManager.instance().saveDownTime(context, downTime+"");
						PreManager.instance().saveRestartTime(context, restartTime+"");
						DataUtils.newData(downTime, restartTime, context, dealDate);
						DataUtils.saveRecordState(context, dealDate, "2");
					}
				}
			}
		}
	}
}
