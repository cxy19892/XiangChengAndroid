package com.yzm.sleep.background;

import java.text.ParseException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class StartTimingService {
	
	private AlarmManager manager;
	private Intent intent;
	private PendingIntent pendingIntent;
	public StartTimingService(Context context) {
//		manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		intent = new Intent("background.PermanentService");
//		pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	}

	public void start() throws ParseException {
//		manager.set(AlarmManager.RTC_WAKEUP, DataUtils.getPermanetServiceStartTime(), pendingIntent);
		// 在这里启动这个service也有延期，不知道到什么时候
		// DataUtils.getPermanetServiceStartTime() 这里的逻辑需要咨询一下程序员
	}
		
}
