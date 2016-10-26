package com.yzm.sleep.background;

public class MyTabList {
	/**
	 * 数据库文件
	 */
	public static final String DATABASENAME = "sleep.db";
	/**
	 * 数据库版本
	 * 2.0-------------------9
	 * 2.1,2.1.1-------------10
	 * 3.2 ------------------14
	 * 3.3-------------------15
	 */
	public static final int DATABASEVERSION = 16;
	/**
	 * 原始数据表
	 */
	public static final String SLEEPDATA = "sleep_data";
	/**
	 * 睡眠设置表
	 */
	public static final String SLEEPTIME = "day";
	
	/**
	 * 签到表
	 */
	public static final String NEW_SLEEPTIME = "sign_in";
	/**
	 * 每周反馈记录表
	 */
	public static final String FEEDBACK = "feedback";
	/**
	 * 睡眠状态表
	 */
//	public static final String INFO = "info";
	/**
	 * 周表
	 */
	public static final String WEEK = "week";
	
	/**
	 * 闹钟表
	 */
	public static final String ALARM_TIME = "alarmtime";
	/**
	 * 下载铃声表
	 */
	public static final String DOWNLOAD_AUDIO = "download_audio";
	/**
	 * 本地录制铃声
	 */
	public static final String RECORD_AUDIO = "record_audio";
	
	/**
	 * 发送失败铃音
	 */
	public static final String SEND_FAILED_AUDIO = "send_failed_audio";
	
	public static final String MODLE_PILLOWS_FILE = "file_pillow";
	
	public static final String PILLOW_SLEEP_DATA = "pillow_sleep_data";
	
	/**
	 * 提醒事项表
	 */
	public static final String REMIND_BUSINESS_INFO = "remind_busniess_info";
	
	/**
	 * 香橙小秘书推送消息存储表
	 */
	public static final String SECRETARY_MSG_LIST = "secretary_msg_list";
	
	
	public static enum TableSecretaryList{
		/** 消息ID */
        ID("id"),
        /** 消息内容 */
        CONTENT("content"),
        /** 发送日期 */
        DATE("date"),
        /** 副标题 */
        SUBTITLE("title"),
        /** 消息链接 */
        LINK("link"),
        /** 是否查看 */
        SCAN("scan");
        
        private String column;
		private TableSecretaryList(String column){
			this.column = column;
		}
		public String getCloumn(){
			return column;
		}
	}
	
	/**
	 * 枚举sleep_data表的列
	 * @author Administrator
	 *
	 */
	public static enum TableSleepData {
		TIME("time"),
		DATE("date"),
		X("x"),
		Y("y"),
		Z("z");
		
		private String column;
		private TableSleepData(String column){
			this.column = column;
		}
		
		public String getCloumn(){
			return column;
		}
	}
	
	/**
	 * 枚举week表的列
	 * @author Administrator
	 *
	 */
	public static enum TableWeek {
		WEEKNUMBER("weeknumber"),	
		SLEEPTIME("sleeptime"),
		SLEEPID("sleepid"),
		AVGTIME("avgtime"),
		AVGID("avgid"),
		UPTIME("uptime"),
		UPID("upid"); 
		
		private String column;
		private TableWeek(String column){
			this.column = column;
		}
		
		public String getCloumn(){
			return column;
		}
	}
	
	/**
	 * 枚举day表的列
	 * @author Administrator
	 *
	 */
	public static enum TableDay {
		DATE("date"),
		SLEEPTIME("sleeptime"), 				
		UPTIME("uptime"),
		SLEEPTIMELONG("sleeptimelong"), 				
		UPTIMELONG("uptimelong"),
		ORGSLEEPTIME("orgsleeptime"),
		ORGUPTIME("orguptime"),
		SLEEPACCE("sleepacce"),
		UPACCE("upacce"),
		SLEEPLENGTH("sleeplength"),
		HEALTHLENGTH("healthlength"),
		STARTTIME("starttime"),
		ENDTIME("endtime"),
		DIAGRAMDATA("diagramdata"),
		ISUPLOAD("isupload"),
		ISPUNCH("ispunch"),
		FILEID("fileid"),
		RECORD_STATE("record_state"),
		ISCHANGE("ischange");
		
		private String column;
		private TableDay(String column){
			this.column = column;
		}
		
		public String getCloumn(){
			return column;
		}
	}
	
	/**
	 * 枚举info表的列
	 * @author Administrator
	 *
	 */
	public static enum TableInfo {
		SLEEPSTATE("sleepstate"),
		ALARMSTATE("alarmstate");  		
		
		private String column;
		private TableInfo(String column){
			this.column = column;
		}
		
		public String getCloumn(){
			return column;
		}
	}	
}
