package com.wuzi.pig.module.alarm;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
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


public class BaseStationFragment extends AlarmListFragment {

    @BindView(R.id.name_icon)
    ImageFilterView mNameIconView;
    @BindView(R.id.time_icon)
    ImageFilterView mTimeIconView;
    @BindView(R.id.status_label)
    AppCompatTextView mStatusLabelView;
    @BindView(R.id.status_icon)
    ImageFilterView mStatusIconView;

    @Override
    protected int getLayoutID() {
        return R.layout.page_fragment_base_station;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mType = AlarmConstant.TYPE_BASE_STATION;
        super.initView(view, savedInstanceState);
        mQuery.type = mType;
        mQuery.desc = true;
        mQuery.orderColumn = AlarmConstant.ORDER_COLUMN_CREATE_TIME;
        mQuery.allStatus = AlarmConstant.ALLSTATUS_ALL;
        setOrderParam(mQuery);
    }

    @OnClick({R.id.name, R.id.time, R.id.status})
    public void onClickView(View v) {
        switch (v.getId()) {
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
            case R.id.status: {
                if (isUpdating()) return;
                if (StringUtils.equals(mQuery.allStatus, AlarmConstant.ALLSTATUS_ALL)) {
                    mQuery.allStatus = AlarmConstant.ALLSTATUS_NORMAL;
                } else if (StringUtils.equals(mQuery.allStatus, AlarmConstant.ALLSTATUS_NORMAL)) {
                    mQuery.allStatus = AlarmConstant.ALLSTATUS_ABNORMAL;
                } else {
                    mQuery.allStatus = AlarmConstant.ALLSTATUS_ALL;
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
        if (StringUtils.equals(mQuery.allStatus, AlarmConstant.ALLSTATUS_ALL)) {
            mStatusLabelView.setText(R.string.alarm_list_title_status_normal);
            mStatusIconView.setImageResource(R.drawable.img_alarm_status2);
        } else if (StringUtils.equals(mQuery.allStatus, AlarmConstant.ALLSTATUS_NORMAL)) {
            mStatusLabelView.setText(R.string.alarm_list_title_status_abnormal);
            mStatusIconView.setImageResource(R.drawable.img_alarm_status1);
        } else if (StringUtils.equals(mQuery.allStatus, AlarmConstant.ALLSTATUS_ABNORMAL)) {
            mStatusLabelView.setText(R.string.alarm_list_title_status_all);
            mStatusIconView.setImageResource(R.drawable.img_alarm_status3);
        }
    }

}
