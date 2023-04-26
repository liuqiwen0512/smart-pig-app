package com.wuzi.pig.base;

import android.content.Context;

import androidx.lifecycle.Lifecycle;

import com.wuzi.pig.net.factory.ResponseListener;
import com.wuzi.pig.net.factory.ResponseObserver;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * P 层
 */
public abstract class BasePresenter<V extends IContract.IView, M extends IContract.IModel> implements IContract.IPresenter<V> {
    protected String TAG = getClass().getSimpleName();
    protected M mModel;
    private WeakReference<V> mViewWeakRef;

    public BasePresenter() {
        mModel = createModel();
    }

    // 绑定View
    @Override
    public void attachView(V v) {
        mViewWeakRef = new WeakReference<>(v);
    }

    // 解绑View
    @Override
    public void detachView() {
        if (mViewWeakRef != null) {
            mViewWeakRef.clear();
            mViewWeakRef = null;
        }
    }


    // 获取 view 层
    public V getView() {
        return mViewWeakRef == null ? null : mViewWeakRef.get();
    }

    public Lifecycle getLifecycle() {
        V view = getView();
        return view == null ? null : view.getLifecycle();
    }

    public Context getContext() {
        V view = getView();
        return view == null ? null : view.getContext();
    }

    protected M createModel() {
        if (mModel != null) {
            return mModel;
        }
        try {
            Type type = getClass().getGenericSuperclass();
            if (type != null && type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                if (actualTypeArguments.length > 1) {
                    Class<M> clazz = (Class<M>) actualTypeArguments[1];
                    mModel = clazz.newInstance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mModel;
    }

    protected boolean isLifecycleActive() {
        Lifecycle lifecycle = getLifecycle();
        return lifecycle != null && lifecycle.getCurrentState() != Lifecycle.State.DESTROYED;
    }

    protected <T> ResponseObserver<T> getCommonResponse(ResponseListener<T> listener) {
        return new ResponseObserver<T>(getLifecycle())
                .setResponseListener(listener);
    }

}
