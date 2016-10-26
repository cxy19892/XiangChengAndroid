package com.yzm.sleep.activity.doctor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.bean.ReservationDetailBean;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceReservationDetailCallback;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 预约情界面
 * @author hetonghua
 *
 */
public class ReservationDetailActivity extends BaseActivity{


	private TextView tvClinic,tvAddress,tvDoctor,tvOrderTime,tvPhone;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation_detail);
		((TextView)findViewById(R.id.title)).setText("预约详情");
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity();
			}
		});
		
		tvClinic = (TextView)findViewById(R.id.tvClinic);
		tvAddress = (TextView)findViewById(R.id.tvAddress);
		tvDoctor = (TextView)findViewById(R.id.tvDoctor);
		tvOrderTime = (TextView)findViewById(R.id.tvOrderTime);
		tvPhone = (TextView)findViewById(R.id.tvPhone);
		String id = "";
		try {
			id = getIntent().getStringExtra("id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		getReservationDetail(id);
		
	}
	
	/**
	 * 获取预约详情 
	 * @param id	预约id
	 */
	private void getReservationDetail(String id){
		new XiangchengProcClass(ReservationDetailActivity.this).reservationDetail(id, new InterfaceReservationDetailCallback() {
			
			@Override
			public void onSuccess(int icode, ReservationDetailBean reservation) {
				tvClinic.setText("医馆：" + reservation.getYgname());
				tvAddress.setText("地址：" + reservation.getAddress());
				tvDoctor.setText("医生：" + reservation.getYname());
				tvOrderTime.setText("时间：" + reservation.getYtime());
				tvPhone.setText("电话：" + reservation.getYphone());
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				Util.show(ReservationDetailActivity.this, strErrMsg);
			}
		});
	}
}
