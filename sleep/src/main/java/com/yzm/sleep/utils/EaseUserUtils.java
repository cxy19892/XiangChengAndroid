package com.yzm.sleep.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.EaseUser;
import com.yzm.sleep.utils.EaseUI.EaseUserProfileProvider;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * 根据username获取相应user
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * 设置用户头像
     * @param userProfileUrl
     */
    public static void setUserAvatar(final Context context, final String url,final String key, final ImageView imageView){
        if(!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)){
            try {
            	ImageLoader.getInstance().displayImage(url, key,imageView, MyApplication.headPicOptn, new CustomImageLoadingListener(context, imageView, key));
            	
            } catch (Exception e) {
            	ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_my_tx, imageView, MyApplication.headPicOptn);
            }
        }else{
        	ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_my_tx, imageView, MyApplication.headPicOptn);
        }
    }
   
   
	
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    
}
