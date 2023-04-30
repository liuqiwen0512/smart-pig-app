package com.wuzi.pig.module.main.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.entity.Statis72HourEntity;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

public interface MainContract {

    int TAG_PIGSTY_COUNT = 100;
    int TAG_STATIS_72_HOUR = 200;

    interface IView extends IContract.IView {
        void performPigstyCountSuccess(String count);

        void performStatis72HourSuccess(Statis72HourEntity entity);

        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
        void getPigstyCount(String pagfarmId);

        void getStatis72Hour(String pagfarmId);
    }

    interface IModel {
        void getPigstyCount(String pagfarmId, ResponseObserver observer);

        void getStatis72Hour(String pagfarmId, ResponseObserver observer);
    }

}
