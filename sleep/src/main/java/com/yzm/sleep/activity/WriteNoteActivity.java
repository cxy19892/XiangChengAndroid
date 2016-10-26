package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.NoteListAdapter;
import com.yzm.sleep.bean.NoteBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.BottomPopDialog;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSaveNoteCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepNoteDelCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepNoteListCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepNoteNowCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.SaveNoteParams;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomDialog;

public class WriteNoteActivity extends BaseActivity implements OnItemClickListener {

//	private AnimationSet animBigerDiasapear, animSmarApear;
	private AlphaAnimation appearAnim;
	private AlphaAnimation disAppearAnim;
	private RelativeLayout relBookcover, relNoteWrite;
	private MaterialRefreshLayout pullrView;
	private View noDataView;
	private LinearLayout relNoteList;
	private ListView mListView;
	private EditText edtNote;
	private TextView title, hisNote;
	private Button rightBtn, deleteBtn;
	private String todayNote = "";
	private int dataPage =1, totalPage = 0;
	private NoteListAdapter mAdapter;
	private List<NoteBean> mNoteList= new ArrayList<NoteBean>();
	private BottomPopDialog popDialog;
	private final int OPEN_STATE_HOME = 1 , OPEN_STATE_HISTORY = 2; 
	private int OPEN_STATE = 0;
	private boolean isRunninganim = false;
	private ImageView bookFace, bookFaceBack;
	private RelativeLayout rlBookContent;
	private boolean isTodayNoted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_note);
		initViews();
		setAnimation();
		if(checkNetWork(this)){
			rlBookContent.setVisibility(View.GONE);
			bookFaceBack.setVisibility(View.GONE);
			bookFace.setVisibility(View.GONE);
			getTodayNote(true);
		}
	}
	
	@SuppressLint("NewApi") 
	private void initViews() {
		
		bookFace = (ImageView) findViewById(R.id.book_face);
		bookFaceBack = (ImageView) findViewById(R.id.book_face_back);
		bookFace.setOnClickListener(this);
		rlBookContent = (RelativeLayout) findViewById(R.id.rl_book_content);
		relBookcover = (RelativeLayout) findViewById(R.id.rel_note_book);
		relNoteList  = (LinearLayout) findViewById(R.id.rel_note_list);
		edtNote      = (EditText) findViewById(R.id.edt_notes);
		relNoteWrite = (RelativeLayout) findViewById(R.id.rel_notes_write);
		rightBtn = (Button) findViewById(R.id.btn_title_right);
		deleteBtn = (Button) findViewById(R.id.btn_delet);
		findViewById(R.id.back).setOnClickListener(this);
		hisNote = (TextView) findViewById(R.id.tv_history_note);
		relBookcover.setOnClickListener(this);
		title = (TextView)findViewById(R.id.title);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
		rightBtn.setText("历史");
		title.setText("思绪清零");
		relBookcover.setVisibility(View.VISIBLE);
		relNoteList.setVisibility(View.GONE);
		relNoteWrite.setVisibility(View.GONE);
		
		pullrView = (MaterialRefreshLayout) findViewById(R.id.pull_refreshview);
		mListView = (ListView) findViewById(R.id.lv_note_list);
		
		mAdapter = new NoteListAdapter(this);
		
		removeFooter();
		
		mListView.setOnItemClickListener(this);
		
		pullrView.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				dataPage = 1;
				if (checkNetWork(WriteNoteActivity.this)) {
					dataPage = 1;
					getNotesList(dataPage, true);
				} else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							pullrView.finishRefresh();
							Util.show(WriteNoteActivity.this, "请检查您的网络");
		            		removeFooter();
							if(mNoteList.size()==0){
								pullrView
	        					.addListViewState(mListView, Constant.NO_NET);
							}
						}
					}, 300);
				}
			}
		});
		
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
				
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount; 
	            if(lastItem == totalItemCount) { 
	            	if (checkNetWork(WriteNoteActivity.this)) {
	            		if(mListView.getChildCount()>0){
	            			View lastItemView=(View) mListView.getChildAt(mListView.getChildCount()-1); 
	            			if ((mListView.getBottom())==lastItemView.getBottom()) { 
	            				if (dataPage < totalPage) {
	            					pullrView.addListViewState(mListView,
											Constant.LOADING);
	            					if(dataPage < totalPage){
		            					dataPage += 1;
		            					getNotesList(dataPage, false);
	            					}
	            				}
	            			} 
	            		}
	            	}
	            }
			}
		});
		
		pullrView.setOnClickRequestListener(new OnClickRequestListener() {
			
			@Override
			public void setRequest() {
				pullrView.autoRefresh();
				dataPage = 1;
				getNotesList(dataPage, false);
			}
		});
		
		mListView.setAdapter(mAdapter);
	}



	private void setAnimation(){
		appearAnim = new AlphaAnimation(0, 1);
		appearAnim.setDuration(500);
		appearAnim.setStartOffset(300);
		
		disAppearAnim = new AlphaAnimation(1, 0);
		disAppearAnim.setDuration(500);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(relNoteList.getVisibility() == View.VISIBLE && deleteBtn.getVisibility() == View.VISIBLE){
				deleteBtn.setVisibility(View.GONE);
				rightBtn.setVisibility(View.VISIBLE);
				mAdapter.setIsSelect(false);
				return true;
			}else if(relNoteList.getVisibility() == View.VISIBLE && deleteBtn.getVisibility() == View.GONE){
				relBookcover.setVisibility(View.VISIBLE);
				if(isTodayNoted){
					bookFace.setImageResource(R.drawable.notebook_face2);
				}else{
					bookFace.setImageResource(R.drawable.notebook_face1);
				}
				relNoteWrite.setVisibility(View.GONE);
				relNoteList.setVisibility(View.GONE);
				bookFace.setVisibility(View.VISIBLE);
				rlBookContent.setVisibility(View.GONE);
				rightBtn.setVisibility(View.VISIBLE);
				title.startAnimation(appearAnim);
				rightBtn.startAnimation(appearAnim);
				title.setText("思绪清零");
				rightBtn.setText("历史");
				return true;
			}
			if(relNoteWrite.getVisibility() == View.VISIBLE){
				if(edtNote.getVisibility() == View.VISIBLE && !todayNote.equals(edtNote.getText().toString())){
					dialogForExit();
				}else{
					if(OPEN_STATE == OPEN_STATE_HISTORY){
						relNoteWrite.setVisibility(View.GONE);
						relNoteList.setVisibility(View.VISIBLE);
						rightBtn.setVisibility(View.VISIBLE);
						title.startAnimation(appearAnim);
						rightBtn.startAnimation(appearAnim);
						title.setText("历史");
						rightBtn.setText("删除");
					}else{
						if(isTodayNoted){
							bookFace.setImageResource(R.drawable.notebook_face2);
						}else{
							bookFace.setImageResource(R.drawable.notebook_face1);
						}
						closeAnim();
//						relBookcover.startAnimation(animSmarApear);
						relBookcover.setVisibility(View.VISIBLE);
						relNoteWrite.setVisibility(View.GONE);
						relNoteList.setVisibility(View.GONE);
						rightBtn.setVisibility(View.VISIBLE);
						title.startAnimation(appearAnim);
						rightBtn.startAnimation(appearAnim);
						title.setText("思绪清零");
						rightBtn.setText("历史");
					}
				}
					
			}else{
				if(relBookcover.getVisibility() == View.GONE){
					if(isTodayNoted){
						bookFace.setImageResource(R.drawable.notebook_face2);
					}else{
						bookFace.setImageResource(R.drawable.notebook_face1);
					}
					closeAnim();
//					relBookcover.startAnimation(animSmarApear);
					relBookcover.setVisibility(View.VISIBLE);
					relNoteWrite.setVisibility(View.GONE);
					relNoteList.setVisibility(View.GONE);
					rightBtn.setVisibility(View.VISIBLE);
					title.startAnimation(appearAnim);
					rightBtn.startAnimation(appearAnim);
					title.setText("思绪清零");
					rightBtn.setText("历史");
				}else
					AppManager.getAppManager().finishActivity();
			}
			todayNote = "";
			return true;
		}else
		return super.onKeyDown(keyCode, event);
	}

	private void openAnim(){
		rlBookContent.setVisibility(View.VISIBLE);
		//内容也放大
		final ScaleAnimation scalAnim2 = new ScaleAnimation(1f, 1.4f, 1f, 1.7f, AnimationSet.RELATIVE_TO_SELF, 0.5f, AnimationSet.RELATIVE_TO_SELF, 0.5f);
		scalAnim2.setDuration(500);
		scalAnim2.setStartOffset(500);
		scalAnim2.setFillAfter(true);
		//封面打开
		ScaleAnimation scalAnim1 = new ScaleAnimation(1f, 0f, 1f, 1f, AnimationSet.RELATIVE_TO_SELF, 0f, AnimationSet.RELATIVE_TO_SELF, 1f);
		scalAnim1.setDuration(500);
		scalAnim1.setFillAfter(true);
		bookFace.startAnimation(scalAnim1);
		//
		final ScaleAnimation scalAnim3 = new ScaleAnimation(0f, -1f, 1f, 1f, AnimationSet.RELATIVE_TO_SELF, 0f, AnimationSet.RELATIVE_TO_SELF, 0f);
		scalAnim3.setDuration(500);
		scalAnim3.setFillAfter(true);
		//封面消失
		final AlphaAnimation alphaAnim1 = new AlphaAnimation(1, 0);
		alphaAnim1.setDuration(500);
		alphaAnim1.setFillAfter(true);
		//
		final TranslateAnimation translate2 = new TranslateAnimation(0, -getScreenWidth(), 0, 0);
		alphaAnim1.setDuration(500);
		translate2.setFillAfter(true);

		final AnimationSet openAnim1 = new AnimationSet(false);
		openAnim1.addAnimation(scalAnim3);
		openAnim1.addAnimation(scalAnim2);
		
		final TranslateAnimation translate1 = new TranslateAnimation(0, -bookFace.getRight()+50, 0, 0);
		translate1.setFillAfter(true);
		translate1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				bookFace.setVisibility(View.INVISIBLE);
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bookFaceBack.startAnimation(openAnim1);
				
			}
		});
		
		
		
		scalAnim1.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				bookFace.setClickable(false);
				isRunninganim = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				bookFaceBack.startAnimation(translate1);
				
			}
		});
		scalAnim3.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				rlBookContent.startAnimation(scalAnim2);
				title.startAnimation(appearAnim);
				rightBtn.startAnimation(appearAnim);
				title.setText(TimeFormatUtil.getTodayMD());
				rightBtn.setText("保存");
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
//				rlBookContent.startAnimation(scalAnim2);
				getTodayNote(false);
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						isRunninganim = false;
						OPEN_STATE = OPEN_STATE_HOME;
						relBookcover.setVisibility(View.GONE);
						bookFaceBack.setVisibility(View.INVISIBLE);
						relNoteWrite.setVisibility(View.VISIBLE);
						hisNote.setVisibility(View.GONE);
						edtNote.setVisibility(View.VISIBLE);
						edtNote.requestFocus();
						edtNote.setText(todayNote);
						Editable etext = edtNote.getText();
						Selection.setSelection(etext, etext.length());
						showKeyboard(edtNote);
					}
				}, 500);
				
			}
		});
	}
	
	private void closeAnim(){
		
		final ScaleAnimation scalAnim1 = new ScaleAnimation(0f, 1f, 1f, 1f, AnimationSet.RELATIVE_TO_SELF, 0f, AnimationSet.RELATIVE_TO_SELF, 0f);
		scalAnim1.setDuration(500);
		scalAnim1.setStartOffset(500);
		scalAnim1.setFillAfter(true);
		
		final ScaleAnimation scalAnim2 = new ScaleAnimation(1.4f, 1f, 1.5f, 1f, AnimationSet.RELATIVE_TO_SELF, 0.5f, AnimationSet.RELATIVE_TO_SELF, 0.5f);
		scalAnim2.setDuration(500);
		scalAnim2.setFillAfter(true);
		
		
		final ScaleAnimation scalAnim3 = new ScaleAnimation(-1f, 0f, 1f, 1f, AnimationSet.RELATIVE_TO_SELF, 0f, AnimationSet.RELATIVE_TO_SELF, 0f);
		scalAnim3.setDuration(500);
		scalAnim3.setStartOffset(500);
		scalAnim3.setFillAfter(true);
		
		final ScaleAnimation scalAnim4 = new ScaleAnimation(1.4f, 1f, 1.5f, 1f, AnimationSet.RELATIVE_TO_SELF, 0.5f, AnimationSet.RELATIVE_TO_SELF, 0.5f);
		scalAnim4.setDuration(500);

		scalAnim4.setFillAfter(true);
		
		final AnimationSet openAnim2 = new AnimationSet(false);
		openAnim2.addAnimation(scalAnim3);
		openAnim2.addAnimation(scalAnim4);
		
		rlBookContent.startAnimation(scalAnim2);
		bookFaceBack.startAnimation(openAnim2);
		
		scalAnim2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				bookFace.setClickable(true);
				hideKeyboardB(edtNote);
				isRunninganim = true;
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bookFace.setVisibility(View.VISIBLE);
				bookFace.startAnimation(scalAnim1);
			}
		});
		
		scalAnim1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				isRunninganim = false;
			}
		});
	}
	
	@SuppressLint("NewApi") 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.book_face:
			if(!isRunninganim)
				openAnim();
			break;
		case R.id.btn_title_right:
			if(!isRunninganim){
				if(relNoteWrite.getVisibility() == View.VISIBLE){//保存操作
					if(TextUtils.isEmpty(todayNote) && TextUtils.isEmpty(edtNote.getText().toString())){
							isTodayNoted = false;
						if(OPEN_STATE == OPEN_STATE_HOME){
							if(isTodayNoted){
								bookFace.setImageResource(R.drawable.notebook_face2);
							}else{
								bookFace.setImageResource(R.drawable.notebook_face1);
							}
							closeAnim();
							relBookcover.setVisibility(View.VISIBLE);
							relNoteWrite.setVisibility(View.GONE);
							title.startAnimation(appearAnim);
							rightBtn.startAnimation(appearAnim);
							title.setText("思绪清零");
							rightBtn.setText("历史");
						}else{
							relNoteWrite.setVisibility(View.GONE);
							relNoteList.setVisibility(View.VISIBLE);
							rightBtn.setVisibility(View.VISIBLE);
							title.startAnimation(appearAnim);
							rightBtn.startAnimation(appearAnim);
							title.setText("历史");
							rightBtn.setText("删除");
						}
					}else{
						if(checkNetWork(this)){
							if(TextUtils.isEmpty(edtNote.getText().toString()))
								isTodayNoted = false;
							else
								isTodayNoted = true;
							saveUserNote(edtNote.getText().toString());
							if(OPEN_STATE == OPEN_STATE_HOME){
								if(isTodayNoted){
									bookFace.setImageResource(R.drawable.notebook_face2);
								}else{
									bookFace.setImageResource(R.drawable.notebook_face1);
								}
								closeAnim();
								relBookcover.setVisibility(View.VISIBLE);
								relNoteWrite.setVisibility(View.GONE);
								title.startAnimation(appearAnim);
								rightBtn.startAnimation(appearAnim);
								title.setText("思绪清零");
								rightBtn.setText("历史");
								todayNote = "";
							}else{
								List<NoteBean> tempNoteList = mAdapter.getData();
								for(int n = 0 ;  n < tempNoteList.size() ; n++){
									long time = 0l;
									try {
										time = Long.parseLong(tempNoteList.get(n).getDateline());
									} catch (NumberFormatException e) {
										e.printStackTrace();
									}
									if(TimeFormatUtil.getTimeForYM(time).
											equals(TimeFormatUtil.getTimeForYM(System.currentTimeMillis()/1000))){
										tempNoteList.get(n).setText(edtNote.getText().toString());
									}
									mAdapter.SetData(tempNoteList);	
								}
								relNoteWrite.setVisibility(View.GONE);
								relNoteList.setVisibility(View.VISIBLE);
								rightBtn.setVisibility(View.VISIBLE);
								title.startAnimation(appearAnim);
								rightBtn.startAnimation(appearAnim);
								title.setText("历史");
								rightBtn.setText("删除");
								todayNote = "";
							}
							
						}else{
							toastMsg("网络连接失败");
						}
						
					}

				}else if(relBookcover.getVisibility() == View.VISIBLE){//历史
					pullrView.autoRefresh();
					relBookcover.startAnimation(disAppearAnim);
					relNoteList.startAnimation(appearAnim);
					relBookcover.setVisibility(View.GONE);
					relNoteList.setVisibility(View.VISIBLE);
					title.startAnimation(appearAnim);
					rightBtn.startAnimation(appearAnim);
					title.setText("历史");
					rightBtn.setText("删除");
				}else if(relNoteList.getVisibility() == View.VISIBLE){//编辑
					rightBtn.setVisibility(View.GONE);
					mAdapter.SetData(initList(mAdapter.getData()));
					mAdapter.setIsSelect(true);
					deleteBtn.setVisibility(View.VISIBLE);
				}
			}
			break;
		case R.id.btn_delet:
			String delIds = getSelectedIds();
			if(delIds.equals("")){
				toastMsg("请选择需要删除的笔记");
			}else{
				String[] noteidArr = delIds.split(",");
				if(popDialog == null){
					popDialog = new BottomPopDialog(WriteNoteActivity.this, new BottomPopDialog.PopDialogClickListener() {
						
						@Override
						public void PopDialogClick(int clickid) {
							if(1 == clickid){
								deleteNotes(getSelectedIds());
								reFreshNoteList();
								popDialog.dismiss();
							}
						}
					});
				}
				popDialog.show();
				popDialog.setShowViews(1, "确定要删除这"+noteidArr.length+"条笔记", "");
				deleteBtn.setVisibility(View.GONE);
				rightBtn.setVisibility(View.VISIBLE);
				mAdapter.setIsSelect(false);
			}
			break;
	
		case R.id.back:
			if(relNoteList.getVisibility() == View.VISIBLE && deleteBtn.getVisibility() == View.VISIBLE){
				deleteBtn.setVisibility(View.GONE);
				rightBtn.setVisibility(View.VISIBLE);
				mAdapter.setIsSelect(false);
				break;
			}else if(relNoteList.getVisibility() == View.VISIBLE && deleteBtn.getVisibility() == View.GONE){
				if(isTodayNoted){
					bookFace.setImageResource(R.drawable.notebook_face2);
				}else{
					bookFace.setImageResource(R.drawable.notebook_face1);
				}
				relBookcover.setVisibility(View.VISIBLE);
				relNoteWrite.setVisibility(View.GONE);
				relNoteList.setVisibility(View.GONE);
				bookFace.setVisibility(View.VISIBLE);
				rlBookContent.setVisibility(View.GONE);
				rightBtn.setVisibility(View.VISIBLE);
				title.startAnimation(appearAnim);
				rightBtn.startAnimation(appearAnim);
				title.setText("思绪清零");
				rightBtn.setText("历史");
				break;
			}
			if(relNoteWrite.getVisibility() == View.VISIBLE){
				if(edtNote.getVisibility() == View.VISIBLE && !todayNote.equals(edtNote.getText().toString())){
					dialogForExit();
				}else{
					if(OPEN_STATE == OPEN_STATE_HISTORY){
						relNoteWrite.setVisibility(View.GONE);
						relNoteList.setVisibility(View.VISIBLE);
						rightBtn.setVisibility(View.VISIBLE);
						title.startAnimation(appearAnim);
						rightBtn.startAnimation(appearAnim);
						title.setText("历史");
						rightBtn.setText("删除");
					}else{
						if(!isRunninganim){
							if(isTodayNoted){
								bookFace.setImageResource(R.drawable.notebook_face2);
							}else{
								bookFace.setImageResource(R.drawable.notebook_face1);
							}
							closeAnim();
							//						relBookcover.startAnimation(animSmarApear);
							relBookcover.setVisibility(View.VISIBLE);
							relNoteWrite.setVisibility(View.GONE);
							relNoteList.setVisibility(View.GONE);
							rightBtn.setVisibility(View.VISIBLE);
							title.startAnimation(appearAnim);
							rightBtn.startAnimation(appearAnim);
							title.setText("思绪清零");
							rightBtn.setText("历史");
						}
					}
				}
					
			}else{
				if(!isRunninganim){
					if(relBookcover.getVisibility() == View.GONE){
						if(isTodayNoted){
							bookFace.setImageResource(R.drawable.notebook_face2);
						}else{
							bookFace.setImageResource(R.drawable.notebook_face1);
						}
						closeAnim();
						//					relBookcover.startAnimation(animSmarApear);
						relBookcover.setVisibility(View.VISIBLE);
						relNoteWrite.setVisibility(View.GONE);
						relNoteList.setVisibility(View.GONE);
						rightBtn.setVisibility(View.VISIBLE);
						title.startAnimation(appearAnim);
						rightBtn.startAnimation(appearAnim);
						title.setText("思绪清零");
						rightBtn.setText("历史");
					}else
						AppManager.getAppManager().finishActivity();
				}
			}
			todayNote = "";
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 当有输入信息的时候提示是否放弃已编辑的内容
	 */
	private void dialogForExit() {
		if (!(TextUtils.isEmpty(edtNote.getText().toString()))) {
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
					if(OPEN_STATE == OPEN_STATE_HISTORY){
						relNoteWrite.setVisibility(View.GONE);
						relNoteList.setVisibility(View.VISIBLE);
						rightBtn.setVisibility(View.VISIBLE);
						title.startAnimation(appearAnim);
						rightBtn.startAnimation(appearAnim);
						title.setText("历史");
						rightBtn.setText("编辑");
					}else{
						if(isTodayNoted){
							bookFace.setImageResource(R.drawable.notebook_face2);
						}else{
							bookFace.setImageResource(R.drawable.notebook_face1);
						}
						closeAnim();
//						relBookcover.startAnimation(animSmarApear);
						relBookcover.setVisibility(View.VISIBLE);
						relNoteWrite.setVisibility(View.GONE);
						relNoteList.setVisibility(View.GONE);
						rightBtn.setVisibility(View.VISIBLE);
						title.startAnimation(appearAnim);
						rightBtn.startAnimation(appearAnim);
						title.setText("思绪清零");
						rightBtn.setText("历史");
					}
					todayNote = "";
					dialog.dismiss();
				}
			});
		} else {
			if(OPEN_STATE == OPEN_STATE_HISTORY){
				relNoteWrite.setVisibility(View.GONE);
				relNoteList.setVisibility(View.VISIBLE);
				rightBtn.setVisibility(View.VISIBLE);
				title.startAnimation(appearAnim);
				rightBtn.startAnimation(appearAnim);
				title.setText("历史");
				rightBtn.setText("编辑");
			}else{
				if(isTodayNoted){
					bookFace.setImageResource(R.drawable.notebook_face2);
				}else{
					bookFace.setImageResource(R.drawable.notebook_face1);
				}
				closeAnim();
//				relBookcover.startAnimation(animSmarApear);
				relBookcover.setVisibility(View.VISIBLE);
				relNoteWrite.setVisibility(View.GONE);
				relNoteList.setVisibility(View.GONE);
				rightBtn.setVisibility(View.VISIBLE);
				title.startAnimation(appearAnim);
				rightBtn.startAnimation(appearAnim);
				title.setText("思绪清零");
				rightBtn.setText("历史");
			}
			todayNote = "";
		}
	}
	
	private void removeFooter(){
		pullrView.addListViewState(mListView, -1);
		try {
			mListView.removeFooterView(noDataView);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<NoteBean> initList(List<NoteBean> list){
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSelected(false);
		}
		return list;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		List<NoteBean> mgetList = mAdapter.getData();
		rightBtn.clearAnimation();
		NoteBean mNoteBean = mgetList.get(position); 
		if(mAdapter.getIsSelect()){//选择删除
			mgetList.get(position).setSelected(!mgetList.get(position).isSelected());
			mAdapter.SetData(mgetList);
		}else{//打开笔记
			long time = 0l;
			try {
				time = Long.parseLong(mNoteBean.getDateline());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if(mNoteBean.getFlag() == 1){
				relNoteList.setVisibility(View.GONE);
				relNoteWrite.setVisibility(View.VISIBLE);
				title.setText(TimeFormatUtil.getTimeForYM(time));
				title.startAnimation(appearAnim);
				rightBtn.startAnimation(appearAnim);
				rightBtn.setVisibility(View.VISIBLE);
				rightBtn.setText("保存");
				OPEN_STATE = OPEN_STATE_HISTORY;
				hisNote.setVisibility(View.GONE);
				edtNote.setVisibility(View.VISIBLE);
				todayNote = mNoteBean.getText();
				edtNote.setText(mNoteBean.getText());
				Editable etext = edtNote.getText();
				Selection.setSelection(etext, etext.length());
			}else{
				OPEN_STATE = OPEN_STATE_HISTORY;
				relNoteList.setVisibility(View.GONE);
				relNoteWrite.setVisibility(View.VISIBLE);
				title.setText(TimeFormatUtil.getTimeForYM(time));
				title.startAnimation(appearAnim);
				rightBtn.setVisibility(View.GONE);;
				hisNote.setVisibility(View.VISIBLE);
				edtNote.setVisibility(View.GONE);
				hisNote.setText(mNoteBean.getText());
			}
		}
	}
	
	private String getSelectedIds(){
		StringBuffer sb = new StringBuffer();
		for (NoteBean iter : mAdapter.getData()) {
			if(iter.isSelected()){
				sb.append(iter.getNoteid());
				sb.append(",");
			}
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	private void reFreshNoteList(){
		List<NoteBean> tempList = new ArrayList<NoteBean>();
		List<NoteBean> getList = mAdapter.getData();
		for (NoteBean iter : mAdapter.getData()) {
			if(iter.isSelected()){
				tempList.add(iter);
			}
		}
		for (NoteBean noteBean : tempList) {
			if(noteBean.getFlag() == 1){
				isTodayNoted = false;
			}
		}
		getList.removeAll(tempList);
		mAdapter.SetData(getList);
	}

	/**
	 * 保存当前的记事
	 * @param note
	 */
	private void saveUserNote(String note){
		SaveNoteParams mparams = new SaveNoteParams();
		mparams.my_int_id = PreManager.instance().getUserId(WriteNoteActivity.this);
		mparams.text = note;

		new XiangchengMallProcClass(WriteNoteActivity.this).sleepNoteSave(mparams, new InterfaceSaveNoteCallBack() {

			@Override
			public void onSuccess(String icode, String strSuccMsg) {
				//					relBookcover.startAnimation(animSmarApear);
				//					relNoteWrite.startAnimation(animBigerDiasapear);

				/*if(OPEN_STATE == OPEN_STATE_HOME){
						closeAnim();
//						relBookcover.startAnimation(animSmarApear);
//						relNoteWrite.startAnimation(animBigerDiasapear);
						relBookcover.setVisibility(View.VISIBLE);
						relNoteWrite.setVisibility(View.GONE);
						title.startAnimation(appearAnim);
						rightBtn.startAnimation(appearAnim);
						title.setText("记事");
						rightBtn.setText("历史");
					}else{
						pullrView.autoRefresh();
//						dataPage = 1;
//						getNotesList(dataPage, true);
						relNoteWrite.setVisibility(View.GONE);
						relNoteList.setVisibility(View.VISIBLE);
						rightBtn.setVisibility(View.VISIBLE);
						title.startAnimation(appearAnim);
						rightBtn.startAnimation(appearAnim);
						title.setText("历史");
						rightBtn.setText("删除");
					}*/
				
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
	
	/**
	 * 获取今天的记事内容
	 */
	private void getTodayNote(final boolean isfirst){
		
		new XiangchengMallProcClass(WriteNoteActivity.this).sleepNoteNow(PreManager.instance().getUserId(WriteNoteActivity.this), new InterfaceSleepNoteNowCallBack() {
			
			@Override
			public void onSuccess(String icode, String text) {
				if(isfirst){
					rlBookContent.setVisibility(View.VISIBLE);
					bookFace.setVisibility(View.VISIBLE);
					bookFaceBack.setVisibility(View.VISIBLE);
					if(TextUtils.isEmpty(text)){
						bookFace.setImageResource(R.drawable.notebook_face1);
					}else{
						bookFace.setImageResource(R.drawable.notebook_face2);
						isTodayNoted = true;
					}
				}else{
					if(relNoteWrite.getVisibility() == View.VISIBLE){
						todayNote = text.concat(edtNote.getText().toString());
						edtNote.setText(todayNote);
						Editable etext = edtNote.getText();
						Selection.setSelection(etext, etext.length());
					}else{
						todayNote = text;
					}
				}
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				if(isfirst){
					rlBookContent.setVisibility(View.VISIBLE);
					bookFace.setVisibility(View.VISIBLE);
					bookFaceBack.setVisibility(View.VISIBLE);
					}
			}
		});
	}
	
	/**
	 * 获取记事本列表
	 */
	private void getNotesList(final int page, final boolean isRefresh){
		LogUtil.d("chen", "获取历史数据");
		new XiangchengMallProcClass(WriteNoteActivity.this).sleepNoteList(PreManager.instance().getUserId(WriteNoteActivity.this), 
			page+"" ,"10", new InterfaceSleepNoteListCallBack() {
			
			@Override
			public void onSuccess(String icode, int totalPage, List<NoteBean> datas) {
				pullrView.finishRefresh();
				WriteNoteActivity.this.totalPage = totalPage;
				if(isRefresh){
					mNoteList = datas;
				}else{
					mNoteList.addAll(datas);
				}
				mAdapter.SetData(mNoteList);
				if(mNoteList.size() == 0){
					pullrView
					.addListViewState(mListView, Constant.NO_DATA);
				}else if(page == totalPage){
					pullrView.addListViewState(mListView, Constant.NO_MORE);
				}
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				pullrView.finishRefresh();
				
			}
		});
	}
	/**
	 * 删除指定id的notes
	 * @param idList
	 */
	private void deleteNotes(String idList){
		new XiangchengMallProcClass(WriteNoteActivity.this).sleepNoteDel(PreManager.instance().getUserId(WriteNoteActivity.this), idList, new InterfaceSleepNoteDelCallBack() {
			
			@Override
			public void onSuccess(String icode, String strSuccMsg) {
				LogUtil.d("chen", strSuccMsg);
				
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				LogUtil.d("chen", strErrMsg);
				
			}
		});
	}

}
