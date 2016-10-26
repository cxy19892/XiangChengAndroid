package com.yzm.sleep.utils;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetQQFriendsListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetQQFriendsListParamClass1;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetQQFriendsListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetQQFriendsListCallBack1;
import com.yzm.sleep.utils.InterFaceUtilsClass.QQFriendsListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.QQFriendsListParamClass1;

public class OtherAccessUtil extends HttpDataTranUtils{
	
	private Context m_context = null;

	public OtherAccessUtil(Context context) {
		m_context = context;
	}
	
	
	
	
	
	
	//==================================================================================================
	//QQ获取已安装了应用的好友列表
	private String getQQFriendsListUrl = "http://113.108.20.23/v3/relation/get_app_friends?";
	private InterfaceGetQQFriendsListCallBack m_InterfaceGetQQFriendsListCallBack;

	public void getQQFriendsList(GetQQFriendsListParamClass mParam,
			InterfaceGetQQFriendsListCallBack callBack) {
		m_InterfaceGetQQFriendsListCallBack = callBack;

		String newString = getQQFriendsListUrl + 
				"openid=" + mParam.openid +
				"&openkey="+ mParam.openkey +
				"&appid="+ mParam.appid + 
				"&sig=" + mParam.sig + 
				"&pf=" + mParam.pf +
				"&format=" + mParam.format +
				"&userip=" + mParam.userip
				;
		super.requestJosnObjectData(m_context, newString);

	};
	
	//////111
	private String getQQFriendsListUrl_1 = "https://graph.qq.com/user/get_app_friends?";
	private InterfaceGetQQFriendsListCallBack1 m_InterfaceGetQQFriendsListCallBack1;
	
	public void getQQFriendsList1(GetQQFriendsListParamClass1 mParam,
			InterfaceGetQQFriendsListCallBack1 callBack) {
		m_InterfaceGetQQFriendsListCallBack1 = callBack;
		
		String newString = getQQFriendsListUrl_1 + 
				"access_token=" + mParam.access_token +
				"&oauth_consumer_key="+ mParam.oauth_consumer_key +
				"&openid="+ mParam.openid + 
				"&format=" + mParam.format
				;
		super.requestJosnObjectData(m_context, newString);
		
	};
	
	
	
	
	
	//=======================================================================================

	@Override
	public void ProcJSONData(JSONObject response) {
		int idValue = -1;

		/*try {

			idValue = (Integer) response.get("ret");
			int is_lost = (Integer) response.get("is_lost");

			if (idValue == 0) {//正确返回
				String string = (String) response.get("items").toString();
				Gson gson=new Gson();
				
				List<QQFriendsListParamClass> list = gson.fromJson(string, new TypeToken<List<QQFriendsListParamClass>>(){}.getType());
				
				m_InterfaceGetQQFriendsListCallBack.onSuccess(list, 0, is_lost);

			}else if (idValue >0) {
				m_InterfaceGetQQFriendsListCallBack.onError(idValue, "调用OpenAPI时发生错误");
			} else if (idValue >= -50 && idValue <= -1) {
				m_InterfaceGetQQFriendsListCallBack.onError(idValue, "接口调用不能通过接口代理机校验");
			}else if (idValue <-50) {
				m_InterfaceGetQQFriendsListCallBack.onError(idValue, "系统内部错误，请通过企业QQ联系技术支持");
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		
		
		/////1
		try {
			
			idValue = (Integer) response.get("ret");
			String msg = (String) response.get("msg").toString();
			
			if (idValue == 0) {//正确返回
				String string = (String) response.get("items").toString();
				Gson gson=new Gson();
				
				List<QQFriendsListParamClass1> list = gson.fromJson(string, new TypeToken<List<QQFriendsListParamClass1>>(){}.getType());
				
				m_InterfaceGetQQFriendsListCallBack1.onSuccess(list, idValue, msg);
				
			}else {
				m_InterfaceGetQQFriendsListCallBack1.onError(idValue, "错误");
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void ProcJSONDataOnErr(VolleyError error) {
		String errmsg = "访问服务器失败";
		
		if (m_InterfaceGetQQFriendsListCallBack != null) {
			m_InterfaceGetQQFriendsListCallBack.onError(3001, errmsg);
		}
	}

}
