package com.yzm.sleep.background;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.utils.LogUtil;

public class SignInDBOperation {

	private static SignInDBOperation operate;
	
	private Context mContext;
	private MyDatabaseHelper dataHelper;
	
	private SignInDBOperation(Context context){
		this.mContext = context;
		dataHelper = MyDatabaseHelper.getInstance(mContext);
	}
	
	public static SignInDBOperation initDB(Context context){
		if(operate == null){
			synchronized (SignInDBOperation.class) {
				if(operate == null){
					operate = new SignInDBOperation(context);
				}
			}
		}
		return operate;
	}
	
	/**
	 * 插入签到的数据
	 */
	public void insertSignInData(SignInData data){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put("date", data.getDate());
			cv.put("sign_id", data.getSignInId());
			cv.put("go_bed_time", data.getGoBedTime());
			cv.put("try_sleep_time", data.getTrySleepTime());
			cv.put("how_long_sleep_time", data.getHowLongSleepTime());
			cv.put("wake_count", data.getWakeCount());
			cv.put("how_long_wake_time", data.getHowLongWakeTime());
			cv.put("wake_up_time", data.getWakeUpTime());
			cv.put("wake_early_time", data.getWakeEarlyTime());
			cv.put("out_bed_time", data.getOutBedTime());
			cv.put("soft_or_orange", data.getSoftOrOrange());
			cv.put("orange_data_upload", data.getUpload());
			cv.put("deep_sleep", data.getDeepsleep());
			cv.put("shallow_sleep", data.getShallowsleep());
			cv.put("result", data.getResult());
			db.insert(MyTabList.NEW_SLEEPTIME, null, cv);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		
	}
	
	/**
	 * 查询单独某一天的数据
	 * @param date  日期
	 * @param type  0是软件，1是硬件
	 * @return
	 */
	public SignInData querySignInData(String date, String type){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		SignInData data = new SignInData();
		data.setDate(date);
		Cursor c = db.rawQuery("select * from " + MyTabList.NEW_SLEEPTIME + " where date = ? and soft_or_orange = ?", new String[]{date, type});
		try {
			if(c.moveToFirst()) {
				data.setSignInId(c.getString(c.getColumnIndex("sign_id")));
				data.setGoBedTime(c.getString(c.getColumnIndex("go_bed_time")));
				data.setTrySleepTime(c.getString(c.getColumnIndex("try_sleep_time")));
				data.setHowLongSleepTime(c.getInt(c.getColumnIndex("how_long_sleep_time")));
				data.setWakeCount(c.getInt(c.getColumnIndex("wake_count")));
				data.setHowLongWakeTime(c.getInt(c.getColumnIndex("how_long_wake_time")));
				data.setWakeUpTime(c.getString(c.getColumnIndex("wake_up_time")));
				data.setWakeEarlyTime(c.getInt(c.getColumnIndex("wake_early_time")));
				data.setOutBedTime(c.getString(c.getColumnIndex("out_bed_time")));
				data.setSoftOrOrange(c.getString(c.getColumnIndex("soft_or_orange")));
				data.setUpload(c.getInt(c.getColumnIndex("orange_data_upload")));
				data.setDeepsleep(c.getInt(c.getColumnIndex("deep_sleep")));
				data.setShallowsleep(c.getInt(c.getColumnIndex("shallow_sleep")));
				data.setResult(c.getString(c.getColumnIndex("result")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
			db.close();
		}
		return data;
	}
	
	/**
	 *  获取多个日期的签到数据
	 * @param dates
	 * @param type  0是软件，1是硬件
	 * @return
	 */
	public List<SignInData> querySignInDataAll(String[] dates, String type){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		List<SignInData> list = new ArrayList<SignInData>();
		if(dates.length == 0) {
			db.close();
			return list;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < dates.length; i++) {
			if(i == 0)
				sb.append("(date = ?");
			else
				sb.append(" or date = ?");
		}
		sb.append(") and soft_or_orange = ?");
		List<String> arrList = new ArrayList<String>();
		for (String str : dates) {
			arrList.add(str);
		}
		arrList.add(type);
		Cursor c = db.query(MyTabList.NEW_SLEEPTIME, null, sb.toString(), arrList.toArray(new String[arrList.size()]), null, null, "date asc");
		try {
			while (c.moveToNext()) {
				SignInData data = new SignInData();
				data.setDate(c.getString(c.getColumnIndex("date")));
				data.setSignInId(c.getString(c.getColumnIndex("sign_id")));
				data.setGoBedTime(c.getString(c.getColumnIndex("go_bed_time")));
				data.setTrySleepTime(c.getString(c.getColumnIndex("try_sleep_time")));
				data.setHowLongSleepTime(c.getInt(c.getColumnIndex("how_long_sleep_time")));
				data.setWakeCount(c.getInt(c.getColumnIndex("wake_count")));
				data.setHowLongWakeTime(c.getInt(c.getColumnIndex("how_long_wake_time")));
				data.setWakeUpTime(c.getString(c.getColumnIndex("wake_up_time")));
				data.setWakeEarlyTime(c.getInt(c.getColumnIndex("wake_early_time")));
				data.setOutBedTime(c.getString(c.getColumnIndex("out_bed_time")));
				data.setSoftOrOrange(c.getString(c.getColumnIndex("soft_or_orange")));
				data.setUpload(c.getInt(c.getColumnIndex("orange_data_upload")));
				data.setDeepsleep(c.getInt(c.getColumnIndex("deep_sleep")));
				data.setShallowsleep(c.getInt(c.getColumnIndex("shallow_sleep")));
				data.setResult(c.getString(c.getColumnIndex("result")));
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
			db.close();
		}
		return list;
	}
	
	/**
	 * 修改某天的签到数据
	 * @param data
	 * @param type 0是软件，1是硬件
	 */
	public synchronized void updateSignInData(SignInData data, String type){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put("date", data.getDate());
			cv.put("sign_id", data.getSignInId());
			cv.put("go_bed_time", data.getGoBedTime());
			cv.put("try_sleep_time", data.getTrySleepTime());
			cv.put("how_long_sleep_time", data.getHowLongSleepTime());
			cv.put("wake_count", data.getWakeCount());
			cv.put("how_long_wake_time", data.getHowLongWakeTime());
			cv.put("wake_up_time", data.getWakeUpTime());
			cv.put("wake_early_time", data.getWakeEarlyTime());
			cv.put("out_bed_time", data.getOutBedTime());
			cv.put("soft_or_orange", data.getSoftOrOrange());
			cv.put("orange_data_upload", data.getUpload());
			cv.put("deep_sleep", data.getDeepsleep());
			cv.put("shallow_sleep", data.getShallowsleep());
			cv.put("result", data.getResult());
			int value = db.update(MyTabList.NEW_SLEEPTIME, cv, "date = ? and soft_or_orange = ?", new String[]{data.getDate(), type});
			LogUtil.i("huang", "value = " + value + ",date = " + data.getDate());
			if(value == 0){
				db.insert(MyTabList.NEW_SLEEPTIME, null, cv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public void updateSignInDataList(List<SignInData> list, String type){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			Iterator<SignInData> itertor = list.iterator();
			while (itertor.hasNext() ) {
				SignInData data = itertor.next();
				ContentValues cv = new ContentValues();
				cv.put("date", data.getDate());
				cv.put("sign_id", data.getSignInId());
				cv.put("go_bed_time", data.getGoBedTime());
				cv.put("try_sleep_time", data.getTrySleepTime());
				cv.put("how_long_sleep_time", data.getHowLongSleepTime());
				cv.put("wake_count", data.getWakeCount());
				cv.put("how_long_wake_time", data.getHowLongWakeTime());
				cv.put("wake_up_time", data.getWakeUpTime());
				cv.put("wake_early_time", data.getWakeEarlyTime());
				cv.put("out_bed_time", data.getOutBedTime());
				cv.put("soft_or_orange", data.getSoftOrOrange());
				cv.put("orange_data_upload", data.getUpload());
				cv.put("deep_sleep", data.getDeepsleep());
				cv.put("shallow_sleep", data.getShallowsleep());
				cv.put("result", data.getResult());
				int value = db.update(MyTabList.NEW_SLEEPTIME, cv, "date = ? and soft_or_orange = ?", new String[]{data.getDate(), type});
				if(value == 0){
					db.insert(MyTabList.NEW_SLEEPTIME, null, cv);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
	}
	
	
	/**
	 * 获取未签到的硬件数据
	 * @return  list 不会为null。如果没有的话size=0
	 */
	public List<SignInData> getUnUploadData(){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		List<SignInData> list = new ArrayList<SignInData>();
		Cursor c = db.query(MyTabList.NEW_SLEEPTIME, null, "soft_or_orange = ? and orange_data_upload = ?", new String[]{"1", "1"}, null, null, null);
		try {
			while (c.moveToNext()) {
				SignInData data = new SignInData();
				data.setDate(c.getString(c.getColumnIndex("date")));
				data.setSignInId(c.getString(c.getColumnIndex("sign_id")));
				data.setGoBedTime(c.getString(c.getColumnIndex("go_bed_time")));
				data.setTrySleepTime(c.getString(c.getColumnIndex("try_sleep_time")));
				data.setHowLongSleepTime(c.getInt(c.getColumnIndex("how_long_sleep_time")));
				data.setWakeCount(c.getInt(c.getColumnIndex("wake_count")));
				data.setHowLongWakeTime(c.getInt(c.getColumnIndex("how_long_wake_time")));
				data.setWakeUpTime(c.getString(c.getColumnIndex("wake_up_time")));
				data.setWakeEarlyTime(c.getInt(c.getColumnIndex("wake_early_time")));
				data.setOutBedTime(c.getString(c.getColumnIndex("out_bed_time")));
				data.setSoftOrOrange(c.getString(c.getColumnIndex("soft_or_orange")));
				data.setUpload(c.getInt(c.getColumnIndex("orange_data_upload")));
				data.setDeepsleep(c.getInt(c.getColumnIndex("deep_sleep")));
				data.setShallowsleep(c.getInt(c.getColumnIndex("shallow_sleep")));
				data.setResult(c.getString(c.getColumnIndex("result")));
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
			c.close();
		}
		return list;
	}
	
	
	
	/**
	 * 清空签到的表
	 */
	public void clearSignInData(){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		try {
			db.delete(MyTabList.NEW_SLEEPTIME, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
}
