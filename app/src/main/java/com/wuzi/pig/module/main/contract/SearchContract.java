package com.wuzi.pig.module.main.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

public interface SearchContract {

    int TAG_PIG_FARM_LIST = 100;
    int TAG_PIGSTY_LIST = 200;

    interface IView extends IContract.IView {
        void performSuccess(Object listEntity, int pageNum, int fromTag);

        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
        void getPigFarmList(String keyword, int pageNum);

        void getPigstyList(String pigfarmId, int pageNum);
    }

    interface IModel {
        void getPigFarmList(String keyword, int pageNum, ResponseObserver observer);

        void getPigstyList(String pigfarmId, int pageNum, ResponseObserver observer);
    }

}
