package com.yzm.sleep.tools;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SleepInfo;

public class InsertTodayUtils {

	private static InsertTodayUtils instance;
	
	private InsertTodayUtils() {
		
	}
	
	public static synchronized InsertTodayUtils getInstance() {
		if(instance == null) {
			instance = new InsertTodayUtils();
		}
		return instance;
	}
	
	public void insert(Context context,String date) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context.getApplicationContext());
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),MyTabList.SLEEPTIME);
		String str = operate.query(MyTabList.TableDay.DATE.getCloumn(), MyTabList.TableDay.DATE.getCloumn()+" = ?",new String[]{date} );
		if(TextUtils.isEmpty(str)) {
			ContentValues cv = new ContentValues(); 
			cv.put(MyTabList.TableDay.DATE.getCloumn(), date);
			cv.put(MyTabList.TableDay.STARTTIME.getCloumn(), SleepInfo.SET_STARTTIME);
			cv.put(MyTabList.TableDay.ENDTIME.getCloumn(), SleepInfo.SET_ENDTIME);
			cv.put(MyTabList.TableDay.RECORD_STATE.getCloumn(), "1");
			operate.insert(cv);
		}
		operate.close();
	}
}
