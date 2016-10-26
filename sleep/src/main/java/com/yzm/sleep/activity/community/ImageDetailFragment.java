package com.yzm.sleep.activity.community;

import java.io.File;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.easemob.util.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.ArticleImageBean;
import com.yzm.sleep.gesture.GestureImageView;
import com.yzm.sleep.im.EaseImageCache;
import com.yzm.sleep.im.EaseLoadLocalBigImgTask;

public class ImageDetailFragment extends Fragment {
    private GestureImageView mGestureImageView;
	private ProgressBar mRangeBar;
	private ImageView mimageView;
	private  ArticleImageBean mArticleImageBean;
	//聊天使用
    private  Uri uri ;   
    private String remotepath;
    private String chatfrom;
    
    private void initViews(View v){
		mGestureImageView = (GestureImageView) v.findViewById(R.id.image_detail_image);
		mRangeBar = (ProgressBar) v.findViewById(R.id.image_detail_rangeBar);
		mimageView= (ImageView) v.findViewById(R.id.mini_image);
		mGestureImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity(getActivity());
			}
		});
		mimageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity(getActivity());
			}
		});
	}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticleImageBean = (ArticleImageBean) (getArguments() != null ? getArguments().getSerializable("item"): null);
        uri = getArguments().getParcelable("uri");
		remotepath = getArguments().getString("remotepath");
		chatfrom=getArguments().getString("chatfrom")==null ? "" : getArguments().getString("chatfrom");
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_image_detail, container, false);
        initViews(v);
        if("1".equals(chatfrom)){
        	mGestureImageView.setVisibility(View.VISIBLE);
        	mimageView.setVisibility(View.GONE);
        	loadChatImage();
        }else
        	checkData();
        return v;
    }
    
    /**
     * 加载 聊天大图片
     */
    private void loadChatImage(){
    	//本地存在，直接显示本地的图片
		if (uri != null && new File(uri.getPath()).exists()) {
			Bitmap bitmap = EaseImageCache.getInstance().get(uri.getPath());
			if (bitmap == null) {
				EaseLoadLocalBigImgTask task = new EaseLoadLocalBigImgTask(getActivity(), uri.getPath(), mGestureImageView, mRangeBar, ImageUtils.SCALE_IMAGE_WIDTH,
						ImageUtils.SCALE_IMAGE_HEIGHT);
				if (android.os.Build.VERSION.SDK_INT > 10) {
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					task.execute();
				}
			} else {
				mGestureImageView.setImageBitmap(bitmap);
				mRangeBar.setVisibility(View.GONE);
			}
		} else if (remotepath != null) { //去服务器下载图片
			ImageLoader.getInstance().loadImage(remotepath, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					mRangeBar.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					mRangeBar.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					if(null != arg2){
						mGestureImageView.setVisibility(View.VISIBLE);
						mGestureImageView.setImageBitmap(arg2);
						mimageView.setVisibility(View.GONE);
					}
					mRangeBar.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					mRangeBar.setVisibility(View.GONE);
				}
			});
		}
    }
	
    private void checkData() {
		String Key, uri;
		if(TextUtils.isEmpty(mArticleImageBean.getContent_attachment())){
			if(!TextUtils.isEmpty(mArticleImageBean.getContent_attachment_sl())){
				if(!TextUtils.isEmpty(mArticleImageBean.getT_attachment_key_sl())){
					Key = mArticleImageBean.getT_attachment_key_sl();
					uri = mArticleImageBean.getContent_attachment_sl();
					startLoadImage(uri, Key, uri, Key);
				}
			}
		}else{
			if(!TextUtils.isEmpty(mArticleImageBean.getContent_attachment())){
				Key = mArticleImageBean.getT_attachment_key();
				uri = mArticleImageBean.getContent_attachment();
				startLoadImage(mArticleImageBean.getContent_attachment_sl(), mArticleImageBean.getT_attachment_key_sl(), uri, Key);
			}
		}
	}
	
	private void startLoadImage(final String urisl, final String keysl, String uri, String Key){
		
		ImageLoader.getInstance().displayImage(uri, Key, mGestureImageView, MyApplication.defaultOption, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				ImageLoader.getInstance ().displayImage(urisl, keysl, mimageView, MyApplication.choicePicOptn);
				mRangeBar.setVisibility(View.VISIBLE);
			}
			
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				mRangeBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				mGestureImageView.setVisibility(View.VISIBLE);
				if(null != arg2){
					mimageView.setVisibility(View.GONE);
				}
				mRangeBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				mRangeBar.setVisibility(View.GONE);
			}
		});
	}

}
