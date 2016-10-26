package com.yzm.sleep.activity.community;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.model.MyAlertDialog;
import com.yzm.sleep.model.MyAlertDialog.MyOnClickListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceConsoleSaveCallBack;
import com.yzm.sleep.utils.MyTextWatcher;
import com.yzm.sleep.utils.MyTextWatcher.TextWatcherCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
/**
 * 编辑页面 用于编辑 文章或者话题 标题 内容 标签等
 * 默认的长度 10 如果需要更改长度 需要修改本类里面的信息
 * 传入的信息包含：
 * int types 用于区分是哪里传入的需要
 * String title 用于显示页面的标题
 * String defaultStr 如果有内容可以传过来继续编辑 没有可以不传n
 * 
 * 返回的resultCode == 700+types
 * @author chen
 * types  10 11 12 13 14 已经被使用
 */
public class ReleaseEditActivity extends BaseActivity implements TextWatcherCallBack {

	private int editType = 10;
	private String editContext = "";
	private String gType = "";
	private EditText releaseEdit;
	private TextView releaseText;
	private int limitLenth = 5;
	private Button back, commit;
	private TextView title;
	private String editTitle = "";
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_release_edit);;
		Intent intent=getIntent(); 
		gType = intent.getStringExtra("gType") == null ? "" : intent.getStringExtra("gType");
		editType = intent.getIntExtra("types", 10);
		editTitle = intent.getStringExtra("title");
		editContext = intent.getStringExtra("defaultStr");
		initViews();
		initEditText();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if ("1".equals(gType)) {
			MobclickAgent.onPageStart("StandardGroup_EditInfo"); 
		}else if ("2".equals(gType)){
			MobclickAgent.onPageStart("ExpertGroup_EditInfo"); 
		}
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if ("1".equals(gType)) {
			MobclickAgent.onPageEnd("StandardGroup_EditInfo");
		}else if ("2".equals(gType)){
			MobclickAgent.onPageEnd("ExpertGroup_EditInfo");
		}
		MobclickAgent.onPause(this);
	}

	private void initViews() {
		back = (Button) findViewById(R.id.back);
		commit = (Button) findViewById(R.id.btn_title_right);
		title  = (TextView) findViewById(R.id.title);
		releaseEdit = (EditText) findViewById(R.id.release_edit_ed);
		releaseText = (TextView) findViewById(R.id.release_edit_lenth_count);
		commit.setVisibility(View.VISIBLE);
		commit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		commit.setOnClickListener(this);
		back.setOnClickListener(this);
		commit.setText("完成");
		title.setText(editTitle);
		switch(editType){
		case 10:
			if(editContext == null || "".equals(editContext)){
				releaseEdit.setHint(R.string.release_conversation_default_title);
			}
			limitLenth = 20;
			break;
		case 11:
			if(editContext == null || "".equals(editContext)){
				releaseEdit.setHint(R.string.release_conversation_default_description);
			}
			limitLenth = 5000;
			break;
		case 12:
			if(editContext == null || "".equals(editContext)){
				releaseEdit.setHint(R.string.release_conversation_default_label);
			}
			limitLenth = 5;
			break;
		case 16:
			if(editContext == null || "".equals(editContext)){
				releaseEdit.setHint("请输入你的昵称");
			}
			limitLenth = 10;
			break;
		case 14:
			limitLenth = 200;
			break;
	
		case 15:
			commit.setText("保存");
			commit.setTextColor(getResources().getColor(R.color.ct_color));
			title.setTextColor(getResources().getColor(R.color.ct_color));
			findViewById(R.id.rl_title).setBackgroundColor(getResources().getColor(R.color.translucent));
			findViewById(R.id.rl_sv).setBackgroundColor(getResources().getColor(R.color.cbg_color));
			break;
		}
//		releaseEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limitLenth)}); 
		
		if(editContext != null && !TextUtils.isEmpty(editContext)){
			releaseEdit.setText(editContext);
		}
		releaseText.setText("剩余"+(limitLenth-releaseEdit.getText().length())+"/"+limitLenth+"字");
		if(editType == 15){
			releaseText.setVisibility(View.GONE);
		}
	}
	
	private void initEditText(){
		releaseEdit.requestFocus();
		//显示剩余输入量
		if(editType != 15)
			releaseEdit.addTextChangedListener(new MyTextWatcher(releaseEdit, this, limitLenth, this));
	}


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_title_right){
			if(16==editType){
				if (!TextUtils.isEmpty(releaseEdit.getText().toString().trim())) {
					Intent intent=new Intent();  
					intent.putExtra("user_nickname", releaseEdit.getText().toString().trim());
					setResult(700+editType, intent);
					AppManager.getAppManager().finishActivity();
				}else {
					Util.show(ReleaseEditActivity.this, "昵称不能为空");
				}
				return;
			}else if(editType == 15){
				if(TextUtils.isEmpty(releaseEdit.getText().toString())){
					Util.show(this, "请输入内容");
					return;
				}
				submit();
			}else{
				if(limitLenth>100 && releaseEdit.getText().toString().trim().length() < 5){
	//				toastMsg("不能少于5个字");
					toastCenter("内容不能少于5个字");
				}else{
					 Intent intent=new Intent();  
		             intent.putExtra("context", releaseEdit.getText().toString().trim());  
		             setResult(700+editType, intent);
		             AppManager.getAppManager().finishActivity();
				}
			}
		}if(v.getId() == R.id.back){
			dialogForExit();
		}
	}
	
	private void submit(){
		new XiangchengMallProcClass(this).sleepConsoleSave(PreManager.instance().getUserId(this), releaseEdit.getText().toString(), new InterfaceConsoleSaveCallBack() {
			
			@Override
			public void onSuccess(String icode, String strSuccMsg) {
	             setResult(RESULT_OK, new Intent().putExtra("context", releaseEdit.getText().toString().trim()));
	             AppManager.getAppManager().finishActivity();
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				Util.show(ReleaseEditActivity.this, strErrMsg);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			AppManager.getAppManager().finishActivity();
			dialogForExit();
		}
		return true;
	}

	@Override
	public void onTextNumsback(int num) {
		releaseText.setText("剩余"+num+"/"+limitLenth+"字");
	}
	
	private void toastCenter(String toastStr){
		Toast toast=Toast.makeText(ReleaseEditActivity.this, toastStr,
				Toast.LENGTH_SHORT); 
		 toast.setGravity(Gravity.CENTER, 0, 0);  
	     toast.show(); 
	}
	
	/*private void dialogForExit() {
		if (releaseEdit.getText().toString().trim().length()>0) {
			final CustomDialog dialog = new CustomDialog(
					ReleaseEditActivity.this);
			dialog.show();
			 dialog.setTitle("操作提示");
			dialog.setSub("是否放弃当前编辑内容？");
			dialog.setOnLeftClickListener("再看看", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {

					dialog.dismiss();
				}
			});
			dialog.setOnRightClickListener("放弃", new

			com.yzm.sleep.widget.CustomDialog.MyOnClickListener() {

				@Override
				public void Onclick(View v) {
					dialog.dismiss();
					AppManager.getAppManager().finishActivity();
				}
			});
		} else {
			AppManager.getAppManager().finishActivity();
		}
	}*/
	/**
	 * 放弃编辑对话框
	 */
	private  MyAlertDialog mDialog;
	private void dialogForExit() {
		if (releaseEdit.getText().toString().trim().length()>0) {
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
}
