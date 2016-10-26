package com.yzm.sleep.brodcast;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.activity.HomeActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.MessageListActivity;
import com.yzm.sleep.background.SecretaryDBOperate;
import com.yzm.sleep.bean.SecretaryMsgBean;
import com.yzm.sleep.utils.PreManager;

public class JPushMsgBroadcast extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent data) {
		String action = data.getAction();
		Bundle bundle = data.getExtras();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
        	processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
        	//打开自定义的Activity
        	Intent i=null;
        	//是否在后台运行
        	if(AppManager.getAppManager().getActivitySize() <= 0){ //在后台运行 
        		try {
        			AppManager.getAppManager().finishAllActivityExceptOne(HomeActivity.class);
				} catch (Exception e) {
				}
        		i = context.getPackageManager().getLaunchIntentForPackage(context.getApplicationInfo().packageName);
        		i.putExtra("jpushmsgtomsglist", true);
        	}else{
        		if(PreManager.instance().getIsLogin(context)){
    	        	i = new Intent(context, MessageListActivity.class);
    	        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	}else{
            		i = new Intent(context, LoginActivity.class);
            		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	}
        	}
        	context.startActivity(i);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(action)) {
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(action)) {
        } else {
        }
	}
	
	private void processCustomMessage(Context context, Bundle bundle) {
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		try {
			JSONObject jsonObj = new JSONObject(extras);
			SecretaryMsgBean bean = new SecretaryMsgBean();
			bean.setDate(String.valueOf(System.currentTimeMillis()));
			bean.setMsg(jsonObj.getString("message"));
			bean.setTitle(jsonObj.getString("title"));
			int type=Integer.parseInt(jsonObj.getString("type"));
			bean.setType(type);
			bean.setMsgId(jsonObj.getString("id"));
			bean.setLink(PreManager.instance().getUserId(context));
			bean.setScan(0);
			//如果类型为8则是点赞评论
			if(type != 8){
				SecretaryDBOperate.insertDB(context, bean);
			}
		} catch (Exception e) {
		}

	}
}
