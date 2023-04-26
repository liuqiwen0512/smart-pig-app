package com.wuzi.pig.base;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseModel implements IContract.IModel {

    protected void apiSubscribe(Observable observable, Observer observer) {
        if (observable != null && observer != null) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }
}
