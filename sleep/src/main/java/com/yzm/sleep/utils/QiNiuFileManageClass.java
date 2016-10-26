package com.yzm.sleep.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.utils.InterFaceUtilsClass.DeletePictureWallCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.DeletePictureWallClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.ExclusiveAudioReceiverListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.ExclusiveAudioSendListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetDownloadQiniuTokenCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetExclusiveAudioReceiverCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetExclusiveAudioReceiverParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetExclusiveAudioSendCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetExclusiveAudioSendParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetUserPictureWallCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSendAudioCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadQiNiuTokenCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.OperationExclusiveAudioCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.OperationExclusiveAudioParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SaveUploadPictureWallCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.SaveUploadPictureWallClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SendExclusiveAudioParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UpdatePictureWallCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.UpdatePictureWallClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadQiuTokenParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserPictureWallClassParam;

public class QiNiuFileManageClass extends HttpDataTranUtils{
	private static final String LOG_TAG = "QiNiuFileManageClass";
	
	private Context m_context = null;

	public QiNiuFileManageClass(Context context) {
		m_context = context;
	}
	
	//===========================================================================================
	//upload_qiniu_token.php  获取上传token
	private String uploadQiNiuTokenURL = g_BaseSiteURL+g_BaseSecond+ g_BaseThread+"upload_qiniu_token.php";
	private InterfaceUploadQiNiuTokenCallBack m_InterfaceUploadQiNiuTokenCallBack;
	
	public void GetUploadQiniuToken (UploadQiuTokenParamClass mParam,
			InterfaceUploadQiNiuTokenCallBack callBack){
		m_InterfaceUploadQiNiuTokenCallBack = callBack;
		
		String newString = uploadQiNiuTokenURL + "?" + "my_int_id="
				+ mParam.my_int_id;
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void uploadQiNiuTokenRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4300")) {
				m_InterfaceUploadQiNiuTokenCallBack.onError(4300, "失败：用户ID不存在或格式错误");
			} else if (idValue.equals("4301")){
				m_InterfaceUploadQiNiuTokenCallBack.onError(4301, "失败：其他错误");
			}else if (idValue.equals("4302")){
				//成功返回token
				String token = (String) response.get("token").toString();
				m_InterfaceUploadQiNiuTokenCallBack.onSuccess(4302, token);
			}
			
		} catch (Exception e) {
			m_InterfaceUploadQiNiuTokenCallBack.onError(4301, "失败：其他错误");
			e.printStackTrace();
		}
	}
	
	//===========================================================================================
	//zsly_send.php   发送专属铃音
	
	private String sendExclusiveAudioURL = g_BaseSiteURL+g_BaseSecond+ g_BaseThread+ "zsly_send.php";
	private InterfaceSendAudioCallBack m_InterfaceSendAudioCallBack;
	
	public void SendExclusiveAudio(SendExclusiveAudioParamClass mParam,
			InterfaceSendAudioCallBack callBack){
		m_InterfaceSendAudioCallBack = callBack;
		String titleString = "";
		try {
			titleString = URLEncoder.encode(mParam.ly_name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String newString = sendExclusiveAudioURL + "?" + "my_int_id=" + mParam.my_int_id
				+ "&ly_to_id=" + mParam.ly_to_id
				+ "&ly_type=" + mParam.ly_type
				+ "&ly_name=" + titleString
				+ "&ly_key=" + mParam.ly_key
				+ "&ly_pic_key=" + mParam.ly_pic_key
				+ "&ly_date=" + mParam.ly_date;
		
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void sendExclusiveAudioRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4304")) {
				m_InterfaceSendAudioCallBack.onError(4304, "失败：发送铃音的用户ID不存在或格式错误");
			} else if (idValue.equals("4305")){
				m_InterfaceSendAudioCallBack.onError(4305, "失败：接收铃音的用户ID不存在或格式错误");
			}else if (idValue.equals("4306")){
				m_InterfaceSendAudioCallBack.onError(4306, "失败：铃音内容不完整或格式错误");
			}else if (idValue.equals("4307")){
				m_InterfaceSendAudioCallBack.onError(4307, "失败：铃音设置时间格式错误");
			}else if (idValue.equals("4308")){
				m_InterfaceSendAudioCallBack.onError(4308, "失败：其他错误");
			}else if (idValue.equals("4309")){
				m_InterfaceSendAudioCallBack.onSuccess(4309, "发送成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//===========================================================================================
	//zsly_send_list.php   专属铃音发送的铃音列表
	
	private String getSendExclusiveAudioListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"zsly_send_list.php";
	private GetExclusiveAudioSendCallBack m_GetExclusiveAudioSendCallBack;
	
	public void SendExclusiveAudioList(GetExclusiveAudioSendParamClass mParam,
			GetExclusiveAudioSendCallBack callBack){
		m_GetExclusiveAudioSendCallBack = callBack;
		
		String newString = getSendExclusiveAudioListURL + "?" + "my_int_id=" + mParam.my_int_id
				+ "&page=" + mParam.page
				+ "&pagesize=" + mParam.pagesize;
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void getSendExclusiveAudioListRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4314")) {
				m_GetExclusiveAudioSendCallBack.onError(4314, "失败：用户ID不存在或格式错误");
			}else if (idValue.equals("4315")){
				m_GetExclusiveAudioSendCallBack.onError(4315, "失败：其他错误");
			}else if (idValue.equals("4316")){
				//获取发送铃音列表成功
				
				Gson gson=new Gson();
				String	listV = (String) response.get("datas").toString();
				int num = response.getInt("list_nums");
				List<ExclusiveAudioSendListClass> m_RstClass = gson.fromJson(listV, new TypeToken<List<ExclusiveAudioSendListClass>>(){}.getType());
				
				m_GetExclusiveAudioSendCallBack.onSuccess(4316, m_RstClass,num);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//===========================================================================================
	//zsly_receive_list.php   专属铃音收到的铃音列表
	
	private String getReceiverExclusiveAudioListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"zsly_receive_list.php";
	private GetExclusiveAudioReceiverCallBack m_GetExclusiveAudioReceiverCallBack;
	
	public void ReceiverExclusiveAudioList(GetExclusiveAudioReceiverParamClass mParam,
			GetExclusiveAudioReceiverCallBack callBack){
		m_GetExclusiveAudioReceiverCallBack = callBack;
		
		String newString = getReceiverExclusiveAudioListURL + "?" + "my_int_id=" + mParam.my_int_id
				+ "&page=" + mParam.page
				+ "&pagesize=" + mParam.pagesize;
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void getReceiverExclusiveAudioListRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4317")) {
				m_GetExclusiveAudioReceiverCallBack.onError(4317, "失败：用户ID不存在或格式错误");
			}else if (idValue.equals("4318")){
				m_GetExclusiveAudioReceiverCallBack.onError(4318, "失败：其他错误");
			}else if (idValue.equals("4319")){
				//获取收到的铃音列表成功
				
				Gson gson=new Gson();
				String	listV = (String) response.get("datas").toString();
				int num = response.getInt("list_nums");
				List<ExclusiveAudioReceiverListClass> m_RstClass = gson.fromJson(listV, new TypeToken<List<ExclusiveAudioReceiverListClass>>(){}.getType());
				
				m_GetExclusiveAudioReceiverCallBack.onSuccess(4319,  m_RstClass,num);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//===========================================================================================
	//zsly_open.php    专属铃音中收到的铃音启用或者拒绝操作
	
	private String OperationExclusiveAudioURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"zsly_open.php";
	private OperationExclusiveAudioCallBack m_OperationExclusiveAudioCallBack;
	
	public void OperationExclusiveAudio(OperationExclusiveAudioParamClass mParam,
			OperationExclusiveAudioCallBack callBack){
		m_OperationExclusiveAudioCallBack = callBack;
		
		String newString = OperationExclusiveAudioURL + "?" + "lyid=" + mParam.lyid
				+ "&status=" + mParam.status;
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void OperationExclusiveAudioRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4320")) {
				m_OperationExclusiveAudioCallBack.onError(4320, "失败：用户ID不存在或格式错误");
			}else if (idValue.equals("4321")){
				m_OperationExclusiveAudioCallBack.onError(4321, "失败：其他错误");
			}else if (idValue.equals("4322")){
				//获取收到的铃音列表成功
				m_OperationExclusiveAudioCallBack.onSuccess(4322,  "成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//===========================================================================================
	//download_qiniu_token.php    获取资源下载url（七牛私有资源）
	
	private String getQiniuResourceURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"download_qiniu_token.php";
	private GetDownloadQiniuTokenCallBack m_GetDownloadQiniuTokenCallBack;
	
	public void DownloadExclusiveAudio(String download_file_key,
			GetDownloadQiniuTokenCallBack callBack){
		m_GetDownloadQiniuTokenCallBack = callBack;
		
		String newString = getQiniuResourceURL + "?" + "download_file_key=" + download_file_key;
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void GetDownloadQiniuResourceUrlRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4323")) {
				m_GetDownloadQiniuTokenCallBack.onError(4323, "失败：key不存在或格式错误（格式错误：长度大于50或者为空)");
			}else if (idValue.equals("4324")){
				m_GetDownloadQiniuTokenCallBack.onError(4324, "失败：其他错误");
			}else if (idValue.equals("4325")){
				//获取收到的铃音列表成功
				String downloadurl = (String) response.get("downloadurl").toString();
				m_GetDownloadQiniuTokenCallBack.onSuccess(4325,  downloadurl);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//===========================================================================================
	//user_photo_add.php    保存用户上传的照片墙key
	
	private String saveUploadPictureWallURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"user_photo_add.php";
	private SaveUploadPictureWallCallBack m_SaveUploadPictureWallCallBack;
	
	public void saveUploadPictureWall(SaveUploadPictureWallClass mParam,
			SaveUploadPictureWallCallBack callBack){
		m_SaveUploadPictureWallCallBack = callBack;
		
		String newString = saveUploadPictureWallURL + "?" + "my_int_id=" + mParam.my_int_id
				+"&photo_key=" + mParam.photo_key;
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void SaveUploadPictureWallRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4377")) {
				m_SaveUploadPictureWallCallBack.onError(4377, "my_int_id不存在");
			}else if (idValue.equals("4378")){
				m_SaveUploadPictureWallCallBack.onError(4378, "photo_key为空");
			}else if (idValue.equals("4379")){
				m_SaveUploadPictureWallCallBack.onError(4379, "其它错误");
			}else if (idValue.equals("4380")){
				m_SaveUploadPictureWallCallBack.onSuccess(4380, "上传成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//===========================================================================================
	//photo_update_by_fid.php  更新用户的照片墙
	
	private String updatePictureWallURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"photo_update_by_fid.php";
	private UpdatePictureWallCallBack m_UpdatePictureWallCallBack;
	
	public void updatePictureWall(UpdatePictureWallClass mParam,
			UpdatePictureWallCallBack callBack){
		m_UpdatePictureWallCallBack = callBack;
		
		String newString = updatePictureWallURL + "?" + "my_int_id=" + mParam.my_int_id
				+"&attachment_key=" + mParam.attachment_key +"&pid=" + mParam.pid;
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void UpdatePictureWallRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4388")) {
				m_UpdatePictureWallCallBack.onError(4388, "my_int_id不存在或者格式错误");
			}else if (idValue.equals("4389")){
				m_UpdatePictureWallCallBack.onError(4389, "attachment_key为空");
			}else if (idValue.equals("4390")){
				m_UpdatePictureWallCallBack.onError(4390, "pid不存在或者格式错误");
			}else if (idValue.equals("4391")){
				m_UpdatePictureWallCallBack.onError(4391, "失败");
			}else if (idValue.equals("4392")){
				m_UpdatePictureWallCallBack.onSuccess(4392, "替换成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//===========================================================================================
	//photo_delete_by_fid.php  删除用户的照片墙
	
	private String deletePictureWallURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"photo_delete_by_fid.php";
	private DeletePictureWallCallBack m_DeletePictureWallCallBack;
	
	public void deletePictureWall(DeletePictureWallClass mParam,
			DeletePictureWallCallBack callBack){
		m_DeletePictureWallCallBack = callBack;
		
		String newString = deletePictureWallURL + "?" + "my_int_id=" + mParam.my_int_id
				+"&pid=" + mParam.pid;
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void DeletePictureWallRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4385")) {
				m_DeletePictureWallCallBack.onError(4385, "my_int_id不存在或者格式错误");
			}else if (idValue.equals("4386")){
				m_DeletePictureWallCallBack.onError(4386, "pid不存在或者格式错误");
			}else if (idValue.equals("4387:0")){
				m_DeletePictureWallCallBack.onError(43870, "删除失败");
			}else if (idValue.equals("4387")){
				m_DeletePictureWallCallBack.onSuccess(4387, "删除成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//===========================================================================================
	//user_photo_list.php  返回用户照片墙（最多8张）
	
	private String getPictureWallListURL = g_BaseSiteURL+g_BaseSecond+g_BaseThread+"user_photo_list.php";
	private GetUserPictureWallCallBack m_GetUserPictureWallCallBack;
	
	public void getUserPictureWall(String my_int_id,
			GetUserPictureWallCallBack callBack){
		m_GetUserPictureWallCallBack = callBack;
		
		String newString = getPictureWallListURL + "?" + "my_int_id=" + my_int_id;
		super.requestJosnObjectData(m_context, newString);
	}
	
	private void GetUserPictureWallRstProc(JSONObject response){
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4381")) {
				m_GetUserPictureWallCallBack.onError(4381, "my_int_id不存在");
			}else if (idValue.equals("4382")){
				//成功，返回照片墙图片信息列表
				Gson gson=new Gson();
				String	listV = (String) response.get("datas").toString();
				List<UserPictureWallClassParam> m_RstClass = gson.fromJson(listV, new TypeToken<List<UserPictureWallClassParam>>(){}.getType());
				m_GetUserPictureWallCallBack.onSuccess(4382, m_RstClass);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//===========================================================================================
	
	//===========================================================================================
	@Override
	public void ProcJSONData(JSONObject response) {
		String idValue;
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("4300") || idValue.equals("430１") || idValue.equals("4302")) 
			{
				if (m_InterfaceUploadQiNiuTokenCallBack != null) {
					uploadQiNiuTokenRstProc(response);
				}
			}else if(idValue.equals("4304") || idValue.equals("4305") || idValue.equals("4306") ||
					idValue.equals("4307") || idValue.equals("4308") || idValue.equals("4309"))
			{
				if (m_InterfaceSendAudioCallBack != null) {
					sendExclusiveAudioRstProc(response);
				}
			}else if(idValue.equals("4314") || idValue.equals("4315") || idValue.equals("4316"))
			{
				if (m_GetExclusiveAudioSendCallBack != null) {
					getSendExclusiveAudioListRstProc(response);
				}
			}else if(idValue.equals("4317") || idValue.equals("4318") || idValue.equals("4319"))
			{
				if (m_GetExclusiveAudioReceiverCallBack != null) {
					getReceiverExclusiveAudioListRstProc(response);
				}
			}else if(idValue.equals("4320") || idValue.equals("4321") || idValue.equals("4322"))
			{
				if (m_OperationExclusiveAudioCallBack != null) {
					OperationExclusiveAudioRstProc(response);
				}
			}else if(idValue.equals("4323") || idValue.equals("4324") || idValue.equals("4325"))
			{
				if (m_GetDownloadQiniuTokenCallBack != null) {
					GetDownloadQiniuResourceUrlRstProc(response);
				}
			}else if(idValue.equals("4377") ||idValue.equals("4378") || idValue.equals("4379") ||
					idValue.equals("4380"))
			{
				if (m_SaveUploadPictureWallCallBack != null) {
					SaveUploadPictureWallRstProc(response);
				}
			}else if(idValue.equals("4388") ||idValue.equals("4389") || idValue.equals("4390") ||
					idValue.equals("4391") || idValue.equals("4392"))
			{
				if (m_UpdatePictureWallCallBack != null) {
					UpdatePictureWallRstProc(response);
				}
			}else if(idValue.equals("4385") ||idValue.equals("4386") || idValue.equals("4387:0") ||
					idValue.equals("4387"))
			{
				if (m_DeletePictureWallCallBack != null) {
					DeletePictureWallRstProc(response);
				}
			}else if(idValue.equals("4381") ||idValue.equals("4382"))
			{
				if (m_GetUserPictureWallCallBack != null) {
					GetUserPictureWallRstProc(response);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    //===================请求数据错误====================
	@Override
	public void ProcJSONDataOnErr(VolleyError error) {
		int icode = 3001;
		String errmsg = "访问服务器失败";
		if (m_InterfaceUploadQiNiuTokenCallBack != null) {
			m_InterfaceUploadQiNiuTokenCallBack.onError(icode, errmsg);
		}
		
		if (m_InterfaceSendAudioCallBack != null) {
			m_InterfaceUploadQiNiuTokenCallBack.onError(icode, errmsg);
		}
		
		if (m_GetExclusiveAudioSendCallBack != null) {
			m_GetExclusiveAudioSendCallBack.onError(icode, errmsg);
		}
		
		if (m_GetExclusiveAudioReceiverCallBack != null) {
			m_GetExclusiveAudioReceiverCallBack.onError(icode, errmsg);
		}
		
		if (m_OperationExclusiveAudioCallBack != null) {
			m_OperationExclusiveAudioCallBack.onError(icode, errmsg);
		}
		
		if (m_GetDownloadQiniuTokenCallBack != null) {
			m_GetDownloadQiniuTokenCallBack.onError(icode, errmsg);
		}
		
		if (m_SaveUploadPictureWallCallBack != null) {
			m_SaveUploadPictureWallCallBack.onError(icode, errmsg);
		}
		
		if (m_UpdatePictureWallCallBack != null) {
			m_UpdatePictureWallCallBack.onError(icode, errmsg);
		}
		
		if (m_DeletePictureWallCallBack != null) {
			m_DeletePictureWallCallBack.onError(icode, errmsg);
		}
		
		if (m_GetUserPictureWallCallBack != null) {
			m_GetUserPictureWallCallBack.onError(icode, errmsg);
		}
	}

}
