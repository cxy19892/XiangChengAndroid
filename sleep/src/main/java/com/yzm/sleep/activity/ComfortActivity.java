package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.ComfortAdapter;
import com.yzm.sleep.adapter.ComfortAdapter.lastPagekListener;
import com.yzm.sleep.bean.ComfortBean;
import com.yzm.sleep.bean.ComfortResult;
import com.yzm.sleep.bean.ComfortShowsBean;
import com.yzm.sleep.bean.ComfortStrBean;
import com.yzm.sleep.refresh.Util;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceComfortCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.SlideCutListView;
import com.yzm.sleep.widget.SlideCutListView.RemoveDirection;
import com.yzm.sleep.widget.SlideCutListView.RemoveListener;

/**
 * 睡眠安慰界面
 * @author Administrator
 *
 */
public class ComfortActivity extends BaseActivity {
	private SlideCutListView mlistv;
	private ComfortAdapter mAdapter;
	private View noNet;
	private Button rightBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comfort);
		initViews();
		getComfortData();
	}

	private void initViews() {
		mlistv = (SlideCutListView) findViewById(R.id.comfort_listview);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		noNet = inflater.inflate(R.layout.layout_no_net, null);
		RelativeLayout rlP = (RelativeLayout) noNet.findViewById(R.id.rl_p);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlP.getLayoutParams();
		params.height = Constant.screenHeight - Util.dip2px(this, 98);
		rlP.setLayoutParams(params);
		mlistv.addHeaderView(noNet, null, false);
		
		findViewById(R.id.back).setOnClickListener(this);
		rightBtn = (Button) findViewById(R.id.btn_title_right);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setText("重置");
		rightBtn.setOnClickListener(this);
		
		((TextView)findViewById(R.id.title)).setText("认知重构");
		mAdapter = new ComfortAdapter(getLayoutInflater(), getScreenHeight());
		mlistv.setAdapter(mAdapter);
		mAdapter.setLastPageClickListener(mClickListener);
		mlistv.setRemoveListener(new RemoveListener() {
			
			@Override
			public void removeItem(RemoveDirection direction, int position) {
				ComfortResult getComfortResult= mAdapter.getData();
				if(getComfortResult.getList().get(position).getContentList().size()>0){
					for(int k = 0 ; k < getComfortResult.getList().get(position).getContentList().size() ; k++){
						if(!getComfortResult.getList().get(position).getContentList().get(k).isIsread()){
							getComfortResult.getList().get(position).getContentList().get(k).setIsread(true);
							break;
						}
					};
					mAdapter.setData(getComfortResult);
				}
			}
		});
		noNet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getData();
			}
		});
	}

	
	private ComfortResult setData(List<ComfortBean> list){
		ComfortResult mComfortResult = new ComfortResult(); 
		List<ComfortShowsBean> mList = new ArrayList<ComfortShowsBean>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).content.size()>20){
				ComfortShowsBean mBean = new ComfortShowsBean();
				List<ComfortStrBean> comfortStrlist = new ArrayList<ComfortStrBean>();
				for (int j = 0 ; j < 20 ;j++) {
					ComfortStrBean mComfortStrBean = new ComfortStrBean();
					mComfortStrBean.setContent(list.get(i).content.get(j));
					mComfortStrBean.setIsread(false);
					comfortStrlist.add(mComfortStrBean);
				}
				mBean.setLaString("这些话都没有安慰到我，我要自己写");
				mBean.setContentList(comfortStrlist);
				mList.add(mBean);
			}else{
				ComfortShowsBean mBean = new ComfortShowsBean();
				List<ComfortStrBean> comfortStrlist = new ArrayList<ComfortStrBean>();
				for (int j = 0 ; j < list.get(i).content.size() ;j++) {
					ComfortStrBean mComfortStrBean = new ComfortStrBean();
					mComfortStrBean.setContent(list.get(i).content.get(j));
					mComfortStrBean.setIsread(false);
					comfortStrlist.add(mComfortStrBean);
				}
				mBean.setLaString("这些话都没有安慰到我，我要自己写");
				mBean.setContentList(comfortStrlist);
				mList.add(mBean);
			}
		}
		mComfortResult.setList(mList);
		return mComfortResult;
	}
	
	private lastPagekListener mClickListener = new lastPagekListener() {
		
		@Override
		public void lastPageCallBack(int position, String text) {
			Intent intent = new Intent(ComfortActivity.this, SelfFormulationActivity.class);
			intent.putExtra("text", text.equals("这些话都没有安慰到我，我要自己写") ? "" : text);
			intent.putExtra("position", position);
			startActivityForResult(intent, 103);
		}
	};
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(mAdapter.getData() != null && mAdapter.getData().getList().size()>0){
				savemChoise(mAdapter.getData().toString());
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back){
			if(mAdapter.getData() != null){
				savemChoise(mAdapter.getData().toString());
			}
			AppManager.getAppManager().finishActivity(ComfortActivity.this);
		}else if(v.getId() == R.id.btn_title_right){
			getData();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void getComfortData(){
		String comfortresaultStr = PreManager.instance().getComfortChoice(this);
		if(!TextUtils.isEmpty(comfortresaultStr)){
			mlistv.removeHeaderView(noNet);
			ComfortResult mResult = com.yzm.sleep.utils.Util.getComfortInfoFromJson(comfortresaultStr);
			mAdapter.setData(mResult);
		}else{
			getData();
		}
	}

	private void getData(){
		mlistv.removeHeaderView(noNet);
		if(checkNetWork(ComfortActivity.this)){
			new XiangchengMallProcClass(this).getComfortList(PreManager.instance().getUserId(this), new InterfaceComfortCallBack() {

				@Override
				public void onSuccess(String icode, List<ComfortBean> datas) {
					mAdapter.setData(setData(datas));
				}

				@Override
				public void onError(String icode, String strErrMsg) {

				}
			});
		}else{
			mlistv.addHeaderView(noNet);
		}
	}

	private void savemChoise(String comform){
		PreManager.instance().saveComfortChoice(this, comform);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 103 && resultCode == RESULT_OK){
			String result = data.getExtras().getString("result");
			int position = data.getExtras().getInt("position");
			ComfortResult resaultComfortResult = mAdapter.getData();
			resaultComfortResult.getList().get(position).setLaString(result);
			mAdapter.setData(resaultComfortResult);
		}
    }
	
}
