package com.wuzi.pig.module.user;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseDialogFragment;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.module.user.contract.RegisterContract;
import com.wuzi.pig.module.user.presenter.RegisterPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StatusbarColorUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.ui.LoadingDialog;
import com.wuzi.pig.utils.ui.ViewCompat;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterDialog extends BaseDialogFragment<RegisterPresenter> implements RegisterContract.IView {

    @BindView(R.id.phone)
    AppCompatEditText mPhoneValueView;
    @BindView(R.id.verification_code_value)
    AppCompatEditText mVerificationCodeValueView;
    @BindView(R.id.verification_code_send)
    AppCompatTextView mVerificationCodeSendView;
    @BindView(R.id.password)
    AppCompatEditText mPwdValueView;
    @BindView(R.id.password_visible)
    View mPwdVisibleView;
    @BindView(R.id.error)
    AppCompatTextView mErrorView;

    private Handler mHandler;
    private LoadingDialog mLoadingDialog;
    private int mCountdown;
    private final int mMaxCountdown = 60;

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_register;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mLoadingDialog = new LoadingDialog(mContext);
        mHandler = new HandlerImpl(this);
    }

    @OnClick({R.id.back, R.id.verification_code_send, R.id.password_visible, R.id.submit})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.back:{
                dismiss();
                break;
            }
            case R.id.verification_code_send:{
                String phone = mPhoneValueView.getText().toString();
                if (StringUtils.isEmpty(phone) || phone.length() != 11) {
                    UIUtils.doHuaweiToastCovered(getWindow());
                    ToastUtils.show("请输入手机号");
                } else if (mCountdown <= 0) {
                    verificationCodeCountdown(mMaxCountdown);
                    mPresenter.verificationCode(phone);
                }
                break;
            }
            case R.id.password_visible:{
                boolean selected = !mPwdVisibleView.isSelected();
                mPwdVisibleView.setSelected(selected);
                if (selected) {
                    mPwdValueView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ViewCompat.passwordCursorToEnd(getActivity(), mPwdValueView);
                } else {
                    mPwdValueView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ViewCompat.passwordCursorToEnd(getActivity(), mPwdValueView);
                }
                break;
            }
            case R.id.submit:{
                String phone = mPhoneValueView.getText().toString();
                String password = mPwdValueView.getText().toString();
                String verificationCode = mVerificationCodeValueView.getText().toString();
                if (StringUtils.isEmpty(phone)) {
                    showError("请输入手机号");
                    break;
                } else if (StringUtils.isEmpty(verificationCode)) {
                    showError("请输入验证码");
                    break;
                } else if (StringUtils.isEmpty(password)) {
                    showError("请输入密码");
                    break;
                } else if (password.length() < 6) {
                    showError("密码不能小于6位");
                    break;
                } else if (password.contains(" ")) {
                    showError("密码不能包含空格");
                    break;
                }
                showError(null);
                mLoadingDialog.show();
                mPresenter.register(phone, password, verificationCode);
                break;
            }
        }
    }

    @Override
    public void performRegisterSuccress(UserEntity entity) {
        mLoadingDialog.hide();
        showError(null);
        ToastUtils.show("注册成功");
        dismiss();
    }

    @Override
    public void performVerificationCodeSuccress() {
        ToastUtils.show("验证码发送成功");
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        mLoadingDialog.hide();
        if (fromTag == RegisterContract.TAG_ERROR_REGISTER) {
            showError(error.getPromptMessage());
        } else {
            ToastUtils.show(error.getPromptMessage());
        }
    }

    @Override
    protected void initWindowTheme(Dialog dialog, Window window) {
        StatusbarColorUtils.setStatusBarDarkIcon(getWindow(), true);
        StatusBarUtils.immersive(window);
        fullDialog(dialog, window);
    }

    private void showError(String error) {
        if (StringUtils.isEmpty(error)) {
            mErrorView.setVisibility(View.GONE);
        } else {
            mErrorView.setText(error);
            mErrorView.setVisibility(View.VISIBLE);
        }
    }

    private void showVerificationCodeLabel(int second) {
        if (second <= 0) {
            mVerificationCodeSendView.setText(getResources().getString(R.string.verification_code_send));
        } else {
            mVerificationCodeSendView.setText(getResources().getString(R.string.verification_code_timer, String.valueOf(second)));
        }
    }

    private void verificationCodeCountdown(int startSecond) {
        mCountdown = startSecond;
        showVerificationCodeLabel(mCountdown);
        mHandler.removeMessages(HandlerImpl.WHAT_TIMER);
        if (startSecond >= 1) {
            mHandler.sendEmptyMessageDelayed(HandlerImpl.WHAT_TIMER, 1000);
        }
    }

    private static class HandlerImpl extends Handler {
        public final static int WHAT_TIMER = 100;
        private WeakReference<RegisterDialog> mDialogWeakRef;

        public HandlerImpl(RegisterDialog dialog) {
            mDialogWeakRef = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_TIMER:{
                    RegisterDialog dialog = mDialogWeakRef.get();
                    if (dialog == null) return;
                    dialog.mCountdown--;
                    dialog.verificationCodeCountdown(dialog.mCountdown);
                    break;
                }
            }
        }
    }
}
