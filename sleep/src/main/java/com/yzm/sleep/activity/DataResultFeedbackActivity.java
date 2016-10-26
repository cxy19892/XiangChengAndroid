package com.yzm.sleep.activity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.CheckBean;
import com.yzm.sleep.bean.FankuiDataBean;
import com.yzm.sleep.bean.ReportDataBean;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceAddRecordFeedbackCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceGetRecordByDateCallBack;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomInputDialog;
import com.yzm.sleep.widget.CustomInputDialog.InputClass;
import com.yzm.sleep.widget.MyCustomClockView;
import com.yzm.sleep.widget.TagStreamLayout;

/**
 * 每日数据反馈
 * @params date 日期  
 * @params type 1.需要请求， 其它不需要
 */
@SuppressLint("SimpleDateFormat") 
public class DataResultFeedbackActivity extends BaseActivity {
	
	private MarginLayoutParams lp;
	private TextView title;
	private TextView tvEfficiency, tvOnbedLenght, tvSleepLenght;
	private LinearLayout rlContent, rljf;
	private ReportDataBean reportData;
	private List<FankuiDataBean> fankuiDatas;
	private String[] answers= {"A","B","C","D","E","F","G"};
	private String date;
	private Button btnRecordtz;
	private TextView btn1, btn2, btn3, btn4, btn5;
	private MyCustomClockView myClock1, myClock2;
	private ProgressUtils pro;
	private View view_bg, viewp;
	
	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
			pro.setCanceledOnTouchOutside(false);
		}
		if (!isFinishing()){
			pro.show();
		}
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null && !isFinishing()) {
			pro.dismiss();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dataresult_feedback);
		
		lp= new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.rightMargin = Util.Dp2Px(this, 10);
        lp.bottomMargin = Util.Dp2Px(this, 10);
        
        reportData = (ReportDataBean) getIntent().getSerializableExtra("reportData");
        fankuiDatas = new Gson().fromJson(getIntent().getStringExtra("fankuiData"), new TypeToken<List<FankuiDataBean>>(){}.getType());
        date = getIntent().getStringExtra("date");
        findViewById(R.id.back).setOnClickListener(this);
		title = ((TextView)findViewById(R.id.title));
		if (!TextUtils.isEmpty(getIntent().getStringExtra("date"))) {
			try {
				title.setText(CalenderUtil.getStrByDate(date, "yyyy-MM-dd"));
			} catch (Exception e) {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -1);
				title.setText(new SimpleDateFormat("MM月dd日").format(calendar.getTime()));
			}
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			title.setText(new SimpleDateFormat("MM月dd日").format(calendar.getTime()));
		}
		
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setText("");
		Drawable drawable = getResources().getDrawable(R.drawable.edit_data);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		right.setCompoundDrawables(drawable, null, null, null);
		right.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
		
		initView();
	}
	
	private void initView(){
		view_bg = findViewById(R.id.view_bg);
		rlContent = (LinearLayout) findViewById(R.id.rl_content);
		rljf = (LinearLayout) findViewById(R.id.rl_jf);
		btnRecordtz = (Button) findViewById(R.id.btn_recordtz);
		btnRecordtz.setOnClickListener(this);
		btnRecordtz.setTag(0);
		tvEfficiency = (TextView) findViewById(R.id.tv_efficiency);
		tvOnbedLenght = (TextView) findViewById(R.id.tv_onbed_lenght);
		tvSleepLenght = (TextView) findViewById(R.id.tv_sleep_lenght);
		myClock1 = (MyCustomClockView) findViewById(R.id.myclock1);
		myClock2 = (MyCustomClockView) findViewById(R.id.myclock2);
		viewp = findViewById(R.id.viewp);
		btn1 = (TextView) findViewById(R.id.btn_1);
		btn1.setOnClickListener(this);
		btn2 = (TextView)findViewById(R.id.btn_2);
		btn2.setOnClickListener(this);
		btn3 = (TextView)findViewById(R.id.btn_3);
		btn3.setOnClickListener(this);
		btn4 = (TextView)findViewById(R.id.btn_4);
		btn4.setOnClickListener(this);
		btn5 = (TextView)findViewById(R.id.btn_5);
		btn5.setOnClickListener(this);
		if("1".equals(getIntent().getStringExtra("type")))
			getDatas();
		else
			initData();
	}
	
	private void getDatas(){
		showPro();
		new XiangchengMallProcClass(this).getRecordByDate(PreManager.instance().getUserId(this), date.replace("-", ""), 
				"1" , new InterfaceGetRecordByDateCallBack() {
					
					@Override
					public void onSuccess(String icode, ReportDataBean data, List<FankuiDataBean> datas) {
						cancelPro();
						reportData = data;
						fankuiDatas = datas;
						initData();
					}
					
					@Override
					public void onError(String icode, String strErrMsg) {
						cancelPro();
						Util.show(DataResultFeedbackActivity.this, strErrMsg);
					}
				});
	}
	
	private void initData(){
		findViewById(R.id.sc_view).setVisibility(View.VISIBLE);
		try {
			int xv = (int) (new BigDecimal(Double.parseDouble(reportData.getXiaolv())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() * 100);
			if(xv < 80){
				view_bg .setBackgroundResource(R.drawable.custom_bg8);
			}else{
				view_bg .setBackgroundResource(R.drawable.custom_bg4);
			}
			RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) view_bg.getLayoutParams();
			int maxhea = viewp.getLayoutParams().height - Util.Dp2Px(this, 70);
			mParams.height = (int) (maxhea * (xv/100f));
			
			if(mParams.height < 200){
				mParams.height = 200;
			}
			
			view_bg.setLayoutParams(mParams);
			tvEfficiency.setText(xv + "%");
		} catch (Exception e) {
			tvEfficiency.setText("0%");
		}
		
		
		try {
			int lenght = Integer.parseInt(reportData.getBedlong());
			myClock1.setData(lenght);
			tvOnbedLenght.setText(((lenght / 60) < 10 ? ("0" + (lenght / 60)) : (lenght / 60)) + "小时" + ((lenght % 60) < 10 ? ("0" + (lenght % 60)) : (lenght % 60) + "分"));
		} catch (Exception e) {
			tvOnbedLenght.setText("00小时00分");
		}
		
		try {
			int lenght = Integer.parseInt(reportData.getSleeplong());
			myClock2.setData(lenght);
			tvSleepLenght.setText(((lenght / 60) < 10 ? ("0" + (lenght / 60)) : (lenght / 60)) + "小时" + ((lenght % 60) < 10 ? ("0" + (lenght % 60)) : (lenght % 60) + "分"));
		} catch (Exception e) {
			tvSleepLenght.setText("00小时00分");
		}
		
		btn1.setBackgroundResource(R.drawable.feel_1);
		btn1.setText("");
		btn2.setBackgroundResource(R.drawable.feel_2);
		btn2.setText("");
		btn3.setBackgroundResource(R.drawable.feel_3);
		btn3.setText("");
		btn4.setBackgroundResource(R.drawable.feel_4);
		btn4.setText("");
		btn5.setBackgroundResource(R.drawable.feel_5);
		btn5.setText("");
		if(fankuiDatas!= null && fankuiDatas.size() >0) {
			switch (getChoiceIndex(fankuiDatas.get(0).getCheck())) {
				case 0:
					btn1.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn1.setText("很差");
					break;
				case 1:
					btn2.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn2.setText("差");
					break;
				case 2:
					btn3.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn3.setText("一般");
					break;
				case 3:
					btn4.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn4.setText("好");
					break;
				case 4:
					btn5.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn5.setText("很好");
					break;
				default:
					break;
			}
		}else{

		}
		initData1();
	}
	private void initData1(){
		rlContent.removeAllViews();
		for (int i = 1; i < (fankuiDatas == null ? 0 : fankuiDatas.size()); i++) {
			if("weight".equals(fankuiDatas.get(i).getFankui_key())){
				rljf.setVisibility(View.VISIBLE);
				btnRecordtz.setTag(fankuiDatas.get(i).getFankui_key());
				try{
					double tz = Double.parseDouble(fankuiDatas.get(i).getCheck().get(0).getChoice_title());
					if(tz >0){
						btnRecordtz.setText(tz + "kg");
						btnRecordtz.setTag(tz);
						btnRecordtz.setTextColor(getResources().getColor(R.color.white));
						btnRecordtz.setBackgroundResource(R.drawable.custom_round_select);
					}else{
						btnRecordtz.setText("+记录体重");
						btnRecordtz.setTag(0);
						btnRecordtz.setTextColor(getResources().getColor(R.color.ct_color));
						btnRecordtz.setBackgroundResource(R.drawable.custom_round_normal);
					}
				}catch(Exception e){
					btnRecordtz.setText("+记录体重");
					btnRecordtz.setTag(0);
					btnRecordtz.setTextColor(getResources().getColor(R.color.ct_color));
					btnRecordtz.setBackgroundResource(R.drawable.custom_round_normal);
				}
				
			}else{
				View viewP = getLayoutInflater().inflate(R.layout.item_dataresult_feedback, null);
				final TagStreamLayout mTagView = (TagStreamLayout) viewP.findViewById(R.id.tag_item);
				TextView rlTitle = (TextView) viewP.findViewById(R.id.rl_title);
				rlTitle.setText(fankuiDatas.get(i).getTitle());
				List<CheckBean> datas = fankuiDatas.get(i).getCheck();
				
				if("issmoke".equals(fankuiDatas.get(i).getFankui_key())){
					int index = getIndex(fankuiDatas, "smoke");
					
					if(getChoiceIndex(fankuiDatas.get(index).getCheck()) <= 0)
						viewP.setVisibility(View.GONE);
					else
						viewP.setVisibility(View.VISIBLE);
				}
				
				for(int j =0; j < datas.size(); j++){
					View view = getLayoutInflater().inflate(R.layout.item_habit_layout, null);
					view.setTag(i + ":" + j);
					TextView texteView = (TextView)view.findViewById(R.id.radiobtn);
					texteView.setText(datas.get(j).getChoice_title());
					if("1".equals(datas.get(j).getChoice_flag())){
						texteView.setTextColor(getResources().getColor(R.color.cbg_color));
						texteView.setBackgroundResource(R.drawable.custom_round_select);
					}else{
						texteView.setTextColor(getResources().getColor(R.color.ct_color));
						texteView.setBackgroundResource(R.drawable.custom_round_normal);
					}
					
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(!Util.checkNetWork(DataResultFeedbackActivity.this)){
								Util.show(DataResultFeedbackActivity.this, "网络连接不可用");
								return;
							}
							String[] indexs = v.getTag().toString().split(":");
							int pIndex = Integer.parseInt(indexs[0]);
							int index = Integer.parseInt(indexs[1]);
							List<CheckBean> datas = fankuiDatas.get(pIndex).getCheck();
							if("customers".equals(fankuiDatas.get(pIndex).getFankui_key())){
								if("1".equals(datas.get(index).getChoice_flag())){
									datas.get(index).setChoice_flag("0");
									TextView texteView = (TextView) mTagView.getChildAt(index).findViewById(R.id.radiobtn);
									texteView.setTextColor(getResources().getColor(R.color.ct_color));
									texteView.setBackgroundResource(R.drawable.custom_round_normal);
								}else{
									datas.get(index).setChoice_flag("1");
									TextView texteView = (TextView) mTagView.getChildAt(index).findViewById(R.id.radiobtn);
									texteView.setTextColor(getResources().getColor(R.color.cbg_color));
									texteView.setBackgroundResource(R.drawable.custom_round_select);
								}
								
							}else{
								for (int k = 0; k < datas.size(); k++) {
									TextView texteView = (TextView) mTagView.getChildAt(k).findViewById(R.id.radiobtn);
									texteView.setTextColor(getResources().getColor(R.color.ct_color));
									texteView.setBackgroundResource(R.drawable.custom_round_normal);
								}
								
								TextView texteView = (TextView) mTagView.getChildAt(index).findViewById(R.id.radiobtn);
								texteView.setTextColor(getResources().getColor(R.color.cbg_color));
								texteView.setBackgroundResource(R.drawable.custom_round_select);
								for (int j = 0; j < datas.size(); j++) {
									datas.get(j).setChoice_flag("0");
								}
								datas.get(index).setChoice_flag("1");
							}
							
							if("smoke".equals(fankuiDatas.get(pIndex).getFankui_key())){
								int itemIndex = getIndex(fankuiDatas, "issmoke");
								if(index > 0){
									if(itemIndex -1 >= 0)
										rlContent.getChildAt(itemIndex -1).setVisibility(View.VISIBLE);
								}else{
									if(itemIndex -1 >= 0)
										rlContent.getChildAt(itemIndex -1).setVisibility(View.GONE);
									for (int k = 1; k < datas.size(); k++) {
										datas.get(k).setChoice_flag("0");
									}
									for (int k = 0; k < fankuiDatas.get(itemIndex).getCheck().size(); k++) {
										fankuiDatas.get(itemIndex).getCheck().get(k).setChoice_flag("0");
									}
									
									initData1();
								}
							}

							submit1(fankuiDatas.get(pIndex));
						}
					});
					mTagView.addView(view, lp);
				}
				rlContent.addView(viewP);
			}
		}
		
	}
	
	private int getChoiceIndex(List<CheckBean> datas){
		for (int i = 0; i < datas.size(); i++) {
			if("1".equals(datas.get(i).getChoice_flag()))
				return i;
		}
		return -1;
	}
	
	private int getIndex(List<FankuiDataBean> datas, String key){
		for (int i = 0; i < datas.size(); i++) {
			if(key.equals(datas.get(i).getFankui_key()))
				return i;
		}
		return -1;
	}
	
	private void submit1(FankuiDataBean data){
		StringBuffer strs = new StringBuffer();
		for (int i = 0; i < data.getCheck().size(); i++) {
			if("1".equals(data.getCheck().get(i).getChoice_flag()))
				strs.append(answers[i]).append(",");
		}
		String an = strs.toString();
		if("customers".equals(data.getFankui_key()))
			submit(data.getFankui_key(), an.length() < 1 ? "无" : an.substring(0, an.length() - 1));
		else{
			if(an.length() > 0)
				submit(data.getFankui_key(), an.substring(0, an.length() - 1));
		}
	}
	
	/**
	 * 提交
	 */
	private void submit(String key, String data){
		RequestParams params = new RequestParams();
		params.put("my_int_id", PreManager.instance().getUserId(this));
		params.put("date", date.replace("-", ""));
		params.put("type", "1");
		params.put(key, data);
		if("smoke".equals(key) && "A".equals(answers)){
			params.put("issmoke", "");
		}
		if("issmoke".equals(key)){
			try {
				for (int i = 0; i < fankuiDatas.get(getIndex(fankuiDatas, "smoke")).getCheck().size(); i++) {
					if("1".equals(fankuiDatas.get(getIndex(fankuiDatas, "smoke")).getCheck().get(i).getChoice_flag())){
						params.put("smoke", answers[i]);
						break;
					}
				}
			} catch (Exception e) {
			}
		}
	
		new XiangchengMallProcClass(this).addRecordFeedback(params, new InterfaceAddRecordFeedbackCallBack() {
			
			@Override
			public void onSuccess(String icode, ReportDataBean reportData,  List<FankuiDataBean> fankuiData) {
				Util.show(DataResultFeedbackActivity.this, "记录成功");
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				Util.show(DataResultFeedbackActivity.this, strErrMsg);
			}
		});
	}
	
	private void custom(int position, TextView view, String text, String answer){
		if(fankuiDatas != null && fankuiDatas.size() >0) {
			btn1.setBackgroundResource(R.drawable.feel_1);
			btn1.setText("");
			btn2.setBackgroundResource(R.drawable.feel_2);
			btn2.setText("");
			btn3.setBackgroundResource(R.drawable.feel_3);
			btn3.setText("");
			btn4.setBackgroundResource(R.drawable.feel_4);
			btn4.setText("");
			btn5.setBackgroundResource(R.drawable.feel_5);
			btn5.setText("");
			for (int j = 0; j < fankuiDatas.get(0).getCheck().size(); j++) {
				fankuiDatas.get(0).getCheck().get(j).setChoice_flag("0");
			}
			view.setBackgroundResource(R.drawable.custom_bg2_ff);
			view.setText(text);
			fankuiDatas.get(0).getCheck().get(position).setChoice_flag("1");
			submit1(fankuiDatas.get(0));
		}
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent();
			intent.setAction(Constant.MODEFY_SLEEP_FEEL_ACTION);
			sendBroadcast(intent);
			
			AppManager.getAppManager().finishActivity();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:

				Intent intent = new Intent();
				intent.setAction(Constant.MODEFY_SLEEP_FEEL_ACTION);
				sendBroadcast(intent);
				
				AppManager.getAppManager().finishActivity();
				break;
			case R.id.btn_title_right:
				startActivity(new Intent(this, RecordSleepDataActivity.class)
				.putExtra("date", getIntent().getStringExtra("date"))
				.putExtra("data", reportData));
				
				break;
			case R.id.btn_recordtz:
				CustomInputDialog inputDialog = new CustomInputDialog(this, new CustomInputDialog.InputCallBack() {
					
					@Override
					public void setResault(String s) {
						try {
							double tem1 = Double.parseDouble(s.toString());
							if(tem1 > 0) {
								btnRecordtz.setTag(tem1);
								btnRecordtz.setText(tem1 + "kg");
								btnRecordtz.setTextColor(getResources().getColor(R.color.white));
								btnRecordtz.setBackgroundResource(R.drawable.custom_round_select);
								submit("weight", tem1 + "");
							}else
								Util.show(DataResultFeedbackActivity.this, "体重必须大于0");
						}catch(Exception e){
						}
					}
				});
				InputClass inputClass = new InputClass();
				inputClass.title ="体重";
				inputClass.tag = "kg";
				try {
					inputClass.current = btnRecordtz.getTag().toString();
				} catch (Exception e) {
					inputClass.current ="";
				}
				
				inputDialog.setData(inputClass);
				inputDialog.show();
				break;
			case R.id.btn_1:
				custom(0, btn1, "很差", "A");
				break;
			case R.id.btn_2:
				custom(1, btn2, "差", "B");
				break;
			case R.id.btn_3:
				custom(2, btn3, "一般", "C");
				break;
			case R.id.btn_4:
				custom(3, btn4, "好", "D");
				break;
			case R.id.btn_5:
				custom(4, btn5, "很好", "E");
				break;
			default:
				break;
			}
	}

}
