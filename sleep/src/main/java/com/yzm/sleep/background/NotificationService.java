package com.yzm.sleep.background;

import java.text.ParseException;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.yzm.sleep.R;
import com.yzm.sleep.activity.HomeActivity;

public class NotificationService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		try{
			String date = DataUtils.getRecordDate(new Date());
			long t1 = DataUtils.getT1(date);
			long t10 = t1 + 1*60*60*1000;
			SharedPreferences sp = getApplicationContext().getSharedPreferences(SleepInfo.SLEEP_SETTIME, getApplicationContext().MODE_APPEND);
			boolean isOpen = sp.getBoolean(SleepInfo.OPEN_SET, true);
			int remindStyle = sp.getInt(SleepInfo.REMIND_TYPE, -1);
			String remindmsg = sp.getString(SleepInfo.REMIND_MSG, "橙橙提醒你，良好的睡眠才能够保证身心的健康！");
			System.out.println("提醒类型"+remindStyle +"提醒内容"+remindmsg);
			if(isOpen && System.currentTimeMillis() <= t10) {
				inidNotification();	
			}
			if(remindStyle != -1  && System.currentTimeMillis() <= t10 ){
				initNotifycation(remindmsg);
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}

		super.onCreate();
	}
	
	private RemoteViews mRemoteView = null;
	private void inidNotification() throws ParseException {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification mNotification = new Notification();
		mNotification.defaults = Notification.DEFAULT_ALL;  
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL; 
        mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		mNotification.icon = R.drawable.logo;
		mNotification.tickerText = "您该睡觉了哦";
		mNotification.when = System.currentTimeMillis();

		if(mRemoteView == null) {
			mRemoteView = new RemoteViews("com.yzm.sleep",R.layout.notification_info);	
		}
		
		Intent intent = new Intent("background.NotificationReceiver");
		PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteView.setOnClickPendingIntent(R.id.sleep, mPendingIntent);
		
		Intent waitintent = new Intent("background.WaitNotificationReceiver");
		PendingIntent ingIntent = PendingIntent.getBroadcast(this, 0, waitintent, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteView.setOnClickPendingIntent(R.id.wait, ingIntent);
		
		mNotification.contentView =  mRemoteView;
		
		manager.notify(1, mNotification);
	}
	
	
	private void initNotifycation(String remindmsg){
	        // 1 得到通知管理器  
	        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
	        // 2 设置通知的点击事件  
	        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);  
	        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100,  
	                intent, 0);  
	        // 3构建通知  
	        Notification.Builder builder = new Notification.Builder(getApplicationContext())  
//	                .setContentIntent(contentIntent).setSmallIcon(R.drawable.ic_launcher)  
	                // 设置状态栏的小标题  
	                .setLargeIcon(  
	                        BitmapFactory.decodeResource(getResources(),  
	                                R.drawable.ic_launcher))
	                .setWhen(System.currentTimeMillis()).setTicker("香橙智能提醒")
	                .setAutoCancel(true)
	                .setContentTitle("橙橙提醒您")
	                .setContentText(remindmsg);
	        Notification notification = builder.build();// API 16添加创建notification的方法  
	        // 通知  
	        notification.defaults=Notification.DEFAULT_SOUND;
	        manager.notify(11, notification);  
	  
	}
}
