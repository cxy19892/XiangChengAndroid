package com.yzm.sleep.utils;

import java.io.Serializable;
import java.util.List;

import com.yzm.sleep.bean.AccessAttentionUserBean;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.ArticleCommentBean;
import com.yzm.sleep.bean.ArticleDetailBean;
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
import com.yzm.sleep.utils.InterFaceUtilsClass.UserSleepDataListClass;

public class InterFaceUtilsClassNew {
	//===========================================================================
	//1、精选文章接口
	public interface InterfaceHandPickArticleListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,List<CommunityBarnerBean> barner_list, List<PushClasifyBean> pushList, List<ArticleBean> article_list, int totalpage);
	}
	public static class HandPickArticleListParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String page;			//当前页数	默认1
		public String pagesize;		//每页显示多少	默认10
	}
	//========================================================
	
	//2、社区小组接口
	public interface InterfaceGetArticleByTagCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<ArticleBean> list, int totalpage);
	}
		
	//根据tag获取数据
	public static class GetArticleByTagParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String page;			//当前页数	默认1
		public String pagesize;		//每页显示多少	默认10
		public String tagid;
	}
	
	//===========================================================================
	//2、社区小组接口
	public interface InterfaceCommunityGroupListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,List<CommunityGroupBean> push_list,List<ArticleBean> hot_thread, int totalpage);
	}
	public static class CommunityGroupListParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String page;			//当前页数	默认1
		public String pagesize;		//每页显示多少	默认10
	}
	//===========================================================================
	//3、小组的最热、最新话题列表接口
	public interface InterfaceGroupHotNewListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,CommunityGroupBean group_info,List<ArticleBean> article_list,int totalpage);
	}
	
	public static class GroupHotNewListParamClass{
		public String gid;			//小组id	不为空且存在
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String flag;			//最热、最新	1为最热排序；2为最新排序。默认是1.
		public String page;			//当前页数	默认1
		public String pagesize;		//每页显示多少	默认10
	}
	
	//===========================================================================
	//4、文章点赞用户列表接口
	public interface InterfaceArticleThumbUpUserCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,List<ThumbUpUserBean> user_list,int totalpage);
	}
	public static class ArticleThumbUpUserParamClass{
		public String tid;
		public String page;
		public String pagesize;
	}
	//===========================================================================
	//5、小组访问用户、小组关注用户列表接口
	public interface InterfaceGroupAccessAttentionUserListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,List<AccessAttentionUserBean> user_list,int totalpage);
	}
	public static class GroupAccessAttentionUserListParamClass{
		public String gid;			//小组id	不为空且存在
		public String flag;			//访问用户、关注用户	1为访问用户列表；2为关注用户列表。默认是1.
		public String page;			//当前页数	默认1
		public String pagesize;		//每页显示多少	默认10
	}
	//===========================================================================
	//6、修改文章访问数量、修改小组访问数量
	public interface InterfaceModifyAccessNumCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,String strSuccMsg);
	}
	public static class ModifyAccessNumParamClass{
		/**修改文章访问数量传文章id*/
		public String tid;
		/**修改小组访问数量传小组id*/
		public String gid;
		public String my_int_id;
	}
	//===========================================================================
	//7、用户手机登录接口
	public interface InterfacePhoneLoginCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, UserInfoBean userInfo,List<UserInfoPhotoBean> photos);
	}
	//===========================================================================
	//8、查询两个时间段内用户的睡眠数据接口  
	public interface InterfaceQueryUserSleepDataCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<UserSleepDataBean> datas);
	}
	public static class QueryUserSleepDataParamClass{
		public String my_int_id;
		public String sdate;//开始日期	必传，格式为：20151015（年月日）
		public String edate;//结束日期	必传，格式为：20151015（年月日）
		public String flag;//硬件和软件区分标识	1表示取软件数据；2表示取硬件数据。默认是1.
	}
	//===========================================================================
	//9、下载更多铃音列表接口
	public interface InterfaceMoreRingtoneListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<RingtoneBean> lylist);
	}
	//===========================================================================
	//10、用户完善昵称，生日，性别信息接口
	public interface InterfaceCompleteUserInfoCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSuccMsg);
	}
	public static class CompleteUserInfoParamClass{
		public String my_int_id;
		public String nickname;		//用户昵称	必传，格式：2-10个中文字符
		public String userbirthday;		//用户生日	必传，格式：19861226
		public String usersex;	//用户性别	必传，格式：01为男，02为女，03为保密
	}
	//===========================================================================
	//11、文章详情接口
	public interface InterfaceArticleDetailMsgCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, ArticleDetailBean article);
	}
	public static class ArticleDetailMsgParamClass{
		public String tid;
		public String my_int_id;
	}
	//===========================================================================
	//12、文章评论列表接口
	public interface InterfaceArticleCommentListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<ArticleCommentBean> list,int totalpage,String page_id);
	}
	public static class ArticleCommentListParamClass{
		public String tid;//文章id	不为空且存在
		public String my_int_id;//登录用户id	未登录用户 my_int_id=0
		public String page;//当前页数	默认1
		public String pagesize;//每页显示多少	默认10
		public String page_id;
	}
	
	//===========================================================================
	//13、根据用户id，返回用户个人信息。  包括用户基本信息  睡眠数据  发表话题
	public interface InterfaceUserMessageCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,UserMessageBean user_info, List<UserSleepDataBean> sleeplist,List<UserSleepDataBean> sleeplist2,List<ArticleBean> thread_list, int totalpage);
	}
	public static class UserMessageParamClass{
		public String uid;
		public String page;
		public String pagesize;
	}
	//===========================================================================
	//14、香橙推荐更多小组列表接口
	public interface InterfaceRecommmendMoreGroupCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<CommunityGroupBean> push_list, int totalpage);
	}
	public static class RecommmendMoreGroupParamClass{
		public String my_int_id;
		public String page;
		public String pagesize;
	}
	//===========================================================================
	//15、 活动列表接口
	public interface InterfaceEventListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<CommunityEventBean> list, int totalpage);
	}
	public static class EventListParamClass{
		public String my_int_id;
		public String page;
		public String pagesize;
	}
	//===========================================================================
	//16、 活动详情接口
	public interface InterfaceEventDetailCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, CommunityEventDetailBean detail);
	}
	public static class EventDetailParamClass{
		public String my_int_id;//登录用户id	没有登录为0
		public String id;		//活动id
	}
	//===========================================================================
	//17、 绑定极光推送用户接口
	public interface InterfaceBoundJpushCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSuccMsg);
	}
	public static class BoundJpushParamClass{
		public String my_int_id;//登录用户id	
		public String alias;		//设备别名
		public String tag;		//设备标签
		public String registrationid;		//极光注册用户id
	}
	//===========================================================================
	//18、 用户评估记录保存接口
	public interface InterfaceSaveUserEvaluationCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSuccMsg);
	}
	public static class SaveUserEvaluationParamClass{
		public String my_int_id;//登录用户id	
	}
	//===========================================================================
	//19、  更新用户昵称接口
	public interface InterfaceUpdateUserNicknameCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSuccMsg);
	}
	public static class UpdateUserNicknameParamClass{
		public String my_int_id;//登录用户id	
		public String my_int_nickname;
	}
	//===========================================================================
	//20、 返回某天的硬件数据接口
	public interface InterfaceGetDayPillowDataCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<HardwareSleepDataBean> data);
	}
	public static class UploadDakaDaysParamClass{
		public String my_int_id;//登录用户id	
		public String date;  //日期 ，格式：yyyymmdd
	}
	//===========================================================================
	//21、 下载用户连续打卡天数接口
	public interface InterfaceGetPunchDayCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String days);
	}
	//===========================================================================
	//22、 医馆列表接口
	public interface InterfaceClinicListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<ClinicBean> clinicList,int totalpage);
	}
	public static class ClinicListParamClass{
		public String city;
		public String location_x;
		public String location_y;
		public String page;	
		public String pagesize;
	}
	//===========================================================================
	//23、 医馆详情接口
	public interface InterfaceClinicDetailCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, ClinicBean clinic,List<DoctorBean> doctorList, List<TaocanBean> shopList);
	}
	
	//===========================================================================
	//24、 专家列表接口
	public interface InterfaceDoctorListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<DoctorBean> doctorList,int totalpage);
	}
	public static class DoctorListParamClass{
		public String city;
		public String location_x;
		public String location_y;
		public String page;	
		public String pagesize;
	}
	//===========================================================================
	//25、 专家详情接口
	public interface InterfaceDoctorDetailCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, DoctorDetailBean doctor, List<TaocanBean> list);
	}
	//===========================================================================
	//26、 预约接口
	public interface InterfaceReservationCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, ReservationResultBean kf);
	}
	public static class ReservationParamClass{
		public String uid;	
		public String zjuid;	
		public String ygid;
		public String content;
		public String phone;
	}
	//===========================================================================
	//27、 我的预约列表接口
	public interface InterfaceReservationListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<ReservationBean> reserationList, int totalpage);
	}
	public static class ReservationListParamClass{
		public String uid;	
		public String page;
		public String pagesize;
	}
	//===========================================================================
	//28、 预约详情接口
	public interface InterfaceReservationDetailCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, ReservationDetailBean reseration);
	}
	
	//===========================================================================
	//29、 上传用户连续打卡天数接口
	public interface InterfaceUpLoadPunchDayCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String days);
	}
	
	//===========================================================================
	//30、 下载用户最近7天的软件数据。
	public interface InterfaceRecentlySevenDaysDataCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<UserSleepDataListClass> soft_list,List<UserSleepDataListClass> pillow_list);
	}
	
	//===========================================================================
	//31、 获取外部购买链接接口
	public interface InterfaceGetBuyUrlCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSucMsg, String url);
	}
	
	//===========================================================================
	//32、 按用户昵称搜索
	public interface InterfaceGetUserIdByNicknameCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, int totalPage, List<SearchedUserNicknameClass> userList);
	}
	public static class GetUserIdParamClass{
		public String key;	
		public String page;
		public String pagesize;
	}
	public static class SearchedUserNicknameClass implements Serializable {

		private static final long serialVersionUID = 1L;
		public String uid;
		public String user_internal_nickname;
	}
	
	//===========================================================================
	//33、 开启或关闭极光推送
	public interface InterfaceOpenJPushCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSuccMsg);
	}
	public static class OpenJPushParamClass{
		public String uid;	
		public String flag;//1为开启；2关闭。 默认为1
	}
}
