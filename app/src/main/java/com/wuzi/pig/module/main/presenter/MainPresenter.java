package com.wuzi.pig.module.main.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.entity.Statis72HourEntity;
import com.wuzi.pig.module.main.contract.MainContract;
import com.wuzi.pig.module.main.model.MainModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

public class MainPresenter extends BasePresenter<MainContract.IView, MainModel> implements MainContract.IPresenter {
    @Override
    public void getPigstyCount(String pagfarmId) {
        ResponseObserver<String> commonResponse = getCommonResponse(new ResponseListener<String>() {
            @Override
            public void onSuccess(String count) {
                getView().performPigstyCountSuccess(count);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, MainContract.TAG_PIGSTY_COUNT);
            }
        });
        mModel.getPigstyCount(pagfarmId, commonResponse);
    }

    @Override
    public void getStatis72Hour(String pagfarmId) {
        ResponseObserver<Statis72HourEntity> commonResponse = getCommonResponse(new ResponseListener<Statis72HourEntity>() {
            @Override
            public void onSuccess(Statis72HourEntity statisCount) {
                getView().performStatis72HourSuccess(statisCount);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, MainContract.TAG_STATIS_72_HOUR);
            }
        });
        mModel.getStatis72Hour(pagfarmId, commonResponse);
    }
}
