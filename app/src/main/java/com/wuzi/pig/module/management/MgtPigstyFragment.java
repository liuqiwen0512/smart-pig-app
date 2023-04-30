package com.wuzi.pig.module.management;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.constant.PigstyConstant;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.PigstyListEntity;
import com.wuzi.pig.module.management.adapter.PigstyAdapter;
import com.wuzi.pig.module.management.contract.PigstyContract;
import com.wuzi.pig.module.management.presenter.PigstyPresenter;
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

public class MgtPigstyFragment extends BaseFragment<PigstyPresenter> implements PigstyContract.IView {

    public final static String ACTION_BACK = "mgtpigstyfragment_action_back";

    @BindView(R.id.motion_layout)
    MotionLayout mMotionLayout;
    @BindView(R.id.back)
    AppCompatTextView mBackView;
    @BindView(R.id.pig_farm_name)
    AppCompatTextView mPigFarmNameView;
    @BindView(R.id.pigsty_edit)
    AppCompatTextView mPigstyEditView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.pigsty_count)
    AppCompatTextView mPigstyCountView;
    @BindView(R.id.pigsty_recycler)
    RecyclerView mPigstyRecyclerView;
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
    private PigstyEditDialog mPigstyEditDialog;
    private PigstyAdapter mPigstyAdapter;
    private PigFarmEntity mPigFarmEntity;
    private int mPigstyCount = 1;
    private int mPageNumber = 1;
    private int mPigstyDeleteCount = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mgt_pigsty;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListenerImpl());
        mPigstyAdapter = new PigstyAdapter(mContext);
        mPigstyAdapter.setEventListener(new ItemEditListenerImpl());

        mPigstyRecyclerView.addItemDecoration(new ItemDecorationImpl());
        mPigstyRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mPigstyRecyclerView.setAdapter(mPigstyAdapter);

        mCheckedAllView.setOnCheckedChangeListener(new CheckedAllChangeListenerImpl());

        mPigFarmNameView.setText(mPigFarmEntity.getPigfarmName());
        setPigstyCount(0);
        mRefreshLayout.autoRefresh();
    }

    @OnClick({R.id.back, R.id.edit_back, R.id.pigsty_add, R.id.pigsty_edit, R.id.checkbox_all, R.id.delete_all})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.back: {
                performClickBack();
                break;
            }
            case R.id.edit_back: {
                performClickEditBack();
                break;
            }
            case R.id.pigsty_add: {
                if (mPigstyEditDialog == null) {
                    mPigstyEditDialog = new PigstyEditDialog();
                    mPigstyEditDialog.setPigFarmEntity(mPigFarmEntity);
                    mPigstyEditDialog.setOnDismissListener(dialog -> {
                        mPigstyEditDialog = null;
                    });
                    mPigstyEditDialog.setSubmitListener(object -> {
                        mRefreshLayout.autoRefresh();
                    });
                }
                mPigstyEditDialog.showNow(getChildFragmentManager());
                break;
            }
            case R.id.pigsty_edit: {
                if (mMainMenuListener != null) {
                    mMainMenuListener.action(false);
                }
                editStatus(true);
                break;
            }
            case R.id.delete_all:{
                Map<String, Object> checkedMap = mPigstyAdapter.getCheckedMap();
                if (checkedMap.size() == 0) {
                    ToastUtils.show(R.string.mgt_pigsty_select_prompt);
                } else {
                    MgtPromptDialog.startPigstyPrompt(getChildFragmentManager(), ok -> {
                        List<String> ids = new ArrayList<>(checkedMap.keySet());
                        mPresenter.deletePigsty(ids);
                    });
                }
                mPigstyDeleteCount = checkedMap.size();
                break;
            }
        }
    }

    //adapter item 间隔
    private class ItemDecorationImpl extends RecyclerView.ItemDecoration {
        private int mMarginLeft = UIUtils.dip2px(6);
        private int mMarginTop = UIUtils.dip2px(10);
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            if (position % 3 == 0) {
                outRect.right = mMarginLeft;
            } else if (position % 3 == 1) {
                outRect.left = mMarginLeft / 2;
                outRect.right = mMarginLeft / 2;
            } else if (position % 3 == 2) {
                outRect.left = mMarginLeft;
            }

            if (position == 0 || position == 1 || position == 2) {
                outRect.top = mMarginTop;
            }
            outRect.bottom = mMarginTop;
        }
    }

    @Override
    public void performSuccess(int fromTag) {
        switch (fromTag) {
            case PigstyContract.TAG_PIGSTY_DELETE: {
                mPigstyAdapter.notifyItemRemovedByChecked();
                setPigstyCount(mPigstyCount - mPigstyDeleteCount);
                setCheckedMessage();
                mPigstyDeleteCount = 0;
                List<PigstyEntity> allList = mPigstyAdapter.getList();
                if (allList.size() == 0) {
                    mRefreshLayout.autoRefresh();
                }
                break;
            }
        }
    }

    @Override
    public void performPigstyList(PigstyListEntity listEntity, int pageNum) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        mPigstyRecyclerView.setVisibility(View.VISIBLE);
        PromptUtils.hidePrompt(mPromptView);
        int total = listEntity.getTotal();
        List<PigstyEntity> list = listEntity.getRows();
        setPigstyCount(total);
        if (pageNum <= 1) {
            mPigstyAdapter.notifyDataSetChanged(list);
        } else {
            mPigstyAdapter.notifyItemInserted(list);
        }
        if (CollectionUtils.isEmpty(list) || list.size() < PigstyConstant.PAGE_SIZE) {
            mRefreshLayout.setNoMoreData(true);
        } else {
            mRefreshLayout.setNoMoreData(false);
        }
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        if (fromTag == PigstyContract.TAG_PIGSTY_LIST) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
            if (mPageNumber == 1) {
                setPigstyCount(0);
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
        } else {
            mPigstyDeleteCount = 0;
            ToastUtils.show(error.getMessage());
        }
    }

    private void setCheckedMessage() {
        Resources res = mContext.getResources();
        List<PigstyEntity> allList = mPigstyAdapter.getList();
        Map<String, Object> checkedMap = mPigstyAdapter.getCheckedMap();
        int checkedSize = checkedMap.size();
        mCheckedtMsgView.setText(res.getString(R.string.mgt_select_message, String.valueOf(checkedMap.size()), String.valueOf(mPigstyCount)));
        mCheckedAllView.setChecked(checkedSize > 0
                && (checkedSize >= PigstyConstant.PIG_FARM_DELETE_ALL_MAX || checkedSize == allList.size()));
        if (!mCheckedAllView.isChecked()) {
            if (checkedSize > 0) {
                ColorStateList stateList = new ColorStateList(new int[][]{{}}, new int[]{getResources().getColor(R.color.colorSelected)});
                mCheckedAllView.setButtonTintList(stateList);
            } else {
                mCheckedAllView.setButtonTintList(null);
            }
        }
    }

    //修改猪场名字
    private class ItemEditListenerImpl implements Function3<View, PigstyEntity, Integer> {
        @Override
        public void action(View view, PigstyEntity entity, Integer evenTag) {
            if (evenTag == PigstyAdapter.CLICK_EDIT) {

            } else if (evenTag == PigstyAdapter.CLICK_DELETE) {
                Map<String, Object> checkedMap = mPigstyAdapter.getCheckedMap();
                CompoundButton checkBox = (CompoundButton) view;
                if (checkedMap.size() >= PigstyConstant.PIG_FARM_DELETE_ALL_MAX) {
                    mPigstyAdapter.notifyItemChecked(entity, false);
                    ToastUtils.show(R.string.mgt_select_max_prompt);
                } else {
                    mPigstyAdapter.notifyItemChecked(entity, checkBox.isChecked());
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
            String pigfarmId = mPigFarmEntity.getPigfarmId();
            mPresenter.getPigstyList(pigfarmId, mPageNumber);
        }

        @Override
        public void onLoadMore(RefreshLayout refreshLayout) {
            mPageNumber += 1;
            String pigfarmId = mPigFarmEntity.getPigfarmId();
            mPresenter.getPigstyList(pigfarmId, mPageNumber);
        }
    }

    private void performClickBack() {
        if (mFragmentEventListener != null) {
            mFragmentEventListener.action(ACTION_BACK, null);
        }
    }

    private void performClickEditBack() {
        editStatus(false);
    }

    private void setPigstyCount(int count) {
        mPigstyCount = count;
        if (count == 0) {
            mPigstyCountView.setVisibility(View.INVISIBLE);
            mPigstyEditView.setVisibility(View.GONE);
        } else {
            mPigstyCountView.setText(getString(R.string.mgt_pigsty_online_count, String.valueOf(count)));
            mPigstyCountView.setVisibility(View.VISIBLE);
            mPigstyEditView.setVisibility(View.VISIBLE);
        }
    }

    //删除全选
    private class CheckedAllChangeListenerImpl implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!buttonView.isPressed()) return;
            mPigstyAdapter.notifyCheckedAll(isChecked);
            setCheckedMessage();
        }
    }

    private void editStatus(final boolean edit) {
        mMotionLayout.transitionToState(edit ? R.id.mgt_edit : R.id.mgt_list);
        setCheckedMessage();
        mPigstyAdapter.setEdit(edit);
    }

    public boolean onBackPressed() {
        if (mPigstyAdapter.isEdit()) {
            performClickEditBack();
        } else {
            performClickBack();
        }
        return true;
    }

    public void setPigFarmEntity(PigFarmEntity pigFarmEntity) {
        mPigFarmEntity = pigFarmEntity;
    }

    public void setMainMenuListener(Function<Boolean> mainMenuListener) {
        mMainMenuListener = mainMenuListener;
    }

    public void setFragmentEventListener(Function2<String, Object> fragmentEventListener) {
        mFragmentEventListener = fragmentEventListener;
    }
}
