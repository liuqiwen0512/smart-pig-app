package com.wuzi.pig.constant;

import com.wuzi.pig.entity.PigFarmEntity;

/**
 * 常量类
 */
public class Constant {

    // log 日志开关
    public static boolean LOG_SWITCH = true;

    //net address
    public static final String BASE_URL = "http://122.114.15.97:19007";
    // net cache 文件名称
    public static String NET_CACHE_DIR = "net_cache";

    public static PigFarmEntity getTestPigFarm() {
        PigFarmEntity entity = new PigFarmEntity();
        entity.setPigfarmId("15");

        return entity;
    }

}
