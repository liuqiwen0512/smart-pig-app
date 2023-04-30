package com.wuzi.pig.module.monitor.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.constant.MonitorConstant;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.entity.Statis72HourEntity;
import com.wuzi.pig.module.monitor.contract.MonitorContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.factory.ResponseObserver;

import io.reactivex.Observable;

public class MonitorModel extends BaseModel implements MonitorContract.IModel {
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

    @Override
    public void getPigstyList(String pagfarmId, int pageNum, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.PAGE_SIZE, MonitorConstant.PAGE_SIZE);
        requestParams.put(RequestParams.PAGE_NUMBER, pageNum);
        requestParams.put(RequestParams.MONITOR_PIGFARM_ID, pagfarmId);
        Observable<ResponseEntity<PigstyListEntity>> observable = ApiManager.getApiService().getPigstyAlarm(requestParams);
        apiSubscribe(observable, observer);
    }
}
