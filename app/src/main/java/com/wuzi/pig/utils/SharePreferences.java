package com.wuzi.pig.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wuzi.pig.base.BaseApplication;
import com.wuzi.pig.entity.PigFarmEntity;


/**
 * 文件存储
 */
public class SharePreferences {
    private static volatile SharePreferences sharePreferences;
    private SharedPreferences prefs;
    private final String SHARE_PREFERENCE_NAME = "wuzi";
    // app版本号
    private final String PREFERENCE_APP_VERSION_CODE = "preference_app_version_code";
    // app更新信息版本号
    private final String PREFERENCE_APP_UPGRADE_INFO = "preference_app_upgrade_info";
    // 用户信息
    private final String PREFERENCE_LOGINED_USER_DATA = "preference_logined_user_data";
    // 选择的猪场
    private final String PREFERENCE_SELECTED_PIG_FARM_DATA = "preference_selected_pig_farm_data";

    public void setLoginedUserData(String user) {
        putString(PREFERENCE_LOGINED_USER_DATA, user);
    }

    public String getLoginedUserData() {
        return getString(PREFERENCE_LOGINED_USER_DATA);
    }

    public void setSelectedPigFarm(PigFarmEntity entity) {
        String json = "";
        if (entity != null) {
            Gson gson = new Gson();
            json = gson.toJson(entity);
        }
        putString(PREFERENCE_SELECTED_PIG_FARM_DATA, json);
    }

    public PigFarmEntity getSelectedPigFarm() {
        String json = getString(PREFERENCE_SELECTED_PIG_FARM_DATA);
        if (!StringUtils.isEmpty(json)) {
            try {
                return new Gson().fromJson(json, PigFarmEntity.class);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static SharePreferences getInstance() {
        if (sharePreferences == null) {
            synchronized (SharePreferences.class) {
                if (sharePreferences == null) {
                    sharePreferences = new SharePreferences(BaseApplication.getApplication());
                }
            }
        }
        return sharePreferences;
    }

    private SharePreferences(Context context) {
        prefs = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 存放普通数据的方法
     *
     * @param key  存储数据的键
     * @param data 存储数据的值
     */
    public void putString(String key, String data) {
        prefs.edit().putString(key, data).apply();
    }

    /**
     * 读取普通数据的方法
     *
     * @param key 要读取数据的key
     * @return 要读取的数据
     */
    public String getString(String key) {
        return prefs.getString(key, "");
    }

    /**
     * 读取普通数据的方法
     *
     * @param key 要读取数据的key
     * @return 要读取的数据
     */
    public String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    /**
     * 存放普通数据的方法
     *
     * @param key  存储数据的键
     * @param data 存储数据的值
     */
    protected void putInt(String key, int data) {
        prefs.edit().putInt(key, data).apply();
    }

    /**
     * 读取普通数据的方法
     *
     * @param key 要读取数据的key
     * @return 要读取的数据
     */
    protected int getInt(String key) {
        return prefs.getInt(key, 0);
    }

    protected int getInt(String key, int value) {
        return prefs.getInt(key, value);
    }

    /**
     * 移除相关key对应的item
     *
     * @param key 需要移除的key
     */
    protected void remove(String key) {
        prefs.edit().remove(key).apply();
    }


    protected void putLong(String key, long data) {
        prefs.edit().putLong(key, data).apply();
    }

    protected Long getLong(String key) {
        return prefs.getLong(key, 0);
    }

    protected void putBoolean(String key, Boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    protected Boolean getBoolean(String value) {
        return getBoolean(value, false);
    }

    protected Boolean getBoolean(String value, boolean defValue) {
        return prefs.getBoolean(value, defValue);
    }

    /**
     * 提交数据至文件
     */
    public void commit() {
        prefs.edit().commit();
    }

    /**
     * 清除相关prefs数据
     */
    public void clear() {
        prefs.edit().clear().apply();
    }


}
