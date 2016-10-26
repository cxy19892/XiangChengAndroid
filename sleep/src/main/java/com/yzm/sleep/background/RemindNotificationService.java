package com.yzm.sleep.background;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yzm.sleep.R;
import com.yzm.sleep.activity.HomeActivity;
import com.yzm.sleep.utils.TimeFormatUtil;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;

public class RemindNotificationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("服务开启");
	}
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("服务收到");
		try{
			String date = DataUtils.getRecordDate(new Date());
			long t1 = DataUtils.getT1(date);
			long t10 = t1 + 1*60*60*1000;
			SharedPreferences sp = getApplicationContext().getSharedPreferences(SleepInfo.SLEEP_SETTIME, getApplicationContext().MODE_APPEND);
//			int remindStyle = sp.getInt(SleepInfo.REMIND_TYPE_DAY, 0)+sp.getInt(SleepInfo.REMIND_TYPE_NIGHT, 0);
			String remindStyle = sp.getString(SleepInfo.REMIND_BEFORE_SLEEP, "");//(SleepInfo.REMIND_TYPE_DAY, 0)+sp.getInt(SleepInfo.REMIND_TYPE_NIGHT, 0);
			String remindmsg = sp.getString(SleepInfo.REMIND_MSG, "没有搜集到昨晚的数据，但今晚也要好好休息哦！");
			System.out.println("提醒类型"+remindStyle +"提醒内容"+remindmsg);
			
			if(remindStyle.equals("")){
				String registTime = sp.getString(SleepInfo.REMIND_TIME, "");
				if(!TextUtils.isEmpty(registTime)){
					String nowtimeStr = TimeFormatUtil.getNowTime();
//					if(registTime.equals(nowtimeStr)){
						initMyNotifycation(remindmsg);
//					}
				}
				
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@SuppressLint("NewApi") private void initNotifycation(String remindmsg){
        // 1 得到通知管理器  
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
        // 2 设置通知的点击事件  
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);  
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100,  
                intent, 0);  
        // 3构建通知  
        Notification.Builder builder = new Notification.Builder(getApplicationContext())  
                .setContentIntent(contentIntent).setSmallIcon(R.drawable.ic_launcher)  
                // 设置状态栏的小标题  
//                .setLargeIcon(  
//                        BitmapFactory.decodeResource(getResources(),  
//                                R.drawable.ic_launcher))
                .setWhen(System.currentTimeMillis()).setTicker("香橙智能提醒")
                .setAutoCancel(true)
                .setContentTitle(remindmsg.substring(0, 20))
                
                .setContentText(remindmsg.substring(20));
        
        Notification notification = builder.build();// API 16添加创建notification的方法  
        // 通知  
        notification.defaults=Notification.DEFAULT_SOUND;
        manager.notify(989, notification);  
        }
	private void initMyNotifycation(String remindmsg){
        // 1 得到通知管理器  
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
        // 2 设置通知的点击事件  
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);  
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100,  
                intent, 0);  
        // 3构建通知  
        int icon = R.drawable.logo; //通知图标

        CharSequence tickerText = "已到您可以上床的最早时间"; //状态栏显示的通知文本提示

        long when = System.currentTimeMillis()+200; //通知产生的时间，会在通知信息里显示

        //用上面的属性初始化Nofification

        Notification notification = new Notification(icon,tickerText,when);


        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.layout_remind_notifycation);
        contentView.setTextViewText(R.id.remind_title,"已到您可以上床的最早时间");
        contentView.setTextViewText(R.id.remind_content,remindmsg);
        notification.contentView = contentView;
        notification.contentIntent =contentIntent;
//        notification.setLatestEventInfo(getApplicationContext(), "香橙智能提醒", remindmsg, contentIntent);
        notification.tickerText = remindmsg;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        
//        Notification.Builder builder = new Notification.Builder(getApplicationContext())  
//                .setContentIntent(contentIntent).setSmallIcon(R.drawable.ic_launcher)  
//                // 设置状态栏的小标题  
////                .setLargeIcon(  
////                        BitmapFactory.decodeResource(getResources(),  
////                                R.drawable.ic_launcher))
//                .setWhen(System.currentTimeMillis()).setTicker("香橙智能提醒")
//                .setAutoCancel(true)
//                .setContentTitle(remindmsg.substring(0, 20))
//                
//                .setContentText(remindmsg.substring(20));
//        
//        Notification notification = builder.build();// API 16添加创建notification的方法  
        // 通知  
        notification.defaults=Notification.DEFAULT_SOUND;
        manager.notify(988, notification);  
        }
}
