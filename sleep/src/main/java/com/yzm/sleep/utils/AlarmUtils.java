package com.yzm.sleep.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.yzm.sleep.MyApplication;
import com.yzm.sleep.background.AlarmDBOperate;
import com.yzm.sleep.background.AlarmService.ListAlarmTime;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.bean.DefaultAudioInfoHead;
import com.yzm.sleep.bean.DefaultAudioInfoParam;
import com.yzm.sleep.dao.AudioDAO;
import com.yzm.sleep.dao.AudioDAO.DownloadAudioInfoClass;
import com.yzm.sleep.model.DownloadAudioInfo;
import com.yzm.sleep.model.RecordAudioInfo;
import com.yzm.sleep.model.RingtoneInfo;

public class AlarmUtils {
	/**
	 * 显示闹钟响起的星期
	 * @param s 1,2,3
	 * @return
	 */
	public static String getWeeksString(String s){
		StringBuffer sb = new StringBuffer();
		String substring = "";
		/*if(s.contains("1")&&s.contains("2")&&s.contains("3")&&s.contains("4")
				&&s.contains("5")&&s.contains("6")&&s.contains("7")){
			sb.append("每天");
			substring = sb.toString();
		}
		else*/ if(!s.contains("1")&&!s.contains("2")&&!s.contains("3")&&!s.contains("4")
				&&!s.contains("5")&&!s.contains("6")&&!s.contains("7"))
		{
			sb.append("不重复");
			substring = sb.toString();
		}
		else{
			sb.append("周");
			for (int i = 0; i < s.length(); i++) {
				String week = s.substring(i, i+1);
				if(week.equals("1")){
					sb.append("一 ");
				}else if(week.equals("2")){
					sb.append("二 ");
				}else if(week.equals("3")){
					sb.append("三 ");
				}else if(week.equals("4")){
					sb.append("四 ");
				}else if(week.equals("5")){
					sb.append("五 ");
				}else if(week.equals("6")){
					sb.append("六 ");
				}else if(week.equals("7")){
					sb.append("日 ");
				}
			}
			String string = sb.toString();
			if (" ".equals(string.substring(string.length() - 1))) {
				substring = string.substring(0, string.length() - 1);
			}
		}
		
		return substring;
	}
	
	//删除sd卡里面的铃音
	private static void deleteDirFormSDcard(){
		FileUtil.deleteFile(new File(FileUtil.getSDPath() + "/xiangcheng/Audio/download"));
	}
	
	//删除sd卡里面的数据库的下载的铃音数据库 download_audio
	private static void deleteTableFromSDcard(Context context){
		MyDatabaseHelper helper = MyDatabaseHelper
				.getInstance(context);
		MytabOperate operateDayDataDelete = new MytabOperate(
				helper.getWritableDatabase(), MyTabList.DOWNLOAD_AUDIO);
		operateDayDataDelete.delete(null, null);
		operateDayDataDelete.close();
	}
	/**
	 * 保存默认铃音和默认闹钟
	 * @param downloadAudioInfoClass
	 */
	public static void saveDefaultAudioAndAlarm(Context context) {
		//获取要保存到sd卡的铃音信息
		DefaultAudioInfoHead dataAudioInfoHead= new DefaultAudioInfoHead();
		dataAudioInfoHead.userid="100050494";
		dataAudioInfoHead.userheadUrl="http://115.29.187.126/orangesleep/upload_file/100050494/profile/profile_20150515113324.jpg";
		dataAudioInfoHead.username="香橙";
		dataAudioInfoHead.usersex = "01" ;
		dataAudioInfoHead.userold = "1986";
		dataAudioInfoHead.userduty = "200000012" ;	
		
		
		DefaultAudioInfoParam mParamDatAudioInfoParam= new DefaultAudioInfoParam();
//		mParamDatAudioInfoParam= new DefaultAudioInfoParam();	
//		mParamDatAudioInfoParam.audioKey = "xiangcheng3";
//		mParamDatAudioInfoParam.title="宝贝起床啦";
//		mParamDatAudioInfoParam.audioUrl="http://115.29.187.126/orangesleep/upload_file/100050494/morningcall_public/3.acc";
//		mParamDatAudioInfoParam.coverUrl="http://115.29.187.126/orangesleep/upload_file/100050494/morningcall_public/3.jpg";
//		mParamDatAudioInfoParam= new DefaultAudioInfoParam();
		mParamDatAudioInfoParam.audioKey = "xiangcheng19";
		mParamDatAudioInfoParam.title="梦幻闹钟";
		mParamDatAudioInfoParam.audioUrl="http://115.29.187.126/orangesleep/upload_file/100050494/morningcall_public/19.acc";
		mParamDatAudioInfoParam.coverUrl="http://115.29.187.126/orangesleep/upload_file/100050494/morningcall_public/19.jpg";
		dataAudioInfoHead.m_list.add(mParamDatAudioInfoParam);
		
		
		DefaultAudioInfoParam mParamDatAudioInfoParam2= new DefaultAudioInfoParam();
		mParamDatAudioInfoParam2.audioKey = "xiangcheng9";
		mParamDatAudioInfoParam2.title="上班族";
		mParamDatAudioInfoParam2.audioUrl="http://115.29.187.126/orangesleep/upload_file/100050494/morningcall_public/9.acc";
		mParamDatAudioInfoParam2.coverUrl="http://115.29.187.126/orangesleep/upload_file/100050494/morningcall_public/9.jpg";
		dataAudioInfoHead.m_list.add(mParamDatAudioInfoParam2);
		
		//清空已保存的铃音信息
		deleteDirFormSDcard();
		deleteTableFromSDcard(context);
		
		//保存铃音信息
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		AudioDAO audioDAO = new AudioDAO(db, MyTabList.DOWNLOAD_AUDIO);
		
		DownloadAudioInfoClass downloadAudioInfoClass;
		DefaultAudioInfoParam audioInfoParam;
		for (int i = 0; i < dataAudioInfoHead.m_list.size(); i++) {
			audioInfoParam = dataAudioInfoHead.m_list.get(i);
			if(FileUtil.copyResToSdcard(context, audioInfoParam.audioKey, MyApplication.XIANGCHENG_AUDIO_PATH)){
				downloadAudioInfoClass = new DownloadAudioInfoClass();
				downloadAudioInfoClass.AudioUserId = dataAudioInfoHead.userid;
				downloadAudioInfoClass.AudioUserNickName = dataAudioInfoHead.username;
				downloadAudioInfoClass.AudioTitle = audioInfoParam.title;
				downloadAudioInfoClass.AudioType = 1;
				downloadAudioInfoClass.AudioKey = audioInfoParam.audioKey;
				downloadAudioInfoClass.AudioCover = audioInfoParam.coverUrl;
				downloadAudioInfoClass.AudioCoverKey = audioInfoParam.coverUrl;
				downloadAudioInfoClass.AudioCoverSuolue = audioInfoParam.coverUrl;
				downloadAudioInfoClass.AudioCoverKeySuolue = audioInfoParam.coverUrl;
				downloadAudioInfoClass.AudioUserProfile = dataAudioInfoHead.userheadUrl;
				downloadAudioInfoClass.UserProfileKey = dataAudioInfoHead.userheadUrl;
				downloadAudioInfoClass.AudioUserProfileSuolue = dataAudioInfoHead.userheadUrl;
				downloadAudioInfoClass.UserProfileKeySuolue = dataAudioInfoHead.userheadUrl;
				downloadAudioInfoClass.AudioLocalPath = MyApplication.XIANGCHENG_AUDIO_PATH + audioInfoParam.audioKey + ".aac";
				audioDAO.saveDownloadInfo(downloadAudioInfoClass);
			}
		}
		/*首次使用默认闹钟，保存默认闹钟*/
		if (!PreManager.instance().getHasGotoAlarm(context)) {
			AlarmUtils.saveDefaultAlarm(context);
			PreManager.instance().saveHasGotoAlarm(context, true);
		}
	}
	
	/**
	 * 保存默认闹钟， 生成两个默认时间为设置起点和睡点的闹钟，状态为关闭状态，
	 */
	public static void saveDefaultAlarm(Context context){
		String AlarmDay = "";
		// 生成默认时间为睡点的闹钟 设置基本属性
		saveDefaultCurrentAudio(context);
		ListAlarmTime m_ListAlarmTime = new ListAlarmTime();
		m_ListAlarmTime.AlarmDay = AlarmDay;
		m_ListAlarmTime.AlarmType = 0;//非专属铃音
		m_ListAlarmTime.AlarmPlant = "1,2,3,4,5";
		m_ListAlarmTime.AlarmTime = PreManager.instance().getSleepTime_Setting(context);
		
		m_ListAlarmTime.AlarmRepeat = 1;
		m_ListAlarmTime.AlarmDelay = 1;
		m_ListAlarmTime.AlarmOnOff = 0;
		
		//获取当前默认选择的铃音
		RingtoneInfo sleepRingtoneInfo = MyApplication.instance().getSleepSelectRingInfo();
		//设置闹钟铃音相关属性
		m_ListAlarmTime.AlarmTitle = sleepRingtoneInfo.title;
		m_ListAlarmTime.AlarmAudio = sleepRingtoneInfo.ringtonePath;
		m_ListAlarmTime.AlarmProfile = sleepRingtoneInfo.profile;
		m_ListAlarmTime.AlarmUserNickname = sleepRingtoneInfo.nickname;
		m_ListAlarmTime.AlarmUserId = sleepRingtoneInfo.int_id;
		m_ListAlarmTime.AlarmDownloads = sleepRingtoneInfo.downloads;
		m_ListAlarmTime.AlarmAudioCover = sleepRingtoneInfo.themePicString;
		m_ListAlarmTime.AudioCoverKey  = sleepRingtoneInfo.themePicString;
		m_ListAlarmTime.remindTitle = "睡觉提醒";
				
		/**操作数据库*/
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		AlarmDBOperate alarmDBOperate = new AlarmDBOperate(db, MyTabList.ALARM_TIME);
		
		//清空闹钟表
		alarmDBOperate.delete(null, null);
		
		//保存时间为  '设置的睡觉时刻' 的默认闹钟
		alarmDBOperate.saveAwakeAlarm(context,m_ListAlarmTime);
		
		//创建第二条默认闹钟
		m_ListAlarmTime.AlarmTime = PreManager.instance().getGetupTime_Setting(context);
		//获取当前默认选择的铃音
		RingtoneInfo currentRingtoneInfo = MyApplication.instance().getCurrentSelectRingInfo();
		//设置闹钟铃音相关属性
		m_ListAlarmTime.AlarmTitle = currentRingtoneInfo.title;
		m_ListAlarmTime.AlarmAudio = currentRingtoneInfo.ringtonePath;
		m_ListAlarmTime.AlarmProfile = currentRingtoneInfo.profile;
		m_ListAlarmTime.AlarmUserNickname = currentRingtoneInfo.nickname;
		m_ListAlarmTime.AlarmUserId = currentRingtoneInfo.int_id;
		m_ListAlarmTime.AlarmDownloads = currentRingtoneInfo.downloads;
		m_ListAlarmTime.AlarmAudioCover = currentRingtoneInfo.themePicString;
		m_ListAlarmTime.AudioCoverKey  = currentRingtoneInfo.themePicString;
		m_ListAlarmTime.remindTitle = "早起提醒";
		//保存时间为  '设置的起床时刻' 的默认闹钟
		alarmDBOperate.saveAwakeAlarm(context,m_ListAlarmTime);
	}
	/**
	 * 保存默认的当前选择铃音信息
	 */
	public static void saveDefaultCurrentAudio(Context context){
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context
				.getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		AudioDAO dao = new AudioDAO(db,
				MyTabList.DOWNLOAD_AUDIO);
		ArrayList<DownloadAudioInfo> getDefaultAudioInfo = null;//dao.GetAudioInfo("xiangcheng19");//dao.GetDefaultAudioInfo();
		try {
			getDefaultAudioInfo = dao.GetAudioInfo("xiangcheng19");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (getDefaultAudioInfo != null && getDefaultAudioInfo.size() > 0) {
			int num = 0;
			try {
				num = new Random().nextInt(getDefaultAudioInfo.size());
			} catch (Exception e) {
				num = 0;
			}
			DownloadAudioInfo downloadAudioInfoClass = getDefaultAudioInfo.get(num);
			
			RingtoneInfo ringInfo = new RingtoneInfo();
			
			ringInfo.int_id = downloadAudioInfoClass.AudioUserId;
			ringInfo.nickname = downloadAudioInfoClass.AudioUserNickName;
			ringInfo.title = downloadAudioInfoClass.title;
			ringInfo.ringtoneType = downloadAudioInfoClass.AudioType;
			ringInfo.fileKey = downloadAudioInfoClass.AudioKey;
			
			ringInfo.themePicString = downloadAudioInfoClass.AudioCover;
			ringInfo.coverKey = downloadAudioInfoClass.AudioCoverKey;
			ringInfo.ly_pic_url_suolue = downloadAudioInfoClass.AudioCoverSuolue;
			ringInfo.ly_pic_key_suolue = downloadAudioInfoClass.AudioCoverKeySuolue;
			ringInfo.profile = downloadAudioInfoClass.AudioUserProfile;
			ringInfo.profileKey = downloadAudioInfoClass.UserProfileKey;
			ringInfo.profile_suolue = downloadAudioInfoClass.AudioUserProfileSuolue;
			ringInfo.profile_key_suolue = downloadAudioInfoClass.UserProfileKeySuolue;
			
			ringInfo.ringtonePath = downloadAudioInfoClass.path;
			MyApplication.instance().setCurrentSelectRingInfo(ringInfo);
		}else {
			RingtoneInfo ringInfo = new RingtoneInfo();
			ringInfo.int_id = "default_audio.aac";
			ringInfo.nickname = "香橙";
			ringInfo.title = "香橙闹钟";
			ringInfo.ringtoneType = 0;
			ringInfo.fileKey = "default_audio.aac";
			
			ringInfo.themePicString = "default_audio.aac";
			ringInfo.coverKey = "default_audio.aac";
			ringInfo.ly_pic_url_suolue = "default_audio.aac";
			ringInfo.ly_pic_key_suolue = "default_audio.aac";
			ringInfo.profile = "default_audio.aac";
			ringInfo.profileKey = "default_audio.aac";
			ringInfo.profile_suolue = "default_audio.aac";
			ringInfo.profile_key_suolue = "default_audio.aac";
			
			ringInfo.ringtonePath = MyApplication.XIANGCHENG_AUDIO_PATH + "default_audio.aac";
			MyApplication.instance().setCurrentSelectRingInfo(ringInfo);
		}
		
		
		ArrayList<DownloadAudioInfo> getSleepAudioInfo = null;//dao.GetAudioInfo("xiangcheng9");
		try {
			getSleepAudioInfo = dao.GetAudioInfo("xiangcheng9");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (getSleepAudioInfo != null && getSleepAudioInfo.size() > 0) {
			int num = 0;
			try {
				num = new Random().nextInt(getSleepAudioInfo.size());
			} catch (Exception e) {
				num = 0;
			}
			DownloadAudioInfo downloadAudioInfoClass = getSleepAudioInfo.get(num);
			
			RingtoneInfo ringInfo = new RingtoneInfo();
			
			ringInfo.int_id = downloadAudioInfoClass.AudioUserId;
			ringInfo.nickname = downloadAudioInfoClass.AudioUserNickName;
			ringInfo.title = downloadAudioInfoClass.title;
			ringInfo.ringtoneType = downloadAudioInfoClass.AudioType;
			ringInfo.fileKey = downloadAudioInfoClass.AudioKey;
			
			ringInfo.themePicString = downloadAudioInfoClass.AudioCover;
			ringInfo.coverKey = downloadAudioInfoClass.AudioCoverKey;
			ringInfo.ly_pic_url_suolue = downloadAudioInfoClass.AudioCoverSuolue;
			ringInfo.ly_pic_key_suolue = downloadAudioInfoClass.AudioCoverKeySuolue;
			ringInfo.profile = downloadAudioInfoClass.AudioUserProfile;
			ringInfo.profileKey = downloadAudioInfoClass.UserProfileKey;
			ringInfo.profile_suolue = downloadAudioInfoClass.AudioUserProfileSuolue;
			ringInfo.profile_key_suolue = downloadAudioInfoClass.UserProfileKeySuolue;
			
			ringInfo.ringtonePath = downloadAudioInfoClass.path;
			MyApplication.instance().setSleepSelectRingInfo(ringInfo);
		}else {
			RingtoneInfo ringInfo = new RingtoneInfo();
			ringInfo.int_id = "default_audio.aac";
			ringInfo.nickname = "香橙";
			ringInfo.title = "香橙闹钟";
			ringInfo.ringtoneType = 0;
			ringInfo.fileKey = "default_audio.aac";
			
			ringInfo.themePicString = "default_audio.aac";
			ringInfo.coverKey = "default_audio.aac";
			ringInfo.ly_pic_url_suolue = "default_audio.aac";
			ringInfo.ly_pic_key_suolue = "default_audio.aac";
			ringInfo.profile = "default_audio.aac";
			ringInfo.profileKey = "default_audio.aac";
			ringInfo.profile_suolue = "default_audio.aac";
			ringInfo.profile_key_suolue = "default_audio.aac";
			
			ringInfo.ringtonePath = MyApplication.XIANGCHENG_AUDIO_PATH + "default_audio.aac";
			MyApplication.instance().setSleepSelectRingInfo(ringInfo);
		}
	}
	
	/**
	 * 获取本地铃声信息
	 * @param context
	 */
	private  static List<String> getLocalRingtone(Context context,String ringtonePath){
		List<String> audioSuffixs = new ArrayList<String>();
		
		// 查询媒体数据库   本地铃声数据
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		// 遍历所有的媒体数据库
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			// 歌曲文件的路径 ：MediaStore.Audio.Media.DATA
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
			 System.out.println("url值：" + url);
			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐（非0为音乐，但在魅族4Pro 音乐文件的这个值是0）
			 System.out.println("isMusic值：" + isMusic);
			if (url.contains(ringtonePath)) {
				audioSuffixs.add(url);
			}else {
				System.out.println("不属于指定文件夹下，当前文件路径：" + url);
			}
		}
		System.out.println(">>>>>>>>>>>>>>>");
		for (String string : audioSuffixs) {
			System.out.println("文件路径：" + string);
		}
		return audioSuffixs;
	}
	
	/**
	 * 获取下载的铃音（在 本地存在）
	 * @param context
	 * @param ringtonePath  铃音所在父目录
	 */
	public static ArrayList<DownloadAudioInfo> getDownloadAudio(Context context,String ringtonePath){
		ArrayList<DownloadAudioInfo> downloadAudios = new ArrayList<DownloadAudioInfo>();
//		ArrayList<DownloadAudioInfo> localAudios = new ArrayList<DownloadAudioInfo>();
//		List<String> audioSuffixs = getLocalRingtone(context, ringtonePath);
		MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
		final AudioDAO audioDAO = new AudioDAO(db, MyTabList.RECORD_AUDIO);
		downloadAudios = audioDAO.GetDownloadAudioInfo();
//		for (DownloadAudioInfo downloadAudio : downloadAudios) {
//			if (audioSuffixs.contains(downloadAudio.path)) {
//				localAudios.add(downloadAudio);
//			}
//		}
		return downloadAudios;
	}
	/**
	 * 获取录制的铃音（在本地存在）
	 * @param context
	 * @param ringtonePath  铃音所在父目录
	 * @return
	 */
	public static ArrayList<RecordAudioInfo> getRecordAudio(Context context,String ringtonePath){
		ArrayList<RecordAudioInfo> recordAudios = new ArrayList<RecordAudioInfo>();
//		ArrayList<RecordAudioInfo> localAudios = new ArrayList<RecordAudioInfo>();
//		List<String> audioSuffixs = getLocalRingtone(context, ringtonePath);
		MyDatabaseHelper myDatabaseHelper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
		final AudioDAO audioDAO = new AudioDAO(db, MyTabList.RECORD_AUDIO);
		recordAudios =  audioDAO.GetRecordAudioInfo(PreManager.instance().getUserId(context),false);
//		for (RecordAudioInfo downloadAudio : recordAudios) {
//			if (audioSuffixs.contains(downloadAudio.path)) {
//				localAudios.add(downloadAudio);
//			}
//		}
		return recordAudios;
	}
}
