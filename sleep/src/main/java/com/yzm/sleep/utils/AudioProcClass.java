package com.yzm.sleep.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.utils.InterFaceUtilsClass.AudioInfoClassParam;
import com.yzm.sleep.utils.InterFaceUtilsClass.DeactivatePublicAudioParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadFriendAudioParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadMusicRstListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadNearbyAudioParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadNearbyAudioRstListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadPublicAudioListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadRankAudioListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DownloadUserAudioListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.FriendsAudioRstListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetAudioInfoByAudiodIdURLCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDeactivatePublicAudioCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDeletePublicAudioCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownloadFriendAudioListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownloadMusicCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownloadNearbyAudioListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownloadPublicAudioListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownloadRankListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSendIncreasePlayTimesInfoCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSendpraiseInfoCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadAudioCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadAudioCoverCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadAudioInfoCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUserAudioListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.PublicAudioListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.RankedAudioRstListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.ReceiverAudioDonotOperatorNumCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.SendIncreasePlayTimesInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SendpraiseInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadAudioCoverParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadAudioInfoClassParam;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserAudioListClass;
import com.yzm.sleep.utils.UploadUtil.OnUploadProcessListener;

public class AudioProcClass extends HttpDataTranUtils {
	private static final String LOG_TAG = "AudioProcClass";

	private Context m_context = null;

	public AudioProcClass(Context context) {
		m_context = context;
	}
	
	// -----------------------------old----------------------------------------------------------------------------------------------
		// upload_public_cover.php	用于上传公开铃声的配套的封面图片
	
		private String uploadAudioCoverURL = g_BaseSiteURL+g_BaseSecond+"upload_public_cover.php";
		
		private InterfaceUploadAudioCoverCallBack m_InterfaceUploadAudioCoverCallBack;
		
		public void UpdateAudioCover(UploadAudioCoverParamClass mParam,
				InterfaceUploadAudioCoverCallBack callBack) {
			m_InterfaceUploadAudioCoverCallBack = callBack;
			
			Map<String,String> map=new HashMap<String, String>();
			
			map.put("my_int_id", mParam.my_int_id);
			
			
			UploadUtil.getInstance().uploadFile(mParam.coverPathString, "file", uploadAudioCoverURL, map,new OnUploadProcessListener() {
				
				@Override
				public void onUploadProcess(int uploadSize) {
					
				}
				
				@Override
				public void onUploadDone(int responseCode, String message) {
					try {
						JSONObject response =new JSONObject(message);
						UploadAudioCoverRstProc(response);
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
		
		
		
		private void UploadAudioCoverRstProc(JSONObject response) {
			String idValue = "";
			try {
				idValue = (String)  response.get("response").toString();
				
				if (idValue.equals("4124")) {
					m_InterfaceUploadAudioCoverCallBack.onError(4124, "失败：用户ID不存在或格式错误");
				}else if(idValue.equals("4125:0")){
					m_InterfaceUploadAudioCoverCallBack.onError(41250, "失败：文件超过1000kB的限制");
				}else if(idValue.equals("4125:1")){
					m_InterfaceUploadAudioCoverCallBack.onError(41251, "失败：上传的文件超过了 php.ini 中 upload_max_filesize 选项限制的值");
				}else if(idValue.equals("4125:2")){
					m_InterfaceUploadAudioCoverCallBack.onError(41252, "失败：上传文件的大小超过了 HTML 表单中 MAX_FILE_SIZE 选项指定的值");
				}else if(idValue.equals("4125:3")){
					m_InterfaceUploadAudioCoverCallBack.onError(41253, "失败：文件只有部分被上传");
				}else if(idValue.equals("4125:4")){
					m_InterfaceUploadAudioCoverCallBack.onError(41254, "失败：没有文件被上传");
				}else if(idValue.equals("4126")){
					String strPathString=  (String)  response.get("public_cover").toString();
					m_InterfaceUploadAudioCoverCallBack.onSuccess(4126, strPathString);
				}else if(idValue.equals("4127")){
					m_InterfaceUploadAudioCoverCallBack.onError(4127, "失败：其他错误");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// upload_public_cover.php	用于上传公开铃声的配套的封面图片  结束
	// ---------------------------------------------------------------------------------------------------------------------------
	

	// --------------------------old-------------------------------------------------------------------------------------------------
	// upload_public_audio.php	用于上传用户录制的铃音文件


	private String SaveAudioURL = g_BaseSiteURL+g_BaseSecond+"upload_public_audio.php";
//	private String SaveAudioURL ="http://10.0.0.121/xiangchengdebug/"+"upload_public_audio.php";
	
	
	private InterfaceUploadAudioCallBack m_InterfaceUploadAudioCallBack;

	
	/** 
	* @Title: UploadAudio 
	* @Description: TODO(用于上传用户录制的铃音文件) 
	* @param @param uploadParamClass 上传参数
	* @param @param callBack    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void UploadAudio(UploadParamClass uploadParamClass,
			InterfaceUploadAudioCallBack callBack) {		
		
		m_InterfaceUploadAudioCallBack = callBack;		
		
		String newString= "";

		{		
			 newString = SaveAudioURL;
				
//						 +"audio_name="+uploadParamClass.audio_name+
//					"&" + "user_location_x="+uploadParamClass.user_location_x+
//					"&" + "user_location_y="+uploadParamClass.user_location_y	;			
		}
	
	//	super.requestJosnObjectData(m_context, newString);
		
		Map<String,String> map=new HashMap<String, String>();
		
		map.put("user_location_x", uploadParamClass.user_location_x);
		map.put("user_location_y", uploadParamClass.user_location_y);
		map.put("audio_file_title", uploadParamClass.audio_file_title);

		
		UploadUtil.getInstance().uploadFile(uploadParamClass.audio_file_path, "file", SaveAudioURL, map,new OnUploadProcessListener() {
			
			@Override
			public void onUploadProcess(int uploadSize) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUploadDone(int responseCode, String message) {
				// TODO Auto-generated method stub
				
				try {
				
					JSONObject response =new JSONObject(message);
					UploadAudioRstProc(response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ProcJSONDataOnErr(null);
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void initUpload(int fileSize) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void UploadError(int responseCode, String message) {
				// TODO Auto-generated method stub
				ProcJSONDataOnErr(null);
			}
		});

	};
	
	private void UploadAudioRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4100:0")) {

				m_InterfaceUploadAudioCallBack.onError(41000,"失败：文件超过2MB的限制");

			} else if (idValue.equals("4100:1")) {

				m_InterfaceUploadAudioCallBack.onError(41001,"失败：上传的文件超过了 php.ini 中 upload_max_filesize 选项限制的值");

			} else if (idValue.equals("4100:2")) {

				m_InterfaceUploadAudioCallBack.onError(41002,"失败：上传文件的大小超过了 HTML 表单中 MAX_FILE_SIZE 选项指定的值");

			} else if (idValue.equals("4100:3")) {

				m_InterfaceUploadAudioCallBack.onError(41003,"失败：文件只有部分被上传");

			} else if (idValue.equals("4100:4")) {
				
				m_InterfaceUploadAudioCallBack.onError(41004,"失败：没有文件被上传");
				
			} else if (idValue.equals("4101")) {

				m_InterfaceUploadAudioCallBack.onError(4101,"失败：文件格式不支持");

			}else if (idValue.equals("4102"))
			{
				m_InterfaceUploadAudioCallBack.onError(4102,"失败：本人ID不存在或为空");	
		
			}else if (idValue.equals("4102:1"))
			{
				m_InterfaceUploadAudioCallBack.onError(4102,"失败：文件标题格式错误 ");	
		
			}else if (idValue.equals("4103"))
			{
				m_InterfaceUploadAudioCallBack.onError(4103,"失败：其他错误");	
			
			}else if (idValue.equals("4104"))
			{
				m_InterfaceUploadAudioCallBack.onSuccess(4104, "文件上传成功");
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 	upload_public_audio.php	用于上传用户录制的铃音文件信息

	
	//========================================================================================
	//public_audio_add.php  用户添加铃音
	
	private String UploadAudioInfoURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"public_audio_add.php?";
	private InterfaceUploadAudioInfoCallBack m_InterfaceUploadAudioInfoCallBack;

	public void UploadAudioInfo(UploadAudioInfoClassParam mParam,
			InterfaceUploadAudioInfoCallBack callBack) {		
		
		m_InterfaceUploadAudioInfoCallBack = callBack;
		String titleString = "";
		try {
			titleString = URLEncoder.encode(mParam.file_title, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String newString= "";
		
		newString = UploadAudioInfoURL + "my_int_id=" + mParam.my_int_id + "&user_location_x=" + mParam.user_location_x
				+"&user_location_y=" + mParam.user_location_y + "&file_title="+ titleString + 
				"&public_cover_key=" + mParam.public_cover_key +"&file_url_key=" + mParam.file_url_key;

	
		super.requestJosnObjectData(m_context, newString);

	};
	
	private void UploadAudioInfoRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4370")) {

				m_InterfaceUploadAudioInfoCallBack.onError(4370,"my_int_id不存在");

			} else if (idValue.equals("4371")) {

				m_InterfaceUploadAudioInfoCallBack.onError(4371,"x坐标为空");

			} else if (idValue.equals("4372")) {

				m_InterfaceUploadAudioInfoCallBack.onError(4372,"y坐标为空");

			} else if (idValue.equals("4373")) {

				m_InterfaceUploadAudioInfoCallBack.onError(4373,"铃音标题为空");

			} else if (idValue.equals("4374")) {
				
				m_InterfaceUploadAudioInfoCallBack.onError(4374,"铃音封面图key为空");
				
			} else if (idValue.equals("4375")) {

				m_InterfaceUploadAudioInfoCallBack.onError(4375,"铃音key为空");

			}else if (idValue.equals("4376:0"))
			{
				m_InterfaceUploadAudioInfoCallBack.onError(43760,"上传失败");	
		
			}else if (idValue.equals("4376"))
			{
				m_InterfaceUploadAudioInfoCallBack.onSuccess(4376, "上传成功");
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//================================================================================================================
	//user_publicly_list.php   用户已发布的铃音列表
	
	private String UserAudioListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"user_publicly_list.php?";
	private InterfaceUserAudioListCallBack m_InterfaceUserAudioListCallBack;
	
	public void GetUserAudioList(DownloadUserAudioListParamClass mParam,
			InterfaceUserAudioListCallBack callBack) {	
		
		m_InterfaceUserAudioListCallBack = callBack;		
		
		String newString= "";
		
		newString = UserAudioListURL +"my_int_id="+mParam.my_int_id + "&page=" + mParam.page +
				"&pagesize=" + mParam.pagesize;

	
		super.requestJosnObjectData(m_context, newString);

	};
	private void GetUserAudioListRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4352")) {

				m_InterfaceUserAudioListCallBack.onError(4352,"my_int_id不存在");

			} else if (idValue.equals("4352:1")) {

				m_InterfaceUserAudioListCallBack.onError(43521,"x坐标为空");

			} else if (idValue.equals("4352:2")) {

				m_InterfaceUserAudioListCallBack.onError(43522,"y坐标为空");

			} else if (idValue.equals("4353")) {

				m_InterfaceUserAudioListCallBack.onError(4353,"铃音标题为空");

			}else if (idValue.equals("4354"))
			{
				Gson gson=new Gson();
				String	listV = (String) response.get("datas").toString();
				List<UserAudioListClass> list = gson.fromJson(listV, new TypeToken<List<UserAudioListClass>>(){}.getType());
				int num = response.getInt("list_nums");
				m_InterfaceUserAudioListCallBack.onSuccess(4354,list,num);
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// -----------------------------------------------------old----------------------------------------------------------------------

	
	// download_public_audio.php	用于返回某用户的公开铃声下载链接地址


	private String DownloadMusicURL = g_BaseSiteURL+g_BaseSecond+"download_public_audio.php?";
	private InterfaceDownloadMusicCallBack m_InterfaceDownloadMusicCallBack;

	/** 
	* @Title: DownloadAudio 
	* @Description: TODO(用于返回某用户的公开铃声下载链接地址) 
	* @param @param DownloadParamClass 上传参数
	* @param @param callBack    回调函数 
	* @return void    返回类型 
	* @throws 
	*/
	public void DownloadAudio(DownloadParamClass downloadParamClass,
			InterfaceDownloadMusicCallBack callBack) {		
		
		m_InterfaceDownloadMusicCallBack = callBack;
		
		String newString= "";
		
		newString = DownloadMusicURL +"target_int_id="+downloadParamClass.target_int_id;			

	
		super.requestJosnObjectData(m_context, newString);

	};
	
	private void DownloadAudioRstProc(JSONObject response) {

		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4105")) {

				m_InterfaceDownloadMusicCallBack.onError(4105,"失败：id不存在");

			} else if (idValue.equals("4106")) {

				m_InterfaceDownloadMusicCallBack.onError(4106,"失败：对方用户没有公开的铃音，或铃音已经失效");

			}else if (idValue.equals("4107"))
			{
			
				Gson gson=new Gson();
				String	listV = (String) response.get("file_info").toString();
				DownloadMusicRstListClass ps = gson.fromJson(listV, DownloadMusicRstListClass.class);
				m_InterfaceDownloadMusicCallBack.onSuccess(4107,ps);	
		
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	// download_public_audio.php	用于返回某用户的公开铃声下载链接地址 结束

	// ---------------------------------------------------------------------------------------------------------------------------

	// download_ranked_audio.php	用于返回按下载量排行的铃声的列表(old)
	// public_audio_ranked_list.php      推荐铃音列表增加分页


	private String DownloadRankListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"public_audio_ranked_list.php";
	private InterfaceDownloadRankListCallBack m_InterfaceDownloadRankListCallBack;

	/** 
	* @Title: DownloadRankList 
	* @Description: TODO(用于返回按下载量排行的铃声的列表) 
	* @param @param callBack    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void DownloadRankList(DownloadRankAudioListParamClass mParam,InterfaceDownloadRankListCallBack callBack) {

		m_InterfaceDownloadRankListCallBack = callBack;
		String newString = DownloadRankListURL + "?" +"page=" + mParam.page + "&pagesize=" + mParam.pagesize;

		super.requestJosnObjectData(m_context, newString);

	};


	private void DownloadRankListRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String)  response.get("response").toString();
//			if (idValue == "null") {
//				idValue = "4108";
//			}

		if (idValue.equals("4360")) {
		
			Gson gson=new Gson();
			String	listV = (String) response.get("ranked_audio_list").toString();//ranked
			int num = response.getInt("list_nums");
			List<RankedAudioRstListClass> ps = gson.fromJson(listV, new TypeToken<List<RankedAudioRstListClass>>(){}.getType());
			m_InterfaceDownloadRankListCallBack.onSuccess(4360, ps,num);

		} 
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 	// download_ranked_audio.php	用于返回按下载量排行的铃声的列表 结束

	// ---------------------------------------------------------------------------------------------------------------------------
		
	// download_friends_audio.php	用于返回好友有铃声的列表  开始(old)
	//  public_audio_friends_list.php      好友铃音列表增加分页(new)
	
	private String DownloadFriendAudioURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"public_audio_friends_list.php?";
	private InterfaceDownloadFriendAudioListCallBack m_InterfaceDownloadFriendAudioListCallBack;

	/** 
	* @Title: DownloadFriendAudio 
	* @Description: TODO(用于返回好友有铃声的列表) 
	* @param @param mParam 参数
	* @param @param callBack   结果回调
	* @return void    返回类型 
	* @throws 
	*/
	public void DownloadFriendAudio(DownloadFriendAudioParamClass mParam,
			InterfaceDownloadFriendAudioListCallBack callBack) {
		
				 
		m_InterfaceDownloadFriendAudioListCallBack = callBack;
		
		String newString = DownloadFriendAudioURL +"my_int_id="+ mParam.my_int_id +"&page=" + mParam.page
				+"&pagesize=" +mParam.pagesize;

		super.requestJosnObjectData(m_context, newString);

	};


	private void DownloadFriendAudioListRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();
			

		if (idValue.equals("4358")) {
	
			m_InterfaceDownloadFriendAudioListCallBack.onError(4358, "错误：用户ID不存在。");

		} else if (idValue.equals("4359")) {
	
			Gson gson=new Gson();
			String	listV = (String) response.get("friend_audio_list").toString();
			int num = response.getInt("list_nums");
			List<FriendsAudioRstListClass> ps = gson.fromJson(listV, new TypeToken<List<FriendsAudioRstListClass>>(){}.getType());
			m_InterfaceDownloadFriendAudioListCallBack.onSuccess(4359, ps,num);

		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//  download_friends_audio.php	用于返回好友有铃声的列表  开始 结束
	
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	
	//download_nearby_audio.php	用于返回附近有铃声的列表  开始(old)
	//public_audio_nearby_list.php  附近铃音列表增加分页 (new)

	private String DownloadNearbyAudioURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"public_audio_nearby_list.php?";
	private InterfaceDownloadNearbyAudioListCallBack m_InterfaceDownloadNearbyAudioListCallBack;

	
	/** 
	* @Title: DownloadNearbyAudio 
	* @Description: TODO(用于返回附近有铃声的列表) 
	* @param @param mParam 经纬度
	* @param @param callBack    结果回调 
	* @return void    返回类型 
	* @throws 
	*/
	public void DownloadNearbyAudio(DownloadNearbyAudioParamClass mParam,
			InterfaceDownloadNearbyAudioListCallBack callBack) {

		m_InterfaceDownloadNearbyAudioListCallBack = callBack;
		
	
		String newString = DownloadNearbyAudioURL + "user_location_x="+ mParam.user_location_x + "&" + 
				"user_location_y="+ mParam.user_location_y +"&page=" + mParam.page + "&pagesize=" + mParam.pagesize ;

		super.requestJosnObjectData(m_context, newString);

	};


	private void DownloadNearbyAudioRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String)  response.get("response").toString();
	

		if (idValue.equals("4355")) {
	
			m_InterfaceDownloadNearbyAudioListCallBack.onError(4355, "参数错误或者为空");

		}else if (idValue.equals("4356")) {
	
			m_InterfaceDownloadNearbyAudioListCallBack.onError(4356, "你附近没有人！");

		} else if (idValue.equals("4357")) {
	
			Gson gson=new Gson();
			String	listV = (String) response.get("nearby_audio_list").toString();
			int num = response.getInt("list_nums");
			List<DownloadNearbyAudioRstListClass> ps = gson.fromJson(listV, new TypeToken<List<DownloadNearbyAudioRstListClass>>(){}.getType());
			m_InterfaceDownloadNearbyAudioListCallBack.onSuccess(4357, ps,num);
			
		} 
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	//add_phone_contact.php	用于将用户的通讯录用户直接添加成好友 结束
	
	
	//-------------------------------------old---------------------------------------------------------------------
	//deactivate_public_audio.php	用于删除用户已经上传了的公共的铃声  开始

		private String DeactivatePublicAudioURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"deactivate_public_audio.php?";
		private InterfaceDeactivatePublicAudioCallBack m_InterfaceDeactivatePublicAudioCallBack;

		
	
		public void DeactivatePublicAudio(DeactivatePublicAudioParamClass mParam,
				InterfaceDeactivatePublicAudioCallBack callBack) {

			m_InterfaceDeactivatePublicAudioCallBack = callBack;
			
		
			String newString = DeactivatePublicAudioURL + "my_int_id="+ mParam.my_int_id;

			super.requestJosnObjectData(m_context, newString);

		};


		private void DeactivatePublicAudioRstProc(JSONObject response) {

			String idValue = "";
			try {

				idValue = (String)  response.get("response").toString();
		

			if (idValue.equals("4114")) {
		
				m_InterfaceDeactivatePublicAudioCallBack.onError(4111, "失败：用户内部ID不存在，或格式不对");

			}else if (idValue.equals("4115")) {
		
				m_InterfaceDeactivatePublicAudioCallBack.onError(4112, "失败：用户没有公共铃音");

			} else if (idValue.equals("4116")) {
		
				m_InterfaceDeactivatePublicAudioCallBack.onSuccess(4110, "成功删除");
				
			} 
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}

		
		
		
	//================================================================================================
	//public_audio_delete.php 用户删除铃音
		private String DeletePublicAudioURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"public_audio_delete.php?";
		private InterfaceDeletePublicAudioCallBack m_InterfaceDeletePublicAudioCallBack;

		
	
		public void DeletePublicAudio(String lyid,
				InterfaceDeletePublicAudioCallBack callBack) {

			m_InterfaceDeletePublicAudioCallBack = callBack;
			
		
			String newString = DeletePublicAudioURL + "public_lyid="+ lyid;

			super.requestJosnObjectData(m_context, newString);

		};


		private void DeletePublicAudioRstProc(JSONObject response) {

			String idValue = "";
			try {

				idValue = (String)  response.get("response").toString();
		

			if (idValue.equals("4402")) {
		
				m_InterfaceDeletePublicAudioCallBack.onError(4402, "lyid不存在或者格式错误");

			}else if (idValue.equals("4403")) {
		
				m_InterfaceDeletePublicAudioCallBack.onError(4403, "删除失败");

			} else if (idValue.equals("4404")) {
		
				m_InterfaceDeletePublicAudioCallBack.onSuccess(4404, "成功");
				
			} 
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		
		
	//================================================================================================
	//public_audio_info_by_id.php  根据铃音id获取铃音所有信息
		
	private String GetAudioInfoByAudioIdURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"public_audio_info_by_id.php?";
	private GetAudioInfoByAudiodIdURLCallBack m_GetAudioInfoByAudiodIdURLCallBack;

	

	public void GetAudioInfoByAudiodId(String adudioId,
			GetAudioInfoByAudiodIdURLCallBack callBack) {

		m_GetAudioInfoByAudiodIdURLCallBack = callBack;
		
	
		String newString = GetAudioInfoByAudioIdURL + "lyid="+ adudioId;

		super.requestJosnObjectData(m_context, newString);

	};
	
	private void GetAudioInfoByAudiodIdRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String)  response.get("response").toString();
	

		if (idValue.equals("4400")) {
	
			m_GetAudioInfoByAudiodIdURLCallBack.onError(4400, "铃音不存在");//铃音id不存在

		}else if (idValue.equals("4401")) {
			//成功获取铃音相关信息
			//
			
			Gson gson = new Gson();
			String	listV = (String) response.get("datas").toString();
			AudioInfoClassParam m_RstClass = gson.fromJson(listV, new TypeToken<AudioInfoClassParam>(){}.getType());
			m_GetAudioInfoByAudiodIdURLCallBack.onSuccess(4401, m_RstClass);
			
		} 
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	
	
	//================================================================================================
	//zsly_receive_num.php  用户未操作的专属铃音数量
		
	private String ReceiverAudioDonotOperatorNumURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"zsly_receive_num.php?";
	private ReceiverAudioDonotOperatorNumCallBack m_ReceiverAudioDonotOperatorNumCallBack;
	
	public void ReceiverAudioDonotOperator(String my_int_id,
			ReceiverAudioDonotOperatorNumCallBack callBack) {

		m_ReceiverAudioDonotOperatorNumCallBack = callBack;
		
	
		String newString = ReceiverAudioDonotOperatorNumURL + "my_int_id="+ my_int_id;

		super.requestJosnObjectData(m_context, newString);

	};

	private void ReceiverAudioDonotOperatorRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String)  response.get("response").toString();
	

		if (idValue.equals("4412")) {
	
			m_ReceiverAudioDonotOperatorNumCallBack.onError(4412, "失败：用户ID不存在或格式错误");

		}if (idValue.equals("4413")) {
	
			m_ReceiverAudioDonotOperatorNumCallBack.onError(4413, "失败：其他错误");

		}else if (idValue.equals("4414")) {
			//成功获取铃音相关信息
			
			int num = response.getInt("num");
			
			m_ReceiverAudioDonotOperatorNumCallBack.onSuccess(4414, num);
		} 
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	//================================================================================================
	//public_audio_list.php  返回闹钟最新或者最热铃音列表（最新、热门）,并可以进行筛选
	
	private String DownloadPublicAudioListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"public_audio_list.php?";
	private InterfaceDownloadPublicAudioListCallBack m_InterfaceDownloadPublicAudioListCallBack;
	
	public void DownloadPublicAudioList(DownloadPublicAudioListParamClass mParam,InterfaceDownloadPublicAudioListCallBack callBack) {
		
		m_InterfaceDownloadPublicAudioListCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(DownloadPublicAudioListURL);
		sb.append("page=" + mParam.page);
		sb.append("&pagesize=" + mParam.pagesize);
		if(!TextUtils.isEmpty(mParam.my_int_id)){
			sb.append("&my_int_id=" + mParam.my_int_id);
		}
		if (!TextUtils.isEmpty(mParam.search)) {
			sb.append("&search=" + mParam.search);
		}
		
		if (mParam.age1 !=null) {
			sb.append("&age1="+mParam.age1);
		}
		if(mParam.sex != null){
			sb.append("&sex=" + mParam.sex);
		}
		
		String newString = sb.toString();

		super.requestJosnObjectData(m_context, newString);

	};


	private void DownloadPublicAudioListRstProc(JSONObject response) {

		String idValue = "";
		try {

			idValue = (String)  response.get("response").toString();
			if (idValue.equals("4474")) {
				
				m_InterfaceDownloadPublicAudioListCallBack.onError(4474, "获取数据失败");

			}else if (idValue.equals("4475")) {
		
			Gson gson=new Gson();
			String	listV = (String) response.get("audio_list").toString();//ranked
			int num = response.getInt("list_nums");
			List<PublicAudioListParamClass> ps = gson.fromJson(listV, new TypeToken<List<PublicAudioListParamClass>>(){}.getType());
			m_InterfaceDownloadPublicAudioListCallBack.onSuccess(4475, ps,num);

		} 
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//================================================================================================
	//favor_send.php 提交点赞的信息
	private String SendPraiseInfoURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"favor_send.php?";
	private InterfaceSendpraiseInfoCallBack mInterfaceSendpraiseInfoCallBack;
	public void SendpraiseInfo(SendpraiseInfoParamClass mParam, InterfaceSendpraiseInfoCallBack callback){
		mInterfaceSendpraiseInfoCallBack = callback;
		StringBuffer sb = new StringBuffer();
		sb.append(SendPraiseInfoURL);
		sb.append("public_lyid=" + mParam.public_lyid);
		sb.append("&my_int_id=" + mParam.my_int_id);
		String newString = sb.toString();
		super.requestJosnObjectData(m_context, newString);
	}
	private void SendpraiseInfoRstProc(JSONObject response){
		String idValue = "";
		try {

			idValue = (String)  response.get("response").toString();
			if(idValue.equals("4332")){
				mInterfaceSendpraiseInfoCallBack.onSuccess(4332, "成功");
			}else if(idValue.equals("4329")){
				mInterfaceSendpraiseInfoCallBack.onError(4329, "铃音ID不存在或格式错误");
			}
			else if(idValue.equals("4330")){
				mInterfaceSendpraiseInfoCallBack.onError(4330, "用户ID不存在或格式错误");
			}
			else if(idValue.equals("4331")){
				mInterfaceSendpraiseInfoCallBack.onError(4331, "你已经赞过了该铃声");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//================================================================================================
	//favor_send.php 提交播放次数的信息
	private String SendIncreasePlayTimesInfoURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"public_audio_play.php?";
	private InterfaceSendIncreasePlayTimesInfoCallBack mInterfaceSendIncreasePlayTimesInfoCallBack;
	public void SendIncreasePlayTimesInfo(SendIncreasePlayTimesInfoParamClass mParam, InterfaceSendIncreasePlayTimesInfoCallBack callback){
		mInterfaceSendIncreasePlayTimesInfoCallBack = callback;
		StringBuffer sb = new StringBuffer();
		sb.append(SendIncreasePlayTimesInfoURL);
		sb.append("public_lyid=" + mParam.public_lyid);
		String newString = sb.toString();
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void SendIncreasePlayTimesInfoRstProc(JSONObject response){
		String idValue = "";
		try {

			idValue = (String)  response.get("response").toString();
			if(idValue.equals("4417")){
				mInterfaceSendIncreasePlayTimesInfoCallBack.onSuccess(4332, "成功");
			}else if(idValue.equals("4416")){
				mInterfaceSendIncreasePlayTimesInfoCallBack.onError(4329, "铃音ID不存在或格式错误");
			}
			else if(idValue.equals("4415")){
				mInterfaceSendIncreasePlayTimesInfoCallBack.onError(4415, "失败");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//================================================================================================
	//================================================================================================

	public void ProcJSONData(JSONObject response) {
		
		String idValue;
		try {
				
			idValue = (String)  response.get("response").toString();
			
			if (idValue=="null") {
				idValue = "4108";
			}
			

			if (		idValue.equals("4101")|| idValue.equals("4102") 
					 || idValue.equals("4103")|| idValue.equals("4104")
					 || idValue.equals("4100:0")|| idValue.equals("4100:1")
					 || idValue.equals("4100:2")|| idValue.equals("4100:3")
					 || idValue.equals("4100:4")|| idValue.equals("4102:1") )
			{
				if (m_InterfaceUploadAudioCallBack != null) {
					UploadAudioRstProc(response);
				};
			}else if(idValue.equals("4105") ||idValue.equals("4106") ||idValue.equals("4107"))
			{			
				if (m_InterfaceDownloadMusicCallBack != null)		
					DownloadAudioRstProc(response);
				
			}else if(idValue.equals("4360"))
			{			
				if (m_InterfaceDownloadRankListCallBack != null)		
					DownloadRankListRstProc(response);
				
			}else if(idValue.equals("4358") ||idValue.equals("4359"))
			{
				if (m_InterfaceDownloadFriendAudioListCallBack != null)		
					DownloadFriendAudioListRstProc(response);	
				
			}else if(idValue.equals("4355") ||idValue.equals("4356")||idValue.equals("4357") )
			{
				if (m_InterfaceDownloadNearbyAudioListCallBack != null)		
					DownloadNearbyAudioRstProc(response);						
			}else if(idValue.equals("4114") ||idValue.equals("4115")||idValue.equals("4116") )
			{
				if (m_InterfaceDeactivatePublicAudioCallBack != null)		
					DeactivatePublicAudioRstProc(response);						
				
			}else if(idValue.equals("4124") ||idValue.equals("4125:0")||idValue.equals("4125:1") ||
					idValue.equals("4125:2") ||idValue.equals("4125:3")||idValue.equals("4125:4") ||
					idValue.equals("4126") ||idValue.equals("4127"))
			{
				if (m_InterfaceUploadAudioCoverCallBack != null)		
					UploadAudioCoverRstProc(response);						
				
			}else if(idValue.equals("4400") || idValue.equals("4401"))
			{
				if (m_GetAudioInfoByAudiodIdURLCallBack != null) {
					GetAudioInfoByAudiodIdRstProc(response);
				}
			}else if(idValue.equals("4370") ||idValue.equals("4371")||idValue.equals("4372") ||
					idValue.equals("4373") ||idValue.equals("4374")||idValue.equals("4375") ||
					idValue.equals("4376:0") ||idValue.equals("4376")){
				if (m_InterfaceUploadAudioInfoCallBack != null) {
					UploadAudioInfoRstProc(response);
				}
			}else if(idValue.equals("4352") || idValue.equals("4352:1") || idValue.equals("4352:2") ||
					idValue.equals("4353") || idValue.equals("4354"))
			{
				if (m_InterfaceUserAudioListCallBack != null) {
					GetUserAudioListRstProc(response);
				}
			}else if(idValue.equals("4402") || idValue.equals("4403") || idValue.equals("4404"))
			{
				if (m_InterfaceDeletePublicAudioCallBack != null) {
					DeletePublicAudioRstProc(response);
				}
			}else if (idValue.equals("4412") || idValue.equals("4413") || idValue.equals("4414")) 
			{
				if (m_ReceiverAudioDonotOperatorNumCallBack != null) {
					ReceiverAudioDonotOperatorRstProc(response);
				}
			}else if (idValue.equals("4474") || idValue.equals("4475")) 
			{
				if (m_InterfaceDownloadPublicAudioListCallBack != null) {
					DownloadPublicAudioListRstProc(response);
				}
			}else if(idValue.equals("4329") ||idValue.equals("4330") ||idValue.equals("4331") ||idValue.equals("4332")){
				if(mInterfaceSendpraiseInfoCallBack != null){
					SendpraiseInfoRstProc(response);
				}
			}else if(idValue.equals("4415") || idValue.equals("4416") || idValue.equals("4417")){
				if(mInterfaceSendIncreasePlayTimesInfoCallBack != null){
					SendIncreasePlayTimesInfoRstProc(response);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// /////////////////////////////////////////////////////////////////////////////////////////////////
		
		
	};

	// ///////////////////请求数据各种错误 和业务无关
	public void ProcJSONDataOnErr(VolleyError error) {
		String errmsg = "访问服务器失败";

		if (m_InterfaceUploadAudioCallBack != null) {
			m_InterfaceUploadAudioCallBack.onError(3001, errmsg);
		};
		
		if (m_InterfaceDownloadMusicCallBack != null) {
			m_InterfaceDownloadMusicCallBack.onError(3001, errmsg);
		};
		
		if (m_InterfaceDownloadRankListCallBack != null) {
			m_InterfaceDownloadRankListCallBack.onError(3001, errmsg);
		};
		
		if (m_InterfaceDownloadFriendAudioListCallBack != null) {
			m_InterfaceDownloadFriendAudioListCallBack.onError(3001, errmsg);
		};
		if (m_InterfaceDownloadNearbyAudioListCallBack != null){	
			m_InterfaceDownloadNearbyAudioListCallBack.onError(3001, errmsg);
		}
	
		if (m_InterfaceDeactivatePublicAudioCallBack != null){	
			m_InterfaceDeactivatePublicAudioCallBack.onError(3001, errmsg);
		}
		
		if(m_InterfaceUploadAudioCoverCallBack != null)
		{
			m_InterfaceUploadAudioCoverCallBack.onError(3001, errmsg);			
		}
		
		if (m_GetAudioInfoByAudiodIdURLCallBack != null) {
			m_GetAudioInfoByAudiodIdURLCallBack.onError(3001, errmsg);
		}
		
		if (m_InterfaceUploadAudioInfoCallBack != null) {
			m_InterfaceUploadAudioInfoCallBack.onError(3001, errmsg);
		}
		
		if (m_InterfaceUserAudioListCallBack != null) {
			m_InterfaceUserAudioListCallBack.onError(3001, errmsg);
		}
		
		if (m_InterfaceDeletePublicAudioCallBack != null) {
			m_InterfaceDeletePublicAudioCallBack.onError(3001, errmsg);
		}
		
		if (m_ReceiverAudioDonotOperatorNumCallBack != null) {
			m_ReceiverAudioDonotOperatorNumCallBack.onError(3001, errmsg);
		}
		
		if (m_InterfaceDownloadPublicAudioListCallBack != null) {
			m_InterfaceDownloadPublicAudioListCallBack.onError(3001, errmsg);
		}
//		Log.d(TAG, error.getMessage(), error);

	};
	
	

}
