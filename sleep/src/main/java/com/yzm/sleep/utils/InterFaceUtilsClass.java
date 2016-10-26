package com.yzm.sleep.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.yzm.sleep.bean.ArticleDetailBean;
import com.yzm.sleep.bean.AutoMsgBean;
import com.yzm.sleep.bean.CommentBean;
import com.yzm.sleep.bean.CommunityGrougsBean;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.bean.CommunityGroupDoctorBean;
import com.yzm.sleep.bean.CommunityTopicBean;
import com.yzm.sleep.bean.FirstRecommentGroupBean;
import com.yzm.sleep.bean.GroupMessageBean;
import com.yzm.sleep.bean.HardwareSleepDataBean;
import com.yzm.sleep.bean.HotTagBean;
import com.yzm.sleep.bean.MyWeiBoFriendBaean;
import com.yzm.sleep.bean.ReportBean;
import com.yzm.sleep.bean.SearchGroupBean;
import com.yzm.sleep.bean.SearchTopicBean;
import com.yzm.sleep.bean.SpecialistBean;
import com.yzm.sleep.bean.TagBean;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;

public class InterFaceUtilsClass {
	/*
	 * 2015-02-28 lzm 接口功能 ：用于验证手机号是否已被绑定/注册过 response：4056 invalid phone number
	 * or id 手机号位数不对或内部ID为空 response：4057 user id invalid 用户ID不+存在 response：4058
	 * phone number in use 该手机号已经与某账户绑定过了 response：4058:1 phone number in use
	 * 该手机号已经与其他账户绑定过了 response：4058:2 phone number in use 该手机号已经与该账户绑定过了
	 * response：4059 valid phone number 该手机号可以注册/绑定
	 */
	public interface InterfaceCheckPhoneNumCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode);
	}

	public interface InterfaceRequestRegCodeCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode);
	}

	// /////////////////////////////////
	/*
	 * 入口参数 参数含义 参数要求 my_phone_num 用户的电话号码 非空，且为11位纯数字，且经过验证 verification_code
	 * 验证码 非空，4位纯数字 my_int_nickname 用户昵称 非空 my_int_pwd 用户密码 非空 my_int_age 用户年龄
	 * 非空，出生年份 my_int_gender 用户性别 非空，值为01或02，含义见下表 my_int_occupation 用户职业
	 * 非空，值为2000000xx，含义见下表 my_int_profile 随机生成的用户头像地址 （serverIP）/profile/x
	 * （x为1-16数字，1-8为男士头像，9-16为女士头像）
	 */
	public static class PhoneRegParamClass {

		public PhoneRegParamClass() {

			// TODO Auto-generated constructor stub
		}

		public String my_phone_num;
		public String verification_code;
		public String my_int_nickname;
		public String my_int_pwd;
		public String my_int_age;
		public String my_int_gender;
		public String my_int_occupation;
		public String my_int_profile;

	}

	public interface InterfaceRegUserByPhoneCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String int_id);
	}

	// ///////////////////////////////////////////////////////////////////////////
	public class VerificationCodeParamClass {
		public String my_phone_num;
		public String verification_code;
	}

	public interface InterfaceVerificationCodeRstCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode);
	}

	// ///////////////////////////////////////////////////////////////////////////
	public class ResetPassWordParamClass {
		public String my_phone_num;
		public String my_int_pwd;
	}

	public interface InterfaceResetPassWordRstCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode);
	}

	// ////----------------------------------------------------------------------------------------------------------------------------------------

	public static class OtherUsersParamClass {
		public String my_phone_num;
		public String platform;// 第三方平台编号 非空，值为30000000x，含义见下表
		public String my_ext_acc;// 第三方账户账号 非空，为第三方平台的唯一用户ID
		public String my_ext_unionid = "";// 微信登录unionid
		public String my_ext_nickname;// 第三方用户昵称 非空
		public String my_ext_profile;// 第三方用户头像地址 非空
		public String my_int_age;// 内部用户年龄 非空，出生年份
		public String my_int_gender;// 内部用户性别 非空，值为01（男）或02（女）
		public String my_int_occupation;// 内部用户职业编号 非空，值为2000000xx
		public String friend_num = "0";// 第三方平台上用户好友的数量 非空
		public List<String> friendacc_x = new ArrayList<String>();// 第三方平台上用户好友的账户账号
																	// 非空，除非friend_num=0

		public OtherUsersParamClass() {
		}
	}

	public static class OtherUsersReturn4031RstClass {
		public String int_id;
		public String is_zj;
		public String nickname;
		public String age;
		public String gender;
		public String occupation;
		public String user_internal_height;
		public String user_internal_weight;
		public String user_internal_birthday;
		public String zjintro;
		public String user_ext_acc_cellphone_new;
		public String profile;
		public String profile_key;
		public String hiding;
		public String user_ext_acc_qq;
		public String user_ext_acc_weibo;
		public String user_ext_acc_wechat;
		public String user_ext_acc_cellphone;
		public String isaddinfo;
		public String ispinggu;
		public String dakadays;
		public String jgtssz;
		public String sleeppg;
		public String sleep;//计划入睡时间点
		public String wakeup;//计划起床时间点
		public OtherUsersReturn4031RstClass() {

			// TODO Auto-generated constructor stub
		}
	}

	public static class OtherUsersReturn4032RstClass {
		public String int_id;
		public String is_zj;
		public String nickname;
		public String age;
		public String gender;
		public String occupation;
		public String user_internal_height;
		public String user_internal_weight;
		public String user_internal_birthday;
		public String zjintro;
		public String user_ext_acc_cellphone_new;
		public String profile;
		public String profile_key;
		public String hiding;
		public String user_ext_acc_qq;
		public String user_ext_acc_weibo;
		public String user_ext_acc_wechat;
		public String user_ext_acc_cellphone;
		public String isaddinfo;
		public String ispinggu;
		public String dakadays;
		public String jgtssz;
		public String sleep_pg;
		public String sleep;//计划入睡时间点
		public String wakeup;//计划起床时间点
		public OtherUsersReturn4032RstClass() {

			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceSaveOtherUsersCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode);

		public void onReturn4031(OtherUsersReturn4031RstClass rst4031Class);

		public void onReturn4032(OtherUsersReturn4032RstClass rst4032Class);

	}

	// ..................----------------------------------------------
	public static class OwerSaveOtherAccParamClass {

		public String target_int_id;
		public String platform;
		public String my_ext_acc;
		public String friend_num;
		public List<String> friendacc_x;

		public OwerSaveOtherAccParamClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceOwerSaveOtherAccURLCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String iSum);

	}

	// ///////////////////////////////////
	public static class OwerSavePhoneNumberParamClass {

		public String my_phone_num;
		public String verification_code;
		public String target_int_id;

		public OwerSavePhoneNumberParamClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceOwerSavePhoneNumberCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode);

	}

	// ///////////////////////////////////---------------------------------------------------------
	public static class SearchNickNameParamClass {

		public String search_key_word;
		public String my_int_id;

		public SearchNickNameParamClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public static class search_list implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public String int_id;
		public String nickname;
		public String profile;
		public String age;
		public String gender;
		public String occupation;
		public String relation;
	}

	public static class OwerSearchedUserRstListClass implements Serializable {

		private static final long serialVersionUID = 1L;
		public String int_id;
		public String nickname;
		public String profile;
		public String age;
		public String gender;
		public String occupation;
		public String relation;

		public OwerSearchedUserRstListClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceSearchNickNameCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<OwerSearchedUserRstListClass> m_list);

	}

	// ///////////////////////////////////---------------------------------------------------------
	public static class AddFriendFromContectParamClass {

		public String my_int_id;
		public String friend_num;
		public List<String> friendacc_x;

		public AddFriendFromContectParamClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public static class FriendRstClass {

		public String num_friend_added;
		public String contact_added;

		public FriendRstClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceAddFriendFromContectCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, FriendRstClass m_FriendRstClass);

	}

	// /////////////////////////////////////
	public static class CheckCellPhoneLoginStatParam {

		public String my_phone_num;
		public String my_int_pwd;

		public CheckCellPhoneLoginStatParam() {
			// TODO Auto-generated constructor stub
		}
	}

	public class UserInfo implements Serializable {
		private static final long serialVersionUID = 1L;
		public String int_id = "";
		public String nickname = "";
		public String age = "";
		public String gender = "";
		public String occupation = "";
		public String profile = "";
		public String profile_key = "";
		public String hiding = "";
		public int user_ext_acc_qq;
		public int user_ext_acc_weibo;
		public int user_ext_acc_wechat;
		public int user_ext_acc_cellphone;
		public String user_internal_birthday;
		public String user_internal_height;
		public String user_internal_weight;
		public String is_zj;
	}

	public static class CheckCellPhoneLoginStatRst {

		public String int_id;
		public String nickname;
		public String age;
		public String gender;
		public String occupation;
		public String profile;
		public String profile_key;
		public String user_ext_acc_qq;
		public String user_ext_acc_weibo;
		public String user_ext_acc_wechat;
		public String user_ext_acc_cellphone;
		public String hide;
		public String is_zj;
		public String height;
		public String weight;
		public String ispinggu;
		public String dakadays;
		public String jgtssz;
		public String sleep_pg;
		public String sleep;//计划入睡时间点
		public String wakeup;//计划起床时间点

		public CheckCellPhoneLoginStatRst() {
			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceCheckCellPhoneLoginStatCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, CheckCellPhoneLoginStatRst m_RstClass);

	}

	// ////////////////////////////////////////////////////////////////upload_public_cover.php

	public static class UploadAudioCoverParamClass {
		public String my_int_id;
		public String coverPathString;

		public UploadAudioCoverParamClass() {

		}
	}

	public interface InterfaceUploadAudioCoverCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String m_RstClass);
	}

	// ////////////////////////////////////////////////////////////////upload_public_audio.php

	public static class UploadParamClass {
		public String audio_file_path;
		public String audio_file_title;
		public String user_location_x;//
		public String user_location_y;//

		public UploadParamClass() {
		}
	}

	public interface InterfaceUploadAudioCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String m_RstClass);

	};

	// download_public_audio.php 用于返回某用户的公开铃声下载链接地址

	public static class DownloadMusicRstListClass implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String file_url;
		public String file_title;
		public String file_upload_time;
		public String play_times;
		public String cover;

		public String getFile_url() {
			return file_url;
		}

		public void setFile_url(String file_url) {
			this.file_url = file_url;
		}

		public String getFile_title() {
			return file_title;
		}

		public void setFile_title(String file_title) {
			this.file_title = file_title;
		}

		public String getFile_upload_time() {
			return file_upload_time;
		}

		public void setFile_upload_time(String file_upload_time) {
			this.file_upload_time = file_upload_time;
		}

		public String getPlay_times() {
			return play_times;
		}

		public void setPlay_times(String play_times) {
			this.play_times = play_times;
		}

		public String getCover() {
			return cover;
		}

		public void setCover(String cover) {
			this.cover = cover;
		}

		public DownloadMusicRstListClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public static class DownloadParamClass {
		public String target_int_id;

		public DownloadParamClass() {
		}
	}

	public interface InterfaceDownloadMusicCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, DownloadMusicRstListClass m_list);

	}

	// ====================================================================================
	// user_publicly_list.php 用户已发布的铃音列表
	public static class DownloadUserAudioListParamClass {
		public String my_int_id;
		public int page;
		public int pagesize;

		public DownloadUserAudioListParamClass() {
		}
	}

	public static class UserAudioListClass {

		/** 铃音ID */
		public String public_lyid;
		/** 铃音标题 */
		public String file_title;
		/** 铃音url */
		public String file_url;
		/** 铃音key (以前上传的铃音key为空) */
		public String file_url_key;
		/** 铃音封面url */
		public String public_cover;
		/** 铃音封面key(以前上传的铃音封面key为空) */
		public String public_cover_key;
		/** 铃音最后上传时间。如：21:11 */
		public String last_upload_time;

		/** 用户头像url */
		public String user_pic_url;
		/** 用户头像key */
		public String user_pic_key;
		/** 用户昵称 */
		public String nickname;
		/** 点赞数量 */
		public int favor_num;
		/** 评论数量 */
		public int pl_num;

		public UserAudioListClass() {
		}
	}

	public interface InterfaceUserAudioListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, List<UserAudioListClass> m_list,
				int list_num);

	}

	// download_ranked_audio.php 用于返回按下载量排行的铃声的列表
	public static class DownloadRankAudioListParamClass {
		public int page;
		public int pagesize;

		public DownloadRankAudioListParamClass() {
		}
	}

	public static class RankedAudioRstListClass implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String int_id;
		public String nickname;
		public String profile;
		public String profile_key;
		public String play_times;
		public String age;
		public String gender;
		public String occupation;
		public String title;
		public String upload_time;
		public String url;
		public String url_key;
		public String cover;
		public String cover_key;

		public String ly_pic_url_suolue;
		public String ly_pic_key_suolue;
		public String profile_suolue;
		public String profile_key_suolue;

		public String public_lyid;

		public RankedAudioRstListClass() {
			// TODO Auto-generated constructor stub
		}
	}

	// ///////////////////////////////最热最新铃声列表/////////////////////////////////////////
	// public static class RankedAudioRstHosttestListClass implements
	// Serializable{
	//
	//
	// public RankedAudioRstHosttestListClass(){
	//
	// }
	// }

	public interface InterfaceDownloadRankListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, List<RankedAudioRstListClass> m_list,
				int num);

	}

	// ///////////////////////////////////////////download_friends_audio.php
	// 用于返回好友有铃声的列表

	public static class DownloadFriendAudioParamClass {

		public String my_int_id;
		public int page;
		public int pagesize;

		public DownloadFriendAudioParamClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public static class FriendsAudioRstListClass implements Serializable {

		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;
		public String int_id;
		public String nickname;
		public String profile;
		public String profile_key;
		public String play_times;
		public String age;
		public String gender;
		public String occupation;
		public String title;
		public String upload_time = "";
		public String url;
		public String url_key;
		public String cover;
		public String cover_key;

		// ly_pic_url_suolue （string） 铃音封面缩略图
		// ly_pic_key_suolue 铃音封面缩略图key
		// profile_suolue 用户头像缩略图
		// profile_key_suolue 用户头像缩略图key

		public String ly_pic_url_suolue;
		public String ly_pic_key_suolue;
		public String profile_suolue;
		public String profile_key_suolue;

		public String public_lyid;

		public FriendsAudioRstListClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceDownloadFriendAudioListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, List<FriendsAudioRstListClass> m_list,
				int num);

	}

	// ///////////////////////////////////////////download_nearby_audio.php
	// 用于返回附近有铃声的列表

	public static class DownloadNearbyAudioParamClass {

		public String user_location_x;
		public String user_location_y;
		public int page;
		public int pagesize;

		public DownloadNearbyAudioParamClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public static class DownloadNearbyAudioRstListClass implements Serializable {

		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;
		public String int_id;
		public String nickname;
		public String profile;
		public String profile_key;
		public String play_times;
		public String age;
		public String gender;
		public String occupation;
		public String title;
		public String upload_time;
		public String url;
		public String url_key;
		public String cover;
		public String cover_key;

		// ly_pic_url_suolue （string） 铃音封面缩略图
		// ly_pic_key_suolue 铃音封面缩略图key
		// profile_suolue 用户头像缩略图
		// profile_key_suolue 用户头像缩略图key

		public String ly_pic_url_suolue;
		public String ly_pic_key_suolue;
		public String profile_suolue;
		public String profile_key_suolue;

		public String public_lyid;

		public DownloadNearbyAudioRstListClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceDownloadNearbyAudioListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<DownloadNearbyAudioRstListClass> m_list, int num);

	}

	// //////old/////////////////////////////////////deactivate_public_audio.php
	// 用于删除用户已经上传了的公共的铃声

	public static class DeactivatePublicAudioParamClass {

		public String my_int_id;

		public DeactivatePublicAudioParamClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceDeactivatePublicAudioCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccessMsg);

	}

	// =====================================================================================================
	// public_audio_delete.php 用户删除铃音
	public interface InterfaceDeletePublicAudioCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccessMsg);

	}

	// ///////////////////////////////////////////deactivate_public_audio.php
	// 用于删除用户已经上传了的公共的铃声

	public static class UpdateUserProfileParamClass {

		public String my_int_id;
		public String strPathString;

		public UpdateUserProfileParamClass() {
			// TODO Auto-generated constructor stub
		}
	}

	public interface InterfaceUpdateUserProfileCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccessMsg);

	}

	// ===================================================================
	// user_profile_update.php

	public static class UserProfileUpdateParamClass {

		public String my_int_id;
		public String profile_key;

		public UserProfileUpdateParamClass() {
		}
	}

	public static class UserProfileInfo {
		// profile：头像地址
		// profile_key：头像地址key
		// profile_suolue 头像图片缩略图
		// profile_key_suolue 头像缩略图key

		public String profile;
		public String profile_key;
		public String profile_suolue;
		public String profile_key_suolue;
	}

	public interface InterfaceUserProfileUpdateCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccessMsg);

	}
	
	public interface InterfaceDowmloadQiniuFileCallBack {
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int iCode, String strSuccessMsg);

	}

	/**
	 * 修改用户昵称
	 * */

	public static class UpdateUserNicknameParamClass {
		public String my_int_id;
		public String my_int_nickname;

		public UpdateUserNicknameParamClass() {

		}
	}

	public interface InterfaceUpdateuserNicknameCallBack {
		public void onErrorListener(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccessMsg);
	}

	// ======================================================================================
	// 返回指定用户id的用户相关信息
	public static class UploadSleepTimeSettingParamClass {
		public String my_int_id;
		/** 睡觉时间 默认为 23:00 */
		public String sleep_date;
		/** 起床时间 默认为 08:30 */
		public String wakeup_date;

	}

	public interface InterfaceUploadSleepTimeSettingCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccMsg);
	}

	// ======================================================================================
	// 返回指定用户id的用户相关信息
	public interface InterfaceGetUserInfoByIdCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, UserBasicInfoClass m_RstClass);
	}

	public static class UserBasicInfoClass implements Serializable {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;
		/** 用户id */
		public String uid = "";
		/** 用户昵称 */
		public String nickname = "";
		/** 用户头像url */
		public String profile = "";
		/** 用户头像七牛key */
		public String profile_key = "";
		/** 用户年龄 */
		public String age = "";
		/** 用户性别 01-男，02-女 */
		public String gender = "";
		/** 用户职业 */
		public String occupation = "";

		public String user_internal_height = "";
		public String user_internal_weight = "";
		public String user_internal_birthday = "";
		public String zjintro = "";
		/** 1是专家 */
		public String is_zj = "";

		public UserBasicInfoClass() {
		}
	}

	// ======================================================================================
	// 获取上传token

	public static class UploadQiuTokenParamClass {
		public String my_int_id;

		public UploadQiuTokenParamClass() {
		}
	}

	public interface InterfaceUploadQiNiuTokenCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String m_RstClass);
	}

	// ======================================================================================
	// 发送专属铃音

	public static class SendExclusiveAudioParamClass {
		/** 发送铃音的用户内部ID */
		public String my_int_id;

		/** 接收铃音的用户内部ID */
		public String ly_to_id;
		/** 铃音类型---2为神秘；1为普通。（默认是1） */
		public String ly_type;
		/** 铃音标题 */
		public String ly_name;
		/** 铃音路径 ，即七牛中这个文件的key */
		public String ly_key;
		/** 封面路径 ，即七牛中这个文件的key */
		public String ly_pic_key;
		/** 铃音设置时间 */
		public String ly_date;

		public SendExclusiveAudioParamClass() {
		}
	}

	public interface InterfaceSendAudioCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String m_RstClass);
	}

	// =========================================================================================
	// 专属铃音发送的铃音列表

	public static class GetExclusiveAudioSendParamClass {
		/** 用户内部id */
		public String my_int_id;
		/** 列表第几页 （默认为1） */
		public int page;
		/** 每页显示多少条数据 （默认为7） */
		public int pagesize;

		public GetExclusiveAudioSendParamClass() {
		}
	}

	public static class ExclusiveAudioSendListClass implements Serializable {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;
		/** 铃音是否发送成功 */
		public boolean myIsSendSuccess = true;
		public int ly_send_failed_id;

		/** 专属铃音id */
		public String lyid;
		/** 铃音类型 */
		public String ly_type;
		/** 铃音设置时间(闹钟时间) */
		public String ly_date;
		/** 铃音标题 */
		public String ly_name;
		/** 铃音封面路径，即七牛中这个封面文件的key */
		public String ly_pic_key;
		/** 铃音封面图片url */
		public String ly_pic_url;
		/** 铃音状态 ( 1启用 -1拒绝 0未启用 ) */
		public String status;
		/** 铃音路径，即七牛中这个铃音文件的key */
		public String ly_key;
		/** 铃音url */
		public String ly_url;

		// to_int_id 发送对象的用户id
		// to_nickname 发送对象的用户昵称
		// to_profile 发送对象的用户头像url
		// to_profile_key 发送对象的用户头像七牛key
		// to_age 发送对象的用户年龄
		// to_gender 发送对象的用户性别（）
		// to_occupation 发送对象的用户职业
		public String to_int_id;
		public String to_nickname;
		public String to_profile;
		public String to_profile_key;
		public String to_age;
		public String to_gender;
		public String to_occupation;

		// to_profile_suolue 发送对象的用户头像缩略图（120*120）
		// to_profile_key_suolue 发送对象的用户头像缩略图key（与头像key比较最后多了“suolue”）
		// ly_pic_key_suolue 铃音封面缩略图key
		// ly_pic_url_suolue 铃音封面缩略图
		public String to_profile_suolue;
		public String to_profile_key_suolue;
		public String ly_pic_key_suolue;
		public String ly_pic_url_suolue;

		public ExclusiveAudioSendListClass() {
		}
	}

	public interface GetExclusiveAudioSendCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<ExclusiveAudioSendListClass> m_RstClass, int num);
	}

	// ==========================================================================================
	// 专属铃音收到的铃音列表
	public static class GetExclusiveAudioReceiverParamClass {
		/** 用户内部id */
		public String my_int_id;
		/** 列表第几页 （默认为1） */
		public int page;
		/** 每页显示多少条数据 （默认为7） */
		public int pagesize;

		public GetExclusiveAudioReceiverParamClass() {
		}
	}

	public static class ExclusiveAudioReceiverListClass implements Serializable {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		// lyid专属铃音ID；
		// ly_type铃音类型； (2为神秘；1为普通)
		// ly_date铃音设置时间(闹钟时间)；
		// ly_name铃音标题；
		// ly_pic_key铃音封面路径，即七牛中这个封面文件的key；
		// ly_pic_url 铃音封面图片url ；
		// status铃音状态； ( 1启用 -1拒绝 0未启用 )
		// ly_key铃音路径，即七牛中这个铃音文件的key ;
		// ly_url 铃音url

		// from_int_id 发送铃音用户id
		// nickname 发送铃音用户昵称
		// profile 发送铃音用户头像url
		// profile_key 发送铃音用户头像七牛key
		// age 发送铃音用户年龄
		// gender 发送铃音用户性别（）
		// occupation 发送铃音用户职业
		//
		// 查询的铃音总数：list_nums

		/** 专属铃音id */
		public String lyid;
		/** 铃音类型 1 - 普通 ， 2 - 神秘 */
		public int ly_type;
		/** 铃音设置时间(闹钟时间) */
		public String ly_date;
		/** 铃音标题 */
		public String ly_name;
		/** 铃音封面路径，即七牛中这个封面文件的key */
		public String ly_pic_key;
		/** 铃音封面图片url */
		public String ly_pic_url;
		/** 铃音状态 ( 1启用 -1拒绝 0未启用 ) */
		public String status;
		/** 铃音路径，即七牛中这个铃音文件的key */
		public String ly_key;
		/** 铃音url */
		public String ly_url;

		/** 发送铃音用户id */
		public String from_int_id;
		/** 发送铃音用户昵称 */
		public String nickname;
		/** 发送铃音用户头像url */
		public String profile;
		/** 发送铃音用户头像七牛key */
		public String profile_key;
		/** 发送铃音用户年龄 */
		public String age;
		/** 发送铃音用户性别 */
		public String gender;
		/** 发送铃音用户职业 */
		public String occupation;
		/***/

		// profile_suolue 发送用户头像缩略图（120*120）
		// profile_key_suolue 发送用户头像缩略图key（与头像key比较最后多了“suolue”）
		// ly_pic_key_suolue 铃音封面缩略图key
		// ly_pic_url_suolue 铃音封面缩略图
		public String profile_suolue;
		public String profile_key_suolue;
		public String ly_pic_key_suolue;
		public String ly_pic_url_suolue;

		public ExclusiveAudioReceiverListClass() {
		}
	}

	public interface GetExclusiveAudioReceiverCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<ExclusiveAudioReceiverListClass> m_RstClass, int num);
	}

	// =============================================================================================
	// 专属铃音中收到的铃音启用或者拒绝操作
	public static class OperationExclusiveAudioParamClass {
		/** 专属铃音id */
		public String lyid;
		/** 专属铃音状态 1为已启用；0为已拒绝 */
		public int status;

		public OperationExclusiveAudioParamClass() {
		}
	}

	public interface OperationExclusiveAudioCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String m_RstClass);
	}

	// =============================================================================================
	// 获取资源下载url（七牛私有资源）
	public interface GetDownloadQiniuTokenCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String m_RstClass);
	}

	// ================================================================================================
	// 保存用户上传的照片墙key
	public static class SaveUploadPictureWallClass {
		public String photo_key;
		public String my_int_id;

		public SaveUploadPictureWallClass() {
		}
	}

	public interface SaveUploadPictureWallCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String m_RstClass);
	}

	// ================================================================================================
	// 更新用户的照片墙
	public static class UpdatePictureWallClass {
		/** 用户内部ID */
		public String my_int_id;
		/** 上传图片的七牛key */
		public String attachment_key;
		/** 照片墙的照片ID */
		public String pid;

		public UpdatePictureWallClass() {
		}
	}

	public interface UpdatePictureWallCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccMsg);
	}

	// ================================================================================================
	// 删除用户的照片墙
	public static class DeletePictureWallClass {
		/** 用户内部ID */
		public String my_int_id;
		/** 照片墙的照片ID */
		public String pid;

		public DeletePictureWallClass() {
		}
	}

	public interface DeletePictureWallCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccMsg);
	}

	// ===============================================================================================
	// 返回用户照片墙（最多8张）
	public interface GetUserPictureWallCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<UserPictureWallClassParam> m_RstClass);
	}

	public static class UserPictureWallClassParam {
		/** 上传用户内部ID */
		public String uid;
		/** 照片ID */
		public String pid;
		/** 照片url地址 */
		public String attachment;
		/** 照片在七牛中的key */
		public String attachment_key;

		// attachment_suolue 照片缩略图
		// attachment_key_suolue 照片缩略图key

		public String attachment_suolue;
		public String attachment_key_suolue;

		public UserPictureWallClassParam() {
		}
	}

	// ===============================================================================================
	// 根据铃音id获取铃音所有信息
	public interface GetAudioInfoByAudiodIdURLCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, AudioInfoClassParam m_RstClass);
	}

	public static class AudioInfoClassParam {
		// int_id 用户内部ID；
		// nickname 昵称；
		// profile 头像地址；
		// profile_key 头像key（七牛中的key）
		// play_times 播放次数；
		// age 年龄；
		// gender 性别；
		// occupation 职业；
		// title 标题 ;
		// url 铃音下载地址；
		// url_key 铃音的key（七牛中的key）
		// upload_time 上传时间戳；
		// cover 铃音封面图片地址；
		// cover_key 铃音封面图片的key（七牛中的key）
		//
		/** 上传用户内部ID */
		public String int_id;
		/** 昵称 */
		public String nickname;
		/** 头像地址 */
		public String profile;
		/** 头像key（七牛中的key） */
		public String profile_key;
		/** 播放次数 */
		public String play_times;
		/** 年龄 */
		public String age;
		/** 性别 */
		public String gender;
		/** 职业 */
		public String occupation;
		/** 标题 */
		public String title;
		/** 铃音下载地址 */
		public String url;
		/** 铃音的key（七牛中的key） */
		public String url_key;
		/** 上传时间戳 */
		public String upload_time;
		/** 铃音封面图片地址 */
		public String cover;
		/** 铃音封面图片的key（七牛中的key） */
		public String cover_key;

		public AudioInfoClassParam() {
		}
	}

	// ========================================================================================
	// public_audio_add.php 用户添加铃音
	public static class UploadAudioInfoClassParam {
		/** 本人当前位置的x坐标 */
		public String user_location_x;
		/** 本人当前位置的y坐标 */
		public String user_location_y;
		/** 用户本人的内部ID */
		public String my_int_id;
		/** 铃音标题 */
		public String file_title;
		/** 铃音封面图,七牛返回的key */
		public String public_cover_key;
		/** 铃音，七牛返回的key */
		public String file_url_key;
	}

	public interface InterfaceUploadAudioInfoCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccMsg);
	}

	// ====================================old=======================================================
	// wakedata_friends_list.php 睡眠数据好友排名列表增加分页
	public static class GetFriendsSleepDataListParamClass {
		public String my_int_id;
		public String date_of_data;
		public int page;
		public int pagesize;

		public GetFriendsSleepDataListParamClass() {
		}

	}

	public static class FriendsSleepDataListParamClass {

		public String int_id;
		public String nickname;
		public String sleep;
		public String age;
		public String gender;
		public String occupation;
		public String profile;
		public String profile_key;
		public String profile_suolue;
		public String profile_key_suolue;
		public String is_zj;

		public String wakeuptime;// 起床时间；
		public String sleeptime;// 睡觉时间
		public String from;// 来自

		public FriendsSleepDataListParamClass() {
		}
	}

	public interface InterfaceFriendsSleepDataListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<FriendsSleepDataListParamClass> mParamClass, int num);
	}

	// ===========================================================================================
	// friends_add_tag.php 好友排行

	public static class GetFriendsRankListParamClass {
		public String my_int_id;
		public String laiyuan;
		/** （json数据） */
		public String data;
	}

	public static class FriendsRankListParamClass {
		public String int_id;
		public String nickname;
		public String sleep;
		public String age;
		public String gender;
		public String occupation;

		public String wakeuptime;
		public String sleeptime;
		public String from;

		public String profile;
		public String profile_key;
		public String profile_suolue;
		public String profile_key_suolue;

	}

	public interface InterfaceFriendsRankListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<FriendsRankListParamClass> mParamClass);
	}

	// ===========================================================================================
	// wakedata_nearby_list.php 睡眠数据附件排名列表增加分页

	public static class GetNearbySleepDataListParamClass {
		public String date_of_data;
		public String user_location_x;
		public String user_location_y;
		public int page;
		public int pagesize;

		public GetNearbySleepDataListParamClass() {
		}

	}

	public static class NearbySleepDataListParamClass {

		public String int_id;
		public String nickname;
		public String sleep;
		public String age;
		public String gender;
		public String occupation;
		public String profile;
		public String profile_key;
		// profile_suolue 头像图片缩略图
		// profile_key_suolue 头像缩略图key

		public String profile_suolue;
		public String profile_key_suolue;

		public NearbySleepDataListParamClass() {
		}
	}

	public interface InterfaceNearbySleepDataListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<NearbySleepDataListParamClass> mParamClass, int num);
	}

	// ===========================================================================================
	// user_nearby_list.php 返回附近的人筛选后的列表。

	public static class GetNearbyUserListParamClass {
		// public String date_of_data;
		// public String user_location_x;
		// public String user_location_y;
		public int page;
		public int pagesize;
		/** 好友搜索，用户输入框输入nickname 或者手机号进行好友搜索 。 */
		public String search;
		/** 登录用户的经度 */
		public String user_x;
		/** 登录用户的经度 */
		public String user_y;
		/** 筛选条件，性别。（1为男，2为女，默认为全部） */
		public String sex;

	}

	public static class NearbyUserListParamClass {

		public String int_id;
		public String nickname;
		public String sleep;
		public String age;
		public String gender;
		public String occupation;
		public String profile;
		public String profile_key;

		public String profile_suolue;
		public String profile_key_suolue;

		/** 距离 */
		public String juli;

	}

	public interface InterfaceNearbyUserListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<NearbyUserListParamClass> mParamClass, int num);
	}

	// ===========================================================================================
	// user_hiding_set.php 用户附近排行隐身

	public static class UserHideParamClass {
		public String my_int_id;
		public String hiding;

	}

	public interface InterfaceNearbyUserHideListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String result);
	}

	// ===========================================================================================
	// zsly_receive_num.php 用户未操作的专属铃音数量

	public interface ReceiverAudioDonotOperatorNumCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, int num);
	}

	// ===========================================================================================
	// download_sleepdata_by_id.php 根据登录用户ID获取睡眠数据

	public static class DownloadSleepDataParamClass {
		public String my_int_id;

		public DownloadSleepDataParamClass() {
		}
	}

	public static class UserSleepDataListClass {
		public String user_internal_id;
		public String date;// 格式yyyyMMdd
		public String sleep_point;
		public String wakeup_point;
		public String bueatysleep_duration;
		public String user_location_x;
		public String user_location_y;

		// /** 当天的日期 ， 如：2015-05-07*/
		// public String riqi;
		// /** 入睡时间， 如：201505011129*/
		// public String sleep_point;
		// /** 入睡时间点， 如 ： 08:35*/
		// public String sleep_point1;
		// /** 起床时间， 如：201505011129*/
		// public String wakeup_point;
		// /** 起床时间点 如 ： 08:35*/
		// public String wakeup_point1;
		// /** （float:2） 睡眠数据， 如：13.48*/
		// public float bueatysleep_duration;

		public UserSleepDataListClass() {
		}
	}

	public interface InterfaceDownloadSleepDataListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, List<UserSleepDataListClass> m_list);
	}

	// ===========================================================================================
	// upload_user_data.php 用于上传用户某日的睡眠数据
	public static class UploadSleepDataParamClass {
		/** 用户ID */
		public String my_int_id;
		/** 需要上传的数据日期 按照yyyymmdd的格式 */
		public String date_of_data;
		/** 　入睡时间　按照yyyymmddhhmm的格式 */
		public String sleep_point;
		/** 　起床时间　按照yyyymmddhhmm的格式 */
		public String wakeup_point;
		/** 位置x坐标 非空，除非用户未开启GPS，按照统一经纬度的格式 */
		public String user_location_x;
		/** 位置y坐标 非空，除非用户未开启GPS，按照统一经纬度的格式 */
		public String user_location_y;
		/** 睡眠时长 非空，以小时为单位显示小数，如7.67（小时） */
		public String sleep_duration;
		/** 内部职业 非空，值为2000000xx */
		public String my_int_occupation;

		public UploadSleepDataParamClass() {
		}
	}

	public interface InterfaceUploadSleepDataCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccMsg);
	}

	// ===========================================================================================
	// download_rom.php 下载最新的ROM包
	public static class GetDownLoadRomInfoParamClass {
		/** 客户端rom版本号 */
		public String version;

		public GetDownLoadRomInfoParamClass() {
		}
	}

	public static class DownLoadRomInfoClass {
		/** rom包下载地址 */
		public String rom_url;
		/** 最新版本号 */
		public String version_new;

		public DownLoadRomInfoClass() {
		}
	}

	public interface InterfaceDownLoadRomCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, DownLoadRomInfoClass mResClass);
	}

	// ===========================================================================================
	// download_version_by_id.php 获取ROM包版本以及服务器时间

	public static class RomVersionInfoClass {
		/** 客户端rom版本号 */
		public String version;
		/** 服务器时间 */
		public String sys_date;

		public RomVersionInfoClass() {
		}
	}

	public interface InterfaceGetRomVersionInfoCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, RomVersionInfoClass mResClass);
	}

	// ===========================================================================================
	// upload_hardware_sleep_data.php 上传硬件每日睡眠数据

	public static class UploadHardwareSleepDataParamClass {
		public String my_int_id;
		public String date_of_data;
		public SleepDataHead sleepDataHead;

		public UploadHardwareSleepDataParamClass() {
		}
	}

	public interface InterfaceUploadHardwareSleepDataCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccMsg);
	}

	// ===========================================================================================
	// upload_hardware_data.php 上传硬件数据

	public static class UploadHardwareDataParamClass {
		/** 用户ID */
		public String my_int_id;
		/** mac地址 */
		public String mac_address;
		/** 电池电量 */
		public String battery_data;
		/** 版本号 */
		public String version;

		public UploadHardwareDataParamClass() {
		}
	}

	public interface InterfaceUploadHardwareDataCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccMsg);
	}

	// ===========================================================================================
	// upload_hardware_allday.php 用户同步硬件总的睡眠数据

	public static class UploadHardwareAllDayParamClass {
		/** 用户ID */
		public String my_int_id;
		/** 需要上传的睡眠数据的时间,非空，按照yyyymmddhhiiSS的格式（如：20150621120136） */
		public String date_of_data;
		/** File */
		public String path;

		public UploadHardwareAllDayParamClass() {
		}
	}

	public interface InterfaceUploadHardwareAllDayCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccMsg);
	}

	// ===========================================================================================
	// upload_hardware_day.php 同步每天的睡眠数据

	public static class UploadHardwareDayParamClass {
		/** 用户ID */
		public String my_int_id;
		/** 需要上传的睡眠数据所属日期 */
		public String date_of_data;
		/** 睡眠时间参数 */
		public String sleep_time;

		/** File */
		public String path;

		public UploadHardwareDayParamClass() {
		}
	}

	public interface InterfaceUploadHardwareDayCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccMsg);
	}

	// ===========================================================================================
	// check_friends_by_phones.php 判断手机通讯录中的手机号跟当前用户是不是好友

	public static class CheckFriendsByCellPhoneParamClass {
		/** 用户ID */
		public String my_int_id;
		/** 用户通讯录 */
		public List<CellPhoneParamClass> phones;

		public CheckFriendsByCellPhoneParamClass() {
		}
	}

	public static class CellPhoneParamClass {
		/** 用户通讯录 */
		public String phone;
		/** 手机备注姓名 */
		public String phonename;

		public CellPhoneParamClass() {
		}
	}

	public static class FriendsByCellPhoneClass {
		/** 用户内部ID */
		public String uid;
		/** 手机备注姓名 */
		public String phonename;
		/** 手机号码 */
		public String phone;
		/** 昵称 */
		public String nickname;
		/** 年龄 */
		public String age;
		/** 性别 */
		public String gender;
		/** 职业 */
		public String occupation;
		/** 头像url */
		public String profile;
		/** 头像图片缩略图url */
		public String profile_suolue;
		/** 头像图片的key */
		public String profile_key;
		/** 头像缩略图key */
		public String profile_key_suolue;
		/**
		 * 好友关系（ 1表示是好友； 2表示已经提交过申请，对方还未通过申请； 3 表示对方向自己提交过好友申请，等待自己的确认；
		 * 4表示不是好友关系）
		 */
		public String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public FriendsByCellPhoneClass() {
		}
	}

	public interface InterfaceCheckFriendsByCellPhoneCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<FriendsByCellPhoneClass> mFriendList);
	}

	// ===========================================================================================
	// download_hardware_day.php 下载睡眠数据,返回这个用户最近一年的数据包

	public static class DownloadHardwareDayDataParamClass {
		/** 用户ID */
		public String my_int_id;

		public DownloadHardwareDayDataParamClass() {
		}
	}

	public static class DownloadHardwareDayDataClass {
		/** 日期 */
		public String date;
		/** 睡眠数据文件下载地址 */
		public String file;
		/** 睡眠时间参数 */
		public String sleep_time;

		public DownloadHardwareDayDataClass() {
		}
	}

	public interface InterfaceDownloadHardwareDayDataCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<DownloadHardwareDayDataClass> mDataList);
	}

	// ==========================================================================================
	// check_friend_status.php 检查用户之间的好友关系
	public static class CheckUserStatusParamClass {
		public String my_int_id;
		public String friend_int_id;

		public CheckUserStatusParamClass() {
		}
	}

	public interface InterfaceCheckUserStatusCallBack {
		public void onResult(int icode, String strRelMsg);

		public void onError(int icode, String strErrMsg);
	}

	// ==========================================================================================
	// add_delete_friend.php 用于添加好友，确认好友申请，删除好友

	public static class AddDeleteFriendParamClass {
		public String my_int_id;
		public String friend_int_id;
		public String operation_type;

		public AddDeleteFriendParamClass() {
		}
	}

	public interface InterfaceAddDeleteFriendCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode, String strSuccessMsg);
	}

	// ================================================================================================
	// public_audio_list.php 返回闹钟最新或者最热铃音列表（最新、热门）,并可以进行筛选

	public static class DownloadPublicAudioListParamClass {

		public String my_int_id;
		public String page;
		public String pagesize;
		/** （1为最新排序，2为热门，默认为2） */
		public String search;
		/** （01为男，02为女，默认为全部） */
		public String sex;
		/** age1 （json） */
		public String age1;

	}

	public static class AgeFiltrate {
		public String age1;
		public String age2;
	}

	public static class PublicAudioListParamClass {
		public String int_id;// 用户内部ID；
		public String nickname;// 昵称；
		public String profile;// 头像地址；
		public String play_times;// 播放次数；
		public String age;// 年龄；
		public String gender;// 性别；
		public String occupation;// 职业；
		public String title;// 标题 ;
		public String url;// 铃音下载地址；
		public String upload_time;// 上传时间戳；
		public String cover;// 铃音封面图片地址；
		public String cover_key;// 铃音封面图片的key（七牛中的key）
		public String url_key;// 铃音的key（七牛中的key）
		public String public_lyid; // (string)铃音ID
		public String ly_pic_url_suolue; // （string） 铃音封面缩略图
		public String ly_pic_key_suolue; // 铃音封面缩略图key
		public String profile_suolue; // 用户头像缩略图
		public String profile_key_suolue; // 用户头像缩略图key
		public String pinlun; // 铃音评论数量
		public int zan; // 点赞数
		public String zanstate; // 点赞状态。0表示已赞；1表示未赞

	}

	public interface InterfaceDownloadPublicAudioListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int iCode,
				List<PublicAudioListParamClass> mDataList, int num);
	}

	// ================================================================================================
	// favor_send.php 提交点赞的信息
	public interface InterfaceSendpraiseInfoCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strErrMsg);
	}

	public static class SendpraiseInfoParamClass {
		public String public_lyid;// 铃声id
		public String my_int_id; // 用户id

	}

	// ================================================================================================
	// public_audio_play.php 播放公共铃音，增加播放数量
	public interface InterfaceSendIncreasePlayTimesInfoCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strErrMsg);
	}

	public static class SendIncreasePlayTimesInfoParamClass {
		public String public_lyid;// 铃声id
	}

	// ==================================================================================================
	// QQ获取已安装了应用的好友列表
	public static class GetQQFriendsListParamClass {
		public String openid;// =B624064BA065E01CB73F835017FE96FA&
		public String openkey;// =5F154D7D2751AEDC8527269006F290F70297B7E54667536C&
		public String appid;// =2&
		public String sig;// =9999b41ad0b688530bb1b21c5957391c&
		public String pf;// =qzone&
		public String format;// =json&
		public String userip;// =112.90.139.30
	}

	public static class QQFriendsListParamClass {
		public String openid;
		/** 仅当pf==3366时返回以下 */
		public String nickname;
		public String gender;
		public String figureurl;
	}

	public interface InterfaceGetQQFriendsListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(List<QQFriendsListParamClass> mDataList, int ret,
				int is_lost);
	}

	// =======================================111===========================================================
	// QQ获取已安装了移动应用的好友信息11111
	public static class GetQQFriendsListParamClass1 {
		public String access_token;
		public String oauth_consumer_key;
		public String openid;
		public String format;
	}

	public static class QQFriendsListParamClass1 {
		public String openid;
		public String nickname;
		public String figureurl;
		public String figureurl_1;
		public String figureurl_2;
		public String figureurl_qq;
	}

	public interface InterfaceGetQQFriendsListCallBack1 {
		public void onError(int ret, String strErrMsg);

		public void onSuccess(List<QQFriendsListParamClass1> mDataList,
				int ret, String msg);
	}

	// =====================================================================================================
	// 更新用户基本信息 update_user_info.php
	public static class UpdateUserBasicInfoParamClass {
		public String my_int_id;
		public String my_int_age;
		public String my_int_gender;
		public String my_int_occupation;
	}

	public interface InterfaceUpdateUserBasicInfoCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String succErrMsg);
	}

	// =======================================================================================================
	// 2015-07-29
	// 以下接口 为香橙新增社区mCommunity模块中使用

	// 1、我的小组 api.php?mod=mygroup
	public static class GetCommunityGroupParamClass {
		/** 登录用户id 不为空且存在 */
		public String my_int_id;
		/** 当前页数 默认1 */
		public String page;
		/** 每页显示条数 默认10 */
		public String pagesize;
	}

	public interface InterfaceGetCommunityGroupCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityGrougsBean> groupList,
				int totalpage);
	}

	// =======================================================================================================
	// 2、添加小组 /api.php?mod=addgroup

	public static class MyTag {
		public String tag;
	}

	public static class AddCommunityGroupParamClass {
		/** 登录用户id 不为空且存在 */
		public String my_int_id;
		/** 小组类型 1为普通类；2为咨询类；3为打卡类； */
		public String g_type;
		/** 小组图标key */
		public String g_ico;
		/** 小组名称 不能为空 */
		public String g_name;
		/** 小组介绍 */
		public String g_intro;
		/** 小组标签 */
		public List<MyTag> g_tag;
	}

	public interface InterfaceAddCommunityGroupCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String succMsg, String gid,
				String g_ico_url);
	}

	// =======================================================================================================
	// 3、话题列表 /api.php?mod=threadlist
	public static class GetCommunityTopicParamClass {
		/** 小组id */
		public String gid;
		public String my_int_id;
		public String page;
		public String pagesize;
		public String page_id;
	}

	public interface InterfaceGetCommunityTopicCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityTopicBean> list,
				int totalpage, String page_id);
	}

	// =======================================================================================================
	// 4、话题 详情 /api.php?mod=threaddetail
	public static class GetCommunityTopicDetailParamClass {
		/** 话题id */
		public String tid;
		public String my_int_id;
	}

	public interface InterfaceGetCommunityTopicDetailCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, ArticleDetailBean info);
	}

	// =======================================================================================================
	// 5、话题点赞 /api.php?mod=threadzan
	public static class TopicPraiseParamClass {
		/** 话题id */
		public String tid;
		/** 当前登录用户id */
		public String my_int_id;
	}

	public interface InterfaceTopicPraiseCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 6、话题删除 /api.php?mod=threaddel
	public static class TopicDeleteParamClass {
		/** 话题id */
		public String tid;
		/** 当前登录用户id */
		public String my_int_id;
	}

	public interface InterfaceTopicDeleteCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 7、话题发布/api.php?mod=threadpost
	public static class TopicPostNewParamClass {
		// /**当前登录用户id*/
		// public String my_int_id;
		// /**话题所属小组id*/
		// public String gid;
		// /**话题内容、描述*/
		// public String t_message;
		// /**话题图片 二进制流*/
		// public String t_attachment;
		// /**标题*/
		// public String t_subject;
		// /**标签 Json数据*/
		// public String t_tag;
		/** 当前登录用户id */
		public String my_int_id;
		/** 话题所属小组id */
		public String gid;
		/** 话题内容、描述 */
		public String t_message;
		/** 话题图片 */
		public String t_attachment_key;
		/** 标题 */
		public String t_subject;
		/** 标签 */
		public String t_tag;
	}

	public interface InterfaceTopicPostNewCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg, String tid,
				String url, String url_original);
	}

	// =======================================================================================================
	// 8、回复发布 /api.php?mod=plpost

	public static class TopicReplyPostNewParamClass {
		/** 当前登录用户id */
		public String my_int_id;
		/** 话题id */
		public String tid;
		/** 内容 */
		public String p_message;
		/** 所属回复id */
		public String pid;
		/** 被回复的用户id */
		public String p_touid;
	}

	public interface InterfaceTopicReplyPostNewCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg, String pid);
	}

	// =======================================================================================================
	// 9、小组信息 /api.php?mod=groupinfo

	public static class GetGroupMessageParamClass {
		/** 当前登录用户id */
		public String my_int_id;
		/** 小组id */
		public String gid;
	}

	public interface InterfaceGetGroupMessageCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<GroupMessageBean> list);
	}

	// =======================================================================================================
	// 10、小组医生列表 /api.php?mod=doctorslist

	public static class GetGroupDoctorListParamClass {
		/** 小组id */
		public String gid;
		public String page;
		public String pagesize;
		public String my_int_id;
		public String page_id;
	}

	public interface InterfaceGetGroupDoctorListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityGroupDoctorBean> list,
				int totalpage, String is_doctor, String page_id);
	}

	// =======================================================================================================
	// 11、添加医生 /api.php?mod=doctorsadd

	public static class AddGroupDoctorParamClass {
		/** 小组id */
		public String gid;
		/** 当前登录用户id */
		public String my_int_id;
		/** 医生手机号码 */
		public String i_telephone;
	}

	public interface InterfaceAddGroupDoctorCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg,
				SpecialistBean mSpecialistBean);
	}

	// =======================================================================================================
	// 12、搜索小组话题 /api.php?mod=xcsearch
	public static class SearchGroupTopicParamClass {
		/** 搜索关键词 */
		public String xckey;
		public String my_int_id;
	}

	public interface InterfaceSearchGroupTopicCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityGroupBean> groupList,
				List<SearchTopicBean> topicList);
	}

	// =======================================================================================================
	// 13、举报 /api.php?mod=xcreport
	public static class ReportTopicParamClass {
		/** 当前登录用户id */
		public String my_int_id;
		/** 话题id */
		public String tid;
		/**
		 * 1为欺诈； 2为广告骚扰； 3为诱导分享； 4为谣言； 5为政治敏感； 6为色情低俗； 7为侵权； 8为编不下去了； 9为其它；
		 * */
		public String report;
	}

	public interface InterfaceReportTopicCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 14、编辑小组简介 /api.php?mod=groupedit
	public static class EditGroupSummaryParamClass {
		/** 当前登录用户id */
		public String my_int_id;
		/** 小组id */
		public String gid;
		/** 小组简介 */
		public String g_intro;
	}

	public interface InterfaceEditGroupSummaryCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 15、关注小组 /api.php?mod=groupfocus

	public static class AttentionGroupParamClass {
		/** 当前登录用户id */
		public String my_int_id;
		/** 小组id */
		public String gid;
	}

	public interface InterfaceAttentionGroupCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 16、发现列表 /api.php?mod=foundlist
	public static class CommunityFoundParamClass {
		public String my_int_id;
		public String page;
		public String pagesize;
		public String page_id;
	}

	public interface InterfaceCommunityFoundCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityTopicBean> list,
				int totalpage, String page_id);
	}

	// =======================================================================================================
	// 17、我的话题列表(按时间) /api.php?mod=mythreadt
	public static class MyTopicByTimeParamClass {
		public String my_int_id;
		public String page;
		public String pagesize;
		public String other_int_id;
	}

	public interface InterfaceMyTopicByTimeCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityTopicBean> list,
				int totalpage);
	}

	// =======================================================================================================
	// 18、我的话题列表(按小组) /api.php?mod=mythreadg
	public static class MyTopicByGroupParamClass {
		public String my_int_id;
		public String page;
		public String pagesize;
	}

	public interface InterfaceMyTopicByGroupCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityGrougsBean> list,
				int totalpage);
	}

	// =======================================================================================================
	// 19、标签列表 /api.php?mod=taglist
	public static class GetTagListParamClass {
		/** 不为空且存在，传gid表示标签属于小组；传tid表示标签属于话题 */
		public String gid;
		public String tid;
		public String my_int_id;
		public String tagname;
	}

	public interface InterfaceGetTagListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<TagBean> tagList,
				List<TagBean> hotTagList);
	}

	// =======================================================================================================
	// 20、删除标签 /api.php?mod=tagdel
	public static class DeleteTagParamClass {
		/** 标签id */
		public String tagid;
		/** 当前登录用户id */
		public String my_in_id;
		// 传gid表示标签属于小组；传tid表示标签属于话题
		public String gid;
		public String tid;
	}

	public interface InterfaceDeleteTagCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 21、热门标签列表 /api.php?mod=taghot
	public interface InterfaceGetHotTagCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<HotTagBean> tagList);
	}

	// =======================================================================================================
	// 22、取消关注小组 /api.php?mod=groupqxfocus

	public static class CancelAttentionGroupParamClass {
		/** 当前登录用户id */
		public String my_int_id;
		/** 小组id */
		public String gid;
	}

	public interface InterfaceCancelAttentionGroupCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 23、首页推荐点击没兴趣 /api.php?mod=nointerest
	public static class CommunityRecommendRefuseParamClass {
		/** 当前登录用户id */
		public String my_int_id;
		/** 小组id */
		public String gid;
	}

	public interface InterfaceCommunityRecommendRefuseCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 24、判断登录用户是否能够创建小组 /api.php?mod=checkaddgroup
	public static class CheckUserCreateGroupParamClass {
		/** 当前登录用户id */
		public String my_int_id;
	}

	public interface InterfaceCheckUserCreateGroupCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 25、搜索话题接口 /api.php?mod=xcsearcht
	public static class SearchTopicParamClass {
		public String xckey;
		public String page;
		public String pagesize;
		public String page_id;
	}

	public interface InterfaceSearchTopicCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<SearchTopicBean> list,
				int totalPage, String page_id);
	}

	// =======================================================================================================
	// 26、搜索小组接口 /api.php?mod=xcsearchg
	public static class SearchGroupParamClass {
		public String xckey;
		public String page;
		public String pagesize;
		public String my_int_id;
		public String page_id;
	}

	public interface InterfaceSearchGroupCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityGroupBean> list,
				int totalPage, String page_id);
	}

	// =======================================================================================================
	// 27、评论列表接口 /api.php?mod=postlist
	public static class GetCommentListParamClass {
		/** 话题id */
		public String tid;
		public String page;
		public String pagesize;
	}

	public interface InterfaceGetCommentLisCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommentBean> list, int totalPage);
	}

	// =======================================================================================================
	// 28、举报列表接口 /api.php?mod=reportlist

	public static class GetResportListParamClass {
		/** 小组id */
		public String gid;
		public String page;
		public String pagesize;
		public String page_id;
	}

	public interface InterfaceGetResportLisCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<ReportBean> list, int totalPage,
				String page_id);
	}

	// =======================================================================================================
	// 29、忽略举报接口 /api.php?mod=reporthl

	public static class IgnoreResportParamClass {
		/** 举报id */
		public String rid;
		public String my_int_id;
	}

	public interface InterfaceIgnoreResportCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 30、删除举报接口 /api.php?mod=reportdel

	public static class DeleteResportParamClass {
		/** 举报id */
		public String rid;
		public String my_int_id;
	}

	public interface InterfaceDeleteResportCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 31、动态列表接口 /api.php?mod=noticelist

	public static class GetAutoListParamClass {
		public String my_int_id;
		public String page;
		public String pagesize;
		public String page_id;
	}

	public interface InterfaceGetAutoLisCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<AutoMsgBean> list, int totalpage,
				String page_id);
	}

	// =======================================================================================================
	// 32、清空动态接口 /api.php?mod=noticedel

	public static class ClearAutoListParamClass {
		/** 举报id */
		public String rid;
		public String my_int_id;
	}

	public interface InterfaceClearAutoLisCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 33、添加标签 /api.php?mod=tagadd
	/**
	 * 小组id或者话题id （两个只需传一个参数） 不为空且存在，传gid表示标签属于小组；传tid表示标签属于话题
	 * 
	 * @author Administrator
	 * 
	 */
	public static class AddTagParamClass {
		public String gid;
		public String tid;
		public String my_int_id;
		public String tagname;
	}

	public interface InterfaceAddTagCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 34、用户未读动态数量接口 /api.php?mod=noticenum
	public static class DynamicMsgNumParamClass {
		public String my_int_id;
	}

	public interface InterfaceDynamicMsgNumCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg, int num);
	}

	// =======================================================================================================
	// 35、修改标签（删除或增加标签后点击确定统一提交） /api.php?mod=tagmodify
	public static class ModifyTagParamClass {
		public String gid;
		public String tid;
		public String my_int_id;
		/** [{ "tag":"失眠"},{"tag":"多梦"}] */
		public String tagname;
	}

	public interface InterfaceModifyTagCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 36、小组医生删除
	public static class DelGroupDoctorParamClass {
		public String my_int_id;
		public String gid;
		/** [{ "id":"10000008"},{"id":"100000009"}] */
		public String id;
	}

	public interface InterfaceDelGroupDoctorCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 37、绑定手机 /api.php?mod=telphonebd

	public static class BoundPhoneParamClass {
		public String my_phone_num;
		/** 验证码 */
		public String verification_code;
		/** 绑定用户id */
		public String target_int_id;
		/** 用户密码 */
		public String pwd;
	}

	public interface InterfaceBoundPhoneCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// 38退出医生 /api.php?mod=doctortc

	public static class cancleDoctorParamClass {
		public String my_int_id;
		public String gid;
	}

	public interface InterfaceCancleDoctorCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 39、发送短信 /api.php?mod=sendmessage
	public static class SendMessageParamClass {
		public String nickname;
		public String g_name;
		public String phone_num;
	}

	public interface InterfaceSendMessageCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 40、推荐小组列表 /api.php?mod=recogroup

	public static class RecomGroupListParamClass {
		public String my_int_id;// 没有登录，my_int_id=0
	}

	public interface InterfaceRecomGroupListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityGrougsBean> list);
	}

	// =======================================================================================================
	// 41、动态列表 /api.php?mod=dongtai
	public static class GetAutoListNewParamClass {
		public String my_int_id;
		public String page;
		public String pagesize;
		/**
		 * 请求第一页时接口返回的参数page_id Page=1时，page_id=0。
		 */
		public String page_id;
	}

	public interface InterfaceGetAutoLisNewCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityTopicBean> list,
				int totalpage, String page_id);
	}

	// =======================================================================================================
	// 42、修改小组头像/api.php?mod=groupeditico

	public static class UpdateGroupIconParamClass {
		public String my_int_id;
		public String gid;
		public String g_ico;
	}

	public interface InterfaceUpdateGroupIconCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg, String g_ico_url);
	}

	// =======================================================================================================
	// 43、我的小组中我发布的话题列表/api.php?mod=mygroupthreadt
	public static class MyGroupPostTopicListParamClass {
		public String my_int_id;
		public String gid;
		public String page;
		public String pagesize;
	}

	public interface InterfaceMyGroupPostTopicListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<CommunityTopicBean> list,
				int totalpage);
	}

	// =======================================================================================================
	// 44、个人信息修改接口 /api.php?mod=edituserinfo
	public static class EditUserInfoParamClass {
		public String my_int_id;
		public String my_int_gender;
		public String my_int_occupation;
		public String user_internal_height;
		public String user_internal_weight;
		public String user_internal_birthday;
	}

	public interface InterfaceEditUserInfoCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 45、新浪微博好友列表 /api.php?mod=weibolist

	public static class GetWeiBoUserListParamClass {
		public String my_int_id;
		public List<MyWeiBoFriendBaean> weiboid = new ArrayList<MyWeiBoFriendBaean>();
	}

	public static class WeiBoUserInfoListClass implements Serializable {
		private static final long serialVersionUID = 1L;
		public String flag;// 1为不是我们平台用户；2为是我们平台的用户
		public String uid;// 用户内部ID；
		public String nickname;// 昵称；
		public String age;// 年龄；
		public String gender;// 性别；
		public String occupation;// 职业；
		public String profile_suolue = "";// 头像图片缩略图url
		public String profile_key_suolue = "";// 头像缩略图key
		public String status;// 好友关系（1表示是好友； 2表示已经提交过申请，对方还未通过申请； 3
								// 表示对方向自己提交过好友申请，等待自己的确认；4表示不是好友关系）
	}

	public interface InterfaceGetWeiBoUserListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg,
				List<WeiBoUserInfoListClass> mList, int toatalpage);
	}

	// =======================================================================================================
	// 46、手机通信录好友列表 /api.php?mod=phonelist

	public static class GetPhoneUserListParamClass {
		public String my_int_id;
		/** 用户通讯录 */
		public List<CellPhoneParamClass> phones;
		public int page;
		public int pagesize;
	}

	public static class PhoneUserInfoListClass {
		/** 1为不是我们平台用户；2为是我们平台的用户 */
		public String flag;//
		public String uid;// 用户内部ID；
		public String phone;// 用户内部ID；
		public String phonename;// 用户内部ID；
		public String nickname;// 昵称；
		public String age;// 年龄；
		public String gender;// 性别；
		public String occupation;// 职业；
		public String profile_suolue;// 头像图片缩略图url
		public String profile_key_suolue;// 头像缩略图key
		public String status;// 好友关系（1表示是好友； 2表示已经提交过申请，对方还未通过申请； 3
								// 表示对方向自己提交过好友申请，等待自己的确认；4表示不是好友关系）
		public boolean isShowTag = false;
	}

	public interface InterfaceGetPhoneUserListCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<PhoneUserInfoListClass> mList,
				int toatalpage);
	}

	// =======================================================================================================
	// 47、删除评论 /api.php?mod=postdel
	public static class DeleteCommentParamClass {
		public String pid;
		public String my_int_id;
	}

	public interface InterfaceDeleteCommentCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 48、置顶话题 /api.php?mod=threadzd

	public static class SetTopTopicParamClass {
		public String tid;
		public String my_int_id;
	}

	public interface InterfaceSetTopTopicCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 49、普通用户咨询专家自动添加专家为好友 /api.php?mod=addfriendszj
	public static class AddExpertFriendParamClass {
		public String my_int_id;
		public String zj_int_id;
	}

	public static class ExpertFriendClass {
		public String user_internal_id;// 专家id
		public String user_internal_nickname;// 专家昵称
		public String user_internal_profile;// 专家头像
		public String user_internal_age;// 专家年龄
		public String user_internal_gender;// 专家性别
		public String user_internal_occupation;// 专家职业
		public String bueatysleep_duration;// 专家睡眠时长
	}

	public interface InterfaceAddExpertFriendCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg,
				ExpertFriendClass friendClass);
	}

	// =======================================================================================================
	// 50、首页小组推荐 /api.php?mod=regroupindex
	public interface InterfaceFirstPageRecommentGroupCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<FirstRecommentGroupBean> mList);
	}

	// =======================================================================================================
	// 51、获取硬件绑定天数 /api.php?mod=yjbangd
	public interface InterfaceGetHardwareBoundDaysCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String boundDays);
	}

	// =======================================================================================================
	// 52、硬件绑定与解绑 /api.php?mod=yjuploadsbd

	public static class HardwareBoundParamClass {
		public String my_int_id;// 登录用户id
		public String jystatus;// 操作状态
		public String bdtime;// 绑定时间
		public String jbtime;// 解绑时间
		public String yjlmd;// 灵敏度
		public String macadd;// mac地址
	}

	public interface InterfaceHardwareBoundCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 53、上传硬件睡眠数据 /api.php?mod=yjuploads

	public static class UploadHardwareDataParamClass1 {
		public String my_int_id;// 登录用户id
		public String date;// 睡眠数据所属日期
		public String xstart;// 起始时间
		public String xstop;// 结束时间
		public String ymax;// Y轴最大量化值
		public String insleeptime;// 入睡时刻
		public String outsleeptime;// 醒来时刻
		public String totalsleeptime;// 睡眠时长
		public String deepsleep;// 深度睡眠总时长
		public String shallowsleep;// 浅度睡眠总时长
		public String awaketimesleep;// 清醒时长（单位分钟）
		public String onbed;// 在床时长
		public String tosleep;// 入睡速度（单位分钟）
		public String awakecount;// 清醒次数
		public String awakenogetupcount;// 赖床时间
		public String gotobedtime;// 上床时间
		public String getuptime;// 起床时间
		public String listlength;// 每日数据集条数
		public String sleepbak1;// 备用字段1
		public String Sleepbak2;// 备用字段2
		public String file;// Model文件二进制流
		public String user_location_x;// X坐标
		public String user_location_y;// Y坐标
	}

	public interface InterfaceUploadHardwareDataCallBack1 {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String strSuccMsg);
	}

	// =======================================================================================================
	// 54、下载硬件睡眠数据（最近14天） /api.php?mod=yjdownloads
	public interface InterfaceDownloadHardwareDataCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, List<HardwareSleepDataBean> mList);
	}

	// =======================================================================================================
	// 55、获取调参接口 /api.php?mod=yjtiaocan
	public interface InterfaceGetTiaocanCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String tiaocanfile);
	}

	// =======================================================================================================
	// 56、获取用户灵敏度/api.php?mod=yjlmd
	public interface InterfaceGetHardwareSensitivityCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String selectLMD, String allLMD);
	}

	// =======================================================================================================
	// 57、用户修改灵敏度 /api.php?mod=yjlmdedit
	public static class MofifyHardwareSensitivityParamClass {
		public String my_int_id;// 登录用户id
		public String yjlmd;// 硬件灵敏度
	}

	public interface InterfaceMofifyHardwareSensitivityCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String tiaocanfile);
	}

	// =======================================================================================================
	// 58、登录用户是否绑定硬件及获取绑定mac地址
	public interface InterfaceGetBundInfoAtLoginTimeCallBack {
		public void onError(int icode, String strErrMsg);

		public void onSuccess(int icode, String flag, String macadd);
	}
}
