package com.yzm.sleep.model;

import java.io.Serializable;
/**
 * 用户下载铃声信息
 * @author Administrator
 *
 */
public class DownloadAudioInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id ;
	/**用户id*/
	public String loginUserId;
	/**铃声标题*/
	public String title = "";
	/**日期*/
	public String downloadDate ="";
	/**铃声时长单位为毫秒*/
	public int duration = 0;
	/**本地路径*/
	public String path = "";
	/**是否显示铃声详细信息*/
	public boolean isShowDetail = false;
	/**下载次数*/
	public int downloads;
	
	/**铃声对应用户id*/
	public String AudioUserId = "";
	/**铃声对应用户昵称*/
	public String AudioUserNickName = "";
	/**铃声对应用户年龄*/
	public String AudioUserAge = "";
	/**铃声对应用户性别*/
	public String AudioUserSex = "";
	/**铃声对应用户头职业*/
	public String AudioUserOccupation = "";
	/**铃音类型  0 - 普通铃音 ， 1 - 神秘铃音*/
	public int AudioType = 0;
	
	public String AudioKey="";
	
	public String AudioCover = "";
	public String AudioCoverKey="";
	public String AudioCoverSuolue="";
	public String AudioCoverKeySuolue="";
	
	public String AudioUserProfile = "";
	public String UserProfileKey="";
	public String AudioUserProfileSuolue = "";
	public String UserProfileKeySuolue="";
	
	
}
