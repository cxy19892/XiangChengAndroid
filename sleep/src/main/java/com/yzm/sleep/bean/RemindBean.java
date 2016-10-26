package com.yzm.sleep.bean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/14.
 */
public class RemindBean {
    private String date;
    private String sleepTime;
    private String getupTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getGetupTime() {
        return getupTime;
    }

    public void setGetupTime(String getupTime) {
        this.getupTime = getupTime;
    }

    public String getString(){
        JSONObject mJSON = new JSONObject();
        try {
            mJSON.put("date", date);
            mJSON.put("sleepTime", sleepTime);
            mJSON.put("getupTime", getupTime);
        }catch (Exception e){

        }
        return mJSON.toString();
    }
}
