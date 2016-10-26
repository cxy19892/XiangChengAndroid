package com.yzm.sleep.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.utils.InterfaceMallUtillClass;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/4/7.
 */
public class FeedBackActivity extends BaseActivity {

    private EditText edtSuggest, editConn;
    private Button commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        findViewById(R.id.back).setOnClickListener(this);
        edtSuggest = (EditText) findViewById(R.id.edt_sugg);
        editConn   = (EditText) findViewById(R.id.edt_conn);

        ((TextView)findViewById(R.id.title)).setText("意见反馈");
        commit = (Button)findViewById(R.id.btn_title_right);
        commit.setText("提交");
        commit.setVisibility(View.VISIBLE);
        commit.setOnClickListener(this);

        edtSuggest.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                AppManager.getAppManager().finishActivity();
                break;

            case R.id.btn_title_right:
                if(!TextUtils.isEmpty(edtSuggest.getText().toString().trim())){
                    if(!TextUtils.isEmpty(editConn.getText().toString().trim())){
                        commtFeedBack();
                    }else{
                        toastMsg("联系方式不能为空");
                    }
                }else{
                    toastMsg("内容不能为空");
                }
                break;

        }
    }

    private void commtFeedBack(){
        InterfaceMallUtillClass.FeedBackParam par = new InterfaceMallUtillClass.FeedBackParam();
        par.contact = editConn.getText().toString();
        par.fankui = edtSuggest.getText().toString();
        par.fenbianl = getdisplay();
        par.my_int_id= TextUtils.isEmpty(PreManager.instance().getUserId(this))?"游客":PreManager.instance().getUserId(this);
        par.network  = Util.getNetType(this) == 1 ? "wifi":"手机网络";
        par.phonever = android.os.Build.MODEL;
        par.version  = getVersion();
        par.romversion = android.os.Build.VERSION.RELEASE;
        par.yunying  = getSimOperatorName();
        par.platform = "Andriod";
        new XiangchengMallProcClass(this).feedBack(par, new InterfaceMallUtillClass.InterfacFeedBackCallBack() {
            @Override
            public void onSuccess(String icode, String flag) {
                toastMsg("提交成功");
                AppManager.getAppManager().finishActivity();
            }

            @Override
            public void onError(String icode, String strErrMsg) {
                toastMsg(strErrMsg);
            }
        });
    }



    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "--";
        }
    }

    private String getSimOperatorName() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String operator = telManager.getSimOperator();

        if (operator != null) {

            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                return "中国移动";
            } else if (operator.equals("46001")) {
                return "中国联通";
            } else if (operator.equals("46003")) {
                return "中国电信";
            }
        }
        return "未知运营商";
    }

    private String getdisplay(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        android.view.Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);

        int width=dm.widthPixels;//*dm.density;
        int height=dm.heightPixels;//*dm.density;

        return width + " x " + height;

    }
}
