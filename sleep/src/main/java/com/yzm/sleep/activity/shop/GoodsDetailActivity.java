package com.yzm.sleep.activity.shop;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.doctor.ClinicDetatilActivity;
import com.yzm.sleep.activity.doctor.DoctorDetailActivity;
import com.yzm.sleep.adapter.GoodsComboAdapter;
import com.yzm.sleep.adapter.GoodsComboAdapter.IntClickBuyListenter;
import com.yzm.sleep.bean.GoodsDetailBean;
import com.yzm.sleep.bean.SaveOrderBean;
import com.yzm.sleep.bean.ShopCommodityBean;
import com.yzm.sleep.bean.UserMessageBean;
import com.yzm.sleep.im.ChatActivity;
import com.yzm.sleep.model.SelectLCDialog;
import com.yzm.sleep.model.SelectLCDialog.IntClickBuyListener;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSaveOrderCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceShopCommodityListCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SaveOrderParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ShopCommodityParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 商品详情
 * @author tianxun
 * @params goods_id String //商品id;
 * @praems kefu String //客服电话
 */
public class GoodsDetailActivity extends BaseActivity implements IntClickBuyListenter{

	private MaterialRefreshLayout refrensh;
	private ListView mListView;
	private int dataPage=1, totalPage=0; 
	private View headView;
	private GoodsComboAdapter adapter;
	private List<ShopCommodityBean> listData;
	private SelectLCDialog dialog;
	private Context context;
	private String kefu;
	private GoodsDetailBean mgoodsDetail;
	private boolean isFromShop = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		((TextView)findViewById(R.id.title)).setText("商品详情");
		findViewById(R.id.back).setOnClickListener(this);
		kefu= getIntent().getStringExtra("kefu");
		isFromShop = getIntent().getBooleanExtra("fromshop", false);
		this.context=this;
		initView();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Service_Store_Item");
		MobclickAgent.onResume(this); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Service_Store_Item"); 
		MobclickAgent.onPause(this);
	}
	
	private void initView() {
		headView=getLayoutInflater().inflate(R.layout.goodsdetail_head_layout, null);
		headView.setVisibility(View.GONE);
		refrensh=(MaterialRefreshLayout) findViewById(R.id.fragment_refresh);
		mListView=(ListView) findViewById(R.id.lv_goods_list);
		TextView textView = new TextView(this);
		textView.setHeight(0);
		mListView.addFooterView(textView, null, false);
		mListView.addHeaderView(headView, null, false);
		adapter=new GoodsComboAdapter(this, getScreenWidth(), this);
		mListView.setAdapter(adapter);
		listData=new ArrayList<ShopCommodityBean>();
		
		refrensh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				if(Util.checkNetWork(GoodsDetailActivity.this)){
					dataPage = 1;
					getGoodsComboList(true);
				}else{
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Util.show(context, "网路连接错误");
							refrensh.finishRefresh();
						}
					}, 300);
				}
			}
		});
		
		refrensh.setOnClickRequestListener(new OnClickRequestListener() {
			
			@Override
			public void setRequest() {
				refrensh.autoRefresh();
			}
		});
	
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// 判断是否加载
				int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					if (dataPage < totalPage) {
						dataPage += 1;
						getGoodsComboList(false);
					}
				}
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position - mListView.getHeaderViewsCount();
				if(index >= 0){
					if(listData.get(index).isIsgoods()){
						startActivity(new Intent(context, PackageDetailsActivity.class)
						.putExtra("shopid", listData.get(index).getShopid())
						.putExtra("flag", listData.get(index).getFlag()));
					}else if(listData.get(index).isIsyg() && isFromShop){
						Intent intent = null;
						intent = new Intent(GoodsDetailActivity.this, ClinicDetatilActivity.class);
						intent.putExtra("id", listData.get(index).getYgid());
						startActivity(intent);
					}else{
						if(isFromShop){
						Intent intent = new Intent(GoodsDetailActivity.this, DoctorDetailActivity.class);
						intent.putExtra("id", listData.get(index).getZjuid());
						startActivity(intent);
						}
					}
				}				
			}
		});
		
		if (Util.checkNetWork(this)) {
			refrensh.autoRefresh();
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					refrensh.finishRefresh();
					refrensh.addListViewState(mListView,Constant.NO_NET);
				}
			}, 300);
		}
	}
	
	private void getGoodsComboList(final boolean isRefrensh){
		ShopCommodityParamClass mParams=new ShopCommodityParamClass();
		if(PreManager.instance().getIsLogin(this))
			mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.page = String.valueOf(dataPage);
		mParams.pagesize = "20";
		mParams.tcid = getIntent().getStringExtra("goods_id");
		
		new XiangchengMallProcClass(context).shopCommodityList(mParams ,new InterfaceShopCommodityListCallback() {
			
			@Override
			public void onSuccess(int icode, GoodsDetailBean goodsDetail, int totalpage) {
				if (isRefrensh) 
					refrensh.finishRefresh();
				mgoodsDetail = goodsDetail;
				doGoodsComData(isRefrensh, goodsDetail, totalpage);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				if (isRefrensh) 
					refrensh.finishRefresh();
				refrensh.addListViewState(mListView, Constant.NO_DATA);
				refrensh.setText(Constant.NO_DATA, "该商品已下架");
			}
		});
	}
	
	private void doGoodsComData(boolean isrefrensh,GoodsDetailBean goodsDetail, int totalpage){
		this.totalPage =totalpage;
		if (isrefrensh) {
			ImageView img = (ImageView) headView.findViewById(R.id.item_goodsimg);
			TextView title = (TextView) headView.findViewById(R.id.item_goods_title);
			TextView sellNum = (TextView) headView.findViewById(R.id.item_goods_sell);
			findViewById(R.id.iv).setVisibility(View.GONE);
			RelativeLayout.LayoutParams mParam = (LayoutParams) img.getLayoutParams();
			mParam.height = getScreenWidth()/2;
			
			img.setLayoutParams(mParam);
			ImageLoader.getInstance().displayImage(goodsDetail.getPicture(), goodsDetail.getPicture_key(), img, MyApplication.defaultOption);
			title.setText(goodsDetail.getTc_title());
			sellNum.setText("已售：" + (TextUtils.isEmpty(goodsDetail.getTcsalenum()) ? "0" : goodsDetail.getTcsalenum()));
			headView.setVisibility(View.VISIBLE);
		}
		
		if(goodsDetail.getShop_list() != null ){
			if(dataPage == 1){
				listData = goodsDetail.getShop_list();
				if(!TextUtils.isEmpty(goodsDetail.getZjuid()) && !TextUtils.isEmpty(goodsDetail.getZjname()) && !TextUtils.isEmpty(goodsDetail.getZjaddress()) && isFromShop){
					ShopCommodityBean maddBean = new ShopCommodityBean();
					maddBean.setIsgoods(false);
					maddBean.setIsyg(false);
					maddBean.setIszj(true);
					maddBean.setZjaddress(goodsDetail.getZjaddress());
					maddBean.setZjname(goodsDetail.getZjname());
					maddBean.setZjuid(goodsDetail.getZjuid());
					listData.add(maddBean);
				}
				if(!TextUtils.isEmpty(goodsDetail.getYgid()) && !TextUtils.isEmpty(goodsDetail.getYgaddress()) && !TextUtils.isEmpty(goodsDetail.getYgname()) && isFromShop){
					ShopCommodityBean maddygBean = new ShopCommodityBean();
					maddygBean.setIsgoods(false);
					maddygBean.setIsyg(true);
					maddygBean.setIszj(false);
					maddygBean.setYgaddress(goodsDetail.getYgaddress());
					maddygBean.setYgid(goodsDetail.getYgid());
					maddygBean.setYgname(goodsDetail.getYgname());
					listData.add(maddygBean);
				}
				
			}
			else
				listData.addAll(goodsDetail.getShop_list());
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(dataPage < totalPage)
					refrensh.addListViewState(mListView, Constant.LOADING);
				else
					refrensh.addListViewState(mListView, Constant.NO_MORE);

				adapter.setData(listData);
			}
		}, 200);
	}
	
	private void showSelsectDialog(final ShopCommodityBean bean, int maxNum){
		dialog = new SelectLCDialog(context, maxNum, new IntClickBuyListener() {
			
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
	

	private void intentActivity(ShopCommodityBean bean){
		showPro();
		SaveOrderParamClass mParams=new SaveOrderParamClass();
		mParams.my_int_id = PreManager.instance().getUserId(this);
		mParams.shopid=bean.getShopid();
		mParams.buy_num = String.valueOf(bean.getBuyNum());
		new XiangchengMallProcClass(context).saveOrder(mParams, new InterfaceSaveOrderCallback() {
			
			@Override
			public void onSuccess(int icode, SaveOrderBean data) {
				cancelPro();
				data.setKefu(kefu);
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
	
	private ProgressUtils pro;
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
		default:
			break;
		}
	}

	@Override
	public void onClickBuy(ShopCommodityBean bean ) {
		if(!Util.checkNetWork(context)){
			Util.show(context, "网路连接错误");
			return;
		}
		
		if(PreManager.instance().getIsLogin(context)){
			int lcNum=1, stockNum = 0;
			try {
				lcNum = Integer.parseInt(bean.getLiaocheng());
			} catch (Exception e) {
			}
			try {
				stockNum = Integer.parseInt(bean.getStock());
			} catch (Exception e) {
			}
			
			if(lcNum > 1){
				if(lcNum > stockNum)
					lcNum = stockNum;
				showSelsectDialog(bean, lcNum);
			}
			else{
				bean.setBuyNum(1);
				intentActivity(bean);
			}
		}else
			startActivity(new Intent(context, LoginActivity.class));
		
		
	}

	@Override
	public void onClickAsk(ShopCommodityBean bean) {
		if(!Util.checkNetWork(context)){
			Util.show(context, "网路连接错误");
			return;
		}
		
		if(PreManager.instance().getIsLogin(context)){
			if(mgoodsDetail != null){
				Intent intent = new Intent(GoodsDetailActivity.this,ChatActivity.class);
				UserMessageBean user=new UserMessageBean();
				user.setUid( mgoodsDetail.getKfuid());
				user.setNickname(mgoodsDetail.getName());
				user.setProfile(mgoodsDetail.getTx());
				user.setProfile_key(mgoodsDetail.getTx_key());
				intent.putExtra("userBean", user);
				startActivity(intent);
			}
		}else
			startActivity(new Intent(context, LoginActivity.class));
	}
}
