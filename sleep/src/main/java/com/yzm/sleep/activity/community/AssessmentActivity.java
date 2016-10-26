package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.SleepPlanActivity;
import com.yzm.sleep.fragment.FragmentProfessional;
import com.yzm.sleep.fragment.FragmentUserBirthday;
import com.yzm.sleep.fragment.FragmentUserHeight;
import com.yzm.sleep.fragment.FragmentUserSex;
import com.yzm.sleep.fragment.FragmentUserWight;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.EditUserInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceEditUserInfoCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceSaveUserEvaluationCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.SaveUserEvaluationParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.XiangchengProcClass;

/**
 * 评估
 * 
 * (如果修改资料则需要传入 ：(int)type - update - title) type 1 = 性别 2:生日 。3：身高。4：体重。5：职业
 * update 传 true title 标题
 */
public class AssessmentActivity extends BaseActivity {

	private FragmentManager fm;
	private FragmentTransaction ft;
	private FragmentUserSex fragmentUserSex;
	private FragmentUserBirthday birthday;
	private FragmentUserHeight fragmentUserHeight;
	private FragmentUserWight fragmentUserWight;
	private FragmentProfessional fragmentTransaction;
	private Button but_complete, btn_back_assess;
	private List<Fragment> fragments;
	private final String TAB1 = "tab1";
	private final String TAB2 = "tab2";
	private final String TAB3 = "tab3";
	private final String TAB4 = "tab4";
	private final String TAB5 = "tab5";
	private HashMap<String, String> userInfo;
	private Context context;
	private ProgressUtils pro;
	private boolean isSaveAssess = false;
	private boolean isUpdate = false;
	private int type  = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assessment);
		this.context = this;

		isUpdate = getIntent().getBooleanExtra("update", false);
		if (isUpdate)
			initUpdateView();
		else
			initView();
	}

	private void initUpdateView() {
	    type = getIntent().getIntExtra("type", -1);
		String title = getIntent().getStringExtra("title");
		btn_back_assess = (Button) findViewById(R.id.back);
		btn_back_assess.setOnClickListener(this);
		((TextView) findViewById(R.id.title)).setText(title);
		
		but_complete = (Button) findViewById(R.id.btn_title_right);
		but_complete.setOnClickListener(this);
		but_complete.setCompoundDrawables(null, null, null, null);
		but_complete.setVisibility(View.VISIBLE);
		
		fragments = new ArrayList<Fragment>();

		fragmentUserSex = new FragmentUserSex();
		birthday = new FragmentUserBirthday();
		fragmentUserHeight = new FragmentUserHeight();
		fragmentUserWight = new FragmentUserWight();
		fragmentTransaction = new FragmentProfessional();
		getFragment(type);
	}

	private void initView() {
		((TextView) findViewById(R.id.title)).setText("评估");

		btn_back_assess = (Button) findViewById(R.id.back);
		btn_back_assess.setOnClickListener(this);

		but_complete = (Button) findViewById(R.id.btn_title_right);
		but_complete.setOnClickListener(this);
		but_complete.setCompoundDrawables(null, null, null, null);
		but_complete.setVisibility(View.VISIBLE);

		fragments = new ArrayList<Fragment>();

		fragmentUserSex = new FragmentUserSex();
		birthday = new FragmentUserBirthday();
		fragmentUserHeight = new FragmentUserHeight();
		fragmentUserWight = new FragmentUserWight();
		fragmentTransaction = new FragmentProfessional();

		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		addFragment();
		fragments.add(fragmentUserSex);
	}

	public void setData(String key, String value) {
		if (userInfo == null) {
			userInfo = new HashMap<String, String>();
		}
		userInfo.put(key, value);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_right:
			if (!isUpdate) {
				if (!but_complete.getText().toString().equals("完成")) {
					if (checkData(fragments.size())) {
						addFragment();
					}
				} else {
					if (checkData(fragments.size())) {
						if (!isSaveAssess)
							saveAssess();

						updataUserInfo();
					}
				}
			} else {
				if(getData(type).equals("")){
					toastMsg("输入错误。请重新输入");
				}else{
					Intent intent = new Intent();
					intent.putExtra("data", getData(type));
					AppManager.getAppManager().finishActivity(100003, intent);
				}
			}
			break;
		case R.id.back:
			if (!isUpdate) {
				if (fm.getBackStackEntryCount() > 1) {
					fm.popBackStack();
					if (fragments.size() == 0) {
						btn_back_assess.setVisibility(View.GONE);
					}
					fragments.remove(fragments.size() - 1);
					if (fragments.size() < 5) {
						but_complete.setText("下一步");
					}
				} else
					AppManager.getAppManager().finishActivity();
			} else {
				AppManager.getAppManager().finishActivity(100002, null);
			}
			break;
		default:
			break;
		}
	}

	private boolean checkData(int pos) {
		switch (pos) {
		case 1:
			if (!fragmentUserSex.isData()) {
				toastMsg("请选择性别");
				return false;
			}
			return true;
		case 2:
			if (!birthday.isDay()) {
				toastMsg("您输入日期有误");
				return false;
			}
			return true;
		case 3:
			if (!fragmentUserHeight.isData()) {
				return false;
			}
			return true;
		case 4:
			return fragmentUserWight.isData();
		case 5:
			return fragmentTransaction.isData();
		default:
			break;
		}
		return false;
	}

	/**
	 * 修改个人信息得到返回值
	 * @param pos
	 * @return
	 */
	private String getData(int pos){
		switch (pos) {
		case 1:
			return fragmentUserSex.getSex();
		case 2:
			return birthday.getBirthday();
		case 3:
			if(fragmentUserHeight.getHeight().equals("")){
				ToastManager.getInstance(AssessmentActivity.this).show("身高输入错误");
				return "";
			}else{
				return fragmentUserHeight.getHeight();
			}
		case 4:
			if(fragmentUserWight.getWight().equals("")){
				ToastManager.getInstance(AssessmentActivity.this).show("体重输入错误");
				return "";
			}else{
				return fragmentUserWight.getWight();
			}
		case 5:
			return fragmentTransaction.getProfession();
		default:
			break;
		}
		return "";
	}
	
	
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

	/**
	 * 保存用户是否评估
	 */
	private void saveAssess() {
		SaveUserEvaluationParamClass mParam = new SaveUserEvaluationParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(context);
		new XiangchengProcClass(context).saveUserEvaluation(mParam,
				new InterfaceSaveUserEvaluationCallback() {

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						PreManager.instance().saveUserIsAssess(context, "1");
						isSaveAssess = true;
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						isSaveAssess = false;
					}
				});
	}

	/**
	 * 更新 个人信息 type修改的类型 1性别,2职业,3身高,4体重,5生日
	 */
	private void updataUserInfo() {
		showPro();

		EditUserInfoParamClass mParam = new EditUserInfoParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(this);
		mParam.my_int_gender = userInfo.get("gender");
		mParam.user_internal_birthday = userInfo.get("birthday");
		mParam.user_internal_height = userInfo.get("height");
		mParam.user_internal_weight = userInfo.get("weight");
		mParam.my_int_occupation = userInfo.get("jobcode");

		new CommunityProcClass(getApplication()).editUserInfo(mParam,
				new InterfaceEditUserInfoCallBack() {
					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						PreManager.instance().saveUserGender(context,
								userInfo.get("gender"));
						PreManager.instance().saveUserBirthday(context,
								userInfo.get("birthday"));
						PreManager.instance().saveUserHeight(context,
								userInfo.get("height"));
						PreManager.instance().saveUserWeight(context,
								userInfo.get("weight"));
						PreManager.instance().saveUserOccupation(context,
								userInfo.get("jobcode"));

						Intent intent = new Intent(context,
								SleepPlanActivity.class);
						intent.putExtra("isFirst", PreManager.instance()
								.getIsFirstUse(context));
						intent.putExtra("user_info", userInfo);
						startActivity(intent);
						AppManager.getAppManager().finishActivity();
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						cancelPro();
						toastMsg("保存失败");
						// AppManager.getAppManager().finishActivity();
					}
				});

	}

	private void addFragment() {
		switch (fragments.size() + 1) {
		case 1:
			btn_back_assess.setVisibility(View.VISIBLE);
			fragmentUserSex = new FragmentUserSex();
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, fragmentUserSex, TAB1);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB1);
			but_complete.setText("下一步");
			ft.commit();
			break;
		case 2:
			btn_back_assess.setVisibility(View.VISIBLE);
			but_complete.setText("下一步");
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, birthday, TAB2);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB2);
			ft.commit();

			fragments.add(birthday);
			break;
		case 3:
			but_complete.setText("下一步");
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, fragmentUserHeight, TAB3);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB3);
			ft.commit();

			fragments.add(fragmentUserHeight);
			break;
		case 4:
			but_complete.setText("下一步");
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, fragmentUserWight, TAB4);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB4);
			ft.commit();

			fragments.add(fragmentUserWight);
			break;
		case 5:
			but_complete.setText("完成");
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, fragmentTransaction, TAB5);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB5);
			ft.commit();

			fragments.add(fragmentTransaction);
			break;

		default:
			break;
		}
	}

	/**
	 * 选择流程
	 * 
	 * @param type
	 */
	private void getFragment(int type) {
		fm = getSupportFragmentManager();
		switch (type) {
		case 1:
			btn_back_assess.setVisibility(View.VISIBLE);
			fragmentUserSex = new FragmentUserSex();
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, fragmentUserSex, TAB1);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB1);
			but_complete.setText("保存");
			ft.commit();
			break;
		case 2:
			btn_back_assess.setVisibility(View.VISIBLE);
			but_complete.setText("保存");
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, birthday, TAB2);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB2);
			ft.commit();

			break;
		case 3:
			but_complete.setText("保存");
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, fragmentUserHeight, TAB3);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB3);
			ft.commit();

			break;
		case 4:
			but_complete.setText("保存");
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, fragmentUserWight, TAB4);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB4);
			ft.commit();

			break;
		case 5:
			but_complete.setText("保存");
			ft = fm.beginTransaction();
			ft.replace(R.id.fragement_assess, fragmentTransaction, TAB5);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(TAB5);
			ft.commit();
			break;

		default:
			break;
		}
	}

}
