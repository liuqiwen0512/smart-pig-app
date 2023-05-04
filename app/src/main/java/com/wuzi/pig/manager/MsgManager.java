package com.wuzi.pig.manager;

import android.os.Message;

import com.wuzi.pig.constant.MsgConstant;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.utils.msg.MsgHandler;

import java.util.List;

public class MsgManager {

    private MsgManager() {
    }

    //显示猪场选择dialog
    public static void showPigFarmDialog() {
        Message obtain = Message.obtain();
        obtain.what = MsgConstant.MSG_WHAT_SHOW_PIGFARM_DIALOG;
        MsgHandler.getInstances().sendMessage(obtain);
    }

    //选择猪场
    public static void selectionPigFarm(PigFarmEntity entity) {
        Message obtain = Message.obtain();
        obtain.what = MsgConstant.MSG_WHAT_PIGFARM_CHANGE;
        obtain.obj = entity;
        MsgHandler.getInstances().sendMessage(obtain);
    }

    //删除猪场
    public static void deletePigFarm(List<String> ids) {
        Message obtain = Message.obtain();
        obtain.what = MsgConstant.MSG_WHAT_PIGFARM_DELETE;
        obtain.obj = ids;
        MsgHandler.getInstances().sendMessage(obtain);
    }

}
