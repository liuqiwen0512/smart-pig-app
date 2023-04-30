package com.wuzi.pig.module.management.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.module.management.contract.PigstyContract;
import com.wuzi.pig.module.management.model.PigstyModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

import java.util.List;

public class PigstyPresenter extends BasePresenter<PigstyContract.IView, PigstyModel> implements PigstyContract.IPresenter {

    @Override
    public void addPigsty(PigstyEntity entity) {
        ResponseObserver<Object> commonResponse = getCommonResponse(new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object entity) {
                getView().performSuccess(PigstyContract.TAG_PIGSTY_ADD);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PigstyContract.TAG_PIGSTY_ADD);
            }
        });
        mModel.addPigsty(entity, commonResponse);
    }

    @Override
    public void updatePigsty(PigstyEntity entity) {
        ResponseObserver<Object> commonResponse = getCommonResponse(new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object entity) {
                getView().performSuccess(PigstyContract.TAG_PIGSTY_UPDATE);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PigstyContract.TAG_PIGSTY_UPDATE);
            }
        });
        mModel.updatePigsty(entity, commonResponse);
    }

    @Override
    public void deletePigsty(List<String> ids) {
        ResponseObserver<Object> commonResponse = getCommonResponse(new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object entity) {
                getView().performSuccess(PigstyContract.TAG_PIGSTY_DELETE);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PigstyContract.TAG_PIGSTY_DELETE);
            }
        });
        mModel.deletePigsty(ids, commonResponse);
    }

    @Override
    public void getPigstyList(String pigfarmId, int pageNum) {
        ResponseObserver<PigstyListEntity> commonResponse = getCommonResponse(new ResponseListener<PigstyListEntity>() {
            @Override
            public void onSuccess(PigstyListEntity listEntity) {
                if (listEntity.getTotal() == 0) {
                    ResponseException error = new ResponseException(ResponseException.ERROR.RESULT_CODE_201);
                    onError(error);
                } else {
                    getView().performPigstyList(listEntity, pageNum);
                }
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PigstyContract.TAG_PIGSTY_LIST);
            }
        });
        mModel.getPigstyList(pigfarmId, pageNum, commonResponse);
    }
}
