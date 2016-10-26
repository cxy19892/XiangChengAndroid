package com.yzm.sleep.activity.community;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.Constant;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.LoginActivity;
import com.yzm.sleep.bean.GroupMessageBean;
import com.yzm.sleep.bean.HotTagBean;
import com.yzm.sleep.crop.CropImageUI;
import com.yzm.sleep.model.MyDialog;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.HLog;
import com.yzm.sleep.utils.InterFaceUtilsClass.AttentionGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.CancelAttentionGroupParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.EditGroupSummaryParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.GetGroupMessageParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceAttentionGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceCancelAttentionGroupCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceEditGroupSummaryCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetGroupMessageCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUpdateGroupIconCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadQiNiuTokenCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.UpdateGroupIconParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadQiuTokenParamClass;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PhotoAccessUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.QiNiuFileManageClass;
import com.yzm.sleep.utils.QiNiuUploadTool;
import com.yzm.sleep.utils.SelectPhotoUtil;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.utils.Util;

/**
 * 需要传入 1.groupId(string) ：小组 id 2.groupType (string) 访问小组类型 2咨询类 1普通
 * 
 * @author chen
 */
public class GroupInfoActivity extends BaseActivity {

	private Button Back;
	private TextView Title;
	private TextView GroupName, GroupCreator, GroupIntroduction;
	private Button CancleBtn;
	private GroupMessageBean mGroupMessageBean;
	private List<GroupMessageBean> mGroupInfoList;
	private RelativeLayout grouppicLayout;
	private CircleImageView groupPic;
	private String gid;
	private int readPictureDegree = 0;
	private String groupIconPath = "";
	private String my_int_id;
	private LinearLayout lin_guanzhu;
	private LinearLayout lin_fangwen;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_group_info);
		gid = getIntent().getStringExtra("groupId");
		initViews();
		showPro();
		getData();
	}

	private void initViews() {
		my_int_id = PreManager.instance().getUserId(this);
		Back = (Button) findViewById(R.id.back);
		lin_guanzhu = (LinearLayout) findViewById(R.id.lin_guanzhu);
		lin_fangwen = (LinearLayout) findViewById(R.id.lin_fangwen);
		Title = (TextView) findViewById(R.id.title);
		GroupName = (TextView) findViewById(R.id.ginfo_tv_name);
		GroupCreator = (TextView) findViewById(R.id.ginfo_tv_creator);
		GroupIntroduction = (TextView) findViewById(R.id.ginfo_tv_introduction);
		grouppicLayout = (RelativeLayout) findViewById(R.id.ginfo_rl_gicon);
		grouppicLayout.setOnClickListener(this);
		groupPic = (CircleImageView) findViewById(R.id.ginfo_imgv_gicon);
		groupPic.setOnClickListener(this);
		CancleBtn = (Button) findViewById(R.id.ginfo_btn_cancle);
		CancleBtn.setOnClickListener(this);
		lin_guanzhu.setOnClickListener(this);
		lin_fangwen.setOnClickListener(this);
		Title.setText("小组信息");
		Back.setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (!GroupInfoActivity.this.isFinishing()) {
				switch (msg.what) {
				case 0:
					setData();
					break;
				case 1:
					break;
				case 2:
					// 取消关注之后需要将关注数-1,用于回传给小组话题列表
					int focusNum = Integer.parseInt(mGroupMessageBean
							.getG_focus_num().equals("null") ? "0"
							: mGroupMessageBean.getG_focus_num());
					if (focusNum > 0) {
						mGroupMessageBean.setG_focus_num((focusNum - 1) + "");
					}
					sendcCancleAttentionGroup(gid);
					mGroupMessageBean.setFocus("0");
					setData();
					break;
				case 3:
					// 关注成功之后需要将关注数+1,用于回传给小组话题列表
					int focusNum2 = Integer.parseInt(mGroupMessageBean
							.getG_focus_num().equals("null") ? "0"
							: mGroupMessageBean.getG_focus_num());
					mGroupMessageBean.setG_focus_num((focusNum2 + 1) + "");
					sendAttentionBroadcast();
					mGroupMessageBean.setFocus("1");
					setData();
					// getData();
					break;
				case 4:
					setData();
					break;
				}
			}

		}

	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public ProgressUtils pro;

	/**
	 * 显示进度
	 */
	public void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	public void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}

	private void setData() {

		mGroupMessageBean = mGroupInfoList.get(0);
		findViewById(R.id.ginfo_rl_introduction).setOnClickListener(this);
		CancleBtn
				.setVisibility("1".equals(mGroupMessageBean.getIs_create()) ? View.GONE
						: View.VISIBLE);
		List<String> labers = new ArrayList<String>();
		List<HotTagBean> mLabers = mGroupMessageBean.getG_tag();
		int lenth = 0;
		for (HotTagBean hlab : mLabers) {
			lenth += hlab.getTagname().length();
			if (lenth >= 10) {
				break;
			}
			if (labers.size() >= 3) {
				break;
			}
			labers.add(hlab.getTagname());
		}
		GroupName.setText(mGroupMessageBean.getG_name());
		GroupCreator.setText(mGroupMessageBean.getGrouper());
		GroupIntroduction.setText(mGroupMessageBean.getG_intro());

		if ("1".equals(mGroupMessageBean.getFocus())) {// 已关注
			CancleBtn.setText("取消关注");
			CancleBtn.setBackgroundResource(R.drawable.button_usergroup);
		} else {// 未关注
			CancleBtn.setText("添加关注");
			CancleBtn.setBackgroundResource(R.drawable.button_login);
			CancleBtn.setTextColor(getResources()
					.getColor(R.color.ct_color));
		}
		ImageLoader.getInstance().displayImage(mGroupMessageBean.getG_ico(),
				mGroupMessageBean.getG_ico_key(), groupPic,
				MyApplication.headPicOptn);

		((TextView) findViewById(R.id.tv_guanzhu)).setText(Util
				.fomatNum(mGroupMessageBean.getG_focus_num()));
		((TextView) findViewById(R.id.tv_fangwen)).setText(Util
				.fomatNum(mGroupMessageBean.getG_fangwen_num()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_fangwen:
			Intent intentFangwen = new Intent(GroupInfoActivity.this,
					GroupFocusnumberActivity.class);
			intentFangwen.putExtra("gid", gid);
			intentFangwen.putExtra("flag", "1");
			startActivity(intentFangwen);
			break;
		case R.id.lin_guanzhu:
			Intent intents = new Intent(GroupInfoActivity.this,
					GroupFocusnumberActivity.class);
			intents.putExtra("gid", gid);
			intents.putExtra("flag", "2");
			startActivity(intents);
			break;
		case R.id.ginfo_btn_cancle:
			if (PreManager.instance().getIsLogin(GroupInfoActivity.this)) {
				if (null != mGroupMessageBean) {
					if (mGroupMessageBean.getFocus().equals("1")) {
						// 取消关注
						showExitDialog();
					} else {
						// 添加关注
						MobclickAgent.onEvent(GroupInfoActivity.this, "510");
						attentionGroup(gid);
					}
				} else {
					toastMsg("获取数据失败");
				}
			} else {
				startActivity(new Intent(GroupInfoActivity.this,
						LoginActivity.class));
			}

			break;
		case R.id.back:
			Intent intentB = new Intent();
			intentB.putExtra("messagebean", mGroupMessageBean);
			intentB.setAction(Constant.POST_GROPINFOEDIT);
			sendBroadcast(intentB);
			AppManager.getAppManager().finishActivity();
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intentB = new Intent();
			intentB.putExtra("messagebean", mGroupMessageBean);
			intentB.setAction(Constant.POST_GROPINFOEDIT);
			sendBroadcast(intentB);
			AppManager.getAppManager().finishActivity();
		}
		return true;
	}

	private void getData() {
		myHandler.postDelayed(new Runnable() {

			@Override
			public void run() {

				getGroupinfoData();
			}
		}, 200);
	}

	/**
	 * 网络请求 获取小组信息
	 */
	private void getGroupinfoData() {
		GetGroupMessageParamClass mParam = new GetGroupMessageParamClass();
		mParam.gid = gid;
		mParam.my_int_id = PreManager.instance().getUserId(
				GroupInfoActivity.this);
		new CommunityProcClass(this)
				.getGroupMessage(mParam, mGroupInfoCallback);
	}

	private InterfaceGetGroupMessageCallBack mGroupInfoCallback = new InterfaceGetGroupMessageCallBack() {

		@Override
		public void onSuccess(int icode, List<GroupMessageBean> list) {

			cancelPro();

			mGroupInfoList = list;
			if (list.size() > 0)
				myHandler.sendEmptyMessage(0);
		}

		@Override
		public void onError(int icode, String strErrMsg) {
			toastMsg("获取小组信息失败");

			cancelPro();

		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ExpertGroup_Info");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ExpertGroup_Info");
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 取消关注
	 */
	private void cancelAttentionGroup() {
		showPro();
		CancelAttentionGroupParamClass mParam = new CancelAttentionGroupParamClass();
		mParam.gid = gid;
		mParam.my_int_id = PreManager.instance().getUserId(
				GroupInfoActivity.this);
		new CommunityProcClass(GroupInfoActivity.this).cancelAttentionGroup(
				mParam, new InterfaceCancelAttentionGroupCallBack() {

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						toastMsg(strSuccMsg);
						myHandler.sendEmptyMessage(2);
						cancelPro();
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						toastMsg(strErrMsg);
						cancelPro();
					}
				});
	}

	/**
	 * 关注小组
	 * 
	 * @param gid
	 *            小组ID
	 */
	private void attentionGroup(String gid) {//
		showPro();
		AttentionGroupParamClass paramClass = new AttentionGroupParamClass();
		paramClass.my_int_id = PreManager.instance().getUserId(
				GroupInfoActivity.this);// 这里需要添加登陆ID
		paramClass.gid = gid;
		new CommunityProcClass(GroupInfoActivity.this).attentionGroup(
				paramClass, new InterfaceAttentionGroupCallBack() {

					@Override
					public void onSuccess(int icode, String strSuccMsg) {
						cancelPro();
						LogUtil.i("masong", "1关注 = " + icode);
						myHandler.sendEmptyMessage(3);
						toastMsg("关注成功");
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						cancelPro();
						LogUtil.i("masong", "2关注= " + icode);
						toastMsg(strErrMsg);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 714) {
			String mChanggedIntr;
			mChanggedIntr = data.getStringExtra("context");
			if (mChanggedIntr != null
					&& !mChanggedIntr
							.equals(mGroupInfoList.get(0).getG_intro())) {

				mGroupInfoList.get(0).setG_intro(mChanggedIntr);// mIntroduction
																// =
																// mChanggedIntr;//获得小组简介
				// 开始网络请求 上传修改后的小组简介
				upDataGroupIntr(mChanggedIntr);
				myHandler.sendEmptyMessage(4);
			}
		}
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case 788:
			getGroupinfoData();
			break;
		case Constant.TAKE_PHOTO:
			readPictureDegree = PhotoAccessUtil.readPictureDegree(PreManager
					.instance().getPhotoPathString(GroupInfoActivity.this));

			String g_PhotoPathString = PreManager.instance()
					.getPhotoPathString(GroupInfoActivity.this);
			if (g_PhotoPathString != "" && g_PhotoPathString != null) {
				Uri fileUri = Uri.fromFile(new File(g_PhotoPathString));

				startPhotoZoom(SelectPhotoUtil.newGetPath(
						GroupInfoActivity.this, fileUri));
			}
			break;
		case Constant.RESULT_LOAD_IMAGE:
			// 获取文件Uri
			Uri selectedImage = data.getData();
			startPhotoZoom(selectedImage);
			break;

		case Constant.CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				showPro();
				readPictureDegree = 0;
				groupIconPath = data.getStringExtra("cropImagePath");
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				upLoadPicture2QINIU();
				Bitmap bm = BitmapFactory.decodeFile(groupIconPath, options);
				groupPic.setImageBitmap(bm);
			}
			break;
		case Constant.REQUEST_CHAGE_GROUPICON:
			String iconKey = data.getStringExtra("g_ico");
			if (null != iconKey) {
				mGroupMessageBean.setG_ico_key((null == data
						.getStringExtra("g_ico")) ? mGroupMessageBean
						.getG_ico_key() : data.getStringExtra("g_ico"));
				mGroupMessageBean.setG_ico((null == data
						.getStringExtra("g_ico_url")) ? mGroupMessageBean
						.getG_ico() : data.getStringExtra("g_ico_url"));
				myHandler.sendEmptyMessage(4);
			}
			break;

		case Constant.NEW_RESULT_LOAD_IMAGE:
			Uri newselectedImage = data.getData();
			readPictureDegree = PhotoAccessUtil.readPictureDegree(
					GroupInfoActivity.this, newselectedImage);
			String realFilePath = SelectPhotoUtil.newGetPath(
					GroupInfoActivity.this, newselectedImage);
			startPhotoZoom(realFilePath);
			break;
		}
	}

	/**
	 * 缩放和切图
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(final Uri uri) {

		Intent mIntent = new Intent(GroupInfoActivity.this, CropImageUI.class);

		mIntent.putExtra("readPictureDegree", readPictureDegree);
		mIntent.putExtra("index", 1);
		mIntent.putExtra("isNew", false);
		mIntent.putExtra("uri", uri.toString());
		startActivityForResult(mIntent, Constant.CROP_PHOTO);
	}

	/**
	 * 缩放和切图,用于android4.4之后的版本
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(final String uri) {

		Intent mIntent = new Intent(GroupInfoActivity.this, CropImageUI.class);

		mIntent.putExtra("readPictureDegree", readPictureDegree);
		mIntent.putExtra("index", 1);
		mIntent.putExtra("isNew", false);
		mIntent.putExtra("uri", uri);
		startActivityForResult(mIntent, Constant.CROP_PHOTO);

	}

	/**
	 * 上传图片到七牛
	 */
	private void upLoadPicture2QINIU() {
		showPro();
		UploadQiuTokenParamClass mParam = new UploadQiuTokenParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(
				GroupInfoActivity.this);
		;
		new QiNiuFileManageClass(GroupInfoActivity.this).GetUploadQiniuToken(
				mParam, new InterfaceUploadQiNiuTokenCallBack() {

					@Override
					public void onSuccess(int iCode, String token) {
						QiniuUploadPictureTopic(groupIconPath, token);

					}

					@Override
					public void onError(int icode, String strErrMsg) {
						toastMsg("图标修改失败");
						if (null != pro) {
							cancelPro();
						}
					}
				});
	}

	private void QiniuUploadPictureTopic(String path, String token) {
		File uploadFile = new File(path);
		// 用户照片墙key命名规范：user/photo/用户id/图片名
		String key = "hd/" + my_int_id + "/" + my_int_id + "_"
				+ SleepUtils.getSystemCurrentTime() + ".jpg";

		QiNiuUploadTool.getInstance().uploadFile(uploadFile, key, token,
				new UpCompletionHandler() {

					@Override
					// key 即uploadManager.put(file, key, …)方法指定的key。
					// info http请求的状态信息等，可记入日志。isOK()返回 true表示上传成功。
					// response 七牛反馈的信息。可从中解析保存在七牛服务的key等信息，具体字段取决于上传策略的设置。
					public void complete(String key, ResponseInfo info,
							JSONObject arg2) {
						if (info.isOK()) {
							commitGroupIcon(key);
						} else {
							toastMsg("图标修改失败");
							if (null != pro) {
								cancelPro();
							}
						}
					}
				}, null);

	}

	/**
	 * 上传key到服务器
	 * 
	 * @param key
	 */
	private void commitGroupIcon(final String key) {
		UpdateGroupIconParamClass mParam = new UpdateGroupIconParamClass();
		mParam.my_int_id = my_int_id;
		mParam.gid = gid;
		mParam.g_ico = key;

		new CommunityProcClass(GroupInfoActivity.this).updateGroupIcon(mParam,
				new InterfaceUpdateGroupIconCallBack() {

					@Override
					public void onSuccess(int icode, String strSuccMsg,
							String g_ico_url) {
						if (null != pro) {
							cancelPro();
						}
						mGroupMessageBean.setG_ico(g_ico_url);
						mGroupMessageBean.setG_ico_key(key);
						toastMsg(strSuccMsg);
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						if (null != pro) {
							cancelPro();
						}
						toastMsg(strErrMsg);
					}
				});
	}

	/**
	 * 网络请求更新小组简介
	 * 
	 * @param strIntro
	 */
	private void upDataGroupIntr(String strIntro) {
		EditGroupSummaryParamClass mParam = new EditGroupSummaryParamClass();
		mParam.g_intro = Util.getURLEncodStr(strIntro);
		mParam.gid = gid;
		mParam.my_int_id = PreManager.instance().getUserId(
				GroupInfoActivity.this);
		new CommunityProcClass(GroupInfoActivity.this).editGroupSummary(mParam,
				mIntorCallBack);
	}

	private InterfaceEditGroupSummaryCallBack mIntorCallBack = new InterfaceEditGroupSummaryCallBack() {

		@Override
		public void onSuccess(int icode, String strSuccMsg) {
			toastMsg("修改成功");
		}

		@Override
		public void onError(int icode, String strErrMsg) {
			HLog.i("chen", "修改小组简介失败" + strErrMsg);
		}
	};

	/**
	 * 添加关注发送广播
	 */
	private void sendAttentionBroadcast() {
		Intent intent = new Intent();
		intent.putExtra("group_id", mGroupMessageBean.getGid());
		intent.putExtra("groupName", mGroupMessageBean.getG_name());
		intent.putExtra("groupIconUrl", mGroupMessageBean.getG_ico());
		intent.putExtra("groupIconKey", mGroupMessageBean.getG_ico_key());
		// intent.putExtra("groupType", accessType);
		intent.putExtra("flag", getIntent().getStringExtra("flag"));
		intent.putExtra("focusNum", mGroupMessageBean.getG_focus_num());
		intent.putExtra("focus", "1");
		intent.setAction(Constant.POST_GROPUSATTENTION);
		sendBroadcast(intent);
	}

	/**
	 * 取消关注发送广播
	 * 
	 * @param gid
	 */
	private void sendcCancleAttentionGroup(String gid) {
		Intent intentCancle = new Intent();
		intentCancle.setAction("com.action.change.NOINTEREST_GROUP");
		intentCancle.putExtra("gid", gid);
		intentCancle.putExtra("focusNum", mGroupMessageBean.getG_focus_num());
		intentCancle.putExtra("focus", "0");
		sendBroadcast(intentCancle);
	}
	/**
	 * 取消关注弹出改提示
	 */
	private void showExitDialog() {
		final MyDialog exitDialog = new MyDialog(GroupInfoActivity.this, 0, 0,
				R.layout.dialog_groupinfo, R.style.bottom_animation,
				Gravity.BOTTOM, 1.0f, 0.0f);
		exitDialog.setCanceledOnTouchOutside(false);

		// 得到view中的控件
		Button btn_goon_exit = (Button) exitDialog
				.findViewById(R.id.btn_group_ok);
		Button btn_cancel = (Button) exitDialog.findViewById(R.id.btn_group_no);

		btn_goon_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(GroupInfoActivity.this, "511");
				cancelAttentionGroup();
				exitDialog.cancel();

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
}
