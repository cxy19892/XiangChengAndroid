package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.BaseFragmentPagerAdapter;
import com.yzm.sleep.bean.CheckBean;
import com.yzm.sleep.bean.FankuiDataBean;
import com.yzm.sleep.fragment.FragmentPageRecordFeel;
import com.yzm.sleep.fragment.FragmentPageRecordFeel.InterSubmit;

/**
 * 补充非量化数据
 */
public class RecordFeelDataActivity extends BaseActivity implements InterSubmit{

	private TextView title;
	private Button right;
	private ViewPager mViewPager;
	private List<Fragment> fragments;
	private BaseFragmentPagerAdapter mAdapter;
	private int smokeIndex = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_feeldata);
		findViewById(R.id.rl_title).setBackgroundColor(getResources().getColor(R.color.white));
		fragments = new ArrayList<Fragment>();
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		right = (Button) findViewById(R.id.btn_title_right);
		title.setText("数据补充");
		right.setText("");
		Drawable drawable = getResources().getDrawable(R.drawable.ic_next_step_w);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		right.setCompoundDrawables(drawable, null, null, null);
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		mViewPager = (ViewPager) findViewById(R.id.my_viewpager);
		mViewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		int feel_flag = getIntent().getIntExtra("feel_flag", 0);
		int env_flag = getIntent().getIntExtra("env_flag", 0);
		int smoke_flag = getIntent().getIntExtra("smoke_flag", 0);
		int win_flag = getIntent().getIntExtra("win_flag", 0);
		int coffo_flag = getIntent().getIntExtra("coffo_flag", 0);
		int sport_flag = getIntent().getIntExtra("sport_flag", 0);
		int weight_flag = getIntent().getIntExtra("weight_flag", 0);
		
		 if(feel_flag == 1){
				FragmentPageRecordFeel fragment= new  FragmentPageRecordFeel();
				fragment.setMyTag("feeling");
				Bundle bundle = new Bundle();
				FankuiDataBean bean = new FankuiDataBean();
				bean.setTitle("本周期感觉自己睡得怎么样？");
				List<CheckBean> list = new ArrayList<CheckBean>();
				CheckBean checkBean = new CheckBean();
				checkBean.setChoice_title("A");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("B");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("C");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("D");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("E");
				list.add(checkBean);
				
				bean.setCheck(list);
				bundle.putSerializable("data", bean);
				fragment.setArguments(bundle);
				fragment.setMInterSubmit(this);
				fragments.add(fragment);
			}
			
			if(env_flag == 1){
				FragmentPageRecordFeel fragment= new  FragmentPageRecordFeel();
				fragment.setMyTag("customers");
				Bundle bundle = new Bundle();
				FankuiDataBean bean = new FankuiDataBean();
				bean.setTitle("本周期哪些哪些因素影响了您的睡眠？");
				List<CheckBean> list = new ArrayList<CheckBean>();
				CheckBean checkBean = new CheckBean();
				checkBean.setChoice_title("室内温度");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("床（枕头）");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("噪音");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("光线");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("身体疾病");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("家人");
				list.add(checkBean);
				
				bean.setCheck(list);
				bundle.putSerializable("data", bean);
				fragment.setArguments(bundle);
				fragment.setMInterSubmit(this);
				fragments.add(fragment);
			}
			
			if(smoke_flag == 1){
				FragmentPageRecordFeel fragment= new  FragmentPageRecordFeel();
				fragment.setMyTag("smoke");
				Bundle bundle = new Bundle();
				FankuiDataBean bean = new FankuiDataBean();
				bean.setTitle("本周期平均每天吸了多少只烟？");
				List<CheckBean> list = new ArrayList<CheckBean>();
				CheckBean checkBean = new CheckBean();
				checkBean.setChoice_title("没吸");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("1~10支");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("11~20支");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("21~40支");
				list.add(checkBean);
				
				bean.setCheck(list);
				bundle.putSerializable("data", bean);
				fragment.setArguments(bundle);
				fragment.setMInterSubmit(this);
				fragments.add(fragment);

				
				FragmentPageRecordFeel fragment1= new  FragmentPageRecordFeel();
				fragment1.setMyTag("issmoke");
				Bundle bundle1 = new Bundle();
				FankuiDataBean bean1 = new FankuiDataBean();
				bean1.setTitle("本周期睡前1小时内吸烟的次数？");
				List<CheckBean> list1 = new ArrayList<CheckBean>();
				CheckBean checkBean1 = new CheckBean();
				checkBean1.setChoice_title("没抽");
				list1.add(checkBean1);
				checkBean1 = new CheckBean();
				checkBean1.setChoice_title("抽");
				list1.add(checkBean1);
				
				bean1.setCheck(list1);
				bundle1.putSerializable("data", bean1);
				fragment1.setArguments(bundle1);
				fragment1.setMInterSubmit(this);
				fragments.add(fragment1);
			}
			
			if(win_flag == 1){
				FragmentPageRecordFeel fragment= new  FragmentPageRecordFeel();
				fragment.setMyTag("wine");
				Bundle bundle = new Bundle();
				FankuiDataBean bean = new FankuiDataBean();
				bean.setTitle("本周期睡前4小时内喝酒的次数？");
				List<CheckBean> list = new ArrayList<CheckBean>();
				CheckBean checkBean = new CheckBean();
				checkBean.setChoice_title("没喝");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("喝过");
				list.add(checkBean);
				
				bean.setCheck(list);
				bundle.putSerializable("data", bean);
				fragment.setMInterSubmit(this);
				fragment.setArguments(bundle);
				fragments.add(fragment);
			}

			if(coffo_flag == 1){
				FragmentPageRecordFeel fragment= new  FragmentPageRecordFeel();
				fragment.setMyTag("coffo");
				Bundle bundle = new Bundle();
				FankuiDataBean bean = new FankuiDataBean();
				bean.setTitle("本周期睡前6小时内喝咖啡的次数？");
				List<CheckBean> list = new ArrayList<CheckBean>();
				CheckBean checkBean = new CheckBean();
				checkBean.setChoice_title("没喝");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("喝过");
				list.add(checkBean);
				
				bean.setCheck(list);
				bundle.putSerializable("data", bean);
				fragment.setArguments(bundle);
				fragment.setMInterSubmit(this);
				fragments.add(fragment);
			}
			
			if(sport_flag == 1){
				FragmentPageRecordFeel fragment= new  FragmentPageRecordFeel();
				fragment.setMyTag("sport");
				Bundle bundle = new Bundle();
				FankuiDataBean bean = new FankuiDataBean();
				bean.setTitle("本周期平均每天运动了多少分钟？");
				List<CheckBean> list = new ArrayList<CheckBean>();
				CheckBean checkBean = new CheckBean();
				checkBean.setChoice_title("没运动");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("15分钟");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("30分钟");
				list.add(checkBean);
				checkBean = new CheckBean();
				checkBean.setChoice_title("30分钟以上");
				list.add(checkBean);
				
				bean.setCheck(list);
				bundle.putSerializable("data", bean);
				fragment.setArguments(bundle);
				fragment.setMInterSubmit(this);
				fragments.add(fragment);
			}
			
			if(weight_flag == 1){
				FragmentPageRecordFeel fragment= new  FragmentPageRecordFeel();
				fragment.setMyTag("weight");
				Bundle bundle = new Bundle();
				FankuiDataBean bean = new FankuiDataBean();
				bean.setTitle("本周期体重？");
				bundle.putSerializable("data", bean);
				fragment.setArguments(bundle);
				fragment.setMInterSubmit(this);
				fragments.add(fragment);
			}
			
		mAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			if(mViewPager.getCurrentItem() > 0){
				if(smokeIndex == 0){
					int currentIndex = mViewPager.getCurrentItem();
					int issmokeIndex = getFragmentIndex("issmoke");
					int cuIndex = getFragmentIndex(((FragmentPageRecordFeel)fragments.get(currentIndex)).getMyTag());
					if(cuIndex - issmokeIndex > 1){
						mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
					}else{
						mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 2, true);
					}
				}else
					mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
			}else{
				AppManager.getAppManager().finishActivity();
			}
			break;
		case R.id.btn_title_right:
			FragmentPageRecordFeel fragment = ((FragmentPageRecordFeel)fragments.get(mViewPager.getCurrentItem()));
			if("issmoke".equals(fragment.getMyTag())){
				fragment.setValur(ansStr);
			}
			fragment.submit1();
			break;
		default:
			break;
		}
	}

	private String ansStr;
	
	@Override
	public void submitFinish(String key, FankuiDataBean datas, String ans) {
		int currentIndex = mViewPager.getCurrentItem();
		if(mViewPager.getCurrentItem() < fragments.size() -1){
			if("smoke".equals(key)){
				if((smokeIndex = getChoiceIndex(datas.getCheck())) == 0 ){
					ansStr = "";
					mViewPager.setCurrentItem(currentIndex + 2, true);
					int issmokeIndex = getFragmentIndex("issmoke");
					try{
						List<CheckBean> data =((FragmentPageRecordFeel)fragments.get(issmokeIndex)).getData().getCheck();
						int size =data == null ? 0  : data.size();
						for (int i = 0; i < size; i++) {
							data.get(i).setChoice_flag("0");
						}
					}catch(Exception e){}
					
				}else{
					ansStr = ans;
					mViewPager.setCurrentItem(currentIndex + 1, true);
				}
			}else{
				ansStr = "";
				mViewPager.setCurrentItem(currentIndex + 1, true);
			}
			
		}else{
			Intent mIntent = new Intent();
			mIntent.setAction(Constant.WEEK_FEED_BACK_SUC);
			sendBroadcast(mIntent);
			startActivity(new Intent(this, SleepDataReportActivity.class));
			AppManager.getAppManager().finishActivity();
		}
	}

	
	private int getChoiceIndex(List<CheckBean> datas){
		for (int i = 0; i < datas.size(); i++) {
			if("1".equals(datas.get(i).getChoice_flag()))
				return i;
		}
		return -1;
	}
	
	private int getFragmentIndex(String tag){
		for (int i = 0; i < fragments.size(); i++) {
			if(tag.equals(((FragmentPageRecordFeel)fragments.get(i)).getMyTag())){
				return i; 
			}
		}
		return -1;
	}
	
}
