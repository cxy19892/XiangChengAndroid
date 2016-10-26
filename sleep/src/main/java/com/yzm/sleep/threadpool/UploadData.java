package com.yzm.sleep.threadpool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yzm.sleep.Constant;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.SleepResult;
import com.yzm.sleep.model.SleepDistributionInfo;
import com.yzm.sleep.utils.LogUtil;

/**
 * 上传数据的方法封装出来的工具类
 * @author Administrator
 *
 */
public class UploadData {

	/**
	 *  上传未上传的睡眠睡眠数据
	 * @param context
	 */
	public static void uploadSleepData(Context context){
		List<SleepResult> datas = getUnUploadDaySleepData(context);
		ThreadPoolManager tpManager = ThreadPoolManager.initstance();
		List<Runnable> list = new LinkedList<Runnable>();
		for (SleepResult result : datas) {
			SoftDataUpLoadTask task = new SoftDataUpLoadTask(result, context);
			list.add(task);
		}
		tpManager.setOnThreadProgress(new ThreadProgress() {

			@Override
			public void threadStart(int poolCunt) {
			}

			@Override
			public void threadEnd() {
			}
		});
		if (list.size() > 0)
			tpManager.addThreadToPool(list).start();
	}
	
	/**
	 * 获取所有未上传的软件睡眠数据
	 * 
	 * @return
	 */
	private static List<SleepResult> getUnUploadDaySleepData(Context context) {
		List<SleepResult> srList = new ArrayList<SleepResult>();
		try {
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			SQLiteDatabase db = helper.getWritableDatabase();
			Cursor result = db.rawQuery("select * from " + MyTabList.SLEEPTIME
					+ " where isupload != '1' and ispunch = '1' order by date(date)", null);

			while (result.moveToNext()) {
				SleepResult sleepResult = new SleepResult();
				sleepResult.setDate((result.getString(result
						.getColumnIndex("date"))) == null ? "" : result
						.getString(result.getColumnIndex("date")));
				String starttime = (result.getString(result
						.getColumnIndex("starttime"))) == null ? "" : result
						.getString(result.getColumnIndex("starttime"));
				if(starttime != null && !TextUtils.isEmpty(starttime)){
					sleepResult
					.setStarttime(DataUtils.dealData((int) (Float
							.valueOf(starttime) / 60))
							+ ":"
							+ DataUtils.dealData((int) (Float
									.valueOf(starttime) % 60)));
				}else{
					sleepResult.setStarttime(starttime);
				}
				
				String endtime = (result.getString(result
						.getColumnIndex("endtime"))) == null ? "" : result
						.getString(result.getColumnIndex("endtime"));
				if(endtime != null && !TextUtils.isEmpty(endtime)){
					sleepResult
					.setEndtime(DataUtils.dealData((int) (Float
							.valueOf(endtime) / 60))
							+ ":"
							+ DataUtils.dealData((int) (Float
									.valueOf(endtime) % 60)));
				}else{
					sleepResult
					.setEndtime(endtime);
				}
				
				sleepResult.setSleeptime((result.getString(result
						.getColumnIndex("sleeptime"))) == null ? "" : result
						.getString(result.getColumnIndex("sleeptime")));
				sleepResult.setUptime((result.getString(result
						.getColumnIndex("uptime"))) == null ? "" : result
						.getString(result.getColumnIndex("uptime")));
				sleepResult.setOrgsleeptime((result.getString(result
						.getColumnIndex("orgsleeptime"))) == null ? "" : result
						.getString(result.getColumnIndex("orgsleeptime")));
				sleepResult.setOrguptime((result.getString(result
						.getColumnIndex("orguptime"))) == null ? "" : result
						.getString(result.getColumnIndex("orguptime")));
				sleepResult.setSleepLength((result.getString(result
						.getColumnIndex("sleeplength"))) == null ? "0" : result
						.getString(result.getColumnIndex("sleeplength")));
				sleepResult.setHealthSleep((result.getString(result
						.getColumnIndex("healthlength"))) == null ? "0"
						: result.getString(result
								.getColumnIndex("healthlength")));
				
				
				sleepResult
						.setSleep_acce(Float.valueOf(((result.getString(result
								.getColumnIndex("sleepacce"))) == null || ""
								.equals((result.getString(result
										.getColumnIndex("sleepacce"))))) ?

						"0" : result.getString(result
								.getColumnIndex("sleepacce"))));
				sleepResult
						.setGetup_acce(Float.valueOf(((result.getString(result
								.getColumnIndex("upacce"))) == null || ""
								.equals((result.getString(result
										.getColumnIndex("sleepacce"))))) ?

						"0" : result.getString(result.getColumnIndex("upacce"))));
				sleepResult.setUpdate(result.getString(result
						.getColumnIndex("ischange")));
				sleepResult.setUpload(result.getString(result
						.getColumnIndex("isupload")));
				sleepResult.setRecordState(result.getString(result
						.getColumnIndex("record_state")));
				String diagram = result.getString(result
						.getColumnIndex("diagramdata"));
				String diagramdata = diagram == null || "".equals(diagram) ? ""
						: diagram;
				ArrayList<SleepDistributionInfo> infoList = new ArrayList<SleepDistributionInfo>();
				if (diagramdata != null && !"".equals(diagramdata)) {
					JSONObject jo = new JSONObject(diagramdata);
					JSONArray ja = jo.getJSONArray("diagramdata");
					for (int i = 0; i < ja.length(); i++) {
						JSONArray jy = ja.getJSONArray(i);
						JSONObject jtime = jy.getJSONObject(0);
						JSONObject jacce = jy.getJSONObject(1);
						SleepDistributionInfo info = new SleepDistributionInfo();
						info.setTime(jtime.getString("time"));
						String acce = jacce.getString("acce");
						if(acce != null && !TextUtils.isEmpty(acce))
							info.setAccelerate_value(Float.valueOf(acce));
						else
							info.setAccelerate_value(0);
						infoList.add(info);
					}
				}
				sleepResult.setInfoList(infoList);

				srList.add(sleepResult);
			}
			result.close();
		} catch (Exception e) {
		}

		return srList;
	}
	
	/**
	 * 只上传指定日期的当天睡眠数据
	 * @param context
	 * @param date
	 */
	public static void uploadDaySleepData(final Context context, String date){
		try {
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			SQLiteDatabase db = helper.getWritableDatabase();
			Cursor result = db.rawQuery("select * from " + MyTabList.SLEEPTIME + " where date = ?", new String[]{date});
			if(result.moveToFirst()){
				final SleepResult sleepResult = new SleepResult();
				sleepResult.setDate((result.getString(result
						.getColumnIndex("date"))) == null ? "" : result
						.getString(result.getColumnIndex("date")));
				String starttime = (result.getString(result
						.getColumnIndex("starttime"))) == null ? "" : result
						.getString(result.getColumnIndex("starttime"));
				if(starttime != null && !TextUtils.isEmpty(starttime)){
					sleepResult
					.setStarttime(DataUtils.dealData((int) (Float
							.valueOf(starttime) / 60))
							+ ":"
							+ DataUtils.dealData((int) (Float
									.valueOf(starttime) % 60)));
				}else{
					sleepResult.setStarttime(starttime);
				}
				String endtime = (result.getString(result
						.getColumnIndex("endtime"))) == null ? "" : result
						.getString(result.getColumnIndex("endtime"));
				
				if(endtime != null && !TextUtils.isEmpty(endtime)){
					sleepResult
					.setEndtime(DataUtils.dealData((int) (Float
							.valueOf(endtime) / 60))
							+ ":"
							+ DataUtils.dealData((int) (Float
									.valueOf(endtime) % 60)));
				}else{
					sleepResult
					.setEndtime(endtime);
				}
				sleepResult.setSleeptime((result.getString(result
						.getColumnIndex("sleeptime"))) == null ? "" : result
						.getString(result.getColumnIndex("sleeptime")));
				sleepResult.setUptime((result.getString(result
						.getColumnIndex("uptime"))) == null ? "" : result
						.getString(result.getColumnIndex("uptime")));
				sleepResult.setOrgsleeptime((result.getString(result
						.getColumnIndex("orgsleeptime"))) == null ? "" : result
						.getString(result.getColumnIndex("orgsleeptime")));
				sleepResult.setOrguptime((result.getString(result
						.getColumnIndex("orguptime"))) == null ? "" : result
						.getString(result.getColumnIndex("orguptime")));
				
				sleepResult.setSleeptimelong((result.getString(result
						.getColumnIndex("sleeptimelong"))) == null ? "" : result
						.getString(result.getColumnIndex("sleeptimelong")));
				sleepResult.setUptimelong((result.getString(result
						.getColumnIndex("uptimelong"))) == null ? "" : result
						.getString(result.getColumnIndex("uptimelong")));
				
				sleepResult.setSleepLength((result.getString(result
						.getColumnIndex("sleeplength"))) == null ? "0" : result
						.getString(result.getColumnIndex("sleeplength")));
				sleepResult.setHealthSleep((result.getString(result
						.getColumnIndex("healthlength"))) == null ? "0"
						: result.getString(result
								.getColumnIndex("healthlength")));
				sleepResult
						.setSleep_acce(Float.valueOf(((result.getString(result
								.getColumnIndex("sleepacce"))) == null || ""
								.equals((result.getString(result
										.getColumnIndex("sleepacce"))))) ?

						"0" : result.getString(result
								.getColumnIndex("sleepacce"))));
				sleepResult
						.setGetup_acce(Float.valueOf(((result.getString(result
								.getColumnIndex("upacce"))) == null || ""
								.equals((result.getString(result
										.getColumnIndex("sleepacce"))))) ?

						"0" : result.getString(result.getColumnIndex("upacce"))));
				sleepResult.setUpdate(result.getString(result
						.getColumnIndex("ischange")));
				sleepResult.setUpload(result.getString(result
						.getColumnIndex("isupload")));
				sleepResult.setRecordState(result.getString(result
						.getColumnIndex("record_state")));
				String diagram = result.getString(result
						.getColumnIndex("diagramdata"));
				String diagramdata = diagram == null || "".equals(diagram) ? ""
						: diagram;
				ArrayList<SleepDistributionInfo> infoList = new ArrayList<SleepDistributionInfo>();
				if (diagramdata != null && !"".equals(diagramdata)) {
					JSONObject jo = new JSONObject(diagramdata);
					JSONArray ja = jo.getJSONArray("diagramdata");
					for (int i = 0; i < ja.length(); i++) {
						JSONArray jy = ja.getJSONArray(i);
						JSONObject jtime = jy.getJSONObject(0);
						JSONObject jacce = jy.getJSONObject(1);
						SleepDistributionInfo info = new SleepDistributionInfo();
						info.setTime(jtime.getString("time"));
						String acce = jacce.getString("acce");
						if(acce != null && !TextUtils.isEmpty(acce))
							info.setAccelerate_value(Float.valueOf(acce));
						else
							info.setAccelerate_value(0);
						infoList.add(info);
					}
				}
				sleepResult.setInfoList(infoList);
				
				ThreadPoolManager tpManager = ThreadPoolManager.initstance();
				List<Runnable> list = new LinkedList<Runnable>();
				SoftDataUpLoadTask task = new SoftDataUpLoadTask(sleepResult, context);
				list.add(task);
				tpManager.setOnThreadProgress(new ThreadProgress() {

					@Override
					public void threadStart(int poolCunt) {
					}

					@Override
					public void threadEnd() {
					}
				});
				tpManager.addThreadToPool(list).start();
			}
			
		} catch (Exception e) {
		}
	}
}
