package com.wuzi.pig.module.monitor.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.entity.ResponseListData;
import com.wuzi.pig.entity.TempListEntity;
import com.wuzi.pig.module.monitor.contract.MonChartContract;
import com.wuzi.pig.module.monitor.model.MonChartModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

public class MonChartPresenter extends BasePresenter<MonChartContract.IView, MonChartModel> implements MonChartContract.IPresenter {

    @Override
    public void getTemperatures(MonChartContract.Query query) {
        ResponseObserver<ResponseListData<TempListEntity>> commonResponse = getCommonResponse(new ResponseListener<ResponseListData<TempListEntity>>() {
            @Override
            public void onSuccess(ResponseListData<TempListEntity> listData) {
                getView().performTemperatures(listData, MonChartContract.TAG_TEMPERATURES);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, MonChartContract.TAG_TEMPERATURES);
            }
        });
        mModel.getTemperatures(query, commonResponse);
    }
}
