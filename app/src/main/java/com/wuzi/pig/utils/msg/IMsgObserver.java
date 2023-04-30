package com.wuzi.pig.utils.msg;

import android.os.Message;

public interface IMsgObserver {

    void onMessage(int what, Message message);
}
