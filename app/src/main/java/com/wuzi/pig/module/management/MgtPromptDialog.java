package com.wuzi.pig.module.management;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

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

public class MgtPromptDialog extends BaseDialogFragment {

    private final static int TYPE_PROMPT_PIGFARM = 100;
    private final static int TYPE_PROMPT_PIGSTY = 200;

    @BindView(R.id.title)
    AppCompatTextView mTitleView;
    @BindView(R.id.message)
    AppCompatTextView mMessageView;

    private Function<Boolean> mClickListener;
    private int mType = 100;

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_mgt_prompt;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        switch (mType) {
            case TYPE_PROMPT_PIGFARM:{
                mTitleView.setText(R.string.mgt_pigfarm_delete_prompt_title);
                mMessageView.setText(R.string.mgt_pigfarm_delete_prompt_message);
                break;
            }
            case TYPE_PROMPT_PIGSTY:{
                mTitleView.setText(R.string.mgt_pigsty_delete_prompt_title);
                mMessageView.setText(R.string.mgt_pigsty_delete_prompt_message);
                break;
            }
        }
    }

    @OnClick({R.id.cancel, R.id.ok})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.ok:{
                dismiss();
                if (mClickListener != null) {
                    mClickListener.action(true);
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

    public void setClickListener(Function<Boolean> clickListener) {
        mClickListener = clickListener;
    }

    //删除猪场
    public static void startPigfarmPrompt(FragmentManager fragmentManager, Function<Boolean> clickListener) {
        if (!TimeUtils.havePast200msec()) return;
        MgtPromptDialog dialog = new MgtPromptDialog();
        dialog.mType = TYPE_PROMPT_PIGFARM;
        dialog.mClickListener = clickListener;
        dialog.showNow(fragmentManager);
    }

    //删除猪栏
    public static void startPigstyPrompt(FragmentManager fragmentManager, Function<Boolean> clickListener) {
        if (!TimeUtils.havePast200msec()) return;
        MgtPromptDialog dialog = new MgtPromptDialog();
        dialog.mType = TYPE_PROMPT_PIGSTY;
        dialog.mClickListener = clickListener;
        dialog.showNow(fragmentManager);
    }
}
