package com.yzm.sleep.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.bean.ReservationResultBean;
import com.yzm.sleep.bean.UserMessageBean;
import com.yzm.sleep.im.ChatActivity;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceReservationCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.ReservationParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 预约界面
 * 
 * @author hetonghua
 * 
 */
public class ReservationActivity extends BaseActivity {
	private EditText etYourState,etYourPhone;
	private String ygid = "",zjuid = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation);
		
		try {
			ygid = getIntent().getStringExtra("ygid");
			zjuid = getIntent().getStringExtra("zjuid");
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		((TextView) findViewById(R.id.title)).setText("咨询");
		etYourState = (EditText) findViewById(R.id.etYourState);
		etYourPhone = (EditText) findViewById(R.id.etYourPhone);
		if(!TextUtils.isEmpty(ygid)){
			etYourState.setHint("输入后，医馆助理将为您推荐最合适的专家");
			etYourPhone.setHint("方便医馆助理与您取得联系");
		}else {
			etYourState.setHint("输入后，方便专家了解您的病情");
			etYourPhone.setHint("方便专家与您取得联系");
		}
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity();
			}
		});
		findViewById(R.id.btnSend).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(etYourState.getText().toString())) {
					
					if (StringUtil.isMobile(etYourPhone.getText().toString().trim())) {
						
						userReservation(ygid, zjuid);
					}else {
						Util.show(ReservationActivity.this, "手机号码不对,请重新输入");
					}
				}else {
					Util.show(ReservationActivity.this, "请输入您的问题");
				}
			}
		});
		
	}
	
	/**
	 * y
	 * @param ygid  医馆id  从医馆详情预约
	 * @param zjuid 专家id  从专家详情预约
	 */
	private void userReservation(String ygid,String zjuid){
		ReservationParamClass mParams = new ReservationParamClass();
		mParams.uid = PreManager.instance().getUserId(ReservationActivity.this);
		mParams.ygid = ygid;
		mParams.zjuid = zjuid;
		mParams.phone = etYourPhone.getText().toString();
		mParams.content = etYourState.getText().toString();
		new XiangchengProcClass(ReservationActivity.this).userReservation(mParams, new InterfaceReservationCallback() {
			
			@Override
			public void onSuccess(int icode, ReservationResultBean info) {
				sendMessage(info);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				Util.show(ReservationActivity.this, strErrMsg);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Service_Institute_Intro_Consult");
		MobclickAgent.onResume(this); 
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Service_Institute_Intro_Consult"); 
		MobclickAgent.onPause(this);
	}
	
	/**
	 * 发消息
	 */
	private void sendMessage(ReservationResultBean info) {
		Intent intent = new Intent(ReservationActivity.this,
				ChatActivity.class);
		UserMessageBean userBean = new UserMessageBean();
			userBean.setUid(info.getKfuid() != null ? info.getKfuid() : "0");
			userBean.setNickname(info.getName() != null  ? info.getName() : "null");
			userBean.setProfile(info.getTx() != null ? info.getTx() : "");
			userBean.setProfile_key(info.getTx_key() != null ? info.getTx_key() : "");
			userBean.setAskString(etYourState.getText().toString());
			userBean.setIs_zj("1");
		if (userBean != null) {
			intent.putExtra("userBean", userBean);
		}
		startActivity(intent);
		AppManager.getAppManager().finishActivity(ReservationActivity.class);
	}
}
