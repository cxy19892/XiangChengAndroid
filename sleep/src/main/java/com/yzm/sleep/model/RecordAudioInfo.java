package com.yzm.sleep.model;

import java.io.Serializable;
/**
 * 用户录制铃声信息类
 * @author Administrator
 *
 */
public class RecordAudioInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id ;
	public String userId;
	/**铃声标题*/
	public String title = "";
	/**日期*/
	public String date ="";
	/**铃声时长单位为毫秒*/
	public int duration = 0;
	/**本地路径*/
	public String path = "";
	/**主题图片路径*/
	public String ThemePicpath = "";
	/**是否显示铃声详细信息*/
	public boolean isShowDetail = false;
}
