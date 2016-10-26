package com.yzm.sleep.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.R.array;
import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.pillow.BundPillowInfoActivity;
import com.yzm.sleep.adapter.MyLocalRingsAdapter;
import com.yzm.sleep.adapter.MyLocalRingsAdapter.ItemClickInterface;
import com.yzm.sleep.background.AlarmDBOperate;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.AlarmService.ListAlarmTime;
import com.yzm.sleep.bean.LocalRingsaBean;
import com.yzm.sleep.dao.AudioDAO;
import com.yzm.sleep.model.DownloadAudioInfo;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.PlayerMsg;
import com.yzm.sleep.model.RingtoneInfo;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.utils.AlarmUtils;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.MyPlayerUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.Util;

public class FragmentRingsMine extends Fragment implements ItemClickInterface{

	Context context;
	/** 下载到本地的铃声路径 */
	private String ringtonePath;
	/** 本地铃声 */
	private ArrayList<RingtoneInfo> ringInfos = new ArrayList<RingtoneInfo>();
	private ListView mlistv;
	private String selectNameString = "";
	private MyLocalRingsAdapter myAdapter;
	//==================铃音播放====================
	private boolean isStartPlay = false;
	private boolean isPlaying = true;
	private RingtoneInfo mRingtoneInfo;
	private PlayerReceiver playerReceiver;
	private MyAlertDialog mRingsDialog;
	private String playingPath = "";
	
	@Override
	public void onAttach(Activity activity) {
		this.context = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_rings_mine, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		ringtonePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Audio/xiangcheng/download";
		if (!FileUtil.isFileExist(ringtonePath)) {
			FileUtil.makeRootDirectory(ringtonePath);
		}
		registerReceiver();
		Intent intent = new Intent();
		intent.setAction("com.yzm.media.MUSIC_SERVICE");
		context.startService(intent);
		
		initViews(view);
		getOrderAudioInfos();
	}
	

	private void initViews(View view) {
		mlistv = (ListView) view.findViewById(R.id.lv_fr_mine);
		myAdapter = new MyLocalRingsAdapter(getActivity(), this);
		mlistv.setAdapter(myAdapter);
		myAdapter.setData(ringInfos);
	}
	
	private void getOrderAudioInfos() {
		ringInfos.clear();
		ArrayList<DownloadAudioInfo> getDownloadAudioInfo = AlarmUtils.getDownloadAudio(getActivity(), MyApplication.XIANGCHENG_AUDIO_PATH);
		RingtoneInfo mySelectRingtoneInfo = MyApplication.instance().getCurrentSelectRingInfo();
		RingtoneInfo ringtoneInfo = null;
		DownloadAudioInfo downloadAudioInfo = null;
		for (int i = 0; i < getDownloadAudioInfo.size(); i++) {
			ringtoneInfo = new RingtoneInfo();
			downloadAudioInfo = getDownloadAudioInfo.get(i);
//			ringtoneInfo.id = /* getRecordAudioInfo.size() + */i;
//			ringtoneInfo.int_id = downloadAudioInfo.AudioUserId;
//			ringtoneInfo.nickname = downloadAudioInfo.AudioUserNickName;
//			ringtoneInfo.age = downloadAudioInfo.AudioUserAge;
//			ringtoneInfo.occupation = downloadAudioInfo.AudioUserOccupation;
			ringtoneInfo.title = downloadAudioInfo.title;
//			ringtoneInfo.upload_time = downloadAudioInfo.downloadDate;
			ringtoneInfo.ringtonePath = downloadAudioInfo.path;
			ringtoneInfo.state = Constant.AUDIO_STATE_DOWNLOADED;
//			ringtoneInfo.isLocalRecord = false;
//
			ringtoneInfo.fileKey = downloadAudioInfo.AudioKey;
//
//			ringtoneInfo.themePicString = downloadAudioInfo.AudioCover;
//			ringtoneInfo.coverKey = downloadAudioInfo.AudioCoverKey;
//			ringtoneInfo.ly_pic_url_suolue = downloadAudioInfo.AudioCoverSuolue;
//			ringtoneInfo.ly_pic_key_suolue = downloadAudioInfo.AudioCoverKeySuolue;
//
//			ringtoneInfo.profile = downloadAudioInfo.AudioUserProfile;
//			ringtoneInfo.profileKey = downloadAudioInfo.UserProfileKey;
//			ringtoneInfo.profile_suolue = downloadAudioInfo.AudioUserProfileSuolue;
//			ringtoneInfo.profile_key_suolue = downloadAudioInfo.UserProfileKeySuolue;
			if(mRingtoneInfo == null){//进入的时候初始化
				if(ringtoneInfo.title !=null && mySelectRingtoneInfo.title!= null && ringtoneInfo.title.equals(mySelectRingtoneInfo.title)){
					ringtoneInfo.isselect = true;
				}else {
					ringtoneInfo.isselect = false;
				}
			}else{//刷新列表的时候初始化
				if(ringtoneInfo.title !=null && mRingtoneInfo.title!= null && ringtoneInfo.title.equals(mRingtoneInfo.title)){
					ringtoneInfo.isselect = true;
				}else {
					ringtoneInfo.isselect = false;
				}
			}
			ringtoneInfo.isdefault = true;
			ringInfos.add(ringtoneInfo);
		}
		
		List<LocalRingsaBean> mLocalrings = GetRingsFile(MyApplication.XIANGCHENG_RINGS_PATH);
		for(int l = 0 ; l < mLocalrings.size() ; l++ ){
			ringtoneInfo = new RingtoneInfo();
			LocalRingsaBean Localrings = mLocalrings.get(l);
			ringtoneInfo.title = Localrings.getName();
			ringtoneInfo.ringtonePath = Localrings.getPath();
			ringtoneInfo.fileKey = Localrings.getName();
			ringtoneInfo.state = Constant.AUDIO_STATE_DOWNLOADED;
			if(mRingtoneInfo == null){//进入的时候初始化
				if(ringtoneInfo.title !=null && mySelectRingtoneInfo.title!= null && ringtoneInfo.title.equals(mySelectRingtoneInfo.title)){
					ringtoneInfo.isselect = true;
				}else {
					ringtoneInfo.isselect = false;
				}
			}else{//刷新列表的时候初始化
				if(ringtoneInfo.title !=null && mRingtoneInfo.title!= null && ringtoneInfo.title.equals(mRingtoneInfo.title)){
					ringtoneInfo.isselect = true;
				}else {
					ringtoneInfo.isselect = false;
				}
			}
			ringtoneInfo.isdefault = false;
			ringInfos.add(ringtoneInfo);
		}
		
	}
	
	private void clearRingtoneInfo(RingtoneInfo ringtoneInfo){
		for (int i = 0; i < ringInfos.size(); i++) {
			if(ringInfos.get(i).title == ringtoneInfo.title){
				ringInfos.get(i).isselect = true;
			}else {
				ringInfos.get(i).isselect = false;
			}
		}
	}
	
	private Handler handlerCheckResult = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0://本地没有铃音
				
				break;
			case 1://本地有铃音
				String ringPath = msg.obj.toString();
				mRingtoneInfo.ringtonePath = ringPath;
				playPauseMethod();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	/*** 检查本地是否存在*/
	private void prePlayRingtone(){
		//当铃音类型为为1的时候播放铃音
//		if(mRingtoneInfo.ringtoneType != 2){
			new Thread(){
				@Override
				public void run() {
					String filePath = "";
					if (mRingtoneInfo.isLocalRecord) {
						filePath = mRingtoneInfo.ringtonePath.substring(mRingtoneInfo.ringtonePath.lastIndexOf("/") + 1);
						filePath = FileUtil.checkIsExsits(filePath);
					}else{
						if(FileUtil.checkIsExsitsfromPath(mRingtoneInfo.ringtonePath)){
							filePath = mRingtoneInfo.ringtonePath;
						}
					}
					if(!TextUtils.isEmpty(filePath)){
						Message msg = handlerCheckResult.obtainMessage(1);
						msg.obj = filePath;
						handlerCheckResult.sendMessage(msg);
					}else{
						handlerCheckResult.sendEmptyMessage(0);
					}
				}
				
			}.start();
	}
	
	
	
	/**注册广播 */
	private void registerReceiver() {
		// 初始化
		playerReceiver = new PlayerReceiver();
		// 初始化IntentFilter对象
		IntentFilter filter = new IntentFilter();
		// 添加一系列动作
		filter.addAction(MyPlayerUtil.UPDATE_ACTION);
		filter.addAction(MyPlayerUtil.MUSIC_CURRENT);
		filter.addAction(MyPlayerUtil.MUSIC_DURATION);
		filter.addAction("com.audio.play.dialog.DIALOG_DISMISS");
		filter.addAction("com.audio.select.MORE_RINGTONE_DOWNLOASDED");
		// 注册
		context.registerReceiver(playerReceiver, filter);
	}
	
	/** 自定义一个广播接受者继承BroadcastReceiver*/
	public class PlayerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			int duration = intent.getIntExtra("duration", -1);
			if (action.equals(MyPlayerUtil.MUSIC_CURRENT)) {
//				currentTime = intent.getIntExtra("currentTime", -1);
//				dialog_sb_progress.setProgress(currentTime);
				
			} else if (action.equals(MyPlayerUtil.MUSIC_DURATION)) {
//				dialog_sb_progress.setMax(duration);
			} else if (action.equals(MyPlayerUtil.UPDATE_ACTION)) {
				// 获取Intent中的current消息，current代表当前正在播放的歌曲
//				currentTime = intent.getIntExtra("currentTime", -1);
				isPlaying = false;
				isStartPlay = false;
			}else if (action.equals("com.audio.play.dialog.DIALOG_DISMISS")) {
				if (context != null && playerReceiver != null) {
					context.unregisterReceiver(playerReceiver);
				}
			}else if (action.equals("com.audio.select.MORE_RINGTONE_DOWNLOASDED")) {
				getOrderAudioInfos();
				myAdapter.setData(ringInfos);
			}
		}
	}
	
	
	/** 播放暂停*/
	private void playPauseMethod() {
		if (isStartPlay) {
			Intent intent = new Intent();
			if(playingPath.equals(mRingtoneInfo.ringtonePath)){
				playingPath = "";
				intent.setAction(MyPlayerUtil.AUDIO);
				intent.putExtra("MSG", PlayerMsg.STOP_MSG);
				context.sendBroadcast(intent);
				isPlaying = false;
			}else{
				playingPath = mRingtoneInfo.ringtonePath;
			if (isPlaying) {
				intent.setAction(MyPlayerUtil.AUDIO);
				intent.putExtra("MSG", PlayerMsg.PLAYING_END);
				context.sendBroadcast(intent);
				isPlaying = false;
			}
			startPlayAudio();
			}
		}else {
			playingPath = mRingtoneInfo.ringtonePath;
			startPlayAudio();
		}
	}
	
	/**开始播放铃声*/
	private void startPlayAudio() {
		isStartPlay = true;
		isPlaying = true;
		Intent intent = new Intent();
		intent.setAction(MyPlayerUtil.AUDIO);
		intent.putExtra("url", mRingtoneInfo.ringtonePath);
		intent.putExtra("MSG", PlayerMsg.PLAY_MSG);
		//启动服务
		context.sendBroadcast(intent);
	}
	
	
	public void leaveThisPage(){
		if(isStartPlay){
			Intent intent = new Intent();
			playingPath = "";
			intent.setAction(MyPlayerUtil.AUDIO);
			intent.putExtra("MSG", PlayerMsg.STOP_MSG);
			context.sendBroadcast(intent);
			isPlaying = false;
		}
	}
	
	public void deletePlayingFile(String delpath){
		if(isStartPlay){
			if(playingPath.equals(delpath)){
				playingPath = "";
				Intent intent = new Intent();
				intent.setAction(MyPlayerUtil.AUDIO);
				intent.putExtra("url", delpath);
				intent.putExtra("MSG", PlayerMsg.DELET_MSG);
				context.sendBroadcast(intent);
				isPlaying = false;
			}
		}
	}
	
	public RingtoneInfo getSelectRingtone(){
		for(RingtoneInfo msRingtoneInfo : myAdapter.getData()){
			if(msRingtoneInfo.isselect){
				return msRingtoneInfo;
			}
		}
		return null;
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void itemClick(RingtoneInfo ringtoneInfo) {
		clearRingtoneInfo(ringtoneInfo);
		myAdapter.setData(ringInfos);
		mRingtoneInfo = ringtoneInfo;
		prePlayRingtone();
	}
	
	@Override
	public void itemLongClick(RingtoneInfo ringtoneInfo) {
		ringsOperation(ringtoneInfo);
	}
	
	
	
	public List<LocalRingsaBean> GetRingsFile(String fileAbsolutePath) {
		List<LocalRingsaBean> ringsList = new ArrayList<LocalRingsaBean>();
		
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if(subFile != null){
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

	
	private void ringsOperation(final RingtoneInfo ringtoneInfo){
		if (null == mRingsDialog) {
			mRingsDialog = new MyAlertDialog(context,
					R.style.bottom_animation);
		}
		mRingsDialog.show();
		mRingsDialog.setTV1("请选择对所选铃音的操作");
		mRingsDialog.setTV2("删除", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				if((MyApplication.instance().getCurrentSelectRingInfo().title).equals(ringtoneInfo.title)){
					Util.toastMessage(getActivity(), "已经设定的铃音不可删除");
				}else if(isAlarmRings(ringtoneInfo.title)){
					Util.toastMessage(getActivity(), "已经设定的铃音不可删除");
				}else{			
					ArrayList<RingtoneInfo> getList = myAdapter.getData();
					if(FileUtil.deleteFile(ringtoneInfo.ringtonePath)){
						Util.toastMessage(getActivity(), "删除成功");
						if(ringtoneInfo.isselect){
							getList.get(0).isselect = true;
						}
						getList.remove(ringtoneInfo);
						deletePlayingFile(ringtoneInfo.ringtonePath);
					}else{
						Util.toastMessage(getActivity(), "删除失败");
					}

					//				getOrderAudioInfos();
					remindRingsDel();
					myAdapter.setData(getList);
				}
				mRingsDialog.dismiss();
			}
		}, View.VISIBLE);
		mRingsDialog.setTV3("", null, View.GONE);
		mRingsDialog.setTV4("", null, View.GONE);
		mRingsDialog.setTVbottom("取消", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {

				mRingsDialog.dismiss();
			}

		}, View.VISIBLE);
	}
	
	
	
	/**
	 * 读取闹钟信息
	 */
	public boolean isAlarmRings(String singName) {
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(getActivity());
		SQLiteDatabase db = helper.getWritableDatabase();
		AlarmDBOperate alarmDBOperate = new AlarmDBOperate(db,
				MyTabList.ALARM_TIME);
		ArrayList<ListAlarmTime> alarmTimes = (ArrayList<ListAlarmTime>) alarmDBOperate
				.GetALarmData();

		ListAlarmTime alarmInfo;
		// 专属铃音 神秘铃音响应后 铃音类型更改为专属铃音
		for (int i = 0; i < alarmTimes.size(); i++) {
			alarmInfo = alarmTimes.get(i);
			if(alarmInfo.AlarmTitle.equals(singName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 铃声删除之后提示下载列表刷新
	 */
	private void remindRingsDel(){
		Intent intent = new Intent();
		intent.setAction("com.audio.select.MINE_RINGTONE_DELEDTED");
		context.sendBroadcast(intent);
	}

}
