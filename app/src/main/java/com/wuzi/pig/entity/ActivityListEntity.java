package com.wuzi.pig.entity;

import java.util.List;

public class ActivityListEntity {

    private String earTag;
    private List<ItemEntity> datas;
    private boolean mSelected;

    public String getEarTag() {
        return earTag;
    }

    public void setEarTag(String earTag) {
        this.earTag = earTag;
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
        private float movement;
        private String time;

        public float getMovement() {
            return movement;
        }

        public void setMovement(float movement) {
            this.movement = movement;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
