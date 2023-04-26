package com.wuzi.pig.module.main;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.utils.SpannableUtils;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.UIUtils;

import butterknife.BindView;

public class MainFragment extends BaseFragment {

    @BindView(R.id.pig_statis_today_value)
    AppCompatTextView mPigStatisTodayValueView;
    @BindView(R.id.pig_statis_yesterday_value)
    AppCompatTextView mPigStatisYesterdayValueView;
    @BindView(R.id.pig_statis_before_yesterday_value)
    AppCompatTextView mPigStatisBeforeYesterdayValueView;
    @BindView(R.id.pig_statis_health_value)
    AppCompatTextView mPigStatisHealthValueView;
    @BindView(R.id.pig_statis_equipment_value)
    AppCompatTextView mPigStatisEquipmentValueView;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);

        int color = getResources().getColor(R.color.grey_3a);
        SpannableStringBuilder highlightSpannable = SpannableUtils.getHighlightSpannable("90808 头", "90808", color);
        SpannableStringBuilder boldSpannable = SpannableUtils.getBoldSpannable(highlightSpannable, "90808 头", "90808");
        SpannableStringBuilder sizeSpannable = SpannableUtils.getSizeSpannable(boldSpannable, "90808 头", "90808", 18);

        mPigStatisTodayValueView.setText(sizeSpannable);
        mPigStatisYesterdayValueView.setText(sizeSpannable);
        mPigStatisBeforeYesterdayValueView.setText(sizeSpannable);
        mPigStatisHealthValueView.setText(sizeSpannable);
        mPigStatisEquipmentValueView.setText(sizeSpannable);
    }
}
