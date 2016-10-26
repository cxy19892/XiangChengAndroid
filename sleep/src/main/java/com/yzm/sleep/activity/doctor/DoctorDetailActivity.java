package com.yzm.sleep.activity.doctor;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
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
import com.yzm.sleep.bean.DoctorDetailBean;
import com.yzm.sleep.bean.TaocanBean;
import com.yzm.sleep.bean.UserMessageBean;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceDoctorDetailCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.JustifyTextView;

/**
* 专家详情界面
*
* @author hetonghua
*
*/
public class DoctorDetailActivity extends BaseActivity {
     private JustifyTextView tvDoctorIntro;
     private CircleImageView civDoctorHead;
//     private ImageView ivUserType;
     private TextView tvDoctor;
     private TextView tvZhichen;
     private TextView tvAddress;
     private TextView tvDistance;
     private String zjuid = "";
     private LinearLayout viewOther, viewData;
     private View loadingView, noNetView;
     private UserMessageBean userBean;
     private ListView docListv;
     private LinearLayout promoreLin;
     private HosShopProAdapter adapter;
     private String localLan="";
     private String locallon="";
     private View headView;
//     private SellEndDialog dialog;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_doctor_detail);
          initView();
     }

     @SuppressLint("NewApi")
     private void initView() {
          ((TextView) findViewById(R.id.title)).setText("专家详情");
          findViewById(R.id.back).setOnClickListener(this);
         
          @SuppressWarnings("static-access")
          LayoutInflater inflater = getLayoutInflater().from(this);
          headView = inflater.inflate(R.layout.head_doctor_detail, null);
          headView.setVisibility(View.INVISIBLE);
         
          docListv = (ListView) findViewById(R.id.doc_list_view);
          docListv.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
          promoreLin = (LinearLayout) headView.findViewById(R.id.lin_product_more);
          promoreLin.setOnClickListener(this);
          adapter = new HosShopProAdapter(DoctorDetailActivity.this);
          civDoctorHead = (CircleImageView) headView.findViewById(R.id.civDoctorHead);
//          ivUserType = (ImageView) findViewById(R.id.ivUserType);
          tvDoctor = (TextView) headView.findViewById(R.id.tvDoctor);
          tvZhichen = (TextView) headView.findViewById(R.id.tvZhichen);
          tvDoctorIntro = (JustifyTextView) headView.findViewById(R.id.tvDoctorIntro);
          tvAddress = (TextView) headView.findViewById(R.id.tvAddress);
          tvDistance=(TextView) headView.findViewById(R.id.tv_distance);
          headView.findViewById(R.id.rel_addr_lay).setOnClickListener(this);;
          headView.findViewById(R.id.btnChat).setOnClickListener(this);

          viewOther = (LinearLayout) findViewById(R.id.viewOther);
          viewData = (LinearLayout) headView.findViewById(R.id.viewData);
          loadingView = getLayoutInflater().inflate(R.layout.listview_loading,
                    null);
          noNetView = getLayoutInflater().inflate(R.layout.layout_no_net, null);
          viewOther.addView(loadingView);
          viewOther.addView(noNetView);
          docListv.addHeaderView(headView, null, false);
          noNetView.setVisibility(View.GONE);
          noNetView.setOnClickListener(new OnClickListener() {
              
               @Override
               public void onClick(View v) {
                    loadingView.setVisibility(View.VISIBLE);
                    noNetView.setVisibility(View.GONE);
                    getDoctorMsg(zjuid);
               }
          });

          try {
               zjuid = getIntent().getStringExtra("id");
          } catch (Exception e) {
               e.printStackTrace();
          }
          getDoctorMsg(zjuid);
          docListv.setOnItemClickListener(new OnItemClickListener() {

               @Override
               public void onItemClick(AdapterView<?> parent, View view,
                         int position, long id) {
                    int index = position - docListv.getHeaderViewsCount();
                    if(index >= 0){
                         TaocanBean bean = adapter.getData().get(index);
                         AppManager.getAppManager().finishActivity(GoodsDetailActivity.class);
                         startActivity(new Intent(DoctorDetailActivity.this, GoodsDetailActivity.class)
                         .putExtra("goods_id", bean.getTcid()).putExtra("kefu", bean.getKefu()));
                    }
               }
          });
          docListv.setAdapter(adapter);
     }
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Service_Institute_Intro_Expert_Detail");
		MobclickAgent.onResume(this); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Service_Institute_Intro_Expert_Detail"); 
		MobclickAgent.onPause(this);
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
			if (userBean != null) {
				Intent intent = new Intent(this, ReservationActivity.class);
				intent.putExtra("zjuid", zjuid);
				intent.putExtra("ygid", "");
				startActivity(intent);
			}
			}else{
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.lin_product_more:
			Intent intentpro = new Intent(DoctorDetailActivity.this, HosShopProlistActivity.class);
			intentpro.putExtra("ygid", zjuid);
			intentpro.putExtra("type", 2);
			startActivity(intentpro);
		break;
		
		case R.id.rel_addr_lay:
			ClinicBean mClinicBean = new ClinicBean();
			mClinicBean.setLocation_x(localLan);
			mClinicBean.setLocation_y(locallon);
			startActivity(new Intent(this, ShopMapPathPlanActivity.class).putExtra("bean", mClinicBean));
			break;
		default:
			break;
		}
	}


     /**
     * 获取专家详情
     *
     * @param zjuid
     *            专家uid
     */
     private void getDoctorMsg(final String zjuid) {
    	 String mLat2 = PreManager.instance().getLocation_x(DoctorDetailActivity.this);
 		 String mLon2 = PreManager.instance().getLocation_y(DoctorDetailActivity.this);
          new XiangchengProcClass(DoctorDetailActivity.this).doctorDetail(zjuid,mLat2, mLon2,
                    new InterfaceDoctorDetailCallback() {

                         @Override
                         public void onSuccess(int icode, DoctorDetailBean doctor, List<TaocanBean> list) {
                              headView.setVisibility(View.VISIBLE);
                              ImageLoader.getInstance().displayImage(
                                        doctor.getProfile(), doctor.getProfile_key(),
                                        civDoctorHead, MyApplication.headPicOptn);
                              tvDoctor.setText(doctor.getName());
                              tvZhichen.setText(doctor.getZhicheng() + " | " + doctor.getDanwei());
                              tvDoctorIntro.setText(doctor.getIntro());
                              tvAddress.setText(doctor.getAddress());
                              viewData.setVisibility(View.VISIBLE);
                              viewOther.setVisibility(View.GONE);
                              tvDistance.setText("距离"+doctor.getJuli()+(doctor.getJuli().equals("未知")?"":" km"));
                              userBean = new UserMessageBean();
                              userBean.setUid(doctor.getZjuid());
                              userBean.setNickname(doctor.getName());
                              userBean.setProfile(doctor.getProfile());
                              userBean.setProfile_key(doctor.getProfile_key());
                              localLan = doctor.getLocation_x();
                              locallon = doctor.getLocation_y();
                              userBean.setIs_zj("1");
                              /*if(list==null || list.size()==0){
                                   promoreLin.setVisibility(View.GONE);
                              }else{
                                   promoreLin.setVisibility(View.VISIBLE);
                              }
                              adapter.setData(list);*/
                              promoreLin.setVisibility(View.GONE);
                              adapter.setData(new ArrayList<TaocanBean>());
                         }

                         @Override
                         public void onError(int icode, String strErrMsg) {
                              Util.show(DoctorDetailActivity.this, strErrMsg);
                              loadingView.setVisibility(View.GONE);
                              noNetView.setVisibility(View.VISIBLE);
                         }
                    });
     }
    

}
