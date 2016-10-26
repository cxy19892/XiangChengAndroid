package com.yzm.sleep.activity.pillow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.bluetoothBLE.PillowBean;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.utils.CommunityProcClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.HardwareBoundParamClass;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceGetHardwareBoundDaysCallBack;
import com.yzm.sleep.utils.InterFaceUtilsClass.InterfaceHardwareBoundCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.widget.SimpleRoundProgressBar;

public class BundPillowInfoActivity extends BaseActivity {

	private ListView mListView;
	private RelativeLayout tip2;
	private SimpleRoundProgressBar bar;
	private TextView tvBarProgress;
	private ImageView iconBar;
	private PillowBean mypillow = null;
	private TextView power_prompt;
	private TextView Title;
	private Button titleLeft, titleRight;
	private MyAlertDialog myAlertDialog;
	private boolean isfromBund;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bund_pillow_info);
		isfromBund = getIntent().getBooleanExtra("frombund", false);
		initViews();
		getUsingTime();
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("PairSP_Succeed_AdditionalInfo"); 
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PairSP_Succeed_AdditionalInfo");
		MobclickAgent.onPause(this);
	}

	private void initViews() {
		Title = (TextView) findViewById(R.id.title);
		titleLeft   = (Button) findViewById(R.id.back);
		titleRight  = (Button) findViewById(R.id.btn_title_right);
		Title.setText("智能枕扣信息");
		titleRight.setVisibility(View.VISIBLE);
		titleRight.setText("更多");
		titleRight.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		titleLeft.setOnClickListener(this);
		titleRight.setOnClickListener(this);
		
		RelativeLayout relaAbove = (RelativeLayout) findViewById(R.id.bind_pillow_above_rl);
		relaAbove
				.setLayoutParams(new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT,
						getScreenWidth()));

		mListView = (ListView) findViewById(R.id.bind_pillow_listview);
		tip2 = (RelativeLayout) findViewById(R.id.bind_pillow_power_tip2);
		bar = (SimpleRoundProgressBar) findViewById(R.id.bind_pillow_power_progress);
		tvBarProgress = (TextView) findViewById(R.id.bind_pillow_power_tv);
		iconBar = (ImageView) findViewById(R.id.bind_pillow_power_icon);
		power_prompt = (TextView) findViewById(R.id.bind_pillow_power_prompt);
		//上传绑定信息
		if(checkNetWork(this)){
			uploadBundInfo();
			broadcastBundSuc();
		}else{
			String info = PreManager.instance().getBundbluetoothPillow(BundPillowInfoActivity.this)+";"+((int)(System.currentTimeMillis()/1000))+";"+PreManager.instance().getBluetoothDevSensitive(BundPillowInfoActivity.this)+";"+PreManager.instance().getUserId(BundPillowInfoActivity.this);
			PreManager.instance().setBluetoothBundInfo(BundPillowInfoActivity.this, info);
		}
		getpillowinfo(PreManager.instance().getUserId(this), this);
		deviceIsBinded(mypillow);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_right:
			showPopupWindow();
			break;
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		default:
			break;
		}
	}
	
	
	private void getpillowinfo(String user_id, Context mContext) {
		mypillow = new PillowBean();
		mypillow.setHardVersion(PreManager.instance().getBluetoothPillowFirmwareVersion(mContext));
		mypillow.setPillowMac(PreManager.instance().getBundbluetoothPillow(mContext));
		mypillow.setPowerValue(PreManager.instance().getBluetoothPillowBatteryValue(mContext));
		mypillow.setUserId(user_id);
	}
	
	/**
	 * 设备绑定的界面菜单显示
	 */
	private void deviceIsBinded(PillowBean pillow){
		tip2.setVisibility(View.VISIBLE);
		power_prompt.setVisibility(View.VISIBLE);
		if(null != pillow && pillow.getPowerValue() !=null && !pillow.getPowerValue().equals("null")){
			initprogressBar((int) Double.parseDouble(pillow.getPowerValue()));
		}else{
			initprogressBar(50);
		}
//		initprogressBar(Integer.parseInt(pillow.getPowerValue()));
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String[] tags = getResources().getStringArray(R.array.bind_pillow_new);
		for (int i = 0; i < tags.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tag", tags[i]);
			if(i == 0)
				if(null != pillow && pillow.getPillowMac()!=null){
					map.put("value", pillow.getPillowMac());//"CX0079527"
				}else{
					map.put("value", "CX0079527");
				}
			if(i == 2 || i == 3)
				map.put("icon", R.drawable.ic_right1);
			if(i == 1)
				if(null != pillow){
					map.put("value", pillow.getHardVersion().split("_").length>=1?pillow.getHardVersion().split("_")[0]:pillow.getHardVersion());
				}else{
					map.put("value", "1.0.1");
				}
			data.add(map); 
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.bind_pillow_menu_item, new String[] { "tag", "icon", "value"},
				new int[] { R.id.bind_pillow_menu_key,
						R.id.bind_pillow_menu_icon , R.id.bind_pillow_menu_value});
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				switch (arg2) {
				case 0://设备编号
					
					break;
				case 1://固件版本
					
					
					break;
				case 2://枕头灵敏度
					Intent intentTosetsensitiveIntent = new Intent(BundPillowInfoActivity.this, SensitiveSetActivity.class);
					intentTosetsensitiveIntent.putExtra("isbund", true);
					startActivity(intentTosetsensitiveIntent);
					break;
				case 3://常见问题
					Intent sucIntent = new Intent(BundPillowInfoActivity.this, BundSucActivity.class);
					sucIntent.putExtra("frombund", false);
					startActivity(sucIntent);
					
					break;
				default:
					break;
				}
				
			}
		});
	}
	
	/**
	 * 生成枕头电量图
	 * @param progress
	 */
	private void initprogressBar(int progress){
		if(progress<=0 || progress>100){
			progress = 100;
		}
		bar.setRoundColor(getResources().getColor(R.color.fct_color_7f));
		bar.setRoundProgressColor(getResources().getColor(R.color.theme_color));
		bar.setMax(100);
		bar.setRoundWidth(12);
		bar.setProgressf((float)progress/100);
		
//		bar.setRoundColor(0xffd3d3d3);
//		bar.setTextSize(40f);
//		bar.setTextColor(0xff666666);
//		if(progress >= 80){
//			bar.setRoundProgressColor(0xff6dbc70);
//			iconBar.setImageResource(R.drawable.cell_green_6dbc70);
//		}else if(progress < 80 && progress >= 50){
//			bar.setRoundProgressColor(0xfffad25b);
//			iconBar.setImageResource(R.drawable.cell_yellow_fad25b);
//		}else if(progress < 50 && progress >= 30){
//			bar.setRoundProgressColor(0xfffcaa30);
//			iconBar.setImageResource(R.drawable.cell_orange_fcaa30);
//		}else{
//			bar.setRoundProgressColor(0xfff3665b);
//			iconBar.setImageResource(R.drawable.cell_red_f3665b);
//		}
		tvBarProgress.setText(progress + "%");
		
	}
	
	private void showPopupWindow() {
		if (null == myAlertDialog) {
			myAlertDialog = new MyAlertDialog(BundPillowInfoActivity.this,
					R.style.bottom_animation);
		}
		myAlertDialog.show();
		myAlertDialog.setTV1("解除绑定后,睡眠数据将丢失,是否继续？");
		myAlertDialog.setTV2("解除绑定", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {
				//取消绑定
//				unbundPillow();
				if(!checkNetWork(BundPillowInfoActivity.this)){
					unbundPillow();
				}else{
					String info = PreManager.instance().getBundbluetoothPillow(BundPillowInfoActivity.this)+";"+((int)(System.currentTimeMillis()/1000))+";"+PreManager.instance().getBluetoothDevSensitive(BundPillowInfoActivity.this)+";"+PreManager.instance().getUserId(BundPillowInfoActivity.this);
					PreManager.instance().setBluetoothUnBundInfo(BundPillowInfoActivity.this, info);
					uploadUnBundInfo();
				}
				myAlertDialog.dismiss();
			}
		}, View.VISIBLE);
		myAlertDialog.setTV3("", null, View.GONE);
		myAlertDialog.setTV4("", null, View.GONE);
		myAlertDialog.setTVbottom("取消", new MyOnClickListener() {

			@Override
			public void Onclick(View v) {

				myAlertDialog.dismiss();
			}

		}, View.VISIBLE);
	}
	
	private void unbundPillow(){
		PreManager.instance().setBundbluetoothPillow(this, "");
		PreManager.instance().setBluetoothPillowBatteryValue(this, "");
		PreManager.instance().setBluetoothPillowFirmwareVersion(this, "");
//		PreManager.instance().setBluetoothDevSensitive(this,"");
		Toast toast = Toast
				.makeText(
						this,
						"解绑成功",
						Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		broadcastUNBundSuc();
		
		AppManager.getAppManager().finishActivity(BundPillowInfoActivity.class);
		if(!isfromBund)
			AppManager.getAppManager().finishActivity(PillowDayDataActivity.class);
	}
	
	private void getUsingTime(){
		new CommunityProcClass(this).getHardwareBoundDays(PreManager.instance().getUserId(this),PreManager.instance().getBundbluetoothPillow(this), new InterfaceGetHardwareBoundDaysCallBack() {
			
			@Override
			public void onSuccess(int icode, String boundDays) {
				if(!BundPillowInfoActivity.this.isFinishing()){
					power_prompt.setText("已经使用"+boundDays+"天");
					try {
						initprogressBar(100-(int)(((float)Integer.parseInt(boundDays)/500)*100));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				
				
			}
		});
	}
	
	/**
	 * 上传解除绑定硬件信息
	 */
	private void uploadUnBundInfo(){
		HardwareBoundParamClass mHardwareBoundParamClass = new HardwareBoundParamClass();
		mHardwareBoundParamClass.bdtime = "";
		mHardwareBoundParamClass.jbtime =""+((int)(System.currentTimeMillis()/1000));
		mHardwareBoundParamClass.jystatus = "2";
		mHardwareBoundParamClass.my_int_id = PreManager.instance().getUserId(this);
		mHardwareBoundParamClass.yjlmd = PreManager.instance().getBluetoothDevSensitive(this);
		mHardwareBoundParamClass.macadd= PreManager.instance().getBundbluetoothPillow(this);
		new CommunityProcClass(this).hardwareBound(mHardwareBoundParamClass, new InterfaceHardwareBoundCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				toastMsg(strSuccMsg);
				PreManager.instance().setBluetoothUnBundInfo(BundPillowInfoActivity.this, "");
				unbundPillow();
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				String info = PreManager.instance().getBundbluetoothPillow(BundPillowInfoActivity.this)+";"+((int)(System.currentTimeMillis()/1000))+";"+PreManager.instance().getBluetoothDevSensitive(BundPillowInfoActivity.this)+";"+PreManager.instance().getUserId(BundPillowInfoActivity.this);
				PreManager.instance().setBluetoothUnBundInfo(BundPillowInfoActivity.this, info);
				unbundPillow();
			}
		});
	}
	/**
	 * 发送解除绑定的广播
	 */
	private void broadcastUNBundSuc() {
		 Intent intent = new Intent(Constant.BUND_BLUETOOTH_CHANGE);
	     intent.putExtra("isbund", false);//绑定成功发送 true,解绑成功发送false
	     sendBroadcast(intent);
    }
	
	/**
	 * 上传绑定硬件信息
	 */
	private void uploadBundInfo(){
		HardwareBoundParamClass mHardwareBoundParamClass = new HardwareBoundParamClass();
		mHardwareBoundParamClass.bdtime = ""+((int)(System.currentTimeMillis()/1000));
		mHardwareBoundParamClass.jbtime ="";
		mHardwareBoundParamClass.jystatus = "1";
		mHardwareBoundParamClass.my_int_id = PreManager.instance().getUserId(this);
		mHardwareBoundParamClass.yjlmd = PreManager.instance().getBluetoothDevSensitive(this);
		mHardwareBoundParamClass.macadd = PreManager.instance().getBundbluetoothPillow(this);
		new CommunityProcClass(this).hardwareBound(mHardwareBoundParamClass, new InterfaceHardwareBoundCallBack() {
			
			@Override
			public void onSuccess(int icode, String strSuccMsg) {
				PreManager.instance().setBluetoothBundInfo(BundPillowInfoActivity.this, "");
			}
			@Override
			public void onError(int icode, String strErrMsg) {
//				toastMsg(strErrMsg);
				String info = PreManager.instance().getBundbluetoothPillow(BundPillowInfoActivity.this)+";"+((int)(System.currentTimeMillis()/1000))+";"+PreManager.instance().getBluetoothDevSensitive(BundPillowInfoActivity.this)+";"+PreManager.instance().getUserId(BundPillowInfoActivity.this);
				PreManager.instance().setBluetoothBundInfo(BundPillowInfoActivity.this, info);
			}
		});
	}
	
	private void broadcastBundSuc() {
        Intent intent = new Intent(Constant.BUND_BLUETOOTH_CHANGE);
        intent.putExtra("isbund", true);
        sendBroadcast(intent);
    }
	
}
