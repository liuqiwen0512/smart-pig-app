package com.wuzi.pig.module.alarm;

import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.wuzi.pig.R;
import com.wuzi.pig.constant.AlarmConstant;
import com.wuzi.pig.module.alarm.contract.AlarmContract;
import com.wuzi.pig.utils.LogUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ActivityFragment extends AlarmListFragment {

    @BindView(R.id.time_icon)
    ImageFilterView mTimeIconView;
    @BindView(R.id.name_icon)
    ImageFilterView mNameIconView;

    @Override
    protected int getLayoutID() {
        return R.layout.page_fragment_ear_tag;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mType = AlarmConstant.TYPE_ACTIVITY;
        super.initView(view, savedInstanceState);
        mQuery.type = mType;
        mQuery.desc = true;
        mQuery.orderColumn = AlarmConstant.ORDER_COLUMN_CREATE_TIME;
        setOrderParam(mQuery);
    }

    @OnClick({R.id.time, R.id.name})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.time: {
                if (isUpdating()) return;
                if (StringUtils.equals(mQuery.orderColumn, AlarmConstant.ORDER_COLUMN_CREATE_TIME)) {
                    mQuery.desc = !mQuery.desc;
                } else {
                    mQuery.desc = true;
                    mQuery.orderColumn = AlarmConstant.ORDER_COLUMN_CREATE_TIME;
                }
                setOrderParam(mQuery);
                mRefreshLayout.autoRefresh();
                break;
            }
            case R.id.name: {
                if (isUpdating()) return;
                if (StringUtils.equals(mQuery.orderColumn, AlarmConstant.ORDER_COLUMN_PIGSTY_NAME)) {
                    mQuery.desc = !mQuery.desc;
                } else {
                    mQuery.desc = true;
                    mQuery.orderColumn = AlarmConstant.ORDER_COLUMN_PIGSTY_NAME;
                }
                setOrderParam(mQuery);
                mRefreshLayout.autoRefresh();
                break;
            }
        }
    }

    private boolean isUpdating() {
        LogUtils.d(TAG, "isUpdating : isRefreshing : " + mRefreshLayout.isRefreshing()
                + "; isLoading : " + mRefreshLayout.isLoading());
        if (mRefreshLayout.getState() != RefreshState.None) {
            ToastUtils.show(R.string.prompt_loading);
            return true;
        }
        return false;
    }

    private void setOrderParam(AlarmContract.QueryEntity query) {
        if (StringUtils.equals(query.orderColumn, AlarmConstant.ORDER_COLUMN_CREATE_TIME)) {
            mNameIconView.setVisibility(View.GONE);
            mTimeIconView.setVisibility(View.VISIBLE);
            mTimeIconView.setSelected(!query.desc);
        }
        if (StringUtils.equals(query.orderColumn, AlarmConstant.ORDER_COLUMN_PIGSTY_NAME)) {
            mTimeIconView.setVisibility(View.GONE);
            mNameIconView.setVisibility(View.VISIBLE);
            mNameIconView.setSelected(!query.desc);
        }
    }
}
