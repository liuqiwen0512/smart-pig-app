package com.wuzi.pig.module.management;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.AppCompatTextView;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseDialogFragment;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.module.management.contract.PigstyContract;
import com.wuzi.pig.module.management.presenter.PigstyPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.LogUtils;
import com.wuzi.pig.utils.QR.QRActivityResultContract;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StatusbarColorUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.XXPermissionsUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.tools.TimeUtils;
import com.wuzi.pig.utils.ui.LoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class PigstyEditDialog extends BaseDialogFragment<PigstyPresenter> implements PigstyContract.IView {

    @BindView(R.id.name_value)
    AppCompatTextView mNameValueView;
    @BindView(R.id.code_value)
    AppCompatTextView mCodeValueView;
    @BindView(R.id.qr_hint)
    AppCompatTextView mQRHintView;
    @BindView(R.id.barcode_scanner)
    DecoratedBarcodeView mBarcodeScannerView;
    @BindView(R.id.error)
    AppCompatTextView mErrorView;

    private ActivityResultLauncher<Object> mQRCodeLauncher;
    private LoadingDialog mLoadingDialog;
    private Function<Object> mSubmitListener;
    private PigFarmEntity mPigFarmEntity;
    private PigstyEntity mOldPigstyEntity;
    private PigstyEntity mNewPigstyEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_pigsty_add;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mLoadingDialog = new LoadingDialog(mContext);
        mQRCodeLauncher = registerForActivityResult(new QRActivityResultContract<Object, String>(getActivity()), result -> {
            if (!StringUtils.isEmpty(result) && result.contains("/")) {
                String[] split = result.split("/");
                mNewPigstyEntity = new PigstyEntity();
                mNewPigstyEntity.setPigfarmId(mPigFarmEntity.getPigfarmId());
                mNewPigstyEntity.setPigstyName(split[0]);
                mNewPigstyEntity.setBaseStation(split[1]);
                setPigstyMessage(mNewPigstyEntity.getPigstyName(), mNewPigstyEntity.getBaseStation());
                LogUtils.e(TAG, "扫描结果 :" + result);
            }
        });
        setPigstyEntity(mOldPigstyEntity);
    }

    @OnClick({R.id.back, R.id.name_value, R.id.code_value, R.id.submit})
    public void onClickView(View v) {
        if (!TimeUtils.havePast200msec()) {
            return;
        }
        switch (v.getId()) {
            case R.id.back: {
                dismiss();
                break;
            }
            case R.id.name_value:
            case R.id.code_value: {
                showError(null);
                XXPermissionsUtils.getInstances().hasCameraPermission(() -> {
                    mQRCodeLauncher.launch(null);
                }, mContext);
                break;
            }
            case R.id.submit: {
                if (mNewPigstyEntity != null) {
                    mPresenter.addPigsty(mNewPigstyEntity);
                    showError(null);
                } else {
                    showError("请先扫描二维码获取猪栏信息");
                }
                break;
            }
        }
    }

    @Override
    protected void initWindowTheme(Dialog dialog, Window window) {
        fullDialog(dialog, window);
        StatusBarUtils.immersive(window);
        StatusBarUtils.setPadding(mContext, getView());
        StatusbarColorUtils.setStatusBarDarkIcon(window, true);
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
    public void performPigstyList(PigstyListEntity listEntity, int pageNum) {

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

    public void setPigFarmEntity(PigFarmEntity pigFarmEntity) {
        mPigFarmEntity = pigFarmEntity;
    }

    public void setPigstyEntity(PigstyEntity oldPigstyEntity) {
        mOldPigstyEntity = oldPigstyEntity;
        if (oldPigstyEntity != null) {
            setPigstyMessage(mOldPigstyEntity.getPigstyName(), mOldPigstyEntity.getBaseStation());
        } else {
            setPigstyMessage(null, null);
        }
    }

    private void setPigstyMessage(String name, String code) {
        if (mNameValueView != null) {
            mNameValueView.setText(!StringUtils.isEmpty(name) ? StringUtils.ASCII16ToString(name)
                    : getString(R.string.mgt_pigsty_add_name_hint));
        }
        if (mCodeValueView != null) {
            mCodeValueView.setText(!StringUtils.isEmpty(code) ? code
                    : getString(R.string.mgt_pigsty_add_code_hint));
            mQRHintView.setVisibility(StringUtils.isEmpty(code) ? View.GONE : View.VISIBLE);
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
