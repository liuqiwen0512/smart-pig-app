package com.wuzi.pig.module.user.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.module.user.contract.LoginContract;
import com.wuzi.pig.module.user.model.LoginModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

public class LoginPresenter extends BasePresenter<LoginContract.IView, LoginModel> implements LoginContract.IPresenter {
    @Override
    public void onLogin(String userName, String password) {
        ResponseObserver<UserEntity> commonResponse = getCommonResponse(new ResponseListener<UserEntity>() {
            @Override
            public void onSuccess(UserEntity entity) {
                LoginContract.IView view = getView();
                entity.setUserName(userName);
                view.performLoginSuccress(entity);
            }

            @Override
            public void onError(ResponseException exception) {
                LoginContract.IView view = getView();
                view.performLoginError(exception);
            }
        });
        mModel.login(userName, password, commonResponse);
    }
}
