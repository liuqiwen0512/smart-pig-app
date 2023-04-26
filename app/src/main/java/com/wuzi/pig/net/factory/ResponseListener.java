package com.wuzi.pig.net.factory;

import io.reactivex.disposables.Disposable;

/**
 * 对外提供接口回调
 */
public abstract class ResponseListener<T> {

    public void onSubscribe(Disposable disposable) {}

    abstract public void onSuccess(T entity);

    public void onError(ResponseException exception) {

    }
}
