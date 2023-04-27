package com.wuzi.pig.module.warm.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.net.factory.ResponseException;

public interface WarmContract {

    interface IView extends IContract.IView {
        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
    }

    interface IModel {
    }

}
