package com.wuzi.pig.module.management;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatTextView;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseDialogFragment;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.module.management.contract.PigFarmContract;
import com.wuzi.pig.module.management.presenter.PigFarmPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.tools.TimeUtils;
import com.wuzi.pig.utils.ui.LoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class PigFarmBindDialog extends BaseDialogFragment<PigFarmPresenter> implements PigFarmContract.IView {

    @BindView(R.id.message)
    AppCompatTextView mMessageView;
    @BindView(R.id.error)
    AppCompatTextView mErrorView;
    @BindView(R.id.ok)
    View mOkView;
    @BindView(R.id.cancel)
    View mCancelView;

    private LoadingDialog mLoadingDialog;
    private Function<Object> mSubmitListener;
    private PigFarmEntity mPigFarmEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_pigfarm_bind;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mLoadingDialog = new LoadingDialog(mContext);
        setPigFarmEntity(mPigFarmEntity);
    }

    @OnClick({R.id.cancel, R.id.ok})
    public void onClickView(View v) {
        if (!TimeUtils.havePast200msec()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ok: {
                showError(null);
                mLoadingDialog.show();
                mPresenter.bindPigFarm(mPigFarmEntity.getPigfarmId());
                break;
            }
            case R.id.cancel: {
                dismiss();
                break;
            }
        }
    }

    @Override
    protected void initWindowTheme(Dialog dialog, Window window) {
        defualtWindowTheme(dialog, window);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (UIUtils.getScreenWidth(mContext) * 0.9f);
        attributes.height = (int) (attributes.width * 0.6f);
        window.setAttributes(attributes);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void performSuccess(Object data, int fromTag) {
        mLoadingDialog.hide();
        ToastUtils.show("绑定成功");
        dismiss();
        if (mSubmitListener != null) {
            mSubmitListener.action(null);
        }
    }

    @Override
    public void performPigFarmList(PigFarmListEntity listEntity, int pageNum) {

    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        mLoadingDialog.hide();
        showError(error.getPromptMessage());
    }

    public void setSubmitListener(Function<Object> submitListener) {
        mSubmitListener = submitListener;
    }

    public void setPigFarmEntity(PigFarmEntity pigFarmEntity) {
        mPigFarmEntity = pigFarmEntity;
        if (mMessageView != null && pigFarmEntity != null) {
            mMessageView.setText(pigFarmEntity.getPigfarmName());
        }
    }

    private void showError(String error) {
        if (StringUtils.isEmpty(error)) {
            mErrorView.setVisibility(View.GONE);
        } else {
            mErrorView.setText(error);
            mErrorView.setVisibility(View.VISIBLE);
        }
    }
}
