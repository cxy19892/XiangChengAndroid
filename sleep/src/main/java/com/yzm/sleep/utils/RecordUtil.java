package com.yzm.sleep.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

public class RecordUtil {
	private Context context;
	/** 存放录音文件的路径 */
	private File myRecAudioDir;// 存放录音的文件夹
	private File myRecAudioFile;// 存放片段的录音
	private File bestFile;// 综合的录音文件
	/** 音频文件的格式 */
	private final String SUFFIX = ".amr";
	private MediaRecorder mMediaRecorder01;
	private int second;// 记录录音的时间(分钟)
	private int minute;
	private Timer timer;
	private boolean isPause;// 是否处于暂停状态
	private boolean inThePause;
	private boolean isStopRecord;// 是否停止录音
	/** 记录需要合成的几段amr语音文件 **/
	private ArrayList<String> list;
	private String length1;// 计算文件大小（单位：KB)

	public RecordUtil(Context context) {
		this.context = context;
		myRecAudioDir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "Record");
		if (!myRecAudioDir.exists()) {
			myRecAudioDir.mkdirs();
			HLog.v("录音", "创建录音文件！" + myRecAudioDir.exists());
		}
		list = new ArrayList<String>();
	}

	private String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH：mm：ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}

	/**
	 * 开始录音
	 */
	public void start() {
		second = 0;
		minute = 0;
		list.clear();
		recordStart();
	}

	/**
	 * 停止录音
	 */

	public void stop() {
		timer.cancel();
		Toast.makeText(context, "录音时间:" + minute + "分" + second + "秒", 1)
				.show();
		// 这里写暂停处理的 文件！加上list里面 语音合成起来

		if (isPause) {
			// 在暂停状态按下结束键,处理list就可以了
			if (inThePause) {
				 getInputCollection(list, false);
			}
			// 在正在录音时，处理list里面的和正在录音的语音
			else {
				list.add(myRecAudioFile.getPath());
				 recodeStop();
				 getInputCollection(list, true);
			}

			// 还原标志位
			isPause = false;
			inThePause = false;
		}
		// 若录音没有经过任何暂停
		else {
			if (myRecAudioFile != null) {
				// 停止录音
				mMediaRecorder01.stop();
				mMediaRecorder01.release();
				mMediaRecorder01 = null;
				DecimalFormat df = new DecimalFormat("#.000");
				if (myRecAudioFile.length() <= 1024 * 1024) {
					// length1 = (myRecAudioFile.length() / 1024.0)+"";

					length1 = df.format(myRecAudioFile.length() / 1024.0) + "K";
				} else {
					// length1 = (myRecAudioFile.length() / 1024.0 / 1024)+"";
					// DecimalFormat df = new DecimalFormat("#.000");
					length1 = df
							.format(myRecAudioFile.length() / 1024.0 / 1024)
							+ "M";
				}
				HLog.d("Record", "录音文件大小：" + length1);
			}
		}
	}

	/**
	 * 暂停录音
	 */
	public void onPause() {
		isPause = true;
		// 已经暂停过了，再次点击按钮 开始录音，录音状态在录音中
		if (inThePause) {
			recordStart();
			inThePause = false;
		}
		// 正在录音，点击暂停,现在录音状态为暂停
		else { 
			// 当前正在录音的文件名，全程
			 recodeStop();
			// 计时停止
			timer.cancel();
			list.add(myRecAudioFile.getPath());
			inThePause = true;
		}
	}

	public void recordStart() {
		timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				second++;
				if (second >= 60) {
					second = 0;
					minute++;
				}

			}
		};
		timer.schedule(timerTask, 1000, 1000);
		String mMinute1 = getTime();
		// 创建音频文件
		myRecAudioFile = new File(myRecAudioDir, mMinute1 + SUFFIX);
		mMediaRecorder01 = new MediaRecorder();
		mMediaRecorder01.reset();
		// 设置录音为麦克风
		mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder01.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		// 录音文件保存这里
		mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());
		try {
			mMediaRecorder01.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaRecorder01.start();
	}

	private void recodeStop() {
		if (mMediaRecorder01 != null && !isStopRecord) {
			// 停止录音
			mMediaRecorder01.stop();
			mMediaRecorder01.release();
			mMediaRecorder01 = null;
		}

		timer.cancel();
	}

	/**
	 * @param isAddLastRecord
	 *            是否需要添加list之外的最新录音，一起合并
	 * @return 将合并的流用字符保存
	 */
	public void getInputCollection(List list, boolean isAddLastRecord) {
		String mMinute1 = getTime();
		// Toast.makeText(EX07.this,
		// "当前时间是:"+mMinute1,Toast.LENGTH_LONG).show();

		// 创建音频文件,合并的文件放这里
		bestFile = new File(myRecAudioDir, mMinute1 + SUFFIX);
		FileOutputStream fileOutputStream = null;

		if (!bestFile.exists()) {
			try {
				bestFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fileOutputStream = new FileOutputStream(bestFile);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件

		for (int i = 0; i < list.size(); i++) {
			File file = new File((String) list.get(i));
			HLog.d("list的长度", list.size() + "");
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] myByte = new byte[fileInputStream.available()];
				// 文件长度
				int length = myByte.length;
				// 头文件
				if (i == 0) {
					while (fileInputStream.read(myByte) != -1) {
						fileOutputStream.write(myByte, 0, length);
					}
				}

				// 之后的文件，去掉头文件就可以了
				else {
					while (fileInputStream.read(myByte) != -1) {

						fileOutputStream.write(myByte, 6, length - 6);
					}
				}

				fileOutputStream.flush();
				fileInputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// 结束后关闭流
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 合成一个文件后，删除之前暂停录音所保存的零碎合成文件
		deleteListRecord(isAddLastRecord);
	}

	/**
	 * 删除录音片段
	 * 
	 * @param isAddLastRecord
	 */
	private void deleteListRecord(boolean isAddLastRecord) {
		for (int i = 0; i < list.size(); i++) {
			File file = new File((String) list.get(i));
			if (file.exists()) {
				file.delete();
			}
		}
		// 正在暂停后，继续录音的这一段音频文件
		if (isAddLastRecord) {
			myRecAudioFile.delete();
		}
	}

	// 结束后需要释放资源
	public void onDestroy() {
		if (mMediaRecorder01 != null && !isStopRecord) {
			// 停止录音
			mMediaRecorder01.stop();
			mMediaRecorder01.release();
			mMediaRecorder01 = null;
		}
	}

}
