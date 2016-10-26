package com.yzm.sleep.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.model.DownloadAudioInfo;
import com.yzm.sleep.model.RecordAudioInfo;
import com.yzm.sleep.model.RingtoneInfo;
import com.yzm.sleep.model.SendAudioFaildInfo;

public class AudioDAO {
	private SQLiteDatabase db = null;
	private String tableName = null;

	public static class DownloadAudioInfoClass{
		public String lyid = "";
		
		public String loginUserId="";
		public String AudioTitle="";
		public String AudioDownloadDate="";
		public int AudioDuration=0;
		public String AudioDownLoads="";
		public String AudioLocalPath="";
		
		
		public String AudioUserId="";
		public String AudioUserNickName="";
		public String AudioUserAge="";
		public String AudioUserSex="";
		public String AudioUserOccupation="";
		
		
		public int AudioType;
		
		public String AudioKey="";
		
		public String AudioCover="";
		public String AudioCoverKey="";
		public String AudioCoverSuolue="";
		public String AudioCoverKeySuolue="";
		
		public String AudioUserProfile="";
		public String UserProfileKey="";
		public String AudioUserProfileSuolue="";
		public String UserProfileKeySuolue="";
		
		
		
		
		public DownloadAudioInfoClass() {
		}
		
	}
	
	public AudioDAO(SQLiteDatabase db, String tableName) {
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

	public void update(ContentValues cv, String whereClause, String whereArgs[]) {
		this.db.update(this.tableName, cv, whereClause, whereArgs);
	}

	public void delete(String whereClause, String whereArgs[]) {
		this.db.delete(this.tableName, whereClause, whereArgs);
	}

	public String query(String[] columns, String selection, String[] selectionArgs) {
		Cursor result = this.db.query(this.tableName, columns,
				selection, selectionArgs, null, null, null);
		try {
			result.moveToFirst();
			int index = result.getCount();
			if (index == 0) {
				return "";
			} else {
				String AudioLocalPath = result.getString(result.getColumnIndex("AudioLocalPath"));
				return AudioLocalPath;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}finally{
			result.close();
		}
	}
	
	/**
	 * 获取录制铃声列表信息
	 * @return
	 */
	public ArrayList<RecordAudioInfo> GetRecordAudioInfo(String loginUserId,boolean isAllRecord) {

		ArrayList<RecordAudioInfo> m_returnList = new ArrayList<RecordAudioInfo>();

		// 查询获得游标
		Cursor cursor = this.db.query(MyTabList.RECORD_AUDIO, null, null, null, null,
				null, null);

		// 判断游标是否为空
		if (cursor.moveToFirst()) {

			// 遍历游标
			for (int i = 0; i < cursor.getCount(); i++) {

				RecordAudioInfo m_Data = new RecordAudioInfo();

				int id = cursor.getInt(0);
				m_Data.id = id;
				
				String title = cursor.getString(cursor
						.getColumnIndex("AudioTitle"));
				m_Data.title = title;

				String date = cursor.getString(cursor
						.getColumnIndex("AudioRecordDate"));
				m_Data.date = date;
				
				int duration = cursor.getInt(cursor
						.getColumnIndex("AudioDuration"));
				m_Data.duration = duration;
				
				String ThemePicpath = cursor.getString(cursor
						.getColumnIndex("AudioThemePic"));
				m_Data.ThemePicpath = ThemePicpath;
				
				String path = cursor.getString(cursor
						.getColumnIndex("AudioLocalPath"));
				m_Data.path = path;
				
				String userId = cursor.getString(cursor.getColumnIndex("UserId"));
				m_Data.userId = userId;
				
				// 输出用户信息
				if (isAllRecord) {
					m_returnList.add(m_Data);
				}else {
					if (cursor.getString(cursor.getColumnIndex("UserId")).equals(loginUserId)) {
						m_returnList.add(m_Data);
					}
				}

				cursor.moveToNext();

			}
		}

		cursor.close();

		return m_returnList;

	};
	public static RecordAudioInfo getRecordAudioInfoByPath(Context context,String audiopath){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor  = null;
		cursor= db.query(MyTabList.RECORD_AUDIO,null, "AudioLocalPath='"+audiopath+"'", null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			RecordAudioInfo m_Data = new RecordAudioInfo();

			int id = cursor.getInt(0);
			m_Data.id = id;
			
			String title = cursor.getString(cursor
					.getColumnIndex("AudioTitle"));
			m_Data.title = title;

			String date = cursor.getString(cursor
					.getColumnIndex("AudioRecordDate"));
			m_Data.date = date;
			
			int duration = cursor.getInt(cursor
					.getColumnIndex("AudioDuration"));
			m_Data.duration = duration;
			
			String ThemePicpath = cursor.getString(cursor
					.getColumnIndex("AudioThemePic"));
			m_Data.ThemePicpath = ThemePicpath;
			
			String path = cursor.getString(cursor
					.getColumnIndex("AudioLocalPath"));
			m_Data.path = path;
			
			cursor.close();
			return m_Data;
		}
		return null;
	}
	/**
	 * 获取下载铃声列表信息
	 * @return
	 */
	public ArrayList<DownloadAudioInfo> GetDownloadAudioInfo() {
		
		ArrayList<DownloadAudioInfo> m_returnList = new ArrayList<DownloadAudioInfo>();
		
		// 查询获得游标
		Cursor cursor = this.db.query(MyTabList.DOWNLOAD_AUDIO, null, null, null, null,
				null, null);
		if (cursor == null) {
			return m_returnList;
		}
		// 判断游标是否为空
		if (cursor.moveToFirst()) {
			
			// 遍历游标
			for (int i = 0; i < cursor.getCount(); i++) {
				
				DownloadAudioInfo m_Data = new DownloadAudioInfo();
				
				int id = cursor.getInt(cursor
						.getColumnIndex("_id"));
				m_Data.id = id;
				
				String title = cursor.getString(cursor
						.getColumnIndex("AudioTitle"));
				m_Data.title = title;
				
				String date = cursor.getString(cursor
						.getColumnIndex("AudioDownloadDate"));
				m_Data.downloadDate = date;
				
				int duration = cursor.getInt(cursor
						.getColumnIndex("AudioDuration"));
				m_Data.duration = duration;
				
				int downloads = cursor.getInt(cursor
						.getColumnIndex("AudioDownLoads"));
				m_Data.downloads = downloads;
				
				String path = cursor.getString(cursor
						.getColumnIndex("AudioLocalPath"));
				m_Data.path = path;
				
				
				
				String audioUserId = cursor.getString(cursor
						.getColumnIndex("AudioUserId"));
				m_Data.AudioUserId = audioUserId;
				
				String userProfile = cursor.getString(cursor
						.getColumnIndex("AudioUserProfile"));
				m_Data.AudioUserProfile = userProfile;
				
				String userNickname = cursor.getString(cursor
						.getColumnIndex("AudioUserNickName"));
				m_Data.AudioUserNickName = userNickname;
				
				String userAge = cursor.getString(cursor
						.getColumnIndex("AudioUserAge"));
				m_Data.AudioUserAge = userAge;
				
				String userOccupation = cursor.getString(cursor
						.getColumnIndex("AudioUserOccupation"));
				m_Data.AudioUserOccupation = userOccupation;
				
				String AudioCover = cursor.getString(cursor
						.getColumnIndex("AudioCover"));
				m_Data.AudioCover = AudioCover;
				
				
				int AudioType = cursor.getInt(cursor
						.getColumnIndex("AudioType"));
				m_Data.AudioType = AudioType;
				
				
				String AudioKey = cursor.getString(cursor
						.getColumnIndex("AudioKey"));
				m_Data.AudioKey = AudioKey;
				
				String AudioCoverKey = cursor.getString(cursor
						.getColumnIndex("AudioCoverKey"));
				m_Data.AudioCoverKey = AudioCoverKey;
				
				String UserProfileKey = cursor.getString(cursor
						.getColumnIndex("UserProfileKey"));
				m_Data.UserProfileKey = UserProfileKey;
				
				String AudioCoverSuolue = cursor.getString(cursor
						.getColumnIndex("AudioCoverSuolue"));
				m_Data.AudioCoverSuolue = AudioCoverSuolue;
				
				String AudioCoverKeySuolue = cursor.getString(cursor
						.getColumnIndex("AudioCoverKeySuolue"));
				m_Data.AudioCoverKeySuolue = AudioCoverKeySuolue;
				
				// 输出铃音信息
				if (m_Data.AudioType != 2) {//神秘铃音不可见
					m_returnList.add(m_Data);
				}
				
				cursor.moveToNext();
				
			}
		}
		
		cursor.close();
		
		return m_returnList;
		
	};
	
	/**
	 * 获取下载铃声列表信息
	 * @return
	 */
	public ArrayList<DownloadAudioInfo> GetDefaultAudioInfo() {
		
		ArrayList<DownloadAudioInfo> m_returnList = new ArrayList<DownloadAudioInfo>();
		
		// 查询获得游标
		Cursor cursor = this.db.query(MyTabList.DOWNLOAD_AUDIO, null, null, null, null,
				null, null);
		
		// 判断游标是否为空
		if (cursor.moveToFirst()) {
			
			// 遍历游标
			for (int i = 0; i < cursor.getCount(); i++) {
				
				DownloadAudioInfo m_Data = new DownloadAudioInfo();
				
				int id = cursor.getInt(cursor
						.getColumnIndex("_id"));
				m_Data.id = id;
				
				String title = cursor.getString(cursor
						.getColumnIndex("AudioTitle"));
				m_Data.title = title;
				
				String date = cursor.getString(cursor
						.getColumnIndex("AudioDownloadDate"));
				m_Data.downloadDate = date;
				
				int duration = cursor.getInt(cursor
						.getColumnIndex("AudioDuration"));
				m_Data.duration = duration;
				
				int downloads = cursor.getInt(cursor
						.getColumnIndex("AudioDownLoads"));
				m_Data.downloads = downloads;
				
				String path = cursor.getString(cursor
						.getColumnIndex("AudioLocalPath"));
				m_Data.path = path;
				
				
				
				String audioUserId = cursor.getString(cursor
						.getColumnIndex("AudioUserId"));
				m_Data.AudioUserId = audioUserId;
				
				String userProfile = cursor.getString(cursor
						.getColumnIndex("AudioUserProfile"));
				m_Data.AudioUserProfile = userProfile;
				
				String userNickname = cursor.getString(cursor
						.getColumnIndex("AudioUserNickName"));
				m_Data.AudioUserNickName = userNickname;
				
				String userAge = cursor.getString(cursor
						.getColumnIndex("AudioUserAge"));
				m_Data.AudioUserAge = userAge;
				
				String userOccupation = cursor.getString(cursor
						.getColumnIndex("AudioUserOccupation"));
				m_Data.AudioUserOccupation = userOccupation;
				
				String AudioCover = cursor.getString(cursor
						.getColumnIndex("AudioCover"));
				m_Data.AudioCover = AudioCover;
				
				
				int AudioType = cursor.getInt(cursor
						.getColumnIndex("AudioType"));
				m_Data.AudioType = AudioType;
				
				
				String AudioKey = cursor.getString(cursor
						.getColumnIndex("AudioKey"));
				m_Data.AudioKey = AudioKey;
				
				String AudioCoverKey = cursor.getString(cursor
						.getColumnIndex("AudioCoverKey"));
				m_Data.AudioCoverKey = AudioCoverKey;
				
				String UserProfileKey = cursor.getString(cursor
						.getColumnIndex("UserProfileKey"));
				m_Data.UserProfileKey = UserProfileKey;
				
				String AudioCoverSuolue = cursor.getString(cursor
						.getColumnIndex("AudioCoverSuolue"));
				m_Data.AudioCoverSuolue = AudioCoverSuolue;
				
				String AudioCoverKeySuolue = cursor.getString(cursor
						.getColumnIndex("AudioCoverKeySuolue"));
				m_Data.AudioCoverKeySuolue = AudioCoverKeySuolue;
				
				// 输出铃音信息
				if (m_Data.AudioUserId.equals("100050494")) {//香橙默认铃音
					m_returnList.add(m_Data);
				}
				
				cursor.moveToNext();
				
			}
		}
		
		cursor.close();
		
		return m_returnList;
		
	};
	
	
	/**
	 * 获取下载铃声列表信息
	 * @return
	 */
	public ArrayList<DownloadAudioInfo> GetAudioInfo(String Audiokey) {
		
		ArrayList<DownloadAudioInfo> m_returnList = new ArrayList<DownloadAudioInfo>();
		Cursor cursor = null;
		try{
		// 查询获得游标
		cursor = this.db.query(MyTabList.DOWNLOAD_AUDIO, null, "AudioKey = '"+Audiokey+"'", null, null,
				null, null);
		
		// 判断游标是否为空
		if (cursor.moveToFirst()) {
			
			// 遍历游标
			for (int i = 0; i < cursor.getCount(); i++) {
				
				DownloadAudioInfo m_Data = new DownloadAudioInfo();
				
				int id = cursor.getInt(cursor
						.getColumnIndex("_id"));
				m_Data.id = id;
				
				String title = cursor.getString(cursor
						.getColumnIndex("AudioTitle"));
				m_Data.title = title;
				
				String date = cursor.getString(cursor
						.getColumnIndex("AudioDownloadDate"));
				m_Data.downloadDate = date;
				
				int duration = cursor.getInt(cursor
						.getColumnIndex("AudioDuration"));
				m_Data.duration = duration;
				
				int downloads = cursor.getInt(cursor
						.getColumnIndex("AudioDownLoads"));
				m_Data.downloads = downloads;
				
				String path = cursor.getString(cursor
						.getColumnIndex("AudioLocalPath"));
				m_Data.path = path;
				
				
				
				String audioUserId = cursor.getString(cursor
						.getColumnIndex("AudioUserId"));
				m_Data.AudioUserId = audioUserId;
				
				String userProfile = cursor.getString(cursor
						.getColumnIndex("AudioUserProfile"));
				m_Data.AudioUserProfile = userProfile;
				
				String userNickname = cursor.getString(cursor
						.getColumnIndex("AudioUserNickName"));
				m_Data.AudioUserNickName = userNickname;
				
				String userAge = cursor.getString(cursor
						.getColumnIndex("AudioUserAge"));
				m_Data.AudioUserAge = userAge;
				
				String userOccupation = cursor.getString(cursor
						.getColumnIndex("AudioUserOccupation"));
				m_Data.AudioUserOccupation = userOccupation;
				
				String AudioCover = cursor.getString(cursor
						.getColumnIndex("AudioCover"));
				m_Data.AudioCover = AudioCover;
				
				
				int AudioType = cursor.getInt(cursor
						.getColumnIndex("AudioType"));
				m_Data.AudioType = AudioType;
				
				
				String AudioKey = cursor.getString(cursor
						.getColumnIndex("AudioKey"));
				m_Data.AudioKey = AudioKey;
				
				String AudioCoverKey = cursor.getString(cursor
						.getColumnIndex("AudioCoverKey"));
				m_Data.AudioCoverKey = AudioCoverKey;
				
				String UserProfileKey = cursor.getString(cursor
						.getColumnIndex("UserProfileKey"));
				m_Data.UserProfileKey = UserProfileKey;
				
				String AudioCoverSuolue = cursor.getString(cursor
						.getColumnIndex("AudioCoverSuolue"));
				m_Data.AudioCoverSuolue = AudioCoverSuolue;
				
				String AudioCoverKeySuolue = cursor.getString(cursor
						.getColumnIndex("AudioCoverKeySuolue"));
				m_Data.AudioCoverKeySuolue = AudioCoverKeySuolue;
				
				// 输出铃音信息
				if (m_Data.AudioUserId.equals("100050494")) {//香橙默认铃音
					m_returnList.add(m_Data);
				}
				
				cursor.moveToNext();
				
			}
		}
		
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor!=null)
				cursor.close();
		}
		
		return m_returnList;
		
	};
	
	/**
	 * 根据下载铃音的id获取铃音信息
	 * @param context
	 * @param audio_id
	 * @return
	 */
	public static DownloadAudioInfo getDownloadAudioInfoByPath(Context context,String audiopath){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor  = null;
		cursor= db.query(MyTabList.DOWNLOAD_AUDIO,null, "AudioLocalPath='"+audiopath+"'", null, null, null, null);   
		if (cursor!=null && cursor.moveToFirst()) {
			
			DownloadAudioInfo m_Data = new DownloadAudioInfo();
			
			int id = cursor.getInt(cursor
					.getColumnIndex("_id"));
			m_Data.id = id;
			
			String title = cursor.getString(cursor
					.getColumnIndex("AudioTitle"));
			m_Data.title = title;
			
			String date = cursor.getString(cursor
					.getColumnIndex("AudioDownloadDate"));
			m_Data.downloadDate = date;
			
			int duration = cursor.getInt(cursor
					.getColumnIndex("AudioDuration"));
			m_Data.duration = duration;
			
			int downloads = cursor.getInt(cursor
					.getColumnIndex("AudioDownLoads"));
			m_Data.downloads = downloads;
			
			String path = cursor.getString(cursor
					.getColumnIndex("AudioLocalPath"));
			m_Data.path = path;
			
			
			
			String audioUserId = cursor.getString(cursor
					.getColumnIndex("AudioUserId"));
			m_Data.AudioUserId = audioUserId;
			
			String userProfile = cursor.getString(cursor
					.getColumnIndex("AudioUserProfile"));
			m_Data.AudioUserProfile = userProfile;
			
			String userNickname = cursor.getString(cursor
					.getColumnIndex("AudioUserNickName"));
			m_Data.AudioUserNickName = userNickname;
			
			String userAge = cursor.getString(cursor
					.getColumnIndex("AudioUserAge"));
			m_Data.AudioUserAge = userAge;
			
			String userOccupation = cursor.getString(cursor
					.getColumnIndex("AudioUserOccupation"));
			m_Data.AudioUserOccupation = userOccupation;
			
			String AudioCover = cursor.getString(cursor
					.getColumnIndex("AudioCover"));
			m_Data.AudioCover = AudioCover;
			
			
			int AudioType = cursor.getInt(cursor
					.getColumnIndex("AudioType"));
			m_Data.AudioType = AudioType;
			
			
			String AudioKey = cursor.getString(cursor
					.getColumnIndex("AudioKey"));
			m_Data.AudioKey = AudioKey;
			
			String AudioCoverKey = cursor.getString(cursor
					.getColumnIndex("AudioCoverKey"));
			m_Data.AudioCoverKey = AudioCoverKey;
			
			String UserProfileKey = cursor.getString(cursor
					.getColumnIndex("UserProfileKey"));
			m_Data.UserProfileKey = UserProfileKey;
			
			
			String AudioCoverSuolue = cursor.getString(cursor
					.getColumnIndex("AudioCoverSuolue"));
			m_Data.AudioCoverSuolue = AudioCoverSuolue;
			
			String AudioCoverKeySuolue = cursor.getString(cursor
					.getColumnIndex("AudioCoverKeySuolue"));
			m_Data.AudioCoverKeySuolue = AudioCoverKeySuolue;
			
			cursor.close();
			return m_Data;
		}
		return null;
	}
	
	/**
	 * 获取发送失败的列表信息
	 * @return
	 */
	public ArrayList<SendAudioFaildInfo> GetSendFaildeAudioInfo(String userId) {
		
		ArrayList<SendAudioFaildInfo> m_returnList = new ArrayList<SendAudioFaildInfo>();
		
		// 查询获得游标
		Cursor cursor = this.db.query(MyTabList.SEND_FAILED_AUDIO, null, null, null, null,
				null, null);
		
		// 判断游标是否为空
		if (cursor.moveToFirst()) {
			
			// 遍历游标
			for (int i = 0; i < cursor.getCount(); i++) {
				
				SendAudioFaildInfo m_Data = new SendAudioFaildInfo();
				
				int id = cursor.getInt(cursor
						.getColumnIndex("_id"));
				m_Data.id = id;
				
				String user_int_id = cursor.getString(cursor
						.getColumnIndex("UserId"));
				m_Data.UserId = user_int_id;
				
				String target_int_id = cursor.getString(cursor
						.getColumnIndex("TargeId"));
				m_Data.TargeId = target_int_id;
				
				String title = cursor.getString(cursor
						.getColumnIndex("AudioTitle"));
				m_Data.AudioTitle = title;
				
				String timeString = cursor.getString(cursor
						.getColumnIndex("AudioTime"));
				m_Data.AudioTime = timeString;
				
				String themePicpath = cursor.getString(cursor
						.getColumnIndex("AudioCoverPath"));
				m_Data.AudioCoverPath = themePicpath;
				
				String audioPath = cursor.getString(cursor
						.getColumnIndex("AudioLocalPath"));
				m_Data.AudioLocalPath = audioPath;
				
				int audioType = cursor.getInt(cursor
						.getColumnIndex("AudioType"));
				m_Data.AudioType = audioType;
				String toNickName = cursor.getString(cursor
						.getColumnIndex("toNickName"));
				m_Data.toNickName = toNickName;
				
				
//				+ "	'UserId' TEXT,"				//用户id
//				+ "	'TargeId' TEXT,"				//用户id
//				+ "	'AudioTitle' TEXT,"			//铃声的标题
//				+ "	'AudioTime'  TEXT,"			//铃音时间  格式为09:20
//				+ "	'AudioCoverPath'  TEXT,"	//铃音封面路径
//				+ " 'AudioLocalPath' TEXT"		//铃声保存路径
				
				// 输出铃音信息
				if (m_Data.UserId.equals(userId)) {//登录用户发送失败的铃音
					m_returnList.add(m_Data);
				}
				
				cursor.moveToNext();
				
			}
		}
		
		cursor.close();
		
		return m_returnList;
		
	};
	
	
	public static class SaveRecordAudioClass{
		public String userId = "";			//登录用户的id
		public String AudioTitle = "";		//铃声标题
		public String AudioRecordDate = ""; //录制日期
		public int AudioDuration = 0;		//铃声时长 单位为秒
		public String AudioThemePic = "";	//铃声主题图片
		public String AudioLocalPath = ""; 	//铃声本地保存路径
		public SaveRecordAudioClass() {
		}
		
	}
	
	/**
	 * 保存录制的铃声
	 * @param mSaveRecordAudioClass
	 */
	public void saveRecordInfo(SaveRecordAudioClass mSaveRecordAudioClass) {
		ContentValues values = new ContentValues();
		values.put("UserId", mSaveRecordAudioClass.userId);
		values.put("AudioTitle", mSaveRecordAudioClass.AudioTitle);
		values.put("AudioRecordDate", mSaveRecordAudioClass.AudioRecordDate);
		values.put("AudioDuration", mSaveRecordAudioClass.AudioDuration);
		values.put("AudioThemePic", mSaveRecordAudioClass.AudioThemePic);
		values.put("AudioLocalPath", mSaveRecordAudioClass.AudioLocalPath);
		insert(values);
	}
	
	/**
	 * 查询是否为本地录制的铃声
	 * @param path
	 * @return
	 */
	public String isLocalRecord(String path){
		String[] column = new String[]{"AudioLocalPath"};
		String selection = "AudioLocalPath=?";
		String[] selectionArgs = new String[]{path};
		return query(column, selection, selectionArgs);
	}
	
	/**
	 * 保存下载铃声信息
	 * 
	 */
	public void saveDownloadInfo(DownloadAudioInfoClass downloadAudioInfoClass) {
		ContentValues values = new ContentValues();
		values.put("LoginUserId", downloadAudioInfoClass.loginUserId);
		values.put("AudioTitle", downloadAudioInfoClass.AudioTitle);
		values.put("AudioDownloadDate", downloadAudioInfoClass.AudioDownloadDate);
		values.put("AudioDuration", downloadAudioInfoClass.AudioDuration);
		values.put("AudioDownLoads", downloadAudioInfoClass.AudioDownLoads);
		values.put("AudioLocalPath", downloadAudioInfoClass.AudioLocalPath);
		
		values.put("AudioUserId", downloadAudioInfoClass.AudioUserId);
		values.put("AudioUserProfile", downloadAudioInfoClass.AudioUserProfile);
		values.put("AudioUserNickName", downloadAudioInfoClass.AudioUserNickName);
		values.put("AudioUserAge", downloadAudioInfoClass.AudioUserAge);
		values.put("AudioUserSex", downloadAudioInfoClass.AudioUserSex);
		values.put("AudioUserOccupation", downloadAudioInfoClass.AudioUserOccupation);
		values.put("AudioCover", downloadAudioInfoClass.AudioCover);
		
		values.put("AudioType", downloadAudioInfoClass.AudioType);
		
		values.put("AudioKey", downloadAudioInfoClass.AudioKey);
		values.put("AudioCoverKey", downloadAudioInfoClass.AudioCoverKey);
		values.put("UserProfileKey", downloadAudioInfoClass.UserProfileKey);
		
		values.put("AudioCoverSuolue", downloadAudioInfoClass.AudioCoverSuolue);
		values.put("AudioCoverKeySuolue", downloadAudioInfoClass.AudioCoverKeySuolue);
		
		insert(values);
		
		
	}
	/**
	 * 保存发送失败的铃声信息
	 * 
	 */
	public void saveSendFaildAudioInfo(SendAudioFaildInfo sendAudioFaildInfo) {
		ContentValues values = new ContentValues();
		values.put("UserId", sendAudioFaildInfo.UserId);
		values.put("TargeId", sendAudioFaildInfo.TargeId);
		values.put("AudioTitle", sendAudioFaildInfo.AudioTitle);
		values.put("AudioTime", sendAudioFaildInfo.AudioTime);
		values.put("AudioCoverPath", sendAudioFaildInfo.AudioCoverPath);
		values.put("AudioLocalPath", sendAudioFaildInfo.AudioLocalPath);
		values.put("AudioType", sendAudioFaildInfo.AudioType);
		values.put("toNickName", sendAudioFaildInfo.toNickName);
		insert(values);
	}
	
	/**
	 * 修改下载铃声信息
	 * 
	 */
	public void updateDownloadInfo(Context context,String path,int audioType) {
		
		ContentValues values = new ContentValues();
		values .put("AudioType", audioType);
		
		String whereClause = "AudioLocalPath=?";
		String[] whereArgs = {path};
		update(values, whereClause, whereArgs);
	}
	public void updateDownloadInfo(Context context,RingtoneInfo ringInfo) {
		
		ContentValues values = new ContentValues();
		values.put("AudioUserNickName", ringInfo.nickname);
		values.put("AudioUserProfile", ringInfo.profile);
		values.put("UserProfileKey", ringInfo.profileKey);
		
		String whereClause = "AudioUserId=?";
		String[] whereArgs = {String.valueOf(ringInfo.int_id)};
		update(values, whereClause, whereArgs);
	}
	/**
	 * 删除铃声信息
	 * @param id
	 */
	public void deleteAudioInfo(String id){
		String whereClause = "_id=?";
		String[] whereArgs = {id};
		delete(whereClause, whereArgs);
	}
	
	public String searchAudioPathByDate(String date){
		String[] column = new String[]{"AudioLocalPath"};
		String selection = "AudioDownloadDate=?";
		String[] selectionArgs = new String[]{date};
		return query(column, selection, selectionArgs);
	}
	
	/**
	 * 获取下载铃声的本地存储路径
	 * @param context
	 * @param date
	 * @return
	 */
	public static String getAudioPathByDate(Context context ,final String date) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		AudioDAO audioDAO = new AudioDAO(db,MyTabList.DOWNLOAD_AUDIO);
		return audioDAO.searchAudioPathByDate(date);
		
	}
}
