package com.yzm.sleep.utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.activity.pillow.PillowDataOpera;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.MytabOperate;
import com.yzm.sleep.background.SleepResult;
import com.yzm.sleep.bean.HardwareSleepDataBean;
import com.yzm.sleep.model.PillowDataModel;
import com.yzm.sleep.threadpool.SoftDataUpLoadTask;
import com.yzm.sleep.threadpool.ThreadPoolManager;
import com.yzm.sleep.threadpool.ThreadProgress;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDownloadHardwareDataCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserSleepDataListClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceRecentlySevenDaysDataCallback;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;


/**
 *  先上传未上传的用户数据，然后再下载用户的软件数据和硬件数据
 * @author Administrator
 *
 */
public class DownloadUserData {

	private Context mContext;
	private static DownloadUserData DOWNLOAD;
	private Handler mHandler;
	public static final int DOWNLOAD_SLEEP_DATA_SUCCESS = 5;
	public static final int DOWNLOAD_SLEEP_DATA_ERROR = 6;
	public static final int DOWNLOAD_SLEEP_DATA_START = 7;
	
	
	private DownloadUserData(Context mContext, Handler mHandler){
		this.mContext = mContext;
		this.mHandler = mHandler;
	}
	
	public static DownloadUserData getInstance(Context Context, Handler mHandler){
		if(DOWNLOAD == null){
			DOWNLOAD = new DownloadUserData(Context, mHandler);
		}
		return DOWNLOAD;
	}
	
	
	/**
	 * 上传未上传的睡眠软件数据
	 */
	public void startUploadAndDownload() {
		List<SleepResult> datas = SleepUtils
				.getUnUploadDaySleepData(mContext);
		ThreadPoolManager tpManager = ThreadPoolManager.initstance();
		List<Runnable> list = new LinkedList<Runnable>();
		for (SleepResult result : datas) {
			SoftDataUpLoadTask task = new SoftDataUpLoadTask(result, mContext);
			list.add(task);
		}
		tpManager.setOnThreadProgress(new ThreadProgress() {

			@Override
			public void threadStart(int poolCunt) {
			}

			@Override
			public void threadEnd() {
				downloadSleepData();
			}
		});
		if (list.size() > 0)
			tpManager.addThreadToPool(list).start();
		else
			downloadSleepData();
	}

	/**
	 * 开始下载软件睡眠相关数据
	 */
	private void downloadSleepData() {
		new XiangchengProcClass(mContext).recentlySevenDaysData(PreManager.instance().getUserId(mContext), new InterfaceRecentlySevenDaysDataCallback() {
			
			@Override
			public void onSuccess(int icode, List<UserSleepDataListClass> soft_list,
					List<UserSleepDataListClass> pillow_list) {
				saveDaySleepData(soft_list);
				downloadHardwareData();
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				downloadHardwareData();
			}
		});
	}

	/**
	 * 保存软件记录睡眠数据
	 * 
	 * @param m_list
	 */
	@SuppressLint("SimpleDateFormat")
	private void saveDaySleepData(final List<UserSleepDataListClass> m_list) {
		MyDatabaseHelper helper = MyDatabaseHelper
				.getInstance(mContext);
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),
				MyTabList.SLEEPTIME);
		// 删除日视图数据
		operate.delete("record_state=?", new String[] { "4" });

		ContentValues cv = null;
		UserSleepDataListClass dataListClass = null;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		
		if (m_list != null) {
			for (int i = 0; i < m_list.size(); i++) {
				cv = new ContentValues();
				dataListClass = m_list.get(i);
				String a1 = "";
				try {
					SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyyMMdd");
					Date aaaDate = dateformat1.parse(dataListClass.date);
					SimpleDateFormat dateformat2 = new SimpleDateFormat(
							"yyyy-MM-dd");
					a1 = dateformat2.format(aaaDate);
				} catch (ParseException e) {
					mHandler.sendEmptyMessage(DOWNLOAD_SLEEP_DATA_ERROR);
				}
				cv.put("date", a1);
				cv.put("starttime", "");
				cv.put("endtime", "");
				String sleeptime = "";
				String uptime = "";
				
				if (dataListClass.sleep_point != null
						&& dataListClass.sleep_point.length() == 12) {
					try {
						sleeptime = sdf2.format(sdf1
								.parse(dataListClass.sleep_point));
					} catch (Exception e) {
					}
				}
				if (dataListClass.wakeup_point != null
						&& dataListClass.wakeup_point.length() == 12) {
					try {
						uptime = sdf2
								.format(sdf1.parse(dataListClass.wakeup_point));
					} catch (Exception e) {
					}
				}
				
				cv.put("sleeptime", sleeptime);
				cv.put("uptime", uptime);
				cv.put(MyTabList.TableDay.ORGSLEEPTIME.getCloumn(), sleeptime);
				cv.put(MyTabList.TableDay.ORGUPTIME.getCloumn(), uptime);
				// cv.put(MyTabList.TableDay.ISCHANGE.getCloumn(), "1");
				// 下载的数据默认为没有修改。不设值。数据库默认为0
				cv.put(MyTabList.TableDay.RECORD_STATE.getCloumn(), "4");
				// add 20151009
				try {
					SoftDayData dayData = new SoftDayData();
					dayData.setDate(a1);
					dayData.setSleepTime(sleeptime);
					dayData.setGetUpTime(uptime);
					dayData.setSleepTimeLong(String.valueOf(sdf1.parse(
							dataListClass.sleep_point).getTime()));
					dayData.setGetUpTimeLong(String.valueOf(sdf1.parse(
							dataListClass.wakeup_point).getTime()));
					dayData = DateOperaUtil.comperaDate(dayData);
					
					if (dayData.isChange()) // 如果数据的日期被修改过，那么上传状态要置为0，好重新上传
						cv.put(MyTabList.TableDay.ISUPLOAD.getCloumn(), "0");
					else
						cv.put(MyTabList.TableDay.ISUPLOAD.getCloumn(), "1");
					cv.put("sleeptimelong", dayData.getSleepTimeLong());
					cv.put("uptimelong", dayData.getGetUpTimeLong());
					cv.put("ispunch", "1");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (operate.queryIsExist(a1))
					operate.update(cv, "date = ?", new String[] { a1 });
				else
					operate.insert(cv);
			}
		}
	}

	/**
	 * 下载硬件数据
	 */
	private void downloadHardwareData() {
		new CommunityProcClass(mContext).downloadHardwareSleepData(PreManager
				.instance().getUserId(mContext),
				new InterfaceDownloadHardwareDataCallBack() {

					@Override
					public void onError(int icode, String strErrMsg) {
						mHandler.sendEmptyMessage(DOWNLOAD_SLEEP_DATA_ERROR);
					}

					@Override
					public void onSuccess(int icode,
							List<HardwareSleepDataBean> mList) {
						if (mList.size() > 0) {
							downloadHardwareFile(mList);
						} else {
							mHandler.sendEmptyMessage(DOWNLOAD_SLEEP_DATA_SUCCESS);
						}
					}

				});
	}

	/**
	 * 下载硬件数据文件model
	 * 
	 * @param mList
	 */
	private void downloadHardwareFile(final List<HardwareSleepDataBean> mList) {
		MyDatabaseHelper helper = MyDatabaseHelper
				.getInstance(mContext);
		MytabOperate operate = new MytabOperate(helper.getWritableDatabase(),
				MyTabList.PILLOW_SLEEP_DATA);
		// 删除日视图数据
		operate.delete(null, null);
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (HardwareSleepDataBean bean : mList) {
			if(bean != null){
				String date = TimeFormatUtil.formatTime1(Long.parseLong(bean.getDate()), "yyyy-MM-dd");
				String path = PillowDataOpera.getPillowDataPath(date + ".model");
				Map<String, String> map = new HashMap<String, String>();
				map.put("date", date);
				map.put("path", path);
				map.put("fileUrl", bean.getModelkey());
				map.put("success", "0");   //0表示还未下载，1表示下载成功，2表示下载失败
				list.add(map);
			}
		}
		AsyncHttpClient client = new AsyncHttpClient();
		for (int i = 0; i < list.size(); i++) {
			try {
				final int index = i;
				client.get(list.get(i).get("fileUrl"),
						new FileAsyncHttpResponseHandler(new File(list.get(i).get("path"))) {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									File file) {
								checkDownloadPools(list, index, "1");
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									Throwable arg2, File arg3) {
								checkDownloadPools(list, index, "2");
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
				checkDownloadPools(list, i, "2");
			}
		}
		
	}

	/**
	 *  插入当前下载线程的状态，并检查所有线程是否执行完毕
	 * @param list      所有的集合
	 * @param current   当前线程是第几个
	 * @param state		0是为完成，1是下载成功，2是下载失败
	 */
	private synchronized void checkDownloadPools(List<Map<String, String>> list, int current, String state){
		list.get(current).put("success", state);
		for (Map<String, String> map : list) {
			if(map.get("success").equals("0"))
				return;
		}
		insetAllOrangeData(list);
	}
	
	/**
	 * 开启线程读取文件插入数据库
	 * @param list
	 */
	private void insetAllOrangeData(final List<Map<String, String>> list){
		new Thread(){

			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					if("1".equals(list.get(i).get("success"))){
						byte[] bfr = PillowDataOpera.readDataToSDcard(list.get(i).get("date") + ".model");
						String date = list.get(i).get("date");
						insertHardwareData(bfr, date);
					}
				}
				mHandler.sendEmptyMessage(DOWNLOAD_SLEEP_DATA_SUCCESS);
			}
			
		}.start();
	}
	
	
	/**
	 * 插入硬件数据库
	 * 
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
		if (PillowDataOpera.queryDataExistFromSQL(mContext, date))
			PillowDataOpera.updateDataToSQL(mContext, model);
		else
			PillowDataOpera.insertDataToSQL(mContext, model);
	}
}
