package com.wuzi.pig.module.management;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.constant.Constant;
import com.wuzi.pig.constant.PigFarmConstant;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.module.management.adapter.PigFarmAdapter;
import com.wuzi.pig.module.management.contract.PigFarmContract;
import com.wuzi.pig.module.management.presenter.PigFarmPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.PromptUtils;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.fun.Function2;
import com.wuzi.pig.utils.fun.Function3;
import com.wuzi.pig.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MgtPigFarmFragment extends BaseFragment<PigFarmPresenter> implements PigFarmContract.IView {

    public final static String ACTION_EDIT_BACK = "mgtpigfarmfragment_action_back";
    public final static String ACTION_PIGSTY = "mgtpigfarmfragment_action_pigsty";

    @BindView(R.id.motion_layout)
    MotionLayout mMotionLayout;
    @BindView(R.id.pig_farm_edit)
    AppCompatTextView mPigFarmEditView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.pig_farm_count)
    AppCompatTextView mPigFarmCountView;
    @BindView(R.id.pig_farm_recycler)
    RecyclerView mPigFarmRecyclerView;
    @BindView(R.id.prompt)
    AppCompatTextView mPromptView;
    @BindView(R.id.checkbox_all)
    AppCompatCheckBox mCheckedAllView;
    @BindView(R.id.checked_msg)
    AppCompatTextView mCheckedtMsgView;
    @BindView(R.id.delete_all)
    AppCompatTextView mDeleteAllView;

    private Function<Boolean> mMainMenuListener;
    private Function2<String, Object> mFragmentEventListener;
    private PigFarmAddDialog mPigFarmAddDialog;
    private PigFarmAdapter mPigFarmAdapter;
    private int mPigFarmCount = 1;
    private int mPageNumber = 1;
    private int mPigFarmDeleteCount = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mgt_pigfarm;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListenerImpl());
        mPigFarmAdapter = new PigFarmAdapter(mContext);
        mPigFarmAdapter.setEventListener(new ItemEventListenerImpl());
        mPigFarmRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPigFarmRecyclerView.setAdapter(mPigFarmAdapter);

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

        mCheckedAllView.setOnCheckedChangeListener(new CheckedAllChangeListenerImpl());

        setPigFarmCount(0);
        mRefreshLayout.autoRefresh();
    }

    @OnClick({R.id.edit_back, R.id.pig_farm_add, R.id.pig_farm_edit, R.id.checkbox_all, R.id.delete_all})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.edit_back:{
                performClickEditBack();
                break;
            }
            case R.id.pig_farm_add:{
                if (mPigFarmAddDialog == null) {
                    mPigFarmAddDialog = new PigFarmAddDialog();
                    mPigFarmAddDialog.setOnDismissListener(dialog -> {
                        mPigFarmAddDialog = null;
                    });
                    mPigFarmAddDialog.setSubmitListener(object -> {
                        mRefreshLayout.autoRefresh();
                    });
                }
                mPigFarmAddDialog.showNow(getChildFragmentManager());
                break;
            }
            case R.id.pig_farm_edit:{
                if (mMainMenuListener != null) {
                    mMainMenuListener.action(false);
                }
                editStatus(true);
                break;
            }
            case R.id.delete_all:{
                Map<String, Object> checkedMap = mPigFarmAdapter.getCheckedMap();
                if (checkedMap.size() == 0) {
                    ToastUtils.show(R.string.mgt_pigfarm_select_prompt);
                } else {
                    MgtPromptDialog.startPigfarmPrompt(getChildFragmentManager(), ok -> {
                        List<String> ids = new ArrayList<>(checkedMap.keySet());
                        mPresenter.deletePigFarm(ids);
                    });
                }
                mPigFarmDeleteCount = checkedMap.size();
                break;
            }
        }
    }

    //修改猪场名字
    private class ItemEventListenerImpl implements Function3<View, PigFarmEntity, Integer> {
        private PigFarmEditDialog mPigFarmEditDialog;
        @Override
        public void action(View view, PigFarmEntity entity, Integer evenTag) {
            if (evenTag == PigFarmAdapter.CLICK_ACTION) {
                if (mFragmentEventListener != null) {
                    mFragmentEventListener.action(MgtPigFarmFragment.ACTION_PIGSTY, entity);
                }
            } else if (evenTag == PigFarmAdapter.CLICK_EDIT) {
                if (mPigFarmEditDialog == null) {
                    mPigFarmEditDialog = new PigFarmEditDialog();
                    mPigFarmEditDialog.setPigFarmEntity(entity);
                    mPigFarmEditDialog.setOnDismissListener(dialog -> {
                        mPigFarmEditDialog = null;
                    });
                    mPigFarmEditDialog.setSubmitListener(object -> {
                        entity.setPigfarmName(object.toString());
                        mPigFarmAdapter.notifyItemChanged(entity);
                    });
                }
                mPigFarmEditDialog.showNow(getChildFragmentManager());
            } else if (evenTag == PigFarmAdapter.CLICK_DELETE) {
                Map<String, Object> checkedMap = mPigFarmAdapter.getCheckedMap();
                CompoundButton checkBox = (CompoundButton) view;
                if (checkedMap.size() >= PigFarmConstant.PIG_FARM_DELETE_ALL_MAX) {
                    mPigFarmAdapter.notifyItemChecked(entity, false);
                    ToastUtils.show(R.string.mgt_select_max_prompt);
                } else {
                    mPigFarmAdapter.notifyItemChecked(entity, checkBox.isChecked());
                }
                setCheckedMessage();
            }
        }
    }

    //下拉刷新、上拉加载
    private class OnRefreshLoadMoreListenerImpl implements OnRefreshLoadMoreListener {
        @Override
        public void onRefresh(RefreshLayout refreshLayout) {
            mPageNumber = 1;
            mPresenter.getPigFarmList(mPageNumber);
        }

        @Override
        public void onLoadMore(RefreshLayout refreshLayout) {
            mPageNumber += 1;
            mPresenter.getPigFarmList(mPageNumber);
        }
    }

    private class CheckedAllChangeListenerImpl implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!buttonView.isPressed()) return;
            mPigFarmAdapter.notifyCheckedAll(isChecked);
            setCheckedMessage();
        }
    }

    @Override
    public void performSuccess(int fromTag) {
        switch (fromTag) {
            case PigFarmContract.TAG_PIG_FARM_DELETE:{
                mPigFarmAdapter.notifyItemRemovedByChecked();
                setPigFarmCount(mPigFarmCount - mPigFarmDeleteCount);
                setCheckedMessage();
                mPigFarmDeleteCount = 0;
                List<PigFarmEntity> allList = mPigFarmAdapter.getList();
                if (allList.size() == 0) {
                    mRefreshLayout.autoRefresh();
                }
                break;
            }
        }
    }

    @Override
    public void performPigFarmList(PigFarmListEntity listEntity, int pageNum) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        mPigFarmRecyclerView.setVisibility(View.VISIBLE);
        PromptUtils.hidePrompt(mPromptView);
        int total = listEntity.getTotal();
        List<PigFarmEntity> list = listEntity.getRows();
        setPigFarmCount(total);
        if (pageNum <= 1) {
            mPigFarmAdapter.notifyDataSetChanged(list);
        } else {
            mPigFarmAdapter.notifyItemInserted(list);
        }
        if (CollectionUtils.isEmpty(list) || list.size() < Constant.PAGE_SIZE) {
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
                    PromptUtils.showEmptyPrompt(mPromptView,getString(R.string.mgt_pigfarm_empty));
                } else {
                    PromptUtils.showEmptyPrompt(mPromptView, error.getMessage());
                }
            } else {
                ToastUtils.show(error.getMessage());
            }
        } else {
            mPigFarmDeleteCount = 0;
            ToastUtils.show(error.getMessage());
        }
    }

    private void performClickEditBack() {
        if (mMainMenuListener != null) {
            mMainMenuListener.action(true);
        }
        editStatus(false);
    }

    private void setPigFarmCount(int count) {
        mPigFarmCount = count;
        if (count == 0) {
            mPigFarmCountView.setVisibility(View.INVISIBLE);
            mPigFarmEditView.setVisibility(View.GONE);
        } else {
            mPigFarmCountView.setText(getString(R.string.mgt_pigfarm_online_count, String.valueOf(count)));
            mPigFarmCountView.setVisibility(View.VISIBLE);
            mPigFarmEditView.setVisibility(View.VISIBLE);
        }
    }

    private void setCheckedMessage() {
        Resources res = mContext.getResources();
        List<PigFarmEntity> allList = mPigFarmAdapter.getList();
        Map<String, Object> checkedMap = mPigFarmAdapter.getCheckedMap();
        int checkedSize = checkedMap.size();
        mCheckedtMsgView.setText(res.getString(R.string.mgt_select_message, String.valueOf(checkedMap.size()), String.valueOf(mPigFarmCount)));
        mCheckedAllView.setChecked(checkedSize > 0
                && (checkedMap.size() >= PigFarmConstant.PIG_FARM_DELETE_ALL_MAX || checkedMap.size() == allList.size()));
    }

    private void editStatus(final boolean edit) {
        mMotionLayout.transitionToState(edit ? R.id.mgt_edit : R.id.mgt_list);
        setCheckedMessage();
        mPigFarmAdapter.setEdit(edit);
    }

    public boolean isShowMainMenu() {
        return mPigFarmAdapter== null || !mPigFarmAdapter.isEdit();
    }

    public boolean onBackPressed() {
        if (mPigFarmAdapter.isEdit()) {
            performClickEditBack();
            return true;
        }
        return false;
    }

    public void setMainMenuListener(Function<Boolean> mainMenuListener) {
        mMainMenuListener = mainMenuListener;
    }

    public void setFragmentEventListener(Function2<String, Object> fragmentEventListener) {
        mFragmentEventListener = fragmentEventListener;
    }
}
