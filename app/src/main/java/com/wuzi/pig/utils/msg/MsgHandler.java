package com.wuzi.pig.utils.msg;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.wuzi.pig.base.BaseApplication;

import java.util.ArrayList;

/**
 * 事件路由
 */
public class MsgHandler {
    private String TAG = getClass().getSimpleName();
    private static volatile MsgHandler mMsgHandler;
    private Context mContext;
    private Handler mHandler = null;

    public MsgHandler(Context context) {
        mContext = context;
        init();
    }

    public void onDestroy() {
        if (mContext != null) {
            mContext = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        MsgObserverManager.getInstances().removeAllObserver();
    }

    private void init() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                int what = msg.what;
                distributeMessage(what, msg);
            }
        };
    }

    private void distributeMessage(int what, Message message) {
        ArrayList<IMsgObserver> observerList = MsgObserverManager.getInstances().getObserverList();
        if (observerList != null && observerList.size() > 0) {
            for (int i = 0; i < observerList.size(); i++) {
                IMsgObserver info = null;
                try {
                    info = observerList.get(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (info != null) {
                    info.onMessage(what, message);
                }
            }
        }
    }

    public void sendMessage(Message message) {
        if (mHandler != null) {
            mHandler.sendMessage(message);
        }
    }

    public static MsgHandler getInstances() {
        if (mMsgHandler == null) {
            synchronized (MsgHandler.class) {
                if (mMsgHandler == null) {
                    mMsgHandler = new MsgHandler(BaseApplication.getApplication());
                }
            }
        }
        return mMsgHandler;
    }
}
