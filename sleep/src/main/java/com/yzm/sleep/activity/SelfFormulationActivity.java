package com.yzm.sleep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceConsoleSaveCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomDialog;
/**
 * 自己用积极的语言表述当前的心理想法
 * @author Administrator
 *
 */
public class SelfFormulationActivity extends BaseActivity {

	private Button rightBtn;
	private EditText mEdt;
	private int position;
	private String getString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_formulation);
		initViews();
	}

	private void initViews() {
		rightBtn= (Button) findViewById(R.id.btn_title_right);
		mEdt = (EditText) findViewById(R.id.edit_self_write);
		findViewById(R.id.back).setOnClickListener(this);
		((TextView)findViewById(R.id.title)).setText("积极表述");
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setText("保存");
		rightBtn.setOnClickListener(this);
		position = getIntent().getIntExtra("position", 0);
		getString = getIntent().getStringExtra("text");
		mEdt.setText(getString);
		Editable etext = mEdt.getText();
		Selection.setSelection(etext, etext.length());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(TextUtils.isEmpty(getString) && TextUtils.isEmpty(mEdt.getText().toString())){
				AppManager.getAppManager().finishActivity(SelfFormulationActivity.this);
			}else if(getIntent().getStringExtra("text").equals(mEdt.getText().toString())){
				AppManager.getAppManager().finishActivity(SelfFormulationActivity.this);
			}else{
				dialogForExit();
			}
			return true;
		}else
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			dialogForExit();
			break;
		case R.id.btn_title_right:
			String consoleString = mEdt.getText().toString();
			if(TextUtils.isEmpty(consoleString) && TextUtils.isEmpty(getString)){
				AppManager.getAppManager().finishActivity(SelfFormulationActivity.this);
			}else{
				saveConsole(consoleString);
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void saveConsole(final String text){
		new XiangchengMallProcClass(this).sleepConsoleSave(PreManager.instance().getUserId(this), 
				text, new InterfaceConsoleSaveCallBack() {
			
			@Override
			public void onSuccess(String icode, String strSuccMsg) {
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.putExtra("result", text);
				 setResult(RESULT_OK, intent);
				AppManager.getAppManager().finishActivity();
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				toastMsg(strErrMsg);
				
			}
		});
	}

	/**
	 * 当有输入信息的时候提示是否放弃已编辑的内容
	 */
	private void dialogForExit() {
		if (!(TextUtils.isEmpty(mEdt.getText().toString()))) {
			final CustomDialog dialog = new CustomDialog(
					this);
			dialog.show();
			dialog.setTitle("是否放弃当前编辑内容？");
			dialog.setSubGone();
			dialog.setOnLeftClickListener("否", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					dialog.dismiss();
				}
			});
			dialog.setOnRightClickListener("是", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					AppManager.getAppManager().finishActivity(SelfFormulationActivity.this);
					dialog.dismiss();
				}
			});
		} else {
			AppManager.getAppManager().finishActivity(SelfFormulationActivity.this);
		}
	}
	
	
}
