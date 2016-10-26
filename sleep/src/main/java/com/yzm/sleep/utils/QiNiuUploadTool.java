package com.yzm.sleep.utils;

import java.io.File;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDowmloadQiniuFileCallBack;

public class QiNiuUploadTool {
		
		private static QiNiuUploadTool qiNiuUploadTool;
		private QiNiuUploadTool() {
	
		}
		
		public static QiNiuUploadTool getInstance() {
			if (null == qiNiuUploadTool) {
				qiNiuUploadTool = new QiNiuUploadTool();
			}
			return qiNiuUploadTool;
		}
		
		
		/*UploadOptions参数说明：

		参数	类型	说明
		params	String	自定义变量，key必须以 x: 开始。
		mimeType	String	指定文件的mimeType。
		checkCrc	boolean	是否验证上传文件。
		progressHandler	UpProgressHandler	上传进度回调。
		cancellationSignal	UpCancellationSignal	取消上传，当isCancelled()返回true时，不再执行更多上传。*/
		
	/*	回调参数说明：

		参数	说明
		key	即uploadManager.put(file, key, …)方法指定的key。
		info	http请求的状态信息等，可记入日志。isOK()返回 true表示上传成功。
		response	七牛反馈的信息。可从中解析保存在七牛服务的key等信息，具体字段取决于上传策略的设置。*/
		
		
		/**
		 * 上传文件
		 * @param uploadFile
		 * 					上传的文件（数据，可以是byte数组，文件路径，文件。）
		 * @param key
		 * 					保存在服务器上的资源唯一标识。
		 * @param token
		 * 					服务器分配的token。
		 * @param upCompletionHandler
		 * 					上传回调函数，必填。
		 * @param options
		 * 					如果需要进度通知、crc校验、中途取消、指定mimeType则需要填写相应字段
		 */
		public void uploadFile(File uploadFile, String key, String token,
				UpCompletionHandler upCompletionHandler,UploadOptions options) {
			UploadManager uploadManager = new UploadManager();
			uploadManager.put(uploadFile, key, token,upCompletionHandler, options);
		}
		
		 /**
	     * 刷新缓存图片
	     * @param context
	     * @param profileKey
	     * @param imageView
	     */ 
		public void refreshDisCache(final Context context, final String profileKey,final ImageView imageView) {
			
			new UserManagerProcClass(context).dowmloadQiniuFile(profileKey,new InterfaceDowmloadQiniuFileCallBack() {
				
				@Override
				public void onSuccess(int iCode, String profile) {
					
					ImageLoader.getInstance().displayImage(profile,
							profileKey,imageView, MyApplication.headPicOptn);
					PreManager.instance().saveUserProfileKey(context, profileKey);
				}
				
				@Override
				public void onError(int icode, String strErrMsg) {
					
				}
			});
		}

}
