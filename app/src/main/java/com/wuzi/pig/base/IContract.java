package com.wuzi.pig.base;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

public interface IContract {
    interface IView extends LifecycleOwner {

        Context getContext();
    }

    interface IPresenter<V> {
        void attachView(V v);
        void detachView();
    }

    interface IModel {

    }
}
