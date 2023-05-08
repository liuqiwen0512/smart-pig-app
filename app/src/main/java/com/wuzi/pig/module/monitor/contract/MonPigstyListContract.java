package com.wuzi.pig.module.monitor.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.entity.Statis72HourEntity;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

public interface MonPigstyListContract {

    int TAG_PIGSTY_COUNT = 100;
    int TAG_STATIS_72_HOUR = 200;
    int TAG_PIGSTY_LIST = 300;

    interface IView extends IContract.IView {
        void performPigstyCountSuccess(String count);

        void performStatis72HourSuccess(Statis72HourEntity entity);

        void performPigstyList(PigstyListEntity listEntity, int pageNum, String pagfarmId);

        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
        void getPigstyCount(String pagfarmId);

        void getStatis72Hour(String pagfarmId);

        void getPigstyList(String pagfarmId, int pageNum);
    }

    interface IModel {
        void getPigstyCount(String pagfarmId, ResponseObserver observer);

        void getStatis72Hour(String pagfarmId, ResponseObserver observer);

        void getPigstyList(String pagfarmId, int pageNum, ResponseObserver observer);
    }

}
