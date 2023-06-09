package com.wuzi.pig.module.management.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

import java.util.List;

public interface PigFarmContract {

    int TAG_PIG_FARM_LIST = 100;
    int TAG_PIG_FARM_ADD = 200;
    int TAG_PIG_FARM_UPDATE = 300;
    int TAG_PIG_FARM_DELETE = 400;
    int TAG_PIG_FARM_BIND = 500;

    interface IView extends IContract.IView {
        void performSuccess(Object data, int fromTag);

        void performPigFarmList(PigFarmListEntity listEntity, int pageNum);
        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
        void addPigFarm(String name);
        void updatePigFarm(String id, String name);
        void deletePigFarm(List<String> ids);

        void getPigFarmList(int pageNum);

        void bindPigFarm(String id);
    }

    interface IModel {
        void addPigFarm(String name, ResponseObserver observer);
        void updatePigFarm(String id, String name, ResponseObserver observer);
        void deletePigFarm(List<String> ids, ResponseObserver observer);

        void getPigFarmList(int pageNum, ResponseObserver observer);

        void bindPigFarm(String id, ResponseObserver observer);
    }

}
