package com.wuzi.pig.net;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApiMethod {

    /**
     * 封装线程管理和订阅的过程
     * observable 被观察者
     * observer 观察者
     */
    public static void ApiSubscribe(Observable observable, Observer observer) {
        if (observable != null && observer != null) {
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

}
