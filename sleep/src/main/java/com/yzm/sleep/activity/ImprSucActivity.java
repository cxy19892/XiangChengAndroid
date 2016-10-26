package com.yzm.sleep.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;

/**
 * Created by Administrator on 2016/3/14.
 */
public class ImprSucActivity extends BaseActivity{

    private TextView sleepTime, getupTime, sleepXl;
    private Button commit;
    private String sleepStr, getupStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impr_suc);
        sleepTime = (TextView)findViewById(R.id.tv_sleep_time);
        getupTime = (TextView)findViewById(R.id.tv_getup_time);
        sleepXl = (TextView)findViewById(R.id.tv_sleep_xl);
        commit  = (Button)findViewById(R.id.commit_btn);
        sleepStr = getIntent().getStringExtra("sleep");
        getupStr = getIntent().getStringExtra("getup");

        sleepTime.setText(sleepStr);
        getupTime.setText(getupStr);
        sleepXl.setText("85%");
        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == commit){
            AppManager.getAppManager().finishActivity(ImprSucActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
