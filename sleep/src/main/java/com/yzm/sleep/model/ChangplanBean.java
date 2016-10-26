package com.yzm.sleep.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/15.
 */
public class ChangplanBean implements Serializable{

    private String up_wakeup;
    private String up_sleep;
    private String flag;//          ( 1增加可睡时间；2减少；3不做调整 )
    private String  sleeplong;//     可睡时间，（格式为483，单位为分钟）
    private String  up_sleeplong;//   本周期设置的可睡时间（用户选择不调整时，传该参数！）
    private String isshow;//         (1显示继续计划；0不显示)

    public String getUp_wakeup() {
        return up_wakeup;
    }

    public void setUp_wakeup(String up_wakeup) {
        this.up_wakeup = up_wakeup;
    }

    public String getUp_sleep() {
        return up_sleep;
    }

    public void setUp_sleep(String up_sleep) {
        this.up_sleep = up_sleep;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSleeplong() {
        return sleeplong;
    }

    public void setSleeplong(String sleeplong) {
        this.sleeplong = sleeplong;
    }

    public String getUp_sleeplong() {
        return up_sleeplong;
    }

    public void setUp_sleeplong(String up_sleeplong) {
        this.up_sleeplong = up_sleeplong;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }
}
