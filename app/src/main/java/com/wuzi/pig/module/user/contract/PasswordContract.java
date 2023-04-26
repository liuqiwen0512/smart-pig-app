package com.wuzi.pig.module.user.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

public interface PasswordContract {

    int TAG_ERROR_VERIFICATION_CODE = 100;
    int TAG_ERROR_PASSWORD = 200;

    interface IView extends IContract.IView {
        void performSuccress(Object entity);
        void performVerificationCodeSuccress();
        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
        void updateByVerificationCode(String newPwd, String verificationCode, String phone);
        void verificationCode(String phone);
    }

    interface IModel {
        void updateByVerificationCode(String newPwd, String verificationCode, String phone, ResponseObserver observer);
        void verificationCode(String phone, ResponseObserver observer);
    }

}
