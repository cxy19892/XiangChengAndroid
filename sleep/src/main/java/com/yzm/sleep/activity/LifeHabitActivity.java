package com.yzm.sleep.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.HabitGridAdapter.InterfaceOnClick;
import com.yzm.sleep.bean.LifeHabitBean;
import com.yzm.sleep.bean.LifeHabitBean.CustomerItem;
import com.yzm.sleep.utils.InterfaceMallUtillClass.HabitChoiceParams;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceHabitChoiceCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepCustomsCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.TagStreamLayout;

/**
 * 睡前自查
 * 
 * @author
 */
public class LifeHabitActivity extends BaseActivity implements InterfaceOnClick{
	
	private int requestCode = 101;
	private List<LifeHabitBean> listData;
	private LinearLayout rlTitle, rlSu;
	private TextView  tvQtitle, tvZjOpinion;
	private View noNet;
	private TagStreamLayout mTagView;
//	private HabitGridAdapter mAdapter;
	private HorizontalScrollView hsvView;
	private int viewWidth;
	private int currentPo, currentChouesPo;
	private Button btnSure;
	private ProgressUtils pro;
	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(this);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lifehabit);
		viewWidth = getScreenWidth()/4 - Util.Dp2Px(this, 24);
		initView();
	}
	
	private void initView(){
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("睡前自查");
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setVisibility(View.VISIBLE);
		right.setText("重评");
		right.setOnClickListener(this);
		rlTitle = (LinearLayout) findViewById(R.id.rl_title);
		rlSu = (LinearLayout) findViewById(R.id.rl_su);
		hsvView = (HorizontalScrollView) findViewById(R.id.hsv_view);
		btnSure = (Button) findViewById(R.id.btn_login);
		btnSure.setOnClickListener(this);
		tvQtitle = (TextView) findViewById(R.id.tv_qtitle);
		tvZjOpinion = (TextView) findViewById(R.id.tv_suggest);
		mTagView = (TagStreamLayout) findViewById(R.id.gridview);
		noNet = findViewById(R.id.no_net);
		((TextView)noNet.findViewById(R.id.text)).setText("请检查网络设置");
		getHabitDatas();
	}

	private void getHabitDatas(){
		if(!Util.checkNetWork(this)){
			noNet.setVisibility(View.VISIBLE);
			return;
		}
		showPro();
		new XiangchengMallProcClass(this).sleepCustoms(PreManager.instance().getUserId(this), new InterfaceSleepCustomsCallBack() {
			
			@Override
			public void onSuccess(String icode, List<LifeHabitBean> datas) {
				cancelPro();
				doHabitCallBackData(datas);
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				cancelPro();
				Util.show(LifeHabitActivity.this, strErrMsg);
			}
		});
	}
	
	private void doHabitCallBackData(List<LifeHabitBean> datas){
		listData = datas;
		rlTitle.removeAllViews();
		LinearLayout.LayoutParams mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mParams.width = viewWidth;
		mParams.height = viewWidth;
		LinearLayout.LayoutParams viewParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		viewParam.width = getScreenWidth()/4;
	  
		for (int i = 0; i < listData.size(); i++) {
			if(isSelect(listData.get(i).getChoice()))
				listData.get(i).setIsEdit("1");
			
			View view = getLayoutInflater().inflate(R.layout.item_life_top, null);
			view.setTag(i);
			ImageView icon = (ImageView) view.findViewById(R.id.icon);
			icon.setLayoutParams(mParams);
			TextView name = (TextView) view.findViewById(R.id.name);
			setImage(icon, i, i==0);
			name.setText(listData.get(i).getTitle());
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					currentPo = Integer.parseInt(String.valueOf(v.getTag()));
					if(currentPo >= listData.size()/2){
						hsvView.smoothScrollBy(getScreenWidth()/4, 0);
					}else{
						hsvView.smoothScrollBy(-getScreenWidth()/4, 0);
					}
					for(int i =0; i < listData.size(); i++){
						rlTitle.getChildAt(i).findViewById(R.id.iv_posi).setVisibility(View.GONE);
						((TextView)rlTitle.getChildAt(i).findViewById(R.id.name)).setTextColor(getResources().getColor(R.color.fct_color));
						setImage((ImageView) rlTitle.getChildAt(i).findViewById(R.id.icon), i, false);
					}
					((TextView)rlTitle.getChildAt(currentPo).findViewById(R.id.name)).setTextColor(getResources().getColor(R.color.cbg_color));
					rlTitle.getChildAt(currentPo).findViewById(R.id.iv_posi).setVisibility(View.VISIBLE);
					setImage((ImageView) rlTitle.getChildAt(currentPo).findViewById(R.id.icon), currentPo, true);
					changData();
				}
			});
			
			view.setLayoutParams(viewParam);
			rlTitle.addView(view);
		}
		hsvView.setVisibility(View.VISIBLE);
		((TextView)rlTitle.getChildAt(0).findViewById(R.id.name)).setTextColor(0xffe0e0f6);
		rlTitle.getChildAt(0).findViewById(R.id.iv_posi).setVisibility(View.VISIBLE);
		changData();
	}
	
	private void setImage(ImageView image, int position, boolean select){
		List<CustomerItem> datas = listData.get(position).getChoice();
		if(datas != null && datas.size() >0){
			CustomerItem bean = datas.get(0);
			if("1".equals(bean.getType()))
				image.setImageResource(select ? R.drawable.ic_yan_selected : R.drawable.ic_yan_normal);
			if("2".equals(bean.getType()))
				image.setImageResource(select ? R.drawable.ic_jiu_selected : R.drawable.ic_jiu_normal);
			if("3".equals(bean.getType()))
				image.setImageResource(select ? R.drawable.ic_coffe_selected : R.drawable.ic_coffe_normal);
			if("4".equals(bean.getType()))
				image.setImageResource(select ? R.drawable.ic_pb_selected : R.drawable.ic_pb_normal);
			if("5".equals(bean.getType()))
				image.setImageResource(select ? R.drawable.ic_jianfei_selected : R.drawable.ic_jianfei_normal);
		}
	}
	
	private boolean isSelect(List<CustomerItem> choice){
		for (int i = 0; i < choice.size(); i++) {
			if("1".equals(choice.get(i).getFlag()))
				return true;
		}
		return false;
	}
	
	private void changData(){
//		mAdapter.setDatas(listData.get(currentPo).getChoice());
		StringBuffer buffer = new StringBuffer();
		List<CustomerItem> datas = listData.get(currentPo).getChoice();
		rlSu.setVisibility(View.GONE);
		tvZjOpinion.setVisibility(View.GONE);
		btnSure.setVisibility(View.GONE);
		MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.rightMargin = Util.Dp2Px(this, 10);
        lp.bottomMargin = Util.Dp2Px(this, 10);
		mTagView.removeAllViews();
		for(int i =0; i< datas.size(); i++){
			CustomerItem bean = listData.get(currentPo).getChoice().get(i);
			View view = getLayoutInflater().inflate(R.layout.item_habit_layout, null);
			view.setTag(i);
			TextView texteView = (TextView)view.findViewById(R.id.radiobtn);
			texteView.setText(bean.getTitle());
			if("1".equals(bean.getFlag())){
				texteView.setTextColor(getResources().getColor(R.color.cbg_color));
				texteView.setBackgroundResource(R.drawable.custom_round_select);
				List<String> su = bean.getSuggest();
				for (int j = 0; j < su.size(); j++) {
					buffer.append(su.get(j));
					if(j < su.size()-1)
						buffer.append("\n\n");
				}
				rlSu.setVisibility(View.VISIBLE);
				tvZjOpinion.setVisibility(View.VISIBLE);
				tvZjOpinion.setText(buffer.toString());
				if(!"1".equals(listData.get(currentPo).getIsEdit()))
					btnSure.setVisibility(View.VISIBLE);
			}else{
				texteView.setTextColor(getResources().getColor(R.color.fct_color));
				texteView.setBackgroundResource(R.drawable.custom_round_normal);
			}
			
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if("1".equals(listData.get(currentPo).getIsEdit())){
						String type = listData.get(currentPo).getChoice().get(0).getType();
						Util.show(LifeHabitActivity.this, "5".equals(type) ? "本周已记录了，下次再来吧！" : "今天记录了，明天再来吧！");
						return;
					}
					
					currentChouesPo = Integer.parseInt(v.getTag().toString());
					for (int i = 0; i < listData.get(currentPo).getChoice().size(); i++) {
						listData.get(currentPo).getChoice().get(i).setFlag("0");
					}
					
					listData.get(currentPo).getChoice().get(currentChouesPo).setFlag("1");
					changData();
				}
			});
			mTagView.addView(view, lp);
		}
		
		if("1".equals(listData.get(currentPo).getChoice().get(0).getType()))
			tvQtitle.setText("今天你抽了几支烟？");
		if("2".equals(listData.get(currentPo).getChoice().get(0).getType()))
			tvQtitle.setText("今天你喝了几杯酒？");
		if("3".equals(listData.get(currentPo).getChoice().get(0).getType()))
			tvQtitle.setText("今天你喝了几杯咖啡？");
		if("4".equals(listData.get(currentPo).getChoice().get(0).getType()))
			tvQtitle.setText("今天你运动了多长时间？");
		if("5".equals(listData.get(currentPo).getChoice().get(0).getType()))
			tvQtitle.setText("本周你已减重多少斤？");
			
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:
				AppManager.getAppManager().finishActivity();
				break;
			case R.id.btn_title_right:
				startActivityForResult(new Intent(this, EstimateWebActivity.class).putExtra("type", "0"), requestCode);
				break;
			case R.id.btn_login:
				if("1".equals(listData.get(currentPo).getIsEdit())){
					String type = listData.get(currentPo).getChoice().get(0).getType();
					Util.show(this, "5".equals(type) ? "本周已记录了，下次再来吧！" : "今天记录了，明天再来吧！");
					return;
				}
				HabitChoiceParams mParams = new HabitChoiceParams();
				mParams.choice_id = listData.get(currentPo).getChoice().get(currentChouesPo).getChoice_id();
				mParams.my_int_id = PreManager.instance().getUserId(this);
				mParams.type = listData.get(currentPo).getChoice().get(currentChouesPo).getType();
				mParams.flag =  listData.get(currentPo).getChoice().get(currentChouesPo).getFlag();
				new XiangchengMallProcClass(this).habitChoice(mParams, new InterfaceHabitChoiceCallBack() {
					
					@Override
					public void onSuccess(String icode, List<LifeHabitBean> datas) {
						listData.get(currentPo).setIsEdit("1");
						btnSure.setVisibility(View.GONE);
						Util.show(LifeHabitActivity.this, "记录成功");
					}
					
					@Override
					public void onError(String icode, String strErrMsg) {
						Util.show(LifeHabitActivity.this, strErrMsg);
					}
				});
				break;
			default:
				break;
		}
	}

	
	@Override
	public void onClick(int position, String flag) {
		if("1".equals(listData.get(currentPo).getIsEdit())){
			String type = listData.get(currentPo).getChoice().get(0).getType();
			Util.show(this, "5".equals(type) ? "本周已记录了，下次再来吧！" : "今天记录了，明天再来吧！");
			return;
		}
		
		this.currentChouesPo = position;
		for (int i = 0; i < listData.get(currentPo).getChoice().size(); i++) {
			listData.get(currentPo).getChoice().get(i).setFlag("0");
		}
		
		listData.get(currentPo).getChoice().get(position).setFlag("1");
		changData();
	
	}

}
