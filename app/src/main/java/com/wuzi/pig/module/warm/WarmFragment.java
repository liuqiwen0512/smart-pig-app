package com.wuzi.pig.module.warm;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.constant.WarmConstant;
import com.wuzi.pig.entity.OptionEntity;
import com.wuzi.pig.module.warm.adapter.TabsAdapter;
import com.wuzi.pig.module.warm.contract.WarmContract;
import com.wuzi.pig.module.warm.presenter.WarmPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.fun.Function;

import butterknife.BindView;

public class WarmFragment extends BaseFragment<WarmPresenter> implements WarmContract.IView {

    @BindView(R.id.tabs)
    RecyclerView mTabRecyclerView;
    @BindView(R.id.pages)
    ViewPager2 mViewPager;

    private SparseArray<Fragment> mFragments = new SparseArray<>();
    private final OptionEntity[] mTabEntitys = WarmConstant.PAGE_TABS;
    private TabsAdapter mTabsAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_warm;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);
        mViewPager.setAdapter(new FragmentStateAdapterImpl(this));
        View childAt = mViewPager.getChildAt(0);
        if (childAt instanceof RecyclerView) {
            childAt.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mTabsAdapter.notifyItemChanged(mTabEntitys[position]);
            }
        });

        mTabsAdapter = new TabsAdapter(mContext);
        mTabsAdapter.setItemListener(new TabListenerImpl());
        mTabRecyclerView.setLayoutManager(new GridLayoutManager(mContext, WarmConstant.PAGE_TABS.length));
        mTabRecyclerView.setAdapter(mTabsAdapter);

        mTabRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        });
    }

    @Override
    public void performError(ResponseException error, int fromTag) {

    }

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

        public FragmentStateAdapterImpl(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = mFragments.get(position);
            OptionEntity tabEntity = mTabEntitys[position];
            if (fragment == null) {
                switch (tabEntity.key) {
                    case WarmConstant.FRAGMENT_TEMPERATURE: {
                        fragment = new TemperatureFragment();
                        break;
                    }
                    case WarmConstant.FRAGMENT_ACTIVITY: {
                        fragment = new ActivityFragment();
                        break;
                    }
                    case WarmConstant.FRAGMENT_EAR_TAG: {
                        fragment = new TemperatureFragment();
                        break;
                    }
                    case WarmConstant.FRAGMENT_BASE_STATION: {
                        fragment = new ActivityFragment();
                        break;
                    }
                    case WarmConstant.FRAGMENT_OUTAGE: {
                        fragment = new TemperatureFragment();
                        break;
                    }
                }
                mFragments.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getItemCount() {
            return mTabEntitys.length;
        }
    }
}
