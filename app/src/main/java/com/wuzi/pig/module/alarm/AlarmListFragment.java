package com.wuzi.pig.module.alarm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.constant.AlarmConstant;
import com.wuzi.pig.constant.PigFarmConstant;
import com.wuzi.pig.entity.AlarmEntity;
import com.wuzi.pig.entity.AlarmListEntity;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.module.alarm.adapter.AlarmListAdapter;
import com.wuzi.pig.module.alarm.contract.AlarmContract;
import com.wuzi.pig.module.alarm.presenter.AlarmPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.LogUtils;
import com.wuzi.pig.utils.PromptUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.tools.CollectionUtils;

import java.util.List;

import butterknife.BindView;

public class AlarmListFragment extends BaseFragment<AlarmPresenter> implements AlarmContract.IView {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.prompt)
    AppCompatTextView mPromptView;
    @BindView(R.id.recycler)
    RecyclerView mAlarmRecyclerView;

    protected String mType = AlarmConstant.TYPE_TEMPERATURE;
    protected AlarmContract.QueryEntity mQuery = new AlarmContract.QueryEntity();
    protected AlarmListAdapter mAlarmListAdapter;
    protected PigFarmEntity mPigFarmEntity;
    protected boolean mInitData = false;

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        LogUtils.d(TAG, "initView : ");
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListenerImpl());
        mAlarmListAdapter = new AlarmListAdapter(mContext, mType);
        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAlarmRecyclerView.setAdapter(mAlarmListAdapter);

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (mPigFarmEntity != null) {
                    mRefreshLayout.autoRefresh();
                }
            }
        });
    }

    //下拉刷新
    private class OnRefreshLoadMoreListenerImpl implements OnRefreshLoadMoreListener {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            mInitData = true;
            mQuery.pageNum = 1;
            mQuery.pigfarmId = mPigFarmEntity.getPigfarmId();
            mPresenter.getAlarmActivityList(mQuery);
        }

        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            mQuery.pageNum += 1;
            mPresenter.getAlarmActivityList(mQuery);
        }
    }

    @Override
    public void performList(AlarmListEntity listEntity, AlarmContract.QueryEntity query) {
        LogUtils.d(TAG, "performList : " + listEntity.getTotal());
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        mAlarmRecyclerView.setVisibility(View.VISIBLE);
        PromptUtils.hidePrompt(mPromptView);
        int total = listEntity.getTotal();
        List<AlarmEntity> list = listEntity.getRows();
        setTemperatureCount(total);
        if (query.pageNum <= 1) {
            mAlarmListAdapter.notifyDataSetChanged(list);
        } else {
            mAlarmListAdapter.notifyItemInserted(list);
        }
        if (CollectionUtils.isEmpty(list) || list.size() < PigFarmConstant.PAGE_SIZE) {
            mRefreshLayout.setNoMoreData(true);
        } else {
            mRefreshLayout.setNoMoreData(false);
        }
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        LogUtils.d(TAG, "performError : " + error.getMessage());
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        if (mQuery.pageNum == 1) {
            setTemperatureCount(0);
            mAlarmRecyclerView.setVisibility(View.GONE);
            if (error.code == ResponseException.ERROR.RESULT_CODE_201) {
                PromptUtils.showEmptyPrompt(mPromptView, getString(R.string.alarm_list_empty, mType));
            } else {
                PromptUtils.showEmptyPrompt(mPromptView, error.getMessage() + " 下拉刷新试试");
            }
            mRefreshLayout.setNoMoreData(true);
        } else {
            ToastUtils.show(error.getMessage());
        }
    }

    public void setType(String type) {
        mType = type;
    }

    public void setPigFarmEntity(PigFarmEntity pigFarmEntity) {
        if (mAlarmRecyclerView != null && mPigFarmEntity != null && pigFarmEntity != null) {
            if (!StringUtils.equals(mPigFarmEntity.getPigfarmId(), pigFarmEntity.getPigfarmId())) {
                mAlarmListAdapter.notifyDataSetChanged(null);
                mInitData = false;
            }
        }
        mPigFarmEntity = pigFarmEntity;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && !mInitData && mRefreshLayout != null) {
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (mPigFarmEntity != null) {
                        mRefreshLayout.autoRefresh();
                    }
                }
            });
        }
        LogUtils.e(TAG, "onHiddenChanged - hidden : " + hidden
                + "; mType : " + mType);
    }

    private void setTemperatureCount(int total) {

    }
}
