package com.wuzi.pig.constant;

import com.wuzi.pig.entity.OptionEntity;

public class AlarmConstant {

    public static int PAGE_SIZE = 50;

    //告警类型
    public final static String TYPE_TEMPERATURE = "发烧";
    public final static String TYPE_ACTIVITY = "活跃度";
    public final static String TYPE_EAR_TAG = "耳标";
    public final static String TYPE_BASE_STATION = "基站";
    public final static String TYPE_OUTAGE = "掉电";

    //告警状态
    public final static String STATUS_ACTIVITY = "活跃";
    public final static String STATUS_HISTORY = "历史";

    //设备状态
    public final static String ALLSTATUS_ABNORMAL = "掉标";
    public final static String ALLSTATUS_NORMAL = "良好";
    public final static String ALLSTATUS_ALL = "全部";

    //排序字段
    public final static String ORDER_COLUMN_PIGSTY_NAME = "pigstyName";
    public final static String ORDER_COLUMN_CREATE_TIME = "createTime";

    //排序方式
    public final static String ORDER_ASC = "asc";
    public final static String ORDER_DESC = "desc";

    //温度等级
    public final static String TEMPERATURE_HIGH = "高烧";
    public final static String TEMPERATURE_LOW = "低烧";


    public final static OptionEntity[] PAGE_TABS = new OptionEntity[]{
            new OptionEntity(TYPE_TEMPERATURE, "发烧"),
            new OptionEntity(TYPE_ACTIVITY, "活跃度"),
            new OptionEntity(TYPE_EAR_TAG, "耳标"),
            new OptionEntity(TYPE_BASE_STATION, "基站"),
            new OptionEntity(TYPE_OUTAGE, "掉电")
    };

}
