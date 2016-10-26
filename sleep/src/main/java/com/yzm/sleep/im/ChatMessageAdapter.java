/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yzm.sleep.im;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.easemob.chat.EMMessage;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.widget.EaseChatRow;
import com.yzm.sleep.widget.EaseChatRowImage;
import com.yzm.sleep.widget.EaseChatRowText;
import com.yzm.sleep.widget.EaseChatRowVoice;

@SuppressLint("HandlerLeak")
public class ChatMessageAdapter extends BaseAdapter {
	
	private Context context;
	private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
	private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
	private static final int HANDLER_MESSAGE_SEEK_TO = 2;
	private static final int MESSAGE_TYPE_RECV_TXT = 0;
	private static final int MESSAGE_TYPE_SENT_TXT = 1;
	private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
	private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
	private static final int MESSAGE_TYPE_SENT_VOICE = 6;
	private static final int MESSAGE_TYPE_RECV_VOICE = 7;
	public int itemTypeCount;
	private List<EMMessage> msgs;
	private ListView msgListView;
	private MessageListItemClickListener itemClickListener;
	
	public ChatMessageAdapter(Context context,ListView msgListView ,int chatType,MessageListItemClickListener itemClickListener) {
		this.context = context;
		this.itemClickListener=itemClickListener;
		this.msgListView=msgListView;
	}

	public void setData(List<EMMessage> msgs){
		this.msgs=msgs;
		this.notifyDataSetChanged();
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message message) {
			switch (message.what) {
			case HANDLER_MESSAGE_REFRESH_LIST:
			case HANDLER_MESSAGE_SELECT_LAST:
				if (msgs.size() > 0) {
					msgListView.setSelection(msgs.size()- 1);
					notifyDataSetChanged();
				}
				break;
			case HANDLER_MESSAGE_SEEK_TO:
				int position = message.arg1;
				msgListView.setSelection(position);
				notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 刷新页面
	 */
	public void refresh() {
		
		if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
			return;
		}
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
		handler.sendMessage(msg);
	}

	/**
	 * 刷新页面, 选择最后一个
	 */
	public void refreshSelectLast() {
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LAST));
	}

	/**
	 * 刷新页面, 选择Position
	 */
	public void refreshSeekTo(int position) {
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
		msg.arg1 = position;
		handler.sendMessage(msg);
	}

	public EMMessage getItem(int position) {
		if (msgs != null && position < msgs.size()) {
			return msgs.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取item数
	 */
	public int getCount() {
		return msgs == null ? 0 : msgs.size();
	}

	/**
	 * 获取item类型数
	 */
	public int getViewTypeCount() {
		return 12;
	}

	/**
	 * 获取item类型
	 */
	public int getItemViewType(int position) {
		EMMessage message = getItem(position);
		if (message == null) {
			return -1;
		}

		if (message.getType() == EMMessage.Type.TXT) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT
					: MESSAGE_TYPE_SENT_TXT;
		}
		if (message.getType() == EMMessage.Type.IMAGE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE
					: MESSAGE_TYPE_SENT_IMAGE;

		}
		if (message.getType() == EMMessage.Type.VOICE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE
					: MESSAGE_TYPE_SENT_VOICE;
		}
		return -1;
	}

	protected EaseChatRow createChatRow(Context context, EMMessage message,int position) {
		EaseChatRow chatRow = null;
		
		switch (message.getType()) {
		case TXT:
			chatRow = new EaseChatRowText(context, message, position, this);
			break;
		case IMAGE:
			chatRow = new EaseChatRowImage(context, message, position, this);
			break;
		case VOICE:
			chatRow = new EaseChatRowVoice(context, message, position, this);
		default:
			break;
		}

		return chatRow;
	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		EMMessage message = getItem(position);
		if (convertView == null) {
			convertView = createChatRow(context, message, position);
		}
		EMMessage receivLastMessage= getReceivLastMessage();
		// 缓存的view的message很可能不是当前item的，传入当前message和position更新ui
		((EaseChatRow) convertView).setUpView(message, receivLastMessage, position,itemClickListener);

		return convertView;
	}

	public void setItemClickListener(MessageListItemClickListener listener) {
		itemClickListener = listener;
	}
	
	/**
	 * 获取接收的最后一条接收消息
	 * @return
	 */
	private EMMessage getReceivLastMessage(){
		for (int i = msgs.size()-1; i >= 0 ; i--) {
			EMMessage message =msgs.get(i);
			if(!message.getFrom().equals(PreManager.instance().getUserId(context))){
				return message;
			}
		}
		return null;
	}
}
