package com.yzm.sleep.background;

import java.util.List;

import android.content.Context;

public class AnalyzeData {

	/**
	 * 判断是否睡过一觉，分析日期analyzeDate,分析开始时间starttime,结束时间endtime
	 * @param context
	 * @param analyzeDate
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws Exception
	 */
	public static boolean haveSleep(Context context,String analyzeDate,long starttime,long endtime) throws Exception {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate dataOperate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPDATA);
		@SuppressWarnings("unchecked")
		List<SleepData> dataList = (List<SleepData>) dataOperate.query(new String[]{"time","date","x","y","z"}, "date = ?", new String[]{analyzeDate},"time");
		if(dataList.size() < 2) {
			return false;
		}
		long te = Long.valueOf(dataList.get(1).getTime());
		long ts = Long.valueOf(dataList.get(0).getTime());
		float x1;							//分析数据获得静置时间段
		float y1;
		float z1;
		float x2;
		float y2;
		float z2;
		int count = 0;
		for(int i = 0;i < dataList.size() - 1;i++) {
			long time = Long.valueOf(dataList.get(i + 1).getTime());
			if(time <= endtime && time >= starttime) {
				x1 = Float.valueOf(dataList.get(i).getX());
				y1 = Float.valueOf(dataList.get(i).getY());
				z1 = Float.valueOf(dataList.get(i).getZ());
				
				x2 = Float.valueOf(dataList.get(i+1).getX());
				y2 = Float.valueOf(dataList.get(i+1).getY());
				z2 = Float.valueOf(dataList.get(i+1).getZ());
				
				if((Math.abs(x2 - x1) <= SleepInfo.ALERATE_DATA) && (Math.abs(y2 - y1) <= SleepInfo.ALERATE_DATA) && (Math.abs(z2 - z1) <= SleepInfo.ALERATE_DATA)) {
					te = Long.valueOf(dataList.get(i+1).getTime());
				}else {
					if((te - ts) >= 2*60*60*1000) {
						count ++;
					}
					ts = Long.valueOf(dataList.get(i+1).getTime());
				}
			}else {
				continue;
			}
		}
		if((te - ts) >= 2*60*60*1000) {
			count ++;
		}
		if(count > 0) {
			return true;
		}
		return false;
	}
}
