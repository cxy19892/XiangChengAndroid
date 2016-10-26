package com.yzm.sleep.utils;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.GetWeixinAccessTokenParamClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.GetWeixinUserInfoRstClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.InterfaceGetWeixinAccessTokenCallBack;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.InterfaceGetWeixinUserInfoCallBack;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.InterfaceRefreshTokenCallBack;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.RefreshTokenParamClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.RefreshTokenRstClass;
import com.yzm.sleep.utils.InterFaceThirdUtilsClass.WeixinAccessTokenRstClass;

public class WeiXinDataProClass extends HttpDataTranUtils {
	private Context mContext;

	public WeiXinDataProClass(Context context) {
		this.mContext = context;
	}

	// 获取微信 access_token 等相关信息
	InterfaceGetWeixinAccessTokenCallBack m_InterfaceGetWeixinAccessTokenCallBack;
	public void getAccessToken(GetWeixinAccessTokenParamClass mParam,
			InterfaceGetWeixinAccessTokenCallBack callBack) {
		m_InterfaceGetWeixinAccessTokenCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(URLUtil.GET_WX_ACCESSTOKEN_URL);
		sb.append("?appid=" + mParam.appid);
		sb.append("&secret=" + mParam.secret);
		sb.append("&code=" + mParam.code);
		sb.append("&grant_type=" + mParam.grant_type);
		super.requestJosnObjectData(mContext, sb.toString());
	}

	// 微信 刷新 access_token
	InterfaceRefreshTokenCallBack m_InterfaceRefreshTokenCallBack;
	public void refreshToken(RefreshTokenParamClass mParam,InterfaceRefreshTokenCallBack callBack) {
		m_InterfaceRefreshTokenCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(URLUtil.REFRESH_WX_ACCESSTOKEN_URL);
		sb.append("?appid=" + mParam.appid);
		sb.append("&grant_type=" + mParam.grant_type);
		sb.append("&refresh_token=" + mParam.refresh_token);
		super.requestJosnObjectData(mContext, sb.toString());
	}

	// 获取微信用户信息
	InterfaceGetWeixinUserInfoCallBack m_InterfaceGetWeixinUserInfoCallBack;
	public void getWeixinUserInfo(String openid, String access_token,InterfaceGetWeixinUserInfoCallBack callBack) {
		m_InterfaceGetWeixinUserInfoCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(URLUtil.GET_WX_USERINFO_URL);
		sb.append("?access_token=" + access_token);
		sb.append("&openid=" + openid);
		super.requestJosnObjectData(mContext, sb.toString());
	}

	@SuppressLint("ShowToast") @Override
	public void ProcJSONData(JSONObject response) {
		try {
			if (m_InterfaceGetWeixinAccessTokenCallBack != null) {
				WeixinAccessTokenRstClass param = new WeixinAccessTokenRstClass();
				if (response.has("access_token")) {
					param.access_token = response.getString("access_token");
				}
				if (response.has("openid")) {
					param.openid = response.getString("openid");
				}
				if (response.has("refresh_token")) {
					param.refresh_token = response.getString("refresh_token");
				}
				m_InterfaceGetWeixinAccessTokenCallBack.onSuccess(0, param);
				
			}else if(m_InterfaceRefreshTokenCallBack != null){
				RefreshTokenRstClass param = new RefreshTokenRstClass();
				param.access_token = response.getString("access_token");
				param.openid = response.getString("openid");
				m_InterfaceRefreshTokenCallBack.onSuccess(0, param);
			}else if (m_InterfaceGetWeixinUserInfoCallBack != null) {
				GetWeixinUserInfoRstClass param = new GetWeixinUserInfoRstClass();
				if (response.has("nickname")) {
					param.nickname = response.getString("nickname");
					param.nickname = !TextUtils.isEmpty(param.nickname) ? param.nickname : "xc_"
							+ SleepUtils.getFormatedDateTime("yyyyMMdd",
									System.currentTimeMillis());
				}
				if (response.has("headimgurl")) {
					param.headimgurl = response.getString("headimgurl");
					param.headimgurl = !TextUtils.isEmpty(param.headimgurl) ? param.headimgurl :
						URLUtil.XIANGCHENG_ICON_URL;
				}
				if (response.has("unionid")) {
					param.unionid = response.getString("unionid");
				}
				m_InterfaceGetWeixinUserInfoCallBack.onSuccess(0, param);
			}else {
				requestFailed(mContext.getResources().getString(R.string.net_requet_error_tip), 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			requestFailed(mContext.getResources().getString(R.string.net_requet_exception_tip), 0);
		}

	}

	@Override
	public void ProcJSONDataOnErr(VolleyError error) {
		String strErrMsg = "访问服务器失败";
		int icode = 3001;
		requestFailed(strErrMsg, icode);
		
	}

	private void requestFailed(String strErrMsg, int icode) {
		if (m_InterfaceGetWeixinAccessTokenCallBack != null) {
			m_InterfaceGetWeixinAccessTokenCallBack.onError(icode, strErrMsg);
		}
		if (m_InterfaceRefreshTokenCallBack != null) {
			m_InterfaceRefreshTokenCallBack.onError(icode, strErrMsg);
		}
		if (m_InterfaceGetWeixinUserInfoCallBack != null) {
			m_InterfaceGetWeixinUserInfoCallBack.onError(icode, strErrMsg);
		}
	}

}
