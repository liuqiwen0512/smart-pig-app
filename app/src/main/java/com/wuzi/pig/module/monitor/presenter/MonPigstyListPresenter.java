package com.wuzi.pig.module.monitor.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.entity.Statis72HourEntity;
import com.wuzi.pig.module.monitor.contract.MonPigstyListContract;
import com.wuzi.pig.module.monitor.model.MonPigstyListModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

public class MonPigstyListPresenter extends BasePresenter<MonPigstyListContract.IView, MonPigstyListModel> implements MonPigstyListContract.IPresenter {
    @Override
    public void getPigstyCount(String pagfarmId) {
        ResponseObserver<String> commonResponse = getCommonResponse(new ResponseListener<String>() {
            @Override
            public void onSuccess(String count) {
                getView().performPigstyCountSuccess(count);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, MonPigstyListContract.TAG_PIGSTY_COUNT);
            }
        });
        mModel.getPigstyCount(pagfarmId, commonResponse);
    }

    @Override
    public void getStatis72Hour(String pagfarmId) {
        ResponseObserver<Statis72HourEntity> commonResponse = getCommonResponse(new ResponseListener<Statis72HourEntity>() {
            @Override
            public void onSuccess(Statis72HourEntity statisCount) {
                getView().performStatis72HourSuccess(statisCount);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, MonPigstyListContract.TAG_STATIS_72_HOUR);
            }
        });
        mModel.getStatis72Hour(pagfarmId, commonResponse);
    }

    @Override
    public void getPigstyList(final String pagfarmId, final int pageNum) {
        ResponseObserver<PigstyListEntity> commonResponse = getCommonResponse(new ResponseListener<PigstyListEntity>() {
            @Override
            public void onSuccess(PigstyListEntity listEntity) {
                if (listEntity.getTotal() == 0) {
                    ResponseException error = new ResponseException(ResponseException.ERROR.RESULT_CODE_201);
                    onError(error);
                } else {
                    getView().performPigstyList(listEntity, pageNum, pagfarmId);
                }
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, MonPigstyListContract.TAG_PIGSTY_LIST);
            }
        });
        mModel.getPigstyList(pagfarmId, pageNum, commonResponse);
    }
}
