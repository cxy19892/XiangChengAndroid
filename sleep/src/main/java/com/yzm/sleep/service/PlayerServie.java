package com.yzm.sleep.service;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;

import com.yzm.sleep.model.PlayRingInfo;
import com.yzm.sleep.model.PlayerMsg;
import com.yzm.sleep.utils.MyPlayerUtil;

public class PlayerServie extends Service {
	private MediaPlayer mediaPlayer; 	// 媒体播放器对象
	private int currentTime;  			// 当前播放进度
	private List<PlayRingInfo> ringInfos; 	// 存放RingInfo对象的集合
	private int current = 0; 		// 记录当前正在播放的音乐
	private String path;			// 音乐文件路径
	private int duration;			//播放长度
	private boolean isPause;		// 暂停状态
	private int msg;				//播放信息
	private MyReceiver myReceiver;	//自定义广播接收器
	// 服务要发送的一些Action
	public static final String UPDATE_ACTION = "com.pl.action.UPDATE_ACTION"; 
	public static final String CTL_ACTION = "com.pl.action.CTL_ACTION"; 
	public static final String MUSIC_CURRENT = "com.pl.action.MUSIC_CURRENT"; 
	public static final String MUSIC_DURATION = "com.pl.action.MUSIC_DURATION";
	public static final String SHOW_LRC = "com.pl.action.SHOW_LRC"; 

	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (!playingEnd) {
				if (msg.what == 1) {
					if (mediaPlayer != null) {
						currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
						Intent intent = new Intent();
						intent.setAction(MUSIC_CURRENT);
						intent.putExtra("currentTime", currentTime);
						sendBroadcast(intent);  // 发送广播
						handler.sendEmptyMessageDelayed(1, 100);
					}
				}
			}
		}

	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setLooping(false);
//		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
//
//		@Override
//		public void onCompletion(MediaPlayer mp) {
//			mediaPlayer.stop();
//			Intent intent = new Intent();
//			intent.setAction(MyPlayerUtil.UPDATE_ACTION);
//			intent.putExtra("currentTime", currentTime);
//			sendBroadcast(intent);  // 发送广播
//			playingEnd = true;
//		}
//	});
		
		/*注册广播*/
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyPlayerUtil.AUDIO);
		registerReceiver(myReceiver, filter);
	}
	
	/**
	 * 播放音乐
	 * @param currentTime
	 */
	public void play(int currentTime) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare(); 
			mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					remindPlayingEnd(path);
					playingEnd = true;
				}
			});
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 暂停音乐
	 */
	private void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}
	/**
	 * 继续播放音乐
	 */
	private void resume() {
		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}


	/**
	 * 停止播放
	 */
	private void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
//			try {
//				mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
	}
	@Override
	public void onDestroy() {
		try {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			mediaPlayer = null;
			unregisterReceiver(myReceiver);
		} catch (Exception e) {
		}
	}
	private final class PreparedListener implements OnPreparedListener{
		private int currentTime;

		public PreparedListener(int currentTime) {
			this.currentTime = currentTime;
		}
		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start(); 
			if (currentTime > 0) { 
				mediaPlayer.seekTo(currentTime);
			}
			Intent intent = new Intent();
			intent.setAction(MUSIC_DURATION);
			duration = mediaPlayer.getDuration();
			intent.putExtra("duration", duration);	//通过Intent来传递歌曲的总长度
			sendBroadcast(intent);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		
		//发广播去更新activity上的时间 
		//1  获取当前音乐播放进度
		//2播放音乐
		
		
		
		super.onStart(intent, startId);
	}
	private boolean playingEnd = false;
	public class MyReceiver extends BroadcastReceiver {


		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MyPlayerUtil.AUDIO)) {
				int msg = intent.getIntExtra("MSG", -1);
				switch (msg) {
				case PlayerMsg.PLAY_MSG://开始播放
					path = intent.getStringExtra("url");//获取要播放的音频文件的路径
					play(0);
					playingEnd = false;
					break;
				case PlayerMsg.PAUSE_MSG://暂停播放
					pause();
					break;
				case PlayerMsg.CONTINUE_MSG://继续播放
					resume();
					break;
				case PlayerMsg.STOP_MSG://停止播放
					stop();
					playingEnd = true;
//					remindPlayingEnd(path);
					break;
				case PlayerMsg.PLAYING_END://播放完成
					playingEnd = true;
//					remindPlayingEnd(path);
					break;
				case PlayerMsg.DELET_MSG://播放文件被删除，停止播放
					if(intent.getStringExtra("url").equals(path)){
						stop();
						playingEnd = true;
					}
					break;
				}
			}
		}
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void remindPlayingEnd(String URL){
		Intent intent = new Intent();
		intent.setAction(MyPlayerUtil.AUDIO_PLAY_END);
		intent.putExtra("url", URL);
		sendBroadcast(intent);  // 发送广播
	}
}