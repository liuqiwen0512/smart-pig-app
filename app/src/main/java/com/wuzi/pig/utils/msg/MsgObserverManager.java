package com.wuzi.pig.utils.msg;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class MsgObserverManager {
    private ArrayList<IMsgObserver> mObserverList = null;
    private static volatile MsgObserverManager mMsgObserverManager = null;

    public MsgObserverManager() {
        mObserverList = new ArrayList<>();
    }

    public static MsgObserverManager getInstances() {
        if (mMsgObserverManager == null) {
            synchronized (MsgObserverManager.class) {
                if (mMsgObserverManager == null) {
                    mMsgObserverManager = new MsgObserverManager();
                }
            }
        }
        return mMsgObserverManager;
    }

    public void addObserver(IMsgObserver IMsgObserver) {
        if (mObserverList != null) {
            WeakReference<IMsgObserver> reference = new WeakReference(IMsgObserver);
            mObserverList.add(reference.get());
        }
    }

    public void removeObserver(IMsgObserver IMsgObserver) {
        try {
            if (mObserverList != null && mObserverList.contains(IMsgObserver)) {
                mObserverList.remove(IMsgObserver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAllObserver() {
        try {
            if (mObserverList != null) {
                Iterator<IMsgObserver> iterator = mObserverList.iterator();
                while (iterator.hasNext()) {
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int currentSize() {
        if (mObserverList != null) {
            return mObserverList.size();
        }
        return 0;
    }

    public ArrayList<IMsgObserver> getObserverList() {
        if (mObserverList != null) {
            return mObserverList;
        }
        return null;
    }
}
