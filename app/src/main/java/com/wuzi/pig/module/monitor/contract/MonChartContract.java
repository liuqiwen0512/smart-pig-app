package com.wuzi.pig.module.monitor.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.entity.ActivityListEntity;
import com.wuzi.pig.entity.ResponseListData;
import com.wuzi.pig.entity.TempListEntity;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

public interface MonChartContract {

    String INTENT_EXTRA_PIGSTY = "INTENT_EXTRA_PIGSTY";
    int TAG_TEMPERATURES = 100;
    int TAG_ACTIVITY = 200;

    interface IView extends IContract.IView {
        void performTemperatures(ResponseListData<TempListEntity> listData, int fromTag);

        void performActivitys(ResponseListData<ActivityListEntity> listData, int fromTag);

        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
        void getTemperatures(Query query);

        void getMovements(Query query);
    }

    interface IModel {
        void getTemperatures(Query query, ResponseObserver observer);

        void getMovements(Query query, ResponseObserver observer);
    }

    class Query {
        public String pigstyId;
        public String beginTime;
        public String endTime;
        public int particle = 10;
        public int pageNum = 1;
        public int pageSize = 100;
    }

}
