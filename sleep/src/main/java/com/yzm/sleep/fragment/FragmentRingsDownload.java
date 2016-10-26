package com.yzm.sleep.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import u.aly.av;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.MyDownloadRingAdapter;
import com.yzm.sleep.adapter.MyDownloadRingAdapter.CallBackinFragment;
import com.yzm.sleep.bean.LocalRingsaBean;
import com.yzm.sleep.bean.RingtoneBean;
import com.yzm.sleep.fragment.FragmentRingsMine.PlayerReceiver;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceMoreRingtoneListCallback;
import com.yzm.sleep.utils.MyPlayerUtil;
import com.yzm.sleep.utils.XiangchengProcClass;

public class FragmentRingsDownload extends Fragment {

	private Context context;
	private MyDownloadRingAdapter mAdapter;
	private ListView mListView;
	private List<RingtoneBean> mlylist;
	private View noMore;
	private RingsDelReceiver delReceiver;
	
	@Override
	public void onAttach(Activity activity) {
		this.context = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_rings_down, null);
	}
	
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mAdapter = new MyDownloadRingAdapter(getActivity(), mCallBackinFragment);
		initViews(view);
		getMoredata();//模拟数据
		registerReceiver();
	}

	private void setDates(List<RingtoneBean> mlist) {
		List<RingtoneBean> mTempList = new ArrayList<RingtoneBean>();
		List<LocalRingsaBean> getDownloadAudioInfo = GetRingsFile(MyApplication.XIANGCHENG_RINGS_PATH);//AlarmUtils.getDownloadAudio(getActivity(), MyApplication.XIANGCHENG_AUDIO_PATH);
		for(RingtoneBean mRingtoneBean :mlist ){
			for(LocalRingsaBean mLocalRingsaBean : getDownloadAudioInfo){
				if(mRingtoneBean.getName().equals(mLocalRingsaBean.getName())){
					mTempList.add(mRingtoneBean);
				}
			}
			mRingtoneBean.setState(Constant.AUDIO_STATE_NOT_DOWNLOAD);
		}
		mlist.removeAll(mTempList);
		if(mlist.size()>0){
			noMore.setVisibility(View.GONE);
		}else{
			noMore.setVisibility(View.VISIBLE);
		}
		mAdapter.SetDate(mlist);
	}

	private void initViews(View view) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		noMore = inflater.inflate(R.layout.listview_footer_nomore, null);
		mListView = (ListView) view.findViewById(R.id.lv_fr_down);
		mListView.addFooterView(noMore, null, false);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	private void getMoredata(){
		if(this.mlylist != null){
			mlylist.clear();
		}
		new XiangchengProcClass(getActivity()).moreRingtoneList(new InterfaceMoreRingtoneListCallback() {
			
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



	public CallBackinFragment mCallBackinFragment = new CallBackinFragment(){

		@Override
		public void refreshViewsCallBack() {
			getMoredata();			
		}
	};
	
	/**注册广播 */
	private void registerReceiver() {
		// 初始化
		delReceiver = new RingsDelReceiver();
		// 初始化IntentFilter对象
		IntentFilter filter = new IntentFilter();
		// 添加一系列动作
		filter.addAction("com.audio.select.MINE_RINGTONE_DELEDTED");
		filter.addAction("com.audio.play.dialog.DIALOG_DISMISS");
		// 注册
		context.registerReceiver(delReceiver, filter);
	}
	
	/** 自定义一个广播接受者
	 * 当我的铃音列表删除之后重新去获取一次数据
	 */
	public class RingsDelReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals("com.audio.select.MINE_RINGTONE_DELEDTED")){
				getMoredata();
			}else if (intent.getAction().equals("com.audio.play.dialog.DIALOG_DISMISS")) {
				if (context != null && delReceiver != null) {
					context.unregisterReceiver(delReceiver);
				}
			}
		}
	}
	
}
