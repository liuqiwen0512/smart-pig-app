package com.wuzi.pig.module.management.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.constant.Constant;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.module.management.contract.PigFarmContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.entity.ResponsePigFarmEntity;
import com.wuzi.pig.net.factory.ResponseObserver;
import com.wuzi.pig.utils.StringUtils;

import java.util.List;

import io.reactivex.Observable;

public class PigFarmModel extends BaseModel implements PigFarmContract.IModel {
    @Override
    public void addPigFarm(String name, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.PIG_FARM_NAME, name);
        Observable<ResponseEntity<Object>> observable = ApiManager.getApiService().addPigfarm(requestParams);
        apiSubscribe(observable, observer);
    }

    @Override
    public void updatePigFarm(String id, String name, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.PIG_FARM_PIGFARMID, id);
        requestParams.put(RequestParams.PIG_FARM_NAME, name);
        Observable<ResponseEntity<Object>> observable = ApiManager.getApiService().updatePigfarm(requestParams);
        apiSubscribe(observable, observer);
    }

    @Override
    public void deletePigFarm(List<String> ids, ResponseObserver observer) {
        String pigfarmIds = "";
        for (String itemId: ids) {
            if (!StringUtils.isEmpty(pigfarmIds)) {
                pigfarmIds += ",";
            }
            pigfarmIds += itemId;
        }
        Observable<ResponseEntity<Object>> observable = ApiManager.getApiService().deletePigfarm(pigfarmIds);
        apiSubscribe(observable, observer);
    }

    @Override
    public void getPigFarmList(int pageNum, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.PAGE_SIZE, Constant.PAGE_SIZE);
        requestParams.put(RequestParams.PAGE_NUMBER, pageNum);
        Observable<ResponseEntity<PigFarmListEntity>> observable = ApiManager.getApiService().getPigFarmList(requestParams);
        apiSubscribe(observable, observer);
    }
}
