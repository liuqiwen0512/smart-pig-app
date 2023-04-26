package com.wuzi.pig.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.wuzi.pig.utils.LogUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * fragment 基类
 */
public abstract class BaseFragment<P extends IContract.IPresenter> extends Fragment implements IContract.IView {
    protected String TAG = getClass().getSimpleName();
    private Unbinder mBind;
    protected P mPresenter;
    protected Context mContext;
    protected boolean mIsRefresh;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutID(), container, false);
        mBind = ButterKnife.bind(this, view);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    protected void changeStatusBarBg(int color) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity)activity).changeStatusBarBg(color);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mContext = null;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    // 获取资源id
    protected abstract int getLayoutID();

    // 子类实现
    protected abstract void initView(View view, Bundle savedInstanceState);

    protected P createPresenter() {
        if (mPresenter != null) {
            return mPresenter;
        }
        try {
            Type type = getClass().getGenericSuperclass();
            if (type != null && type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                    Class<P> clazz = (Class<P>) actualTypeArguments[0];
                    mPresenter = clazz.newInstance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mPresenter;
    }


}
