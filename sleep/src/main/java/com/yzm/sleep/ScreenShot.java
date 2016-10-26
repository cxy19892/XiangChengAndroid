package com.yzm.sleep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yzm.sleep.utils.LogUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Message;
import android.view.View;

public class ScreenShot {
	static Activity mActivity;
	private static Bitmap b;
	private boolean isFiletrue = true;

	private static Bitmap takeScreenShot(Activity activity, int x, int y,
			int width, int height) {
		mActivity = activity;
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕长和高
		// int width =
		// activity.getWindowManager().getDefaultDisplay().getWidth();
		// int height = activity.getWindowManager().getDefaultDisplay()
		// .getHeight();
		// b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
		// - statusBarHeight);
		b = Bitmap.createBitmap(b1, x, y + statusBarHeight, width, height);
		view.destroyDrawingCache();

		return b;
	}

	private static void savePic(Bitmap b, File filePath) {
		FileOutputStream fos = null;
		try {
			filePath.createNewFile();
			fos = new FileOutputStream(filePath);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 10, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public static Bitmap shoot(Activity a, int x, int y, int width, int height,
			File filePath) {
		if (filePath == null) {
			return null;
		}
		if (!filePath.getParentFile().exists()) {
			filePath.getParentFile().mkdirs();
		}
		ScreenShot.savePic(ScreenShot.takeScreenShot(a, x, y, width, height),
				filePath);
		return b;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	private String getFilePath() {
		try {

			File sdDir = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			} else
				return null;
			File f = new File(sdDir.toString() + "/data/profile.pa");
			return f.toString();
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 下载文件
	 */
	public boolean doDownloadFile(String URL) {
		String filePath = getFilePath();
		LogUtil.i("masong", "SD卡文件保存地址==" + getFilePath());
//		HttpUtils http = new HttpUtils();
		File file = new File(filePath);
		if (null == URL || URL.equals("")) {
			return false;
		}
		if (null == filePath || filePath.equals("")) {
			return false;
		}
		if (file.exists()) {
			return true;
		}
//		http.download(URL, filePath, false, false, new RequestCallBack<File>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				LogUtil.i("masong", "下载失败");
//				isFiletrue = false;
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<File> arg0) {
//				LogUtil.i("masong", "下载成功");
//				isFiletrue = true;
//			}
//		});
		
//		File file = new File(target);
		new AsyncHttpClient().get(URL, new FileAsyncHttpResponseHandler(file) {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, File arg2) {
				LogUtil.i("masong", "下载成功");
				isFiletrue = true;
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				LogUtil.i("masong", "下载失败");
				isFiletrue = false;
			}

			@Override
			public void onProgress(int current, int total) {
				// TODO Auto-generated method stub
				super.onProgress(current, total);
			}
		});
		LogUtil.i("masong", isFiletrue+"");
		return isFiletrue;
	}
}
