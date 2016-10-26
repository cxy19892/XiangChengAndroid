package com.yzm.sleep.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.exception.WeiboShareException;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.yzm.sleep.AccessTokenKeeper;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.HomeActivity;
import com.yzm.sleep.bean.ShareObjectClass;
import com.yzm.sleep.widget.MyInviteDialog;
import com.yzm.sleep.widget.MyInviteDialog.ShareClassParam;

public class ShareUtil {
	/**
	 * 
	 * @param activity
	 * @param type  传1  int 类型
	 * @param url	连接地址
	 * @param title	标题
	 * @param summary 副标题
	 * @param sharePicUrl  分享图片的地址
	 */
//	public static void shareTopicOrEvent(Activity activity,int type, String url,
//			String title,String summary, String sharePicUrl){
//		if (activity == null || url == null || sharePicUrl == null) {
//			return;
//		}
//		ShareClassParam m_ShareClassParam = new ShareClassParam();
//		m_ShareClassParam.shareType = type;
//		m_ShareClassParam.targetUrl = url;
//		m_ShareClassParam.shareTitle = title;
//		m_ShareClassParam.shareSummary = summary;
//		m_ShareClassParam.sharePictureUrl = sharePicUrl;
//		MyInviteDialog inivteDialog = new MyInviteDialog(activity, 
//				0, 
//				0, 
//				0, 
//				R.style.bottom_animation, 
//				Gravity.BOTTOM, 
//				1.0f, 
//				0.0f,
//				m_ShareClassParam);
//		inivteDialog.setCanceledOnTouchOutside(true);
//		inivteDialog.show();
//	}
	
	/**
	 * 分享给QQ好友
	 * */
	public static void doQQShare(Activity activity,ShareObjectClass shareObject,IUiListener listener) {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, shareObject.getShareTitle());
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
				shareObject.getShareSummary());
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				shareObject.getTargetUrl());
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,shareObject.getSharePictureUrl());
		if(HomeActivity.mTencent != null){
			HomeActivity.mTencent.shareToQQ(activity, params, listener);
		}
		
	}
	public static void doQQShareImag(Activity activity,ShareObjectClass shareObject,IUiListener listener) {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, shareObject.getShareTitle());
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
				shareObject.getShareSummary());
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				shareObject.getTargetUrl());
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,shareObject.getSharePicturePath());
		if(HomeActivity.mTencent != null){
			HomeActivity.mTencent.shareToQQ(activity, params, listener);
		}
		
	}
	/**
	 * 分享到QQ空间
	 * @param activity
	 * @param shareObject
	 * @param picflag   图片类型 ，0 网络图片， 1分享本地图片
	 * @param listener
	 */
	public static void doQQQzoneShareImage(Activity activity,ShareObjectClass shareObject,final int picflag,IUiListener listener) {
		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
				shareObject.getShareTitle());
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
				shareObject.getShareSummary());
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
				shareObject.getTargetUrl());
		// QQ空间分享网络图片
		if (picflag == 0) {
			ArrayList<String> imageUrls = new ArrayList<String>();
			imageUrls.add(shareObject.getSharePictureUrl());
			params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
					imageUrls);
		}else if(picflag == 1){
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,shareObject.getSharePicturePath());
//			params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,Environment.getExternalStorageDirectory().getAbsolutePath()
//					+ "/DCIM/ceshi.png");
		    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
		}

		if(HomeActivity.mTencent != null){
			HomeActivity.mTencent.shareToQzone(activity, params, listener);
		}
		
	}
	
	/**
	 * 邀请微信好友
	 * */
	private static IWXAPI wxapi;
	/**
	 * 
	 * @param activity
	 * @param wechartflag      微信分享类型 ，0 分享给微信好友 1分享到朋友圈
	 * @param picflag      图片类型 ，0 网络图片， 1分享本地图片
	 * @param shareObject
	 * @param hander
	 */
	public static void doWeixinShare(final Activity activity,final int wechartflag,final int picflag,final ShareObjectClass shareObject,IWXAPIEventHandler hander){
		String WXAPP_ID = SleepConstants.WECHAT_SLEEP_APP_ID;
		wxapi = WXAPIFactory.createWXAPI(activity, WXAPP_ID, true);
		wxapi.registerApp(WXAPP_ID);
		wxapi.handleIntent(activity.getIntent(), hander); 
		
		if (!wxapi.isWXAppInstalled()) {
			ToastManager.getInstance(activity).show("请先安装微信客户端");
			return;
		}
		new Thread() {

			@Override
			public void run() {
				WXWebpageObject wxWebpage = new WXWebpageObject();
				WXMediaMessage mediamsg = new WXMediaMessage(wxWebpage);
				wxWebpage.webpageUrl = shareObject.getTargetUrl();
				mediamsg.title = shareObject.getShareTitle();
				mediamsg.description = shareObject.getShareSummary();
				try {
					byte[] data = getImageData(shareObject,picflag);
					mediamsg.thumbData = data;
					synchronized (mediamsg) {
						SendMessageToWX.Req req = new SendMessageToWX.Req();
						req.transaction = String
								.valueOf(System.currentTimeMillis());
						req.message = mediamsg;
//						req.scene = SendMessageToWX.Req.WXSceneSession;
						req.scene = wechartflag == 0 ? SendMessageToWX.Req.WXSceneSession
								: SendMessageToWX.Req.WXSceneTimeline;
						wxapi.sendReq(req);
						mediamsg = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
					ToastManager.getInstance(activity).show("请求分享图片失败");
				}
			}

		}.start();
	}
	/**
	 * 微信分享
	 * 
	 * @param activity
	 * @param wechartflag      微信分享类型 ，0 分享给微信好友 1分享到朋友圈
	 * @param picflag      图片类型 ，0 网络图片， 1分享本地图片
	 * @param shareObject
	 * @param hander
	 */
	public static void doWeixinShareImage(final Activity activity,final int wechartflag,final int picflag,final ShareObjectClass shareObject,final IWXAPI wxApi){
		new Thread() {
			
			@Override
			public void run() {
				WXImageObject wxImage = new WXImageObject();
				wxImage.imagePath = shareObject.getSharePicturePath();
				WXMediaMessage mediamsg = new WXMediaMessage(wxImage);
				mediamsg.mediaObject = wxImage;
				byte[] data;
				try {
					data = getImageData(shareObject,picflag);
					mediamsg.thumbData = data;
				} catch (IOException e) {
					e.printStackTrace();
				}
				synchronized (wxImage) {
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = String
							.valueOf(System.currentTimeMillis());
					req.message = mediamsg;
					req.scene = wechartflag == 0 ? SendMessageToWX.Req.WXSceneSession
							: SendMessageToWX.Req.WXSceneTimeline;
					wxApi.sendReq(req);
					mediamsg = null;
				}
			}
			
		}.start();
	}
	
	public static void doWeiboShare(final int picflag,final ShareObjectClass shareObject) {
	}
	/**
	 * 获取图片
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] getImageData(ShareObjectClass object,final int picflag) throws IOException {
		 byte[] data = new byte[1024];
		 if (picflag == 0) {
			 ByteArrayOutputStream bos = null;
			 URL url = new URL(object.getSharePictureUrl());
			 bos = new ByteArrayOutputStream();
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 InputStream in = conn.getInputStream();
			 int len = 0;
			 while((len = in.read(data)) != -1) {
				 bos.write(data,0,len);
			 }
			 byte result[] = bos.toByteArray();
			 return ImageCompressUtil.comp32k(BitmapFactory.decodeByteArray(result, 0, result.length));
		}else if(picflag == 1){
			try {
				InputStream is = new FileInputStream(new File(object.getSharePicturePath()));
				return ImageCompressUtil.comp32k(BitmapFactory.decodeStream(is));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 return data;
	}
	
	public static byte[] getImageDataOra(ShareObjectClass object,final int picflag) throws IOException {
		 byte[] data = new byte[1024];
		 if (picflag == 0) {
			 ByteArrayOutputStream bos = null;
			 URL url = new URL(object.getSharePictureUrl());
			 bos = new ByteArrayOutputStream();
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 InputStream in = conn.getInputStream();
			 int len = 0;
			 while((len = in.read(data)) != -1) {
				 bos.write(data,0,len);
			 }
			 byte result[] = bos.toByteArray();
			 return result;
		}else if(picflag == 1){
			try {
				InputStream is = new FileInputStream(new File(object.getSharePicturePath()));
				 int len = 0;
				 ByteArrayOutputStream bos = new ByteArrayOutputStream();
				 while((len = is.read(data)) != -1) {
					 bos.write(data,0,len);
				 }
				 byte result[] = bos.toByteArray();
				 return result;
//				return ImageCompressUtil.comp32k(BitmapFactory.decodeStream(is));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 return data;
	}
	
	
	/**
	 * 新浪微博分享
	 * @param shareType //0:SHARE_CLIENT  1:SHARE_ALL_IN_ONE
	 */
	public static void doWeiboShare(Activity context,IWeiboShareAPI mWeiboShareAPI,ShareObjectClass shareObject,boolean hasText, boolean hasImage, 
			boolean hasWebpage, boolean hasMusic, boolean hasVideo, boolean hasVoice){
		try {
            // 检查微博客户端环境是否正常，如果未安装微博，弹出对话框询问用户下载微博客户端
            sendMessage(context,
            		mWeiboShareAPI,
            		shareObject,
            		hasText, 
            		hasImage, 
            		hasWebpage,
            		hasMusic, 
            		hasVideo, 
            		hasVoice);
        } catch (WeiboShareException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
	}
	public static IWeiboShareAPI initWBShare(Context context) {
		IWeiboShareAPI mWeiboShareAPI = null;
		if (mWeiboShareAPI == null) {
			mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context,
					SleepConstants.SINA_SLEEP_APP_KEY);// 创建微博分享接口实例
			if (mWeiboShareAPI.isWeiboAppInstalled()) {
				
				mWeiboShareAPI.registerApp();
			}
		}
		return mWeiboShareAPI;
	}
	 /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * @see {@link #sendMultiMessage} 或者 {@link #sendSingleMessage}
     */
    private static void sendMessage(Activity context,IWeiboShareAPI mWeiboShareAPI,ShareObjectClass shareObject,boolean hasText, boolean hasImage, 
			boolean hasWebpage, boolean hasMusic, boolean hasVideo, boolean hasVoice) {
    	int shareType = 1;
    	if (shareType == 0) {
    		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
    			int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
    			if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
    				sendMultiMessage(context,mWeiboShareAPI,shareObject,hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice,shareType);
    			} else {
    				sendSingleMessage(context,mWeiboShareAPI,shareObject,hasText, hasImage, hasWebpage, hasMusic, hasVideo/*, hasVoice*/);
    			}
    		} else {
    			Util.show(context, "微博客户端不支持 SDK 分享或微博客户端未安装或微博客户端是非官方版本。");
    		}
    	}else {
    		sendMultiMessage(context,mWeiboShareAPI,shareObject,hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice,shareType);
		}
	}
    
    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */
    private static void sendMultiMessage(final Activity context,IWeiboShareAPI mWeiboShareAPI,ShareObjectClass shareObject,boolean hasText, boolean hasImage, boolean hasWebpage,
            boolean hasMusic, boolean hasVideo, boolean hasVoice,int shareType) {
        
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        if (hasText) {
//            weiboMessage.textObject = getTextObj();
//        }
        
        if (hasImage) {
            weiboMessage.imageObject = getImageObj(shareObject);
        }
        
        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
//        if (hasWebpage) {
//            weiboMessage.mediaObject = getWebpageObj();
//        }
//        if (hasMusic) {
//            weiboMessage.mediaObject = getMusicObj();
//        }
//        if (hasVideo) {
//            weiboMessage.mediaObject = getVideoObj();
//        }
//        if (hasVoice) {
//            weiboMessage.mediaObject = getVoiceObj();
//        }
        
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        
        // 3. 发送请求消息到微博，唤起微博分享界面
        if (shareType == 0) {
        	mWeiboShareAPI.sendRequest(context,request);
		}else {
			AuthInfo authInfo = new AuthInfo(context, SleepConstants.SINA_SLEEP_APP_KEY, SleepConstants.REDIRECT_URL, SleepConstants.SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context.getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest(context, request, authInfo, token, new WeiboAuthListener() {
                
                @Override
                public void onWeiboException( WeiboException arg0 ) {
                }
                
                @Override
                public void onComplete( Bundle bundle ) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(context.getApplicationContext(), newToken);
                    Toast.makeText(context.getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), 0).show();
                }
                
                @Override
                public void onCancel() {
                }
            });
		}
    }
    
    /**
     * 创建图片消息对象。
     * 
     * @return 图片消息对象。
     */
    private static ImageObject getImageObj(ShareObjectClass shareObject) {
        ImageObject imageObject = new ImageObject();
        byte[] data = null;
			try {
				data = ShareUtil.getImageDataOra(shareObject, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Bitmap newMap = BitmapFactory.decodeByteArray(data, 0,
					data.length);
        imageObject.setImageObject(newMap);
        return imageObject;
    }
    
    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     */
    private static void sendSingleMessage(Activity context,IWeiboShareAPI mWeiboShareAPI,ShareObjectClass shareObject,boolean hasText, boolean hasImage, boolean hasWebpage,
            boolean hasMusic, boolean hasVideo/*, boolean hasVoice*/) {
        
        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
//        if (hasText) {
//            weiboMessage.mediaObject = getTextObj();
//        }
        if (hasImage) {
            weiboMessage.mediaObject = getImageObj(shareObject);
        }
//        if (hasWebpage) {
//            weiboMessage.mediaObject = getWebpageObj();
//        }
//        if (hasMusic) {
//            weiboMessage.mediaObject = getMusicObj();
//        }
//        if (hasVideo) {
//            weiboMessage.mediaObject = getVideoObj();
//        }
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/
        
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(context,request);
    }
}
