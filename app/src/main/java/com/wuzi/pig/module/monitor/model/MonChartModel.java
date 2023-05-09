package com.wuzi.pig.module.monitor.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.entity.ResponseListData;
import com.wuzi.pig.entity.TempListEntity;
import com.wuzi.pig.module.monitor.contract.MonChartContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.factory.ResponseObserver;

import io.reactivex.Observable;

public class MonChartModel extends BaseModel implements MonChartContract.IModel {

    @Override
    public void getTemperatures(MonChartContract.Query query, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.MONITOR_PIGSTY_ID, query.pigstyId);
        requestParams.put(RequestParams.MONITOR_BEGIN_TIME, query.beginTime);
        requestParams.put(RequestParams.MONITOR_END_TIME, query.endTime);
        requestParams.put(RequestParams.MONITOR_PARTICLE, query.particle);
        requestParams.put(RequestParams.PAGE_NUMBER, query.pageNum);
        requestParams.put(RequestParams.PAGE_SIZE, query.pageSize);
        Observable<ResponseEntity<ResponseListData<TempListEntity>>> observable = ApiManager.getApiService().getTemperatures(requestParams);
        apiSubscribe(observable, observer);
    }
}
