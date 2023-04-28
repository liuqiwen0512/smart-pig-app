package com.wuzi.pig.module.management;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.fun.Function2;

import java.util.List;

public class MgtMainFragment extends BaseFragment {

    public final static String FRAGMENT_KEY_PIGFARM = "fragment_key_pigfarm";
    public final static String FRAGMENT_KEY_PIGSTY = "fragment_key_pigsty";

    private Function<Boolean> mMainMenuListener;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main_mgt;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        switchFragment(FRAGMENT_KEY_PIGFARM);
    }

    private void switchFragment(String key) {
        switchFragment(key, null);
    }

    private void switchFragment(String key, Object data) {
        Fragment fragment = getFragment(key);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment item: fragments) {
            if (item == fragment) {
                continue;
            }
            if (item.isAdded() && item.isVisible()) {
                transaction.hide(item);
            }
        }
        switch (key) {
            case FRAGMENT_KEY_PIGSTY:{
                MgtPigstyFragment pigstyFragment = (MgtPigstyFragment) fragment;
                pigstyFragment.setPigFarmEntity((PigFarmEntity) data);
                break;
            }
        }
        try {
            if (!fragment.isAdded()) {
                transaction.add(R.id.fragment, fragment, key);
            } else {
                transaction.show(fragment);
            }
            transaction.commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Fragment getFragment(String key) {
        if (StringUtils.equals(key, FRAGMENT_KEY_PIGSTY)) {
            MgtPigstyFragment pigstyFragment = new MgtPigstyFragment();
            pigstyFragment.setMainMenuListener(mMainMenuListener);
            pigstyFragment.setFragmentEventListener(new FragmentEventListenerImpl());
            return pigstyFragment;
        }
        Fragment fragment = getChildFragmentManager().findFragmentByTag(key);
        if (fragment != null) {
            return fragment;
        }
        switch (key) {
            case FRAGMENT_KEY_PIGFARM:{
                MgtPigFarmFragment pigFarmFragment = new MgtPigFarmFragment();
                pigFarmFragment.setMainMenuListener(mMainMenuListener);
                pigFarmFragment.setFragmentEventListener(new FragmentEventListenerImpl());
                fragment = pigFarmFragment;
                break;
            }
        }
        return fragment;
    }

    //fragment 事件
    private class FragmentEventListenerImpl implements Function2<String, Object> {
        private boolean showMainMenu = true;
        @Override
        public void action(String eventTag, Object data) {
            switch (eventTag) {
                case MgtPigFarmFragment.ACTION_PIGSTY:{//猪场列表 跳转至 猪栏列表
                    MgtPigFarmFragment pigFarmFragment = (MgtPigFarmFragment) getFragment(FRAGMENT_KEY_PIGFARM);
                    showMainMenu = pigFarmFragment.isShowMainMenu();
                    if (mMainMenuListener != null) {
                        mMainMenuListener.action(false);
                    }
                    switchFragment(FRAGMENT_KEY_PIGSTY, data);
                    break;
                }
                case MgtPigstyFragment.ACTION_BACK:{//猪栏列表 返回至 猪场列表
                    if (mMainMenuListener != null) {
                        mMainMenuListener.action(showMainMenu);
                    }
                    switchFragment(FRAGMENT_KEY_PIGFARM, null);
                    break;
                }
            }
        }
    }

    public boolean onBackPressed() {
        FragmentManager fragmentManager = getChildFragmentManager();
        MgtPigstyFragment mgtPigstyFragment = (MgtPigstyFragment) fragmentManager.findFragmentByTag(FRAGMENT_KEY_PIGSTY);
        if (mgtPigstyFragment != null
                && mgtPigstyFragment.isVisible()
                && mgtPigstyFragment.onBackPressed()) {
            return true;
        } else {
            MgtPigFarmFragment mgtPigFarmFragment = (MgtPigFarmFragment) fragmentManager.findFragmentByTag(FRAGMENT_KEY_PIGFARM);
            if (mgtPigFarmFragment != null
                    && mgtPigFarmFragment.isVisible()
                    && mgtPigFarmFragment.onBackPressed()) {
                return true;
            }
        }
        return false;
    }

    public void setMainMenuListener(Function<Boolean> mainMenuListener) {
        mMainMenuListener = mainMenuListener;
    }

}
