package com.wuzi.pig.module.user.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.module.user.contract.LoginContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.factory.ResponseObserver;

import io.reactivex.Observable;

public class LoginModel extends BaseModel implements LoginContract.IModel {
    @Override
    public void login(String userName, String password, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.LOGIN_USERNAME, userName);
        requestParams.put(RequestParams.LOGIN_PASSWORD, password);
        Observable<ResponseEntity<UserEntity>> observable = ApiManager.getApiService().login(requestParams);
        apiSubscribe(observable, observer);
    }

    @Override
    public void refreshPushToken(String token, ResponseObserver observer) {

    }
}
