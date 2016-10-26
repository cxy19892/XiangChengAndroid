package com.yzm.sleep.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.yzm.sleep.bean.MyWeiBoFriendBaean;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.GetWeiboFanParamClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.InterfaceGetWeiboFanCallBack;

public class WeiboDataProClass extends HttpDataTranUtils{
	private Context mContext;
	public WeiboDataProClass(Context context){
		this.mContext = context;
	}
	
	private String getWeiboFanURL = "https://api.weibo.com/2/friendships/friends/bilateral.json";
	private InterfaceGetWeiboFanCallBack m_InterfaceGetWeiboFanCallBack;

	public void getWeiboFan(GetWeiboFanParamClass mParams,
			InterfaceGetWeiboFanCallBack callBack) {
		m_InterfaceGetWeiboFanCallBack = callBack;

		StringBuffer sb = new StringBuffer();
		sb.append(getWeiboFanURL);
		sb.append("?");
		if (!TextUtils.isEmpty(mParams.source)) {
//			sb.append("source" + mParams.source);
		}
		if (!TextUtils.isEmpty(mParams.access_token)) {
			sb.append("access_token=" + mParams.access_token);
		}
		sb.append("&uid=" + mParams.uid);
		sb.append("&count=" + mParams.count);
		sb.append("&page=" + mParams.page);
		sb.append("&sort=" + mParams.sort);
		super.requestJosnObjectData(mContext, sb.toString());

	};


	@Override
	public void ProcJSONData(JSONObject response) {
		if (m_InterfaceGetWeiboFanCallBack != null) {
			try {
				String string = (String) response.get("users").toString();
				PreManager.instance().saveBoundWeiboFriendsMsg(mContext, string);
				int total_number = response.getInt("total_number");
				JSONArray jsonAry = new JSONArray(string);
				
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
				m_InterfaceGetWeiboFanCallBack.onSuccess(1, infos, total_number);
			} catch (Exception e) {
				m_InterfaceGetWeiboFanCallBack.onError(0, "获取好友信息失败");
			}
		}
		
	}

	@Override
	public void ProcJSONDataOnErr(VolleyError error) {
		if (m_InterfaceGetWeiboFanCallBack != null) {
			m_InterfaceGetWeiboFanCallBack.onError(0, "获取好友信息失败");
		}
	}

}
