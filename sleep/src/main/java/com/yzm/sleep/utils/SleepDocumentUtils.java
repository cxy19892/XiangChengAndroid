package com.yzm.sleep.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.yzm.sleep.R;

public class SleepDocumentUtils {

	private static final int SLEEP_TAG = 0;
	private static final int SLEEPLENGTH_TAG = 1;
	private static final int GETUP_TAG = 2;
	/** 睡眠时长分析结果 */
	private String analyze_sleep = "";
	/** 入睡时刻分析结果 */
	private String analyze_sleeptime = "";
	/** 起床时刻分析结果 */
	private String analyze_getuptime = "";
	String[] strComm = { "睡眠作为生命所必须的过程，是机体复原、整合和巩固记忆的重要环节，是健康不可缺少的组成部分。" };

	/**
	 * 根据睡觉时间获取分析结果
	 * 
	 * @param context
	 * @param sleepTime
	 *            入睡时间 00:00
	 * @param getupTime
	 *            起床时间 00:00
	 * @param SleepLength
	 *            睡觉时长 float 小时
	 * @return
	 */
	Context mContext;
	Object[] documents;

	@SuppressLint("SimpleDateFormat")
	public List<String> getAnalyzeResultBySleepTime(Context context,
			String sleepTime, Float sleeplength, String getupTime) {
		mContext = context;
		String[] week_sleeptime_health_male = getStringArray(R.array.week_sleeptime_health_male);
		String[] week_sleeptime_health_female = getStringArray(R.array.week_sleeptime_health_female);
		String[] week_sleeptime_late_male = getStringArray(R.array.week_sleeptime_late_male);
		String[] week_sleeptime_late_female = getStringArray(R.array.week_sleeptime_late_female);
		String[] week_sleeptime_xlate_male = getStringArray(R.array.week_sleeptime_xlate_male);
		String[] week_sleeptime_xlate_female = getStringArray(R.array.week_sleeptime_xlate_female);
		String[] week_sleeptime_xxlate_male = getStringArray(R.array.week_sleeptime_xxlate_male);
		String[] week_sleeptime_xxlate_female = getStringArray(R.array.week_sleeptime_xxlate_female);

		String[] week_sleeplength_child_less = getStringArray(R.array.week_sleeplength_child_less);
		String[] week_sleeplength_child_health = getStringArray(R.array.week_sleeplength_child_health);
		String[] week_sleeplength_child_more = getStringArray(R.array.week_sleeplength_child_more);

		String[] week_sleeplength_nonage_less = getStringArray(R.array.week_sleeplength_nonage_less);
		String[] week_sleeplength_nonage_health = getStringArray(R.array.week_sleeplength_nonage_health);
		String[] week_sleeplength_nonage_more = getStringArray(R.array.week_sleeplength_nonage_more);

		String[] week_sleeplength_adult_less = getStringArray(R.array.week_sleeplength_adult_less);
		String[] week_sleeplength_adult_health = getStringArray(R.array.week_sleeplength_adult_health);
		String[] week_sleeplength_adult_more = getStringArray(R.array.week_sleeplength_adult_more);

		String[] week_sleeplength_aged_less = getStringArray(R.array.week_sleeplength_aged_less);
		String[] week_sleeplength_aged_health = getStringArray(R.array.week_sleeplength_aged_health);
		String[] week_sleeplength_aged_more = getStringArray(R.array.week_sleeplength_aged_more);

		String[] week_getuptime_health_male = getStringArray(R.array.week_getuptime_health_male);
		String[] week_getuptime_health_female = getStringArray(R.array.week_getuptime_health_female);
		String[] week_getuptime_late_male = getStringArray(R.array.week_getuptime_late_male);
		String[] week_getuptime_late_female = getStringArray(R.array.week_getuptime_late_female);
		String[] week_getuptime_xlate_male = getStringArray(R.array.week_getuptime_xlate_male);
		String[] week_getuptime_xlate_female = getStringArray(R.array.week_getuptime_xlate_female);
		String[] week_getuptime_xxlate_male = getStringArray(R.array.week_getuptime_xxlate_male);
		String[] week_getuptime_xxlate_female = getStringArray(R.array.week_getuptime_xxlate_female);

		documents = new Object[] { week_sleeptime_health_male,
				week_sleeptime_health_female, week_sleeptime_late_male,
				week_sleeptime_late_female, week_sleeptime_xlate_male,
				week_sleeptime_xlate_female, week_sleeptime_xxlate_male,
				week_sleeptime_xxlate_female, week_sleeplength_child_less,
				week_sleeplength_child_health, week_sleeplength_child_more,
				week_sleeplength_nonage_less, week_sleeplength_nonage_health,
				week_sleeplength_nonage_more, week_sleeplength_adult_less,
				week_sleeplength_adult_health, week_sleeplength_adult_more,
				week_sleeplength_aged_less, week_sleeplength_aged_health,
				week_sleeplength_aged_more, week_getuptime_health_male,
				week_getuptime_health_female, week_getuptime_late_male,
				week_getuptime_late_female, week_getuptime_xlate_male,
				week_getuptime_xlate_female, week_getuptime_xxlate_male,
				week_getuptime_xxlate_female };

		String averageSleeptime = sleepTime;
		String averageUptime = getupTime;
		averageSleeptime = averageSleeptime.replace("-", "0");
		averageUptime = averageUptime.replace("-", "0");

		int age = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			java.util.Date d = sdf.parse(PreManager.instance().getUserBirthday(
					context));
			SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
			age = Integer.parseInt(sdfYear.format(d.getTime()));
		} catch (Exception e) {
		}
		if (age > 1000) {
			Calendar c = Calendar.getInstance();
			int i = c.get(Calendar.YEAR);
			age = i - age;
		}
		if (averageSleeptime == "" || averageSleeptime == null
				|| averageUptime == "" || averageUptime == null) {// || age == 0
			return null;
		}

		String userGender = PreManager.instance().getUserGender(context);

		float sleeptime_hour = SleepUtils.getHours(averageSleeptime);

		// 入睡时刻结果分析逻辑（新）
		if (userGender.equals("01")) {
			if (sleeptime_hour >= 12 && sleeptime_hour < 23) {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_HEALTH_MALE,
						week_sleeptime_health_male);
			} else if (sleeptime_hour >= 23 || sleeptime_hour < 1) {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_LATE_MALE,
						week_sleeptime_late_male);
			} else if (sleeptime_hour >= 1 && sleeptime_hour < 3) {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_XLATE_MALE,
						week_sleeptime_xlate_male);
			} else if (sleeptime_hour >= 3 && sleeptime_hour < 12) {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_XXLATE_MALE,
						week_sleeptime_xxlate_male);
			} else {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_XXLATE_MALE,
						week_sleeptime_xxlate_male);
			}
		} else {//if (userGender.equals("02")) 
			if (sleeptime_hour >= 12 && sleeptime_hour < 23) {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_HEALTH_FEMALE,
						week_sleeptime_health_female);
			} else if (sleeptime_hour >= 23 || sleeptime_hour < 1) {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_LATE_FEMALE,
						week_sleeptime_late_female);
			} else if (sleeptime_hour >= 1 && sleeptime_hour < 3) {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_XLATE_FEMALE,
						week_sleeptime_xlate_female);
			} else if (sleeptime_hour >= 3 && sleeptime_hour < 12) {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_XXLATE_FEMALE,
						week_sleeptime_xxlate_female);
			} else {
				setAnalyseTextID(SLEEP_TAG,
						WeekDocumentID.WEEK_SLEEPTIME_XXLATE_FEMALE,
						week_sleeptime_xxlate_female);
			}
		}

		/* 起床时刻结果分析 */
		float getuptime_hour = SleepUtils.getHours(averageUptime);

		// 起床时刻结果分析逻辑（新）
		if (userGender.equals("01")) {
			if (getuptime_hour < 8) {
				setAnalyseTextID(GETUP_TAG,
						WeekDocumentID.WEEK_GETUPTIME_HEALTH_MALE,
						week_getuptime_health_male);
			} else if (getuptime_hour >= 8 && getuptime_hour < 9) {
				setAnalyseTextID(GETUP_TAG,
						WeekDocumentID.WEEK_GETUPTIME_LATE_MALE,
						week_getuptime_late_male);
			} else if (getuptime_hour >= 9 && getuptime_hour < 10) {
				setAnalyseTextID(GETUP_TAG,
						WeekDocumentID.WEEK_GETUPTIME_XLATE_MALE,
						week_getuptime_xlate_male);
			} else if (getuptime_hour >= 10) {
				setAnalyseTextID(GETUP_TAG,
						WeekDocumentID.WEEK_GETUPTIME_XXLATE_MALE,
						week_getuptime_xxlate_male);
			}
		} else {// if (userGender.equals("02"))
			if (getuptime_hour < 8) {
				setAnalyseTextID(GETUP_TAG,
						WeekDocumentID.WEEK_GETUPTIME_HEALTH_FEMALE,
						week_getuptime_health_female);
			} else if (getuptime_hour >= 8 && getuptime_hour < 9) {
				setAnalyseTextID(GETUP_TAG,
						WeekDocumentID.WEEK_GETUPTIME_LATE_FEMALE,
						week_getuptime_late_female);
			} else if (getuptime_hour >= 9 && getuptime_hour < 10) {
				setAnalyseTextID(GETUP_TAG,
						WeekDocumentID.WEEK_GETUPTIME_XLATE_FEMALE,
						week_getuptime_xlate_female);
			} else if (getuptime_hour >= 10) {
				setAnalyseTextID(GETUP_TAG,
						WeekDocumentID.WEEK_GETUPTIME_XXLATE_FEMALE,
						week_getuptime_xxlate_female);
			}
		}

		/* 睡眠时长结果分析 */
		if (age < 12) {
			// setAnalyseTextID(SLEEPLENGTH_TAG, -1, strComm);
			if (sleeplength < 10) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_CHILD_LESS,
						week_sleeplength_child_less);
			} else if (sleeplength >= 10 && sleeplength <= 12) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_CHILD_HEALTH,
						week_sleeplength_child_health);
			} else if (sleeplength > 12) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_CHILD_MORE,
						week_sleeplength_child_more);
			}
		} else if (age >= 12 && age < 18) {// age >= 12 && age < 18
			if (sleeplength < 7) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_NONAGE_LESS,
						week_sleeplength_nonage_less);
			} else if (sleeplength >= 7 && sleeplength <= 9) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_NONAGE_HEALTH,
						week_sleeplength_nonage_health);
			} else if (sleeplength > 9) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_NONAGE_MORE,
						week_sleeplength_nonage_more);
			}
		} else if (age >= 18 && age < 55) {
			if (sleeplength < 6.5) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_ADULT_LESS,
						week_sleeplength_adult_less);
			} else if (sleeplength >= 6.5 && sleeplength <= 8.5) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_ADULT_HEALTH,
						week_sleeplength_adult_health);
			} else if (sleeplength > 8.5) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_ADULT_MORE,
						week_sleeplength_adult_more);
			}
		} else if (age >= 55) {
			if (sleeplength < 5) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_AGED_LESS,
						week_sleeplength_aged_less);
			} else if (sleeplength >= 5 && sleeplength <= 7) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_AGED_HEALTH,
						week_sleeplength_aged_health);
			} else if (sleeplength > 7) {
				setAnalyseTextID(SLEEPLENGTH_TAG,
						WeekDocumentID.WEEK_SLEEPLENGTH_AGED_MORE,
						week_sleeplength_aged_more);
			}
		}

		// 睡眠分析结果数据，0 睡觉时间分析结果 ，1睡眠时长分析结果，2起床时间分析结果
		List<String> sleepAnalyzeResultList = new ArrayList<String>();
		sleepAnalyzeResultList.add(analyze_sleeptime);
		sleepAnalyzeResultList.add(analyze_sleep);
		sleepAnalyzeResultList.add(analyze_getuptime);


		return sleepAnalyzeResultList;
	}

	private void setAnalyseTextID(int tagid, int id, String[] strs) {
		switch (tagid) {
		case SLEEP_TAG:
			String str = PreManager.instance().getSleepDocDate(mContext);
			if (!TextUtils.isEmpty(str)) {
				String[] temp = str.split(":");
				if (String.valueOf(id).equals(temp[0])) {
					try {
						int index = Integer.parseInt(temp[1]);
						analyze_sleeptime = strs[index];
					} catch (Exception e) {
						int index = (int) (Math.abs(Math.random() * strs.length
								- 0.1));
						analyze_sleeptime = strs[index];
						PreManager.instance().saveSleepDocDate(mContext,
								id + ":" + index);
					}
				} else {
					int index = (int) (Math.abs(Math.random() * strs.length
							- 0.1));
					analyze_sleeptime = strs[index];
					PreManager.instance().saveSleepDocDate(mContext,
							id + ":" + index);
				}
			} else {
				int index = (int) (Math.abs(Math.random() * strs.length - 0.1));
				analyze_sleeptime = strs[index];
				PreManager.instance().saveSleepDocDate(mContext,
						id + ":" + index);
			}
			break;
		case SLEEPLENGTH_TAG:

			String str1 = PreManager.instance().getSleepLengthDocDate(mContext);
			if (!TextUtils.isEmpty(str1)) {
				String[] temp = str1.split(":");
				if (String.valueOf(id).equals(temp[0])) {
					try {
						int index = Integer.parseInt(temp[1]);
						analyze_sleep = strs[index];
					} catch (Exception e) {
						if (id == -1) {
							analyze_sleep = strs[0];
							PreManager.instance().saveSleepLengthDocDate(
									mContext, id + ":-1");
						} else {
							int index = (int) (Math.abs(Math.random()
									* strs.length - 0.1));
							analyze_sleep = strs[index];
							PreManager.instance().saveSleepLengthDocDate(
									mContext, id + ":" + index);
						}
					}
				} else {
					if (id == -1) {
						analyze_sleep = strs[0];
						PreManager.instance().saveSleepLengthDocDate(mContext,
								id + ":-1");
					} else {
						int index = (int) (Math.abs(Math.random() * strs.length
								- 0.1));
						analyze_sleep = strs[index];
						PreManager.instance().saveSleepLengthDocDate(mContext,
								id + ":" + index);
					}
				}
			} else {
				if (id == -1) {
					analyze_sleep = strs[0];
					PreManager.instance().saveSleepLengthDocDate(mContext,
							id + ":-1");
				} else {
					int index = (int) (Math.abs(Math.random() * strs.length
							- 0.1));
					analyze_sleep = strs[index];
					PreManager.instance().saveSleepLengthDocDate(mContext,
							id + ":" + index);
				}
			}
			break;
		case GETUP_TAG:

			String str2 = PreManager.instance().getGetUpDocDate(mContext);
			if (!TextUtils.isEmpty(str2)) {
				String[] temp = str2.split(":");
				if (String.valueOf(id).equals(temp[0])) {
					try {
						int index = Integer.parseInt(temp[1]);
						analyze_getuptime = strs[index];
					} catch (Exception e) {
						int index = (int) (Math.abs(Math.random() * strs.length
								- 0.1));
						analyze_getuptime = strs[index];
						PreManager.instance().saveGetUpDocDate(mContext,
								id + ":" + index);
					}
				} else {
					int index = (int) (Math.abs(Math.random() * strs.length
							- 0.1));
					analyze_getuptime = strs[index];
					PreManager.instance().saveGetUpDocDate(mContext,
							id + ":" + index);
				}
			} else {
				int index = (int) (Math.abs(Math.random() * strs.length - 0.1));
				analyze_getuptime = strs[index];
				PreManager.instance().saveGetUpDocDate(mContext,
						id + ":" + index);
			}
			break;

		default:
			break;
		}
	}

	private String[] getStringArray(int id) {
		return mContext.getApplicationContext().getResources()
				.getStringArray(id);
	}

}
