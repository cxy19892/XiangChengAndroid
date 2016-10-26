package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.MyAutoMsgActivity;
import com.yzm.sleep.adapter.MessageAdapter;
import com.yzm.sleep.background.SecretaryDBOperate;
import com.yzm.sleep.bean.MsgListBean;
import com.yzm.sleep.bean.UserMessageBean;
import com.yzm.sleep.im.ChatActivity;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.EaseUI;
import com.yzm.sleep.utils.InterFaceUtilsClass.DynamicMsgNumParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceDynamicMsgNumCallBack;
import com.yzm.sleep.utils.PreManager;

/**
 * 消息列表 总的列表
 * @author tianxun
 */
@SuppressLint("HandlerLeak") 
public class MessageListActivity extends BaseActivity implements EMEventListener {
	private ListView mListView;
	private MessageAdapter mAdapter;
	private List<MsgListBean> messages;
	private MyAlertDialog dialog;
	private String myLoginId;
	private ImageView hasCommunitMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);
		myLoginId=PreManager.instance().getUserId(this);
		initView();
//		getUnReadMessage();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initData();
		EaseUI.getInstance().getNotifier().reset();
		EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage,
                        EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventDeliveryAck,
                        EMNotifierEvent.Event.EventReadAck });
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("消息");;
		findViewById(R.id.rl_my_receive).setOnClickListener(this);
		mListView=(ListView) findViewById(R.id.message_list);
		mAdapter=new MessageAdapter(getApplicationContext());
		mListView.setAdapter(mAdapter);
		messages=new ArrayList<MsgListBean>();
		hasCommunitMsg=(ImageView) findViewById(R.id.has_receive_communit);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index=position-mListView.getHeaderViewsCount();
				MsgListBean bean = messages.get(index);
				if("1".equals(bean.getMsgListTyp())){
					startActivity(new Intent(MessageListActivity.this,XcSecretaryActivity.class));
				}else{
					Intent intent = new Intent(MessageListActivity.this,ChatActivity.class);
					UserMessageBean user=new UserMessageBean();
					user.setUid( bean.getUserId());
					user.setNickname(bean.getUserName());
					user.setProfile(bean.getUserUrl());
					user.setProfile_key(bean.getUserKey());
					user.setIs_zj(bean.getType());
					intent.putExtra("userBean", user);
					startActivity(intent);
				}
			}
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				final int index=position-mListView.getHeaderViewsCount();
				if (index < 0) 
					return true;
				MsgListBean bean = messages.get(index);
				if(!"1".equals(bean.getMsgListTyp())){
					dialog  = new MyAlertDialog(MessageListActivity.this,R.style.bottom_animation);
					dialog.setCanceledOnTouchOutside(true);
					dialog.show();
					dialog.setTV1("",View.GONE);
					dialog.setTV2("", null, View.GONE);
					dialog.setTV3("", null,View.GONE);
					
					dialog.setTVbottom("取消", new MyOnClickListener() {
						@Override
						public void Onclick(View v) {
							dialog.dismiss();
							dialog = null;
						}
					}, View.VISIBLE);
					
					dialog.setTV4("删除", new MyOnClickListener() {
						@Override
						public void Onclick(View v) {
							EMChatManager.getInstance().deleteConversation(messages.get(index).getUserId());
							messages.remove(index);
							mAdapter.setData(messages = classifyAndSort(messages));
							dialog.dismiss();
							dialog = null;
						}
					}, View.VISIBLE);
				}
				
				return true;
			}
		});
	}
	                 
	@SuppressWarnings({ "rawtypes", "incomplete-switch" })
	private void initData(){
		messages.clear();
		MsgListBean myTip=SecretaryDBOperate.queryLastMsg(getApplicationContext());
		if (myTip!=null) 
			messages.add(myTip);
		
		Hashtable<String, EMConversation> consList = EMChatManager.getInstance().getAllConversations();
		for(Iterator itr = consList.keySet().iterator(); itr.hasNext();){
			MsgListBean bean=new MsgListBean();
			String key = (String) itr.next();
			EMConversation convers=consList.get(key);
			if(convers.getMsgCount()>0){
				EMMessage message = convers.getLastMessage();
				try {
					bean.setDate(message.getMsgTime());
					bean.setMsgCount(convers.getUnreadMsgCount());
					switch (message.getType()) {
					 case TXT:
						TextMessageBody body = (TextMessageBody)message.getBody();
						bean.setMessage(body.getMessage());
		                break;
		            case IMAGE:
		            	bean.setMessage("[图片]");
		                break;
		            case VOICE:
		            	bean.setMessage("[语音]");
		                break;
					}
					
					JSONObject json = message.getJSONObjectAttribute("attr");
					if(myLoginId.equals(message.getFrom())){
						bean.setUserId(json.getString("r_id"));
						bean.setUserName(json.getString("r_n"));
						bean.setUserUrl(json.getString("r_l"));
						bean.setUserKey(json.getString("r_k"));
						bean.setType(json.getString("r_t"));
					}else{
						bean.setUserId(json.getString("id"));
						bean.setUserName(json.getString("name"));
						bean.setUserUrl(json.getString("url"));
						bean.setUserKey(json.getString("key"));
						bean.setType(json.getString("type"));
					}
				} catch (Exception e) {
				}
				messages.add(bean);
			}
		}
		mAdapter.setData(messages = classifyAndSort(messages));
		
	}
	
	/**
	 * 获取未读消息数
	 */
	private void getUnReadMessage() {
		DynamicMsgNumParamClass mParam = new DynamicMsgNumParamClass();
		mParam.my_int_id = PreManager.instance().getUserId(getApplicationContext());
		new CommunityProcClass(getApplicationContext()).getDynamicMsgNum(mParam,new InterfaceDynamicMsgNumCallBack() {
				@Override
				public void onError(int icode, String strErrMsg) {
					// 暂不处理
				}
				@Override
				public void onSuccess(int icode, String strSuccMsg, int num) {
					if (num > 0)
						hasCommunitMsg.setVisibility(View.VISIBLE);
					else 
						hasCommunitMsg.setVisibility(View.GONE);
				}
			});
	}

	/**
	 * 按时时间 和 未读数 排序
	 * @return List<MsgListBean>
	 */
	private List<MsgListBean> classifyAndSort(List<MsgListBean> messages){
		int size = messages.size();
		List<MsgListBean> tempUnRead = null, tempRead = null, list = null;
		for(int i =0 ; i < size ; i++){
			MsgListBean bean=messages.get(i);
			if("1".equals(bean.getMsgListTyp())){
				if(list == null)
					list = new ArrayList<MsgListBean>();
				list.add(bean);
			}else{
				if(bean.getMsgCount() > 0){
					if(tempUnRead == null)
						tempUnRead=new ArrayList<MsgListBean>();
					tempUnRead.add(bean);
				}else{
					if(tempRead == null)
						tempRead = new ArrayList<MsgListBean>();
					tempRead.add(bean);
				}
			}
		}
		
		if(list == null)
			list = new ArrayList<MsgListBean>();
		
		if (tempUnRead != null){
			tempUnRead = sortByTime(tempUnRead);
			list.addAll(tempUnRead);
		}
		
		if(tempRead != null ){
			tempRead = sortByTime(tempRead);
			list.addAll(tempRead);
		}
		
		return list;
	}
	
	/**
	 * 按时间排序
	 * @param messages 
	 * @return  List<MsgListBean> list
	 */
	private List<MsgListBean> sortByTime(List<MsgListBean> messages){
		int size = messages.size();
		for (int i = 0; i < size ; i++) {
			for (int j = i+1; j < size; j++) {
				MsgListBean tempi = messages.get(i);
				MsgListBean tempj = messages.get(j);
				if(tempj.getDate() > tempi.getDate()){
					messages.set(i, tempj);
					messages.set(j, tempi);
				}
			}
		}
		return messages;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.rl_my_receive:
			startActivity(new Intent(this, MyAutoMsgActivity.class));
			hasCommunitMsg.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	@Override
    public void onStop() {
        super.onStop();
        EMChatManager.getInstance().unregisterEventListener(this);
    }
	
	private int exist(String userId){
		int size = messages.size();
		for (int i = 0; i < size; i++) {
			if(!TextUtils.isEmpty(userId)&& userId.equals(messages.get(i).getUserId())){
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void onEvent(EMNotifierEvent emEvent) {
		EMMessage message = (EMMessage) emEvent.getData();
    	String from = message.getFrom();
		  switch (emEvent.getEvent()) {
	        case EventNewMessage:
	        	EaseUI.getInstance().getNotifier().onNewMsg(message);
	        	if(exist(from) > -1){
	  	        	switch (message.getType()) {
	  				 case TXT:
	  					TextMessageBody body = (TextMessageBody)message.getBody();
	  					messages.get(exist(from)).setMessage(body.getMessage());
	  	                break;
	  	            case IMAGE:
	  	            	messages.get(exist(from)).setMessage("[图片]");
	  	                break;
	  	            case VOICE:
	  	            	messages.get(exist(from)).setMessage("[语音]");
	  	                break;
	  				}
	  	        	
	  	        	try {
	  	        		JSONObject jsonObj=message.getJSONObjectAttribute("attr");
	  	        		messages.get(exist(from)).setUserUrl(jsonObj.getString("url"));
	  	        		messages.get(exist(from)).setUserKey(jsonObj.getString("key"));
					} catch (Exception e) {
					}
	  	        	messages.get(exist(from)).setMsgCount(messages.get(exist(from)).getMsgCount()+1);
	  	        	messages.get(exist(from)).setDate(message.getMsgTime());
	  	        	runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mAdapter.setData(messages = classifyAndSort(messages));
						}
					});
	  	        }else{
	  	        	MsgListBean bean=new MsgListBean();
	  	        	bean.setDate(message.getMsgTime());
	  	        	bean.setUserId(message.getFrom());
	  	        	bean.setMsgCount(1);
	  	        	
	  	        	switch (message.getType()) {
	  				 case TXT:
	  					TextMessageBody body = (TextMessageBody)message.getBody();
	  					bean.setMessage(body.getMessage());
	  	                break;
	  	            case IMAGE:
	  	            	bean.setMessage("[图片]");
	  	                break;
	  	            case VOICE:
	  	            	bean.setMessage("[语音]");
	  	                break;
	  				}
	  	        	try {
	  	        		JSONObject json = message.getJSONObjectAttribute("attr");
	  					bean.setUserName(json.getString("name"));
	  					bean.setUserUrl(json.getString("url"));
	  					bean.setUserKey(json.getString("key"));
	  					bean.setType(json.getString("type"));
  					} catch (Exception e) {}
	  	        	messages.add(bean);
	  	        	runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							mAdapter.setData(messages = classifyAndSort(messages));
						}
					});
	  	        }
	        	
	            break;
	        case EventOfflineMessage: //离线消息
	            break;
	        default:
	            break;
	        }
	}
}
