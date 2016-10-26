package com.yzm.sleep.background;

import java.text.ParseException;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yzm.sleep.utils.PreManager;

public class WaitNotificationReceiver extends BroadcastReceiver{

	private Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context = context;
//		startTicker();
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(1);
		PreManager.instance().saveAlarmState(context.getApplicationContext(), "1");
		PreManager.instance().saveAlarmTime(context.getApplicationContext(), System.currentTimeMillis());
	}
	
	private PendingIntent mPendingIntent;
	private Intent notificationIntent;
	private AlarmManager mAlarmManager;
	private void startTicker() {
		if(notificationIntent == null) {
			notificationIntent = new Intent("background.NotificationService");
		}
		if(mPendingIntent == null) {
			mPendingIntent = PendingIntent.getService(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
		if (mAlarmManager == null) {
			mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		}
		try {
			mAlarmManager.set(AlarmManager.RTC_WAKEUP,
					DataUtils.getNextAlertTime(context.getApplicationContext()), mPendingIntent);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
