package com.yzm.sleep.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.activity.shop.GoodsDetailActivity;
import com.yzm.sleep.activity.shop.ShopMapPathPlanActivity;
import com.yzm.sleep.adapter.HosShopProAdapter;
import com.yzm.sleep.bean.ClinicBean;
import com.yzm.sleep.bean.DoctorBean;
import com.yzm.sleep.bean.TaocanBean;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceClinicDetailCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 医馆详情界面
 * 
 * @author hetonghua
 * 
 */
public class ClinicDetatilActivity extends BaseActivity implements OnItemClickListener {
	List<DoctorBean> clinicDoctors;
	private HosShopProAdapter adapter;
	private ImageView ivClinic;
	private TextView tvClinicName;
	private TextView tvAdddrss,title;
	private String ygid = "";
	private View headView,loadingView,noNetView;
	private ListView listView;
	private LinearLayout moredocLin, showdocLin, moreproLin;
	private ClinicBean mClinic;
	
//	private SellEndDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clinic_detail);
		initView();
		clinicDoctors = new ArrayList<DoctorBean>();
		try {
			ygid = getIntent().getStringExtra("id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		adapter.setData(new ArrayList<TaocanBean>());
		getClinicMsg(ygid);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Service_Institute_Intro");
		MobclickAgent.onResume(this); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Service_Institute_Intro"); 
		MobclickAgent.onPause(this);
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title);
		findViewById(R.id.back).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		
		@SuppressWarnings("static-access")
		LayoutInflater inflater = getLayoutInflater().from(this);
		headView = inflater.inflate(R.layout.head_clinic_detail, null);
		headView.setVisibility(View.INVISIBLE);
		loadingView = getLayoutInflater().inflate(R.layout.listview_loading,
				null);
		noNetView = getLayoutInflater().inflate(R.layout.layout_no_net, null);
		
		ivClinic = (ImageView) headView.findViewById(R.id.ivClinic);
		tvClinicName = (TextView) headView.findViewById(R.id.tvClinicName);
		tvAdddrss = (TextView) headView.findViewById(R.id.tvAdddrss);
		headView.findViewById(R.id.btnChat).setOnClickListener(this);
		moredocLin = (LinearLayout) headView.findViewById(R.id.lin_doctor_more);
		showdocLin = (LinearLayout) headView.findViewById(R.id.lin_doctor_show);
		moreproLin = (LinearLayout) headView.findViewById(R.id.lin_product_more);
		
		moredocLin.setOnClickListener(this);
		moreproLin.setOnClickListener(this);
		listView.addHeaderView(loadingView, null, false);
		listView.addHeaderView(headView, null, false);
		listView.addFooterView(noNetView, null, false);
		noNetView.setVisibility(View.GONE);
		headView.findViewById(R.id.rl_tomap).setOnClickListener(this);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.height = getScreenWidth() * 1 / 2;
		ivClinic.setLayoutParams(params);
		adapter = new HosShopProAdapter(ClinicDetatilActivity.this);
		listView.setAdapter(adapter);
		noNetView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeFooterView();
				initView();
				getClinicMsg(ygid);
			}
		});
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btnChat:
			if(PreManager.instance().getIsLogin(this)){
				Intent intent = new Intent(this, ReservationActivity.class);
				intent.putExtra("ygid", ygid);
				intent.putExtra("zjuid", "");
				startActivity(intent);
			}else
				startActivity(new Intent(this, LoginActivity.class));
			break;
		case R.id.lin_doctor_more:
			Intent intentdoc = new Intent(ClinicDetatilActivity.this, HosdoctorListActivity.class);
			intentdoc.putExtra("ygid", ygid);
			startActivity(intentdoc);
			break;
		case R.id.lin_product_more:
			Intent intentpro = new Intent(ClinicDetatilActivity.this, HosShopProlistActivity.class);
			intentpro.putExtra("ygid", ygid);
			intentpro.putExtra("type", 1);
			startActivity(intentpro);
			break;
		case R.id.rl_tomap:
			if (mClinic != null) 
				startActivity(new Intent(this, ShopMapPathPlanActivity.class).putExtra("bean", mClinic));
			break;
		default:
			break;
		}
	}
	
	private void removeFooterView() {
		listView.removeFooterView(noNetView);
		
	}

	/**
	 * 获取医馆详情
	 * @param ygid  医馆id
	 */
	private void getClinicMsg(String ygid) {
		String mLat2 = PreManager.instance().getLocation_x(ClinicDetatilActivity.this);
		String mLon2 = PreManager.instance().getLocation_y(ClinicDetatilActivity.this);
		
		new XiangchengProcClass(ClinicDetatilActivity.this).clinicDetail(ygid,mLat2, mLon2, 
				new InterfaceClinicDetailCallback() {

					@Override
					public void onSuccess(int icode, ClinicBean clinic,
							List<DoctorBean> doctorList, List<TaocanBean> shopList ) {
						mClinic = clinic; 
						headView.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(
								clinic.getPicture(), clinic.getPicture_key(),
								ivClinic, MyApplication.defaultOption,new ImageLoadingListener() {
									
									@Override
									public void onLoadingStarted(String arg0, View arg1) {
										
									}
									
									@Override
									public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
										
									}
									
									@Override
									public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
										ivClinic.setVisibility(View.VISIBLE);
									}
									
									@Override
									public void onLoadingCancelled(String arg0, View arg1) {
										
									}
								});
						title.setText(clinic.getName());
						tvClinicName.setText(clinic.getAddress());
//						tvAdddrss.setText(clinic.getAddress());
						clinicDoctors.addAll(doctorList);
						tvAdddrss.setText("距离"+clinic.getJuli()+(clinic.getJuli().equals("未知")?"":" km"));
//						getHosShopPros(clinic.getYgid());
						if(clinicDoctors==null||clinicDoctors.size()==0){
							showdocLin.setVisibility(View.GONE);
							moredocLin.setVisibility(View.GONE);
						}else{
							showdocLin.setVisibility(View.VISIBLE);
							moredocLin.setVisibility(View.VISIBLE);
							addViews(showdocLin, clinicDoctors);
						}
//						if(shopList == null || shopList.size() == 0){
//							moreproLin.setVisibility(View.GONE);
//						}else{
//							moreproLin.setVisibility(View.VISIBLE);
//						}
						moreproLin.setVisibility(View.GONE);
						adapter.setData(/*shopList*/new ArrayList<TaocanBean>());
						removeFooterView();
						listView.removeHeaderView(loadingView);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						Util.show(ClinicDetatilActivity.this, strErrMsg);
						listView.removeHeaderView(loadingView);
						if (-1 == PreManager
								.getNetType(ClinicDetatilActivity.this)) {
							listView.removeHeaderView(headView);
							noNetView.setVisibility(View.VISIBLE);
						}
					}
				});
	}
	

	
	private void addViews(LinearLayout linview, List<DoctorBean> pic_list){
		@SuppressWarnings("static-access")
		LayoutInflater inflater2 = getLayoutInflater().from(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((getScreenWidth()-Util.Dp2Px(this, 60))/3,LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(getScreenWidth()/5,getScreenWidth()/5);
		lp.leftMargin = Util.Dp2Px(this, 10);
		lp.rightMargin = Util.Dp2Px(this, 10);
		if(linview.getChildCount()!=0){
			linview.removeAllViews();
		}
		CircleImageView molderImg;
		TextView name;
		for (int i = 0; i < (pic_list.size() >3 ? 3 : pic_list.size()); i++) {
			DoctorBean mItemBean = pic_list.get(i);
			View view  = inflater2.inflate(R.layout.item_pic_and_text, null);
			molderImg = (CircleImageView) view.findViewById(R.id.item_pic_img);
			name  = (TextView) view.findViewById(R.id.pic_title);
			molderImg.setLayoutParams(lp2);
			view.setLayoutParams(lp);
			ImageLoader.getInstance().displayImage(mItemBean.getProfile(), mItemBean.getProfile_key(), molderImg, MyApplication.headPicOptn);
			name.setText(mItemBean.getName());
			linview.addView(view);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int index = position - listView.getHeaderViewsCount();
		if(index >= 0){
			TaocanBean bean = adapter.getData().get(index);
			AppManager.getAppManager().finishActivity(GoodsDetailActivity.class);
			startActivity(new Intent(ClinicDetatilActivity.this, GoodsDetailActivity.class)
			.putExtra("goods_id", bean.getTcid()).putExtra("kefu", bean.getKefu()));
		}
	}
	
}
