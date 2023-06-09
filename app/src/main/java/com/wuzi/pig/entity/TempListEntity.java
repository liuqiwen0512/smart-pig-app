package com.wuzi.pig.entity;

import java.util.List;

public class TempListEntity {

    private String earTag;
    private String baseStation;
    private String code;
    private List<ItemEntity> datas;
    private boolean mSelected;

    public String getEarTag() {
        return earTag;
    }

    public void setEarTag(String earTag) {
        this.earTag = earTag;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public void setBaseStation(String baseStation) {
        this.baseStation = baseStation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ItemEntity> getDatas() {
        return datas;
    }

    public void setDatas(List<ItemEntity> datas) {
        this.datas = datas;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public static class ItemEntity {
        private float temperature;
        private String time;

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
