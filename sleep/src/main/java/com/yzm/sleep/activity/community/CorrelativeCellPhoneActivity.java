package com.yzm.sleep.activity.community;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.BoundPhoneParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceBoundPhoneCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCheckPhoneNumCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceRequestRegCodeCallBack;
import com.yzm.sleep.utils.PhoneProcClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.StringUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.UserInfoUtil;

public class CorrelativeCellPhoneActivity extends BaseActivity {
	private EditText etPhone, etAuthcode;
	private Button btnGetAuthcode, btnCorrePhone;
	private Context mContext;
	private Timer timer;
	private Button btnCommit;
	/** 手机号码 */
	private String phoneNumber;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				if (timer != null) {
					timer.cancel();
					btnGetAuthcode.setText("获取验证码");
					btnGetAuthcode.setClickable(true);
				}
			} else {
				btnGetAuthcode.setText("重新获取(" + msg.what + ")s");
				btnGetAuthcode.setClickable(false);
			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_correlative_cellphone);
		phoneNumber = getIntent().getStringExtra("phoneNumber");
		mContext = this;
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("LinkCellphone");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("LinkCellphone");
		MobclickAgent.onPause(this);
	}

	private void initView() {
		((TextView) findViewById(R.id.title)).setText("关联手机");
		etPhone = (EditText) findViewById(R.id.et_phone_number);
		etAuthcode = (EditText) findViewById(R.id.et_vercode);
		btnGetAuthcode = (Button) findViewById(R.id.btn_get_vercode);
		btnCorrePhone = (Button) findViewById(R.id.btn_bound);

		if (phoneNumber != null && !"".equals(phoneNumber)) {
			etAuthcode.setText(phoneNumber);
		} else {
			etAuthcode.setText("");
			etAuthcode.setHint("输入验证码");
		}
		String platformBoundMsg = PreManager.instance().getPlatformBoundMsg(
				this);
		boolean isPhoneBound = UserInfoUtil.isCommunityPlatformBound(
				platformBoundMsg, 3);
		findViewById(R.id.back).setOnClickListener(this);
		// 设置获取手机验证码点击监听
		btnGetAuthcode.setOnClickListener(this);

		// 设置关联手机点击监听
		btnCorrePhone.setOnClickListener(this);

		// 设置手机号码输入框文本改变监听
		etPhone.addTextChangedListener(new MyTextWatcher(0, 11));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_get_vercode:
			phoneNumber = etPhone.getText().toString();
			if (checkPhone(phoneNumber)) {
				getVerifyCode(phoneNumber);
			}
			break;
		case R.id.btn_bound:
			MobclickAgent.onEvent(this, "617");

			String phone = etPhone.getText().toString();
			String code = etAuthcode.getText().toString();
			if (checkPhone(phone)) {
				if (checkVerCode(code)) {
					correPhone(phone, "123456", code);//关联手机，默认密码为123456
				}

			}
			break;
		default:
			break;
		}
	}

	public class MyTextWatcher implements TextWatcher {
		private int mETType = 0;
		private int mMaxSize = 0;

		/**
		 * 
		 * @param etType
		 *            0:手机号码 ， 1：密码
		 */
		public MyTextWatcher(int etType, int maxSize) {
			this.mETType = etType;
			this.mMaxSize = maxSize;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			switch (mETType) {
			case 0:
				if (s.length() > mMaxSize) {
					String string = s.toString();
					etPhone.setText(string.substring(0, mMaxSize));
					etPhone.setSelection(mMaxSize);
				}

				break;
			case 1:
				if (s.length() > mMaxSize) {
					ToastManager.getInstance(mContext).show("密码长度应在6到20个字符之间");
				}
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 绑定手机 获取验证码
	 */
	private void getVerifyCode(String phoneNumber) {
		// 获取手机号是否被注册 绑定---调用internal_signup_1上传手机号
		new PhoneProcClass(mContext).CheckPhoneRegStat(phoneNumber,
				new InterfaceCheckPhoneNumCallBack() {

					@Override
					public void onSuccess(int iCode) {// 该手机号未被注册 绑定（可以绑定手机号）

						// 开启获取验证码倒计时
						timer = new Timer();
						timer.schedule(new TimerTask() {
							int waitTime = 60;

							@Override
							public void run() {
								mHandler.sendEmptyMessage(waitTime--);
							}
						}, 0, 1000);

						// 发送验证码---调用internal_signup_2
						new PhoneProcClass(mContext).RequestRegCode(etPhone
								.getText().toString(),
								new InterfaceRequestRegCodeCallBack() {

									@Override
									public void onSuccess(int iCode) {// 发送验证码成功，不做任何操作

									}

									@Override
									public void onError(int icode,
											String strErrMsg) {// 发送验证码失败，提示手机号无效，返回好友排行界面
										ToastManager.getInstance(mContext)
												.show(strErrMsg);
										if (timer != null) {
											timer.cancel();
										}
									}
								});
					}

					@Override
					public void onError(int icode, String strErrMsg) {// 该手机号已经注册
																		// 绑定（无法绑定手机号）
						ToastManager.getInstance(mContext).show(strErrMsg);
					}
				});

	}

	/**
	 * 关联手机
	 * 
	 * @param phoneNmu
	 *            手机号码
	 * @param pwd
	 *            密码
	 * @param code
	 *            验证码
	 */
	private void correPhone(final String phoneNmu, String pwd, String code) {
		BoundPhoneParamClass mBoundPhoneParamClass = new BoundPhoneParamClass();
		mBoundPhoneParamClass.my_phone_num = phoneNmu;
		mBoundPhoneParamClass.verification_code = code;
		mBoundPhoneParamClass.target_int_id = PreManager.instance().getUserId(
				mContext);
		mBoundPhoneParamClass.pwd = pwd;
		new CommunityProcClass(mContext).boundPhone(mBoundPhoneParamClass,
				new InterfaceBoundPhoneCallBack() {

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						saveBoundMsg(phoneNmu);
						if ("1".equals(getIntent()
								.getStringExtra("need_result"))) {
							Intent mIntent = getIntent();
							mIntent.putExtra("phone_num", phoneNmu);
							setResult(Constant.BUNDPHONERESULTCODE, mIntent);
						}
						AppManager.getAppManager().finishActivity();
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						ToastManager.getInstance(mContext).show(strErrMsg);
					}
				});
	}

	private void saveBoundMsg(String phoneNumber) {
		String platformBoundMsg = PreManager.instance().getPlatformBoundMsg(
				mContext);// 0000
		PreManager.instance().savePlatformBoundMsg(mContext,
				platformBoundMsg.substring(0, 3) + "1");
		if (phoneNumber != null) {
			PreManager.instance().saveBoundPhoneNumber(
					CorrelativeCellPhoneActivity.this, phoneNumber);
		}
	}

	/**
	 * 检查手机号码
	 */
	private boolean checkPhone(String phone) {
		if (!TextUtils.isEmpty(phone)) {
			if (StringUtil.isMobile(phone)) {
				return true;
			} else {
				ToastManager.getInstance(mContext).show("手机号码不正确，请核对后重新输入");
			}
		} else {
			ToastManager.getInstance(mContext).show("请输入手机号码");
		}
		return false;
	}


	/**
	 * 检查验证码
	 */
	private boolean checkVerCode(String verCode) {
		if (!TextUtils.isEmpty(verCode)) {
			return true;
		} else {
			ToastManager.getInstance(mContext).show("请输入验证码");
		}
		return false;
	}
}
