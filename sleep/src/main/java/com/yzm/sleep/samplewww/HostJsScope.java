/**
 * Summary: js脚本所能执行的函数空间
 * Version 1.0
 * Date: 13-11-20
 * Time: 下午4:40
 * Copyright: Copyright (c) 2013
 */

package com.yzm.sleep.samplewww;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.SafeWebViewBridge.JsCallback;
import com.yzm.sleep.utillwww.TaskExecutor;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.InterFaceUtilsClass.ExclusiveAudioSendListClass;



//HostJsScope中需要被JS调用的函数，必须定义成public static，且必须包含WebView这个参数
public class HostJsScope{
	public static final String WEB_VISIT_HOMEPAGE_TAG = "fetchUserID";
	public static final String WEB_PLAY_AUDIO_TAG = "playRing";
	public static final String WEB_PLAY_AUDIO_INDEX_TAG = "playRing1";
	public static final String WEB_SHOW_PICTURE_WALL = "browsePhoto";
	public static final String WEB_SHOW_AUDIO_DETAIL = "showurl";
	public static final String WEB_SHOW_PICTURE_WALL_NONE = "nopic";
	public static final String WEB_OPERATE_CAOZUO = "caozuo";
	
	
    /**
     * 短暂气泡提醒
     * @param webView 浏览器
     * @param message 提示信息
     * */
    public static void toast (WebView webView, String message) {
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 可选择时间长短的气泡提醒
     * @param webView 浏览器
     * @param message 提示信息
     * @param isShowLong 提醒时间方式
     * */
    public static void toast (WebView webView, String message, int isShowLong) {
        Toast.makeText(webView.getContext(), message, isShowLong).show();
    }

    /**
     * 弹出记录的测试JS层到Java层代码执行损耗时间差
     * @param webView 浏览器
     * @param timeStamp js层执行时的时间戳
     * */
    public static void testLossTime (WebView webView, long timeStamp) {
        timeStamp = System.currentTimeMillis() - timeStamp;
        alert(webView, String.valueOf(timeStamp));
    }
    
    
    public static class PictureWallInfo implements Serializable{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String path;
    	public String key;
    	public PictureWallInfo(){}
    }
    public static class PictureSelectInfo implements Serializable{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public int index;
    	public ArrayList<PictureWallInfo> photoArray;
    	public PictureSelectInfo(){}
    }
    static WebView mWebView;
    /**
     * 系统弹出提示框
     * @param webView 浏览器
     * @param message 提示信息
     * */
    public static void alert (WebView webView, String message) {
    	mWebView = webView;
//    	ToastManager.getInstance(webView.getContext()).show(message);
    	System.out.println(" index ---webMsg:" + message);
    	String vistString = null;
    	String playAudioStringIndex = null;
    	String playAudioString = null;
    	String showPictureString = null;
    	String noPictureString = null;
    	PictureSelectInfo pictureInfo = null;
    	String showDetailUrlString = null;
    	int caozuo = -1;
    	try {
    		vistString = new JSONObject(message).getString(WEB_VISIT_HOMEPAGE_TAG);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	try {
    		playAudioStringIndex = new JSONObject(message).getString(WEB_PLAY_AUDIO_INDEX_TAG);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	try {
    		playAudioString = new JSONObject(message).getString(WEB_PLAY_AUDIO_TAG);
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
    	
		try {
			showPictureString = new JSONObject(message).getJSONObject("browsePhoto").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			showDetailUrlString = new JSONObject(message).getString(WEB_SHOW_AUDIO_DETAIL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			noPictureString = new JSONObject(message).getString(WEB_SHOW_PICTURE_WALL_NONE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			caozuo = new JSONObject(message).getInt(WEB_OPERATE_CAOZUO);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
		
		Gson gson=new Gson();
		pictureInfo = gson.fromJson(showPictureString, new TypeToken<PictureSelectInfo>(){}.getType());
    		
    	if (!TextUtils.isEmpty(vistString)) {
    		Intent intent = new Intent();
    		intent.setAction("com.www.web.INIT_USER_HOME_PAGE");
    		intent.putExtra("target_int_id", vistString);
//    		webView.getContext().sendBroadcast(intent);
    		webView.getContext().sendOrderedBroadcast(intent, null);
		}else if(!TextUtils.isEmpty(playAudioStringIndex)){
			Intent intent = new Intent();
    		intent.setAction("com.www.web.WEB_PLAY_AUDIO");
    		intent.putExtra("lyid", playAudioStringIndex);
    		webView.getContext().sendBroadcast(intent);
		}else if(!TextUtils.isEmpty(playAudioString)){
			Intent intent = new Intent();
    		intent.setAction("com.www.web.WEB_DETAIL_PLAY_AUDIO");
    		intent.putExtra("lyid", playAudioString);
    		webView.getContext().sendBroadcast(intent);
		}else if (!TextUtils.isEmpty(showPictureString) && pictureInfo != null) {
			Intent intent = new Intent();
			intent.setAction("com.www.web.WEB_VISIT_PICTURE_WALL");
			intent.putExtra("picture_wall_info", pictureInfo);
			webView.getContext().sendBroadcast(intent);
		}else if(!TextUtils.isEmpty(showDetailUrlString)){
			Intent intent = new Intent();
    		intent.setAction("com.www.web.WEB_AUDIO_DETAIL");
    		intent.putExtra("audio_detail_url", showDetailUrlString);
    		webView.getContext().sendBroadcast(intent);
		}else if(!TextUtils.isEmpty(noPictureString)){
			Intent intent = new Intent();
			intent.setAction("com.www.web.WEB_ADD_PICTURE");
			webView.getContext().sendBroadcast(intent);
		}else if (caozuo > -1) {
			if (caozuo == 1) {//加好友
				Intent intent = new Intent();
				intent.setAction("com.www.web.WEB_ADD_FRIEND");
				webView.getContext().sendBroadcast(intent);
			}else if (caozuo == 2) {//发闹钟
				Intent intent = new Intent();
				intent.setAction("com.www.web.WEB_SEND_AUDIO");
				webView.getContext().sendBroadcast(intent);
			}
		}
    }

    public static void alert (WebView webView, int msg) {
        alert(webView, String.valueOf(msg));
    }

    public static void alert (WebView webView, boolean msg) {
        alert(webView, String.valueOf(msg));
    }

    /**
     * 获取设备IMSI
     * @param webView 浏览器
     * @return 设备IMSI
     * */
    public static String getIMSI (WebView webView) {
        return ((TelephonyManager) webView.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * 获取用户系统版本大小
     * @param webView 浏览器
     * @return 安卓SDK版本
     * */
    public static int getOsSdk (WebView webView) {
        return Build.VERSION.SDK_INT;
    }

    //---------------- 界面切换类 ------------------

    /**
     * 结束当前窗口
     * @param view 浏览器
     * */
    public static void goBack (WebView view) {
        if (view.getContext() instanceof Activity) {
            ((Activity)view.getContext()).finish();
        }
    }

    /**
     * 传入Json对象
     * @param view 浏览器
     * @param jo 传入的JSON对象
     * @return 返回对象的第一个键值对
     * */
    public static String passJson2Java (WebView view, JSONObject jo) {
        Iterator iterator = jo.keys();
        String res = null;
        if(iterator.hasNext()) {
            try {
                String keyW = (String)iterator.next();
                res = keyW + ": " + jo.getString(keyW);
            } catch (JSONException je) {

            }
        }
        return res;
    }

    /**
     * 将传入Json对象直接返回
     * @param view 浏览器
     * @param jo 传入的JSON对象
     * @return 返回对象的第一个键值对
     * */
    public static JSONObject retBackPassJson (WebView view, JSONObject jo) {
        return jo;
    }

    public static int overloadMethod(WebView view, int val) {
        return val;
    }

    public static String overloadMethod(WebView view, String val) {
        return val;
    }

    public static class RetJavaObj {
        public int intField;
        public String strField;
        public boolean boolField;
    }

    public static List<RetJavaObj> retJavaObject(WebView view) {
        RetJavaObj obj = new RetJavaObj();
        obj.intField = 1;
        obj.strField = "mine str";
        obj.boolField = true;
        List<RetJavaObj> rets = new ArrayList<RetJavaObj>();
        rets.add(obj);
        return rets;
    }

    public static void delayJsCallBack(WebView view, int ms, final String backMsg, final JsCallback jsCallback) {
        TaskExecutor.scheduleTaskOnUiThread(ms * 1000, new Runnable() {
            @Override
            public void run() {
                try {
                    jsCallback.apply(backMsg);
                } catch (JsCallback.JsCallbackException je) {
                    je.printStackTrace();
                }
            }
        });
    }

    public static long passLongType (WebView view, long i) {
        return i;
    }
    
}