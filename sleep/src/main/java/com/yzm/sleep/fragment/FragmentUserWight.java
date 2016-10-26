package com.yzm.sleep.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.AssessmentActivity;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;

/**
 * 体重
 * 
 * @author Administrator
 * 
 */
public class FragmentUserWight extends Fragment {
	private EditText ed_userWeight;
	private String weight = "";
	private Activity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 初始化非控件对象
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 加载布局，或初始化布局对象
		return inflater.inflate(R.layout.activity_wight, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// 初始化控件
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	private void initView(View view) {
		ed_userWeight = (EditText) view.findViewById(R.id.ed_userweight);
		ed_userWeight.requestFocus();
		((InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE)).showSoftInput(ed_userWeight, 0);
		ed_userWeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				weight = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!"".equals(s.toString().trim())) {
					try {
						double tem1 = Double.parseDouble(s.toString());
						if (tem1 > 200) {
							double tem2 = Double.parseDouble(weight);
							if (tem2 < 200) {
								ed_userWeight.setText(weight);
								Util.show(activity, "超过了我们地记录了");
								ed_userWeight.setText("");
							} else {
								ed_userWeight.setText("");
							}
						}
					} catch (Exception e) {
						ed_userWeight.setText("");
					}
				}
			}
		});
	}

	public boolean isData() {
		weight = ed_userWeight.getText().toString();
		if (weight != null && weight.length() > 0) {
			if (weight.subSequence(0, 1).equals("0")) {
				Util.show(activity, "你是有多轻？");
			} else if (weight.subSequence(0, 1).equals(".")) {
				Util.show(activity, "体重输入不对");
			} else {
				if (!weight.equals(PreManager.instance()
						.getUserWeight(activity))) {
					PreManager.instance().setIsUpdata(activity, true);
				}
				((AssessmentActivity) activity).setData("weight", weight);

				// PreManager.instance().saveUserWeight(activity,
				// weight);
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	public String getWight() {
		weight = ed_userWeight.getText().toString();
		if(weight!=null && weight.equals("")){
			return "";
		}
		return weight;
	}
}
