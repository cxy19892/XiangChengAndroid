package com.yzm.sleep.widget;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.ImageDetailActivity;
import com.yzm.sleep.model.EaseImageCache;
import com.yzm.sleep.utils.EaseImageUtils;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.Util;

public class EaseChatRowImage extends EaseChatRow{

    protected ImageView imageView;
    private ImageMessageBody imgBody;

    public EaseChatRowImage(Context context,EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_picture : R.layout.ease_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
    }

    
    @Override
    protected void onSetUpView() {
        imgBody = (ImageMessageBody) message.getBody();
        // 接收方向的消息
        if (message.direct == EMMessage.Direct.RECEIVE) {
            if (message.status == EMMessage.Status.INPROGRESS) {
                imageView.setImageResource(R.drawable.ease_default_image);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ease_default_image);
                if (imgBody.getLocalUrl() != null) {
                    String remotePath = imgBody.getRemoteUrl();
//                    String filePath = EaseImageUtils.getImagePath(remotePath);
//                    String thumbRemoteUrl = imgBody.getThumbnailUrl();
//                    String thumbnailPath = EaseImageUtils.getThumbnailImagePath(thumbRemoteUrl);
//                    showImageView(thumbnailPath, imageView, filePath, message);
                    ImageLoader.getInstance().displayImage(remotePath, imageView, MyApplication.defaultOption);
                }
            }
            return;
        }
        
        String filePath = imgBody.getLocalUrl();
        if (filePath != null) {
        	ImageLoader.getInstance().displayImage("file://"+filePath, imageView, MyApplication.defaultOption);
        } 
        handleSendMessage();
    }
    
	/**
	 * 处理发送消息
	 */
    protected void handleSendMessage() {
        setMessageSendCallback();
        switch (message.status) {
        case SUCCESS:
            progressBar.setVisibility(View.INVISIBLE);
            if(percentageView != null)
                percentageView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.INVISIBLE);
            break;
        case FAIL:
            progressBar.setVisibility(View.INVISIBLE);
            if(percentageView != null)
                percentageView.setVisibility(View.INVISIBLE);
            statusView.setVisibility(View.VISIBLE);
            break;
        case INPROGRESS:
            progressBar.setVisibility(View.VISIBLE);
            if(percentageView != null){
                percentageView.setVisibility(View.VISIBLE);
                percentageView.setText(message.progress + "%");
            }
            statusView.setVisibility(View.INVISIBLE);
            break;
        default:
            progressBar.setVisibility(View.VISIBLE);
            if(percentageView != null){
                percentageView.setVisibility(View.VISIBLE);
                percentageView.setText(message.progress + "%");
            }
            statusView.setVisibility(View.INVISIBLE);
            break;
        }
    }
    
    @Override
    protected void onUpdateView() {
    	adapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onBubbleClick() {
    	try {
    		Intent intent = new Intent(activity, ImageDetailActivity.class);
	        intent.putExtra("chatfrom", "1");
	        File file = new File(imgBody.getLocalUrl());
	        if (file.exists()) {
	            Uri uri = Uri.fromFile(file);
	            intent.putExtra("uri", uri);
	        } else {
	            intent.putExtra("secret", imgBody.getSecret());
	            intent.putExtra("remotepath", imgBody.getRemoteUrl());
	        }
	        activity.startActivity(intent);
	        if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked) {
	            try {
	                EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
	                message.isAcked = true;
	            } catch (Exception e) {
	            }
	        }
		} catch (Exception e) {
		}
    }
    
    /**
     * load image into image view
     * 
     * @param thumbernailPath
     * @param iv
     * @param position
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath,final EMMessage message) {
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
            return true;
        } else {
            new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    if (file.exists()) {
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 160, 160);
                    } else {
                        if (message.direct == EMMessage.Direct.SEND) {
                            return EaseImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        iv.setImageBitmap(image);
                        EaseImageCache.getInstance().put(thumbernailPath, image);
                    } else {
                        if (message.status == EMMessage.Status.FAIL) {
                            if (Util.checkNetWork(activity)) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EMChatManager.getInstance().asyncFetchMessage(message);
                                    }
                                }).start();
                            }
                        }

                    }
                }
            }.execute();

            return true;
        }
    }

}
