package com.wuzi.pig.module.management;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseDialogFragment;
import com.wuzi.pig.constant.PigFarmConstant;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.module.management.contract.PigFarmContract;
import com.wuzi.pig.module.management.presenter.PigFarmPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.listener.TextWatcherImpl;
import com.wuzi.pig.utils.tools.TimeUtils;
import com.wuzi.pig.utils.ui.LoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class PigFarmAddDialog extends BaseDialogFragment<PigFarmPresenter> implements PigFarmContract.IView {

    @BindView(R.id.title)
    AppCompatTextView mTitleView;
    @BindView(R.id.name_value)
    AppCompatEditText mNameValueView;
    @BindView(R.id.name_length)
    AppCompatTextView mNameLengthView;
    @BindView(R.id.error)
    AppCompatTextView mErrorView;

    private LoadingDialog mLoadingDialog;
    private Function<Object> mSubmitListener;

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_pigfarm_add;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mLoadingDialog = new LoadingDialog(mContext);
        mNameLengthView.setText("0/" + PigFarmConstant.PIG_FARM_NAME_MAX_LENGTH);
        mNameValueView.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int length = text.length();
                if (length > PigFarmConstant.PIG_FARM_NAME_MAX_LENGTH) {
                    showError("名字长度不能超过" + PigFarmConstant.PIG_FARM_NAME_MAX_LENGTH);
                } else {
                    showError(null);
                }
                mNameLengthView.setText(length + "/" + PigFarmConstant.PIG_FARM_NAME_MAX_LENGTH);
            }
        });
    }

    @OnClick({R.id.cancel, R.id.ok})
    public void onClickView(View v) {
        if (!TimeUtils.havePast200msec()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ok:{
                mLoadingDialog.show();
                String name = mNameValueView.getText().toString();
                if (name.length() <= PigFarmConstant.PIG_FARM_NAME_MAX_LENGTH) {
                    mPresenter.addPigFarm(name);
                }
                break;
            }
            case R.id.cancel:{
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
        window.setAttributes(attributes);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void performSuccess(int fromTag) {
        ToastUtils.show("添加成功");
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

    @Override
    public void dismiss() {
        mLoadingDialog.hide();
        super.dismiss();
    }

    public void setSubmitListener(Function<Object> submitListener) {
        mSubmitListener = submitListener;
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
