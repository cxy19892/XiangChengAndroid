package com.yzm.sleep.activity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.CorrelativeCellPhoneActivity;
import com.yzm.sleep.activity.community.ReleaseEditActivity;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.background.SignInDBOperation;
import com.yzm.sleep.background.SleepResult;
import com.yzm.sleep.bean.OccpationBean;
import com.yzm.sleep.crop.CropImageUI;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.model.MyDialog;
import com.yzm.sleep.model.RollPickerDialog;
import com.yzm.sleep.rectCrop.Crop;
import com.yzm.sleep.threadpool.SoftDataUpLoadTask;
import com.yzm.sleep.threadpool.ThreadPoolManager;
import com.yzm.sleep.threadpool.ThreadProgress;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.InterFaceUtilsClass.EditUserInfoParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceEditUserInfoCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadQiNiuTokenCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUserProfileUpdateCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadQiuTokenParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserBasicInfoClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UserProfileUpdateParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.InterfaceUpdateUserNicknameCallback;
import com.yzm.sleep.utils.InterFaceUtilsClassNew.UpdateUserNicknameParamClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.QiNiuFileManageClass;
import com.yzm.sleep.utils.QiNiuUploadTool;
import com.yzm.sleep.utils.SelectPhotoUtil;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.ToastManager;
import com.yzm.sleep.utils.UserManagerProcClass;
import com.yzm.sleep.utils.XiangchengProcClass;
import com.yzm.sleep.widget.CustomInputDialog;
import com.yzm.sleep.widget.CustomInputDialog.InputClass;

/**
 * 我的资料
 * 
 * @author tianxun
 * 
 */
public class MyDetailInfoActivity extends BaseActivity {
	public static final int GET_PICTURE_WALL_SUCCESS = 3;
	public static final int UPLOAD_DATA_TOCLOUD_SUCCESS = 10;
	public static final int UPLOAD_DATA_TOCLOUD_SOMEONEFAIL = 11;
	public static final int UPLOAD_DATA_TOCLOUD_FAIL = 12;

	private CircleImageView userHead;
	private MyDialog syncDataDialog;
	private String login_user_id, g_filename; // 图片名称
	private Uri g_imageUri; // 图片路径
	private int readPictureDegree = 0;
	private ProgressUtils pb;
	private Context context;
	private TextView nickname, gender, userAge, userJob, userHeight,
			userWeight, userPhone;
	private MyAlertDialog selsetPicDialog, modifyCorrePhoneDialog;
	private RollPickerDialog rollPickerDialog;

	/** 是否绑定手机 */
	private boolean isBoundPhone = false;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.DELETEDBANDLOGOUTCODE:
				logout();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydetail_info);
		login_user_id = PreManager.instance().getUserId(this);
		context = this;
		initView();
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("UserHomepage_Edit"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("UserHomepage_Edit"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		MobclickAgent.onPause(this);
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("个人信息");
		findViewById(R.id.rl_head_iocn).setOnClickListener(this);
		findViewById(R.id.rl_nickname).setOnClickListener(this);
		findViewById(R.id.rl_gender).setOnClickListener(this);
		findViewById(R.id.rl_age).setOnClickListener(this);
		findViewById(R.id.rl_job).setOnClickListener(this);
		findViewById(R.id.rl_height).setOnClickListener(this);
		findViewById(R.id.rl_weight).setOnClickListener(this);
		findViewById(R.id.rl_phone_num).setOnClickListener(this);
		findViewById(R.id.btn_person_exit).setOnClickListener(this);

		userHead = (CircleImageView) findViewById(R.id.user_pic);
		nickname = (TextView) findViewById(R.id.tv_user_nickname);
		gender = (TextView) findViewById(R.id.tv_user_gender);
		userAge = (TextView) findViewById(R.id.tv_user_age);
		userJob = (TextView) findViewById(R.id.tv_user_job);
		userHeight = (TextView) findViewById(R.id.tv_user_height);
		userWeight = (TextView) findViewById(R.id.tv_user_weight);
		userPhone = (TextView) findViewById(R.id.tv_user_phone);
	}

	@SuppressLint("SimpleDateFormat")
	private void initData() {
		final String profileKey = PreManager.instance().getUserProfileKey(this);
		ImageLoader.getInstance().displayImage(
				PreManager.instance().getUserProfileUrl(this), profileKey,
				userHead, MyApplication.headPicOptn,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						QiNiuUploadTool.getInstance().refreshDisCache(context,
								profileKey, userHead);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {

					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});
		nickname.setText(PreManager.instance().getUserNickname(this));
		String userGender = PreManager.instance().getUserGender(this);
		gender.setText("01".equals(userGender) ? "男"
				: ("02".equals(userGender) ? "女" : "傻傻分不清"));

		String birthday = PreManager.instance().getUserBirthday(context);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sd = new SimpleDateFormat("yyyy");
			String year = sd.format(sdf.parse(birthday).getTime());
			int y = Integer.parseInt(year);
			if (y > 1000) {
				Calendar c = Calendar.getInstance();
				int i = c.get(Calendar.YEAR);
				y = i - y;
			}
			userAge.setText(String.valueOf(y));
		} catch (Exception e) {
			Calendar c = Calendar.getInstance();
			int i = c.get(Calendar.YEAR);
			int y = i - 1990;
			userAge.setText(String.valueOf(y));
		}

		userJob.setText(PreManager.instance().getUserOccupationName(this));
		String boundPhoneNumber = PreManager.instance().getBoundPhoneNumber(
				this);
		if (!TextUtils.isEmpty(boundPhoneNumber)) {
			userPhone.setText(boundPhoneNumber);
			isBoundPhone = true;
		} else {
			userPhone.setText("立即绑定");
		}
		if (PreManager.instance().getUserHeight(context) > 0) {
			userHeight
					.setText((PreManager.instance().getUserHeight(context) + "cm")
							.replace(".0", ""));
		}
		if (PreManager.instance().getUserWeight(context) > -1) {
			userWeight
					.setText((PreManager.instance().getUserWeight(context) + "kg")
							.replace(".0", ""));
		}
	}

	/** 显示新增照片选择对话框 */
	private void selectPicture() {
		if (null == selsetPicDialog) {
			selsetPicDialog = new MyAlertDialog(this, R.style.bottom_animation);
		}
		selsetPicDialog.show();
		selsetPicDialog.setTV1("", View.GONE);
		selsetPicDialog.setTV2("拍照", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				selsetPicDialog.dismiss();
				tackphoto();
			}
		}, View.VISIBLE);
		selsetPicDialog.setTV3("从手机相册选取", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				selsetPicDialog.dismiss();
				getImgByStorage();
			}
		}, View.VISIBLE);
		selsetPicDialog.setTV4("", null, View.GONE);
		selsetPicDialog.setTVbottom("取消", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				selsetPicDialog.dismiss();
			}

		}, View.VISIBLE);
	}

	/** 照相 */
	private void tackphoto() {
		String Init = login_user_id;
		// 存储至DCIM文件夹
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File outputImage = new File(path, Init + ".jpg");
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
		}
		g_filename = outputImage.getPath();
		// 将File对象转换为Uri并启动照相程序
		g_imageUri = Uri.fromFile(outputImage);
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); // 照相
		PreManager.instance().savePhotoPathString(this, g_filename);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, g_imageUri); // 指定图片输出地址
		startActivityForResult(intent, Constant.TAKE_PHOTO); // 启动照相
	}

	/** 从相册选择 */
	public void getImgByStorage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		// 回收从系统文件中选择的图片
		startActivityForResult(intent, Constant.RESULT_LOAD_IMAGE);
	}

	/** 裁剪 @param uri */
	public void startPhotoZoom(Uri uri) {
		Intent mIntent = new Intent(this, CropImageUI.class);
		mIntent.putExtra("readPictureDegree", readPictureDegree);
		mIntent.putExtra("index", 1);
		mIntent.putExtra("uri", SelectPhotoUtil.newGetPath(this, uri)/*
																	 * uri.toString
																	 * ()
																	 */);
		startActivityForResult(mIntent, Constant.CROP_PHOTO);

	}

	/** 删除枕头睡眠相关数据 */
	private void deleLocalSleepPillowData() {
		// 删除硬件数据中的数据保留最后一条数据不删除
		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.execSQL("delete from " + MyTabList.PILLOW_SLEEP_DATA
					+ " where _id != (select _id from "
					+ MyTabList.PILLOW_SLEEP_DATA
					+ " order by date desc limit 1)");
		} catch (Exception e) {
			e.printStackTrace();
			// 当数据库被删除的时候会报错
		}
		db.close();

		PreManager.instance().setBluetoothDevSensitive(context, "0");
		PreManager.instance().setBundbluetoothPillow(this, "");
		PreManager.instance().setBluetoothPillowBatteryValue(this, "");
		PreManager.instance().setBluetoothPillowFirmwareVersion(this, "");
		PreManager.instance().saveUserIsAssess(this, "0");
		PreManager.instance().saveIsCompleteSleepPg(this,false);
		PreManager.instance().saveSMPGResult(this, "");
		PreManager.instance().saveSHXGPGResult(this, "");
		PreManager.instance().saveSMGLPGResult(this, "");
		PreManager.instance().saveSMHJPGResult(this, "");
		PreManager.instance().saveXLHDPGResult(this, "");
		PreManager.instance().saveJSON_SHXGPGResult(this, "");
		PreManager.instance().saveJSON_SMGLPGResult(this, "");
		PreManager.instance().saveJSON_SMHJPGResult(this, "");
		PreManager.instance().saveJSON_XLHDPGResult(this, "");
		PreManager.instance().saveGetupTime_Setting(this, "08:00");
		PreManager.instance().saveSleepTime_Setting(this, "00:30");
		// 删除睡眠相关文件
		FileUtil.deleteFile(new File(FileUtil.getSDPath() + "/data/yzm/pillow"));
	}

	private void logout() {
		deleLocalSleepPillowData();
		deleteDBAndLogout();// 删除软件睡眠数据并清空登录信息
		SignInDBOperation.initDB(context).clearSignInData();//delete signin data
		PreManager.instance().saveSleepPlan(context, "");
		PreManager.instance().saveComfortChoice(context, "");
		PreManager.instance().saveSearchKey(this, "");
		PreManager.instance().saveCommunitySearchKey(this, "");
	}

	/**
	 * 读取照片exif信息中的旋转角度
	 * 
	 * @param path
	 *            照片路径
	 * @return角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	private void getUploadPictureWallToken(final String path,
			final boolean isUploadPictureWall) {
		pb = new ProgressUtils(this);
		pb.setCanceledOnTouchOutside(false);
		pb.setMessage("上传中");
		pb.show();
		UploadQiuTokenParamClass mParam = new UploadQiuTokenParamClass();
		mParam.my_int_id = login_user_id;
		new QiNiuFileManageClass(this).GetUploadQiniuToken(mParam,
				new InterfaceUploadQiNiuTokenCallBack() {

					@Override
					public void onSuccess(int iCode, String token) {
						QiniuUploadProfile(path, token);
					}

					@Override
					public void onError(int icode, String strErrMsg) {

					}
				});
	}

	private void QiniuUploadProfile(String path, final String token) {
		File uploadFile = new File(path);
		// 用户头像key命名规范： user/avatar/用户id/图片名
		String profilekey = "user/avatar/" + login_user_id + "/"
				+ login_user_id + "_" + SleepUtils.getSystemCurrentTime()
				+ ".jpg";
		QiNiuUploadTool.getInstance().uploadFile(uploadFile, profilekey, token,
				new UpCompletionHandler() {
					@Override
					public void complete(String key, ResponseInfo info,
							JSONObject arg2) {
						if (info.isOK()) {
							updateUserProfile(login_user_id, key);
						}
					}
				}, null);
	}

	/**
	 * 上传头像
	 * 
	 * @param id
	 * @param profileKey
	 */
	private void updateUserProfile(String id, final String profileKey) {
		UserProfileUpdateParamClass mUserProfileUpdateParamClass = new UserProfileUpdateParamClass();
		mUserProfileUpdateParamClass.my_int_id = id;
		mUserProfileUpdateParamClass.profile_key = profileKey;
		new UserManagerProcClass(this).UserProfileUpdate(
				mUserProfileUpdateParamClass,
				new InterfaceUserProfileUpdateCallBack() {
					@Override
					public void onSuccess(int iCode, String profile) {
						PreManager.instance().saveUserProfileKey(context,
								profileKey);
						PreManager.instance().saveUserProfileUrl(context,
								profile);
						ImageLoader.getInstance().displayImage(
								PreManager.instance()
										.getUserProfileUrl(context),
								PreManager.instance()
										.getUserProfileKey(context), userHead,
								MyApplication.headPicOptn);
						if (pb != null) {
							pb.dismiss();
						}
						sendBroadcast(new Intent(
								Constant.RECEVER_USER_MESSAGE_UPDATE));
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						ToastManager.getInstance(context).show("头像上传失败");
					}
				});
	}

	// 同步完成之后显示
	private void showExitDialog() {
		final MyDialog exitDialog = new MyDialog(context, 0, 0,
				R.layout.dialog_exit_xiangcheng, R.style.bottom_animation,
				Gravity.BOTTOM, 1.0f, 0.0f);
		exitDialog.setCanceledOnTouchOutside(false);

		// 得到view中的控件
		TextView dialog_title = (TextView) exitDialog
				.findViewById(R.id.dialog_title);
		TextView dialog_msg = (TextView) exitDialog
				.findViewById(R.id.dialog_msg);
		dialog_title.setText("确认退出登录");
		dialog_msg.setText("退出登录后，手机将与枕头解除绑定，并清\n空您的个人数据，是否继续？");

		Button btn_goon_exit = (Button) exitDialog
				.findViewById(R.id.btn_goon_exit);
		Button btn_cancel = (Button) exitDialog.findViewById(R.id.btn_cancel);

		btn_goon_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.cancel();
				uploadSoftSleepData();
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.cancel();
			}
		});

		// 显示
		exitDialog.show();
	}

	/**
	 * 上传未上传的睡眠软件数据
	 */
	private void uploadSoftSleepData() {
		syncDataDialog = new MyDialog(context, 0, 0,
				R.layout.dialog_synchro_data, R.style.bottom_animation,
				Gravity.BOTTOM, 1.0f, 0.0f);
		syncDataDialog.setCanceledOnTouchOutside(false);

		List<SleepResult> datas = SleepUtils
				.getUnUploadDaySleepData(MyDetailInfoActivity.this);
		ThreadPoolManager tpManager = ThreadPoolManager.initstance();
		List<Runnable> list = new LinkedList<Runnable>();
		for (SleepResult result : datas) {
			SoftDataUpLoadTask task = new SoftDataUpLoadTask(result, this);
			list.add(task);
		}
		tpManager.setOnThreadProgress(new ThreadProgress() {

			@Override
			public void threadStart(int poolCunt) {
				syncDataDialog.show();
			}

			@Override
			public void threadEnd() {
				syncDataDialog.cancel();
				mHandler.sendEmptyMessage(Constant.DELETEDBANDLOGOUTCODE);
			}
		});
		if (list.size() > 0)
			tpManager.addThreadToPool(list).start();
		else
			mHandler.sendEmptyMessage(Constant.DELETEDBANDLOGOUTCODE);
	}

	/**
	 * 删除数据库数据和登录状态
	 */
	private void deleteDBAndLogout() {
		SleepUtils.deletSleepData(context);
		PreManager.instance().logoutClearLoginMsg(this);
		toastMsg("注销成功");
		sendBroadcast(new Intent(Constant.RECEVER_EXIT));
		EMChatManager.getInstance().logout();// 此方法为同步方法
		AppManager.getAppManager().finishAllActivityExceptOne(
				HomeActivity.class);
	}

	private void setCancelData() {
		Intent resultIntent = getIntent();
		UserBasicInfoClass userBean = new UserBasicInfoClass();
		userBean.uid = PreManager.instance().getUserId(context);
		userBean.nickname = PreManager.instance().getUserNickname(context);
		userBean.profile = PreManager.instance().getUserProfileUrl(context);
		userBean.profile_key = PreManager.instance().getUserProfileKey(context);
		userBean.gender = PreManager.instance().getUserGender(context);
		userBean.age = String.valueOf(PreManager.instance().getUserBirthday(
				context));
		userBean.occupation = PreManager.instance().getUserOccupation(context);
		userBean.user_internal_birthday = PreManager.instance()
				.getUserBirthday(context);
		resultIntent.putExtra("user_bean", userBean);
		setResult(Constant.MYDETAILINFO_RESULTCODE, resultIntent);
	}

	/**
	 * 修改昵称
	 */
	private void updateUserNickname(String my_int_id,
			final String my_int_nickname) {
		UpdateUserNicknameParamClass mParams = new UpdateUserNicknameParamClass();
		mParams.my_int_id = my_int_id;
		try {
			mParams.my_int_nickname = URLEncoder
					.encode(my_int_nickname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		new XiangchengProcClass(context).updateUserNickname(mParams,
				new InterfaceUpdateUserNicknameCallback() {

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						nickname.setText(my_int_nickname);
						PreManager.instance().saveUserNickname(context,
								my_int_nickname);
						toastMsg(strSuccMsg);
						sendBroadcast(new Intent(
								Constant.RECEVER_USER_MESSAGE_UPDATE));
						System.out.println("成功");
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						toastMsg(strErrMsg);
						System.out.println("成功");
					}
				});
	}

	/** 获取用户是否绑定手机 */
	private boolean preLoginBoundPhone() {
		String platformBoundMsg = PreManager.instance().getPlatformBoundMsg(
				context);
		if (platformBoundMsg.substring(platformBoundMsg.length() - 1).equals(
				"1")) {
			return true;
		}
		return false;
	}

	/**
	 * 显示修改关联手机对话框
	 */
	private void showModifyPhoneDialog() {
		if (null == modifyCorrePhoneDialog) {
			modifyCorrePhoneDialog = new MyAlertDialog(this,
					R.style.bottom_animation);
		}
		modifyCorrePhoneDialog.show();
		modifyCorrePhoneDialog.setTV1("手机号码已关联，是否更换", View.VISIBLE);
		modifyCorrePhoneDialog.setTV2("修改绑定", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				modifyCorrePhoneDialog.dismiss();
				Intent intent = new Intent(context,
						CorrelativeCellPhoneActivity.class);
				intent.putExtra("phone_num", PreManager.instance()
						.getBoundPhoneNumber(context));
				intent.putExtra("need_result", "1");
				startActivityForResult(intent, Constant.BUNDPHONEREQUSTCODE);
			}
		}, View.VISIBLE);
		modifyCorrePhoneDialog.setTV3("", null, View.GONE);
		modifyCorrePhoneDialog.setTV4("", null, View.GONE);
		modifyCorrePhoneDialog.setTVbottom("取消", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				modifyCorrePhoneDialog.dismiss();
			}

		}, View.VISIBLE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case Constant.TAKE_PHOTO:

				try {
					File out = new File(PreManager.instance()
							.getPhotoPathString(this));
					Uri uriPhoto = Uri.fromFile(out);
					beginCrop(uriPhoto);
				} catch (Exception e) {
				}
				break;
			case Constant.RESULT_LOAD_IMAGE:
				Uri uriImage = data.getData();
				beginCrop(uriImage);
				break;
			case Constant.CROP_PHOTO:
				if (resultCode == RESULT_OK) {
					String path = data.getStringExtra("cropImagePath");
					getUploadPictureWallToken(path, false);
				}
				break;
			case Crop.REQUEST_CROP:
				handleCrop(resultCode, data);
				break;
			default:
				break;
			}
		}

		if (requestCode == Constant.NICKNAMEEDITRESULT
				&& resultCode == (700 + Constant.TYPES_EDIT_NIACKNAME)) {
			String name = data.getStringExtra("user_nickname");
			if (name != null
					&& !"".equals(name)
					&& !name.equals(PreManager.instance().getUserNickname(
							context))) {
				// 更新昵称
				updateUserNickname(login_user_id, name);
			}
		}

		if (requestCode == Constant.BUNDPHONEREQUSTCODE
				&& resultCode == Constant.BUNDPHONERESULTCODE) {
			String phoneNum = data.getStringExtra("phone_num");
			if (phoneNum != null && !"".equals(phoneNum)) {
				userPhone.setText(phoneNum);
				PreManager.instance().saveBoundPhoneNumber(context, phoneNum);
			}
		}
		if(requestCode==Constant.JOBSELECTREQUESTCODE && resultCode==Constant.JOBSELECTRESULTCODE){
			OccpationBean bean=(OccpationBean) data.getSerializableExtra("occupation");
			if(bean !=null){
				if(!bean.getCode().equals(PreManager.instance().getUserOccupation(context))){
					updataUserInfo(2,bean.getCode());
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪
	 * 
	 * @param source
	 */
	private void beginCrop(Uri source) {
		Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
		new Crop(source).output(outputUri).asSquare().start(this);
	}

	/**
	 * 获取裁剪的图片
	 * 
	 * @param resultCode
	 * @param result
	 */
	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == RESULT_OK) {
			Uri uri = Crop.getOutput(result);
			String photoPath = "";
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1)
				photoPath = SelectPhotoUtil.newGetPath(context, uri);
			else
				photoPath = SelectPhotoUtil.oldGetPath(context, result);
			getUploadPictureWallToken(photoPath, false);
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(this, Crop.getError(result).getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 更新 个人信息 type修改的类型 1性别,2职业,3身高,4体重,5生日
	 */
	private void updataUserInfo(final int type, final String msg) {
		pb = new ProgressUtils(MyDetailInfoActivity.this);
		pb.show();
		EditUserInfoParamClass mParam = new EditUserInfoParamClass();
		mParam.my_int_id = login_user_id;
		mParam.my_int_gender = PreManager.instance().getUserGender(context);
		mParam.my_int_occupation = PreManager.instance().getUserOccupation(
				context);
		mParam.user_internal_height = String.valueOf(PreManager.instance()
				.getUserHeight(context));
		mParam.user_internal_weight = String.valueOf(PreManager.instance()
				.getUserWeight(context));
		mParam.user_internal_birthday = PreManager.instance().getUserBirthday(
				context);
		switch (type) {
		case 1:
			mParam.my_int_gender = msg;
			break;
		case 2:
			mParam.my_int_occupation = msg;
			break;
		case 3:
			mParam.user_internal_height = msg;
			break;
		case 4:
			mParam.user_internal_weight = msg;
			break;
		case 5:
			mParam.user_internal_birthday = msg;
			break;
		default:
			break;
		}
		new CommunityProcClass(this).editUserInfo(mParam,
				new InterfaceEditUserInfoCallBack() {
					@SuppressLint("SimpleDateFormat")
					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						switch (type) {
						case 1:
							PreManager.instance().saveUserGender(context, msg);
							gender.setText("01".equals(msg) ? "男" : "女");
							break;
						case 2:
							PreManager.instance().saveUserOccupation(context,
									msg);
							userJob.setText(PreManager.instance()
									.getUserOccupationName(msg));
							break;
						case 3:
							PreManager.instance().saveUserHeight(context, msg);
							userHeight.setText(msg + "cm");
							break;
						case 4:
							PreManager.instance().saveUserWeight(context, msg);
							userWeight.setText(msg + "kg");
							break;
						case 5:
							PreManager.instance()
									.saveUserBirthday(context, msg);
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyyMMdd");
								java.util.Date d = sdf.parse(msg);
								int birthNumber = Integer
										.parseInt(new SimpleDateFormat("yyyy")
												.format(d.getTime()));
								if (birthNumber > 1000) {
									Calendar c = Calendar.getInstance();
									int i = c.get(Calendar.YEAR);
									birthNumber = i - birthNumber;
								}
								userAge.setText(String.valueOf(birthNumber));
							} catch (Exception e) {
							}
							// 刷新首页日睡眠数据分析
							context.sendBroadcast(new Intent(
									Constant.RECEVER_USER_BIRTHDAY_UPDATE));
							break;
						default:
							break;
						}
						pb.cancel();
						toastMsg(strSuccMsg);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						pb.cancel();
						toastMsg(strErrMsg);
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setCancelData();
			AppManager.getAppManager().finishActivity();
		}
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			setCancelData();
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.rl_head_iocn:
			// showPopupWindow();
			selectPicture();
			break;
		case R.id.rl_nickname:
			Intent intentNickname = new Intent(context,
					ReleaseEditActivity.class);
			intentNickname.putExtra("types", Constant.TYPES_EDIT_NIACKNAME);
			intentNickname.putExtra("title", "您的昵称");
			intentNickname.putExtra("defaultStr", PreManager.instance()
					.getUserNickname(context));
			startActivityForResult(intentNickname, Constant.NICKNAMEEDITRESULT);
			break;
		case R.id.rl_gender:
			rollPickerDialog = new RollPickerDialog(MyDetailInfoActivity.this,new RollPickerDialog.RollCallBack() {
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
				}

				@Override
				public void setResaultRollString(String select1,
						String select2, String select3) {
					updataUserInfo(1, "男".equals(select1)?"01":("女".equals(select1)?"02":"03"));
				}
			});
			ArrayList<String> list = new ArrayList<String>();
			list.add("男");
			list.add("女");
			String userGender = PreManager.instance().getUserGender(this);
			int parseInt = Integer.parseInt(userGender);
			rollPickerDialog.SetStrDate(2, "选择性别", list, parseInt - 1, null, 0, null, 0,false);
			rollPickerDialog.show();
			break;
		case R.id.rl_age:
			rollPickerDialog = new RollPickerDialog(MyDetailInfoActivity.this,new RollPickerDialog.RollCallBack() {
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
					System.out.println("-------->" + "setResaultRoll");
					updataUserInfo(5, String.valueOf(select1) + (select2 < 10 ? "0" + String.valueOf(select2) : String.valueOf(select2))
							+ (select3 < 10 ? "0" + String.valueOf(select3) : String.valueOf(select3)));
				}

				@Override
				public void setResaultRollString(String select1,
						String select2, String select3) {
					
				}
			});
			String userBirthday = PreManager.instance().getUserBirthday(this);
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
			SimpleDateFormat sdf3 = new SimpleDateFormat("dd");
			
			Date parse;
			int Index1 = 1980;
			int Index2 = 1;
			int Index3 = 1;
			try {
				parse = sdf0.parse(userBirthday);
				Index1 = Integer.parseInt(sdf1.format(parse));
				Index2 = Integer.parseInt(sdf2.format(parse));
				Index3 = Integer.parseInt(sdf3.format(parse));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Time time = new Time("GMT+8");    
	        time.setToNow();
	        int year = time.year;
	        int month = time.month;
	        int monthDay = time.monthDay;
	        System.out.println( year +"年" + month +"月" + monthDay + "日");
	        
			rollPickerDialog.SetData(0, "选择年龄", new int[]{1920,Integer.parseInt(TimeFormatUtil.getTodayYearTime())}, Index1-1920, new int[]{1,12}, Index2-1, new int[]{1,31}, Index3-1);
			rollPickerDialog.show();
			break;
		case R.id.rl_height:
			rollPickerDialog = new RollPickerDialog(MyDetailInfoActivity.this,new RollPickerDialog.RollCallBack() {
				
				@Override
				public void setResaultRoll(int select1, int select2, int select3) {
					updataUserInfo(3, String.valueOf(select1));
				}

				@Override
				public void setResaultRollString(String select1,
						String select2, String select3) {
					
				}
			});
			int[] data = {55,230};
			int userHeight = (int) PreManager.instance().getUserHeight(this);
			
			rollPickerDialog.SetData(1, "设置身高(cm)", data, userHeight - 55, null, 0, null, 0);
			rollPickerDialog.show();
			
			break;
		case R.id.rl_weight:
			
			CustomInputDialog inputDialog = new CustomInputDialog(context, new CustomInputDialog.InputCallBack() {
				
				@Override
				public void setResault(String s) {
					updataUserInfo(4, s);
				}
			});
			InputClass inputClass = new InputClass();
			inputClass.title ="体重";
			inputClass.tag = "kg";
			inputClass.current = String.valueOf(PreManager.instance().getUserWeight(this));
			inputDialog.setData(inputClass);
			inputDialog.show();
			
			
			break;
		case R.id.rl_job:
			Intent intentJob = new Intent(context,SelectProfessionActivity.class);
			startActivityForResult(intentJob, Constant.JOBSELECTREQUESTCODE);
			break;
		case R.id.rl_phone_num:
			if (preLoginBoundPhone() || isBoundPhone) {
				showModifyPhoneDialog();
			} else {
				Intent intent = new Intent(context,
						CorrelativeCellPhoneActivity.class);
				intent.putExtra("phone_num", "");
				intent.putExtra("need_result", "1");
				startActivityForResult(intent, Constant.BUNDPHONEREQUSTCODE);
			}
			break;
		case R.id.btn_person_exit:
			showExitDialog();
			break;
		case R.id.btn_modify:
			Intent intent = new Intent(context,
					CorrelativeCellPhoneActivity.class);
			intent.putExtra("phone_num", PreManager.instance()
					.getBoundPhoneNumber(context));
			intent.putExtra("need_result", "1");
			startActivityForResult(intent, Constant.BUNDPHONEREQUSTCODE);
			if (modifyCorrePhoneDialog != null) {
				modifyCorrePhoneDialog.dismiss();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
