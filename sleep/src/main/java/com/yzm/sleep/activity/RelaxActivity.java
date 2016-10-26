package com.yzm.sleep.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.RelaxAdapter;
import com.yzm.sleep.adapter.RelaxAdapter.RelaxCallBack;
import com.yzm.sleep.bean.LocalRingsaBean;
import com.yzm.sleep.bean.RingtoneBean;
import com.yzm.sleep.model.PlayerMsg;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceRelaxRingsCallback;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.MyPlayerUtil;
import com.yzm.sleep.utils.XiangchengMallProcClass;

public class RelaxActivity extends BaseActivity {

	private ListView mlistView;
	private RelaxAdapter mAdapter;
	private List<RingtoneBean> mlylist;
	//==================铃音播放====================
	private boolean isStartPlay = false;
	private boolean isPlaying = true;
	private String playingPath = "";
	private String putplayPath = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relax);
		
		initviews();
		Intent intent = new Intent();
		intent.setAction("com.yzm.media.MUSIC_SERVICE");
		startService(intent);
		registerReceiver();
	}

	private void initviews() {
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("放松身心");
		mlistView = (ListView) findViewById(R.id.lv_fr_music);
		mAdapter = new RelaxAdapter(this, mCallBack);
		mlistView.setAdapter(mAdapter);
		
	}

	@Override
	protected void onDestroy() {
		try {
			mAdapter.cancleDownLoad();
			leaveThisPage();
			unregisterReceiver(mReceiver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back){
			AppManager.getAppManager().finishActivity();
		}
	}
	/**
	 * 数据处理，只显示网络获取的音频文件，如果本地存在则显示为一下载状态，否则
	 * @param mlist
	 */
	private void setDates(List<RingtoneBean> mlist) {
		List<RingtoneBean> mTempList = new ArrayList<RingtoneBean>();
		List<LocalRingsaBean> getDownloadAudioInfo = GetRingsFile(MyApplication.XIANGCHENG_RINGS_PATH);//AlarmUtils.getDownloadAudio(getActivity(), MyApplication.XIANGCHENG_AUDIO_PATH);
		for(RingtoneBean mRingtoneBean :mlist ){
			for(LocalRingsaBean mLocalRingsaBean : getDownloadAudioInfo){
				if(mRingtoneBean.getName().equals(mLocalRingsaBean.getName())){
					mRingtoneBean.setState(Constant.AUDIO_STATE_DOWNLOADED);
					mRingtoneBean.setPath(mLocalRingsaBean.getPath());
					mTempList.add(mRingtoneBean);
				}
			}
		}
		mlist.removeAll(mTempList);
		for(RingtoneBean mRingtoneBean :mlist ){
			mRingtoneBean.setState(Constant.AUDIO_STATE_NOT_DOWNLOAD);
		}
		mlist.addAll(mTempList);
		mAdapter.setData(mlist);
	}
	
	private void getMoredata(){
		if(this.mlylist != null){
			mlylist.clear();
		}
//		new XiangchengProcClass(this).moreRingtoneList(new InterfaceMoreRingtoneListCallback() {
		new XiangchengMallProcClass(this).getRelaxRingtoneList(new InterfaceRelaxRingsCallback() {
			
			@Override
			public void onSuccess(int icode, List<RingtoneBean> lylist) {
				mlylist = lylist;
//				mlylist.get(0).setDownloadurl("http://5.595818.com/2015/ring/000/139/e45571eea7603f59f38c763bde87d1ee.mp3");
				setDates(mlylist);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				
				
			}
		});
	}
	
	public List<LocalRingsaBean> GetRingsFile(String fileAbsolutePath) {
		List<LocalRingsaBean> ringsList = new ArrayList<LocalRingsaBean>();
		
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if(null!=subFile){
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
            	
                String filename = subFile[iFileLength].getName();
                // 判断是否为aac结尾
                if (filename.trim().toLowerCase().endsWith(".aac")) {
                	LocalRingsaBean mLocalRingsaBean = new LocalRingsaBean();
                	mLocalRingsaBean.setName(filename.substring(0, filename.length()-4));
                	mLocalRingsaBean.setPath(subFile[iFileLength].getAbsolutePath());
                	ringsList.add(mLocalRingsaBean);
                }
            }
        }
        }
        return ringsList;
    }
	
	private RelaxCallBack mCallBack = new RelaxAdapter.RelaxCallBack() {
		
		@Override
		public void RelaxStateCallBack(RingtoneBean mRingsBean) {
			int state = -1;
			switch (mRingsBean.getState()) {
			case Constant.AUDIO_STATE_NOT_DOWNLOAD://未下载
				state = Constant.AUDIO_STATE_DOWNLOADED;
				break;
			case Constant.AUDIO_STATE_DOWNLOADED://已下载
				state = Constant.AUDIO_STATE_PLAYIING;
				putplayPath = mRingsBean.getPath();
				playPauseMethod();
				break;
			case Constant.AUDIO_STATE_DOWNLOADING://下载中
				
				break;
			case Constant.AUDIO_STATE_PLAYIING://播放
				state = Constant.AUDIO_STATE_PLAY_PAUSE;
				putplayPath = mRingsBean.getPath();
				playPauseMethod();
				break;
			case Constant.AUDIO_STATE_PLAY_PAUSE://暂停
				state = Constant.AUDIO_STATE_PLAYIING;
				putplayPath = mRingsBean.getPath();
				playPauseMethod();
				break;
			default:
				break;
			}
			if(-1 != state){
				List<RingtoneBean> mlist = mAdapter.getData();
				for(int r = 0 ; r < mlist.size() ; r++){
					if(mlist.get(r).getKey().equals(mRingsBean.getKey())){
						mlist.get(r).setState(state);
					}else if(state == Constant.AUDIO_STATE_PLAYIING && (mlist.get(r).getState() == Constant.AUDIO_STATE_PLAY_PAUSE || mlist.get(r).getState() == Constant.AUDIO_STATE_PLAYIING)){
						mlist.get(r).setState(Constant.AUDIO_STATE_DOWNLOADED);
					}
				}
				mAdapter.setData(mlist);
			}
		}
	};
	
	/** 播放暂停*/
	private void playPauseMethod() {
		if (isStartPlay) {
			Intent intent = new Intent();
			if(playingPath.equals(putplayPath)){
//				playingPath = "";
				if (isPlaying) {
					intent.setAction(MyPlayerUtil.AUDIO);
					intent.putExtra("MSG", PlayerMsg.PAUSE_MSG);
					sendBroadcast(intent);
					isPlaying = false;
				}else{
					intent.setAction(MyPlayerUtil.AUDIO);
					intent.putExtra("MSG", PlayerMsg.CONTINUE_MSG);
					sendBroadcast(intent);
					isPlaying = true;
				}
			}else{
				playingPath = putplayPath;
			if (isPlaying) {
				intent.setAction(MyPlayerUtil.AUDIO);
				intent.putExtra("MSG", PlayerMsg.PLAYING_END);
				sendBroadcast(intent);
				isPlaying = false;
			}
			startPlayAudio();
			}
		}else {
			playingPath = putplayPath;
			startPlayAudio();
		}
	}
	
	/**开始播放铃声*/
	private void startPlayAudio() {
		isStartPlay = true;
		isPlaying = true;
		Intent intent = new Intent();
		intent.setAction(MyPlayerUtil.AUDIO);
		intent.putExtra("url", putplayPath);
		intent.putExtra("MSG", PlayerMsg.PLAY_MSG);
		//启动服务
		sendBroadcast(intent);
	}
	/**
	 *离开当前页面  停止播放任务。停止服务
	 */
	private void leaveThisPage(){
		if(isStartPlay){
			Intent intent = new Intent();
			playingPath = "";
			intent.setAction(MyPlayerUtil.AUDIO);
			intent.putExtra("MSG", PlayerMsg.STOP_MSG);
			sendBroadcast(intent);
			isPlaying = false;
		}
		Intent intent = new Intent();
		intent.setAction("com.yzm.media.MUSIC_SERVICE");
		stopService(intent);
	}
	
	//
	private void registerReceiver() {
		IntentFilter inFilter = new IntentFilter();
		inFilter.addAction(MyPlayerUtil.AUDIO_PLAY_END);
		this.registerReceiver(mReceiver, inFilter);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(MyPlayerUtil.AUDIO_PLAY_END)){
				List<RingtoneBean> list = mAdapter.getData();
				if(list != null){
					String url = intent.getStringExtra("url");
//					LogUtil.d("chen", "url = "+url);
					for (int i = 0; i < list.size(); i++) {
						if(list.get(i).getPath() != null && list.get(i).getPath().equals(intent.getStringExtra("url"))){
							list.get(i).setState(Constant.AUDIO_STATE_DOWNLOADED);
							playingPath = "";
							break;
						}
					}
					mAdapter.setData(list);
				}
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		getMoredata();
		
	}
	
	
	
}
