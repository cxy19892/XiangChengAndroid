package com.yzm.sleep.model;

import java.io.Serializable;

public class SendAudioFaildInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id ;
	public String UserId = "";		//用户id
	public String TargeId = "";		//发送用户id
	public String AudioTitle = "";	//铃声的标题
	public String AudioTime = "";	//铃音时间  格式为09:20
	public String AudioCoverPath = "";	//铃音封面路径
	public String AudioLocalPath = "";	//铃音本地路径
	public int AudioType ;	//铃音类型  1 - 普通 ，2 - 神秘
	
	public String toNickName="";
}
