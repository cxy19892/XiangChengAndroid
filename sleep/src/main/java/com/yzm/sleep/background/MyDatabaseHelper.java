package com.yzm.sleep.background;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.utils.DateOperaUtil;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;

public class MyDatabaseHelper extends SQLiteOpenHelper{	
	private static Context context;
	private static final String TAG="MyDatabaseHelper";
	private static MyDatabaseHelper Instance;
	
	private MyDatabaseHelper(){
		super(context, MyTabList.DATABASENAME, null, MyTabList.DATABASEVERSION);
	}
	
	public synchronized static MyDatabaseHelper getInstance(Context c){
		context = c.getApplicationContext();
		if(Instance == null){
			Instance = new MyDatabaseHelper();
		}
		return Instance;
	}

	private SQLiteDatabase mDatabase;
	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		mDatabase.close();
		mDatabase = null;
		super.close();
	}


	/**
	 * 将sqlite数据移到sd卡下面
	 */
	@SuppressLint("NewApi")
	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
//		super.getWritableDatabase();
		if(mDatabase != null) {
			if(!mDatabase.isOpen()) {
				mDatabase = null;
			}else {
				return mDatabase;	
			}
		}
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(context, R.string.sdk_error, Toast.LENGTH_SHORT).show();
			return mDatabase;
		}
		String sdkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/database";
		String dbPath = sdkPath + "/" + MyTabList.DATABASENAME;
		File sdkf = new File(sdkPath);
		File dbf = new File(dbPath);
		if(!sdkf.exists()) {
			sdkf.mkdirs();
		}
		boolean isFileCreateSuccess=false;
		if(!dbf.exists()) {
			try {
				isFileCreateSuccess=dbf.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(isFileCreateSuccess||mDatabase == null) {//15/6/5 修改71行空指针  42行置空了 可能过掉这里后面直接用
			try {
				mDatabase = SQLiteDatabase.openOrCreateDatabase(dbf, null);
			} catch (Exception e) {
			}
		}
		
		final int version = mDatabase.getVersion();
		LogUtil.i(TAG,"测试版本号：version "+version+" databaseversion:"+ MyTabList.DATABASEVERSION);
        if (version != MyTabList.DATABASEVERSION) {
        	mDatabase.beginTransaction();
            try {
                if (version == 0) {
                    onCreate(mDatabase);
                } else {
                    if (version > MyTabList.DATABASEVERSION) {
                        onDowngrade(mDatabase, version, MyTabList.DATABASEVERSION);
                    } else {
                        onUpgrade(mDatabase, version, MyTabList.DATABASEVERSION);
                    }
                }
                mDatabase.setVersion(MyTabList.DATABASEVERSION);
                mDatabase.setTransactionSuccessful();
            } finally {
            	mDatabase.endTransaction();
            }
        }
        onOpen(mDatabase);
        return mDatabase;
	}

	/* (非 Javadoc) 
	* <p>Title: onCreate</p> 
	* <p>Description: </p> 
	* @param db 
	* @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase) 
	*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String dataSql = "create table "+MyTabList.SLEEPDATA+"(time varchar(100) not null," +//格式为long
				"date varchar(100) not null," +				//日期，同下，格式yyyy-MM-dd
				"x varchar(100) not null," +
				"y varchar(100) not null," +
				"z varchar(100) not null)";
		String timeSql = "create table "+MyTabList.SLEEPTIME+"(date varchar(100) not null," +
				"sleeptime varchar(100)," +				//格式HH : mm
				"uptime varchar(100)," +				//格式HH : mm
				"orgsleeptime varchar(100)," +				//格式HH : mm
				"orguptime varchar(100)," +				//格式HH : mm
				"sleeptimelong varchar(100)," +				//格式long
				"uptimelong varchar(100)," +				//格式long
				"sleepacce varchar(100)," +	//入睡时刻加速度 float
				"upacce varchar(100)," +		//起床时刻加速度float
				"sleeplength varchar(100)," +			//睡眠时长， 单位为分钟数，比如01:30，则为90
				"healthlength varchar(100)," +			//美容觉时长， 单位为分钟数，比如01:30，则为90
				"starttime varchar(100)," +		//为分钟数，比如01:30，则为90
				"endtime varchar(100)," +		//为分钟数，比如01:30，则为90
				"diagramdata varchar(100)," +	//记录曲线图数据，以json的数据格式
				"isupload varchar(100) default 0," +	//0代表没有上传，1代表已经上传
				"fileid varchar(100)," +	//日视图文案id
				"record_state varchar(100) default 1," +	//加速度记录状态，默认为1,1表示当前周期还未开始记录加速度
															//2代表当前周期正在记录加速度
															//3代表当前周期已经结束加速度记录，但是睡眠结果尚未给出
															//4代表周期已经结束加速度，睡眠结果已经给出
				"ischange varchar(100) default 0,"    //为1的时候，则表示数据被修改过		
				+ "ispunch varchar(100) default 0)";  //为0的时候表示还未打卡，打卡过后修改为1											
		String weekSql = "create table "+MyTabList.WEEK+"(weeknumber varchar(100)," +	
				"sleeptime varchar(100),"+
				"sleepid varchar(100),"+
				"avgtime varchar(100),"+
				"avgid varchar(100),"+
				"uptime varchar(100),"+
				"upid varchar(100))"; 
		
		String	testSql = "create table test(downtime varchar(100),downtimeinfo varchar(100),accstarttime varchar(100))";
	
//		/**
//		 * 创建闹钟信息表
//		 * */
//		String alarmSql = "CREATE TABLE "+ MyTabList.ALARMS +"(" +
//                "_id INTEGER PRIMARY KEY," +
//                "hour INTEGER, " +
//                "minutes INTEGER, " +
//                "daysofweek INTEGER, " +
//                "alarmtime INTEGER, " +
//                "enabled INTEGER, " +
//                "vibrate INTEGER, " +
//                "message TEXT, " +
//                "alert TEXT);";
		
		
		String alarmSetSqlString=createAlarmSetTable();
		
		db.execSQL(alarmSetSqlString);	
		
		//创建录制铃声信息表
		String audioRecordSqlString = createRecordAudioTable();
		db.execSQL(audioRecordSqlString);
		
		//创建下载铃声信息表
		String audioDownloadSqlString = createDownLoadAudioTable();
		db.execSQL(audioDownloadSqlString);
		
		
		//创建发送铃音失败信息表
		String audioSendFailedString = createSendFaildAudioTable();
		db.execSQL(audioSendFailedString);
		
		db.execSQL(dataSql);
		db.execSQL(timeSql);
		db.execSQL(weekSql);
		db.execSQL(testSql);
//		db.execSQL(alarmSql);
//    	db.execSQL(createPillowFileTable());
//		db.execSQL(createPillowDAY());
		
		String newPillowSql = "CREATE TABLE " + MyTabList.PILLOW_SLEEP_DATA 
				+ "	(_id INTEGER PRIMARY KEY, date TEXT, isUpload TEXT, fileName TEXT, datIsUpload TEXT, datFileName TEXT, model BLOB)"	;			//时间 2015-06-08格式
		db.execSQL(newPillowSql);
		
		String secretaryMsgSql = "CREATE TABLE " + MyTabList.SECRETARY_MSG_LIST + "	(_id INTEGER PRIMARY KEY, "
				+ "id TEXT, "
				+ "type INTEGER, "
				+ "content TEXT, "
				+ "date TEXT, "
				+ "title TEXT, "
				+ "link TEXT, "
				+ "scan INTEGER)"; 
		db.execSQL(secretaryMsgSql);
		
		//新增签到表
		String newSoftData = "CREATE TABLE " + MyTabList.NEW_SLEEPTIME + "(_id INTEGER PRIMARY KEY, "
				+ "date TEXT, "                                //签到数据所属日期
				+ "sign_id TEXT, "                             //签到Id
				+ "go_bed_time TEXT, "                         //上床时间
				+ "try_sleep_time TEXT, "					   //尝试入睡时间
				+ "how_long_sleep_time INTEGER DEFAULT 0, "	   //多长时间入睡 ，单位:分钟
				+ "wake_count INTEGER DEFAULT 0, "			   //醒来次数
				+ "how_long_wake_time INTEGER DEFAULT 0,"	   //醒来总时长， 单位：分钟
				+ "wake_up_time TEXT, "						   //醒来时间
				+ "wake_early_time INTEGER DEFAULT 0, "		   //比计划提前多久醒来：单位：分钟
				+ "out_bed_time TEXT, "                        //起床时间
				+ "soft_or_orange TEXT, "                      // 0代表软件，1代表硬件
				+ "orange_data_upload INTEGER DEFAULT 0,"       // 硬件数据是否签到成功 0签到成功 1表示签到不成功，方便补签
				+ "deep_sleep INTEGER DEFAULT 0, "             //深度睡眠时长，单位：分钟
				+ "shallow_sleep INTEGER DEFAULT 0, "          //浅度睡眠时长，单位：分钟
				+ "result TEXT)";						   
		db.execSQL(newSoftData);
//		//一周反馈是否提交
//		String feedbackTable = "CREATE TABLE " + MyTabList.FEEDBACK + "(_id INTEGER PRIMARY KEY, "
//				+ "feedback_date TEXT, "
//				+ "iscommit TEXT DEFAULT 0"     //0：代表还未提醒反馈， 1：取消反馈， 2:已经反馈
//				+ ")";
//		db.execSQL(feedbackTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		//9 以后升级数据库执行新标准
		if(oldVersion<8)
		{
			String dataSql = "drop table if exists "+MyTabList.SLEEPDATA;
			String timeSql = "drop table if exists "+MyTabList.SLEEPTIME;
			String weekSql = "drop table if exists "+MyTabList.WEEK;
			String testSql = "drop table if exists test";
//			String alarmSql = "drop table if exists " + MyTabList.ALARMS;
			db.execSQL(dataSql);
			db.execSQL(timeSql);
			db.execSQL(weekSql);
			db.execSQL(testSql);
//			db.execSQL(alarmSql);
			this.onCreate(db);
			
		}else if(oldVersion>=8)
		{	
			for (int i = oldVersion; i < newVersion; i++) 
			{				
		            switch (i) 
		            {
			            case 8:  //8升级到9 增加闹钟创建表
	            			String alarmSetSqlString=createAlarmSetTable();		        		
	            			db.execSQL(alarmSetSqlString);	
	            			
	            			//创建录制铃声信息表
	            			String audioRecordSqlString = createRecordAudioTable();
	            			db.execSQL(audioRecordSqlString);
	            			
	            			//创建下载铃声信息表
	            			String audioDownloadSqlString = createDownLoadAudioTable();
	            			db.execSQL(audioDownloadSqlString);
	            			break;
			            case 9:	//9升级到10
			            	/*
			            	 * 修改 DOWNLOAD_AUDIO
			            	 * */
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.DOWNLOAD_AUDIO, "AudioType"))
			            	{
			            		String alterDownloadSqlString1 = "ALTER TABLE " + MyTabList.DOWNLOAD_AUDIO +
			            				" ADD AudioType INTEGER DEFAULT 0";
			            		db.execSQL(alterDownloadSqlString1);
			            	}
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.DOWNLOAD_AUDIO, "AudioKey"))
			            	{
			            		String alterDownloadSqlString2 = "ALTER TABLE " + MyTabList.DOWNLOAD_AUDIO +
			            				" ADD AudioKey TEXT DEFAULT '0'";
			            		db.execSQL(alterDownloadSqlString2);
			            	}
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.DOWNLOAD_AUDIO, "AudioCoverKey"))
			            	{
			            		String alterDownloadSqlString3 = "ALTER TABLE " + MyTabList.DOWNLOAD_AUDIO +
			            				" ADD AudioCoverKey TEXT DEFAULT '0'";
			            		db.execSQL(alterDownloadSqlString3);
			            	}
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.DOWNLOAD_AUDIO, "UserProfileKey"))
			            	{
			            		String alterDownloadSqlString4 = "ALTER TABLE " + MyTabList.DOWNLOAD_AUDIO +
			            				" ADD UserProfileKey TEXT DEFAULT '0'";
			            		db.execSQL(alterDownloadSqlString4);
			            	}
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.DOWNLOAD_AUDIO, "AudioCoverSuolue"))
			            	{
			            		String alterDownloadSqlString5 = "ALTER TABLE " + MyTabList.DOWNLOAD_AUDIO +
			            				" ADD AudioCoverSuolue TEXT DEFAULT '0'";
			            		db.execSQL(alterDownloadSqlString5);
			            	}
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.DOWNLOAD_AUDIO, "AudioCoverKeySuolue"))
			            	{
			            		String alterDownloadSqlString6 = "ALTER TABLE " + MyTabList.DOWNLOAD_AUDIO +
			            				" ADD AudioCoverKeySuolue TEXT DEFAULT '0'";
			            		db.execSQL(alterDownloadSqlString6);
			            	}
			            	
			            	/*
			            	 * 修改 MyTabList.ALARM_TIME
			            	 * */
			            	
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.ALARM_TIME, "AudioIsLocalRecord"))
			            	{
			            		String alterAlarmTimeSqlString0 = "ALTER TABLE " + MyTabList.ALARM_TIME +
			            				" ADD AudioIsLocalRecord INTEGER DEFAULT 0";
			            		db.execSQL(alterAlarmTimeSqlString0);
			            	}
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.ALARM_TIME, "AlarmType"))
			            	{
			            		String alterAlarmTimeSqlString1 = "ALTER TABLE " + MyTabList.ALARM_TIME +
			            				" ADD AlarmType INTEGER DEFAULT 0";
			            		db.execSQL(alterAlarmTimeSqlString1);
			            	}
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.ALARM_TIME, "AudioKey"))
			            	{
			            		String alterAlarmTimeSqlString2 = "ALTER TABLE " + MyTabList.ALARM_TIME + 
			            				" ADD AudioKey TEXT DEFAULT '0'";
			            		db.execSQL(alterAlarmTimeSqlString2);
			            	}
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.ALARM_TIME, "AudioCoverKey"))
			            	{
			            		String alterAlarmTimeSqlString3 = "ALTER TABLE " + MyTabList.ALARM_TIME +
			            				" ADD AudioCoverKey TEXT DEFAULT '0'";
			            		db.execSQL(alterAlarmTimeSqlString3);
			            	}
			            	if(!checkColumnExist1(this.getWritableDatabase(), MyTabList.ALARM_TIME, "UserProfileKey"))
			            	{
			            		String alterAlarmTimeSqlString4 = "ALTER TABLE " + MyTabList.ALARM_TIME +
			            				" ADD UserProfileKey TEXT DEFAULT '0'";
			            		db.execSQL(alterAlarmTimeSqlString4);
			            	}
			            	
//			            	+ " 'AudioKey' TEXT,"		//铃音key(七牛中的key)
//							+ " 'AudioCoverKey' TEXT"		//铃音封面key
			            	
			            	//创建发送铃音失败信息表
			        		String audioSendFailedString = createSendFaildAudioTable();
			        		
			        		db.execSQL(audioSendFailedString);
			        		
			        		
	            			break;
			            case 10: //10升级到11
			            	
//			            	String usersPillowString = createPillowTable();
			            	db.execSQL(createPillowFileTable());
			            	db.execSQL(createPillowDAY());
			        		clearLoginMessage();
			            	break;
			            	
			            case 12:  //12升级13时，把以前的无用的硬件数据的2张表给卸载掉，创建新的硬件数据表
			            	String pillowSql = "drop table if exists " + MyTabList.PILLOW_SLEEP_DATA;
			    			String modelSql = "drop table if exists " + MyTabList.MODLE_PILLOWS_FILE;
			    			db.execSQL(pillowSql);
			    			db.execSQL(modelSql);
			    			String newPillowSql = "CREATE TABLE " + MyTabList.PILLOW_SLEEP_DATA 
			    					+ "	(_id INTEGER PRIMARY KEY, date TEXT, isUpload TEXT, fileName TEXT, datIsUpload TEXT, datFileName TEXT, model BLOB)"	;			//时间 2015-06-08格式
			    			db.execSQL(newPillowSql);
			            	break;
			            	
			            case 13: //13升级14.在软件睡眠数据表中加入两个带日期的long型时间点(入睡和起床)
			            	String softDataSql = "alter table " + MyTabList.SLEEPTIME + " add sleeptimelong varchar(100)";
			            	db.execSQL(softDataSql);
			            	
			            	String softDataSql5 = "alter table " + MyTabList.SLEEPTIME + " add uptimelong varchar(100)";
			            	db.execSQL(softDataSql5);
			            	dataToData(db);
			            	break;
			            	
			            case 14: //14升级15   
//			            	1.在闹铃提醒上加入记事提醒所需的字段表
			            	String audioInsert0 = "alter table " + MyTabList.ALARM_TIME + " add IsRemind INTEGER";
			        		String audioInsert1 = "alter table " + MyTabList.ALARM_TIME + " add RemindTitle TEXT";
			        		String audioInsert2 = "alter table " + MyTabList.ALARM_TIME + " add RemindContext TEXT";
			            	db.execSQL(audioInsert0);
			            	db.execSQL(audioInsert1);
			            	db.execSQL(audioInsert2);
			            	
//			            	2.给日视图的数据表增加是否打卡的字段
			            	String dayDataInsert = "alter table " + MyTabList.SLEEPTIME + " add ispunch varchar(100) default 0";
			            	db.execSQL(dayDataInsert);
			            	
//			            	3.增加香橙小秘书的消息列表
			            	String secretaryMsgSql = "CREATE TABLE " + MyTabList.SECRETARY_MSG_LIST + "	(_id INTEGER PRIMARY KEY, "
			        				+ "id TEXT, "
			        				+ "type INTEGER, "
			        				+ "content TEXT, "
			        				+ "date TEXT, "
			        				+ "title TEXT, "
			        				+ "link TEXT, "
			        				+ "scan INTEGER)"; 
			        		db.execSQL(secretaryMsgSql);
			            	break;
			            case 15: //15升级16
			            	String newSoftData = "CREATE TABLE " + MyTabList.NEW_SLEEPTIME + "(_id INTEGER PRIMARY KEY, "
			            			+ "date TEXT, "   
			            			+ "sign_id TEXT, "                            
			        				+ "go_bed_time TEXT, "
			        				+ "try_sleep_time TEXT, "
			        				+ "how_long_sleep_time INTEGER DEFAULT 0, "
			        				+ "wake_count INTEGER DEFAULT 0, "
			        				+ "how_long_wake_time INTEGER DEFAULT 0, "
			        				+ "wake_up_time TEXT, "
			        				+ "wake_early_time INTEGER DEFAULT 0, "
			        				+ "out_bed_time TEXT, "
			        				+ "soft_or_orange TEXT, "
			        				+ "orange_data_upload INTEGER DEFAULT 0,"
			        				+ "deep_sleep INTEGER DEFAULT 0, "
			        				+ "shallow_sleep INTEGER DEFAULT 0, "
			        				+ "result TEXT)";	
			        		db.execSQL(newSoftData);
//			        		String feedbackTable = "CREATE TABLE " + MyTabList.FEEDBACK + "(_id INTEGER PRIMARY KEY, "
//			        				+ "feedback_date TEXT, "
//			        				+ "iscommit TEXT DEFAULT 0"
//			        				+ ")";
//			        		db.execSQL(feedbackTable);
			            	break;
			            default:
	            			break;
		            }

		      }		
			
		}
	
	}
	
	/**
	 * 数据库版本13升级14的时候，因为加入sleeptimelong和uptimelong两个字段，所以要做数据库数据迁移
	 * @param db
	 */
	@SuppressLint("SimpleDateFormat") 
	private void dataToData(SQLiteDatabase db){
		try {
			String sqlSelct = "select * from day where record_state ='4'";
			Cursor c = db.rawQuery(sqlSelct, null);
			List<SleepResult> list = new ArrayList<SleepResult>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			while (c.moveToNext()) {
				SleepResult result = new SleepResult();
				String date = c.getString(c.getColumnIndex("date"));
				String st = c.getString(c.getColumnIndex("sleeptime"));
				String et = c.getString(c.getColumnIndex("uptime"));
				if(!TextUtils.isEmpty(st) && !TextUtils.isEmpty(et)){
					SoftDayData dayData = new SoftDayData();
					dayData.setDate(date);
					dayData.setSleepTime(st);
					dayData.setGetUpTime(et);
					dayData.setSleepTimeLong(String.valueOf(sdf.parse(date + " " + st).getTime()));
					dayData.setGetUpTimeLong(String.valueOf(sdf.parse(date + " " + et).getTime()));
					dayData = DateOperaUtil.comperaDate(dayData);
					
					result.setDate(date);
					result.setSleeptimelong(dayData.getSleepTimeLong());
					result.setUptimelong(dayData.getGetUpTimeLong());
					list.add(result);
				}
			}
			for (int i = 0; i < list.size(); i++) {
				SleepResult result = list.get(i);
				ContentValues values = new ContentValues();
				values.put("sleeptimelong", result.getSleeptimelong());
				values.put("uptimelong", result.getUptimelong());
				db.update(MyTabList.SLEEPTIME, values, "date=?", new String[]{result.getDate()});
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	private void clearLoginMessage(){
		PreManager.instance().saveIsLogin(context, false);
	}

	private String createAlarmSetTable() {
		// TODO Auto-generated method stub
		String alarmSql = 
//
//		"CREATE TABLE alarmtime (_id  int(11) NOT NULL AUTO_INCREMENT,  "+
//				"`AlarmTime` varchar(20) DEFAULT NULL COMMENT '闹钟时间例如12:00',"+
//		  "`AlarmRepeat` decimal(10,0) DEFAULT NULL COMMENT '重复周期为0表示不重复。为1表示重复',"+
//		 " `AlarmPlant` varchar(255) DEFAULT NULL COMMENT 'AlarmRepeat 标识为1   周期 ：1234567',"+
//		 " `AlarmTitle` varchar(255) DEFAULT NULL COMMENT 'AlarmTitle Title ....',"+
//		 " `AlarmDay` varchar(20) DEFAULT NULL COMMENT 'AlarmRepeat标识为0时  时间是年月日  ',"+
//		 " `AlarmDelay` decimal(10,0) unsigned zerofill DEFAULT NULL COMMENT '小睡时长 为0表示不小睡',"+
//		 " `AlarmAudio` varchar(255) DEFAULT NULL COMMENT '闹钟铃声',"+
//		  "`AlarmOnOff` int(11) DEFAULT NULL COMMENT '启动开关 0 表示未启动  1表示启动',"+
//		 " PRIMARY KEY (`_id`)";		
		
		
		"	CREATE TABLE alarmtime(	'_id'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
		+ "	'AlarmTime' TEXT,"
		+ "	'AlarmRepeat'  INTEGER,"
		+ "	'AlarmPlant'  TEXT,"
		+ "	'AlarmTitle'  TEXT,"
		+ "	'AlarmDay'  TEXT,"
		+ "	'AlarmDelay'  INTEGER,"
		+ "	'AlarmAudio'  TEXT,"
		+ "	'AlarmOnOff'  INTEGER,"
		+ " 'AlarmProfile' TEXT,"	//铃声对应用户头像
		+ " 'AlarmUserNickname' TEXT,"	//铃声对应用户昵称
		+ " 'AlarmUserId' TEXT,"	//铃声对应用户id
		+ " 'AlarmAudioId' INTEGER,"	//铃声响铃信息id
		+ " 'AlarmAudioCover' TEXT,"	//铃声响铃封面
		+ " 'AlarmDownloads' INTEGER,"  //铃声历史下载次数
		+ " 'AlarmType' INTEGER DEFAULT 0,"		//闹钟类型 0 - 非专属铃音闹钟 ， 1 - 普通闹钟  ，2-神秘闹钟
		
		+ " 'AudioIsLocalRecord' INTEGER DEFAULT 0,"		//是否为本地录制铃音  0- 不是  1-是
		+ " 'AudioKey' TEXT,"		//铃音key(七牛中的key)
		+ " 'AudioCoverKey' TEXT,"		//铃音封面key
		+ " 'UserProfileKey' TEXT,"		//用户头像key
		+ " 'IsRemind' INTEGER,"
		+ " 'RemindTitle' TEXT,"
		+ " 'RemindContext' TEXT"
		+ ")"; 
		
		
		
		return alarmSql;
		
	}
	
	private String createDownLoadAudioTable() {
		String downLoadAudioSql = "CREATE TABLE " + MyTabList.DOWNLOAD_AUDIO 
				+ " ('_id'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "	'LoginUserId' TEXT,"				//登录用户id
				+ "	'AudioTitle' TEXT,"				//下载铃声的标题
				+ "	'AudioDownloadDate'  TEXT,"		//下载时间 格式yyyy-MM-dd
				+ "	'AudioDuration'  INTEGER,"		//时长 单位为毫秒
				+ "	'AudioDownLoads'  INTEGER,"		//下载次数
				
				+ "	'AudioUserId'  TEXT,"		//下载铃声对应用户账号id
				+ "	'AudioUserProfile'  TEXT,"		//下载铃声对应用户头像地址
				+ "	'AudioUserNickName'  TEXT,"		//下载铃声对应用户昵称
				+ "	'AudioUserAge'  TEXT,"		//下载铃声对应用户年龄
				+ "	'AudioUserSex'  TEXT,"		//下载铃声对应用户性别
				+ "	'AudioUserOccupation'  TEXT,"		//下载铃声对应用户职业
				+ "	'AudioCover'  TEXT,"	// 铃声封面
				+ " 'AudioLocalPath' TEXT,"//下载铃声保存路径
				+ " 'AudioType' INTEGER DEFAULT 0,"		//铃音类型 0 - 普通铃音 ， 1 - 神秘铃音
				
				+ " 'AudioKey' TEXT,"		//铃音key(七牛中的key)
				+ " 'AudioCoverKey' TEXT,"		//铃音封面key
				+ " 'UserProfileKey' TEXT,"		//用户头像key
				
				+ "	'AudioCoverSuolue'  TEXT,"	// 铃声封面
				+ " 'AudioCoverKeySuolue' TEXT"		//铃音封面key
				
				+ ")"; 		
		return downLoadAudioSql;
	}
	private String createDownLoadAudioTableV8() {
		String downLoadAudioSql = "CREATE TABLE " + MyTabList.DOWNLOAD_AUDIO 
				+ " ('_id'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "	'LoginUserId' TEXT,"				//登录用户id
				+ "	'AudioTitle' TEXT,"				//下载铃声的标题
				+ "	'AudioDownloadDate'  TEXT,"		//下载时间 格式yyyy-MM-dd
				+ "	'AudioDuration'  INTEGER,"		//时长 单位为毫秒
				+ "	'AudioDownLoads'  INTEGER,"		//下载次数
				
				+ "	'AudioUserId'  TEXT,"		//下载铃声对应用户账号id
				+ "	'AudioUserProfile'  TEXT,"		//下载铃声对应用户头像地址
				+ "	'AudioUserNickName'  TEXT,"		//下载铃声对应用户昵称
				+ "	'AudioUserAge'  TEXT,"		//下载铃声对应用户年龄
				+ "	'AudioUserSex'  TEXT,"		//下载铃声对应用户性别
				+ "	'AudioUserOccupation'  TEXT,"		//下载铃声对应用户职业
				+ "	'AudioCover'  TEXT,"	// 铃声封面
				+ " 'AudioLocalPath' TEXT,"//下载铃声保存路径
				+ " 'AudioType' INTEGER DEFAULT 0,"		//铃音类型 0 - 普通铃音 ， 1 - 神秘铃音
				
				+ " 'AudioKey' TEXT,"		//铃音key(七牛中的key)
				+ " 'AudioCoverKey' TEXT,"		//铃音封面key
				+ " 'UserProfileKey' TEXT,"		//用户头像key
				
				+ "	'AudioCoverSuolue'  TEXT,"	// 铃声封面
				+ " 'AudioCoverKeySuolue' TEXT"		//铃音封面key
				
				+ ")"; 		
		return downLoadAudioSql;
	}
	
	private String createRecordAudioTable() {
		String recordAudioSql = "CREATE TABLE " + MyTabList.RECORD_AUDIO 
						+ " ('_id'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
						+ "	'UserId' TEXT,"				//用户id
						+ "	'AudioTitle' TEXT,"			//铃声的标题
						+ "	'AudioRecordDate'  TEXT,"	//录制时间 格式yyyy-MM-dd
						+ "	'AudioDuration'  INTEGER,"	//时长 单位为毫秒
						+ "	'AudioThemePic'  TEXT,"	// 铃声主题
						+ " 'AudioLocalPath' TEXT)";    //铃声保存路径
		return recordAudioSql;
	}
	
	private String createSendFaildAudioTable() {
		String recordAudioSql = "CREATE TABLE " + MyTabList.SEND_FAILED_AUDIO 
				+ " ('_id'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "	'UserId' TEXT,"				//用户id
				+ "	'TargeId' TEXT,"				//用户id
				+ "	'AudioTitle' TEXT,"			//铃声的标题
				+ "	'AudioTime'  TEXT,"			//铃音时间  格式为09:20
				+ "	'AudioCoverPath'  TEXT,"	//铃音封面路径
				+ " 'AudioLocalPath' TEXT,"		//铃声保存路径
				+ " 'AudioType' INTEGER,"		//铃声类型 1 - 普通，2 - 神秘
				+ " 'toNickName' TEXT"		//接收人昵称
				+ ")";    
		return recordAudioSql;
	}
	
	
	private String createPillowFileTable() {
		String recordAudioSql = "CREATE TABLE " + MyTabList.MODLE_PILLOWS_FILE 
				+ "	(mlTotalTime varchar(100),"	
				+ " pillow_mtime varchar(100),"
				+ " pillow_fliename varchar(100),"
				+ " isupload varchar(10)"
				+ ")";    
		return recordAudioSql;
	}
	
	private String createPillowDAY() {
		String recordAudioSql = "CREATE TABLE " + MyTabList.PILLOW_SLEEP_DATA 
				+ "	(Time varchar(100),"				//时间 2015-06-08格式
				+ " XStart varchar(100),"				
				+ "	XStop varchar(100),"			
				+ "	YMax varchar(100),"	
				+ "	InSleepTime varchar(100),"	
				+ "	OutSleepTime varchar(100),"	
				+ "	TotalSleepTime varchar(100),"	
				+ "	Deep_Sleep varchar(100),"	
				+ "	Shallow_Sleep varchar(100),"	
				+ "	AwakeTime_Sleep varchar(100),"	
				+ "	OnBed varchar(100),"	
				+ "	ToSleep varchar(100),"	
				+ "	AwakeCount varchar(100),"	
				+ "	AwakeNoGetUpCount varchar(100),"	
				+ "	GoToBedTime varchar(100),"	
				+ "	GetUpTime varchar(100),"	
				+ "	ListLength varchar(100),"	
				+ "	SleepBak1 varchar(100),"	
				+ "	SleepBak2 varchar(100),"	
				+ " ListData varchar(100)"	
				+ ")";    
		return recordAudioSql;
	}
	
	private String creatRemindBusinessInfo(){
		String recordAudioSql = 
		"	CREATE TABLE "+MyTabList.REMIND_BUSINESS_INFO +" ('_id'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
		+ "	'AlarmTime' TEXT,"
		+ "	'AlarmRepeat'  INTEGER,"
		+ "	'AlarmPlant'  TEXT,"
		+ "	'AlarmTitle'  TEXT,"
		+ "	'AlarmContext'  TEXT,"
		+ "	'AlarmOnOff'  INTEGER,"
		+ ")"; 
		return recordAudioSql;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getVersionCode(Context mContext) {
		SQLiteDatabase db = null;
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return 0;
		}
		String sdkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/database";
		String dbPath = sdkPath + "/" + MyTabList.DATABASENAME;
		File sdkf = new File(sdkPath);
		File dbf = new File(dbPath);
		if(!sdkf.exists()) {
			sdkf.mkdir();
		}
		boolean isFileCreateSuccess=false;
		if(!dbf.exists()) {
			try {
				isFileCreateSuccess=dbf.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			isFileCreateSuccess = true;
		}
		
		int version = 0;
		if(isFileCreateSuccess) {	
		
			db = SQLiteDatabase.openOrCreateDatabase(dbf, null);
			version=db.getVersion();
			db.close();
		}
	
		return version;
	}

	
	/**
	* 方法1：检查某表列是否存在
	* @param db
	* @param tableName 表名
	* @param columnName 列名
	* @return
	*/
	private boolean checkColumnExist1(SQLiteDatabase db, String tableName
	        , String columnName) {
	    boolean result = false ;
	    Cursor cursor = null ;
	    try{
	        //查询一行
	        cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
	            , null );
	        result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
	    }catch (Exception e){
	         Log.e(TAG,"checkColumnExists1..." + e.getMessage()) ;
	    }finally{
	        if(null != cursor && !cursor.isClosed()){
	            cursor.close() ;
	        }
	    }

	    return result ;
	}
}
