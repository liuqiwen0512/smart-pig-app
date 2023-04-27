package com.wuzi.pig.module.management.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.constant.PigstyConstant;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.module.management.contract.PigstyContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.factory.ResponseObserver;
import com.wuzi.pig.utils.StringUtils;

import java.util.List;

import io.reactivex.Observable;

public class PigstyModel extends BaseModel implements PigstyContract.IModel {
    @Override
    public void addPigsty(PigstyEntity entity, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.PIGSTY_PIGFARM_ID, entity.getPigfarmId());
        requestParams.put(RequestParams.PIGSTY_PIGSTY_NAME, entity.getPigstyName());
        requestParams.put(RequestParams.PIGSTY_BASE_STATION, entity.getBaseStation());
        Observable<ResponseEntity<Object>> observable = ApiManager.getApiService().addPigsty(requestParams);
        apiSubscribe(observable, observer);
    }

    @Override
    public void updatePigsty(PigstyEntity entity, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.PIGSTY_PIGFARM_ID, entity.getPigfarmId());
        requestParams.put(RequestParams.PIGSTY_PIGSTY_ID, entity.getPigstyId());
        requestParams.put(RequestParams.PIGSTY_PIGSTY_NAME, entity.getPigstyName());
        requestParams.put(RequestParams.PIGSTY_BASE_STATION, entity.getBaseStation());
        Observable<ResponseEntity<Object>> observable = ApiManager.getApiService().addPigsty(requestParams);
        apiSubscribe(observable, observer);
    }

    @Override
    public void deletePigsty(List<String> ids, ResponseObserver observer) {
        String pigstyIds = "";
        for (String itemId : ids) {
            if (!StringUtils.isEmpty(pigstyIds)) {
                pigstyIds += ",";
            }
            pigstyIds += itemId;
        }
        Observable<ResponseEntity<Object>> observable = ApiManager.getApiService().deletePigsty(pigstyIds);
        apiSubscribe(observable, observer);
    }

    @Override
    public void getPigstyList(int pageNum, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.PAGE_SIZE, PigstyConstant.PAGE_SIZE);
        requestParams.put(RequestParams.PAGE_NUMBER, pageNum);
        Observable<ResponseEntity<PigstyListEntity>> observable = ApiManager.getApiService().getPigstyList(requestParams);
        apiSubscribe(observable, observer);
    }
}
