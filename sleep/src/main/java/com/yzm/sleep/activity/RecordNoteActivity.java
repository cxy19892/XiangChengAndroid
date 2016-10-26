package com.yzm.sleep.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.NoteRecordAdapter;
import com.yzm.sleep.adapter.NoteRecordAdapter.NotesClickInterface;
import com.yzm.sleep.bean.NoteBean;
import com.yzm.sleep.refresh.MaterialRefreshLayout;
import com.yzm.sleep.refresh.MaterialRefreshListener;
import com.yzm.sleep.refresh.OnClickRequestListener;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepNoteDelCallBack;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSleepNoteListCallBack;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 记录心事
 * 
 * @author Administrator
 *
 */
public class RecordNoteActivity extends BaseActivity {
	private MaterialRefreshLayout myRefresh;
	private NoteRecordAdapter mAdapter;
	private ListView mLisView;
	private int dataPage =1, totalPage = 3;
	private TextView title;
	private LinearLayout Header;
	private RelativeLayout headerRel, titleRel, lineRel;
	private ImageView addImg;
	private ImageButton backButton;
	
	private List<NoteBean> mNoteList= new ArrayList<NoteBean>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.record_note_activity);
		initviews();
	}
	
	private void initviews(){
		backButton = (ImageButton) findViewById(R.id.back);
		backButton.setOnClickListener(this);
		title = ((TextView)findViewById(R.id.title));
		title.setTextColor(getResources().getColor(R.color.onet_color));
		titleRel = (RelativeLayout) findViewById(R.id.rel_title_lay);
		lineRel = (RelativeLayout) findViewById(R.id.header_rel_line);
		Header = (LinearLayout) findViewById(R.id.header);
		myRefresh = (MaterialRefreshLayout) findViewById(R.id.my_refresh_layout);
		mLisView = (ListView) findViewById(R.id.my_listv);
		mAdapter = new NoteRecordAdapter(this, mNotesClickInterface);
		headerRel = (RelativeLayout) findViewById(R.id.header_rel);
		addImg = (ImageView) findViewById(R.id.img_add_new);
		addImg.setOnClickListener(this);
		title.setText("思绪清零");
		
		RelativeLayout.LayoutParams relp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Constant.screenHeight/3);
		Header.setLayoutParams(relp1);
//		mLisView.addHeaderView(Header, null, false);
		mLisView.setAdapter(mAdapter);
		myRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				if(Util.checkNetWork(RecordNoteActivity.this)){
					dataPage = 1;
					getNotesList(dataPage, true);
					myRefresh.finishRefreshing();
				}else{
					myRefresh.addListViewState(mLisView, Constant.NO_NET);
					myRefresh.finishRefreshing();
				}
			}
		});
		
		mLisView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int lastItem = firstVisibleItem + visibleItemCount; 
	            if(lastItem == totalItemCount) { 
	            	if(mLisView.getChildCount()>0){
	            		View lastItemView=(View) mLisView.getChildAt(mLisView.getChildCount()-1); 
		                if ((mLisView.getBottom())==lastItemView.getBottom()) { 
		                	if (dataPage < totalPage) {
		    					dataPage += 1;
		    					getNotesList(dataPage, false);
		    					
		    				} 
		                } 
	            	}
	            } 
			}
		});
		
		myRefresh.setOnClickRequestListener(new OnClickRequestListener() {
			
			@Override
			public void setRequest() {
				myRefresh.autoRefresh();
			}
		});
		
		if(checkNetWork(this))
			myRefresh.autoRefresh();
		else{
			myRefresh
			.addListViewState(mLisView, Constant.NO_NET);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.img_add_new:
			Intent intent = new Intent(RecordNoteActivity.this, NoteRecordingActivity.class);
			startActivityForResult(intent, 100);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 100 && resultCode == RESULT_OK){
			myRefresh.autoRefresh();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private NotesClickInterface mNotesClickInterface = new NotesClickInterface() {
		
		@Override
		public void DeletNotes(NoteBean mNoteBean) {
			List<NoteBean> getList = mAdapter.getDate();
			List<NoteBean> tempList = new ArrayList<NoteBean>();
			for (NoteBean iter : getList) {
				if(!(iter.getNoteid().equals(mNoteBean.getNoteid()))){
					tempList.add(iter);
				}
			}
			mAdapter.setData(tempList);
			deleteNotes(mNoteBean.getNoteid());
		}
	};

	private void getNotesList(final int page, final boolean isRefresh){
		if(checkNetWork(this)){
		new XiangchengMallProcClass(this).sleepNoteList(PreManager.instance().getUserId(this), 
			page+"" ,"10", new InterfaceSleepNoteListCallBack() {
			
			@Override
			public void onSuccess(String icode, int totalPage, List<NoteBean> datas) {
				myRefresh.finishRefreshing();
				RecordNoteActivity.this.totalPage = totalPage;
				if(isRefresh){
					mNoteList = datas;
				}else{
					mNoteList.addAll(datas);
				}
				mAdapter.setData(mNoteList);
				if(mNoteList.size() == 0){
					lineRel.setVisibility(View.GONE);
					myRefresh
					.addListViewState(mLisView, Constant.NO_DATA);
				}else{
					lineRel.setVisibility(View.VISIBLE);
					if(page == totalPage){
						myRefresh.addListViewState(mLisView, -1);
					}
				} 
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				myRefresh.finishRefreshing();
				
			}
		});
		}else{
			myRefresh.addListViewState(mLisView, Constant.NO_NET);
			myRefresh.finishRefreshing();
		}
	}
	/**
	 * 删除指定id的notes
	 * @param idList
	 */
	private void deleteNotes(final String idList){
		new XiangchengMallProcClass(this).sleepNoteDel(PreManager.instance().getUserId(this), idList, new InterfaceSleepNoteDelCallBack() {
			
			@Override
			public void onSuccess(String icode, String strSuccMsg) {
				
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				LogUtil.d("chen", strErrMsg);
				
			}
		});
	}

}
