package com.wuzi.pig.module.monitor.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.entity.ActivityListEntity;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.ResponseListData;
import com.wuzi.pig.entity.TempListEntity;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    class UIEntity {
        public String model; //当前数据类型
        public PigstyEntity pigstyEntity; //猪栏数据
        public Calendar calendar; //日期
        public ResponseListData<TempListEntity> tempList; //温度数据
        public ResponseListData<ActivityListEntity> activityList; //运动量数据
        public Map<String, String> unSelectedTagMap = new HashMap<>(); //反选的猪
        public float scaleX; // chart x scale
        public float leastX; // chart x least
        public int[] colors; // chart line color
    }

}
