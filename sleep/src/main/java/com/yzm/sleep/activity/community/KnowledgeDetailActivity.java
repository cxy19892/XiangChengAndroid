package com.yzm.sleep.activity.community;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.activity.ShareActivity;
import com.yzm.sleep.bean.KnowledgeBean;
import com.yzm.sleep.bean.ShareClassParam;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceKnowledgeDetailCallBack;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.URLUtil;
import com.yzm.sleep.utils.XiangchengMallProcClass;

/**
 * 
 * @author 
 * 知识详情  传入参数 String object_id 文章ID
 */
public class KnowledgeDetailActivity extends BaseActivity {
	
	private Button shareBtn;
	private WebView mWebView;
	private TextView mTitle, title;
	private String object_id;
	private KnowledgeBean mknowledge;
	private String picUrl;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_knowledge_detail);
		initViews();
	}

	private void initViews() {
		object_id = getIntent().getStringExtra("object_id");
		picUrl    = getIntent().getStringExtra("pic_url");
		title = (TextView) findViewById(R.id.title);
		findViewById(R.id.back).setOnClickListener(this);
		shareBtn = (Button) findViewById(R.id.btn_title_right);
		mTitle = (TextView) findViewById(R.id.topice_title);
		mWebView = (WebView) findViewById(R.id.topice_webpage);
		Drawable drawable = getResources().getDrawable(R.drawable.share_new_icon);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		shareBtn.setCompoundDrawables(drawable, null, null, null);
		shareBtn.setVisibility(View.VISIBLE);
		shareBtn.setOnClickListener(this);
		if(TextUtils.isEmpty(object_id)){
			toastMsg("获取失败");
			AppManager.getAppManager().finishActivity(KnowledgeDetailActivity.this);
		}else{
			getDetailInfo(object_id);
		}
	}
	
	private void getDetailInfo(String object_id){
		new XiangchengMallProcClass(this).getKnowledgeDetail(PreManager.instance().getUserId(this), object_id, new InterfaceKnowledgeDetailCallBack() {
			
			@Override
			public void onSuccess(String icode, KnowledgeBean knowledge) {
				mknowledge = knowledge;
				title.setText(knowledge.getPost_title());
				mTitle.setText(knowledge.getPost_title());
				initWebView(knowledge.getPost_content());
			}
			
			@Override
			public void onError(String icode, String strErrMsg) {
				
				
			}
		});
	}
	
	@SuppressLint("SetJavaScriptEnabled") private void initWebView(String htm) {
		WebSettings setting = mWebView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setSupportZoom(false);
		setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		setting.setRenderPriority(RenderPriority.HIGH); //提高渲染速度
		setting.setBlockNetworkImage(false); //将图片渲染放在渲染完成之后
		mWebView.loadDataWithBaseURL(null, htm, "text/html", "UTF-8", null);
//		mWebView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
		mWebView.setWebViewClient(new CustomWebClient());
		mWebView.setVisibility(View.VISIBLE);
	}
	
	@SuppressLint("SetJavaScriptEnabled") class CustomWebClient extends WebViewClient{
		public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            return super.shouldOverrideUrlLoading(view, url);  
        }  
  
        @Override  
        public void onPageFinished(WebView view, String url) {  
            view.getSettings().setJavaScriptEnabled(true);  
            super.onPageFinished(view, url);
           
        }  
  
        @Override  
        public void onPageStarted(WebView view, String url, Bitmap favicon) {  
            view.getSettings().setJavaScriptEnabled(true);  
            super.onPageStarted(view, url, favicon);  
        }  
  
        @Override  
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
            super.onReceivedError(view, errorCode, description, failingUrl);  
        }  
	}
	
	 // js通信接口  
//    private class JavascriptInterface {  
//        public JavascriptInterface(Context context) {  
//        }  
//  
//        @android.webkit.JavascriptInterface
//        public void openImage(String img) { 
//        }  
//        
//        @android.webkit.JavascriptInterface
//        public void imagsUrl(String img) {  
////        	if(imagesUrl==null){
////        		imagesUrl=new ArrayList<String>();
////        	}
////        	imagesUrl.add(img);
//        }  
//    } 

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back:
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.btn_title_right:
			if(mknowledge != null){//http://apporange.cn/orangesleep_debug/share/hd/knowdetail.php?object_id=211
			String title= mknowledge.getPost_title();
			String url=URLUtil.BASEURL+"/share/hd/knowdetail.php?object_id="+object_id;
			String intro=mknowledge.getPost_excerpt();
			String summary=intro.length() > 40 ? intro.substring(0,40) :intro;
			
			Intent intent = new Intent(this,ShareActivity.class);
			ShareClassParam param = new ShareClassParam();
			param.targetUrl = url;
			param.shareTitle = title;
			param.shareSummary = summary;
			if(picUrl!=null && !picUrl.isEmpty())
				param.sharePictureUrl = picUrl;
			else
				param.sharePictureUrl = "";
			intent.putExtra("from", Constant.SHARE_FROM_COMMUNITY_EVENT);
			intent.putExtra("shareData", param);
			startActivity(intent);
			}
			break;
		}
	}
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	

}
