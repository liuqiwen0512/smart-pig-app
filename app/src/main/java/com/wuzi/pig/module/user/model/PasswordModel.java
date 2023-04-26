package com.wuzi.pig.module.user.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.module.user.contract.PasswordContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.factory.ResponseObserver;

import io.reactivex.Observable;

public class PasswordModel extends BaseModel implements PasswordContract.IModel {

    @Override
    public void updateByVerificationCode(String newPwd, String verificationCode, String phone, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.UPDATE_PASSWORD_PHONE, phone);
        requestParams.put(RequestParams.UPDATE_PASSWORD_CODE, verificationCode);
        requestParams.put(RequestParams.UPDATE_PASSWORD_NEW, newPwd);
        Observable<ResponseEntity<UserEntity>> observable = ApiManager.getApiService().forgetPwd(requestParams);
        apiSubscribe(observable, observer);
    }

    @Override
    public void verificationCode(String phone, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.REGISTER_PHONE, phone);
        Observable<ResponseEntity<Object>> observable = ApiManager.getApiService().getSend(requestParams);
        apiSubscribe(observable, observer);
    }
}
