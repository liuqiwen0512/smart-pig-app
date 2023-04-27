package com.wuzi.pig.module.management.contract;

import com.wuzi.pig.base.IContract;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseObserver;

import java.util.List;

public interface PigstyContract {

    int TAG_PIGSTY_LIST = 100;
    int TAG_PIGSTY_ADD = 200;
    int TAG_PIGSTY_UPDATE = 300;
    int TAG_PIGSTY_DELETE = 400;

    interface IView extends IContract.IView {
        void performSuccess(int fromTag);

        void performPigstyList(PigstyListEntity listEntity, int pageNum);

        void performError(ResponseException error, int fromTag);
    }

    interface IPresenter {
        void addPigsty(PigstyEntity entity);

        void updatePigsty(PigstyEntity entity);

        void deletePigsty(List<String> ids);

        void getPigstyList(int pageNum);
    }

    interface IModel {
        void addPigsty(PigstyEntity entity, ResponseObserver observer);

        void updatePigsty(PigstyEntity entity, ResponseObserver observer);

        void deletePigsty(List<String> ids, ResponseObserver observer);

        void getPigstyList(int pageNum, ResponseObserver observer);
    }

}
