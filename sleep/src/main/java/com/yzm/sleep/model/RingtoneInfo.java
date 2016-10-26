package com.yzm.sleep.model;

import java.io.Serializable;

import android.R.integer;

public class RingtoneInfo extends ResultMsg implements Serializable{
	private static final long serialVersionUID = 1L;
	/** id*/
	public int id  = 0;
//	public int audioDBID;
	public String lyid;
	/** 铃声标题*/
	public String title = "";
	/** 铃声时长*/
	public String duration = "";
	/** 铃声文件大小*/
	public long size = 0;
	/** 本地铃声存储路径*/
	public String url = "";
	
	/** 铃声状态 默认为未下载
	 * 0 已设置的铃声
	 * 1 未下载
	 * 2 已下载未设置
	 * 3 下载中*/
	public int state = -1;
	/**铃声本地路径*/
	public String ringtonePath = "";
	
	/**网络铃声下载地址*/
	public String file_url = "";
	public String fileKey = "";
	
	//铃声列表基本信息
	/**用户的内部ID*/
	public String int_id = "";
	/**用户昵称*/
	public String nickname = "";
	/**用户头像地址*/
	public String profile = "";    
	public String profileKey = "";    
	/**播放次数*/
	public String play_times = ""; 
	/**年龄*/
	public String age = "";  
	/**性别  1 男 2 女*/
	public String gender ="";
	/**职业*/
	public String occupation = "";
	
	
	/**铃声时间戳*/
	public String upload_time = "";
	/**铃声下载次数*/
	public int downloads = 0;
	
	/**铃声录制时间*/
	public String record_time = "";
	
	/**铃声下载进度*/
	public String downloadProgress = "";
	
	/**铃声主题图片*/
	public String themePicString = "";
	public String coverKey = "";
	
	/**是否是本地录制的铃音*/
	public boolean isLocalRecord = false;
	
	/**铃音类型  1 - 普通铃音 ， 2 - 神秘铃音*/
	public int ringtoneType = 1;
	
	/**  闹钟时间*/
	public String alarmTime = "";
	
	
	/**铃音点赞数量*/
	public int favorNum;
	/**铃音评论数量*/
	public int commentNum;
	
	public String profile_suolue;
	public String profile_key_suolue;
	public String ly_pic_url_suolue;
	public String ly_pic_key_suolue;
	
	public String zanstate;
	
	public boolean isselect = false;
	public boolean isdefault = false;
}
