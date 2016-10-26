package com.yzm.sleep.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 		HTTP PROC Class Volley
 *     2015-02-14   by  LZM  
 *     
 * 
 */

public abstract class HttpDataTranUtils {

	public String TAG = "HTTPPROC";
	
//	public static String g_BaseSiteURL="http://10.0.0.110:81/";
	public static String g_BaseSiteURL = URLUtil.BASEURL +"/";
	public static String g_BaseSecond="";
	public static String g_BaseThread="zsly/";
	public static String g_BaseVersionThread="yjapi/";
//	public static String g_BaseCommunityURL="yjapi/";
	
	public void requestStringData(Context context, String strUrl) {

		RequestQueue mRequestQueue = Volley.newRequestQueue(context);
		
		StringRequest mRequest = new StringRequest(Method.POST, strUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, error.getMessage(), error);
					}
				}

		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> mParams = new HashMap<String, String>();
				mParams.put("params1", "value1");
				mParams.put("params2", "value2");
				return mParams;
			}
		
			 @Override
			    public Map<String, String> getHeaders() {
			        HashMap<String, String> headers = new HashMap<String, String>();
			        headers.put("Accept", "application/json");
			        headers.put("Content-Type", "application/json; charset=UTF-8");
			                 
			        return headers;
			    }
		
		};
		mRequestQueue.add(mRequest);
	}

	public void requestJosnObjectData(Context context, String strUrl) {
		
		BaseRequest.get(context, strUrl, null, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					JSONObject obj=new JSONObject(new String(arg2));
					ProcJSONData(obj);
				} catch (Exception e) {
					e.printStackTrace();
					VolleyError error=new VolleyError("数据错误");
					ProcJSONDataOnErr(error);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			
				VolleyError error=new VolleyError("网络错误");
				ProcJSONDataOnErr(error);
				
			}
		});
//		RequestQueue mRequestQueue = Volley.newRequestQueue(context);
//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET,strUrl,
//				null, new Response.Listener<JSONObject>() {
//					@Override
//					public void onResponse(JSONObject response) {
//						ProcJSONData(response);
//					}
//				}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						// Log.e(TAG, error.getMessage(), error);
//						ProcJSONDataOnErr(error);
//					}
//				}){
//					
//			@Override
//		    public Map<String, String> getHeaders() {
//		        HashMap<String, String> headers = new HashMap<String, String>();
//		        headers.put("Charset", "UTF-8"); 		     
//		                 
//		        return headers;
//		    }
//		};
//		mRequestQueue.add(jsonObjectRequest);

	}
	
	
	public void postJosnObjectData(Context context, String strUrl, RequestParams params) {
		BaseRequest.post(strUrl, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					JSONObject obj=new JSONObject(new String(arg2));
					ProcJSONData(obj);
				} catch (Exception e) {
					e.printStackTrace();
					VolleyError error=new VolleyError("数据错误");
					ProcJSONDataOnErr(error);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			
				VolleyError error=new VolleyError("网络错误");
				ProcJSONDataOnErr(error);
				
			}
		});

	}

	public abstract void ProcJSONData(JSONObject response);

	public abstract void ProcJSONDataOnErr(VolleyError error);

}
