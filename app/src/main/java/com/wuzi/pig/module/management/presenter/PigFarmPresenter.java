package com.wuzi.pig.module.management.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.module.management.contract.PigFarmContract;
import com.wuzi.pig.module.management.model.PigFarmModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

import java.util.List;

public class PigFarmPresenter extends BasePresenter<PigFarmContract.IView, PigFarmModel> implements PigFarmContract.IPresenter {

    @Override
    public void addPigFarm(String name) {
        ResponseObserver<Object> commonResponse = getCommonResponse(new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object entity) {
                getView().performSuccess(PigFarmContract.TAG_PIG_FARM_ADD);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PigFarmContract.TAG_PIG_FARM_ADD);
            }
        });
        mModel.addPigFarm(name, commonResponse);
    }

    @Override
    public void updatePigFarm(String id, String name) {
        ResponseObserver<Object> commonResponse = getCommonResponse(new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object entity) {
                getView().performSuccess(PigFarmContract.TAG_PIG_FARM_UPDATE);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PigFarmContract.TAG_PIG_FARM_UPDATE);
            }
        });
        mModel.updatePigFarm(id, name, commonResponse);
    }

    @Override
    public void deletePigFarm(List<String> ids) {
        ResponseObserver<Object> commonResponse = getCommonResponse(new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object entity) {
                getView().performSuccess(PigFarmContract.TAG_PIG_FARM_DELETE);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PigFarmContract.TAG_PIG_FARM_DELETE);
            }
        });
        mModel.deletePigFarm(ids, commonResponse);
    }

    @Override
    public void getPigFarmList(int pageNum) {
        ResponseObserver<PigFarmListEntity> commonResponse = getCommonResponse(new ResponseListener<PigFarmListEntity>() {
            @Override
            public void onSuccess(PigFarmListEntity listEntity) {
                if (listEntity.getTotal() == 0) {
                    ResponseException error = new ResponseException(ResponseException.ERROR.RESULT_CODE_201);
                    onError(error);
                } else {
                    getView().performPigFarmList(listEntity, pageNum);
                }
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PigFarmContract.TAG_PIG_FARM_LIST);
            }
        });
        mModel.getPigFarmList(pageNum, commonResponse);
    }

    @Override
    public void bindPigFarm(String id) {
        ResponseObserver<Object> commonResponse = getCommonResponse(new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object entity) {
                getView().performSuccess(PigFarmContract.TAG_PIG_FARM_BIND);
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, PigFarmContract.TAG_PIG_FARM_BIND);
            }
        });
        mModel.bindPigFarm(id, commonResponse);
    }
}
