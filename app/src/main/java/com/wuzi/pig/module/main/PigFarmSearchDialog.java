package com.wuzi.pig.module.main;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseDialogFragment;
import com.wuzi.pig.constant.PigFarmConstant;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.manager.MsgManager;
import com.wuzi.pig.module.main.adapter.SearchAdapter;
import com.wuzi.pig.module.main.contract.SearchContract;
import com.wuzi.pig.module.main.presenter.SearchPresenter;
import com.wuzi.pig.module.management.contract.PigFarmContract;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.PromptUtils;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.fun.Function3;
import com.wuzi.pig.utils.tools.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PigFarmSearchDialog extends BaseDialogFragment<SearchPresenter> implements SearchContract.IView {

    @BindView(R.id.search_value)
    AppCompatEditText mSearchValueView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.pig_farm_recycler)
    RecyclerView mPigFarmRecyclerView;
    @BindView(R.id.prompt)
    AppCompatTextView mPromptView;

    private SearchAdapter mSearchAdapter;
    private String mSearchKeyword;
    private int mPageNumber = 1;

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_search_pigfarm;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListenerImpl());
        mSearchAdapter = new SearchAdapter(mContext);
        mSearchAdapter.setEventListener(new ItemEventListenerImpl());
        mPigFarmRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPigFarmRecyclerView.setAdapter(mSearchAdapter);

        mPigFarmRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int mMargin = UIUtils.dip2px(32);

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.top = mMargin;
                }
            }
        });

        mRefreshLayout.autoRefresh();
    }

    @OnClick({R.id.back})
    protected void onClickView(View v) {
        switch (v.getId()) {
            case R.id.back: {
                dismiss();
                break;
            }
        }
    }

    //修改猪场名字
    private class ItemEventListenerImpl implements Function3<View, PigFarmEntity, Integer> {
        @Override
        public void action(View view, PigFarmEntity entity, Integer evenTag) {
            if (evenTag == SearchAdapter.CLICK_TARGET) {
                MsgManager.selectionPigFarm(entity);
                dismiss();
            }
        }
    }

    //下拉刷新、上拉加载
    private class OnRefreshLoadMoreListenerImpl implements OnRefreshLoadMoreListener {
        @Override
        public void onRefresh(RefreshLayout refreshLayout) {
            mPageNumber = 1;
            mPresenter.getPigFarmList(mSearchKeyword, mPageNumber);
        }

        @Override
        public void onLoadMore(RefreshLayout refreshLayout) {
            mPageNumber += 1;
            mPresenter.getPigFarmList(mSearchKeyword, mPageNumber);
        }
    }

    @Override
    protected void initWindowTheme(Dialog dialog, Window window) {
        fullDialog(dialog, window);
    }

    @Override
    public void performSuccess(Object object, int pageNum, int fromTag) {
        PigFarmListEntity listEntity = (PigFarmListEntity) object;
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        mPigFarmRecyclerView.setVisibility(View.VISIBLE);
        PromptUtils.hidePrompt(mPromptView);
        int total = listEntity.getTotal();
        List<PigFarmEntity> list = listEntity.getRows();
        setPigFarmCount(total);
        if (pageNum <= 1) {
            mSearchAdapter.notifyDataSetChanged(list);
        } else {
            mSearchAdapter.notifyItemInserted(list);
        }
        if (CollectionUtils.isEmpty(list) || list.size() < PigFarmConstant.PAGE_SIZE) {
            mRefreshLayout.setNoMoreData(true);
        } else {
            mRefreshLayout.setNoMoreData(false);
        }
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        if (fromTag == PigFarmContract.TAG_PIG_FARM_LIST) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
            if (mPageNumber == 1) {
                setPigFarmCount(0);
                mPigFarmRecyclerView.setVisibility(View.GONE);
                if (error.code == ResponseException.ERROR.RESULT_CODE_201) {
                    PromptUtils.showEmptyPrompt(mPromptView, getString(R.string.mgt_pigfarm_empty));
                } else {
                    PromptUtils.showEmptyPrompt(mPromptView, error.getMessage() + " 下拉刷新试试");
                }
                mRefreshLayout.setNoMoreData(true);
            } else {
                ToastUtils.show(error.getMessage());
            }
        } else {
            ToastUtils.show(error.getMessage());
        }
    }

    private void setPigFarmCount(int count) {
    }

}
