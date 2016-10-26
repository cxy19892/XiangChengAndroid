package com.yzm.sleep.widget;

import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Direct;
import com.easemob.util.DateUtils;
import com.yzm.sleep.CircleImageView;
import com.yzm.sleep.R;
import com.yzm.sleep.im.ChatMessageAdapter;
import com.yzm.sleep.im.MessageListItemClickListener;
import com.yzm.sleep.utils.EaseUserUtils;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;

public abstract class EaseChatRow extends LinearLayout {
    protected static final String TAG = EaseChatRow.class.getSimpleName();

    protected LayoutInflater inflater;
    protected BaseAdapter adapter;
    protected EMMessage message,receivLaseMessage;
    protected int position;

    protected TextView timeStampView;
    protected CircleImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView,userType;
    protected Activity activity;

    protected TextView ackedView;
    protected TextView deliveredView;

    protected EMCallBack messageSendCallback;
    protected EMCallBack messageReceiveCallback;

    protected MessageListItemClickListener itemClickListener;

    public EaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.activity = (Activity) context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        inflater = LayoutInflater.from(context);

        initView();
    }

    private void initView() {
        onInflatView();
        timeStampView = (TextView) findViewById(R.id.timestamp);
        userAvatarView = (CircleImageView) findViewById(R.id.iv_userhead);
        userType= (ImageView) findViewById(R.id.iv_usertype);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
        ackedView = (TextView) findViewById(R.id.tv_ack);
        deliveredView = (TextView) findViewById(R.id.tv_delivered);

        onFindViewById();
    }

    /**
     * 根据当前message和position设置控件属性等
     * 
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, EMMessage receivLaseMessage, int position,MessageListItemClickListener itemClickListener) {
        this.message = message;
        this.position = position;
        this.receivLaseMessage=receivLaseMessage;
        this.itemClickListener = itemClickListener;

        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

	private void setUpBaseView() {
    	String url="";
    	String key="";
    	String name="";
    	String type="0";
    	try {
    		JSONObject jsonObj=message.getJSONObjectAttribute("attr");
    		name=jsonObj.getString("name");
    		type=jsonObj.getString("type");
		} catch (Exception e) {
		}
    	if(receivLaseMessage != null){
    		try {
    			JSONObject jsonObj=receivLaseMessage.getJSONObjectAttribute("attr");
    			name=jsonObj.getString("name");
        		url=jsonObj.getString("url");
        		key=jsonObj.getString("key");
    		} catch (Exception e) {
    		}
    	}else{
    		url="";
        	key="";
    	}
    	
        // 设置用户昵称头像，bubble背景等
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // 两条消息时间离得如果稍长，显示时间
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }
        
        //设置头像和nick
        if(message.direct == Direct.SEND){
            EaseUserUtils.setUserAvatar(activity,PreManager.instance().getUserProfileUrl(activity),PreManager.instance().getUserProfileKey(activity),userAvatarView);
        }else{
            EaseUserUtils.setUserAvatar(activity, url,key, userAvatarView);
            EaseUserUtils.setUserNick(name, usernickView);
        }
        
        //设置用户标示
        if("1".equals(type))
        	userType.setVisibility(View.VISIBLE);
        else
        	userType.setVisibility(View.GONE);
        
        if(deliveredView != null){
            if (message.isDelivered) {
                deliveredView.setVisibility(View.VISIBLE);
            } else {
                deliveredView.setVisibility(View.INVISIBLE);
            }
        }
        
        if(ackedView != null){
            if (message.isAcked) {
                if (deliveredView != null) {
                    deliveredView.setVisibility(View.INVISIBLE);
                }
                ackedView.setVisibility(View.VISIBLE);
            } else {
                ackedView.setVisibility(View.INVISIBLE);
            }
        }
        

        if (adapter instanceof ChatMessageAdapter) {
        	 userAvatarView.setVisibility(View.VISIBLE);
           
            if (usernickView != null) {
            	usernickView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置消息发送callback
     */
    protected void setMessageSendCallback(){
        if(messageSendCallback == null){
            messageSendCallback = new EMCallBack() {
                
                @Override
                public void onSuccess() {
                    updateView();
                }
                
                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(percentageView != null)
                                percentageView.setText(progress + "%");

                        }
                    });
                }
                
                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageSendCallback);
    }
    
    /**
     * 设置消息接收callback
     */
    protected void setMessageReceiveCallback(){
        if(messageReceiveCallback == null){
            messageReceiveCallback = new EMCallBack() {
                
                @Override
                public void onSuccess() {
                    updateView();
                }
                
                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if(percentageView != null){
                                percentageView.setText(progress + "%");
                            }
                        }
                    });
                }
                
                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageReceiveCallback);
    }
    
    
    private void setClickListener() {
        if(bubbleLayout != null){
            bubbleLayout.setOnClickListener(new OnClickListener() {
    
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null){
                        if(!itemClickListener.onBubbleClick(message)){
                            //如果listener返回false不处理这个事件，执行lib默认的处理
                            onBubbleClick();
                        }
                    }
                }
            });
    
            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {
    
                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onResendClick(message);
                    }
                }
            });
        }

        userAvatarView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    if (message.direct == Direct.SEND) {
                        itemClickListener.onUserAvatarClick(EMChatManager.getInstance().getCurrentUser());
                    } else {
                        itemClickListener.onUserAvatarClick(message.getFrom());
                    }
                }
            }
        });
    }


    protected void updateView() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
//                if (message.status == EMMessage.Status.FAIL) {
//                    if (message.getError() == EMError.MESSAGE_SEND_INVALID_CONTENT) {
//                        Toast.makeText(activity,"发送失败", Toast.LENGTH_SHORT).show();
//                    } else if (message.getError() == EMError.MESSAGE_SEND_NOT_IN_THE_GROUP) {
//                        Toast.makeText(activity,"发送失败", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(activity,"发送失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
                onUpdateView();
            }
        });

    }

    /**
     * 填充layout
     */
    protected abstract void onInflatView();

    /**
     * 查找chatrow里的控件
     */
    protected abstract void onFindViewById();

    /**
     * 消息状态改变，刷新listview
     */
    protected abstract void onUpdateView();

    /**
     * 设置更新控件属性
     */
    protected abstract void onSetUpView();
    
    /**
     * 聊天气泡被点击事件
     */
    protected abstract void onBubbleClick();

}
