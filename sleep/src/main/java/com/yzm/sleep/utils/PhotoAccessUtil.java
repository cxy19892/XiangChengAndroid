package com.yzm.sleep.utils;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;

import com.yzm.sleep.Constant;
import com.yzm.sleep.crop.CropImageUI;

public class PhotoAccessUtil {
	public static File tackphoto(Activity activity) {
		String Init = PreManager.instance().getUserId(activity);
		// 创建File对象用于存储拍照的图片 SD卡根目录
		// File outputImage = new
		// File(Environment.getExternalStorageDirectory(),test.jpg);
		// 存储至DCIM文件夹
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File outputImage = new File(path, Init + ".jpg");
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String gFilename = outputImage.getPath(); // 图片名称
		// 将File对象转换为Uri并启动照相程序
		Uri gImageUri = Uri.fromFile(outputImage);// 图片路径

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); // 照相
		PreManager.instance().savePhotoPathString(activity, gFilename);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, gImageUri); // 指定图片输出地址
		activity.startActivityForResult(intent, Constant.TAKE_PHOTO); // 启动照相
		return outputImage;
	}

	/**
	 * 从相册选择
	 */
	public static void getImgByStorage(Activity activity) {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");

		// 回收从系统文件中选择的图片
		activity.startActivityForResult(intent, Constant.RESULT_LOAD_IMAGE);
	}
	
	public interface InterfacePhotoZoomCallback{
		public void onSuccess(String succMsg,String path);
		public void onError(String errMsg);
	}
	
	public static void startPhotoZoom(Activity activity,Uri uri,int readPictureDegree) {
		Intent mIntent = new Intent(activity,
				CropImageUI.class);
		mIntent.putExtra("readPictureDegree", readPictureDegree);
		mIntent.putExtra("index", 1);
		mIntent.putExtra("uri", SelectPhotoUtil.newGetPath(activity, uri));
		activity.startActivityForResult(mIntent, Constant.CROP_PHOTO);

	}
	
	/**
	 * 读取照片exif信息中的旋转角度
	 * 
	 * @param path
	 *            照片路径
	 * @return角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	public static int readPictureDegree(Context context,Uri uri) {
		String realFilePath = PhotoAccessUtil.getRealFilePath(context, uri);
		if (!TextUtils.isEmpty(realFilePath)) {
			return readPictureDegree(realFilePath);
		}
		return 0;
	}
	
	public static String getRealFilePath( final Context context, final Uri uri ) {
	    if ( null == uri ) return null;
	    final String scheme = uri.getScheme();
	    String data = null;
	    try {
			if ( scheme == null )
			    data = uri.getPath();
			else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			    data = uri.getPath();
			} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			    Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
			    if ( null != cursor ) {
			        if ( cursor.moveToFirst() ) {
			            int index = cursor.getColumnIndex( ImageColumns.DATA );
			            if ( index > -1 ) {
			                data = cursor.getString( index );
			            }
			        }
			        cursor.close();
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return data;
	}
}
