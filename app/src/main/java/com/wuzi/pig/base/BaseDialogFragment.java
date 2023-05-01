package com.wuzi.pig.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.wuzi.pig.R;
import com.wuzi.pig.utils.UIUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * dialogFragment 基类
 */
public abstract class BaseDialogFragment<P extends IContract.IPresenter> extends DialogFragment implements IContract.IView {
    protected String TAG = getClass().getSimpleName();
    private Unbinder mBind;
    protected P mPresenter;
    protected Context mContext;
    protected DialogInterface.OnDismissListener mDismissListener;
    protected boolean mSaveInstanceState = false;

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
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() != null && getDialog().getWindow() != null) {
            initWindowTheme(getDialog(), getDialog().getWindow());
        }
    }

    protected void initWindowTheme(Dialog dialog, Window window) {
        defualtWindowTheme(dialog, window);
    }

    protected void fullDialog(Dialog dialog, Window window) {
        fullDialog(dialog, window, R.style.dialog_fade_style2);
    }

    protected void fullDialog(Dialog dialog, Window window, int style) {
        //setStyle(STYLE_NO_TITLE, android.R.style.Theme_NoTitleBar);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_NoTitleBar_Fullscreen);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.windowAnimations = style;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.dimAmount = 0;
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setAttributes(attributes);
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext, android.R.color.transparent)));
    }

    protected void defualtWindowTheme(Dialog dialog, Window window) {
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.windowAnimations = R.style.dialog_fade_style;
        attributes.dimAmount = 0.35f;
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setAttributes(attributes);
        window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext, android.R.color.transparent)));
    }

    protected void defualtWindowTheme2(Dialog dialog, Window window) {
        defualtWindowTheme(dialog, window);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (UIUtils.getScreenWidth(mContext) * 0.8f);
        window.setAttributes(attributes);
    }

    protected Window getWindow() {
        Dialog dialog = getDialog();
        return dialog == null ? null : dialog.getWindow();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mDismissListener != null) {
            mDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mSaveInstanceState) {
            super.onSaveInstanceState(outState);
        }
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

    public void showNow(FragmentManager manager) {
        showNow(manager, TAG);
    }

    public void showNow(FragmentManager manager, String tag) {
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null && fragment.isAdded()) {
            return;
        }
        super.showNow(manager, tag);
    }
}
