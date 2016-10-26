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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Status;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.TextMessageBody;
import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.EstimateBaseActivity;
import com.yzm.sleep.activity.EstimateWebActivity;
import com.yzm.sleep.activity.PersonalInfoActivity;
import com.yzm.sleep.activity.WebViewActivity;
import com.yzm.sleep.bean.ExtAttrBean;
import com.yzm.sleep.bean.ExtBean;
import com.yzm.sleep.bean.SaveChatInfoBean;
import com.yzm.sleep.bean.UserMessageBean;
import com.yzm.sleep.imageSelect.MultiImageSelectorActivity;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.utils.CustomTextWatcher;
import com.yzm.sleep.utils.CustomTextWatcher.TextWatcherCallBack;
import com.yzm.sleep.utils.EaseUI;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.ImageCompressUtil;
import com.yzm.sleep.utils.InterfaceMallUtillClass;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceCheckIsKefuCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSaveChatInfoCallback;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SaveChatInfoParamClass;
import com.yzm.sleep.utils.PicUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.EaseChatRowVoicePlayClickListener;
import com.yzm.sleep.widget.EaseEmojiconMenu;
import com.yzm.sleep.widget.EaseEmojiconMenuBase;
import com.yzm.sleep.widget.EaseEmojiconMenuBase.EaseEmojiconMenuListener;
import com.yzm.sleep.widget.EaseVoiceRecorderView;
import com.yzm.sleep.widget.EaseVoiceRecorderView.EaseVoiceRecorderCallback;

/**
 * 聊天页面 tianxun
 * @params userBean UserMessageBean
 */
@SuppressWarnings("deprecation")
public class ChatActivity extends BaseActivity implements OnClickListener,OnTouchListener,EMEventListener{
	
	private List<EMMessage> msgs;
	private ChatMessageAdapter messageAdapter;
    protected boolean haveMoreData = true, isKF= false, isloadHistory=false;
    private EMConversation conversation;
	protected ClipboardManager clipboard;
	// 表情控件
	private EaseVoiceRecorderView recordMenu;
	private EaseEmojiconMenuBase emojiconMenu;
	private ImageButton btnTexteinput, btnRecord,btnAdd;
	private Button btnTouchRecord,btnSendMesage;
	private ListView listMessage;
	private EditText edtMesage;
	private MyAlertDialog picSelectDialog,dialog;
	private String photoUri;
	private TextView title, work_time;
	private UserMessageBean userInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatpage);
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setCompoundDrawables(null, null, null, null);
		right.setText("主页");
		right.setVisibility(View.GONE);
		right.setOnClickListener(this);
		userInfo=(UserMessageBean) getIntent().getSerializableExtra("userBean");
		chekIsKf(userInfo.getUid());
		title.setText(userInfo.getNickname());
		initView();

	}

	private  void loginStatusHX(){
		new XiangchengMallProcClass(this).loginStatus(userInfo.getUid(), new InterfaceMallUtillClass.InterfacLoginStatusHXCallBack() {
			@Override
			public void onSuccess(String icode, String flag) {
				if("1".equals(flag)){
					title.setText(userInfo.getNickname() +"(在线)");
				}else {
					title.setText(userInfo.getNickname() +"(离线)");
				}
			}

			@Override
			public void onError(String icode, String strErrMsg) {
				title.setText(userInfo.getNickname() +"(离线)");
			}
		});
	}
	
	private void initView() {
		work_time = (TextView) findViewById(R.id.work_time);
		listMessage=(ListView) findViewById(R.id.list_message);
		messageAdapter=new ChatMessageAdapter(this, listMessage, Constant.CHATTYPE_SINGLE, mListItemClickListener);
        listMessage.setAdapter(messageAdapter);
		btnTexteinput = (ImageButton) findViewById(R.id.btn_texteinput);
		btnTexteinput.setOnClickListener(this);
		btnRecord = (ImageButton) findViewById(R.id.btn_record);
		btnRecord.setOnClickListener(this);
		findViewById(R.id.btn_face).setOnClickListener(this);
		btnAdd=(ImageButton) findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(this);
		edtMesage = (EditText) findViewById(R.id.edt_mesage);
		edtMesage.setOnTouchListener(this);
		btnTouchRecord = (Button) findViewById(R.id.btn_touch_record);
		btnTouchRecord.setOnTouchListener(this);
		btnSendMesage=(Button) findViewById(R.id.btn_sendmesage);
		btnSendMesage.setOnClickListener(this);
		emojiconMenu = (EaseEmojiconMenu) findViewById(R.id.emojicon);
		recordMenu = (EaseVoiceRecorderView) findViewById(R.id.voice_recorder);
		clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		emojiconMenu.setEmojiconMenuListener(new EaseEmojiconMenuListener() {
			@Override
			public void onExpressionClicked(CharSequence emojiContent) {
				 String oriContent = edtMesage.getText().toString();
				 int index = Math.max(edtMesage.getSelectionStart(), 0);
				 StringBuilder sBuilder = new StringBuilder(oriContent);
				 sBuilder.insert(index, emojiContent);
				 edtMesage.setText(sBuilder.toString());
				 edtMesage.setSelection(index + emojiContent.length());
				  
			}
			@Override
			public void onDeleteImageClicked() {
				if (!TextUtils.isEmpty(edtMesage.getText())) {
					KeyEvent event = new KeyEvent(0, 0, 0,KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,KeyEvent.KEYCODE_ENDCALL);
					edtMesage.dispatchKeyEvent(event);
				}
			}
		});
		
		edtMesage.addTextChangedListener(new CustomTextWatcher(ChatActivity.this, edtMesage, new TextWatcherCallBack() {
			
			@Override
			public void onTextNumsBack(int num,String msg) {
				if(num > 0){
					btnAdd.setVisibility(View.GONE);
					btnSendMesage.setVisibility(View.VISIBLE);
				}else{
					btnSendMesage.setVisibility(View.GONE);
					btnAdd.setVisibility(View.VISIBLE);
				}
			}
		}));
		
		listMessage.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				isloadHistory = true;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (isloadHistory && firstVisibleItem == 0 && haveMoreData && listMessage.getChildCount() > 1) {
                    List<EMMessage> messages = conversation.loadMoreMsgFromDB(msgs.get(0).getMsgId(),20);
                    if (messages.size() > 0) {
                    	msgs.addAll(0, messages);
                    	messageAdapter.setData(msgs);
                        messageAdapter.refreshSeekTo(messages.size() - 1);
                        if (messages.size() < 20) 
                            haveMoreData = false;
                        else
                        	haveMoreData = true;
                    }else
                    	haveMoreData = false;
                }
			}
		});
		
	}
	
	/**
	 * 检查是否是客服
	 * @param chatUserId
	 */
	private void chekIsKf(String chatUserId){
		new XiangchengMallProcClass(this).checkIsKefu(chatUserId, new InterfaceCheckIsKefuCallback() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				if("1".equals(strSuccMsg)) {
					loginStatusHX();
					isKF = true;
					work_time.setVisibility(View.VISIBLE);
				}else {
					isKF = false;
					work_time.setVisibility(View.GONE);
				}
				loadData();
				
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				isKF = false;
				work_time.setVisibility(View.GONE);
				loadData();
				
			}
		});
	}
	
	/**
	 * 加载数据
	 */
	private void loadData(){
	    conversation = EMChatManager.getInstance().getConversation(userInfo == null ? "" : userInfo.getUid());
	    conversation.markAllMessagesAsRead();
	    messageAdapter.setData(msgs = conversation.getAllMessages());
	    if(msgs.size() < 20)
	    	haveMoreData=false;
	    //发送病情
	    if(userInfo != null && !TextUtils.isEmpty(userInfo.getAskString())){
	    	new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					EMMessage message = EMMessage.createTxtSendMessage(userInfo.getAskString(), userInfo ==null ? "" : userInfo.getUid());
					sendMessage(message);
				}
			}, 200);
	    }
		selecePo(messageAdapter.getCount() - 1);
//	    messageAdapter.refreshSelectLast();
	}

	/*滑动到指定位置*/
	private void selecePo(int position){
		if(position >= 0) {
			if (android.os.Build.VERSION.SDK_INT >= 8)
				listMessage.smoothScrollToPosition(position);
			else
				listMessage.setSelection(position);
		}
	}

	 //listView item 的点击效果
	 private MessageListItemClickListener mListItemClickListener= new MessageListItemClickListener() {
         
         @Override
         public void onUserAvatarClick(String username) {
         	 //头像点击事件
         	 AppManager.getAppManager().finishActivity(PersonalInfoActivity.class);
	         Intent intent = new Intent(getApplicationContext(), PersonalInfoActivity.class);
	         intent.putExtra("user_id", username);
	         intent.putExtra("isfinish", true);//用于从个人主页再次进入聊天 页面关闭 个人主页
	         startActivity(intent);
         }
         
         @Override //发送失败
         public void onResendClick(final EMMessage message) {
        	 message.status=EMMessage.Status.CREATE;
        	 EMChatManager.getInstance().sendMessage(message, null);
        	 messageAdapter.refresh();
         }
         
         @Override
         public void onBubbleLongClick(EMMessage message) {
         	final EMMessage mes = message;
         	dialog  = new MyAlertDialog(ChatActivity.this,R.style.bottom_animation);
    		dialog.setCanceledOnTouchOutside(true);
    		dialog.show();
    		dialog.setTV1("",View.GONE);
    		dialog.setTV2("", null, View.GONE);
    		dialog.setTV3("复制", new MyOnClickListener() {
				@Override
				public void Onclick(View v) {
					TextMessageBody	body=(TextMessageBody)mes.getBody();
					clipboard.setText(body.getMessage());
					dialog.dismiss();
					dialog=null;
				}
			}, (message.getType() == Type.TXT)? View.VISIBLE : View.GONE);
    		
    		dialog.setTVbottom("取消", new MyOnClickListener() {
    			@Override
    			public void Onclick(View v) {
    				dialog.dismiss();
    				dialog = null;
    			}
    		}, View.VISIBLE);
    		
    		dialog.setTV4((message.getType() == Type.VOICE)? "删除语音" : "删除", new MyOnClickListener() {
    			@Override
    			public void Onclick(View v) {
    				 if(EaseChatRowVoicePlayClickListener.isPlaying && EaseChatRowVoicePlayClickListener.currentPlayListener!=null)
    						EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice(mes.getMsgId());
    				conversation.removeMessage(mes.getMsgId());
					int index=exist(mes.getMsgId());
					if(index > -1){
						msgs.remove(index);
					}
					messageAdapter.setData(msgs);
					messageAdapter.refresh();
					dialog.dismiss();
					dialog=null;
    			}
    		}, View.VISIBLE);
 		}
        
         
         @Override
         public boolean onBubbleClick(EMMessage message) {
    	 	try {
         		//r_pg 评估
         		JSONObject jsonObj=message.getJSONObjectAttribute("attr");
         		if(jsonObj.has("r_pg") && "1".equals(jsonObj.getString("r_pg"))){
         			if(PreManager.instance().getIsCompleteSleepPg(ChatActivity.this))
             			startActivity(new Intent(ChatActivity.this, EstimateBaseActivity.class));
         			else
         				startActivity(new Intent(ChatActivity.this, EstimateWebActivity.class).putExtra("type", "0"));
         		}
				if (jsonObj.has("r_url_type")  && "1".equals(jsonObj.getString("r_url_type")) ) {
					if(jsonObj.has("r_url") && !TextUtils.isEmpty(jsonObj.getString("r_url"))){
						startActivity(new Intent(ChatActivity.this, WebViewActivity.class)
								.putExtra("type", 2).putExtra("url",jsonObj.getString("r_url"))
								.putExtra("title", "预约"));
					}
				}

			} catch (Exception e) {}
    	 	
             return false; 
         }
     };

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private  int  exist(String msgId){
		int size =  msgs.size();
		for (int i = 0; i < size; i++) {
			if(msgId.equals(msgs.get(i).getMsgId())){
				return i;
			}
		}
		return -1;
	}
	
	 /**
     * @param uriString
     */
    private String dealPictureNew(String uriString){
    	Bitmap bitmap = getBitpMapNew(uriString);
    	if (getBitpMapNew(uriString) != null) {
    		if( null == PicUtil. bitmap2Drawable(bitmap)){
    			Util.show(ChatActivity.this, "图片错误");
    			return "";
    		}
    		return ImageCompressUtil.saveMyBitmap(bitmap, 0);
    	}
    	return "";
    }
	
	/**
	 * 对图片进行大小的压缩
	 * @param uriString
	 * @return
	 */
	private Bitmap getBitpMapNew(String uriString) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(uriString);
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;// 只读边,不读内容
			Bitmap bitmap = BitmapFactory.decodeFile(uriString, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			float hh = 800f;//
			float ww = 480f;//
			int be = 1;
			if (w > h && w > ww) {
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;// 设置采样率
			newOpts.inPreferredConfig = Config.RGB_565;// 该模式是默认的,可不设
			newOpts.inPurgeable = true;// 同时设置才会有效
			newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
			bitmap = BitmapFactory.decodeStream(fis, null, newOpts);
			return bitmap;
		} catch (Exception e) {
		}finally{
			try {
				fis.close();
			} catch (Exception e2) {
			}
		}
		return null;
	}

	/**
	 * onActivityResult
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constant.TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				String picPath=dealPictureNew(photoUri);
				if(!TextUtils.isEmpty(picPath)){
					 File file = new File(picPath);
			         if (file != null && file.exists()) {
			        	 EMMessage message = EMMessage.createImageSendMessage(picPath, false, userInfo ==null ? "" : userInfo.getUid());
					     sendMessage(message);
			         }
				}
			}
			break;
		case Constant.SELECT_PIC_REPLACE:
			if (resultCode == RESULT_OK) {
				ArrayList<String> selectImgs = data.getStringArrayListExtra(Constant.EXTRA_RESULT);
				if (selectImgs.size() > 0){
					 File file = new File(selectImgs.get(0));
			         if (file != null && file.exists()) {
			        	 EMMessage message = EMMessage.createImageSendMessage(selectImgs.get(0), false, userInfo ==null ? "" : userInfo.getUid());
					     sendMessage(message);
			         }
				}
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.btn_touch_record:
			return recordMenu.onPressToSpeakBtnTouch(v, event,
					new EaseVoiceRecorderCallback() {
						@Override
						public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
							// 发送语音消息
							EMMessage message = EMMessage.createVoiceSendMessage(voiceFilePath, voiceTimeLength, userInfo ==null ? "" : userInfo.getUid());
					        sendMessage(message);
						}
					});
		case R.id.edt_mesage:
			emojiconMenu.setVisibility(View.GONE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					showKeyboard(edtMesage);
				}
			}, 300);
			return false;
		default:
			break;
		}
		return false;
	}
	
	/**照相*/
	private void tackphoto() {
		// 存储至DCIM文件夹
		Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);  
        photoUri = FileUtil.getPhotopath();
        File out = new File(photoUri);  
        Uri uri = Uri.fromFile(out);  
        // 获取拍照后未压缩的原图片，并保存在uri路径中 
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intentPhote, Constant. TAKE_PHOTO); // 启动照相
	}
	
	/**
	 * 从相册选择一张图片
	 */
	private void getOneImgByStorage() {
		Intent intent = new Intent(this,MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(Constant.EXTRA_SHOW_CAMERA, false);
		// 最大可选择图片数量
		intent.putExtra(Constant.EXTRA_SELECT_COUNT, 1);
		// 选择模式
		intent.putExtra(Constant.EXTRA_SELECT_MODE, Constant.MODE_SINGLE);
		intent.putStringArrayListExtra(Constant.EXTRA_DEFAULT_SELECTED_LIST,new ArrayList<String>());
		startActivityForResult(intent, Constant.SELECT_PIC_REPLACE);
	}
	
	/**显示新增照片选择对话框*/
	private void showPopupWindow() {
		if (picSelectDialog == null) {
			picSelectDialog  = new MyAlertDialog(ChatActivity.this,R.style.bottom_animation);
			picSelectDialog.setCanceledOnTouchOutside(true);
			picSelectDialog.show();
			picSelectDialog.setTV1("",View.GONE);
			picSelectDialog.setTV2("", null, View.GONE);
			picSelectDialog.setTVbottom("取消", new MyOnClickListener() {
    			@Override
    			public void Onclick(View v) {
    				picSelectDialog.dismiss();
					picSelectDialog=null;
    			}
    		}, View.VISIBLE);
    		
    		picSelectDialog.setTV3("拍照", new MyOnClickListener() {
				@Override
				public void Onclick(View v) {
					tackphoto();
					picSelectDialog.dismiss();
					picSelectDialog=null;
				}
			}, View.VISIBLE);
    		
    		picSelectDialog.setTV4("相册选取", new MyOnClickListener() {
				@Override
				public void Onclick(View v) {
					getOneImgByStorage();
					picSelectDialog.dismiss();
					picSelectDialog=null;
				}
			}, View.VISIBLE);
		}
	}
	
	/**
	 * 发送消息
	 * @param message
	 */
    private void sendMessage(EMMessage message){
    	
    	JSONObject jsonObj = new JSONObject();
    	try {
    		//用户id
			jsonObj.put("id", PreManager.instance().getUserId(getApplicationContext()));
			//用户头像key
			jsonObj.put("key", PreManager.instance().getUserProfileKey(getApplicationContext()));
			//用户头像头像Url
			jsonObj.put("url", PreManager.instance().getUserProfileUrl(getApplicationContext()));
			//用户昵称
			jsonObj.put("name", PreManager.instance().getUserNickname(getApplicationContext()));
			//用户性别
			jsonObj.put("sex", PreManager.instance().getUserGender(getApplicationContext()));
			//用户 是否是专家
			jsonObj.put("type", PreManager.instance().getUserIsExpert(getApplicationContext()));
			//用户的 是否绑定硬件
			jsonObj.put("pillow", PreManager.instance().getBundbluetoothPillow(getApplicationContext()));
			
			//接收人的id
			jsonObj.put("r_id", userInfo ==null ? "" : userInfo.getUid());
			//接收人的头像key
			jsonObj.put("r_k", userInfo==null ? "" : userInfo.getProfile_key());
			//接收人的头像地址
			jsonObj.put("r_l", userInfo==null ? "" : userInfo.getProfile());
			//接收人的昵称
			jsonObj.put("r_n", userInfo==null ? "" : userInfo.getNickname());
			//接收人的类型
			jsonObj.put("r_t", userInfo==null ? "" : (userInfo.getIs_zj() == null ? "0" : userInfo.getIs_zj()));
			
		} catch (Exception e){
		}
    	message.setAttribute("attr", jsonObj);
    	EMChatManager.getInstance().updateCurrentUserNick(PreManager.instance().getUserNickname(getApplicationContext()));//设置ios 推送昵称
        //发送消息
    	if(Util.checkNetWork(this)){
    		message.status=Status.FAIL;
    	}
        EMChatManager.getInstance().sendMessage(message, null);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				selecePo(messageAdapter.getCount() - 1);
			}
		},300);
        //刷新ui
//        messageAdapter.refreshSelectLast();
        
        if(isKF && message.getType() == EMMessage.Type.TXT){
        	SaveChatInfoBean bean =new SaveChatInfoBean();
        	bean.setId("132897500334916208");
        	bean.setType("chat");
        	bean.setData(((TextMessageBody)message.getBody()).getMessage());
        	bean.setFrom(PreManager.instance().getUserId(getApplicationContext()));
        	bean.setTo(userInfo ==null ? "" : userInfo.getUid());
        	ExtBean ext=new ExtBean();
        	ExtAttrBean exb=new ExtAttrBean();
        	exb.setId(PreManager.instance().getUserId(getApplicationContext()));
        	exb.setName(PreManager.instance().getUserNickname(getApplicationContext()));
        	exb.setKey(PreManager.instance().getUserProfileKey(getApplicationContext()));
        	exb.setUrl(PreManager.instance().getUserProfileUrl(getApplicationContext()));
        	exb.setSex(PreManager.instance().getUserGender(getApplicationContext()));
        	exb.setPillow(PreManager.instance().getBundbluetoothPillow(getApplicationContext()));
        	exb.setType(PreManager.instance().getUserIsExpert(getApplicationContext()));
        	exb.setR_id(userInfo ==null ? "" : userInfo.getUid());
        	exb.setR_k(userInfo==null ? "" : userInfo.getProfile_key());
        	exb.setR_l(userInfo==null ? "" : userInfo.getProfile());
        	exb.setR_n(userInfo==null ? "" : userInfo.getNickname());
        	exb.setR_t(userInfo==null ? "" : (userInfo.getIs_zj() == null ? "0" : userInfo.getIs_zj()));
        	ext.setAttr(exb);
        	bean.setExt(ext);
        	saveChatMse(message, bean);
        }
    }
    
    private void saveChatMse(EMMessage message ,SaveChatInfoBean bean){
    	SaveChatInfoParamClass mParam=new SaveChatInfoParamClass();
    	mParam.touid = (userInfo ==null ? "" : userInfo.getUid());
    	mParam.uid = PreManager.instance().getUserId(this);
    	mParam.message = bean;
    	new XiangchengMallProcClass(this).saveChatInfo(mParam, new InterfaceSaveChatInfoCallback() {
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
			}
		});
    }
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_texteinput:
			btnTexteinput.setVisibility(View.GONE);
			btnRecord.setVisibility(View.VISIBLE);
			btnTouchRecord.setVisibility(View.GONE);
			edtMesage.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_record:
			btnRecord.setVisibility(View.GONE);
			btnTexteinput.setVisibility(View.VISIBLE);
			btnTouchRecord.setVisibility(View.VISIBLE);
			edtMesage.setVisibility(View.GONE);
			edtMesage.setText("");
			hideKeyboardB(edtMesage);
			emojiconMenu.setVisibility(View.GONE);
			break;
		case R.id.btn_face:
			hideKeyboardB(edtMesage);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					btnRecord.setVisibility(View.VISIBLE);
					btnTexteinput.setVisibility(View.GONE);
					btnTouchRecord.setVisibility(View.GONE);
					edtMesage.setVisibility(View.VISIBLE);
					if (emojiconMenu.getVisibility() == View.VISIBLE)
						emojiconMenu.setVisibility(View.GONE);
					else {
						emojiconMenu.setVisibility(View.VISIBLE);
					}
				}
			}, 300);
			break;
		case R.id.btn_add:
			hideKeyboardB(edtMesage);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					 showPopupWindow();
				}
			}, 300);
			break;
		case R.id.btn_title_right:
			
			break;
		case R.id.btn_sendmesage:
			String mesage=edtMesage.getText().toString(); 
			edtMesage.setText("");
			EMMessage message = EMMessage.createTxtSendMessage(mesage, userInfo ==null ? "" : userInfo.getUid());
	        sendMessage(message);
			break;
		default:
			break;
		}
		
		if(picSelectDialog!=null && view.getId()!=R.id.btn_add){
			picSelectDialog.dismiss();
			picSelectDialog=null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		hideKeyboardB(edtMesage);
		EMChatManager.getInstance().unregisterEventListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EaseUI.getInstance().getNotifier().reset();
		EMChatManager.getInstance().registerEventListener(this,
	                new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage,
	                        EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventDeliveryAck,
	                        EMNotifierEvent.Event.EventReadAck });
		MobclickAgent.onPageStart("FriendsRank_HomePage_Chat"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}
	
	@Override
    public void onStop() {
        super.onStop();
        if(EaseChatRowVoicePlayClickListener.isPlaying && EaseChatRowVoicePlayClickListener.currentPlayListener!=null)
			EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
    }

	@Override
	protected void onPause() {
		super.onPause();
		hideKeyboardB(edtMesage);
		// 中会保存信息
		MobclickAgent.onPageEnd("FriendsRank_HomePage_Chat");
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		UserMessageBean bean=(UserMessageBean) intent.getSerializableExtra("userBean");
		if ((userInfo ==null ? "" : userInfo.getUid()).equals(bean == null ? "0" : bean.getUid()))
			super.onNewIntent(intent);
		else {
			userInfo = bean;
			loadData();
			title.setText(userInfo.getNickname());
		}
	}

	@Override
	public void onEvent(EMNotifierEvent emEvent) {
		EMMessage message = (EMMessage) emEvent.getData();
		String msId=message.getMsgId();
    	String from = message.getFrom();
		  switch (emEvent.getEvent()) {
	        case EventNewMessage:
	            if (from.equals(userInfo ==null ? "" : userInfo.getUid())) {
	                EaseUI.getInstance().getNotifier().viberateAndPlayTone(message);
	                conversation.markMessageAsRead(msId);
	                messageAdapter.refreshSelectLast();
	            } else {
	                EaseUI.getInstance().getNotifier().onNewMsg(message);
	            }
	            break;
	        case EventDeliveryAck: //送达  回执
	        	if (conversation != null) {
					EMMessage msg = conversation.getMessage(msId);
					if (msg != null) 
						msg.isAcked = true;
				}
	        	break;
	        case EventReadAck: //已读 回执
				if (conversation != null) {
					EMMessage msg = conversation.getMessage(msId);
					if (msg != null) 
						msg.isAcked = true;
				}
	            break;
	        case EventOfflineMessage: //离线消息
	        	messageAdapter.refreshSelectLast();
	            break;
	        default:
	            break;
	        }
	}
}
