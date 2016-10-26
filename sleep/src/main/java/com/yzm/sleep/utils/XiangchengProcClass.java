package com.yzm.sleep.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.yzm.sleep.bean.AccessAttentionUserBean;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.ArticleCommentBean;
import com.yzm.sleep.bean.ArticleCorrelatGroupBean;
import com.yzm.sleep.bean.ArticleDetailBean;
import com.yzm.sleep.bean.ArticleImageBean;
import com.yzm.sleep.bean.ArticleThumbUpUserBean;
import com.yzm.sleep.bean.ClinicBean;
import com.yzm.sleep.bean.CommunityBarnerBean;
import com.yzm.sleep.bean.CommunityEventBean;
import com.yzm.sleep.bean.CommunityEventDetailBean;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.bean.DoctorBean;
import com.yzm.sleep.bean.DoctorDetailBean;
import com.yzm.sleep.bean.HardwareSleepDataBean;
import com.yzm.sleep.bean.PushClasifyBean;
import com.yzm.sleep.bean.ReservationBean;
import com.yzm.sleep.bean.ReservationDetailBean;
import com.yzm.sleep.bean.ReservationResultBean;
import com.yzm.sleep.bean.RingtoneBean;
import com.yzm.sleep.bean.TaocanBean;
import com.yzm.sleep.bean.ThumbUpUserBean;
import com.yzm.sleep.bean.UserInfoBean;
import com.yzm.sleep.bean.UserInfoPhotoBean;
import com.yzm.sleep.bean.UserMessageBean;
import com.yzm.sleep.bean.UserSleepDataBean;
import com.yzm.sleep.utils.InterFaceUtilsClass.CheckCellPhoneLoginStatRst;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserSleepDataListClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ArticleCommentListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ArticleDetailMsgParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ArticleThumbUpUserParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.BoundJpushParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ClinicListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.CommunityGroupListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.CompleteUserInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.DoctorListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.EventDetailParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.EventListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.GetArticleByTagParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.GetUserIdParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.GroupAccessAttentionUserListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.GroupHotNewListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.HandPickArticleListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceArticleCommentListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceArticleDetailMsgCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceArticleThumbUpUserCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceBoundJpushCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceClinicDetailCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceClinicListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceCommunityGroupListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceCompleteUserInfoCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceDoctorDetailCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceDoctorListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceEventDetailCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceEventListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetArticleByTagCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetBuyUrlCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetDayPillowDataCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetPunchDayCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetUserIdByNicknameCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGroupAccessAttentionUserListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGroupHotNewListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceHandPickArticleListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceModifyAccessNumCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceMoreRingtoneListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceOpenJPushCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfacePhoneLoginCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceQueryUserSleepDataCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceRecentlySevenDaysDataCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceRecommmendMoreGroupCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceReservationCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceReservationDetailCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceReservationListCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceSaveUserEvaluationCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceUpLoadPunchDayCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceUpdateUserNicknameCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceUserMessageCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ModifyAccessNumParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.OpenJPushParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.QueryUserSleepDataParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.RecommmendMoreGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ReservationListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ReservationParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.SaveUserEvaluationParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.SearchedUserNicknameClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.UpdateUserNicknameParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.UploadDakaDaysParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.UserMessageParamClass;

public class XiangchengProcClass extends HttpDataTranUtils {
	private Context mContext = null;
	private String errMsgNoneString = "未知错误";
	private String errMsgExceptionString = "未知异常";
	private int errMsgNoneCode = 101;
	private int errMsgExceptionCode = 102;
	private String phon;

	public XiangchengProcClass(Context context) {
		mContext = context;
	}

	/**
	 * 获取签名参数值
	 * 
	 * @param interfaceName
	 * @return
	 */
	private String getRequestToken(String interfaceName) {
		String encryption = Util.encryption(interfaceName + // 接口名
				// SleepUtils.getFormatedDateTime("yyyyMMdd",
				// System.currentTimeMillis()) + //日期
				SleepConstants.NET_REQUEST_KEY);// 加密密钥

		return encryption;
	}

	// ===========================================================================
	// 1、精选文章接口 /api.php?mod=articlejinghua
	private String handPickArticleUrl = g_BaseSiteURL
			+ "api.php?mod=articlejinghua";
	private InterfaceHandPickArticleListCallback m_InterfaceHandPickArticleListCallback;

	public void handPickArticle(HandPickArticleListParamClass mParams,
			InterfaceHandPickArticleListCallback callBack) {
		m_InterfaceHandPickArticleListCallback = callBack;
		String newString = handPickArticleUrl + "&my_int_id="
				+ mParams.my_int_id + "&page=" + mParams.page + "&pagesize="
				+ mParams.pagesize + "&xctoken="
				+ getRequestToken("articlepush");
		super.requestJosnObjectData(mContext, newString);
	}

	private void handPickArticleRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4718")) {

				m_InterfaceHandPickArticleListCallback.onError(4718, "参数错误");

			} else if (idValue.equals("4719")) {

				m_InterfaceHandPickArticleListCallback.onError(4719,
						"用户id不存在或者格式错误");

			} else if (idValue.equals("4856")) {
				Gson gson = new Gson();
				int totalpage = (int) response.getInt("totalpage");
				List<ArticleBean> article_list;
				try {
					String articleString = (String) response
							.getString("article_list");
				    article_list = gson.fromJson(articleString, new TypeToken<List<ArticleBean>>() {}.getType());
				} catch (Exception e) {
					article_list = new ArrayList<ArticleBean>();
				}
				List<PushClasifyBean> pushList;
				try {
					String pushClassifyString = (String) response.getString("push_list");
					pushList = gson.fromJson(pushClassifyString, new TypeToken<List<PushClasifyBean>>() {}.getType());
				} catch (Exception e) {
					pushList =new ArrayList<PushClasifyBean>();
				}
				List<CommunityBarnerBean> barner_list;
				try {
					String barnerString = (String) response.getString("barner_list");
					barner_list = gson.fromJson( barnerString, new TypeToken<List<CommunityBarnerBean>>() {}.getType());
				} catch (Exception e) {
					barner_list = new ArrayList<CommunityBarnerBean>();
				}
				m_InterfaceHandPickArticleListCallback.onSuccess(4856,barner_list, pushList, article_list, totalpage);
			}
		} catch (Exception e) {
			LogUtil.i("tianxun", e.getMessage());
			m_InterfaceHandPickArticleListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	
	//根据标签分类
	private String articlelistbytagUrl = g_BaseSiteURL
			+ "api.php?mod=articlelistbytag";
	private InterfaceGetArticleByTagCallback mInterfaceGetArticleByTagCallback;

	public void getArticleByTag(GetArticleByTagParamClass mParams,
			InterfaceGetArticleByTagCallback callBack) {
		
		mInterfaceGetArticleByTagCallback = callBack;
		String newString = articlelistbytagUrl 
				+ "&my_int_id="+ mParams.my_int_id 
				+ "&page="+ mParams.page 
				+ "&tagid="+ mParams.tagid 
				+ "&pagesize="+ mParams.pagesize 
				+ "&xctoken="+ getRequestToken("articlepush");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getArticleByTagRstProc(JSONObject response){
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4857")) 
				mInterfaceGetArticleByTagCallback.onError(4857, "分类ID不存在");
			
			if(idValue.equals("4858")){
				Gson gson = new Gson();
				int totalpage = (int) response.getInt("totalpage");
				List<ArticleBean> article_list;
				try {
					String articleString = (String) response
							.getString("article_list");
				    article_list = gson.fromJson(articleString, new TypeToken<List<ArticleBean>>() {}.getType());
				} catch (Exception e) {
					article_list = new ArrayList<ArticleBean>();
				}
				mInterfaceGetArticleByTagCallback.onSuccess(4858, article_list, totalpage);
			}

		} catch (Exception e) {
			mInterfaceGetArticleByTagCallback.onError(errMsgExceptionCode,errMsgExceptionString);
		}
	}


	// ===========================================================================
	// 2、社区小组接口 /api.php?mod=shequgroup
	private String communityGroupListUrl = g_BaseSiteURL
			+ "api.php?mod=shequgroup";
	private InterfaceCommunityGroupListCallback m_InterfaceCommunityGroupListCallback;

	public void communityGroupList(CommunityGroupListParamClass mParams,
			InterfaceCommunityGroupListCallback callBack) {
		m_InterfaceCommunityGroupListCallback = callBack;
		String newString = communityGroupListUrl + "&my_int_id="
				+ mParams.my_int_id + "&page=" + mParams.page + "&pagesize="
				+ mParams.pagesize + "&xctoken="
				+ getRequestToken("shequgroup");
		super.requestJosnObjectData(mContext, newString);
	}

	private void communityGroupLisRstProc(JSONObject response) {
		String idValue = "";

		try {
			LogUtil.d("chen", "response="+response.toString());
			idValue = (String) response.get("response").toString();

			if (idValue.equals("4721")) {

				m_InterfaceCommunityGroupListCallback.onError(4721, "参数错误");

			} else if (idValue.equals("4722")) {
				Gson gson = new Gson();
				String pushString = (String) response.getString("push_list");
				String hotString = (String) response.getString("hot_thread");
				List<CommunityGroupBean> push_list = gson.fromJson(pushString,
						new TypeToken<List<CommunityGroupBean>>() {
						}.getType());
				List<ArticleBean> hot_thread = gson.fromJson(hotString,
						new TypeToken<List<ArticleBean>>() {
						}.getType());
				int totalpage = (int) response.getInt("totalpage");
				m_InterfaceCommunityGroupListCallback.onSuccess(4722,
						push_list, hot_thread, totalpage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceCommunityGroupListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ===========================================================================
	// 3、小组的最热、最新话题列表接口 /api.php?mod=articlelist
	private String groupHotNewListUrl = g_BaseSiteURL
			+ "api.php?mod=articlelist";
	private InterfaceGroupHotNewListCallback m_InterfaceGroupHotNewListCallback;

	public void groupHotNewList(GroupHotNewListParamClass mParams,
			InterfaceGroupHotNewListCallback callBack) {
		m_InterfaceGroupHotNewListCallback = callBack;
		String newString = groupHotNewListUrl + "&my_int_id="
				+ mParams.my_int_id + "&gid=" + mParams.gid + "&flag="
				+ mParams.flag + "&page=" + mParams.page + "&pagesize="
				+ mParams.pagesize + "&xctoken="
				+ getRequestToken("articlelist");
		super.requestJosnObjectData(mContext, newString);
	}

	private void groupHotNewListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4723")) {

				m_InterfaceGroupHotNewListCallback.onError(4723, "参数错误");

			} else if (idValue.equals("4724")) {

				m_InterfaceGroupHotNewListCallback.onError(4724, "小组不存在");

			} else if (idValue.equals("4725")) {
				Gson gson = new Gson();
				String groupString = (String) response.getString("group_info");
				String articleString = (String) response
						.getString("article_list");
				int totalpage = (int) response.getInt("totalpage");
				// List<CommunityGroupBean> group_info =
				// gson.fromJson(groupString, new
				// TypeToken<List<CommunityGroupBean>>(){}.getType());
				CommunityGroupBean group_info = gson.fromJson(groupString,
						CommunityGroupBean.class);
				List<ArticleBean> article_list = gson.fromJson(articleString,
						new TypeToken<List<ArticleBean>>() {
						}.getType());
				m_InterfaceGroupHotNewListCallback.onSuccess(4725, group_info,
						article_list, totalpage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGroupHotNewListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ===========================================================================
	// 4、文章点赞用户列表接口 /api.php?mod=articlezanuser
	private String articleThumbUpUserUrl = g_BaseSiteURL
			+ "api.php?mod=articlezanuser";
	private InterfaceArticleThumbUpUserCallback m_InterfaceArticleThumbUpUserCallback;

	public void articleThumbUpUserLists(ArticleThumbUpUserParamClass mParams,
			InterfaceArticleThumbUpUserCallback callBack) {
		m_InterfaceArticleThumbUpUserCallback = callBack;
		String newString = articleThumbUpUserUrl + "&tid=" + mParams.tid
				+ "&page=" + mParams.page + "&pagesize=" + mParams.pagesize
				+ "&xctoken=" + getRequestToken("articlezanuser");
		super.requestJosnObjectData(mContext, newString);
	}

	private void articleThumbUpUserListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4726")) {

				m_InterfaceArticleThumbUpUserCallback.onError(4726, "参数错误");

			} else if (idValue.equals("4727")) {

				m_InterfaceArticleThumbUpUserCallback.onError(4727, "文章不存在");

			} else if (idValue.equals("4728")) {
				Gson gson = new Gson();
				String userString = (String) response.getString("user_list");
				int totalpage = (int) response.getInt("totalpage");
				List<ThumbUpUserBean> user_list = gson.fromJson(userString,
						new TypeToken<List<ThumbUpUserBean>>() {
						}.getType());
				m_InterfaceArticleThumbUpUserCallback.onSuccess(4728,
						user_list, totalpage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceArticleThumbUpUserCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ===========================================================================
	// 5、小组访问用户、小组关注用户列表接口 /api.php?mod=groupviewuser
	private String groupAccessAttentionUserListUrl = g_BaseSiteURL
			+ "api.php?mod=groupviewuser";
	private InterfaceGroupAccessAttentionUserListCallback m_InterfaceGroupAccessAttentionUserListCallback;

	public void groupAccessAttentionUserList(
			GroupAccessAttentionUserListParamClass mParams,
			InterfaceGroupAccessAttentionUserListCallback callBack) {
		m_InterfaceGroupAccessAttentionUserListCallback = callBack;
		String newString = groupAccessAttentionUserListUrl + "&gid="
				+ mParams.gid + "&flag=" + mParams.flag + "&page="
				+ mParams.page + "&pagesize=" + mParams.pagesize + "&xctoken="
				+ getRequestToken("groupviewuser");
		super.requestJosnObjectData(mContext, newString);
	}

	private void groupAccessAttentionUserListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4729")) {

				m_InterfaceGroupAccessAttentionUserListCallback.onError(4729,
						"参数错误");

			} else if (idValue.equals("4730")) {

				m_InterfaceGroupAccessAttentionUserListCallback.onError(4730,
						"小组存在");

			} else if (idValue.equals("4731")) {
				Gson gson = new Gson();
				String userString = (String) response.getString("user_list");
				int totalpage = (int) response.getInt("totalpage");
				List<AccessAttentionUserBean> user_list = gson.fromJson(
						userString,
						new TypeToken<List<AccessAttentionUserBean>>() {
						}.getType());
				m_InterfaceGroupAccessAttentionUserListCallback.onSuccess(4731,
						user_list, totalpage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGroupAccessAttentionUserListCallback.onError(
					errMsgExceptionCode, errMsgExceptionString);
		}
	}

	// ===========================================================================
	// 6、修改文章访问数量、修改小组访问数量 /api.php?mod=viewsadd
	private String modifyAccessNumUrl = g_BaseSiteURL + "api.php?mod=viewsadd";
	private InterfaceModifyAccessNumCallback m_InterfaceModifyAccessNumCallback;

	public void modifyAccessNum(ModifyAccessNumParamClass mParams,
			InterfaceModifyAccessNumCallback callBack) {
		m_InterfaceModifyAccessNumCallback = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(modifyAccessNumUrl);
		if (TextUtils.isEmpty(mParams.tid)) {
			sb.append("&tid=" + mParams.tid);
		}
		if (TextUtils.isEmpty(mParams.gid)) {
			sb.append("&gid=" + mParams.gid);
		}
		sb.append("&my_int_id=" + mParams.my_int_id);
		sb.append("&xctoken=" + getRequestToken("viewsadd"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}

	private void modifyAccessNumRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4732")) {

				m_InterfaceModifyAccessNumCallback.onError(4732, "参数错误");

			} else if (idValue.equals("4733")) {

				m_InterfaceModifyAccessNumCallback.onError(4733, "用户存在");

			} else if (idValue.equals("4734")) {

				m_InterfaceModifyAccessNumCallback.onError(4734, "小组或者文章不存在");

			} else if (idValue.equals("4735")) {

				m_InterfaceModifyAccessNumCallback.onError(4735, "失败");

			} else if (idValue.equals("4736")) {
				m_InterfaceModifyAccessNumCallback.onSuccess(4736, "成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceModifyAccessNumCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 7、手机用户登录 /api.php?mod=phonelogin

	private String phoneLoginUrl = g_BaseSiteURL + "api.php?mod=phonelogin";
	private InterfacePhoneLoginCallback m_InterfacePhoneLoginCallback;

	/**
	 * 手机登录
	 * 
	 * @param phone
	 *            手机号码
	 * @param callBack
	 */
	public void phoneLogin(String phone, String verification_code,
			InterfacePhoneLoginCallback callBack) {
		m_InterfacePhoneLoginCallback = callBack;
		String newString = phoneLoginUrl + "&my_phone_num=" + phone
				+ "&verification_code=" + verification_code + "&xctoken="
				+ getRequestToken("phonelogin");
		phon = phone;
		super.requestJosnObjectData(mContext, newString);
	}

	private void phoneLoginRstProc(JSONObject response) {
		LogUtil.i("txg", response.toString());
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4738")) {
				m_InterfacePhoneLoginCallback.onError(4738, "参数错误");
			} else if (idValue.equals("4739")) {
				m_InterfacePhoneLoginCallback.onError(4739, "验证码错误");
			} else if (idValue.equals("4740")) {
				m_InterfacePhoneLoginCallback.onError(4740, "验证码过期");
			} else if (idValue.equals("4741")) {
				Gson gson = new Gson();
				String user_info = (String) response.getString("user_info");
				String photos = (String) response.getString("photos");
				UserInfoBean userInfo = gson.fromJson(user_info, new TypeToken<UserInfoBean>() { }.getType());
				List<UserInfoPhotoBean> userPhotoInfo = gson.fromJson(photos, new TypeToken<List<UserInfoPhotoBean>>() { }.getType());
				CheckCellPhoneLoginStatRst rst = new CheckCellPhoneLoginStatRst();
				rst.int_id = userInfo.getInt_id();
				rst.nickname = userInfo.getNickname();
				String age = userInfo.getAge();
				if (age.length() == 4) {
					age = age + "0101";
				}
				rst.age = age;
				rst.gender = userInfo.getGender();
				rst.profile = userInfo.getProfile();
				rst.hide = userInfo.getHiding();
				rst.profile_key = userInfo.getProfile_key();
				rst.user_ext_acc_qq = String.valueOf(userInfo.getUser_ext_acc_qq());
				rst.user_ext_acc_weibo = String.valueOf(userInfo.getUser_ext_acc_weibo());
				rst.user_ext_acc_wechat = String.valueOf(userInfo.getUser_ext_acc_wechat());
				rst.user_ext_acc_cellphone = String.valueOf(userInfo.getUser_ext_acc_cellphone());
				rst.occupation = String.valueOf(userInfo.getOccupation());
				rst.height = String.valueOf(userInfo.getHeight());
				rst.weight = String.valueOf(userInfo.getWeight());
				rst.ispinggu = userInfo.getIspinggu();
				rst.dakadays = userInfo.getDakadays();
				rst.is_zj = userInfo.getIs_zj();
				rst.jgtssz = userInfo.getJgtssz();
				rst.sleep_pg = userInfo.getSleep_pg();
				GetUserData(phon, userInfo);
				m_InterfacePhoneLoginCallback.onSuccess(4741, userInfo, userPhotoInfo);

			}

		} catch (Exception e) {
			e.printStackTrace();
			m_InterfacePhoneLoginCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	private void GetUserData(String my_phone_num, UserInfoBean userInfo) {

		PreManager.instance().saveUserId(mContext, userInfo.getInt_id());
		PreManager.instance().saveUserNickname(mContext, userInfo.getNickname());
		PreManager.instance().saveUserProfession(mContext, Integer.parseInt(userInfo.getOccupation()));
		PreManager.instance().saveUserOccupation(mContext, userInfo.getOccupation());
		PreManager.instance().saveUserProfileUrl(mContext, userInfo.getProfile());
		PreManager.instance().saveUserProfileKey(mContext, userInfo.getProfile_key());
		PreManager.instance().saveUserBirthday(mContext, userInfo.getAge());
		PreManager.instance().saveUserGender(mContext, userInfo.getGender());
		PreManager.instance().saveUserHeight(mContext, userInfo.getHeight());
		PreManager.instance().saveUserWeight(mContext, userInfo.getWeight());
		PreManager.instance().saveUserIsAssess(mContext, userInfo.getIspinggu());
		PreManager.instance().saveUserIsExpert(mContext, userInfo.getIs_zj());
		PreManager.instance().saveUserDadaDays(mContext, userInfo.getDakadays());
		PreManager.instance().saveIsCompleteSleepPg(mContext, "1".equals(userInfo.getSleep_pg()) ? true : false);
		PreManager.instance().saveGetupTime_Setting(mContext, userInfo.getWakeup());
		PreManager.instance().saveSleepTime_Setting(mContext, userInfo.getSleep());
		if ("1".equals(userInfo.getHiding()))
			PreManager.instance().saveUserHide(mContext, true);
		else
			PreManager.instance().saveUserHide(mContext, false);
		PreManager.instance().savePlatformBoundMsg(mContext,
				userInfo.getUser_ext_acc_weibo() + userInfo.getUser_ext_acc_wechat()
						+ userInfo.getUser_ext_acc_qq()
						+ userInfo.getUser_ext_acc_cellphone());
		PreManager.instance().saveBoundPhoneNumber(mContext, my_phone_num);
		PreManager.instance().saveLoginUserName(mContext, my_phone_num);
		PreManager.instance().saveIsOpenFormuInform(mContext,userInfo.getJgtssz());

	}

	// ==========================================================================
	// 8、查询两个时间段内用户的睡眠数据接口 /api.php?mod=monthsleepdata

	private String queryUserSleepDataUrl = g_BaseSiteURL
			+ "api.php?mod=monthsleepdata";
	private InterfaceQueryUserSleepDataCallback m_InterfaceQueryUserSleepDataCallback;

	public void queryUserSleepData(QueryUserSleepDataParamClass mParams,
			InterfaceQueryUserSleepDataCallback callBack) {
		m_InterfaceQueryUserSleepDataCallback = callBack;
		String newString = queryUserSleepDataUrl + "&my_int_id="
				+ mParams.my_int_id + "&sdate=" + mParams.sdate + "&edate="
				+ mParams.edate + "&flag=" + mParams.flag + "&xctoken="
				+ getRequestToken("monthsleepdata");
		super.requestJosnObjectData(mContext, newString);
	}

	private void queryUserSleepDataRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4743")) {

				m_InterfaceQueryUserSleepDataCallback.onError(4743, "参数错误");

			} else if (idValue.equals("4744")) {

				m_InterfaceQueryUserSleepDataCallback.onError(4744, "用户不存在");

			} else if (idValue.equals("4746")) {

				m_InterfaceQueryUserSleepDataCallback.onError(4746, "失败");

			} else if (idValue.equals("4747")) {
				Gson gson = new Gson();
				String string = (String) response.getString("datas");
				List<UserSleepDataBean> datas = gson.fromJson(string,
						new TypeToken<List<UserSleepDataBean>>() {
						}.getType());
				m_InterfaceQueryUserSleepDataCallback.onSuccess(4747, datas);

			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceQueryUserSleepDataCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 9、下载更多铃音列表接口 /api.php?mod=lydownloads

	private String moreRingtoneListUrl = g_BaseSiteURL
			+ "api.php?mod=lydownloads";
	private InterfaceMoreRingtoneListCallback m_InterfaceMoreRingtoneListCallback;

	public void moreRingtoneList(InterfaceMoreRingtoneListCallback callBack) {
		m_InterfaceMoreRingtoneListCallback = callBack;
		String newString = moreRingtoneListUrl + "&xctoken="
				+ getRequestToken("lydownloads");
		super.requestJosnObjectData(mContext, newString);
	}

	private void moreRingtoneListRstProc(JSONObject response) {
		String idValue = "";

		try {
//			LogUtil.d("chen", response.toString());
			idValue = (String) response.get("response").toString();

			if (idValue.equals("4748")) {
				Gson gson = new Gson();
				String string = (String) response.getString("lylist");
				List<RingtoneBean> lylist = gson.fromJson(string,
						new TypeToken<List<RingtoneBean>>() {
						}.getType());
				m_InterfaceMoreRingtoneListCallback.onSuccess(4748, lylist);

			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceMoreRingtoneListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 10、用户完善昵称，生日，性别信息接口 /api.php?mod=adduserinfo

	private String completeUserInfoUrl = g_BaseSiteURL
			+ "api.php?mod=adduserinfo";
	private InterfaceCompleteUserInfoCallback m_InterfaceCompleteUserInfoCallback;

	public void completeUserInfo(CompleteUserInfoParamClass mParams,
			InterfaceCompleteUserInfoCallback callBack) {
		m_InterfaceCompleteUserInfoCallback = callBack;
		String newString = completeUserInfoUrl + "&my_int_id="
				+ mParams.my_int_id + "&nickname=" + mParams.nickname
				+ "&userbirthday=" + mParams.userbirthday + "&usersex="
				+ mParams.usersex + "&xctoken="
				+ getRequestToken("adduserinfo");
		super.requestJosnObjectData(mContext, newString);
	}

	private void completeUserInfoRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4749")) {

				m_InterfaceCompleteUserInfoCallback.onError(4749, "用户不存在");

			} else if (idValue.equals("4749:1")) {

				m_InterfaceCompleteUserInfoCallback.onError(47491, "昵称已经存在");

			} else if (idValue.equals("4750")) {

				m_InterfaceCompleteUserInfoCallback.onError(4750, "参数错误");

			} else if (idValue.equals("4751")) {
				m_InterfaceCompleteUserInfoCallback.onSuccess(4751, "成功");

			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceCompleteUserInfoCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 11、 /api.php?mod=articledetail  /api.php?mod=articledetail_new 

	private String articleDetailMsgUrl = g_BaseSiteURL
			+ "api.php?mod=articledetail_new";
	private InterfaceArticleDetailMsgCallback m_InterfaceArticleDetailMsgCallback;

	/**
	 * 获取文章详情
	 * 
	 * @param mParams
	 * @param callBack
	 */
	public void articleDetailMsg(ArticleDetailMsgParamClass mParams,
			InterfaceArticleDetailMsgCallback callBack) {
		m_InterfaceArticleDetailMsgCallback = callBack;
		String newString = articleDetailMsgUrl + "&my_int_id="
				+ mParams.my_int_id + "&tid=" + mParams.tid + "&xctoken="
				+ getRequestToken("articledetail");
		super.requestJosnObjectData(mContext, newString);
	}

	private void articleDetailMsgRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4752")) {
				m_InterfaceArticleDetailMsgCallback.onError(4752, "文章不存在");
			} else if (idValue.equals("4753")) {
				m_InterfaceArticleDetailMsgCallback.onError(4753, "参数错误");
			} else if (idValue.equals("4754")) {
				ArticleDetailBean bean = new ArticleDetailBean();
				String string = (String) response.getString("datas");
				JSONObject json = new JSONObject(string);
				bean.setTid(json.getString("tid"));
				bean.setGid(json.getString("gid"));
				bean.setUid(json.getString("uid"));
				bean.setT_subject(json.getString("t_subject"));
				bean.setT_message(json.getString("t_message"));
				bean.setT_dateline(json.getString("t_dateline"));
				bean.setT_lastpost(json.getString("t_lastpost"));
				bean.setT_lastposter(json.getString("t_lastposter"));
				bean.setT_zans(json.getString("t_zans"));
				bean.setT_view(json.getString("t_views"));
				bean.setT_replies(json.getString("t_replies"));
				bean.setT_displayorder(json.getString("t_displayorder"));
				bean.setT_isshare(json.getString("t_isshare"));
				bean.setT_author(json.getString("t_author"));
				bean.setAuthor_profile(json.getString("author_profile"));
				bean.setAuthor_profile_key(json.getString("author_profile_key"));
				bean.setIs_zj(json.getString("is_zj"));
				bean.setStatus(json.getString("status"));
				bean.setQuanxian(json.getString("quanxian"));
				bean.setIntro(json.getString("intro"));

				JSONArray zanuser = json.getJSONArray("t_zan_user");
				List<ArticleThumbUpUserBean> t_zan_user = new ArrayList<ArticleThumbUpUserBean>();
				for (int i = 0; i < (zanuser == null ? 0 : zanuser.length()); i++) {
					ArticleThumbUpUserBean beanz = new ArticleThumbUpUserBean();
					JSONObject jsonz = zanuser.getJSONObject(i);
					beanz.setUid(jsonz.getString("uid"));
					beanz.setNickname(jsonz.getString("nickname"));
					beanz.setAuthor_profile(jsonz.getString("author_profile"));
					beanz.setAuthor_profile_key(jsonz
							.getString("author_profile_key"));
					t_zan_user.add(beanz);
				}

				bean.setT_zan_user(t_zan_user);

				JSONArray connectGroup = json.getJSONArray("connect_group");
				List<ArticleCorrelatGroupBean> connect_group = new ArrayList<ArticleCorrelatGroupBean>();
				for (int i = 0; i < (connectGroup == null ? 0 : connectGroup
						.length()); i++) {
					ArticleCorrelatGroupBean beang = new ArticleCorrelatGroupBean();
					JSONObject jsong = connectGroup.getJSONObject(i);
					beang.setBjpic(jsong.getString("bjpic"));
					beang.setBjpic_key(jsong.getString("bjpic_key"));
					beang.setG_intro(jsong.getString("g_intro"));
					beang.setGid(jsong.getString("gid"));
					beang.setG_ico(jsong.getString("g_ico"));
					beang.setG_ico_key(jsong.getString("g_ico_key"));
					beang.setG_name(jsong.getString("g_name"));
					connect_group.add(beang);
				}
				bean.setConnect_group(connect_group);

				JSONArray jsonImage = json.getJSONArray("images");
				List<ArticleImageBean> images = new ArrayList<ArticleImageBean>();
				for (int i = 0; i < (jsonImage == null ? 0 : jsonImage.length()); i++) {
					ArticleImageBean img = new ArticleImageBean();
					JSONObject jsonImg = jsonImage.getJSONObject(i);
					img.setContent_attachment(jsonImg
							.getString("content_attachment"));
					img.setContent_attachment_sl(jsonImg
							.getString("content_attachment_sl"));
					img.setT_attachment_key(jsonImg
							.getString("t_attachment_key"));
					img.setT_attachment_key_sl(jsonImg
							.getString("t_attachment_key_sl"));
					images.add(img);
				}
				bean.setImages(images);
				m_InterfaceArticleDetailMsgCallback.onSuccess(4754, bean);

			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceArticleDetailMsgCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 12、文章评论列表接口 /api.php?mod=articleposts
	private String articleCommentListUrl = g_BaseSiteURL
			+ "api.php?mod=articleposts";
	private InterfaceArticleCommentListCallback m_InterfaceArticleCommentListCallback;

	/**
	 * 获取评论列表
	 * 
	 * @param mParams
	 * @param callBack
	 */
	public void articleCommentList(ArticleCommentListParamClass mParams,
			InterfaceArticleCommentListCallback callBack) {
		m_InterfaceArticleCommentListCallback = callBack;
		String newString = articleCommentListUrl + "&tid=" + mParams.tid
				+ "&my_int_id=" + mParams.my_int_id + "&page=" + mParams.page
				+ "&page_id=" + mParams.page_id + "&pagesize="
				+ mParams.pagesize + "&xctoken="
				+ getRequestToken("articleposts");
		super.requestJosnObjectData(mContext, newString);
	}

	private void articleCommentListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4755")) {

				m_InterfaceArticleCommentListCallback.onError(4755, "文章不存在");

			} else if (idValue.equals("4756")) {

				m_InterfaceArticleCommentListCallback.onError(4756, "参数错误");

			} else if (idValue.equals("4757")) {
				Gson gson = new Gson();
				String string = (String) response.getString("pl_list");
				List<ArticleCommentBean> pl_list = gson.fromJson(string,
						new TypeToken<List<ArticleCommentBean>>() {
						}.getType());
				int totalpage = (int) response.getInt("totalpage");
				String page_id = response.getString("page_id");
				m_InterfaceArticleCommentListCallback.onSuccess(4757, pl_list,
						totalpage, page_id);

			}
		} catch (Exception e) {
			m_InterfaceArticleCommentListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}


	// ==========================================================================
	// 13、根据用户id，返回用户个人信息。 包括用户基本信息 睡眠数据 发表话题 /api.php?mod=userinfobyid
	// /api.php?mod=userinfobyidnew
	private String userMessageUrl = g_BaseSiteURL + "api.php?mod=userinfobyidnew";
	private InterfaceUserMessageCallback m_InterfaceUserMessageCallback;

	public void userMessage(UserMessageParamClass mParams,
			InterfaceUserMessageCallback callBack) {
		m_InterfaceUserMessageCallback = callBack;
		String newString = userMessageUrl + "&uid=" + mParams.uid + "&page="
				+ mParams.page + "&pagesize=" + mParams.pagesize + "&xctoken="
				+ getRequestToken("userinfobyidnew");
		super.requestJosnObjectData(mContext, newString);
	}

	private void userMessageRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4758")) {

				m_InterfaceUserMessageCallback.onError(4758, "用户不存在");

			} else if (idValue.equals("4759")) {

				m_InterfaceUserMessageCallback.onError(4759, "参数错误");

			} else if (idValue.equals("4760")) {
				Gson gson = new Gson();
				String userInfoString = (String) response
						.getString("user_info");
				String sleepdataString = (String) response
						.getString("sleep_data");
				String sleepdataString2 = (String) response
						.getString("sleep_data_2");
				String threadListString = (String) response
						.getString("thread_list");
				int totalpage = (int) response.getInt("totalpage");

				UserMessageBean user_info = gson.fromJson(userInfoString,
						UserMessageBean.class);
				List<UserSleepDataBean> sleep_data = gson.fromJson(
						sleepdataString,
						new TypeToken<List<UserSleepDataBean>>() {
						}.getType());
				List<UserSleepDataBean> sleep_data_2 = gson.fromJson(
						sleepdataString2,
						new TypeToken<List<UserSleepDataBean>>() {
						}.getType());
				List<ArticleBean> thread_list = gson.fromJson(threadListString,
						new TypeToken<List<ArticleBean>>() {
						}.getType());
				m_InterfaceUserMessageCallback.onSuccess(4760, user_info,
						sleep_data, sleep_data_2,thread_list, totalpage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceUserMessageCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 14、香橙推荐更多小组列表接口 /api.php?mod=shequgroupmore

	private String recommmendMoreGroupUrl = g_BaseSiteURL
			+ "api.php?mod=shequgroupmore";
	private InterfaceRecommmendMoreGroupCallback m_InterfaceRecommmendMoreGroupCallback;

	public void recommmendMoreGroup(RecommmendMoreGroupParamClass mParams,
			InterfaceRecommmendMoreGroupCallback callBack) {
		m_InterfaceRecommmendMoreGroupCallback = callBack;
		String newString = recommmendMoreGroupUrl + "&my_int_id="
				+ mParams.my_int_id + "&page=" + mParams.page + "&pagesize="
				+ mParams.pagesize + "&xctoken="
				+ getRequestToken("shequgroupmore");
		super.requestJosnObjectData(mContext, newString);
	}

	private void recommmendMoreGroupRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			/*
			 * if (idValue.equals("4758")) {
			 * 
			 * m_InterfaceRecommmendMoreGroupCallback.onError(4758, "用户不存在");
			 * 
			 * }else if (idValue.equals("4759")) {
			 * 
			 * m_InterfaceRecommmendMoreGroupCallback.onError(4759, "参数错误");
			 * 
			 * }else
			 */if (idValue.equals("4761")) {
				Gson gson = new Gson();
				String pushListString = (String) response
						.getString("push_list");
				int totalpage = (int) response.getInt("totalpage");
				List<CommunityGroupBean> push_list = gson.fromJson(
						pushListString,
						new TypeToken<List<CommunityGroupBean>>() {
						}.getType());
				m_InterfaceRecommmendMoreGroupCallback.onSuccess(4761,
						push_list, totalpage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceRecommmendMoreGroupCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 15、 活动列表接口 /api.php?mod=huodong

	private String eventListUrl = g_BaseSiteURL + "api.php?mod=huodong";
	private InterfaceEventListCallback m_InterfaceEventListCallback;

	public void eventList(EventListParamClass mParams,
			InterfaceEventListCallback callBack) {
		m_InterfaceEventListCallback = callBack;
		String newString = eventListUrl + "&my_int_id=" + mParams.my_int_id
				+ "&page=" + mParams.page + "&pagesize=" + mParams.pagesize
				+ "&xctoken=" + getRequestToken("huodong");
		super.requestJosnObjectData(mContext, newString);
	}

	private void eventListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
			if (idValue.equals("4762")) {
				Gson gson = new Gson();
				String pushListString = (String) response.getString("list");
				int totalpage = (int) response.getInt("totalpage");
				List<CommunityEventBean> list = gson.fromJson(pushListString,
						new TypeToken<List<CommunityEventBean>>() {
						}.getType());
				m_InterfaceEventListCallback.onSuccess(4762, list, totalpage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceEventListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 16、 活动详情接口 /api.php?mod=huodongdetail  /api.php?mod=huodongdetail_new 

	private String eventDetailUrl = g_BaseSiteURL + "api.php?mod=huodongdetail_new";
	private InterfaceEventDetailCallback m_InterfaceEventDetailCallback;

	public void eventDetail(EventDetailParamClass mParams,
			InterfaceEventDetailCallback callBack) {
		m_InterfaceEventDetailCallback = callBack;
		String newString = eventDetailUrl + "&my_int_id=" + mParams.my_int_id
				+ "&id=" + mParams.id + "&xctoken="
				+ getRequestToken("huodongdetail");
		super.requestJosnObjectData(mContext, newString);
	}

	private void eventDetailRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4764")) {

				m_InterfaceRecommmendMoreGroupCallback.onError(4764, "活动不存在");

			} else if (idValue.equals("4763")) {
				CommunityEventDetailBean detail = new CommunityEventDetailBean();
				JSONObject jsonObject = response.getJSONObject("data");
				String picture = (String) jsonObject.getString("picture");
				String picturekey = (String) jsonObject.getString("picturekey");
				String content = (String) jsonObject.getString("content");
				String over  = (String) jsonObject.getString("over");
				detail.setPicture(picture);
				detail.setOver(over);
				detail.setPicturekey(picturekey);
				detail.setContent(content);

				m_InterfaceEventDetailCallback.onSuccess(4763, detail);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceEventDetailCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 17、 绑定极光推送用户接口 /api.php?mod=jiguanglogin

	private String boundJpushlUrl = g_BaseSiteURL + "api.php?mod=jiguanglogin";
	private InterfaceBoundJpushCallback m_InterfaceBoundJpushCallback;

	public void boundJpushlUrl(BoundJpushParamClass mParams,
			InterfaceBoundJpushCallback callBack) {
		m_InterfaceBoundJpushCallback = callBack;
		String newString = boundJpushlUrl + "&my_int_id=" + mParams.my_int_id
				+ "&alias=" + mParams.alias + "&tag=" + mParams.tag
				+ "&registrationid=" + mParams.registrationid + "&xctoken="
				+ getRequestToken("jiguanglogin");
		super.requestJosnObjectData(mContext, newString);
	}

	private void boundJpushlUrlRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4765")) {

				m_InterfaceBoundJpushCallback.onError(4765, "用户不存在");

			} else if (idValue.equals("4766")) {

				m_InterfaceBoundJpushCallback.onError(4766, "参数不能为空");

			} else if (idValue.equals("4767")) {

				m_InterfaceBoundJpushCallback.onSuccess(4767, "成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceBoundJpushCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 18、 用户评估记录保存接口/api.php?mod=pinggu

	private String saveUserEvaluationUrl = g_BaseSiteURL + "api.php?mod=pinggu";
	private InterfaceSaveUserEvaluationCallback m_InterfaceSaveUserEvaluationCallback;

	public void saveUserEvaluation(SaveUserEvaluationParamClass mParams,
			InterfaceSaveUserEvaluationCallback callBack) {
		m_InterfaceSaveUserEvaluationCallback = callBack;
		String newString = saveUserEvaluationUrl + "&my_int_id="
				+ mParams.my_int_id + "&xctoken=" + getRequestToken("pinggu");
		super.requestJosnObjectData(mContext, newString);
	}

	private void saveUserEvaluationUrlRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4769")) {

				m_InterfaceSaveUserEvaluationCallback.onError(4769, "用户不存在");

			} else if (idValue.equals("4770")) {

				m_InterfaceSaveUserEvaluationCallback.onSuccess(4770, "成功");

			} else if (idValue.equals("4771")) {

				m_InterfaceSaveUserEvaluationCallback.onError(4771, "失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceSaveUserEvaluationCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 19、 更新用户昵称接口 /api.php?mod=modifynickname

	private String updateUserNicknameUrl = g_BaseSiteURL
			+ "api.php?mod=modifynickname";
	private InterfaceUpdateUserNicknameCallback m_InterfaceUpdateUserNicknameCallback;

	public void updateUserNickname(UpdateUserNicknameParamClass mParams,
			InterfaceUpdateUserNicknameCallback callBack) {
		m_InterfaceUpdateUserNicknameCallback = callBack;
		String newString = updateUserNicknameUrl + "&my_int_id="
				+ mParams.my_int_id + "&my_int_nickname="
				+ mParams.my_int_nickname + "&xctoken="
				+ getRequestToken("modifynickname");
		super.requestJosnObjectData(mContext, newString);
	}

	private void updateUserNicknameRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4777")) {

				m_InterfaceUpdateUserNicknameCallback.onError(4777, "用户不存在");

			} else if (idValue.equals("4778")) {

				m_InterfaceUpdateUserNicknameCallback.onError(4778, "用户昵称已经存在");

			} else if (idValue.equals("4779")) {

				m_InterfaceUpdateUserNicknameCallback.onError(4779, "参数错误");

			} else if (idValue.equals("4780")) {

				m_InterfaceUpdateUserNicknameCallback.onSuccess(4780, "成功");

			} else if (idValue.equals("4781")) {

				m_InterfaceUpdateUserNicknameCallback.onError(4781, "失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceUpdateUserNicknameCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 20、 返回某天的硬件数据接口 /api.php?mod=dayyjdata

	private String getDayPillowDataUrl = g_BaseSiteURL
			+ "api.php?mod=dayyjdata";
	private InterfaceGetDayPillowDataCallback m_InterfaceGetDayPillowDataCallback;

	/**
	 * 下载某一天的硬件睡眠数据
	 * 
	 * @param mParams
	 * @param callBack
	 */
	public void getDayPillowData(UploadDakaDaysParamClass mParams,
			InterfaceGetDayPillowDataCallback callBack) {
		m_InterfaceGetDayPillowDataCallback = callBack;
		String newString = getDayPillowDataUrl + "&my_int_id="
				+ mParams.my_int_id + "&date=" + mParams.date + "&xctoken="
				+ getRequestToken("dayyjdata");
		super.requestJosnObjectData(mContext, newString);
	}

	private void getDayPillowDataRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4782")) {

				m_InterfaceGetDayPillowDataCallback.onError(4782, "用户不存在");

			} else if (idValue.equals("4783")) {

				m_InterfaceGetDayPillowDataCallback.onError(4783, "参数错误");

			} else if (idValue.equals("4784")) {

				m_InterfaceGetDayPillowDataCallback.onError(4784, "失败");
			} else if (idValue.equals("4785")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("list");
				List<HardwareSleepDataBean> list = gson.fromJson(dataString,
						new TypeToken<List<HardwareSleepDataBean>>() {
						}.getType());
				m_InterfaceGetDayPillowDataCallback.onSuccess(4785, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetDayPillowDataCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 21、/api.php?mod=downloadcard
	private String getPunchDaysUrl = g_BaseSiteURL + "api.php?mod=downloadcard";
	private InterfaceGetPunchDayCallback m_interfaceGetPunchDayCallback;

	/**
	 * 下载用户连续打卡天数
	 * 
	 * @param in_it_id
	 * @param callback
	 */
	public void getGetPunchDays(String in_it_id,
			InterfaceGetPunchDayCallback callback) {
		m_interfaceGetPunchDayCallback = callback;
		String newString = getPunchDaysUrl + "&my_int_id=" + in_it_id
				+ "&xctoken=" + getRequestToken("downloadcard");
		super.requestJosnObjectData(mContext, newString);
	}

	private void getGetPunchDaysRstProc(JSONObject response) {
		String idValue = "";

		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4776")) {
				String days = (String) response.getString("days");
				m_interfaceGetPunchDayCallback.onSuccess(4776, days);
			} else {
				m_interfaceGetPunchDayCallback.onError(4775, "用户不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_interfaceGetPunchDayCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 22、 医馆列表接口/api.php?mod=yiguanlistbycity // 旧 ：api.php?mod=yiguanlist

	private String clinicLisUrl = g_BaseSiteURL + "api.php?mod=yiguanlistbycity";
	private InterfaceClinicListCallback m_InterfaceClinicListCallback;

	public void clinicList(ClinicListParamClass mParams,
			InterfaceClinicListCallback callBack) {
		m_InterfaceClinicListCallback = callBack;
		String newString = clinicLisUrl 
				+ "&city=" + mParams.city
				+ "&location_x=" + mParams.location_x 
				+ "&location_y=" + mParams.location_y
				+ "&page=" + mParams.page
				+ "&pagesize=" + mParams.pagesize + "&xctoken="
				+ getRequestToken("yiguanlistbycity");
		super.requestJosnObjectData(mContext, newString);
	}

	private void clinicListRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4848")) {
				m_InterfaceClinicListCallback.onError(4787, "失败");
			} else if (idValue.equals("4849")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("list");
				int totalpage = (int) response.getInt("totalpage");
				List<ClinicBean> list = gson.fromJson(dataString, new TypeToken<List<ClinicBean>>() {}.getType());
				m_InterfaceClinicListCallback.onSuccess(4849, list, totalpage);
			}
		} catch (Exception e) {
			m_InterfaceClinicListCallback.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 23、 医馆详情接口 /api.php?mod=yiguandetail

	private String clinicDetailUrl = g_BaseSiteURL + "api.php?mod=yiguandetail";
	private InterfaceClinicDetailCallback m_InterfaceClinicDetailCallback;

	public void clinicDetail(String ygid,String loc_x, String loc_y,
			InterfaceClinicDetailCallback callBack) {
		m_InterfaceClinicDetailCallback = callBack;
		String newString = clinicDetailUrl + "&ygid=" + ygid + "&location_x="+loc_x+"&location_y="+loc_y+"&xctoken="
				+ getRequestToken("yiguandetail");
		super.requestJosnObjectData(mContext, newString);
	}

	private void clinicDetailRstProc(JSONObject response) {
		String idValue = "";

		try {
			idValue = (String) response.get("response").toString();

			if (idValue.equals("4790")) {

				m_InterfaceClinicDetailCallback.onError(4790, "失败");

			} else if (idValue.equals("4791")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("data");
				String zhuanjiaString = (String) response.getString("zj_list");
				String shopString =  response.getString("shop_list");
				ClinicBean data = gson.fromJson(dataString, ClinicBean.class);
				List<DoctorBean> zj_list = gson.fromJson(zhuanjiaString,
						new TypeToken<List<DoctorBean>>() {
						}.getType());
				List<TaocanBean> shopList =  gson.fromJson(shopString,
						new TypeToken<List<TaocanBean>>() {
						}.getType());
				m_InterfaceClinicDetailCallback.onSuccess(4791, data, zj_list, shopList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceClinicDetailCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 24、 专家列表接口/api.php?mod=zhuanjialistbycity // 旧：/api.php?mod=zhuanjialist

	private String doctorLisUrl = g_BaseSiteURL + "api.php?mod=zhuanjialistbycity";
	private InterfaceDoctorListCallback m_InterfaceDoctorListCallback;

	public void doctorLis(DoctorListParamClass mParams,
			InterfaceDoctorListCallback callBack) {
		m_InterfaceDoctorListCallback = callBack;
		String newString = doctorLisUrl 
				+ "&city=" + mParams.city
				+ "&location_x=" + mParams.location_x
				+ "&location_y=" + mParams.location_y
				+ "&page=" + mParams.page
				+ "&pagesize=" + mParams.pagesize + "&xctoken="
				+ getRequestToken("zhuanjialistbycity");
		super.requestJosnObjectData(mContext, newString);
	}

	private void doctorListRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4850")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("list");
				int totalpage = (int) response.getInt("totalpage");
				List<DoctorBean> list = gson.fromJson(dataString,
						new TypeToken<List<DoctorBean>>() {
						}.getType());
				m_InterfaceDoctorListCallback.onSuccess(4792, list, totalpage);
			}
		} catch (Exception e) {
			m_InterfaceDoctorListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 25、 专家详情接口 /api.php?mod=zhuanjiadetail

	private String doctorDetailUrl = g_BaseSiteURL
			+ "api.php?mod=zhuanjiadetail";
	private InterfaceDoctorDetailCallback m_InterfaceDoctorDetailCallback;

	public void doctorDetail(String zjuid,String loc_x,String loc_y,
			InterfaceDoctorDetailCallback callBack) {
		m_InterfaceDoctorDetailCallback = callBack;
		String newString = doctorDetailUrl + "&zjuid=" + zjuid + "&location_x="+loc_x+"&location_y="+loc_y+"&xctoken="
				+ getRequestToken("zhuanjiadetail");
		super.requestJosnObjectData(mContext, newString);
	}

	private void doctorDetailRstProc(JSONObject response) {
		String idValue = "";

		try {
			idValue = (String) response.get("response").toString();

			if (idValue.equals("4794")) {

				m_InterfaceDoctorDetailCallback.onError(4794, "失败");

			} else if (idValue.equals("4795")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("data");
				String listString = (String) response.getString("shop_list");
				DoctorDetailBean data = gson.fromJson(dataString,
						DoctorDetailBean.class);
				List<TaocanBean> list = gson.fromJson(listString,
						new TypeToken<List<TaocanBean>>() {
				}.getType());
				m_InterfaceDoctorDetailCallback.onSuccess(4795, data, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceDoctorDetailCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 26、预约接口 /api.php?mod=yuyue

	private String reservationUrl = g_BaseSiteURL + "api.php?mod=yuyue";
	private InterfaceReservationCallback m_InterfaceReservationCallback;

	public void userReservation(ReservationParamClass mParams,
			InterfaceReservationCallback callBack) {
		m_InterfaceReservationCallback = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(reservationUrl);
		sb.append("&uid=" + mParams.uid);
		if (!TextUtils.isEmpty(mParams.ygid)) {
			sb.append("&ygid=" + mParams.ygid);
		}
		if (!TextUtils.isEmpty(mParams.zjuid)) {
			sb.append("&zjuid=" + mParams.zjuid);
		}
		sb.append("&content=" + mParams.content);
		sb.append("&phone=" + mParams.phone);
		sb.append("&xctoken=" + getRequestToken("yuyue"));
//		String newString = sb.toString();
		
		RequestParams params = new RequestParams();
		params.put("mod", "yuyue");
		params.put("uid", mParams.uid);
		if (!TextUtils.isEmpty(mParams.ygid)) {
			params.put("ygid", mParams.ygid);
		}
		if (!TextUtils.isEmpty(mParams.zjuid)) {
			params.put("zjuid", mParams.zjuid);
		}
		params.put("content", mParams.content);
		params.put("phone", mParams.phone);
		params.put("xctoken", getRequestToken("yuyue"));
		super.postJosnObjectData(mContext, reservationUrl, params);
//		super.requestJosnObjectData(mContext, newString);
	}

	private void userReservationRstProc(JSONObject response) {
		String idValue = "";

		try {
			idValue = (String) response.get("response").toString();

			if (idValue.equals("4796")) {

				m_InterfaceReservationCallback.onError(4796, "用户不存在");

			} else if (idValue.equals("4797")) {

				m_InterfaceReservationCallback.onError(4797, "医生不存在或者医馆不存在");

			}else if (idValue.equals("4798")) {

				m_InterfaceReservationCallback.onError(4798, "失败");

			} else if (idValue.equals("4799")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("info");
				ReservationResultBean info = gson.fromJson(dataString,
						ReservationResultBean.class);
				m_InterfaceReservationCallback.onSuccess(4799, info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceReservationCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	
	// ==========================================================================
	// 27、我的预约列表接口   /api.php?mod=myyuyuelist

	private String reservationListUrl = g_BaseSiteURL
			+ "api.php?mod=myyuyuelist";
	private InterfaceReservationListCallback m_InterfaceReservationListCallback;

	public void reservationList(ReservationListParamClass mParams,
			InterfaceReservationListCallback callBack) {
		m_InterfaceReservationListCallback = callBack;
		String newString = reservationListUrl + 
				"&uid=" + mParams.uid +
				"&page=" + mParams.page +
				"&pagesize=" + mParams.pagesize +
				"&xctoken="+ getRequestToken("myyuyuelist");
		super.requestJosnObjectData(mContext, newString);
	}

	private void reservationListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();

			if (idValue.equals("4800")) {

				m_InterfaceReservationListCallback.onError(4800, "失败");

			} else if (idValue.equals("4801")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("list");
				int totalpage = (int) response.getInt("totalpage");
				List<ReservationBean> list = gson.fromJson(dataString,
						new TypeToken<List<ReservationBean>>() {
						}.getType());
				m_InterfaceReservationListCallback.onSuccess(4801, list,totalpage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceReservationListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	// 28、我的预约详情接口   /api.php?mod=yuyuedetail
	
	private String reservationDetailUrl = g_BaseSiteURL
			+ "api.php?mod=yuyuedetail";
	private InterfaceReservationDetailCallback m_InterfaceReservationDetailCallback;
	
	public void reservationDetail(String id,
			InterfaceReservationDetailCallback callBack) {
		m_InterfaceReservationDetailCallback = callBack;
		String newString = reservationDetailUrl + 
				"&id=" + id +
				"&xctoken="+ getRequestToken("yuyuedetail");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void reservationDetailRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4802")) {
				
				m_InterfaceReservationDetailCallback.onError(4802, "预约不存在");
				
			} else if (idValue.equals("4803")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("data");
				ReservationDetailBean data = gson.fromJson(dataString, ReservationDetailBean.class);
				m_InterfaceReservationDetailCallback.onSuccess(4803, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceReservationDetailCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	//29、/api.php?mod=uploadcard   上传打卡天数
	private String upLoadPunchDaysUrl = g_BaseSiteURL + "api.php?mod=uploadcard";
	private InterfaceUpLoadPunchDayCallback m_interfaceUpLoadPunchDayCallback;

	/**
	 * 下载用户连续打卡天数
	 * @param in_it_id
	 * @param callback
	 */
	public void getUpLoadPunchDays(String in_it_id, String time, InterfaceUpLoadPunchDayCallback callback){
		m_interfaceUpLoadPunchDayCallback = callback;
		String newString = upLoadPunchDaysUrl + 
				"&my_int_id=" + in_it_id +
				"&date="+ time +
				"&xctoken=" + getRequestToken("uploadcard");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void upLoadPunchDaysRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			idValue = (String) response.get("response").toString();
			if(idValue.equals("4773")){
				String days = (String) response
						.getString("days");
				m_interfaceUpLoadPunchDayCallback.onSuccess(4773, days);
			}else if(idValue.equals("4772")){
				m_interfaceUpLoadPunchDayCallback.onError(4772, "用户不存在");
			}else{
				m_interfaceUpLoadPunchDayCallback.onError(4774, "用户不存在");
			}
		}catch(Exception e){
			e.printStackTrace();
			m_interfaceUpLoadPunchDayCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	
	// ==========================================================================
	// 30、下载用户最近7天的软件数据。    /api.php?mod=downloadssoftdata
	
	private String recentlySevenDaysDataUrl = g_BaseSiteURL
			+ "api.php?mod=downloadssoftdata";
	private InterfaceRecentlySevenDaysDataCallback m_InterfaceRecentlySevenDaysDataCallback;
	
	public void recentlySevenDaysData(String uid,
			InterfaceRecentlySevenDaysDataCallback callBack) {
		m_InterfaceRecentlySevenDaysDataCallback = callBack;
		String newString = recentlySevenDaysDataUrl + 
				"&uid=" + uid +
				"&xctoken="+ getRequestToken("downloadssoftdata");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void recentlySevenDaysDataRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4805")) {
				
				m_InterfaceRecentlySevenDaysDataCallback.onError(4805, "用户不存在");
				
			} else if (idValue.equals("4806")) {
				Gson gson = new Gson();
				String dataString1 = (String) response.getString("sleep_data");
				String dataString2 = (String) response.getString("sleep_data_2");
				List<UserSleepDataListClass> sleep_data = gson.fromJson(dataString1,
						new TypeToken<List<UserSleepDataListClass>>() {
						}.getType());
				List<UserSleepDataListClass> sleep_data_2 = gson.fromJson(dataString2,
						new TypeToken<List<UserSleepDataListClass>>() {
						}.getType());
				m_InterfaceRecentlySevenDaysDataCallback.onSuccess(4806, sleep_data,sleep_data_2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceRecentlySevenDaysDataCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	// 31、获取外部购买地址  /api.php?mod=buyurl
	private String getBuyUrl = g_BaseSiteURL
			+ "api.php?mod=buyurl";
	private InterfaceGetBuyUrlCallback m_InterfaceGetBuyUrlCallback;
	public void getBuyUrlData(InterfaceGetBuyUrlCallback callBack) {
		m_InterfaceGetBuyUrlCallback = callBack;
		String newString = getBuyUrl;
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getBuyUrlDataRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4804")) {
				String strmsg = (String) response.getString("message");
				String url = (String) response.getString("url");
				m_InterfaceGetBuyUrlCallback.onSuccess(4804, strmsg, url);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetBuyUrlCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	
	// ==========================================================================
	// 32、按用户昵称搜索  /api.php?mod=searchnickname
	private String getUserIdByNicknameUrl = g_BaseSiteURL
			+ "api.php?mod=searchnickname";
	private  InterfaceGetUserIdByNicknameCallback m_InterfaceGetUserIdByNicknameCallback;
	public void GetUserIdByNickname(GetUserIdParamClass mParam, InterfaceGetUserIdByNicknameCallback callBack){
		m_InterfaceGetUserIdByNicknameCallback = callBack;
//		String newString = getUserIdByNicknameUrl +
//				"&key="+mParam.key+
//				"&page="+mParam.page+
//				"&pagesize="+mParam.pagesize;
//		super.requestJosnObjectData(mContext, newString);
		RequestParams params = new RequestParams();
		params.put("key", mParam.key);
		params.put("page", mParam.page);
		params.put("pagesize", mParam.pagesize);
		super.postJosnObjectData(mContext, getUserIdByNicknameUrl, params);
	}
	
	private void GetUserIdByNicknameRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4786")) {
				int totalpage  = response.getInt("totalpage");
				Gson gson = new Gson();
				String list = (String) response.getString("list");
				List<SearchedUserNicknameClass> mList = gson.fromJson(list,
						new TypeToken<List<SearchedUserNicknameClass>>() {
						}.getType());
				m_InterfaceGetUserIdByNicknameCallback.onSuccess(4786, totalpage, mList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetUserIdByNicknameCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	
	// ==========================================================================
	// 33、开启或关闭极光推送  /api.php?mod=shezhijgts
	private String openJPushUrl = g_BaseSiteURL
			+ "api.php?";
	private  InterfaceOpenJPushCallback m_InterfaceOpenJPushCallback;
	public void openJPush(OpenJPushParamClass mParam, InterfaceOpenJPushCallback callBack){
		m_InterfaceOpenJPushCallback = callBack;
		RequestParams params = new RequestParams();
		params.put("mod", "shezhijgts");
		params.put("uid", mParam.uid);
		params.put("flag", mParam.flag);
		params.put("xctoken", getRequestToken("shezhijgts"));
		super.postJosnObjectData(mContext, openJPushUrl, params);
	}
	
	private void openJPushRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4807")) {
				
				m_InterfaceOpenJPushCallback.onError(4807, "用户不存在");
				
			}else if (idValue.equals("4808")) {
				
				m_InterfaceOpenJPushCallback.onError(4808, "Flag参数错误");
				
			}else if (idValue.equals("4809")) {
				
				m_InterfaceOpenJPushCallback.onError(4809, "失败");
				
			}else if (idValue.equals("4810")) {
				m_InterfaceOpenJPushCallback.onSuccess(4810, "成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceOpenJPushCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void ProcJSONData(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("410")) {

				requestFailed(idValue + "：非法访问", Integer.parseInt(idValue));

			} else if (idValue.equals("411")) {

				requestFailed(idValue + "：服务器数据库连接错误",
						Integer.parseInt(idValue));

			} else if (idValue.equals("412")) {

				requestFailed(idValue + "：服务器数据库操作错误",
						Integer.parseInt(idValue));

			} else if (idValue.equals("4999")) {

				requestFailed(idValue + "：提交的参数含有非法字符",
						Integer.parseInt(idValue));

			} else if (idValue.equals("4718") || idValue.equals("471９")
					|| idValue.equals("4856")) {

				handPickArticleRstProc(response);

			} else if (idValue.equals("4721") || idValue.equals("4722")) {

				communityGroupLisRstProc(response);

			} else if (idValue.equals("4723") || idValue.equals("4724")
					|| idValue.equals("4725")) {

				groupHotNewListRstProc(response);

			} else if (idValue.equals("4726") || idValue.equals("4727")
					|| idValue.equals("4728")) {

				articleThumbUpUserListRstProc(response);

			} else if (idValue.equals("4729") || idValue.equals("4730")
					|| idValue.equals("4731")) {

				groupAccessAttentionUserListRstProc(response);

			} else if (idValue.equals("4732") || idValue.equals("4733")
					|| idValue.equals("4734") || idValue.equals("4735")
					|| idValue.equals("4736")) {

				modifyAccessNumRstProc(response);

			} else if (idValue.equals("4738") || idValue.equals("4739")
					|| idValue.equals("4740") || idValue.equals("4741")) {

				phoneLoginRstProc(response);

			} else if (idValue.equals("4743") || idValue.equals("4744")
					|| idValue.equals("4746") || idValue.equals("4747")) {

				queryUserSleepDataRstProc(response);

			} else if (idValue.equals("4748")) {

				moreRingtoneListRstProc(response);

			} else if (idValue.equals("4749") || idValue.equals("4749:1")
					|| idValue.equals("4750") || idValue.equals("4751")) {

				completeUserInfoRstProc(response);

			} else if (idValue.equals("4752") || idValue.equals("4753")
					|| idValue.equals("4754")) {

				articleDetailMsgRstProc(response);

			} else if (idValue.equals("4755") || idValue.equals("4756")
					|| idValue.equals("4757")) {

				articleCommentListRstProc(response);

			} else if (idValue.equals("4758") || idValue.equals("4759")
					|| idValue.equals("4760")) {

				userMessageRstProc(response);

			} else if (idValue.equals("4761")) {

				recommmendMoreGroupRstProc(response);

			} else if (idValue.equals("4762")) {

				eventListRstProc(response);

			} else if (idValue.equals("4763") || idValue.equals("4764")) {

				eventDetailRstProc(response);

			} else if (idValue.equals("4765") || idValue.equals("4766")
					|| idValue.equals("4767")) {

				boundJpushlUrlRstProc(response);

			} else if (idValue.equals("4769") || idValue.equals("4770")
					|| idValue.equals("4771")) {

				saveUserEvaluationUrlRstProc(response);

			} else if (idValue.equals("4777") || idValue.equals("4778")
					|| idValue.equals("4779") || idValue.equals("4780")
					|| idValue.equals("4781")) {

				updateUserNicknameRstProc(response);

			} else if (idValue.equals("4782") || idValue.equals("4783")
					|| idValue.equals("4784") || idValue.equals("4785")) {

				getDayPillowDataRstProc(response);
			} else if (idValue.equals("4775") || idValue.equals("4776")) {
				
				getGetPunchDaysRstProc(response);
				
			} else if (idValue.equals("4848") || idValue.equals("4849")) {
				clinicListRstProc(response);
			} else if (idValue.equals("4790") || idValue.equals("4791")) {
				
				clinicDetailRstProc(response);
				
			} else if (idValue.equals("4850")) {
				
				doctorListRstProc(response);
				
			} else if (idValue.equals("4794") || idValue.equals("4795")) {
				
				doctorDetailRstProc(response);
				
			} else if(idValue.equals("4796") || idValue.equals("4797") ||
					idValue.equals("4798") || idValue.equals("4799")){
				
				userReservationRstProc(response);
				
			}else if(idValue.equals("4800") || idValue.equals("4801")){
				
				reservationListRstProc(response);
				
			}else if(idValue.equals("4802") || idValue.equals("4803")){
				
				reservationDetailRstProc(response);
				
			}else if(idValue.equals("4772") || idValue.equals("4773") || idValue.equals("4774")){
				
				upLoadPunchDaysRstProc(response);
				
			}else if(idValue.equals("4805") || idValue.equals("4806")){
				
				recentlySevenDaysDataRstProc(response);
				
			}else if(idValue.equals("4804")){
				
				getBuyUrlDataRstProc(response);
				
			}else if(idValue.equals("4786")){
				
				GetUserIdByNicknameRstProc(response);
				
			}else if(idValue.equals("4807") || idValue.equals("4808") || idValue.equals("4809")
					|| idValue.equals("4810")){
				openJPushRstProc(response);
			}else if(idValue.equals("4858")|| idValue.equals("4857")){
				getArticleByTagRstProc(response);
			}
			else {
				
				requestFailed(errMsgNoneString, errMsgNoneCode);
				
			}
		} catch (Exception e) {
			
			requestFailed(errMsgExceptionString, errMsgExceptionCode);
			
		}
	}

	@Override
	public void ProcJSONDataOnErr(VolleyError error) {
		String errmsg = "访问服务器失败";
		int iCode = 3001;

		requestFailed(errmsg, iCode);
	}

	private void requestFailed(String errmsg, int iCode) {
		if (m_InterfaceHandPickArticleListCallback != null) {
			m_InterfaceHandPickArticleListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceCommunityGroupListCallback != null) {
			m_InterfaceCommunityGroupListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceGroupHotNewListCallback != null) {
			m_InterfaceGroupHotNewListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceArticleThumbUpUserCallback != null) {
			m_InterfaceArticleThumbUpUserCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceGroupAccessAttentionUserListCallback != null) {
			m_InterfaceGroupAccessAttentionUserListCallback.onError(iCode,
					errmsg);
		}
		if (m_InterfaceModifyAccessNumCallback != null) {
			m_InterfaceModifyAccessNumCallback.onError(iCode, errmsg);
		}
		if (m_InterfacePhoneLoginCallback != null) {
			m_InterfacePhoneLoginCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceQueryUserSleepDataCallback != null) {
			m_InterfaceQueryUserSleepDataCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceMoreRingtoneListCallback != null) {
			m_InterfaceMoreRingtoneListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceCompleteUserInfoCallback != null) {
			m_InterfaceCompleteUserInfoCallback.onError(iCode, errmsg);
		}

		// --
		if (m_InterfaceArticleDetailMsgCallback != null) {
			m_InterfaceArticleDetailMsgCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceArticleCommentListCallback != null) {
			m_InterfaceArticleCommentListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceUserMessageCallback != null) {
			m_InterfaceUserMessageCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceRecommmendMoreGroupCallback != null) {
			m_InterfaceRecommmendMoreGroupCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceEventListCallback != null) {
			m_InterfaceEventListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceEventDetailCallback != null) {
			m_InterfaceEventDetailCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceBoundJpushCallback != null) {
			m_InterfaceBoundJpushCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceSaveUserEvaluationCallback != null) {
			m_InterfaceSaveUserEvaluationCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceUpdateUserNicknameCallback != null) {
			m_InterfaceUpdateUserNicknameCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceGetDayPillowDataCallback != null) {
			m_InterfaceGetDayPillowDataCallback.onError(iCode, errmsg);
		}

		// --
		if (m_interfaceGetPunchDayCallback != null) {
			m_interfaceGetPunchDayCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceClinicListCallback != null) {
			m_InterfaceClinicListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceClinicDetailCallback != null) {
			m_InterfaceClinicDetailCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceDoctorListCallback != null) {
			m_InterfaceDoctorListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceDoctorDetailCallback != null) {
			m_InterfaceDoctorDetailCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceReservationCallback != null) {
			m_InterfaceReservationCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceReservationListCallback != null) {
			m_InterfaceReservationListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceReservationDetailCallback != null) {
			m_InterfaceReservationDetailCallback.onError(iCode, errmsg);
		}
		if (m_interfaceUpLoadPunchDayCallback != null) {
			m_interfaceUpLoadPunchDayCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceRecentlySevenDaysDataCallback != null) {
			m_InterfaceRecentlySevenDaysDataCallback.onError(iCode, errmsg);
		}
		
		//--
		if (m_InterfaceGetBuyUrlCallback != null){
			m_InterfaceGetBuyUrlCallback.onError(iCode, errmsg);
		}
		if(m_InterfaceGetUserIdByNicknameCallback != null){
			m_InterfaceGetUserIdByNicknameCallback.onError(iCode, errmsg);
		}
		if(m_InterfaceOpenJPushCallback != null){
			m_InterfaceOpenJPushCallback.onError(iCode, errmsg);
		}
		
		if(mInterfaceGetArticleByTagCallback != null){
			mInterfaceGetArticleByTagCallback.onError(iCode, errmsg);
		}
		
	}

}
