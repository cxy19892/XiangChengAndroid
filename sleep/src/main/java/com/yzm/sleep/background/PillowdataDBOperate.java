package com.yzm.sleep.background;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.render.GetSleepResultValueClass;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PillowdataDBOperate {

	private SQLiteDatabase db = null;
	private String tableName = null;
	private static PillowdataDBOperate pDBO;
	
	private PillowdataDBOperate(Context context){
		MyDatabaseHelper helper2 = MyDatabaseHelper.getInstance(context);
		this.db = helper2.getWritableDatabase();
		this.tableName = MyTabList.PILLOW_SLEEP_DATA;
	}
	
	public static PillowdataDBOperate getinit(Context context){
		if(pDBO == null)
			pDBO = new PillowdataDBOperate(context);
		return pDBO;
	}
	
	/**
	 * @param cv 
	 */
	public void insert(ContentValues cv) {
		this.db.insert(this.tableName, null, cv);
	}
	
	public void update(ContentValues cv,String whereClause,String whereArgs[]) {
		this.db.update(this.tableName, cv, whereClause, whereArgs);
	}
	
	public void delete(String whereClause,String whereArgs[]) {
		this.db.delete(this.tableName, whereClause, whereArgs);
	}
	
	
	public String query(String[] column,String selection,String[] selectionArgs){
		Cursor result= this.db.query(this.tableName, column, selection, selectionArgs, null, null, null);
		result.moveToFirst();
		int index = result.getCount();
		if(index == 0) {
			result.close();
			return "";
		}else {
			String str = result.getString(result.getColumnIndex(column[0]));
			result.close();
			return str;
		}
	}
	
	/**
	 * 返回数据库里所有的数据
	 * @return
	 * @throws ParseException
	 */
	public ArrayList<SleepDataHead> queryDisplayAllSleepDataList(){
		try {
			Cursor result = this.db.query(true, MyTabList.PILLOW_SLEEP_DATA, null, null, null, null, null, null, null);
			ArrayList<SleepDataHead> list = new ArrayList<SleepDataHead>();
			GetSleepResultValueClass  SleepResult = new GetSleepResultValueClass();
			result.moveToFirst();
			while(result.moveToNext()){
				SleepDataHead sleepdata = SleepResult.new SleepDataHead();; 
				sleepdata.XStart = result.getLong(result.getColumnIndex("XStart"));
				sleepdata.XStop = result.getLong(result.getColumnIndex("XStop"));
				sleepdata.YMax= result.getInt(result.getColumnIndex("YMax"));
				sleepdata.InSleepTime= result.getLong(result.getColumnIndex("InSleepTime"));
				sleepdata.OutSleepTime= result.getLong(result.getColumnIndex("OutSleepTime"));
				sleepdata.TotalSleepTime= result.getInt(result.getColumnIndex("TotalSleepTime"));
				sleepdata.Deep_Sleep= result.getInt(result.getColumnIndex("Deep_Sleep"));
				sleepdata.Shallow_Sleep= result.getInt(result.getColumnIndex("Shallow_Sleep"));
				sleepdata.AwakeTime_Sleep= result.getInt(result.getColumnIndex("AwakeTime_Sleep"));
				sleepdata.OnBed= result.getInt(result.getColumnIndex("OnBed"));
			    sleepdata.ToSleep= result.getInt(result.getColumnIndex("ToSleep"));
				sleepdata.AwakeCount= result.getInt(result.getColumnIndex("AwakeCount"));
				sleepdata.AwakeNoGetUpCount= result.getInt(result.getColumnIndex("AwakeNoGetUpCount"));
				sleepdata.GoToBedTime= result.getLong(result.getColumnIndex("GoToBedTime"));
				sleepdata.GetUpTime= result.getLong(result.getColumnIndex("GetUpTime"));
				sleepdata.ListLength = result.getInt(result.getColumnIndex("ListLength"));
				sleepdata.SleepBak1= result.getInt(result.getColumnIndex("SleepBak1"));
				sleepdata.SleepBak2= result.getInt(result.getColumnIndex("SleepBak2"));
				Gson gson = new Gson();
				sleepdata.m_pValue = gson.fromJson(result.getString(result.getColumnIndex("ListData")), new TypeToken<List< GetSleepResultValueClass.SleepData >>(){}.getType());
				list.add(sleepdata);
			}
			result.close();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
	}
	/**
	 * 返回保存的数据信息的时间列表
	 * @return
	 */
	public ArrayList<String> querryDaytimeinSleepData(){
		ArrayList<String> datetimelist = new ArrayList<String>();
		Cursor result = this.db.rawQuery("select 'Time' from "+MyTabList.PILLOW_SLEEP_DATA, null);//query(MyTabList.PILLOW_SLEEP_DATA, new String[]{"Time"}, null, null, null, null, null);
		
		result.moveToFirst();
		while(!result.moveToNext()){
			String stime = result.getString(0);
			datetimelist.add(stime);
		}
		result.close();
		return datetimelist;
	}
	
	/**
	 * 返回所请求的时间的数据
	 * @param context
	 * @param date  查询当天的时间 long 格式 yyyy-MM-dd 测试时间可以传 null
	 * @return
	 * @throws ParseException
	 */
	public SleepDataHead  queryDisplaySleepData(String date){
		try {
			String queryDate = "1970-01-17";
			if(null == date){
				 queryDate = "1970-01-17";
			}else{
				 queryDate = date;
			}

			
			GetSleepResultValueClass  SleepResult = new GetSleepResultValueClass();
			SleepDataHead sleepdata = SleepResult.new SleepDataHead();
			Cursor result = this.db.query(MyTabList.PILLOW_SLEEP_DATA, null, "Time = ?", new String[]{queryDate}, null, null, null);
			result.moveToFirst();
			if(!(result.getCount() <= 0)) {
				sleepdata.XStart = result.getLong(result.getColumnIndex("XStart"));
				sleepdata.XStop = result.getLong(result.getColumnIndex("XStop"));
				sleepdata.YMax= result.getInt(result.getColumnIndex("YMax"));
				sleepdata.InSleepTime= result.getLong(result.getColumnIndex("InSleepTime"));
				sleepdata.OutSleepTime= result.getLong(result.getColumnIndex("OutSleepTime"));
				sleepdata.TotalSleepTime= result.getInt(result.getColumnIndex("TotalSleepTime"));
				sleepdata.Deep_Sleep= result.getInt(result.getColumnIndex("Deep_Sleep"));
				sleepdata.Shallow_Sleep= result.getInt(result.getColumnIndex("Shallow_Sleep"));
				sleepdata.AwakeTime_Sleep= result.getInt(result.getColumnIndex("AwakeTime_Sleep"));
				sleepdata.OnBed= result.getInt(result.getColumnIndex("OnBed"));
			    sleepdata.ToSleep= result.getInt(result.getColumnIndex("ToSleep"));
				sleepdata.AwakeCount= result.getInt(result.getColumnIndex("AwakeCount"));
				sleepdata.AwakeNoGetUpCount= result.getInt(result.getColumnIndex("AwakeNoGetUpCount"));
				sleepdata.GoToBedTime= result.getLong(result.getColumnIndex("GoToBedTime"));
				sleepdata.GetUpTime= result.getLong(result.getColumnIndex("GetUpTime"));
				sleepdata.ListLength = result.getInt(result.getColumnIndex("ListLength"));
				sleepdata.SleepBak1= result.getInt(result.getColumnIndex("SleepBak1"));
				sleepdata.SleepBak2= result.getInt(result.getColumnIndex("SleepBak2"));
				Gson gson = new Gson();
				sleepdata.m_pValue = gson.fromJson(result.getString(result.getColumnIndex("ListData")), new TypeToken<List< GetSleepResultValueClass.SleepData >>(){}.getType());
			}
			result.close();
			return sleepdata;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
}
