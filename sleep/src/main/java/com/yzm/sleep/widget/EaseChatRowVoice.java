package com.yzm.sleep.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.VoiceMessageBody;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.Util;

public class EaseChatRowVoice extends EaseChatRow{

    private ImageView voiceImageView;
    private TextView voiceLengthView;
    private ImageView readStutausView;
    private Context context;
    private RelativeLayout bubble;
    
    public EaseChatRowVoice(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        this.context=context;
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?R.layout.ease_row_received_voice : R.layout.ease_row_sent_voice, this);
    }

    @Override
    protected void onFindViewById() {
        voiceImageView = ((ImageView) findViewById(R.id.iv_voice));
        voiceLengthView = (TextView) findViewById(R.id.tv_length);
        readStutausView = (ImageView) findViewById(R.id.iv_unread_voice);
        bubble=(RelativeLayout) findViewById(R.id.bubble);
    }

    @Override
    protected void onSetUpView() {
        VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
        int len = voiceBody.getLength();
        if(len>0){
            voiceLengthView.setText(voiceBody.getLength() + "\"");
            voiceLengthView.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) bubble.getLayoutParams();
            int minWidth = Util.Dp2Px(context, 60);
            params.width=(int) (minWidth + ((len-10) <= 0 ? 0 : (len-10)/50f) * (Constant.screenWidht/2 - minWidth));
            bubble.setLayoutParams(params);
        }else{
            voiceLengthView.setVisibility(View.INVISIBLE);
        }
        if (EaseChatRowVoicePlayClickListener.playMsgId != null
                && EaseChatRowVoicePlayClickListener.playMsgId.equals(message.getMsgId()) && EaseChatRowVoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
            if (message.direct == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(R.drawable.voice_from_icon);
            } else {
                voiceImageView.setImageResource(R.drawable.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) voiceImageView.getDrawable();
            voiceAnimation.start();
        } else {
            if (message.direct == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(R.drawable.ease_chatfrom_voice_playing);
            } else {
                voiceImageView.setImageResource(R.drawable.ease_chatto_voice_playing);
            }
        }
        
        if (message.direct == EMMessage.Direct.RECEIVE) {
            if (message.isListened()) {
                // 隐藏语音未听标志
                readStutausView.setVisibility(View.INVISIBLE);
            } else {
                readStutausView.setVisibility(View.VISIBLE);
            }
            if (message.status == EMMessage.Status.INPROGRESS) {
                progressBar.setVisibility(View.VISIBLE);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.INVISIBLE);

            }
            return;
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
    		new EaseChatRowVoicePlayClickListener(message, voiceImageView, readStutausView, adapter, activity).onClick(bubbleLayout);
		} catch (Exception e) {
		}
    }
    
}
