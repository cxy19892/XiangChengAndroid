package com.yzm.sleep.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpClientConnection;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public final class BaseRequest {

    private BaseRequest() {
    }

//    private static final String TAG = "BaseRequest";

    /**
     * Remote server address
     */
    public static final int TIME_OUT = 15 * 1000; // 5秒

    /**
     * Remote server address
     */
    /**
     * 新加坡服务器
     */
    // public static final String BASE_URL = "http://54.254.213.121";
    
    public static  final String SEARCH_URL="";
    
    /**
     * 公司开发服务器
     */
    //public static final String BASE_URL = "http://172.16.2.240";
    /**
     * The mobile of the connection address.
     */
   

    /**
     * Connect to the server success.
     */
    public static final String SERVER_REQUEST_SUCCESS = "1";
    /**
     * return server status.
     */
    public static final String RESPONSECODE = "status";
    /**
     * return server error info.
     */
    public static final String ERRORINFO = "errorinfo";

    private static AsyncHttpClient mClient = new AsyncHttpClient();

    private static PersistentCookieStore myCookieStore;

    public static String header;

    static {
        mClient.setTimeout(TIME_OUT);
    }

    /**
     * @param url
     *            address
     * @param params
     *            parameter
     * @param responseHandler
     *            response
     */
    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//    	LogUtil.i(TAG, "get url:"+url);
    	try {
			if(mClient!=null)
			mClient.get(context, url, params, responseHandler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    public static void getFlie(String url,AsyncHttpResponseHandler responseHandler){
    	if(mClient!=null)
    	mClient.get(url, responseHandler);
    }


    /**
     * @param url
     *            address
     * @param params
     *            parameter
     * @param responseHandler
     *            response
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.i("chen", "post params:"+params);
        mClient.post(url, params, responseHandler);
        
    }

    public static void post(Context mContext, String url, AsyncHttpResponseHandler responseHandler) {
        StringEntity json_data = null;
        try {
            json_data = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Header> headers = new ArrayList<Header>();
        mClient.post(mContext, url, headers.toArray(new Header[headers.size()]), json_data, null, responseHandler);
    }

   /* *//**
     * @param relativeUrl
     *            get relativeUrl
     * @return
     *//*
    private static String getAbsoluteUrl(String relativeUrl) {
        Log.d(TAG, "getAbsoluteUrl url:" + BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }*/

    /**
     * @param context
     *            context
     * @param mayInterruptIfRunning
     *            see if cancelRequest or not
     */
    public static void cancelRequest(Context context, boolean mayInterruptIfRunning) {
        mClient.cancelRequests(context, mayInterruptIfRunning);
    }

    /**
     * @param url
     *            address
     * @param params
     *            parameter
     * @param responseHandler
     *            response
     */
    public static void postPic(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.addHeader("Content-Type", "multipart/form-data");
        mClient.post(url, params, responseHandler);
    }

//    public static void postWithJSON(Context mContext, String url, JSON params, AsyncHttpResponseHandler responseHandler) {
//        StringEntity json_data = null;
//        if (params != null) {
//            try {
//                json_data = new StringEntity(params.toString(), "UTF-8");
//                json_data.setContentType(new BasicHeader("Content-Type", "application/json"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//       
//        mClient.addHeader("Content-Type", "application/json");
//        mClient.post(mContext, url, null, json_data, null, responseHandler);
//
//    }

    /*
     * public static void postWithJSON(Context mContext, String url, String json, AsyncHttpResponseHandler responseHandler) { StringEntity json_data =
     * null; try { json_data = new StringEntity(json, "UTF-8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
     * json_data.setContentType(new BasicHeader("Content-Type", "application/json")); mClient.addHeader("Content-Type", "application/json");
     * mClient.post(mContext, url, null, json_data, null, responseHandler); }
     */

    public static void postPic(Context context, String url, FileEntity entity, AsyncHttpResponseHandler handler) {
        mClient.post(context, url, entity, "image/jpeg", handler);
    }
    
    public static void postFile(Context context, String url, FileEntity entity, AsyncHttpResponseHandler handler) {
        mClient.post(context, url, entity, "multipart/form-data", handler);
    }

    public static AsyncHttpClient getClient() {
        return mClient;
    }

    public static PersistentCookieStore getMyCookieStore() {
        return myCookieStore;
    }

    public static void setMyCookieStore(PersistentCookieStore myCookieStore) {
        BaseRequest.myCookieStore = myCookieStore;
    }

}
