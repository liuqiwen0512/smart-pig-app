package com.wuzi.pig.constant;

import com.wuzi.pig.entity.OptionEntity;

public class WarmConstant {

    public final static String FRAGMENT_TEMPERATURE = "temperature";
    public final static String FRAGMENT_ACTIVITY = "activity";
    public final static String FRAGMENT_EAR_TAG = "ear_tag";
    public final static String FRAGMENT_BASE_STATION = "base_station";
    public final static String FRAGMENT_OUTAGE = "outage";

    public final static OptionEntity[] PAGE_TABS = new OptionEntity[]{
            new OptionEntity(FRAGMENT_TEMPERATURE, "发烧"),
            new OptionEntity(FRAGMENT_ACTIVITY, "活跃度"),
            new OptionEntity(FRAGMENT_EAR_TAG, "耳标"),
            new OptionEntity(FRAGMENT_BASE_STATION, "基站"),
            new OptionEntity(FRAGMENT_OUTAGE, "掉电")
    };

}
