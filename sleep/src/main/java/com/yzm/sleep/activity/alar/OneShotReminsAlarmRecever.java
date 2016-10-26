package com.yzm.sleep.activity.alar;

import com.yzm.sleep.background.RemindNotificationService;
import com.yzm.sleep.utils.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class OneShotReminsAlarmRecever extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		LogUtil.d("chen", "onReceive Action()=" +arg1.getAction() +"\nFlags="+arg1.getFlags());
		arg0.startService(new Intent(arg0,RemindNotificationService.class)); 
	}

}
