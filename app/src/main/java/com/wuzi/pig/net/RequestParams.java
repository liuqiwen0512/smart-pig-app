package com.wuzi.pig.net;

import androidx.annotation.Nullable;

import java.util.HashMap;


public class RequestParams extends HashMap<String, Object> {

    public final static String AUTHORIZATION = "Authorization";
    public final static String PAGE_NUMBER = "pageNum";
    public final static String PAGE_SIZE = "pageSize";

    //登录参数
    public final static String LOGIN_USERNAME = "username";
    public final static String LOGIN_PASSWORD = "password";

    //注册
    public final static String REGISTER_PHONE = "phone";
    public final static String REGISTER_VERIFICATION_CODE = "verificationCode";
    public final static String REGISTER_USERNAME = "userName";
    public final static String REGISTER_PASSWORD = "password";

    //修改密码
    public final static String UPDATE_PASSWORD_NEW = "password";
    public final static String UPDATE_PASSWORD_CODE = "code";
    public final static String UPDATE_PASSWORD_PHONE = "phone";

    //猪场
    public final static String PIG_FARM_PIGFARMID = "pigfarmId";
    public final static String PIG_FARM_NAME = "pigfarmName";
    public final static String PIG_FARM_PIGFARMIDS = "pigfarmIds";

    private RequestParams() {}

    @Nullable
    @Override
    public Object put(String key, Object value) {
        if (value == null) value = "";
        return super.put(key, value);
    }

    public static RequestParams getCommonParams() {
        RequestParams commonMap = new RequestParams();
/*
        commonMap.put(NetConstants.REQUEST_PARAM_OS, "android");
        commonMap.put(NetConstants.REQUEST_PARAM_BRAND, SystemUtils.getDeviceBrand());
        commonMap.put(NetConstants.REQUEST_PARAM_TYPE, SystemUtils.getSystemModel());
        commonMap.put(NetConstants.REQUEST_PARAM_VERSION, SystemUtils.getSystemVersion());
        commonMap.put(NetConstants.REQUEST_PARAM_APP_VERSION, BuildConfig.VERSION_CODE);
*/
        return commonMap;
    }

    public static RequestParams getEmptyParams() {
        return new RequestParams();
    }

    public RequestParams copy() {
        RequestParams params = new RequestParams();
        params.putAll(this);
        return params;
    }

}
