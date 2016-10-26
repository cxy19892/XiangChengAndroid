package com.yzm.sleep.background;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yzm.sleep.bean.MsgListBean;
import com.yzm.sleep.bean.SecretaryMsgBean;
import com.yzm.sleep.utils.PreManager;

public class SecretaryDBOperate {

	/**
	 *  往数据库里插入一条小秘书的消息
	 * @param context
	 * @param bean
	 */
	public static void insertDB(Context context, SecretaryMsgBean bean){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put("id", bean.getMsgId());
			cv.put("type", bean.getType());
			cv.put("title", bean.getTitle());
			cv.put("content", bean.getMsg());
			cv.put("date", bean.getDate());
			cv.put("link", bean.getLink());
			cv.put("scan", bean.getScan());
			db.insert(MyTabList.SECRETARY_MSG_LIST, null, cv);
		} catch (Exception e) {
		}finally{
			db.close();
		}
	}
	
	/**
	 *  查询推送消息列表
	 * @param context
	 * @param bean
	 */
	public static List<SecretaryMsgBean> queryMsg(Context context){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.query(MyTabList.SECRETARY_MSG_LIST, null, "link=? or link=?", new String[]{PreManager.instance().getUserId(context),""}, null, null, "date desc");
		List<SecretaryMsgBean> list = new ArrayList<SecretaryMsgBean>();
		try {
			SecretaryMsgBean bean;
			while (c.moveToNext()) {
				bean = new SecretaryMsgBean();
				bean.setMsgId(c.getString(c.getColumnIndex("id")));
				bean.setDate(c.getString(c.getColumnIndex("date")));
				bean.setMsg(c.getString(c.getColumnIndex("content")));
				bean.setTitle(c.getString(c.getColumnIndex("title")));
				bean.setType(c.getInt(c.getColumnIndex("type")));
				bean.setScan(c.getInt(c.getColumnIndex("scan")));
				bean.setLink(c.getString(c.getColumnIndex("link")));
				list.add(bean);
			}
		} catch (Exception e) {
		}finally{
			c.close();
			db.close();
		}
		return list;
	}
	
	/**
	 * 查询最后一条消息
	 * @param context
	 * @return  如果没有当前消息。返回null
	 */
	public static MsgListBean queryLastMsg(Context context){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c1 = db.query(MyTabList.SECRETARY_MSG_LIST, null, "scan = ? and (link = ? or link = ?) ", new String[]{"0",PreManager.instance().getUserId(context),""}, null, null, null);
		try {
			MsgListBean bean = new MsgListBean();
			bean.setMsgListTyp("1");
			if(c1.getCount() > 0){
				c1.moveToLast();
				bean.setDate(Long.parseLong(c1.getString(c1.getColumnIndex("date"))));
				bean.setMessage(c1.getString(c1.getColumnIndex("content")));
				bean.setMsgCount(c1.getCount());
				bean.setType("0");
				bean.setUserName("香橙小秘书");
			}else{
				Cursor c2 = db.query(MyTabList.SECRETARY_MSG_LIST, null, null, null, null, null, null);
				c2.moveToLast();
				bean.setDate(Long.parseLong(c2.getString(c2.getColumnIndex("date"))));
				bean.setMessage(c2.getString(c2.getColumnIndex("content")));
				bean.setMsgCount(0);
				bean.setType("0");
				bean.setUserName("香橙小秘书");
			}
			return bean;
		} catch (Exception e) {
		}finally{
			db.close();
		}
		return null;
	}
	
	/**
	 *  获取列表之后，将所有的消息置为已读状态
	 * @param context
	 */
	public static void scanAllMsg(Context context){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("scan", 1);
			db.update(MyTabList.SECRETARY_MSG_LIST, values, null, null);
		} catch (Exception e) {
		}finally{
			db.close();
		}
	}
}
