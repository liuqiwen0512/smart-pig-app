package com.wuzi.pig.manager;

import android.os.Message;

import com.wuzi.pig.constant.MsgConstant;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.utils.msg.MsgHandler;

public class MsgManager {

    private MsgManager() {
    }

    public static void showPigFarmDialog() {
        Message obtain = Message.obtain();
        obtain.what = MsgConstant.MSG_WHAT_SHOW_PIGFARM_DIALOG;
        MsgHandler.getInstances().sendMessage(obtain);
    }

    public static void selectionPigFarm(PigFarmEntity entity) {
        Message obtain = Message.obtain();
        obtain.what = MsgConstant.MSG_WHAT_PIGFARM_CHANGE;
        obtain.obj = entity;
        MsgHandler.getInstances().sendMessage(obtain);
    }

}
