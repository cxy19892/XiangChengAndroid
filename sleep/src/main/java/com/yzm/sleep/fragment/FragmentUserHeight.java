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
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.Util;

/**
 * 身高
 * 
 * @author Administrator
 * 
 */
public class FragmentUserHeight extends Fragment {
	// private RelativeLayout re_userHeight;// 第三阶段
	private EditText ed_userHeight;
	private Activity activity;
	private String height;

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
		return inflater.inflate(R.layout.activity_height, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// 初始化控件
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	private void initView(View view) {
		ed_userHeight = (EditText) view.findViewById(R.id.ed_userHeight);
		ed_userHeight.requestFocus();
		((InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE)).showSoftInput(ed_userHeight, 0);

		ed_userHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				height = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!"".equals(s.toString().trim())) {
					try {
						double tem1 = Double.parseDouble(s.toString());
						if (tem1 > 230) {
							double tem2 = Double.parseDouble(height);
							if (tem2 < 55) {
								ed_userHeight.setText(height);
								ed_userHeight.setSelection(height.length() - 1);
								Util.show(activity, "身高输入不对");
							} else {
								ed_userHeight.setText("");
							}
						}
					} catch (Exception e) {
						ed_userHeight.setText("");
					}
				}
			}
		});
	}

	public boolean isData() {
		height = ed_userHeight.getText().toString();
		if (height != null && height.length() > 0) {
			if (height.subSequence(0, 1).equals("0")) {
				Util.show(activity, "你是有多矮？");
			} else if (height.subSequence(0, 1).equals(".")) {
				Util.show(activity, "身高输入不对");
			} else if (height.indexOf(".")!=-1) {
				Util.show(activity, "身高不能有小数点");
				return false;
			} else if ( Double.parseDouble(height) > 230) {
				Util.show(activity, "身高输入不对");
				return false;
			}else if(Double.parseDouble(height) <55){
				Util.show(activity, "身高输入不对");
				return false;
			}else{
				if (!height.equals(PreManager.instance()
						.getUserHeight(activity))) {
					PreManager.instance().setIsUpdata(activity, true);
				}
				((AssessmentActivity) activity).setData("height", height);
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	public String getHeight() {
		height = ed_userHeight.getText().toString();
		if (Integer.parseInt(height) < 55) {
			ToastManager.getInstance(activity).show("身高输入不对");
			return "";
		} else if (Integer.parseInt(height) > 230) {
			return "";
		}
		return height;
	}
}
