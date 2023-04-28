package com.wuzi.pig.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.wuzi.pig.BuildConfig;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.module.main.SplashActivity;
import com.wuzi.pig.utils.manager.LoginManager;

import butterknife.BindView;
import butterknife.OnClick;

public class UserMainFragment extends BaseFragment {

    @BindView(R.id.user_name)
    AppCompatTextView mUserNameView;
    @BindView(R.id.version)
    AppCompatTextView mVersionView;

    private PasswordDialog mPasswordDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main_user;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        mUserNameView.setText(LoginManager.getUserName());
        mVersionView.setText("当前版本：" + BuildConfig.VERSION_NAME);
    }

    @OnClick({R.id.update_password, R.id.logout})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.update_password:{
                if (mPasswordDialog == null) {
                    mPasswordDialog = new PasswordDialog();
                    mPasswordDialog.setOnDismissListener(dialog -> {
                        mPasswordDialog = null;
                    });
                }
                mPasswordDialog.showNow(getFragmentManager());
                break;
            }
            case R.id.logout:{
                LoginManager.logout();
                startActivity(new Intent(mContext, SplashActivity.class));
                break;
            }
        }
    }
}
