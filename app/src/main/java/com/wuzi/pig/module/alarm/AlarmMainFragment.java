package com.wuzi.pig.module.alarm;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.constant.AlarmConstant;
import com.wuzi.pig.constant.MsgConstant;
import com.wuzi.pig.entity.AlarmListEntity;
import com.wuzi.pig.entity.OptionEntity;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.manager.MsgManager;
import com.wuzi.pig.module.alarm.adapter.TabsAdapter;
import com.wuzi.pig.module.alarm.contract.AlarmContract;
import com.wuzi.pig.module.alarm.presenter.AlarmPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.LogUtils;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.ui.view.GroupWrap;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AlarmMainFragment extends BaseFragment<AlarmPresenter> implements AlarmContract.IView {

    @BindView(R.id.info_group)
    GroupWrap mInfoGroupView;
    @BindView(R.id.tabs)
    RecyclerView mTabRecyclerView;
    @BindView(R.id.pages)
    ViewPager2 mViewPager;
    @BindView(R.id.pigsty_name)
    AppCompatTextView mPigstyNameView;

    private final OptionEntity[] mTabEntitys = AlarmConstant.PAGE_TABS;
    private TabsAdapter mTabsAdapter;
    private FragmentStateAdapterImpl mFragmentStateAdapter;
    private PigFarmEntity mPigFarmEntity;
    private int mSelectedPosition = -1;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main_alarm;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);

        mTabsAdapter = new TabsAdapter(mContext);
        mTabsAdapter.setItemListener(new TabListenerImpl());
        mTabRecyclerView.setLayoutManager(new GridLayoutManager(mContext, AlarmConstant.PAGE_TABS.length));
        mTabRecyclerView.setAdapter(mTabsAdapter);
        mTabRecyclerView.addItemDecoration(new ItemDecorationImpl());

        mFragmentStateAdapter = new FragmentStateAdapterImpl(this);
        mViewPager.setAdapter(mFragmentStateAdapter);
        View childAt = mViewPager.getChildAt(0);
        if (childAt instanceof RecyclerView) {
            childAt.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                LogUtils.d(TAG, "onPageSelected : " + position);
                mTabsAdapter.notifyItemChanged(mTabEntitys[position]);
                //显示新的页面
                Fragment fragment = getChildFragmentManager().findFragmentByTag("f" + position);
                if (fragment != null && fragment.isAdded() && fragment instanceof AlarmListFragment) {
                    fragment.onHiddenChanged(false);
                }
                //隐藏之前显示的页面
                fragment = getChildFragmentManager().findFragmentByTag("f" + mSelectedPosition);
                if (fragment != null && fragment.isAdded() && fragment instanceof AlarmListFragment) {
                    fragment.onHiddenChanged(false);
                }
                mSelectedPosition = position;
            }
        });

        setPigFarmEntity(mPigFarmEntity, true);
    }

    @OnClick({R.id.alarm_selection})
    protected void onClickView(View v) {
        switch (v.getId()) {
            case R.id.alarm_selection: {
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

    @Override
    public void performList(AlarmListEntity list, AlarmContract.QueryEntity query) {

    }

    @Override
    public void performError(ResponseException error, int fromTag) {

    }

    // tab click listener
    private class TabListenerImpl implements Function<OptionEntity> {

        @Override
        public void action(OptionEntity entity) {
            for (int i = 0; i < mTabEntitys.length; i++) {
                OptionEntity itemEntity = mTabEntitys[i];
                if (StringUtils.equals(itemEntity.key, entity.key)) {
                    mViewPager.setCurrentItem(i, false);
                    break;
                }
            }
        }
    }

    // viewpage adapter
    private class FragmentStateAdapterImpl extends FragmentStateAdapter {

        private SparseArray<Fragment> mFragments = new SparseArray();
        private int mNextPosition = -1;

        public FragmentStateAdapterImpl(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = null;
            OptionEntity tabEntity = mTabEntitys[position];
            switch (tabEntity.key) {
                case AlarmConstant.TYPE_TEMPERATURE: {
                    fragment = new TemperatureFragment();
                    break;
                }
                case AlarmConstant.TYPE_ACTIVITY: {
                    fragment = new ActivityFragment();
                    break;
                }
                case AlarmConstant.TYPE_EAR_TAG: {
                    fragment = new EarTagFragment();
                    break;
                }
                case AlarmConstant.TYPE_BASE_STATION: {
                    fragment = new BaseStationFragment();
                    break;
                }
                case AlarmConstant.TYPE_OUTAGE: {
                    fragment = new OutageFragment();
                    break;
                }
            }
            if (fragment instanceof AlarmListFragment) {
                ((AlarmListFragment) fragment).setPigFarmEntity(mPigFarmEntity);
            }
            mFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
            LogUtils.d("FragmentStateAdapterImpl", "onBindViewHolder - position : " + position + "(" + mTabEntitys[position].key + ")");
            super.onBindViewHolder(holder, position, payloads);
            holder.setIsRecyclable(false);
            //当前显示界面
            if (mSelectedPosition == position) {
                Fragment fragment = mFragments.get(position);
                if (fragment != null && fragment instanceof AlarmListFragment) {
                    AlarmListFragment listFragment = (AlarmListFragment) fragment;
                    listFragment.setPigFarmEntity(mPigFarmEntity);
                    listFragment.onHiddenChanged(false);
                }
            }
            //预进入界面
            if (mNextPosition != position) {
                Fragment fragment = mFragments.get(position);
                if (fragment != null && fragment instanceof AlarmListFragment) {
                    AlarmListFragment listFragment = (AlarmListFragment) fragment;
                    listFragment.setPigFarmEntity(mPigFarmEntity);
                }
                mNextPosition = position;
            }
        }

        @Override
        public int getItemCount() {
            return mTabEntitys.length;
        }
    }

    //tab recyclerview Decoration
    private class ItemDecorationImpl extends RecyclerView.ItemDecoration {
        private int mMargin = UIUtils.dip2px(6);

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int itemCount = state.getItemCount();
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                outRect.right = mMargin;
            } else if (position == itemCount - 1) {
                outRect.left = mMargin;
            } else {
                outRect.left = mMargin / 2;
                outRect.right = mMargin / 2;
            }
        }
    }

    public void setPigFarmEntity(PigFarmEntity entity, boolean refresh) {
        if (!mPigFarmEntity.equals(mPigFarmEntity, entity) || refresh) {
            if (mViewPager != null) {
                if (entity == null) {
                    getView().setBackgroundResource(R.drawable.img_main_bg2);
                    mInfoGroupView.setVisibility(View.INVISIBLE);
                    mPigstyNameView.setText(R.string.selection_pig_farm_default);
                } else {
                    getView().setBackgroundResource(R.drawable.img_main_bg);
                    mInfoGroupView.setVisibility(View.VISIBLE);
                    mPigstyNameView.setText(StringUtils.nullToString(entity.getPigfarmName()));
                    if (isVisible()) {
                        mFragmentStateAdapter.notifyDataSetChanged();
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
}
