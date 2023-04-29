package com.wuzi.pig.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseActivity;
import com.wuzi.pig.entity.UserEntity;
import com.wuzi.pig.module.main.SplashActivity;
import com.wuzi.pig.module.user.contract.LoginContract;
import com.wuzi.pig.module.user.presenter.LoginPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StatusbarColorUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.manager.LoginManager;
import com.wuzi.pig.utils.ui.LoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.IView {

    @BindView(R.id.error)
    AppCompatTextView mErrorView;
    @BindView(R.id.password_visible)
    View mPwdVisibleView;
    @BindView(R.id.username_value)
    AppCompatEditText mUserNameValueView;
    @BindView(R.id.password_value)
    AppCompatEditText mPwdValueView;

    private LoadingDialog mLoadingDialog;
    private RegisterDialog mRegisterDialog;
    private PasswordDialog mPasswordDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mLoadingDialog = new LoadingDialog(mContext);
        StatusBarUtils.immersive(getWindow());
        StatusbarColorUtils.setStatusBarDarkIcon(getWindow(), true);
        mUserNameValueView.setText(StringUtils.nullToString(LoginManager.getUserName()));
    }

    @OnClick({R.id.password_visible, R.id.submit, R.id.register, R.id.update_pwd})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.password_visible:{
                boolean selected = !mPwdVisibleView.isSelected();
                mPwdVisibleView.setSelected(selected);
                if (selected) {
                    mPwdValueView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordCursorToEnd();
                } else {
                    mPwdValueView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordCursorToEnd();
                }
                break;
            }
            case R.id.submit:{
                String username = mUserNameValueView.getText().toString();
                String password = mPwdValueView.getText().toString();
                if (StringUtils.isEmpty(username)) {
                    showError("请输入用户名");
                    break;
                } else if (StringUtils.isEmpty(password)) {
                    showError("请输入密码");
                    break;
                }
                showError(null);
                mLoadingDialog.show();
                mPresenter.onLogin(username, password);
                break;
            }
            case R.id.register:{
                if (mRegisterDialog == null) {
                    mRegisterDialog = new RegisterDialog();
                    mRegisterDialog.setOnDismissListener(dialog -> {
                        mRegisterDialog = null;
                    });
                }
                mRegisterDialog.showNow(getSupportFragmentManager());
                break;
            }
            case R.id.update_pwd:{
                if (mPasswordDialog == null) {
                    mPasswordDialog = new PasswordDialog();
                    mPasswordDialog.setOnDismissListener(dialog -> {
                        mPasswordDialog = null;
                    });
                }
                mPasswordDialog.showNow(getSupportFragmentManager());
                break;
            }
        }
    }

    private void passwordCursorToEnd() {
        View currentFocus = getCurrentFocus();
        if (currentFocus == mPwdValueView) {
            mPwdValueView.setSelection(mPwdValueView.getText().toString().length());
        }
        mPwdValueView.requestFocus();
    }

    private void showError(String error) {
        if (StringUtils.isEmpty(error)) {
            mErrorView.setVisibility(View.GONE);
        } else {
            mErrorView.setText(error);
            mErrorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void performLoginSuccress(UserEntity entity) {
        mLoadingDialog.hide();
        LoginManager.save(entity);
        startActivity(new Intent(mContext, SplashActivity.class));
        finish();
    }

    @Override
    public void performLoginError(ResponseException error) {
        mLoadingDialog.hide();
        mErrorView.setText(error.getPromptMessage());
        mErrorView.setVisibility(View.VISIBLE);
    }
}
