package com.yzm.sleep.utils;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.CitysBean;
import com.yzm.sleep.bean.ClassifyTagBean;
import com.yzm.sleep.bean.CoffoBean;
import com.yzm.sleep.bean.ComfortBean;
import com.yzm.sleep.bean.CommodityDetailBean;
import com.yzm.sleep.bean.DoctorBean;
import com.yzm.sleep.bean.EnvBean;
import com.yzm.sleep.bean.EnvironmentBean;
import com.yzm.sleep.bean.FankuiDataBean;
import com.yzm.sleep.bean.GoodsDetailBean;
import com.yzm.sleep.bean.HomeDataBean;
import com.yzm.sleep.bean.KnowledgeBean;
import com.yzm.sleep.bean.LifeHabitBean;
import com.yzm.sleep.bean.MusicBean;
import com.yzm.sleep.bean.NoteBean;
import com.yzm.sleep.bean.OrderDetailBean;
import com.yzm.sleep.bean.PlanBean;
import com.yzm.sleep.bean.ReportDataBean;
import com.yzm.sleep.bean.RingtoneBean;
import com.yzm.sleep.bean.SaveChatInfoBean;
import com.yzm.sleep.bean.SaveOrderBean;
import com.yzm.sleep.bean.ShopOrderBean;
import com.yzm.sleep.bean.SleepCaseBean;
import com.yzm.sleep.bean.SleepSignDataBean;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.bean.SmokeBean;
import com.yzm.sleep.bean.SportBean;
import com.yzm.sleep.bean.TaocanBean;
import com.yzm.sleep.bean.UserDeliveryBean;
import com.yzm.sleep.bean.WeightBean;
import com.yzm.sleep.bean.WinBean;
import com.yzm.sleep.bean.keywordBean;
import com.yzm.sleep.model.ChangplanBean;


public class InterfaceMallUtillClass {
	//===========================================================================
	//1、套餐商品列表接口
	public interface InterfaceShopCommodityListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,GoodsDetailBean goodesDetail, int totalpage);
	}
	public static class ShopCommodityParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String page;			//当前页数	默认1
		public String pagesize;		//每页显示多少	默认10
		public String tcid;		//套餐id	不为空且存在
	}
	//===========================================================================
	//2、我的订单列表接口
	public interface InterfaceMyOrderListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,List<ShopOrderBean> order_list, String kefutel, int totalpage);
	}
	public static class MyOrderListParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String page;			//当前页数	默认1
		public String pagesize;		//每页显示多少	默认10
	}
	//===========================================================================
	//3、商品详情接口
	public interface InterfaceProductDetailCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,CommodityDetailBean data);
	}
	public static class ProductDetailParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String shopid;		
	}
	//===========================================================================
	//4、确认订单详情页接口
	public interface InterfaceQuRenOrderMsgCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode,OrderDetailBean data);
	}
	public static class QuRenOrderMsgParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String shopid;		
		public String buynums;		
	}
	//===========================================================================
	//5、用户修改收货信息接口
	public interface InterfaceModifyDeliveryAddressCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSuccMsg);
	}
	public static class ModifyDeliveryAddressParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String realname;		//收货人
		public String phone;		
		public String address;		
	}
	//===========================================================================
	//6、保存订单接口
	public interface InterfaceSaveOrderCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, SaveOrderBean order);
	}
	public static class SaveOrderParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String shopid;		//商品id
		public String buy_num;		//购买数量
	}
	//===========================================================================
	//7、用户收货信息接口
	public interface InterfaceDeliveryAddressMsgCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, UserDeliveryBean data);
	}
//	public static class DeliveryAddressMsgParamClass{
//	}
	//===========================================================================
	//8、套餐列表接口
	public interface InterfaceTaocanListCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<TaocanBean> tc_list,int totalpage);
	}
	public static class TaocanMsgParamClass{
		public String my_int_id;	//登录用户id	未登录用户 my_int_id=0
		public String page;		//每页显示多少	默认10
		public String pagesize;		//套餐id	不为空且存在
	}
	//===========================================================================
	//10、保存聊天记录接口
	public interface InterfaceSaveChatInfoCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSuccMsg);
	}
	public static class SaveChatInfoParamClass{
		public String uid;//	string	登录用户id	必填
		public String touid;//	string	聊天对象用户id	必填
		public SaveChatInfoBean message;//	string	封装的内容	
	}
	//===========================================================================
	//11、判断是不是客服接口
	public interface InterfaceCheckIsKefuCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSuccMsg);
	}
	
	//===========================================================================
	//12、已开通城市列表接口
	public interface InterfaceOpenCitysCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<CitysBean> citylist);
	}

	//===========================================================================
	//13、医馆名医列表接口
	public interface InterfaceHosDoctorsCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<DoctorBean> doctorslist, int totalpage);
	}
	
	public static class HosDoctorsParamClass{
		public String page;	//登录用户id	未登录用户 my_int_id=0
		public String pagesize;		//每页显示多少	默认10
		public String ygid;		//套餐id	不为空且存在
	}
	
	public static class DoctorProParamClass{
		public String page;	//登录用户id	未登录用户 my_int_id=0
		public String pagesize;		//每页显示多少	默认10
		public String zjuid;		//套餐id	不为空且存在
	}
	//===========================================================================
	//14、产品服务列表接口
	public interface InterfaceHosProAndSerCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<TaocanBean> taocanlist, int totalpage);
	}
	
	//===========================================================================
	//15、评估结果接口
	public interface InterfaceEstimateCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, JSONObject response);
	}
	
	//===========================================================================
	//16、睡眠指数评估结果页面接口
	public interface InterfaceSleepIndexCallback{
		public void onError(int icode, String strErrMsg);
		/**
		 * 
		 * @param icode
		 * @param sleep_pg 睡眠评估结果
		 * @param shxg_pg  生活习惯评估结果
		 * @param smgl_pg  睡眠规律评估结果
		 * @param smhj_pg  睡眠环境评估结果
		 * @param xlhd_pg  心理活动评估结果
		 */
		public void onSuccess(int icode, String sleep_pg, String shxg_pg, String smgl_pg, String smhj_pg, String xlhd_pg);
	}

	
	//17、收集硬件准确度
	public interface InterfaceCollectionDevDataCallBack{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, String strSuccMsg);
	}
	public static class CollectionDevParamClass{
		public String my_int_id;		//string	登录用户id	不为空且存在
		public String sleep;			//	string	入睡时间	格式：时间戳
		public String wakeup;			//	string	起床时间	格式：时间戳
		public File file;				//	文件	dat文件	二进制流
		public String wakeuptimes;		//	int	中途醒来次数	整数
		public String wakeuplong;		//	string	中途醒来大概时间	
		public String riqi;				//	string	数据所属日期	格式：yyyymmdd
		public String shangchuang;		//上床时间(时间戳)
		public String lichuang;			//离床时间(时间戳)
		public String zhunque;			//string	是否准确	是/否
	}
	
	
	//18.用户每日签到
	public interface InterfaceSignInCallBack{
		public void onSuccess(int icode, JSONObject response);
		public void onError(int icode, String strErrMsg);
	} 
	
	public static class SignInParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 签到日期
		 */
		public String date;
		/**
		 * 签到的内容
		 */
		public String content;
		
		
	}
	
	//19.获取用户签到数据接口
	public interface GetSignInDataCallBack{
		public void onSuccess(int icode, List<SleepStatusBean> list);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class GetSignInDataParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 开始日期
		 */
		public String date;
		/**
		 * 连续几天
		 */
		public String days;
		
		/**
		 * 签到数据类型	(1表示软件数据；2表示硬件数据)
		 */
		public String type;		
	}
	
	//20 .用户7日计划列表
	public interface GetPlanListCallBack{
		public void onSuccess(int icode, List<PlanBean> mlist);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class GetPlanListParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 日期，格式为：20150101 不为空，且小于真实日期
		 */
		public String date;
	}
	
	//21.修改7日计划
	public interface EditPlanListCallBack{
		public void onSuccess(int icode, String strSuccMsg);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class EditPlanListParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * json	封装的结果数据	格式为： {"planid":是否显示,"planid":是否显} （1显示；0不显示）
		 * 如：{"8":0,"9":1}，表示计划ID=8，设置为不显示；计划ID=9设置为显示
		 */
		public String content; 
	}
	
	//22.获取首页数据
	public interface GetHomePageDataCallBack{
//		public void onSuccess(int icode, String pinggu, String sginIn, List<DayReportBean> reportList, List<PlanBean> planList);
		public void onSuccess(int icode, String pinggu, String sginIn, JSONObject response);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class GetHomePageDataParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 日期，格式为：20150101 不为空，且小于真实日期
		 */
		public String date;
		
		/**
		 * (1表示软件数据；2表示硬件数据)
		 */
		public String type;
	}
	
	//23.情绪管理
	public interface MoodManageCallBack{
		public void onSuccess(int icode, String strSuccMsg);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class MoodManageParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * string	将题目和答案封装成字符串	格式：
		 * ti_1##aaaaa&ti_2##bbbbb&ti_3##ccccccc
		 * 说明：①　ti_1##aaaaa表示第一题输入的内容为aaaaa ②　不同题之间用&连接
		 */
		public String content;
	}
	
	//24. 签到感觉好坏提交
	public interface EvaluateSignInCallBack{
		public void onSuccess(int icode, String strSuccMsg);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class EvaluateSignInParams{
		/**
		 * string	签到ID	不为空且存在
		 */
		public String qiandaoid;
		/**
		 * string	1感觉好；-1感觉差
		 */
		public String feeling;
	}
	
	//25. 检查最近7天签到数据是否完整
	public interface CheckSignInCallBack{
		public void onSuccess(int icode, String strSuccMsg);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class CheckSignInParams{
		/**
		 * string	登录用户id	不为空且存在
		 */
		public String my_int_id;
		/**
		 * string	日期，格式为：20150101	不为空，且小于真实日期
		 */
		public String date;
		/**
		 * 签到数据类型	(1表示软件数据；2表示硬件数据)
		 */
		public String type;		
	}
	
	//26. 睡眠报告
	public interface GetSleepReportCallBack{
		public void onSuccess(int icode, String content);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class GetSleepReportParams{
		/**
		 * string	登录用户id	不为空且存在
		 */
		public String my_int_id;
		/**
		 * string	日期，格式为：20150101	不为空，且小于真实日期
		 */
		public String date;
	}
	
	//27. 完成计划
	public interface ComplatePlanCallBack{
		public void onSuccess(int icode, String content);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class ComplatePlanParams{
		/**
		 * string	登录用户id	不为空且存在
		 */
		public String my_int_id;
		/**
		 * string	计划ID	不为空且存在
		 */
		public String planid;
		/**
		 * string 	(1完成；0未完成)	默认是1
		 */
		public String isfinish;
	}
	
	//28. 用户取消一周反馈后调用接口
	public interface CancelFeedbackCallBack{
		public void onSuccess(int icode, String strSuccMsg);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class CancelFeedbackParams{
		/**
		 * string	登录用户id	不为空且存在
		 */
		public String my_int_id;
		/**
		 * string	日期，格式为：20150101	不为空，且小于真实日期
		 */
		public String date;
		/**
		 * 签到数据类型	(1表示软件数据；2表示硬件数据)
		 */
		public String type;		
	}
	
	//29. 用户修改签到
	public interface ModifySignCallBack{
		public void onSuccess(int icode, JSONObject response);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class ModifySignParams{
		/**
		 * string	登录用户id	不为空且存在
		 */
		public String my_int_id;
		/**
		 * 用户签到的ID
		 */
		public String qiandaoid;
		/**
		 * 将题目和答案封装成字符串
		 */
		public String content;
		
	}
	
	//30. 根据日期获取签到选择内容
	public interface GetSignInfoCallBack{
		public void onSuccess(int icode, String response);
		public void onError(int icode, String strErrMsg);
	}
	
	public static class GetSignInfoParams{
		/**
		 * string	登录用户id	不为空且存在
		 */
		public String my_int_id;
		/**
		 * string	日期，格式为：20150101	不为空，且小于真实日期
		 */
		public String date;
		/**
		 * 签到数据类型	(1表示软件数据；2表示硬件数据)
		 */
		public String type;		
	}
	
	//31 放松的铃音列表接口
	public interface InterfaceRelaxRingsCallback{
		public void onError(int icode, String strErrMsg);
		public void onSuccess(int icode, List<RingtoneBean> lylist);
	}
	
	
	//31 放松的铃音列表接口
	public interface UpLoadSetTimeCallback{
		public void onError(String icode, String strErrMsg);
		public void onSuccess(String icode, String strSuccMsg);
	}
	/*-----------------------v4.2.1-------------------------------------*/
	//32.用户每日签到
	public interface InterfaceSignInCallBack4_2_1{
		public void onSuccess(String icode, JSONObject response);
		public void onError(String icode, String strErrMsg);
	} 
	
	//33.睡眠改善方案
	public interface InterfaceImproveSleepCallBack{
		public void onSuccess(String icode, List<SleepCaseBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	
	public static class SignInParams4_2_1{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 签到日期
		 */
		public String date;
		/**
		 * 签到的内容
		 */
		public String content;
	}
	
	//34.睡眠改善方案
	public interface InterfaceHomeDateCallBack{
		public void onSuccess(String icode, JSONObject response);
		public void onError(String icode, String strErrMsg);
	}
	
	
	public static class HomeDateParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 签到日期
		 */
		public String date;
		/**
		 * (1表示软件数据；2表示硬件数据)
		 */
		public String type;
	}
	
	//35.睡眠改善方案
	public interface InterfaceCaseCallBack{
		public void onSuccess(String icode, List<SleepCaseBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	//36.睡眠改善方案
	public interface InterfaceSetSleepTimeCallBack{
		public void onSuccess(String icode, List<SleepCaseBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	public static class SetSleepTimeParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 几点起床	格式为08:30
		 */
		public String wakeup;
		/**
		 * 睡眠时长	格式为483，单位为分钟
		 */
		public String sleeplong;
	}
		
	//37.周反馈接口
	public interface InterfaceSleepFeedbackCallBack{
		public void onSuccess(String icode, String report_ok, String text, List<SleepSignDataBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	public static class SleepFeedbackParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 日期，格式为：20150101	不为空，且小于真实日期
		 */
		public String date;
		/**
		 * (1表示软件数据；2表示硬件数据)
		 */
		public String type;
	}
	
	//38.增加睡眠时长接口
	public interface InterfaceSleepAddLongCallBack{
		public void onSuccess(String icode, String strSuccMsg);
		public void onError(String icode, String strErrMsg);
	}
	
	public static class SleepAddLongParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 增加的睡眠时长	单位为分钟
		 */
		public String datelong;
	}
	
	//39.编辑或者新建用户记事
	public interface InterfaceSaveNoteCallBack{
		public void onSuccess(String icode, String strSuccMsg);
		public void onError(String icode, String strErrMsg);
	}
	
	public static class SaveNoteParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 记事内容
		 */
		public String text;
	}
	
	//40.用户记事列表
	public interface InterfaceSleepNoteListCallBack{
		public void onSuccess(String icode, int totalpage, List<NoteBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	//41.用户记事删除接口
	public interface InterfaceSleepNoteDelCallBack{
		public void onSuccess(String icode, String strSuccMsg);
		public void onError(String icode, String strErrMsg);
	}
	
	//42.获取用户当前记事内容接口
	public interface InterfaceSleepNoteNowCallBack{
		public void onSuccess(String icode, String text);
		public void onError(String icode, String strErrMsg);
	}
	
	//43.获取用户当前记事内容接口
	public interface InterfaceConsoleSaveCallBack{
		public void onSuccess(String icode, String strSuccMsg);
		public void onError(String icode, String strErrMsg);
	}
	
	//44.获取用户当前记事内容接口
	public interface InterfaceSleepCustomsCallBack{
		public void onSuccess(String icode, List<LifeHabitBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	//45.卧室环境
	public interface InterfaceSleepEnvironmentCallBack{
		public void onSuccess(String icode, List<EnvironmentBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	//46.生活习惯选项选中或者取消接口
	public interface InterfaceHabitChoiceCallBack{
		public void onSuccess(String icode, List<LifeHabitBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	public static class HabitChoiceParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * (1烟；2酒；3咖啡因；4运动；5减肥)
		 */
		public String type;
		/**
		 * 选项ID
		 */
		public String choice_id;
		/**
		 * 选项是否被选择	（0取消；1选择）默认为1
		 */
		public String flag;
	}
	
	//46.生活习惯选项选中或者取消接口
	public interface InterfaceMusicListCallBack{
		public void onSuccess(String icode, List<MusicBean> datas, int totalpage);
		public void onError(String icode, String strErrMsg);
	}
	
	//48 
	public interface InterfaceComfortCallBack{
		public void onSuccess(String icode, List<ComfortBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	
	//49
	public interface InterfaceKnowCallBack{
		public void onSuccess(String icode, List<ClassifyTagBean> term_list, List<ArticleBean> article_list,int totalpage);
		public void onError(String icode, String strErrMsg);
	}
	
	
	public static class KnowListParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 生活习惯为2；睡眠环境为3；睡眠常识为4； 睡眠规律为5；心理调节为6；其它。
		 */
		public String term_id;
		/**
		 * 当前页数
		 */
		public String page;
		/**
		 * 每页显示多少
		 */
		public String pagesize;
	}
	
	//50
	public interface InterfaceKnowledgeDetailCallBack{
		public void onSuccess(String icode, KnowledgeBean knowledge);
		public void onError(String icode, String strErrMsg);
	}
	
	//51 knowkey
	public interface InterfaceKnowledgeKeysCallBack{
		public void onSuccess(String icode, List<keywordBean> Keylist);
		public void onError(String icode, String strErrMsg);
	}
	
	//52 knowsearch
	public interface InterfaceKnowledgeSearchCallBack{
		public void onSuccess(String icode, List<ArticleBean> article_list,String totalpage);
		public void onError(String icode, String strErrMsg);
	}
	
	//53. 改善睡眠计划接口
	public interface InterfaceNewSleepplanCallBack{
		public void onSuccess(String icode, List<SleepCaseBean> caseBane);
		public void onError(String icode, String strErrMsg);
	}
	
	//54.获取用户睡眠计划
	public interface InterfaceGetSleepplanCallBack {
		public void onSuccess(String icode, List<SleepCaseBean> planList);
		public void onError(String icode, String strErrMsg);
	}
	
	//55.获取用户睡眠计划
	public interface InterfaceHomeDataCallBack {
		public void onSuccess(String icode, String response, HomeDataBean dataBena);
		public void onError(String icode, String strErrMsg);
	}
	
	//56.每日记录增加睡眠数据
	public interface InterfaceAddRecordCallBack {
		public void onSuccess(String icode, ReportDataBean reportData, List<FankuiDataBean> fankuiData);
		public void onError(String icode, String strErrMsg);
	}
	
	//57.每日记录增加睡眠总体感觉；哪些环境对睡眠有影响；吸烟....等数据
	public interface InterfaceAddRecordFeedbackCallBack {
		public void onSuccess(String icode, ReportDataBean reportData, List<FankuiDataBean> fankuiData);
		public void onError(String icode, String strErrMsg);
	}
	public static class RecordFeedbackParams{
		/**
		 * 当前登陆用户id
		 */
		public String my_int_id;
		/**
		 * 日期，格式为：20150101
		 */
		public String date;//
		/**
		 * 睡眠总体感觉分为5个等级.	每个答案分别对应A,B,C,D,E.....
		 */
		public String feeling;
		/**
		 * 哪些环境对睡眠有影响	每个答案分别对应A,B,C,D,E.....
		 */
		public String customers;
		/**
		 * 吸烟	每个答案分别对应A,B,C,D,E.....
		 */
		public String smoke;
		/**
		 * 咖啡	每个答案分别对应A,B,C,D,E.....
		 */
		public String coffo;
		/**
		 * 酒	每个答案分别对应A,B,C,D,E.....
		 */
		public String wine;
		/**
		 * 运动	每个答案分别对应A,B,C,D,E.....
		 */
		public String sport;
		/**
		 * 减肥	具体的重量
		 */
		public String weight;
		/**
		 * 签到数据类型	(1表示软件数据；2表示硬件数据) 默认是1
		 */
		public String type;
	}

	//58.每日记录增加睡眠总体感觉；哪些环境对睡眠有影响；吸烟....等数据
	public interface InterfaceGetRecordByDateCallBack {
		public void onSuccess(String icode, ReportDataBean reportData, List<FankuiDataBean> datas);
		public void onError(String icode, String strErrMsg);
	}
	
	//59.获取用户睡眠报告
	public interface InterfaceGetReportCallBack {
		public void onSuccess(String icode, ChangplanBean changplanBean, String topText, String plansleep,
				List<SleepSignDataBean> datas, SmokeBean smokeBean, WinBean winBean,
				CoffoBean coffoBean, WeightBean weightBean, SportBean sportBean, EnvBean envBean);
		public void onError(String icode, String strErrMsg);
	}
	
	//60. 数据补充--睡眠数据
	public interface InterfaceSupplyRecordCallBack {
		public void onSuccess(String icode, String strMsg);
		public void onError(String icode, String strErrMsg);
	}
	
	//61. 数据补充--睡眠总体感觉；哪些环境对睡眠有影响；吸烟....等
	public interface InterfaceSupplyFeelRecordCallBack {
		public void onSuccess(String icode, String strMsg);
		public void onError(String icode, String strErrMsg);
	}
	
	//62. 数据补充--睡眠总体感觉；哪些环境对睡眠有影响；吸烟....等
	public interface InterfaceContinuePlanCallBack {
		public void onSuccess(String icode, HomeDataBean dataBena);
		public void onError(String icode, String strErrMsg);
	}
	
	//63. 判断用户是否参与保险
	public interface InterfaceSafeappCallBack {
		public void onSuccess(String icode, int flag, String kefuid);
		public void onError(String icode, String strErrMsg);
	}
	
	//64. 认知疗养
	public interface InterfacePerceiveCallBack {
		public void onSuccess(String icode, List<ComfortBean> datas);
		public void onError(String icode, String strErrMsg);
	}

	//65 环信用户登陆状态
	public interface InterfacLoginStatusHXCallBack {
		public void onSuccess(String icode, String flag);
		public void onError(String icode, String strErrMsg);
	}

	//65 环信用户登陆状态
	public interface InterfacFeedBackCallBack {
		public void onSuccess(String icode, String flag);
		public void onError(String icode, String strErrMsg);
	}

	public static class FeedBackParam{
		public String my_int_id;//          	string	用户ID	不为空且存在
		public String fankui;//          	string	反馈内容	不为空
		public String contact;//	string	联系方式	不为空
		public String version;//	string	产品版本	不为空
		public String phonever;//	string	机型	不为空
		public String romversion;//	string	ROM版本	不为空
		public String network;//	string	网络	不为空
		public String yunying;//	string	运营商	不为空
		public String fenbianl;//	string	分辨率	不为空
		public String platform;//	string	平台（iOS 或者 Andriod）	不为空

		public String getMy_int_id() {
			return my_int_id;
		}

		public void setMy_int_id(String my_int_id) {
			this.my_int_id = my_int_id;
		}

		public String getFankui() {
			return fankui;
		}

		public void setFankui(String fankui) {
			this.fankui = fankui;
		}

		public String getContact() {
			return contact;
		}

		public void setContact(String contact) {
			this.contact = contact;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getPhonever() {
			return phonever;
		}

		public void setPhonever(String phonever) {
			this.phonever = phonever;
		}

		public String getRomversion() {
			return romversion;
		}

		public void setRomversion(String romversion) {
			this.romversion = romversion;
		}

		public String getNetwork() {
			return network;
		}

		public void setNetwork(String network) {
			this.network = network;
		}

		public String getYunying() {
			return yunying;
		}

		public void setYunying(String yunying) {
			this.yunying = yunying;
		}

		public String getFenbianl() {
			return fenbianl;
		}

		public void setFenbianl(String fenbianl) {
			this.fenbianl = fenbianl;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}
	}
}
