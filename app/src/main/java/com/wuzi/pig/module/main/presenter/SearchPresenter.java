package com.wuzi.pig.module.main.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.module.main.contract.SearchContract;
import com.wuzi.pig.module.main.model.SearchModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

public class SearchPresenter extends BasePresenter<SearchContract.IView, SearchModel> implements SearchContract.IPresenter {

    @Override
    public void getPigFarmList(String keyword, int pageNum) {
        ResponseObserver<PigFarmListEntity> commonResponse = getCommonResponse(new ResponseListener<PigFarmListEntity>() {
            @Override
            public void onSuccess(PigFarmListEntity listEntity) {
                if (listEntity.getTotal() == 0) {
                    ResponseException error = new ResponseException(ResponseException.ERROR.RESULT_CODE_201);
                    onError(error);
                } else {
                    getView().performSuccess(listEntity, pageNum, SearchContract.TAG_PIG_FARM_LIST);
                }
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, SearchContract.TAG_PIG_FARM_LIST);
            }
        });
        mModel.getPigFarmList(keyword, pageNum, commonResponse);
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
                    getView().performSuccess(listEntity, pageNum, SearchContract.TAG_PIGSTY_LIST);
                }
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, SearchContract.TAG_PIGSTY_LIST);
            }
        });
        mModel.getPigstyList(pigfarmId, pageNum, commonResponse);
    }
}
