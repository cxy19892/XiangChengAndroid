package com.yzm.sleep.activity;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.MusicBean;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceMusicListCallBack;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.MyRoundProgressBar;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" }) 
public class PlayMusicActivity extends BaseActivity {

	private ListView mListView;
	private MusicListAdapter mAdapter;
	private ImageButton btnPlay, btnLoops;
	private Button btnTime;
	private MyRoundProgressBar mProgressBar;
	private VideoView mVideoView;
	private String path = "";
	/*单曲循环*/
	private boolean SINGLE_LOOPS = false;
	/*默认播放时间90min*/
	private long PLAY_TIME = 60 * 60 * 1000;
	
	private Timer mTimer;
	private ProgressTimeTask mTask;
	private View noNetView;
	private RelativeLayout relaMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vitamio.isInitialized(this);
		setContentView(R.layout.activity_playmusic);
		findViewById(R.id.back).setOnClickListener(this);
		initView();
	}

	private void initView() {
		relaMusic = (RelativeLayout) findViewById(R.id.music_control);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) relaMusic.getLayoutParams();
		lp.width = getScreenWidth();
		lp.height = getScreenWidth() * 977/1125;
		relaMusic.setLayoutParams(lp);
		setImmerseLayout(relaMusic);
		mListView = (ListView) findViewById(R.id.play_listview);
		mProgressBar = (MyRoundProgressBar) findViewById(R.id.play_control_bar);
		mProgressBar.setCricleColor(getResources().getColor(R.color.transparent));
		mProgressBar.setCricleProgressColor(getResources().getColor(R.color.orange));
		mProgressBar.setMax(100);
		btnPlay = (ImageButton) findViewById(R.id.play_control_btn);
		btnLoops = (ImageButton) findViewById(R.id.play_mode);
		btnTime = (Button) findViewById(R.id.play_time);
		String str = PLAY_TIME/(60 * 1000) + "<br>min";
		btnTime.setText(Html.fromHtml(str));
		btnPlay.setOnClickListener(this);
		btnLoops.setOnClickListener(this);
		btnTime.setOnClickListener(this);
		mAdapter = new MusicListAdapter(getLayoutInflater());
		mListView.setAdapter(mAdapter);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				toastMsg("播放发生错误!");
				String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/vitomio/";
				try {
					File f = new File(path);
					if(!f.exists())
						f.mkdirs();
					String time = new SimpleDateFormat("MM-dd HH:mm").format(new Date());
					String str =  time + " /发生错误：what=" + what + ", extra=" + extra + "。倒计时还剩：" + mTask.getCountDown();
					
					FileOutputStream fos = new FileOutputStream(new File(path, time + ".txt"));
					fos.write(str.getBytes());
					fos.flush();
					fos.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(nowPrepare)
					nowPrepare = false;
				btnPlay.setImageResource(R.drawable.play_icon);
				return true;
			}
		});
		mVideoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mProgressBar.setProgress(0);
				if(SINGLE_LOOPS) {
					playfunction(path);
				} else {
					List<MusicBean> datas = mAdapter.getData();
					if(datas.size() > 0) {
						int selectPos = mAdapter.getSelectItemPosi();
						if( ++selectPos >= datas.size())
							selectPos = 0;
						mAdapter.setSelectItemPosi(selectPos);
						mAdapter.notifyDataSetChanged();
						path = "";
						playfunction(path);
					}
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int realPoi = arg2 - mListView.getHeaderViewsCount();
				mAdapter.setSelectItemPosi(realPoi);
				mAdapter.notifyDataSetChanged();
				path = mAdapter.getData().get(realPoi).getUrl();
				playfunction(path);
				startTimer();
			}
		});
		getListForService();
	}
	
	void getListForService(){
		if(!Util.checkNetWork(this)){
			if(mAdapter.getData().size() == 0){
				if(noNetView == null)
					noNetView = getLayoutInflater().inflate(R.layout.layout_no_net, null);
				try {
					mListView.removeFooterView(noNetView);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mListView.addFooterView(noNetView, null, false);
				noNetView.findViewById(R.id.rl_p).setOnClickListener(this);
			}
		}else{
			try {
				mListView.removeFooterView(noNetView);
			} catch (Exception e) {
				e.printStackTrace();
			}
			getMusicList();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.rl_p:
			getListForService();
			break;

		case R.id.play_mode:
			SINGLE_LOOPS = !SINGLE_LOOPS;
			if(SINGLE_LOOPS)
				btnLoops.setImageResource(R.drawable.single_play_icon);
			else
				btnLoops.setImageResource(R.drawable.loop_play_icon);
			break;

		case R.id.play_control_btn:
			if(mVideoView != null && mVideoView.isPlaying()){
				mVideoView.pause();
				mVideoView.stopPlayback();
				mProgressBar.setProgress(0);
				btnPlay.setImageResource(R.drawable.play_icon);
			}else{
				if(mVideoView != null){
					startTimer();
					playfunction(path);
				}
			}
			break;
			
		case R.id.play_time:
			PLAY_TIME -= 30 * 60 * 1000;
			if(PLAY_TIME == 0)
				PLAY_TIME = 15 * 60 * 1000;
			else if(PLAY_TIME < 0)
				PLAY_TIME = 90 * 60 * 1000;
			String str = PLAY_TIME/(60 * 1000) + "<br>min";
			btnTime.setText(Html.fromHtml(str));
			startTimer();
			break;
			
		default:
			break;
		}
	}
	
	private boolean nowPrepare = false;
	
	/**
	 * 开始播放
	 * @param path
	 */
	synchronized void playfunction(final String path) {
		if(mVideoView != null && mVideoView.isPlaying()){
			mVideoView.pause();
			mVideoView.stopPlayback();
			mProgressBar.setProgress(0);
		}
		
		
		if (TextUtils.isEmpty(path)) {
			List<MusicBean> datas = mAdapter.getData();
			if(datas.size() > 0) {
				this.path = datas.get(mAdapter.getSelectItemPosi()).getUrl();
				if(!TextUtils.isEmpty(this.path))
					playfunction(this.path);
			}else
				Toast.makeText(
					this,
					"没有可播放的音乐",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			if(!nowPrepare){
				File file = startDownloadAudioFile(path);
				
				nowPrepare = true;
				if(file != null){
					mVideoView.setVideoPath(file.getAbsolutePath());
				}else{
					Uri uri = Uri.parse(path);
					mVideoView.setVideoURI(uri);
				}
				mVideoView.requestFocus();
				
				mVideoView
						.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
							@Override
							public void onPrepared(MediaPlayer mediaPlayer) {
								// optional need Vitamio 4.0
								mediaPlayer.setPlaybackSpeed(1.0f);
								btnPlay.setImageResource(R.drawable.pause_icon);
								nowPrepare = false;

								try {
									List<MusicBean> datas = mAdapter.getData();
									for (int i = 0; i < datas.size(); i++) {
										if (datas.get(i).getUrl().equals(path)) {
											mAdapter.setSelectItemPosi(i);
											mAdapter.notifyDataSetChanged();
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
			}
			
		}
	}

	/**
	 * 开始检查是否已经下载
	 * @param path       下载路径
	 */
	File startDownloadAudioFile(String path){
		File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/yzm/Audio");
		if(!f.exists())
			f.mkdirs();
		String name = "";
		List<MusicBean> list = mAdapter.getData();
		for (int i = 0; i < list.size(); i++) {
			String url = list.get(i).getUrl();
			String title = list.get(i).getTitle();
			if(path.equals(url)){
//				name = title + ".aac";
				name = title;
				path = list.get(i).getReal_url();
			}
		}
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++) {
			if(name.equals(files[i].getName())){ 
				return files[i];
			}
		}
		downloadAudio(path, new File(f, name));
		return null;
	}
	
	/**
	 * 下载视频，并且将下载好的视频复制到指定文件夹中
	 * @param path
	 * @param targetFile
	 */
	void downloadAudio(String path, final File targetFile){
		new AsyncHttpClient().get(path, new FileAsyncHttpResponseHandler(this) {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, File file) {
				// TODO Auto-generated method stub
				FileUtil.copyFile(file, targetFile);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	/**
	 * 开始/重置 倒计时
	 */
	void startTimer(){
		if(mTimer == null);
			mTimer = new Timer();
		if(mTask != null){
			mTask.cancel();
		}
		mTask = new ProgressTimeTask();
		mTask.refresTime();
		mTimer.schedule(mTask, 0, 1000);
	}
	
	class ProgressTimeTask extends TimerTask{
		
		private int time;
		
		public void refresTime(){
			time = (int) (PLAY_TIME / 1000);
		}
		
		public int getCountDown(){
			return time;
		}
		
		@Override
		public void run() {
			if(mVideoView != null && mVideoView.isPlaying()){
				double progress = (mVideoView.getCurrentPosition() * 100) / mVideoView.getDuration();
				if(progress > 100)
					progress = 100;
				mProgressBar.setProgress((int)progress);
			}
			if(time-- <= 0){
				mHandler.sendEmptyMessage(0);
				this.cancel();
			}
		}
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				if(mVideoView != null && mVideoView.isPlaying()){
					mVideoView.pause();
					mVideoView.stopPlayback();
					mProgressBar.setProgress(0);
					btnPlay.setImageResource(R.drawable.play_icon);
				}
			}
		}
	};
	
	@Override
	protected void onResume() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mVideoView != null && mVideoView.isPlaying())
					startTimer();
			}
		}, 1000);
		
		super.onResume();
	}

	class MusicListAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		private List<MusicBean> datas;
		private DisplayImageOptions option;
		
		public MusicListAdapter(LayoutInflater inflater){
			this.inflater = inflater;
			if(option == null)
				option = new DisplayImageOptions.Builder()  
				.showImageOnLoading(new ColorDrawable(getResources().getColor(R.color.transparent)))
				.showImageForEmptyUri(new ColorDrawable(getResources().getColor(R.color.transparent)))
				.showImageOnFail(new ColorDrawable(getResources().getColor(R.color.transparent)))
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true)
				.build();
		}
		
		public void setData(List<MusicBean> datas){
			this.datas = datas;
			this.notifyDataSetChanged();
		}
		
		public List<MusicBean> getData(){
			return datas == null ? new ArrayList<MusicBean>() : datas;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas == null ? 0 : datas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		private int currentPosi;
		
		public void setSelectItemPosi(int currentPosi){
			this.currentPosi = currentPosi;
		}
		
		public int getSelectItemPosi(){
			return this.currentPosi;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.play_music_item, null, false);
				holder = new Holder();
				holder.ivIcon = convertView.findViewById(R.id.music_tag);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.music_title);
				holder.tvSummary = (TextView) convertView.findViewById(R.id.music_summary);
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}
			if(currentPosi == position){
				holder.ivIcon.setVisibility(View.VISIBLE);
				holder.tvTitle.setTextColor(getResources().getColor(R.color.orange));
			} else {
				holder.ivIcon.setVisibility(View.INVISIBLE);
				holder.tvTitle.setTextColor(getResources().getColor(R.color.onet_color));
			}
			holder.tvTitle.setText(datas.get(position).getTitle());
			holder.tvSummary.setText(datas.get(position).getIntro());
			return convertView;
		}
		
		class Holder{
			View ivIcon;
			TextView tvTitle, tvSummary;
		}
		
	}
	
	/**
	 * 获取助眠音乐列表
	 */
	void getMusicList(){
		String userId = PreManager.instance().getUserId(this.getApplicationContext());
		new XiangchengMallProcClass(this).getMusicList(userId, "1", "50", new InterfaceMusicListCallBack(){

			@Override
			public void onSuccess(String icode, List<MusicBean> datas,
					int totalpage) {
				// TODO Auto-generated method stub
				if(datas.size() > 0) {
					mAdapter.setSelectItemPosi(0);
				}
				mAdapter.setData(datas);
			}

			@Override
			public void onError(String icode, String strErrMsg) {
				// TODO Auto-generated method stub
				toastMsg(strErrMsg);
			}
			
		});
	}

	@Override
	protected void onDestroy() {
		if(mTask != null)
			mTask.cancel();
		super.onDestroy();
	}

	
	
}
