package com.yzm.sleep.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AIDLReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("background.AIDLReceiver")) {
				SleepInfo.SET_STARTTIME = intent.getLongExtra(SleepInfo.AIDL_STARTTIME, SleepInfo.SET_STARTTIME);
				SleepInfo.SET_ENDTIME = intent.getLongExtra(SleepInfo.AIDL_ENDTIME, SleepInfo.SET_ENDTIME);
				
				
				SharedPreferences sp = context.getSharedPreferences(SleepInfo.SLEEP_SETTIME, context.getApplicationContext().MODE_APPEND);	
				Editor edit = sp.edit();
				edit.putString(SleepInfo.STARTTIME, SleepInfo.SET_STARTTIME+"");
				edit.putString(SleepInfo.ENDTIME, SleepInfo.SET_ENDTIME + "");
				edit.commit();
//			}
		}
	}

}
