package com.wuzi.pig.module.user.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.module.user.contract.RegisterContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.factory.ResponseObserver;

import io.reactivex.Observable;

public class RegisterModel extends BaseModel implements RegisterContract.IModel {
    @Override
    public void register(String userName, String password, String verificationCode, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.REGISTER_USERNAME, userName);
        requestParams.put(RequestParams.REGISTER_PASSWORD, password);
        requestParams.put(RequestParams.REGISTER_VERIFICATION_CODE, verificationCode);
        Observable<ResponseEntity<UserEntity>> observable = ApiManager.getApiService().register(requestParams);
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
