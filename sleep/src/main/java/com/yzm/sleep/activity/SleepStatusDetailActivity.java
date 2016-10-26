package com.yzm.sleep.activity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.SoftDayData;
import com.yzm.sleep.bean.SleepStatusBean;
import com.yzm.sleep.model.CalenderSelectDialog;
import com.yzm.sleep.model.CalenderSelectDialog.SelectDayDateListener;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.GetSignInDataParams;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.OrangeSleepLengthTable;
import com.yzm.sleep.widget.SoftSleepLengthTable;

/**
 * 睡眠状态 详情
 * @author
 * @params String date 当天的时间 格式 yyyy-MM-dd
 */
@SuppressLint("SimpleDateFormat") 
public class SleepStatusDetailActivity extends BaseActivity {

	private TextView textHint, title, efficiency, sleep, course, morning, deepsleepLength, qiansleepLength;
	private CalenderSelectDialog caDialog;
	private Context mContext;
	private SimpleDateFormat simp; 
	private SoftSleepLengthTable softView;
	private OrangeSleepLengthTable orangTabView;
	private List<SoftDayData> softDatas;
	private List<SleepStatusBean> list;
	private String type;
	private ProgressUtils pro;
	
	/**
	 * 显示进度
	 */
	private void showPro(){
		if(pro==null){
			pro = new ProgressUtils(this);
		}
		pro.show();
	}
	
	/**
	 * 取消进度
	 */
	private void cancelPro(){
		if(pro!=null){
			pro.dismiss();
			pro=null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleepstatus_detail);
		findViewById(R.id.back).setOnClickListener(this);
		if(TextUtils.isEmpty(PreManager.instance().getBundbluetoothPillow(this))){
			type = "1";
			findViewById(R.id.rl_qiansleep).setVisibility(View.GONE);
			findViewById(R.id.rl_deepsleep).setVisibility(View.GONE);
		}else{
			type = "2";
			findViewById(R.id.rl_qiansleep).setVisibility(View.VISIBLE);
			findViewById(R.id.rl_deepsleep).setVisibility(View.VISIBLE);
		}
		title = (TextView) findViewById(R.id.title);
		this.mContext = this;
		simp = new SimpleDateFormat("yyyy-MM-dd");
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setText("日历");
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		softView = (SoftSleepLengthTable) findViewById(R.id.softsleeplengthform);
		orangTabView = (OrangeSleepLengthTable) findViewById(R.id.orangsleeplengthform);
		
		efficiency = (TextView) findViewById(R.id.sleep_efficiency);
		sleep = (TextView) findViewById(R.id.sleep);
		course = (TextView) findViewById(R.id.sleep_course);
		morning = (TextView) findViewById(R.id.sleep_morning);
		deepsleepLength = (TextView) findViewById(R.id.deepsleep_lenght);
		qiansleepLength = (TextView) findViewById(R.id.qiansleep_lenght);
		textHint = (TextView) findViewById(R.id.text_hint);
		title.setTag(getIntent().getStringExtra("date"));
		title.setText(CalenderUtil.getStrByDate(getIntent().getStringExtra("date")));
		if(!Util.checkNetWork(mContext)){
			findViewById(R.id.rl_tab).setVisibility(View.GONE);
			findViewById(R.id.rl_res).setVisibility(View.GONE);
			findViewById(R.id.nonet).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.nonet).findViewById(R.id.text)).setText("请检查网路设置");
		}else{
			findViewById(R.id.rl_tab).setVisibility(View.VISIBLE);
			findViewById(R.id.rl_res).setVisibility(View.VISIBLE);
			findViewById(R.id.nonet).setVisibility(View.GONE);
			textHint.setVisibility(View.VISIBLE);
			textHint.setText(Html.fromHtml(Constant.texteHint));
			if("2".equals(type))
				orangTabView.setVisibility(View.VISIBLE);
			else
				softView.setVisibility(View.VISIBLE);
			
			getDatas(getIntent().getStringExtra("date"));
		}
		
		setListtener();
	}
	
	
	private void setListtener(){
		softView.setOnClickPosition(new SoftSleepLengthTable.SleepLengthSelect() {
			
			@Override
			public void select(int position) {
				title.setText(CalenderUtil.getStrByDate(softDatas.get(position).getDate()));
				title.setTag(softDatas.get(position).getDate());
				modifiShowData(position);
			}
		});
		
		orangTabView.setOnClickPosition(new OrangeSleepLengthTable.SleepLengthSelect() {
			
			@Override
			public void select(int position) {
				title.setText(CalenderUtil.getStrByDate(list.get(position).getDatestring(), "yyyyMMdd"));
				title.setTag(softDatas.get(position).getDate());
				modifiShowData(position);
			}
		});
	}
	
	private void modifiShowData(int position) {
		try {
			efficiency.setText((int) (new BigDecimal(Double.parseDouble(list.get(position).getXiaolv())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100) +"%");
		} catch (Exception e) {
			efficiency.setText("0%");
		}
		try {
			sleep.setText(list.get(position).getSleep1());
			course.setText(list.get(position).getSleep2());
			morning.setText(list.get(position).getSleep3());
			if("2".equals(type)){
				SleepStatusBean bean = list.get(position);
				float deepLength = new BigDecimal(Double.parseDouble(bean.getDeepsleep())/60).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
				float totalLength = new BigDecimal(Double.parseDouble(bean.getSleeplong())/60).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
				deepsleepLength.setText(deepLength + "h");
				qiansleepLength.setText((totalLength*10 - deepLength*10)/10 + "h");
			} 
		} catch (Exception e) {
			sleep.setText("无数据");
			course.setText("无数据");
			morning.setText("无数据");
			if("2".equals(type)){
				deepsleepLength.setText("无数据");
				qiansleepLength.setText("无数据");
			}
		}
	}
	
	/**
	 * 获取数据
	 * @param date
	 */
	private void getDatas(final String date){
		if(!Util.checkNetWork(mContext)){
			Util.show(mContext, "网路连接错误");
			return;
		}
		showPro();
		GetSignInDataParams mParams = new GetSignInDataParams();
		mParams.my_int_id = PreManager.instance().getUserId(mContext);
		mParams.date = date.replace("-", "");
		mParams.days = "7";
		mParams.type = type;
		new XiangchengMallProcClass(mContext).getSignInData(mParams, new GetSignInDataCallBack() {
			
			@Override
			public void onSuccess(int icode, List<SleepStatusBean> list) {
				cancelPro();
				doCallbBackData(date, list);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				cancelPro();
				Util.show(mContext, strErrMsg);
			}
		});
	}
	
	private int findIndex(String date, List<SleepStatusBean> list){
		for (int i = 0; i < list.size(); i++) {
			if(date.equals(list.get(i).getDatestring())){
				return i;
			}
		}
		return -1;
	}
	
	private void doCallbBackData(String date, List<SleepStatusBean> mlist){
		list = new ArrayList<SleepStatusBean>();
		softDatas = new ArrayList<SoftDayData>();
		String[] dates = CalenderUtil.getSevenDay(date, 7);
		for(int i=0; i < dates.length; i++){
			String str= dates[i];
			SoftDayData softData = new SoftDayData();
			softData.setDate(str);
			int index = findIndex(str.replace("-", ""), mlist);
			if(index >= 0){
				try {
					float sleepLength = Float.parseFloat(mlist.get(index).getSleeplong());
					int lenght = (int)((sleepLength/60)*10);
//					if(lenght == 0){
//						mlist.get(index).setSleep1("无数据");
//						mlist.get(index).setSleep2("无数据");
//						mlist.get(index).setSleep3("无数据");
//						mlist.get(index).setDeepsleep("0");
//						mlist.get(index).setQiansleep("0");
//					}
					softData.setSleepLength(lenght/10f);
				} catch (Exception e) {
					softData.setSleepLength(0f);
				}
				list.add(mlist.get(index));
			}else{
				SleepStatusBean bean= new SleepStatusBean();
				bean.setDatestring(str.replace("-", ""));
				bean.setXiaolv("0");
				bean.setDeepsleep("0");
				bean.setQiansleep("0");
				bean.setSleeplong("0");
				bean.setSleep1("无数据");
				bean.setSleep2("无数据");
				bean.setSleep3("无数据");
				list.add(bean);
				softData.setSleepLength(0f);
			}
			softDatas.add(softData);
		}
		
		
		if("2".equals(type))
			orangTabView.setData(list);
		else
			softView.setData(softDatas);
		
		modifiShowData(list.size() - 1);
		
	}

	/**
	 * 日历选择
	 */
	private void showCalendar(){
		try {
			if(title.getTag() == null || TextUtils.isEmpty(title.getTag().toString()))
				title.setTag(simp.format(new Date()));
			caDialog = new CalenderSelectDialog(this,getScreenWidth(),title.getTag().toString(), new SelectDayDateListener() {
				@Override
				public void selectday(String dayDate) {
					try {
						long day = simp.parse(dayDate).getTime();
						Calendar calendar=Calendar.getInstance();
						calendar.add(Calendar.DAY_OF_MONTH, -1);
						if(day <= simp.parse(simp.format(calendar.getTime())).getTime()){
							title.setText(CalenderUtil.getStrByDate(dayDate));
							title.setTag(dayDate);
							getDatas(dayDate);
						}else{
							Util.show(mContext, "不能玩穿越");
						}
						
					} catch (Exception e) {
					}
				}
			});
			caDialog.show();
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			showCalendar();
			break;
		default:
			break;
		}
	}

}
