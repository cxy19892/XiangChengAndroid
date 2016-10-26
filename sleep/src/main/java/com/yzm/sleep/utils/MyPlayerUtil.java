package com.yzm.sleep.utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yzm.sleep.model.PlayRingInfo;
import com.yzm.sleep.model.PlayerMsg;

public class MyPlayerUtil {
	Context context;
	/**香橙存储下载铃音的地址*/
	private String ringpath;
	private static ArrayList<PlayRingInfo> ringInfos = new ArrayList<PlayRingInfo>();
	private boolean isPlaying; // 正在播放
	private boolean isPause; // 暂停
	private TextView tv_currenttime;
	private SeekBar sb_progress;
	private Button btn_run;
	
	// 选择的哪个
	private int index = 0;
	private String title;// 歌曲名字
	private String artist;// 歌手名字
	private int currentTime = 0; // 当前歌曲播放时间
	private String duration; // 歌曲长度
	
	
	public static final String UPDATE_ACTION = "com.pl.action.UPDATE_ACTION"; // 更新动作
	public static final String CTL_ACTION = "com.pl.action.CTL_ACTION"; // 控制动作
	public static final String MUSIC_CURRENT = "com.pl.action.MUSIC_CURRENT"; // 音乐当前时间改变动作
	public static final String MUSIC_DURATION = "com.pl.action.MUSIC_DURATION";// 音乐播放长度改变动作
	public static final String MUSIC_PLAYING = "com.pl.action.MUSIC_PLAYING"; // 音乐正在播放动作
	public static final String REPEAT_ACTION = "com.pl.action.REPEAT_ACTION"; // 音乐重复播放动作
	public static final String SHUFFLE_ACTION = "com.pl.action.SHUFFLE_ACTION";// 音乐随机播放动作
	
	
	
	public static final String AUDIO = "com.pl.action.AUDIO";
	public static final String AUDIO_START = "com.pl.action.AUDIO_START";
	public static final String AUDIO_DURATION = "com.pl.action.AUDIO_DURATION";
	public static final String AUDIO_PLAYING = "com.pl.action.AUDIO_PLAYING";
	public static final String AUDIO_PAUSE = "com.pl.action.AUDIO_PAUSE";
	public static final String AUDIO_CONTINUE = "com.pl.action.AUDIO_CONTINUE";
	public static final String AUDIO_STOP = "com.pl.action.AUDIO_STOP";
	
	public static final String AUDIO_PLAY_END = "com.pl.action.AUDIO_PLAY_END";
	
	
	


	

	/**
	 * 播放音乐
	 * @param currentposition
	 */
	public static void play(Context context,String url) {
		repeat_none(context);
		Intent intent = new Intent();
		intent.setAction("com.yzm.media.MUSIC_SERVICE");
		intent.putExtra("url", url);
		intent.putExtra("MSG", PlayerMsg.PLAY_MSG);
		//启动服务
		context.startService(intent);
		
	}
	/**
	 * 停止音乐
	 * @param currentposition
	 */
	public static void stop(Context context) {
		Intent intent = new Intent();
		intent.setAction("com.yzm.media.MUSIC_SERVICE");
		intent.putExtra("MSG", PlayerMsg.STOP_MSG);
		//启动服务
		context.startService(intent);
		
	}
	/**
	 * 不重复播放
	 * */
	public static void repeat_none(Context context) {
		Intent intent = new Intent(CTL_ACTION);
		intent.putExtra("control", 3);
		context.sendBroadcast(intent);
	}
	

	/**
	 * 设置进度条方法
	 * */
	public void seekbarchange(Context context,int progress,String url,int currentPosition) {
		Intent intent = new Intent();
		intent.setAction("com.xcs.media.MUSIC_SERVICE");
		intent.putExtra("url", url);
		intent.putExtra("currentposition", currentPosition);
		intent.putExtra("MSG", PlayerMsg.PROGRESS_CHANGE);
		intent.putExtra("progress", progress);
		context.startService(intent);
	}
	
	/**
	 * 格式化时间，将毫秒转换为分:秒格式
	 * 
	 * @param time
	 * @return
	 * 
	 * */
	public static String formatMillisecondTime(long duration) {
		// 获取分钟
		String min = duration / (1000 * 60) + "";
		// 获取秒
		String sec = duration % (1000 * 60) + "";
		// 分钟只有一位时
		if (min.length() < 2) {
			min = "0" + duration / (1000 * 60) + "";
		} else {
			min = duration / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (duration % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (duration % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (duration % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (duration % (1000 * 60)) + "";
		}
		// 返回格式是分钟+秒截取两位
		return min + ":" + sec.trim().substring(0, 2);
	}
}
