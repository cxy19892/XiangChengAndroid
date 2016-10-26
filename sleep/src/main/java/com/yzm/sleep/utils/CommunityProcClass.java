package com.yzm.sleep.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.yzm.sleep.bean.ArticleDetailBean;
import com.yzm.sleep.bean.ArticleImageBean;
import com.yzm.sleep.bean.ArticleThumbUpUserBean;
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
import com.yzm.sleep.bean.ReportBean;
import com.yzm.sleep.bean.SearchTopicBean;
import com.yzm.sleep.bean.SpecialistBean;
import com.yzm.sleep.bean.TagBean;
import com.yzm.sleep.utils.InterFaceUtilsClass.AddCommunityGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.AddExpertFriendParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.AddGroupDoctorParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.AddTagParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.AttentionGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.BoundPhoneParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.CancelAttentionGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.CheckUserCreateGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.ClearAutoListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.CommunityFoundParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.CommunityRecommendRefuseParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DelGroupDoctorParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DeleteCommentParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DeleteResportParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DeleteTagParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.DynamicMsgNumParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.EditGroupSummaryParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.EditUserInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.ExpertFriendClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetAutoListNewParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetAutoListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetCommentListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetCommunityGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetCommunityTopicDetailParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetCommunityTopicParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetGroupDoctorListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetGroupMessageParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetPhoneUserListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetResportListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetTagListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetWeiBoUserListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.HardwareBoundParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.IgnoreResportParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceAddCommunityGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceAddExpertFriendCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceAddGroupDoctorCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceAddTagCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceAttentionGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceBoundPhoneCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCancelAttentionGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCancleDoctorCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCheckUserCreateGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceClearAutoLisCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCommunityFoundCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCommunityRecommendRefuseCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDelGroupDoctorCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDeleteCommentCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDeleteResportCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDeleteTagCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownloadHardwareDataCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDynamicMsgNumCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceEditGroupSummaryCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceEditUserInfoCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceFirstPageRecommentGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetAutoLisCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetAutoLisNewCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetBundInfoAtLoginTimeCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetCommentLisCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetCommunityGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetCommunityTopicCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetCommunityTopicDetailCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetGroupDoctorListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetGroupMessageCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetHardwareBoundDaysCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetHardwareSensitivityCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetHotTagCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetPhoneUserListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetResportLisCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetTagListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetTiaocanCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetWeiBoUserListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceHardwareBoundCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceIgnoreResportCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceModifyTagCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceMofifyHardwareSensitivityCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceMyGroupPostTopicListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceMyTopicByGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceMyTopicByTimeCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceRecomGroupListCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceReportTopicCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSearchGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSearchGroupTopicCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSearchTopicCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSendMessageCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceSetTopTopicCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceTopicDeleteCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceTopicPostNewCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceTopicPraiseCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceTopicReplyPostNewCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUpdateGroupIconCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareDataCallBack1;
import com.yzm.sleep.utils.InterFaceUtilsClass.ModifyTagParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.MofifyHardwareSensitivityParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.MyGroupPostTopicListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.MyTopicByGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.MyTopicByTimeParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.PhoneUserInfoListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.RecomGroupListParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.ReportTopicParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SearchGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SearchGroupTopicParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SearchTopicParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SendMessageParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.SetTopTopicParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.TopicDeleteParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.TopicPostNewParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.TopicPraiseParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.TopicReplyPostNewParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UpdateGroupIconParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareDataParamClass1;
import com.yzm.sleep.utils.InterFaceUtilsClass.WeiBoUserInfoListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.cancleDoctorParamClass;
import com.yzm.sleep.utils.UploadUtil.OnUploadProcessListener;
/**
 * 社区模块相关接口类
 * @author Administrator
 *
 */
public class CommunityProcClass extends HttpDataTranUtils{
	private Context mContext = null;
	private String errMsgNone = "未知错误";
	private String errMsgException = "未知异常";

	public CommunityProcClass(Context context) {
		mContext = context;
	}
	/**
	 * 获取签名参数值
	 * @param interfaceName
	 * @return
	 */
	private String getRequestToken(String interfaceName){
		String encryption = Util.encryption(interfaceName + //接口名
				SleepUtils.getFormatedDateTime("yyyyMMdd", System.currentTimeMillis()) + //日期
				SleepConstants.NET_REQUEST_KEY);//加密密钥
		
		return encryption;
	}
	
	
	
	
	//=====================================================================================
	//1、我的小组  /api.php?mod=mygroup
	private String urlGetMyGroup = g_BaseSiteURL+"api.php?mod=mygroup";
	private InterfaceGetCommunityGroupCallBack m_InterfaceGetCommunityGroupCallBack;
	public void getMyGroup(GetCommunityGroupParamClass mParam,
			InterfaceGetCommunityGroupCallBack callBack) {
		m_InterfaceGetCommunityGroupCallBack = callBack;

		String newString = urlGetMyGroup + "&" + "my_int_id="
				+ mParam.my_int_id +
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&xctoken=" + getRequestToken("mygroup");
		super.requestJosnObjectData(mContext, newString);

	}
	private void getMyGroupRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4500")) {
				m_InterfaceGetCommunityGroupCallBack.onError(4500,"用户id不存在或者格式错误");
			} else if (idValue.equals("4501")) {
				m_InterfaceGetCommunityGroupCallBack.onError(4501,"失败");
			}else if (idValue.equals("4502")) {
				Gson gson=new Gson();
				String string = (String) response.get("group_list").toString();
				List<CommunityGrougsBean> grouupList = gson.fromJson(string, new TypeToken<List<CommunityGrougsBean>>(){}.getType());
				int totalpage = 1;
				if(!response.getString("totalpage").equals("null"))
					totalpage = Integer.parseInt(response.getString("totalpage"));
				m_InterfaceGetCommunityGroupCallBack.onSuccess(4502,grouupList, totalpage);
			}else {
				m_InterfaceGetCommunityGroupCallBack.onError(0,errMsgNone);
			}
		}catch (Exception e) {
			m_InterfaceGetCommunityGroupCallBack.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//2、添加小组   /api.php?mod=addgroup
	private String addGroupUrlNew = 	g_BaseSiteURL+"api.php?";
	private InterfaceAddCommunityGroupCallBack m_InterfaceAddCommunityGroupCallBack;
	public void addGroup(AddCommunityGroupParamClass mParam,
			InterfaceAddCommunityGroupCallBack callBack) {
		m_InterfaceAddCommunityGroupCallBack = callBack;

		RequestParams params = new RequestParams();
		params.put("mod", "addgroup");
		params.put("my_int_id", mParam.my_int_id);
		params.put("g_type", mParam.g_type);
		params.put("g_ico", mParam.g_ico);
		params.put("g_name", mParam.g_name);
		params.put("g_intro", mParam.g_intro);
		params.put("g_tag", new Gson().toJson(mParam.g_tag));
		params.put("xctoken", getRequestToken("addgroup"));
		super.postJosnObjectData(mContext, addGroupUrlNew, params);

	}
	private void addGroupRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
			
		
			if (idValue.equals("4503")) {

				m_InterfaceAddCommunityGroupCallBack.onError(4503,"用户id不存在或者格式错误");

			} else if (idValue.equals("4504")) {

				m_InterfaceAddCommunityGroupCallBack.onError(4504,"参数错误");

			} else if (idValue.equals("4504:1")) {

				m_InterfaceAddCommunityGroupCallBack.onError(45041,"24小时内用户已经创建了小组");

			} else if (idValue.equals("4504:2")) {

				m_InterfaceAddCommunityGroupCallBack.onError(45042,"小组名称已经存在");

			}else if (idValue.equals("4505")) {

				m_InterfaceAddCommunityGroupCallBack.onError(4505,"失败");

			}else if (idValue.equals("4506")) {
				String gid = (String) response.get("gid").toString();
				String g_ico_url = (String) response.get("g_ico_url").toString();
				m_InterfaceAddCommunityGroupCallBack.onSuccess(4506, "成功",gid,g_ico_url);
			}else {
				m_InterfaceAddCommunityGroupCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceAddCommunityGroupCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceAddCommunityGroupCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//3、话题列表 /api.php?mod=threadlist
	private String getCommunityTopicUrl = 	g_BaseSiteURL+"api.php?mod=threadlist";
	private InterfaceGetCommunityTopicCallBack m_InterfaceGetCommunityTopicCallBack;
	public void getCommunityTopic(GetCommunityTopicParamClass mParam,
			InterfaceGetCommunityTopicCallBack callBack) {
		m_InterfaceGetCommunityTopicCallBack = callBack;

		String newString = getCommunityTopicUrl + "&" + "gid="
				+ mParam.gid +
				"&my_int_id=" + mParam.my_int_id + 
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&page_id=" + mParam.page_id + 
				"&xctoken=" + getRequestToken("threadlist");
		super.requestJosnObjectData(mContext, newString);

	}
	private void getCommunityTopicRstProc(JSONObject response) {
		String idValue = "";

		try {
			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4507")) {

				m_InterfaceGetCommunityTopicCallBack.onError(4507,"小组id不存在或者格式错误");

			}else if (idValue.equals("4507:1")) {

				m_InterfaceGetCommunityTopicCallBack.onError(45071,"用户id不存在或者格式错误");

			} else if (idValue.equals("4508")) {

				m_InterfaceGetCommunityTopicCallBack.onError(4508,"失败");

			}else if (idValue.equals("4509")) {
				Gson gson=new Gson();
				String string = (String) response.get("thread_list").toString();
				List<CommunityTopicBean> list = gson.fromJson(string, new TypeToken<List<CommunityTopicBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				String page_id = (String) response.getString("page_id");
				m_InterfaceGetCommunityTopicCallBack.onSuccess(4509, list, totalpage,page_id);
			}else {
				m_InterfaceGetCommunityTopicCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceGetCommunityTopicCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceGetCommunityTopicCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetCommunityTopicCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//4、话题 详情 /api.php?mod=threaddetail
	
	private String getCommunityTopicDetailUrl = 	g_BaseSiteURL+"api.php?mod=threaddetail";
	private InterfaceGetCommunityTopicDetailCallBack m_InterfaceGetCommunityTopicDetailCallBack;
	public void getCommunityTopicDetail(GetCommunityTopicDetailParamClass mParam,
			InterfaceGetCommunityTopicDetailCallBack callBack) {
		m_InterfaceGetCommunityTopicDetailCallBack = callBack;

		String newString = getCommunityTopicDetailUrl + "&" + "tid="
				+ mParam.tid +
				"&my_int_id=" + mParam.my_int_id + 
				"&xctoken=" + getRequestToken("threaddetail");
		super.requestJosnObjectData(mContext, newString);

	}
	private void getCommunityTopicDetailRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4510")) {
				m_InterfaceGetCommunityTopicDetailCallBack.onError(4510,"话题id不存在或者格式错误");
			} else if (idValue.equals("4511")) {
				m_InterfaceGetCommunityTopicDetailCallBack.onError(4511,"失败");
			}else if (idValue.equals("4512")) {
//				Gson gson=new Gson();
//				String string = (String) response.get("datas").toString();
//				CommunityTopicBean info = gson.fromJson(string, CommunityTopicBean.class);
				ArticleDetailBean bean=new ArticleDetailBean();
				String string = (String) response.getString("datas");
				JSONObject json=new JSONObject(string);
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
				bean.setIntro(json.getString("t_message"));
				
				JSONArray zanuser = json.getJSONArray("t_zan_user");
				List<ArticleThumbUpUserBean> t_zan_user=new ArrayList<ArticleThumbUpUserBean>();
				for(int i=0;i< (zanuser==null ? 0: zanuser.length());i++){
					ArticleThumbUpUserBean beanz=new ArticleThumbUpUserBean();
					JSONObject jsonz=zanuser.getJSONObject(i);
					beanz.setUid(jsonz.getString("uid"));
					beanz.setNickname(jsonz.getString("nickname"));
					beanz.setAuthor_profile(jsonz.getString("author_profile"));
					beanz.setAuthor_profile_key(jsonz.getString("author_profile_key"));
					t_zan_user.add(beanz);
				}
				
				bean.setT_zan_user(t_zan_user);
				
				JSONArray jsonImage = json.getJSONArray("images");
				List<ArticleImageBean> images=new ArrayList<ArticleImageBean>();
				for (int i = 0; i < (jsonImage == null ? 0 :jsonImage.length()); i++) {
					ArticleImageBean img=new ArticleImageBean();
					JSONObject jsonImg = jsonImage.getJSONObject(i);
					img.setContent_attachment(jsonImg.getString("content_attachment"));
					img.setContent_attachment_sl(jsonImg.getString("content_attachment_sl"));
					img.setT_attachment_key(jsonImg.getString("t_attachment_key"));
					img.setT_attachment_key_sl(jsonImg.getString("t_attachment_key_sl"));
					images.add(img);
				}
				bean.setImages(images);
				
				m_InterfaceGetCommunityTopicDetailCallBack.onSuccess(4512, bean);
			}else {
				m_InterfaceGetCommunityTopicDetailCallBack.onError(0, errMsgNone);
			}
		}catch (Exception e) {
			m_InterfaceGetCommunityTopicDetailCallBack.onError(0, errMsgException);
		}
	}
	
	
	//=======================================================================================================
	//5、话题点赞   /api.php?mod=threadzan
	
	private String topicPraiseUrl = 	g_BaseSiteURL+"api.php?mod=threadzan";
	private InterfaceTopicPraiseCallBack m_InterfaceTopicPraiseCallBack;
	public void topicPraise(TopicPraiseParamClass mParam,
			InterfaceTopicPraiseCallBack callBack) {
		m_InterfaceTopicPraiseCallBack = callBack;

		String newString = topicPraiseUrl +
				"&tid=" + mParam.tid + 
				"&my_int_id=" + mParam.my_int_id + 
				"&xctoken=" + getRequestToken("threadzan")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void topicPraiseRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4513")) {

				m_InterfaceTopicPraiseCallBack.onError(4513,"话题id不存在或者格式错误");

			} else if (idValue.equals("4514")) {

				m_InterfaceTopicPraiseCallBack.onError(4514,"用户id不存在或者格式错误");

			}else if (idValue.equals("4514:1")) {

				m_InterfaceTopicPraiseCallBack.onError(45141,"其它错误");

			}else if (idValue.equals("4515")) {

				m_InterfaceTopicPraiseCallBack.onError(4515,"失败");

			}else if (idValue.equals("4516")) {
				m_InterfaceTopicPraiseCallBack.onSuccess(4516, "成功");
			}else {
				m_InterfaceTopicPraiseCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceTopicPraiseCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceTopicPraiseCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//6、话题删除  /api.php?mod=threaddel
	private String topicDeleteUrl = 	g_BaseSiteURL+"api.php?mod=threaddel";
	private InterfaceTopicDeleteCallBack m_InterfaceTopicDeleteCallBack;
	public void topicDelete(TopicDeleteParamClass mParam,
			InterfaceTopicDeleteCallBack callBack) {
		m_InterfaceTopicDeleteCallBack = callBack;

		String newString = topicDeleteUrl +
				"&tid=" + mParam.tid + 
				"&my_int_id=" + mParam.my_int_id + 
				"&xctoken=" + getRequestToken("threaddel")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void topicDeleteRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4517")) {

				m_InterfaceTopicDeleteCallBack.onError(4517,"话题id不存在或者格式错误");

			} else if (idValue.equals("4518")) {

				m_InterfaceTopicDeleteCallBack.onError(4518,"用户id不存在或者格式错误");

			}else if (idValue.equals("4518:1")) {

				m_InterfaceTopicDeleteCallBack.onError(45181,"其它错误");

			}else if (idValue.equals("4519")) {

				m_InterfaceTopicDeleteCallBack.onError(4519,"失败");

			}else if (idValue.equals("4520")) {
				m_InterfaceTopicDeleteCallBack.onSuccess(4520, "成功");
			}else {
				m_InterfaceTopicDeleteCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceTopicDeleteCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceTopicDeleteCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//7、话题发布/api.php?mod=threadpost
	
	private String topicPostNewUrl1 = 	g_BaseSiteURL+"api.php?";
	private InterfaceTopicPostNewCallBack m_InterfaceTopicPostNewCallBack;
	public void topicPostNew(TopicPostNewParamClass mParam,
			InterfaceTopicPostNewCallBack callBack) {
		m_InterfaceTopicPostNewCallBack = callBack;

		RequestParams params = new RequestParams();
		params.put("mod", "threadpost");
		params.put("my_int_id", mParam.my_int_id);
		params.put("gid", mParam.gid);
		params.put("t_message", mParam.t_message);
		params.put("t_attachment_key", ((mParam.t_attachment_key==null)?(""):mParam.t_attachment_key));
		params.put("t_subject", (mParam.t_subject==null)?"":mParam.t_subject);
		params.put("t_tag", (mParam.t_tag==null)?"":mParam.t_tag);
		params.put("xctoken", getRequestToken("threadpost"));
		super.postJosnObjectData(mContext, topicPostNewUrl1, params);

	}
	private void topicPostNewRstProc(JSONObject response) {
		String idValue = "";

		try {
			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4521")) {

				m_InterfaceTopicPostNewCallBack.onError(4521,"用户id不存在或者格式错误");

			} else if (idValue.equals("4522")) {

				m_InterfaceTopicPostNewCallBack.onError(4522,"小组id不存在或者格式错误");

			}else if (idValue.equals("4523")) {

				m_InterfaceTopicPostNewCallBack.onError(4523,"文字内容不能为空");

			}else if (idValue.equals("4524")) {

				m_InterfaceTopicPostNewCallBack.onError(4524,"失败");

			}else if (idValue.equals("4525")) {
				String tid = (String) response.get("tid").toString();
				String url = response.get("url").toString();
				String url_original = response.get("url_original").toString();
				m_InterfaceTopicPostNewCallBack.onSuccess(4525, "成功", tid, url, url_original);
			}else {
				m_InterfaceTopicPostNewCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceTopicPostNewCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceTopicPostNewCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//8、回复发布  /api.php?mod=plpost
	
	private String topicReplyPostUrl = 	g_BaseSiteURL+"api.php?";
	private InterfaceTopicReplyPostNewCallBack m_InterfaceTopicReplyPostNewCallBack;
	public void topicReplyPost(TopicReplyPostNewParamClass mParam,
			InterfaceTopicReplyPostNewCallBack callBack) {
		m_InterfaceTopicReplyPostNewCallBack = callBack;

		RequestParams params = new RequestParams();
		params.put("mod", "plpost");
		params.put("my_int_id", mParam.my_int_id);
		params.put("tid", mParam.tid);
		params.put("p_message", mParam.p_message);
		params.put("p_topid", mParam.pid);
		params.put("p_touid", mParam.p_touid);
		params.put("xctoken", getRequestToken("plpost"));
		super.postJosnObjectData(mContext, topicReplyPostUrl, params);
	}
	private void topicReplyPostRstProc(JSONObject response) {
		String idValue = "";
		String pid="";
		try {

			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4526")) {

				m_InterfaceTopicReplyPostNewCallBack.onError(4526,"用户id不存在或者格式错误");

			} else if (idValue.equals("4527")) {

				m_InterfaceTopicReplyPostNewCallBack.onError(4527,"话题id不存在或者格式错误");

			}else if (idValue.equals("4528")) {

				m_InterfaceTopicReplyPostNewCallBack.onError(4528,"文字内容不能为空");

			}else if (idValue.equals("4529")) {

				m_InterfaceTopicReplyPostNewCallBack.onError(4529,"其它参数错误");

			}else if (idValue.equals("4530")) {

				m_InterfaceTopicReplyPostNewCallBack.onError(4530,"失败");

			}else if (idValue.equals("4531")) {
				pid=response.getString("pid").toString();
				m_InterfaceTopicReplyPostNewCallBack.onSuccess(4531,"成功",pid);
			}else {
				m_InterfaceTopicReplyPostNewCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceTopicReplyPostNewCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceTopicReplyPostNewCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//9、小组信息     /api.php?mod=groupinfo
	private String getGroupMessageUrl = 	g_BaseSiteURL+"api.php?mod=groupinfo";
	private InterfaceGetGroupMessageCallBack m_InterfaceGetGroupMessageCallBack;
	public void getGroupMessage(GetGroupMessageParamClass mParam,
			InterfaceGetGroupMessageCallBack callBack) {
		m_InterfaceGetGroupMessageCallBack = callBack;

		String newString = getGroupMessageUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&gid=" + mParam.gid + 
				"&xctoken=" + getRequestToken("groupinfo")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void getGroupMessageRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4532")) {

				m_InterfaceGetGroupMessageCallBack.onError(4532,"用户id不存在或者格式错误");

			} else if (idValue.equals("4533")) {

				m_InterfaceGetGroupMessageCallBack.onError(4533,"话题id不存在或者格式错误");

			}else if (idValue.equals("4534")) {

				m_InterfaceGetGroupMessageCallBack.onError(4534,"失败");

			}else if (idValue.equals("4535")) {
				Gson gson=new Gson();
				String string = (String) response.get("datas").toString();
				List<GroupMessageBean> list = new ArrayList<GroupMessageBean>();
				GroupMessageBean mGroupMessageBean = gson.fromJson(string, GroupMessageBean.class);
				list.add(mGroupMessageBean);
				m_InterfaceGetGroupMessageCallBack.onSuccess(4535, list);
			}else{
				m_InterfaceGetGroupMessageCallBack.onError(0,errMsgNone);
			}
		} catch (Exception e) {
			m_InterfaceGetGroupMessageCallBack.onError(0,errMsgException);
			e.printStackTrace();
		}
	}

	//=======================================================================================================
	//10、小组医生列表     /api.php?mod=doctorslist
	private String getGroupDoctorListUrl = 	g_BaseSiteURL+"api.php?mod=doctorslist";
	private InterfaceGetGroupDoctorListCallBack m_InterfaceGetGroupDoctorListCallBack;
	public void getGroupDoctorList(GetGroupDoctorListParamClass mParam,
			InterfaceGetGroupDoctorListCallBack callBack) {
		m_InterfaceGetGroupDoctorListCallBack = callBack;

		String newString = getGroupDoctorListUrl +
				"&gid=" + mParam.gid + 
				"&my_int_id="+mParam.my_int_id +
				"&page=" + mParam.page +
				"&pagesize=" + mParam.pagesize +
				"&page_id=" + mParam.page_id +
				"&xctoken=" + getRequestToken("doctorslist")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void getGroupDoctorListRstProc(JSONObject response) {
		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4539")) {

				m_InterfaceGetGroupDoctorListCallBack.onError(4539,"用户id不存在或者格式错误");

			} else if (idValue.equals("4540")) {

				m_InterfaceGetGroupDoctorListCallBack.onError(4540,"话题id不存在或者格式错误");

			}else if (idValue.equals("4541")) {
				Gson gson=new Gson();
				String string = (String) response.get("doctor_list").toString();
				List<CommunityGroupDoctorBean> list = gson.fromJson(string, new TypeToken<List<CommunityGroupDoctorBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				String is_doctor = response.getString("is_doctor");
				String page_id = (String) response.getString("page_id");
				m_InterfaceGetGroupDoctorListCallBack.onSuccess(4541, list,totalpage,is_doctor,page_id);
			}else {
				m_InterfaceGetGroupDoctorListCallBack.onError(0, errMsgNone);
			}
		} catch (Exception e) {
			m_InterfaceGetGroupDoctorListCallBack.onError(0, errMsgException);
		} 
	}
	
	
	//=======================================================================================================
	//11、添加医生     /api.php?mod=doctorsadd
	
	private String addGroupDoctorUrl = 	g_BaseSiteURL+"api.php?mod=doctorsadd";
	private InterfaceAddGroupDoctorCallBack m_InterfaceAddGroupDoctorCallBack;
	public void addGroupDoctor(AddGroupDoctorParamClass mParam,
			InterfaceAddGroupDoctorCallBack callBack) {
		m_InterfaceAddGroupDoctorCallBack = callBack;

		String newString = addGroupDoctorUrl +
				"&gid=" + mParam.gid +
				"&my_int_id=" + mParam.my_int_id +
				"&i_telephone=" + mParam.i_telephone + 
				"&xctoken=" + getRequestToken("doctorsadd")
				;

		super.requestJosnObjectData(mContext, newString);

	}
	private void addGroupDoctorRstProc(JSONObject response) {
		String idValue = "";

		try {
			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4542")) {

				m_InterfaceAddGroupDoctorCallBack.onError(4542,"小组id不存在或者格式错误");

			} else if (idValue.equals("4543")) {

				m_InterfaceAddGroupDoctorCallBack.onError(4543,"电话号码为空或者格式错误");

			}else if (idValue.equals("4544")) {

				m_InterfaceAddGroupDoctorCallBack.onError(4544,"失败");

			}else if (idValue.equals("4545")) {
				Gson gson=new Gson();
				String string = (String) response.get("datas").toString();
				SpecialistBean mSpecialistBean = gson.fromJson(string, SpecialistBean.class);
				m_InterfaceAddGroupDoctorCallBack.onSuccess(4545, "成功", mSpecialistBean);
			}else if(idValue.equals("4544:1")){
				m_InterfaceAddGroupDoctorCallBack.onError(4544,"已经邀请，不能重复邀请");
			}else if(idValue.equals("4544:2")){
				m_InterfaceAddGroupDoctorCallBack.onError(4544,"自己不能邀请自己为医生");
			}else {
				m_InterfaceAddGroupDoctorCallBack.onError(0, errMsgNone);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceAddGroupDoctorCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//12、搜索小组话题    /api.php?mod=xcsearch
	private String searchGroupTopicUrl = 	g_BaseSiteURL+"api.php?mod=xcsearch";
	private InterfaceSearchGroupTopicCallBack m_InterfaceSearchGroupTopicCallBack;
	public void searchGroupTopic(SearchGroupTopicParamClass mParam,
			InterfaceSearchGroupTopicCallBack callBack) {
		m_InterfaceSearchGroupTopicCallBack = callBack;

//		String newString = searchGroupTopicUrl +
//				"&xckey=" + mParam.xckey +
//				"&my_int_id=" + mParam.my_int_id+
//				"&xctoken=" + getRequestToken("xcsearch")
//				;
//
//		super.requestJosnObjectData(mContext, newString);
		RequestParams params = new RequestParams();
		params.put("xckey", mParam.xckey);
		params.put("my_int_id", mParam.my_int_id);
		params.put("xctoken", getRequestToken("xcsearch"));
		super.postJosnObjectData(mContext, searchGroupTopicUrl, params);

	}
	private void searchGroupTopicRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4546")) {

				m_InterfaceSearchGroupTopicCallBack.onError(4546,"用户id不存在或者格式错误");

			} else if (idValue.equals("4547")) {

				m_InterfaceSearchGroupTopicCallBack.onError(4547,"失败");

			}else if (idValue.equals("4548")) {
				Gson gson=new Gson();
				String group_list = (String) response.get("group_list").toString();
				String thread_list = (String) response.get("thread_list").toString();
				List<CommunityGroupBean> groupList = gson.fromJson(group_list, new TypeToken<List<CommunityGroupBean>>(){}.getType());
				List<SearchTopicBean> topicList = gson.fromJson(thread_list, new TypeToken<List<SearchTopicBean>>(){}.getType());
				m_InterfaceSearchGroupTopicCallBack.onSuccess(4548, groupList, topicList);
			}else{
				m_InterfaceSearchGroupTopicCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceSearchGroupTopicCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceSearchGroupTopicCallBack.onError(0, errMsgException);
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceSearchGroupTopicCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//13、举报    /api.php?mod=xcreport
	private String reportTopicUrl = 	g_BaseSiteURL+"api.php?mod=xcreport";
	private InterfaceReportTopicCallBack m_InterfaceReportTopicCallBack;
	public void reportTopic(ReportTopicParamClass mParam,
			InterfaceReportTopicCallBack callBack) {
		m_InterfaceReportTopicCallBack = callBack;

		String newString = reportTopicUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&tid=" + mParam.tid + 
				"&report=" + mParam.report + 
				"&xctoken=" + getRequestToken("xcreport")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void reportTopicRstProc(JSONObject response) {
		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4549")) {

				m_InterfaceReportTopicCallBack.onError(4549,"用户id不存在或者格式错误");

			} else if (idValue.equals("4550")) {

				m_InterfaceReportTopicCallBack.onError(4550,"话题id不存在或者格式错误");

			}else if (idValue.equals("4551")) {

				m_InterfaceReportTopicCallBack.onError(4551,"失败");

			}else if (idValue.equals("4552")) {
				m_InterfaceReportTopicCallBack.onSuccess(4552, "成功");

			}else {
				m_InterfaceReportTopicCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceReportTopicCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceReportTopicCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//14、编辑小组简介   /api.php?mod=groupedit
	
	private String editGroupSummaryUrl = 	g_BaseSiteURL+"api.php?mod=groupedit";
	private InterfaceEditGroupSummaryCallBack m_InterfaceEditGroupSummaryCallBack;
	public void editGroupSummary(EditGroupSummaryParamClass mParam,
			InterfaceEditGroupSummaryCallBack callBack) {
		m_InterfaceEditGroupSummaryCallBack = callBack;

		String newString = editGroupSummaryUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&gid=" + mParam.gid + 
				"&g_intro=" + mParam.g_intro + 
				"&xctoken=" + getRequestToken("groupedit")
				;

		super.requestJosnObjectData(mContext, newString);

	}
	private void editGroupSummaryRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4553")) {

				m_InterfaceEditGroupSummaryCallBack.onError(4553,"用户id不存在或者格式错误");

			} else if (idValue.equals("4554")) {

				m_InterfaceEditGroupSummaryCallBack.onError(4554,"小组id不存在或者格式错误");

			}else if (idValue.equals("4555")) {

				m_InterfaceEditGroupSummaryCallBack.onError(4555,"失败");

			}else if (idValue.equals("4556")) {
				m_InterfaceEditGroupSummaryCallBack.onSuccess(4556, "成功");

			}else {
				m_InterfaceEditGroupSummaryCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceEditGroupSummaryCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceEditGroupSummaryCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//15、关注小组  /api.php?mod=groupfocus
	private String attentionGroupUrl = 	g_BaseSiteURL+"api.php?mod=groupfocus";
	private InterfaceAttentionGroupCallBack m_InterfaceAttentionGroupCallBack;
	public void attentionGroup(AttentionGroupParamClass mParam,
			InterfaceAttentionGroupCallBack callBack) {
		m_InterfaceAttentionGroupCallBack = callBack;

		String newString = attentionGroupUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&gid=" + mParam.gid + 
				"&xctoken=" + getRequestToken("groupfocus")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void attentionGroupRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
			String message = (String) response.get("message").toString();
		
			if (idValue.equals("4557")) {

				m_InterfaceAttentionGroupCallBack.onError(4557,message);//用户id不存在或者格式错误

			} else if (idValue.equals("4558")) {

				m_InterfaceAttentionGroupCallBack.onError(4558,message);//小组id不存在或者格式错误

			}else if (idValue.equals("4559")) {

				m_InterfaceAttentionGroupCallBack.onError(4559,message);//失败

			}else if (idValue.equals("4560")) {
				m_InterfaceAttentionGroupCallBack.onSuccess(4560,"关注成功");//成功

			}else{
				m_InterfaceAttentionGroupCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceAttentionGroupCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceAttentionGroupCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//16、发现列表  /api.php?mod=foundlist
	private String communityFoundUrl = 	g_BaseSiteURL+"api.php?mod=foundlist";
	private InterfaceCommunityFoundCallBack m_InterfaceCommunityFoundCallBack;
	public void communityFound(CommunityFoundParamClass mParam,
			InterfaceCommunityFoundCallBack callBack) {
		m_InterfaceCommunityFoundCallBack = callBack;

		String newString = communityFoundUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&page_id=" + mParam.page_id + 
				"&xctoken=" + getRequestToken("foundlist")
				;

		super.requestJosnObjectData(mContext, newString);

	}
	private void communityFoundRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4561")) {

				m_InterfaceCommunityFoundCallBack.onError(4561,"用户id不存在或者格式错误");

			} else if (idValue.equals("4562")) {

				m_InterfaceCommunityFoundCallBack.onError(4562,"失败");

			}else if (idValue.equals("4563")) {
				Gson gson=new Gson();
				String string = (String) response.get("thread_list").toString();
				List<CommunityTopicBean> list = gson.fromJson(string, new TypeToken<List<CommunityTopicBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				String page_id = (String) response.getString("page_id");
				m_InterfaceCommunityFoundCallBack.onSuccess(4502, list, totalpage,page_id);

			}else{
				m_InterfaceCommunityFoundCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceCommunityFoundCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceCommunityFoundCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceCommunityFoundCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//17、我的话题列表(按时间)  /api.php?mod=mythreadt
	
	
	private String myTopicByTimeUrl = 	g_BaseSiteURL+"api.php?mod=mythreadt";
	private InterfaceMyTopicByTimeCallBack m_InterfaceMyTopicByTimeCallBack;
	public void myTopicByTime(MyTopicByTimeParamClass mParam,
			InterfaceMyTopicByTimeCallBack callBack) {
		m_InterfaceMyTopicByTimeCallBack = callBack;

		String newString = myTopicByTimeUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&other_int_id=" + mParam.other_int_id + 
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&xctoken=" + getRequestToken("mythreadt")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void myTopicByTimeRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4564")) {

				m_InterfaceMyTopicByTimeCallBack.onError(4564,"用户id不存在或者格式错误");

			} else if (idValue.equals("4565")) {

				m_InterfaceMyTopicByTimeCallBack.onError(4565,"失败");

			}else if (idValue.equals("4566")) {
				Gson gson=new Gson();
				String string = (String) response.get("thread_list").toString();
				List<CommunityTopicBean> list = gson.fromJson(string, new TypeToken<List<CommunityTopicBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				m_InterfaceMyTopicByTimeCallBack.onSuccess(4566, list, totalpage);
			}else {
				m_InterfaceMyTopicByTimeCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceMyTopicByTimeCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceMyTopicByTimeCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceMyTopicByTimeCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//18、我的话题列表(按小组)  /api.php?mod=mythreadg
	private String myTopicByGroupUrl = 	g_BaseSiteURL+"api.php?mod=mythreadg";
	private InterfaceMyTopicByGroupCallBack m_InterfaceMyTopicByGroupCallBack;
	public void myTopicByGroup(MyTopicByGroupParamClass mParam,
			InterfaceMyTopicByGroupCallBack callBack) {
		m_InterfaceMyTopicByGroupCallBack = callBack;

		String newString = myTopicByGroupUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&xctoken=" + getRequestToken("mythreadg")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void myTopicByGroupRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4567")) {

				m_InterfaceMyTopicByGroupCallBack.onError(4567,"用户id不存在或者格式错误");

			} else if (idValue.equals("4568")) {

				m_InterfaceMyTopicByGroupCallBack.onError(4568,"失败");

			}else if (idValue.equals("4569")) {
				Gson gson=new Gson();
				String string = (String) response.get("group_list").toString();
				List<CommunityGrougsBean> list = gson.fromJson(string, new TypeToken<List<CommunityGrougsBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				m_InterfaceMyTopicByGroupCallBack.onSuccess(4569, list, totalpage);
			}else {
				m_InterfaceMyTopicByGroupCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceMyTopicByGroupCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceMyTopicByGroupCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceMyTopicByGroupCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//19、标签列表 /api.php?mod=taglist
	private String getTagListUrl = 	g_BaseSiteURL+"api.php?mod=taglist";
	private InterfaceGetTagListCallBack m_InterfaceGetTagListCallBack;
	public void addNewTag(GetTagListParamClass mParam,
			InterfaceGetTagListCallBack callBack) {
		m_InterfaceGetTagListCallBack = callBack;
		
		StringBuffer sb = new StringBuffer();
		sb.append(getTagListUrl);
		
		if (!TextUtils.isEmpty(mParam.gid)) {
			sb.append("&gid=" + mParam.gid);
		}
		if (!TextUtils.isEmpty(mParam.tid)) {
			sb.append("&tid=" + mParam.tid);
		}
		
		sb.append("&xctoken=" + getRequestToken("taglist"));
		String newString = sb.toString();

		super.requestJosnObjectData(mContext, newString);

	}
	private void getTagListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4570")) {

				m_InterfaceGetTagListCallBack.onError(4570,"参数错误");

			} else if (idValue.equals("4571")) {

				m_InterfaceGetTagListCallBack.onError(4571,"失败");

			}else if (idValue.equals("4572")) {
				Gson gson=new Gson();
				String tagString = (String) response.get("tag_list").toString();
				String hotString = (String) response.get("hot_list").toString();
				List<TagBean> tagList = gson.fromJson(tagString, new TypeToken<List<TagBean>>(){}.getType());
				List<TagBean> hotList = gson.fromJson(hotString, new TypeToken<List<TagBean>>(){}.getType());
				m_InterfaceGetTagListCallBack.onSuccess(4572, tagList,hotList);
			}else {
				m_InterfaceGetTagListCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceGetTagListCallBack.onError(0, errMsgException);
		}catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceGetTagListCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetTagListCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//20、删除标签    /api.php?mod=tagdel
	
	private String deleteTagUrl = 	g_BaseSiteURL+"api.php?mod=tagdel";
	private InterfaceDeleteTagCallBack m_InterfaceDeleteTagCallBack;
	public void deleteTag(DeleteTagParamClass mParam,
			InterfaceDeleteTagCallBack callBack) {
		m_InterfaceDeleteTagCallBack = callBack;
		
		StringBuffer sb = new StringBuffer();
		sb.append(deleteTagUrl);
		sb.append("&tagid="+mParam.tagid);
		if (!TextUtils.isEmpty(mParam.gid)) {
			sb.append("&gid=" + mParam.gid);
		}
		if (!TextUtils.isEmpty(mParam.tid)) {
			sb.append("&tid=" + mParam.tid);
		}
		sb.append("&my_in_id="+mParam.my_in_id);
		sb.append("&xctoken=" + getRequestToken("tagdel"));
		
		String newString = sb.toString();

		super.requestJosnObjectData(mContext, newString);

	}
	private void deleteTagRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4573")) {

				m_InterfaceDeleteTagCallBack.onError(4573,"标签id不存在或者格式错误");

			} else if (idValue.equals("4574")) {

				m_InterfaceDeleteTagCallBack.onError(4574,"用户id不存在或者格式错误");

			}else if (idValue.equals("4575")) {

				m_InterfaceDeleteTagCallBack.onError(4575,"失败");

			}else if (idValue.equals("4576")) {
				m_InterfaceDeleteTagCallBack.onSuccess(4576, "成功");

			}else {
				m_InterfaceDeleteTagCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceDeleteTagCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceDeleteTagCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//21、热门标签列表    /api.php?mod=taghot
	
	private String getHotTagUrl = 	g_BaseSiteURL+"api.php?mod=taghot";
	private InterfaceGetHotTagCallBack m_InterfaceGetHotTagCallBack;
	public void getHotTag(InterfaceGetHotTagCallBack callBack) {
		m_InterfaceGetHotTagCallBack = callBack;
		String newString = getHotTagUrl + 
				"&xctoken=" + getRequestToken("taghot");
		super.requestJosnObjectData(mContext, newString);

	}
	private void getHotTagRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4577")) {

				m_InterfaceGetHotTagCallBack.onError(4577,"失败");

			}else if (idValue.equals("4578")) {
				Gson gson=new Gson();
				String string = (String) response.get("hot_list").toString();
				List<HotTagBean> list = gson.fromJson(string, new TypeToken<List<HotTagBean>>(){}.getType());
				m_InterfaceGetHotTagCallBack.onSuccess(4578, list);

			}else {
				m_InterfaceGetHotTagCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceGetHotTagCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceGetHotTagCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetHotTagCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//22、取消关注小组    /api.php?mod=groupqxfocus
	
	private String cancelAttentionUrl = g_BaseSiteURL+"api.php?mod=groupqxfocus";
	private InterfaceCancelAttentionGroupCallBack m_InterfaceCancelAttentionGroupCallBack;
	public void cancelAttentionGroup(CancelAttentionGroupParamClass mParam,
			InterfaceCancelAttentionGroupCallBack callBack) {
		m_InterfaceCancelAttentionGroupCallBack = callBack;
		
		String newString = cancelAttentionUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&gid=" + mParam.gid + 
				"&xctoken=" + getRequestToken("groupqxfocus")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void cancelAttentionRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4579")) {

				m_InterfaceCancelAttentionGroupCallBack.onError(4579,"登录用户id不存在或者格式错误");

			}else if (idValue.equals("4580")) {

				m_InterfaceCancelAttentionGroupCallBack.onError(4580,"小组id不存在或者格式错误");

			}else if (idValue.equals("4581")) {

				m_InterfaceCancelAttentionGroupCallBack.onError(4581,"失败");

			}else if (idValue.equals("4582")) {
				m_InterfaceCancelAttentionGroupCallBack.onSuccess(4582, "成功");

			}else {
				m_InterfaceCancelAttentionGroupCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceCancelAttentionGroupCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceCancelAttentionGroupCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//23、首页推荐点击没兴趣    /api.php?mod=nointerest
	
	private String recommendRefuseUrl = g_BaseSiteURL+"api.php?mod=nointerest";
	private InterfaceCommunityRecommendRefuseCallBack m_InterfaceCommunityRecommendRefuseCallBack;
	public void recommendRefuse(CommunityRecommendRefuseParamClass mParam,
			InterfaceCommunityRecommendRefuseCallBack callBack) {
		m_InterfaceCommunityRecommendRefuseCallBack = callBack;
		
		String newString = recommendRefuseUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&gid=" + mParam.gid + 
				"&xctoken=" + getRequestToken("nointerest")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void recommendRefuseRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4583")) {

				m_InterfaceCommunityRecommendRefuseCallBack.onError(4583,"登录用户id不存在或者格式错误");

			}else if (idValue.equals("4584")) {

				m_InterfaceCommunityRecommendRefuseCallBack.onError(4584,"小组id不存在或者格式错误");

			}else if (idValue.equals("4585")) {

				m_InterfaceCommunityRecommendRefuseCallBack.onError(4585,"失败");

			}else if (idValue.equals("4586")) {
				m_InterfaceCommunityRecommendRefuseCallBack.onSuccess(4586, "成功");

			}else {
				m_InterfaceCommunityRecommendRefuseCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceCommunityRecommendRefuseCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceCommunityRecommendRefuseCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//24、判断登录用户是否能够创建小组  /api.php?mod=checkaddgroup
	
	private String checkUserCreateGroupUrl = g_BaseSiteURL+"api.php?mod=checkaddgroup";
	private InterfaceCheckUserCreateGroupCallBack m_InterfaceCheckUserCreateGroupCallBack;
	public void checkUserCreateGroup(CheckUserCreateGroupParamClass mParam,
			InterfaceCheckUserCreateGroupCallBack callBack) {
		m_InterfaceCheckUserCreateGroupCallBack = callBack;
		
		String newString = checkUserCreateGroupUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&xctoken=" + getRequestToken("checkaddgroup")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void checkUserCreateGroupRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4587")) {

				m_InterfaceCheckUserCreateGroupCallBack.onError(4587,"登录用户id不存在或者格式错误");

			}else if (idValue.equals("4588")) {

				m_InterfaceCheckUserCreateGroupCallBack.onError(4588,"不能添加，用户没有绑定手机号码");

			}else if (idValue.equals("4589")) {

				m_InterfaceCheckUserCreateGroupCallBack.onError(4589,"不能添加，用户24小时内只能创建一个小组");

			}else if (idValue.equals("4590")) {
				m_InterfaceCheckUserCreateGroupCallBack.onSuccess(4590, "可以添加");

			}else {
				m_InterfaceCheckUserCreateGroupCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceCheckUserCreateGroupCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceCheckUserCreateGroupCallBack.onError(0, errMsgException);
		}
	}
	
	
	//=======================================================================================================
	//25、搜索话题接口  /api.php?mod=xcsearcht
	private String searchTopicUrl = g_BaseSiteURL+"api.php?mod=xcsearcht";
	private InterfaceSearchTopicCallBack m_InterfaceSearchTopicCallBack;
	public void searchTopic(SearchTopicParamClass mParam,
			InterfaceSearchTopicCallBack callBack) {
		m_InterfaceSearchTopicCallBack = callBack;
		
//		String newString = searchTopicUrl +
//				"&xckey=" + mParam.xckey + 
//				"&page=" + mParam.page + 
//				"&pagesize=" + mParam.pagesize + 
//				"&page_id=" + mParam.page_id + 
//				"&xctoken=" + getRequestToken("xcsearcht")
//				;
//		super.requestJosnObjectData(mContext, newString);
		RequestParams params = new RequestParams();
		params.put("xckey", mParam.xckey);
		params.put("page", mParam.page);
		params.put("pagesize", mParam.pagesize);
		params.put("page_id", mParam.page_id);
		params.put("xctoken", getRequestToken("xcsearcht"));
		super.postJosnObjectData(mContext, searchTopicUrl, params);
	}
	private void searchTopicRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4591")) {

				m_InterfaceSearchTopicCallBack.onError(4591,"参数错误");

			}else if (idValue.equals("4592")) {

				m_InterfaceSearchTopicCallBack.onError(4592,"失败");

			}else if (idValue.equals("4593")) {
				Gson gson=new Gson();
				String string = (String) response.get("search_list").toString();
				List<SearchTopicBean> list = gson.fromJson(string, new TypeToken<List<SearchTopicBean>>(){}.getType());
				int totalpage = 1;
				try {
					totalpage = response.getInt("totalpage");
				} catch (Exception e) {
					e.printStackTrace();
				}
				String page_id = (String) response.getString("page_id");
				m_InterfaceSearchTopicCallBack.onSuccess(4593, list,totalpage,page_id);

			}else {
				m_InterfaceSearchTopicCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceSearchTopicCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceSearchTopicCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceSearchTopicCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//26、搜索小组接口  /api.php?mod=xcsearchg
	private String searchGroupUrl = g_BaseSiteURL+"api.php?mod=xcsearchg";
	private InterfaceSearchGroupCallBack m_InterfaceSearchGroupCallBack;
	public void searchGroup(SearchGroupParamClass mParam,
			InterfaceSearchGroupCallBack callBack) {
		m_InterfaceSearchGroupCallBack = callBack;
		
//		String newString = searchGroupUrl +
//				"&xckey=" + mParam.xckey + 
//				"&my_int_id="+mParam.my_int_id+
//				"&page=" + mParam.page + 
//				"&pagesize=" + mParam.pagesize + 
//				"&page_id=" + mParam.page_id + 
//				"&xctoken=" + getRequestToken("xcsearchg")
//				;
//		super.requestJosnObjectData(mContext, newString);
		RequestParams params = new RequestParams();
		params.put("xckey", mParam.xckey);
		params.put("my_int_id", mParam.my_int_id);
		params.put("page", mParam.page);
		params.put("pagesize", mParam.pagesize);
		params.put("page_id", mParam.page_id);
		params.put("xctoken", getRequestToken("xcsearchg"));
		super.postJosnObjectData(mContext, searchGroupUrl, params);

	}
	private void searchGroupRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4594")) {

				m_InterfaceSearchGroupCallBack.onError(4594,"参数错误");

			}else if (idValue.equals("4595")) {

				m_InterfaceSearchGroupCallBack.onError(4595,"失败");

			}else if (idValue.equals("4596")) {
				Gson gson=new Gson();
				String string = (String) response.get("search_list").toString();
				List<CommunityGroupBean> list = gson.fromJson(string, new TypeToken<List<CommunityGroupBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				String page_id = (String) response.getString("page_id");
				m_InterfaceSearchGroupCallBack.onSuccess(4596, list,totalpage,page_id);

			}else {
				m_InterfaceSearchGroupCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceSearchGroupCallBack.onError(0, errMsgException);
		}  catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceSearchGroupCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceSearchGroupCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//27、评论列表接口  /api.php?mod=postlist
	private String getCommentsListUrl = g_BaseSiteURL+"api.php?mod=postlist";
	private InterfaceGetCommentLisCallBack m_InterfaceGetCommentLisCallBack;
	public void getCommentsList(GetCommentListParamClass mParam,
			InterfaceGetCommentLisCallBack callBack) {
		m_InterfaceGetCommentLisCallBack = callBack;
		
		String newString = getCommentsListUrl +
				"&tid=" + mParam.tid + 
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&xctoken=" + getRequestToken("postlist")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void getCommentsListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4597")) {

				m_InterfaceGetCommentLisCallBack.onError(4597,"话题id不存在或者格式错误");

			}else if (idValue.equals("4598")) {

				m_InterfaceGetCommentLisCallBack.onError(4598,"失败");

			}else if (idValue.equals("4599")) {
				Gson gson=new Gson();
				String string = (String) response.get("pl_list").toString();
				List<CommentBean> list = gson.fromJson(string, new TypeToken<List<CommentBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				m_InterfaceGetCommentLisCallBack.onSuccess(4599, list,totalpage);

			}else {
				m_InterfaceGetCommentLisCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceGetCommentLisCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceGetCommentLisCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetCommentLisCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//28、举报列表接口  /api.php?mod=reportlist
	private String getReportListUrl = g_BaseSiteURL+"api.php?mod=reportlist";
	private InterfaceGetResportLisCallBack m_InterfaceGetResportLisCallBack;
	public void getReportList(GetResportListParamClass mParam,
			InterfaceGetResportLisCallBack callBack) {
		m_InterfaceGetResportLisCallBack = callBack;
		
		String newString = getReportListUrl +
				"&gid=" + mParam.gid + 
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&page_id=" + mParam.page_id + 
				"&xctoken=" + getRequestToken("reportlist")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void getReportListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4600")) {

				m_InterfaceGetResportLisCallBack.onError(4600,"用户id不存在或者格式错误");

			}else if (idValue.equals("4601")) {

				m_InterfaceGetResportLisCallBack.onError(4601,"其它错误");

			}else if (idValue.equals("4602")) {

				m_InterfaceGetResportLisCallBack.onError(4602,"其它错误");

			}else if (idValue.equals("4603")) {
				Gson gson=new Gson();
				String string = (String) response.get("report_list").toString();
				List<ReportBean> list = gson.fromJson(string, new TypeToken<List<ReportBean>>(){}.getType());
			    int totalpage = response.getInt("totalpage");
			    String page_id = (String) response.getString("page_id");
				m_InterfaceGetResportLisCallBack.onSuccess(4603, list,totalpage,page_id);

			}else {
				m_InterfaceGetResportLisCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceGetResportLisCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceGetResportLisCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetResportLisCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//29、忽略举报接口  /api.php?mod=reporthl
	private String ignoreResportUrl = g_BaseSiteURL+"api.php?mod=reporthl";
	private InterfaceIgnoreResportCallBack m_InterfaceIgnoreResportCallBack;
	public void ignoreResport(IgnoreResportParamClass mParam,
			InterfaceIgnoreResportCallBack callBack) {
		m_InterfaceIgnoreResportCallBack = callBack;
		
		String newString = ignoreResportUrl +
				"&rid=" + mParam.rid + 
				"&my_int_id=" + mParam.my_int_id + 
				"&xctoken=" + getRequestToken("reporthl")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void ignoreResportRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4604")) {

				m_InterfaceIgnoreResportCallBack.onError(4604,"举报id不存在或者格式错误");

			}else if (idValue.equals("4605")) {

				m_InterfaceIgnoreResportCallBack.onError(4605,"用户id不存在或者格式错误");

			}else if (idValue.equals("4606")) {

				m_InterfaceIgnoreResportCallBack.onError(4606,"失败");

			}else if (idValue.equals("4607")) {
				m_InterfaceIgnoreResportCallBack.onSuccess(4607, "成功");

			}else {
				m_InterfaceIgnoreResportCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceIgnoreResportCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceIgnoreResportCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//30、删除举报接口  /api.php?mod=reportdel
	private String deleteResportUrl = g_BaseSiteURL+"api.php?mod=reportdel";
	private InterfaceDeleteResportCallBack m_InterfaceDeleteResportCallBack;
	public void deleteResport(DeleteResportParamClass mParam,
			InterfaceDeleteResportCallBack callBack) {
		m_InterfaceDeleteResportCallBack = callBack;
		
		String newString = deleteResportUrl +
				"&rid=" + mParam.rid + 
				"&my_int_id=" + mParam.my_int_id + 
				"&xctoken=" + getRequestToken("reportdel")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void deleteResportRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4608")) {

				m_InterfaceDeleteResportCallBack.onError(4608,"举报id不存在或者格式错误");

			}else if (idValue.equals("4609")) {

				m_InterfaceDeleteResportCallBack.onError(4609,"用户id不存在或者格式错误");

			}else if (idValue.equals("4610")) {

				m_InterfaceDeleteResportCallBack.onError(4610,"失败");

			}else if (idValue.equals("4611")) {
				m_InterfaceDeleteResportCallBack.onSuccess(4611, "成功");

			}else {
				m_InterfaceDeleteResportCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceDeleteResportCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceDeleteResportCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//31、消息列表接口  /api.php?mod=noticelist
	private String getAutoListUrl = g_BaseSiteURL+"api.php?mod=noticelist";
	private InterfaceGetAutoLisCallBack m_InterfaceGetAutoLisCallBack;
	public void getAutoList(GetAutoListParamClass mParam,
			InterfaceGetAutoLisCallBack callBack) {
		m_InterfaceGetAutoLisCallBack = callBack;
		
		String newString = getAutoListUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&page_id=" + mParam.page_id + 
				"&xctoken=" + getRequestToken("noticelist")
				;
		super.requestJosnObjectData(mContext, newString);
	}
	private void getAutoListRstProc(JSONObject response) {
		String idValue = "";
		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4612")) {

				m_InterfaceGetAutoLisCallBack.onError(4612,"用户id不存在或者格式错误");

			}else if (idValue.equals("4613")) {

				m_InterfaceGetAutoLisCallBack.onError(4613,"其它错误");

			}else if (idValue.equals("4614")) {
				Gson gson=new Gson();
				String string = (String) response.get("notice_list").toString();
				List<AutoMsgBean> list = gson.fromJson(string, new TypeToken<List<AutoMsgBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				String page_id = (String) response.getString("page_id");
				m_InterfaceGetAutoLisCallBack.onSuccess(4614, list,totalpage,page_id);

			}else {
				m_InterfaceGetAutoLisCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceGetAutoLisCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceGetAutoLisCallBack.onError(0, errMsgException);
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetAutoLisCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//32、清空动态接口  /api.php?mod=noticedel
	private String clearAutoListUrl = g_BaseSiteURL+"api.php?mod=noticedel";
	private InterfaceClearAutoLisCallBack m_InterfaceClearAutoLisCallBack;
	public void clearAutoList(ClearAutoListParamClass mParam,
			InterfaceClearAutoLisCallBack callBack) {
		m_InterfaceClearAutoLisCallBack = callBack;
		
		String newString = clearAutoListUrl +
				"&rid=" + mParam.rid + 
				"&my_int_id=" + mParam.my_int_id + 
				"&xctoken=" + getRequestToken("noticedel")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void clearAutoListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4615")) {

				m_InterfaceClearAutoLisCallBack.onError(4615,"用户id不存在或者格式错误");

			}else if (idValue.equals("4616")) {

				m_InterfaceClearAutoLisCallBack.onError(4616,"其它错误");

			}else if (idValue.equals("4617")) {
				m_InterfaceClearAutoLisCallBack.onSuccess(4617, "成功");

			}else {
				m_InterfaceClearAutoLisCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceClearAutoLisCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceClearAutoLisCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//33、添加标签  /api.php?mod=tagadd
	private String addTagUrl = g_BaseSiteURL+"api.php?mod=tagadd";
	private InterfaceAddTagCallBack m_InterfaceAddTagCallBack;
	public void clearAutoList(AddTagParamClass mParam,
			InterfaceAddTagCallBack callBack) {
		m_InterfaceAddTagCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(addTagUrl);
		if (!TextUtils.isEmpty(mParam.gid)) {
			sb.append("&gid=" + mParam.gid);
		}
		if (!TextUtils.isEmpty(mParam.tid)) {
			sb.append("&tid=" + mParam.tid);
		}
		sb.append("&my_int_id=" + mParam.my_int_id);
		sb.append("&tagname=" + mParam.tagname);
		sb.append("&xctoken=" + getRequestToken("tagadd"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);

	}
	private void addTagRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4618")) {

				m_InterfaceAddTagCallBack.onError(4618,"gid或者tid不存在");

			}else if (idValue.equals("4619")) {

				m_InterfaceAddTagCallBack.onError(4619,"用户id不存在或者格式错误");

			}else if (idValue.equals("4620")) {

				m_InterfaceAddTagCallBack.onError(4620,"标签内容不能为空");

			}else if (idValue.equals("4621")) {

				m_InterfaceAddTagCallBack.onError(4621,"只能发布者才能添加标签");

			}else if (idValue.equals("4622")) {
				m_InterfaceAddTagCallBack.onSuccess(4622, "成功");

			}else {
				m_InterfaceAddTagCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceAddTagCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceAddTagCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//34、用户未读动态数量接口 /api.php?mod=noticenum
	private String dynamicMsgNumUrl = g_BaseSiteURL+"api.php?mod=noticenum";
	private InterfaceDynamicMsgNumCallBack m_InterfaceDynamicMsgNumCallBack;
	public void getDynamicMsgNum(DynamicMsgNumParamClass mParam,
			InterfaceDynamicMsgNumCallBack callBack) {
		m_InterfaceDynamicMsgNumCallBack = callBack;
		String newString = dynamicMsgNumUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&xctoken=" + getRequestToken("noticenum")
				;
		super.requestJosnObjectData(mContext, newString);

	}
	private void dynamicMsgNumRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4623")) {

				m_InterfaceDynamicMsgNumCallBack.onError(4623,"用户id不存在或者格式错误");

			}else if (idValue.equals("4624")) {

				m_InterfaceDynamicMsgNumCallBack.onError(4624,"其它错误");

			}else if (idValue.equals("4625")) {
				int num = 0;
				try {
					num = response.getInt("nums");
				} catch (Exception e) {
					e.printStackTrace();
				}
				m_InterfaceDynamicMsgNumCallBack.onSuccess(4625, "成功", num);

			}else {
				m_InterfaceDynamicMsgNumCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceDynamicMsgNumCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceDynamicMsgNumCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//35、修改标签（删除或增加标签后点击确定统一提交） /api.php?mod=tagmodify
	
	private String modifyTagUrl = g_BaseSiteURL+"api.php?mod=tagmodify";
	private InterfaceModifyTagCallBack m_InterfaceModifyTagCallBack;
	public void clearAutoList(ModifyTagParamClass mParam,
			InterfaceModifyTagCallBack callBack) {
		m_InterfaceModifyTagCallBack = callBack;
		
		StringBuffer sb = new StringBuffer();
		sb.append(modifyTagUrl);
		if (!TextUtils.isEmpty(mParam.gid)) {
			sb.append("&gid=" + mParam.gid);
		}
		if (!TextUtils.isEmpty(mParam.tid)) {
			sb.append("&tid=" + mParam.tid);
		}
		sb.append("&my_int_id=" + mParam.my_int_id);
		sb.append("&tagname=" + mParam.tagname);
		sb.append("&xctoken=" + getRequestToken("tagadd"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);

	}
	private void modifyTagRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4626")) {

				m_InterfaceModifyTagCallBack.onError(4626,"gid或者tid不存在");

			}else if (idValue.equals("4627")) {

				m_InterfaceModifyTagCallBack.onError(4627,"用户id不存在或者格式错误");

			}else if (idValue.equals("4628")) {

				m_InterfaceModifyTagCallBack.onError(4628,"标签内容不能为空");

			}else if (idValue.equals("4629")) {

				m_InterfaceModifyTagCallBack.onError(4629,"只能发布者才能添加标签");

			}else if (idValue.equals("4630")) {
				m_InterfaceModifyTagCallBack.onSuccess(4630, "成功");

			}else {
				m_InterfaceModifyTagCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceModifyTagCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceModifyTagCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//36 小组医生删除   /api.php?mod=doctordel
	private String delDoctorUrl = g_BaseSiteURL+"api.php?mod=doctordel";
	private InterfaceDelGroupDoctorCallBack mInterfaceDelGroupDoctorCallBack;
	public void delGroupDoctor(DelGroupDoctorParamClass mParam,
			InterfaceDelGroupDoctorCallBack callBack) {
		mInterfaceDelGroupDoctorCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(delDoctorUrl);
		sb.append("&my_int_id=" + mParam.my_int_id);
		sb.append("&gid=" + mParam.gid);
		sb.append("&id=" + mParam.id);
		sb.append("&xctoken=" + getRequestToken("doctordel"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void delGroupDoctorRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4631")) {

				mInterfaceDelGroupDoctorCallBack.onError(4631,"登录用户id不存在或者格式错误");

			}else if (idValue.equals("4632")) {

				mInterfaceDelGroupDoctorCallBack.onError(4632,"小组id不存在或者格式错误");

			}else if (idValue.equals("4633")) {

				mInterfaceDelGroupDoctorCallBack.onError(4633,"医生id不能为空");

			}else if (idValue.equals("4634")) {

				mInterfaceDelGroupDoctorCallBack.onSuccess(4634,"成功");

			}else if (idValue.equals("4635")) {
				mInterfaceDelGroupDoctorCallBack.onError(4635, "只有小组管理员才能删除医生");

			}else {
				mInterfaceDelGroupDoctorCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceDelGroupDoctorCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceDelGroupDoctorCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//37、绑定手机  /api.php?mod=telphonebd
	private String boundPhoneUrl = g_BaseSiteURL+"api.php?mod=telphonebd";
	private InterfaceBoundPhoneCallBack mInterfaceBoundPhoneCallBack;
	public void boundPhone(BoundPhoneParamClass mParam,
			InterfaceBoundPhoneCallBack callBack) {
		mInterfaceBoundPhoneCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(boundPhoneUrl);
		sb.append("&my_phone_num=" + mParam.my_phone_num);
		sb.append("&verification_code=" + mParam.verification_code);
		sb.append("&target_int_id=" + mParam.target_int_id);
		sb.append("&pwd=" + mParam.pwd);
		sb.append("&xctoken=" + getRequestToken("telphonebd"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void telphonebdRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4635")) {
				
				mInterfaceBoundPhoneCallBack.onError(4635,"登录用户id不存在或者格式错误");
				
			}else if (idValue.equals("4636")) {
				
				mInterfaceBoundPhoneCallBack.onError(4636,"电话号码不对");
				
			}else if (idValue.equals("4637")) {
				
				mInterfaceBoundPhoneCallBack.onError(4637,"验证码不对");
				
			}else if (idValue.equals("4638")) {
				
				mInterfaceBoundPhoneCallBack.onError(4638,"该手机号已经注册过或与其他账户绑定过了");
				
			}else if (idValue.equals("4639")) {
				
				mInterfaceBoundPhoneCallBack.onError(4639,"该手机号与验证码不匹配");
				
			}else if (idValue.equals("4640")) {
				
				mInterfaceBoundPhoneCallBack.onError(4640,"验证码过期");
				
			}else if (idValue.equals("4641")) {
				
				mInterfaceBoundPhoneCallBack.onError(4641,"该目标账号已经绑定过一次手机号，不能重复绑定 ");
				
			}else if (idValue.equals("4642")) {
				mInterfaceBoundPhoneCallBack.onSuccess(4642, "绑定成功");
				
			}else {
				mInterfaceBoundPhoneCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceBoundPhoneCallBack.onError(0, errMsgException);
		} catch (Exception e) {
			e.printStackTrace();
			mInterfaceBoundPhoneCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//38退出医生/api.php?mod=doctortc
	private String cancleDoctorUrl = g_BaseSiteURL+"api.php?mod=doctortc";
	private InterfaceCancleDoctorCallBack mInterfaceCancleDoctorCallBack;
	public void cancleDoctor(cancleDoctorParamClass mParam,
			InterfaceCancleDoctorCallBack callBack) {
		mInterfaceCancleDoctorCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(cancleDoctorUrl);
		sb.append("&my_int_id=" + mParam.my_int_id);
		sb.append("&gid=" + mParam.gid);
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void cancleDoctorRstProc(JSONObject response){
			String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4643")) {
				
				mInterfaceCancleDoctorCallBack.onError(4643,"登录用户id不存在或者格式错误");
				
			}else if (idValue.equals("4644")) {
				
				mInterfaceCancleDoctorCallBack.onError(4644,"小组id不存在或者格式错误");
				
			}else if (idValue.equals("4645")) {
				
				mInterfaceCancleDoctorCallBack.onError(4645,"失败");
				
			}else if (idValue.equals("4646")) {
				
				mInterfaceCancleDoctorCallBack.onSuccess(4646, "成功");
				
			}else {
				mInterfaceCancleDoctorCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceCancleDoctorCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceCancleDoctorCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//39、发送短信 /api.php?mod=sendmessage
	private String sendMessageUrl = g_BaseSiteURL+"api.php?mod=sendmessage";
	private InterfaceSendMessageCallBack mInterfaceSendMessageCallBack;
	public void cancleDoctor(SendMessageParamClass mParam,
			InterfaceSendMessageCallBack callBack) {
		mInterfaceSendMessageCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(sendMessageUrl);
		sb.append("&nickname=" + mParam.nickname);
		sb.append("&g_name=" + mParam.g_name);
		sb.append("&phone_num=" + mParam.phone_num);
		sb.append("&xctoken=" + getRequestToken("sendmessage"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void sendMessageRstProc(JSONObject response){
			String idValue = "";
			String message = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			message = (String) response.get("message").toString();
			
			if (idValue.equals("4647")) {
				
				mInterfaceSendMessageCallBack.onError(4647,message);
				
			}else if (idValue.equals("4648")) {
				
				mInterfaceSendMessageCallBack.onError(4648,message);
				
			}else if (idValue.equals("4649")) {
				
				mInterfaceSendMessageCallBack.onError(4649,message);
				
			}else if (idValue.equals("4650")) {
				
				mInterfaceSendMessageCallBack.onSuccess(4650, message);
				
			}else {
				mInterfaceSendMessageCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceSendMessageCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceSendMessageCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//40、推荐小组列表 /api.php?mod=recogroup
	
	private String urlRecomGroupList = 	g_BaseSiteURL+"api.php?mod=recogroup";
	private InterfaceRecomGroupListCallBack m_InterfaceRecomGroupListCallBack;
	public void getRecomGroupList(RecomGroupListParamClass mParam,
			InterfaceRecomGroupListCallBack callBack) {
		m_InterfaceRecomGroupListCallBack = callBack;

		String newString = urlRecomGroupList + "&" + "my_int_id="
				+ mParam.my_int_id +
				"&xctoken=" + 
				getRequestToken("recogroup");

		super.requestJosnObjectData(mContext, newString);

	}
	private void getRecomGroupListRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4651")) {

				m_InterfaceRecomGroupListCallBack.onError(4651,"失败");

			}else if (idValue.equals("4652")) {
				Gson gson=new Gson();
				String string = (String) response.get("push_list").toString();
				List<CommunityGrougsBean> grouupList = gson.fromJson(string, new TypeToken<List<CommunityGrougsBean>>(){}.getType());
				m_InterfaceRecomGroupListCallBack.onSuccess(4652,grouupList);
			}else {
				m_InterfaceRecomGroupListCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceRecomGroupListCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceRecomGroupListCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceRecomGroupListCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//41、动态列表 /api.php?mod=dongtai
	
	private String autoListNewUrl = 	g_BaseSiteURL+"api.php?mod=dongtai";
	private InterfaceGetAutoLisNewCallBack m_InterfaceGetAutoLisNewCallBack;
	public void getAutoListNew(GetAutoListNewParamClass mParam,
			InterfaceGetAutoLisNewCallBack callBack) {
		m_InterfaceGetAutoLisNewCallBack = callBack;

		String newString = autoListNewUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&page_id=" + mParam.page_id + 
				"&xctoken=" + getRequestToken("dongtai")
				;

		super.requestJosnObjectData(mContext, newString);

	}
	private void  getAutoListNewRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4653")) {

				m_InterfaceGetAutoLisNewCallBack.onError(4653,"用户id不存在或者格式错误");

			}else if (idValue.equals("4654")) {
				Gson gson=new Gson();
				String string = (String) response.get("dt_list").toString();
				List<CommunityTopicBean> list = gson.fromJson(string, new TypeToken<List<CommunityTopicBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				String page_id = (String) response.getString("page_id");
				m_InterfaceGetAutoLisNewCallBack.onSuccess(4654, list, totalpage,page_id);

			}else{
				m_InterfaceGetAutoLisNewCallBack.onError(0,errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceGetAutoLisNewCallBack.onError(0,errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceGetAutoLisNewCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetAutoLisNewCallBack.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//42、修改小组头像/api.php?mod=groupeditico
	
	private String updateGroupIconUrl = 	g_BaseSiteURL+"api.php?mod=groupeditico";
	private InterfaceUpdateGroupIconCallBack m_InterfaceUpdateGroupIconCallBack;
	public void updateGroupIcon(UpdateGroupIconParamClass mParam,
			InterfaceUpdateGroupIconCallBack callBack) {
		m_InterfaceUpdateGroupIconCallBack = callBack;

		String newString = updateGroupIconUrl +
				"&my_int_id=" + mParam.my_int_id + 
				"&gid=" + mParam.gid + 
				"&g_ico=" + mParam.g_ico + 
				"&xctoken=" + getRequestToken("groupeditico")
				;

		super.requestJosnObjectData(mContext, newString);

	}
	private void  updateGroupIconRstProc(JSONObject response) {
		String idValue = "";

		try {

			idValue = (String) response.get("response").toString();
		
			if (idValue.equals("4655")) {

				m_InterfaceUpdateGroupIconCallBack.onError(4655,"登录用户id不存在或者格式错误");

			}else if (idValue.equals("4656")) {

				m_InterfaceUpdateGroupIconCallBack.onError(4656,"小组id不存在或者格式错误");

			}else if (idValue.equals("4657")) {

				m_InterfaceUpdateGroupIconCallBack.onError(4657,"小组创建者才能修改小组头像");

			}else if (idValue.equals("4658")) {

				m_InterfaceUpdateGroupIconCallBack.onError(4658,"参数错误");

			}else if (idValue.equals("4659")) {
				String g_ico_url = (String) response.getString("g_ico_url");
				m_InterfaceUpdateGroupIconCallBack.onSuccess(4659, "成功", g_ico_url);

			}else if (idValue.equals("4660")) {
				m_InterfaceUpdateGroupIconCallBack.onError(4660,"失败");
			}else {
				m_InterfaceUpdateGroupIconCallBack.onError(0,errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceUpdateGroupIconCallBack.onError(0,errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceUpdateGroupIconCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceUpdateGroupIconCallBack.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//43、我的小组中我发布的话题列表/api.php?mod=mygroupthreadt
	
	private String getMyGroupPostTopicListUrl = 	g_BaseSiteURL+"api.php?mod=mygroupthreadt";
	private InterfaceMyGroupPostTopicListCallBack m_InterfaceMyGroupPostTopicListCallBack;
	public void getMyGroupPostTopicList(MyGroupPostTopicListParamClass mParam,
			InterfaceMyGroupPostTopicListCallBack callBack) {
		m_InterfaceMyGroupPostTopicListCallBack = callBack;

		String newString = getMyGroupPostTopicListUrl  +
				"&my_int_id=" + mParam.my_int_id + 
				"&gid=" + mParam.gid +
				"&page=" + mParam.page + 
				"&pagesize=" + mParam.pagesize + 
				"&xctoken=" + getRequestToken("mygroupthreadt");
		super.requestJosnObjectData(mContext, newString);

	}
	private void getMyGroupPostTopicListRstProc(JSONObject response) {
		String idValue = "";
		String message = "";

		try {
			idValue = (String) response.get("response").toString();
			message = (String) response.get("message").toString();
		
			if (idValue.equals("4661")) {

				m_InterfaceMyGroupPostTopicListCallBack.onError(4661,message);

			}else if (idValue.equals("4662")) {

				m_InterfaceMyGroupPostTopicListCallBack.onError(4662,message);

			}else if (idValue.equals("4663")) {
				Gson gson=new Gson();
				String string = (String) response.get("thread_list").toString();
				List<CommunityTopicBean> list = gson.fromJson(string, new TypeToken<List<CommunityTopicBean>>(){}.getType());
				int totalpage = response.getInt("totalpage");
				m_InterfaceMyGroupPostTopicListCallBack.onSuccess(4663, list, totalpage);
			}else {
				m_InterfaceMyGroupPostTopicListCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceMyGroupPostTopicListCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceMyGroupPostTopicListCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceMyGroupPostTopicListCallBack.onError(0, errMsgException);
		}
	}
	
	
	//=======================================================================================================
	//44、个人信息修改接口     /api.php?mod=edituserinfo
	
	
	private String editUserInfoUrl = g_BaseSiteURL+"api.php?mod=edituserinfo";
	private InterfaceEditUserInfoCallBack mInterfaceEditUserInfoCallBack;
	public void editUserInfo(EditUserInfoParamClass mParam,
			InterfaceEditUserInfoCallBack callBack) {
		mInterfaceEditUserInfoCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(editUserInfoUrl);
		sb.append("&my_int_id=" + mParam.my_int_id);
		sb.append("&my_int_gender=" + mParam.my_int_gender);
		sb.append("&my_int_occupation=" + mParam.my_int_occupation);
		sb.append("&user_internal_height=" + mParam.user_internal_height);
		sb.append("&user_internal_weight=" + mParam.user_internal_weight);
		sb.append("&user_internal_birthday=" + mParam.user_internal_birthday);
		sb.append("&xctoken=" + getRequestToken("edituserinfo"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void editUserInfoRstProc(JSONObject response){
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4664")) {
				mInterfaceEditUserInfoCallBack.onError(4664,"登录用户不存在");
			}else if (idValue.equals("4665")) {
				mInterfaceEditUserInfoCallBack.onError(4665,"参数格式错误");
			}else if (idValue.equals("4666")) {
				mInterfaceEditUserInfoCallBack.onSuccess(4666,"成功");
			}else if (idValue.equals("4667")) {
				mInterfaceEditUserInfoCallBack.onError(4667, "失败");
			}else {
				mInterfaceEditUserInfoCallBack.onError(0, errMsgNone);
			}
		} catch (Exception e) {
			LogUtil.i("masong", e.getMessage());
			mInterfaceEditUserInfoCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//45、新浪微博好友列表    /api.php?mod=weibolist
//	private String  getWeiBoUserListUrl = 	g_BaseSiteURL+"api.php?mod=weibolist";
	private String  getWeiBoUserListUrl = 	g_BaseSiteURL+"api.php?";
	private InterfaceGetWeiBoUserListCallBack m_InterfaceGetWeiBoUserListCallBack;
	public void getWeiBoUserList(GetWeiBoUserListParamClass mParam,
			InterfaceGetWeiBoUserListCallBack callBack) {
		m_InterfaceGetWeiBoUserListCallBack = callBack;
		

//		String newString = getWeiBoUserListUrl  +
//				"&my_int_id=" + mParam.my_int_id + 
//				"&weiboid=" + new Gson().toJson(mParam.weiboid) +
//				"&xctoken=" + getRequestToken("weibolist");
//		super.requestJosnObjectData(mContext, newString);
		
		
		
		RequestParams params = new RequestParams();
		params.put("mod", "weibolist");
		params.put("my_int_id", mParam.my_int_id);
		params.put("weiboid", new Gson().toJson(mParam.weiboid));
		params.put("xctoken", getRequestToken("weibolist"));
		super.postJosnObjectData(mContext, getWeiBoUserListUrl, params);
	}
	private void getWeiBoUserListRstProc(JSONObject response) {
		String idValue = "";
		String message = "";

		try {
			idValue = (String) response.get("response").toString();
			message = (String) response.get("message").toString();
		
			if (idValue.equals("4668")) {

				m_InterfaceGetWeiBoUserListCallBack.onError(4668,message);

			}else if (idValue.equals("4669")) {

				m_InterfaceGetWeiBoUserListCallBack.onError(4669,message);

			}else if (idValue.equals("4670")) {
				Gson gson=new Gson();
				String string = (String) response.get("weibo_list").toString();
				List<WeiBoUserInfoListClass> list = gson.fromJson(string, new TypeToken<List<WeiBoUserInfoListClass>>(){}.getType());
//				int totalpage = response.getInt("totalpage");
				m_InterfaceGetWeiBoUserListCallBack.onSuccess(4670, "成功", list, 0);
			}else {
				m_InterfaceGetWeiBoUserListCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceGetWeiBoUserListCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceGetWeiBoUserListCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetWeiBoUserListCallBack.onError(0, errMsgException);
		}
	}
	
	//=======================================================================================================
	//46、手机通信录好友列表     /api.php?mod=phonelist
//	private String  getPhoneUserListUrl = 	g_BaseSiteURL+"api.php?mod=phonelist";
	private String  getPhoneUserListUrl = 	g_BaseSiteURL+"api.php?";
	private InterfaceGetPhoneUserListCallBack m_InterfaceGetPhoneUserListCallBack;
	public void getPhoneUserList(Context context,GetPhoneUserListParamClass mParam,
			InterfaceGetPhoneUserListCallBack callBack) {
		m_InterfaceGetPhoneUserListCallBack = callBack;

//		String newString = getPhoneUserListUrl  +
//				"&my_int_id=" + mParam.my_int_id + 
//				"&phones=" + new Gson().toJson(mParam.phones) +
//				"&page=" + mParam.page +
//				"&pagesize=" + mParam.pagesize +
//				"&xctoken=" + getRequestToken("phonelist");
//		System.out.println("--->friend" + newString);
		
		RequestParams params = new RequestParams();
		params.put("mod", "phonelist");
		params.put("my_int_id", mParam.my_int_id);
		params.put("phones", new Gson().toJson(mParam.phones));
		params.put("page", mParam.page);
		params.put("pagesize", mParam.pagesize);
		params.put("xctoken", getRequestToken("phonelist"));
		super.postJosnObjectData(context, getPhoneUserListUrl, params);

	}
	private void getPhoneUserListRstProc(JSONObject response) {
		String idValue = "";
		String message = "";

		try {
			idValue = (String) response.get("response").toString();
			message = (String) response.get("message").toString();
		
			if (idValue.equals("4672")) {

				m_InterfaceGetPhoneUserListCallBack.onError(4672,message);

			}else if (idValue.equals("4673")) {

				m_InterfaceGetPhoneUserListCallBack.onError(4673,message);

			}else if (idValue.equals("4674")) {
				Gson gson=new Gson();
				String string = (String) response.get("phone_list").toString();
//				System.out.println(string);
				List<PhoneUserInfoListClass> list = gson.fromJson(string, new TypeToken<List<PhoneUserInfoListClass>>(){}.getType());
//				int totalpage = response.getInt("totalpage");
				m_InterfaceGetPhoneUserListCallBack.onSuccess(4674, list, 0);
			}else{
				m_InterfaceGetPhoneUserListCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			m_InterfaceGetPhoneUserListCallBack.onError(0, errMsgException);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			m_InterfaceGetPhoneUserListCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			m_InterfaceGetPhoneUserListCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//47、删除评论     /api.php?mod=postdel
	private String deleteCommentUrl = g_BaseSiteURL+"api.php?mod=postdel";
	private InterfaceDeleteCommentCallBack mInterfaceDeleteCommentCallBack;
	public void deleteComment(DeleteCommentParamClass mParam,
			InterfaceDeleteCommentCallBack callBack) {
		mInterfaceDeleteCommentCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(deleteCommentUrl);
		sb.append("&pid=" + mParam.pid);
		sb.append("&my_int_id=" + mParam.my_int_id);
		sb.append("&xctoken=" + getRequestToken("postdel"));
		String newString = sb.toString();
		
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void deleteCommentRstProc(JSONObject response){
			String idValue = "";
//			String message = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
//			message = (String) response.get("message").toString();
			
			if (idValue.equals("4675")) {
				
				mInterfaceDeleteCommentCallBack.onError(4675,"评论不存在");
				
			}else if (idValue.equals("4676")) {
				
				mInterfaceDeleteCommentCallBack.onError(4676,"用户不存在");
				
			}else if (idValue.equals("4677")) {
				
				mInterfaceDeleteCommentCallBack.onError(4677,"你不能删除该评论");
				
			}else if (idValue.equals("4678")) {
				
				mInterfaceDeleteCommentCallBack.onSuccess(4678, "成功");
				
			}else {
				mInterfaceDeleteCommentCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceDeleteCommentCallBack.onError(0, errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceDeleteCommentCallBack.onError(0, errMsgException);
		}
	}
	//=======================================================================================================
	//48、置顶话题    /api.php?mod=threadzd
	private String setTopTopicUrl = g_BaseSiteURL+"api.php?mod=threadzd";
	private InterfaceSetTopTopicCallBack mInterfaceSetTopTopicCallBack;
	public void setTopTopic(SetTopTopicParamClass mParam,
			InterfaceSetTopTopicCallBack callBack) {
		mInterfaceSetTopTopicCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(setTopTopicUrl);
		sb.append("&tid=" + mParam.tid);
		sb.append("&my_int_id=" + mParam.my_int_id);
		sb.append("&xctoken=" + getRequestToken("threadzd"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void setTopTopicRstProc(JSONObject response){
			String idValue = "";
//			String message = "";
		try {
			idValue = (String) response.get("response").toString();
//			message = (String) response.get("message").toString();
			
			if (idValue.equals("4679")) {
				
				mInterfaceSetTopTopicCallBack.onError(4679,"话题不存在");
				
			}else if (idValue.equals("4680")) {
				
				mInterfaceSetTopTopicCallBack.onError(4680,"用户不存在");
				
			}else if (idValue.equals("4681")) {
				
				mInterfaceSetTopTopicCallBack.onError(4681,"只有小组管理员（创建者）才能置顶话题。");
				
			}else if (idValue.equals("4682")) {
				
				mInterfaceSetTopTopicCallBack.onSuccess(4682, "成功");
				
			}else {
				mInterfaceSetTopTopicCallBack.onError(0, errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceSetTopTopicCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceSetTopTopicCallBack.onError(0,errMsgException);
		}
	}
	
	//=======================================================================================================
	//49、普通用户咨询专家自动添加专家为好友   /api.php?mod=addfriendszj
	
	private String addExpertFriendUrl = g_BaseSiteURL+"api.php?mod=addfriendszj";
//	private String addExpertFriendUrl = g_BaseSiteURL+"api.php?";
	private InterfaceAddExpertFriendCallBack mInterfaceAddExpertFriendCallBack;
	public void addExpertFriend(AddExpertFriendParamClass mParam,
			InterfaceAddExpertFriendCallBack callBack) {
		mInterfaceAddExpertFriendCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(addExpertFriendUrl);
		sb.append("&my_int_id=" + mParam.my_int_id);
		sb.append("&zj_int_id=" + mParam.zj_int_id);
		sb.append("&xctoken=" + getRequestToken("addfriendszj"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void addExpertFriendRstProc(JSONObject response){
			String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4683")) {
				
				mInterfaceAddExpertFriendCallBack.onError(4683,"登录用户不存在");
				
			}else if (idValue.equals("4684")) {
				
				mInterfaceAddExpertFriendCallBack.onError(4684,"专家用户不存在");
				
			}else if (idValue.equals("4685")) {
				
				mInterfaceAddExpertFriendCallBack.onError(4685,"失败");
				
			}else if (idValue.equals("4686")) {
				Gson gson=new Gson();
				String string = (String) response.get("datas").toString();
				ExpertFriendClass friendClass = (ExpertFriendClass) gson.fromJson(string, ExpertFriendClass.class);
				mInterfaceAddExpertFriendCallBack.onSuccess(4686, "成功",friendClass);
				
			}else{
				mInterfaceAddExpertFriendCallBack.onError(0,errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceAddExpertFriendCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceAddExpertFriendCallBack.onError(0,errMsgException);
		}
	}
	
	//=======================================================================================================
	//50、首页小组推荐   /api.php?mod=regroupindex
	private String getFirstPageRecommentGroupsUrl = g_BaseSiteURL+"api.php?mod=regroupindex";
	private InterfaceFirstPageRecommentGroupCallBack mInterfaceFirstPageRecommentGroupCallBack;
	public void getFirstPageRecommentGroups(String my_int_id,
			InterfaceFirstPageRecommentGroupCallBack callBack) {
		mInterfaceFirstPageRecommentGroupCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(getFirstPageRecommentGroupsUrl);
		sb.append("&my_int_id=" + my_int_id);
		sb.append("&xctoken=" + getRequestToken("regroupindex"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getFirstPageRecommentGroupsRstProc(JSONObject response){
		String idValue = "";
		
		try {
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4687")) {
				
				mInterfaceFirstPageRecommentGroupCallBack.onError(4687,"参数格式错误");
				
			}else if (idValue.equals("4688")) {
				Gson gson=new Gson();
				String string = (String) response.get("index_push_list").toString();
				List<FirstRecommentGroupBean> grouupList = gson.fromJson(string, new TypeToken<List<FirstRecommentGroupBean>>(){}.getType());
				mInterfaceFirstPageRecommentGroupCallBack.onSuccess(4688,grouupList);
				
			}else{
				mInterfaceFirstPageRecommentGroupCallBack.onError(0,errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceFirstPageRecommentGroupCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceFirstPageRecommentGroupCallBack.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//51、获取硬件绑定天数   /api.php?mod=yjbangd
	private String getHardwareBoundDaysUrl = g_BaseSiteURL+"api.php?mod=yjbangd";
	private InterfaceGetHardwareBoundDaysCallBack mInterfaceGetHardwareBoundDaysCallBack;
	public void getHardwareBoundDays(String my_int_id,String macadd,
			InterfaceGetHardwareBoundDaysCallBack callBack) {
		mInterfaceGetHardwareBoundDaysCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(getHardwareBoundDaysUrl);
		sb.append("&my_int_id=" + my_int_id);
		sb.append("&macadd=" + macadd);
		sb.append("&xctoken=" + getRequestToken("yjbangd"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getHardwareBoundDaysRstProc(JSONObject response){
		String idValue = "";
		
		try {
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4689")) {
				
				mInterfaceGetHardwareBoundDaysCallBack.onError(4689,"参数格式错误");
				
			}else if (idValue.equals("4690")) {
				String days = (String) response.get("days").toString();
				mInterfaceGetHardwareBoundDaysCallBack.onSuccess(4690,days);
				
			}else{
				mInterfaceGetHardwareBoundDaysCallBack.onError(0,errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceGetHardwareBoundDaysCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceGetHardwareBoundDaysCallBack.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//52、硬件绑定与解绑  /api.php?mod=yjuploadsbd
	private String hardwareBoundUrl = g_BaseSiteURL+"api.php?mod=yjuploadsbd";
	private InterfaceHardwareBoundCallBack mInterfaceHardwareBoundCallBack;
	public void hardwareBound(HardwareBoundParamClass mParams,
			InterfaceHardwareBoundCallBack callBack) {
		mInterfaceHardwareBoundCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(hardwareBoundUrl);
		sb.append("&my_int_id=" + mParams.my_int_id);
		sb.append("&jystatus=" + mParams.jystatus);
		sb.append("&bdtime=" + mParams.bdtime);
		sb.append("&jbtime=" + mParams.jbtime);
		sb.append("&yjlmd=" + mParams.yjlmd);
		sb.append("&macadd=" + mParams.macadd);
		sb.append("&xctoken=" + getRequestToken("yjuploadsbd"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void hardwareBoundRstProc(JSONObject response){
		String idValue = "";
		
		try {
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4691")) {
				
				mInterfaceHardwareBoundCallBack.onError(4691,"用户不存在");
				
			}else if (idValue.equals("4691:1")) {
				
				mInterfaceHardwareBoundCallBack.onError(46911,"参数错误");
				
			}else if (idValue.equals("4692")) {
				
				mInterfaceHardwareBoundCallBack.onError(4692,"失败");
				
			}else if (idValue.equals("4693")) {
				
				mInterfaceHardwareBoundCallBack.onSuccess(4693,"成功");
				
			}else{
				
				mInterfaceHardwareBoundCallBack.onError(0,errMsgNone);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceHardwareBoundCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceHardwareBoundCallBack.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//53、上传硬件睡眠数据  /api.php?mod=yjuploads
//	private String uploadHardwareDataUrl = g_BaseSiteURL+"api.php?mod=yjuploads";
	private String uploadHardwareDataUrl = g_BaseSiteURL+"api.php?mod=yjuploads";
	private InterfaceUploadHardwareDataCallBack1 mInterfaceUploadHardwareDataCallBack1;
	public void UploadHardwareSleepData(UploadHardwareDataParamClass1 mParam,
			InterfaceUploadHardwareDataCallBack1 callBack) {
		mInterfaceUploadHardwareDataCallBack1 = callBack;
		
		Map<String,String> map=new HashMap<String, String>();
		map.put("mod", "yjuploads");
		
		map.put("awakecount", mParam.awakecount);
		map.put("awakenogetupcount", mParam.awakenogetupcount);
		map.put("awaketimesleep", mParam.awaketimesleep);
		map.put("date", mParam.date);
		map.put("deepsleep", mParam.deepsleep);
		
		map.put("getuptime", mParam.getuptime);
		map.put("gotobedtime", mParam.gotobedtime);
		map.put("insleeptime", mParam.insleeptime);
		map.put("listlength", mParam.listlength);
		map.put("my_int_id", mParam.my_int_id);
		
		map.put("onbed", mParam.onbed);
		map.put("outsleeptime", mParam.outsleeptime);
		map.put("shallowsleep", mParam.shallowsleep);
		map.put("sleepbak1", mParam.sleepbak1);
		map.put("Sleepbak2", mParam.Sleepbak2);
		
		map.put("tosleep", mParam.tosleep);
		map.put("totalsleeptime", mParam.totalsleeptime);
		map.put("user_location_x", mParam.user_location_x);
		map.put("user_location_y", mParam.user_location_y);
		map.put("xstart", mParam.xstart);
		
		map.put("xstop", mParam.xstop);
		map.put("ymax", mParam.ymax);
		
		
		map.put("xctoken", getRequestToken("yjuploads"));
		System.out.println("开始上传dat数据");
		
		try {
			UploadUtil.getInstance().uploadFile(mParam.file, "file", uploadHardwareDataUrl, map,new OnUploadProcessListener() {
				
				@Override
				public void onUploadProcess(int uploadSize) {
					
				}
				
				@Override
				public void onUploadDone(int responseCode, String message) {
					try {
						System.out.println("上传所有数据的返回"+message+responseCode);
						JSONObject response =new JSONObject(message);
						uploadHardwareDataRstProc(response);
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
	
	private void uploadHardwareDataRstProc(JSONObject response){
		String idValue = "";
		
		try {
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4694")) {
				
				mInterfaceUploadHardwareDataCallBack1.onError(4694,"用户不存在");
				
			}else if (idValue.equals("4695")) {
				
				mInterfaceUploadHardwareDataCallBack1.onError(4695,"参数错误");
				
			}else if (idValue.equals("4696")) {
				
				mInterfaceUploadHardwareDataCallBack1.onError(4696,"失败");
				
			}else if (idValue.equals("4697")) {
				
				mInterfaceUploadHardwareDataCallBack1.onSuccess(4697,"成功");
				
			}else{
				
				mInterfaceUploadHardwareDataCallBack1.onError(0,errMsgNone);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceUploadHardwareDataCallBack1.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceUploadHardwareDataCallBack1.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//54、下载硬件睡眠数据（最近14天） /api.php?mod=yjdownloads
	private String downloadHardwareSleepDataUrl = g_BaseSiteURL+"api.php?mod=yjdownloads";
	private InterfaceDownloadHardwareDataCallBack mInterfaceDownloadHardwareDataCallBack;
	public void downloadHardwareSleepData(String my_int_id,
			InterfaceDownloadHardwareDataCallBack callBack) {
		mInterfaceDownloadHardwareDataCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(downloadHardwareSleepDataUrl);
		sb.append("&my_int_id=" + my_int_id);
		sb.append("&xctoken=" + getRequestToken("yjdownloads"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void downloadHardwareSleepDataRstProc(JSONObject response){
		String idValue = "";
		
		try {
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4698")) {
				
				mInterfaceDownloadHardwareDataCallBack.onError(4698,"用户不存在");
				
			}else if (idValue.equals("4699")) {
				
				mInterfaceDownloadHardwareDataCallBack.onError(4699,"参数格式错误");
				
			}else if (idValue.equals("4700")) {
				
				mInterfaceDownloadHardwareDataCallBack.onError(4700,"失败");
				
			}else if (idValue.equals("4701")) {
				Gson gson=new Gson();
				String string = (String) response.get("list").toString();
				List<HardwareSleepDataBean> grouupList = gson.fromJson(string, new TypeToken<List<HardwareSleepDataBean>>(){}.getType());
				mInterfaceDownloadHardwareDataCallBack.onSuccess(4701,grouupList);
				
			}else{
				mInterfaceDownloadHardwareDataCallBack.onError(0,errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceDownloadHardwareDataCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceDownloadHardwareDataCallBack.onError(0,errMsgException);
		}
	}
	
	//=======================================================================================================
	//55、获取调参接口 /api.php?mod=yjtiaocan
	
	private String getTiaocanUrl = g_BaseSiteURL+"api.php?mod=yjtiaocan";
	private InterfaceGetTiaocanCallBack mInterfaceGetTiaocanCallBack;
	public void getTiaocanFileUrl(String my_int_id,
			InterfaceGetTiaocanCallBack callBack) {
		mInterfaceGetTiaocanCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(getTiaocanUrl);
		sb.append("&my_int_id=" + my_int_id);
		sb.append("&xctoken=" + getRequestToken("yjtiaocan"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getTiaocanFileUrlRstProc(JSONObject response){
		String idValue = "";
		
		try {
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4702")) {
				
				mInterfaceGetTiaocanCallBack.onError(4702,"用户不存在");
				
			}else if (idValue.equals("4703")) {
				
				mInterfaceGetTiaocanCallBack.onError(4703,"失败");
				
			}else if (idValue.equals("4704")) {
				String tiaocanfile = (String) response.get("tiaocanfile").toString();
				mInterfaceGetTiaocanCallBack.onSuccess(4704,tiaocanfile);
				
			}else{
				mInterfaceGetTiaocanCallBack.onError(0,errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceGetTiaocanCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceGetTiaocanCallBack.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//56、获取用户灵敏度/api.php?mod=yjlmd
	
	private String getHardwareSensitivityUrl = g_BaseSiteURL+"api.php?mod=yjlmd";
	private InterfaceGetHardwareSensitivityCallBack mInterfaceGetHardwareSensitivityCallBack;
	public void getHardwareSensitivity(String my_int_id,
			InterfaceGetHardwareSensitivityCallBack callBack) {
		mInterfaceGetHardwareSensitivityCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(getHardwareSensitivityUrl);
		sb.append("&my_int_id=" + my_int_id);
		sb.append("&xctoken=" + getRequestToken("yjlmd"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getHardwareSensitivityRstProc(JSONObject response){
		String idValue = "";
		
		try {
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4705")) {
				
				mInterfaceGetHardwareSensitivityCallBack.onError(4705,"用户不存在");
				
			}else if (idValue.equals("4706")) {
				
				mInterfaceGetHardwareSensitivityCallBack.onError(4706,"失败");
				
			}else if (idValue.equals("4707")) {
				String yjlmd = (String) response.get("yjlmd").toString();
				String public_yjlmd = response.get("public_yjlmd").toString();
				mInterfaceGetHardwareSensitivityCallBack.onSuccess(4707,yjlmd, public_yjlmd);
				
			}else{
				mInterfaceGetHardwareSensitivityCallBack.onError(0,errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceGetHardwareSensitivityCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceGetHardwareSensitivityCallBack.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//57、用户修改灵敏度 /api.php?mod=yjlmdedit
	
	private String mofifyHardwareSensitivityUrl = g_BaseSiteURL+"api.php?mod=yjlmdedit";
	private InterfaceMofifyHardwareSensitivityCallBack mInterfaceMofifyHardwareSensitivityCallBack;
	public void mofifyHardwareSensitivity(MofifyHardwareSensitivityParamClass mParams,
			InterfaceMofifyHardwareSensitivityCallBack callBack) {
		mInterfaceMofifyHardwareSensitivityCallBack = callBack;
		StringBuffer sb = new StringBuffer();
		sb.append(mofifyHardwareSensitivityUrl);
		sb.append("&my_int_id=" + mParams.my_int_id);
		sb.append("&yjlmd=" + mParams.yjlmd);
		sb.append("&xctoken=" + getRequestToken("yjlmdedit"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void mofifyHardwareSensitivityRstProc(JSONObject response){
		String idValue = "";
		
		try {
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4708")) {
				
				mInterfaceMofifyHardwareSensitivityCallBack.onError(4708,"用户不存在");
				
			}else if (idValue.equals("4709")) {
				
				mInterfaceMofifyHardwareSensitivityCallBack.onError(4709,"参数错误");
				
			}else if (idValue.equals("4710")) {
				
				mInterfaceMofifyHardwareSensitivityCallBack.onError(4710,"用户没有绑定过硬件");
				
			}else if (idValue.equals("4711")) {
				
				mInterfaceMofifyHardwareSensitivityCallBack.onError(4711,"失败");
				
			}else if (idValue.equals("4712")) {
				mInterfaceMofifyHardwareSensitivityCallBack.onSuccess(4712,"成功");
				
			}else{
				mInterfaceMofifyHardwareSensitivityCallBack.onError(0,errMsgNone);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mInterfaceMofifyHardwareSensitivityCallBack.onError(0,errMsgException);
		}catch (Exception e) {
			e.printStackTrace();
			mInterfaceMofifyHardwareSensitivityCallBack.onError(0,errMsgException);
		}
	}
	//=======================================================================================================
	//58、登录用户是否绑定硬件及获取绑定mac地址
	private String GetBundInfoAtLoginTimeUrl = g_BaseSiteURL+"/api.php?mod=yjuserbd";
	private  InterfaceGetBundInfoAtLoginTimeCallBack mInterfaceGetBundInfoAtLoginTimeCallBack;
	public void GetBundInfoAtLoginTime(String my_int_id, InterfaceGetBundInfoAtLoginTimeCallBack callback){
		mInterfaceGetBundInfoAtLoginTimeCallBack = callback;
		StringBuffer sb = new StringBuffer();
		sb.append(GetBundInfoAtLoginTimeUrl);
		sb.append("&my_int_id=" + my_int_id);
		sb.append("&xctoken=" + getRequestToken("yjuserbd"));
		String newString = sb.toString();
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void GetBundInfoAtLoginTimeRstProc(JSONObject response){
		String idValue = "";
		
		
			try {
				idValue = (String) response.get("response").toString();
				if (idValue.equals("4713")) {
					mInterfaceGetBundInfoAtLoginTimeCallBack.onError(4713, "参数错误");
				}else if(idValue.equals("4714")){
					String flag = (String) response.get("flag").toString();
					String macadd = (String) response.get("macadd").toString();
					mInterfaceGetBundInfoAtLoginTimeCallBack.onSuccess(4714, flag, macadd);
				}else{
					mInterfaceGetBundInfoAtLoginTimeCallBack.onError(0, errMsgNone);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mInterfaceGetBundInfoAtLoginTimeCallBack.onError(0, errMsgException);
			}catch (Exception e) {
				e.printStackTrace();
				mInterfaceGetBundInfoAtLoginTimeCallBack.onError(0, errMsgException);
			}
			
			
		
	}
	//////////////////////////////////
	

	@Override
	public void ProcJSONData(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String)  response.get("response").toString();
			
			if (idValue.equals("410") ||idValue.equals("41１") ||idValue.equals("412")) {
				requestFailed(idValue + "：访问数据库失败", Integer.parseInt(idValue));
			}
			if (idValue.equals("4999") ) {
				requestFailed(idValue + "：提交的参数含有非法字符", Integer.parseInt(idValue));
			}
			
			if (idValue.equals("4500") || idValue.equals("4501") || idValue.equals("4502")) 
			{
				if (m_InterfaceGetCommunityGroupCallBack != null) {
					getMyGroupRstProc(response);
				}
			}else if(idValue.equals("4503") || idValue.equals("4504") || idValue.equals("4504:1") || idValue.equals("4504:2")||
					idValue.equals("4505") || idValue.equals("4506"))
			{
				if (m_InterfaceAddCommunityGroupCallBack != null) {
					addGroupRstProc(response);
				}
			}else if(idValue.equals("4507") || idValue.equals("4507:1") || idValue.equals("4508") || idValue.equals("4509"))
			{
				if (m_InterfaceGetCommunityTopicCallBack != null) {
					getCommunityTopicRstProc(response);
				}
			}else if(idValue.equals("4510") || idValue.equals("4511") || idValue.equals("4512"))
			{
				if (m_InterfaceGetCommunityTopicDetailCallBack != null) {
					getCommunityTopicDetailRstProc(response);
				}
			}else if(idValue.equals("4513") || idValue.equals("4514") || idValue.equals("4514:1") || idValue.equals("4515")
					|| idValue.equals("4516"))
			{
				if (m_InterfaceTopicPraiseCallBack != null) {
					topicPraiseRstProc(response);
				}
			}else if(idValue.equals("4517") || idValue.equals("4518") || idValue.equals("4518:1")|| idValue.equals("4519")
					|| idValue.equals("4520"))
			{
				if (m_InterfaceTopicDeleteCallBack != null) {
					topicDeleteRstProc(response);
				}
			}else if(idValue.equals("4521") || idValue.equals("4522") || idValue.equals("4523")
					|| idValue.equals("4524") || idValue.equals("4525"))
			{
				if (m_InterfaceTopicPostNewCallBack != null) {
					topicPostNewRstProc(response);
				}
			}else if(idValue.equals("4526") || idValue.equals("4527") || idValue.equals("4528")
					|| idValue.equals("4529") || idValue.equals("4530")|| idValue.equals("4531"))
			{
				if (m_InterfaceTopicReplyPostNewCallBack != null) {
					topicReplyPostRstProc(response);
				}
			}else if(idValue.equals("4532") || idValue.equals("4533") || idValue.equals("4534")
					|| idValue.equals("4535"))
			{
				if (m_InterfaceGetGroupMessageCallBack != null) {
					getGroupMessageRstProc(response);
				}
			}else if(idValue.equals("4539") || idValue.equals("4540") || idValue.equals("4541"))
			{
				if (m_InterfaceGetGroupDoctorListCallBack != null) {
					getGroupDoctorListRstProc(response);
				}
			}else if(idValue.equals("4542") || idValue.equals("4543") || idValue.equals("4544")
					|| idValue.equals("4545")|| idValue.equals("4544:1") || idValue.equals("4544:2"))
			{
				if (m_InterfaceAddGroupDoctorCallBack != null) {
					addGroupDoctorRstProc(response);
				}
			}else if(idValue.equals("4546") || idValue.equals("4547") || idValue.equals("4548"))
			{
				if (m_InterfaceSearchGroupTopicCallBack != null) {
					searchGroupTopicRstProc(response);
				}
			}else if(idValue.equals("4549") || idValue.equals("4550") || idValue.equals("4551")
					|| idValue.equals("4552"))
			{
				if (m_InterfaceReportTopicCallBack != null) {
					reportTopicRstProc(response);
				}
			}else if(idValue.equals("4553") || idValue.equals("4554") || idValue.equals("4555")
					|| idValue.equals("4556"))
			{
				if (m_InterfaceEditGroupSummaryCallBack != null) {
					editGroupSummaryRstProc(response);
				}
			}else if(idValue.equals("4557") || idValue.equals("4558") || idValue.equals("4559")
					|| idValue.equals("4560"))
			{
				if (m_InterfaceAttentionGroupCallBack != null) {
					attentionGroupRstProc(response);
				}
			}else if(idValue.equals("4561") || idValue.equals("4562") || idValue.equals("4563"))
			{
				if (m_InterfaceCommunityFoundCallBack != null) {
					communityFoundRstProc(response);
				}
			}else if(idValue.equals("4564") || idValue.equals("4565") || idValue.equals("4566"))
			{
				if (m_InterfaceMyTopicByTimeCallBack != null) {
					myTopicByTimeRstProc(response);
				}
			}else if(idValue.equals("4567") || idValue.equals("4568") || idValue.equals("4569"))
			{
				if (m_InterfaceMyTopicByGroupCallBack != null) {
					myTopicByGroupRstProc(response);
				}
			}else if(idValue.equals("4570") || idValue.equals("4571") || idValue.equals("4572"))
			{
				if (m_InterfaceGetTagListCallBack != null) {
					getTagListRstProc(response);
				}
			}else if(idValue.equals("4573") || idValue.equals("4574") || idValue.equals("4575")
					|| idValue.equals("4576"))
			{
				if (m_InterfaceDeleteTagCallBack != null) {
					deleteTagRstProc(response);
				}
			}else if(idValue.equals("4577") || idValue.equals("4578"))
			{
				if (m_InterfaceGetHotTagCallBack != null) {
					getHotTagRstProc(response);
				}
			}else if(idValue.equals("4579") || idValue.equals("4580")|| idValue.equals("4581")
					|| idValue.equals("4582"))
			{
				if (m_InterfaceCancelAttentionGroupCallBack != null) {
					cancelAttentionRstProc(response);
				}
			}else if(idValue.equals("4583") || idValue.equals("4584")|| idValue.equals("4585")
					|| idValue.equals("4586"))
			{
				if (m_InterfaceCommunityRecommendRefuseCallBack != null) {
					recommendRefuseRstProc(response);
				}
			}else if(idValue.equals("4587") || idValue.equals("4588")|| idValue.equals("4589")
					|| idValue.equals("4590"))
			{
				if (m_InterfaceCheckUserCreateGroupCallBack != null) {
					checkUserCreateGroupRstProc(response);
				}
			}else if(idValue.equals("4591") || idValue.equals("4592")|| idValue.equals("4593"))
			{
				if (m_InterfaceSearchTopicCallBack != null) {
					searchTopicRstProc(response);
				}
			}else if(idValue.equals("4594") || idValue.equals("4595")|| idValue.equals("4596"))
			{
				if (m_InterfaceSearchGroupCallBack != null) {
					searchGroupRstProc(response);
				}
			}else if(idValue.equals("4597") || idValue.equals("4598")|| idValue.equals("4599"))
			{
				if (m_InterfaceGetCommentLisCallBack != null) {
					getCommentsListRstProc(response);
				}
			}else if(idValue.equals("4600") || idValue.equals("4601")|| idValue.equals("4602")
					|| idValue.equals("4603"))
			{
				if (m_InterfaceGetResportLisCallBack != null) {
					getReportListRstProc(response);
				}
			}else if(idValue.equals("4604") || idValue.equals("4605")|| idValue.equals("4606")
					|| idValue.equals("4607"))
			{
				if (m_InterfaceIgnoreResportCallBack != null) {
					ignoreResportRstProc(response);
				}
			}else if(idValue.equals("4608") || idValue.equals("4609")|| idValue.equals("4610")
					|| idValue.equals("4611"))
			{
				if (m_InterfaceDeleteResportCallBack != null) {
					deleteResportRstProc(response);
				}
			}else if(idValue.equals("4612") || idValue.equals("4613")|| idValue.equals("4614"))
			{
				if (m_InterfaceGetAutoLisCallBack != null) {
					getAutoListRstProc(response);
				}
			}else if(idValue.equals("4615") || idValue.equals("4616")|| idValue.equals("4617"))
			{
				if (m_InterfaceClearAutoLisCallBack != null) {
					clearAutoListRstProc(response);
				}
			}else if(idValue.equals("4618") || idValue.equals("4619")|| idValue.equals("4620")
					|| idValue.equals("4621")|| idValue.equals("4622"))
			{
				if (m_InterfaceAddTagCallBack != null) {
					addTagRstProc(response);
				}
			}else if(idValue.equals("4623") || idValue.equals("4624")|| idValue.equals("4625"))
			{
				if (m_InterfaceDynamicMsgNumCallBack != null) {
					dynamicMsgNumRstProc(response);
				}
			}else if(idValue.equals("4626") || idValue.equals("4627")|| idValue.equals("4628")
					|| idValue.equals("4629")|| idValue.equals("4630"))
			{
				if (m_InterfaceModifyTagCallBack != null) {
					modifyTagRstProc(response);
				}
			}else if(idValue.equals("4631") || idValue.equals("4632") || idValue.equals("4633")
					|| idValue.equals("4634") || idValue.equals("4635"))
			{
				if (mInterfaceDelGroupDoctorCallBack != null) {
					delGroupDoctorRstProc(response);
				}
			}else if (idValue.equals("4635") || idValue.equals("4636") || idValue.equals("4637")
					|| idValue.equals("4638") || idValue.equals("4639") || idValue.equals("4640")
					|| idValue.equals("4641") || idValue.equals("4642")) {
				if (mInterfaceBoundPhoneCallBack != null) {
					telphonebdRstProc(response);
				}
			}else if(idValue.equals("4643") || idValue.equals("4644") || idValue.equals("4645")
					|| idValue.equals("4646")){
				if (mInterfaceCancleDoctorCallBack != null) {
					cancleDoctorRstProc(response);
				}
			}else if(idValue.equals("4647") || idValue.equals("4648") || idValue.equals("4649")
					|| idValue.equals("4650")){
				if (mInterfaceSendMessageCallBack != null) {
					sendMessageRstProc(response);
				}
			}else if(idValue.equals("4651") || idValue.equals("4652")){
				if (m_InterfaceRecomGroupListCallBack != null) {
					getRecomGroupListRstProc(response);
				}
			}else if(idValue.equals("4653") || idValue.equals("4654")){
				if (m_InterfaceGetAutoLisNewCallBack != null) {
					getAutoListNewRstProc(response);
				}
			}else if(idValue.equals("4655") || idValue.equals("4656")|| idValue.equals("4657")
					|| idValue.equals("4658")|| idValue.equals("4659")|| idValue.equals("4660")){
				if (m_InterfaceUpdateGroupIconCallBack != null) {
					updateGroupIconRstProc(response);
				}
			}else if(idValue.equals("4661") || idValue.equals("4662")|| idValue.equals("4663")){
				if (m_InterfaceMyGroupPostTopicListCallBack != null) {
					getMyGroupPostTopicListRstProc(response);
				}
			}else if(idValue.equals("4664") || idValue.equals("4665")|| idValue.equals("4666")
					|| idValue.equals("4667")){
				if (mInterfaceEditUserInfoCallBack != null) {
					editUserInfoRstProc(response);
				}
			}else if(idValue.equals("4668") || idValue.equals("4669")|| idValue.equals("4670")){
				if (m_InterfaceGetWeiBoUserListCallBack != null) {
					getWeiBoUserListRstProc(response);
				}
			}else if(idValue.equals("4672") || idValue.equals("4673")|| idValue.equals("4674")){
				if (m_InterfaceGetPhoneUserListCallBack != null) {
					getPhoneUserListRstProc(response);
				}
			}else if(idValue.equals("4675") || idValue.equals("4676")|| idValue.equals("4677")
					|| idValue.equals("4678")){
				if (mInterfaceDeleteCommentCallBack != null) {
					deleteCommentRstProc(response);
				}
			}else if(idValue.equals("4679") || idValue.equals("4680")|| idValue.equals("4681")
					|| idValue.equals("4682")){
				if (mInterfaceSetTopTopicCallBack != null) {
					setTopTopicRstProc(response);
				}
			}else if(idValue.equals("4683") || idValue.equals("4684")|| idValue.equals("4685")
					|| idValue.equals("4686")){
				if (mInterfaceAddExpertFriendCallBack != null) {
					addExpertFriendRstProc(response);
				}
			}else if(idValue.equals("4687") || idValue.equals("4688")){
				if (mInterfaceFirstPageRecommentGroupCallBack != null) {
					getFirstPageRecommentGroupsRstProc(response);
				}
			}else if(idValue.equals("4689") || idValue.equals("4690")){
				if (mInterfaceGetHardwareBoundDaysCallBack != null) {
					getHardwareBoundDaysRstProc(response);
				}
			}else if(idValue.equals("4691") || idValue.equals("4691:1")|| idValue.equals("4692")
					|| idValue.equals("4693")){
				if (mInterfaceHardwareBoundCallBack != null) {
					hardwareBoundRstProc(response);
				}
			}else if(idValue.equals("4694") || idValue.equals("4695")|| idValue.equals("4696")
					|| idValue.equals("4697")){
				if (mInterfaceUploadHardwareDataCallBack1 != null) {
					uploadHardwareDataRstProc(response);
				}
			}else if(idValue.equals("4698") || idValue.equals("4699")|| idValue.equals("4700")
					|| idValue.equals("4701")){
				if (mInterfaceDownloadHardwareDataCallBack != null) {
					downloadHardwareSleepDataRstProc(response);
				}
			}else if(idValue.equals("4702") || idValue.equals("4703")|| idValue.equals("4704")){
				if (mInterfaceGetTiaocanCallBack != null) {
					getTiaocanFileUrlRstProc(response);
				}
			}else if(idValue.equals("4705") || idValue.equals("4706")|| idValue.equals("4707")){
				if (mInterfaceGetHardwareSensitivityCallBack != null) {
					getHardwareSensitivityRstProc(response);
				}
			}else if(idValue.equals("4708") || idValue.equals("4709")|| idValue.equals("4710")
					|| idValue.equals("4711") || idValue.equals("4712")){
				if (mInterfaceMofifyHardwareSensitivityCallBack != null) {
					mofifyHardwareSensitivityRstProc(response);
				}
			}else if(idValue.equals("4713") ||idValue.equals("4714")){
				if(mInterfaceGetBundInfoAtLoginTimeCallBack != null ){
					GetBundInfoAtLoginTimeRstProc(response);
				}
			}
			else {
				requestFailed(Integer.parseInt(idValue) + ":未知错误", Integer.parseInt(idValue));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void ProcJSONDataOnErr(VolleyError error) {
		String errmsg = "访问服务器失败";
		int iCode = 3001;
		
		requestFailed(errmsg, iCode);
		
	}
	private void requestFailed(String errmsg, int iCode) {
		if (m_InterfaceGetCommunityGroupCallBack != null) {
			m_InterfaceGetCommunityGroupCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceAddCommunityGroupCallBack != null) {
			m_InterfaceAddCommunityGroupCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceGetCommunityTopicCallBack != null) {
			m_InterfaceGetCommunityTopicCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceGetCommunityTopicDetailCallBack != null) {
			m_InterfaceGetCommunityTopicDetailCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceTopicPraiseCallBack != null) {
			m_InterfaceTopicPraiseCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceTopicDeleteCallBack != null) {
			m_InterfaceTopicDeleteCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceTopicPostNewCallBack != null) {
			m_InterfaceTopicPostNewCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceTopicReplyPostNewCallBack != null) {
			m_InterfaceTopicReplyPostNewCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceGetGroupMessageCallBack != null) {
			m_InterfaceGetGroupMessageCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceGetGroupDoctorListCallBack != null) {
			m_InterfaceGetGroupDoctorListCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceAddGroupDoctorCallBack != null) {
			m_InterfaceAddGroupDoctorCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceSearchGroupTopicCallBack != null) {
			m_InterfaceSearchGroupTopicCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceReportTopicCallBack != null) {
			m_InterfaceReportTopicCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceEditGroupSummaryCallBack != null) {
			m_InterfaceEditGroupSummaryCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceAttentionGroupCallBack != null) {
			m_InterfaceAttentionGroupCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceCommunityFoundCallBack != null) {
			m_InterfaceCommunityFoundCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceMyTopicByTimeCallBack != null) {
			m_InterfaceMyTopicByTimeCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceMyTopicByGroupCallBack != null) {
			m_InterfaceMyTopicByGroupCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceGetTagListCallBack != null) {
			m_InterfaceGetTagListCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceDeleteTagCallBack != null) {
			m_InterfaceDeleteTagCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceGetHotTagCallBack != null) {
			m_InterfaceGetHotTagCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceCancelAttentionGroupCallBack != null) {
			m_InterfaceCancelAttentionGroupCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceCommunityRecommendRefuseCallBack != null) {
			m_InterfaceCommunityRecommendRefuseCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceCheckUserCreateGroupCallBack != null) {
			m_InterfaceCheckUserCreateGroupCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceSearchTopicCallBack != null) {
			m_InterfaceSearchTopicCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceSearchGroupCallBack != null) {
			m_InterfaceSearchGroupCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceGetCommentLisCallBack != null) {
			m_InterfaceGetCommentLisCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceGetResportLisCallBack != null) {
			m_InterfaceGetResportLisCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceIgnoreResportCallBack != null) {
			m_InterfaceIgnoreResportCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceDeleteResportCallBack != null) {
			m_InterfaceDeleteResportCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceGetAutoLisCallBack != null) {
			m_InterfaceGetAutoLisCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceClearAutoLisCallBack != null) {
			m_InterfaceClearAutoLisCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceAddTagCallBack != null) {
			m_InterfaceAddTagCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceDynamicMsgNumCallBack != null) {
			m_InterfaceDynamicMsgNumCallBack.onError(iCode, errmsg);
		}
		if (m_InterfaceModifyTagCallBack != null) {
			m_InterfaceModifyTagCallBack.onError(iCode, errmsg);
		}
		if (mInterfaceDelGroupDoctorCallBack != null) {
			mInterfaceDelGroupDoctorCallBack.onError(iCode, errmsg);
		}
		if (mInterfaceBoundPhoneCallBack != null) {
			mInterfaceBoundPhoneCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceCancleDoctorCallBack != null){
			mInterfaceCancleDoctorCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceSendMessageCallBack != null){
			mInterfaceSendMessageCallBack.onError(iCode, errmsg);
		}
		if(m_InterfaceRecomGroupListCallBack != null){
			m_InterfaceRecomGroupListCallBack.onError(iCode, errmsg);
		}
		if(m_InterfaceGetAutoLisNewCallBack != null){
			m_InterfaceGetAutoLisNewCallBack.onError(iCode, errmsg);
		}
		if(m_InterfaceUpdateGroupIconCallBack != null){
			m_InterfaceUpdateGroupIconCallBack.onError(iCode, errmsg);
		}
		if(m_InterfaceMyGroupPostTopicListCallBack != null){
			m_InterfaceMyGroupPostTopicListCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceEditUserInfoCallBack != null){
			mInterfaceEditUserInfoCallBack.onError(iCode, errmsg);
		}
		if(m_InterfaceGetWeiBoUserListCallBack != null){
			m_InterfaceGetWeiBoUserListCallBack.onError(iCode, errmsg);
		}
		if(m_InterfaceGetPhoneUserListCallBack != null){
			m_InterfaceGetPhoneUserListCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceDeleteCommentCallBack != null){
			mInterfaceDeleteCommentCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceSetTopTopicCallBack != null){
			mInterfaceSetTopTopicCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceAddExpertFriendCallBack != null){
			mInterfaceAddExpertFriendCallBack.onError(iCode, errmsg);
		}
		//---硬件
		if(mInterfaceFirstPageRecommentGroupCallBack != null){
			mInterfaceFirstPageRecommentGroupCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceGetHardwareBoundDaysCallBack != null){
			mInterfaceGetHardwareBoundDaysCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceHardwareBoundCallBack != null){
			mInterfaceHardwareBoundCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceUploadHardwareDataCallBack1 != null){
			mInterfaceUploadHardwareDataCallBack1.onError(iCode, errmsg);
		}
		if(mInterfaceDownloadHardwareDataCallBack != null){
			mInterfaceDownloadHardwareDataCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceGetTiaocanCallBack != null){
			mInterfaceGetTiaocanCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceGetHardwareSensitivityCallBack != null){
			mInterfaceGetHardwareSensitivityCallBack.onError(iCode, errmsg);
		}
		if(mInterfaceMofifyHardwareSensitivityCallBack != null){
			mInterfaceMofifyHardwareSensitivityCallBack.onError(iCode, errmsg);
		}
		if (mInterfaceGetBundInfoAtLoginTimeCallBack != null) {
			mInterfaceGetBundInfoAtLoginTimeCallBack.onError(iCode, errmsg);
		}
	}

}
