package com.yzm.sleep.utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
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
import com.yzm.sleep.bean.PlanListBean;
import com.yzm.sleep.bean.ReportDataBean;
import com.yzm.sleep.bean.RingtoneBean;
import com.yzm.sleep.bean.SaveOrderBean;
import com.yzm.sleep.bean.ShopCommodityBean;
import com.yzm.sleep.bean.ShopOrderBean;
import com.yzm.sleep.bean.SleepCaseBean;
import com.yzm.sleep.bean.SleepSignDataBean;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.bean.SmokeBean;
import com.yzm.sleep.bean.SportBean;
import com.yzm.sleep.bean.TaocanBean;
import com.yzm.sleep.bean.UserBean;
import com.yzm.sleep.bean.UserDeliveryBean;
import com.yzm.sleep.bean.WeightBean;
import com.yzm.sleep.bean.WinBean;
import com.yzm.sleep.bean.keywordBean;
import com.yzm.sleep.model.ChangplanBean;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.utils.InterfaceMallUtillClass.CancelFeedbackCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.CancelFeedbackParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.CheckSignInCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.CheckSignInParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.CollectionDevParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ComplatePlanCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ComplatePlanParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.EditPlanListCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.EditPlanListParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.EvaluateSignInCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.EvaluateSignInParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetHomePageDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetHomePageDataParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetPlanListCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetPlanListParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInfoCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInfoParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSleepReportCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSleepReportParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.HabitChoiceParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.HomeDateParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.HosDoctorsParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceAddRecordCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceAddRecordFeedbackCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceCaseCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceCheckIsKefuCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceCollectionDevDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceComfortCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceConsoleSaveCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceContinuePlanCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceDeliveryAddressMsgCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceEstimateCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceGetRecordByDateCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceGetReportCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceGetSleepplanCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHabitChoiceCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHomeDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHomeDateCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHosDoctorsCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHosProAndSerCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceImproveSleepCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceKnowCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceKnowledgeDetailCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceKnowledgeKeysCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceKnowledgeSearchCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceModifyDeliveryAddressCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceMusicListCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceMyOrderListCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceNewSleepplanCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceOpenCitysCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfacePerceiveCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceProductDetailCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceQuRenOrderMsgCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceRelaxRingsCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSafeappCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSaveChatInfoCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSaveNoteCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSaveOrderCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSetSleepTimeCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceShopCommodityListCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSignInCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSignInCallBack4_2_1;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepAddLongCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepCustomsCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepEnvironmentCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepFeedbackCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepIndexCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepNoteDelCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepNoteListCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepNoteNowCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSupplyFeelRecordCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSupplyRecordCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceTaocanListCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.KnowListParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ModifyDeliveryAddressParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ModifySignCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.MoodManageCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.MoodManageParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.MyOrderListParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ProductDetailParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.QuRenOrderMsgParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SaveChatInfoParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SaveNoteParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SaveOrderParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SetSleepTimeParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ShopCommodityParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SleepAddLongParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SleepFeedbackParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.TaocanMsgParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.UpLoadSetTimeCallback;

public class XiangchengMallProcClass extends HttpDataTranUtils{
	private Context mContext;
	private String errMsgNoneString = "未知错误";
	private String errMsgExceptionString = "未知异常";
	private int errMsgNoneCode = 101;
	private int errMsgExceptionCode = 102;
	
	public XiangchengMallProcClass(Context context){
		this.mContext = context;
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
	
	
	
	// ==========================================================================
	// 1、套餐商品列表接口    /api.php?mod=shoplist

	private String shopCommodityListUrl = g_BaseSiteURL + "api.php?mod=shoplist";
	private InterfaceShopCommodityListCallback m_InterfaceShopCommodityListCallback;

	public void shopCommodityList(ShopCommodityParamClass mParams,
			InterfaceShopCommodityListCallback callBack) {
		m_InterfaceShopCommodityListCallback = callBack;
		String newString = shopCommodityListUrl +
				"&my_int_id=" + mParams.my_int_id +
				"&tcid=" + mParams.tcid +
				"&page=" + mParams.page +
				"&pagesize=" + mParams.pagesize +
				"&xctoken=" + getRequestToken("shoplist") ;
		
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void shopCommodityListRstProc(JSONObject response) {
		String idValue = "";

		try {
			idValue = (String) response.get("response").toString();

			if (idValue.equals("4811")) {

				m_InterfaceShopCommodityListCallback.onError(4811, "用户不存在");

			} else if (idValue.equals("4811:1")) {

				m_InterfaceShopCommodityListCallback.onError(48111, "套餐不存在");

			} else if (idValue.equals("4812")) {
				GoodsDetailBean goodsDetail = new GoodsDetailBean();
				goodsDetail.setTcid((String) response.getString("tcid"));
				goodsDetail.setTcsalenum((String)response.getString("tcsalenum"));
				goodsDetail.setPicture((String) response.getString("picture"));
				goodsDetail.setPicture_key((String) response.getString("picture_key"));
				goodsDetail.setTc_title((String) response.getString("tc_title"));
				goodsDetail.setYgid((String)response.getString("ygid"));
				goodsDetail.setYgname((String)response.getString("ygname"));
				goodsDetail.setYgaddress((String)response.getString("ygaddress"));
				goodsDetail.setZjuid((String)response.getString("zjuid"));
				goodsDetail.setZjname((String)response.getString("zjname"));
				goodsDetail.setZjaddress((String)response.getString("zjaddress"));
				goodsDetail.setKfuid(response.getString("kfuid"));
				goodsDetail.setName(response.getString("name"));
				goodsDetail.setTx(response.getString("tx"));
				goodsDetail.setTx_key(response.getString("tx_key"));
				String listString = (String) response.getString("shop_list");
				Gson gson = new Gson();
				List<ShopCommodityBean> shop_list = gson.fromJson(listString,new TypeToken<List<ShopCommodityBean>>() {}.getType());
				goodsDetail.setShop_list(shop_list);
				int totalpage = (int) response.getInt("totalpage");
				m_InterfaceShopCommodityListCallback.onSuccess(4812, goodsDetail, totalpage);
			}
		} catch (Exception e) {
			m_InterfaceShopCommodityListCallback.onError(errMsgExceptionCode,errMsgExceptionString);
		}
	}

	// ==========================================================================
	// 2、我的订单列表接口   /api.php?mod=shoporderlist
	
	private String myOrderListUrl = g_BaseSiteURL + "api.php?mod=shoporderlist";
	private InterfaceMyOrderListCallback m_InterfaceMyOrderListCallback;
	
	public void myOrderList(MyOrderListParamClass mParams,
			InterfaceMyOrderListCallback callBack) {
		m_InterfaceMyOrderListCallback = callBack;
		String newString = myOrderListUrl +
				"&my_int_id=" + mParams.my_int_id +
				"&page=" + mParams.page +
				"&pagesize=" + mParams.pagesize +
				"&xctoken=" + getRequestToken("shoporderlist");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void myOrderListRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4813")) {
				
				m_InterfaceMyOrderListCallback.onError(4813, "用户不存在");
				
			} else if (idValue.equals("4814")) {
				Gson gson = new Gson();
				String kefutel = response.getString("kefu1");
				String listString = (String) response.getString("order_list");
				List<ShopOrderBean> order_list = gson.fromJson(listString,
						new TypeToken<List<ShopOrderBean>>() {
				}.getType());
				int totalpage = (int) response.getInt("totalpage");
				m_InterfaceMyOrderListCallback.onSuccess(4814, order_list, kefutel, totalpage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceMyOrderListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	// 3、商品详情接口   /api.php?mod=shopdetail  /api.php?mod=shopdetail_new
	
	private String productDetailUrl = g_BaseSiteURL + "api.php?mod=shopdetail_new";
	private InterfaceProductDetailCallback m_InterfaceProductDetailCallback;
	
	public void productDetail(ProductDetailParamClass mParams,
			InterfaceProductDetailCallback callBack) {
		m_InterfaceProductDetailCallback = callBack;
		String newString = productDetailUrl +
				"&my_int_id=" + mParams.my_int_id +
				"&shopid=" + mParams.shopid +
				"&xctoken=" + getRequestToken("shopdetail");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void productDetailRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4815")) {
				
				m_InterfaceProductDetailCallback.onError(4815, "用户不存在");
				
			} else if (idValue.equals("4816")) {
				
				m_InterfaceProductDetailCallback.onError(4816, "商品不存在");
				
			}else if (idValue.equals("4817")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("data");
				CommodityDetailBean data = gson.fromJson(dataString, CommodityDetailBean.class);
				m_InterfaceProductDetailCallback.onSuccess(4817, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceProductDetailCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	// 4、确认订单详情页接口   /api.php?mod=shoporderdetail
	
	private String quRenOrderMsgUrl = g_BaseSiteURL + "api.php?mod=shoporderdetail";
	private InterfaceQuRenOrderMsgCallback m_InterfaceQuRenOrderMsgCallback;
	
	public void quRenOrderMsg(QuRenOrderMsgParamClass mParams,
			InterfaceQuRenOrderMsgCallback callBack) {
		m_InterfaceQuRenOrderMsgCallback = callBack;
		String newString = quRenOrderMsgUrl +
				"&my_int_id=" + mParams.my_int_id +
				"&shopid=" + mParams.shopid +
				"&buynums=" + mParams.buynums +
				"&xctoken=" + getRequestToken("shoporderdetail");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void quRenOrderMsgRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4818")) {
				
				m_InterfaceQuRenOrderMsgCallback.onError(4818, "用户不存在");
				
			} else if (idValue.equals("4819")) {
				
				m_InterfaceQuRenOrderMsgCallback.onError(4819, "购买数量错误");
				
			}else if (idValue.equals("4819:1")) {
				
				m_InterfaceQuRenOrderMsgCallback.onError(48191, "收货信息没有完善");
				
			}else if (idValue.equals("4820")) {
				
				m_InterfaceQuRenOrderMsgCallback.onError(4820, "商品不存在");
				
			}else if (idValue.equals("4821")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("data");
				OrderDetailBean data = gson.fromJson(dataString, OrderDetailBean.class);
				m_InterfaceQuRenOrderMsgCallback.onSuccess(4821, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceProductDetailCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	// 5、用户修改收货信息接口 /api.php?mod=shopmodifyaddress
	
	private String modifyDeliveryAddressUrl = g_BaseSiteURL + "api.php?mod=shopmodifyaddress";
	private InterfaceModifyDeliveryAddressCallback m_InterfaceModifyDeliveryAddressCallback;
	
	public void modifyDeliveryAddress(ModifyDeliveryAddressParamClass mParams,
			InterfaceModifyDeliveryAddressCallback callBack) {
		m_InterfaceModifyDeliveryAddressCallback = callBack;
		String newString = modifyDeliveryAddressUrl +
				"&my_int_id=" + mParams.my_int_id +
				"&realname=" + mParams.realname +
				"&phone=" + mParams.phone +
				"&address=" + mParams.address +
				"&xctoken=" + getRequestToken("shopmodifyaddress");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void modifyDeliveryAddressRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4822")) {
				
				m_InterfaceModifyDeliveryAddressCallback.onError(4822, "用户不存在");
				
			} else if (idValue.equals("4823")) {
				
				m_InterfaceModifyDeliveryAddressCallback.onSuccess(4823, "成功");
				
			}else if (idValue.equals("4824")) {
				m_InterfaceModifyDeliveryAddressCallback.onError(4824, "失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceModifyDeliveryAddressCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	// 6、保存订单接口 /api.php?mod=shopsaveorder
	
//	private String saveOrderUrl = g_BaseSiteURL + "api.php?mod=shopsaveorder";
	private String saveOrderUrl = g_BaseSiteURL + "api.php?";
	private InterfaceSaveOrderCallback m_InterfaceSaveOrderCallback;
	
	public void saveOrder(SaveOrderParamClass mParams,
			InterfaceSaveOrderCallback callBack) {
		m_InterfaceSaveOrderCallback = callBack;
//		String newString = saveOrderUrl +
//				"&my_int_id=" + mParams.my_int_id +
//				"&shopid=" + mParams.shopid +
//				"&order_id=" + mParams.order_id +
//				"&buy_num=" + mParams.buy_num +
//				"&count_price=" + mParams.count_price +
//				"&youhui_price=" + mParams.youhui_price +
//				"&flag=" + mParams.flag +
//				"&xctoken=" + getRequestToken("shopsaveorder");
//		super.requestJosnObjectData(mContext, newString);
		
		RequestParams params = new RequestParams();
		params.put("mod", "shopsaveorder");
		params.put("my_int_id", mParams.my_int_id);
		params.put("shopid", mParams.shopid);
		params.put("buy_num", mParams.buy_num);
		params.put("xctoken", getRequestToken("shopsaveorder"));
		super.postJosnObjectData(mContext, saveOrderUrl, params);
	}
	
	private void saveOrderRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4825")) {
				
				m_InterfaceSaveOrderCallback.onError(4825, "用户不存在");
				
			} else if (idValue.equals("4826")) {
				
				m_InterfaceSaveOrderCallback.onError(4826, "商品不存在");
				
			}else if (idValue.equals("4827")) {
				
				m_InterfaceSaveOrderCallback.onError(4827, "订单号错误");
				
			}else if (idValue.equals("4827:1")) {
				
				m_InterfaceSaveOrderCallback.onError(48271, "订单状态错误");
				
			}else if (idValue.equals("4828")) {
				
				m_InterfaceSaveOrderCallback.onError(4828, "失败");
				
			}else if (idValue.equals("4829")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("data");
				SaveOrderBean data = gson.fromJson(dataString, SaveOrderBean.class);
				m_InterfaceSaveOrderCallback.onSuccess(4829, data);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceSaveOrderCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	// 7、用户收货信息接口  /api.php?mod=shopaddressinfo
	
	private String deliveryAddressMsgUrl = g_BaseSiteURL + "api.php?mod=shopaddressinfo";
	private InterfaceDeliveryAddressMsgCallback m_InterfaceDeliveryAddressMsgCallback;
	
	public void deliveryAddressMsg(String my_int_id,
			InterfaceDeliveryAddressMsgCallback callBack) {
		m_InterfaceDeliveryAddressMsgCallback = callBack;
		String newString = deliveryAddressMsgUrl +
				"&my_int_id=" + my_int_id +
				"&xctoken=" + getRequestToken("shopaddressinfo");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void deliveryAddressMsgRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4830")) {
				
				m_InterfaceDeliveryAddressMsgCallback.onError(4830, "用户不存在");
				
			} else if (idValue.equals("4831")) {
				
				m_InterfaceDeliveryAddressMsgCallback.onError(4831, "失败");
				
			}else if (idValue.equals("4832")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("data");
				UserDeliveryBean data = gson.fromJson(dataString, UserDeliveryBean.class);
				m_InterfaceDeliveryAddressMsgCallback.onSuccess(4832, data);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceDeliveryAddressMsgCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	// 8、套餐列表接口  /api.php?mod=shoptclist
	
	private String taocanListUrl = g_BaseSiteURL + "api.php?mod=shoptclist";
	private InterfaceTaocanListCallback m_InterfaceTaocanListCallback;
	
	public void taocanList(TaocanMsgParamClass mParam,
			InterfaceTaocanListCallback callBack) {
		m_InterfaceTaocanListCallback = callBack;
		String newString = taocanListUrl +
				"&my_int_id=" + mParam.my_int_id +
				"&page=" + mParam.page +
				"&pagesize=" + mParam.pagesize +
				"&xctoken=" + getRequestToken("shoptclist");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void taocanListUrlRstProc(JSONObject response) {
		String idValue = "";
		
		try {
			
			idValue = (String) response.get("response").toString();
			
			if (idValue.equals("4833")) {
				
				m_InterfaceTaocanListCallback.onError(4833, "用户不存在");
				
			} else if (idValue.equals("4834")) {
				
				m_InterfaceTaocanListCallback.onError(4834, "失败");
				
			}else if (idValue.equals("4835")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("tc_list");
				List<TaocanBean> tc_list = gson.fromJson(dataString,new TypeToken<List<TaocanBean>>() {
						}.getType());
				int totalpage = (int) response.getInt("totalpage");
				m_InterfaceTaocanListCallback.onSuccess(4835, tc_list, totalpage);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceTaocanListCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	// 10、保存聊天记录接口 /api.php?mod=sendchat
	
//	private String saveChatInfoUrl = g_BaseSiteURL + "api.php?mod=sendchat";
	private String saveChatInfoUrl = g_BaseSiteURL + "api.php?";
	private InterfaceSaveChatInfoCallback m_InterfaceSaveChatInfoCallback;
	
	public void saveChatInfo(SaveChatInfoParamClass mParam,
			InterfaceSaveChatInfoCallback callBack) {
		m_InterfaceSaveChatInfoCallback = callBack;
//		String newString = saveChatInfoUrl +
//				"&uid=" + mParam.uid +
//				"&touid=" + mParam.touid +
//				"&message=" + new Gson().toJson(mParam.message) +---
//				"&xctoken=" + getRequestToken("sendchat");
//		super.requestJosnObjectData(mContext, newString);
		
		RequestParams params = new RequestParams();
		params.put("mod", "sendchat");
		params.put("uid", mParam.uid);
		params.put("touid", mParam.touid);
		params.put("message", new Gson().toJson(mParam.message));
		params.put("xctoken", getRequestToken("sendchat"));
		super.postJosnObjectData(mContext, saveChatInfoUrl, params);
	}
	
	private void saveChatInfoRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4836")) {
				m_InterfaceSaveChatInfoCallback.onError(4836, "用户不存在");
			} else if (idValue.equals("4837")) {
				m_InterfaceSaveChatInfoCallback.onError(4837, "失败");
			}else if (idValue.equals("4838")) {
				m_InterfaceSaveChatInfoCallback.onSuccess(4838, "成功");
			}else if (idValue.equals("4839")) {
				m_InterfaceSaveChatInfoCallback.onError(4839, "失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceSaveChatInfoCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	// ==========================================================================
	// 11、判断是不是客服接口/api.php?mod=checkkefu
	
	private String checkIsKefuUrl = g_BaseSiteURL + "api.php?mod=checkkefu";
	private InterfaceCheckIsKefuCallback m_InterfaceCheckIsKefuCallback;
	
	public void checkIsKefu(String touid,
			InterfaceCheckIsKefuCallback callBack) {
		m_InterfaceCheckIsKefuCallback = callBack;
		String newString = checkIsKefuUrl +
				"&touid=" + touid +
				"&xctoken=" + getRequestToken("checkkefu");
		super.requestJosnObjectData(mContext, newString);
		
	}
	
	private void checkIsKefuRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4840")) {
				m_InterfaceCheckIsKefuCallback.onSuccess(4840, "0");
			} else if (idValue.equals("4841")) {
				m_InterfaceCheckIsKefuCallback.onSuccess(4841, "1");
			}
		} catch (Exception e) {
			m_InterfaceCheckIsKefuCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	//12、已开通城市列表接口  /api.php?mod=citylist
	private String getOpenCitysUrl = g_BaseSiteURL+"api.php?mod=citylist";
	private InterfaceOpenCitysCallback m_InterfaceOpenCitysCallback;
	public void getOpenCitys(InterfaceOpenCitysCallback callback){
		m_InterfaceOpenCitysCallback = callback;
		String newString = getOpenCitysUrl +
				"&xctoken=" + getRequestToken("citylist");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getOpenCitysRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4842")) {
				m_InterfaceOpenCitysCallback.onError(4842, "获取失败");
			} else if (idValue.equals("4843")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("list");
				List<CitysBean> mlist = gson.fromJson(dataString,new TypeToken<List<CitysBean>>() {
						}.getType());
				m_InterfaceOpenCitysCallback.onSuccess(4841, mlist);
			}
		} catch (Exception e) {
			m_InterfaceOpenCitysCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	//13、医馆名医列表接口  /api.php?mod=yiguanyslist
	private String HosDoctorsUrl = g_BaseSiteURL+"api.php?mod=yiguanyslist";
	private InterfaceHosDoctorsCallback m_InterfaceHosDoctorsCallback;
	public void getHosDoctors(HosDoctorsParamClass mparams, InterfaceHosDoctorsCallback callback){
		m_InterfaceHosDoctorsCallback = callback;
		String newString = HosDoctorsUrl +
				"&page=" + mparams.page+
				"&pagesize=" + mparams.pagesize+
				"&ygid=" + mparams.ygid+
				"&xctoken=" + getRequestToken("yiguanyslist");
		super.requestJosnObjectData(mContext, newString);
	}
	
	
	private void getHosDoctorsRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4844")) {
				m_InterfaceHosDoctorsCallback.onError(4844, "医馆不存在");
			} else if (idValue.equals("4845")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("list");
				List<DoctorBean> mlist = new ArrayList<DoctorBean>();
				if(!dataString.equals("[]")){
					mlist = gson.fromJson(dataString,new TypeToken<List<DoctorBean>>() {
						}.getType());
				}
				int totalpage = (int) response.getInt("totalpage");
				m_InterfaceHosDoctorsCallback.onSuccess(4845, mlist, totalpage);
			}
		} catch (Exception e) {
			m_InterfaceHosDoctorsCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	//14、产品服务列表接口 /api.php?mod=yiguanshoplist
	private String HosShopProUrl = g_BaseSiteURL+"api.php?mod=yiguanshoplist";
	private InterfaceHosProAndSerCallback m_InterfaceHosProAndSerCallback;
	public void getHosShopPro(int type, HosDoctorsParamClass mparams, InterfaceHosProAndSerCallback callback){
		m_InterfaceHosProAndSerCallback = callback;
		String newString = "";
			if(type == 1){
			newString = HosShopProUrl +
					"&page=" + mparams.page+
					"&pagesize=" + mparams.pagesize+
					"&ygid=" + mparams.ygid+
					"&xctoken=" + getRequestToken("yiguanshoplist");
		}else{
			newString = HosShopProUrl +
					"&page=" + mparams.page+
					"&pagesize=" + mparams.pagesize+
					"&zjuid=" + mparams.ygid;
		}
		super.requestJosnObjectData(mContext, newString);
	}
	
	
	private void getHosShopProRstProc(JSONObject response) {
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4846")) {
				m_InterfaceHosProAndSerCallback.onError(4846, "医馆不存在");
			}else if(idValue.equals("4846:1")){
				m_InterfaceHosProAndSerCallback.onError(4846, "专家不存在");
			}else if(idValue.equals("4846:2")){
				m_InterfaceHosProAndSerCallback.onError(4846, "参数错误");
			} else if (idValue.equals("4847")) {
				Gson gson = new Gson();
				String dataString = (String) response.getString("list");
				List<TaocanBean> mlist = gson.fromJson(dataString,new TypeToken<List<TaocanBean>>() {
						}.getType());
				int totalpage = (int) response.getInt("totalpage");
				m_InterfaceHosProAndSerCallback.onSuccess(4846, mlist, totalpage);
			}
		} catch (Exception e) {
			m_InterfaceHosProAndSerCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	//15评估结果接口 /api.php?mod=pingguresult
	private String EstimateUrl = g_BaseSiteURL+"api.php?mod=pingguresult";
	private InterfaceEstimateCallback m_InterfaceEstimateCallback;
	public void getEstimateResult(String my_int_id, String 	type, InterfaceEstimateCallback callback){
		m_InterfaceEstimateCallback = callback;
		
		String newString = EstimateUrl +
				"&my_int_id=" + my_int_id+
				"&pinggutype=" + type+
				"&xctoken=" + getRequestToken("pingguresult");
		
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getEstimateRstProc(JSONObject response) {
		String idValue = "";
		try {
//			System.out.println(response.toString());
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4851")) {
				m_InterfaceEstimateCallback.onError(4851, "用户不存在");
			}else if(idValue.equals("4852")){
				m_InterfaceEstimateCallback.onError(4852, "评估类型错误");
			}else if (idValue.equals("4853")) {
				m_InterfaceEstimateCallback.onSuccess(4853, response);
			}
		} catch (Exception e) {
			m_InterfaceEstimateCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	//16、 睡眠指数评估结果页面接口/api.php?mod=pinggusleep
	private String sleepIndexUrlString = g_BaseSiteURL+"api.php?mod=pinggusleep";
	private InterfaceSleepIndexCallback m_InterfaceSleepIndexCallback;
	public void getSleepIndex(String my_int_id, InterfaceSleepIndexCallback callback){
		m_InterfaceSleepIndexCallback = callback;
		String newString = sleepIndexUrlString +
				"&my_int_id=" + my_int_id+
				"&xctoken=" + getRequestToken("pinggusleep");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getSleepIndexRstProc(JSONObject response){
		String idValue = "";
		try {
//			System.out.println(response.toString());
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4854")) {
				m_InterfaceSleepIndexCallback.onError(4854, "用户不存在");
			}else if (idValue.equals("4855")) {
				String sleepPgString = response.getString("sleep_pg");
				String shxgPgString = response.getString("shxg_pg");
				String smglPgString = response.getString("smgl_pg");
				String smhjPgString = response.getString("smhj_pg");
				String xlhdPgString = response.getString("xlhd_pg");
				m_InterfaceSleepIndexCallback.onSuccess(4855, sleepPgString, shxgPgString, smglPgString, smhjPgString,xlhdPgString);
			}
		} catch (Exception e) {
			m_InterfaceSleepIndexCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	// ==========================================================================
	//17、保存硬件数据接口 /api.php?mod=uploadsyijiandata
	private String collectDataUrlString = g_BaseSiteURL+"api.php?mod=uploadsyijiandata";
	private InterfaceCollectionDevDataCallBack m_InterfaceCollectionDevDataCallBack;
	public void postPillowData(CollectionDevParamClass mparam, InterfaceCollectionDevDataCallBack callback) throws FileNotFoundException{
		m_InterfaceCollectionDevDataCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", mparam.my_int_id);
		params.put("sleep", mparam.sleep);
		params.put("wakeup", mparam.wakeup);
		params.put("file", mparam.file);
		params.put("wakeuptimes", mparam.wakeuptimes);
		params.put("wakeuplong", mparam.wakeuplong);
		params.put("riqi", mparam.riqi);
		params.put("xctoken", getRequestToken("uploadsyijiandata"));
		
		super.postJosnObjectData(mContext, collectDataUrlString, params);
	}
	
	private void getCollectionDevDataRstProc(JSONObject response){
		String idValue = "";
		try {
			idValue = (String) response.get("response").toString();
			if (idValue.equals("4900")) {
				m_InterfaceCollectionDevDataCallBack.onError(4900, "用户不存在");
			}else if(idValue.equals("4901")){
				m_InterfaceCollectionDevDataCallBack.onError(4901, "失败");
			}else if(idValue.equals("4903")){
				m_InterfaceCollectionDevDataCallBack.onError(4903, "文件上传失败");
			}else if (idValue.equals("4902")) {
				m_InterfaceCollectionDevDataCallBack.onSuccess(4855, "成功");
			}
		} catch (Exception e) {
			m_InterfaceCollectionDevDataCallBack.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	///18.用户每日签到接口   /api.php?mod=xcsleepesign
	private String signInUrl = g_BaseSiteURL+"api.php?mod=xcsleepesign";
	private InterfaceSignInCallBack signInCallback;
	/**
	 * 用户每日签到接口
	 * @param my_int_id 登录用户id
	 * @param type string	签到数据类型	(1表示软件数据；2表示硬件数据)
	 * @param signData
	 * @param signInCallback
	 */
	public void signIn(String my_int_id, String type, SignInData signData, InterfaceSignInCallBack signInCallback){
		this.signInCallback = signInCallback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("date", signData.getDate().replace("-", ""));
		params.put("type", type);
		try {
			params.put("content", new StringBuffer()
			.append("ti_1##").append(Long.parseLong(signData.getGoBedTime())/1000)
			.append("&ti_2##").append(Long.parseLong(signData.getTrySleepTime())/1000)
			.append("&ti_3##").append(signData.getHowLongSleepTime())
			.append("&ti_4##").append(signData.getWakeCount())
			.append("&ti_5##").append(signData.getHowLongWakeTime())
			.append("&ti_6##").append(Long.parseLong(signData.getWakeUpTime())/1000)
			.append("&ti_7##").append(signData.getWakeEarlyTime())
			.append("&ti_8##").append(Long.parseLong(signData.getOutBedTime())/1000)
			.append("&ti_9##").append(signData.getDeepsleep())
			.append("&ti_10##").append(signData.getShallowsleep())
			.toString());
			params.put("xctoken", getRequestToken("xcsleepesign"));
		} catch (Exception e) {
		}
		
		super.postJosnObjectData(mContext, signInUrl, params);
	}
	
	private void signInCallBack(JSONObject response){
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if (idValue.equals("4868")) {
				signInCallback.onError(4868, message);
			}else if(idValue.equals("4869")){
				signInCallback.onError(4869, message);
			}else if(idValue.equals("4870")){
				signInCallback.onError(4870, message);
			}else if (idValue.equals("4872")) {
				signInCallback.onSuccess(4872, response);
			}else 
				signInCallback.onError(0, "失败");
		} catch (Exception e) {
			signInCallback.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//19.获取用户签到数据接口 /api.php?mod=xcsleepesignlist
	private String getSignDataInUrl = g_BaseSiteURL + "api.php?mod=xcsleepesignlist";
	private GetSignInDataCallBack getSignInDataCallback;
	public void getSignInData(GetSignInDataParams mParams, GetSignInDataCallBack getSignInDataCallback){
		this.getSignInDataCallback = getSignInDataCallback;
		RequestParams params = new RequestParams();
		params.put("my_int_id",  mParams.my_int_id);
		params.put("date", mParams.date);
		params.put("days", mParams.days);
		params.put("type", mParams.type);
		params.put("xctoken", getRequestToken("xcsleepesignlist"));
		super.postJosnObjectData(mContext, getSignDataInUrl, params);
	}
	
	private void getSignInDataCallBack(JSONObject response){
		try {
			String idValue = (String) response.get("response").toString();
			if (idValue.equals("4876")) {
				getSignInDataCallback.onError(4876, "用户不存在");
			}else if(idValue.equals("4877")){
				getSignInDataCallback.onError(4877, "天数大于0");
			}else if(idValue.equals("4878")){
				getSignInDataCallback.onError(4878, "日期错误");
			}else if (idValue.equals("4879")) {
				String data = response.getString("list");
				Gson gson = new Gson();
				List<SleepStatusBean> list = gson.fromJson(data,new TypeToken<List<SleepStatusBean>>() {}.getType()); 
				getSignInDataCallback.onSuccess(4879, list);
			}else 
				getSignInDataCallback.onError(0, "失败");
		} catch (Exception e) {
			e.printStackTrace();
			getSignInDataCallback.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//20 .用户7日计划列表/api.php?mod=xcsleepplan
	private String getPlanUrl = g_BaseSiteURL + "api.php?mod=xcsleepplan";
	private GetPlanListCallBack getPlanListCallBack;
	/**
	 * 用户7日计划列表
	 * @param mParams
	 * @param getPlanListCallBack
	 */
	public void getPlanList(GetPlanListParams mParams, GetPlanListCallBack getPlanListCallBack){
		this.getPlanListCallBack = getPlanListCallBack;
		String newString = getPlanUrl +
				"&my_int_id=" + mParams.my_int_id+
				"&date=" + mParams.date+
				"&xctoken=" + getRequestToken("xcsleepplan");
		
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void getPlanListCallBack(JSONObject response){
		try {
			String idValue = (String) response.get("response").toString();
			if (idValue.equals("4859")) {
				getPlanListCallBack.onError(4859, "用户不存在");
			}else if(idValue.equals("4860")){
				getPlanListCallBack.onError(4860, "天数大于0");
			}else if(idValue.equals("4861")){
				Gson gson = new Gson();
				String dataString = (String) response.getString("list");
				List<PlanBean> mlist = gson.fromJson(dataString,new TypeToken<List<PlanBean>>() {}.getType());
				getPlanListCallBack.onSuccess(4879, mlist);
			}else if(idValue.equals("4861:1"))
				getPlanListCallBack.onError(-1, "未进行睡眠评估");
			else
				getPlanListCallBack.onError(0, "失败");
		} catch (Exception e) {
			getPlanListCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//21.修改7日计划 /api.php?mod=xcsleepmodifyplan
	private String editPlanUrl = g_BaseSiteURL + "api.php?mod=xcsleepmodifyplan";
	private EditPlanListCallBack editPlanListCallBack;
	/**
	 * 修改7日计划
	 * @param mParams
	 * @param editPlanListCallBack
	 */
	public void modifyPlan(EditPlanListParams mParams, EditPlanListCallBack editPlanListCallBack){
		this.editPlanListCallBack  = editPlanListCallBack;
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("content", mParams.content);
		params.put("xctoken", getRequestToken("xcsleepmodifyplan"));
		super.postJosnObjectData(mContext, editPlanUrl, params);
	}
	
	private void editPlanCallBack(JSONObject response){
		try {
			String idValue = (String) response.get("response").toString();
			if (idValue.equals("4862")) {
				editPlanListCallBack.onSuccess(4862, "成功");
			}else{
				editPlanListCallBack.onError(0, "失败");
			}
		} catch (Exception e) {
			editPlanListCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//22.根据日期获取首页数据接口 /api.php?mod=xcsleepindex
	private String getHomePageDataUrl = g_BaseSiteURL + "api.php?mod=xcsleepindex";
	private GetHomePageDataCallBack getHomePageDataCallBack;
	/**
	 * 根据日期获取首页数据接口
	 * @param mParams
	 * @param getHomePageDataCallBack
	 */
	public void getHomePage(GetHomePageDataParams mParams, GetHomePageDataCallBack getHomePageDataCallBack){
		this.getHomePageDataCallBack = getHomePageDataCallBack;
//		String newString = getHomePageDataUrl +
//				"&my_int_id=" + mParams.my_int_id+
//				"&date=" + mParams.date+
//				"&type=" + mParams.type+
//				"&xctoken=" + getRequestToken("xcsleepindex");
//		super.requestJosnObjectData(mContext, newString);
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("date", mParams.date);
		params.put("type", mParams.type);
		params.put("xctoken", getRequestToken("xcsleepindex"));
		super.postJosnObjectData(mContext, getHomePageDataUrl, params);
	}
	
	private void getHomePageDataCallBack(JSONObject response){
		try {
			String idValue = (String) response.get("response").toString();
			if (idValue.equals("4863")) {
				getHomePageDataCallBack.onError(4863, "用户不存在");
			}else if(idValue.equals("4864")){
				String message = (String) response.get("message").toString();
				getHomePageDataCallBack.onError(4864, message);
			}else if(idValue.equals("4865")){
				String pinggu = "";//response.getString("pinggu");
				String sginIn = "";//response.getString("qiandao");
				getHomePageDataCallBack.onSuccess(4865, pinggu, sginIn, response);
			}else{
				getHomePageDataCallBack.onError(0, "失败");
			}
		} catch (Exception e) {
			getHomePageDataCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//23.情绪管理接口 /api.php?mod=xcsleepemotion 
	private String moodManageUrl = g_BaseSiteURL + "api.php?mod=xcsleepemotion";
	private MoodManageCallBack moodManageCallBack;
	public void moodManage(MoodManageParams mParams, MoodManageCallBack moodManageCallBack){
		this.moodManageCallBack = moodManageCallBack;
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("content", mParams.content);
		params.put("xctoken", getRequestToken("xcsleepemotion"));
		super.postJosnObjectData(mContext, moodManageUrl, params);
	}
	
	private void moocManageCallBack(JSONObject response){
		try {
			String idValue = (String) response.get("response").toString();
			if("4867".equals(idValue)){
				moodManageCallBack.onError(4867, "失败");
			}else if("4866".equals(idValue)){
				moodManageCallBack.onSuccess(4866, "成功");
			}else{
				moodManageCallBack.onError(0, "失败");
			}
		} catch (Exception e) {
			moodManageCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//24.用户每日签到感觉好坏提交接口 /api.php?mod=xcsleepesignfeel
	private String evaluateSignInUrl = g_BaseSiteURL + "api.php?mod=xcsleepesignfeel";
	private EvaluateSignInCallBack evaluateSignInCallBack;
	public void evaluateSignIn(EvaluateSignInParams mParams, EvaluateSignInCallBack evaluateSignInCallBack){
		this.evaluateSignInCallBack = evaluateSignInCallBack;
		RequestParams params = new RequestParams();
		params.put("qiandaoid", mParams.qiandaoid);
		params.put("feeling", mParams.feeling);
		params.put("xctoken", getRequestToken("xcsleepesignfeel"));
		super.postJosnObjectData(mContext, evaluateSignInUrl, params);
	}
	
	private void evaluateSignInCallBack(JSONObject response){
		try {
			String idValue = response.get("response").toString();
			if("4873".equals(idValue)){
				evaluateSignInCallBack.onError(4873, "签到不存在");
			}else if("4875".equals(idValue)){
				evaluateSignInCallBack.onSuccess(4875, "成功");
			}else{
				evaluateSignInCallBack.onError(4874, "失败");
			}
		} catch (Exception e) {
			evaluateSignInCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//25. 检查最近7天签到数据是否完整  /api.php?mod=xcsleepechecksign
	private String checkSignUrl = g_BaseSiteURL + "api.php?mod=xcsleepechecksign";
	private CheckSignInCallBack checkSignInCallBack;
	/**
	 * 检查最近7天签到数据是否完整
	 */
	public void checkSignIn(CheckSignInParams mParams, CheckSignInCallBack checkSignInCallBack){
		this.checkSignInCallBack = checkSignInCallBack;
		String newString = checkSignUrl +
				"&my_int_id=" + mParams.my_int_id+
				"&date=" + mParams.date+
				"&type=" + mParams.type+
				"&xctoken=" + getRequestToken("xcsleepechecksign");
		super.requestJosnObjectData(mContext, newString);
	}
	
	private void checkSignInCallBack(JSONObject response){
		try {
			String idValue = response.get("response").toString();
			if("4881".equals(idValue)){
				checkSignInCallBack.onError(4881, "日期错误");
			}else if("4882".equals(idValue)){
				checkSignInCallBack.onSuccess(4882, "完整");
			}else if("4883".equals(idValue)){
				checkSignInCallBack.onSuccess(4883, "不完整");
			}else {
				checkSignInCallBack.onError(4880, "用户不存在");
			}
			
		} catch (Exception e) {
			checkSignInCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//26. 睡眠报告 /api.php?mod=xcsleepreport
	private String getSleepReportUrl = g_BaseSiteURL + "api.php?mod=xcsleepreport";
	private GetSleepReportCallBack sleepReportCallBack;
	/**
	 *睡眠报告
	 */
	public void getSleepReport(GetSleepReportParams mParams, GetSleepReportCallBack getSleepReportCallBack){
		this.sleepReportCallBack = getSleepReportCallBack;
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("date", mParams.date);
		params.put("xctoken", getRequestToken("xcsleepreport"));
		super.postJosnObjectData(mContext, getSleepReportUrl, params);
	}
	
	private void getSleepReportCallBack(JSONObject response){
		try {
			String idValue = response.get("response").toString();
			if("4885".equals(idValue)){
				sleepReportCallBack.onError(4885, "日期错误");
			}else if("4886".equals(idValue)){
				String content = response.getString("content");
				sleepReportCallBack.onSuccess(4886, content);
			}else{
				sleepReportCallBack.onError(4884, response.get("message").toString());
			}
		} catch (Exception e) {
			sleepReportCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//27. 计划完成   /api.php?mod=xcsleepplanok
	private String complatePlanUrl = g_BaseSiteURL + "api.php?mod=xcsleepplanok";
	private ComplatePlanCallBack complatePlanCallBack;
	/**
	 * 完成计划
	 */
	public void complatePlan(ComplatePlanParams mParams, ComplatePlanCallBack complatePlanCallBack){
		this.complatePlanCallBack = complatePlanCallBack;
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("planid", mParams.planid);
		params.put("isfinish", mParams.isfinish);
		params.put("xctoken", getRequestToken("xcsleepplanok"));
		super.postJosnObjectData(mContext, complatePlanUrl, params);
	}
	
	private void complatePlanCallback(JSONObject response){
		try {
//			LogUtil.d("chen", response.toString());
			String idValue = response.getString("response");
			if("4888".equals(idValue)){
				complatePlanCallBack.onError(4888, "该条计划不存在");
			}else if("4889".equals(idValue)){
				complatePlanCallBack.onSuccess(4889, "成功");
			}else{
				complatePlanCallBack.onError(4887, "用户不存在");
			}
		} catch (Exception e) {
			complatePlanCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//28. 用户取消一周反馈后调用接口  /api.php?mod=xcsleepcancelfk
	private String cancelFeedbackUrl = g_BaseSiteURL + "api.php?mod=xcsleepcancelfk";
	private CancelFeedbackCallBack cancelFeedbackCallBack;
	/**
	 * 用户取消一周反馈后调用接口
	 */
	public void complatePlan(CancelFeedbackParams mParams, CancelFeedbackCallBack cancelFeedbackCallBack){
		this.cancelFeedbackCallBack = cancelFeedbackCallBack;
//		String newString = cancelFeedbackUrl +
//				"&my_int_id=" + mParams.my_int_id+
//				"&date=" + mParams.date+
//				"&type=" + mParams.type+
//				"&xctoken=" + getRequestToken("xcsleepcancelfk");
//		super.requestJosnObjectData(mContext, newString);
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("date", mParams.date);
		params.put("type", mParams.type);
		params.put("xctoken", getRequestToken("xcsleepcancelfk"));
		
		super.postJosnObjectData(mContext, cancelFeedbackUrl, params);
	}
	
	private void cancelFeedbackCallBackSponse(JSONObject response){
		try {
			String idValue = response.get("response").toString();
			if("4891".equals(idValue)){
				cancelFeedbackCallBack.onError(4891, "日期错误");
			}else if("4892".equals(idValue)){
				cancelFeedbackCallBack.onSuccess(4892, "成功");
			}else{
				cancelFeedbackCallBack.onError(4890, "用户不存在");
			}
		} catch (Exception e) {
			cancelFeedbackCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//29.用户修改签到接口 /api.php?mod=xcsleepemodifysign
	private String modifySignUrl = g_BaseSiteURL + "api.php?mod=xcsleepemodifysign";
	private ModifySignCallBack modifySignCallBack;
	/**
	 * 用户修改签到接口 
	 */
	public void  modifySign(String my_int_id, String type, SignInData signData, ModifySignCallBack modifySignCallBack){
		this.modifySignCallBack = modifySignCallBack;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("qiandaoid", signData.getSignInId());
		params.put("type", type);
		try {
			params.put("content", new StringBuffer()
					.append("ti_1##").append(Long.parseLong(signData.getGoBedTime())/1000)
					.append("&ti_2##").append(Long.parseLong(signData.getTrySleepTime())/1000)
					.append("&ti_3##").append(signData.getHowLongSleepTime())
					.append("&ti_4##").append(signData.getWakeCount())
					.append("&ti_5##").append(signData.getHowLongWakeTime())
					.append("&ti_6##").append(Long.parseLong(signData.getWakeUpTime())/1000)
					.append("&ti_7##").append(signData.getWakeEarlyTime())
					.append("&ti_8##").append(Long.parseLong(signData.getOutBedTime())/1000)
					.append("&ti_9##").append(signData.getDeepsleep())
					.append("&ti_10##").append(signData.getShallowsleep())
					.toString());
			params.put("xctoken", getRequestToken("xcsleepesign"));
		} catch (Exception e) {
		}
		super.postJosnObjectData(mContext, modifySignUrl, params);
	}
	
	private void modifySign(JSONObject response){
		try {
			String idValue = response.getString("response");
			String message = response.getString("message");
			if("4894".equals(idValue)){
				modifySignCallBack.onError(4888, message);
			}else if("4895".equals(idValue)){
				modifySignCallBack.onError(4889, message);
			}else if("4896".equals(idValue)){
				modifySignCallBack.onSuccess(4889, response);
			}else{
				modifySignCallBack.onError(4890, message);
			}
		} catch (Exception e) {
			modifySignCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	
	//30. 根据日期获取签到选择内容 /api.php?mod=xcsleepesigninfo
	private String getSignInfoUrl = g_BaseSiteURL + "api.php?mod=xcsleepesigninfo";
	private GetSignInfoCallBack getSignInfoCallBack;
	/**
	 * 根据日期获取签到选择内容
	 */
	public void  getSignInfo(GetSignInfoParams mParams, GetSignInfoCallBack getSignInfoCallBack){
		this.getSignInfoCallBack = getSignInfoCallBack;
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("date", mParams.date);
		params.put("type", mParams.type);
		params.put("xctoken", getRequestToken("xcsleepesigninfo"));
		super.postJosnObjectData(mContext, getSignInfoUrl, params);
	}
	
	private void getSignInfo(JSONObject response){
		try {
			String idValue = response.get("response").toString();
			if("4898".equals(idValue)){
				getSignInfoCallBack.onError(4898, "签到不存在");
			}else if("4899".equals(idValue)){
				String signInfo = response.getString("sign_data");
				getSignInfoCallBack.onSuccess(4899, signInfo);
			}else{
				getSignInfoCallBack.onError(4897, "用户不存在");
			}
		} catch (Exception e) {
			getSignInfoCallBack.onError(errMsgExceptionCode, errMsgExceptionString);
		}
	}
	//
	//31 
	private String RelaxRingsListUrl = g_BaseSiteURL
			+ "api.php?mod=lydownloadsnew";
	private InterfaceRelaxRingsCallback m_InterfaceRelaxRingsCallback;

	public void getRelaxRingtoneList(InterfaceRelaxRingsCallback callBack) {
		m_InterfaceRelaxRingsCallback = callBack;
		String newString = RelaxRingsListUrl + "&xctoken="
				+ getRequestToken("lydownloadsnew");
		super.requestJosnObjectData(mContext, newString);
	}

	private void RelaxRingtoneRstProc(JSONObject response) {
		String idValue = "";

		try {
//			LogUtil.d("chen", response.toString());
			idValue = (String) response.get("response").toString();

			if (idValue.equals("4905")) {
				Gson gson = new Gson();
				String string = (String) response.getString("lylist");
				List<RingtoneBean> lylist = gson.fromJson(string,
						new TypeToken<List<RingtoneBean>>() {
						}.getType());
				m_InterfaceRelaxRingsCallback.onSuccess(4748, lylist);

			}
		} catch (Exception e) {
			e.printStackTrace();
			m_InterfaceRelaxRingsCallback.onError(errMsgExceptionCode,
					errMsgExceptionString);
		}
	}
	
	/**
	 * 上传 设置的时间 /zsly/user_set_date.php
	 */
	private String upLoadSetDateUrl = g_BaseSiteURL+ "zsly/user_set_date.php";
	private UpLoadSetTimeCallback upLoadSetTimeCallback;
	public void upLoadDate(String userId, String sleepTime, String setUpTime, UpLoadSetTimeCallback upLoadSetTimeCallback){
		this.upLoadSetTimeCallback = upLoadSetTimeCallback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", userId);
		params.put("sleep_date", sleepTime);
		params.put("wakeup_date", setUpTime);
		super.postJosnObjectData(mContext, upLoadSetDateUrl, params);
	}
	
	private void upLoadSetTiemCa(JSONObject response){
		try {
			String idValue = (String) response.get("response").toString();
			if("4408".equals(idValue)){
				upLoadSetTimeCallback.onSuccess(idValue, "");
			}else{
				upLoadSetTimeCallback.onError(idValue, "");
			}
		} catch (Exception e) {
			upLoadSetTimeCallback.onError(errMsgExceptionCode+"",errMsgExceptionString);
		}
	}
	
	/*---------------------------v4.2.1---------------------------*/
	
	//32.获取用户签到数据接口 /api.php?mod=sleepesign
	private String signInUrl4_2_1 = g_BaseSiteURL+"api.php?mod=sleepesign";
	private InterfaceSignInCallBack4_2_1 signInCallback4_2_1;
	/**
	 * 用户每日签到接口
	 * @param my_int_id 登录用户id
	 * @param type string	签到数据类型	(1表示软件数据；2表示硬件数据)
	 * @param signData
	 * @param signInCallback
	 */
	public void signIn4_2_1(String my_int_id, String type, SignInData signData, InterfaceSignInCallBack4_2_1 signInCallback4_2_1){
		this.signInCallback4_2_1 = signInCallback4_2_1;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("date", signData.getDate().replace("-", ""));
		params.put("type", type);
		try {
			params.put("content", new StringBuffer()
			.append("ti_1##").append(Long.parseLong(signData.getGoBedTime())/1000)
			.append("&ti_2##").append(Long.parseLong(signData.getTrySleepTime())/1000)
			.append("&ti_3##").append(signData.getHowLongSleepTime())
			.append("&ti_4##").append(signData.getWakeCount())
			.append("&ti_5##").append(signData.getHowLongWakeTime())
			.append("&ti_6##").append(Long.parseLong(signData.getWakeUpTime())/1000)
			.append("&ti_7##").append(signData.getWakeEarlyTime())
			.append("&ti_8##").append(Long.parseLong(signData.getOutBedTime())/1000)
			.append("&ti_9##").append(signData.getDeepsleep())
			.append("&ti_10##").append(signData.getShallowsleep())
			.toString());
			params.put("xctoken", getRequestToken("xcsleepesign"));
		} catch (Exception e) {
		}
		
		super.postJosnObjectData(mContext, signInUrl4_2_1, params);
	}
	
	private void signInCallBack4_2_1(JSONObject response){
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if (idValue.equals("4923")) 
				signInCallback4_2_1.onSuccess(idValue, response);
			else 
				signInCallback4_2_1.onError(idValue, message);
		} catch (Exception e) {
			signInCallback4_2_1.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	//33睡眠改善方案/api.php?mod=sleepchange
	private String improveSleepUrl = g_BaseSiteURL+"/api.php?mod=sleepchange";
	private InterfaceImproveSleepCallBack mInterfaceImproveSleepCallBack;
	
	public void ImproveSleep(String userid , String getuptime, int sleeplenth, String answear, InterfaceImproveSleepCallBack callBack){
		mInterfaceImproveSleepCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", userid);
		params.put("answer1", answear);
		params.put("answer2", getuptime);
		params.put("answer3", sleeplenth);
		params.put("xctoken", getRequestToken("sleepchange"));
		
		super.postJosnObjectData(mContext, improveSleepUrl, params);
	}
	
	private void ImproveSleepRstProc(JSONObject response) {
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4915")){
				String datastr = response.getString("case");
				Gson gson =new Gson();
				List<SleepCaseBean> datas = gson.fromJson(datastr, new TypeToken<List<SleepCaseBean>>(){}.getType());
				mInterfaceImproveSleepCallBack.onSuccess(idValue, datas);
			}else{
				mInterfaceImproveSleepCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			mInterfaceImproveSleepCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//34获取首页数据  /api.php?mod=sleepindex
	private String getHomePageData = g_BaseSiteURL+"/api.php?mod=sleepindex";
	private InterfaceHomeDateCallBack interfaceHomeDateCallBack;
	
	/**
	 * 获取首页数据
	 * @param mParams
	 * @param callBack
	 */
	public void getHomePageData(HomeDateParams mParams, InterfaceHomeDateCallBack callBack){
		this.interfaceHomeDateCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("date", mParams.date);
		params.put("type", mParams.type);
		params.put("xctoken", getRequestToken("sleepindex"));
		
		super.postJosnObjectData(mContext, getHomePageData, params);
	}
	
	private void getHomePageData(JSONObject response) {
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4912")){
				interfaceHomeDateCallBack.onSuccess(idValue, response);
			}else{
				interfaceHomeDateCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceHomeDateCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//35获取睡眠改善方案  /api.php?mod=sleepchangecase
	private String getChangecase = g_BaseSiteURL+"/api.php?mod=sleepchangecase";
	private InterfaceCaseCallBack interfaceCaseCallBack;
	
	/**
	 * 获取睡眠改善方案
	 * @param mParams
	 * @param callBack
	 */
	public void getSleepCaseData(String my_int_id, InterfaceCaseCallBack callBack){
		this.interfaceCaseCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("xctoken", getRequestToken("sleepchangecase"));
		super.postJosnObjectData(mContext, getChangecase, params);
	}
	
	private void getSleepCase(JSONObject response) {
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4917")){
				String datastr = response.getString("case");
				Gson gson =new Gson();
				List<SleepCaseBean> datas = gson.fromJson(datastr, new TypeToken<List<SleepCaseBean>>(){}.getType());
				interfaceCaseCallBack.onSuccess(idValue, datas);
			}else{
				interfaceCaseCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceCaseCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//36设置睡眠时间  /api.php?mod=sleepset
	private String getSetSleepTime = g_BaseSiteURL+"/api.php?mod=sleepset";
	private InterfaceSetSleepTimeCallBack interfaceSetSleepTimeCallBack;
	
	/**
	 * 设置睡眠时间
	 * @param mParams
	 * @param callBack
	 */
	public void setSleepTime(SetSleepTimeParams mParams, InterfaceSetSleepTimeCallBack callBack){
		this.interfaceSetSleepTimeCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("wakeup", mParams.wakeup);
		params.put("sleeplong", mParams.sleeplong);
		params.put("xctoken", getRequestToken("sleepset"));
		
		super.postJosnObjectData(mContext, getSetSleepTime, params);
	}
	
	private void setSleepTimeCallBack(JSONObject response) {
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4919")){
				String datastr = response.getString("case");
				Gson gson =new Gson();
				List<SleepCaseBean> datas = gson.fromJson(datastr, new TypeToken<List<SleepCaseBean>>(){}.getType());
				interfaceSetSleepTimeCallBack.onSuccess(idValue, datas);
			}else{
				interfaceSetSleepTimeCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceSetSleepTimeCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
		
	//37周反馈接口  /api.php?mod=sleepfeedback
	private String sleepfeedback = g_BaseSiteURL+"/api.php?mod=sleepfeedback";
	private InterfaceSleepFeedbackCallBack interfaceSleepFeedbackCallBack;
	
	/**
	 * 周反馈接口
	 * @param mParams
	 * @param callBack
	 */
	public void sleepFeedback(SleepFeedbackParams mParams, InterfaceSleepFeedbackCallBack callBack){
		this.interfaceSleepFeedbackCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("date", mParams.date);
		params.put("type", mParams.type);
		
		params.put("xctoken", getRequestToken("sleepfeedback"));
		
		super.postJosnObjectData(mContext, sleepfeedback, params);
	}
	
	private void sleepFeedbackCallBack(JSONObject response) {
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4926")){
				String text = "";
				if(response.has("text"))
					text = response.getString("text");
				if(response.has("data_list")){
					String datas = response.getString("data_list");
					String report_ok = response.getString("report_ok");
					Gson gson = new Gson();
					List<SleepSignDataBean> list = gson.fromJson(datas, new TypeToken<List<SleepSignDataBean>>(){}.getType());
					interfaceSleepFeedbackCallBack.onSuccess(idValue, report_ok, text, list);
				}else
					interfaceSleepFeedbackCallBack.onError(idValue, errMsgExceptionString);
			}else{
				interfaceSleepFeedbackCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceSleepFeedbackCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//38增加睡眠时长接口 /api.php?mod=sleepaddlong
	private String sleepaddlong = g_BaseSiteURL+"/api.php?mod=sleepaddlong";
	private InterfaceSleepAddLongCallBack interfaceSleepAddLongCallBack;
	
	/**
	 * 增加睡眠时长
	 * @param mParams
	 * @param callBack
	 */
	public void sleepAddLong(SleepAddLongParams mParams, InterfaceSleepAddLongCallBack callBack){
		this.interfaceSleepAddLongCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("datelong", mParams.datelong);
		params.put("xctoken", getRequestToken("sleepaddlong"));
		
		super.postJosnObjectData(mContext, sleepaddlong, params);
	}
	
	private void sleepAddLongCallBack(JSONObject response) {
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4928")){
				interfaceSleepAddLongCallBack.onSuccess(idValue, "成功");
			}else{
				interfaceSleepAddLongCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceSleepAddLongCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//39保存用户记事 接口 /api.php?mod=sleepnotesave
	private String sleepnotesave = g_BaseSiteURL+"/api.php?mod=sleepnotesave";
	private InterfaceSaveNoteCallBack interfaceSaveNoteCallBack;
	
	/**
	 * 增加睡眠时长
	 * @param mParams
	 * @param callBack
	 */
	public void sleepNoteSave (SaveNoteParams mParams, InterfaceSaveNoteCallBack callBack){
		this.interfaceSaveNoteCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("text", mParams.text);
		params.put("xctoken", getRequestToken("sleepnotesave"));
		
		super.postJosnObjectData(mContext, sleepnotesave, params);
	}
	
	private void sleepNoteSaveCallBack(JSONObject response) {
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4930")){
				interfaceSaveNoteCallBack.onSuccess(idValue, "成功");
			}else{
				interfaceSaveNoteCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceSaveNoteCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}

	//40用户记事历史列表 /api.php?mod=sleepnotelist
	private String sleepnotelist = g_BaseSiteURL+"/api.php?mod=sleepnotelist";
	private InterfaceSleepNoteListCallBack interfaceSleepNoteListCallBack;
	
	/**
	 * 用户记事历史列表
	 * @param mParams
	 * @param callBack
	 */
	public void sleepNoteList (String my_int_id, String page, String pagesize, InterfaceSleepNoteListCallBack callBack){
		this.interfaceSleepNoteListCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("page", page);
		params.put("pagesize", pagesize);
		params.put("xctoken", getRequestToken("sleepnotelist"));
		
		super.postJosnObjectData(mContext, sleepnotelist, params);
	}
	
	private void sleepNoteListCallBack(JSONObject response) {
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4932")){
				if(response.has("list")){
					String data = response.getString("list");
					int totalPage = response.getInt("totalpage");
					Gson gson = new Gson();
					List<NoteBean> datas = gson.fromJson(data, new TypeToken<List<NoteBean>>(){}.getType());
					interfaceSleepNoteListCallBack.onSuccess(idValue, totalPage, datas);
				}else
					interfaceSleepNoteListCallBack.onError(idValue, errMsgExceptionString);
			}else{
				interfaceSleepNoteListCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceSleepNoteListCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//41用户记事删除接口/api.php?mod=sleepnotedel
	private String sleepnotedel = g_BaseSiteURL+"/api.php?mod=sleepnotedel";
	private InterfaceSleepNoteDelCallBack interfaceSleepNoteDelCallBack;
	
	/**
	 * 用户记事删除
	 * @param my_int_id
	 * @param noteids 如果要删除多条记事；拼接格式为：12,14,15 .不同的id之间用逗号隔开
	 * @param callBack
	 */
	public void sleepNoteDel (String my_int_id, String noteids, InterfaceSleepNoteDelCallBack callBack){
		this.interfaceSleepNoteDelCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("noteids", noteids);
		params.put("xctoken", getRequestToken("sleepnotedel"));
		
		super.postJosnObjectData(mContext, sleepnotedel, params);
	}
	
	private void sleepNoteDelCallBack(JSONObject response) {
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4934")){
				interfaceSleepNoteDelCallBack.onSuccess(idValue, "成功");
			}else{
				interfaceSleepNoteDelCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceSleepNoteDelCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//42获取用户当前记事内容接口 /api.php?mod=sleepnotenow
	private String sleepnotenow = g_BaseSiteURL+"/api.php?mod=sleepnotenow";
	private InterfaceSleepNoteNowCallBack interfaceSleepNoteNowCallBack;
	
	/**
	 * 获取用户当前记事内容
	 * @param my_int_id
	 * @param callBack
	 */
	public void sleepNoteNow (String my_int_id, InterfaceSleepNoteNowCallBack callBack){
		this.interfaceSleepNoteNowCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("xctoken", getRequestToken("sleepnotenow"));
		
		super.postJosnObjectData(mContext, sleepnotenow, params);
	}
	
	private void sleepNoteNowCallBack(JSONObject response) {
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4936")){
				String data = "";
				if(response.has("text"))
					data = response.getString("text");
				interfaceSleepNoteNowCallBack.onSuccess(idValue, data);
			}else{
				interfaceSleepNoteNowCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceSleepNoteNowCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//43保存安慰接口 /api.php?mod=sleepconsolesave
	private String sleepconsolesave = g_BaseSiteURL+"/api.php?mod=sleepconsolesave";
	private InterfaceConsoleSaveCallBack interfaceConsoleSaveCallBack;
	
	/**
	 * 保存安慰
	 * @param my_int_id
	 * @param callBack
	 */
	public void sleepConsoleSave (String my_int_id, String text, InterfaceConsoleSaveCallBack callBack){
		this.interfaceConsoleSaveCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("text", text);
		params.put("xctoken", getRequestToken("sleepconsolesave"));
		
		super.postJosnObjectData(mContext, sleepconsolesave, params);
	}
	
	private void sleepConsoleSaveCallBack(JSONObject response) {
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4938")){
				interfaceConsoleSaveCallBack.onSuccess(idValue, "成功");
			}else{
				interfaceConsoleSaveCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceConsoleSaveCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//44生活习惯接口 /api.php?mod=sleepcustoms
	private String sleepcustoms = g_BaseSiteURL+"/api.php?mod=sleepcustoms";
	private InterfaceSleepCustomsCallBack interfaceSleepCustomsCallBack;
	
	/**
	 * 生活习惯
	 * @param my_int_id
	 * @param callBack
	 */
	public void sleepCustoms(String my_int_id, InterfaceSleepCustomsCallBack callBack){
		this.interfaceSleepCustomsCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("xctoken", getRequestToken("sleepcustoms"));
		
		super.postJosnObjectData(mContext, sleepcustoms, params);
	}
	
	private void sleepCustomsCallBack(JSONObject response) {
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4940")){
				String  dataStr= response.getString("customer");
				Gson gson = new Gson();
				List<LifeHabitBean> datas = gson.fromJson(dataStr, new TypeToken<List<LifeHabitBean>>(){}.getType());
				interfaceSleepCustomsCallBack.onSuccess(idValue, datas);
			}else{
				interfaceSleepCustomsCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			LogUtil.i("tianxun", e.getMessage());
			interfaceSleepCustomsCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//45卧室环境接口 /api.php?mod=sleepenvironment
	private String sleepenvironment = g_BaseSiteURL+"/api.php?mod=sleepenvironment";
	private InterfaceSleepEnvironmentCallBack interfaceSleepEnvironmentCallBack;
	
	/**
	 * 卧室环境
	 * @param my_int_id
	 * @param callBack
	 */
	public void sleepEnvironment(String my_int_id, InterfaceSleepEnvironmentCallBack callBack){
		this.interfaceSleepEnvironmentCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("xctoken", getRequestToken("sleepEnvironment"));
		
		super.postJosnObjectData(mContext, sleepenvironment, params);
	}
	
	private void sleepEnvironmentCallBack(JSONObject response) {
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4942")){
				String  datas = response.getString("environment");
				Gson gson = new Gson();
				List<EnvironmentBean> list = gson.fromJson(datas, new TypeToken<List<EnvironmentBean>>(){}.getType());
				interfaceSleepEnvironmentCallBack.onSuccess(idValue, list);
			}else{
				interfaceSleepEnvironmentCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceSleepEnvironmentCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}	
	
	//46生活习惯选项选中或者取消接口 /api.php?mod=sleepchoice
	private String habitchoice = g_BaseSiteURL+"/api.php?mod=sleepchoice";
	private InterfaceHabitChoiceCallBack interfaceHabitChoiceCallBack;
	
	/**
	 * 生活习惯选项选中或者取消
	 * @param my_int_id
	 * @param callBack
	 */
	public void habitChoice(HabitChoiceParams mParams, InterfaceHabitChoiceCallBack callBack){
		this.interfaceHabitChoiceCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("type", mParams.type);
		params.put("choice_id", mParams.choice_id);
		params.put("flag", mParams.flag);
		params.put("xctoken", getRequestToken("sleepchoice"));
		
		super.postJosnObjectData(mContext, habitchoice, params);
	}
	
	private void habitChoiceCallBack(JSONObject response) {
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4945")){
				String  dataStr= response.getString("customer");
				Gson gson = new Gson();
				List<LifeHabitBean> datas = gson.fromJson(dataStr, new TypeToken<List<LifeHabitBean>>(){}.getType());
				interfaceHabitChoiceCallBack.onSuccess(idValue, datas);
			}else{
				interfaceHabitChoiceCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceHabitChoiceCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//47音乐助眠列表 /api.php?mod=sleeplylist
	private String sleeplylist = g_BaseSiteURL+"/api.php?mod=sleeplylist";
	private InterfaceMusicListCallBack interfaceMusicListCallBack;
		
	/**
	 * 音乐助眠列表
	 * @param my_int_id
	 * @param callBack
	 */
	public void getMusicList(String myId, String page, String pageSize, InterfaceMusicListCallBack callBack){
		this.interfaceMusicListCallBack = callBack;
		
		RequestParams params = new RequestParams();
		params.put("my_int_id", myId);
		params.put("page", page);
		params.put("pagesize", pageSize);
		params.put("xctoken", getRequestToken("sleeplylist"));
		
		super.postJosnObjectData(mContext, sleeplylist, params);
	}
	
	private void musicListCallBack(JSONObject response) {
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4947")){
				String  dataStr= response.getString("list");
				int totalPage = response.getInt("totalpage");
				Gson gson = new Gson();
				List<MusicBean> datas = gson.fromJson(dataStr, new TypeToken<List<MusicBean>>(){}.getType());
				interfaceMusicListCallBack.onSuccess(idValue, datas, totalPage);
			}else{
				interfaceMusicListCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			interfaceMusicListCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
			
	//48.安慰首页列表
	private String comfortListUrl = g_BaseSiteURL+"/api.php?mod=sleepanweilist";
	private InterfaceComfortCallBack mInterfaceComfortCallBack;
	public void getComfortList(String userid, InterfaceComfortCallBack callBack){
		mInterfaceComfortCallBack = callBack;
		RequestParams params = new RequestParams();
		params.put("my_int_id", userid);
		
		super.postJosnObjectData(mContext, comfortListUrl, params);
	}
	
	
	private void ComfortResponse(JSONObject response){
		try {
		String idValue =  response.getString("response");
		String message = response.getString("message");
		if(idValue.equals("4949")){
			String  dataStr= response.getString("list");
//			LogUtil.d("chen", dataStr);
			Gson gson = new Gson();
			List<ComfortBean> datas = gson.fromJson(dataStr, new TypeToken<List<ComfortBean>>(){}.getType());
			mInterfaceComfortCallBack.onSuccess(idValue, datas);
		}else{
			mInterfaceComfortCallBack.onError(idValue, message);
		}
		} catch (Exception e) {
			mInterfaceComfortCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	
	//49.知识列表接口
	private String knowlistUrl = g_BaseSiteURL+"/articleapi.php?mod=knowlist";
	private InterfaceKnowCallBack mInterfaceKnowCallBack;
	public void getknowList(KnowListParams mParams, InterfaceKnowCallBack callBack){
		mInterfaceKnowCallBack = callBack;
		RequestParams params = new RequestParams();
		params.put("my_int_id", mParams.my_int_id);
		params.put("term_id", mParams.term_id);
		params.put("page", mParams.page);
		params.put("pagesize", mParams.pagesize);
		
		super.postJosnObjectData(mContext, knowlistUrl, params);
	}
	
	
	private void knowlistResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			if("4960".equals(idValue)){
				Gson gson = new Gson();
				int totalpage = response.getInt("totalpage");
				String datas1 = response.getString("term_list");
				String datas2 = response.getString("article_list");
				List<ClassifyTagBean> term_list = gson.fromJson(datas1, new TypeToken<List<ClassifyTagBean>>(){}.getType());
				List<ArticleBean> article_list = gson.fromJson(datas2, new TypeToken<List<ArticleBean>>(){}.getType());
				mInterfaceKnowCallBack.onSuccess(idValue, term_list, article_list, totalpage);
			}else{
				String message = response.getString("message");
				mInterfaceKnowCallBack.onError(idValue, message);
			}
		} catch (Exception e) {
			mInterfaceKnowCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
		
	//50、知识详情   /articleapi.php?mod=knowdetail
	private String knowDetailUrl = g_BaseSiteURL+ "/articleapi.php?mod=knowdetail";
	private InterfaceKnowledgeDetailCallBack mInterfaceKnowledgeDetailCallBack;
	public void getKnowledgeDetail(String userid, String object_id, InterfaceKnowledgeDetailCallBack callback){
		mInterfaceKnowledgeDetailCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", userid);
		params.put("object_id", object_id);
		params.put("xctoken", getRequestToken("knowdetail"));
		super.postJosnObjectData(mContext, knowDetailUrl, params);
	}
	
	private void knowDetailResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			if(idValue.equals("4962")){
				Gson gson = new Gson();
				String dataString = response.getString("data");
				KnowledgeBean detail = gson.fromJson(dataString, new TypeToken<KnowledgeBean>(){}.getType());
				mInterfaceKnowledgeDetailCallBack.onSuccess(idValue, detail);
			}else{
				String message = response.getString("message");
				mInterfaceKnowledgeDetailCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			mInterfaceKnowledgeDetailCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//51、大家都在搜（关键词）列表  /articleapi.php?mod=knowkey
	private String knowKeysUrl = g_BaseSiteURL+ "/articleapi.php?mod=knowkey";
	private InterfaceKnowledgeKeysCallBack mInterfaceKnowledgeKeysCallBack;
	public void getKnowKeys(String my_int_id, InterfaceKnowledgeKeysCallBack callback){
		mInterfaceKnowledgeKeysCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("xctoken", getRequestToken("knowkey"));
		super.postJosnObjectData(mContext, knowKeysUrl, params);
	}
	
	private void knowKeysResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			if(idValue.equals("4963")){
				Gson gson = new Gson();
				String dataString = response.getString("list");
				List<keywordBean> Keylist = gson.fromJson(dataString, new TypeToken<List<keywordBean>>(){}.getType());
				mInterfaceKnowledgeKeysCallBack.onSuccess(idValue, Keylist);
			}else{
				String message = response.getString("message");
				mInterfaceKnowledgeKeysCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			mInterfaceKnowledgeKeysCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//52、根据标题或者关键字搜索数据列表 /articleapi.php?mod=knowsearch
	
	private String knowSearchUrl = g_BaseSiteURL+ "/articleapi.php?mod=knowsearch";
	private InterfaceKnowledgeSearchCallBack mInterfaceKnowledgeSearchCallBack;
	public void getKnowSearch(String keywords , String page, int pagesize, InterfaceKnowledgeSearchCallBack callback){
		mInterfaceKnowledgeSearchCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("keywords", keywords);
//		params.put("type", "");
		params.put("page", page);
		params.put("pagesize", pagesize);
		params.put("xctoken", getRequestToken("knowsearch"));
		super.postJosnObjectData(mContext, knowSearchUrl, params);
	}
	
	private void knowSearchResponse(JSONObject response){
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			if(idValue.equals("4965")){
				String totalpage =  response.getString("totalpage");
				Gson gson = new Gson();
				String dataString = response.getString("article_list");
				List<ArticleBean> articlelist = gson.fromJson(dataString, new TypeToken<List<ArticleBean>>(){}.getType());
				mInterfaceKnowledgeSearchCallBack.onSuccess(idValue, articlelist, totalpage);
			}else{
				String message = response.getString("message");
				mInterfaceKnowledgeSearchCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			mInterfaceKnowledgeSearchCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//53、改善睡眠计划接口/api.php?mod=newsleepplanadd
	private String newsleepplanaddUrl = g_BaseSiteURL+ "api.php?mod=newsleepplanadd";
	private InterfaceNewSleepplanCallBack mInterfaceNewSleepplanCallBack;
	/**
	 * 改善睡眠计划
	 * @param my_int_id 登陆用户id
	 * @param wakeup 几点起床	格式为08:30
	 * @param sleeplong 睡眠时长	格式为483，单位为分钟
	 * @param customer 最近一周是否有以下习惯?	各答案依次对应A，B，C，D......。多选答案之间用逗号隔开, 如: B,C
	 * @param flag 1表示新增； 2表示重置。  （默认为1，新增操作）
	 */
	public void newSleepPlan(String my_int_id , String wakeup, String sleeplong, String customer, String flag, InterfaceNewSleepplanCallBack callback){
		mInterfaceNewSleepplanCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("wakeup", wakeup);
		params.put("sleeplong", sleeplong);
		params.put("customer", customer);
		params.put("flag", flag);
		params.put("xctoken", getRequestToken("newsleepplanadd"));
		super.postJosnObjectData(mContext, newsleepplanaddUrl, params);
	}
	
	private void newSleepPlanResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			if(idValue.equals("4972")){
				Gson gson = new Gson();
				String dataString = response.getString("data");
				List<SleepCaseBean> caseList = gson.fromJson(dataString, new TypeToken<List<SleepCaseBean>>(){}.getType());
				mInterfaceNewSleepplanCallBack.onSuccess(idValue, caseList);
			}else{
				String message = response.getString("message");
				mInterfaceNewSleepplanCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			mInterfaceNewSleepplanCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//54、 获取用户睡眠计划/api.php?mod=newsleepplanbyuid
	private String getsleepplanUrl = g_BaseSiteURL+ "api.php?mod=newsleepplanbyuid";
	private InterfaceGetSleepplanCallBack mInterfaceGetSleepplanCallBack;
	/**
	 * 获取用户睡眠计划
	 * @param my_int_id 登陆用户id
	 */
	public void getSleepPlanById(String my_int_id, InterfaceGetSleepplanCallBack callback){
		mInterfaceGetSleepplanCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("xctoken", getRequestToken("newsleepplanbyuid"));
		super.postJosnObjectData(mContext, getsleepplanUrl, params);
	}
	
	private void getSleepPlanResponse(JSONObject response){
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			if(idValue.equals("4974")){
				Gson gson = new Gson();
				String dataString = response.getString("data");
				List<SleepCaseBean> planList = gson.fromJson(dataString, new TypeToken<List<SleepCaseBean>>(){}.getType());
				mInterfaceGetSleepplanCallBack.onSuccess(idValue, planList);
			}else{
				String message = response.getString("message");
				mInterfaceGetSleepplanCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			mInterfaceGetSleepplanCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	//55、首页数据/api.php?mod=newsleepindex
	private String homePadeDataUrl = g_BaseSiteURL+ "api.php?mod=newsleepindex";
	private InterfaceHomeDataCallBack mInterfaceHomeDataCallBack;
	/**
	 * 获取用户睡眠计划
	 * @param my_int_id 登陆用户id
	 */
	public void getHomeData(String my_int_id, String type, InterfaceHomeDataCallBack callback){
		mInterfaceHomeDataCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("type", type);
		params.put("xctoken", getRequestToken("newsleepindex"));
		super.postJosnObjectData(mContext, homePadeDataUrl, params);
	}
	
	private void getHomeDataResponse(JSONObject response){
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			if(idValue.equals("4976")){
				Gson gson = new Gson();
				HomeDataBean dataBena = new HomeDataBean();
				dataBena.setNums(response.getString("nums"));
				dataBena.setZongjie_flag(response.getInt("zongjie_flag"));
				dataBena.setPlan_flag(response.getInt("plan_flag"));
				dataBena.setData_flag(response.getInt("data_flag"));
				dataBena.setFeel_flag(response.getInt("feel_flag"));
				dataBena.setEnv_flag(response.getInt("env_flag"));
				dataBena.setSmoke_flag(response.getInt("smoke_flag"));
				dataBena.setWin_flag(response.getInt("win_flag"));
				dataBena.setCoffo_flag(response.getInt("coffo_flag"));
				dataBena.setWeight_flag(response.getInt("weight_flag"));
				dataBena.setIndex_flag(response.getInt("index_flag"));
				dataBena.setSport_flag(response.getInt("sport_flag"));
				dataBena.setReport_before(response.getInt("report_before"));
				dataBena.setSleep(response.getString("sleep"));
				dataBena.setWakeup(response.getString("wakeup"));

				String planListStr = response.getString("plan_list");
				List<PlanListBean> planList = gson.fromJson(planListStr, new TypeToken<List<PlanListBean>>(){}.getType());
				dataBena.setPlan_list(planList);
				
				String userStr = response.getString("user_list");
				List<UserBean> userList = gson.fromJson(userStr, new TypeToken<List<UserBean>>(){}.getType());
				dataBena.setUser_list(userList);
				
				String planStr = response.getString("plan");
				if(!TextUtils.isEmpty(planStr) && !planStr.equals("[]")){
					SleepCaseBean sleepCase = gson.fromJson(planStr, new TypeToken<SleepCaseBean>(){}.getType());
					dataBena.setPlan(sleepCase);
				}
				mInterfaceHomeDataCallBack.onSuccess(idValue, response.toString(), dataBena);
			}else{
				String message = response.getString("message");
				mInterfaceHomeDataCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			mInterfaceHomeDataCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//56、每日记录增加睡眠数据/api.php?mod=newsleepjiluadd
	private String jiluaddUrl = g_BaseSiteURL+ "api.php?mod=newsleepjiluadd";
	private InterfaceAddRecordCallBack mInterfaceAddRecordCallBack;
	/**
	 * 每日记录增加睡眠数据
	 * @param my_int_id 登陆用户id
	 * @param date 日期，格式为：20150101	不为空，且小于真实日期
	 * @param content 将题目和答案封装成字符串
	 * @param type 签到数据类型
	 */
	public void addRecordData(String my_int_id, String type, SignInData signData, InterfaceAddRecordCallBack callback){
		mInterfaceAddRecordCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("date", signData.getDate().replace("-", ""));
		params.put("type", type);
		try {
			params.put("content", new StringBuffer()
			.append("ti_1##").append(Long.parseLong(signData.getGoBedTime())/1000)
			.append("&ti_2##").append(Long.parseLong(signData.getTrySleepTime())/1000)
			.append("&ti_3##").append(signData.getHowLongSleepTime())
			.append("&ti_4##").append(signData.getWakeCount())
			.append("&ti_5##").append(signData.getHowLongWakeTime())
			.append("&ti_6##").append(Long.parseLong(signData.getWakeUpTime())/1000)
			.append("&ti_7##").append(signData.getWakeEarlyTime())
			.append("&ti_8##").append(Long.parseLong(signData.getOutBedTime())/1000)
			.append("&ti_9##").append(signData.getDeepsleep())
			.append("&ti_10##").append(signData.getShallowsleep())
			.toString());
		} catch (Exception e) {
		}
		params.put("xctoken", getRequestToken("newsleepjiluadd"));
		super.postJosnObjectData(mContext, jiluaddUrl, params);
	}
	

	private void addRecordResponse(JSONObject response){
		LogUtil.i("tx", response.toString());
		try {
			String idValue =  response.getString("response");
			if(idValue.equals("4980")){
				Gson gson = new Gson();
				String report_data = response.getString("report_data");
				ReportDataBean reportData= gson.fromJson(report_data, ReportDataBean.class);
				String fankui_data = response.getString("fankui_data");
				List<FankuiDataBean> fankuiData = gson.fromJson(fankui_data, new TypeToken<List<FankuiDataBean>>(){}.getType());
				mInterfaceAddRecordCallBack.onSuccess(idValue, reportData, fankuiData);
			}else{
				String message = response.getString("message");
				mInterfaceAddRecordCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			mInterfaceAddRecordCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//57、每日记录增加睡眠总体感觉；哪些环境对睡眠有影响；吸烟....等数据/api.php?mod=newsleepjiluadd
	private String jiluadd2Url = g_BaseSiteURL+ "api.php?mod=newsleepjiluadd2";
	private InterfaceAddRecordFeedbackCallBack mInterfaceAddRecordFeedbackCallBack;
	/**
	 * 每日记录增加睡眠总体感觉；哪些环境对睡眠有影响；吸烟....等数据
	 */
	public void addRecordFeedback(RequestParams params, InterfaceAddRecordFeedbackCallBack callback){
		mInterfaceAddRecordFeedbackCallBack = callback;
		params.put("xctoken", getRequestToken("newsleepjiluadd2"));
		super.postJosnObjectData(mContext, jiluadd2Url, params);
	}

	private void addRecordFeedbackResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			if(idValue.equals("4983")){
				Gson gson = new Gson();
				String report_data = response.getString("report_data");
				ReportDataBean reportData= gson.fromJson(report_data, ReportDataBean.class);
				String fankui_data = response.getString("fankui_data");
				List<FankuiDataBean> fankuiData = gson.fromJson(fankui_data, new TypeToken<List<FankuiDataBean>>(){}.getType());
				mInterfaceAddRecordFeedbackCallBack.onSuccess(idValue, reportData, fankuiData);
			}else{
				String message = response.getString("message");
				mInterfaceAddRecordFeedbackCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			mInterfaceAddRecordFeedbackCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//58、根据日期获取用户每日记录 /api.php?mod=newsleepjilu
	private String newsleepjiluUrl = g_BaseSiteURL+ "api.php?mod=newsleepjilu";
	private InterfaceGetRecordByDateCallBack mInterfaceGetRecordByDateCallBack;
	/**
	 * 根据日期获取用户每日记录 
	 * @param my_int_id 登陆用户id
	 * @param date 日期，格式为：20150101
	 * @param type 数据类型 (1表示软件数据；2表示硬件数据)默认为1
	 */
	public void getRecordByDate(String my_int_id, String date, String type, InterfaceGetRecordByDateCallBack callback){
		mInterfaceGetRecordByDateCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id",my_int_id);
		params.put("date", date);
		params.put("type", type);
		params.put("xctoken", getRequestToken("newsleepjilu"));
		super.postJosnObjectData(mContext, newsleepjiluUrl, params);
	}

	private void getRecordResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			if(idValue.equals("4986")){
				Gson gson = new Gson();
				String report_data = response.getString("report_data");
				ReportDataBean reportData= gson.fromJson(report_data, ReportDataBean.class);
				String fankui_data = response.getString("fankui_data");
				List<FankuiDataBean> fankuiData = gson.fromJson(fankui_data, new TypeToken<List<FankuiDataBean>>(){}.getType());
				mInterfaceGetRecordByDateCallBack.onSuccess(idValue, reportData, fankuiData);
			}else{
				String message = response.getString("message");
				mInterfaceGetRecordByDateCallBack.onError(idValue, message);
			}
		} catch (JSONException e) {
			mInterfaceGetRecordByDateCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//59、获取用户睡眠报告/api.php?mod=newsleepreport
	private String newsleepreportUrl = g_BaseSiteURL+ "api.php?mod=newsleepreport";
	private InterfaceGetReportCallBack mInterfaceGetReportCallBack;
	/**
	 * 获取用户睡眠报告
	 * @param my_int_id 登陆用户id
	 * @param type 数据类型 (1表示软件数据；2表示硬件数据)默认为1
	 */
	public void getSleepReport(String my_int_id, String type, InterfaceGetReportCallBack callback){
		mInterfaceGetReportCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id",my_int_id);
		params.put("type", type);
		params.put("xctoken", getRequestToken("newsleepreport"));
		super.postJosnObjectData(mContext, newsleepreportUrl, params);
	}

	private void getSleepReportResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			if(idValue.equals("4989")){
				Gson gson = new Gson();
				String topText = response.getString("text");
				String plansleep =response.getString("plansleep");
				String datastr = response.getString("data_list");
				String smoke = response.getString("smoke");
				String win =response.getString("win");
				String coffo = response.getString("coffo");
				String weight = response.getString("weight");
				String sport = response.getString("sport");
				String environment = response.getString("environment");
				String changplan = response.getString("changplan");
				
				List<SleepSignDataBean> datas = gson.fromJson(datastr, new TypeToken<List<SleepSignDataBean>>() {
				}.getType());
				
				SmokeBean smokeBean = gson.fromJson(smoke, SmokeBean.class);
				WinBean winBean = gson.fromJson(win, WinBean.class);
				CoffoBean coffoBean = gson.fromJson(coffo, CoffoBean.class);
				WeightBean weightBean = gson.fromJson(weight, WeightBean.class);
				SportBean sportBean = gson.fromJson(sport, SportBean.class);
				EnvBean envBean= gson.fromJson(environment, EnvBean.class);
				ChangplanBean changplanBean = gson.fromJson(changplan, ChangplanBean.class);
				mInterfaceGetReportCallBack.onSuccess(idValue,changplanBean, topText, plansleep, datas, smokeBean, winBean, coffoBean, weightBean,sportBean,envBean);
			}else{
				String message = response.getString("message");
				mInterfaceGetReportCallBack.onError(idValue, message);
			}
		} catch (Exception e) {
			LogUtil.i("tx", "" + e.getMessage());
			mInterfaceGetReportCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//60、数据补充--睡眠数据/api.php?mod=newsleepsupply
	private String newsleepsupplytUrl = g_BaseSiteURL+ "api.php?mod=newsleepsupply";
	private InterfaceSupplyRecordCallBack mInterfaceSupplyRecordCallBack;
	/**
	 * 数据补充--睡眠数据 
	 * @param my_int_id 登陆用户id
	 * @param content 将题目和答案封装成字符串
	 * @param type 数据类型 (1表示软件数据；2表示硬件数据)默认为1
	 */
	public void supplySleepDataRecord(String my_int_id, SignInData signData, String type, InterfaceSupplyRecordCallBack callback){
		mInterfaceSupplyRecordCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("type", type);
		try {
			params.put("content", new StringBuffer()
			.append("ti_1##").append(Long.parseLong(signData.getGoBedTime())/1000)
			.append("&ti_2##").append(Long.parseLong(signData.getTrySleepTime())/1000)
			.append("&ti_3##").append(signData.getHowLongSleepTime())
			.append("&ti_4##").append(signData.getWakeCount())
			.append("&ti_5##").append(signData.getHowLongWakeTime())
			.append("&ti_6##").append(Long.parseLong(signData.getWakeUpTime())/1000)
			.append("&ti_7##").append(signData.getWakeEarlyTime())
			.append("&ti_8##").append(Long.parseLong(signData.getOutBedTime())/1000)
			.append("&ti_9##").append(signData.getDeepsleep())
			.append("&ti_10##").append(signData.getShallowsleep())
			.toString());
			params.put("xctoken", getRequestToken("xcsleepesign"));
		} catch (Exception e) {
		}
		params.put("xctoken", getRequestToken("newsleepsupply"));
		super.postJosnObjectData(mContext, newsleepsupplytUrl, params);
	}

	private void supplySleepDataResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4992"))
				mInterfaceSupplyRecordCallBack.onSuccess(idValue, message);
			else
				mInterfaceSupplyRecordCallBack.onError(idValue, message);
			
		} catch (JSONException e) {
			mInterfaceSupplyRecordCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}

	//61、数据补充--睡眠总体感觉；哪些环境对睡眠有影响；吸烟....等/api.php?mod=newsleepsupply2
	private String newsleepsupplyt2Url = g_BaseSiteURL+ "api.php?mod=newsleepsupply2";
	private InterfaceSupplyFeelRecordCallBack mInterfaceSupplyFeelRecordCallBack;
	/**
	 * 数据补充--睡眠总体感觉；哪些环境对睡眠有影响；吸烟....等
	 */
	public void supplyFeelDataRecord(RequestParams params, InterfaceSupplyFeelRecordCallBack callback){
		mInterfaceSupplyFeelRecordCallBack = callback;
//		RequestParams params = new RequestParams();
//		params.put("my_int_id", mParams.my_int_id);
//		params.put("feeling", mParams.feeling);
//		params.put("customers", mParams.customers);
//		params.put("smoke", mParams.smoke);
//		params.put("coffo", mParams.coffo);
//		params.put("wine", mParams.wine);
//		params.put("sport", mParams.sport);
//		params.put("weight", mParams.weight);
//		params.put("type", mParams.type);
		params.put("xctoken", getRequestToken("newsleepsupply2"));
		super.postJosnObjectData(mContext, newsleepsupplyt2Url, params);
	}

	private void supplyFeelDataResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4994"))
				mInterfaceSupplyFeelRecordCallBack.onSuccess(idValue, message);
			else
				mInterfaceSupplyFeelRecordCallBack.onError(idValue, message);
			
		} catch (JSONException e) {
			mInterfaceSupplyFeelRecordCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	//62、继续计划/api.php?mod=newsleepplanjx
	private String newsleepplanjxUrl = g_BaseSiteURL+ "api.php?mod=newsleepplanjx";
	private InterfaceContinuePlanCallBack mInterfaceContinuePlanCallBack;

	/**
	 * 继续计划
	 * @param my_int_id
	 * @param type (1表示软件数据；2表示硬件数据)默认为1
	 * @param wakeup 几点起床	格式为08:30
	 * @param sleeplong 睡眠时长	格式为483，单位为分钟
	 * @param callback
	 */
	public void continuePlan(String my_int_id, String type, String wakeup, String sleeplong, InterfaceContinuePlanCallBack callback){
		mInterfaceContinuePlanCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("type", type);
		params.put("wakeup", wakeup);
		params.put("sleeplong", sleeplong);
		params.put("xctoken", getRequestToken("newsleepplanjx"));
		super.postJosnObjectData(mContext, newsleepplanjxUrl, params);
	}

	private void continuePlanResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("4997")){
//				Gson gson = new Gson();
//				HomeDataBean dataBena = new HomeDataBean();
//				dataBena.setNums(response.getString("nums"));
//				dataBena.setZongjie_flag(response.getInt("zongjie_flag"));
//				dataBena.setPlan_flag(response.getInt("plan_flag"));
//				dataBena.setData_flag(response.getInt("data_flag"));
//				dataBena.setFeel_flag(response.getInt("feel_flag"));
//				dataBena.setEnv_flag(response.getInt("env_flag"));
//				dataBena.setSmoke_flag(response.getInt("smoke_flag"));
//				dataBena.setWin_flag(response.getInt("win_flag"));
//				dataBena.setCoffo_flag(response.getInt("coffo_flag"));
//				dataBena.setWeight_flag(response.getInt("weight_flag"));
//				String planListStr = response.getString("plan_list");
//				List<PlanListBean> planList = gson.fromJson(planListStr, new TypeToken<List<PlanListBean>>(){}.getType());
//				dataBena.setPlan_list(planList);
//				
//				String userStr = response.getString("user_list");
//				List<UserBean> userList = gson.fromJson(userStr, new TypeToken<List<UserBean>>(){}.getType());
//				dataBena.setUser_list(userList);
//				
//				String planStr = response.getString("plan");
//				SleepCaseBean sleepCase = gson.fromJson(planStr, new TypeToken<SleepCaseBean>(){}.getType());
//				dataBena.setPlan(sleepCase);
				
				mInterfaceContinuePlanCallBack.onSuccess(idValue, null);
			}
			else
				mInterfaceContinuePlanCallBack.onError(idValue, message);
			
		} catch (JSONException e) {
			mInterfaceContinuePlanCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
		
	//63、判断用户是否参与保险/admin.php?mod=safeapp
	private String safeappUrl = g_BaseSiteURL+ "admin.php?mod=safeapp";
	private InterfaceSafeappCallBack mInterfaceSafeappCallBack;
	/**
	 * 判断用户是否参与保险
	 */
	public void safeapp(String my_int_id, InterfaceSafeappCallBack callback){
		mInterfaceSafeappCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("xctoken", getRequestToken("safeapp"));
		super.postJosnObjectData(mContext, safeappUrl, params);
	}
	
	private void safeappResponse(JSONObject response){
		try {
//			LogUtil.d("chen", response.toString());
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("5001")){
				int flag = response.getInt("flag");
				String kefuid = response.getString("kefuuid");
				mInterfaceSafeappCallBack.onSuccess(idValue, flag, kefuid);
			}else
				mInterfaceSafeappCallBack.onError(idValue, message);
		}catch(Exception e){
			mInterfaceSafeappCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}
	
	
	//64、认知疗养/api.php?mod=newsleeprenzhi
	private String renzhiUrl = g_BaseSiteURL+ "api.php?mod=newsleeprenzhi";
	private InterfacePerceiveCallBack mInterfacePerceiveCallBack;
	/**
	 * 认知疗养
	 */
	public void perceive(String my_int_id, InterfacePerceiveCallBack callback){
		mInterfacePerceiveCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("xctoken", getRequestToken("newsleeprenzhi"));
		super.postJosnObjectData(mContext, renzhiUrl, params);
	}
	
	private void perceiveResponse(JSONObject response){
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("5003")){
				String  dataStr= response.getString("list");
				Gson gson = new Gson(); 
				List<ComfortBean> datas = gson.fromJson(dataStr, new TypeToken<List<ComfortBean>>() {
				}.getType());
				mInterfacePerceiveCallBack.onSuccess(idValue, datas);
			}else
				mInterfacePerceiveCallBack.onError(idValue, message);
		}catch(Exception e){
			mInterfacePerceiveCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}

	//65用户环信登录状态
	private String loginStatusHX = g_BaseSiteURL+ "api.php?mod=huanxinloginstatus";
	private InterfaceMallUtillClass.InterfacLoginStatusHXCallBack mInterfacLoginStatusHXCallBack;
	/**
	 * 用户环信登录状态
	 */
	public void loginStatus(String my_int_id, InterfaceMallUtillClass.InterfacLoginStatusHXCallBack callback){
		mInterfacLoginStatusHXCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", my_int_id);
		params.put("xctoken", getRequestToken("huanxinloginstatus"));
		super.postJosnObjectData(mContext, loginStatusHX, params);
	}

	private void loginStatusCallBack(JSONObject response){
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("5005")){
				mInterfacLoginStatusHXCallBack.onSuccess(idValue,  response.getString("flag"));
			}else
				mInterfacLoginStatusHXCallBack.onError(idValue, message);
		}catch(Exception e){
			mInterfacLoginStatusHXCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}

	//67反馈提交
	private String fankui_url = g_BaseSiteURL+ "api.php?mod=appfankui";
	private InterfaceMallUtillClass.InterfacFeedBackCallBack mInterfacFeedBackCallBack;
	/**
	 * 反馈提交
	 */
	public void feedBack(InterfaceMallUtillClass.FeedBackParam param, InterfaceMallUtillClass.InterfacFeedBackCallBack callback){
		mInterfacFeedBackCallBack = callback;
		RequestParams params = new RequestParams();
		params.put("my_int_id", param.getMy_int_id());
		params.put("fankui", param.getFankui());
		params.put("contact", param.getContact());
		params.put("version", param.getVersion());
		params.put("phonever", param.getPhonever());
		params.put("romversion", param.getRomversion());
		params.put("network", param.getNetwork());
		params.put("yunying", param.getYunying());
		params.put("fenbianl", param.getFenbianl());
		params.put("platform", param.getPlatform());
		params.put("xctoken", getRequestToken("appfankui"));
		super.postJosnObjectData(mContext, fankui_url, params);
	}

	private void feedBackCallBack(JSONObject response){
		try {
			String idValue =  response.getString("response");
			String message = response.getString("message");
			if(idValue.equals("5009")){
				mInterfacFeedBackCallBack.onSuccess(idValue,  message);
			}else
				mInterfacFeedBackCallBack.onError(idValue, message);
		}catch(Exception e){
			mInterfacFeedBackCallBack.onError(errMsgExceptionCode+"", errMsgExceptionString);
		}
	}


	// ========================================================================== 
	// ==========================================================================
	
	@Override
	public void ProcJSONData(JSONObject response) {
		String idValue = "";
		LogUtil.i("tx", response.toString());
		try {
			idValue = response.get("response").toString();
			if (idValue.equals("410")) {
				requestFailed(idValue + "：非法访问", Integer.parseInt(idValue));
			} else if (idValue.equals("411")) {
				requestFailed(idValue + "：服务器数据库连接错误",
						Integer.parseInt(idValue));
			} else if (idValue.equals("412")) {
				requestFailed(idValue + "：服务器数据库操作错误",
						Integer.parseInt(idValue));
			}else if(idValue.equals("4811") || idValue.equals("4811:1") || idValue.equals("4812")){
				shopCommodityListRstProc(response);
			}else if (idValue.equals("4813") || idValue.equals("4814")) {
				myOrderListRstProc(response);
			}else if (idValue.equals("4815") || idValue.equals("4816") || idValue.equals("4817")) {
				productDetailRstProc(response);
			}else if (idValue.equals("4818") || idValue.equals("4819") || idValue.equals("4819:1")
					|| idValue.equals("4820") || idValue.equals("4821")) {
				quRenOrderMsgRstProc(response);
			}else if (idValue.equals("4822") || idValue.equals("4823") || idValue.equals("4824")) {
				modifyDeliveryAddressRstProc(response);
			}else if (idValue.equals("4825") || idValue.equals("4826") || idValue.equals("4827") ||
					idValue.equals("4827:1") || idValue.equals("4828") || idValue.equals("4829")) {
				saveOrderRstProc(response);
			}else if (idValue.equals("4830") || idValue.equals("4831") || idValue.equals("4832")) {
				deliveryAddressMsgRstProc(response);
			}else if (idValue.equals("4833") || idValue.equals("4834") || idValue.equals("4835")) {
				taocanListUrlRstProc(response);
			}else if(idValue.equals("4836") || idValue.equals("4837") || idValue.equals("4838") || 
					idValue.equals("4839")){
				saveChatInfoRstProc(response);
			}else if (idValue.equals("4840") || idValue.equals("4841")) {
				checkIsKefuRstProc(response);
			}else if(idValue.equals("4842") || idValue.equals("4843")){
				getOpenCitysRstProc(response);
			}else if(idValue.equals("4844") || idValue.equals("4845")){
				getHosDoctorsRstProc(response);
			}else if(idValue.equals("4846") || idValue.equals("4847") || idValue.equals("4846:1") || idValue.equals("4846:2")){
				getHosShopProRstProc(response);
			}else if(idValue.equals("4851") || idValue.equals("4852") || idValue.equals("4853")){
				getEstimateRstProc(response);
			}else if(idValue.equals("4854") || idValue.equals("4855")){
				getSleepIndexRstProc(response);
			}else if(idValue.equals("4900") || idValue.equals("4901") || idValue.equals("4902") || idValue.equals("4903")){
				getCollectionDevDataRstProc(response);
			}else if(idValue.equals("4868")||idValue.equals("4869")||idValue.equals("4870")||idValue.equals("4871")||idValue.equals("4872")){
				signInCallBack(response);
			}else if(idValue.equals("4876")||idValue.equals("4877")||idValue.equals("4878")||idValue.equals("4879")){
				getSignInDataCallBack(response);
			}else if(idValue.equals("4859")||idValue.equals("4860")||idValue.equals("4860:1")||idValue.equals("4861")){
				getPlanListCallBack(response);
			}else if(idValue.equals("4862")){
				editPlanCallBack(response);
			}else if(idValue.equals("4863")||idValue.equals("4864")||idValue.equals("4865")){
				getHomePageDataCallBack(response);
			}else if(idValue.equals("4866")||idValue.equals("4867")){
				moocManageCallBack(response);
			}else if("4873".equals(idValue) || "4874".equals(idValue) || "4875".equals(idValue)){
				evaluateSignInCallBack(response);
			}else if("4880".equals(idValue) || "4881".equals(idValue) || "4882".equals(idValue)|| "4883".equals(idValue)){
				checkSignInCallBack(response);
			}else if("4884".equals(idValue) || "4885".equals(idValue) || "4886".equals(idValue) || "4885:1".equals(idValue)){
				getSleepReportCallBack(response);
			}else if("4887".equals(idValue) || "4888".equals(idValue) || "4889".equals(idValue)){
				complatePlanCallback(response);
			}else if("4890".equals(idValue) || "4891".equals(idValue) || "4892".equals(idValue)){
				cancelFeedbackCallBackSponse(response);
			}else if("4893".equals(idValue) || "4894".equals(idValue) || "4895".equals(idValue) || "4896".equals(idValue)){
				modifySign(response);
			}else if("4897".equals(idValue) || "4898".equals(idValue) || "4899".equals(idValue)){
				getSignInfo(response);
			}else if("4905".equals(idValue)){
				RelaxRingtoneRstProc(response);
			}else if("4405".equals(idValue) || "4406".equals(idValue) || "4407".equals(idValue) || "4408".equals(idValue)){
				upLoadSetTiemCa(response);
			}else if("4923".equals(idValue) || "4922".equals(idValue) || "4921".equals(idValue) || "4920".equals(idValue)){
				signInCallBack4_2_1(response);
			}else if("4913".equals(idValue) ||"4914".equals(idValue) ||"4915".equals(idValue)){
				ImproveSleepRstProc(response);
			}else if("4910".equals(idValue) || "4911".equals(idValue) || "4912".equals(idValue))
				getHomePageData(response);
			else if("4917".equals(idValue) || "4916".equals(idValue))
				getSleepCase(response);
			else if("4918".equals(idValue) || "4919".equals(idValue))
				setSleepTimeCallBack(response);
			else if("4924".equals(idValue) || "4925".equals(idValue) || "4925:1".equals(idValue) || "4925:2".equals(idValue) || "4926".equals(idValue))
				sleepFeedbackCallBack(response);
			else if("4927".equals(idValue) || "4928".equals(idValue))
				sleepAddLongCallBack(response);
			else if("4929".equals(idValue) || "4930".equals(idValue))
				sleepNoteSaveCallBack(response);
			else if("4931".equals(idValue) || "4932".equals(idValue))
				sleepNoteListCallBack(response);
			else if("4933".equals(idValue) || "4934".equals(idValue))
				sleepNoteDelCallBack(response);
			else if("4935".equals(idValue) || "4936".equals(idValue))
				sleepNoteNowCallBack(response);
			else if("4937".equals(idValue) || "4938".equals(idValue))
				sleepConsoleSaveCallBack(response);
			else if("4939".equals(idValue) || "4940".equals(idValue))
				sleepCustomsCallBack(response);
			else if("4941".equals(idValue) || "4942".equals(idValue))
				sleepEnvironmentCallBack(response);
			else if("4943".equals(idValue) || "4943:1".equals(idValue) || "4944".equals(idValue) ||"4945".equals(idValue))
				habitChoiceCallBack(response);
			else if("4946".equals(idValue) || "4947".equals(idValue))
				musicListCallBack(response);
			else if("4948".equals(idValue) || "4948:1".equals(idValue) || "4949".equals(idValue)){
				ComfortResponse(response);
			}
			else if("4959".endsWith(idValue) || "4960".equals(idValue))
				knowlistResponse(response);
			else if("4961".equals(idValue) || "4962".equals(idValue)){
				knowDetailResponse(response);
			}else if("4963".equals(idValue))
				knowKeysResponse(response);
			else if("4964".equals(idValue) || "4965".equals(idValue)){
				knowSearchResponse(response);
			}
			//4.3.0
			else if("4970".equals(idValue) || "4971".equals(idValue)||"4972".equals(idValue) ||"4972:1".equals(idValue))
				newSleepPlanResponse(response);
			else if("4973".equals(idValue) || "4973:1".equals(idValue) || "4974".equals(idValue))
				getSleepPlanResponse(response);
			else if("4975".equals(idValue) || "4976".equals(idValue))
				getHomeDataResponse(response);
			else if("4977".equals(idValue) || "4978".equals(idValue) || "4979".equals(idValue)|| "4980".equals(idValue)) 
				addRecordResponse(response);
			else if("4981".equals(idValue)|| "4982".equals(idValue) ||"4982:1".equals(idValue) || "4983".equals(idValue))
				addRecordFeedbackResponse(response);
			else if("4984".equals(idValue)||"4985".equals(idValue) || "4986".equals(idValue))
				getRecordResponse(response);
			else if("4987".equals(idValue) || "4988".equals(idValue) || "4988:1".equals(idValue) || "4989".equals(idValue))
				getSleepReportResponse(response);
			else if("4990".equals(idValue) || "4990:1".equals(idValue) || "4991".equals(idValue) || "4992".equals(idValue))
				supplySleepDataResponse(response);
			else if("4993".equals(idValue) || "4993:1".equals(idValue) || "4994".equals(idValue))
				supplyFeelDataResponse(response);
			else if("4995".equals(idValue) || "4996".equals(idValue) || "4997".equals(idValue))
				continuePlanResponse(response);
			else if("5000".equals(idValue) || "5001".equals(idValue))
				safeappResponse(response);
			else if("5002".equals(idValue) || "5003".equals(idValue))
				perceiveResponse(response);
			else if("5004".equals(idValue) || "5005".equals(idValue))
				loginStatusCallBack(response);

			else if("5006".equals(idValue) || "5007".equals(idValue) || "5008".equals(idValue) || "5009".equals(idValue))
				feedBackCallBack(response);
			else {
				requestFailed(errMsgNoneString, errMsgNoneCode);
			}
		} catch (JSONException e) {
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
		///
		if (m_InterfaceShopCommodityListCallback != null) {
			m_InterfaceShopCommodityListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceMyOrderListCallback != null) {
			m_InterfaceMyOrderListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceProductDetailCallback != null) {
			m_InterfaceProductDetailCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceQuRenOrderMsgCallback != null) {
			m_InterfaceQuRenOrderMsgCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceModifyDeliveryAddressCallback != null) {
			m_InterfaceModifyDeliveryAddressCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceSaveOrderCallback != null) {
			m_InterfaceSaveOrderCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceDeliveryAddressMsgCallback != null) {
			m_InterfaceDeliveryAddressMsgCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceTaocanListCallback != null) {
			m_InterfaceTaocanListCallback.onError(iCode, errmsg);
		}
		if (m_InterfaceSaveChatInfoCallback != null) {
			m_InterfaceSaveChatInfoCallback.onError(iCode, errmsg);
		}
		if(m_InterfaceOpenCitysCallback != null){
			m_InterfaceOpenCitysCallback.onError(iCode, errmsg);
		}
		if(m_InterfaceHosDoctorsCallback != null){
			m_InterfaceHosDoctorsCallback.onError(iCode, errmsg);
		}
		if(m_InterfaceHosProAndSerCallback != null){
			m_InterfaceHosProAndSerCallback.onError(iCode, errmsg);
		}
		
		///
		if (m_InterfaceCheckIsKefuCallback != null) {
			m_InterfaceCheckIsKefuCallback.onError(iCode, errmsg);
		}
		if(m_InterfaceEstimateCallback != null){
			m_InterfaceEstimateCallback.onError(iCode, errmsg);
		}
		if(m_InterfaceSleepIndexCallback != null){
			m_InterfaceSleepIndexCallback.onError(iCode, errmsg);
		}
		if(m_InterfaceCollectionDevDataCallBack != null){
			m_InterfaceCollectionDevDataCallBack.onError(iCode, errmsg);
		}
		if(signInCallback != null){
			signInCallback.onError(iCode, errmsg);
		}
		if(getSignInDataCallback != null){
			getSignInDataCallback.onError(iCode, errmsg);
		}
		if(getPlanListCallBack != null){
			getPlanListCallBack.onError(iCode, errmsg);
		}
		if(editPlanListCallBack != null){
			editPlanListCallBack.onError(iCode, errmsg);
		}
		if(getHomePageDataCallBack != null){
			getHomePageDataCallBack.onError(iCode, errmsg);
		}
		if(moodManageCallBack != null){
			moodManageCallBack.onError(iCode, errmsg);
		}
		if(evaluateSignInCallBack != null){
			evaluateSignInCallBack.onError(iCode, errmsg);
		}
		if(checkSignInCallBack != null){
			checkSignInCallBack.onError(iCode, errmsg);
		}
		if(sleepReportCallBack != null){
			sleepReportCallBack.onError(iCode, errmsg);
		}
		if(complatePlanCallBack != null){
			complatePlanCallBack.onError(iCode, errmsg);
		}
		if(cancelFeedbackCallBack != null){
			cancelFeedbackCallBack.onError(iCode, errmsg);
		}
		if(modifySignCallBack != null){
			modifySignCallBack.onError(iCode, errmsg);
		}
		if(getSignInfoCallBack != null){
			getSignInfoCallBack.onError(iCode, errmsg);
		}
		if(m_InterfaceRelaxRingsCallback != null){
			m_InterfaceRelaxRingsCallback.onError(iCode, errmsg);
		}
		if(upLoadSetTimeCallback != null){
			upLoadSetTimeCallback.onError(iCode+"", errmsg);
		}
		
		if(signInCallback4_2_1 != null )
			signInCallback4_2_1.onError(iCode+"", errmsg);
		
		if(mInterfaceImproveSleepCallBack != null){
			mInterfaceImproveSleepCallBack.onError(iCode+"", errmsg);
		}
		if(interfaceHomeDateCallBack != null)
			interfaceHomeDateCallBack.onError(iCode+"", errmsg);
		
		if(interfaceCaseCallBack != null)
			interfaceCaseCallBack.onError(iCode+"", errmsg);
		
		if(interfaceSetSleepTimeCallBack != null)
			interfaceSetSleepTimeCallBack.onError(iCode+"", errmsg);
		
		if(interfaceSleepAddLongCallBack != null)
			interfaceSleepAddLongCallBack.onError(iCode+"", errmsg);
		
		if(interfaceSaveNoteCallBack != null)
			interfaceSaveNoteCallBack.onError(iCode+"", errmsg);
		
		if(interfaceSleepNoteListCallBack != null)
			interfaceSleepNoteListCallBack.onError(iCode+"", errmsg);
		
		if(interfaceSleepNoteDelCallBack != null)
			interfaceSleepNoteDelCallBack.onError(iCode+"", errmsg);
		
		if(interfaceSleepNoteNowCallBack != null)
			interfaceSleepNoteNowCallBack.onError(iCode+"", errmsg);
		
		if(interfaceConsoleSaveCallBack != null)
			interfaceConsoleSaveCallBack.onError(iCode+"", errmsg);
		
		if(interfaceSleepCustomsCallBack != null)
			interfaceSleepCustomsCallBack.onError(iCode+"", errmsg);
		
		if(interfaceSleepEnvironmentCallBack != null)
			interfaceSleepEnvironmentCallBack.onError(iCode+"", errmsg);
		
		if(interfaceHabitChoiceCallBack != null)
			interfaceHabitChoiceCallBack.onError(iCode+"", errmsg);
		
		if(interfaceMusicListCallBack != null)
			interfaceMusicListCallBack.onError(iCode+"", errmsg);
		
		if(mInterfaceComfortCallBack != null)
			mInterfaceComfortCallBack.onError(iCode + "", errmsg);
		
		if(mInterfaceKnowCallBack != null)
			mInterfaceKnowCallBack.onError(iCode + "", errmsg);
		if(mInterfaceKnowledgeDetailCallBack != null)
			mInterfaceKnowledgeDetailCallBack.onError(iCode + "", errmsg);
		if(mInterfaceKnowledgeKeysCallBack != null)
			mInterfaceKnowledgeKeysCallBack.onError(iCode+"", errmsg);
		if(mInterfaceKnowledgeSearchCallBack != null)
			mInterfaceKnowledgeSearchCallBack.onError(iCode+"", errmsg);
		
		// 4.3.0
		if(mInterfaceNewSleepplanCallBack != null)
			mInterfaceNewSleepplanCallBack.onError(iCode + "", errmsg);
		
		if(mInterfaceGetSleepplanCallBack != null)
			mInterfaceGetSleepplanCallBack.onError(iCode + "", errmsg);
		if(mInterfaceHomeDataCallBack != null)
			mInterfaceHomeDataCallBack.onError(iCode + "", errmsg);
		if(mInterfaceAddRecordCallBack != null)
			mInterfaceAddRecordCallBack.onError(iCode + "", errmsg);
		if(mInterfaceAddRecordFeedbackCallBack != null)
			mInterfaceAddRecordFeedbackCallBack.onError(iCode + "", errmsg);
		if(mInterfaceGetRecordByDateCallBack != null)
			mInterfaceGetRecordByDateCallBack.onError(iCode + "", errmsg);
		if(mInterfaceGetReportCallBack != null)
			mInterfaceGetReportCallBack.onError(iCode + "", errmsg);
		if(mInterfaceSupplyRecordCallBack != null)
			mInterfaceSupplyRecordCallBack.onError(iCode + "", errmsg);
		if(mInterfaceSupplyFeelRecordCallBack != null)
			mInterfaceSupplyFeelRecordCallBack.onError(iCode + "", errmsg);
		if(mInterfaceContinuePlanCallBack != null)
			mInterfaceContinuePlanCallBack.onError(iCode + ",", errmsg);
		if(mInterfaceSafeappCallBack != null)
			mInterfaceSafeappCallBack.onError(iCode + "", errmsg);
		if(mInterfacePerceiveCallBack != null)
			mInterfacePerceiveCallBack.onError(iCode + "", errmsg);
		if(mInterfacLoginStatusHXCallBack  != null)
			mInterfacLoginStatusHXCallBack.onError(iCode + "", errmsg);

		if(mInterfacFeedBackCallBack != null)
			mInterfacFeedBackCallBack.onError(iCode + "", errmsg);
	}

}
