package com.wuzi.pig.module.user.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.module.user.contract.PasswordContract;
import com.wuzi.pig.module.user.model.PasswordModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

public class PasswordPresenter extends BasePresenter<PasswordContract.IView, PasswordModel> implements PasswordContract.IPresenter {

    @Override
    public void updateByVerificationCode(String newPwd, String verificationCode, String phone) {
        ResponseObserver<Object> commonResponse = getCommonResponse(new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object entity) {
                getView().performSuccress(entity);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PasswordContract.TAG_ERROR_PASSWORD);
            }
        });
        mModel.updateByVerificationCode(newPwd, verificationCode, phone, commonResponse);
    }

    @Override
    public void verificationCode(String phone) {
        ResponseObserver<Object> commonResponse = getCommonResponse(new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object entity) {
                getView().performVerificationCodeSuccress();
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PasswordContract.TAG_ERROR_VERIFICATION_CODE);
            }
        });
        mModel.verificationCode(phone, commonResponse);
    }
}
