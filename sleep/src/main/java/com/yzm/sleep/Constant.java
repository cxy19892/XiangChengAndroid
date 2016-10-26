/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yzm.sleep;

import java.util.List;

public class Constant {

	public static  final String IS_SHOW_GUIDE = "guide_4.3.2";

	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final int CHATTYPE_SINGLE = 1;
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
	public static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
	public static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
	public static final String ACCOUNT_REMOVED = "account_removed";
	/** 0-热点。。。1-动态 */
	public static int PAGETAG_FOUND = 0;
	/** 登陆成功广播 */
	public static final String RECEVER_LOGIN_ACTION = "com.action.change.LOGIN_SUCCESS";
	/** 微信授权成功广播 */
	public static final String RECEVER_WEIXIN_RESPONSE_SUCCESS = "com.action.change.WEIXIN_RESPONS_SUCCESS";
	/** 退出登陆的广播 */
	public static final String RECEVER_EXIT = "com.action.change.EXIT";
	/** 当前账号已在其它设备登录 */
	// public static final String RECEVER_OTHER_LOGIN_ACTION =
	// "com.login.action.OTHER_LOGIN_ACTION";
	public static final String RECEIVER_ALARM_SAVE_SUCCESS = "com.xc.alarm.add.ALARM_SAVE_SUCCESS";
	public static final String RECEIVER_ALARM_UPDATE_SUCCESS = "com.xc.alarm.add.ALARM_UPDATE_SUCCESS";
	/** 添加新创建的小组的广播或者关注广播 */
	public static final String RECEVER_CREATEGROUP_ACTION = "com.action.change.CREATE_GROUP";
	/** 用户生日修改广播 用于首页刷新日睡眠数据分析 */
	public static final String RECEVER_USER_BIRTHDAY_UPDATE = "com.action.change.USER_BIRTHDAY";
	/** 用户信息修改头像、昵称 用于我的界面显示刷新 */
	public static final String RECEVER_USER_MESSAGE_UPDATE = "com.action.change.USER_MESSAGE";
	/** 修改小组信息后发送广播 */
	public static final String UPDATA_ACTION = "com.action.change.UPDATA";
	/** 取消关注的广播 */
	public static final String RECEVER_NOINTEREST_ACTION = "com.action.change.NOINTEREST_GROUP";
	public static final String POST_TOPICS_ACTION = "com.action.topics.TOPIC_POST_SUC";
	public static final int DELETEDBANDLOGOUTCODE = 100;
	/**评估完成*/
	public static final String PINGGU_DEAL_ACTION = "com.action.change.PINGGU_DEAL_ACTION";
	/**
	 * 睡眠计划生成成功
	 */
	public static final String SLEEP_PLAN_IMPROVE_ACTION = "com.action.change.SLEEP_PLAN_IMPROVE_ACTION";
	
	/**
	 * 修改非量化数据
	 */
	public static final String MODEFY_SLEEP_FEEL_ACTION = "com.action.change.MODEFY_SLEEP_FEEL_ACTION";

	/** 屏幕的像素宽 */
	public static int screenWidht = 0;
	/** 屏幕的像素高 */
	public static int screenHeight = 0;

	/** 发点赞 */
	public static final String POST_TOPICEZAN = "com.action.xchange.POST_TOPICEZAN";

	/** 发评论 */
	public static final String POST_TOPICECOMMENT = "com.action.xchange.POST_TOPICECOMMENT";

	/** 发删除 */
	public static final String POST_TOPICEDELETE = "com.action.xchange.POST_TOPICEDELETE";

	/** 发到置顶 */
	public static final String POST_TOPICESETTOP = "com.action.xchange.POST_TOPICESETTOP";

	/** 小组关注 */
	public static final String POST_GROPUSATTENTION = "com.action.xchange.POST_GROPUSATTENTION";

	/** 小组信息修改 */
	public static final String POST_GROPINFOEDIT = "com.action.xchange.POST_GROPINFOEDIT";

	/** 新增话题 */
	public static final String POST_ADDTOPICE = "com.action.xchange.POST_ADDTOPICE";

	/** 删除专家成员列表 */
	public static final String POST_DELETEZJMENBER = "com.action.xchange.POST_DELETEZJMENBER";
	/** 修改7日计划 **/
	public static final String MODIFY_PLAN = "com.action.xchange.MODIFY_PLAN";
	/** 修改7日计划 **/
	public static final String IMPROVE_SLEEP_PLAN = "com.action.xchange.IMPROVE_SLEEP_PLAN";
	/** 周反馈完成 */
	public static final String WEEK_FEED_BACK_SUC = "com.action.xchange.WEEK_FEED_BACK_SUC";

	/** 我的页面周反馈变化 */
	public static final String WEEK_FEED_BACK_SUC_INPERSONAL = "com.action.xchange.WEEK_FEED_BACK_SUC_INPERSONAL";
	/**
	 * 小组请求码
	 */
	public static final int GROUP_CODE = 1001;
	/**
	 * 小组返回码
	 */
	public static final int GROUP_RESUIT_CODE = 1002;
	/**
	 * 标签的请求码
	 */
	public static final int ADD_LABLE = 9771;
	/**
	 * 描述的请求码
	 */
	public static final int ADD_DESCRIPTION = 9772;
	/**
	 * 相机的请求码
	 */
	public static final int TAKE_PHOTO = 9773;
	/**
	 * 裁剪图片的请求码
	 */
	public static final int CROP_PHOTO = 9774;
	/**
	 * 从文件选择图片的请求码
	 */
	public static final int RESULT_LOAD_IMAGE = 9775;
	/**
	 * 修改小组图标的请求码
	 */
	public static final int REQUEST_CHAGE_GROUPICON = 9776;
	/**
	 * 选择图片
	 */
	public static final int SELECT_PIC = 9777;
	/**
	 * 相机的请求码 用于替换图片
	 */
	public static final int CROP_PHOTO_REPLACE = 9778;
	/**
	 * 选择图片 用于替换图片
	 */
	public static final int SELECT_PIC_REPLACE = 9779;
	/**
	 * 从文件选择图片的请求码
	 */
	public static final int NEW_RESULT_LOAD_IMAGE = 9005;
	/**
	 * 创建小组的时候未绑定手机，关联手机请求码
	 */
	public static final int COORRELATIVE_PHONE_CREATE_GROUP = 8001;
	public static final int COORRELATIVE_PHONE_SUCCESS = 8002;
	/**
	 * /描述的字数限制
	 */
	public static final int COMMON_DESCRIPTION_LENGTH_LIMIT = 5000;

	/* 进入编辑页面的请求type */
	/**
	 * 标题的type
	 */
	public static final int TYPES_EDIT_TITLE = 10;
	/**
	 * 描述的type
	 */
	public static final int TYPES_EDIT_DESCRIPTION = 11;
	/**
	 * 标签的type
	 */
	public static final int TYPES_EDIT_LABLE = 12;
	/**
	 * 修改小组信息type
	 */
	public static final int TYPES_EDIT_GROUPINFO_LABLE = 14;
	/**
	 * 修改小组标签的type
	 */
	public static final int TYPES_EDIT_GROUPLABLE = 15;

	/**
	 * 修改昵称的投type
	 */
	public static final int TYPES_EDIT_NIACKNAME = 16;

	/**
	 * 话题列表详情请求码
	 */
	public static final int REQUSTCODE = 1001;
	/**
	 * 发话题请求码
	 */
	public static final int REQUSTCODE_POST = 2001;
	/**
	 * 详情删除结果码
	 */
	public static final int RESULTCODE = 1004;
	/**
	 * 话题置顶结果码
	 */
	public static final int RESULTCODE_SETTOP = 1005;
	/**
	 * 详情点赞评论结果码
	 */
	public static final int ZEN_COMMENT_CODE = 20015;

	/**
	 * 我的话题 详情请求码
	 */
	public static final int MYREQUSTCODE = 1003;

	/**
	 * 话题列表请求码
	 */
	public static final int TOPICELIST_REQUSTCODE = 1007;

	/**
	 * 话题列表结果码
	 */
	public static final int TOPICELIST_RESULTCODE = 1008;

	/**
	 * 热点列表请求码
	 */
	public static final int FOUNDCODE = 2000;
	/**
	 * 动态列表请求嘛
	 */
	public static final int ACTIONCODE = 2001;
	/**
	 * 小组信息修改返回码
	 */
	public static final int RESULTCODE_EDITGROUPINFO = 1006;

	/**
	 * 我的主页请求码
	 */
	public static final int MYDETAILINFO_REQUSTCODE = 1009;

	/**
	 * 我的资料结果码
	 */
	public static final int MYDETAILINFO_RESULTCODE = 1010;
	
	/**
	 * 编辑认知疗养
	 */
	public static final int EDITPERCEIVER_REQUSTCODE = 1011;

	/**
	 * 搜索提示
	 */
	public static List<String> SEARCHWD;
	/** 同步数据成功 */
	public static final int UPLOAD_DATA_TOCLOUD_SUCCESS = 10;
	/** 昵称修改请求码 */
	public static final int NICKNAMEEDITRESULT = 22;
	/** 绑定手机请求码 */
	public static final int BUNDPHONEREQUSTCODE = 23;
	/** 绑定手机结果码 */
	public static final int BUNDPHONERESULTCODE = 24;
	/** 职业选择结果码 */
	public static final int JOBSELECTRESULTCODE = 25;
	/** 职业选择结请求 */
	public static final int JOBSELECTREQUESTCODE = 26;
	/** 睡眠计划到完善资料请求码 */
	public static final int SLEEPPLANEQUESTCODE = 27;
	/** 完善资料结果码 */
	public static final int SLEEPPLANRESULTCODE = 28;

	/** 加载 */
	public static final int LOADING = 0;
	/** 没有更多 */
	public static final int NO_MORE = 1;
	/** 没有网路 */
	public static final int NO_NET = 2;
	/** 没有数据 */
	public static final int NO_DATA = 3;

	// 铃音播放页面移到此处
	/** 录制铃音界面，本地录制的铃音 */
	public static final int RINGTONE_PUBLISH_RECORD = 0;
	/** 当前发布界面 */
	public static final int RINGTONE_PUBLISH_CURRENTPUBLISH = 1;
	/** 闹钟编辑界面，当前选择的铃音 */
	public static final int RINGTONE_SELECT_CURRENT_SELECTED = 2;
	public static final int RINGTONE_SELECT_CURRENT_SELECTED_NET = 3;
	public static final int RINGTONE_MY_DOWNLOAD = 4;
	public static final int RINGTONE_GET_FROM_SERVER = 5;
	public static final int RINGTONE_GET_FROM_WEB = 6;
	/** 收到的专属铃音界面播放铃音 */
	public static final int RINGTONE_GET_EXCLUSIVE_RECEIVER = 7;
	/** 发送的专属铃音界面播放铃音 */
	public static final int RINGTONE_GET_EXCLUSIVE_SEND = 8;
	/** 给好友发送铃音编辑界面播放铃音 */
	public static final int RINGTONE_GET_SENDAUDIO_SEND = 9;
	/** 闹钟编辑界面播放当前选中的铃音 */
	public static final int RINGTONE_SELECT_CURRENT_SELECTEDEDIT = 10;
	// //从FragmentPage4Profile 移至
	// private static final int MESSAGE_COUNT_CHANGE = 3;
	// private static final int REQUEST_CODE = 4;
	// private static final int REQUEST_FALSE = 5;

	// 从选择多张图片处移至
	/** 最大图片选择次数，int类型，默认9 */
	public static final String EXTRA_SELECT_COUNT = "max_select_count";
	/** 图片选择模式，默认多选 */
	public static final String EXTRA_SELECT_MODE = "select_count_mode";
	/** 是否显示相机，默认显示 */
	public static final String EXTRA_SHOW_CAMERA = "show_camera";
	/** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合 */
	public static final String EXTRA_RESULT = "select_result";
	/** 默认选择集 */
	public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

	/** 单选 */
	public static final int MODE_SINGLE = 0;
	/** 多选 */
	public static final int MODE_MULTI = 1;

	// 多图选择adapter ImageGridAdapter
	// public static final int TYPE_CAMERA = 0;
	// public static final int TYPE_NORMAL = 1;
	/**
	 * 首页数据分享
	 */
	public static final int SHARE_FROM_SLEEP_DATA = 0;
	public static final int SHARE_FROM_COMMUNITY_EVENT = 1;

	public static final String ADD_NEW_MENBER_TO_GROUP_SUCCESS = "com.action.change.ADD_NEW_MENBER_TO_GROUP_SUCCESS";

	// 收到铃音页面
	public static final int LISTVIEW_PAGESIZE = 10;

	// 录音界面
	/**
	 * 还未开始录音
	 */
	public static final int RECORD_NOTSTART = -1;
	/**
	 * 录音中
	 */
	public static final int RECORDING = 0;
	/**
	 * 暂停录音
	 */
	public static final int RECORD_PAUSE = 1;
	/**
	 * 结束录音
	 */
	public static final int RECORD_COMPLETE = 2;
	/**
	 * 正在播放
	 */
	public static final int RECORD_PLAYING = 4;
	/**
	 * 播放暂停
	 */
	public static final int RECORD_PLAY_PAUSE = 5;
	/**
	 * 播放完毕
	 */
	public static final int RECORD_PLAY_END = 6;
	// RingtoneSelectActivity
	/** 当前状态为 已设置的铃声 */
	public static final int AUDIO_STATE_SELECTED = 0;
	/** 当前状态为 未下载 */
	public static final int AUDIO_STATE_NOT_DOWNLOAD = 1;
	/** 当前状态为 已下载 */
	public static final int AUDIO_STATE_DOWNLOADED = 2;
	/** 当前状态为 下载中 */
	public static final int AUDIO_STATE_DOWNLOADING = 3;
	/** 当前状态为 播放 */
	public static final int AUDIO_STATE_PLAYIING = 4;
	/** 当前状态为暂停 */
	public static final int AUDIO_STATE_PLAY_PAUSE = 5;
	/** 绑定设备状态改变成功 */
	public static final String BUND_BLUETOOTH_CHANGE = "com.action.xchange.BUND_DEV_CHANGE";

	/* 阿里支付PID */
	public static final String PARTNER = "2088121044959522";
	/* 商户收款账号 */
	public static final String SELLER = "sales@apporange.cn";
	/* 商户私钥，pkcs8格式 */
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANZXyLzPZZq"
			+ "obzHKrGi4pAPXZSUki1M8ejvzdJ8mZ4LuQSryKMLQhoW7F8oY/M/5opZkHdHVtRH+8w67Yp2+0Zq3zmPpE9Hb+h4LW"
			+ "DvUDinVqu3vqdQSlPbYqaPf15DSaptctAatpSsYV6r8OcnYfJHBC2RPz2UcaX/0hN8MGycLAgMBAAECgYEAgisdhUy"
			+ "Ob7YKkTp8Kby4Hn0Yt83RruLzeKUJFf3ErwbyHJqjlmv8xgoxEFLMdaEs1BhI473dEOi7wx6ZbSMNNsPoc0KzWddrr"
			+ "++eBKQQBuKKia/98Kj7VXnAs5tR6/iEfYeeTRMiyUHLb2KCVIhraEHPlzOxV2NeGZX17m1EdMECQQDymAVvZdvuXg3"
			+ "pF6QI71FTqZdErTvMR6l4pXowM/yXgrA6QlRohTu0E7SIpcOVORoJbz5wcdVVMeGlFHlmhDC9AkEA4jAYuvzd7mW6b"
			+ "6hAgiztEwCxoMzw8UTSXAOqTa4h4FaQcLzLN7H+fZqOxzyazWAS0889DwLs7+J552VBCkXnZwJAOZjucWx1szqRgu4"
			+ "h/B0V6g8x4xReRjodeo+PWzJ8nlxhCvQEpmw7qJ2lF2yD60ILdIH9CSV9ySR2toMN79Ei4QJAOiMcqCtIq/cP1ZVMh"
			+ "2kewoyiG2+x2oHTA/2ZL4A7itsVIzUTksw4WnZ4jnNgYrt3+2NAEXSyC/KNXNGkxwwyrwJBAL9HSm8YJ03+0p9rlcf"
			+ "tek9XGpfJAFT0fjMSQKrvWdXg7lJ1kM7xwUQTFpJFTGxXavmPISCW2WW1vGlVoD9YY2I=";
	/* 阿里支付成功回调页面 */
	public static final String ALI_NOTIFA_URL = "http://apporange.cn/orangesleep_debug/notify/notify.php";
//	public static final String ALI_NOTIFA_URL = "http://apporange.cn/orangesleep/notify/notify.php";
	/* 微信支付成功回调页面 */
	public static final String WX_NOTIFA_URL = "http://apporange.cn/orangesleep_debug/wxnotify/notify.php";
//	public static final String WX_NOTIFA_URL = "http://apporange.cn/orangesleep/wxnotify/notify.php";

	/** 硬件解析默认调试参数 */
	public static final byte[] profile = { 0x68,0x65,0x61,0x64,(byte) 0xb0,0x00,0x00,0x00,  0x32,0x30,0x31,0x36,0x30,0x31,0x32,0x37,  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,  0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,  
		0x00,0x00,0x00,0x00,0x70,0x61,0x72,0x61,  0x08,0x07,0x00,0x00,0x08,0x07,0x00,0x00,  0x4e,0x01,0x00,0x00,(byte) 0x8c,0x0a,0x00,0x00,  (byte) 0x84,0x03,0x00,0x00,0x00,0x00,0x00,0x00,  
		0x00,0x00,(byte) 0xf0,0x3f,(byte) 0x8c,0x0a,0x00,0x00,  0x0a,0x00,0x00,0x00,0x00,0x00,0x00,0x00,  0x00,0x00,(byte) 0xe0,0x3f,0x2c,0x01,0x00,0x00,  0x3c,0x00,0x00,0x00,0x00,0x00,0x00,0x00,  
		0x00,0x00,0x00,0x40,0x58,0x02,0x00,0x00,  (byte) 0xb4,0x00,0x00,0x00,0x04,0x00,0x00,0x00,  0x05,0x00,0x00,0x00,0x0f,0x00,0x00,0x00,  0x58,0x02,0x00,0x00,0x1e,0x00,0x00,0x00,  
		(byte) 0x9a,(byte) 0x99,(byte) 0x99,(byte) 0x99,(byte) 0x99,(byte) 0x99,(byte) 0xe9,0x3f,  0x4b,0x00,0x00,0x00,0x32,0x00,0x00,0x00,  0x02,0x00,0x00,0x00,0x1e,0x00,0x00,0x00,  0x14,0x00,0x00,0x00,0x03,0x00,0x00,0x00,  
		0x19,0x00,0x00,0x00,0x05,0x00,0x00,0x00,  (byte) 0xcd,(byte) 0xcc,(byte) 0xcc,(byte) 0xcc,(byte) 0xcc,(byte) 0xcc,(byte) 0xec,0x3f,  0x33,0x33,0x33,0x33,0x33,0x33,(byte) 0xeb,0x3f,  0x66,0x66,0x66,0x66,0x66,0x66,(byte) 0xe6,0x3f,  
		0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,  (byte) 0xcd,(byte) 0xcc,(byte) 0xcc,(byte) 0xcc,(byte) 0xcc,(byte) 0xcc,(byte) 0xec,0x3f,  0x00,0x00,0x00,0x00,0x00,0x00,0x08,0x40 };
	
	public static String texteHint = "一、做好睡眠日志 <br/>"
			+ "睡眠日志的记录是任何睡眠改善方案的基础，从医学的角度来说它包含了一个人睡眠情况的全方位信息，也是用以评估睡眠质量、制定个性化睡眠计划、评估改善效果的重要参考。"
			+ "香橙希望能够依此帮你找到最有效的睡眠改善方法，并提供衡量睡眠情况和趋势的重要病历数据。"
			+ "所以坚持使用香橙认真记做好睡眠日志非常重要。当然，睡眠日志不需要绝对的精准，但是每天起床后尽快完成前一晚的记录，将极大地提高信息的准确度。<br /><br />"
			+ "二、关注睡眠效率"
			+ "<br/>睡眠效率是用以衡量睡眠质量的重要指标，其计算方法是：睡眠效率&nbsp;=（睡着时间/躺在床上的时间）x&nbsp;100%。"
			+ "睡眠效率越高，说明睡眠越好。通常，睡眠较好的人，睡眠效率通常能达到90%-100%，他们入睡快、半夜醒来时间短。相反，睡眠较差的人，睡眠效率可能只有50%，他们入睡慢、半夜容易醒来。"
			+ "睡眠效率至少应该达到85%才能算较好，并且应该以睡眠效率达到90%作为努力的目标。&nbsp;";
		
	
	public static String setRemindHint = "一、设置固的起床时间<br />"
			+ "为了让睡眠限制法发挥最好的作用，首先应该固定一个起床时间，以7天为单位，这7天严格在同一个时间起床。这样能够培养稳定的睡眠状态，从而形成良好的睡眠习惯。<br /><br />"
			+ "二、选择上床上时间<br />"
			+ "至于上床时间，应该选择躺上床就能马上睡着的那个时间点，既不要过早上床，也不要拖延入睡。<br /><br />"
			+ "三、限制睡眠时间<br />"
			+ "用你设定的目标起床时间减去你平时睡眠的平均时间，可以得到入睡的“门槛时间”，从门槛时间到目标起床时间的这个时间段被称作“睡眠窗口”。在“睡眠窗口”以外的时间，请尽量保持清醒，避免睡觉。<br /><br />"
			+ "四、合理安排睡觉时长<br />如果在一周内你的睡眠效率达都到了90%，那么下一周可以增加15分钟睡眠时间，即适当延长“睡眠窗口”。之后可以慢慢增长你的睡眠时间，直到睡眠需求完全得到满足为止。&nbsp;";
}
