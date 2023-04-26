package com.wuzi.pig.utils.manager;

import com.google.gson.Gson;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.utils.SharePreferences;
import com.wuzi.pig.utils.StringUtils;

public class LoginManager {

    private static LoginManager sInstance = null;
    private UserEntity mUserEntity;

    private LoginManager() {
        String userData = SharePreferences.getInstance().getLoginedUserData();
        if (!StringUtils.isEmpty(userData)) {
            mUserEntity = new Gson().fromJson(userData, UserEntity.class);
        } else {
            mUserEntity = new UserEntity();
        }
    }

    public static LoginManager getInstance() {
        if (sInstance == null) {
            synchronized (LoginManager.class) {
                if (sInstance == null) {
                    sInstance = new LoginManager();
                }
            }
        }
        return sInstance;
    }

    public static boolean isLogin() {
        UserEntity userEntity = getInstance().mUserEntity;
        return !StringUtils.isEmpty(userEntity.getAccess_token());
    }

    public static boolean logout() {
        UserEntity entity = new UserEntity();
        entity.setUserName(getUserName());
        String json = new Gson().toJson(entity);
        SharePreferences.getInstance().setLoginedUserData(json);
        SharePreferences.getInstance().commit();

        getInstance().mUserEntity = null;
        sInstance = null;
        return true;
    }

    public static void save(UserEntity entity) {
        String json = new Gson().toJson(entity);
        SharePreferences.getInstance().setLoginedUserData(json);
        SharePreferences.getInstance().commit();
        getInstance().mUserEntity = null;
        sInstance = null;
    }

    public static String getUserName() {
        return getInstance().mUserEntity.getUserName();
    }

    public static String getToken() {
        return getInstance().mUserEntity.getAccess_token();
    }
}
