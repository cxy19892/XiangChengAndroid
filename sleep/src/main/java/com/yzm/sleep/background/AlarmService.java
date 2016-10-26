package com.yzm.sleep.background;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.yzm.sleep.activity.alar.AlarmReceiver;

public class AlarmService extends Service {

	static int DATE_MODE_FIX = 1;
	static int DATE_MODE_WEEK = 2;
	static int DATE_MODE_MONTH = 3;

	public static final String ACTION_ALARM = "com.yzm.sleep.AlarmControl.ALARM";
	public static final String ACTION_INITDB = "com.yzm.sleep.AlarmControl.InitDB";
	public static final String ACTION_DELETEALARM = "com.yzm.sleep.AlarmControl.DeleteRecord";
	public static final String ACTION_REPEATALARM = "com.yzm.sleep.AlarmControl.AlarmCalc";
	public static final String ACTION_MIUIALARM = "com.yzm.sleep.AlarmControl.MIUI";
	
	CommandReceiver m_recv = new CommandReceiver();

	public static class ListAlarmTime implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public int AlarmID;
		public String AlarmTime = "";
		public int AlarmRepeat;
		public String AlarmPlant = "";
		public String AlarmTitle = "";
		public String AlarmDay = "";
		public int AlarmDelay;
		public String AlarmAudio = "";
		public int AlarmOnOff;
		
		public String AlarmProfile = "";
		public String AlarmUserNickname = "";
		public String AlarmUserId = "";
		public int AlarmAudioId;
		public int AlarmDownloads;
		
		public String AlarmAudioCover = "";
		
		public int isRemind;
		public String remindTitle;
		public String remindContext;
		
		/**闹钟类型  0 - 非专属铃音 ， 1 - 普通闹钟 ， 2 - 神秘闹钟, -1专属闹钟*/
		public int AlarmType;
		public String AudioKey;
		public String AudioCoverKey;
		public String UserProfileKey;
		public int AudioIsLocalRecord;

		public ListAlarmTime() {

		};
		
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

	}
	
	public static class AlarmStat {
		public int AlarmID;
		public int AlarmCalc;
		public AlarmStat() {

		};

	}
	
	
	
	private Lock m_ListLock = new ReentrantLock();

	private List<ListAlarmTime> m_alarmList = null;
	
	private List<AlarmStat> m_aStatList = new ArrayList<AlarmStat>();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private Handler mHandler = new Handler();
	private File file;
	private RandomAccessFile rank;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		try {
			// registerScreenActionReceiver();
			mHandler.removeCallbacks(runnable);
			mHandler.postDelayed(runnable, 5000);

			System.out.println("测试：闹钟服务开始****");
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/alarmTest.txt";
			file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			rank = new RandomAccessFile(path, "rw");
			rank.seek(rank.length());
			StringBuffer sb = new StringBuffer();
			long currenttime = System.currentTimeMillis();
			sb.append(sdf.format(new Date(currenttime))).append(
					"   alarm ndk run \r\n");
			rank.writeBytes(sb.toString());

			IntentFilter filter = new IntentFilter();

			filter.addAction(ACTION_INITDB);

			filter.addAction(ACTION_DELETEALARM);
			
			filter.addAction(ACTION_REPEATALARM);
			
			registerReceiver(m_recv, filter);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 初始化闹钟
		initAlarm();

		super.onCreate();
	}

	private int id;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		id = startId;
		System.out.println("测试：开始flags:" + flags);

		return super.onStartCommand(intent, flags, startId);

	}

	private long lasttime = 0;
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {

			// 纯属自检线程。定时重新设置闹钟，
			String strStatString = "5s alarm check";

			// TODO Auto-generated method stub

			StringBuffer sb = new StringBuffer();
			long currenttime = System.currentTimeMillis();
			sb.append(sdf.format(new Date(currenttime)))
					.append("   ------- 5s running -------")
					.append(strStatString);

			try {

				if (rank != null) {
					rank.seek(rank.length());
					rank.writeBytes(sb.toString());

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// if(Math.abs(currenttime - lasttime) > 1*60*1000)
			{
				// acquireWakeLock();
			}
			
			//每5分钟加载一次闹钟
			if(Math.abs(currenttime - lasttime) > 5*60*1000)
			{
				System.out.println("测试：闹钟5分钟自检运行****");
				initAlarm();
				lasttime = currenttime;
			}
			mHandler.postDelayed(this, 5000);
			// System.out.println("测试：包名  "+getApplication().getPackageName());
			// forceStopAPK(getApplication().getPackageName());
		}
	};

	WakeLock wakeLock = null;

	// 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
	@SuppressWarnings("deprecation")
	private void acquireWakeLock() {
		if (null == wakeLock) {
			try {
				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/powerstart.txt";
				file = new File(path);
				if (!file.exists()) {
					file.createNewFile();
				}
				rank = new RandomAccessFile(path, "rw");
				rank.seek(rank.length());
				StringBuffer sb = new StringBuffer();
				long currenttime = System.currentTimeMillis();
				sb.append(sdf.format(new Date(currenttime))).append(
						"   wakeup CPU  \r\n");
				rank.writeBytes(sb.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}

			System.out.println("测试：电源开启****");
			PowerManager pm = (PowerManager) this
					.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "PostLocationService");
			if (null != wakeLock) {
				wakeLock.acquire();
			}
		}
	}

	// 释放设备电源锁
	private void releaseWakeLock() {
		if (null != wakeLock) {
			System.out.println("测试：电源关闭****");
			wakeLock.release();
			wakeLock = null;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		try {

			System.out.println("测试：闹钟服务2关闭****");
			StringBuffer sb = new StringBuffer();
			long currenttime = System.currentTimeMillis();
			sb.append(sdf.format(new Date(currenttime))).append(
					"   alarm service destory \r\n");
			rank.seek(rank.length());
			rank.writeBytes(sb.toString());
			System.gc();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		unregisterReceiver(m_recv);
		super.onDestroy();
	}

	
	
	
	
	public void DeletedAlarm(int deleteAlarm) {

		
		
		
		
		
			Intent intent = new Intent(AlarmService.this, AlarmReceiver.class);

			PendingIntent sender = PendingIntent.getBroadcast(
					AlarmService.this, deleteAlarm, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);

			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

			am.cancel(sender);


	}
	
	
	
	
	/** 
	* @Title: SetAlarm 
	* @Description: TODO(安装 删除闹钟) 
	* @param @param AlarmParam  闹钟参数
	* @return PendingIntent    返回类型 
	* @throws 
	*/
	public PendingIntent SetAlarm(ListAlarmTime AlarmParam) 
	{
		PendingIntent sender =null;
		
		Intent intent = new Intent(AlarmService.this,AlarmReceiver.class);
		
		intent.setAction(ACTION_ALARM);
		
		intent.putExtra("AlarmID", AlarmParam.AlarmID);
		
		intent.putExtra("AlarmTitle", AlarmParam.AlarmTitle);
		
		intent.putExtra("AlarmDelay",AlarmParam.AlarmDelay);
		
		intent.putExtra("AlarmAudio",AlarmParam.AlarmAudio);
		
		intent.putExtra("AlarmTime",AlarmParam.AlarmTime);
		
		intent.putExtra("AlarmAudioCover",AlarmParam.AlarmAudioCover);
		
		intent.putExtra("AudioCoverKey", AlarmParam.AudioCoverKey);
		
		//remind 添加
		
		intent.putExtra("IsRemind", AlarmParam.isRemind);
		
		intent.putExtra("RemindTitle", AlarmParam.remindTitle);
		
		intent.putExtra("RemindContext", AlarmParam.remindContext);

		sender = PendingIntent.getBroadcast(AlarmService.this,AlarmParam.AlarmID, intent,PendingIntent.FLAG_UPDATE_CURRENT);
			
		return sender;
	}

	// 把字符串转为日期
	public static Date ConverToDate(String strDate) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.parse(strDate);
	}

	/**
	 * @Title: initAlarm
	 * @Description: TODO(初始化闹钟)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void initAlarm() {

		initDBData();

		String brand =android.os.Build.BRAND;	
		

		
		if (m_alarmList != null && m_alarmList.size() > 0)// 表示
		{

			for (int i = 0; i < m_alarmList.size(); i++) {

				ListAlarmTime oneAlarm = m_alarmList.get(i);

				if (oneAlarm != null) {

				

					PendingIntent sender =SetAlarm(oneAlarm);					
					
					
					AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

					if (oneAlarm.AlarmOnOff == 1) // 打开
					{

						Calendar calendar = Calendar.getInstance();

						if (oneAlarm.AlarmRepeat == 1)// 重复的情况填写  	// 周期：1,2,3,4,5,6,7													
						{
											
							System.out.println("round alarm");			
							
							long timeValue = getNextAlarmTime(2,	oneAlarm.AlarmPlant, oneAlarm.AlarmTime);
					
							calendar.setTimeInMillis(timeValue);
							
							
							SimpleDateFormat Day = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							
							Date  dated = new Date(timeValue);
							
							String  TimeSet = Day.format(dated);
							
							System.out.println("round alarm");
							
							System.out.println(TimeSet);

						} else {

							// 不需要重复的情况填写 TIME+DAY

							try {

								// String strDayString="2015-04-01";
								// String strTimeString="19:32";

								String datee = oneAlarm.AlarmDay + " "	+ oneAlarm.AlarmTime;
			
								SimpleDateFormat Day = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm");
								Date TimeSet = Day.parse(datee);

								long PlantTime = TimeSet.getTime();
								long currSysTime = System.currentTimeMillis();

								if (currSysTime - PlantTime > 0)// 表示当前系统时间已经超过预设时间
								{
									continue;

								} else {

									
									
									
									calendar.setTimeInMillis(PlantTime);
								
									System.out.println("one alarm"+ datee);
									System.out.println(datee);
									
								}

							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

//						if (oneAlarm.AlarmDelay > 0)
//								am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),oneAlarm.AlarmDelay *10* 1000, sender);
//						else
						
						if(brand!=""&&brand.equals("Xiaomi"))
						{						
	
							am.set(AlarmManager.RTC,	calendar.getTimeInMillis(), sender);						
							
						}	else					
							am.set(AlarmManager.RTC_WAKEUP,	calendar.getTimeInMillis(), sender);

					} else {

						am.cancel(sender); // 关闭
					}

				}
				;

			}
			;

		}
		
			
			SharedPreferences sp = AlarmService.this.getSharedPreferences("AlarmDelay", MODE_PRIVATE);
            
    		long sysTime= System.currentTimeMillis();    	

    		int iAlarmID= sp.getInt("AlarmID", -1);
    		
    		if(iAlarmID==9999)//小睡
    		{
    	 		long lAlarmCurrTime= sp.getLong("AlarmCurrTime", -1);
        	
    	 		int iAlarmDelay= sp.getInt("AlarmDelay", -1);
        	
        		String sAlarmTitle= sp.getString("AlarmTitle", ""); 
        
        		String sAlarmAudio= sp.getString("AlarmAudio", ""); 
        		
        		String sAlarmTime= sp.getString("AlarmTime", ""); 
        		
        		String sAlarmAudioCover= sp.getString("AlarmAudioCover", ""); 
        		
        		String sAudioCoverKey= sp.getString("AudioCoverKey", ""); 
        		
    			long AlarmTime=lAlarmCurrTime+iAlarmDelay*60*1000;
        	
    			if(AlarmTime>sysTime)
        		{
        			
        			Intent intent = new Intent(AlarmService.this,AlarmReceiver.class);
            		
            		intent.setAction(AlarmService.ACTION_ALARM);
            		
            		intent.putExtra("AlarmID",9999);
            		
            		intent.putExtra("AlarmTitle",sAlarmTitle);
            		
            		intent.putExtra("AlarmDelay",iAlarmDelay);
            		
            		intent.putExtra("AlarmAudio",sAlarmAudio);

            		intent.putExtra("AlarmTime",sAlarmTime);
            		
            		intent.putExtra("AlarmAudioCover",sAlarmAudioCover);    	
            		
            		intent.putExtra("AudioCoverKey",sAudioCoverKey);   
            		
            		
            		
                  	PendingIntent sender = PendingIntent.getBroadcast(AlarmService.this,9999, intent,PendingIntent.FLAG_UPDATE_CURRENT);

                	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                	
                //	long lAlarmTime=lAlarmCurrTime+iAlarmDelay*60*1000;
                	
                	if(brand!=""&&brand.equals("Xiaomi"))
                	{
                		am.set(AlarmManager.RTC,AlarmTime, sender);						
						
					}else
						am.set(AlarmManager.RTC_WAKEUP,	AlarmTime, sender);
                    
					SimpleDateFormat Day = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					
					Date  dated = new Date(AlarmTime);
					
					String  TimeSet = Day.format(dated);
					
					String strMSG="sleep delay AlarmTime:"+TimeSet;	
					
					System.out.println(strMSG);                	
        			
        			
        		}
        		
        		
        		
    		}
   

		
		
		
		
		
		

	}

	private String getWeek() {
		String Week = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 也可将此值当参数传进来
		Date curDate = new Date(System.currentTimeMillis());
		String pTime = format.format(curDate);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			Week = "7";
			break;
		case 2:
			Week = "1";
			break;
		case 3:
			Week = "2";
			break;
		case 4:
			Week = "3";
			break;
		case 5:
			Week = "4";
			break;
		case 6:
			Week = "5";
			break;
		case 7:
			Week = "6";
			break;
		default:
			break;
		}
		return Week;
	}

	/**
	 * 闹钟三种设置模式（dateMode）： 1、DATE_MODE_FIX：指定日期，如20120301 ,
	 * 参数dateValue格式：2012-03-01 2、DATE_MODE_WEEK：按星期提醒，如星期一、星期三 ,
	 * 参数dateValue格式：1,3 3、DATE_MODE_MONTH：按月提醒，如3月2、3号，4月2、3号,
	 * 参数dateValue格式：3,4|2,3
	 * 
	 * startTime:为当天开始时间，如上午9点, 参数格式为09:00
	 */
	public static long getNextAlarmTime(int dateMode, String dateValue,
			String startTime) {
		final SimpleDateFormat fmt = new SimpleDateFormat();
		final Calendar c = Calendar.getInstance();
		final long now = System.currentTimeMillis();

		// 设置开始时间
		try {
			if (DATE_MODE_FIX == dateMode) {
				fmt.applyPattern("yyyy-MM-dd");
				Date d = fmt.parse(dateValue);
				c.setTimeInMillis(d.getTime());
			}
	
			fmt.applyPattern("HH:mm");
			Date d = fmt.parse(startTime);
			int value=d.getHours();
			c.set(Calendar.HOUR_OF_DAY, d.getHours());
			c.set(Calendar.MINUTE, d.getMinutes());
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		long nextTime = 0;
		if (DATE_MODE_FIX == dateMode) { // 按指定日期
			nextTime = c.getTimeInMillis();
			// 指定日期已过
			if (now >= nextTime)
				nextTime = 0;
		} else if (DATE_MODE_WEEK == dateMode) { // 按周
		
			final long[] checkedWeeks = parseDateWeeks(dateValue);
		
			if (null != checkedWeeks) {
			
				for (long week : checkedWeeks) {
 	
					int WeekDay=0;
	            	
	            	switch((int)week)
	            	{
	            	case 1 :
	            		WeekDay =Calendar.MONDAY;
	            		break;
	            	case 2 :
	            		WeekDay =Calendar.TUESDAY;
	             		break;
	               	case 3 :
	            		WeekDay =Calendar.WEDNESDAY;
	             		break;
	            	case 4:
	            		WeekDay =Calendar.THURSDAY;
	             		break;
	             	case 5:
	            		WeekDay =Calendar.FRIDAY;
	             		break;
	            	case 6:
	            		WeekDay =Calendar.SATURDAY;
	             		break;
	             	case 7:
	            		WeekDay =Calendar.SUNDAY;
	             		break;	
	            	}
	            	
					c.set(Calendar.DAY_OF_WEEK,WeekDay);

					long triggerAtTime = c.getTimeInMillis();
					if ((triggerAtTime) <= now) { // 下周
						triggerAtTime += AlarmManager.INTERVAL_DAY * 7;
					}
					// 保存最近闹钟时间
					if (0 == nextTime) {
						nextTime = triggerAtTime;
					} else {
						nextTime = Math.min(triggerAtTime, nextTime);
					}
				}
			}
		} else if (DATE_MODE_MONTH == dateMode) { // 按月
			final long[][] items = parseDateMonthsAndDays(dateValue);
			final long[] checkedMonths = items[0];
			final long[] checkedDays = items[1];

			if (null != checkedDays && null != checkedMonths) {
				boolean isAdd = false;
				for (long month : checkedMonths) {
					c.set(Calendar.MONTH, (int) (month - 1));
					for (long day : checkedDays) {
						c.set(Calendar.DAY_OF_MONTH, (int) day);

						long triggerAtTime = c.getTimeInMillis();
						if (triggerAtTime <= now) { // 下一年
							c.add(Calendar.YEAR, 1);
							triggerAtTime = c.getTimeInMillis();
							isAdd = true;
						} else {
							isAdd = false;
						}
						if (isAdd) {
							c.add(Calendar.YEAR, -1);
						}
						// 保存最近闹钟时间
						if (0 == nextTime) {
							nextTime = triggerAtTime;
						} else {
							nextTime = Math.min(triggerAtTime, nextTime);
						}
					}
				}
			}
		}
		return nextTime;
	}

	public static long[] parseDateWeeks(String value) {
		long[] weeks = null;
		try {
			final String[] items = value.split(",");
			weeks = new long[items.length];
			int i = 0;
			for (String s : items) {
				weeks[i++] = Long.valueOf(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weeks;
	}

	public static long[][] parseDateMonthsAndDays(String value) {
		long[][] values = new long[2][];
		try {
			final String[] items = value.split("\\|");
			final String[] monthStrs = items[0].split(",");
			final String[] dayStrs = items[1].split(",");
			values[0] = new long[monthStrs.length];
			values[1] = new long[dayStrs.length];

			int i = 0;
			for (String s : monthStrs) {
				values[0][i++] = Long.valueOf(s);
			}
			i = 0;
			for (String s : dayStrs) {
				values[1][i++] = Long.valueOf(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	/**
	 * 
	 * @Title: initDBData
	 * @Description: TODO(初始化闹钟数据)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */

	private void initDBData() {

		try {
			
			MyDatabaseHelper helper = MyDatabaseHelper	.getInstance(this);
			
		if(helper!=null)	
		{
			AlarmDBOperate operate = new AlarmDBOperate(helper.getWritableDatabase(), "");
			m_alarmList = operate.GetALarmData();
		}
	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	

	};

	
	
	
	/**
	 * 
	 * @ClassName: CommandReceiver
	 * @Description: TODO(接受消息，然后增加删除数据)
	 * @author LZM
	 * @date 2015年4月1日 下午6:33:33
	 * 
	 */
	private class CommandReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action.equals(ACTION_INITDB)) {

				initAlarm();

			} else if (action.equals(ACTION_DELETEALARM)) {

				// DeletedAlarm(deleteAlarm);
				ListAlarmTime data = new ListAlarmTime();

				Bundle dataBundle = intent.getExtras();

				data.AlarmID = dataBundle.getInt("AlarmID");

				data.AlarmTime = dataBundle.getString("AlarmTime");

				data.AlarmRepeat = dataBundle.getInt("AlarmRepeat");

				data.AlarmPlant = dataBundle.getString("AlarmPlant");

				data.AlarmTitle = dataBundle.getString("AlarmTitle");

				data.AlarmDay = dataBundle.getString("AlarmDay");

				data.AlarmDelay = dataBundle.getInt("AlarmDelay");

				data.AlarmAudio = dataBundle.getString("AlarmAudio");

				data.AlarmOnOff = dataBundle.getInt("AlarmOnOff");

				PendingIntent deletePend=SetAlarm(data);
			
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.cancel(deletePend); // 关闭
				
				
			}else if(action.equals(ACTION_REPEATALARM))
			{
		
				
				ListAlarmTime data = new ListAlarmTime();
				
				data.AlarmID =	intent.getIntExtra("AlarmID",0);
				
				data.AlarmTitle = intent.getStringExtra("AlarmTitle");
				
				data.AlarmDelay = 	intent.getIntExtra("AlarmDelay",0);

				data.AlarmAudio = intent.getStringExtra("AlarmAudio");

				PendingIntent deletePend=SetAlarm(data);
			
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		
				am.cancel(deletePend); 
		
				
				System.out.println("删除了闹钟");
			}
			
			
		}

	}

}
