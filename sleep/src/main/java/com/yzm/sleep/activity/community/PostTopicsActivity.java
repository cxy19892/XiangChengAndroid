package com.yzm.sleep.activity.community;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadOptions;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.adapter.ReleasePicGridAdapter;
import com.yzm.sleep.adapter.ReleasePicGridAdapter.ImageUploadLisenner;
import com.yzm.sleep.bean.ReleasePicbean;
import com.yzm.sleep.bean.ReleaseUpLoadPicBean;
import com.yzm.sleep.imageSelect.MultiImageSelectorActivity;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.FileSizeUtil;
import com.yzm.sleep.utils.ImageCompressUtil;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceTopicPostNewCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceUploadQiNiuTokenCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.TopicPostNewParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.UploadQiuTokenParamClass;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.MyTextWatcher;
import com.yzm.sleep.utils.PicUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.QiNiuFileManageClass;
import com.yzm.sleep.utils.QiNiuUploadTool;
import com.yzm.sleep.utils.SleepUtils;
import com.yzm.sleep.widget.CustomDialog;
import com.yzm.sleep.widget.CustomGridView;

public class PostTopicsActivity extends BaseActivity implements ImageUploadLisenner{
	private Context mContext;
	private List<String> selectImgs;
	private String groupName;
	private String groupId;
	private EditText edtTitle,edtConten;
	private RelativeLayout picReLayout, picRelBtn;
	private CustomGridView picGridView;
	private TextView picNum;
	private Button cancle, commit;
	private TextView title;
	private ReleasePicGridAdapter mAdapter;
	private ProgressUtils pro;
	private String my_int_id;
	private String replaceKey="";
	private boolean isCommitToServer = false;
	private boolean isCancleUpLoad = false;
	private ImageView topicImgIcon;
	List<ReleaseUpLoadPicBean> picList = new ArrayList<ReleaseUpLoadPicBean>();
	/**
	 * 选取图片
	 */
	private MyAlertDialog myAlertDialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_post_topics);
		selectImgs = new ArrayList<String>();// 已经选择图片的列表
		this.mContext = this;
		Intent intent = getIntent();
		groupName = intent.getStringExtra("groupName");
		groupId = intent.getStringExtra("groupId");
		
		initViews();
	}

	private void initViews() {
//		mCommunityTopicBean = new CommunityTopicBean();
		my_int_id = PreManager.instance().getUserId(mContext);
		edtTitle = (EditText) findViewById(R.id.topic_edt_title);
		edtConten = (EditText) findViewById(R.id.topic_edt_content);
		picReLayout = (RelativeLayout) findViewById(R.id.topic_rel_pic_btn);
		picRelBtn = (RelativeLayout) findViewById(R.id.topic_rel_pic);
		picGridView = (CustomGridView) findViewById(R.id.topic_gridview_pic);
		picNum = (TextView) findViewById(R.id.topic_num_pics);
		cancle = (Button) findViewById(R.id.back);
		commit = (Button) findViewById(R.id.btn_title_right);
		title = (TextView) findViewById(R.id.title);
		topicImgIcon = (ImageView) findViewById(R.id.topic_img_pics);
		commit.setVisibility(View.VISIBLE);
		commit.setCompoundDrawables(null, null, null, null);
		commit.setText("发布");
		title.setText("话题发布");
		cancle.setOnClickListener(this);
		commit.setOnClickListener(this);
		picRelBtn.setOnClickListener(this);
		picGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		ReleasePicbean mReleasePicbean = new ReleasePicbean();
		ReleaseUpLoadPicBean addReleaseUpLoadPicBean = new ReleaseUpLoadPicBean();
		addReleaseUpLoadPicBean.setKey("add");
		addReleaseUpLoadPicBean.setIscanceled(false);
		addReleaseUpLoadPicBean.setIscomplete(true);
		picList.add(addReleaseUpLoadPicBean);
		mReleasePicbean.setUpLoadImags(picList);
		mReleasePicbean.setReadyCommit(true);
		mAdapter = new ReleasePicGridAdapter(this, getScreenWidth());
		mAdapter.setLisenner(this);
//		mReleasePicbean.setSelectImgs(selectImgs);
		
		picGridView.setAdapter(mAdapter);
		mAdapter.setData(mReleasePicbean);
		edtTitle.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					picReLayout.setVisibility(View.GONE);
					picGridView.setVisibility(View.GONE);
					topicImgIcon.setImageResource(R.drawable.ic_shequ_photo_normal);
				}
			}
		});
		edtConten.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					picReLayout.setVisibility(View.GONE);
					picGridView.setVisibility(View.GONE);
					topicImgIcon.setImageResource(R.drawable.ic_shequ_photo_normal);
				}
			}
		});
		edtTitle.addTextChangedListener(new MyTextWatcher(edtTitle, this, 20, null));
		edtConten.addTextChangedListener(new MyTextWatcher(edtConten, this, 5000, null));
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topic_rel_pic:
			if(picReLayout.getVisibility() == View.GONE){
			picReLayout.setVisibility(View.VISIBLE);
			picGridView.setVisibility(View.VISIBLE);
			picGridView.requestFocus();
			topicImgIcon.setImageResource(R.drawable.ic_shequ_photo_selected);
			}else{
				picReLayout.setVisibility(View.GONE);
				picGridView.setVisibility(View.GONE);
				topicImgIcon.setImageResource(R.drawable.ic_shequ_photo_normal);
			}
			break;
		case R.id.back://返回
			dialogForExit();
			break;
		case R.id.btn_title_right://发布
			
			if (checkNetWork(PostTopicsActivity.this)) {
				String topictitle= edtTitle.getText().toString();
				String topiccontent=  edtConten.getText().toString();
				if ((!TextUtils.isEmpty(topictitle))&&(!TextUtils.isEmpty(topiccontent))) {
					if (topiccontent.length() < 5) {
						toastCenter("内容不能少于5个字");
					} else {
						showPro();
						isCancleUpLoad = false;
						isCommitToServer = true;
						StartUploadPicToServer();
						// 点击发布
						MobclickAgent.onEvent(PostTopicsActivity.this,
								"522");
					}
				} else if (TextUtils.isEmpty(topictitle)) {
					toastMsg("请输入文章标题");
				} else if (TextUtils.isEmpty(topiccontent)) {
					toastMsg("请输入文章内容");
				} else {
					toastMsg("请完善文章信息");
				}

			} else {
				toastMsg("当前网络不可用，请检查网络设置");
			}

			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constant.TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				ReleasePicbean mReleasePicbean = mAdapter.getData();
				selectImgs = mReleasePicbean.getSelectImgs();
				String photoPath = dealPictureNew(photoUri);
				if(photoPath != null){
					selectImgs.add(photoPath);
					setPicNum(selectImgs.size());
					mAdapter.setData(dealPicinfo(selectImgs, mReleasePicbean, my_int_id));
				}
			}
			break;
		case Constant.SELECT_PIC:
			if (resultCode == RESULT_OK) {
				ReleasePicbean mReleasePicbean = mAdapter.getData();
				selectImgs = data
						.getStringArrayListExtra(Constant.EXTRA_RESULT);// data.getBundleExtra("result").getStringArrayList("selectImgs");
				if (selectImgs.size() != 0)
					mReleasePicbean.setReadyCommit(false);
//				mReleasePicbean.setSelectImgs(selectImgs);
				mAdapter.setData(dealPicinfo(selectImgs, mReleasePicbean, my_int_id));
				setPicNum(selectImgs.size());
			}
			break;
		case Constant.CROP_PHOTO_REPLACE:
			if (resultCode == RESULT_OK) {
				ReleasePicbean tempReleasePicbean = new ReleasePicbean();
				tempReleasePicbean.setReadyCommit(false);
				tempReleasePicbean.setUpLoadImags(new ArrayList<ReleaseUpLoadPicBean>());
				ReleasePicbean mReleasePicbean = mAdapter.getData();
				String photoPath = dealPictureNew(photoUri);
				if(photoPath != null){
					for(ReleaseUpLoadPicBean mReleaseUpLoadPicBean : mReleasePicbean.getUpLoadImags() ){
						if(mReleaseUpLoadPicBean.getKey().equals(replaceKey)){
							tempReleasePicbean.getUpLoadImags().add(getReplace(photoPath));
						}else{
							tempReleasePicbean.getUpLoadImags().add(mReleaseUpLoadPicBean);
						}
					}
				}
				replaceKey = "";
				setPicNum(tempReleasePicbean.getSelectImgs().size());
				mAdapter.setData(tempReleasePicbean);
			}
			break;
		case Constant.SELECT_PIC_REPLACE:
			if (resultCode == RESULT_OK) {
				ReleasePicbean tempReleasePicbean = new ReleasePicbean();
				tempReleasePicbean.setReadyCommit(false);
				tempReleasePicbean.setUpLoadImags(new ArrayList<ReleaseUpLoadPicBean>());
				ReleasePicbean mReleasePicbean = mAdapter.getData();
				List<String> replaceImg = data
						.getStringArrayListExtra(Constant.EXTRA_RESULT);
				for(ReleaseUpLoadPicBean mReleaseUpLoadPicBean : mReleasePicbean.getUpLoadImags() ){
					if(mReleaseUpLoadPicBean.getKey().equals(replaceKey)){
						mReleaseUpLoadPicBean = getReplace(replaceImg.get(0));
						tempReleasePicbean.getUpLoadImags().add(getReplace(replaceImg.get(0)));
					}else{
						tempReleasePicbean.getUpLoadImags().add(mReleaseUpLoadPicBean);
					}
				}
				replaceKey = "";
				setPicNum(tempReleasePicbean.getSelectImgs().size());
				mAdapter.setData(tempReleasePicbean);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置图片数量的标志
	 */
	private void setPicNum(int num){
		if(num>0){
			picNum.setVisibility(View.VISIBLE);
			picNum.setText(""+num);
		}else{
			picNum.setVisibility(View.GONE);
		}
	}
	
	private ReleaseUpLoadPicBean getReplace(String release){
		ReleaseUpLoadPicBean newReleaseUpLoadPicBean = new ReleaseUpLoadPicBean();
		newReleaseUpLoadPicBean.setIscanceled(false);
		newReleaseUpLoadPicBean.setIscomplete(false);
		newReleaseUpLoadPicBean.setIsupload(false);
		newReleaseUpLoadPicBean.setKey(GetKeyOfPic(my_int_id));
		// newReleaseUpLoadPicBean.setPath("");//dealPicture(release,
		// KK);
		newReleaseUpLoadPicBean.setPercent(0);
		newReleaseUpLoadPicBean.setUrl(release);
		return newReleaseUpLoadPicBean;
	}
	
	/**
	 * 完善数据
	 * 
	 * @param mreleasebean
	 */
	public ReleasePicbean dealPicinfo(List<String> SelectImgs, ReleasePicbean mreleasebean,
			String my_int_id) {
		if (mreleasebean.getUpLoadImags() == null) {
			mreleasebean.setUpLoadImags(new ArrayList<ReleaseUpLoadPicBean>());
		}
		// mreleasebean.getUpLoadImags().clear();
		for (int KK = 0; KK < SelectImgs.size(); KK++) {
			String release = SelectImgs.get(KK);
			if (!checkIsExidOfPath(release, mreleasebean)) {

				ReleaseUpLoadPicBean newReleaseUpLoadPicBean = new ReleaseUpLoadPicBean();
				newReleaseUpLoadPicBean.setIscanceled(false);
				newReleaseUpLoadPicBean.setIscomplete(false);
				newReleaseUpLoadPicBean.setIsupload(false);
				newReleaseUpLoadPicBean.setKey(GetKeyOfPic(my_int_id));
				// newReleaseUpLoadPicBean.setPath("");//dealPicture(release,
				// KK);
				newReleaseUpLoadPicBean.setPercent(0);
				newReleaseUpLoadPicBean.setUrl(release);
				mreleasebean.setReadyCommit(false);
				mreleasebean.getUpLoadImags().add(newReleaseUpLoadPicBean);
			}
		}
		if(SelectImgs.size() >= 9){
			mreleasebean.getUpLoadImags().remove(0);
		}
		return mreleasebean;
	}
	
	/**
	 * 检查该路径的图片是否已经存在<br> true 存在<br> false 不存在
	 */
	private boolean checkIsExidOfPath(String path, ReleasePicbean mreleasebean) {
		if (mreleasebean.getUpLoadImags() == null) {
			mreleasebean.setUpLoadImags(new ArrayList<ReleaseUpLoadPicBean>());
		}
		for (ReleaseUpLoadPicBean mpicbean : mreleasebean.getUpLoadImags()) {
			if (path.equals(mpicbean.getUrl())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取图片的key
	 */
	private String GetKeyOfPic(String my_int_id) {
		return "hd/" + my_int_id + "/" + my_int_id
				+ ("_" + SleepUtils.getSystemCurrentTime())
				+ (Math.random() * 1000 + 1) + ".jpg";
	}
	
	
	 /**
     * @param uriString
     * @return
     */
    private String dealPictureNew(String uriString){
          Bitmap bitmap = null;
          bitmap = getBitpMapNew(uriString);
           if (bitmap != null) {
                if( null == PicUtil. bitmap2Drawable(bitmap)){
                    Toast. makeText(this, "选择图片有误" , Toast.LENGTH_SHORT).show();
                     return null;
               }
                return ImageCompressUtil.saveMyBitmap(bitmap, selectImgs .size());
          }else{
          }
           return null;
    }
	
	/**
	 * 对图片进行大小的压缩
	 * @param uriString
	 * @return
	 */
	private Bitmap getBitpMapNew(String uriString) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(uriString);
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;// 只读边,不读内容
			Bitmap bitmap = BitmapFactory.decodeFile(uriString, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			float hh = 800f;//
			float ww = 480f;//
			int be = 1;
			if (w > h && w > ww) {
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;// 设置采样率
			newOpts.inPreferredConfig = Config.RGB_565;// 该模式是默认的,可不设
			newOpts.inPurgeable = true;// 同时设置才会有效
			newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
			bitmap = BitmapFactory.decodeStream(fis, null, newOpts);
			// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
			// 其实是无效的,大家尽管尝试
			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				fis.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return null;
	}
	

	
	private void showPopupWindow(final boolean isReplace) {
		if (null == myAlertDialog) {
			myAlertDialog = new MyAlertDialog(this,
					R.style.bottom_animation);
		}
		myAlertDialog.show();
		if(isReplace){
			myAlertDialog.setTV1("请选择替换图片的操作");
		}else{
			myAlertDialog.setTV1("请选择操作");
		}
		myAlertDialog.setTV2("拍照", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				tackphoto(isReplace);
				myAlertDialog.dismiss();
			}
		}, View.VISIBLE);
		myAlertDialog.setTV3("从手机相册选择", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				if(isReplace){
					getOneImgByStorage();
				}else{
					getImgByStorage();
				}
				myAlertDialog.dismiss();
			}

		}, View.VISIBLE);
		myAlertDialog.setTV4("", null, View.GONE);
		myAlertDialog.setTVbottom("取消", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {

				myAlertDialog.dismiss();
			}

		}, View.VISIBLE);
	}
	
	
	private String photoUri;
	
	private void tackphoto(boolean isReplace) {
		
		Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);  
        photoUri = getPhotopath();
        File out = new File(photoUri);  
//        LogUtil.i("chen", out.getAbsolutePath());
        Uri uri = Uri.fromFile(out);  
        // 获取拍照后未压缩的原图片，并保存在uri路径中  
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri); 
        if(isReplace){
        	startActivityForResult(intentPhote, Constant. CROP_PHOTO_REPLACE); // 启动照相 用于替换图片
        }else{
        	startActivityForResult(intentPhote, Constant. TAKE_PHOTO); // 启动照相
        }
  }
	
	/** 
     * 获取原图片存储路径 
     * @return 
     */  
    private String getPhotopath() {  
        // 文件夹路径  
        String pathUrl = Environment.getExternalStorageDirectory()
				+ "/data/yzm/compress";  
        String imageName = "imageTest"+ System.currentTimeMillis() +".jpg";  
        File file = new File(pathUrl);
        if(!file.exists())
        	file.mkdirs();// 创建文件夹  
        return new File(new File(pathUrl), imageName).getAbsolutePath();  
    } 

	/**
	 * 从相册选择多张图片
	 */
	private void getImgByStorage() {

		Intent intent = new Intent(this,
				MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(Constant.EXTRA_SHOW_CAMERA, false);
		// 最大可选择图片数量
		intent.putExtra(Constant.EXTRA_SELECT_COUNT, 9);
		// 选择模式
		intent.putExtra(Constant.EXTRA_SELECT_MODE, Constant.MODE_MULTI);
		// 默认选择
		ArrayList<String> selectedIcons = (ArrayList<String>) mAdapter
				.getData().getSelectImgs();
		intent.putStringArrayListExtra(Constant.EXTRA_DEFAULT_SELECTED_LIST,
				selectedIcons);
		startActivityForResult(intent, Constant.SELECT_PIC);
	}
	
	/**
	 * 从相册选择一张图片
	 */
	private void getOneImgByStorage() {

		Intent intent = new Intent(this,
				MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(Constant.EXTRA_SHOW_CAMERA, false);
		// 最大可选择图片数量
		intent.putExtra(Constant.EXTRA_SELECT_COUNT, 1);
		// 选择模式
		intent.putExtra(Constant.EXTRA_SELECT_MODE, Constant.MODE_SINGLE);
		intent.putStringArrayListExtra(Constant.EXTRA_DEFAULT_SELECTED_LIST,
				new ArrayList<String>());
		startActivityForResult(intent, Constant.SELECT_PIC_REPLACE);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			dialogForExit();
		}
		return true;
	}

	/**
	 * 当有输入信息的时候提示是否放弃已编辑的内容
	 */
	private void dialogForExit() {
		if (!(TextUtils.isEmpty(edtTitle.getText().toString()) && TextUtils.isEmpty(edtConten.getText().toString()))
				|| (mAdapter.getData().getSelectImgs() != null && mAdapter.getData().getSelectImgs().size() > 0)) {
			final CustomDialog dialog = new CustomDialog(
					this);
			dialog.show();
			dialog.setTitle("是否放弃当前编辑内容？");
			dialog.setSubGone();
			dialog.setOnLeftClickListener("否", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {

					dialog.dismiss();
				}
			});
			dialog.setOnRightClickListener("是", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					AppManager.getAppManager().finishActivity();
					dialog.dismiss();
				}
			});
		} else {
			AppManager.getAppManager().finishActivity();
		}
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
		pro.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if(isCommitToServer){
					LogUtil.e("chen", "取消上传");
					isCancleUpLoad = true;
					isCommitToServer = false;
					mAdapter.setData(SetShowPicInfo());
					
				}
			}
		});
	}

	/**
	 * 取消进度
	 */
	public void cancelPro() {
		if(!this.isFinishing()){
			if (pro != null) {
				pro.cancel();
				pro = null;
			}
		}
	}

	@Override
	public void ImageUploadCallBack(TextView tv, String key) {
//		StartUploadPicToQiniu(tv, key);
	}

	@Override
	public OnClickListener ClickLisenner(final int type, View view, final String key) {
		// TODO Auto-generated method stub
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (3 == type) {
					ReleasePicbean mReleasePicbean = mAdapter.getData();
					if(mReleasePicbean == null|| mReleasePicbean.getUpLoadImags() == null || mReleasePicbean.getSelectImgs().size() < 9){
						showPopupWindow(false);
					}else{
						toastCenter("最多支持9张图片上传");
					}
				} else {
					for (int cl = 0; cl < mAdapter.getData().getUpLoadImags()
							.size(); cl++) {// ReleaseUpLoadPicBean
											// upLoadinfo:mReleasePicbean.getUpLoadImags()){
						ReleasePicbean mReleasePicbean = mAdapter.getData();
						ReleaseUpLoadPicBean upLoadinfo = mReleasePicbean
								.getUpLoadImags().get(cl);
						if (key.equals(upLoadinfo.getKey())) {
							switch (type) {
							case 1:// 删除
								if(mReleasePicbean.getSelectImgs().size()<9){//不需要重置加號
									mReleasePicbean.getSelectImgs().remove(
											upLoadinfo.getUrl());
									mReleasePicbean.getUpLoadImags().remove(
											upLoadinfo);
									selectImgs = (ArrayList<String>) mReleasePicbean.getSelectImgs();
									setPicNum(mReleasePicbean.getSelectImgs().size());
									mAdapter.setData(mReleasePicbean);
									mAdapter.setData(SetShowPicInfo());
								}else{//需要重置加號
									mReleasePicbean.getSelectImgs().remove(
											upLoadinfo.getUrl());
									mReleasePicbean.getUpLoadImags().remove(
											upLoadinfo);
									selectImgs = (ArrayList<String>) mReleasePicbean.getSelectImgs();
									setPicNum(mReleasePicbean.getSelectImgs().size());
									ReleasePicbean newReleasePicbean = new ReleasePicbean();
									ReleaseUpLoadPicBean addReleaseUpLoadPicBean = new ReleaseUpLoadPicBean();
									addReleaseUpLoadPicBean.setKey("add");
									List<ReleaseUpLoadPicBean> tempPicList = new ArrayList<ReleaseUpLoadPicBean>(); 
									tempPicList.add(addReleaseUpLoadPicBean);
									
									for(ReleaseUpLoadPicBean mTempReleaseUpLoadPicBean : mReleasePicbean.getUpLoadImags()){
										tempPicList.add(mTempReleaseUpLoadPicBean);
									}
									newReleasePicbean.setUpLoadImags(tempPicList);
									mAdapter.setData(newReleasePicbean);
								}
								break;
							case 2:
								/*// 重传
								if (upLoadinfo.isIscanceled()) {
									upLoadinfo.setIscanceled(false);
									upLoadinfo.setIsupload(false);
									upLoadinfo.setIscomplete(false);
								}
								mAdapter.setData(mReleasePicbean);*/
								//替换
								break;
							case 4:
								if (upLoadinfo.isIscanceled()) {
									upLoadinfo.setIscanceled(false);
									upLoadinfo.setIsupload(false);
									upLoadinfo.setIscomplete(false);
								}
								replaceKey = key;
								showPopupWindow(true);
								break;
							default:
								break;
							}
							break;
						}
					}
				}

			}
		};
	}
	//==================================图片操作=====================================
	/**
	 * 压缩处理
	 * 
	 * @param uriString
	 * @return
	 */
	private String dealPicture(String uriString, int Position) {
		double fileSize = FileSizeUtil.getFileOrFilesSize(uriString, 2);
		if(50 > fileSize){//如果文件小于50kb 就不进行压缩
			return uriString;
		}else{//大于100k则开始压缩
			Bitmap bitmap = getBitpMapNew(uriString);
			if (bitmap != null) {//
				byte[] arr = ImageCompressUtil.compToByte(bitmap);//compressImageTobyte(bitmap);//
				if (arr == null) {
					bitmap.recycle();
					System.gc();
					return null;
				}
				String imgPath = ImageCompressUtil.saveMyBitmap(arr, Position);
				bitmap.recycle();
				System.gc();
				return imgPath;
			}
			return null;
		}
	}
	
	/**
	 * 检查是否所有的图片都上传完成
	 * 
	 * @return
	 */
	private boolean IsAllOfPicUploaded() {
		for (ReleaseUpLoadPicBean mReleaseUpLoadPicBean : mAdapter.getData()
				.getUpLoadImags()) {
			if (mReleaseUpLoadPicBean.isIscomplete()) {
				// 图片上传成功
			} else if (mReleaseUpLoadPicBean.isIscanceled()) {
				// 图片被取消上传
				return false;
			} else {
				// 图片还没有上传
				// System.out.println(">>> meiyou 还没有上传的key"+mReleaseUpLoadPicBean.getKey());
				return false;
			}
		}
		return true;
	}
	/**
	 * 删除上传失败的图片
	 */
	private void delUpLoadFailedPic(){
		ReleasePicbean mReleasePicbean = mAdapter.getData();
		List<ReleaseUpLoadPicBean> deLoadPicList = new ArrayList<ReleaseUpLoadPicBean>();
		if (mReleasePicbean.getUpLoadImags() != null) {
			for (int k = 0; k < mAdapter.getData().getUpLoadImags().size(); k++) {
				ReleaseUpLoadPicBean removeBeam = mReleasePicbean.getUpLoadImags().get(k);
				if (removeBeam.isIscanceled()) {
					deLoadPicList.add(removeBeam);
				}
			}
			mReleasePicbean.getUpLoadImags().removeAll(deLoadPicList);
			selectImgs = (ArrayList<String>) mReleasePicbean.getSelectImgs();
			setPicNum(mReleasePicbean.getSelectImgs().size());
			mAdapter.setData(mReleasePicbean);
		}
		StartUploadPicToServer();
	}
	
	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void myDeleteFile() {
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					sleep(500);
					File dirFile = new File(Environment.getExternalStorageDirectory()
							+ "/data/yzm/compress");
					if (!dirFile.exists()) {
						return;
					}
					if (dirFile.isDirectory()) {
						String[] children = dirFile.list();
						for (int i = 0; i < children.length; i++) {
							new File(dirFile, children[i]).delete();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start(); 
	}
	
	//==================================网络操作=====================================
	/**
	 * adapter 发送消息  开始上传图片
	 * @param tv
	 * @param key
	 */
	private void StartUploadPicToQiniu(String key) {
		ReleasePicbean mReleasePicbean = getPostPicInfo();
		if (mReleasePicbean.getUpLoadImags() != null) {
			for (int k = 0; k < mReleasePicbean.getUpLoadImags().size(); k++) {
				if (mReleasePicbean.getUpLoadImags().get(k).getKey()
						.equals(key)) {
					if (!mReleasePicbean.getUpLoadImags().get(k).isIscomplete()
							&& !mReleasePicbean.getUpLoadImags().get(k)
									.isIsupload()) {
						upLoadPicture2QINIU(key);
						break;
					}
				}
			}
		}

	}
	
	/**
	 * 获取图片上传的token
	 */
	private void upLoadPicture2QINIU(final String key) {
		UploadQiuTokenParamClass mParam = new UploadQiuTokenParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(mContext);
		new QiNiuFileManageClass(mContext).GetUploadQiniuToken(mParam,
				new InterfaceUploadQiNiuTokenCallBack() {

					@Override
					public void onSuccess(int iCode, String token) {
						ReleasePicbean mReleasePicbean = getPostPicInfo();
						
						for (int k = 0; k < mReleasePicbean.getUpLoadImags()
								.size(); k++) {
							if (mReleasePicbean.getUpLoadImags().get(k)
									.getKey().equals(key)) {
								String pathString = null;
								int imgesnum = mReleasePicbean.getSelectImgs().size();
								pathString = dealPicture(mReleasePicbean
										.getSelectImgs().get(k), k);
								mReleasePicbean
										.getUpLoadImags()
										.get(k)
										.setPath(
												pathString == null ? mReleasePicbean
														.getSelectImgs().get(k)
														: pathString);
								mReleasePicbean.getUpLoadImags().get(k)
										.setIsupload(true);
								QiniuUploadPictureTopic(getPostPicInfo().getUpLoadImags().get(k)
										.getPath(),key, token);
							}
						}

					}

					@Override
					public void onError(int icode, String strErrMsg) {
						if(!checkNetWork(mContext)){
							toastMsg("网络连接失败");
						}else{
							toastMsg(strErrMsg);
							ReleasePicbean mReleasePicbean = mAdapter.getData();
							for (ReleaseUpLoadPicBean mBean : mReleasePicbean.getUpLoadImags()) {
								if (key.equals(mBean.getKey())) {
									mReleasePicbean.getSelectImgs().remove(
											mBean.getUrl());
									mReleasePicbean.getUpLoadImags().remove(mBean);
									selectImgs = (ArrayList<String>) mReleasePicbean.getSelectImgs();
									break;
								}
							}
							setPicNum(mReleasePicbean.getSelectImgs().size());
							mAdapter.setData(mReleasePicbean);
						}
					}
				});
	}

	/**
	 * 上传图片到七牛
	 * @param k
	 * @param token
	 * @param tv
	 */
	private void QiniuUploadPictureTopic(String path, String key, String token
			) {

		File uploadFile = null;
		try {
			uploadFile = new File(path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(uploadFile == null){
			ReleasePicbean mReleasePicbean = getPostPicInfo();
			for (int p = 0; p < mReleasePicbean
					.getUpLoadImags().size(); p++) {
				if (key.equals(mReleasePicbean.getUpLoadImags()
						.get(p).getKey())) {
					mReleasePicbean.getUpLoadImags().get(p)
					.setIscanceled(true);
					mReleasePicbean.setReadyCommit(false);
				}
			}
			return;
		}
		QiNiuUploadTool.getInstance().uploadFile(
				uploadFile,
				key,
				token,
				new UpCompletionHandler() {

					ReleasePicbean mReleasePicbean = mAdapter.getData();
					@Override
					// key 即uploadManager.put(file, key, …)方法指定的key。
					// info http请求的状态信息等，可记入日志。isOK()返回 true表示上传成功。
					// response 七牛反馈的信息。可从中解析保存在七牛服务的key等信息，具体字段取决于上传策略的设置。
					public void complete(String key, ResponseInfo info,
							JSONObject arg2) {

						if (info.isOK()) {
							for (int p = 0; p < mReleasePicbean
									.getUpLoadImags().size(); p++) {
								if (key.equals(mReleasePicbean.getUpLoadImags()
										.get(p).getKey())) {// 可能在回来的时候原图已经被remov掉了
									// System.out.println(">>> 第"+p+"张图上传成功 key值="+mReleasePicbean.getUpLoadImags().get(p).getKey());
									mReleasePicbean.getUpLoadImags().get(p)
									.setIscomplete(true);
									if (IsAllOfPicUploaded()) {// 如果图片已经全部上传完成
										//										mHandler.sendEmptyMessage(1);
										mReleasePicbean.setReadyCommit(true);
										mAdapter.setData(mReleasePicbean);
//										StartUploadPicToServer();// 上传信息到服务器
									}
								}
							}
							StartUploadPicToServer();// 上传信息到服务器
						} else {
							for (int p = 0; p < mReleasePicbean
									.getUpLoadImags().size(); p++) {
								if (key.equals(mReleasePicbean.getUpLoadImags()
										.get(p).getKey())) {
									mReleasePicbean.getUpLoadImags().get(p)
									.setIscanceled(true);
									mReleasePicbean.setReadyCommit(false);

								}
							}
						}
					}
				},
				new UploadOptions(null, null, false, null/*new UpProgressHandler() {

					@Override
					public void progress(String key, final double percent) {
						ReleasePicbean mReleasePicbean = mAdapter.getData();
						for (int q = 0; q < mReleasePicbean.getUpLoadImags()
								.size(); q++) {
							if (key.equals(mReleasePicbean.getUpLoadImags()
									.get(q).getKey())) {

								mReleasePicbean.getUpLoadImags().get(q)
								.setPercent(percent);
								// 刷新显示
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										tv.setText((int) (percent * 100) + "%");
										if (percent == 1) {
											tv.setVisibility(View.GONE);
										}
									}
								});
								if (percent == 1) {
									mAdapter.notifyDataSetChanged();
									// System.out.println(">>> 第"+q+"张图上传 100% key值="+mReleasePicbean.getUpLoadImags().get(q).getKey());
								}
							}
						}
					}
				}*/, new UpCancellationSignal() {
					
					@Override
					public boolean isCancelled() {
						// TODO Auto-generated method stub
						return isCancleUpLoad;
					}
				}));
	}
	
	/**
	 * 开始上传图片信息到服务器
	 */
	private void StartUploadPicToServer() {
		if(isCommitToServer){
			ReleasePicbean mReleasePicbean = getPostPicInfo();
			if (mReleasePicbean.isReadyCommit()) {// 图片上传完成且点击了提交按钮的
				// 开始上传到服务器
				List<String> list = new ArrayList<String>();
				if (mReleasePicbean.getUpLoadImags() != null) {
					for (int k = 0; k < mReleasePicbean.getUpLoadImags().size() && k < 9; k++) {
						list.add(mReleasePicbean.getUpLoadImags().get(k).getKey());
					}
				}
				commitTopics(list);
			} else{//图片没有上传完成，且没有取消发布
				if (IsAllOfPicUploaded()) {// 重新再检查一遍图片是否已经全部上传完成
					mReleasePicbean.setReadyCommit(true);
					StartUploadPicToServer();// 上传信息到服务器
				} else {
					for (ReleaseUpLoadPicBean mUpLoadPicBean : mReleasePicbean
							.getUpLoadImags()) {
						if (mUpLoadPicBean.isIscanceled()) {
							if (pro != null) {
								cancelPro();
							}
							toastMsg("图片上传失败");
							showUploadFailed();
							break;
						}else{
							if(!mUpLoadPicBean.isIsupload()){
								if(!"add".equals(mUpLoadPicBean.getKey())){
									StartUploadPicToQiniu(mUpLoadPicBean.getKey());
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 图片上传失败的提示
	 */
	private void showUploadFailed() {
		final CustomDialog FailedDialog = new CustomDialog(
				this);
		FailedDialog.show();
		FailedDialog.setTitle("图片上传失败");
		FailedDialog.setSub("如果直接发布将会忽略已失败的图片!");
		FailedDialog.setOnLeftClickListener("查看", new

		com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				if(picReLayout.getVisibility() == View.GONE){
					picReLayout.setVisibility(View.VISIBLE);
					picGridView.setVisibility(View.VISIBLE);
					picGridView.requestFocus();
					topicImgIcon.setImageResource(R.drawable.ic_shequ_photo_selected);
					}
				mAdapter.notifyDataSetChanged();
				FailedDialog.dismiss();
			}
		});
		FailedDialog.setOnRightClickListener("直接发布", new

		com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				delUpLoadFailedPic();
				FailedDialog.dismiss();
			}
		});
	}

	/**
	 * 提交文本信息和图片信息到服务器
	 * 
	 * @param fileKey
	 */
	private void commitTopics(final List<String> fileKey) {
		if (pro == null) {
			showPro();
		}
		TopicPostNewParamClass mParam = new TopicPostNewParamClass();
		mParam.gid = groupId;
		mParam.my_int_id = my_int_id;
		try {
			JSONArray jsonAry = new JSONArray();
			for (int i = 0; i < fileKey.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("t_attachment_key", fileKey.get(i));
				jsonAry.put(jsonObj);
			}
			mParam.t_attachment_key = jsonAry.toString();
		} catch (Exception e) {

		}
		mParam.t_message = edtConten.getText().toString();
		mParam.t_subject = edtTitle.getText().toString();
		mParam.t_tag = "";
		new CommunityProcClass(mContext).topicPostNew(mParam,
				new InterfaceTopicPostNewCallBack() {

					@Override
					public void onSuccess(int icode, String strSuccMsg,
							String tid, String url, String url_original) {
						cancelPro();
						toastMsg(strSuccMsg);
						myDeleteFile();
						
		                setResult(RESULT_OK, getIntent());  
						AppManager.getAppManager().finishActivity();
					}

					@Override
					public void onError(int icode, String strErrMsg) {
						cancelPro();
						toastMsg(strErrMsg);
					}
				});
	}
	/**
	 * 提交发布的时候将手动添加的加号图标去掉
	 * @return
	 */
	private ReleasePicbean getPostPicInfo(){
		ReleaseUpLoadPicBean mRemovePicBean = null;
		ReleasePicbean mReleasePicbean = mAdapter.getData();
		for(ReleaseUpLoadPicBean mpostPicBean : mReleasePicbean.getUpLoadImags()){
			if("add".equals(mpostPicBean.getKey())){
				mRemovePicBean = mpostPicBean;
			}
		}
		mReleasePicbean.getUpLoadImags().remove(mRemovePicBean);
		return mReleasePicbean;
	}
	/**
	 * 给没有加号图标的别彪加上加号图标。用于显示添加按钮
	 * @return
	 */
	private ReleasePicbean SetShowPicInfo(){
		ReleasePicbean mReleasePicbean = mAdapter.getData();
		for(ReleaseUpLoadPicBean mpostPicBean : mReleasePicbean.getUpLoadImags()){
			if("add".equals(mpostPicBean.getKey())){
				return mReleasePicbean;
			}
		}
		if(mReleasePicbean.getSelectImgs().size() < 9){
			List<ReleaseUpLoadPicBean> tempPicList = new ArrayList<ReleaseUpLoadPicBean>();
			ReleasePicbean firstReleasePicbean = new ReleasePicbean();
			ReleaseUpLoadPicBean addReleaseUpLoadPicBean = new ReleaseUpLoadPicBean();
			addReleaseUpLoadPicBean.setKey("add");
			addReleaseUpLoadPicBean.setIscanceled(false);
			addReleaseUpLoadPicBean.setIscomplete(true);
			tempPicList.add(addReleaseUpLoadPicBean);
			for(ReleaseUpLoadPicBean tmpostPicBean : mReleasePicbean.getUpLoadImags()){
				tempPicList.add(tmpostPicBean);
			}
			firstReleasePicbean.setUpLoadImags(tempPicList);
			if(mReleasePicbean.getSelectImgs().size() == 0){
				firstReleasePicbean.setReadyCommit(true);
			}else{
				firstReleasePicbean.setReadyCommit(false);
			}
			return firstReleasePicbean;
		}else{
			return mReleasePicbean;
		}
	}
	
	/**
	 * 居中显示toast
	 * 
	 * @param toastStr
	 */
	private void toastCenter(String toastStr) {
		Toast toast = Toast.makeText(PostTopicsActivity.this, toastStr,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
