package com.yzm.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.model.RollPickerDialog.RollCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadSleepTimeSettingCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadSleepTimeSettingParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.CompleteUserInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceCompleteUserInfoCallback;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.MyTextWatcher;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.UserManagerProcClass;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 登陆成功后完善个人信息界面
 * 
 * @author Administrator
 * 
 */
public class PerfectUserDataActivity extends BaseActivity implements
		RollCallBack {
	private EditText et_perusername;
	private TextView tv_birthday, tv_month, tv_day;// 年月日
	private RelativeLayout re_birthday;
	private ImageView im_male, im_female, im_unknown;// 男。女。人妖
	private ProgressUtils progressUtils;
	private String sex = "", years = "", userName = " ", age = "",
			geander = "01";
	private RollPickerDialog pickerDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfectuserdata);
		initView();
	}

	private void initView() {
		pickerDialog = new RollPickerDialog(PerfectUserDataActivity.this, this);
		if (getIntent() != null) {
			age = getIntent().getStringExtra("userAge");
			geander = getIntent().getStringExtra("userGeander");
			userName = getIntent().getStringExtra("userName");
		}
		if (geander == null) {
			geander = "01";
		}
		((TextView) findViewById(R.id.title)).setText("登录成功");
		((Button) findViewById(R.id.back)).setVisibility(View.GONE);
		((Button) findViewById(R.id.btn_complete)).setOnClickListener(this);
		et_perusername = (EditText) findViewById(R.id.et_perusername);
		et_perusername.setText(userName);
		if (userName != null && userName.length() > 0)
			et_perusername.setSelection(userName.length());
		tv_birthday = (TextView) findViewById(R.id.tv_birthday);
		tv_month = (TextView) findViewById(R.id.tv_month);
		tv_day = (TextView) findViewById(R.id.tv_day);
		if (age != null && age.length() > 0) {
			tv_birthday.setText(age.substring(0, 4));
			if (age.substring(4, 5).equals("0")) {
				tv_month.setText(age.substring(5, 6));
			} else {
				tv_month.setText(age.substring(4, 6));
			}
			if (age.substring(6, 7).equals("0")) {
				tv_day.setText(age.substring(7, 8));
			} else {
				tv_day.setText(age.substring(6, 8));
			}
			String data1 = tv_birthday.getText().toString();
			String data2 = tv_month.getText().toString();
			String data3 = tv_day.getText().toString();
			years = data1 + data2 + data3;
		}

		((RelativeLayout) findViewById(R.id.re_nan)).setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.re_nv)).setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.re_renyao))
				.setOnClickListener(this);

		re_birthday = (RelativeLayout) findViewById(R.id.re_birthday);
		re_birthday.setOnClickListener(this);
		im_male = (ImageView) findViewById(R.id.im_male);
		im_female = (ImageView) findViewById(R.id.im_female);
		im_unknown = (ImageView) findViewById(R.id.im_unknown);

		et_perusername.addTextChangedListener(new MyTextWatcher(et_perusername,
				getApplicationContext(), 10, null));

		sexIcon(geander);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_complete:
			checkUserInformation();
			break;
		case R.id.re_birthday:
			String nian = tv_birthday.getText().toString();
			String yue = tv_month.getText().toString();
			String ri = tv_day.getText().toString();
			if (nian.equals("")) {
				nian = "1990";
			}
			if (yue.equals("")) {
				yue = "0";
			}
			if (ri.equals("")) {
				ri = "0";
			}
			pickerDialog.SetData(
							0,
							"选择生日",
							new int[] {1950,Integer.parseInt(TimeFormatUtil.getTodayYearTime()) },
							Integer.parseInt(nian) - 1950, new int[] { 1, 12 },
							Integer.parseInt(yue), new int[] { 1, 31 },
							Integer.parseInt(ri));
			pickerDialog.show();
			break;
		case R.id.re_nan:// 男
			sexIcon("01");
			break;
		case R.id.re_nv:// 女
			sexIcon("02");
			break;
		case R.id.re_renyao:// 人妖
			sexIcon("03");
			break;
		default:
			break;
		}
	}

	/*
	 * 更改ICon (男。女。人妖)
	 */
	private void sexIcon(String pos) {
		if (pos.equals("01")) {
			sex = "01";
			im_male.setBackgroundResource(R.drawable.ic_man_selected);
			im_female.setBackgroundResource(R.drawable.ic_woman_normal);
			im_unknown.setBackgroundResource(R.drawable.ic_other_normal);
		} else if (pos.equals("02")) {
			sex = "02";
			im_male.setBackgroundResource(R.drawable.ic_man_normal);
			im_female.setBackgroundResource(R.drawable.ic_woman_selected);
			im_unknown.setBackgroundResource(R.drawable.ic_other_normal);
		} else if (pos.equals("03")) {
			sex = "03";
			im_male.setBackgroundResource(R.drawable.ic_man_normal);
			im_female.setBackgroundResource(R.drawable.ic_woman_normal);
			im_unknown.setBackgroundResource(R.drawable.ic_other_selected);
		}

	}




	private void checkUserInformation() {
		userName = et_perusername.getText().toString();
		if (sex.equals("")) {
			toastMsg("还没选择性别");
		} else if (years.equals("")) {
			toastMsg("请选择出生日期");
		} else if (userName.equals("") || userName.length() == 0) {
			toastMsg("取个响亮的名字");
		} else if (userName.length() > 10) {
			toastMsg("您的名字没这么长吧？");
		} else if (userName.trim().length() <= 1) {
			ToastManager.getInstance(PerfectUserDataActivity.this).show(
					"名称长度不够");
		} else {
			PreManager.instance().saveUserBirthday(
					PerfectUserDataActivity.this, years);
			PreManager.instance().saveUserGender(PerfectUserDataActivity.this,
					sex);
			PreManager.instance().saveUpdateUserInfoState(
					PerfectUserDataActivity.this, "1");// 表示修改了基础数据尚未同步成功
			PreManager.instance().saveUserNickname(
					PerfectUserDataActivity.this, userName);
			upDateUserInfo();
		}

	}

	private void upDateUserInfo() {
		progressUtils = new ProgressUtils(PerfectUserDataActivity.this);
		progressUtils.setMessage("正在提交");
		progressUtils.show();
		CompleteUserInfoParamClass completeUserInfoParamClass = new CompleteUserInfoParamClass();
		completeUserInfoParamClass.my_int_id = PreManager.instance().getUserId(
				PerfectUserDataActivity.this);
		completeUserInfoParamClass.nickname = userName;
		completeUserInfoParamClass.usersex = sex;
		completeUserInfoParamClass.userbirthday = years;
		new XiangchengProcClass(PerfectUserDataActivity.this).completeUserInfo(
				completeUserInfoParamClass,
				new InterfaceCompleteUserInfoCallback() {

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						PreManager.instance().saveUpdateUserInfoState(
								PerfectUserDataActivity.this, "2");
						HuanXinLogin(
								PreManager.instance().getUserId(
										PerfectUserDataActivity.this), "123456");
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						progressUtils.dismiss();
						ToastManager.getInstance(getApplicationContext()).show(
								strErrMsg);
					}
				});

	}

	/**
	 * 环信登陆
	 * 
	 * @param userName
	 * @param password
	 */
	private void HuanXinLogin(String userName, String password) {
		LogUtil.i("Enid", "HuanXinLogin");
		EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							public void run() {
								uploadSleepTimeSetting(PerfectUserDataActivity.this);
								Log.d("main", "登陆聊天服务器成功！");
								PreManager.instance().saveIsLogin(
										PerfectUserDataActivity.this, true);// 保存登录成功信息
								EMChatManager.getInstance()
										.loadAllConversations();
								ToastManager.getInstance(
										PerfectUserDataActivity.this).show(
										"登录成功");
								// downloadSleepData();
								Intent intent = new Intent();
								intent.setAction(Constant.RECEVER_LOGIN_ACTION);
								PerfectUserDataActivity.this
										.sendBroadcast(intent);
								AppManager.getAppManager().finishActivity();
								AppManager.getAppManager().finishActivity(
										LoginActivity.class);
								progressUtils.dismiss();
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {
						LogUtil.i("Enid", "登录环形过程：" + progress);
					}

					@Override
					public void onError(int code, String message) {
						LogUtil.i("Enid", "登陆聊天服务器失败！");
						// MyApplication.instance().logout(null);
					}
				});
		progressUtils.dismiss();
	}

	public static void uploadSleepTimeSetting(Context context) {
		UploadSleepTimeSettingParamClass mParam = new UploadSleepTimeSettingParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(context);
		mParam.sleep_date = PreManager.instance().getSleepTime_Setting(context);
		mParam.wakeup_date = PreManager.instance()
				.getGetupTime_Setting(context);
		new UserManagerProcClass(context).UploadSleepTimeSetting(mParam,
				new InterfaceUploadSleepTimeSettingCallBack() {

					@Override
					public void onSuccess(int iCode, String strSuccMsg) {
					}

					@Override
					public void onError(int icode, String strErrMsg) {
					}
				});
	}

	/**
	 * 判断选择日期是否大于当前日期
	 * 
	 * @param x
	 *            选择日期
	 * @return
	 */
	public boolean isGreaterDate(String x) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		int ys = Integer.parseInt(str);
		int xs = Integer.parseInt(x);
		if (xs > ys) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void setResaultRoll(int select1, int select2, int select3) {
		// 提交年龄
		StringBuffer sb = new StringBuffer();
		sb.append(select1).append(select2 < 10 ? "0" + select2 : select2).append(select3 < 10 ? "0" + select3 : select3);
		String date = sb.toString();
		if (isGreaterDate(date)) {
			if (!date.equals(PreManager.instance().getUserBirthday(this))) {
				years = date;
				tv_birthday.setText(String.valueOf(select1));
				tv_month.setText(String.valueOf(select2));
				tv_day.setText(String.valueOf(select3));
			}
		} else {
			ToastManager.getInstance(getApplicationContext()).show(
					"选择的日期大于当前日期了");
		}
		pickerDialog.dismiss();
	}

	@Override
	public void setResaultRollString(String select1, String select2,
			String select3) {
	}
}
