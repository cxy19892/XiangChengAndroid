package com.yzm.sleep.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.pillow.PillDataCountActivity;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.background.DataUtils;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.background.SleepResult;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.model.SleepDistributionInfo;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.AvgTimeResult;
import com.yzm.sleep.render.GetSleepAvgTimeValueClass.GetAvgTimeParamItem;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserSleepDataListClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.WeiBoUserInfoListClass;

@SuppressLint("SimpleDateFormat") public class SleepUtils {
	public static float density;

	/**
	 * Returns true if the string is null or 0-length.
	 * 
	 * @param str
	 *            the string to be examined
	 * @return true if str is null or zero length
	 */
	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * get text of a EditText
	 * 
	 * @param et
	 * 
	 * @return
	 */
	public static String getTextOfView(TextView tv) {
		return tv.getText().toString();
	}

	public static String getTextOfView(EditText et) {
		return et.getText().toString().trim();
	}

	public static float dipToPx(Context context, float dp) {
		if (density == 0) {
			WindowManager wm = (WindowManager) context
					.getSystemService(context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);

			HLog.i("densityDpi", dm.densityDpi + "");
			density = dm.density;
		}
		return dp * density;
	}

	public static float pxToDip(Context context, float px) {
		if (density == 0) {
			WindowManager wm = (WindowManager) context
					.getSystemService(context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			density = dm.density;
		}
		return px / density;
	}

	public static float getScreenDensity(Context context) {
		if (density == 0) {
			WindowManager wm = (WindowManager) context
					.getSystemService(context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			density = dm.density;
		}
		return density;
	}

	/**
	 * 获取小时值
	 * 
	 * @param s
	 * @return
	 */
	public static float getHours(String s) {
		s = s.replaceAll("-", "0");
		return Integer.parseInt(s.substring(0, s.indexOf(":")))
				+ Integer.parseInt(s.substring(s.indexOf(":") + 1)) / 60F;
	}

	/**
	 * 获取时间小时
	 * 
	 * @param s
	 * @return
	 */
	public static int getHourValue(String s) {
		return Integer.parseInt(s.substring(0, s.indexOf(":")));
	}

	/**
	 * 获取时间分钟
	 * 
	 * @param s
	 * @return
	 */
	public static int getMinutValue(String s) {
		return Integer.parseInt(s.substring(s.indexOf(":") + 1));
	}

	/**
	 * 检测是否为为手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断网络是否连接可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected() && info.isAvailable()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					} else {
						Toast.makeText(context, "网络连接失败，请确认网络连接",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "网络连接失败，请确认网络连接",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(context, "网络连接失败，请确认网络连接", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			Log.v("error", e.toString());
		}

		return false;
	}

	/**
	 * 获取日期 （格式为yyyymmdd）
	 * 
	 * @param s
	 * @return
	 */
	public static String dateformat(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			// 如果是小写
			if (s.substring(i, i + 1).equals("-")) {
			} else {
				str += s.substring(i, i + 1);
			}
		}
		return str;
	}

	/**
	 * 验证邮箱格式
	 * 
	 * @param email
	 * @return
	 */
	public static boolean emailFormat(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	/**
	 * 截取scrollview的屏幕
	 * **/
	public static Bitmap getBitmapByView(ScrollView scrollView) {
		int h = 0;
		Bitmap bitmap = null;
		// 获取listView实际高度
		for (int i = 0; i < scrollView.getChildCount(); i++) {
			h += scrollView.getChildAt(i).getHeight();
			// scrollView.getChildAt(i).setBackgroundResource(R.drawable.bg_guide);
		}
		// 创建对应大小的bitmap
		bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
				Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		scrollView.draw(canvas);
		// 测试输出
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/DCIM/shoot.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (null != out) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 10, out);
				out.flush();
				out.close();
			}
		} catch (IOException e) {
		}
		return bitmap;
	}

	/**
	 * 截图listview
	 * **/
	public static Bitmap getbBitmap(ListView listView) {
		int h = 0;
		Bitmap bitmap = null;
		// 获取listView实际高度
		for (int i = 0; i < listView.getChildCount(); i++) {
			h += listView.getChildAt(i).getHeight();
		}
		// 创建对应大小的bitmap
		bitmap = Bitmap.createBitmap(listView.getWidth(), h,
				Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		listView.draw(canvas);
		// 测试输出
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/DCIM/shoot.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (null != out) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 10, out);
				out.flush();
				out.close();
			}
		} catch (IOException e) {
		}
		return bitmap;
	}

	public static Bitmap getBitmapByView(View view,String filePath) {
		int h = 0;
		Bitmap bitmap = null;
		// 获取listView实际高度
		h += view.getHeight();
		// 创建对应大小的bitmap
		bitmap = Bitmap.createBitmap(view.getWidth(), h,
				Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		// 测试输出
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (null != out) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
			}
		} catch (IOException e) {
		}
		return bitmap;
	}

	/**
	 * 判断字符串中是否包含某个字符
	 * 
	 * @param str
	 * @param s
	 * @return
	 */
	public static boolean IsContains(String str, String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 格式化方法，显示时间格式为00
	 * 
	 * @param x
	 * @return
	 */
	public static String formate(int x) {
		String s = "";
		if (x < 10) {
			s = "0" + x;
		} else {
			s = s + x;
		}
		return s;
	}

	/**
	 * 式化方法，显示时间格式为00
	 * 
	 * @param x
	 * @return
	 */
	public static String formate(String x) {
		String s = x;
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static boolean isGenderMale(String code) {
		if (code.equals("01")) {
			return true;
		} else if (code.equals("02")) {
			return false;
		}
		return false;
	}

	/**
	 * 根据用户出生年份显示年龄
	 * 
	 * @param birth
	 * @return
	 */
	public static String getBirthTime(String birth) {
		Calendar c = Calendar.getInstance();
		String birthTime = "";
		int birthNumber = 0;
		if (!TextUtils.isEmpty(birth)) {
			birthNumber = Integer.parseInt(birth);
		}
		if (birthNumber < 999) {// 年龄
			if (birthNumber >= 100) {
				birthTime = "Old People";
			} else {
				birthNumber = c.get(Calendar.YEAR) - birthNumber;
			}
		}
		// 出生年
		if (birthNumber < 1900) {
			birthTime = "Old People";
		} else if (birthNumber >= 1900 && birthNumber < 1910) {
			birthTime = "00后";
		} else if (birthNumber >= 1910 && birthNumber < 1920) {
			birthTime = "10后";
		} else if (birthNumber >= 1920 && birthNumber < 1930) {
			birthTime = "20后";
		} else if (birthNumber >= 1930 && birthNumber < 1940) {
			birthTime = "30后";
		} else if (birthNumber >= 1940 && birthNumber < 1950) {
			birthTime = "40后";
		} else if (birthNumber >= 1950 && birthNumber < 1960) {
			birthTime = "50后";
		} else if (birthNumber >= 1960 && birthNumber < 1970) {
			birthTime = "60后";
		} else if (birthNumber >= 1970 && birthNumber < 1980) {
			birthTime = "70后";
		} else if (birthNumber >= 1980 && birthNumber < 1990) {
			birthTime = "80后";
		} else if (birthNumber >= 1990 && birthNumber < 2000) {
			birthTime = "90后";
		} else if (birthNumber >= 2000 && birthNumber < 2010) {
			birthTime = "蛋蛋后";
		} else if (birthNumber >= 2010 && birthNumber < 2020) {
			birthTime = "一零后";
		} else if (birthNumber >= 2020) {
			birthTime = "熊孩子";
		}

		return birthTime;
	}

	public static boolean checkIsUpdate(Context c) {
		return PreManager.instance().getIsUpdateVersion(c);
	}

	/**
	 * 根据职业编号获取职业名称
	 * 
	 * @param occupationId
	 * @return
	 */
	public static String getOccupation1(String occupationId) {
		if (occupationId.equals("200000000")) {
			return "未分类";
		} else if (occupationId.equals("200000001")) {
			return "IT";
		} else if (occupationId.equals("200000002")) {
			return "金融";
		} else if (occupationId.equals("200000003")) {
			return "人事行政";
		} else if (occupationId.equals("200000004")) {
			return "教育法律";
		} else if (occupationId.equals("200000005")) {
			return "销售";
		} else if (occupationId.equals("200000006")) {
			return "房地产/建筑";
		} else if (occupationId.equals("200000007")) {
			return "文化传媒";
		} else if (occupationId.equals("200000008")) {
			return "物流";
		} else if (occupationId.equals("200000009")) {
			return "制造生产";
		} else if (occupationId.equals("200000010")) {
			return "医疗";
		} else if (occupationId.equals("200000011")) {
			return "服务业";
		} else if (occupationId.equals("200000012")) {
			return "学生/其它";
		}
		return "未知";
	}
	
	/**
	 * 获取软件记录的所有睡眠数据
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static List<SoftDayData> getSleepData(Context context) {
		if (context == null)
			return null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String crrentDate = sdf
					.format(new Date(System.currentTimeMillis()));
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			SQLiteDatabase db = helper.getWritableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM " + MyTabList.SLEEPTIME
					+ " where date != '" + crrentDate
					+ "' and record_state='4' order by date(date) asc", null);
			List<SoftDayData> data = new ArrayList<SoftDayData>();
			while (cursor.moveToNext()) {
				String sleepTime = cursor.getString(cursor
						.getColumnIndex(MyTabList.TableDay.SLEEPTIME
								.getCloumn()));
				String getUpTime = cursor.getString(cursor
						.getColumnIndex(MyTabList.TableDay.UPTIME.getCloumn()));
				String date = cursor.getString(cursor
						.getColumnIndex(MyTabList.TableDay.DATE.getCloumn()));
				SoftDayData dayData = new SoftDayData();
				dayData.setSleepTime(sleepTime);
				dayData.setGetUpTime(getUpTime);
				dayData.setDate(date);
				data.add(dayData);
			}
			cursor.close();
			return data;
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 获取本周软件记录的睡眠数据
	 * @param data
	 * @return
	 */ 
	@SuppressLint("SimpleDateFormat")
	public static List<SoftDayData> getWeekSoftSleepData(Context context) {
		List<SoftDayData> weekDatas = new ArrayList<SoftDayData>();
		List<String> list  = null;
//		if(TimeFormatUtil.getWeekAllOnMonday()){
//			list = TimeFormatUtil.getWeekAllOnDate("yyyy-MM-dd");
//		}else{
//			list = TimeFormatUtil.getWeekAllDateForPillow();
//		}
		list = CalenderUtil.getLastSevenDay(7);
		for (String string : list) {
			weekDatas.add(getDaySleepData(context,string, "1"));
		}
		return weekDatas;
	}

	/**
	 * 获取所有未上传的软件睡眠数据
	 * 
	 * @return
	 */
	public static  List<SleepResult> getUnUploadDaySleepData(Context context) {
		List<SleepResult> srList = new ArrayList<SleepResult>();
		try {
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			SQLiteDatabase db = helper.getWritableDatabase();
			Cursor result = db.rawQuery("select * from " + MyTabList.SLEEPTIME
					+ " where isupload != '1' and record_state == '4' and ispunch = '1' order by date(date)", null);

			while (result.moveToNext()) {
				SleepResult sleepResult = new SleepResult();
				sleepResult.setDate((result.getString(result
						.getColumnIndex("date"))) == null ? "" : result
						.getString(result.getColumnIndex("date")));
				String starttime = (result.getString(result
						.getColumnIndex("starttime"))) == null ? "" : result
						.getString(result.getColumnIndex("starttime"));
				if(starttime != null && !TextUtils.isEmpty(starttime)){
					sleepResult
					.setStarttime(DataUtils.dealData((int) (Float
							.valueOf(starttime) / 60))
							+ ":"
							+ DataUtils.dealData((int) (Float
									.valueOf(starttime) % 60)));
				}else{
					sleepResult
					.setStarttime("");
				}
				
				String endtime = (result.getString(result
						.getColumnIndex("endtime"))) == null ? "" : result
						.getString(result.getColumnIndex("endtime"));
				if(endtime != null && !TextUtils.isEmpty(endtime)){
					sleepResult
					.setEndtime(DataUtils.dealData((int) (Float
							.valueOf(endtime) / 60))
							+ ":"
							+ DataUtils.dealData((int) (Float
									.valueOf(endtime) % 60)));
				}else{
					sleepResult
					.setEndtime(endtime);
				}
				
				sleepResult.setSleeptime((result.getString(result.getColumnIndex("sleeptime"))) == null ? "" : result.getString(result.getColumnIndex("sleeptime")));
				sleepResult.setUptime((result.getString(result
						.getColumnIndex("uptime"))) == null ? "" : result
						.getString(result.getColumnIndex("uptime")));
				sleepResult.setOrgsleeptime((result.getString(result
						.getColumnIndex("orgsleeptime"))) == null ? "" : result
						.getString(result.getColumnIndex("orgsleeptime")));
				sleepResult.setOrguptime((result.getString(result
						.getColumnIndex("orguptime"))) == null ? "" : result
						.getString(result.getColumnIndex("orguptime")));
				
				sleepResult.setSleeptimelong((result.getString(result
						.getColumnIndex("sleeptimelong"))) == null ? "" : result
						.getString(result.getColumnIndex("sleeptimelong")));
				sleepResult.setUptimelong((result.getString(result
						.getColumnIndex("uptimelong"))) == null ? "" : result
						.getString(result.getColumnIndex("uptimelong")));
				
				sleepResult.setSleepLength((result.getString(result
						.getColumnIndex("sleeplength"))) == null ? "0" : result
						.getString(result.getColumnIndex("sleeplength")));
				sleepResult.setHealthSleep((result.getString(result
						.getColumnIndex("healthlength"))) == null ? "0"
						: result.getString(result
								.getColumnIndex("healthlength")));
				sleepResult
						.setSleep_acce(Float.valueOf(((result.getString(result
								.getColumnIndex("sleepacce"))) == null || ""
								.equals((result.getString(result
										.getColumnIndex("sleepacce"))))) ?

						"0" : result.getString(result
								.getColumnIndex("sleepacce"))));
				sleepResult
						.setGetup_acce(Float.valueOf(((result.getString(result
								.getColumnIndex("upacce"))) == null || ""
								.equals((result.getString(result
										.getColumnIndex("sleepacce"))))) ?

						"0" : result.getString(result.getColumnIndex("upacce"))));
				sleepResult.setUpdate(result.getString(result
						.getColumnIndex("ischange")));
				sleepResult.setUpload(result.getString(result
						.getColumnIndex("isupload")));
				sleepResult.setRecordState(result.getString(result
						.getColumnIndex("record_state")));
				String diagram = result.getString(result
						.getColumnIndex("diagramdata"));
				String diagramdata = diagram == null || "".equals(diagram) ? ""
						: diagram;
				ArrayList<SleepDistributionInfo> infoList = new ArrayList<SleepDistributionInfo>();
				if (diagramdata != null && !"".equals(diagramdata)) {
					JSONObject jo = new JSONObject(diagramdata);
					JSONArray ja = jo.getJSONArray("diagramdata");
					for (int i = 0; i < ja.length(); i++) {
						JSONArray jy = ja.getJSONArray(i);
						JSONObject jtime = jy.getJSONObject(0);
						JSONObject jacce = jy.getJSONObject(1);
						SleepDistributionInfo info = new SleepDistributionInfo();
						info.setTime(jtime.getString("time"));
						String acce = jacce.getString("acce");
						if(acce != null && !TextUtils.isEmpty(acce))
							info.setAccelerate_value(Float.valueOf(acce));
						else
							info.setAccelerate_value(0);
						infoList.add(info);
					}
				}
				sleepResult.setInfoList(infoList);

				srList.add(sleepResult);
			}
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return srList;
	}

	/**
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (pinfo != null) {
			int j = 0;
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
	}

	public static boolean isIntentAvailable(Context context, String action) {

		final PackageManager packageManager = context.getPackageManager();

		final Intent intent = new Intent(action);

		List<ResolveInfo> list =

		packageManager.queryIntentActivities(intent,

		PackageManager.MATCH_DEFAULT_ONLY);

		return list.size() > 0;

	}

	@SuppressLint("SimpleDateFormat")
	/**格式化时间*/
	public static String getFormatedDateTime(String pattern, long dateTime) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
		return sDateFormat.format(new Date(dateTime + 0));
	}

	/**
	 * 获取系统当前时间 yyyy-MM-dd HH：mm：ss
	 * 
	 * @return 时间字符串
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getSystemCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}

	/**
	 * 格式化时间，将毫秒转换为分:秒格式
	 * 
	 * @param time
	 * @return
	 * 
	 * */
	public static String formatMillisecondTime(long duration) {
		// 获取分钟
		String min = duration / (1000 * 60) + "";
		// 获取秒
		String sec = duration % (1000 * 60) + "";
		// 分钟只有一位时
		if (min.length() < 2) {
			min = "0" + duration / (1000 * 60) + "";
		} else {
			min = duration / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (duration % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (duration % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (duration % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (duration % (1000 * 60)) + "";
		}
		// 返回格式是分钟+秒截取两位
		return min + ":" + sec.trim().substring(0, 2);
	}

	/***
	 * 根究平台编号获取平台名称
	 * 
	 * @param code
	 * @return
	 */
	public static String getPlatformStringByCode(String code) {
		String string = "";
		if (code.equals("300000000")) {
			string = "xiangcheng";
		} else if (code.equals("300000001")) {
			string = "wechat";
		} else if (code.equals("300000002")) {
			string = "weibo";
		} else if (code.equals("300000003")) {
			string = "qq";
		}
		return string;
	}



	
	/**
	 * 清空睡眠数据
	 * @param context
	 */
	public static void deletSleepData(Context context){
		/*List<SleepResult> unUploadDaySleepData = getCompleteSleepData(context);
		if (unUploadDaySleepData.size() > 0) {
			SleepResult sleepResult = unUploadDaySleepData.get(unUploadDaySleepData.size()-1);
			
			UserSleepDataListClass dataListClass = new UserSleepDataListClass();
			dataListClass.date = sleepResult.getDate();
			dataListClass.user_internal_id = PreManager.instance().getUserId(context);
			dataListClass.sleep_point = sleepResult.getSleeptime();
			dataListClass.wakeup_point = sleepResult.getUptime();
			dataListClass.bueatysleep_duration = sleepResult.getSleepLength();
			dataListClass.user_location_x = PreManager.instance().getLocation_x(context);
			dataListClass.user_location_y = PreManager.instance().getLocation_y(context);
			saveDaySleepData(context, dataListClass);
		}*/
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.execSQL("delete from " + MyTabList.SLEEPTIME + " where date != (select date from " + MyTabList.SLEEPTIME + " where record_state = '4' order by date desc limit 1)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.close();
	}
	
	/**
	 * 获取所有未上传的软件睡眠数据
	 * 
	 * @return
	 */
	public static List<SleepResult> getCompleteSleepData(Context context) {
		List<SleepResult> srList = new ArrayList<SleepResult>();
		try {
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			SQLiteDatabase db = helper.getWritableDatabase();
			Cursor result = db.rawQuery("select * from " + MyTabList.SLEEPTIME
					+ " where record_state = '4' order by date(date)", null);

			while (result.moveToNext()) {
				SleepResult sleepResult = new SleepResult();
				sleepResult.setDate((result.getString(result
						.getColumnIndex("date"))) == null ? "" : result
						.getString(result.getColumnIndex("date")));
				String starttime = (result.getString(result
						.getColumnIndex("starttime"))) == null ? "" : result
						.getString(result.getColumnIndex("starttime"));
				if(starttime != null && !TextUtils.isEmpty(starttime)){
					sleepResult
					.setStarttime(DataUtils.dealData((int) (Float
							.valueOf(starttime) / 60))
							+ ":"
							+ DataUtils.dealData((int) (Float
									.valueOf(starttime) % 60)));
				}else{
					sleepResult
					.setStarttime("");
				}
				
				String endtime = (result.getString(result
						.getColumnIndex("endtime"))) == null ? "" : result
						.getString(result.getColumnIndex("endtime"));
				if(endtime != null && !TextUtils.isEmpty(endtime)){
					sleepResult
					.setEndtime(DataUtils.dealData((int) (Float
							.valueOf(endtime) / 60))
							+ ":"
							+ DataUtils.dealData((int) (Float
									.valueOf(endtime) % 60)));
				}else{
					sleepResult
					.setEndtime(endtime);
				}
				
				sleepResult.setSleeptime((result.getString(result.getColumnIndex("sleeptime"))) == null ? "" : result.getString(result.getColumnIndex("sleeptime")));
				sleepResult.setUptime((result.getString(result
						.getColumnIndex("uptime"))) == null ? "" : result
						.getString(result.getColumnIndex("uptime")));
				sleepResult.setOrgsleeptime((result.getString(result
						.getColumnIndex("orgsleeptime"))) == null ? "" : result
						.getString(result.getColumnIndex("orgsleeptime")));
				sleepResult.setOrguptime((result.getString(result
						.getColumnIndex("orguptime"))) == null ? "" : result
						.getString(result.getColumnIndex("orguptime")));
				sleepResult.setSleepLength((result.getString(result
						.getColumnIndex("sleeplength"))) == null ? "0" : result
						.getString(result.getColumnIndex("sleeplength")));
				sleepResult.setHealthSleep((result.getString(result
						.getColumnIndex("healthlength"))) == null ? "0"
						: result.getString(result
								.getColumnIndex("healthlength")));
				sleepResult
						.setSleep_acce(Float.valueOf(((result.getString(result
								.getColumnIndex("sleepacce"))) == null || ""
								.equals((result.getString(result
										.getColumnIndex("sleepacce"))))) ?

						"0" : result.getString(result
								.getColumnIndex("sleepacce"))));
				sleepResult
						.setGetup_acce(Float.valueOf(((result.getString(result
								.getColumnIndex("upacce"))) == null || ""
								.equals((result.getString(result
										.getColumnIndex("sleepacce"))))) ?

						"0" : result.getString(result.getColumnIndex("upacce"))));
				sleepResult.setUpdate(result.getString(result
						.getColumnIndex("ischange")));
				sleepResult.setUpload(result.getString(result
						.getColumnIndex("isupload")));
				sleepResult.setRecordState(result.getString(result
						.getColumnIndex("record_state")));
				String diagram = result.getString(result
						.getColumnIndex("diagramdata"));
				String diagramdata = diagram == null || "".equals(diagram) ? ""
						: diagram;
				ArrayList<SleepDistributionInfo> infoList = new ArrayList<SleepDistributionInfo>();
				if (diagramdata != null && !"".equals(diagramdata)) {
					JSONObject jo = new JSONObject(diagramdata);
					JSONArray ja = jo.getJSONArray("diagramdata");
					for (int i = 0; i < ja.length(); i++) {
						JSONArray jy = ja.getJSONArray(i);
						JSONObject jtime = jy.getJSONObject(0);
						JSONObject jacce = jy.getJSONObject(1);
						SleepDistributionInfo info = new SleepDistributionInfo();
						info.setTime(jtime.getString("time"));
						String acce = jacce.getString("acce");
						if(acce != null && !TextUtils.isEmpty(acce))
							info.setAccelerate_value(Float.valueOf(acce));
						else
							info.setAccelerate_value(0);
						infoList.add(info);
					}
				}
				sleepResult.setInfoList(infoList);

				srList.add(sleepResult);
			}
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return srList;
	}
	
	/**
	 * 保存软件最近一天有效数据记录睡眠数据
	 * @param m_list
	 */
	@SuppressLint("SimpleDateFormat")
	private static void saveDaySleepData(final Context context,final UserSleepDataListClass dataListClass){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		//删除日视图数据
		operate.delete(null, null);
		
		ContentValues cv = null;
		cv = new ContentValues();
		
		cv.put("date", dataListClass.date);
		cv.put("starttime", SleepInfo.SET_STARTTIME);
		cv.put("endtime", SleepInfo.SET_ENDTIME);
		
		
		cv.put("sleeptime", dataListClass.sleep_point);
		cv.put("uptime", dataListClass.wakeup_point);
//		cv.put(MyTabList.TableDay.ORGSLEEPTIME.getCloumn(), sleeptime);
//		cv.put(MyTabList.TableDay.ORGUPTIME.getCloumn(), uptime);
		cv.put(MyTabList.TableDay.ISCHANGE.getCloumn(), "1");
		cv.put(MyTabList.TableDay.RECORD_STATE.getCloumn(), "4");
		cv.put(MyTabList.TableDay.ISUPLOAD.getCloumn(), "1");
		cv.put(MyTabList.TableDay.ISPUNCH.getCloumn(), "1");
		operate.insert(cv);
	}
	
	/**
	 * 获取当天的睡眠数据
	 * 
	 * @return
	 */
	public static SoftDayData getDaySleepData(Context context,String date, String ispunch) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + MyTabList.SLEEPTIME
				+ " where date = ? and ispunch = ?", new String[] { date, ispunch});
		SoftDayData dayData = new SoftDayData();
		dayData.setDate(date);
		try {
			cursor.moveToFirst();
			String sleepTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.SLEEPTIME.getCloumn()));
			String getUpTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.UPTIME.getCloumn()));
			String sleepTimeLong = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.SLEEPTIMELONG.getCloumn()));
			String getUpTimeLong = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.UPTIMELONG.getCloumn()));
			String startTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.STARTTIME.getCloumn()));
			String endTime = cursor.getString(cursor
					.getColumnIndex(MyTabList.TableDay.ENDTIME.getCloumn()));
			
			dayData.setSleepTime(sleepTime);
			dayData.setGetUpTime(getUpTime);
			dayData.setSleepTimeLong(sleepTimeLong);
			dayData.setGetUpTimeLong(getUpTimeLong);
			dayData.setStartTime(startTime);
			dayData.setEndTime(endTime);

		} catch (Exception e) {
		}finally{
			cursor.close();
		}
		return dayData;
	}
	/**
	 * 获取当天的硬件睡眠数据
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") @SuppressWarnings("null")
	public static SoftDayData getDayPillowSleepData(Context context,String date) {
		SoftDayData softData = new SoftDayData();
		if (context == null)
			return null;
		PillowDataModel pModel = null;
		try {
			pModel = PillowDataOpera.queryDataFromSQL(
					context, new SimpleDateFormat("yyyy-MM-dd")
					.format(new SimpleDateFormat("yyyy-MM-dd")
					.parse(date)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (pModel == null) {
			return softData;
		}
		byte[] bfr = pModel.getBfr();
		if (bfr != null && bfr.length > 0) {
			SleepDataHead data = BluetoothDataFormatUtil.format3(bfr,
					10);
			if (data != null) {
				softData.setSleepTime(TimeFormatUtil.formatTime1(
						data.InSleepTime, "HH:mm"));
				softData.setGetUpTime(TimeFormatUtil.formatTime1(
						data.OutSleepTime, "HH:mm"));
				softData.setDate(date);
			}
		}
		return softData;
	}
	
	/**
	 * 获取本周硬件睡眠数据
	 * @return
	 */
	public static List<SoftDayData> getWeekPillowSleepData(Context context){
		List<SoftDayData> pillowdatas = new ArrayList<SoftDayData>();
		String[] days;
		if (TimeFormatUtil.getWeekAllOnMonday()) {
			days = TimeFormatUtil.getWeekAllOnDate("yyyy-MM-dd");
		}else {
			days = TimeFormatUtil.getWeekAllDateForPillow();
		}
		
		List<byte[]> list = new ArrayList<byte[]>();
		for (int i = 0; i < days.length; i++) {
			PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(
					context, days[i]);
			byte[] bfr = pModel.getBfr();
			try {
				SoftDayData softData = null;
				if (bfr != null && bfr.length > 0) {
					list.add(bfr);
					SleepDataHead data = BluetoothDataFormatUtil.format3(bfr,
							10);
					softData = new SoftDayData();
					softData.setSleepTime(TimeFormatUtil.formatTime1(
							data.InSleepTime, "HH:mm"));
					softData.setGetUpTime(TimeFormatUtil.formatTime1(
							data.OutSleepTime, "HH:mm"));
					softData.setDate(days[i]);
					softData.setTotalSleepTime(data.TotalSleepTime);
					
				}else
					list.add(null);
				
				pillowdatas.add(softData);
			} catch (Exception e) {
				LogUtil.i("masong_错误信息", e.getMessage());
			}
		}
		return pillowdatas;
		
	}

	/**
	 * 获取睡眠时长状态
	 * @param sleepLength  睡眠时长小时数
	 * @return int -1 偏短，0健康，1偏长
	 */
	public static int getSleepLengthState(Context context,float sleepLength){
		/**-1 偏短，0健康，1偏长*/
		int sleepTags [] = {-1,0,1};
		String userBirthday = PreManager.instance().getUserBirthday(context);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
		int birthInt = 0,currentYerInt = 0,age;
		try {
			Date parse = sdf1.parse(userBirthday);
			String birthday = sdf2.format(parse);
			String currrentYer = sdf2.format(new Date());
			birthInt = Integer.parseInt(birthday);
			currentYerInt = Integer.parseInt(currrentYer);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		age = currentYerInt - birthInt;
		if (age < 12) {
			return dividerSleepLength(sleepTags, sleepLength, 10, 12);
		}else if (age >=12 && age < 18) {
			return dividerSleepLength(sleepTags, sleepLength, 7, 9);
		}else if (age >=18 && age < 55) {
			return dividerSleepLength(sleepTags, sleepLength, 6.5f, 8.5f);
		}else if (age >= 55) {
			return dividerSleepLength(sleepTags, sleepLength, 5f, 7f);
		}
		
		return sleepTags[1];
	}
	
	/***
	 * 获取睡眠时长00小时00分格式  和画圆环数据算法相同
	 * @param dayData
	 * @return
	 */
	public static String getSleepLengthString(Context context,SoftDayData dayData){
		if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(context))) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				Date d1 = sdf.parse(dayData.getGetUpTime());
				Date d2 = sdf.parse(dayData.getSleepTime());
				float hour = (float)(d1.getTime() - d2.getTime())/(1000 * 60 * 60);
				if(hour < 0)
					hour += 24;
				int h = (int)hour;
				float mm = (60 * (hour - h));
				int m = Integer.parseInt(new BigDecimal(mm).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
				String hourString = "00";
				String minString = "00";
				if(h < 10)
					hourString = "0" + String.valueOf(h);
				else
					hourString = String.valueOf(h);
				if(m < 10)
					minString = "0" + String.valueOf(m);
				else 
					minString = String.valueOf(m);
				return hourString + "小时" + minString +"分";
			} catch (Exception e) {
			}
		}else {
			int totalHour = 0 ,totalMinute = 0;
			int totalSleepTime = dayData.getTotalSleepTime();
			totalHour = totalSleepTime / 60;
			totalMinute = totalSleepTime % 60;
			
			return formate(totalHour + totalMinute / 60) + "小时" + formate(totalMinute % 60) + "分";
		}
		return "00小时00分";
	}
	public static String getSleepLengthString1(SoftDayData dayData){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date d1 = sdf.parse(dayData.getGetUpTime());
			Date d2 = sdf.parse(dayData.getSleepTime());
			float hour = (float)(d1.getTime() - d2.getTime())/(1000 * 60 * 60);
			if(hour < 0)
				hour += 24;
			int h = (int)hour;
			float mm = (60 * (hour - h));
			int m = Integer.parseInt(new BigDecimal(mm).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			String hourString = "00";
			String minString = "00";
			if(h < 10)
				hourString = "0" + String.valueOf(h);
			else
				hourString = String.valueOf(h);
			if(m < 10)
				minString = "0" + String.valueOf(m);
			else 
				minString = String.valueOf(m);
			return hourString + ":" + minString;
		} catch (Exception e) {
		}
		return "00:00";
	}
	/**
	 * 获取周睡眠总时长
	 * 
	 * @return
	 */
	public static float getTotalSleepLengthSelf(List<SoftDayData> weekDatas) {
		if (weekDatas != null && weekDatas.size() > 0) {
			@SuppressWarnings("unused")
			int i = 0;
			float totalHour = 0;
			for (SoftDayData dayData : weekDatas) {
				if (dayData == null || dayData.getGetUpTime() == null
						|| dayData.getSleepTime() == null)
					continue;
				float dayLength = countLengthByTime(dayData.getSleepTime(),
						dayData.getGetUpTime());
				if (dayLength > 0) {
					totalHour += dayLength;
					i++;
				}
			}
			return totalHour;
		}
		return 0f;
	}
	/**
	 * 获取一周总时长  时长计算方法同圆环
	 * @param weekDatas
	 * @return
	 */
	public static String getTotalSleepLengthString(Context context,List<SoftDayData> weekDatas) {
		try {
			if (weekDatas != null && weekDatas.size() > 0) {
				int totalHour = 0 ,totalMinute = 0;
				if (TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(context))) {
					for (SoftDayData dayData : weekDatas) {
						if (dayData == null || dayData.getGetUpTime() == null
								|| dayData.getSleepTime() == null)
							continue;
						String dayLength = getSleepLengthString1(dayData);
						totalHour += Integer.parseInt(dayLength.split(":")[0]);
						totalMinute += Integer.parseInt(dayLength.split(":")[1]);
					}
				}else {
					for (SoftDayData dayData : weekDatas) {
						if (dayData == null || dayData.getGetUpTime() == null
								|| dayData.getSleepTime() == null)
							continue;
						totalMinute += dayData.getTotalSleepTime();
					}
				}
				return formate(totalHour + totalMinute / 60) + ":" + formate(totalMinute % 60);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "00:00";
		}
		return "00:00";
	}
	/**
	 * 获取一周平均总时长  时长计算方法同圆环
	 * @param weekDatas
	 * @return hh:mm
	 */
	public static String getAverageSleepLengthString(Context context,List<SoftDayData> weekDatas) {
		try {
			if (weekDatas != null && weekDatas.size() > 0) {
				int i = 0;
				for (SoftDayData softDayData : weekDatas) {
					if (softDayData == null || softDayData.getGetUpTime() == null
							|| softDayData.getSleepTime() == null)
						continue;
					float dayLength = SleepUtils.countLengthByTime(
							softDayData.getSleepTime(), softDayData.getGetUpTime());
					if (dayLength >0) {
						i++;
					}
				}
				
				String totalSleepLengthString = getTotalSleepLengthString(context,weekDatas);
				float mt = Integer.parseInt(totalSleepLengthString.split(":")[0]) * 60 + Integer.parseInt(totalSleepLengthString.split(":")[1]);
				int ma = Math.round(mt / i);
				int h = ma / 60;
				int m = ma % 60;
				return formate(h) + ":" + formate(m);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "00:00";
		}
		return "00:00";
	}
	/**
	 * 根据起床时间和入睡时间，计算睡眠时长
	 * 
	 * @param alarmSleepTime
	 *            入睡时间（开始时间）
	 * @param alarmGetUpTime
	 *            起床时间（结束时间）
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static float countLengthByTime(String sleepTime, String getUpTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			long sleepTimeLong = sdf.parse(sleepTime).getTime();
			long getUpTimeLong = sdf.parse(getUpTime).getTime();
			long totalTime = getUpTimeLong - sleepTimeLong;
			if (totalTime < 0)
				totalTime += 24 * 60 * 60 * 1000;
			return totalTime / ((float) 60 * 60 * 1000);
		} catch (Exception e) {
		}
		return 0;
	}
	/**
	 * 将float类型的小时数转为xx小时时xx分格式
	 * @param hour
	 * @return
	 */
	public static String getHoursFormat(float hour) {
		try {
			double floor = Math.floor(hour);
			double minu = Math.floor((hour - floor) * 60);
			String mHour = String.valueOf(floor);
			String mMinu = String.valueOf(minu);
			return mHour.substring(0, mHour.indexOf(".")) + "小时"
					+ mMinu.substring(0, mMinu.indexOf(".")) + "分";
		} catch (Exception e) {
			e.printStackTrace();
			return "0小时";
		}
	}
	
	/**
	 * 获取软件平均入睡时间点,起床时间点平均时长
	 * @return [0]入睡,[1]醒来
	 */
	public static String[] getAvgSleepTimeAndGetupTime(List<SoftDayData> weekDatas){
		String[] avg=new String[3];
		try{
			GetSleepAvgTimeValueClass avgTimeCls = new GetSleepAvgTimeValueClass();
			ArrayList<GetAvgTimeParamItem> list1 = new ArrayList<GetAvgTimeParamItem>();
			int totalTime = 0;
			for(int i=0;i<(weekDatas==null?0:weekDatas.size());i++){
				SoftDayData dayData=weekDatas.get(i);
				if(dayData!=null && !TextUtils.isEmpty(dayData.getGetUpTime())&&!TextUtils.isEmpty(dayData.getSleepTime())){
					int inSleepTime = TimeFormatUtil.timeToMiss(dayData.getSleepTime());
					int outSleepTime = TimeFormatUtil.timeToMiss(dayData.getGetUpTime());
					GetAvgTimeParamItem item = new GetAvgTimeParamItem();
					//以下3个值必须都有，没有的话，传0
					item.iInSleepTime = inSleepTime;
					item.iOutSleepTime = outSleepTime;
					try {
						item.iSleepCountTime = dayData.getTotalSleepTime();
					} catch (Exception e) {
						e.printStackTrace();
						item.iSleepCountTime = totalTime;
					} 
					list1.add(item);
				}
			}
			AvgTimeResult result = avgTimeCls.getAVG(list1);
			int avgInSleepTime = result.iAvgInSleepTime;
			int avgOutSleepTime = result.iAvgOutSleepTime;
			String avgInSleepTimeFormat = ((avgInSleepTime / 3600 ) < 10 ? "0" + (avgInSleepTime / 3600 ) : (avgInSleepTime / 3600 )) + ":" +  
					(((avgInSleepTime/60)%60 ) < 10 ? "0" + ((avgInSleepTime/60)%60 ) : ((avgInSleepTime/60)%60 ));
			
			String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600 ) < 10 ? "0" + (avgOutSleepTime / 3600 ) : (avgOutSleepTime / 3600 )) + ":" +  
					(((avgOutSleepTime/60)%60 ) < 10 ? "0" + ((avgOutSleepTime/60)%60 ) : ((avgOutSleepTime/60)%60 ));
			
			avg[0]=avgInSleepTimeFormat;
			avg[1]=avgOutSleepTimeFormat;
		}catch(Exception e){
			avg[0]="--:--";
			avg[1]="--:--";
		}
		return avg;
	}
	
	/**
	 * 获取硬件平均入睡时间点,起床时间点平均时长
	 * @return [0] 时长  [1]入睡,[2]醒来 
	 * @return
	 */
	public static String [] getPillAvgSleepTimeAndGetupTime(List<PillowDataModel> PillowDatas){
		String[] avgTimes=new String[3];
		GetSleepAvgTimeValueClass avgTimeCls = new GetSleepAvgTimeValueClass();
		ArrayList<GetAvgTimeParamItem> list1 = new ArrayList<GetAvgTimeParamItem>();
		int totalTime = 0;
		try {
			for (int i = 0; i < PillowDatas.size(); i++) {
				byte[] bfr = PillowDatas.get(i).getBfr();
				if (bfr != null) {
					SleepDataHead datahead = BluetoothDataFormatUtil.format3(bfr, 10);
					int inSleepTime = TimeFormatUtil.timeToMiss(TimeFormatUtil.formatTime1(datahead.InSleepTime, "HH:mm"));
					int outSleepTime = TimeFormatUtil.timeToMiss(TimeFormatUtil.formatTime1(datahead.OutSleepTime, "HH:mm"));
					totalTime += datahead.TotalSleepTime;
					GetAvgTimeParamItem item = new GetAvgTimeParamItem();
					//以下3个值必须都有，没有的话，传0
					item.iInSleepTime = inSleepTime;
					item.iOutSleepTime = outSleepTime;
					item.iSleepCountTime = totalTime; 
					list1.add(item);
				}
			}
			AvgTimeResult result = avgTimeCls.getAVG(list1);
			if(result!=null){
				int avgInSleepTime = result.iAvgInSleepTime;
				int avgOutSleepTime = result.iAvgOutSleepTime;
				int avgTotalSleepTime = totalTime/list1.size();
				
				String avgInSleepTimeFormat = ((avgInSleepTime / 3600 ) < 10 ? "0" + (avgInSleepTime / 3600 ) : (avgInSleepTime / 3600 )) + ":" +  
						(((avgInSleepTime/60)%60 ) < 10 ? "0" + ((avgInSleepTime/60)%60 ) : ((avgInSleepTime/60)%60 ));
				String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600 ) < 10 ? "0" + (avgOutSleepTime / 3600 ) : (avgOutSleepTime / 3600 )) + ":" +  
						(((avgOutSleepTime/60)%60 ) < 10 ? "0" + ((avgOutSleepTime/60)%60 ) : ((avgOutSleepTime/60)%60 ));
				String avgTotalTimeFormat = ((avgTotalSleepTime / 60 ) < 10 ? "0" + (avgTotalSleepTime / 60 ) : (avgTotalSleepTime / 60 )) + ":" +  
						((avgTotalSleepTime%60 ) < 10 ? "0" + (avgTotalSleepTime%60 ) : (avgTotalSleepTime%60 ));
				
				avgTimes[0]=avgTotalTimeFormat;
				avgTimes[1]=avgInSleepTimeFormat;
				avgTimes[2]=avgOutSleepTimeFormat;
			}else{
				avgTimes[0]="00:00";
				avgTimes[1]="--:--";
				avgTimes[2]="--:--";
			}
		} catch (Exception e) {
			avgTimes[0]="00:00";
			avgTimes[1]="--:--";
			avgTimes[2]="--:--";
		}
		return avgTimes;
	}
	

	/**
	 * 获取硬件平均入睡时间点,起床时间点平均时长
	 * @return [0]入睡,[1]醒来
	 * @return
	 */
	public static String [] getPillAvgSleepTimeAndGetupTime(Activity activity,String[] daysForSoft){
		String[] avgTimes=new String[3];
		try {
			GetSleepAvgTimeValueClass avgTimeCls = new GetSleepAvgTimeValueClass();
			ArrayList<GetAvgTimeParamItem> list1 = new ArrayList<GetAvgTimeParamItem>();
			int totalTime = 0;
//			int k = 0;
			for (int i = 0; i < daysForSoft.length; i++) {
				PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(activity, daysForSoft[i]);
				byte[] bfr = pModel.getBfr();
				if (bfr != null) {
					SleepDataHead datahead = BluetoothDataFormatUtil.format3(bfr, 10);
					
					int inSleepTime = TimeFormatUtil.timeToMiss(TimeFormatUtil.formatTime1(datahead.InSleepTime, "HH:mm"));
					int outSleepTime = TimeFormatUtil.timeToMiss(TimeFormatUtil.formatTime1(datahead.OutSleepTime, "HH:mm"));
					totalTime += datahead.TotalSleepTime;
//					k++;
					GetAvgTimeParamItem item = new GetAvgTimeParamItem();
					//以下3个值必须都有，没有的话，传0
					item.iInSleepTime = inSleepTime;
					item.iOutSleepTime = outSleepTime;
					item.iSleepCountTime = totalTime; 
					list1.add(item);
				}
			}
			AvgTimeResult result = avgTimeCls.getAVG(list1);
			int avgInSleepTime = result.iAvgInSleepTime;
			int avgOutSleepTime = result.iAvgOutSleepTime;
//			int avgTotalSleepTime = totalTime/k;
			
			String avgInSleepTimeFormat = ((avgInSleepTime / 3600 ) < 10 ? "0" + (avgInSleepTime / 3600 ) : (avgInSleepTime / 3600 )) + ":" +  
					(((avgInSleepTime/60)%60 ) < 10 ? "0" + ((avgInSleepTime/60)%60 ) : ((avgInSleepTime/60)%60 ));
			String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600 ) < 10 ? "0" + (avgOutSleepTime / 3600 ) : (avgOutSleepTime / 3600 )) + ":" +  
					(((avgOutSleepTime/60)%60 ) < 10 ? "0" + ((avgOutSleepTime/60)%60 ) : ((avgOutSleepTime/60)%60 ));
//			String avgTotalTimeFormat = ((avgTotalSleepTime / 60 ) < 10 ? "0" + (avgTotalSleepTime / 60 ) : (avgTotalSleepTime / 60 )) + ":" +  
//					((avgTotalSleepTime%60 ) < 10 ? "0" + (avgTotalSleepTime%60 ) : (avgTotalSleepTime%60 ));
			
			avgTimes[0]=avgInSleepTimeFormat;
			avgTimes[1]=avgOutSleepTimeFormat;
//			avgTimes[2]=avgTotalTimeFormat;
			
		} catch (Exception e) {
			avgTimes[0]="";
			avgTimes[1]="";
//			avgTimes[2]="";
		}
		return avgTimes;
		
	}
	
	private static int dividerSleepLength(int[] sleepTags,float sleepLength,float len1,float len2){
		System.out.println("----->len1:"  + len1);
		System.out.println("----->len2:"  + len2);
		if (sleepLength <= len1) {
			return sleepTags[0];
		}else if (sleepLength > len1 && sleepLength <=len2) {
			return sleepTags[1];
		}else if(sleepLength > len2){
			return sleepTags[2];
		}
		System.out.println("----->reurn:"  + sleepTags[1]);
		return sleepTags[1];
	}
	public static void getSleepTimeState(){
		
	}
	public static void getGetUpTimeState(){
		
	}
	
	
	/**
	 * 枕头睡眠数据
	 */
	public static ArrayList<String> getByDateSleepDate(Context context){
		ArrayList<String> sleepWeekPillow = new ArrayList<String>();
		String alarmSleepTime, alarmGetUpTime,deepSleep="00小时00分",shallowSleep="00小时00分";
		int hasDataCoun = 0,totalTime = 0 , complateGetUpCount =0 ,hasDataCount = 0 ,complateSleepCount = 0;
		AvgTimeResult result;
		ArrayList<SoftDayData> softList=new ArrayList<SoftDayData>();
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
		alarmSleepTime = PreManager.instance().getSleepTime_Setting(context);
		alarmGetUpTime = PreManager.instance().getGetupTime_Setting(context);
		
		List<String> listDayDates = CalenderUtil.getLastSevenDay(7);
		GetSleepAvgTimeValueClass avgTimeCls = new GetSleepAvgTimeValueClass();
		ArrayList<GetAvgTimeParamItem> list1 = new ArrayList<GetAvgTimeParamItem>();
		int totalDeeptime=0,totalshallowSleep=0;
		ArrayList<byte[]> listByte;
		try {
			listByte = new ArrayList<byte[]>();
			for (int i = 0; i < listDayDates.size(); i++) {
				//获取一天的睡眠数据
				PillowDataModel pModel = PillowDataOpera.queryDataFromSQL(context, listDayDates.get(i));
				byte[] bfr = pModel.getBfr();
				SoftDayData softData = new SoftDayData();
				softData.setDate(listDayDates.get(i));
				listByte.add(bfr);
				try {
					if (bfr != null) {
						hasDataCount++;
						SleepDataHead datahead = BluetoothDataFormatUtil.format3(bfr, 10);
						softData.setSleepTime(TimeFormatUtil.formatTime1(datahead.InSleepTime, "HH:mm"));
						softData.setGetUpTime(TimeFormatUtil.formatTime1(datahead.OutSleepTime, "HH:mm"));
						softData.setSleepTimeLong(String.valueOf(datahead.InSleepTime * 1000));
						softData.setGetUpTimeLong(String.valueOf(datahead.OutSleepTime * 1000));
						
						int inSleepTime = TimeFormatUtil.timeToMiss(TimeFormatUtil.formatTime1(datahead.InSleepTime, "HH:mm"));
						int outSleepTime = TimeFormatUtil.timeToMiss(TimeFormatUtil.formatTime1(datahead.OutSleepTime, "HH:mm"));
						totalTime += datahead.TotalSleepTime;
						totalDeeptime+=datahead.Deep_Sleep;
						totalshallowSleep+=datahead.Shallow_Sleep;
						
						GetAvgTimeParamItem item = new GetAvgTimeParamItem();
						// 以下3个值必须都有，没有的话，传0
						item.iInSleepTime = inSleepTime;
						item.iOutSleepTime = outSleepTime;
						item.iSleepCountTime = totalTime;
						list1.add(item);
						
						//统计入睡达标的天数
						long targets=sdfTime.parse(alarmSleepTime).getTime();//目标入睡时间
						int targetsM=(int) (targets/(60 * 1000));
						int temp=inSleepTime-targetsM;
						if(temp>=-15 && temp<=15)
							complateSleepCount+=1;
						
						//统计起床达标的天数
						long targetu=sdfTime.parse(alarmGetUpTime).getTime();
						int targetMu=(int) (targetu/(60 * 1000));
						int tempup=outSleepTime-targetMu;
						if(tempup>=-15 && tempup<=15)
							complateGetUpCount+=1;
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				softList.add(softData);
			}
		} catch (Exception e) {
		}
		
		result = avgTimeCls.getAVG(list1);
		
		if(result!=null){
			int avgInSleepTime = result.iAvgInSleepTime;
			int avgOutSleepTime = result.iAvgOutSleepTime;
			int avgTotalSleepTime = totalTime / hasDataCount;
			int avgTotalDeepTime=totalDeeptime/hasDataCount;
			int avgTotalshallowSleep=totalshallowSleep/hasDataCount;

			String avgInSleepTimeFormat = ((avgInSleepTime / 3600) < 10 ? "0"+ (avgInSleepTime / 3600) : (avgInSleepTime / 3600))+ ":"+ (((avgInSleepTime / 60) % 60) < 10 ? "0"
							+ ((avgInSleepTime / 60) % 60): ((avgInSleepTime / 60) % 60));
			String avgOutSleepTimeFormat = ((avgOutSleepTime / 3600) < 10 ? "0"+ (avgOutSleepTime / 3600) : (avgOutSleepTime / 3600))
					+ ":"+ (((avgOutSleepTime / 60) % 60) < 10 ? "0"+ ((avgOutSleepTime / 60) % 60): ((avgOutSleepTime / 60) % 60));
			String avgTotalTimeFormat = ((avgTotalSleepTime / 60) < 10 ? "0"+ (avgTotalSleepTime / 60) : (avgTotalSleepTime / 60))+ ":"
					+ ((avgTotalSleepTime % 60) < 10 ? "0"+ (avgTotalSleepTime % 60): (avgTotalSleepTime % 60));
			String TotalTimeFormat = ((totalTime / 60) < 10 ? "0"+ (totalTime / 60) : (totalTime / 60))+ ":"
					+ ((totalTime % 60) < 10 ? "0"+ (totalTime % 60): (totalTime % 60));
			System.out.println("--->7天总时长：" + totalTime + "分钟");
			System.out.println("--->7天总时长：" + TotalTimeFormat);
			deepSleep= ((avgTotalDeepTime / 60) < 10 ? "0"+ (avgTotalDeepTime / 60) : (avgTotalDeepTime / 60))+ "小时"
					+ ((avgTotalDeepTime % 60) < 10 ? "0"+ (avgTotalDeepTime % 60): (avgTotalDeepTime % 60))+"分";
			
			shallowSleep=((avgTotalshallowSleep / 60) < 10 ? "0"+ (avgTotalshallowSleep / 60) : (avgTotalshallowSleep / 60))+ "小时"
					+ ((avgTotalshallowSleep % 60) < 10 ? "0"+ (avgTotalshallowSleep % 60): (avgTotalshallowSleep % 60))+"分";
			try {
				if (avgInSleepTimeFormat != null && avgInSleepTimeFormat.length()>0) {
//					average1.setText();
					sleepWeekPillow.add(new SimpleDateFormat("HH小时mm分").format(new SimpleDateFormat("HH:mm").parse(avgTotalTimeFormat).getTime()));
//					average2.setText(avgOutSleepTimeFormat);
					sleepWeekPillow.add(avgInSleepTimeFormat);
//					average3.setText(avgOutSleepTimeFormat);
					sleepWeekPillow.add(avgOutSleepTimeFormat);
					
					String[] sleepLength = TotalTimeFormat.split(":");
					sleepWeekPillow.add(sleepLength[0] + "小时" + sleepLength[1] + "分");
			    	
			    	float target=PreManager.instance().getRecommendTarget(context);
				} else {
					sleepWeekPillow.add("00小时00分");
					sleepWeekPillow.add("00:00");
					sleepWeekPillow.add("00:00");
					sleepWeekPillow.add("00小时00分");
				}
			} catch (Exception e) {
				sleepWeekPillow.add("00小时00分");
				sleepWeekPillow.add("00:00");
				sleepWeekPillow.add("00:00");
				sleepWeekPillow.add("00小时00分");
			}
		}else{
			sleepWeekPillow.add("00小时00分");
			sleepWeekPillow.add("00:00");
			sleepWeekPillow.add("00:00");
			sleepWeekPillow.add("00小时00分");
		}
		return sleepWeekPillow;
	}
}
