package com.yzm.sleep.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownLoadRomInfoClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadHardwareDayDataClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadHardwareDayDataParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadSleepDataParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.FriendsRankListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.FriendsSleepDataListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetDownLoadRomInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetFriendsRankListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetFriendsSleepDataListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetNearbySleepDataListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetNearbyUserListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownLoadRomCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownloadHardwareDayDataCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownloadSleepDataListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceFriendsRankListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceFriendsSleepDataListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetRomVersionInfoCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceNearbySleepDataListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceNearbyUserListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareAllDayCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareDataCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareDayCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareSleepDataCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadSleepDataCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.NearbySleepDataListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.NearbyUserListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.RomVersionInfoClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareAllDayParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareDataParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareDayParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareSleepDataParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadSleepDataParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserSleepDataListClass;
import com.yzm.sleep.utils.UploadUtil.OnUploadProcessListener;

public class SleepDataProClass extends HttpDataTranUtils{
	
	private Context m_context = null;

	public SleepDataProClass(Context context) {
		m_context = context;
	}
	
	
	//=======================================old============================================
	//wakedata_friends_list.php         睡眠数据好友排名列表增加分页
	private String FriendsSleepDataListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"wakedata_friends_list.php?";
	
	private InterfaceFriendsSleepDataListCallBack m_InterfaceFriendsSleepDataListCallBack;
	
	public void DownloadFriendsRankList(GetFriendsSleepDataListParamClass mParam,InterfaceFriendsSleepDataListCallBack callBack) {

		m_InterfaceFriendsSleepDataListCallBack = callBack;
		String newString = FriendsSleepDataListURL + "my_int_id=" + mParam.my_int_id + "&date_of_data=" + mParam.date_of_data
				+"&page=" + mParam.page + "&pagesize=" + mParam.pagesize;
		super.requestJosnObjectData(m_context, newString);

	}
	
	private void DownloadFriendsRankListRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4361")) {

				m_InterfaceFriendsSleepDataListCallBack.onError(4361,"本人ID不存在");

			} else if (idValue.equals("4362")) {

				m_InterfaceFriendsSleepDataListCallBack.onError(4362,"错误的参数");

			} else if (idValue.equals("4363"))
			{
				//获取好友睡眠数据成功
				Gson gson=new Gson();
				String	listV = (String) response.get("friend_list").toString();
				List<FriendsSleepDataListParamClass> list = gson.fromJson(listV, new TypeToken<List<FriendsSleepDataListParamClass>>(){}.getType());
				int num = response.getInt("list_nums");
				if(list == null)
					list = new ArrayList<InterFaceUtilsClass.FriendsSleepDataListParamClass>();
				m_InterfaceFriendsSleepDataListCallBack.onSuccess(4363, list,num);
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	//===================================================================================
	//friends_add_tag.php        好友排行
	
	
	private String FriendsRankListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"friends_add_tag.php?";
	
	private InterfaceFriendsRankListCallBack m_InterfaceFriendsRankListCallBack;
	
	public void DownloadFriendsRankListAdd(GetFriendsRankListParamClass mParam,InterfaceFriendsRankListCallBack callBack) {
		m_InterfaceFriendsRankListCallBack = callBack;
		StringBuffer sb = new StringBuffer(); 
		sb.append(FriendsRankListURL);
		sb.append("my_int_id=" + mParam.my_int_id);
		if (!TextUtils.isEmpty(mParam.laiyuan) &&!TextUtils.isEmpty(mParam.data) && !mParam.laiyuan.equals("xiangcheng")) {
			sb.append("&laiyuan=" + mParam.laiyuan);
			sb.append("&data=" + mParam.data);
		}
		String newString = sb.toString();
		System.out.println("my url"+newString);
		super.requestJosnObjectData(m_context, newString);

	}
	
	private void DownloadFriendsRankListAddRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4478")) {

				m_InterfaceFriendsRankListCallBack.onError(4478,"获取好友列表失败");

			} else if (idValue.equals("4479"))
			{
				//获取好友睡眠数据成功
				Gson gson=new Gson();
				String	listV = (String) response.get("friends_list").toString();
				List<FriendsRankListParamClass> list = gson.fromJson(listV, new TypeToken<List<FriendsRankListParamClass>>(){}.getType());
//				int num = response.getInt("list_nums");
				m_InterfaceFriendsRankListCallBack.onSuccess(4479, list);
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	//===================================================================================
	//wakedata_nearby_list.php         睡眠数据附件排名列表增加分页
	
	
	private String NearbySleepDataListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"wakedata_nearby_list.php?";
	
	private InterfaceNearbySleepDataListCallBack m_InterfaceNearbySleepDataListCallBack;
	
	public void DownloadNearbyRankList(GetNearbySleepDataListParamClass mParam,InterfaceNearbySleepDataListCallBack callBack) {

		m_InterfaceNearbySleepDataListCallBack = callBack;
		String newString = NearbySleepDataListURL 
				+ "date_of_data=" + mParam.date_of_data 
				+ "&user_location_x=" + mParam.user_location_x
				+ "&user_location_y=" + mParam.user_location_y
				+ "&page=" + mParam.page 
				+ "&pagesize=" + mParam.pagesize;

		super.requestJosnObjectData(m_context, newString);

	}
	
	private void DownloadNearbyRankListRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4364")) {

				m_InterfaceNearbySleepDataListCallBack.onError(4364,"错误的位置和日期信息");

			} else if (idValue.equals("4365")) {

				m_InterfaceNearbySleepDataListCallBack.onError(4365,"附近没有人员信息");

			} else if (idValue.equals("4366"))
			{
				//获取附近用户睡眠数据成功
				Gson gson=new Gson();
				String	listV = (String) response.get("nearby_list").toString();
				List<NearbySleepDataListParamClass> list = gson.fromJson(listV, new TypeToken<List<NearbySleepDataListParamClass>>(){}.getType());
				int num = response.getInt("list_nums");
				m_InterfaceNearbySleepDataListCallBack.onSuccess(4366, list,num);
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//===========================================================================================
	//user_nearby_list.php        返回附近的人筛选后的列表。
	
	private String NearbyUserListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"user_nearby_list.php?";
	
	private InterfaceNearbyUserListCallBack m_InterfaceNearbyUserListCallBack;
	
	public void DownloadNearbyRankList(GetNearbyUserListParamClass mParam,InterfaceNearbyUserListCallBack callBack) {

		m_InterfaceNearbyUserListCallBack = callBack;
		
		StringBuffer sb = new StringBuffer();
		sb.append(NearbyUserListURL);
		sb.append("page=" + mParam.page);
		sb.append("&pagesize=" + mParam.pagesize);
		
		if (!TextUtils.isEmpty(mParam.search)) {
			sb.append("&search=" + mParam.search);
		}
		
		sb.append("&user_x=" + mParam.user_x);
		sb.append("&user_y=" + mParam.user_y);
		
		if (!TextUtils.isEmpty(mParam.sex)) {
			sb.append("&sex=" + mParam.sex);
		}

		String newString = sb.toString();
		super.requestJosnObjectData(m_context, newString);

	}
	
	private void DownloadNearbyUserListRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4476")) {

				m_InterfaceNearbyUserListCallBack.onError(4476,"获取数据失败");

			}else if (idValue.equals("4477"))
			{
				//获取附近用户睡眠数据成功
				Gson gson=new Gson();
				String	listV = (String) response.get("user_list").toString();
				List<NearbyUserListParamClass> list = gson.fromJson(listV, new TypeToken<List<NearbyUserListParamClass>>(){}.getType());
				int num = response.getInt("list_nums");
				m_InterfaceNearbyUserListCallBack.onSuccess(4477, list,num);
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	//===========================================================================================
	//===========================================================================================
	//download_sleepdata_by_id.php  根据登录用户ID获取睡眠数据
	
	private String DownloadSleepDataListURL = g_BaseSiteURL + g_BaseVersionThread + "download_sleepdata_by_id.php?";
	
	private InterfaceDownloadSleepDataListCallBack m_InterfaceDownloadSleepDataListCallBack;
	
	public void DownloadSleepDataList(DownloadSleepDataParamClass mParam,InterfaceDownloadSleepDataListCallBack callBack) {

		m_InterfaceDownloadSleepDataListCallBack = callBack;
		String newString = DownloadSleepDataListURL + "my_int_id=" + mParam.my_int_id ;
		super.requestJosnObjectData(m_context, newString);

	}
	
	private void DownloadSleepDataListRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4442")) {

				m_InterfaceDownloadSleepDataListCallBack.onError(4442,"ID不存在或者格式错误");

			}else if (idValue.equals("4443"))
			{
				//获取附近用户睡眠数据成功
				Gson gson=new Gson();
				String	listV = (String) response.get("datas").toString();
				List<UserSleepDataListClass> list = gson.fromJson(listV, new TypeToken<List<UserSleepDataListClass>>(){}.getType());
//				int num = response.getInt("list_nums");
				m_InterfaceDownloadSleepDataListCallBack.onSuccess(4443, list);
			
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceDownloadSleepDataListCallBack.onError(0, "未知错误");
		}
	}
	
	
	//===========================================================================================
	//upload_user_data.php  用于上传用户某日的睡眠数据
	
	private String UploadSleepDataListURL = g_BaseSiteURL + g_BaseVersionThread + "upload_user_data.php?";
	
	private InterfaceUploadSleepDataCallBack m_InterfaceUploadSleepDataCallBack;
	
	public void UploadSleepDataList(UploadSleepDataParamClass mParam,InterfaceUploadSleepDataCallBack callBack) {

		m_InterfaceUploadSleepDataCallBack = callBack;
		String newString = UploadSleepDataListURL +
				"my_int_id=" + mParam.my_int_id +
			    "&date_of_data=" + mParam.date_of_data + 
			    "&sleep_point=" + mParam.sleep_point + 
			    "&wakeup_point=" + mParam.wakeup_point + 
			    "&user_location_x=" + mParam.user_location_x +
			    "&user_location_y=" + mParam.user_location_y +
			    "&sleep_duration=" + mParam.sleep_duration +
			    "&my_int_occupation=" + mParam.my_int_occupation;

		super.requestJosnObjectData(m_context, newString);

	}
	
	private void UploadSleepDataListRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4033")) {

				m_InterfaceUploadSleepDataCallBack.onError(4033,"错误：用户ID不存在");

			}else if (idValue.equals("4034")) {

				m_InterfaceUploadSleepDataCallBack.onError(4034,"错误：用户ID不存在");

			}else if (idValue.equals("4035")) {

				m_InterfaceUploadSleepDataCallBack.onError(4035,"错误：参数长度或内容非法");

			}else if (idValue.equals("4036")){
				m_InterfaceUploadSleepDataCallBack.onSuccess(4036, "成功覆盖睡眠数据");
			
			}else if (idValue.equals("4037:1")){
				m_InterfaceUploadSleepDataCallBack.onSuccess(40371, "成功创建睡眠数据；统计添加成功");
			}else if (idValue.equals("4037:2")){
				m_InterfaceUploadSleepDataCallBack.onSuccess(40372, "成功创建睡眠数据；统计更新成功");
			}else if (idValue.equals("4037:3")){
				m_InterfaceUploadSleepDataCallBack.onSuccess(40373, "成功创建睡眠数据；统计添加失败");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//===========================================================================================
	//download_rom.php  下载最新的ROM包
	
	
	private String DownloadNewRomURL = g_BaseSiteURL + g_BaseVersionThread +"download_rom.php?";
	
	private InterfaceDownLoadRomCallBack m_InterfaceDownLoadRomCallBack;
	
	public void GetDownloadNewRomUrl(GetDownLoadRomInfoParamClass mParam,InterfaceDownLoadRomCallBack callBack) {

		m_InterfaceDownLoadRomCallBack = callBack;
		String newString = DownloadNewRomURL + "version=" + mParam.version ;

		super.requestJosnObjectData(m_context, newString);
	}
	
	private void GetDownloadNewRomUrlRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4447")) {

				m_InterfaceDownLoadRomCallBack.onError(4447,"已经是最新版本");

			}else if (idValue.equals("4448")) {

				m_InterfaceDownLoadRomCallBack.onError(4448,"获取失败");

			}else if (idValue.equals("4449"))
			{
				String	rom_url = (String) response.get("rom_url").toString();
				String	version_new = (String) response.get("version_new").toString();
				DownLoadRomInfoClass romInfo = new DownLoadRomInfoClass();
				romInfo.rom_url = rom_url;
				romInfo.version_new = version_new;
				m_InterfaceDownLoadRomCallBack.onSuccess(4449, romInfo);
			
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//===========================================================================================
	//download_version_by_id.php  获取ROM包版本以及服务器时间
	
	private String GetRomVersionInfoURL = g_BaseSiteURL+g_BaseVersionThread+"download_version_by_id.php";
	
	private InterfaceGetRomVersionInfoCallBack m_InterfaceGetRomVersionInfoCallBack;
	
	public void GetRomVersionInfo(InterfaceGetRomVersionInfoCallBack callBack) {

		m_InterfaceGetRomVersionInfoCallBack = callBack;
//		String newString = DownloadNewRomURL + "version=" + mParam.version ;

		super.requestJosnObjectData(m_context, GetRomVersionInfoURL);

	}
	
	private void GetRomVersionInfoRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4451")) {

				m_InterfaceGetRomVersionInfoCallBack.onError(4451,"获取rom版本信息失败");

			}else if (idValue.equals("4452"))
			{
				String	version = (String) response.get("version").toString();
				String	sys_date = (String) response.get("sys_date").toString();
				RomVersionInfoClass versionInfo = new RomVersionInfoClass();
				versionInfo.version = version;
				versionInfo.sys_date = sys_date;
				m_InterfaceGetRomVersionInfoCallBack.onSuccess(4452, versionInfo);
			
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//===========================================================================================
	//upload_hardware_sleep_data.php  上传硬件每日睡眠数据
	private String UploadHardwareSleepDataURL = g_BaseSiteURL+g_BaseVersionThread+"upload_hardware_sleep_data.php?";
	
	private InterfaceUploadHardwareSleepDataCallBack m_InterfaceUploadHardwareSleepDataCallBack;
	
	
	public void UploadHardwareSleepData(UploadHardwareSleepDataParamClass mParam,InterfaceUploadHardwareSleepDataCallBack callBack) {

		m_InterfaceUploadHardwareSleepDataCallBack = callBack;
		String newString = UploadHardwareSleepDataURL +
				"my_int_id=" + mParam.my_int_id +
				"&date_of_data=" + mParam.date_of_data +
				"&XStart=" + mParam.sleepDataHead.XStart +
				"&XStop=" + mParam.sleepDataHead.XStop +
				"&YMax=" + mParam.sleepDataHead.YMax +
				"&Deep_Sleep=" + mParam.sleepDataHead.Deep_Sleep +
				"&Shallow_Sleep=" + mParam.sleepDataHead.Shallow_Sleep +
				"&AwakeTime_Sleep=" + mParam.sleepDataHead.AwakeTime_Sleep +
				"&OnBed=" + mParam.sleepDataHead.OnBed +
				"&ToSleep=" + mParam.sleepDataHead.ToSleep +
				"&AwakeCount=" + mParam.sleepDataHead.AwakeCount +
				"&GoToBedTime=" + mParam.sleepDataHead.GoToBedTime +
				"&GetUpTime=" + mParam.sleepDataHead.GetUpTime +
				"&AwakeNoGetUpCount=" + mParam.sleepDataHead.AwakeNoGetUpCount +
				"&OutSleepTime=" + mParam.sleepDataHead.OutSleepTime +
				"&InSleepTime=" + mParam.sleepDataHead.InSleepTime +
				"&TotalSleepTime=" + mParam.sleepDataHead.TotalSleepTime 
				;

		super.requestJosnObjectData(m_context, newString);

	}
	
	
	private void UploadHardwareSleepDataRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4453")) {
				m_InterfaceUploadHardwareSleepDataCallBack.onError(4453,"ID不存在或者格式错误");
			}else if (idValue.equals("4455")){
				m_InterfaceUploadHardwareSleepDataCallBack.onError(4455, "失败");
			}else if (idValue.equals("4456")) {
				m_InterfaceUploadHardwareSleepDataCallBack.onSuccess(4456, "成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//===========================================================================================
	//upload_hardware_data.php  上传硬件数据
	
	private String UploadHardwareDataURL = g_BaseSiteURL+g_BaseVersionThread+"upload_hardware_data.php?";
	
	private InterfaceUploadHardwareDataCallBack m_InterfaceUploadHardwareDataCallBack;
	
	public void UploadHardwareData(UploadHardwareDataParamClass mParam,InterfaceUploadHardwareDataCallBack callBack) {

		m_InterfaceUploadHardwareDataCallBack = callBack;
		String newString = UploadHardwareDataURL +
				"my_int_id=" + mParam.my_int_id +
				"&mac_address=" + mParam.mac_address +
				"&battery_data=" + mParam.battery_data +
				"&version=" + mParam.version
				;

		super.requestJosnObjectData(m_context, newString);

	}
	
	private void UploadHardwareDataRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4457")) {
				m_InterfaceUploadHardwareDataCallBack.onError(4457,"ID不存在或者格式错误");
			}else if (idValue.equals("4458")){
				m_InterfaceUploadHardwareDataCallBack.onError(4458, "失败");
			}else if (idValue.equals("4459")) {
				m_InterfaceUploadHardwareDataCallBack.onSuccess(4459, "成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//===========================================================================================
	//upload_hardware_allday.php  用户同步硬件总的睡眠数据
	
	private String UploadHardwareAllDayURL = g_BaseSiteURL+g_BaseVersionThread+"upload_hardware_allday.php?";
	
	private InterfaceUploadHardwareAllDayCallBack m_InterfaceUploadHardwareAllDayCallBack;
	
	public void UploadHardwareAllDay(UploadHardwareAllDayParamClass mParam,
			InterfaceUploadHardwareAllDayCallBack callBack) {
		m_InterfaceUploadHardwareAllDayCallBack = callBack;
		
		Map<String,String> map=new HashMap<String, String>();
		
		map.put("my_int_id", mParam.my_int_id);
		map.put("date_of_data", mParam.date_of_data);
		System.out.println("开始上传dat数据");
		
		try {
			UploadUtil.getInstance().uploadFile(mParam.path, "file", UploadHardwareAllDayURL, map,new OnUploadProcessListener() {
				
				@Override
				public void onUploadProcess(int uploadSize) {
					
				}
				
				@Override
				public void onUploadDone(int responseCode, String message) {
					try {
						JSONObject response =new JSONObject(message);
						UploadHardwareAllDayRstProc(response);
					} catch (Exception e) {
						e.printStackTrace();
						ProcJSONDataOnErr(null);
					}
				}
				
				@Override
				public void initUpload(int fileSize) {
					
				}
				
				@Override
				public void UploadError(int responseCode, String message) {
					ProcJSONDataOnErr(null);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ProcJSONDataOnErr(null);
		}
	}
	
	private void UploadHardwareAllDayRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4460")) {
				m_InterfaceUploadHardwareAllDayCallBack.onError(4460,"ID不存在或者格式错误");
			}else if (idValue.equals("4460:1")){
				m_InterfaceUploadHardwareAllDayCallBack.onError(44601, "文件超出上传限制或者文件为空败");
			}else if (idValue.equals("4461")) {
				m_InterfaceUploadHardwareAllDayCallBack.onError(4461, "失败");
			}else if (idValue.equals("4462")) {
				m_InterfaceUploadHardwareAllDayCallBack.onSuccess(4462, "成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//===========================================================================================
	//upload_hardware_day.php  同步每天的睡眠数据
	
	private String UploadHardwareDayURL = g_BaseSiteURL+g_BaseVersionThread+"upload_hardware_day.php?";
	
	private InterfaceUploadHardwareDayCallBack m_InterfaceUploadHardwareDayCallBack;
	
	public void UploadHardwareDay(UploadHardwareDayParamClass mParam,
			InterfaceUploadHardwareDayCallBack callBack) {
		m_InterfaceUploadHardwareDayCallBack = callBack;
		
		Map<String,String> map=new HashMap<String, String>();
		
		map.put("my_int_id", mParam.my_int_id);
		
		map.put("date_of_data", mParam.date_of_data);
		
		map.put("sleep_time", mParam.sleep_time);
		
		
		UploadUtil.getInstance().uploadFile(mParam.path, "file", UploadHardwareDayURL, map,new OnUploadProcessListener() {
			
			@Override
			public void onUploadProcess(int uploadSize) {
				
			}
			
			@Override
			public void onUploadDone(int responseCode, String message) {
				try {
					System.out.println(responseCode+"---"+message);
					JSONObject response =new JSONObject(message);
					UploadHardwareDayRstProc(response);
				} catch (Exception e) {
					e.printStackTrace();
					ProcJSONDataOnErr(null);
				}
			}
			
			@Override
			public void initUpload(int fileSize) {
				
			}
			
			@Override
			public void UploadError(int responseCode, String message) {
				ProcJSONDataOnErr(null);
			}
		});
	}
	
	private void UploadHardwareDayRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4463")) {
				m_InterfaceUploadHardwareDayCallBack.onError(4463,"ID不存在或者格式错误");
			}else if (idValue.equals("4463:0")){
				m_InterfaceUploadHardwareDayCallBack.onError(44630, "文件超出上传限制或者文件为空败");
			}else if (idValue.equals("4464")) {
				m_InterfaceUploadHardwareDayCallBack.onError(4464, "失败");
			}else if (idValue.equals("4465")) {
				m_InterfaceUploadHardwareDayCallBack.onSuccess(4465, "成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//===========================================================================================
	//download_hardware_day.php 下载睡眠数据,返回这个用户最近一年的数据包
	
	
	private String DownloadHardwareDayURL = g_BaseSiteURL+g_BaseVersionThread+"download_hardware_day.php?";
	
	private InterfaceDownloadHardwareDayDataCallBack m_InterfaceDownloadHardwareDayDataCallBack;
	
	public void DownloadHardwareDay(DownloadHardwareDayDataParamClass mParam,
			InterfaceDownloadHardwareDayDataCallBack callBack) {
		m_InterfaceDownloadHardwareDayDataCallBack = callBack;
		String newString = DownloadHardwareDayURL + "my_int_id=" + mParam.my_int_id ;

		super.requestJosnObjectData(m_context, newString);
		
	}
	
	private void DownloadHardwareDayRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4469")) {
				m_InterfaceDownloadHardwareDayDataCallBack.onError(4469,"ID不存在或者格式错误");
			}else if (idValue.equals("4470")){
				m_InterfaceDownloadHardwareDayDataCallBack.onError(4470, "文件超出上传限制或者文件为空败");
			}else if (idValue.equals("4471")) {
				String string = (String) response.get("datas").toString();
				
				if(string!=null)
				{
					Gson gson=new Gson();
	
					List<DownloadHardwareDayDataClass> list = gson.fromJson(string, new TypeToken<List<DownloadHardwareDayDataClass>>(){}.getType());
					
					m_InterfaceDownloadHardwareDayDataCallBack.onSuccess(4471, list);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	//===================================================================================
	//===================================================================================

	@Override
	public void ProcJSONData(JSONObject response) {
		String idValue;
		try {
				
			idValue = (String)  response.get("response").toString();

			if (idValue.equals("4361")|| idValue.equals("4362") || idValue.equals("4363"))
			{
				if (m_InterfaceFriendsSleepDataListCallBack != null) {
					DownloadFriendsRankListRstProc(response);
				};
			}else if(idValue.equals("4364")|| idValue.equals("4365") || idValue.equals("4366"))
			{
				if (m_InterfaceNearbySleepDataListCallBack != null) {
					DownloadNearbyRankListRstProc(response);
				}
			}else if(idValue.equals("4442")|| idValue.equals("4443"))
			{
				if (m_InterfaceDownloadSleepDataListCallBack != null) {
					DownloadSleepDataListRstProc(response);
				}
			}else if(idValue.equals("4033")|| idValue.equals("4034") || idValue.equals("4035") || 
					idValue.equals("4036")|| idValue.equals("4037:1") || idValue.equals("4037:2") ||
					idValue.equals("4037:3"))
			{
				if (m_InterfaceUploadSleepDataCallBack != null) {
					UploadSleepDataListRstProc(response);
				}
			}else if (idValue.equals("4447")|| idValue.equals("4448") || idValue.equals("4449")) 
			{
				if (m_InterfaceDownLoadRomCallBack != null) {
					GetDownloadNewRomUrlRstProc(response);
				}
			}else if (idValue.equals("4451") || idValue.equals("4452")) 
			{
				if (m_InterfaceGetRomVersionInfoCallBack != null) {
					GetRomVersionInfoRstProc(response);
				}
			}else if(idValue.equals("4453") || idValue.equals("4455") || idValue.equals("4456"))
			{
				if (m_InterfaceUploadHardwareSleepDataCallBack != null) {
					UploadHardwareSleepDataRstProc(response);
				}
			}else if(idValue.equals("4457") || idValue.equals("4458") || idValue.equals("4459"))
			{
				if (m_InterfaceUploadHardwareDataCallBack != null) {
					UploadHardwareDataRstProc(response);
				}
			}else if(idValue.equals("4460") || idValue.equals("4460:1") || idValue.equals("4461") || 
					idValue.equals("4462"))
			{
				if (m_InterfaceUploadHardwareAllDayCallBack != null) {
					UploadHardwareAllDayRstProc(response);
				}
			}else if(idValue.equals("4463") || idValue.equals("4463:0") || idValue.equals("4464") || 
					idValue.equals("4465"))
			{
				if (m_InterfaceUploadHardwareDayCallBack != null) {
					UploadHardwareDayRstProc(response);
				}
			}else if(idValue.equals("4469") || idValue.equals("4470") || idValue.equals("4471"))
			{
				if (m_InterfaceDownloadHardwareDayDataCallBack != null) {
					DownloadHardwareDayRstProc(response);
				}
			}else if(idValue.equals("4478") || idValue.equals("4479"))
			{
				if (m_InterfaceFriendsRankListCallBack != null) {
					System.out.println("--------->3");
					DownloadFriendsRankListAddRstProc(response);
				}
			}else if(idValue.equals("4476") || idValue.equals("4477"))
			{
				if (m_InterfaceNearbyUserListCallBack != null) {
					DownloadNearbyUserListRstProc(response);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void ProcJSONDataOnErr(VolleyError error) {
		int idValue = 3001;
		String errmsg = "访问服务器失败";

		if (m_InterfaceFriendsSleepDataListCallBack != null) {
			m_InterfaceFriendsSleepDataListCallBack.onError(idValue, errmsg);
		};
		
		if(m_InterfaceNearbySleepDataListCallBack != null){
			m_InterfaceNearbySleepDataListCallBack.onError(idValue, errmsg);
		}
		
		if (m_InterfaceDownloadSleepDataListCallBack != null) {
			m_InterfaceDownloadSleepDataListCallBack .onError(idValue, errmsg);
		}
		
		if (m_InterfaceUploadSleepDataCallBack != null) {
			m_InterfaceUploadSleepDataCallBack .onError(idValue, errmsg);
		}
		
		if (m_InterfaceDownLoadRomCallBack != null) {
			m_InterfaceDownLoadRomCallBack .onError(idValue, errmsg);
		}
		
		if (m_InterfaceGetRomVersionInfoCallBack != null) {
			m_InterfaceGetRomVersionInfoCallBack .onError(idValue, errmsg);
		}
		
		if (m_InterfaceUploadHardwareSleepDataCallBack != null) {
			m_InterfaceUploadHardwareSleepDataCallBack .onError(idValue, errmsg);
		}
		
		if (m_InterfaceUploadHardwareDataCallBack != null) {
			m_InterfaceUploadHardwareDataCallBack .onError(idValue, errmsg);
		}
		
		if (m_InterfaceUploadHardwareAllDayCallBack != null) {
			m_InterfaceUploadHardwareAllDayCallBack .onError(idValue, errmsg);
		}
		
		if (m_InterfaceUploadHardwareDayCallBack != null) {
			m_InterfaceUploadHardwareDayCallBack .onError(idValue, errmsg);
		}
		
		if (m_InterfaceDownloadHardwareDayDataCallBack != null) {
			m_InterfaceDownloadHardwareDayDataCallBack.onError(idValue, errmsg);
		}
		
		if (m_InterfaceFriendsRankListCallBack != null) {
			m_InterfaceFriendsRankListCallBack.onError(idValue, errmsg);
		}
		
		if (m_InterfaceNearbyUserListCallBack != null) {
			m_InterfaceNearbyUserListCallBack.onError(idValue, errmsg);
		}
	}

}
