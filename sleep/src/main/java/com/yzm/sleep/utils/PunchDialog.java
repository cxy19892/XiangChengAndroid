package com.yzm.sleep.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SleepResult;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.model.RollPickerDialog.RollCallBack;
import com.yzm.sleep.threadpool.SoftDataUpLoadTask;
import com.yzm.sleep.threadpool.ThreadPoolManager;
import com.yzm.sleep.threadpool.ThreadProgress;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceGetPunchDayCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceUpLoadPunchDayCallback;

public class PunchDialog extends BaseActivity implements OnClickListener, RollCallBack{

	
	private TextView tvInSleep, tvOutSleep, tvTitle;
	private Button btnSave;
	private View focusView = null;
	private SoftDayData dayData = null;
	private ProgressUtils pro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_punch);
		initView();
		initData();
		getpunchDays();
	}

	private void initData() {
		String date = CalenderUtil.getYesterdayDate();
		dayData = SleepUtils.getDaySleepData(this, date, "0");
		if(dayData != null && !TextUtils.isEmpty(dayData.getSleepTime()) && !TextUtils.isEmpty(dayData.getGetUpTime())){
			tvInSleep.setText(dayData.getSleepTime());
			tvOutSleep.setText(dayData.getGetUpTime());
		}else{
			tvOutSleep.setText(TimeFormatUtil.getNowTime());
		}
		
	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.punch_title);
		tvInSleep = (TextView) findViewById(R.id.punch_insleep);
		tvOutSleep = (TextView) findViewById(R.id.punch_outsleep);
		btnSave = (Button) findViewById(R.id.punch_enter);
		tvInSleep.setOnClickListener(this);
		tvOutSleep.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		tvTitle.setText("我已经连续打卡"+PreManager.instance().getUserDakaDays(this)+"天");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.punch_insleep:
			focusView = v;
			String getsleeptime;
			if(!TextUtils.isEmpty(tvInSleep.getText().toString())){
				getsleeptime = tvInSleep.getText().toString();
			}else{
				getsleeptime = "00:01";
			}
			changeTheTime("入睡时间", getsleeptime);
			break;

		case R.id.punch_outsleep:
			focusView = v;
			String getuptime;
			if(!TextUtils.isEmpty(tvOutSleep.getText().toString())){
				getuptime = tvOutSleep.getText().toString();
			}else{
				getuptime = TimeFormatUtil.getNowTime();
			}
			changeTheTime("醒来时间", getuptime);
			break;
			
		case R.id.punch_enter:
			String insleepTime = tvInSleep.getText().toString();
			String outSleepTime = tvOutSleep.getText().toString();
			if(TextUtils.isEmpty(insleepTime)){
				showTextToast("请确认睡觉时间");
				return;
			}
			if(TextUtils.isEmpty(outSleepTime)){
				showTextToast("请确认起床时间");
				return;
			}
			dayData.setSleepTime(insleepTime);
			dayData.setGetUpTime(outSleepTime);
			uploadSleepSoftData();
				
				//2刷新数据库
				//3关闭当前页面
			break;
		default:
			break;
		}
	}
	
	private boolean isDialogDissmiss = true;
	private void changeTheTime(String title, String gettime){
		if(isDialogDissmiss){
			RollPickerDialog tdialog = new RollPickerDialog(this, this);
			isDialogDissmiss = false;
			int hour = 0;
			int min = 0;
			try {
				hour = Integer.parseInt(gettime.split(":")[0]);
				min  = Integer.parseInt(gettime.split(":")[1]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tdialog.SetData(1, title, new int[]{0,23}, hour, new int[]{0,59}, min, null, 0);//(title, hour, min);
			tdialog.show();
			tdialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					isDialogDissmiss = true;
				}
			});
		}
	}

	
	 private void showTextToast(String msg) {
	       Toast.makeText(this, msg, 0).show();
	    }
	 
	 private void getpunchDays(){
		 if(checkNetWork(this)){
			 new XiangchengProcClass(this).getGetPunchDays(PreManager.instance().getUserId(this), new InterfaceGetPunchDayCallback() {
				
				@Override
				public void onSuccess(int icode, String days) {
					PreManager.instance().saveUserDadaDays(PunchDialog.this,days);
					tvTitle.setText("我已经连续打卡"+days+"天");
				}
				
				@Override
				public void onError(int icode, String strErrMsg) {
					
					
				}
			});
		 }else{
			 tvTitle.setText("我已经连续打卡"+PreManager.instance().getUserDakaDays(PunchDialog.this)+"天");
		 }
	 }
	 /**
	  * 上传一天的睡眠数据到服务器
	  * @param strSleepTime
	  * @param strGetUpTime
	  */
	 private void uploadSleepSoftData(){
		 String insleepString  = dayData.getDate() + " " + tvInSleep.getText().toString();
			String outsleepString = dayData.getDate() + " " + tvOutSleep.getText().toString();
			try {
				dayData.setSleepTimeLong(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(insleepString).getTime()));
				dayData.setGetUpTimeLong(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(outsleepString).getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dayData = DateOperaUtil.comperaDate(dayData);
			saveDaySleepData(dayData, "0");
		    upLoadPunchDays();
	    	List<SleepResult> datas = SleepUtils
					.getUnUploadDaySleepData(this);
			ThreadPoolManager tpManager = ThreadPoolManager.initstance();
			List<Runnable> list = new LinkedList<Runnable>();
			for (SleepResult result : datas) {
				SoftDataUpLoadTask task = new SoftDataUpLoadTask(result, this);
				list.add(task);
			}
			tpManager.setOnThreadProgress(new ThreadProgress() {

				@Override
				public void threadStart(int poolCunt) {
//					showPro();
				}
				@Override
				public void threadEnd() {
//					cancelPro();
					setResult(101);
					
				}
			});
			if (list.size() > 0)
				tpManager.addThreadToPool(list).start();
			
			setResult(101);
			PunchDialog.this.finish();
			
	 }
	 
	 	/**
		 * 显示进度
		 */
		public void showPro() {
			if (pro == null) {
				pro = new ProgressUtils(this);
			}
			pro.setCanceledOnTouchOutside(false);
			pro.show();
		}

		/**
		 * 取消进度
		 */
		public void cancelPro() {
			if (pro != null) {
				pro.cancel();
				pro = null;
			}
		}
		
	 /**
		 * 保存软件记录睡眠数据
		 * 
		 * @param m_list
		 */
		@SuppressLint("SimpleDateFormat")
		private void saveDaySleepData(SoftDayData dayData, String isupload) {
			MyDatabaseHelper helper = MyDatabaseHelper
					.getInstance(this);
			MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),
					MyTabList.SLEEPTIME);

			String date = dayData.getDate();
			
			ContentValues cv = new ContentValues();
			cv = new ContentValues();
			cv.put("date", date);
			cv.put("starttime", "");
			cv.put("endtime", "");
			cv.put("sleeptime", dayData.getSleepTime());
			cv.put("uptime", dayData.getGetUpTime());
			cv.put(MyTabList.TableDay.ORGSLEEPTIME.getCloumn(), dayData.getSleepTime());
			cv.put(MyTabList.TableDay.ORGUPTIME.getCloumn(), dayData.getGetUpTime());
			// cv.put(MyTabList.TableDay.ISCHANGE.getCloumn(), "1");
			// 下载的数据默认为没有修改。不设值。数据库默认为0
			cv.put(MyTabList.TableDay.RECORD_STATE.getCloumn(), "4");
			cv.put(MyTabList.TableDay.ISUPLOAD.getCloumn(), isupload);
			cv.put("sleeptimelong", dayData.getSleepTimeLong());
			cv.put("uptimelong", dayData.getGetUpTimeLong());
			cv.put("uptimelong", dayData.getGetUpTimeLong());
			cv.put("ispunch", "1");
			if (operate.queryIsExist(date))
				operate.update(cv, "date = ?", new String[] { date });
			else
				operate.insert(cv);
		}
		
		 private void upLoadPunchDays(){
			 if(checkNetWork(this)){
				 new XiangchengProcClass(this).getUpLoadPunchDays(PreManager.instance().getUserId(this), TimeFormatUtil.getTodayTimeyyyyMMdd(), new InterfaceUpLoadPunchDayCallback() {
					
					@Override
					public void onSuccess(int icode, String days) {
						PreManager.instance().saveUserDadaDays(PunchDialog.this,days);
						tvTitle.setText("我已经连续打卡"+days+"天");
					}
					@Override
					public void onError(int icode, String strErrMsg) {
						
						
					}
				});
			 }
		 }

	@Override
	public void setResaultRoll(int select1, int select2, int select3) {
		if(focusView == tvInSleep){
			tvInSleep.setText((select1<10 ? ("0"+select1):select1) +":"+(select2<10 ? ("0"+select2) : select2));
		}else if(focusView == tvOutSleep){
			tvOutSleep.setText((select1<10 ? ("0"+select1):select1) +":"+(select2<10 ? ("0"+select2) : select2));
		}
	}

	@Override
	public void setResaultRollString(String select1, String select2,
			String select3) {
		// TODO Auto-generated method stub
		
	}
	 
	 
}
