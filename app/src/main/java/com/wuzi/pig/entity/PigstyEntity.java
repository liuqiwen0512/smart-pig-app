package com.wuzi.pig.entity;

public class PigstyEntity {

    private String mName;
    private int mOnlineCount;
    private int mTemperature;
    private int mLiveness;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getOnlineCount() {
        return mOnlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        mOnlineCount = onlineCount;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(int temperature) {
        mTemperature = temperature;
    }

    public int getLiveness() {
        return mLiveness;
    }

    public void setLiveness(int liveness) {
        mLiveness = liveness;
    }
}
