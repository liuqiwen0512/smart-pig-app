package com.wuzi.pig.module.main.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.constant.PigFarmConstant;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.module.main.contract.SearchContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.factory.ResponseObserver;
import com.wuzi.pig.utils.StringUtils;

import io.reactivex.Observable;

public class SearchModel extends BaseModel implements SearchContract.IModel {

    @Override
    public void getPigFarmList(String keyword, int pageNum, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        if (!StringUtils.isEmpty(keyword)) {
            requestParams.put(RequestParams.PIG_FARM_SEARCH_VALUE, keyword);
        }
        requestParams.put(RequestParams.PAGE_SIZE, PigFarmConstant.PAGE_SIZE);
        requestParams.put(RequestParams.PAGE_NUMBER, pageNum);
        Observable<ResponseEntity<PigFarmListEntity>> observable = ApiManager.getApiService().getPigFarmList(requestParams);
        apiSubscribe(observable, observer);
    }

    @Override
    public void getPigstyList(String pigfarmId, int pageNum, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.PIG_FARM_PIGFARMID, pigfarmId);
        requestParams.put(RequestParams.PAGE_SIZE, PigFarmConstant.PAGE_SIZE);
        requestParams.put(RequestParams.PAGE_NUMBER, pageNum);
        Observable<ResponseEntity<PigstyListEntity>> observable = ApiManager.getApiService().getPigstyList(requestParams);
        apiSubscribe(observable, observer);
    }
}
