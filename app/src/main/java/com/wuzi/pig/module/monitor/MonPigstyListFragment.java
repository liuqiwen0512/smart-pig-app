package com.wuzi.pig.module.monitor;

import android.content.Intent;
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
import com.wuzi.pig.module.monitor.contract.MonChartContract;
import com.wuzi.pig.module.monitor.contract.MonPigstyListContract;
import com.wuzi.pig.module.monitor.presenter.MonPigstyListPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.PromptUtils;
import com.wuzi.pig.utils.SpannableUtils;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.tools.CollectionUtils;
import com.wuzi.pig.utils.ui.view.GroupWrap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MonPigstyListFragment extends BaseFragment<MonPigstyListPresenter> implements MonPigstyListContract.IView {

    @BindView(R.id.pig_farm_name)
    AppCompatTextView mPigFarmNameView;
    @BindView(R.id.pig_farm_title)
    AppCompatTextView mPigFarmTitleView;
    @BindView(R.id.pig_farm_title_prompt)
    AppCompatTextView mPigFarmTitlePromptView;
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
        mPigstyAdapter.setClickListener(new AdapterItemClicklistener());
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
            if (mPigFarmEntity != null) {
                mRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.autoRefresh();
                    }
                });
            }
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

    //猪栏 adapter click
    private class AdapterItemClicklistener implements Function<PigstyEntity> {

        @Override
        public void action(PigstyEntity entity) {
            Intent intent = new Intent(mContext, MonChartActivity.class);
            intent.putExtra(MonChartContract.INTENT_EXTRA_PIGSTY, entity);
            startActivity(intent);
        }
    }

    @Override
    public void performPigstyCountSuccess(String count) {
        finishRefresh(String.valueOf(MonPigstyListContract.TAG_PIGSTY_COUNT));
        setCountView(mPigstyCountView, count + " 个", count);
    }

    @Override
    public void performStatis72HourSuccess(Statis72HourEntity entity) {
        finishRefresh(String.valueOf(MonPigstyListContract.TAG_STATIS_72_HOUR));
        String count = String.valueOf(entity.getToday());
        setCountView(mPigCountView, count + " 头", count);
    }

    @Override
    public void performPigstyList(PigstyListEntity listEntity, int pageNum, String pagfarmId) {
        finishRefresh(String.valueOf(MonPigstyListContract.TAG_PIGSTY_LIST));
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
        if (fromTag == MonPigstyListContract.TAG_PIGSTY_LIST) {
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
        if (fromTag == MonPigstyListContract.TAG_PIGSTY_COUNT) {
            setCountView(mPigstyCountView, null, null);
        }
        if (fromTag == MonPigstyListContract.TAG_STATIS_72_HOUR) {
            setCountView(mPigCountView, null, null);
        } else {
            ToastUtils.show(error.getMessage());
        }
    }

    public void setPigFarmEntity(PigFarmEntity newEntity, boolean refresh) {
        PigFarmEntity oldEntity = mPigFarmEntity;
        mPigFarmEntity = newEntity;
        if (!PigFarmEntity.equals(oldEntity, newEntity) || refresh) {
            if (mRefreshLayout != null) {
                if (newEntity == null) {
                    getView().setBackgroundResource(R.drawable.img_main_bg2);
                    mRefreshLayout.setEnableRefresh(false);
                    mPromptView.setVisibility(View.GONE);
                    mInfoGroupView.setVisibility(View.INVISIBLE);
                    mPigFarmTitleView.setText(R.string.selection_pig_farm_default);
                    mPigFarmTitlePromptView.setVisibility(View.VISIBLE);
                    mPigFarmNameView.setText(R.string.selection_pig_farm_default);
                } else {
                    getView().setBackgroundResource(R.drawable.img_main_bg);
                    mRefreshLayout.setEnableRefresh(true);
                    mInfoGroupView.setVisibility(View.VISIBLE);
                    mPigFarmTitleView.setText(StringUtils.nullToString(newEntity.getPigfarmName()));
                    mPigFarmTitlePromptView.setVisibility(View.GONE);
                    mPigFarmNameView.setText(StringUtils.nullToString(newEntity.getPigfarmName()));
                }
            }
        }
    }

    @Override
    public void onMessage(int what, Message message) {
        switch (what) {
            case MsgConstant.MSG_WHAT_PIGFARM_CHANGE: {
                setPigFarmEntity((PigFarmEntity) message.obj, false);
                if (isVisible()) {
                    mRefreshLayout.autoRefresh();
                } else {
                    mIsRefresh = true;
                }
                break;
            }
            case MsgConstant.MSG_WHAT_PIGFARM_DELETE: {
                if (mPigFarmEntity == null) break;
                List<String> pigfarmIds = (List<String>) message.obj;
                for (String id : pigfarmIds) {
                    if (StringUtils.equals(id, mPigFarmEntity.getPigfarmId())) {
                        if (!isVisible()) {
                            mIsRefresh = true;
                        }
                        setPigFarmEntity(null, false);
                        break;
                    }
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
