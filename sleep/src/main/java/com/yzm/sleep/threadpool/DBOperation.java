package com.yzm.sleep.threadpool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;

public class DBOperation {

	private Context context;
	private static DBOperation operation;
	
	private DBOperation(Context context){
		this.context = context;
	}
	
	public static DBOperation initinstance(Context context){
		if(operation == null)
			operation = new DBOperation(context);
		return operation;
	}
	
	/**
	 * 上传成功后修改上传状态
	 * @param date   日期
	 * @param state  0为未上传 1为已上传
	 */
	public synchronized void updateUploadState(String date, String state){
		try {
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			SQLiteDatabase db = helper.getWritableDatabase();
			db.execSQL("update " + MyTabList.SLEEPTIME + " set isupload = '"+ state +"' where date = '" + date + "'");
		} catch (Exception e) {
		}
	}
	
	
}
