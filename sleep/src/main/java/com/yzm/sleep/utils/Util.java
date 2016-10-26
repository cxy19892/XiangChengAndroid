package com.yzm.sleep.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import junit.framework.Assert;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.bean.BuildStringListIndexBean;
import com.yzm.sleep.bean.ComfortResult;
import com.yzm.sleep.bean.HomeDataBean;
import com.yzm.sleep.model.ModifySignInResult;
import com.yzm.sleep.model.SignInResult;

@SuppressLint("DefaultLocale")
public class Util {

	private static final String TAG = "SDK_Sample.Util";

	private static Dialog mProgressDialog;
	private static Toast mToast;

	/*
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src byte[] data
	 * 
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/*
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789ABCDEF";

	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String toHexString(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = null;
		try {
			bytes = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (bytes == null)
			return null;
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	// 转换十六进制编码为字符串
	public static String hexToString(String s) {
		if ("0x".equals(s.substring(0, 2))) {
			s = s.substring(2);
		}
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static byte[] getHtmlByteArray(final String url) {
		URL htmlUrl = null;
		InputStream inStream = null;
		try {
			htmlUrl = new URL(url);
			URLConnection connection = htmlUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = inputStreamToByte(inStream);

		return data;
	}

	public static byte[] inputStreamToByte(InputStream is) {
		try {
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] readFromFile(String fileName, int offset, int len) {
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			Log.i(TAG, "readFromFile: file not found");
			return null;
		}

		if (len == -1) {
			len = (int) file.length();
		}

		Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len
				+ " offset + len = " + (offset + len));

		if (offset < 0) {
			Log.e(TAG, "readFromFile invalid offset:" + offset);
			return null;
		}
		if (len <= 0) {
			Log.e(TAG, "readFromFile invalid len:" + len);
			return null;
		}
		if (offset + len > (int) file.length()) {
			Log.e(TAG, "readFromFile invalid file len:" + file.length());
			return null;
		}

		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len];
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 获取指定长度
	 * 
	 * @param str
	 *            原字符
	 * @param length
	 *            长度
	 * @return
	 */
	public static String getAssignLenght(String str, int length) {
		try {
			if (str.length() > length) {
				return str.substring(0, length) + "...";
			} else {
				return str.substring(0, length);
			}
		} catch (Exception e) {
			return str;
		}
	}

	public static int computeSampleSize(BitmapFactory.Options options,

	int minSideLength, int maxNumOfPixels) {

		int initialSize = computeInitialSampleSize(options, minSideLength,

		maxNumOfPixels);

		int roundedSize;

		if (initialSize <= 8) {

			roundedSize = 1;

			while (roundedSize < initialSize) {

				roundedSize <<= 1;

			}

		} else {

			roundedSize = (initialSize + 7) / 8 * 8;

		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,

	int minSideLength, int maxNumOfPixels) {

		double w = options.outWidth;

		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 :

		(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

		int upperBound = (minSideLength == -1) ? 128 :

		(int) Math.min(Math.floor(w / minSideLength),

		Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {

			// return the larger one when there is no overlapping zone.

			return lowerBound;

		}

		if ((maxNumOfPixels == -1) &&

		(minSideLength == -1)) {

			return 1;

		} else if (minSideLength == -1) {

			return lowerBound;

		} else {

			return upperBound;

		}
	}

	/**
	 * 以最省内存的方式读取图片
	 */
	public static Bitmap readBitmap(final String path) {
		try {
			FileInputStream stream = new FileInputStream(new File(path
					+ "test.jpg"));
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 8;
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, opts);
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

	public static Bitmap extractThumbNail(final String path, final int height,
			final int width, final boolean crop) {
		Assert.assertTrue(path != null && !path.equals("") && height > 0
				&& width > 0);

		BitmapFactory.Options options = new BitmapFactory.Options();

		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

			Log.d(TAG, "extractThumbNail: round=" + width + "x" + height
					+ ", crop=" + crop);
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = "
					+ beY);
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY)
					: (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;

			Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight
					+ ", orig=" + options.outWidth + "x" + options.outHeight
					+ ", sample=" + options.inSampleSize);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				Log.e(TAG, "bitmap decode failed");
				return null;
			}

			Log.i(TAG,
					"bitmap decoded size=" + bm.getWidth() + "x"
							+ bm.getHeight());
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth,
					newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm,
						(bm.getWidth() - width) >> 1,
						(bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
				Log.i(TAG,
						"bitmap croped size=" + bm.getWidth() + "x"
								+ bm.getHeight());
			}
			return bm;

		} catch (final OutOfMemoryError e) {
			Log.e(TAG, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}

	public static final void showResultDialog(Context context, String msg,
			String title) {
		if (msg == null)
			return;
		String rmsg = msg.replace(",", "\n");
		Log.d("Util", rmsg);
		new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
				.setNegativeButton("知道了", null).create().show();
	}

	public static final void showProgressDialog(Context context, String title,
			String message) {
		dismissDialog();
		if (TextUtils.isEmpty(title)) {
			title = "请稍候";
		}
		if (TextUtils.isEmpty(message)) {
			message = "正在加载...";
		}
		mProgressDialog = ProgressDialog.show(context, title, message);
	}

	public static final void dismissDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	/**
	 * 打印消息并且用Toast显示消息
	 * 
	 * @param activity
	 * @param message
	 * @param logLevel
	 *            填d, w, e分别代表debug, warn, error; 默认是debug
	 */
	public static final void toastMessage(final Activity activity,
			final String message, String logLevel) {
		if ("w".equals(logLevel)) {
			Log.w("sdkDemo", message);
		} else if ("e".equals(logLevel)) {
			Log.e("sdkDemo", message);
		} else {
			Log.d("sdkDemo", message);
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mToast != null) {
					mToast.cancel();
					mToast = null;
				}
				mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
				mToast.show();
			}
		});
	}

	/**
	 * 打印消息并且用Toast显示消息
	 * 
	 * @param activity
	 * @param message
	 * @param logLevel
	 *            填d, w, e分别代表debug, warn, error; 默认是debug
	 */
	public static final void toastMessage(final Activity activity,
			final String message) {
		toastMessage(activity, message, null);
	}

	/**
	 * 根据一个网络连接(String)获取bitmap图像
	 * 
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmap(String imageUri) {
		// 显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Log.v(TAG, "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			Log.v(TAG, "getbitmap bmp fail---");
			return null;
		}
		return bitmap;
	}

	public static void release() {
		mProgressDialog = null;
		mToast = null;
	}

	/**
	 * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
	 * 
	 * @param bitmap
	 *            原图
	 * @param edgeLength
	 *            希望得到的正方形部分的边长
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
		if (null == bitmap || edgeLength <= 0) {
			return null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if (widthOrg > edgeLength && heightOrg > edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math
					.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				return null;
			}

			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
						edgeLength, edgeLength);
				scaledBitmap.recycle();
			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}

	/**
	 * dp格式的长度转为px格式的长度
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 精确到小数点后一位
	 */
	public static float getFloat(float data) {
		DecimalFormat decimalFormat = new DecimalFormat(".#");
		return Float.parseFloat(decimalFormat.format(data));
	}

	/**
	 * 计算出该TextView中文字的长度(像素)
	 * 
	 * @param textView
	 * @param text
	 * @return
	 */
	public static float getTextViewLength(TextView textView, String text) {
		TextPaint paint = textView.getPaint();
		// 得到使用该paint写上text的时候,像素为多少
		float textLength = paint.measureText(text);
		return textLength;
	}

	/**
	 * 检查网络是否连接可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetWork(Context context) {
		if (getNetType(context) != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @Title: getNetType
	 * @Description: TODO(获取当前网络类型)
	 * @param @param context
	 * @return int 设定文件 -1 网络为没有网络 1 代表 WIFI 其他情况都属于手机网络
	 * @throws
	 */

	public static int getNetType(Context context) {
		int iRst = -1;
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = connectMgr.getActiveNetworkInfo();

		if (info != null) {
			iRst = info.getType();
		}

		return iRst;

	}

	/**
	 * md5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encryption(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * 获取显示时间
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getShowTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));
		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		default:
			result = getTime(timesamp);
			break;
		}
		return result;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getHourAndMin(long timesamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		return sdf.format(new Date(timesamp));
	}

	@SuppressLint("DefaultLocale")
	public static String getTime(long timesamp) {
		String strv = "";
		// 数据时间
		Calendar mCalendar = Calendar.getInstance(Locale.CHINA);
		mCalendar.setTimeInMillis(timesamp);
		int month = mCalendar.get(Calendar.MONTH) + 1;
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = mCalendar.get(Calendar.MINUTE);
		// 当前
		Calendar currentCalendar = Calendar.getInstance(Locale.CHINA);
		currentCalendar.setTimeInMillis(new Date().getTime());
		int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
		int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

		if (currentMonth == month) {
			switch (currentDay - day) {
			case 0:
				strv = "今天  " + (hour < 10 ? ("0" + hour) : hour) + ":"
						+ (minute < 10 ? ("0" + minute) : minute);
				break;
			case 1:
				strv = "昨天 " + (hour < 10 ? ("0" + hour) : hour) + ":"
						+ (minute < 10 ? ("0" + minute) : minute);
				break;
			default:
				strv = (month < 10 ? ("0" + month) : month) + "月"
						+ (day < 10 ? ("0" + day) : day) + "日"
						+ (hour < 10 ? ("0" + hour) : hour) + ":"
						+ (minute < 10 ? ("0" + minute) : minute);
				break;
			}
		} else {
			if (currentDay == 1) {
				strv = "昨天 " + (hour < 10 ? ("0" + hour) : hour) + ":"
						+ (minute < 10 ? ("0" + minute) : minute);
			} else {
				strv = (month < 10 ? ("0" + month) : month) + "月"
						+ (day < 10 ? ("0" + day) : day) + "日"
						+ (hour < 10 ? ("0" + hour) : hour) + ":"
						+ (minute < 10 ? ("0" + minute) : minute);
			}
		}
		return strv;
	}

	/**
	 * 格式化点赞 评论人数 例如（12302-->>12.3k）
	 * 
	 * @param num
	 */
	public static String fomatNum(String num) throws NumberFormatException {
		if (null == num || "".equals(num)) {
			return "0";
		}
		try {
			double temp = Double.parseDouble(num);
			if (temp >= 10000) {
				temp = temp / 10000;
				String str = new DecimalFormat("#.#").format(temp);
				return String.format(str + "万");
			} else {
				return num;
			}
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * 获取url编码
	 * 
	 * @param str
	 *            字符
	 * @return
	 */
	public static String getURLEncodStr(String str) {
		String selectage = "";
		try {
			selectage = URLEncoder.encode(str.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			selectage = "";
		}
		return selectage;
	}

	/**
	 * 过滤特殊字符
	 * 
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String StringFilter(String str) {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`%^&*|{}':;',\\[\\].<>/?~！@#￥%……&;*（）——+|{}‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 检测是否有 emoji表情
	 * 
	 * @param source
	 * @return
	 */
	public static boolean containsEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是 Emoji表情
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是 Emoji
	 * 
	 * @param codePoint
	 *            比较的单个字符
	 * @return
	 */
	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 获取资源文件中的String
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static String getResString(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	/**
	 * 调起系统发短信
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public static void sendMessage(Context context, String phoneNumber,
			String message) {
		if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
					+ phoneNumber));
			intent.putExtra("sms_body", message);
			context.startActivity(intent);
		} else {
			ToastManager.getInstance(context).show("手机号码错误");
		}
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 新出的第三方通信提供商：17* 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][35789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/**
	 * 获取构建的 数字listBean
	 * 
	 * @param indexText
	 * @return
	 */
	public static BuildStringListIndexBean getBuildListBean(int start,
			int size, String indexText) {
		int index = 0;
		BuildStringListIndexBean bean = new BuildStringListIndexBean();
		for (int i = start; i < size; i++) {
			if (indexText.equals(String.valueOf(i))) {
				index = i;
			}
			bean.getContentList().add(String.valueOf(i));
		}
		bean.setIndex(index);
		return bean;
	}

	/**
	 * 取消toast
	 */
	public static void hideToast() {
		if (mToast != null) {
			mToast.cancel();
			mToast = null;
		}
	}

	/**
	 * 默认位置
	 * 
	 * @param context
	 * @param text
	 */
	public static void show(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	/**
	 * 指定显示的位置
	 * 
	 * @param context
	 * @param text
	 *            文本
	 * @param gravity
	 * @param xOffset
	 *            x 偏移量
	 * @param yOffset
	 *            y 偏移量
	 */
	public static void show(Context context, String text, int gravity,
			int xOffset, int yOffset) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		mToast.setGravity(gravity, xOffset, xOffset);
		show(context, text);
	}

	/**
	 * 获得用户设定的灵敏度参数
	 * 
	 * @param allsensitive
	 * @param index
	 * @return
	 */
	public static String getSensitive(String allsensitive, int index) {
		if (index == 0) {
			return "6,2,2";
		}
		try {
			String getsensitiveString = "";
			String sensitive[] = null;
			if (null != allsensitive) {
				sensitive = allsensitive.replace("[", "").replace("]", "")
						.replace("\",\"", "\";\"").replace("\"", "").split(";");
			}
			if (sensitive.length < (index - 1)) {
				return "6,2,2";
			}
			getsensitiveString = sensitive[index - 1];
			if (getsensitiveString.length() != 5) {
				return "6,2,2";
			} else
				return getsensitiveString;

		} catch (Exception e) {
			e.printStackTrace();
			return "6,2,2";
		}
	}

	public static final boolean isQQInstalled(Context context) {
		try {
			context.getPackageManager().getApplicationInfo(
					"com.tencent.mobileqq",
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 防止短时间内重复点击两次
	 */
	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 800) {
			lastClickTime = time;
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 获取分钟数
	 * @param date 形式 xx小时xx分钟 ; xx小时 ; xx分钟
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public static int getMinutes(String date) {
		long longTime = -1;
		SimpleDateFormat df = new SimpleDateFormat("H小时m分钟");
		SimpleDateFormat dfH = new SimpleDateFormat("H小时");
		SimpleDateFormat dfM = new SimpleDateFormat("m分钟");
		try {
			longTime = df.parse(date).getTime();
		} catch (Exception e) { }
		if(longTime == -1){
			try {
				longTime = dfH.parse(date).getTime();
			} catch (Exception e2) {
			}
		}
		if(longTime == -1){
			try {
				longTime = dfM.parse(date).getTime();
			} catch (Exception e3) {
			}
		}
		if(longTime != -1){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("H:m");
				String[] s = sdf.format(longTime).split(":");
				return Integer.parseInt(s[0])* 60 + Integer.parseInt(s[1]);
			} catch (Exception e) {
				return 0;
			}
		}else
			return 0;
	}
	
	/**
	 *   
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public static long getDateLong(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			return sdf.parse(date).getTime();
		} catch (Exception e) {
			LogUtil.i("tianxun", e.getMessage());
			return 0;
		}
	}
	
	public static SignInResult getSleepInfoFromJson(String obj){
		SignInResult result = new Gson().fromJson(obj, new TypeToken<SignInResult>() {}.getType());		
		return result;
	}
	
	
	public static ModifySignInResult getModifySleepInfoFromJson(String obj){
		ModifySignInResult result = new Gson().fromJson(obj, new TypeToken<ModifySignInResult>() {}.getType());		
		return result;
	}
	
	
	public static HomeDataBean getHomeDataBeanFromJson(String obj){
		HomeDataBean result = new Gson().fromJson(obj, new TypeToken<HomeDataBean>() {}.getType());		
		return result;
	}
	
	public static ComfortResult getComfortInfoFromJson(String obj){
		ComfortResult result = new Gson().fromJson(obj, new TypeToken<ComfortResult>() {}.getType());		
		return result;
	}
	
	/**
	 * 从传入字符串中获取"yyyy-MM-dd"类型的时间戳
	 * @param Str
	 * @return 有时间戳返回时间戳，没有的返回空字符串
	 */
	public static String getTimeFromString(String Str){
		String macherStr = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
		Pattern pattern = Pattern.compile(macherStr);
		Matcher matcher = pattern.matcher(Str);
		if(matcher.find()){
			return matcher.group();
		}else{
			return "";
		}
	}


}
