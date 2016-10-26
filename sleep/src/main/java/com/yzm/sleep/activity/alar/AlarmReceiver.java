/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yzm.sleep.activity.alar;


import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.yzm.sleep.R;
import com.yzm.sleep.background.AlarmDBOperate;
import com.yzm.sleep.background.AlarmService;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.AlarmService.ListAlarmTime;
import com.yzm.sleep.utils.TimeFormatUtil;

/**
 * Glue class: connects AlarmAlert IntentReceiver to AlarmAlert activity. Passes
 * through Alarm ID.
 */
public class AlarmReceiver extends BroadcastReceiver {

	/**
	 * If the alarm is older than STALE_WINDOW, ignore. It is probably the
	 * result of a time or timezone change
	 */
	private final static int STALE_WINDOW = 30 * 60 * 1000;

	@Override
	public void onReceive(Context context, Intent intent) {

		//关闭闹钟响起的功能，即便收到闹钟的广播。
		/*if (AlarmService.ACTION_ALARM.equals(intent.getAction())) {
			AlarmAlertWakeLock.acquireCpuWakeLock(context);

			int AlarmID = intent.getIntExtra("AlarmID", 0);

			int AlarmDelay = intent.getIntExtra("AlarmDelay", 0);

			String AlarmTitle = intent.getStringExtra("AlarmTitle");

			String AlarmAudio = intent.getStringExtra("AlarmAudio");
			
			String AlarmTime = intent.getStringExtra("AlarmTime");
			
			String AlarmAudioCover = intent.getStringExtra("AlarmAudioCover");
			
			String AudioCoverKey = intent.getStringExtra("AudioCoverKey");
			
			String RemindTitle = intent.getStringExtra("RemindTitle");
			
			int IsRemind = intent.getIntExtra("IsRemind", 0);
			String remindTitle =  intent.getStringExtra("RemindTitle");
			String remindContext= intent.getStringExtra("RemindContext");
			
			if(0 == IsRemind){
				if(!AlarmTime.equals(TimeFormatUtil.getNowTime())){
					return;
				}
				 Close dialogs and window shade 
				Intent closeDialogs = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
				context.sendBroadcast(closeDialogs);
		
				// Decide which activity to start based on the state of the keyguard.
				Class c = AlarmAlert.class;
				KeyguardManager km = (KeyguardManager) context
						.getSystemService(Context.KEYGUARD_SERVICE);
				if (km.inKeyguardRestrictedInputMode()) {
					// Use the full screen activity for security.
					c = AlarmAlertFullScreen.class;
				}
		
				Intent alarmIntent = new Intent(context, c);
				alarmIntent.putExtra("AlarmID", AlarmID);
				alarmIntent.putExtra("AlarmTitle", RemindTitle);
				alarmIntent.putExtra("AlarmDelay", AlarmDelay);
				alarmIntent.putExtra("AlarmAudio", AlarmAudio);
				alarmIntent.putExtra("AlarmTime", AlarmTime);
				alarmIntent.putExtra("AlarmAudioCover", AlarmAudioCover);
				alarmIntent.putExtra("AudioCoverKey", AudioCoverKey);
				alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				
				MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
				SQLiteDatabase db = helper.getWritableDatabase();
				AlarmDBOperate audioDAO = new AlarmDBOperate(db,MyTabList.ALARM_TIME);
	//			String searchAwakeAlarm = audioDAO.searchAwakeAlarmByAlarmID(String.valueOf(AlarmID));
				
				ListAlarmTime m_ListAlarmTime = new ListAlarmTime();
				m_ListAlarmTime.AlarmTitle = AlarmTitle;
				m_ListAlarmTime.AlarmDelay = AlarmDelay;
				m_ListAlarmTime.AlarmAudio = AlarmAudio;
				m_ListAlarmTime.AlarmAudioCover = AlarmAudioCover;
				m_ListAlarmTime.AudioCoverKey = AudioCoverKey;
				String searchAwakeAlarm = audioDAO.searcchByArrays(m_ListAlarmTime);
				if(!TextUtils.isEmpty(searchAwakeAlarm)){//如果闹钟存在则开启
					context.startActivity(alarmIntent);
				}
			}else{
				if(!AlarmTime.equals(TimeFormatUtil.getNowTime())){
					return;
				}
				initMyNotifycation(context, remindTitle, remindContext);
			}
		}*/
	}
	
	private void initMyNotifycation(Context context,String remindtitle, String remindContext){
		//使用自定义的方式显示推送消息
        // 1 得到通知管理器  
        NotificationManager manager = (NotificationManager) context.getSystemService("notification");  
        // 2 设置通知的点击事件  
        Intent intent = new Intent(context, AlarmActivity.class);  
        PendingIntent contentIntent = PendingIntent.getActivity(context, 100,  
                intent, 0);  
        // 3构建通知  
        int icon = R.drawable.logo; //通知图标

        CharSequence tickerText = "香橙智能提醒"; //状态栏显示的通知文本提示

        long when = System.currentTimeMillis() + 2000; //通知产生的时间，会在通知信息里显示

        Notification notification = new Notification(icon,tickerText,when);


//        RemoteViews contentView = new RemoteViews("com.yzm.sleep",R.layout.layout_remind_notifycation);
//        contentView.setTextViewText(R.id.remind_title,remindtitle);
//        contentView.setTextViewText(R.id.remind_content,remindContext);
//        notification.contentView = contentView;
        notification.icon = R.drawable.logo;
        notification.setLatestEventInfo(context, remindtitle, remindContext, contentIntent);
//        notification.contentIntent =contentIntent;
        notification.defaults=Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.tickerText = remindContext;
        manager.notify(989, notification);  
        
        Intent alarmintent = new Intent();
        alarmintent.setAction("com.xc.alarm.add.ALARM_UPDATE_SUCCESS");
		context.sendBroadcast(alarmintent);
        //使用jpush 的接口显示推送消息
        /*JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(0);
        ln.setContent(remindContext);
        ln.setTitle(remindtitle);
        ln.setNotificationId(989) ;
        ln.setBroadcastTime(System.currentTimeMillis() + 2000);

        Map<String , Object> map = new HashMap<String, Object>() ;
        map.put("name", "jpush") ;
        map.put("test", "111") ;
        JSONObject json = new JSONObject(map) ;
        ln.setExtras(json.toString()) ;
        JPushInterface.addLocalNotification(context, ln);*/
        }
}
