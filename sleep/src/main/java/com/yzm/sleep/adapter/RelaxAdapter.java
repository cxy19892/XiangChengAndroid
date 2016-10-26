package com.yzm.sleep.adapter;

import java.io.File;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
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
import com.yzm.sleep.bean.RingtoneBean;
import com.yzm.sleep.dao.AudioDAO.DownloadAudioInfoClass;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.widget.SimpleRoundProgressBar;

public class RelaxAdapter extends BaseAdapter {

	public List<RingtoneBean> mlist;
	private RingtoneBean mRingsInfoOnlineBean;
	private LayoutInflater inflater;
	private Context context;
	private AsyncHttpClient client;
	private RelaxCallBack mCallBack;
	
	
	public RelaxAdapter(Context context , RelaxCallBack callback){
		this.context = context;
		this.mCallBack = callback;
		this.inflater = LayoutInflater.from(context);
		client = new AsyncHttpClient();
	}
	
	public void setData(List<RingtoneBean> list){
		this.mlist = list;
		notifyDataSetChanged();
	}
	
	public List<RingtoneBean> getData(){
		return this.mlist;
	}
	@Override
	public int getCount() {
		if(mlist!=null){
			return mlist.size();
		}else
		return 0;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		mRingsInfoOnlineBean = mlist.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_relax_self, null);
			holder.itemTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.itemDownloadBtn = (ImageView) convertView.findViewById(R.id.img_download_view);
			holder.itemProssesRL = (RelativeLayout) convertView.findViewById(R.id.rl_state_loading);
			holder.itemMyProcess = (SimpleRoundProgressBar) convertView.findViewById(R.id.pb_state_loading);
			holder.itemProcessNum = (TextView) convertView.findViewById(R.id.tv_loading_curpro);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.itemTitle.setText(mRingsInfoOnlineBean.getName());
		
		switch (mRingsInfoOnlineBean.getState()) {
		case Constant.AUDIO_STATE_NOT_DOWNLOAD://未下载
			holder.itemDownloadBtn.setImageResource(R.drawable.ic_down_normal);
			break;
		case Constant.AUDIO_STATE_DOWNLOADED://已下载
			holder.itemDownloadBtn.setImageResource(R.drawable.ic_play);
			break;
		case Constant.AUDIO_STATE_DOWNLOADING://下载中
			holder.itemDownloadBtn.setImageResource(R.drawable.ic_down_normal);
			break;
		case Constant.AUDIO_STATE_PLAYIING://播放
			holder.itemDownloadBtn.setImageResource(R.drawable.ic_pause);
			break;
		case Constant.AUDIO_STATE_PLAY_PAUSE://暂停
			holder.itemDownloadBtn.setImageResource(R.drawable.ic_play);
			break;
		default:
			break;
		}
		Listener listener = new Listener(holder, position, mRingsInfoOnlineBean);
		holder.itemMyProcess.setRoundProgressColor(context.getResources().getColor(R.color.theme_color));
		holder.itemMyProcess.setRoundWidth(6);
		holder.itemTitle.setText(mRingsInfoOnlineBean.getName());
		holder.itemDownloadBtn.setOnClickListener(listener);
		
		return convertView;
	}
	
	public class ViewHolder{
		 TextView itemTitle;
		 ImageView itemDownloadBtn;
		 RelativeLayout itemProssesRL;
		 SimpleRoundProgressBar  itemMyProcess;
		 TextView itemProcessNum;
	}
	
	public interface RelaxCallBack{
		public void RelaxStateCallBack(RingtoneBean mRingsBean);
	}

	class Listener implements OnClickListener{
		private RingtoneBean mRingsBean;
		private ViewHolder viewHold;
		private int position;
		private String target = "";
		
		
		public Listener(ViewHolder viewHold, int position, RingtoneBean mRingsBean) {
			this.mRingsBean = mRingsBean;
			this.viewHold = viewHold;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_download_view:
				switch (mRingsBean.getState()) {
				case Constant.AUDIO_STATE_NOT_DOWNLOAD://未下载
					viewHold.itemProssesRL.setVisibility(View.VISIBLE);
					viewHold.itemDownloadBtn.setVisibility(View.GONE);
					viewHold.itemTitle.setTextColor(context.getResources().getColor(R.color.ct_color));
					setDatas(mRingsBean, viewHold);
					break;
				case Constant.AUDIO_STATE_DOWNLOADED://已下载-->播放
					if(mCallBack != null){
						mCallBack.RelaxStateCallBack(mRingsBean);
					}
					break;
				case Constant.AUDIO_STATE_DOWNLOADING://下载中
					break;
				case Constant.AUDIO_STATE_PLAYIING://播放 -->暂停
					if(mCallBack != null){
						mCallBack.RelaxStateCallBack(mRingsBean);
					}
					break;
				case Constant.AUDIO_STATE_PLAY_PAUSE://暂停 -->播放
					if(mCallBack != null){
						mCallBack.RelaxStateCallBack(mRingsBean);
					}
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
			hold.itemMyProcess.setProgressf(0);
			String audioKey = downloadAudioInfoClass.AudioKey;
			audioKey = audioKey.replaceAll("/", "_");
			audioKey = audioKey.replaceAll(":", "_");
			if (audioKey.contains(".aac")) {
				target = MyApplication.XIANGCHENG_RINGS_PATH + audioKey;
			}else {
				target = MyApplication.XIANGCHENG_RINGS_PATH + audioKey +".aac";
			}
			downloadAudioInfoClass.AudioLocalPath = target;
			//调用cancel()方法停止下载
			//		handler.cancel();
//			String newUrlString = "";
//			if (url.contains("?")) {
//				newUrlString = url;
//			}else {
//				if (url.contains("aac")) {
//					newUrlString = url;
//				}else {
//					newUrlString = url + ".aac";
//				}
//			}
			File dirfile = new File(MyApplication.XIANGCHENG_RINGS_PATH);
			File dirfileTemp = new File(MyApplication.XIANGCHENG_RINGS_PATH_TEMP);
			if(!dirfile.exists())
				dirfile.mkdirs();// 创建文件夹  
			if(!dirfileTemp.exists())
				dirfileTemp.mkdirs();
			final File file = new File(MyApplication.XIANGCHENG_RINGS_PATH, audioKey+".aac");
			final File fileTemp = new File(MyApplication.XIANGCHENG_RINGS_PATH_TEMP, audioKey+".aac");
			LogUtil.d("chen", "url="+url);
			client.get(context, url, new FileAsyncHttpResponseHandler(fileTemp) {

				@Override
				public void onSuccess(int arg0, Header[] arg1, File arg2) {
					FileUtil.copyFile(fileTemp, file);
					if (hold != null) {
						hold.itemProssesRL.setVisibility(View.INVISIBLE);
						hold.itemDownloadBtn.setVisibility(View.VISIBLE);
						mRingsBean.setPath(target);
						if(mCallBack != null){
							mCallBack.RelaxStateCallBack(mRingsBean);
						}
						mRingsBean.setState(Constant.AUDIO_STATE_DOWNLOADED);
					}
					ToastManager.getInstance(context).show("下载完成");
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
					if(hold != null){
						hold.itemProssesRL.setVisibility(View.GONE);
						hold.itemDownloadBtn.setVisibility(View.VISIBLE);
//						if(mCallBack != null){
//							mCallBack.RelaxStateCallBack(mRingsBean);
//						}
						
					}
					ToastManager.getInstance(context).show("下载失败");
					mRingsBean.setState(Constant.AUDIO_STATE_NOT_DOWNLOAD);

					try {//下载失败之后删除该文件
						new File(arg3.getAbsolutePath()).delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onProgress(int current, int total) {
					// TODO Auto-generated method stub
					super.onProgress(current, total);
					if (total == 0) {
						total = 1;
					}
					if (hold != null) {
//						LogUtil.i("chen", "current="+current+"--total="+total);
						int Value=(int) (((float)current/total) * 100);
						//					String str=String.format("%d", Value);
						hold.itemProcessNum.setText( Value+ "%");
						hold.itemMyProcess.setMax(total);
						hold.itemMyProcess.setProgressf((float)current/total);
					}
				}
			});
		}
	}
	
	
	public void cancleDownLoad(){
		try {
			client.cancelRequests(context, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
