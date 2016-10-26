package com.yzm.sleep.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.PerfectUserDataActivity;
import com.yzm.sleep.activity.community.CorrelativeCellPhoneActivity;
import com.yzm.sleep.bean.MyWeiBoFriendBaean;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;


public class UserInfoUtil {
	/**
	 * 获取平台是否绑定
	 * @param platformBoundMsg		平台绑定信息
	 * @param platform				平台标识
	 * 								 0 - 微博， 1 - 微信， 2 - QQ，3 - 手机
	 * @return
	 */
	public static boolean isCommunityPlatformBound(String platformBoundMsg,int platform){
		boolean isBound = false;
		switch (platform) {
		case 0:
			if (platformBoundMsg.substring(0, 1).equals("1")) {
				isBound = true;
			} else {
				isBound = false;
			}
			break;
		case 1:
			if (platformBoundMsg.substring(1, 2).equals("1")) {
				isBound = true;
			} else {
				isBound = false;
			}
			break;
		case 2:
			if (platformBoundMsg.substring(2, 3).equals("1")) {
				isBound = true;
			} else {
				isBound = false;
			}
			break;
		case 3:
			if (platformBoundMsg.substring(3).equals("1")) {
				isBound = true;
			} else {
				isBound = false;
			}
			break;

		default:
			break;
		}
		return isBound;
	}
	
	/***
	 * 登录成功后是否跳转到关联手机界面
	 * @param context
	 */
	public static void isGotoBoundPhone(Context context){
		String platformBoundMsg = PreManager.instance().getPlatformBoundMsg(
				context);
		boolean isPhoneBound = isCommunityPlatformBound(
				platformBoundMsg, 3);
	}
	/**
	 * 获取已保存的微博好友
	 * @param context
	 * @return
	 */
	public static List<MyWeiBoFriendBaean> getBoundWeiboFriends(Context context){
		String boundWeiboFriendsMsg = PreManager.instance().getBoundWeiboFriendsMsg(context);
		if (!TextUtils.isEmpty(PreManager.instance().getBoundWeiboFriendsMsg(context))) {
			JSONArray jsonAry;
			try {
				jsonAry = new JSONArray(boundWeiboFriendsMsg);
				List<MyWeiBoFriendBaean> infos = new ArrayList<MyWeiBoFriendBaean>();
				MyWeiBoFriendBaean info;
				for (int i = 0; i < jsonAry.length(); i++) {
					info = new MyWeiBoFriendBaean();
					JSONObject jsonObj = new JSONObject(jsonAry.getString(i));
					info.id = jsonObj.getString("id");
					info.name = jsonObj.getString("screen_name");
					info.avatar_hd = jsonObj.getString("profile_image_url");
					infos.add(info);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
