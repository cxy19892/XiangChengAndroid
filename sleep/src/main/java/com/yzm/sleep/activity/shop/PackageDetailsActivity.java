package com.yzm.sleep.activity.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.bean.CommodityDetailBean;
import com.yzm.sleep.bean.SaveOrderBean;
import com.yzm.sleep.bean.ShopCommodityBean;
import com.yzm.sleep.model.SelectLCDialog;
import com.yzm.sleep.model.SelectLCDialog.IntClickBuyListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceProductDetailCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSaveOrderCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ProductDetailParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SaveOrderParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 套餐详情
 * @author tianxun
 * @pramas shopid String; //商品id
 * @params flag String //是否售完
 */
public class PackageDetailsActivity extends BaseActivity {
	private WebView webView;
	private CommodityDetailBean data;
	private SelectLCDialog dialog;
	private Button buy;
	private Context context;
	private TextView deleteView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_packagedeta);
		this.context = this;
		init();
	}

	@SuppressLint("SetJavaScriptEnabled") 
	private void init() {
		findViewById(R.id.back).setOnClickListener(this);
		buy = (Button) findViewById(R.id.but_buy);
		buy.setOnClickListener(this);
		deleteView = (TextView) findViewById(R.id.delete_view);
		((TextView) findViewById(R.id.title)).setText("套餐详情");
		webView = (WebView) findViewById(R.id.webpackagedeta);
		WebSettings setting = webView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setUseWideViewPort(true);
		setting.setLoadWithOverviewMode(true);
		setting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webView.setBackgroundColor(getResources().getColor(R.color.bg_color));
		getConmopDetail();
	}
	

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Service_Store_Item_Detail");
		MobclickAgent.onResume(this); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Service_Store_Item_Detail"); 
		MobclickAgent.onPause(this);
	}
	
	/**
	 * 获取套餐详情
	 */
	private void getConmopDetail(){
		ProductDetailParamClass mParams=new ProductDetailParamClass();
		mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.shopid  = getIntent().getStringExtra("shopid");
		
		new XiangchengMallProcClass(this).productDetail(mParams, new InterfaceProductDetailCallback() {
			
			@Override
			public void onSuccess(int icode, CommodityDetailBean data) {
				doDetailData(data);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				deleteView.setVisibility(View.VISIBLE);
				Util.show(PackageDetailsActivity.this, strErrMsg);
			}
		});
	}
	
	private void doDetailData(CommodityDetailBean data){
		this.data = data;
		webView.loadDataWithBaseURL(null, data.getIntro(), "html/text", "UTF-8", null);
		if("1".equals(getIntent().getStringExtra("flag"))){
			buy.setEnabled(true);
			buy.setText("立即购买");
			buy.setBackgroundResource(R.color.theme_color);	
			findViewById(R.id.view_v).setVisibility(View.GONE);
		}else{
			buy.setEnabled(false);
			buy.setText("售罄");
			buy.setBackgroundResource(R.color.bg_color);
			findViewById(R.id.view_v).setVisibility(View.VISIBLE);
		}
		
		if(getIntent().getBooleanExtra("no_shop", false)){
			buy.setVisibility(View.GONE);
			findViewById(R.id.view_v).setVisibility(View.GONE);
		}else
			buy.setVisibility(View.VISIBLE);
	}
	
	private void buy(){
		if(!Util.checkNetWork(this)){
			Util.show(this, "网路连接错误");
			return;
		}
		
		if(PreManager.instance().getIsLogin(this)){
			ShopCommodityBean bean =new ShopCommodityBean();
			bean.setFlag(data.getFlag());
			bean.setLiaocheng(data.getLiaocheng());
			bean.setMorepicture(data.getMorepicture());
			bean.setPrice(data.getPrice());
			bean.setSale_price(data.getSale_price());
			bean.setSalenum(data.getSalenum());
			bean.setShopid(data.getShopid());
			bean.setTitle(data.getTitle());
			int lcNum=1;
			try {
				lcNum = Integer.parseInt(data.getLiaocheng());
			} catch (Exception e) {
			}
			if(lcNum > 1)
				showSelsectDialog(bean, lcNum);
			else{
				bean.setBuyNum(1);
				intentActivity(bean);
			}
		}
		else
			startActivity(new Intent(this, LoginActivity.class));
	}

	
	private void showSelsectDialog(final ShopCommodityBean bean, int maxNum){
		dialog = new SelectLCDialog(this, maxNum, new IntClickBuyListener() {
			
			@Override
			public void clickBuy(int lcNum) {
				bean.setBuyNum(lcNum);
				intentActivity(bean);
				dialog.dismiss();
			}
		});
		
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
	
	private ProgressUtils pro;
	private void intentActivity(final ShopCommodityBean bean){
		showPro();
		SaveOrderParamClass mParams=new SaveOrderParamClass();
		mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.shopid=bean.getShopid();
		mParams.buy_num = String.valueOf(bean.getBuyNum());
		new XiangchengMallProcClass(context).saveOrder(mParams, new InterfaceSaveOrderCallback() {
			
			@Override
			public void onSuccess(int icode, SaveOrderBean data) {
				cancelPro();
				if(!TextUtils.isEmpty(data.getRealname()) && !TextUtils.isEmpty(data.getPhone()) && !TextUtils.isEmpty(data.getAddress())){
					startActivity(new Intent(context,ConfirmOrderActivity.class).putExtra("bean", data));
				}else
					startActivity(new Intent(context,GoodsAddressActivity.class).putExtra("bean", data));
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				cancelPro();
				Util.show(context, strErrMsg);
			}
		});
		
	}
	
	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
		}
		pro.setCanceledOnTouchOutside(false);
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.but_buy://立即购买
			buy();
			break;
		default:
			break;
		}
	}

}
