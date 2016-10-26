package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.OccupationAdapter;
import com.yzm.sleep.adapter.SelectJobAdapter;
import com.yzm.sleep.bean.OccpationBean;
import com.yzm.sleep.bean.OccpationType;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.EditUserInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceEditUserInfoCallBack;
import com.yzm.sleep.utils.PreManager;

/**
 * 职业选择 需要传入 <type=1>评估 <type=2>修改职业
 * 
 * @author tianuxn
 * 
 */
public class SelectProfessionActivity extends BaseActivity {

	private ListView types, typeContens;
	private List<OccpationBean> typeData;
	private List<OccpationType> occpationTypes;
	private SelectJobAdapter jobAdapter;
	private OccupationAdapter occupationAdapter;
	private int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_profession);
		type = getIntent().getIntExtra("type", -1);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SelectOccupation");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SelectOccupation");
		MobclickAgent.onPause(this);
	}

	private void initData() {
		occpationTypes = PreManager.instance().getOccupationTypes();
		if (occpationTypes.size() > 0) {
			occpationTypes.get(0).setSelset(true);
			typeData = occpationTypes.get(0).getType();
		} else {
			typeData = new ArrayList<OccpationBean>();
		}
		occupationAdapter = new OccupationAdapter(this, occpationTypes);
		jobAdapter = new SelectJobAdapter(this, typeData);

		types.setAdapter(occupationAdapter);
		typeContens.setAdapter(jobAdapter);
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		if (type == 1) {
			findViewById(R.id.btn_title_right2).setVisibility(View.VISIBLE);
			;
			findViewById(R.id.btn_title_right2).setOnClickListener(this);
			title.setText("评估");
		} else {
			title.setText("职业选择");
		}
//		title.setText("");
		types = (ListView) findViewById(R.id.types);
		typeContens = (ListView) findViewById(R.id.type_contens);

		types.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < occpationTypes.size(); i++) {
					occpationTypes.get(i).setSelset(false);
				}
				occpationTypes.get(position).setSelset(true);
				occupationAdapter.setDatas(occpationTypes);
				jobAdapter.setDatas(typeData = occpationTypes.get(position)
						.getType());
			}
		});

		typeContens.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (type != 1) {
					Intent intent = getIntent();
					intent.putExtra("occupation", typeData.get(position));
					setResult(Constant.JOBSELECTRESULTCODE, intent);
					AppManager.getAppManager().finishActivity();
				} else {
					PreManager.instance().saveUserOccupation(
							SelectProfessionActivity.this,
							typeData.get(position).getCode());
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right2:
			PreManager.instance().saveUpdateUserInfoState(
					SelectProfessionActivity.this, "1");
			updataUserInfo();
			break;
		default:
			break;
		}
	}

	/**
	 * 更新 个人信息 type修改的类型 1性别,2职业,3身高,4体重,5生日
	 */
	private void updataUserInfo() {
		EditUserInfoParamClass mParam = new EditUserInfoParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(this);
		mParam.my_int_gender = PreManager.instance().getUserGender(
				SelectProfessionActivity.this);
		mParam.my_int_occupation = PreManager.instance().getUserOccupation(
				SelectProfessionActivity.this);
		mParam.user_internal_height = String.valueOf(PreManager.instance()
				.getUserHeight(SelectProfessionActivity.this));
		mParam.user_internal_weight = String.valueOf(PreManager.instance()
				.getUserWeight(SelectProfessionActivity.this));
		mParam.user_internal_birthday = PreManager.instance().getUserBirthday(
				SelectProfessionActivity.this);
		new CommunityProcClass(this).editUserInfo(mParam,
				new InterfaceEditUserInfoCallBack() {
					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						PreManager.instance().saveUpdateUserInfoState(
								SelectProfessionActivity.this, "2");
						toastMsg(strSuccMsg);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						toastMsg(strErrMsg);
						PreManager.instance().saveUpdateUserInfoState(
								SelectProfessionActivity.this, "1");
					}
				});

	}
}
