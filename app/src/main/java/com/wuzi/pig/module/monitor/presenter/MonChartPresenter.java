package com.wuzi.pig.module.monitor.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.entity.ActivityListEntity;
import com.wuzi.pig.entity.ResponseEntity;
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
                //listData = getTestData();
                if (listData.getTotal() == 0) {
                    onError(new ResponseException(ResponseException.ERROR.RESULT_CODE_201));
                } else {
                    getView().performTemperatures(listData, MonChartContract.TAG_TEMPERATURES);
                }
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, MonChartContract.TAG_TEMPERATURES);
            }
        });
        mModel.getTemperatures(query, commonResponse);
    }

    @Override
    public void getMovements(MonChartContract.Query query) {
        ResponseObserver<ResponseListData<ActivityListEntity>> commonResponse = getCommonResponse(new ResponseListener<ResponseListData<ActivityListEntity>>() {
            @Override
            public void onSuccess(ResponseListData<ActivityListEntity> listData) {
                //listData = getTestData();
                if (listData.getTotal() == 0) {
                    onError(new ResponseException(ResponseException.ERROR.RESULT_CODE_201));
                } else {
                    getView().performActivitys(listData, MonChartContract.TAG_ACTIVITY);
                }
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, MonChartContract.TAG_ACTIVITY);
            }
        });
        mModel.getMovements(query, commonResponse);
    }


    private ResponseListData<TempListEntity> getTestData() {
        String json = "{\n" +
                "    \"total\": 0,\n" +
                "    \"rows\": null,\n" +
                "    \"code\": 200,\n" +
                "    \"data\": {\n" +
                "        \"rows\": [\n" +
                "            {\n" +
                "                \"earTag\": \"00,00,00,11\",\n" +
                "                \"code\": \"12874\",\n" +
                "                \"datas\": [\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 45,\n" +
                "                        \"time\": \"2023-05-11 14:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 44.7,\n" +
                "                        \"time\": \"2023-05-11 14:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.7,\n" +
                "                        \"time\": \"2023-05-11 14:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 14:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 44.3,\n" +
                "                        \"time\": \"2023-05-11 14:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.5,\n" +
                "                        \"time\": \"2023-05-11 14:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.55,\n" +
                "                        \"time\": \"2023-05-11 15:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 44.2,\n" +
                "                        \"time\": \"2023-05-11 15:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.3,\n" +
                "                        \"time\": \"2023-05-11 15:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 15:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.2,\n" +
                "                        \"time\": \"2023-05-11 15:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43,\n" +
                "                        \"time\": \"2023-05-11 15:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.55,\n" +
                "                        \"time\": \"2023-05-11 16:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.4,\n" +
                "                        \"time\": \"2023-05-11 16:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 16:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 16:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 16:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 16:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:50\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"baseStation\": \"BC:DD:C2:6D:52:90\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"earTag\": \"FF,FF,FF,99\",\n" +
                "                \"code\": \"21557\",\n" +
                "                \"datas\": [\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 00:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 01:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 02:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 03:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 04:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 05:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 06:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 07:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 08:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 09:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 10:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 11:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 12:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 13:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 14:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 14:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 14:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.75,\n" +
                "                        \"time\": \"2023-05-11 14:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.7,\n" +
                "                        \"time\": \"2023-05-11 14:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 44.1,\n" +
                "                        \"time\": \"2023-05-11 14:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.2,\n" +
                "                        \"time\": \"2023-05-11 15:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.3,\n" +
                "                        \"time\": \"2023-05-11 15:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.05,\n" +
                "                        \"time\": \"2023-05-11 15:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.3,\n" +
                "                        \"time\": \"2023-05-11 15:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 42.9,\n" +
                "                        \"time\": \"2023-05-11 15:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.2,\n" +
                "                        \"time\": \"2023-05-11 15:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 43.15,\n" +
                "                        \"time\": \"2023-05-11 16:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 42.5,\n" +
                "                        \"time\": \"2023-05-11 16:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 16:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 16:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 16:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 16:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 17:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 18:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 19:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 20:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 21:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 22:50\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:00\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:10\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:20\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:40\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"temperature\": 0,\n" +
                "                        \"time\": \"2023-05-11 23:50\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"baseStation\": \"BC:DD:C2:6D:52:90\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"total\": 2\n" +
                "    },\n" +
                "    \"msg\": \"查询成功\"\n" +
                "}";
        ResponseEntity<ResponseListData<TempListEntity>> responseEntity = new Gson().fromJson(json, new TypeToken<ResponseEntity<ResponseListData<TempListEntity>>>() {
        }.getType());
        ResponseListData<TempListEntity> tempListData = responseEntity.getData();
        tempListData.setTotal(2);
        return tempListData;
    }
}
