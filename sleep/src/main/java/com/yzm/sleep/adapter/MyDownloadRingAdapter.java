package com.yzm.sleep.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.bean.RingtoneBean;
import com.yzm.sleep.dao.AudioDAO;
import com.yzm.sleep.dao.AudioDAO.DownloadAudioInfoClass;
import com.yzm.sleep.model.DownloadAudioInfo;
import com.yzm.sleep.model.RingtoneInfo;
import com.yzm.sleep.utils.AlarmUtils;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.widget.SimpleRoundProgressBar;

public class MyDownloadRingAdapter extends BaseAdapter {

	public List<RingtoneBean> mlist;
	private RingtoneBean mRingsInfoOnlineBean;
	private LayoutInflater inflater;
	private Context context;
	private AsyncHttpClient client;
	private CallBackinFragment mCallBack;
	
	public MyDownloadRingAdapter(Context context, CallBackinFragment CallBack){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.mCallBack = CallBack;
		client = new AsyncHttpClient();
	}
	
	public void SetDate(List<RingtoneBean> list){
		this.mlist = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if(mlist != null){
			return mlist.size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mRingsInfoOnlineBean = (RingtoneBean) getItem(position);
		ViewHolder viewHold = null;
		if (convertView == null) {
			viewHold = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_of_download_ring, null);
			viewHold.itemTitle = (TextView) convertView.findViewById(R.id.item_tv_rings_name);
			viewHold.itemDownloadBtn =  (ImageView) convertView.findViewById(R.id.img_download_view);
			viewHold.itemProssesRL = (RelativeLayout) convertView.findViewById(R.id.rl_state_loading);
			viewHold.itemMyProcess = (SimpleRoundProgressBar) convertView.findViewById(R.id.pb_state_loading);
			viewHold.itemProcessNum= (TextView) convertView.findViewById(R.id.tv_loading_curpro);
			convertView.setTag(viewHold);
		}else{
			viewHold = (ViewHolder) convertView.getTag();
		}
		viewHold.itemTitle.setTextColor(context.getResources().getColor(R.color.ct_color));
		Listener listener = new Listener(viewHold, position, mRingsInfoOnlineBean);
		viewHold.itemMyProcess.setRoundProgressColor(context.getResources().getColor(R.color.theme_color));
		viewHold.itemMyProcess.setRoundWidth(6);
		viewHold.itemTitle.setText(mRingsInfoOnlineBean.getName());
		viewHold.itemDownloadBtn.setOnClickListener(listener);
		return convertView;
	}
	
	class Listener implements OnClickListener{
		private RingtoneBean mRingsBean;
		private ViewHolder viewHold;
		private int position;
		
		public Listener(ViewHolder viewHold, int position, RingtoneBean mRingsBean) {
			this.mRingsBean = mRingsBean;
			this.viewHold = viewHold;
			this.position = position;
		}
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_download_view:
				viewHold.itemProssesRL.setVisibility(View.VISIBLE);
				viewHold.itemDownloadBtn.setVisibility(View.INVISIBLE);
				viewHold.itemTitle.setTextColor(context.getResources().getColor(R.color.ct_color));
				setDatas(mRingsBean, viewHold);
				break;

			default:
				break;
			}
			
		}
	}
	
	
	
	private void setDatas(RingtoneBean mRingsInfoOnlineBean, ViewHolder hold){
		DownloadAudioInfoClass downloadAudioInfoClass = new DownloadAudioInfoClass();
		downloadAudioInfoClass.AudioKey   = mRingsInfoOnlineBean.getName();
		downloadAudioInfoClass.AudioTitle = mRingsInfoOnlineBean.getName();
		downloadAudioInfoClass.AudioType = 1;
		doDownload(downloadAudioInfoClass, mRingsInfoOnlineBean.getDownloadurl(), hold, mRingsInfoOnlineBean);
	}
	
	private void doDownload(final DownloadAudioInfoClass downloadAudioInfoClass,final String url,final ViewHolder hold, final RingtoneBean mRingsBean){
//		HttpUtils http = new HttpUtils();
		hold.itemMyProcess.setMax(10000);
		hold.itemMyProcess.setProgressf(0f);
		String audioKey = downloadAudioInfoClass.AudioKey;
		audioKey = audioKey.replaceAll("/", "_");
		audioKey = audioKey.replaceAll(":", "_");
		String target = "";
		if (audioKey.contains(".aac")) {
			target = MyApplication.XIANGCHENG_RINGS_PATH + audioKey;
		}else {
			target = MyApplication.XIANGCHENG_RINGS_PATH + audioKey +".aac";
		}
		downloadAudioInfoClass.AudioLocalPath = target;
		//调用cancel()方法停止下载
//		handler.cancel();
		String newUrlString = "";
		if (url.contains("?")) {
			newUrlString = url;
		}else {
			if (url.contains("aac")) {
				newUrlString = url;
			}else {
				newUrlString = url + ".aac";
			}
		}
		File dirfile = new File(MyApplication.XIANGCHENG_RINGS_PATH);
        if(!dirfile.exists())
        	dirfile.mkdirs();// 创建文件夹  
		
		File file = new File(MyApplication.XIANGCHENG_RINGS_PATH, audioKey+".aac");
		client.get(url, new FileAsyncHttpResponseHandler(file) {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, File arg2) {
				if (hold != null) {
					hold.itemProssesRL.setVisibility(View.INVISIBLE);
					hold.itemDownloadBtn.setVisibility(View.VISIBLE);
					mRingsBean.setState(Constant.AUDIO_STATE_DOWNLOADED);
					saveDownloadAudio(downloadAudioInfoClass);
					Intent intent = new Intent();
					intent.setAction("com.audio.select.MORE_RINGTONE_DOWNLOASDED");
					context.sendBroadcast(intent);
				}
				ToastManager.getInstance(context).show("下载完成");
				RingtoneBean mTempRings = null;
				for(RingtoneBean mRings:mlist){
					if(mRings.getDownloadurl().equals(url)){
						mTempRings = mRings;
					}
				}
				if(mTempRings != null){
					mlist.remove(mTempRings);
				}
				mHandler.sendEmptyMessage(0);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				if(hold != null){
					hold.itemProssesRL.setVisibility(View.INVISIBLE);
					hold.itemDownloadBtn.setVisibility(View.VISIBLE);
				}
				ToastManager.getInstance(context).show("下载失败");
				mRingsBean.setState(Constant.AUDIO_STATE_NOT_DOWNLOAD);
				
				try {//下载失败之后删除该文件
					new File(arg3.getAbsolutePath()).delete();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(0);
			}

			@Override
			public void onProgress(int current, int total) {
				// TODO Auto-generated method stub
				super.onProgress(current, total);
				if (total == 0) {
					total = 1;
				}
				if (hold != null) {
//					LogUtil.i("chen", "current="+current+"--total="+total);
					float valuef = (float)current/total;
					int Value=(int) (valuef * 100);
//					String str=String.format("%d", Value);
					hold.itemProcessNum.setText( Value+ "%");
					hold.itemMyProcess.setMax(total);
					hold.itemMyProcess.setProgressf(valuef);
				}
			}
			
			
		});
	}
	
	/**保存下载的铃音@param downloadAudioInfoClass*/
	private void saveDownloadAudio(final DownloadAudioInfoClass downloadAudioInfoClass) {
		new Thread(new Runnable() {
			@Override
			public void run() {
//				if(getOrderAudioInfos(downloadAudioInfoClass.AudioLocalPath)){
//					MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
//					SQLiteDatabase db = helper.getWritableDatabase();
//					AudioDAO audioDAO = new AudioDAO(db, MyTabList.DOWNLOAD_AUDIO);
//					audioDAO.saveDownloadInfo(downloadAudioInfoClass);
//				}
				Intent intent2 = new Intent();
				intent2.setAction("com.audio.select.LOCAL_RINGTONE_GET_COVER");
				context.sendBroadcast(intent2);
				
				Intent intent3 = new Intent();
				intent3.setAction("com.audio.select.MORE_RINGTONE_UPDATE");
				context.sendBroadcast(intent3);
			}
		}).start();
	}
	
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				if(mlist.size() == 0){
					if(mCallBack != null){
						mCallBack.refreshViewsCallBack();
					}
				}
				break;

			default:
				break;
			}
		};
	};
	
	
	private boolean getOrderAudioInfos(String path) {
		ArrayList<DownloadAudioInfo> getDownloadAudioInfo = AlarmUtils.getDownloadAudio(context, MyApplication.XIANGCHENG_AUDIO_PATH);
		
		RingtoneInfo ringtoneInfo = null;
		DownloadAudioInfo downloadAudioInfo = null;
		for (int i = 0; i < getDownloadAudioInfo.size(); i++) {
			downloadAudioInfo = getDownloadAudioInfo.get(i);
			if(path.endsWith(downloadAudioInfo.path)){
				return false;
			}
		}
		return true;
	}
	
	
	public class ViewHolder{
		private TextView itemTitle;
		private ImageView itemDownloadBtn;
		private RelativeLayout itemProssesRL;
		private SimpleRoundProgressBar  itemMyProcess;
		private TextView itemProcessNum;
	}
	
	public interface CallBackinFragment{
		public void refreshViewsCallBack();
	}

}
