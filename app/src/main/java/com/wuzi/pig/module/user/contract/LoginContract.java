package com.wuzi.pig.module.user.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

public interface LoginContract {

    interface IView extends IContract.IView {
        void performLoginSuccress(UserEntity entity);
        void performLoginError(ResponseException error);
    }

    interface IPresenter {
        void onLogin(String userName, String password);
    }

    interface IModel {
        void login(String userName, String password, ResponseObserver observer);
        void refreshPushToken(String token, ResponseObserver observer);
    }
}
