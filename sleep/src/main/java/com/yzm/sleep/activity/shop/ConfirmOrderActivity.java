package com.yzm.sleep.activity.shop;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.adapter.ComboAdapter;
import com.yzm.sleep.alipay.PayResult;
import com.yzm.sleep.alipay.SignUtils;
import com.yzm.sleep.bean.SaveOrderBean;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.widget.CustomGridView;
import com.yzm.sleep.wxpay.Constants;
import com.yzm.sleep.wxpay.MD5;

/**
 * 确认订单
 * @params ShopCommodityBean bean;
 */
public class ConfirmOrderActivity extends BaseActivity {

	private SaveOrderBean bean;
	private TextView userPhone, userName, address, orderNum, buyNum,
			goodsPrice, goodsYouhPrice, goodsPayPrice;
	private CustomGridView cvComboList;
	private RelativeLayout zhifubaoPay, weixinpay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_order);
		findViewById(R.id.back).setOnClickListener(this);
		((TextView) findViewById(R.id.title)).setText("确认订单");
		bean = (SaveOrderBean) getIntent().getSerializableExtra("bean");
		initView();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Service_Store_ConfirmPurchase");
		MobclickAgent.onResume(this); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Service_Store_ConfirmPurchase"); 
		MobclickAgent.onPause(this);
	}

	private void initView() {
		zhifubaoPay = (RelativeLayout) findViewById(R.id.btn_pay_zhifubo);
		zhifubaoPay.setOnClickListener(this);
		weixinpay   = (RelativeLayout) findViewById(R.id.btn_pay_weixin);
		weixinpay.setOnClickListener(this);
		
		findViewById(R.id.rl_info).setOnClickListener(this);
		findViewById(R.id.rl_goods).setOnClickListener(this);
		userName = (TextView) findViewById(R.id.user_name);
		userPhone = (TextView) findViewById(R.id.user_phone);
		address = (TextView) findViewById(R.id.address);
		orderNum = (TextView) findViewById(R.id.goods_name);
		buyNum = (TextView) findViewById(R.id.buy_num);
		cvComboList = (CustomGridView) findViewById(R.id.cv_combo_list);
		cvComboList.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		goodsPrice = (TextView) findViewById(R.id.tv_goods_price);
		goodsYouhPrice = (TextView) findViewById(R.id.tv_goods_youh_price);
		goodsPayPrice = (TextView) findViewById(R.id.tv_goods_pay_price);
		initData();
		
		cvComboList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					startActivity(new Intent(ConfirmOrderActivity.this, PackageDetailsActivity.class)
					.putExtra("shopid", bean.getShopid()).putExtra("no_shop", true));
				}
				return false;
			}
		});
	}

	private void initData() {
		if (bean != null && bean.getMorepicture()!=null && bean.getMorepicture().size() > 0) {
			cvComboList.setVisibility(View.VISIBLE);
			cvComboList.setAdapter(new ComboAdapter(this, bean.getMorepicture()));
		}else
			cvComboList.setVisibility(View.GONE);
		
		userName.setText("收货人：" + bean.getRealname());
		userPhone.setText("" + bean.getPhone());
		address.setText("收货地址：" + bean.getAddress());
		orderNum.setText("订单号：" + bean.getOut_trade_no());
		buyNum.setText("x" + bean.getBuy_num());
		goodsPrice.setText(bean.getPrice());
		goodsYouhPrice.setText(bean.getYouhui_price());
		goodsPayPrice.setText(bean.getCount_price());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.rl_info:
			startActivity(new Intent(this, GoodsAddressActivity.class).putExtra("bean", bean));
			break;
		case R.id.rl_goods:
			startActivity(new Intent(this, PackageDetailsActivity.class).putExtra("shopid", bean.getShopid()).putExtra("no_shop", true));
			break;
		case R.id.btn_pay_zhifubo:
			String pay = StringUtil.getPrice(bean.getCount_price());
			String outTradeNo = bean.getOut_trade_no();
			if(!TextUtils.isEmpty(pay))
				payByAli(bean.getTitle(), bean.getTitle(), pay, outTradeNo); //pay
			zhifubaoPay.setClickable(false);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					zhifubaoPay.setClickable(true);
				}
			}, 100);
			break;
		case R.id.btn_pay_weixin:
			req = new PayReq();
			String wxpay = null;
			try {
				wxpay = (int)(Float.parseFloat(StringUtil.getPrice(bean.getCount_price())) * 100)+"";
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(ConfirmOrderActivity.this, "支付出错", Toast.LENGTH_SHORT).show();
				return;
			}
			String wxOutTradeNo = bean.getOut_trade_no();
			new GetPrepayIdTask().execute(wxpay, wxOutTradeNo);
			break;
		default:
			break;
		}
	}

	/*
	 * ---------------------------------阿里支付--------------------------------------
	 */
	private final int SDK_PAY_FLAG = 1;

	private final int SDK_CHECK_FLAG = 2;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Util.show(ConfirmOrderActivity.this, "支付成功");
					startActivity(new Intent(ConfirmOrderActivity.this, PaySuccessfulActivity.class).putExtra("kefu", bean.getKefu()));
					AppManager.getAppManager().finishActivity();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Util.show(ConfirmOrderActivity.this, "支付结果确认中");
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Util.show(ConfirmOrderActivity.this, "支付失败");
					}
				}
				
				break;
			}
			case SDK_CHECK_FLAG: {
				Util.show(ConfirmOrderActivity.this, "检查结果为：" + msg.obj);
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 * @param goodsName
	 *            商品名
	 * @param description
	 *            商品描述
	 * @param price
	 *            商品价格 for:"0.01"
	 */
	private void payByAli(String goodsName, String description, String price, String outTradeNO) {
		if (TextUtils.isEmpty(Constant.PARTNER)
				|| TextUtils.isEmpty(Constant.RSA_PRIVATE)
				|| TextUtils.isEmpty(Constant.SELLER)) {
			new AlertDialog.Builder(this)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									//
									finish();
								}
							}).show();
			return;
		}
		// 订单
		String orderInfo = getOrderInfo(goodsName, description, price, outTradeNO);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(ConfirmOrderActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(ConfirmOrderActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price, String outTradeNO) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Constant.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Constant.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + outTradeNO + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Constant.ALI_NOTIFA_URL + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, Constant.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	
	/*-------------------------------------------微信支付方式---------------------------------------------------------*/
	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
//	TextView show;
	Map<String,String> resultunifiedorder;
//	StringBuffer sb;
	
	/**
	 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);
		
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return packageSign;
	}
	
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

//        this.sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion",appSign);
		return appSign;
	}
	
	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");


			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		Log.e("orion",sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends AsyncTask<String, Void, Map<String,String>> {

		private ProgressDialog dialog;


		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ConfirmOrderActivity.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String,String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
//			sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
//			show.setText(sb.toString());

			resultunifiedorder=result;
			genPayReq();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String,String>  doInBackground(String... params) {

			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs(params[0], params[1]);

			Log.e("orion",entity);

			byte[] buf = com.yzm.sleep.wxpay.Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String,String> xml=decodeXml(content);

			return xml;
		}
	}



	public Map<String,String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if("xml".equals(nodeName)==false){
							//实例化student对象
							xml.put(nodeName,parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion",e.toString());
		}
		return null;

	}


	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	


	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	

	/**
	 *  生成微信支付参数
	 * @param price  分为单位，字符串格式
	 * @return
	 */
	private String genProductArgs(String price, String outTradeNo) {
		StringBuffer xml = new StringBuffer();

		try {
			String	nonceStr = genNonceStr();


			xml.append("</xml>");
           List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "weixin"));
			packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", Constant.WX_NOTIFA_URL));
			packageParams.add(new BasicNameValuePair("out_trade_no",outTradeNo));
			packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", price));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));


			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));


		   String xmlstring =toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {
			return null;
		}
		

	}
	private void genPayReq() {
		
		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());


		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

//		sb.append("sign\n"+req.sign+"\n\n");

//		show.setText(sb.toString());

		Log.e("orion", signParams.toString());

		sendPayReq();
	}
	private void sendPayReq() {
		

		msgApi.registerApp(Constants.APP_ID);
		boolean senback = msgApi.sendReq(req);
		if(!senback){
			toastMsg("请先安装微信");
		}
		
	}
}
