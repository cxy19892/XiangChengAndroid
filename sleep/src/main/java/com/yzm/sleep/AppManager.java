package com.yzm.sleep;

import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppManager {

	private static Stack<FragmentActivity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(FragmentActivity activity) {
		if (activityStack == null) {
			activityStack = new Stack<FragmentActivity>();
		}
		activityStack.add(activity);
	}

	
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		if (!activityStack.isEmpty()) {
			Activity activity = activityStack.lastElement();
			finishActivity(activity);
		}
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）并返回参数
	 */
	public void finishActivity(int resultCode, Intent intent) {
		if (!activityStack.isEmpty()) {
			Activity activity = activityStack.lastElement();
			finishActivity(activity, resultCode, intent);
		}
	}
	
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity, int resultCode, Intent intent) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.setResult(resultCode, intent);
			activity.finish();
			activity = null;
		}
	}
	
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
				break;
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}
	
	public void finishAllActivityExceptOne(Class<?> cls){
		if(activityStack == null)
			return;
		FragmentActivity exceptActivity = null;
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (!activityStack.get(i).getClass().equals(cls)) {
				activityStack.get(i).finish();
			}else {
				exceptActivity = activityStack.get(i);
			}
		}
		activityStack.clear();
		if (exceptActivity != null) {
			addActivity(exceptActivity);
		}
	}
	
	/**
	 * 获取堆栈中activity个数
	 * @return activity个数
	 */
	public int getActivitySize(){
		if(activityStack==null)
			return 0;
		return activityStack.size();
	}
	
}