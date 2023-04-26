package com.wuzi.pig.module.user.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.module.user.contract.RegisterContract;
import com.wuzi.pig.module.user.model.RegisterModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

public class RegisterPresenter extends BasePresenter<RegisterContract.IView, RegisterModel> implements RegisterContract.IPresenter {
    @Override
    public void register(String userName, String password, String verificationCode) {
        ResponseObserver<UserEntity> commonResponse = getCommonResponse(new ResponseListener<UserEntity>() {
            @Override
            public void onSuccess(UserEntity entity) {
                getView().performRegisterSuccress(entity);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, RegisterContract.TAG_ERROR_REGISTER);
            }
        });
        mModel.register(userName, password, verificationCode, commonResponse);
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
                getView().performError(exception, RegisterContract.TAG_ERROR_VERIFICATION_CODE);
            }
        });
        mModel.verificationCode(phone, commonResponse);
    }
}
