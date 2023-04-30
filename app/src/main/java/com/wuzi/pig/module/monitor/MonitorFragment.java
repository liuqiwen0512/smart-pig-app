package com.wuzi.pig.module.monitor;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableStringBuilder;
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
import com.wuzi.pig.constant.MsgConstant;
import com.wuzi.pig.constant.PigFarmConstant;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.entity.Statis72HourEntity;
import com.wuzi.pig.manager.MsgManager;
import com.wuzi.pig.module.monitor.adapter.PigstyAdapter;
import com.wuzi.pig.module.monitor.contract.MonitorContract;
import com.wuzi.pig.module.monitor.presenter.MonitorPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.PromptUtils;
import com.wuzi.pig.utils.SpannableUtils;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.tools.CollectionUtils;
import com.wuzi.pig.utils.ui.view.GroupWrap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MonitorFragment extends BaseFragment<MonitorPresenter> implements MonitorContract.IView {

    @BindView(R.id.pig_farm_name)
    AppCompatTextView mPigFarmNameView;
    @BindView(R.id.pig_farm_title)
    AppCompatTextView mPigFarmTitleView;
    @BindView(R.id.pigsty_count)
    AppCompatTextView mPigstyCountView;
    @BindView(R.id.pig_count)
    AppCompatTextView mPigCountView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.pigsty_recycler)
    RecyclerView mPigstyRecyclerView;
    @BindView(R.id.prompt)
    AppCompatTextView mPromptView;
    @BindView(R.id.info_group)
    GroupWrap mInfoGroupView;

    private Map<String, String> mNetLoadedMap = new HashMap<>();
    private PigstyAdapter mPigstyAdapter;
    private PigFarmEntity mPigFarmEntity;
    private int mPageNumber = 1;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main_monitor;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);

        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListenerImpl());
        mPigstyAdapter = new PigstyAdapter(mContext);
        mPigstyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPigstyRecyclerView.setAdapter(mPigstyAdapter);
        mPigstyRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int mMargin = UIUtils.dip2px(18);

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = mMargin;
            }
        });
        setPigFarmEntity(mPigFarmEntity, true);
        if (mPigFarmEntity != null) {
            mRefreshLayout.autoRefresh();
        }
    }

    @OnClick({R.id.pig_farm_selection, R.id.pig_farm_search})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.pig_farm_selection: {
                MsgManager.showPigFarmDialog();
                break;
            }
            case R.id.pig_farm_search: {
                MsgManager.showPigFarmDialog();
                break;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && mIsRefresh) {
            setPigFarmEntity(mPigFarmEntity, mIsRefresh);
            mIsRefresh = false;
        }
    }

    //下拉刷新、上拉加载
    private class OnRefreshLoadMoreListenerImpl implements OnRefreshLoadMoreListener {
        @Override
        public void onRefresh(RefreshLayout refreshLayout) {
            mNetLoadedMap.clear();
            mPageNumber = 1;
            String pigfarmId = mPigFarmEntity.getPigfarmId();
            mPresenter.getPigstyList(pigfarmId, mPageNumber);
            mPresenter.getPigstyCount(pigfarmId);
            mPresenter.getStatis72Hour(pigfarmId);
        }

        @Override
        public void onLoadMore(RefreshLayout refreshLayout) {
            mPageNumber += 1;
            String pigfarmId = mPigFarmEntity.getPigfarmId();
            mPresenter.getPigstyList(pigfarmId, mPageNumber);
        }
    }

    @Override
    public void performPigstyCountSuccess(String count) {
        finishRefresh(String.valueOf(MonitorContract.TAG_PIGSTY_COUNT));
        setCountView(mPigstyCountView, count + " 个", count);
    }

    @Override
    public void performStatis72HourSuccess(Statis72HourEntity entity) {
        finishRefresh(String.valueOf(MonitorContract.TAG_STATIS_72_HOUR));
        String count = String.valueOf(entity.getToday());
        setCountView(mPigCountView, count + " 头", count);
    }

    @Override
    public void performPigstyList(PigstyListEntity listEntity, int pageNum, String pagfarmId) {
        finishRefresh(String.valueOf(MonitorContract.TAG_PIGSTY_LIST));
        mRefreshLayout.finishLoadMore();
        mPigstyRecyclerView.setVisibility(View.VISIBLE);
        PromptUtils.hidePrompt(mPromptView);
        int total = listEntity.getTotal();
        List<PigstyEntity> list = listEntity.getRows();
        setPigFarmCount(total);
        if (pageNum <= 1) {
            mPigstyAdapter.notifyDataSetChanged(list);
        } else {
            mPigstyAdapter.notifyItemInserted(list);
        }
        if (CollectionUtils.isEmpty(list) || list.size() < PigFarmConstant.PAGE_SIZE) {
            mRefreshLayout.setNoMoreData(true);
        } else {
            mRefreshLayout.setNoMoreData(false);
        }
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        finishRefresh(String.valueOf(fromTag));
        if (fromTag == MonitorContract.TAG_PIGSTY_LIST) {
            mRefreshLayout.finishLoadMore();
            if (mPageNumber == 1) {
                setPigFarmCount(0);
                mPigstyRecyclerView.setVisibility(View.GONE);
                if (error.code == ResponseException.ERROR.RESULT_CODE_201) {
                    PromptUtils.showEmptyPrompt(mPromptView, getString(R.string.mgt_pigsty_empty));
                } else {
                    PromptUtils.showEmptyPrompt(mPromptView, error.getMessage() + " 下拉刷新试试");
                }
                mRefreshLayout.setNoMoreData(true);
            } else {
                ToastUtils.show(error.getMessage());
            }
        }
        if (fromTag == MonitorContract.TAG_PIGSTY_COUNT) {
            setCountView(mPigstyCountView, null, null);
        }
        if (fromTag == MonitorContract.TAG_STATIS_72_HOUR) {
            setCountView(mPigCountView, null, null);
        } else {
            ToastUtils.show(error.getMessage());
        }
    }

    public void setPigFarmEntity(PigFarmEntity entity, boolean refresh) {
        if (!PigFarmEntity.equals(mPigFarmEntity, entity) || refresh) {
            if (mRefreshLayout != null) {
                if (entity == null) {
                    getView().setBackgroundResource(R.drawable.img_main_bg2);
                    mPromptView.setVisibility(View.GONE);
                    mInfoGroupView.setVisibility(View.INVISIBLE);
                    mPigFarmTitleView.setText("");
                    mPigFarmNameView.setText(R.string.selection_pig_farm_default);
                } else {
                    getView().setBackgroundResource(R.drawable.img_main_bg);
                    mInfoGroupView.setVisibility(View.VISIBLE);
                    mPigFarmTitleView.setText(StringUtils.nullToString(entity.getPigfarmName()));
                    mPigFarmNameView.setText(StringUtils.nullToString(entity.getPigfarmName()));
                    if (isVisible()) {
                        mRefreshLayout.autoRefresh();
                    }
                }
            }
        }
        mPigFarmEntity = entity;
    }

    @Override
    public void onMessage(int what, Message message) {
        switch (what) {
            case MsgConstant.MSG_WHAT_PIGFARM_CHANGE: {
                if (isVisible()) {
                    setPigFarmEntity((PigFarmEntity) message.obj, false);
                } else {
                    mIsRefresh = true;
                }
                break;
            }
        }
    }

    private void setPigFarmCount(int count) {

    }

    private void setCountView(AppCompatTextView textView, String text, String keyword) {
        if (StringUtils.isEmpty(text)) {
            text = "数据异常";
            keyword = text;
            int color = getResources().getColor(R.color.grey_3a);
            int textSize = 12;
            SpannableStringBuilder highlightSpannable = SpannableUtils.getHighlightSpannable(text, keyword, color);
            SpannableStringBuilder sizeSpannable = SpannableUtils.getSizeSpannable(highlightSpannable, text, keyword, textSize);
            textView.setText(sizeSpannable);
        } else {
            int color = getResources().getColor(R.color.grey_3a);
            int textSize = 22;
            SpannableStringBuilder highlightSpannable = SpannableUtils.getHighlightSpannable(text, keyword, color);
            SpannableStringBuilder boldSpannable = SpannableUtils.getBoldSpannable(highlightSpannable, text, keyword);
            SpannableStringBuilder sizeSpannable = SpannableUtils.getSizeSpannable(boldSpannable, text, keyword, textSize);
            textView.setText(sizeSpannable);
        }
    }

    private void finishRefresh(String tag) {
        mNetLoadedMap.put(tag, tag);
        if (mNetLoadedMap.size() >= 2) {
            mRefreshLayout.finishRefresh();
        }
    }
}
