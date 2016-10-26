package com.yzm.sleep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSaveNoteCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepNoteNowCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SaveNoteParams;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.XiangchengMallProcClass;

public class NoteRecordingActivity extends BaseActivity {

	private Button commit;
	private TextView title;
	private EditText noteEditText;
	private String todayNote = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_recording);
		
		initViews();
		getTodayNote();
	}
	
	private void initViews(){
		findViewById(R.id.back).setOnClickListener(this);
		commit = (Button) findViewById(R.id.btn_title_right);
		title  = (TextView) findViewById(R.id.title);
		noteEditText = (EditText) findViewById(R.id.recording_edit_ed);
		commit.setVisibility(View.VISIBLE);
		commit.setOnClickListener(this);
		commit.setText("完成");
		title.setText("把想的事情记录下来");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			dialogForExit();
			break;
			
		case R.id.btn_title_right:
			if(noteEditText.getText().toString().trim().length()>0){
				//提交
				saveUserNote(noteEditText.getText().toString());
			}else{
				toastMsg("没有内容");
			}
			break;

		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			dialogForExit();
		}
		return true;
	}
	
	private  MyAlertDialog mDialog;
	private void dialogForExit() {
		if (noteEditText.getText().toString().trim().length()>0 && !noteEditText.getText().toString().equals(todayNote)) {
			if (null == mDialog) {
				mDialog = new MyAlertDialog(this,
						R.style.bottom_animation);
			}
			mDialog.show();
			mDialog.setTV1("是否放弃当前编辑内容？");
			mDialog.setTV2("再看看", new MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					mDialog.dismiss();
				}
			}, View.VISIBLE);
			mDialog.setTV3("", null, View.GONE);
			mDialog.setTV4("", null, View.GONE);
			mDialog.setTVbottom("放弃", new MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					mDialog.dismiss();
					AppManager.getAppManager().finishActivity();
				}

			}, View.VISIBLE);
		} else {
			AppManager.getAppManager().finishActivity();
		}
	}
	
	/**
	 * 获取今天的记事内容
	 */
	private void getTodayNote(){
		
		new XiangchengMallProcClass(this).sleepNoteNow(PreManager.instance().getUserId(this), new InterfaceSleepNoteNowCallBack() {
			
			@Override
			public void onSuccess(String icode, String text) {
				todayNote = text;
				noteEditText.setText(text);
				Editable etext = noteEditText.getText();
				Selection.setSelection(etext, etext.length());
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
			}
		});
	}
	
	/**
	 * 保存当前的记事
	 * @param note
	 */
	private void saveUserNote(final String note){
		SaveNoteParams mparams = new SaveNoteParams();
		mparams.my_int_id = PreManager.instance().getUserId(this);
		mparams.text = note;

		new XiangchengMallProcClass(this).sleepNoteSave(mparams, new InterfaceSaveNoteCallBack() {

			@Override
			public void onSuccess(String icode, String strSuccMsg) {
				Intent intent=new Intent();  
	            intent.putExtra("data", note);
	            setResult(RESULT_OK, intent);
	            AppManager.getAppManager().finishActivity();
			}

			@Override
			public void onError(String icode, String strErrMsg) {
				if (strErrMsg.equals("未知错误")) {
					toastMsg("保存失败");
				}else
					toastMsg(strErrMsg);

			}
		});

	}
}
