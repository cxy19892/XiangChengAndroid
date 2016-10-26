package com.yzm.sleep.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.bean.SaveOrderBean;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceModifyDeliveryAddressCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.ModifyDeliveryAddressParamClass;
import com.yzm.sleep.utils.MyTextWatcher;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 收货地址
 * 
 * @author Administrator
 * 
 */
public class GoodsAddressActivity extends BaseActivity {
	private EditText ed_goodsUserName, ed_goodsPhon, ed_goodsaddress;
	private Button btn_title_right2;
	private Context context;
	private SaveOrderBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goodsaddress);
		this.context = this; 
		bean = (SaveOrderBean) getIntent().getSerializableExtra("bean");
		initView();
	}

	private void initView() {
		ed_goodsUserName = (EditText) findViewById(R.id.ed_goodsUserName);
		ed_goodsPhon = (EditText) findViewById(R.id.ed_goodsPhon);
		ed_goodsaddress = (EditText) findViewById(R.id.ed_goodsaddress);

		btn_title_right2 = (Button) findViewById(R.id.btn_title_right2);
		btn_title_right2.setVisibility(View.VISIBLE);
		btn_title_right2.setText("保存");
		btn_title_right2.setOnClickListener(this);

		findViewById(R.id.back).setOnClickListener(this);
		((TextView) findViewById(R.id.title)).setText("收货信息");
		ed_goodsUserName.addTextChangedListener(new MyTextWatcher(ed_goodsUserName, this, 10, null));
		ed_goodsaddress.addTextChangedListener(new MyTextWatcher(ed_goodsUserName, this, Integer.MAX_VALUE, null));
		ed_goodsUserName.setText(bean.getRealname());
		ed_goodsPhon.setText(bean.getPhone());
		ed_goodsaddress.setText(bean.getAddress());
	}
	
	private void save(){
		final String userName = ed_goodsUserName.getText().toString().trim();
		if(TextUtils.isEmpty(userName)){
			Util.show(this, "填写收货人姓名");
			return;
		}
		final String phoneNum = ed_goodsPhon.getText().toString().trim();
		if(TextUtils.isEmpty(phoneNum)){
			Util.show(this, "填写收货人联系电话");
			return;
		}
		
		if(!StringUtil.isMobile(phoneNum)){
			Util.show(this, "填写正确的联系电话");
			return;
		}
		
		final String address = ed_goodsaddress.getText().toString().trim();
		if(TextUtils.isEmpty(address)){
			Util.show(this, "填写收货地址");
			return;
		}
		showPro();
		ModifyDeliveryAddressParamClass mParam = new ModifyDeliveryAddressParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(context);
		mParam.realname = userName;
		mParam.phone = phoneNum;
		mParam.address= address;
		new XiangchengMallProcClass(context).modifyDeliveryAddress(mParam, new InterfaceModifyDeliveryAddressCallback() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				cancelPro();
				bean.setRealname(userName);
				bean.setPhone(phoneNum);
				bean.setAddress(address);
				AppManager.getAppManager().finishActivity(ConfirmOrderActivity.class);
				startActivity(new Intent(context, ConfirmOrderActivity.class).putExtra("bean", bean));
				AppManager.getAppManager().finishActivity();
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				cancelPro();
				Util.show(context, "保存失败");
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
		case R.id.btn_title_right2:// 保存
			save();
			break;
		default:
			break;
		}
	}
}
