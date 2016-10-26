package com.yzm.sleep.background;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.model.JudgeObject;
import com.yzm.sleep.model.SleepDistributionInfo;
import com.yzm.sleep.utils.DateOperaUtil;
import com.yzm.sleep.utils.PreManager;

/**
 * 计算分析结果，需传入两个参数，一个是当前应用的上下文，一个是统计的日期，格式为yyyy-MM-dd
 * @author Administrator
 *
 */
public class JudgFuction {	
	private long sleeptime = 0;
	private long uptime = 0;
	private float sleepAcce = 0;
	private float upAcce = 0;
	/**手机活动及睡眠时间分布数据*/
	public ArrayList<SleepDistributionInfo> dists = new ArrayList<SleepDistributionInfo>();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private boolean haveSleep = true;												//当天有没有睡觉
	private List<SummaryResult> resultList = new ArrayList<SummaryResult>();	//记录静置时间段数据
	private List<DiagramData> diagramList = new ArrayList<DiagramData>();	//记录画图所需的每半小时数据
	private Context context;
	private String fault_date;
	private String record_sate;
	private String fault_state;
	
	/**
	 * 执行判断函数，从本地保存的错误状态执行判断函数，无需指定开始时间和结束时间，因为可以通过错误状态从本地种读取出来
	 * @param context
	 * @param sysdate
	 * @throws Exception
	 */
	public void judge(Context context,String sysdate) throws Exception {
		fault_state = PreManager.instance().getFaultState(context);
		fault_date = PreManager.instance().getFaultDate(context);
		if(TextUtils.isEmpty(this.fault_date)) {
			this.fault_date = sysdate;
		}
		JudgeObject judgeObject = DataUtils.getJudge(fault_state, fault_date, context);
		if(judgeObject == null) {
			return;
		}
		judgeBody(context,sysdate,judgeObject.getJudgeStarttime(),judgeObject.getJudgeEndtime());
	}

	/**
	 * 通过限定开始时间和结束时间执行判断函数,不需要
	 * @param context
	 * @param sysdate 格式yyyy-MM-dd
	 * @param starttime 单位毫秒
	 * @param endtime	单位毫秒
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void judgeBody(Context context,String sysdate,long starttime,long endtime) throws Exception {
		this.context = context;
		this.fault_date = PreManager.instance().getFaultDate(context);
		if(TextUtils.isEmpty(this.fault_date)) {
			this.fault_date = sysdate;
		}
		record_sate = DataUtils.getRecordState(context, fault_date);
		if(TextUtils.isEmpty(record_sate) || !"3".equals(record_sate)) {
			return;
		}
		PreManager.instance().saveAnalysisState(context.getApplicationContext(), "2");
		
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate dataOperate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPDATA);
		MytabOperate timeOperate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		//这里可以插入测试数据
		
		//清楚3天前所有原始数据
		SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd");
		Date deleteDate = daySdf.parse(fault_date);
		DataUtils.deleteHistoricalData(context, deleteDate, 3);
		
		List<SleepData> dataList = (List<SleepData>) dataOperate.query(
				new String[]{MyTabList.TableSleepData.TIME.getCloumn(),
							 MyTabList.TableSleepData.DATE.getCloumn(),
							 MyTabList.TableSleepData.X.getCloumn(),
							 MyTabList.TableSleepData.Y.getCloumn(),
							 MyTabList.TableSleepData.Z.getCloumn()
							}, 
				MyTabList.TableSleepData.DATE.getCloumn()+" = ? and "+
						MyTabList.TableSleepData.TIME.getCloumn()+" >= ? and "+
						MyTabList.TableSleepData.TIME.getCloumn()+" <= ?", 
				new String[]{fault_date,
								starttime+"",
								endtime+""
							},
				MyTabList.TableSleepData.TIME.getCloumn());
		if(dataList.size() < 2) {
			closeOut();
			throw new Exception("没有当天的记录数据");
		}else {
			resultList.clear();
			diagramList.clear();
			haveSleep = true;
			sleeptime = 0;
			uptime = 0;
			alalize(dataList,timeOperate);
		}			
		SleepResult result = new SleepResult();
		result.setDate(fault_date);
		result.setStarttime(DataUtils.dealData((int)(SleepInfo.SET_STARTTIME/60)) + ":" + DataUtils.dealData((int)(SleepInfo.SET_STARTTIME % 60)));
		result.setEndtime(DataUtils.dealData((int)(SleepInfo.SET_ENDTIME/60)) + ":" + DataUtils.dealData((int)(SleepInfo.SET_ENDTIME % 60)));
		result.setHaveSleep(haveSleep);
		if(!haveSleep) {	//如果统计之后是没有睡觉，那么sleeptime和uptime都是0
			saveInfoList(result);
			if(sleeptime == 0 && uptime == 0){   //如果没有睡觉，除了保存时间外，还是要推理出long型的时间
				SoftDayData data = new SoftDayData();
				data.setSleepTime(result.getSleeptime());
				data.setGetUpTime(result.getUptime());
				data = DateOperaUtil.comperaDate(data);
				result.setSleeptimelong(data.getSleepTimeLong());
				result.setUptimelong(data.getGetUpTimeLong());
			}
			saveData(result,timeOperate);
			dataOperate.close();
			timeOperate.close();
			closeOut();
			return;
		}
		result.setSleeptimelong(String.valueOf(sleeptime));
		result.setUptimelong(String.valueOf(uptime));
		result.setSleeptime(sdf.format(new Date(sleeptime)).substring(11,16).replace("-", ":"));
		result.setUptime(sdf.format(new Date(uptime)).substring(11,16).replace("-", ":"));
		result.setOrgsleeptime(result.getSleeptime());
		result.setOrguptime(result.getUptime());
		
		saveInfoList(result);
		//计算美容觉
		long healthSummary = getHealthLength(result);
		//计算睡眠长度
		long sleeplength = getSleepLength(result);
		result.setSleepLength(sleeplength + "");
		result.setHealthSleep(healthSummary + "");
		saveData(result,timeOperate);
		
		dataOperate.close();
		timeOperate.close();
		closeOut();
	}
	
	private void closeOut() {
		PreManager.instance().saveFaultState(context, "0");
		PreManager.instance().saveFaultDate(context, "");
		PreManager.instance().saveDownTime(context, System.currentTimeMillis()+"");
		PreManager.instance().saveRestartTime(context, "0");
		PreManager.instance().saveAnalysisState(context, "1");
		DataUtils.saveRecordState(context, fault_date, "4");
		onEvent(this.context.getApplicationContext(), SleepInfo.UMENG_COUNT_ID);
	}
	
	public static void onEvent(Context context, String id){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("test_counts", "1");
	    MobclickAgent.onEvent(context, id, map);
	}
	
	/**
	 * 记录曲线图数据
	 */
	private void saveInfoList(SleepResult result) {
		for(SummaryResult sr : resultList) {
			DiagramData data = new DiagramData();
			data.setDate(fault_date);
			data.setTime(sr.getStarttime());
			data.setAcce(sr.getStartacce());
			
			DiagramData endData = new DiagramData();
			endData.setDate(fault_date);
			endData.setTime(sr.getEndtime());
			endData.setAcce(sr.getEndacce());
			diagramList.add(data);
			diagramList.add(endData);
			
			if(sleeptime == 0 || uptime == 0) {
				
			}else {
				if(result.getSleeptime().equals(sr.getStarttime().substring(11,16).replace("-", ":"))) {
					sleepAcce = sr.getStartacce();
				}
				if(result.getUptime().equals(sr.getEndtime().substring(11,16).replace("-", ":"))) {
					upAcce = sr.getEndacce();
				}	
			}
		}
		
		Collections.sort(diagramList);
		dists.clear();
//		float maxAcce = 0;
		for(DiagramData data : diagramList) {
			SleepDistributionInfo info  = new SleepDistributionInfo();
			info.setTime(data.getTime().substring(11,16).replace("-", ":"));
			info.setAccelerate_value(data.getAcce());
//			if(data.getAcce() > maxAcce) {
//				maxAcce = data.getAcce();
//			}
			dists.add(info);
		}
//		result.setMaxacce(maxAcce);
		result.setSleep_acce(sleepAcce);
		result.setGetup_acce(upAcce);
		result.setInfoList(dists);
	}
	/**
	 * 计算睡眠时间长度
	 * @param result
	 * @return
	 */
	public long getSleepLength(SleepResult result) {
		long sleeplength = 0;
		String sleepMinutes[] = result.getSleeptime().split(":");
		long sleepMinute = Long.valueOf(sleepMinutes[0]) * 60 + Long.valueOf(sleepMinutes[1]);
		String upMinutes[] = result.getUptime().split(":");
		long upMinute = Long.valueOf(upMinutes[0]) * 60 + Long.valueOf(upMinutes[1]);
		if(upMinute >= sleepMinute) {
			sleeplength = upMinute - sleepMinute;
		}else {
			sleeplength = 24*60 - sleepMinute + upMinute;
		}
		return sleeplength;
	}
	
	/**
	 * 计算美容觉时间
	 * @return
	 */
	public long getHealthLength(SleepResult result) {
		long summary = 0;
		String sleepMinutes[] = result.getSleeptime().split(":");
		long sleepMinute = Long.valueOf(sleepMinutes[0]) * 60 + Long.valueOf(sleepMinutes[1]);
		String upMinutes[] = result.getUptime().split(":");
		long upMinute = Long.valueOf(upMinutes[0]) * 60 + Long.valueOf(upMinutes[1]);
		long healthStart = 0;   //通过美容时间与入睡时间比较之后得到两者之间的交集
		long healthEnd = 0;
		if(sleepMinute <= upMinute) {
			if(sleepMinute > SleepInfo.HELTH_UP && upMinute < SleepInfo.HELTH_SLEEP) {
				summary = 0;
				return summary;
			}else if(sleepMinute < SleepInfo.HELTH_SLEEP && sleepMinute > SleepInfo.HELTH_UP && upMinute > SleepInfo.HELTH_SLEEP) {
				healthStart = SleepInfo.HELTH_SLEEP;
				healthEnd = upMinute;
				summary = getLength(healthStart,healthEnd,result);
				return summary;
			}else if(sleepMinute >= SleepInfo.HELTH_SLEEP) {
				healthStart = sleepMinute;
				healthEnd = upMinute;
				summary = getLength(healthStart,healthEnd,result);
				return summary;
			}else if(sleepMinute <= SleepInfo.HELTH_UP && upMinute <= SleepInfo.HELTH_UP) {
				healthStart = sleepMinute;
				healthEnd = upMinute;
				summary = getLength(healthStart,healthEnd,result);
				return summary;
			}else if(sleepMinute <= SleepInfo.HELTH_UP && upMinute <= SleepInfo.HELTH_SLEEP) {
				healthStart = sleepMinute;
				healthEnd = SleepInfo.HELTH_UP;
				summary = getLength(healthStart,healthEnd,result);
				return summary;
			}else if(sleepMinute <= SleepInfo.HELTH_UP && upMinute > SleepInfo.HELTH_SLEEP) {		//特殊
				summary = getLength(healthStart,SleepInfo.HELTH_UP,result);
				summary = summary + getLength(SleepInfo.HELTH_SLEEP,upMinute,result);
				return summary;
			}
		}else {
			if(sleepMinute <= SleepInfo.HELTH_SLEEP && upMinute <= SleepInfo.HELTH_UP && sleepMinute > SleepInfo.HELTH_UP) {
				healthStart = SleepInfo.HELTH_SLEEP;
				healthEnd = upMinute;
				summary = getLength(healthStart,healthEnd,result);
				return summary;
			}else if(sleepMinute <= SleepInfo.HELTH_SLEEP && upMinute > SleepInfo.HELTH_UP && sleepMinute > SleepInfo.HELTH_UP) {
				healthStart = SleepInfo.HELTH_SLEEP;
				healthEnd =  SleepInfo.HELTH_UP;
				summary = getLength(healthStart,healthEnd,result);
				return summary;
			}else if(sleepMinute >= SleepInfo.HELTH_SLEEP && upMinute <= SleepInfo.HELTH_UP) {
				healthStart = sleepMinute;
				healthEnd = upMinute;
				summary = getLength(healthStart,healthEnd,result);
				return summary;
			}else if(sleepMinute >= SleepInfo.HELTH_SLEEP && upMinute > SleepInfo.HELTH_UP && upMinute < SleepInfo.HELTH_SLEEP) {
				healthStart = sleepMinute;
				healthEnd = SleepInfo.HELTH_UP;
				summary = getLength(healthStart,healthEnd,result);
				return summary;
			}else if(sleepMinute <= SleepInfo.HELTH_UP && upMinute < SleepInfo.HELTH_UP) {	//特殊
				summary = getLength(sleepMinute,SleepInfo.HELTH_UP,result);
				summary = summary + getLength(SleepInfo.HELTH_SLEEP,upMinute,result);
				return summary;
			}else if(sleepMinute >= SleepInfo.HELTH_SLEEP && upMinute >= SleepInfo.HELTH_SLEEP) {
				summary = getLength(sleepMinute,SleepInfo.HELTH_UP,result);
				summary = summary + getLength(SleepInfo.HELTH_SLEEP,upMinute,result);
				return summary;
			}
		}
		return summary;
	}
	/**
	 * 获取指定时间段内，静置时间的长度
	 * @param healthStart
	 * @param healthEnd
	 * @return
	 */
	private long getLength(long healthStart,long healthEnd,SleepResult result) {
		long summary = 0;
		List<Map<String,String>> dataList = new ArrayList<Map<String,String>>();
		boolean isUpdate = false;
		if("1".equals(result.getUpdate())) {
			isUpdate = true;
		}
		if(isUpdate) {
			Map<String,String> map = new HashMap<String,String>();
			String[] sleepTime = result.getSleeptime().split(":");
			String[] upTime = result.getUptime().split(":");
			long sleep = Long.valueOf(sleepTime[0])*60+Long.valueOf(sleepTime[1]);
			long up = Long.valueOf(upTime[0])*60+Long.valueOf(upTime[1]);
			map.put("start", sleep+"");
			map.put("end", up+"");
			dataList.add(map);
		}else {
			for(SummaryResult sleepResult : resultList) {
				int startHour = Integer.valueOf(sleepResult.getStarttime().substring(11,13));
				int startMinute = Integer.valueOf(sleepResult.getStarttime().substring(14,16));
				int endHour = Integer.valueOf(sleepResult.getEndtime().substring(11,13));
				int endMinute = Integer.valueOf(sleepResult.getEndtime().substring(14,16));
				int start = startHour*60 + startMinute;
				int end = endHour*60 + endMinute;
				Map<String,String> map = new HashMap<String,String>();
				map.put("start", start+"");
				map.put("end", end+"");
				dataList.add(map);
			}
		}
		if(healthStart <= healthEnd) {
			for(Map<String,String> map : dataList) {
				int start = Integer.valueOf(map.get("start"));
				int end = Integer.valueOf(map.get("end"));
				if(start <= end ) {
					if(end <= healthStart) {
						summary = summary + 0;
					}else if(start < healthStart && end > healthStart && end <= healthEnd) {
						summary = summary + end - healthStart;
					}else if(start >= healthStart && end <= healthEnd) {
						summary = summary + end - start;
					}else if(start >= healthStart && end > healthEnd && start < healthEnd) {
						summary = summary + healthEnd - start;
					}else if(start >= healthEnd) {
						summary = summary + 0;
					}
				}else {
					if(start <= healthStart) {
						summary = summary + healthEnd - healthStart;
					}else if (end >= healthEnd) {
						summary = summary + 0;
					}else if (start > healthStart && start < healthEnd && end <= healthStart) {
						summary = summary + healthEnd - start;
					}else if (start > healthStart && start < healthEnd && end > healthStart) {
						summary = summary + healthEnd - start + end - healthStart;
					}else if (start >= healthEnd && end <= healthStart) {
						summary = summary + 0;
					}else if (start >= healthEnd && end > healthStart && end < healthEnd) {
						summary = end - healthStart;
					}
				}
			}
		}else {
			for(Map<String,String> map : dataList) {
				int start = Integer.valueOf(map.get("start"));
				int end = Integer.valueOf(map.get("end"));
				if(start <= end ) {
				if(start >= healthEnd && end <= healthStart) {
					summary = summary + 0;
				}else if(start >= healthEnd && end > healthStart && start < healthStart) {
					summary = summary + end - healthStart;
				}else if(start <= healthEnd && end < healthStart && end >= healthEnd) {
					summary = summary + healthEnd - start;
				}else if(end <= healthEnd) {
					summary = summary + end - start;
				}else if(start <= healthEnd && end > healthStart) {
					summary = summary + end - healthStart + healthEnd - start;
				}else if(start >= healthStart) {
					summary = summary + end - start;
				}
			}else {
				if(start <= healthEnd) {
					summary = summary + healthEnd - start + end + 24 * 60 - healthStart;
				}else if(start > healthEnd && start <= healthStart && end <= healthEnd) {
					summary = summary + end + 24 * 60 - healthStart;
				}else if(start > healthEnd && start <= healthStart && end > healthEnd) {
					summary = summary +24 * 60 - healthStart + healthEnd;
				}else if(start >= healthStart && end < healthEnd) {
					summary = summary +24*60 - start + end;
				}else if(start >= healthStart && end >= healthEnd && end <= healthStart) {
					summary = summary + 24*60 -start + healthEnd;
				}else if(end > healthStart) {
					summary = summary + 24*60 -start + healthEnd + end - healthStart;
				}
			}
			}
		}
		return summary;
	}
	
	/**
	 * 保存最终数据结果，以及分析结果
	 * @param result
	 */
	private void saveData(SleepResult result,MytabOperate operate) {
		StringBuffer sb = new StringBuffer();
		if(result.getInfoList().size() > 0) {
			sb.append("{\"diagramdata\":[");
			for(SleepDistributionInfo info : result.getInfoList()) {
				sb.append("[{").append("\"time\":").append("\""+info.getTime()+"\"").append("},{\"acce\":").append("\""+info.getAccelerate_value()+"\"").append("}],");
			}
			sb.replace(sb.length()-1, sb.length(), "");//去掉最后一个逗号
			sb.append("]}");
		}
		ContentValues cv = new ContentValues();
		cv.put("sleeptime", ("".equals(result.getSleeptime()) || null == result.getSleeptime()) ? "" : result.getSleeptime());
		cv.put("uptime", ("".equals(result.getUptime()) || null == result.getUptime()) ? "" : result.getUptime());
		cv.put("orgsleeptime", ("".equals(result.getSleeptime()) || null == result.getSleeptime()) ? "" : result.getSleeptime());
		cv.put("orguptime", ("".equals(result.getUptime()) || null == result.getUptime()) ? "" : result.getUptime());
		
		cv.put("sleeptimelong", TextUtils.isEmpty(result.getSleeptimelong()) ? "" : result.getSleeptimelong());
		cv.put("uptimelong", TextUtils.isEmpty(result.getUptimelong()) ? "" : result.getUptimelong());
		
		cv.put("sleeplength", ("".equals(result.getSleepLength()) || null == result.getSleepLength()) ? "" : result.getSleepLength());
		cv.put("healthlength", ("".equals(result.getHealthSleep()) || null == result.getHealthSleep()) ? "" : result.getHealthSleep());
		cv.put("diagramdata", "".equals(sb.toString()) || null == sb.toString() ? "":sb.toString());	
		cv.put("sleepacce", result.getSleep_acce()+"");
		cv.put("upacce", result.getGetup_acce()+"");
		operate.update(cv, "date = ?", new String[]{fault_date});
	}
	
	private void alalize(List<SleepData> dataList,MytabOperate operate) throws Exception {
		//分析当天睡眠数据，数据从表sleep_data中提取
		float x1;							//分析数据获得静置时间段
		float y1;
		float z1;
		float x2;
		float y2;
		float z2;
		long te = Long.valueOf(dataList.get(1).getTime());
		long ts = Long.valueOf(dataList.get(0).getTime());
		long t1 = ts;
		long t2 = te;
		long tBegin = Long.valueOf(dataList.get(0).getTime());
		float endAcce = 0.0f;
		float startAcce = 0.0f;
		int count = 0;
		int dataCount = 0;	//为计算加速度平均值
		float summaryAcce = 0;
//		float maxAcce = 0;
		for(int i = 0;i<dataList.size() - 1;i++) {
			x1 = Float.valueOf(dataList.get(i).getX());
			y1 = Float.valueOf(dataList.get(i).getY());
			z1 = Float.valueOf(dataList.get(i).getZ());
			t1 = Long.valueOf(dataList.get(i).getTime());
			
			x2 = Float.valueOf(dataList.get(i+1).getX());
			y2 = Float.valueOf(dataList.get(i+1).getY());
			z2 = Float.valueOf(dataList.get(i+1).getZ());
			t2 = Long.valueOf(dataList.get(i+1).getTime());
			
			dataCount++;
//			summaryAcce = summaryAcce+ getAcce(x1,y1,z1);
//			if(getAcce(x1,y1,z1) > maxAcce) {
//				maxAcce = getAcce(x1,y1,z1);	
//			}
			float x = Math.abs(x2 - x1);
			float y = Math.abs(y2 - y1);
			float z = Math.abs(z2 - z1);
			
			float summary = x+y+z;
			summaryAcce = summaryAcce + summary;
			
			if((Long.valueOf(dataList.get(i).getTime())- tBegin >= 30*60*1000)) {	//半个小时记录一次
				DiagramData data = new DiagramData();
				data.setTime(sdf.format(new Date(Long.valueOf(dataList.get(i).getTime()))));
				data.setDate(fault_date);
				data.setAcce(summaryAcce / dataCount);
				summaryAcce = 0;
//				data.setAcce(maxAcce);
//				maxAcce = 0;
				dataCount = 0;
				diagramList.add(data);
				tBegin = Long.valueOf(dataList.get(i).getTime());
			}
			
			if((Math.abs(x2 - x1) <= SleepInfo.ALERATE_DATA) 
					&& (Math.abs(y2 - y1) <= SleepInfo.ALERATE_DATA) 
					&& (Math.abs(z2 - z1) <= SleepInfo.ALERATE_DATA)
//					&& Math.abs(t2 - t1) < 2*60*1000
					) {
				te = Long.valueOf(dataList.get(i+1).getTime());
				endAcce = getAcce(x2 - x1,y2 - y1,z2 - z1);
				if((i == dataList.size() - 2)  
						&&((te - ts) >= 60*60*1000)) {
					SummaryResult summaryResult = new SummaryResult();
					summaryResult.setStarttime(sdf.format(new Date(ts)));
					summaryResult.setEndtime(sdf.format(new Date(te)));
					summaryResult.setDate(fault_date);
					summaryResult.setEndacce(endAcce);
					summaryResult.setStartacce(startAcce);
					resultList.add(summaryResult);
				}
			}else{
				count++;
				if(Math.abs(te - ts) >= 60*60*1000) {
					SummaryResult summaryResult = new SummaryResult();
					summaryResult.setStarttime(sdf.format(new Date(ts)));
					summaryResult.setEndtime(sdf.format(new Date(te)));
					summaryResult.setDate(fault_date);
					summaryResult.setEndacce(endAcce);
					summaryResult.setStartacce(startAcce);
					resultList.add(summaryResult);
				}
				ts = Long.valueOf(dataList.get(i+1).getTime());
				startAcce = getAcce(x2 - x1,y2 - y1,z2 - z1);
			}
		}
		
		int n = resultList.size();
		if(n == 0) {
			haveSleep = false;
			return;	
		}else if(n >= 1) {
			//加速度置零操作
			float zero = PreManager.instance().getZeroAcce(this.context.getApplicationContext());
			if(zero == 0 || zero != 0) {
				float summaryGravity = 0.0f;
				int acceCount = 0;
				for(SummaryResult summaryResult : resultList) {
					String starttime = sdf.parse(summaryResult.getStarttime()).getTime()+"";
					String endtime = sdf.parse(summaryResult.getEndtime()).getTime()+"";
					for(SleepData sleepData : dataList) {
						if(DataUtils.compareString(sleepData.getTime(), starttime) && 
								DataUtils.compareString(endtime, sleepData.getTime()) && 
								acceCount <= 100) {
							float x = Float.valueOf(sleepData.getX());
							float y = Float.valueOf(sleepData.getY());
							float z = Float.valueOf(sleepData.getZ());
							summaryGravity += Math.abs(Math.sqrt(x*x + y*y + z*z));
							acceCount++;
						}
					}
					break;
				}
				float avgacce = summaryGravity / acceCount;
				PreManager.instance().saveZeroAcce(this.context.getApplicationContext(), avgacce);
			}
			//获取入睡点
			long startTime = 0;
			long endTime = 0;
			for(int i = 0 ;i < resultList.size();i++) {
				startTime = sdf.parse(resultList.get(i).getStarttime()).getTime();
				endTime = sdf.parse(resultList.get(i).getEndtime()).getTime();
				if((endTime - startTime) >= 2*60*60*1000) {
					sleeptime = startTime;
					break;
				}
			}
			if(sleeptime == 0) {
				haveSleep = false;
				return;
			}
			//获取起床点
			if(n == 1) {
				uptime = endTime;
			}else {
				boolean islastResult = true;
				for(int i = 0 ;i < resultList.size() - 1;i++) {
					long sTime = sdf.parse(resultList.get(i + 1).getStarttime()).getTime();
					long eTime = sdf.parse(resultList.get(i).getEndtime()).getTime();
					if(!((sTime - eTime) <= 3*60*1000)) {
						islastResult = false;
						break;
					}
				}
				if(islastResult) {
					uptime = sdf.parse(resultList.get(resultList.size() - 1).getEndtime()).getTime();
					return;
				}
				
				long kUptime = 0;
				for(int i = 0 ;i < resultList.size() - 1;i++) {
					long sTime = sdf.parse(resultList.get(i + 1).getStarttime()).getTime();
					long eTime = sdf.parse(resultList.get(i).getEndtime()).getTime();
					if(((sTime - eTime) > 3*60*1000) && ((sTime - eTime) <= 10*60*1000) && (sTime > getUptime(operate))) {
						kUptime = eTime;
						break;
					}
				}
				
				long mUptime = 0;
				for(int i = 0 ;i < resultList.size() - 1;i++) {
					long sTime = sdf.parse(resultList.get(i + 1).getStarttime()).getTime();
					long eTime = sdf.parse(resultList.get(i).getEndtime()).getTime();
					if(((sTime - eTime) > 10*60*1000)) {
						mUptime = eTime;
						break;
					}
				}
				
				if((kUptime <= mUptime) && kUptime != 0) {
					uptime = kUptime;
				}else if((mUptime < kUptime) && mUptime != 0) {
					uptime = mUptime;
				}
				
				if(uptime == 0) {
					uptime = sdf.parse(resultList.get(resultList.size() - 1).getEndtime()).getTime();
				}
			}
			
		}
	}
	
	/**
	 * 获取某一点加速度值
	 * @return
	 */
	private float getAcce(float x,float y,float z) {
//		return (float) Math.abs(Math.sqrt(x*x + y*y + z*z));
		return Math.abs(x) + Math.abs(y) + Math.abs(z);
	}

	//获得设置的起床时间,毫秒级
	private long getUptime(MytabOperate operate) throws Exception {
		StringBuffer sb = new StringBuffer();
		long minute = getSetUptime(operate,fault_date);
		int hour = (int) (minute/60);
		int minuteOfHoure = (int) (minute % 60);
		sb.append(fault_date).append("-").append(hour + "").append("-").append(DataUtils.dealData(minuteOfHoure)).append("-").append("00");
		Date date = sdf.parse(sb.toString());
		if(DataUtils.getAllerateStartTime() > DataUtils.getAllerateStopTime()) {
			return date.getTime() + 24 * 60 * 60 *1000;
		}
		return date.getTime();
	}
	
	//获得设置的入睡时间，毫秒级
	private long getStarttime(MytabOperate operate) throws Exception {
		StringBuffer sb = new StringBuffer();
		long minute = getSetStarttime(operate,fault_date);
		int hour = (int) (minute/60);
		int minuteOfHoure = (int) (minute % 60);
		sb.append(fault_date).append(hour + "").append("-").append(DataUtils.dealData(minuteOfHoure)).append("-").append("00");
		Date date = sdf.parse(sb.toString());
		return date.getTime();
	}
	
	//从数据库获得设置的起床时间，当天的分钟数
	private long getSetUptime(MytabOperate operate,String update) throws Exception{
		String uptime = operate.query("endtime", "date = ?", new String[]{update});
		if("".equals(uptime)) {
			throw new Exception("没有当天的设置时间   getSetUptime");	
		}else {
			return Long.valueOf(uptime);
		}
	}
	
	//从数据库获得设置的起床时间，当天的分钟数
	private long getSetStarttime(MytabOperate operate,String startdate) throws Exception{
		String starttime = operate.query("starttime", "date = ?", new String[]{startdate});
		if("".equals(starttime)) {
			throw new Exception("没有当天的设置时间");	
		}else {
			return Long.valueOf(starttime);
		}
	}
}
