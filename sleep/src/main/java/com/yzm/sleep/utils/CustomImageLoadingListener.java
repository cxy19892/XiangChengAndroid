package com.yzm.sleep.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDowmloadQiniuFileCallBack;

public class CustomImageLoadingListener implements ImageLoadingListener {
	private ImageView imageView;
	private String key;
	private Context context;
	private boolean needSave;
	
	public CustomImageLoadingListener(Context context,ImageView imageView,String key){
		this(context,imageView,key,false);
	}

	public CustomImageLoadingListener(Context context,ImageView imageView,String key,boolean needSave){
		this.context=context;
		this.imageView=imageView;
		this.key=key;
		this.needSave=needSave;
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		if(needSave)
			refreshDisCacheNeedSave();
		else
			refreshDisCache();
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
	}

	private  void refreshDisCache() {
		new UserManagerProcClass(context).dowmloadQiniuFile(key,new InterfaceDowmloadQiniuFileCallBack() {
			
			@Override
			public void onSuccess(int iCode, String profile) {
				ImageLoader.getInstance().displayImage(profile,
						key,imageView, MyApplication.headPicOptn);					
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
			}
		});
	}
	
	private void refreshDisCacheNeedSave() {
		new UserManagerProcClass(context).dowmloadQiniuFile(key,new InterfaceDowmloadQiniuFileCallBack() {
			
			@Override
			public void onSuccess(int iCode, String profile) {
				PreManager.instance().saveUserProfileUrl(context, profile);
				PreManager.instance().saveUserProfileKey(context, key);
				ImageLoader.getInstance().displayImage(profile,
						key,imageView, MyApplication.headPicOptn);					
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
			}
		});
	}

}
