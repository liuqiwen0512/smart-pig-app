package com.wuzi.pig.net.factory;

import androidx.lifecycle.Lifecycle;
import java.lang.ref.WeakReference;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 网络响应观察
 */
public class ResponseObserver<T> implements Observer<T> {
    private Lifecycle mLifecycleOwner;
    private ResponseListener<T> mResponseListener;
    private int mLoadingArg;
    private Disposable mDisposable;

    public ResponseObserver() {

    }

    public ResponseObserver(Lifecycle lifecycle) {
        mLifecycleOwner = lifecycle;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (isLifecycleActive()) {
            this.mDisposable = d;
            showLoading();
            if (mResponseListener != null) {
                mResponseListener.onSubscribe(d);
            }
        }
    }


    @Override
    public void onNext(T t) {
        if (isLifecycleActive()) {
            if (mResponseListener != null) {
                mResponseListener.onSuccess(t);
            }
        } else {
            dispose();
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        hideLoading();
        if (isLifecycleActive()) {
            if (mResponseListener != null) {
                if (e instanceof ResponseException) {
                    mResponseListener.onError((ResponseException) e);
                } else {
                    ResponseException responseException = NetExceptionUtils.handleException(e);
                    mResponseListener.onError(responseException);
                }
            }
        }
    }

    @Override
    public void onComplete() {
        dispose();
        hideLoading();
    }

    private void dispose() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void showLoading() {

    }

    private void hideLoading() {

    }

    public ResponseObserver<T> setResponseListener(ResponseListener<T> responseListener) {
        mResponseListener = responseListener;
        return this;
    }

    public ResponseObserver<T> setLifecycle(Lifecycle lifecycle) {
        mLifecycleOwner = lifecycle;
        return this;
    }

    public ResponseObserver<T> setLoadingArg(int loadingArg) {
        mLoadingArg = loadingArg;
        return this;
    }

    /**
     * 判断宿主是否处于活动状态
     */
    private boolean isLifecycleActive() {
        if (mLifecycleOwner == null) {
            return true;
        }
        return mLifecycleOwner.getCurrentState() != Lifecycle.State.DESTROYED;
    }
}
