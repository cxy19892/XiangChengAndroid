package com.yzm.sleep.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
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

/**
 * 生日
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat")
public class FragmentUserBirthday extends Fragment {
	private EditText ed_userbirthday, ed_usermonth, ed_userday;// 第二阶段年月日

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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
		return inflater.inflate(R.layout.activity_birthday, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// 初始化控件
		super.onViewCreated(view, savedInstanceState);
		initView(view);

	}

	private void initView(View view) {
		ed_userbirthday = (EditText) view.findViewById(R.id.ed_userbirthday);
		ed_usermonth = (EditText) view.findViewById(R.id.ed_usermonth);
		ed_userday = (EditText) view.findViewById(R.id.ed_userday);
		ed_userbirthday.requestFocus();
		((InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE))
				.showSoftInput(ed_userbirthday, 0);

		ed_userbirthday.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String cont = ed_userbirthday.getText().toString();
				if (cont.length() > 3) {
					ed_userbirthday.clearFocus();
					ed_usermonth.requestFocus();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		ed_usermonth.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String cont = ed_usermonth.getText().toString();
				if (!cont.equals("00") && !cont.trim().equals("")) {
					int i = Integer.parseInt(cont);
					if (i > 1) {
						ed_usermonth.clearFocus();
						ed_userday.requestFocus();
					} else {
						if (cont.length() > 1) {
							ed_usermonth.clearFocus();
							ed_userday.requestFocus();
						}
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private InputMethodManager getSystemService(String inputMethodService) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDay() {
		String years = ed_userbirthday.getText().toString();
		String month = ed_usermonth.getText().toString();
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = ed_userday.getText().toString();
		if (day.length() == 1) {
			day = "0" + day;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		int systemDate = Integer.parseInt(formatter.format(curDate));
		if (years.length() == 0) {
			return false;
		}
		int strYears = Integer.parseInt(years);
		if (strYears > systemDate) {
			resetView();
			return false;
		} else {
			String data = years + month + day;
			if (chkDateFormat(data)) {
				if (isGreaterDate(data)) {
					if (!data.equals(PreManager.instance().getUserBirthday(
							getActivity()))) {
						PreManager.instance().setIsUpdata(getActivity(), true);
					}
					((AssessmentActivity) getActivity()).setData("birthday",
							data);
					// PreManager.instance().saveUserBirthday(
					// getActivity(), data);
					return true;
				} else {
					return false;
				}
			} else {
				resetView();
				return false;
			}
		}

	}

	private void resetView() {
		ed_userbirthday.requestFocus();
		ed_userbirthday.setText("");
		ed_usermonth.setText("");
		ed_userday.setText("");
	}

	/**
	 * 判断日期是否格式是否正确
	 * 
	 * @param date
	 * @return
	 */
	public static boolean chkDateFormat(String date) {
		try {
			// 如果输入日期不是8位的,判定为false.
			if (null == date || "".equals(date) || !date.matches("[0-9]{8}")) {
				return false;
			}
			int year = Integer.parseInt(date.substring(0, 4));
			int month = Integer.parseInt(date.substring(4, 6)) - 1;
			int day = Integer.parseInt(date.substring(6));
			Calendar calendar = GregorianCalendar.getInstance();
			// 当 Calendar 处于 non-lenient 模式时，如果其日历字段中存在任何不一致性，它都会抛出一个异常。
			calendar.setLenient(false);
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DATE, day);
			// 如果日期错误,执行该语句,必定抛出异常.
			calendar.get(Calendar.YEAR);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
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

	public String getBirthday() {
		String years = ed_userbirthday.getText().toString();
		String month = ed_usermonth.getText().toString();
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = ed_userday.getText().toString();
		if (day.length() == 1) {
			day = "0" + day;
		}

		return years + month + day;
	}
}
