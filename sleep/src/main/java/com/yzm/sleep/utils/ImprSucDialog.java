package com.yzm.sleep.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.Constant;
import com.yzm.sleep.R;

/**
 * Created by Administrator on 2016/3/14.
 */
public class ImprSucDialog extends AlertDialog{

    private TextView sleepTime, getupTime, sleepXl, tvcontent;
    private Button commit;
    private String sleepStr, getupStr;

    public ImprSucDialog(Context context) {
        super(context);
    }

    public void setData(String sleepT, String getupT){
        this.sleepStr = sleepT;
        this.getupStr = getupT;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_impr_suc);
        setCanceledOnTouchOutside(false);
            getWindow().setLayout((int) (Constant.screenWidht * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
        sleepTime = (TextView)findViewById(R.id.tv_sleep_time);
        getupTime = (TextView)findViewById(R.id.tv_getup_time);
        sleepXl = (TextView)findViewById(R.id.tv_sleep_xl);
        tvcontent = (TextView)findViewById(R.id.tv_content);
        tvcontent.setMovementMethod(ScrollingMovementMethod.getInstance());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(Constant.screenWidht * 0.9) / 5, (int)(Constant.screenWidht * 0.9) / 5);

        sleepTime.setLayoutParams(lp);
        getupTime.setLayoutParams(lp);
        sleepXl.setLayoutParams(lp);

        commit  = (Button)findViewById(R.id.commit_btn);
        sleepTime.setText(sleepStr);
        getupTime.setText(getupStr);
        sleepXl.setText("85%");
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
