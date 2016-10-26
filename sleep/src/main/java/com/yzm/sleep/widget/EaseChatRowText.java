package com.yzm.sleep.widget;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.EaseSmileUtils;

public class EaseChatRowText extends EaseChatRow{

	private TextView contentView;
	private Context mContext;
	
    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
		super(context, message, position, adapter);
		this.mContext = context;
	}

	@Override
	protected void onInflatView() {
		inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
	}

	@Override
	protected void onFindViewById() {
		contentView = (TextView) findViewById(R.id.tv_chatcontent);
	}

    @Override
    public void onSetUpView() {
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        Spannable span = EaseSmileUtils.getSmiledText(activity, txtBody.getMessage());
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);

        try {
     		//r_pg 评估
     		JSONObject jsonObj=message.getJSONObjectAttribute("attr");
     		if(jsonObj.has("r_pg") && "1".equals(jsonObj.getString("r_pg"))){
     			contentView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
     			contentView.getPaint().setAntiAlias(true);//抗锯齿
//     			contentView.setTextColor(message.direct == EMMessage.Direct.RECEIVE ?
//     					mContext.getResources().getColor(R.color.theme_color): mContext.getResources().getColor(R.color.ct_color));
     		}else{
     			contentView.getPaint().setFlags(0);
                contentView.getPaint().setAntiAlias(true);
//     			contentView.setTextColor(message.direct == EMMessage.Direct.RECEIVE ?
//     					mContext.getResources().getColor(R.color.ct_color): mContext.getResources().getColor(R.color.white));
     		}
     		
 		} catch (Exception e) {}
        
        if (message.direct == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status) {
            case CREATE: 
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                // 发送消息
//                sendMsgInBackground(message);
                break;
            case SUCCESS: // 发送成功
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case FAIL: // 发送失败
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS: // 发送中
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                break;
            default:
               break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == ChatType.Chat){
                try {
                    EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                    message.isAcked = true;
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        
    }

}
