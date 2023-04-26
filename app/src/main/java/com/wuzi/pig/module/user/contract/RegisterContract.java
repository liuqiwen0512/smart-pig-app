package com.wuzi.pig.module.user.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

public interface RegisterContract {

    int TAG_ERROR_VERIFICATION_CODE = 100;
    int TAG_ERROR_REGISTER = 200;

    interface IView extends IContract.IView {
        void performRegisterSuccress(UserEntity entity);
        void performVerificationCodeSuccress();
        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
        void register(String userName, String password, String verificationCode);
        void verificationCode(String phone);
    }

    interface IModel {
        void register(String userName, String password, String verificationCode, ResponseObserver observer);
        void verificationCode(String phone, ResponseObserver observer);
    }

}
