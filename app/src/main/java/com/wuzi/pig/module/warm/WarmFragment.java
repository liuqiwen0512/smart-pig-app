package com.wuzi.pig.module.warm;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.utils.SpannableUtils;
import com.wuzi.pig.utils.StatusBarUtils;

import butterknife.BindView;

public class WarmFragment extends BaseFragment {

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_warm;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);

    }
}
