package com.yzm.sleep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.HomeDataBean;
import com.yzm.sleep.model.ChangplanBean;
import com.yzm.sleep.utils.InterfaceMallUtillClass;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.MyClock;
import com.yzm.sleep.widget.MyClockPro;
import com.yzm.sleep.widget.RulerView;

/**
 * Created by Administrator on 2016/3/15.
 */
public class ContinuePlansActivity extends BaseActivity{

    private TextView stateSuggest;
    private MyClockPro myClockPro;
    private Button nextBtn, lastBtn, notChange;
    private ChangplanBean changplanBean;
    private RelativeLayout relTime;
    private LinearLayout linLenth;
    private int state = 0;
    private ImageButton rightBtn;
    private RulerView mRulerView;
    private MyClock myclock;
    private String waketime;
    private String SleepTime;
    private int wakeValue = 32;
    private int lenthValue = 34;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_plans);
        changplanBean = (ChangplanBean)getIntent().getSerializableExtra("change");
        if(changplanBean != null) {
            waketime = changplanBean.getUp_wakeup();
            SleepTime = changplanBean.getUp_sleep();
            String[] arrwake = waketime.split(":");
            int w_hour = Integer.parseInt(arrwake[0]);
            int w_min  = Integer.parseInt(arrwake[1]);
            wakeValue = w_hour*4 + w_min/15 + (w_min%15 > 7 ? 1 :0);
        }
        initview();

    }

    private void initview(){
        findViewById(R.id.back).setOnClickListener(this);
        ((TextView)findViewById(R.id.title)).setText("续订计划");
        relTime = (RelativeLayout)findViewById(R.id.rel_time);
        linLenth= (LinearLayout)findViewById(R.id.rel_length);
        myClockPro = (MyClockPro)findViewById(R.id.my_clock_pro);
        stateSuggest = (TextView)findViewById(R.id.tv_plan_suggest);
        nextBtn = (Button) findViewById(R.id.btn_next);
        lastBtn = (Button) findViewById(R.id.btn_last);
        notChange = (Button) findViewById(R.id.btn_notchange);
        rightBtn = (ImageButton) findViewById(R.id.ib_right);
        mRulerView = (RulerView) findViewById(R.id.myrulerview);
        myclock = (MyClock) findViewById(R.id.myclock);
        nextBtn.setVisibility(View.GONE);
        lastBtn.setVisibility(View.GONE);
        notChange.setVisibility(View.GONE);
        nextBtn.setOnClickListener(this);
        lastBtn.setOnClickListener(this);
        notChange.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        initState0();
    }


    private void initState0(){
        relTime.setVisibility(View.VISIBLE);
        linLenth.setVisibility(View.GONE);
        rightBtn.setVisibility(View.VISIBLE);
        myclock.setTime(wakeValue / 4, (wakeValue % 4) * 15, true);
        mRulerView.setValueChangeListener(new RulerView.OnValueChangeListener() {

            @Override
            public void onValueChange(int value) {
                myclock.setTime(value / 4, (value % 4) * 15, true);
                waketime = (value / 4 >= 10 ? value / 4 : ("0" + (value / 4 == 0 ? "0" : value / 4))) + ":" + ((value % 4) * 15 > 0 ? (value % 4) * 15 : "00");
                wakeValue = value;
            }
        });
        mRulerView.setValue(96, 0, wakeValue, 1);
    }

    private void initState1(){
        relTime.setVisibility(View.GONE);
        linLenth.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.GONE);
        if(changplanBean == null){
            toastMsg("changplanBean == null");
            return;
        }
        int newminutes = Integer.parseInt(changplanBean.getSleeplong());
        int oldminutes = Integer.parseInt(changplanBean.getUp_sleeplong());
        int newhour = newminutes/60;
        int newmin  = newminutes%60;
        int oldhour = oldminutes/60;
        int oldmin  = oldminutes%60;
        lenthValue = newhour * 4 + newmin/15 + (newmin % 15 > 7 ? 1 : 0);
        length = oldhour * 4 + oldmin/15 + (oldmin % 15 > 7 ? 1 : 0);;

        if(changplanBean.getFlag().equals("1")){//增加
            nextBtn.setVisibility(View.VISIBLE);
            lastBtn.setVisibility(View.VISIBLE);
            notChange.setVisibility(View.GONE);

            stateSuggest.setText("上个计划中，您每天的睡眠效率都达到了85%，建议延长"+(lenthValue-length)*15+"分钟可睡时间，调整为"+ lenthValue/4+"小时"+(lenthValue%4) * 15 +"分钟");
            myClockPro.setChangeTime(((wakeValue - length) / 4), (wakeValue - length) % 4 * 15, (wakeValue / 4), wakeValue % 4 * 15, lenthValue - length , true);
        }else if(changplanBean.getFlag().equals("2")){//减少
            nextBtn.setVisibility(View.VISIBLE);
            lastBtn.setVisibility(View.VISIBLE);
            notChange.setVisibility(View.GONE);

            stateSuggest.setText("上个计划中，您有不少于4天的睡眠效率低于80%，建议将可睡时间缩短为" + lenthValue / 4 + "小时" + (lenthValue % 4) * 15 + "分钟");
            myClockPro.setChangeTime(((wakeValue - length) / 4), (wakeValue - length) % 4 * 15, (wakeValue / 4), wakeValue % 4 * 15, lenthValue - length , true);
        }else{//不调整
            nextBtn.setVisibility(View.GONE);
            lastBtn.setVisibility(View.GONE);
            notChange.setVisibility(View.VISIBLE);
            stateSuggest.setText("上个计划中，您没能实现每天睡眠效率都达到85%，无法延长可睡时间。请继续沿用上期计划，力争每日效率都达标。");//"在改善睡眠的过程中，一两天睡眠效率不达标很正常。请继续坚持改善计划，力争每日效率都达标");
            myClockPro.setChangeTime(((wakeValue - length) / 4), (wakeValue - length) % 4 * 15, (wakeValue / 4), wakeValue % 4 * 15, 0 , false);
        }



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                if(state == 1){
                    state = 0;
                    myClockPro.stopRunning();
                    initState0();
                }else{
                    AppManager.getAppManager().finishActivity();
                }
                break;

            case R.id.ib_right:
                if(state == 0){
                    state = 1;
                    initState1();
                }
                break;

            case R.id.btn_next://不调整
                lenthValue = length;
                setContinuePlan(changplanBean.getUp_sleeplong());
                break;

            case R.id.btn_last:

                setContinuePlan(changplanBean.getSleeplong());
                break;
            case R.id.btn_notchange:
                lenthValue = length;
                setContinuePlan(changplanBean.getSleeplong());
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        myClockPro.stopRunning();
        super.onDestroy();
    }


    private void setContinuePlan(String sleeplong){

        if(wakeValue - lenthValue < 0){
            int hour_ = (96 + (wakeValue - lenthValue))/4;
            int min_  = (96 + (wakeValue - lenthValue))%4 *15;
            SleepTime = (hour_ <10 ? "0"+hour_ : hour_) +  ":" + (min_ < 10 ? "0" + (min_==0?"0":min_) : min_);
        }else
            SleepTime = ((wakeValue - lenthValue)/4 < 10 ? "0" + (wakeValue - lenthValue)/4 : (wakeValue - lenthValue)/4) + ":"
                    + ((wakeValue - lenthValue)%4 * 15 < 10 ? "00" : (wakeValue - lenthValue)%4 * 15);
        PreManager.instance().saveSleepTime_Setting(
                this, SleepTime);
        PreManager.instance().saveGetupTime_Setting(this,waketime);


        new XiangchengMallProcClass(this).continuePlan(PreManager.instance().getUserId(this), "1", waketime, sleeplong, new InterfaceMallUtillClass.InterfaceContinuePlanCallBack() {
            @Override
            public void onSuccess(String icode, HomeDataBean dataBena) {
                sendBroadC();
                AppManager.getAppManager().finishActivity(ContinuePlansActivity.class);
                AppManager.getAppManager().finishActivity(SleepDataReportActivity.class);
                AppManager.getAppManager().finishActivity(PersonalActivity.class);
            }

            @Override
            public void onError(String icode, String strErrMsg) {
                toastMsg(strErrMsg);
            }
        });
    }


    private void sendBroadC(){
        Intent intent = new Intent();
        intent.setAction(Constant.IMPROVE_SLEEP_PLAN);
        intent.putExtra("getup", waketime);
        intent.putExtra("sleep", SleepTime);
        sendBroadcast(intent);
    }
}
