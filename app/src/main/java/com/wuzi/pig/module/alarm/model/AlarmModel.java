package com.wuzi.pig.module.alarm.model;

import com.wuzi.pig.base.BaseModel;
import com.wuzi.pig.constant.AlarmConstant;
import com.wuzi.pig.entity.AlarmListEntity;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.module.alarm.contract.AlarmContract;
import com.wuzi.pig.net.ApiManager;
import com.wuzi.pig.net.RequestParams;
import com.wuzi.pig.net.factory.ResponseObserver;
import com.wuzi.pig.utils.StringUtils;

import io.reactivex.Observable;

public class AlarmModel extends BaseModel implements AlarmContract.IModel {

    @Override
    public void getAlarmActivityList(AlarmContract.QueryEntity query, ResponseObserver observer) {
        RequestParams requestParams = RequestParams.getCommonParams();
        requestParams.put(RequestParams.ALARM_PIGFARM_ID, query.pigfarmId);
        requestParams.put(RequestParams.ALARM_TYPE, query.type);
        requestParams.put(RequestParams.ALARM_STATUS, AlarmConstant.STATUS_ACTIVITY);
        requestParams.put(RequestParams.PAGE_NUMBER, query.pageNum);
        requestParams.put(RequestParams.PAGE_SIZE, AlarmConstant.PAGE_SIZE);
        if (!StringUtils.isEmpty(query.beginTime)) {
            requestParams.put(RequestParams.ALARM_BEGIN_TIME, query.beginTime);
        }
        if (!StringUtils.isEmpty(query.endTime)) {
            requestParams.put(RequestParams.ALARM_END_TIME, query.endTime);
        }
        if (!StringUtils.isEmpty(query.allStatus)) {
            requestParams.put(RequestParams.ALARM_ALL_STATUS, query.allStatus);
        }
        if (!StringUtils.isEmpty(query.orderColumn)) {
            requestParams.put(RequestParams.ALARM_COLUMN, query.orderColumn);
            requestParams.put(RequestParams.ALARM_ASC_OR_DESC, query.desc ? AlarmConstant.ORDER_DESC : AlarmConstant.ORDER_ASC);
        }

        Observable<ResponseEntity<AlarmListEntity>> observable = ApiManager.getApiService().getAlarms(requestParams);
        apiSubscribe(observable, observer);
    }

    @Override
    public void getAlarmHistoryList(AlarmContract.QueryEntity query, ResponseObserver observer) {

    }
}
