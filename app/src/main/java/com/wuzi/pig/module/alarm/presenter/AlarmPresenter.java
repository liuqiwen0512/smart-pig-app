package com.wuzi.pig.module.alarm.presenter;

import com.wuzi.pig.base.BasePresenter;
import com.wuzi.pig.constant.AlarmConstant;
import com.wuzi.pig.entity.AlarmEntity;
import com.wuzi.pig.entity.AlarmListEntity;
import com.wuzi.pig.module.alarm.contract.AlarmContract;
import com.wuzi.pig.module.alarm.model.AlarmModel;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;
import com.wuzi.pig.utils.StringUtils;

import java.util.List;

public class AlarmPresenter extends BasePresenter<AlarmContract.IView, AlarmModel> implements AlarmContract.IPresenter {
    @Override
    public void getAlarmActivityList(AlarmContract.QueryEntity query) {
        final AlarmContract.QueryEntity queryClone = query.clone();
        ResponseObserver<AlarmListEntity> commonResponse = getCommonResponse(new ResponseListener<AlarmListEntity>() {
            @Override
            public void onSuccess(AlarmListEntity listEntity) {
                if (StringUtils.equals(queryClone.pigfarmId, "15")) {
                    if (StringUtils.equals(queryClone.type, AlarmConstant.TYPE_BASE_STATION)) {
                        initBaseStation15(listEntity);
                    } else if (StringUtils.equals(queryClone.type, AlarmConstant.TYPE_OUTAGE)) {
                        initOutage15(listEntity);
                    } else {
                        init15(listEntity);
                    }
                } else {
                    if (StringUtils.equals(queryClone.type, AlarmConstant.TYPE_BASE_STATION)) {
                        initBaseStation14(listEntity);
                    } else if (StringUtils.equals(queryClone.type, AlarmConstant.TYPE_OUTAGE)) {
                        initOutage14(listEntity);
                    } else {
                        init14(listEntity);
                    }
                }


                if (listEntity.getTotal() == 0) {
                    ResponseException error = new ResponseException(ResponseException.ERROR.RESULT_CODE_201);
                    onError(error);
                } else {
                    getView().performList(listEntity, queryClone);
                }
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, 0);
            }
        });
        mModel.getAlarmActivityList(queryClone, commonResponse);
    }

    @Override
    public void getAlarmHistoryList(AlarmContract.QueryEntity query) {
        final AlarmContract.QueryEntity queryClone = query.clone();
        ResponseObserver<AlarmListEntity> commonResponse = getCommonResponse(new ResponseListener<AlarmListEntity>() {
            @Override
            public void onSuccess(AlarmListEntity listEntity) {
                if (listEntity.getTotal() == 0) {
                    ResponseException error = new ResponseException(ResponseException.ERROR.RESULT_CODE_201);
                    onError(error);
                } else {
                    getView().performList(listEntity, queryClone);
                }
            }

            @Override
            public void onError(ResponseException exception) {
                getView().performError(exception, 0);
            }
        });
        mModel.getAlarmHistoryList(queryClone, commonResponse);
    }

    private AlarmListEntity init14(AlarmListEntity listEntity) {
        listEntity.setTotal(4);
        List<AlarmEntity> rows = listEntity.getRows();

        AlarmEntity entity = new AlarmEntity();
        entity.setEarTag("1");
        entity.setCreateTime("2023-04-20 10:05");
        entity.setPigstyName("猪栏141");
        entity.setLevel("高烧");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("2");
        entity.setCreateTime("2023-04-23 11:15");
        entity.setPigstyName("猪栏142");
        entity.setLevel("高烧");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("3");
        entity.setCreateTime("2023-04-24 12:30");
        entity.setPigstyName("猪栏143");
        entity.setLevel("低烧");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("4");
        entity.setCreateTime("2023-04-25 08:15");
        entity.setPigstyName("猪栏144");
        entity.setLevel("低烧");
        rows.add(entity);


        return listEntity;
    }

    private AlarmListEntity init15(AlarmListEntity listEntity) {
        listEntity.setTotal(4);
        List<AlarmEntity> rows = listEntity.getRows();

        AlarmEntity entity = new AlarmEntity();
        entity.setEarTag("1");
        entity.setCreateTime("2023-04-20 10:05");
        entity.setPigstyName("猪栏151");
        entity.setLevel("高烧");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("2");
        entity.setCreateTime("2023-04-23 11:15");
        entity.setPigstyName("猪栏152");
        entity.setLevel("高烧");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("3");
        entity.setCreateTime("2023-04-24 12:30");
        entity.setPigstyName("猪栏153");
        entity.setLevel("低烧");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("4");
        entity.setCreateTime("2023-04-25 08:15");
        entity.setPigstyName("猪栏154");
        entity.setLevel("低烧");
        rows.add(entity);


        return listEntity;
    }

    private AlarmListEntity initBaseStation14(AlarmListEntity listEntity) {
        listEntity.setTotal(4);
        List<AlarmEntity> rows = listEntity.getRows();

        AlarmEntity entity = new AlarmEntity();
        entity.setEarTag("1");
        entity.setCreateTime("2023-04-20 10:05");
        entity.setPigstyName("猪栏141");
        entity.setLevel("良好");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("2");
        entity.setCreateTime("2023-04-23 11:15");
        entity.setPigstyName("猪栏142");
        entity.setLevel("掉标");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("3");
        entity.setCreateTime("2023-04-24 12:30");
        entity.setPigstyName("猪栏143");
        entity.setLevel("良好");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("4");
        entity.setCreateTime("2023-04-25 08:15");
        entity.setPigstyName("猪栏144");
        entity.setLevel("掉标");
        rows.add(entity);


        return listEntity;
    }

    private AlarmListEntity initBaseStation15(AlarmListEntity listEntity) {
        listEntity.setTotal(4);
        List<AlarmEntity> rows = listEntity.getRows();

        AlarmEntity entity = new AlarmEntity();
        entity.setEarTag("1");
        entity.setCreateTime("2023-04-20 10:05");
        entity.setPigstyName("猪栏151");
        entity.setLevel("良好");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("2");
        entity.setCreateTime("2023-04-23 11:15");
        entity.setPigstyName("猪栏152");
        entity.setLevel("掉标");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("3");
        entity.setCreateTime("2023-04-24 12:30");
        entity.setPigstyName("猪栏153");
        entity.setLevel("良好");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("4");
        entity.setCreateTime("2023-04-25 08:15");
        entity.setPigstyName("猪栏154");
        entity.setLevel("掉标");
        rows.add(entity);


        return listEntity;
    }

    private AlarmListEntity initOutage14(AlarmListEntity listEntity) {
        listEntity.setTotal(4);
        List<AlarmEntity> rows = listEntity.getRows();

        AlarmEntity entity = new AlarmEntity();
        entity.setEarTag("1");
        entity.setCreateTime("2023-04-20 10:05");
        entity.setPigstyName("猪场141");
        entity.setLevel("良好");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("2");
        entity.setCreateTime("2023-04-23 11:15");
        entity.setPigstyName("猪场142");
        entity.setLevel("掉标");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("3");
        entity.setCreateTime("2023-04-24 12:30");
        entity.setPigstyName("猪场143");
        entity.setLevel("良好");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("4");
        entity.setCreateTime("2023-04-25 08:15");
        entity.setPigstyName("猪场144");
        entity.setLevel("掉标");
        rows.add(entity);


        return listEntity;
    }

    private AlarmListEntity initOutage15(AlarmListEntity listEntity) {
        listEntity.setTotal(4);
        List<AlarmEntity> rows = listEntity.getRows();

        AlarmEntity entity = new AlarmEntity();
        entity.setEarTag("1");
        entity.setCreateTime("2023-04-20 10:05");
        entity.setPigstyName("猪场151");
        entity.setLevel("良好");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("2");
        entity.setCreateTime("2023-04-23 11:15");
        entity.setPigstyName("猪场152");
        entity.setLevel("掉标");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("3");
        entity.setCreateTime("2023-04-24 12:30");
        entity.setPigstyName("猪场153");
        entity.setLevel("良好");
        rows.add(entity);

        entity = new AlarmEntity();
        entity.setEarTag("4");
        entity.setCreateTime("2023-04-25 08:15");
        entity.setPigstyName("猪场154");
        entity.setLevel("掉标");
        rows.add(entity);


        return listEntity;
    }


}
