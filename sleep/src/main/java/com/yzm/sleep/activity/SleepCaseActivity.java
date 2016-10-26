package com.yzm.sleep.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.SleepCaseAdapter;
import com.yzm.sleep.bean.SleepCaseBean;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceCaseCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 睡眠方案
 * @author 
 */
public class SleepCaseActivity extends BaseActivity {
		
	private ListView mListView;
	private TextView tvOpinion;
	private SleepCaseAdapter mAdapter;
	private List<SleepCaseBean> listDatas;
	private int requesCode = 1002;
	private String state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleepcase);
		initView();
		state = getIntent().getStringExtra("state");
		getSleepCase();
	}
	
	private void initView(){
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("7日睡眠改善方案");
		Button right = (Button) findViewById(R.id.btn_title_right);
		right.setText("设置");
		right.setVisibility(View.VISIBLE);
		right.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.listview);
		tvOpinion = (TextView) findViewById(R.id.tv_opinion);
		tvOpinion.setMovementMethod(ScrollingMovementMethod.getInstance());
		mAdapter = new SleepCaseAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectPosition(position);
			}
		});
	}
	
	private void selectPosition(int posi){
		/*mAdapter.selectPo(posi);
		List<String> sugg = listDatas.get(posi).getSuggest();
		StringBuffer buffer = new StringBuffer();
		buffer.append("专家建议:\n\n");
		for (String string : sugg) {
			buffer.append(string);
			buffer.append("\n\n");
		}*/
		
		tvOpinion.setText(listDatas.get(posi).getSuggest()/*buffer.toString()*/);
	}
	
	private void getSleepCase(){
		new XiangchengMallProcClass(this).getSleepCaseData(PreManager.instance().getUserId(this), new InterfaceCaseCallBack() {
			
			@Override
			public void onSuccess(String icode, List<SleepCaseBean> datas) {
				mListView.setVisibility(View.VISIBLE);
				listDatas = datas;
				mAdapter.setDatas(datas);
				if(datas != null && datas.size() >0){
					int po = 0;
					for (int i = 0; i < datas.size(); i++) {
						if(datas.get(i).getTitle().equals(state)){
							po = i;
							break;
						}
					}
					selectPosition(po);
				}
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				Util.show(SleepCaseActivity.this, strErrMsg);
			}
		});
	}
	
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		super.onActivityResult(arg0, arg1, data);
		if(arg0 == requesCode && arg1 == RESULT_OK){
			Gson gson = new Gson();
			List<SleepCaseBean> datas = gson.fromJson(data.getStringExtra("resule"), new TypeToken<List<SleepCaseBean>>(){}.getType());
			if(datas != null && datas.size() > 0){
				listDatas = datas;
				mAdapter.setDatas(datas);
				selectPosition(0);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			startActivityForResult(new Intent(this, SetSleepTimeActivity.class), requesCode);
			break;
		default:
			break;
		}
	}
	
}
