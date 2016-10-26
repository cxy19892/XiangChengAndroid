package com.yzm.sleep.background;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

import com.yzm.sleep.model.DaySleepMsg;
import com.yzm.sleep.model.SleepDistributionInfo;
import com.yzm.sleep.model.SleepStatistics;
import com.yzm.sleep.model.WeekSleepMsg;
import com.yzm.sleep.utils.SleepUtils;
/**
 *
 *
 */
public class MytabOperate {
	private SQLiteDatabase db = null;
	private String tableName = null;
	
	public MytabOperate(SQLiteDatabase db,String tableName){
		this.db = db;
		this.tableName = tableName;
	}
	
	/**
	 * 
	 * @param cv 
	 */
	public void insert(ContentValues cv) {
		this.db.insert(this.tableName, null, cv);
	}
	
	public void update(ContentValues cv,String whereClause,String whereArgs[]) {
		this.db.update(this.tableName, cv, whereClause, whereArgs);
	}
	
	public void delete(String whereClause,String whereArgs[]) {
		this.db.delete(this.tableName, whereClause, whereArgs);
	}
	
	/**
	 * 
	public void clear(){
		this.db.delete(this.tableName, null, null);
	}
	
	/**
	 * 
	 */
	public void close(){
//		this.db.close();
	}
	
	/**
	 * 
	 */
	public int queryCount(String[] qury){
		try {
			Cursor result = this.db.query(this.tableName, qury, null, null, null, null, null);
			int count = result.getCount();
			result.close();
			return count;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	
	public boolean queryIsExist(String date){
		try {
			Cursor result = this.db.rawQuery("select * from " + this.tableName + " where date = '" + date + "'", null);
			int count = result.getCount();
			if(count > 0)
				return true;
			result.close();
		} catch (Exception e) {
		}
		return false;
	}
	
	public int queryCount(String[] columns,String selection,String[] selectionArgs) {
		Cursor result = this.db.query(this.tableName, columns, selection, selectionArgs, null, null, null);
		int count = result.getCount();
		result.close();
		return count;
	}
	
	/**
	 * 	 */
	public String queryMaxValue(String columnName){
		String maxValue = "";
		Cursor result = this.db.query(this.tableName, new String[]{columnName}, null, null, null, null, columnName);
		if(result.moveToLast()){
			maxValue = result.getString(result.getColumnIndex(columnName));
		}
		result.close();
		return maxValue;
	}
	
	/**
	 * 获取插空数据
	 * @return
	 */
	public SleepData queryInsertData() {
		SleepData data = new SleepData();
		if(MyTabList.SLEEPDATA.equals(this.tableName)) {
			Cursor cursor = this.db.query(this.tableName, new String[]{
					MyTabList.TableSleepData.TIME.getCloumn(),
					MyTabList.TableSleepData.X.getCloumn(),
					MyTabList.TableSleepData.Y.getCloumn(),
					MyTabList.TableSleepData.Z.getCloumn()
			}, null, null, null, null, MyTabList.TableSleepData.TIME.getCloumn());
			if(cursor.getCount() > 0) {
				cursor.moveToLast();
				data.setTime(cursor.getString(cursor.getColumnIndex(MyTabList.TableSleepData.TIME.getCloumn())));
				data.setX(cursor.getString(cursor.getColumnIndex(MyTabList.TableSleepData.X.getCloumn())));
				data.setY(cursor.getString(cursor.getColumnIndex(MyTabList.TableSleepData.Y.getCloumn())));
				data.setZ(cursor.getString(cursor.getColumnIndex(MyTabList.TableSleepData.Z.getCloumn())));	
			}else {
				data.setX("0");
				data.setY("0");
				data.setZ("0");
			}
			cursor.close();
		}
		return data;
	}
	
	/**
	 * 查询columns里的结果，这个方法查询的是columns[index]列的数据,切记index不能超过columns的length - 1;另外注意为""的情况
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public String query(String[] columns,String selection,String[] selectionArgs,int index) {
		String result = "";
		Cursor cursor = this.db.query(this.tableName, columns, selection, selectionArgs, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			result = cursor.getString(index);
			if(!TextUtils.isEmpty(result)) {
				cursor.close();
				return result;
			}
			cursor.moveToNext();
		}
		cursor.close();
		return result;
	}
	
	/**
	 * @param column
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public String query(String column,String selection,String[] selectionArgs){
		Cursor result= this.db.query(this.tableName, new String[]{column}, selection, selectionArgs, null, null, null);
		
		if(result == null)
			return "";
	
		result.moveToFirst();
		int index = result.getCount();
		if(index == 0) {
			result.close();
			return "";
		}else {
			String str = result.getString(result.getColumnIndex(column));
			result.close();
			return str;
		}
	}
	
	/**
	 * 
	 * @param column
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public long querylong(String column,String selection,String[] selectionArgs){
		Cursor result= this.db.query(this.tableName, new String[]{column}, selection, selectionArgs, null, null, null);
		
		if(result == null)
			return 0;
	
		result.moveToFirst();
		int index = result.getCount();
		if(index == 0) {
			result.close();
			return 0;
		}else {
			long lon = result.getLong(result.getColumnIndex(column));
			result.close();
			return lon;
		}
	}
	
	/**
	 * 
	 * @throws Exception 
	 */
	public List<?> query(String[] columns) throws Exception{
		return query(columns,null,null,null);
	}
	
	/**
	 * 获取展示日期
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public ArrayList<DaySleepMsg> queryDisplayDateList(String date,Context context) throws ParseException {
		if(!MyTabList.SLEEPTIME.equals(this.tableName)) {
			return null;
		}
		String record_state = DataUtils.getRecordState(context, date);
		String queryDate = "";
		if("4".equals(record_state)) {
			queryDate = date;
		}else {
			queryDate = DataUtils.getBeforeDate(date);
		}
		ArrayList<DaySleepMsg> list = new ArrayList<DaySleepMsg>();
		Cursor result = this.db.query(this.tableName, new String[]{MyTabList.TableDay.DATE.getCloumn()}, 
																	MyTabList.TableDay.DATE.getCloumn()+" <= ?", 
																	new String[]{queryDate}, null, null, 
																	MyTabList.TableDay.DATE.getCloumn());
		result.moveToFirst();
		while(!result.isAfterLast()) {
			DaySleepMsg str = new DaySleepMsg();
			str.setDate(result.getString(result.getColumnIndex(MyTabList.TableDay.DATE.getCloumn())));
			boolean isExits = false;
			for(DaySleepMsg ds : list){
				if(ds.getDate().equals(str.getDate())) {
					isExits = true;
					break;
				}
			}
			if(!isExits) {
				list.add(str);
			}
			result.moveToNext();
		}
		result.close();
		return list; 
	}
	
	/**
	 * 获取周视图展示列表
	 * @return
	 * @throws ParseException 
	 */
	public ArrayList<WeekSleepMsg> queryDisplayWeekList() throws ParseException {
		if(!MyTabList.SLEEPTIME.equals(this.tableName)) {
			return null;
		}
		Cursor result = this.db.query(true, MyTabList.SLEEPTIME, new String[]{"date","sleeptime","uptime","starttime","endtime"}, "date <= ?", new String[]{DataUtils.getRecordDate(new Date())}, null, null, null, null);
		ArrayList<WeekSleepMsg> list = new ArrayList<WeekSleepMsg>();
		ArrayList<SleepStatistics> ssList = new ArrayList<SleepStatistics>();
		result.moveToFirst();
		while(!result.isAfterLast()) {						//先取出所有日数据
			SleepStatistics ss = new SleepStatistics();
			String date = result.getString(result.getColumnIndex("date"));
			String sleeptime = result.getString(result.getColumnIndex("sleeptime"));
			String uptime = result.getString(result.getColumnIndex("uptime"));
			String starttime = result.getString(result.getColumnIndex("starttime"));
			String endtime = result.getString(result.getColumnIndex("endtime"));
			if(date == null || "".equals(date) || sleeptime == null || "".equals(sleeptime) || uptime == null|| "".equals(uptime) || starttime == null|| "".equals(starttime) || endtime == null|| "".equals(endtime)){
				result.moveToNext();
				continue;
			}
			String setStartTime = DataUtils.dealData((int)(Long.valueOf(starttime)/60))+":"+DataUtils.dealData((int)(Long.valueOf(starttime)%60));
			String setEndTime = DataUtils.dealData((int)(Long.valueOf(endtime)/60))+":"+DataUtils.dealData((int)(Long.valueOf(endtime)%60));
			ss.setSleepTimeSetting(setStartTime);
			ss.setGetupTimeSetting(setEndTime);
			ss.setDate((date == null || "".equals(date)) ? "":date);
			ss.setSleep_time(sleeptime);
			ss.setGetup_time(uptime);
			if(!SleepUtils.isEmpty(ss.getSleep_time()) && !SleepUtils.isEmpty(ss.getGetup_time())) {
				String sleep[] = ss.getSleep_time().split(":");
				String up[] = ss.getGetup_time().split(":");
				long sleepHour = Integer.valueOf(sleep[0])*60;
				long sleepMinute = Integer.valueOf(sleep[1]);
				long upHour = Integer.valueOf(up[0])*60;
				long upMinute = Integer.valueOf(up[1]);
				long sleepMinutes = sleepHour + sleepMinute;
				long upMinutes = upHour + upMinute;
				if(sleepMinutes <= upMinutes) {
					ss.setSleep_hour((upMinutes - sleepMinutes)/60.0f);	
				}else {
					ss.setSleep_hour((upMinutes + 24*60 - sleepMinutes)/60.0f);
				}
			}
			ss.setEmpty(false);
			boolean isExits = false;
			for(SleepStatistics sss : ssList) {
				if(ss.getDate().equals(sss.getDate())) {
					isExits = true;
					break;
				}
			}
			if(!isExits) {
				ssList.add(ss);
			}
			result.moveToNext();
		}
		
		result.close();
		
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(SleepStatistics ss : ssList) {								//将日数据分组，分成周数据
			cal.setTime(sdf.parse(ss.getDate()));
			int weakNumber = (int) ((sdf.parse(ss.getDate()).getTime() - (sdf.parse("1900-01-01").getTime())) / (7*24*60*60*1000));
			if(1 == cal.get(Calendar.DAY_OF_WEEK)) {
				cal.add(Calendar.WEEK_OF_YEAR, -1);
//				weakNumber -= 1;
			}
			boolean isWeakExits = false;
			int count = 0;
			for(int i=0;i<list.size();i++) {
				WeekSleepMsg wsm = list.get(i);
				if(wsm.getWeakNumber() == weakNumber) {
					isWeakExits = true;
					count = i;
					break;
				}
			}
			if(isWeakExits) {
				WeekSleepMsg msg = list.get(count);
				msg.getSleepStatistics().add(ss);
			}else {
				WeekSleepMsg msg = new WeekSleepMsg();
				msg.setWeakNumber(weakNumber);
				ArrayList<SleepStatistics> itemList = new ArrayList<SleepStatistics>();
				itemList.add(ss);
				msg.setSleepStatistics(itemList);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				String monday = DataUtils.dateFomate(cal.getTime());
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				cal.add(Calendar.WEEK_OF_YEAR, 1);
				String sunday = DataUtils.dateFomate(cal.getTime());
				msg.setWeakTitle(monday+" 到 "+sunday);
				list.add(msg);
			}
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for(WeekSleepMsg msg : list) {				//分析周数据中不足七天的数据，并填上空
			ArrayList<SleepStatistics> dayList = msg.getSleepStatistics();
			String date = dayList.get(0).getDate();
			Calendar dayCalendar = Calendar.getInstance();
			dayCalendar.setTime(sdf.parse(date));
			if(1 == dayCalendar.get(Calendar.DAY_OF_WEEK)) {
				dayCalendar.add(Calendar.WEEK_OF_YEAR, -1);
			}
			List<String> dateList = new ArrayList<String>();
			dayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			dateList.add(df.format(dayCalendar.getTime()));
			dayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			dateList.add(df.format(dayCalendar.getTime()));
			dayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
			dateList.add(df.format(dayCalendar.getTime()));
			dayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
			dateList.add(df.format(dayCalendar.getTime()));
			dayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			dateList.add(df.format(dayCalendar.getTime()));
			dayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			dateList.add(df.format(dayCalendar.getTime()));
			dayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			dayCalendar.add(Calendar.WEEK_OF_YEAR, 1);
			dateList.add(df.format(dayCalendar.getTime()));
			
			for(String day : dateList) {
				boolean dayExits = false;
				for(SleepStatistics daydata : dayList) {
					if(day.equals(daydata.getDate())) {
						dayExits = true;
						break;
					}
				}
				if(!dayExits) {
					SleepStatistics sl = new SleepStatistics();
					sl.setDate(day);
					sl.setEmpty(true);
					sl.setSleep_hour(0);
					dayList.add(sl);
				}
			}
		}
		
		Collections.sort(list);					//根据周的number排序
		for(WeekSleepMsg msg : list) {			//根据日期对日排序
			Collections.sort(msg.getSleepStatistics());
			int count = 0;
			boolean isSame = true;
			float summarySleeptime = 0 ;
			float summaryUptime = 0;
			float summarySleepLength = 0;
			float sleepHourMax = 0;
			String setSleepTime = "";
			String setUptime = "";
			for(SleepStatistics s :msg.getSleepStatistics()) {
				if(!s.isEmpty()) {
					if(s.getSleep_hour() > sleepHourMax) {
						sleepHourMax = s.getSleep_hour();
					}
				}	
					
				if(!SleepUtils.isEmpty(s.getSleepTimeSetting()) && !SleepUtils.isEmpty(s.getGetupTimeSetting())) {
					setSleepTime = s.getSleepTimeSetting();
					setUptime = s.getGetupTimeSetting();
				}
			}
			for(SleepStatistics se : msg.getSleepStatistics()){
				if(!se.isEmpty()) {
					count ++;
					String sleeptime[] = se.getSleep_time().split(":");
					String uptime[] = se.getGetup_time().split(":");
					long seSleeptime = Long.valueOf(sleeptime[0]) * 60 + Long.valueOf(sleeptime[1]);
					long seUptime = Long.valueOf(uptime[0]) * 60 + Long.valueOf(uptime[1]);
					long tempSleeptime = seSleeptime;
					long tempUptime = seUptime;
					if(seSleeptime < 12*60) {
						tempSleeptime = tempSleeptime + 24*60;
					}
					if(seUptime < 12*60) {
						tempUptime = tempUptime + 24*60;
					}
					summarySleeptime = summarySleeptime + tempSleeptime;
					summaryUptime = summaryUptime + tempUptime;
					summarySleepLength = summarySleepLength + se.getSleep_hour();
					
					String setSleep[] = se.getSleepTimeSetting().split(":");
					String setUp[] = se.getGetupTimeSetting().split(":");
					long sleep = Long.valueOf(setSleep[0]) * 60 + Long.valueOf(setSleep[1]) - SleepInfo.BEFORE_SLEEP;
					long up = Long.valueOf(setUp[0]) * 60 + Long.valueOf(setUp[1]) + SleepInfo.AFTER_SLEEP;
					if(sleep < 0) {
						sleep = 24*60 + sleep;
					}
					if(up >= (24*60)) {
						up = up - 24*60;
					}
					if(DataUtils.isOutOfTime(sleep, up, seSleeptime, seUptime)) {
						isSame = false;
					}
					if((isSame && !setSleepTime.equals(se.getSleepTimeSetting())) || (isSame && !setUptime.equals(se.getGetupTimeSetting()))) {
						isSame = false;
					}
				}
			}
			msg.setSleepHourMax(sleepHourMax);
			msg.setSame(isSame);
			msg.setAverageSleeplength((summarySleepLength / count)+"");
			int sleepHour = (int)((summarySleeptime / count)/60);
			if(sleepHour >= 24) {
				sleepHour = sleepHour - 24;
			}
			int upHour = (int)((summaryUptime / count)/60);
			if(upHour >= 24) {
				upHour = upHour - 24;
			}
			msg.setAverageSleeptime(DataUtils.dealData(sleepHour)+":"+DataUtils.dealData((int)((summarySleeptime / count)%60)));
			msg.setAverageUptime(DataUtils.dealData(upHour)+":"+DataUtils.dealData((int)((summaryUptime / count)%60)));
		}
		
		return list;
	}
	
	private boolean haveColumn(String[] columnArray,String columnName) {
		for(int i=0;i<columnArray.length;i++) {
			if(columnName.equals(columnArray[i])) {
				return true;
			}
		}
		return false;
	}
	
	public String querDocid(String date,String sleeptime,String uptime,String sleeplength) {
		String result = "";
		if(!this.tableName.equals(MyTabList.SLEEPTIME)) {
			return result;
		}
		String[] columns = new String[]{"date","sleeptime","uptime","sleeplength","fileid"};
		Cursor cursor = this.db.query(this.tableName, columns, columns[0]+" = ? and "+columns[1]+" = ? and "+columns[2]+" = ? and "+columns[3]+" = ?",
				new String[]{date,sleeptime,uptime,sleeplength}, null, null, null);
		cursor.moveToFirst();
		if(!(cursor.getCount() <= 0)) {
			result = cursor.getString(cursor.getColumnIndex(columns[4]));
		}
		cursor.close();
		return result;
	}
	
	/**
	 * 获取入睡周文案
	 * @param weeknumber
	 * @param sleeptime
	 * @return
	 */
	public String querySleepid(String weeknumber,String sleeptime) {
		String result = "";
		if(!this.tableName.equals(MyTabList.WEEK)) {
			return result;
		}
		//注意数组组成，前两个是作为sql查询条件，最后为本方法要获取的列表值
		String[] columns = new String[]{"weeknumber","sleeptime","sleepid"};
		Cursor cursor = this.db.query(this.tableName, columns, columns[0]+" = ? and "+columns[1]+" = ?", new String[]{weeknumber,sleeptime}, null, null, null);
		cursor.moveToFirst();
		if(!(cursor.getCount() <= 0)) {
			result = cursor.getString(cursor.getColumnIndex(columns[2]));
		}
		cursor.close();
		return result;
	}
	
	/**
	 * 获取时长周文案
	 * @param weeknumber
	 * @param avgtime
	 * @return
	 */
	public String queryAvgid(String weeknumber,String avgtime) {
		String result = "";
		if(!this.tableName.equals(MyTabList.WEEK)) {
			return result;
		}
		//注意数组组成，前两个是作为sql查询条件，最后为本方法要获取的列表值
		String[] columns = new String[]{"weeknumber","avgtime","avgid"};
		Cursor cursor = this.db.query(this.tableName, columns, columns[0]+" = ? and "+columns[1]+" = ?", new String[]{weeknumber,avgtime}, null, null, null);
		cursor.moveToFirst();
		if(!(cursor.getCount() <= 0)) {
			result = cursor.getString(cursor.getColumnIndex(columns[2]));
		}
		cursor.close();
		return result;
	}
	
	/**
	 * 获取起床周文案
	 * @param weeknumber
	 * @param uptime
	 * @return
	 */
	public String queryUpid(String weeknumber,String uptime) {
		String result = "";
		if(!this.tableName.equals(MyTabList.WEEK)) {
			return result;
		}
		//注意数组组成，前两个是作为sql查询条件，最后为本方法要获取的列表值
		String[] columns = new String[]{"weeknumber","uptime","upid"};
		Cursor cursor = this.db.query(this.tableName, columns, columns[0]+" = ? and "+columns[1]+" = ?", new String[]{weeknumber,uptime}, null, null, null);
		cursor.moveToFirst();
		if(!(cursor.getCount() <= 0)) {
			result = cursor.getString(cursor.getColumnIndex(columns[2]));
		}
		cursor.close();
		return result;
	}
	
	public List<?> query(String[] columns,String selection,String[] selectionArgs,String oderBy) throws Exception{
		if(this.tableName.equals(MyTabList.SLEEPDATA)){
			List<SleepData> list = new ArrayList<SleepData>();
			Cursor result = this.db.query(this.tableName, columns, selection, selectionArgs, null, null, oderBy);
			SleepData data = null;
			result.moveToFirst();
			while(!result.isAfterLast()){
				data = new SleepData();
				try{
					data.setTime((result.getString(result.getColumnIndex("time"))) == null ?
							"":result.getString(result.getColumnIndex("time")));
					data.setX((result.getString(result.getColumnIndex("x"))) == null ?
							"":result.getString(result.getColumnIndex("x")));
					data.setY((result.getString(result.getColumnIndex("y"))) == null ?
							"":result.getString(result.getColumnIndex("y")));
					data.setZ((result.getString(result.getColumnIndex("z"))) == null ?
							"":result.getString(result.getColumnIndex("z")));
				}catch(Exception e){
					e.printStackTrace();
					list.clear();
					result.close();
					return list;
				}
				list.add(data);
				result.moveToNext();
			}
			result.close();
			return list;
		}else if(this.tableName.equals(MyTabList.SLEEPTIME)) {
			List<SleepResult> list = new ArrayList<SleepResult>();
			Cursor result = this.db.query(this.tableName, columns, selection, selectionArgs, null, null, oderBy);
			SleepResult data = null;
			result.moveToFirst();
			while(!result.isAfterLast()){
				data = new SleepResult();
				try{
					if(haveColumn(columns,"date")) {
						data.setDate((result.getString(result.getColumnIndex("date"))) == null ?
								"":result.getString(result.getColumnIndex("date")));	
					}
					if(haveColumn(columns, "starttime")) {
						String starttime = (result.getString(result.getColumnIndex("starttime"))) == null ?
								"":result.getString(result.getColumnIndex("starttime"));
						if(!TextUtils.isEmpty(starttime))
							data.setStarttime(DataUtils.dealData((int)(Float.valueOf(starttime)/60)) + ":" + DataUtils.dealData((int)(Float.valueOf(starttime) % 60)));
						else
							data.setStarttime("");
					}
					if(haveColumn(columns, "endtime")) {
						String endtime = (result.getString(result.getColumnIndex("endtime"))) == null ?
								"":result.getString(result.getColumnIndex("endtime"));
						if(!TextUtils.isEmpty(endtime))
							data.setEndtime(DataUtils.dealData((int)(Float.valueOf(endtime)/60)) + ":" + DataUtils.dealData((int)(Float.valueOf(endtime) % 60)));
						else
							data.setEndtime("");
					}
					if(haveColumn(columns, "sleeptime")) {
						data.setSleeptime((result.getString(result.getColumnIndex("sleeptime"))) == null ?
								"":result.getString(result.getColumnIndex("sleeptime")));
					}
					if(haveColumn(columns, "uptime")) {
						data.setUptime((result.getString(result.getColumnIndex("uptime"))) == null ?
								"":result.getString(result.getColumnIndex("uptime")));
					}
					if(haveColumn(columns,"orgsleeptime")) {
						data.setOrgsleeptime((result.getString(result.getColumnIndex("orgsleeptime"))) == null ?
								"":result.getString(result.getColumnIndex("orgsleeptime")));
					}
					if(haveColumn(columns,"orguptime")) {
						data.setOrguptime((result.getString(result.getColumnIndex("orguptime"))) == null ?
								"":result.getString(result.getColumnIndex("orguptime")));
					}
					if(haveColumn(columns, "sleeplength")) {
						data.setSleepLength((result.getString(result.getColumnIndex("sleeplength"))) == null ?
								"0":result.getString(result.getColumnIndex("sleeplength")));
					}
					if(haveColumn(columns, "healthlength")) {
						data.setHealthSleep((result.getString(result.getColumnIndex("healthlength"))) == null ?
								"0":result.getString(result.getColumnIndex("healthlength")));
					}
					if(haveColumn(columns, "sleepacce")) {
						data.setSleep_acce(Float.valueOf(((result.getString(result.getColumnIndex("sleepacce"))) == null || "".equals((result.getString(result.getColumnIndex("sleepacce"))))) ?
								"0":result.getString(result.getColumnIndex("sleepacce"))));
					}
					if(haveColumn(columns, "upacce")) {
						data.setGetup_acce(Float.valueOf(((result.getString(result.getColumnIndex("upacce"))) == null || "".equals((result.getString(result.getColumnIndex("sleepacce"))))) ?
								"0":result.getString(result.getColumnIndex("upacce"))));
					}
					if(haveColumn(columns, "ischange")) {
						data.setUpdate(result.getString(result.getColumnIndex("ischange")));
					}
					if(haveColumn(columns, "isupload")) {
						data.setUpload(result.getString(result.getColumnIndex("isupload")));
					}
					if(haveColumn(columns, "record_state")) {
						data.setRecordState(result.getString(result.getColumnIndex("record_state")));
					}
					if(haveColumn(columns, "diagramdata")) {
						String diagram = result.getString(result.getColumnIndex("diagramdata"));
						String diagramdata = diagram == null 
								|| "".equals(diagram) 
								? "":diagram;
						ArrayList<SleepDistributionInfo> infoList = new ArrayList<SleepDistributionInfo>();
						if(diagramdata != null && !"".equals(diagramdata)) {
							JSONObject jo = new JSONObject(diagramdata);
							JSONArray ja = jo.getJSONArray("diagramdata");
							for(int i = 0;i<ja.length();i++) {
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
						data.setInfoList(infoList);
					}	
				}catch(Exception e){
					e.printStackTrace();
					list.clear();
					result.close();
					return list;
				}
				list.add(data);
				result.moveToNext();
			}
			result.close();
			return list;
		}
		return null;
	}
	
	private final String FILE_X_PATH = "/sdcard/DCIM/x.txt";
	private final String FILE_Y_PATH = "/sdcard/DCIM/y.txt";
	private final String FILE_Z_PATH = "/sdcard/DCIM/z.txt";
	private final String FILE_TIME_PATH = "/sdcard/DCIM/jiasudutime.txt";
	
	private final String FILE_DATE_PATH = "/sdcard/DCIM/result_date.txt";
	private final String FILE_STARTTIME_PATH = "/sdcard/DCIM/result_starttime.txt";
	private final String FILE_EDNTIME_PATH = "/sdcard/DCIM/result_endtime.txt";
	private final String FILE_SLEEPTIME_PATH = "/sdcard/DCIM/result_sleeptime.txt";
	private final String FILE_UPTIME_PATH = "/sdcard/DCIM/result_uptime.txt";
	
	private final String FILE_SETDATE_PATH = "/sdcard/DCIM/sleep_result.txt";
	@SuppressWarnings("resource")
	public void exportDataToSDcards() throws IOException {

		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		
		if(this.tableName.equals(MyTabList.SLEEPDATA)) {
			File timefile = new File(FILE_TIME_PATH);			
			if(!timefile.exists()) {
				timefile.createNewFile();
			}
			RandomAccessFile timeFile = new RandomAccessFile(FILE_TIME_PATH,"rw");
			Cursor result = this.db.query(this.tableName, new String[]{"time","x","y","z","date"}, null, null, null, null, null);
			StringBuffer timeBuffer = new StringBuffer();
			result.moveToFirst();
			while(!result.isAfterLast()) {
				timeBuffer.append(result.getString(result.getColumnIndex("time")).equals("") ? "":(result.getString(result.getColumnIndex("time"))+" 日期："))
				.append(result.getString(result.getColumnIndex("date")) + " x:")
				.append(result.getString(result.getColumnIndex("x")).equals("") ? "":(result.getString(result.getColumnIndex("x"))+" y:"))
				.append(result.getString(result.getColumnIndex("y")).equals("") ? "":(result.getString(result.getColumnIndex("y"))+" z:"))
				.append(result.getString(result.getColumnIndex("z")).equals("") ? "":(result.getString(result.getColumnIndex("z"))+"\r\n"));
				result.moveToNext();
			}
			timeFile.setLength(0);
			timeFile.seek(0);
			timeFile.writeBytes(timeBuffer.toString());
			result.close();
		}
		
		if(this.tableName.equals(MyTabList.SLEEPTIME)) {
			File setdatefile = new File(FILE_SETDATE_PATH);
			if(!setdatefile.exists()) {
				setdatefile.createNewFile();
			}	
			RandomAccessFile dateFile = new RandomAccessFile(FILE_SETDATE_PATH,"rw");
			Cursor result = this.db.query(this.tableName, new String[]{"date","sleeptime","starttime",
					"uptime","endtime","sleepacce","upacce","sleeplength","healthlength","diagramdata","ischange"}, null, null, null, null, null);
			StringBuffer dateBuffer = new StringBuffer();
			result.moveToFirst();
			while(!result.isAfterLast()) {
				dateBuffer.append("".equals(result.getString(result.getColumnIndex("date"))) || null == result.getString(result.getColumnIndex("date")) ? "null":(result.getString(result.getColumnIndex("date"))+" sleeptime:"))
				.append("".equals(result.getString(result.getColumnIndex("sleeptime"))) || null == result.getString(result.getColumnIndex("sleeptime")) ? "null uptime:":(result.getString(result.getColumnIndex("sleeptime"))+" uptime:"))
				.append("".equals(result.getString(result.getColumnIndex("uptime"))) || null == result.getString(result.getColumnIndex("uptime")) ? "null starttime:":(result.getString(result.getColumnIndex("uptime"))+" starttime:"))
				.append("".equals(result.getString(result.getColumnIndex("starttime"))) || null == result.getString(result.getColumnIndex("starttime")) ? "null endtime:":(result.getString(result.getColumnIndex("starttime"))+" endtime:"))
				.append("".equals(result.getString(result.getColumnIndex("endtime"))) || null == result.getString(result.getColumnIndex("endtime")) ? "null sleepacce:":(result.getString(result.getColumnIndex("endtime"))+" sleepacce:"))
				.append("".equals(result.getString(result.getColumnIndex("sleepacce"))) || null == result.getString(result.getColumnIndex("sleepacce")) ? "null upacce:":(result.getString(result.getColumnIndex("sleepacce"))+" upacce:"))
				.append("".equals(result.getString(result.getColumnIndex("upacce"))) || null == result.getString(result.getColumnIndex("upacce")) ? "null sleeplength:":(result.getString(result.getColumnIndex("upacce"))+" sleeplength:"))
				.append("".equals(result.getString(result.getColumnIndex("sleeplength"))) || null == result.getString(result.getColumnIndex("sleeplength")) ? "null healthlength:":(result.getString(result.getColumnIndex("sleeplength"))+" healthlength:"))
				.append("".equals(result.getString(result.getColumnIndex("healthlength"))) || null == result.getString(result.getColumnIndex("healthlength")) ? "null diagramdata:":(result.getString(result.getColumnIndex("healthlength"))+" diagramdata:"))
				.append("".equals(result.getString(result.getColumnIndex("diagramdata"))) || null == result.getString(result.getColumnIndex("diagramdata")) ? "null ischange:":(result.getString(result.getColumnIndex("diagramdata"))+" ischange:"))
				.append("".equals(result.getString(result.getColumnIndex("ischange"))) || null == result.getString(result.getColumnIndex("ischange")) ? "null":(result.getString(result.getColumnIndex("ischange"))+"\r\n\r\n"));
				result.moveToNext();
			}
			
			dateFile.setLength(0);
			dateFile.seek(0);
			dateFile.writeBytes(dateBuffer.toString());
			result.close();
		}
	}
	
	
	
}
