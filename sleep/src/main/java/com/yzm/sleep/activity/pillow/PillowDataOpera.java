package com.yzm.sleep.activity.pillow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;









import java.util.ArrayList;
import java.util.List;

import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.model.PillowDataModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

public class PillowDataOpera {

	/** 文件存放文件夹*/
	private final static String filePath = "/data/pillow";
	
	
	public static String getPillowDataPath(String fileName){
		try {
			File sdDir = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			}else
				return "";
			File f = new File(sdDir.toString() + filePath);
			if(!f.exists())
				f.mkdirs();
			File file = new File(sdDir.toString() + filePath, fileName);
			return file.getAbsolutePath();
		} catch (Exception e) {
		}
		return "";
	}
	
	public static byte[] readPillowProfile(){
		try {
			File sdDir = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			} else
				return null;
			File f = new File(sdDir.toString() + "/data/profile.pa");
			FileInputStream fis = new FileInputStream(f);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fis.close();
			return b;
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 向文件夹中写入智能枕头的数据
	 * @param data
	 * @param fileName
	 * @return
	 */
	public static String saveDataToSDcard(byte[] data, String fileName){
		try {
			File sdDir = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			}else
				return "";
			File f = new File(sdDir.toString() + filePath);
			if(!f.exists())
				f.mkdirs();
			File file = new File(sdDir.toString() + filePath, fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
			return file.getAbsolutePath();
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * 根据文件名从文件夹中读取文件流
	 * @param fileName
	 * @return
	 */
	public static byte[] readDataToSDcard(String fileName){
		try {
			File sdDir = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			} else
				return null;
			File f = new File(sdDir.toString() + filePath);
			if(!f.exists())
				f.mkdirs();
			File file = new File(sdDir.toString() + filePath, fileName);
			FileInputStream fis = new FileInputStream(file);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fis.close();
			return b;
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 *  判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public static boolean fileExist(String fileName){
		try {
			File sdDir = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			} else
				return false;
			File f = new File(sdDir.toString() + filePath);
			if(!f.exists())
				f.mkdirs();
			File file = new File(sdDir.toString() + filePath);
			if(file.isDirectory()){
				String[] names = file.list();
				boolean isExist = false;
				for (String string : names) {
					if(string.equals(fileName))
						isExist = true;
				}
				return isExist;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 *   往数据库中插入枕头的睡眠数据
	 * @param context
	 * @param date
	 * @param isUpload
	 * @param fileName
	 * @param bfr
	 */
	public static void insertDataToSQL(Context context, PillowDataModel model){
		try {
			MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if(!TextUtils.isEmpty(model.getDate()))
				cv.put("date", model.getDate());
			if(!TextUtils.isEmpty(model.getIsUpload()))
				cv.put("isUpload", model.getIsUpload());
			if(!TextUtils.isEmpty(model.getFileName()))
				cv.put("fileName", model.getFileName());
			if(model.getBfr() != null)
				cv.put("model", model.getBfr());
			if(!TextUtils.isEmpty(model.getDatFileName()))
				cv.put("datFileName", model.getDatFileName());
			if(!TextUtils.isEmpty(model.getDatIsUpload()))
				cv.put("datIsUpload", model.getDatIsUpload());
			db.insert(MyTabList.PILLOW_SLEEP_DATA, null, cv);
		} catch (Exception e) {
		}
	}
	
	/**
	 *  修改数据库的枕头睡眠数据
	 * @param context
	 * @param date
	 * @param isUpload
	 * @param fileName
	 * @param bfr
	 */
	public static void updateDataToSQL(Context context, PillowDataModel model){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues cv = new ContentValues();
			if(!TextUtils.isEmpty(model.getDate()))
				cv.put("date", model.getDate());
			if(!TextUtils.isEmpty(model.getIsUpload()))
				cv.put("isUpload", model.getIsUpload());
			if(!TextUtils.isEmpty(model.getFileName()))
				cv.put("fileName", model.getFileName());
			if(model.getBfr() != null)
				cv.put("model", model.getBfr());
			if(!TextUtils.isEmpty(model.getDatFileName()))
				cv.put("datFileName", model.getDatFileName());
			if(!TextUtils.isEmpty(model.getDatIsUpload()))
				cv.put("datIsUpload", model.getDatIsUpload());
			db.update(MyTabList.PILLOW_SLEEP_DATA, cv, "date = ?", new String[]{model.getDate()});
		} catch (Exception e) {
		}finally{
			db.close();
		}
	}
	
	
	/**
	 * 判断是否存在数据库
	 * @param context
	 * @param date
	 * @return
	 */
	public static boolean queryDataExistFromSQL(Context context, String date){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			Cursor c = db.query(MyTabList.PILLOW_SLEEP_DATA, null, "date=?", new String[]{date}, null, null, null);
			if(c.getCount() > 0){
				c.close();
				return true;
			}
			c.close();
		} catch (Exception e) {
		}finally{
			db.close();
		}
		return false;
	}
	
	/**
	 * 数据库读取指定日期的数据
	 * @param context
	 * @param date
	 * @return
	 */
	public static PillowDataModel queryDataFromSQL(Context context, String date){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.query(MyTabList.PILLOW_SLEEP_DATA, null, "date=?", new String[]{date}, null, null, null);
		PillowDataModel modelData = new PillowDataModel();
		try {
			if(c.getCount() > 0){
				c.moveToFirst();
				modelData.setDate(c.getString(c.getColumnIndex("date")));
				modelData.setIsUpload(c.getString(c.getColumnIndex("isUpload")));
				modelData.setFileName(c.getString(c.getColumnIndex("fileName")));
				
				modelData.setBfr(c.getBlob(c.getColumnIndex("model")));
				modelData.setDatFileName(c.getString(c.getColumnIndex("datFileName")));
				modelData.setDatIsUpload(c.getString(c.getColumnIndex("datIsUpload")));
				c.close();
			}
		} catch (Exception e) {
		}finally{
			c.close();
			db.close();
		}
		return modelData;
		
	}
	
	/**
	 * 数据库读取所有日期的数据
	 * @param context
	 * @param date
	 * @return
	 */
	public static List<PillowDataModel> queryAllDataFromSQL(Context context){
		List<PillowDataModel> list = new ArrayList<PillowDataModel>();
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			db = helper.getWritableDatabase();
			c = db.query(MyTabList.PILLOW_SLEEP_DATA, null, null, null, null, null, null);
			while (c.moveToNext()) {
				PillowDataModel modelData = new PillowDataModel();
				modelData.setDate(c.getString(c.getColumnIndex("date")));
				modelData.setIsUpload(c.getString(c.getColumnIndex("isUpload")));
				modelData.setFileName(c.getString(c.getColumnIndex("fileName")));
				modelData.setBfr(c.getBlob(c.getColumnIndex("model")));
				modelData.setDatFileName(c.getString(c.getColumnIndex("datFileName")));
				modelData.setDatIsUpload(c.getString(c.getColumnIndex("datIsUpload")));
				list.add(modelData);
			}
		} catch (Exception e) {
		} finally {
			if(c!=null)
				c.close();
			if(db!=null)
				db.close();
		}
		return list;
	}
}
