package com.wuzi.pig.module.main.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.entity.Statis72HourEntity;
import com.wuzi.pig.module.main.contract.MainContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.factory.ResponseObserver;

import io.reactivex.Observable;

public class MainModel extends BaseModel implements MainContract.IModel {
    @Override
    public void getPigstyCount(String pagfarmId, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.MONITOR_PIGFARM_ID, pagfarmId);
        Observable<ResponseEntity<String>> observable = ApiManager.getApiService().getPigstyCount(requestParams);
        apiSubscribe(observable, observer);
    }

    @Override
    public void getStatis72Hour(String pagfarmId, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.MONITOR_PIGFARM_ID, pagfarmId);
        Observable<ResponseEntity<Statis72HourEntity>> observable = ApiManager.getApiService().getStatis72Hour(requestParams);
        apiSubscribe(observable, observer);
    }
}
