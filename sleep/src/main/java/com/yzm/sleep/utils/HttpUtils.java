package com.yzm.sleep.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;


public class HttpUtils {
	private static HttpUtils instance;
	private static final int TIMEOUT_CONNECTION = 8000;
	private static final int TIMEOUT_SO = 8000;
	
//	public static synchronized HttpUtils instance() {
//		if (instance == null)
//			instance = new HttpUtils();
//		return instance;
//	}
	
	public String doGetString(String strUrl) throws Exception{
		String strResult = null;
		HttpResponse response = doConnection(strUrl);
        if(isResponseNotFound(response)){
        	throw new Exception("Not Found");
        }
		if(isResponseAvailable(response)){
			strResult = EntityUtils.toString(response.getEntity(),"utf-8");				//response编码
		}
		return strResult;
	}
	
	public static String doPost(String url, ArrayList<NameValuePair> data) throws Exception{
//		String strResult = null;
		HttpPost httpPost =new HttpPost(url);  
        HttpResponse httpResponse = null; 
        httpPost.setEntity(new UrlEncodedFormEntity((List<? extends org.apache.http.NameValuePair>) data, HTTP.UTF_8)); 
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT_SO);
        httpResponse = httpClient.execute(httpPost);
        
        if (httpResponse.getStatusLine().getStatusCode() == 200) { 
        	return EntityUtils.toString(httpResponse.getEntity(),"utf-8");
        } 
		return "";		
	}
	
	private static HttpResponse doConnection(String strUrl) throws Exception{
		HttpResponse response = null;
		final URI uri = new URI(strUrl);
		HttpGet httpGet = new HttpGet(uri);
		if(URLUtil.SESSIONID != null){
			httpGet.addHeader("Cookie","JSESSIONID="+URLUtil.SESSIONID);	
		}
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT_SO);
        
        response = httpClient.execute(httpGet);
        
    	CookieStore mCookieStore = ((DefaultHttpClient) httpClient).getCookieStore();
    	List<Cookie> cookies = mCookieStore.getCookies();
    	for(Cookie cookie:cookies){
    		if("JSESSIONID".equals(cookie.getName())){
    			if(URLUtil.SESSIONID == null){						//客户端保存sessionid
    				URLUtil.SESSIONID = cookie.getValue();
    			}
    				
    			break;
    		}
    	}
		return response;
	}
	
	private static boolean	isResponseAvailable(HttpResponse response){
		if(response != null && 
				response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			return true;
		}
		return false;
	}
	
	private static boolean isResponseNotFound(HttpResponse response){
		if(response != null &&
				response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND){
			return true;
		}
		return false;
	}
	
	/**
	 * 注册
	 * 
	 * @param context
	 * @param my_int_username
	 * @param my_int_nickname
	 * @param my_int_pwd
	 * @param my_int_age
	 * @param my_int_gender
	 * @param my_int_occupation
	 * @return
	 * @throws Exception 
	 */
	public String doRegist(Context context, String my_int_username,
			String my_int_nickname, String my_int_pwd, String my_int_age,
			String my_int_gender, String my_int_occupation) throws Exception {
		String url = String.format(SleepConstants.DO_SIGNUP,
				URLUtil.SIGNUP_URL, my_int_username, my_int_nickname,
				my_int_pwd, my_int_age, my_int_gender, my_int_occupation);
		return doGetString(url);
	}
	public String doLogin(Context context) throws Exception {
		String url = "http://10.0.0.110/internal_login.php?my_int_username=18781987559&my_int_pwd=123";
		return doGetString(url);
	}
	
	/**
	 * 保存第三方用户信息
	 * 
	 * @param platform
	 *            登录平台
	 * @param my_ext_acc
	 *            平台用户标识ID
	 * @param my_ext_nickname
	 *            用户昵称
	 * @param my_ext_profile
	 *            用户头像
	 * @param my_int_age
	 *            用户年龄
	 * @param my_int_gender
	 *            用户性别
	 * @param my_int_occupation
	 *            用户职业
	 * @param friend_num
	 *            好友数量
	 * @param friendacc_1
	 *            好友id
	 * @return
	 * @throws Exception 
	 */
	public String saveThirdPartyUserInfo(String platform, String my_ext_acc,
			String my_ext_nickname, String my_ext_profile, String my_int_age,
			String my_int_gender, String my_int_occupation, String friend_num,
			ArrayList<String> ids) throws Exception {
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("platform", platform));
		data.add(new BasicNameValuePair("my_ext_acc", my_ext_acc));
		data.add(new BasicNameValuePair("my_ext_nickname", my_ext_nickname));
		data.add(new BasicNameValuePair("my_ext_profile", my_ext_profile));
		data.add(new BasicNameValuePair("my_int_age", my_int_age));
		data.add(new BasicNameValuePair("my_int_gender", my_int_gender));
		data.add(new BasicNameValuePair("my_int_occupation", my_int_occupation));
		data.add(new BasicNameValuePair("friend_num", friend_num));
		
		for(int i = 1;i< ids.size() + 1 ;i++){
			data.add(new BasicNameValuePair("friendacc_" + i , ids.get(i-1)));
		}
			
		return doPost(URLUtil.SAVE_THIRDPARTY_USERINFO_URL, data);
//		return doGetString(url);
	}
	public String test() throws Exception{
		return doGetString("http://10.9.39.174/~leiyuechuan/newfiles/test.php");
	}
}
