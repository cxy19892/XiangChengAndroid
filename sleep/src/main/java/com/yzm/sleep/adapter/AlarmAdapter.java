package com.yzm.sleep.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.background.AlarmDBOperate;
import com.yzm.sleep.background.AlarmService.ListAlarmTime;
import com.yzm.sleep.background.MyDatabaseHelper;
import com.yzm.sleep.background.MyTabList;
import com.yzm.sleep.utils.AlarmUtils;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.SleepUtils;
/**
 * 起床闹钟适配器类
 * @author Administrator
 *
 */
public class AlarmAdapter extends BaseAdapter{
	Context mContext;
	private ArrayList<ListAlarmTime> getALarmData ;
	private LayoutInflater inflater;
	private Context context;

	public AlarmAdapter(Context context){
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}
	public void setAlarm(Context context,ArrayList<ListAlarmTime> getALarmData){
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.getALarmData = getALarmData;
		
	}
	
	public void setDate(ArrayList<ListAlarmTime> getALarmData){
		this.getALarmData = getALarmData;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return getALarmData != null ? getALarmData.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return getALarmData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ListAlarmTime alarmInfo = (ListAlarmTime)getItem(position);
		ViewHold viewHold = null;
		if(convertView == null){
			viewHold = new ViewHold();
			convertView = inflater.inflate(R.layout.alarm_item, null);

//			viewHold.iv = (CircleImageView)convertView.findViewById(R.id.iv);
//			viewHold.iv_alarm_type_pre = (ImageView)convertView.findViewById(R.id.iv_alarm_type_pre);
//			viewHold.tv_awaketime = (TextView)convertView.findViewById(R.id.tv_awaketime);
//			viewHold.tv_weeks = (TextView)convertView.findViewById(R.id.tv_weeks);
//			viewHold.cb_switch = (ToggleButton) convertView.findViewById(R.id.toggleButton);
//			viewHold.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//			viewHold.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
//			viewHold.item_back = (RelativeLayout) convertView.findViewById(R.id.item_back);
//			viewHold.toggleButton_p=(RelativeLayout)convertView.findViewById(R.id.toggleButton_p);
			viewHold.lin_switcLayout = (RelativeLayout) convertView.findViewById(R.id.lin_switch_layout);
			viewHold.tv_time = (TextView) convertView.findViewById(R.id.item_tv_time);
			viewHold.v_switch_icon = convertView.findViewById(R.id.item_v_icon);
			viewHold.tv_title = (TextView) convertView.findViewById(R.id.item_tv_remind_title);
			viewHold.tv_repeat = (TextView) convertView.findViewById(R.id.item_tv_remind_repeat);
			viewHold.iv_next   = (ImageView) convertView.findViewById(R.id.iv_img_next);
			
			convertView.setTag(viewHold);
		}else{
			viewHold = (ViewHold)convertView.getTag();
		}
		
		viewHold.lin_switcLayout.setOnClickListener(new MyOnClickListener(viewHold, alarmInfo));
		viewHold.tv_time.setText(alarmInfo.AlarmTime);
		if(alarmInfo.AlarmOnOff == 1){
			viewHold.v_switch_icon.setBackgroundColor(context.getResources().getColor(R.color.theme_color));
			viewHold.tv_time.setTextColor(context.getResources().getColor(R.color.ct_color));
			viewHold.tv_title.setTextColor(context.getResources().getColor(R.color.ct_color));
			viewHold.tv_repeat.setTextColor(context.getResources().getColor(R.color.ct_color));
			viewHold.iv_next  .setImageDrawable(context.getResources().getDrawable(R.drawable.ic_right1));
		}else{
			viewHold.v_switch_icon.setBackgroundColor(context.getResources().getColor(R.color.fct_color));
			viewHold.tv_time.setTextColor(context.getResources().getColor(R.color.fct_color));
			viewHold.tv_title.setTextColor(context.getResources().getColor(R.color.fct_color));
			viewHold.tv_repeat.setTextColor(context.getResources().getColor(R.color.fct_color));
			viewHold.iv_next  .setImageDrawable(context.getResources().getDrawable(R.drawable.ic_right));
		}
		viewHold.tv_title.setText(TextUtils.isEmpty(alarmInfo.remindTitle)?(alarmInfo.isRemind==0?(alarmInfo.AlarmID==1?"睡觉提醒":"早起提醒"):"提醒"):alarmInfo.remindTitle);
		viewHold.tv_repeat.setText(alarmInfo.AlarmOnOff == 1?AlarmUtils.getWeeksString(alarmInfo.AlarmPlant):"关闭");
//		viewHold.cb_switch.setOnClickListener(new MyOnClickListener(viewHold,viewHold.cb_switch,alarmInfo));
////		viewHold.toggleButton_p.setOnClickListener(new MyOnClickListener(viewHold,viewHold.cb_switch,alarmInfo));
//		
//		viewHold.tv_awaketime.setText(alarmInfo.AlarmTime);
//		viewHold.tv_weeks.setText(AlarmUtils.getWeeksString(alarmInfo.AlarmPlant));
//		viewHold.tv_title.setText(alarmInfo.AlarmTitle);
//		viewHold.tv_nickname.setText("【" + alarmInfo.AlarmUserNickname + "】");
//		if (alarmInfo.AlarmOnOff == 0) {
//			viewHold.cb_switch.setChecked(false);
//		}else if (alarmInfo.AlarmOnOff == 1){		//闹钟开启   一次性过时闹钟显示关闭
//			viewHold.cb_switch.setChecked(true);
//			if (alarmInfo.AlarmRepeat == 0) { //闹钟不重复
//				SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
//				//日期，同下，格式yyyy-MM-dd
//				//
//				 Date setDate = null;
//				try {
//					setDate = dateformat1.parse(alarmInfo.AlarmDay + " " + alarmInfo.AlarmTime);
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				 
//				 Date mDate = new Date(System.currentTimeMillis());
//				
//				 if (setDate != null && mDate != null) {
//					
//					 if (mDate.getTime() > setDate.getTime()) {//mdate 在 setDate之后，闹钟过期
//						 viewHold.cb_switch.setChecked(false);
//					 }else {									//mdate 在 setDate之前,闹钟未过期
//						 viewHold.cb_switch.setChecked(true);
//					 }
//				}
//			}
//		}
		
		
		
		//设置闹钟图标 
//		MyDatabaseHelper helper = MyDatabaseHelper.getInstance(context);
//		SQLiteDatabase db = helper.getWritableDatabase();
//		AudioDAO audioDAO = new AudioDAO(db,MyTabList.RECORD_AUDIO);
//		
//		if (!TextUtils.isEmpty(alarmInfo.AlarmAudioCover)) {//如果有铃声封面显示铃声封面
//			if (alarmInfo.AudioIsLocalRecord == 1) {
//				String string = (Uri.fromFile(new File(alarmInfo.AlarmAudioCover)).toString());
//				ImageLoaderUtils.getInstance().displayImage(string, viewHold.iv,ImageLoaderUtils.getOpthion());
//			}else {
//				if (alarmInfo.AudioCoverKey != null) {
//					ImageLoaderUtils.getInstance().displayImage(alarmInfo.AlarmAudioCover,alarmInfo.AudioCoverKey,viewHold.iv,ImageLoaderUtils.getOpthion());
//				}
//			}
//		}
//		else 
//		{
//			if (!TextUtils.isEmpty(audioDAO.isLocalRecord(alarmInfo.AlarmAudio))) {
//				viewHold.iv.setImageResource(R.drawable.music_icon);
//			}else {
//				ImageLoaderUtils.getInstance().displayImage(alarmInfo.AlarmProfile, viewHold.iv,ImageLoaderUtils.getOpthion());
//			}
//		}
//		
//		//设置闹钟类型图标
//		if (alarmInfo.AlarmType == 0) {//非专属铃音
//			viewHold.iv_alarm_type_pre.setBackgroundDrawable(null);
//		}else if (alarmInfo.AlarmType == 1){//普通铃音
//			viewHold.iv_alarm_type_pre.setBackgroundResource(R.drawable.alarm_exclusive_ring_tone_icon);
//		}else if (alarmInfo.AlarmType == 2){//神秘铃音
//			viewHold.iv_alarm_type_pre.setBackgroundResource(R.drawable.alarm_mysterious_ring_tone_icon);
//		}
		
		//关闭的闹钟灰度
//		int black = context.getResources().getColor(R.color.black);
//		int text_color_xshallow = context.getResources().getColor(R.color.text_color_xshallow);
//		if (alarmInfo.AlarmOnOff == 0) {//未启动
//			viewHold.tv_awaketime.setTextColor(text_color_xshallow);
//			viewHold.tv_weeks.setTextColor(text_color_xshallow);
//			viewHold.tv_title.setTextColor(text_color_xshallow);
//			viewHold.tv_nickname.setTextColor(text_color_xshallow);
//			viewHold.item_back.setBackgroundColor(Color.parseColor("#efefef"));
//		}else if (alarmInfo.AlarmOnOff == 1){//启动
//			viewHold.tv_awaketime.setTextColor(black);
//			viewHold.tv_weeks.setTextColor(black);
//			viewHold.tv_title.setTextColor(black);
//			viewHold.tv_nickname.setTextColor(black);
//			viewHold.item_back.setBackgroundColor(context.getResources().getColor(R.color.white));
//		}
		
		return convertView;
	}
	
	private class MyOnClickListener implements OnClickListener{
		ListAlarmTime alarmInfo;
		ViewHold mHold;
		public MyOnClickListener(ViewHold viewHold, ListAlarmTime alarm){
			this.mHold = viewHold;
			this.alarmInfo = alarm;
		}
		@SuppressLint("SimpleDateFormat") @Override
		public void onClick(View arg0) {
			ListAlarmTime m_ListAlarmTime = new ListAlarmTime();
			m_ListAlarmTime.AlarmTime = alarmInfo.AlarmTime;
			m_ListAlarmTime.AlarmRepeat = alarmInfo.AlarmRepeat;
			m_ListAlarmTime.AlarmPlant = alarmInfo.AlarmPlant;
			m_ListAlarmTime.AlarmTitle = alarmInfo.AlarmTitle;
			m_ListAlarmTime.AlarmDay = alarmInfo.AlarmDay;
			m_ListAlarmTime.AlarmDelay = alarmInfo.AlarmDelay;
			
			m_ListAlarmTime.AlarmOnOff = alarmInfo.AlarmOnOff;
			
			m_ListAlarmTime.AlarmAudio = alarmInfo.AlarmAudio;
			m_ListAlarmTime.AlarmProfile = alarmInfo.AlarmProfile;
			m_ListAlarmTime.AlarmUserNickname = alarmInfo.AlarmUserNickname;
			m_ListAlarmTime.AlarmUserId = alarmInfo.AlarmUserId;
			m_ListAlarmTime.AlarmDownloads = alarmInfo.AlarmDownloads;
			m_ListAlarmTime.AlarmAudioId = alarmInfo.AlarmAudioId;
			m_ListAlarmTime.AlarmAudioCover = alarmInfo.AlarmAudioCover;
			m_ListAlarmTime.AlarmType = alarmInfo.AlarmType;
			m_ListAlarmTime.AudioIsLocalRecord = alarmInfo.AudioIsLocalRecord;
			m_ListAlarmTime.isRemind = alarmInfo.isRemind;
			m_ListAlarmTime.remindTitle = alarmInfo.remindTitle;
			m_ListAlarmTime.remindContext = alarmInfo.remindContext;
			if (m_ListAlarmTime.AlarmRepeat == 0) {
				
				 SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				 Date aaaDate = null;
				 try {
					aaaDate = dateformat1.parse(m_ListAlarmTime.AlarmDay + " "+ m_ListAlarmTime.AlarmTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (aaaDate != null) {
					long time = aaaDate.getTime();
					if (time// 如果闹钟设置时间还没过期
							- System.currentTimeMillis() > 0) {
						
						m_ListAlarmTime.AlarmDay = SleepUtils.getFormatedDateTime("yyyy-MM-dd", time);
					} else {// 闹钟时间过期
						m_ListAlarmTime.AlarmDay = SleepUtils.getFormatedDateTime("yyyy-MM-dd", time + 24*60*60*1000);
					}
				}
			}
			
			if (m_ListAlarmTime.AlarmOnOff == 0) {
				m_ListAlarmTime.AlarmOnOff = 1;
				alarmInfo.AlarmOnOff = 1;
				updateAwakeAlarm(alarmInfo.AlarmID + "", m_ListAlarmTime);
				mHold.v_switch_icon.setBackgroundColor(context.getResources().getColor(R.color.theme_color));
				mHold.tv_time.setTextColor(context.getResources().getColor(R.color.ct_color));
				mHold.tv_title.setTextColor(context.getResources().getColor(R.color.ct_color));
				mHold.tv_repeat.setTextColor(context.getResources().getColor(R.color.ct_color));
				mHold.iv_next  .setImageDrawable(context.getResources().getDrawable(R.drawable.ic_right1));
			}else {
				m_ListAlarmTime.AlarmOnOff = 0;
				alarmInfo.AlarmOnOff = 0;
				updateAwakeAlarm(alarmInfo.AlarmID + "", m_ListAlarmTime);
				mHold.v_switch_icon.setBackgroundColor(context.getResources().getColor(R.color.fct_color));
				mHold.tv_time.setTextColor(context.getResources().getColor(R.color.fct_color));
				mHold.tv_title.setTextColor(context.getResources().getColor(R.color.fct_color));
				mHold.tv_repeat.setTextColor(context.getResources().getColor(R.color.fct_color));
				mHold.iv_next  .setImageDrawable(context.getResources().getDrawable(R.drawable.ic_right));
			}
			mHold.tv_repeat.setText(alarmInfo.AlarmOnOff == 1?AlarmUtils.getWeeksString(alarmInfo.AlarmPlant):"关闭");
//			int black = context.getResources().getColor(R.color.black);
//			int text_color_xshallow = context.getResources().getColor(R.color.text_color_xshallow);
//			if (m_ListAlarmTime.AlarmOnOff == 0) {//未启动
//				
//				mHold.tv_awaketime.setTextColor(text_color_xshallow);
//				mHold.tv_weeks.setTextColor(text_color_xshallow);
//				mHold.tv_title.setTextColor(text_color_xshallow);
//				mHold.tv_nickname.setTextColor(text_color_xshallow);
//				mHold.item_back.setBackgroundColor(Color.parseColor("#efefef"));
//			}else if (m_ListAlarmTime.AlarmOnOff == 1){//启动
//				
//				mHold.tv_awaketime.setTextColor(black);
//				mHold.tv_weeks.setTextColor(black);
//				mHold.tv_title.setTextColor(black);
//				mHold.tv_nickname.setTextColor(black);
//				mHold.item_back.setBackgroundColor(context.getResources().getColor(R.color.white));
//			}
			
		}
		
	}

	private class ViewHold {
//		public CircleImageView iv;
//		public ImageView iv_alarm_type_pre;
//		public TextView tv_awaketime;
//		public TextView tv_weeks;
//		public ToggleButton cb_switch;
//		public TextView tv_title;
//		public TextView tv_nickname;
//		public RelativeLayout item_back,toggleButton_p;
		
		public TextView tv_time;
		public View     v_switch_icon;
		public TextView tv_title;
		public TextView tv_repeat;
		public RelativeLayout lin_switcLayout;
		public ImageView iv_next;
	}
	/**
	 * 更新闹钟信息
	 * @param id
	 * @param AlarmTime
	 * @param AlarmRepeat
	 * @param AlarmPlant
	 * @param AlarmTitle
	 * @param AlarmDay
	 * @param AlarmDelay
	 * @param alarmAudio
	 * @param AlarmOnOff
	 * @param AlarmProfile
	 * @param AlarmUserNickname
	 * @param AlarmUserId
	 * @param AlarmDownloads
	 */
	ProgressUtils pb;
	private void updateAwakeAlarm(final String id,final ListAlarmTime m_ListAlarmTime) {
//		pb = new ProgressUtils(context);
//		pb.show();
		AsyncTask<Void, Void, Void> task  = new AsyncTask<Void, Void, Void>(){
			
			@Override
			protected Void doInBackground(Void... params) {
				MyDatabaseHelper helper = MyDatabaseHelper.getInstance(mContext);
				SQLiteDatabase db = helper.getWritableDatabase();
				AlarmDBOperate alarmDBOperate = new AlarmDBOperate(db, MyTabList.ALARM_TIME);
				alarmDBOperate.updateAwakeAlarm(mContext,id,m_ListAlarmTime);
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
//				if (pb != null) {
//					pb.dismiss();
//				}
//				ToastManager.getInstance(mContext).show("设置成功");
				Intent intent = new Intent();
				intent.setAction("com.xc.alarm.add.ALARM_UPDATE_SUCCESS");
				mContext.sendBroadcast(intent);
			}
		};
		task.execute();
	}

}
