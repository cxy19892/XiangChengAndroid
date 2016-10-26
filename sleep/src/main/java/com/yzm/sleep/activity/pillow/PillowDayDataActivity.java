package com.yzm.sleep.activity.pillow;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.xpillowjni.XpillowInterface;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.bean.HardwareSleepDataBean;
import com.yzm.sleep.bluetoothBLE.CopyOfPillowHelper;
import com.yzm.sleep.bluetoothBLE.PillowCallback;
import com.yzm.sleep.model.CalenderSelectDialog;
import com.yzm.sleep.model.CalenderSelectDialog.SelectDayDateListener;
import com.yzm.sleep.model.MyDialog;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.model.SignInData;
import com.yzm.sleep.render.GetModelsValueClass.ModelsValues;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepData;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepDataHead;
import com.yzm.sleep.utils.BluetoothDataFormatUtil;
import com.yzm.sleep.utils.BluetoothUtil;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareAllDayCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadHardwareDataCallBack1;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareAllDayParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadHardwareDataParamClass1;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetDayPillowDataCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.UploadDakaDaysParamClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSignInCallBack4_2_1;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SleepDataProClass;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.CustomListView;
import com.yzm.sleep.widget.OneKeyDialog;
import com.yzm.sleep.widget.OrangeDayDataTable;
import com.yzm.sleep.widget.SyncAlertDialog;
import com.yzm.sleep.widget.SyncAlertDialog.MySyncOnClickListener;

@SuppressLint("SimpleDateFormat") 
public class PillowDayDataActivity extends BaseActivity implements MySyncOnClickListener{

	private TextView tvSoberTime,tvShallowTime,tvDeepTime/*, tvSleepLength,time2,time4*/ ;
	private OrangeDayDataTable orangeDayDataTable;
	private CustomListView mListView;
//	private SeekBar sbTimeValue;
//	private int countTime =4;
	private TextView titleTv;
	private Button titleRightBtn;
//	private float target;
	private CustomAdapter mAdapter;
//	private TextView relative1T1;
//	private TextView relative1T3;
//	private TextView relative1T5;
	private ProgressUtils pro;
	private String[] content = new String[]{"暂无数据", "暂无数据", "暂无数据", "暂无数据", "暂无数据"};
	//4.3
	private LinearLayout syncLin;
	private Button syncBtn;
	private SyncAlertDialog syncDialog;
//	private BluetoothAdapter mBluetoothAdapter;
	private CopyOfPillowHelper pillowserver = null;
	private boolean isgotdata = false;
	private boolean isneedUpLoad = false;
	private boolean isConnect = false;
	private boolean IS_NEED_TO_OPEN_BLUETOOTH = false;
	private ProgressUtils pro2;
	private OneKeyDialog mOneKeyDialog;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pillow_data);
		((TextView)findViewById(R.id.title)).setText(TimeFormatUtil.getYesterDay("yyyy-MM-dd"));
		findViewById(R.id.back).setOnClickListener(this);
		initView();
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SP_LastNightDetail"); 
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SP_LastNightDetail");
		MobclickAgent.onPause(this);
	}

	private void initView() {
		mOneKeyDialog = new OneKeyDialog(this);
		syncBtn = (Button) findViewById(R.id.btn_start_bund);
		syncLin = (LinearLayout) findViewById(R.id.pillow_sync_lin);
		syncBtn.setOnClickListener(this);
		
		titleTv = (TextView) findViewById(R.id.title);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleRightBtn.setVisibility(View.VISIBLE);
		titleRightBtn.setCompoundDrawables(null, null, null, null);
		titleRightBtn.setText("更多");
		titleRightBtn.setOnClickListener(this);
//		relative1T1=(TextView) findViewById(R.id.time1);
//		relative1T3=(TextView) findViewById(R.id.time3);
//		relative1T5=(TextView) findViewById(R.id.time5);
		mListView = (CustomListView) findViewById(R.id.pillow_day_listview);
//		findViewById(R.id.tip).setOnClickListener(this);
		tvSoberTime = (TextView) findViewById(R.id.sober_time);
		tvShallowTime=(TextView) findViewById(R.id.shallow_time);
		tvDeepTime=(TextView) findViewById(R.id.deep_time);
		orangeDayDataTable = (OrangeDayDataTable) findViewById(R.id.pillow_day_data);
//		sbTimeValue=(SeekBar) findViewById(R.id.sb_time_value);
//		sbTimeValue.setEnabled(false);
//		tvSleepLength=(TextView) findViewById(R.id.tv_sleeplength);
//		time2=(TextView) findViewById(R.id.time2);
//		time4=(TextView) findViewById(R.id.time4);
		int formHeight = getScreenWidth() * 60/100;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getScreenWidth(), formHeight);
		lp.topMargin = 50;
		orangeDayDataTable.setLayoutParams(lp);
//		target=PreManager.instance().getRecommendTarget(this);
//		time2.setText(String.valueOf(target-1)+"h");
//		time4.setText(String.valueOf(target+1)+"h");
		mAdapter = new CustomAdapter(this);
		mListView.setAdapter(mAdapter);
		//============================================
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		titleTv.setTag(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
		titleTv.setText(CalenderUtil.getStrByDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime())));
		getPillowDataByDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
	}

	private void refreshPillowData(SleepDataHead dataHead){
		List<SleepData> list = dataHead.m_pValue;
		orangeDayDataTable.setData(list, 
				TimeFormatUtil.formatTime1(dataHead.InSleepTime, "HH:mm"), 
				TimeFormatUtil.formatTime1(dataHead.OutSleepTime, "HH:mm"), 
				dataHead.YMax);
	}
	
	/**
	 * 获取睡眠数据
	 * @param dayDate
	 */
	private void getPillowDataByDate(String dayDate){
//		relative1T1.setTextColor(0xff7F7D92);
//		relative1T3.setTextColor(0xff7F7D92);
//		relative1T5.setTextColor(0xff7F7D92);
		PillowDataModel model = PillowDataOpera.queryDataFromSQL(getApplicationContext(), dayDate);
		if(model!=null && model.getBfr() != null )
			doData(model.getBfr(), dayDate);
		else
			downLoadData(dayDate);
	}
	
	/**
	 * 下载服务器数据
	 * @param dayDate
	 */
	private void downLoadData(final String dayDate){
		showPro();
		UploadDakaDaysParamClass mParams=new UploadDakaDaysParamClass();
		mParams.my_int_id=PreManager.instance().getUserId(this);
		mParams.date=dayDate.replace("-", "");
		new XiangchengProcClass(this).getDayPillowData(mParams, new InterfaceGetDayPillowDataCallback() {
			
			@Override
			public void onSuccess(int icode, List<HardwareSleepDataBean> data) {
				if(data!=null && data.size()>0&& !TextUtils.isEmpty(data.get(0).getModelkey()))
					downModelFile(dayDate,data.get(0).getModelkey());
				else
					clearView(dayDate);
				
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				clearView(dayDate);
			}
		});
	}
	
	/**
	 * 下载modele
	 * @param fileUrl
	 */
	private void downModelFile(final String dayDate,String fileUrl){
		AsyncHttpClient client = new AsyncHttpClient();
		String path = PillowDataOpera.getPillowDataPath(dayDate+ ".model");
		client.get(fileUrl,
				new FileAsyncHttpResponseHandler(new File(path)) {
					@Override
					public void onSuccess(int arg0, Header[] arg1,File file) {
						byte[] bfr =PillowDataOpera.readDataToSDcard(dayDate + ".model");
						doData(bfr, dayDate);
						insertHardwareData(bfr,dayDate);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1,Throwable arg2, File arg3) {
						clearView(dayDate);
					}
				});
	}
	
	/**
	 * 处理数据
	 * @param model
	 */
	private void doData(byte[] bfr, String dayDate){
		clearView(dayDate);
		//传入控件的宽度，按像素点获取值
		if(bfr != null && bfr.length > 0){
			SleepDataHead datahead = BluetoothDataFormatUtil.format3(bfr,getScreenWidth());
			refreshPillowData(datahead);
			try {
				content[0] = TimeFormatUtil.formatTime1(datahead.InSleepTime, "HH:mm");
				content[1] = TimeFormatUtil.formatTime1(datahead.OutSleepTime, "HH:mm");
				content[2] = datahead.AwakeNoGetUpCount + "分钟";
				content[3] = datahead.ToSleep + "分钟";
				content[4] = datahead.AwakeCount + "次";
				String week=datahead.AwakeTime_Sleep/60 < 0 ? "0"+(datahead.AwakeTime_Sleep/60):(datahead.AwakeTime_Sleep/60)+"小时"+(datahead.AwakeTime_Sleep%60 < 0 ? "0"+(datahead.AwakeTime_Sleep%60):(datahead.AwakeTime_Sleep%60))+"分";
				tvSoberTime.setText(week);
				String shallow=datahead.Shallow_Sleep/60 < 0 ? "0"+(datahead.Shallow_Sleep/60):(datahead.Shallow_Sleep/60)+"小时"+(datahead.Shallow_Sleep%60 < 0 ? "0"+(datahead.Shallow_Sleep%60):(datahead.Shallow_Sleep%60))+"分";
				tvShallowTime.setText(shallow);
				String deep=datahead.Deep_Sleep/60 < 0 ? "0"+(datahead.Deep_Sleep/60):(datahead.Deep_Sleep/60)+"小时"+(datahead.Deep_Sleep%60 < 0 ? "0"+(datahead.Deep_Sleep%60):(datahead.Deep_Sleep%60))+"分";
				tvDeepTime.setText(deep);
//				float sleepTimeLength=datahead.TotalSleepTime/(float)60;
//				if(sleepTimeLength < (target-1)){
//					float tempTime =(target-1)-sleepTimeLength;
//					int tempPro= (int) (tempTime*60);
//					sbTimeValue.setProgress((countTime*60-tempPro)<25?25:(countTime*60-tempPro));
//					sbTimeValue.setThumb(getResources().getDrawable(R.drawable.index_icon1));
//					relative1T1.setTextColor(0xff4471FF);
//				}else if(sleepTimeLength > (target+1)){
//					float tempTime = sleepTimeLength-(target+1);
//					int tempPro= (int) (tempTime*60);
//					sbTimeValue.setProgress(((countTime*60*2)+tempPro)>695?695:((countTime*60*2)+tempPro));	
//					sbTimeValue.setThumb(getResources().getDrawable(R.drawable.index_icon1));
//					relative1T5.setTextColor(0xff4471FF);
//				}else{
//					float tempTime = sleepTimeLength-(target-1);
//					int tempPro= (int) (tempTime*(60*countTime/2));
//					sbTimeValue.setProgress(((countTime*60)+tempPro)>695?695:((countTime*60)+tempPro));
//					sbTimeValue.setThumb(getResources().getDrawable(R.drawable.index_icon));
//					relative1T3.setTextColor(0xff8745ff);
//				}
//				tvSleepLength.setText("睡眠时长："+datahead.TotalSleepTime/60+"小时"+datahead.TotalSleepTime%60+"分");
//				sbTimeValue.setVisibility(View.VISIBLE);
			} catch (Exception e) {
//				sbTimeValue.setVisibility(View.INVISIBLE);
//				tvSleepLength.setText("睡眠时长：00小时00分");
			}
		}else{
//			sbTimeValue.setVisibility(View.INVISIBLE);
//			tvSleepLength.setText("睡眠时长：00小时00分");
		}
		
		mAdapter.SetDate(new String[]{"入睡时间","醒来时间","赖床时间","入睡速度","中途醒来次数"}, content);
	}
	
	/**
	 * 插入硬件数据库
	 * @param fileKey
	 * @param date
	 */
	private void insertHardwareData(byte[] bfr, String date) {
		PillowDataModel model = new PillowDataModel();
		model.setBfr(bfr);
		model.setDate(date);
		model.setDatFileName(date + ".dat");
		model.setDatIsUpload("1");
		model.setFileName(date + ".model");
		model.setIsUpload("1");
		if (PillowDataOpera.queryDataExistFromSQL(this, date))
			PillowDataOpera.updateDataToSQL(this, model);
		else
			PillowDataOpera.insertDataToSQL(this, model);
	}
	
	private void clearView(String dayDate) {
		if(dayDate.equals(TimeFormatUtil.getYesterDay("yyyy-MM-dd"))){
			if(checkIsShowBtn()){
				orangeDayDataTable.setVisibility(View.GONE);
				syncLin.setVisibility(View.VISIBLE);
			}else{
				orangeDayDataTable.setVisibility(View.VISIBLE);
				syncLin.setVisibility(View.GONE);
			}
		}else{
			orangeDayDataTable.setData(null, null, null, 0);
		}
		
		cancelPro();
		
//		sbTimeValue.setVisibility(View.INVISIBLE);
//		tvSleepLength.setText("睡眠时长：00小时00分");
		tvSoberTime.setText("00小时00分");
		tvShallowTime.setText("00小时00分");
		tvDeepTime.setText("00小时00分");
		mAdapter.SetDate(new String[]{"入睡时间","醒来时间","赖床时间","入睡速度","中途醒来次数"}, new String[]{"暂无数据", "暂无数据", "暂无数据", "暂无数据", "暂无数据"});
	}
	
	
	private boolean checkIsShowBtn(){
		boolean showBtn = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String crrentTime = sdf.format(new Date());
			long t1 = sdf.parse("04:00").getTime();
			long t2 = sdf.parse(crrentTime).getTime();
			if (t2 - t1 >= 0) {
				String syncHardwareSleepDate = PreManager.instance()
						.getSyncHardwareSleepDate(this);
				if (!TextUtils.isEmpty(syncHardwareSleepDate)
						&& syncHardwareSleepDate.equals(PreManager
								.instance().getUserId(this)
								+ new SimpleDateFormat("yyyy-MM-dd")
										.format(new Date())))
					showBtn = false;
				else
					showBtn = true;
			}
		} catch (Exception e) {
		}
		return showBtn;
	}
	
	
	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
//		case R.id.tip:
//			SleepTargetDialog dialog=new SleepTargetDialog(this);
//			dialog.setCanceledOnTouchOutside(false);
//			dialog.show();
//			break;
		case R.id.btn_title_right:
			showPopupWindow(titleRightBtn);
			break;
		case R.id.btn_start_bund:
			onClickSyncData();
			break;
		
		default:
			break;
		}
	}
	
	class CustomAdapter extends BaseAdapter{
		private String[] title ;
		private String[] content ;
		private LayoutInflater mInflater;
		
		public CustomAdapter(Context context){
			this.mInflater=LayoutInflater.from(context);
		}
		
		public void SetDate(String[] title ,String[] content){
			this.title=title;
			this.content=content;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return title==null ? 0 : title.length;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder=null;
			if(view!=null && view.getTag()!=null){
				holder=(ViewHolder) view.getTag();
			}else{
				holder=new ViewHolder();
				view=mInflater.inflate(R.layout.listview_text_item, null);
				holder.title=(TextView) view.findViewById(R.id.title);
				holder.content=(TextView) view.findViewById(R.id.tv_cous_content);
				view.setTag(holder);
			}
			holder.title.setText(title[position]);
			holder.content.setText(content[position]);
			return view;
		}
	}
	
	class ViewHolder{
		TextView title,content;
	}
	
	//4.3
	
	@Override
	protected void onDestroy() {
		if (pillowserver != null)
			pillowserver.Stop_server(this);
		
		super.onDestroy();
	}
	/**
	 * 点击同步按钮
	 */

	private void onClickSyncData() {
		if (!TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(
				this))) {
			boolean isOpen = BluetoothUtil.bluetoothIsOn(this);
			if (isOpen) {
				MobclickAgent.onEvent(this, "646");
				syncDialog = new SyncAlertDialog(this,
						R.style.bottom_animation, this);
				syncDialog.show();
				isgotdata = false;
				isneedUpLoad = false;
				isConnect = false;
				connectDevice(PreManager.instance().getBundbluetoothPillow(
						this));
				syncDialog.setCanceledOnTouchOutside(false);
				syncDialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(
							android.content.DialogInterface dialog,
							int keyCode, android.view.KeyEvent event) {
						if (keyCode == android.view.KeyEvent.KEYCODE_BACK
								&& event.getRepeatCount() == 0) {
							MobclickAgent.onEvent(PillowDayDataActivity.this, "647");
							if (syncDialog != null)
								syncDialog.cancel();
							if (pillowserver != null)
								pillowserver.Stop_server(PillowDayDataActivity.this);
						}
						return false;
					}
				});
			} else {
				showOpenbluetoothDialog();
			}
		} else {
			Toast.makeText(this, "绑定数据出错，请重新绑定", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 通过mac链接设备
	 * @param mac
	 */
	@SuppressLint("NewApi")
	private void connectDevice(final String mac) {
		// 开始链接设备并同步数据 in_iSelect 1代表同步数据， 2代表绑定设备， 3代表空中升级
		final BluetoothManager bluetoothManager = (BluetoothManager) this
				.getSystemService(Context.BLUETOOTH_SERVICE);
//		mBluetoothAdapter = bluetoothManager.getAdapter();
		String sensitive = "6,2,2";
		sensitive = Util.getSensitive(
				PreManager.instance().getAllSensitives(this), Integer
						.parseInt(PreManager.instance()
								.getBluetoothDevSensitive(this)));

		pillowserver = CopyOfPillowHelper.Getinstance(this, callback,
				sensitive);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				pillowserver.startConnectDev(1, mac);
			}
		}, 100);

	}

	/**
	 * 设置同步百分比的显示
	 * @param data
	 */
	private void setSyncData(String data) {
		if (syncDialog != null) {
			float percent = 0;
			try {
				percent = Float.parseFloat(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			int per = (int) percent;
			syncDialog.updataSyncValue(per);
		}
	}
	
	
	/**
	 * 硬件枕扣的回调信息
	 */
	private PillowCallback callback = new PillowCallback() {
		/**
		 * 8000:传入数据有误，提示需要先绑定在同步<br>
		 * 8001:没有找到设备<br>
		 * 8002:找到了多个设备<br>
		 * 8003:找到设备开始链接<br>
		 * 8004:未连接GATT服务端<br>
		 * 8005:未发现GATT服务<br>
		 * 8006:DEVICE_DOES_NOT_SUPPORT_UART<br>
		 * 9000:密码校验失败<br>
		 * 9001:密码校验成功,开始同步数据<br>
		 * 9002:更换绑定成功<br>
		 * 9003:更滑绑定失败<br>
		 * 9004:电池电量返回 msg.obj<br>
		 * 9005:固件版本返回 msg.obj<br>
		 * 9006:数据包个数 msg.obj<br>
		 * 9007:固件收到发送的升级命令返回<br>
		 * 9008:数据包接受中 百分比 <br>
		 * 9009:数据包接收结束<br>
		 * 9010:时间同步完成<br>
		 * 9011:恢复出厂设置完成<br>
		 * 9012:异常结束<br>
		 * 9020:询问是否继续绑定
		 */
		@SuppressLint("SimpleDateFormat")
		@Override
		public void getPillowcallback(Message msg) {
			// Log.i(TAG, "getPillowcallback " + msg.what);
			switch (msg.what) {
			case 8004:// 链接 断开
				if (isConnect) {

					if (isgotdata) {// 已经获得数据
						if (!isneedUpLoad) {
							ToastManager.getInstance(PillowDayDataActivity.this).show("同步完成");
							if(!PillowDayDataActivity.this.isFinishing()){
								syncDialog.cancel();
							}
						}
						PreManager.instance().saveSyncHardwareSleepDate(
								PillowDayDataActivity.this,
								PreManager.instance().getUserId(PillowDayDataActivity.this)
										+ new SimpleDateFormat("yyyy-MM-dd")
												.format(new Date()));
						if(!PillowDayDataActivity.this.isFinishing()){
							if (syncDialog.isShowing())
								syncDialog.cancel();
						}
					} else {// 获取数据失败，可能是校验错误
						if(!PillowDayDataActivity.this.isFinishing()){
							if (syncDialog.isShowing()) {
								syncDialog.updataViews(2);
							}
						}
					}
					if (pillowserver != null)
						pillowserver.Stop_server(PillowDayDataActivity.this);

				} else {
					if (pillowserver != null)
						pillowserver.Stop_server(PillowDayDataActivity.this);
					if (syncDialog != null)
						connectDevice(PreManager.instance()
								.getBundbluetoothPillow(PillowDayDataActivity.this));
				}
				break;
			case 8006:// 用于重复链接
				if (pillowserver != null)
					pillowserver.Stop_server(PillowDayDataActivity.this);
				if (syncDialog != null)
					connectDevice(PreManager.instance().getBundbluetoothPillow(
							PillowDayDataActivity.this));
				break;
			case 8007:
				isConnect = true;
				break;
			case 9001:// 密码校验成功,开始同步数据
				break;
			case 9009:// 数据包接收结束
				isgotdata = true;
				syncDialog.updataSyncValue(100);
				break;
			case 9008:// 同步数据的进度
				syncDialog.updataViews(1);// changeSyncLoading();
				setSyncData(msg.obj.toString());
				break;
			case 9016:// 有新的升级信息
				PreManager.instance().saveSyncHardwareSleepDate(
						PillowDayDataActivity.this,
						PreManager.instance().getUserId(PillowDayDataActivity.this)
								+ new SimpleDateFormat("yyyy-MM-dd")
										.format(new Date()));
				isneedUpLoad = true;
				syncDialog.updataViews(3);
				break;
			default:
				break;
			}
		}

		@Override
		public void getPillowError(int errorcode, String errormsg) {
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public void pillowData(byte[] buffer) {
			PreManager.instance().saveSyncHardwareSleepDate(
					PillowDayDataActivity.this,
					PreManager.instance().getUserId(PillowDayDataActivity.this)
							+ new SimpleDateFormat("yyyy-MM-dd")
									.format(new Date()));
			saveDataToFile(buffer);
		}
	};

	/**
	 * 保存数据
	 * 
	 * @param buffer
	 */
	@SuppressLint("SimpleDateFormat")
	private void saveDataToFile(byte[] buffer) {
		if (buffer == null || buffer.length == 0)
			return;
		List<ModelsValues> Modellist = BluetoothDataFormatUtil.format1(buffer);
		// 先存储总的buffer为.dat
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datName = sdf.format(new Date()) + ".dat";
		String datPath = PillowDataOpera.saveDataToSDcard(buffer,
				datName);
		handleData(false, Modellist, datPath, datName);

	}

	/**
	 * 处理硬件数据
	 * 
	 * @param batIsUpload
	 * @param Modellist
	 * @param datPath
	 */
	private void handleData(boolean batIsUpload, List<ModelsValues> Modellist,
			String datPath, String datName) {
		List<ModelsValues> pModellist = new ArrayList<ModelsValues>();
		if (Modellist == null || Modellist.size() == 0) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					clearView(TimeFormatUtil.getYesterDay("yyyy-MM-dd"));
					mOneKeyDialog.show();
					mOneKeyDialog.setTitle("昨晚枕扣未检测到您的睡眠");
				}
			});
			uploadDatFile(datPath, new ArrayList<String>());
			return;
		}
		List<String> list = new ArrayList<String>();
		for (ModelsValues modelsValues : Modellist) {
			if (modelsValues == null || modelsValues.mBuf == null
					|| modelsValues.mBuf.length == 0)
				continue;
			try {
				long dateTime = modelsValues.mtime;
				String sqlDate = TimeFormatUtil.formatTime1(dateTime,
						"yyyy-MM-dd");
				list.add(sqlDate);
				boolean isExist = PillowDataOpera.queryDataExistFromSQL(
						this.getApplicationContext(), sqlDate);
				if (isExist) {
					PillowDataModel oldData = PillowDataOpera.queryDataFromSQL(
							this.getApplicationContext(), sqlDate);
					if (oldData != null && oldData.getBfr() != null
							&& oldData.getBfr().length > 0) {
						XpillowInterface inter = new XpillowInterface();

						int which = inter.OneDayJudge(modelsValues.mBuf,
								modelsValues.mBuf.length, oldData.getBfr(),
								oldData.getBfr().length);
						if (which == 2 || which == -1) {
							continue;
						}
					}
				}
				String modelPath = PillowDataOpera.saveDataToSDcard(
						modelsValues.mBuf, sqlDate + ".model");
				PillowDataModel model = new PillowDataModel();
				model.setDate(sqlDate);
				model.setBfr(modelsValues.mBuf);
				model.setFileName(sqlDate + ".model");
				model.setIsUpload("0");
				if (batIsUpload)
					model.setDatIsUpload("1");
				else
					model.setDatIsUpload("0");
				model.setDatFileName(datName);
				if (isExist)
					PillowDataOpera.updateDataToSQL(
							this.getApplicationContext(), model);
				else
					PillowDataOpera.insertDataToSQL(
							this.getApplicationContext(), model);
				if (!TextUtils.isEmpty(modelPath))// 跟服务器保持一致，上传日期时间传long型（按秒来计算的long型）
					uploadDataToService(modelsValues, String.valueOf(dateTime),
							modelPath, sqlDate);
			} catch (Exception e) {
			}
			pModellist .add(modelsValues);
		}
		
		uploadDatFile(datPath, list);
		punchForPillowData(pModellist);
	}

	/** 上传dat文件@param datPath */
	private void uploadDatFile(String datPath, final List<String> dates) {
		UploadHardwareAllDayParamClass mParam = new UploadHardwareAllDayParamClass();
		mParam.path = datPath;
		mParam.my_int_id = PreManager.instance().getUserId(this);
		mParam.date_of_data = String.valueOf(TimeFormatUtil.formatTime(
				System.currentTimeMillis(), "yyyyMMddHHmmss"));
		new SleepDataProClass(this).UploadHardwareAllDay(mParam,
				new InterfaceUploadHardwareAllDayCallBack() {
					@Override
					public void onSuccess(int iCode, String strSuccMsg) {
						updateSqlState(dates, "1");
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						updateSqlState(dates, "0");
					}
				});
	}

	/**
	 * 将获取的硬件文件信息存入数据库
	 * @param dates 时间
	 * @param state 是否上传成功的状态
	 */
	private void updateSqlState(List<String> dates, String state) {
		for (String date : dates) {
			PillowDataModel model = new PillowDataModel();
			model.setDate(date);
			model.setDatIsUpload(state);
			PillowDataOpera.updateDataToSQL(getApplicationContext(),
					model);
		}
	}

	/**
	 * 上传数据到服务器备份
	 * 
	 * @param modelsValues
	 * @param date
	 */
	private void uploadDataToService(ModelsValues modelsValues, String date,
			String modelPath, final String sqlDate) {
		SleepDataHead datahead = BluetoothDataFormatUtil.format2(modelsValues,
				10);
		UploadHardwareDataParamClass1 param = new UploadHardwareDataParamClass1();
		param.awakecount = String.valueOf(datahead.AwakeCount);
		param.awakenogetupcount = String.valueOf(datahead.AwakeNoGetUpCount);
		param.awaketimesleep = String.valueOf(datahead.AwakeTime_Sleep);
		param.date = date;
		param.deepsleep = String.valueOf(datahead.Deep_Sleep);
		param.file = modelPath;
		param.getuptime = String.valueOf(datahead.GetUpTime);
		param.gotobedtime = String.valueOf(datahead.GoToBedTime);
		param.insleeptime = String.valueOf(datahead.InSleepTime);
		param.listlength = "0";
		param.my_int_id = PreManager.instance().getUserId(this);
		param.onbed = String.valueOf(datahead.OnBed);
		param.outsleeptime = String.valueOf(datahead.OutSleepTime);
		param.shallowsleep = String.valueOf(datahead.Shallow_Sleep);
		param.sleepbak1 = String.valueOf(datahead.SleepBak1);
		param.Sleepbak2 = String.valueOf(datahead.SleepBak2);
		param.tosleep = String.valueOf(datahead.ToSleep);
		param.totalsleeptime = String.valueOf(datahead.TotalSleepTime);
		param.user_location_x = PreManager.instance().getLocation_x(this);
		param.user_location_y = PreManager.instance().getLocation_y(this);
		param.xstart = String.valueOf(datahead.XStart);
		param.xstop = String.valueOf(datahead.XStop);
		param.ymax = String.valueOf(datahead.YMax);
		new CommunityProcClass(this).UploadHardwareSleepData(param,
				new InterfaceUploadHardwareDataCallBack1() {

					@Override
					public void onError(int icode, String strErrMsg) {
					}

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						PillowDataModel model = new PillowDataModel();
						model.setIsUpload("1");
						model.setDate(sqlDate);
						PillowDataOpera.updateDataToSQL(PillowDayDataActivity.this, model);
					}
				});
	}
	
	
	/**
	 * 显示提示打开蓝牙的dialog
	 */
	private void showOpenbluetoothDialog() {

		final MyDialog exitDialog = new MyDialog(this, 0, 0,
				R.layout.dialog_open_blutooth, R.style.bottom_animation,
				Gravity.BOTTOM, 0.96f, 0.0f);
		exitDialog.setCanceledOnTouchOutside(false);

		// 得到view中的控件
		TextView dialog_title = (TextView) exitDialog
				.findViewById(R.id.dialog_title);

		dialog_title.setText("Orange枕扣需要使用蓝牙功能，是否允许？");

		Button btn_allow = (Button) exitDialog
				.findViewById(R.id.btn_allow_open_bluetooth);
		Button btn_refuse = (Button) exitDialog
				.findViewById(R.id.btn_refuse_open_bluetooth);

		btn_allow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IS_NEED_TO_OPEN_BLUETOOTH = true;
				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
						.getDefaultAdapter();
				mBluetoothAdapter.enable();
				exitDialog.cancel();
			}
		});

		btn_refuse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				exitDialog.cancel();
			}
		});

		// 显示
		exitDialog.show();
	}

	/**
	 * 开始同步数据
	 */
	private void startToConnectdEV() {
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported,
					Toast.LENGTH_SHORT).show();
		} else {
			syncDialog = new SyncAlertDialog(this,
					R.style.bottom_animation, this);
			syncDialog.show();
			isgotdata = false;
			connectDevice(PreManager.instance()
					.getBundbluetoothPillow(this));
		}
	}

	/**
	 * 蓝牙具体状态改变时的具体操作
	 */
	BroadcastReceiver bluetoothState = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (!PillowDayDataActivity.this.isFinishing()) {
				String stateExtra = BluetoothAdapter.EXTRA_STATE;
				int state = intent.getIntExtra(stateExtra, -1);
				switch (state) {
				case BluetoothAdapter.STATE_TURNING_ON:
					break;
				case BluetoothAdapter.STATE_ON:
					if (IS_NEED_TO_OPEN_BLUETOOTH) {
						IS_NEED_TO_OPEN_BLUETOOTH = false;
						startToConnectdEV();
					}
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					break;
				case BluetoothAdapter.STATE_OFF:
					if(!PillowDayDataActivity.this.isFinishing()){
						if (syncDialog != null
								&& syncDialog.isShowing()
								) {
							syncDialog.updataViews(2);
						}
					}
					if (pillowserver != null)
						pillowserver.Stop_server(PillowDayDataActivity.this);
					break;
				}
			}
		}
	};
	
	/**
	 * 硬件签到
	 */
	private void punchForPillowData(List<ModelsValues> Modellist){
		//1获取硬件的信息，打卡的日期是硬件日期的后一天
		//2强硬件的打卡信息存入一个list
		//3遍历为每一天的硬件信息，为确切的时间打卡
		List<SignInData> list = new ArrayList<SignInData>();
			for (ModelsValues modelsValues : Modellist) {
				if (modelsValues == null || modelsValues.mBuf == null
						|| modelsValues.mBuf.length == 0)
					continue;
				
				SleepDataHead datahead = null;
				String sqlDate = null;
				try {
					datahead = BluetoothDataFormatUtil.format3(
							modelsValues.mBuf, 10);
					
					long dateTime = modelsValues.mtime;
					sqlDate = TimeFormatUtil.formatTime1(dateTime,
							"yyyy-MM-dd");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(datahead!=null && sqlDate !=null){
					SignInData mSignInData = new SignInData();
					mSignInData.setDate(sqlDate);
					mSignInData.setGoBedTime((datahead.GoToBedTime*1000)+"");
					mSignInData.setHowLongSleepTime(datahead.ToSleep);
					mSignInData.setHowLongWakeTime(datahead.AwakeTime_Sleep - (datahead.ToSleep + datahead.AwakeNoGetUpCount));
					mSignInData.setOutBedTime((datahead.GetUpTime*1000)+"");
					mSignInData.setTrySleepTime(((datahead.InSleepTime - datahead.ToSleep * 60)*1000) +"");
					mSignInData.setWakeCount(datahead.AwakeCount);
					mSignInData.setWakeEarlyTime(0);
					mSignInData.setWakeUpTime((datahead.OutSleepTime*1000)+"");
					mSignInData.setShallowsleep(datahead.Shallow_Sleep);
					mSignInData.setDeepsleep(datahead.Deep_Sleep);
					mSignInData.setSoftOrOrange("1");
					list.add(mSignInData);
				}
			}
		
		if(list.size()>0){
			showProNotCancle();
			if(Util.checkNetWork(this)){
				for (SignInData signInData : list) {
					LogUtil.d("chen", "硬件数据"+signInData.getDate());
					if(signInData.getDate().equals(TimeFormatUtil.getYesterdayTime())){
						signInData.setUpload(1);
						signInPillowData(signInData);
					}else{
						signInData.setUpload(1);
					}
				}
				SignInDBOperation.initDB(this).updateSignInDataList(list, "1");
				mHandler.sendEmptyMessage(2);
				cancelProNotCancle();
				getPillowSigInFaildData();
			}else{
				LogUtil.d("chen", "网络不可用");
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setUpload(1);
				}
				SignInDBOperation.initDB(PillowDayDataActivity.this).updateSignInDataList(list, "1");
				mHandler.sendEmptyMessage(2);
				cancelProNotCancle();
			}
		}else{
			mHandler.sendEmptyMessage(1);
		}
	}
	//============================================================================================
	
	/**
	 * 将硬件信息上传服务器进行签到
	 * @param list
	 * @param signIn
	 */
	private void signInPillowData(final SignInData signIn){
		new XiangchengMallProcClass(this).signIn4_2_1(PreManager.instance().getUserId(this), "2", signIn, new InterfaceSignInCallBack4_2_1() {
			
			@Override
			public void onSuccess(String icode, JSONObject response) {
				try {
//					JSONObject json = new JSONObject(response.getString("report_data"));
//					signIn.setSignInId(json.getString("qiandaoid"));
					signIn.setResult(response.toString());
					SignInDBOperation.initDB(PillowDayDataActivity.this).updateSignInData(signIn, "1");//0软件1硬件
				} catch (Exception e) {
				}
				mHandler.sendEmptyMessage(2);
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
//				list.remove(0);
				signIn.setUpload(1);
				SignInDBOperation.initDB(PillowDayDataActivity.this).updateSignInData(signIn, "1");
			}
		}); 
	}
	
	private void getPillowSigInFaildData(){
		List<SignInData> list = SignInDBOperation.initDB(this).getUnUploadData();
		if(list.size()>0){
			for(SignInData mSignInData : list){
				signInPillowOherData(mSignInData);
			}
		}
	}
	
	private void signInPillowOherData(final SignInData signIn){
		if(signIn != null){
			
			new XiangchengMallProcClass(this).signIn4_2_1(PreManager.instance().getUserId(this), "2", signIn, new InterfaceSignInCallBack4_2_1() {
				
				@Override
				public void onSuccess(String icode, JSONObject response) {
					try {
						LogUtil.d("chen", signIn.getDate()+"签到成功");
						JSONObject json = new JSONObject(response.getString("report_data"));
						signIn.setSignInId(json.getString("qiandaoid"));
						signIn.setResult(response.toString());
						signIn.setUpload(0);
						SignInDBOperation.initDB(PillowDayDataActivity.this).updateSignInData(signIn, "1");//0软件1硬件
					} catch (Exception e) {
					}
				}
				
				@Override
				public void onError(String icode, String strErrMsg) {
				}
			});
		}
	}
	
	/**
	 * 硬件同步dialog的具体操作
	 */
	@Override
	public void OnSyncClick(int value) {
		switch (value) {
		case 0:
			MobclickAgent.onEvent(this, "647");
			if(!PillowDayDataActivity.this.isFinishing()){
				if (syncDialog != null)
					syncDialog.cancel();
			}
			if (pillowserver != null)
				pillowserver.Stop_server(PillowDayDataActivity.this);
			break;
		case 1:
			MobclickAgent.onEvent(this, "649");
			syncDialog.dismiss();
			break;
		case 2:
			MobclickAgent.onEvent(this, "648");
			boolean isOpen = BluetoothUtil.bluetoothIsOn(this);
			if (isOpen) {
				syncDialog.updataViews(0);
				isgotdata = false;
				isneedUpLoad = false;
				isConnect = false;
				connectDevice(PreManager.instance().getBundbluetoothPillow(
						this));
			} else {
				showOpenbluetoothDialog();
			}
			break;
		case 3:
			MobclickAgent.onEvent(this, "655");
			pillowserver.cancleDevUpload();
			if (syncDialog != null)
				syncDialog.cancel();
			break;
		case 4:
			pillowserver.ensureDevUpload();
			MobclickAgent.onEvent(this, "654");
			startActivity(new Intent(PillowDayDataActivity.this,

			PillowUpgradeActivity.class));
			if (syncDialog != null)
				syncDialog.cancel();
			break;
		default:
			break;
		}

	}
	
	/**
	 * 显示进度
	 */
	public void showProNotCancle() {
		if (pro2 == null) {
			pro2 = new ProgressUtils(this);
		}
		pro2.setCancelable(false);
		pro2.setCanceledOnTouchOutside(false);
		pro2.show();
	}

	/**
	 * 取消进度
	 */
	public void cancelProNotCancle() {
		if(!this.isFinishing()){
			if (pro2 != null) {
				pro2.cancel();
				pro2 = null;
			}
		}
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				getPillowDataByDate(TimeFormatUtil.getYesterDay("yyyy-MM-dd"));
				break;
			case 2:
				getPillowDataByDate(TimeFormatUtil.getYesterDay("yyyy-MM-dd"));
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}
		
	};
	
	 private void showPopupWindow(View view) {

	        // 一个自定义的布局，作为显示的内容
	        View contentView = LayoutInflater.from(this).inflate(
	                R.layout.pop_pillow_set, null);
	        final PopupWindow popupWindow = new PopupWindow(contentView,
	                Constant.screenWidht/5, LayoutParams.WRAP_CONTENT, true);
	        
	        // 设置按钮的点击事件
	        Button dateBtn = (Button) contentView.findViewById(R.id.btn_date);
	        dateBtn.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	popupWindow.dismiss();
	            	new CalenderSelectDialog(PillowDayDataActivity.this,getScreenWidth(),titleTv.getTag().toString(),new SelectDayDateListener() {
						@Override
						public void selectday(String dayDate) {
							titleTv.setTag(dayDate);
							getPillowDataByDate(dayDate);
							try {
								long day = new SimpleDateFormat("yyyy-MM-dd").parse(dayDate).getTime();
								Calendar calendar=Calendar.getInstance();
								calendar.setTime(new Date());
								String currentYear=new SimpleDateFormat("yyyy").format(calendar.getTime());
								String selectYear=new SimpleDateFormat("yyyy").format(day);
								if(currentYear.equals(selectYear))
									titleTv.setText(new SimpleDateFormat("MM月dd日").format(day));
								else
									titleTv.setText(dayDate);
							} catch (Exception e) {
								titleTv.setText(dayDate);
							}
						}
					}).show();
	            }
	        });
	        Button settingBtn = (Button) contentView.findViewById(R.id.btn_setting);
	        settingBtn.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	popupWindow.dismiss();
	            	Intent intent = new Intent(PillowDayDataActivity.this, BundPillowInfoActivity.class);
					startActivity(intent);
	            }
	        });

	        popupWindow.setTouchable(true);

	        popupWindow.setTouchInterceptor(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
	        });

	        popupWindow.setBackgroundDrawable(getResources().getDrawable(
	                R.color.cbg_color));

	        // 设置好参数之后再show
	        popupWindow.showAsDropDown(view);

	    }

	
}
