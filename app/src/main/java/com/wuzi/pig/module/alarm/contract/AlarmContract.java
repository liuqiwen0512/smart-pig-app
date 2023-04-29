package com.wuzi.pig.module.alarm.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.entity.AlarmListEntity;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

public interface AlarmContract {

    interface IView extends IContract.IView {
        void performList(AlarmListEntity list, QueryEntity query);

        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
        void getAlarmActivityList(QueryEntity query);

        void getAlarmHistoryList(QueryEntity query);
    }

    interface IModel {
        void getAlarmActivityList(QueryEntity query, ResponseObserver observer);

        void getAlarmHistoryList(QueryEntity query, ResponseObserver observer);
    }

    class QueryEntity {
        public String pigfarmId;
        public String beginTime;
        public String endTime;
        public String type;
        public String allStatus;
        public String orderColumn;
        public boolean desc;
        public int pageNum;

        public QueryEntity clone() {
            QueryEntity entity = new QueryEntity();
            entity.pigfarmId = pigfarmId;
            entity.beginTime = beginTime;
            entity.endTime = endTime;
            entity.type = type;
            entity.allStatus = allStatus;
            entity.orderColumn = orderColumn;
            entity.desc = desc;
            entity.pageNum = pageNum;
            return entity;
        }
    }

}
