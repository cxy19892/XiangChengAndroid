package com.yzm.sleep.background;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;

import com.yzm.sleep.background.AlarmService.ListAlarmTime;
/**
 *
 *
 */
public class AlarmDBOperate {
	private SQLiteDatabase db = null;
	private String tableName = null;
	
	public AlarmDBOperate(SQLiteDatabase db,String tableName){
		this.db = db;
		this.tableName = tableName;
	}
	
	/**
	 * 
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
		try {
			result.moveToFirst();
			int index = result.getCount();
			if(index == 0) {
				return "";
			}else {
				String str = result.getString(result.getColumnIndex(column[0]));
				return str;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}finally{
			result.close();
		}
	}
	public boolean queryExit(String[] column,String selection,String[] selectionArgs){
		Cursor result= this.db.query(this.tableName, column, selection, selectionArgs, null, null, null);
		try {
			result.moveToFirst();
			int index = result.getCount();
			if(index == 0) {
				return false;
			}else {
//			String str = result.getString(result.getColumnIndex(column[0]));
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			result.close();
		}
	}
	

	public  List<ListAlarmTime> GetALarmData() {
		
		
		List<ListAlarmTime> m_returnList= new ArrayList<ListAlarmTime>();
		
		//查询获得游标  
		   Cursor cursor = db.query(MyTabList.ALARM_TIME,null,null,null,null,null,null); 
		   
		   //判断游标是否为空  
		   if(cursor.moveToFirst()) {  
		   
		       //遍历游标  
		       for(int i=0;i<cursor.getCount();i++){  
		   
		            ListAlarmTime m_Data= new ListAlarmTime();
		            
		            int AlarmID = cursor.getInt(0);      		            
		            m_Data.AlarmID = AlarmID;
		            		            
		            String AlarmTime=cursor.getString(cursor.getColumnIndex("AlarmTime"));  
		            m_Data.AlarmTime = AlarmTime;
		            
		            int AlarmRepeat=cursor.getInt(cursor.getColumnIndex("AlarmRepeat"));  
		            m_Data.AlarmRepeat = AlarmRepeat;
		            
		            String AlarmPlant=cursor.getString(cursor.getColumnIndex("AlarmPlant"));  
		            m_Data.AlarmPlant = AlarmPlant;
		            
		            String AlarmTitle=cursor.getString(cursor.getColumnIndex("AlarmTitle"));  
		            m_Data.AlarmTitle = AlarmTitle;
		            
		            String AlarmDay=cursor.getString(cursor.getColumnIndex("AlarmDay"));  
		            m_Data.AlarmDay = AlarmDay;
		            
		            int AlarmDelay=cursor.getInt(cursor.getColumnIndex("AlarmDelay"));  
		            m_Data.AlarmDelay = AlarmDelay;
		            
		            String AlarmAudio=cursor.getString(cursor.getColumnIndex("AlarmAudio"));  
		            m_Data.AlarmAudio = AlarmAudio;
		            
		            int AlarmOnOff=cursor.getInt(cursor.getColumnIndex("AlarmOnOff"));  
		            m_Data.AlarmOnOff = AlarmOnOff;
		            
		            
		            
		            String AlarmProfile=cursor.getString(cursor.getColumnIndex("AlarmProfile"));  
		            m_Data.AlarmProfile = AlarmProfile;
		            
		            String AlarmUserNickname=cursor.getString(cursor.getColumnIndex("AlarmUserNickname"));  
		            m_Data.AlarmUserNickname = AlarmUserNickname;
		            
		            String AlarmUserId=cursor.getString(cursor.getColumnIndex("AlarmUserId"));  
		            m_Data.AlarmUserId = AlarmUserId;
		            
		            int AlarmAudioId=cursor.getInt(cursor.getColumnIndex("AlarmAudioId"));  
		            m_Data.AlarmAudioId = AlarmAudioId;
		            
		            String AlarmAudioCover=cursor.getString(cursor.getColumnIndex("AlarmAudioCover"));  
		            m_Data.AlarmAudioCover = AlarmAudioCover;
		            
		            String AudioCoverKey=cursor.getString(cursor.getColumnIndex("AudioCoverKey"));  
		            m_Data.AudioCoverKey = AudioCoverKey;
		            
		            int AlarmDownloads=cursor.getInt(cursor.getColumnIndex("AlarmDownloads"));  
		            m_Data.AlarmDownloads = AlarmDownloads;
		            
		            int AlarmType=cursor.getInt(cursor.getColumnIndex("AlarmType"));
		            m_Data.AlarmType = AlarmType;
		            
		            int AudioIsLocalRecord=cursor.getInt(cursor.getColumnIndex("AudioIsLocalRecord"));
		            m_Data.AudioIsLocalRecord = AudioIsLocalRecord;
		            
		            //remindinfo
		            int isRemind = cursor.getInt(cursor.getColumnIndex("IsRemind"));
		            m_Data.isRemind = isRemind;
		            
		            String RemindTitle = cursor.getString(cursor.getColumnIndex("RemindTitle"));  
		            m_Data.remindTitle = RemindTitle;
		            
		            String remindContext = cursor.getString(cursor.getColumnIndex("RemindContext")); 
		            m_Data.remindContext = remindContext;
		            //输出用户信息  
		            m_returnList.add(m_Data);
		            
		    	    cursor.moveToNext();
		            
		       }  
		   }   
			
				
		   cursor.close();
		
		
		return m_returnList;		
		
	};
	
	
	/**
	 * 按闹钟时间升序排列
	 * @return
	 */
	public  List<ListAlarmTime> GetALarmDataASC() {
		
		
		List<ListAlarmTime> m_returnList= new ArrayList<ListAlarmTime>();
		
		//查询获得游标  
		   Cursor cursor = db.query(MyTabList.ALARM_TIME,null,null,null,null,null,"AlarmTime asc"); 
		   
		   //判断游标是否为空  
		   if(cursor.moveToFirst()) {  
		   
		       //遍历游标  
		       for(int i=0;i<cursor.getCount();i++){  
		   
		            ListAlarmTime m_Data= new ListAlarmTime();
		            
		            int AlarmID = cursor.getInt(0);      		            
		            m_Data.AlarmID = AlarmID;
		            		            
		            String AlarmTime=cursor.getString(cursor.getColumnIndex("AlarmTime"));  
		            m_Data.AlarmTime = AlarmTime;
		            
		            int AlarmRepeat=cursor.getInt(cursor.getColumnIndex("AlarmRepeat"));  
		            m_Data.AlarmRepeat = AlarmRepeat;
		            
		            String AlarmPlant=cursor.getString(cursor.getColumnIndex("AlarmPlant"));  
		            m_Data.AlarmPlant = AlarmPlant;
		            
		            String AlarmTitle=cursor.getString(cursor.getColumnIndex("AlarmTitle"));  
		            m_Data.AlarmTitle = AlarmTitle;
		            
		            String AlarmDay=cursor.getString(cursor.getColumnIndex("AlarmDay"));  
		            m_Data.AlarmDay = AlarmDay;
		            
		            int AlarmDelay=cursor.getInt(cursor.getColumnIndex("AlarmDelay"));  
		            m_Data.AlarmDelay = AlarmDelay;
		            
		            String AlarmAudio=cursor.getString(cursor.getColumnIndex("AlarmAudio"));  
		            m_Data.AlarmAudio = AlarmAudio;
		            
		            int AlarmOnOff=cursor.getInt(cursor.getColumnIndex("AlarmOnOff"));  
		            m_Data.AlarmOnOff = AlarmOnOff;
		            
		            
		            
		            String AlarmProfile=cursor.getString(cursor.getColumnIndex("AlarmProfile"));  
		            m_Data.AlarmProfile = AlarmProfile;
		            
		            String AlarmUserNickname=cursor.getString(cursor.getColumnIndex("AlarmUserNickname"));  
		            m_Data.AlarmUserNickname = AlarmUserNickname;
		            
		            String AlarmUserId=cursor.getString(cursor.getColumnIndex("AlarmUserId"));  
		            m_Data.AlarmUserId = AlarmUserId;
		            
		            int AlarmAudioId=cursor.getInt(cursor.getColumnIndex("AlarmAudioId"));  
		            m_Data.AlarmAudioId = AlarmAudioId;
		            
		            String AlarmAudioCover=cursor.getString(cursor.getColumnIndex("AlarmAudioCover"));  
		            m_Data.AlarmAudioCover = AlarmAudioCover;
		            
		            String AudioCoverKey=cursor.getString(cursor.getColumnIndex("AudioCoverKey"));  
		            m_Data.AudioCoverKey = AudioCoverKey;
		            
		            int AlarmDownloads=cursor.getInt(cursor.getColumnIndex("AlarmDownloads"));  
		            m_Data.AlarmDownloads = AlarmDownloads;
		            
		            int AlarmType=cursor.getInt(cursor.getColumnIndex("AlarmType"));
		            m_Data.AlarmType = AlarmType;
		            
		            int AudioIsLocalRecord=cursor.getInt(cursor.getColumnIndex("AudioIsLocalRecord"));
		            m_Data.AudioIsLocalRecord = AudioIsLocalRecord;
		            
		            //remindinfo
		            int isRemind = cursor.getInt(cursor.getColumnIndex("IsRemind"));
		            m_Data.isRemind = isRemind;
		            
		            String RemindTitle = cursor.getString(cursor.getColumnIndex("RemindTitle"));  
		            m_Data.remindTitle = RemindTitle;
		            
		            String remindContext = cursor.getString(cursor.getColumnIndex("RemindContext")); 
		            m_Data.remindContext = remindContext;
		            //输出用户信息  
		            m_returnList.add(m_Data);
		            
		    	    cursor.moveToNext();
		            
		       }  
		   }   
			
				
		   cursor.close();
		
		
		return m_returnList;		
		
	};
	
	/**
	 * 获取在制定时间之后的闹钟并按升序排列
	 * @param time
	 * @return
	 */
	public  List<ListAlarmTime> GetALarmDataAftertimeASC(String time) {
	
		
		List<ListAlarmTime> m_returnList= new ArrayList<ListAlarmTime>();
		Cursor cursor = db.query(MyTabList.ALARM_TIME, null, null, null, "AlarmTime", "AlarmTime > '"+time+"'", "AlarmTime asc");
		//查询获得游标  
//		String sql = "select * from alarmtime where AlarmTime >= ? order by AlarmTime asc";
//		   Cursor cursor = db.rawQuery(sql, new String[]{time});
//		db.execSQL("select * from alarmtime where AlarmTime > "+time+" order by AlarmTime asc");//query(MyTabList.ALARM_TIME,null,"AlarmTime",new String[]{time},null,null,"AlarmTime asc"); 
		   
		   //判断游标是否为空  
		   if(cursor.moveToFirst()) {  
		   
		       //遍历游标  
		       for(int i=0;i<cursor.getCount();i++){  
		   
		            ListAlarmTime m_Data= new ListAlarmTime();
		            
		            int AlarmID = cursor.getInt(0);      		            
		            m_Data.AlarmID = AlarmID;
		            		            
		            String AlarmTime=cursor.getString(cursor.getColumnIndex("AlarmTime"));  
		            m_Data.AlarmTime = AlarmTime;
		            
		            int AlarmRepeat=cursor.getInt(cursor.getColumnIndex("AlarmRepeat"));  
		            m_Data.AlarmRepeat = AlarmRepeat;
		            
		            String AlarmPlant=cursor.getString(cursor.getColumnIndex("AlarmPlant"));  
		            m_Data.AlarmPlant = AlarmPlant;
		            
		            String AlarmTitle=cursor.getString(cursor.getColumnIndex("AlarmTitle"));  
		            m_Data.AlarmTitle = AlarmTitle;
		            
		            String AlarmDay=cursor.getString(cursor.getColumnIndex("AlarmDay"));  
		            m_Data.AlarmDay = AlarmDay;
		            
		            int AlarmDelay=cursor.getInt(cursor.getColumnIndex("AlarmDelay"));  
		            m_Data.AlarmDelay = AlarmDelay;
		            
		            String AlarmAudio=cursor.getString(cursor.getColumnIndex("AlarmAudio"));  
		            m_Data.AlarmAudio = AlarmAudio;
		            
		            int AlarmOnOff=cursor.getInt(cursor.getColumnIndex("AlarmOnOff"));  
		            m_Data.AlarmOnOff = AlarmOnOff;
		            
		            
		            
		            String AlarmProfile=cursor.getString(cursor.getColumnIndex("AlarmProfile"));  
		            m_Data.AlarmProfile = AlarmProfile;
		            
		            String AlarmUserNickname=cursor.getString(cursor.getColumnIndex("AlarmUserNickname"));  
		            m_Data.AlarmUserNickname = AlarmUserNickname;
		            
		            String AlarmUserId=cursor.getString(cursor.getColumnIndex("AlarmUserId"));  
		            m_Data.AlarmUserId = AlarmUserId;
		            
		            int AlarmAudioId=cursor.getInt(cursor.getColumnIndex("AlarmAudioId"));  
		            m_Data.AlarmAudioId = AlarmAudioId;
		            
		            String AlarmAudioCover=cursor.getString(cursor.getColumnIndex("AlarmAudioCover"));  
		            m_Data.AlarmAudioCover = AlarmAudioCover;
		            
		            String AudioCoverKey=cursor.getString(cursor.getColumnIndex("AudioCoverKey"));  
		            m_Data.AudioCoverKey = AudioCoverKey;
		            
		            int AlarmDownloads=cursor.getInt(cursor.getColumnIndex("AlarmDownloads"));  
		            m_Data.AlarmDownloads = AlarmDownloads;
		            
		            int AlarmType=cursor.getInt(cursor.getColumnIndex("AlarmType"));
		            m_Data.AlarmType = AlarmType;
		            
		            int AudioIsLocalRecord=cursor.getInt(cursor.getColumnIndex("AudioIsLocalRecord"));
		            m_Data.AudioIsLocalRecord = AudioIsLocalRecord;
		            
		            //remindinfo
		            int isRemind = cursor.getInt(cursor.getColumnIndex("IsRemind"));
		            m_Data.isRemind = isRemind;
		            
		            String RemindTitle = cursor.getString(cursor.getColumnIndex("RemindTitle"));  
		            m_Data.remindTitle = RemindTitle;
		            
		            String remindContext = cursor.getString(cursor.getColumnIndex("RemindContext")); 
		            m_Data.remindContext = remindContext;
		            //输出用户信息  
		            m_returnList.add(m_Data);
		            
		    	    cursor.moveToNext();
		            
		       }  
		   }   
			
				
		   cursor.close();
		
		
		return m_returnList;		
		
	};
	
	/**
	 * 
	 * 保存闹钟信息
	 * @param AlarmTime
	 * 					闹钟时间例如12:00
	 * @param AlarmRepeat
	 * 					重复周期为0表示不重复。为1表示重复
	 * @param AlarmPlant
	 * 					AlarmRepeat 标识为1   周期 ：1,2,3,4,5,6,7
	 * @param AlarmTitle
	 * 					AlarmTitle Title
	 * @param AlarmDay
	 * 					AlarmRepeat标识为0时  时间是年月日
	 * @param AlarmDelay
	 * 					小睡时长 为0表示不小睡
	 * @param alarmAudio
	 * 					闹钟铃声
	 * @param AlarmOnOff
	 * 					启动开关 0 表示未启动  1表示启动
	 * @param AlarmProfile
	 * 					铃声对应用户头像
	 */
//	String AlarmTime,int AlarmRepeat,String AlarmPlant,
//	String AlarmTitle,String AlarmDay,int AlarmDelay,String alarmAudio,int AlarmOnOff,String 
//	AlarmProfile,String AlarmUserNickname,String AlarmUserId,int AlarmDownloads
	public void saveAwakeAlarm(Context context,ListAlarmTime m_ListAlarmTime) {
		ContentValues values = new ContentValues();
		values.put("AlarmTime", m_ListAlarmTime.AlarmTime);
		values.put("AlarmRepeat", m_ListAlarmTime.AlarmRepeat);
		values.put("AlarmPlant", m_ListAlarmTime.AlarmPlant);
		values.put("AlarmTitle", m_ListAlarmTime.AlarmTitle);
		values.put("AlarmDay", m_ListAlarmTime.AlarmDay);
		values.put("AlarmDelay", m_ListAlarmTime.AlarmDelay);
		values.put("AlarmAudio", m_ListAlarmTime.AlarmAudio);
		values.put("AlarmOnOff",m_ListAlarmTime. AlarmOnOff);
		
		values .put("AlarmProfile", m_ListAlarmTime.AlarmProfile);
		values.put("AlarmUserNickname", m_ListAlarmTime.AlarmUserNickname);
		values.put("AlarmUserId", m_ListAlarmTime.AlarmUserId);
		values.put("AlarmAudioId", m_ListAlarmTime.AlarmAudioId);
		values.put("AlarmAudioCover", m_ListAlarmTime.AlarmAudioCover);
		values.put("AudioCoverKey", m_ListAlarmTime.AudioCoverKey);
		values .put("AlarmDownloads", m_ListAlarmTime.AlarmDownloads);
		
		values .put("AlarmType", m_ListAlarmTime.AlarmType);
		
		values .put("AudioIsLocalRecord", m_ListAlarmTime.AudioIsLocalRecord);
		
		//remind
		values.put("IsRemind", m_ListAlarmTime.isRemind);
		values.put("RemindContext", m_ListAlarmTime.remindContext);
		values.put("RemindTitle", m_ListAlarmTime.remindTitle);
		
		insert(values);
		Intent intent = new Intent();
		intent.setAction(AlarmService.ACTION_INITDB);
		context.sendBroadcast(intent);
	}
	
	/**
	 * 更新闹钟
	 */
	public void updateAwakeAlarm(Context context,String id,ListAlarmTime m_ListAlarmTime) {
		ContentValues values = new ContentValues();
		values.put("AlarmTime", m_ListAlarmTime.AlarmTime);
		values.put("AlarmRepeat", m_ListAlarmTime.AlarmRepeat);
		values.put("AlarmPlant", m_ListAlarmTime.AlarmPlant);
		values.put("AlarmTitle", m_ListAlarmTime.AlarmTitle);
		values.put("AlarmDay", m_ListAlarmTime.AlarmDay);
		values.put("AlarmDelay", m_ListAlarmTime.AlarmDelay);
		values.put("AlarmAudio", m_ListAlarmTime.AlarmAudio);
		values.put("AlarmOnOff", m_ListAlarmTime.AlarmOnOff);
		
		values .put("AlarmProfile", m_ListAlarmTime.AlarmProfile);
		values.put("AlarmUserNickname", m_ListAlarmTime.AlarmUserNickname);
		values.put("AlarmUserId", m_ListAlarmTime.AlarmUserId);
		values.put("AlarmAudioId", m_ListAlarmTime.AlarmAudioId);
		values.put("AlarmAudioCover", m_ListAlarmTime.AlarmAudioCover);
		values.put("AudioCoverKey", m_ListAlarmTime.AudioCoverKey);
		values .put("AlarmDownloads", m_ListAlarmTime.AlarmDownloads);
		
		values .put("AlarmType", m_ListAlarmTime.AlarmType);
		values .put("AudioIsLocalRecord", m_ListAlarmTime.AudioIsLocalRecord);
		//remind
		values.put("IsRemind", m_ListAlarmTime.isRemind);
		values.put("RemindContext", m_ListAlarmTime.remindContext);
		values.put("RemindTitle", m_ListAlarmTime.remindTitle);
				
		String whereClause = "_id=?";
		String[] whereArgs = {id};
		update(values, whereClause, whereArgs);
		/*发送广播*/
		Intent intent = new Intent();
		intent.setAction(AlarmService.ACTION_INITDB);
		context.sendBroadcast(intent);
	}
	/**
	 * 删除闹钟
	 * @param id
	 */
	public void deleteAwakeAlarm(Context context,String id,ListAlarmTime m_ListAlarmTime) {
		String whereClause = "_id=?";
		String[] whereArgs = {id};
		delete(whereClause, whereArgs);
		Intent intent = new Intent();
		intent.setAction(AlarmService.ACTION_DELETEALARM);
		Bundle values = new Bundle();
		values.putInt("AlarmID", Integer.parseInt(id));
		values.putString("AlarmTime", m_ListAlarmTime.AlarmTime);
		values.putInt("AlarmRepeat", m_ListAlarmTime.AlarmRepeat);
		values.putString("AlarmPlant", m_ListAlarmTime.AlarmPlant);
		values.putString("AlarmTitle", m_ListAlarmTime.AlarmTitle);
		values.putString("AlarmDay", m_ListAlarmTime.AlarmDay);
		values.putInt("AlarmDelay", m_ListAlarmTime.AlarmDelay);
		values.putString("AlarmAudio", m_ListAlarmTime.AlarmAudio);
		values.putInt("AlarmOnOff", m_ListAlarmTime.AlarmOnOff);
		
		values.putString("AlarmProfile", m_ListAlarmTime.AlarmProfile);
		values.putString("AlarmUserNickname", m_ListAlarmTime.AlarmUserNickname);
		values.putString("AlarmUserId", m_ListAlarmTime.AlarmUserId);
		values.putInt("AlarmAudioId", m_ListAlarmTime.AlarmAudioId);
		values.putString("AlarmAudioCover", m_ListAlarmTime.AlarmAudioCover);
		values.putInt("AlarmDownloads", m_ListAlarmTime.AlarmDownloads);
		
		values.putInt("AlarmType", m_ListAlarmTime.AlarmType);
		values.putInt("AudioIsLocalRecord", m_ListAlarmTime.AudioIsLocalRecord);
		
		intent.putExtras(values);
		context.sendBroadcast(intent);
	}
	/**
	 * 查询数据库，根据闹钟AlarmID获取闹钟路径
	 * @param alarmID
	 * @return
	 */
	public String searchAwakeAlarmByAlarmID(String id){
		String[] columns = new String[]{"AlarmAudio"};
		String selection = "_id=?";
		String[] selectionArgs = new String[]{id};
		return query(columns, selection, selectionArgs);
	}
	
	/*
	*intent.putExtra("AlarmID", AlarmID);//9999

			intent.putExtra("AlarmTitle", AlarmTitle);

			intent.putExtra("AlarmDelay", AlarmDelay);

			intent.putExtra("AlarmAudio", AlarmAudio);

			intent.putExtra("AlarmTime", TimeSet);

			intent.putExtra("AlarmAudioCover", AlarmAudioCover);
			
			intent.putExtra("AudioCoverKey", AudioCoverKey);
	*
	*/
	public String searcchByArrays(ListAlarmTime m_ListAlarmTime){
		String[] columns = new String[]{"AlarmAudio"};
		int i = 0;
		if(!TextUtils.isEmpty(m_ListAlarmTime.AlarmTitle)){
			i++;
		}
		if(!TextUtils.isEmpty(String.valueOf(m_ListAlarmTime.AlarmDelay))){
			i++;
		}
		if(!TextUtils.isEmpty(m_ListAlarmTime.AlarmAudio)){
			i++;
		}
		if(!TextUtils.isEmpty(m_ListAlarmTime.AlarmAudioCover)){
			i++;
		}
		if(!TextUtils.isEmpty(m_ListAlarmTime.AudioCoverKey)){
			i++;
		}
		String[] selectionArgs = new String[i];
		StringBuffer sb = new StringBuffer();
		if(!TextUtils.isEmpty(m_ListAlarmTime.AlarmTitle)){
			sb.append("AlarmTitle = ? ");
			selectionArgs[0] = m_ListAlarmTime.AlarmTitle;
		}
		if(!TextUtils.isEmpty(String.valueOf(m_ListAlarmTime.AlarmDelay))){
			sb.append("and AlarmDelay = ? ");
			selectionArgs[1] = String.valueOf(m_ListAlarmTime.AlarmDelay);
		}
		if(!TextUtils.isEmpty(m_ListAlarmTime.AlarmAudio)){
			sb.append("and AlarmAudio = ? ");
			selectionArgs[2] = m_ListAlarmTime.AlarmAudio;
		}
		if(!TextUtils.isEmpty(m_ListAlarmTime.AlarmAudioCover)){
			sb.append("and AlarmAudioCover = ? ");
			selectionArgs[3] = m_ListAlarmTime.AlarmAudioCover;
		}
		if(!TextUtils.isEmpty(m_ListAlarmTime.AudioCoverKey)){
			sb.append("and AudioCoverKey = ? ");
			selectionArgs[4] = m_ListAlarmTime.AudioCoverKey;
		}
//		String selection = "AlarmTitle = ? and AlarmDelay=? and AlarmAudio=? and AlarmAudioCover = ? and AudioCoverKey = ?";
//		String[] selectionArgs = new String[]{m_ListAlarmTime.AlarmTitle,String.valueOf(m_ListAlarmTime.AlarmDelay),
//				m_ListAlarmTime.AlarmAudio,m_ListAlarmTime.AlarmAudioCover,m_ListAlarmTime.AudioCoverKey};
		return query(columns, sb.toString(), selectionArgs);
	}
	/**
	 * 查询闹钟信息
	 * @param id
	 */
	public String searchAlarmPathByUserId(String AlarmUserId){
		String[] columns = new String[]{"AlarmAudio"};
		String selection = "AlarmUserId=?";
		String[] selectionArgs = new String[]{AlarmUserId};
		return query(columns, selection, selectionArgs);
	}
	/**判断铃音是否已经设置为闹钟*/
	public boolean IsAlreadySetAlarm(String AlarmAudio){
		String[] columns = new String[]{"AlarmAudio"};
		String selection = "AlarmAudio=?";
		String[] selectionArgs = new String[]{AlarmAudio};
		return queryExit(columns, selection, selectionArgs);
	}
	
}
