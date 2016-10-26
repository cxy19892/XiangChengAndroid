package com.yzm.sleep.threadpool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.SleepResult;
import com.yzm.sleep.utils.HttpDataTranUtils;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadSleepDataCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadSleepDataParamClass;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.SleepDataProClass;

/**
 *  通过网络上传睡眠数据到服务器备份
 * @author Administrator
 *
 */
public class SoftDataUpLoadTask implements Runnable {

	private SleepResult result;
	private ThreadEndInterface threadEnd;
	private Context context;
	
	public SoftDataUpLoadTask(SleepResult result, Context context){
		this.result = result;
		this.context = context;
	}
	
	public void run() {
		uploadSleepSoftData();
	}
	
	public SleepResult getTaskName(){
		return result;
	}

	public void setThreadEndInterface(ThreadEndInterface threadEnd){
		this.threadEnd = threadEnd;
	}
	
	public interface ThreadEndInterface{
		public void isSuccess(SleepResult result, boolean is);
	}
	
	
	/**
	 *  上传一天的睡眠数据到服务器
	 * @param context
	 * @param result
	 */
	@SuppressLint("SimpleDateFormat") 
	private void uploadSleepSoftData(){
		String url = HttpDataTranUtils.g_BaseSiteURL + HttpDataTranUtils.g_BaseVersionThread + "upload_user_data.php?";
		/*建立HTTP Post连线*/  
	    HttpPost httpRequest =new HttpPost(url);  
	    //Post运作传送变数必须用NameValuePair[]阵列储存  
	    //传参数 服务端获取的方法为request.getParameter("name")  
	    String date = result.getDate().replace("-", "");
	    String sleepTime = "";//(result.getDate() + result.getSleeptime()).replace("-", "").replace(":", "");
	    String getUpTime = "";//(result.getDate() + result.getUptime()).replace("-", "").replace(":", "");
	    try {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	    	sleepTime = sdf.format(new Date(Long.parseLong(result.getSleeptimelong())));
	    	getUpTime = sdf.format(new Date(Long.parseLong(result.getUptimelong())));
		} catch (Exception e1) {
			e1.printStackTrace();
			if(threadEnd != null)
				threadEnd.isSuccess(result, false);
			return;
		}
	    
	    
	    List <NameValuePair> params=new ArrayList<NameValuePair>();  
	    params.add(new BasicNameValuePair("my_int_id", PreManager.instance().getUserId(context))); 
	    params.add(new BasicNameValuePair("date_of_data", date)); 
	    params.add(new BasicNameValuePair("sleep_point", sleepTime)); 
	    params.add(new BasicNameValuePair("wakeup_point", getUpTime)); 
	    params.add(new BasicNameValuePair("user_location_x", PreManager.instance().getLocation_x(context))); 
	    params.add(new BasicNameValuePair("user_location_y", PreManager.instance().getLocation_y(context))); 
	    params.add(new BasicNameValuePair("sleep_duration", String.valueOf(countLengthByTime()))); 
	   
	    params.add(new BasicNameValuePair("my_int_occupation", "200000001")); 
	    try{
		     httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));  
		     HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);  
		     //若状态码为200 ok   
		     if(httpResponse.getStatusLine().getStatusCode() == 200){  
		    	 //上传成功后把数据库上传的状态切换到1，表示已上传
		    	 String response = EntityUtils.toString(httpResponse  
                         .getEntity()); 
		    	 if(response.contains("4036") || response.contains("4037")){
		    		 DBOperation.initinstance(context).updateUploadState(result.getDate(), "1");
			    	 if(threadEnd != null)
							threadEnd.isSuccess(result, true);
			    	 return;
		    	 }
		     } 
	    }catch(Exception e){  
	    	e.printStackTrace();  
	    } 
	    if(threadEnd != null)
			threadEnd.isSuccess(result, false);
	}
	
	/**
	 * 根据起床时间和入睡时间，计算睡眠时长
	 * @param alarmSleepTime  入睡时间（开始时间）
	 * @param alarmGetUpTime  起床时间（结束时间）
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	private float countLengthByTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			long sleepTimeLong = sdf.parse(result.getSleeptime()).getTime();
			long getUpTimeLong = sdf.parse(result.getUptime()).getTime();
			long totalTime = getUpTimeLong - sleepTimeLong;
			if(totalTime < 0)
				totalTime += 24 * 60 * 60 * 1000;
			return totalTime/((float)60 * 60 * 1000);
		} catch (Exception e) {
		}
		return 0;
	}
}
