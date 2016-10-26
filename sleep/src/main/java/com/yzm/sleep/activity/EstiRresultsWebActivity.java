package com.yzm.sleep.activity;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.community.CommunityTopiceDetailActivity;
import com.yzm.sleep.activity.community.ProgramDetailsActivity;
import com.yzm.sleep.activity.community.TeamDetailsActivity;
import com.yzm.sleep.adapter.PicAndImgAdapter;
import com.yzm.sleep.adapter.PushListAdapter;
import com.yzm.sleep.bean.BePushBean;
import com.yzm.sleep.bean.CommunityGroupBean;
import com.yzm.sleep.bean.EvaluationLawBean;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceEstimateCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.CustomGridView;
import com.yzm.sleep.widget.SimpleRoundProgressBar;

/**
 * 评估结果页面
 * @author chen
 *
 */
public class EstiRresultsWebActivity extends BaseActivity implements OnItemClickListener {

	private View headView;
	private TextView footerView;
	private ListView mListView;
	private LinearLayout viewOther, viewData;
    private View loadingView, noNetView;
    private String Type;
    private String title;
//    private Button titleRBtn;
    private PushListAdapter madapter;
    private EstiRresultsWebActivity activity;
    private LinearLayout EstiLinType1,EstiLinType2,EstiLinType3,EstiLinType4;// 
	private TextView resaultToptips, scoresTv, tvContent ,listviewTips;//
//	private SimpleRoundProgressBar bar;
	private CustomGridView mGridview1, mGridview3,mGridview4;
//	private ProgressBar /*pbar1,*/ pbar2/*, pbar3*/;
	private TextView pbarTv1, pbarTv2, pbarTv3, pbarResauTv1, pbarResauTv2, pbarResauTv3;
	private PicAndImgAdapter picAdapter;
	private TextView pbarv1, pbarv2;
	private RelativeLayout relBar1, relBar2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_esti_results);
		activity = this;
		initView();
        loadingView = getLayoutInflater().inflate(R.layout.listview_loading,
                  null);
        noNetView = getLayoutInflater().inflate(R.layout.layout_no_net, null);
        viewOther.addView(loadingView);
        viewOther.addView(noNetView);
        mListView.addFooterView(footerView,null,false);
        mListView.addHeaderView(headView, null, false);
        viewOther.setVisibility(View.VISIBLE);
        noNetView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        noNetView.setOnClickListener(new OnClickListener() {
            
             @Override
             public void onClick(View v) {
                  loadingView.setVisibility(View.VISIBLE);
                  noNetView.setVisibility(View.GONE);
                  getData();
             }
        });
        madapter = new PushListAdapter(this);
        mListView.setAdapter(madapter);
        mListView.setOnItemClickListener(this);
	}
	
	private void initView(){
		@SuppressWarnings("static-access")
		LayoutInflater inflater = getLayoutInflater().from(this);
        headView = inflater.inflate(R.layout.esti_results_web_header, null);
        footerView = new TextView(activity);
     // 第一个参数为宽的设置，第二个参数为高的设置。
        footerView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
        		30));
        mListView = (ListView) findViewById(R.id.esti_listview);
        viewOther = (LinearLayout) findViewById(R.id.esti_viewOther);
        viewData = (LinearLayout) headView.findViewById(R.id.esti_viewData);
        EstiLinType1 = (LinearLayout) headView.findViewById(R.id.esti_lin_type1);
        EstiLinType2 = (LinearLayout) headView.findViewById(R.id.esti_lin_type2);
        EstiLinType3 = (LinearLayout) headView.findViewById(R.id.esti_lin_type3);
        EstiLinType4 = (LinearLayout) headView.findViewById(R.id.esti_lin_type4);
        resaultToptips= (TextView) headView.findViewById(R.id.esti_result_top_tips);
        scoresTv = (TextView) headView.findViewById(R.id.esti_tv_scor);
        tvContent= (TextView) headView.findViewById(R.id.esti_tv_res_content);
//        bar = (SimpleRoundProgressBar) headView.findViewById(R.id.esti_progress);
        mGridview1 = (CustomGridView) headView.findViewById(R.id.esti_gview_type1);
        mGridview3 = (CustomGridView) headView.findViewById(R.id.esti_gview_type3);
        mGridview4 = (CustomGridView) headView.findViewById(R.id.esti_gview_type4);
//        pbar1 = (ProgressBar) headView.findViewById(R.id.pb_smxl_realy);
//        pbar2 = (ProgressBar) headView.findViewById(R.id.pb_smsc_realy);
        pbarv1 = (TextView) headView.findViewById(R.id.pb_v_smxl_realy);
        relBar1= (RelativeLayout) headView.findViewById(R.id.rel_smxl_realy);
        pbarv2 =  (TextView) headView.findViewById(R.id.pb_v_smsc_realy);
        relBar2= (RelativeLayout) headView.findViewById(R.id.rel_smsc_realy);
//        pbar3 = (ProgressBar) headView.findViewById(R.id.pb_rssj_realy);
        pbarTv1 = (TextView) headView.findViewById(R.id.tv_smxl_realy);
        pbarTv2 = (TextView) headView.findViewById(R.id.tv_smsc_realy);
        pbarTv3 = (TextView) headView.findViewById(R.id.tv_rssj_realy);
        pbarResauTv1 = (TextView) headView.findViewById(R.id.tv_smxl_result);
        pbarResauTv2 = (TextView) headView.findViewById(R.id.tv_smsc_result);
        pbarResauTv3 = (TextView) headView.findViewById(R.id.tv_rssj_result);
        listviewTips = (TextView) headView.findViewById(R.id.esti_result_tips);
        Type = getIntent().getStringExtra("type");
		if("1".equals(Type)){
			title = "生活习惯";
			EstiLinType1.setVisibility(View.VISIBLE);
			EstiLinType2.setVisibility(View.GONE);
			EstiLinType3.setVisibility(View.GONE);
			EstiLinType4.setVisibility(View.GONE);
			picAdapter = new PicAndImgAdapter(activity, getScreenWidth(), 4);
			
		}else if("2".equals(Type)){
			title = "睡眠规律";
			EstiLinType1.setVisibility(View.GONE);
			EstiLinType2.setVisibility(View.VISIBLE);
			EstiLinType3.setVisibility(View.GONE);
			EstiLinType4.setVisibility(View.GONE);
			
		}else if("3".equals(Type)){
			title = "卧室环境";
			EstiLinType1.setVisibility(View.GONE);
			EstiLinType2.setVisibility(View.GONE);
			EstiLinType3.setVisibility(View.VISIBLE);
			EstiLinType4.setVisibility(View.GONE);
			picAdapter = new PicAndImgAdapter(activity, getScreenWidth(), 3);
		}else if("4".equals(Type)){
			title = "心理活动";
			EstiLinType1.setVisibility(View.GONE);
			EstiLinType2.setVisibility(View.GONE);
			EstiLinType3.setVisibility(View.GONE);
			EstiLinType4.setVisibility(View.VISIBLE);
			picAdapter = new PicAndImgAdapter(activity, getScreenWidth(), 3);
		}
		if(title != null){
			((TextView)findViewById(R.id.title)).setText(title);
			resaultToptips.setText("您的"+title);	
		}
//		titleRBtn = (Button) findViewById(R.id.btn_title_right);
//		titleRBtn.setVisibility(View.VISIBLE);
//		titleRBtn.setText("重评");
//		titleRBtn.setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
	}
	
	private void getData(){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				getEstiData(Type);
			}
		}, 300);
	}
	
	

	
	private void getEstiData(String type){
		if(checkNetWork(activity)){
			new XiangchengMallProcClass(this).getEstimateResult(PreManager.instance().getUserId(this), type, new InterfaceEstimateCallback() {

				@Override
				public void onSuccess(int icode, JSONObject response) {
					if("1".equals(Type) && !TextUtils.isEmpty(response.toString())){
						PreManager.instance().saveJSON_SHXGPGResult(activity, response.toString());
					}else if("2".equals(Type)  && !TextUtils.isEmpty(response.toString()) ){
						PreManager.instance().saveJSON_SMGLPGResult(activity, response.toString());
					}else if("3".equals(Type) && !TextUtils.isEmpty(response.toString())){
						PreManager.instance().saveJSON_SMHJPGResult(activity, response.toString());
					}else if("4".equals(Type) && !TextUtils.isEmpty(response.toString())){
						PreManager.instance().saveJSON_XLHDPGResult(activity, response.toString());
					}
					refreshData();
				}

				@Override
				public void onError(int icode, String strErrMsg) {
					toastMsg(strErrMsg);
					refreshData();
				}
			});
		}else{
			refreshData();
		}
	}
	
	private void refreshData(){
		String response = "";
		if("1".equals(Type)){
			response = PreManager.instance().getJSON_SHXGPGRsult(activity);
		}else if("2".equals(Type)){
			response = PreManager.instance().getJSON_SMGLPGRsult(activity);
		}else if("3".equals(Type)){
			response = PreManager.instance().getJSON_SMHJPGRsult(activity);
		}else if("4".equals(Type)){
			response = PreManager.instance().getJSON_XLHDPGRsult(activity);
		}
		if("".equals(response)){
			viewOther.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			noNetView.setVisibility(View.VISIBLE);
		}else{
		try {
			JSONObject json=new JSONObject(response);
			Gson gson = new Gson();
			String pgresult = json.getString("pgresult");
			String pgpoint = json.getString("pgpoint");
			String pgmessage = json.getString("pgmessage");
			EvaluationLawBean mEva = new EvaluationLawBean();
			try {
				JSONObject Evaluation  = json.getJSONObject("pg_fenxi");
				mEva.setPgshichang(Evaluation.getString("pgshichang"));
				mEva.setPgshichang_r(Evaluation.getString("pgshichang_r"));
				mEva.setPgsleep(Evaluation.getString("pgsleep"));
				mEva.setPgsleep_r(Evaluation.getString("pgsleep_r"));
				mEva.setPgxiaol(Evaluation.getString("pgxiaol"));
				mEva.setPgxiaolv_r(Evaluation.getString("pgxiaol_r"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			String publishsString = (String)json.getString("xgpush_list");
			List<BePushBean> mlist = gson.fromJson(publishsString,new TypeToken<List<BePushBean>>() {
					}.getType());
			String picturs = (String)json.getString("picture_list");
			List<BePushBean> mpiclist = gson.fromJson(picturs,new TypeToken<List<BePushBean>>() {
			}.getType());
			
			loadingView.setVisibility(View.GONE);
			viewData.setVisibility(View.VISIBLE);
			viewOther.setVisibility(View.GONE);
			setViews(pgresult, pgpoint, pgmessage, mEva, mlist, mpiclist);
		} catch (Exception e) {
			e.printStackTrace();
			viewOther.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			noNetView.setVisibility(View.VISIBLE);
		}
		}
	}
	
	private void setViews(String pgresult, String pgpoint, String pgmessage, EvaluationLawBean evalus, 
			List<BePushBean> mpushlist, List<BePushBean> mpiclist){
		scoresTv.setText(pgresult);
		tvContent.setText(pgmessage);
		
//		bar.setRoundColor(getResources().getColor(R.color.color_7d7d90));
//		bar.setRoundProgressColor(getResources().getColor(R.color.color_8745ff));
//		bar.setMax(100);
//		bar.setRoundWidth(8);
//		bar.setProgress("100".equals(pgpoint)?100:0);//pgpoint
		
		if(Type.equals("2") && evalus != null){
			PreManager.instance().saveSMGLPGResult(activity, pgresult);
			int persent1 = 0;
			int sleeplenth = 240;
			try {
				float persenttemp1 = Float.parseFloat(evalus.getPgxiaol()) * 100;
				
				BigDecimal b = new BigDecimal(persenttemp1);  
				persent1 = (int) b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();  
//				persent1 = (int)(Float.parseFloat(evalus.getPgxiaol()) * 100);
				sleeplenth = Integer.parseInt(evalus.getPgshichang());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final int per1 = persent1;
//			pbar1.setProgress(persent1);
//			pbarv1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT));
			relBar1.post(new Runnable() {
				
				@Override
				public void run() {
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.MATCH_PARENT);
					lp.width = relBar1.getWidth()*per1/100;
					pbarv1.setLayoutParams(lp);
					pbarTv1.setText(per1+"%");
				}
			});
			
			if(persent1 == 0){
				pbarv1.setText("");
				pbarTv1.setVisibility(View.VISIBLE);
			}else if(pbarv1.getWidth() < pbarTv1.getWidth()){
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.MATCH_PARENT);
				pbarv1.setLayoutParams(lp);
				pbarv1.setText(persent1+"%");
				pbarv1.setPadding(0, 0, 5, 0);
				pbarTv1.setVisibility(View.GONE);
			}else{
				pbarv1.setText(persent1+"%");
				pbarv1.setPadding(0, 0, 5, 0);
				pbarTv1.setVisibility(View.GONE);
			}
			
			
//			if(persent1 > 15){
//				pbarv1.setText(persent1+"%");
//				pbarv1.setPadding(0, 0, 5, 0);
//				pbarTv1.setVisibility(View.GONE);
//			}else{
//				pbarv1.setText("");
//				pbarTv1.setVisibility(View.VISIBLE);
//			}
			
			pbarResauTv1.setText(evalus.getPgxiaolv_r());
			int persent2 = 0;
			persent2 = (int)((sleeplenth-240)/480f *100);
			if(persent2 > 100){
				persent2 = 100;
			}else if(persent2 < 0){
				persent2 = 0;
			}
			final int per = persent2;
//			pbar2.setProgress(persent2);
			
			pbarResauTv2.setText(evalus.getPgshichang_r());
			
			relBar2.post(new Runnable() {
				
				@Override
				public void run() {
					RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.MATCH_PARENT);
					
					lp2.width = relBar2.getWidth() * per / 100;
					pbarv2.setLayoutParams(lp2);
				}
			});
			
			if(sleeplenth == 0){
				pbarv2.setText("");
				pbarv2.setVisibility(View.GONE);
				pbarTv2.setVisibility(View.VISIBLE);
				pbarTv2.setText(sleeplenth/60+"小时"+sleeplenth%60+"分");
			}else{			
				if(persent2 > 25){
					pbarv2.setPadding(0, 0, 5, 0);
					pbarv2.setText(sleeplenth/60+"小时"+sleeplenth%60+"分");
					pbarv2.setVisibility(View.VISIBLE);
					pbarTv2.setVisibility(View.GONE);
				}else{
					pbarv2.setVisibility(View.GONE);
					pbarTv2.setVisibility(View.VISIBLE);
					pbarTv2.setText(sleeplenth/60+"小时"+sleeplenth%60+"分");
					pbarTv2.setBackgroundResource(R.drawable.corners_btn_color_theme);
					pbarTv2.setPadding(5, 0, 5, 0);
				}
			}
			
			pbarTv3.setText(evalus.getPgsleep());
			pbarResauTv3.setText(evalus.getPgsleep_r());
		}else if(Type.equals("1")){
			PreManager.instance().saveSHXGPGResult(activity, pgresult);
			mGridview1.setAdapter(picAdapter);
			picAdapter.setData(mpiclist);
		}else if(Type.equals("3")){
			PreManager.instance().saveSMHJPGResult(activity, pgresult);
			mGridview3.setAdapter(picAdapter);
			picAdapter.setData(mpiclist);
		}else if(Type.equals("4")){
			PreManager.instance().saveXLHDPGResult(activity, pgresult);
			mGridview4.setAdapter(picAdapter);
			picAdapter.setData(mpiclist);
		}
		if(mpushlist.size()==0){
			listviewTips.setVisibility(View.GONE);
		}else{
			listviewTips.setVisibility(View.VISIBLE);
		}
		madapter.setData(mpushlist);
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().finishActivity();
			return true;
		}else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
//		case R.id.btn_title_right:
//			Intent intentRedid = new Intent(EstiRresultsWebActivity.this, EstimateWebActivity.class);
//			intentRedid.putExtra("type", Type);
//			startActivity(intentRedid);
//			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int index = position - mListView.getHeaderViewsCount();
        if(index >= 0){
        	BePushBean bean = madapter.getData().get(index);
        	// 1活动详情 ; 2话题详情  ;  3文章详情 ; 4 小组 
        	Intent intent = null;
        	if("1".equals(bean.getType())){
        		intent = new Intent(activity,
						ProgramDetailsActivity.class);
				intent.putExtra("id", bean.getTjid());
				intent.putExtra("title", bean.getTitle());
        	}else if("2".equals(bean.getType())){
        		intent = new Intent(activity, CommunityTopiceDetailActivity.class);
        		intent.putExtra("topices_id", bean.getTjid());
        		intent.putExtra("topices_title", "");
        		intent.putExtra("is_choiceness", false);
        	}else if("3".equals(bean.getType())){
        		intent = new Intent(activity, CommunityTopiceDetailActivity.class);
        		intent.putExtra("topices_id", bean.getTjid());
        		intent.putExtra("topices_title", "");
        		intent.putExtra("is_choiceness", true);
        	}else if("4".equals(bean.getType())){
        		intent= new Intent(activity, TeamDetailsActivity.class);
				
				CommunityGroupBean mCommunityGroupBean = new CommunityGroupBean();
				mCommunityGroupBean.setIscode(true);
				mCommunityGroupBean.setGid(bean.getTjid());
				intent.putExtra("bean", mCommunityGroupBean);
        	}
        	
        	if(intent!= null){
        		startActivity(intent);
        	}
        }
		
	}

	
	
}
