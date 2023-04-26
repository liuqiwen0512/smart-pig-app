package com.wuzi.pig.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import com.wuzi.pig.BuildConfig;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StatusbarColorUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * view 层
 */
public abstract class BaseActivity<P extends IContract.IPresenter> extends AppCompatActivity implements IContract.IView {
    protected String TAG = getClass().getSimpleName();
    private Unbinder mBind;
    protected Context mContext;
    protected Activity mActivity;
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutID = getLayoutID();
        if (layoutID != 0) {
            setContentView(layoutID);
        }
        mBind = ButterKnife.bind(this);
        mContext = this;
        mActivity = this;
        //StatusbarColorUtils.setStatusBarDarkIcon(this, true);
        //changeStatusBarBg(R.color.colorWindowBg);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        init(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
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
    }

    // 改变状态栏的背景色
    public void changeStatusBarBg(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtils.forStatusBar(this, ContextCompat.getColor(this, color));
        }
    }

    /**
     * 是否需要展示
     */
    public boolean needFullScreen() {
        return false;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    // 获取资源id
    protected abstract int getLayoutID();

    // 初始化
    protected abstract void init(Bundle savedInstanceState);

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
