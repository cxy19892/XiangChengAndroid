package com.yzm.sleep.wxapi;


import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelbase.BaseResp.ErrCode;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yzm.sleep.R;
import com.yzm.sleep.wxpay.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, OnClickListener{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
    
    private ImageView resImg;
    private TextView resTitle,resContent, title;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paysuccessful);
        initViews();
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

	private void initViews() {
		resImg = (ImageView) findViewById(R.id.res_img);
		resTitle = (TextView) findViewById(R.id.res_title);
		resContent = (TextView) findViewById(R.id.res_context);
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("支付结果");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
//		Toast.makeText(WXPayEntryActivity.this, "errCode = " + resp.errCode + "errMsg = " + resp.errStr, Toast.LENGTH_SHORT).show();
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == 0){
				Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				resImg.setVisibility(View.VISIBLE);
				resTitle.setText("您已购买成功");
				resContent.setText("客服将会与您取得沟通相关事宜");
			}else if(resp.errCode == ErrCode.ERR_USER_CANCEL){
				resImg.setVisibility(View.INVISIBLE);
				resTitle.setText("支付失败");
				resContent.setText("您取消了支付操作");
			}else{
				resImg.setVisibility(View.INVISIBLE);
				resTitle.setText("支付失败");
				resContent.setText(getString(R.string.pay_result_callback_msg, resp.errStr +"."));
//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setTitle("提示");
//				builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errStr +"."));
//				builder.show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back){
			finish();
		}
		
	}
}