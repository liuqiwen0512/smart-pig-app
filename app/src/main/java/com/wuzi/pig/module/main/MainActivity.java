package com.wuzi.pig.module.main;

import android.os.Bundle;
import android.os.Message;
import android.widget.FrameLayout;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseActivity;
import com.wuzi.pig.constant.MenuConstant;
import com.wuzi.pig.constant.MsgConstant;
import com.wuzi.pig.entity.MenuEntity;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.module.alarm.AlarmMainFragment;
import com.wuzi.pig.module.management.MgtMainFragment;
import com.wuzi.pig.module.monitor.MonitorFragment;
import com.wuzi.pig.module.user.UserMainFragment;
import com.wuzi.pig.utils.SharePreferences;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StatusbarColorUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.motion_layout)
    MotionLayout mMotionLayout;
    @BindView(R.id.fl)
    FrameLayout mFrameLayout;
    @BindView(R.id.menus)
    RecyclerView mMenuRecyclerView;

    private PigFarmSearchDialog mPigFarmSearchDialog;
    private MenuAdapter mMenuAdapter;
    private PigFarmEntity mPigFarmEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        StatusBarUtils.immersive(getWindow());
        StatusbarColorUtils.setStatusBarDarkIcon(getWindow(), true);
        mPigFarmEntity = SharePreferences.getInstance().getSelectedPigFarm();

        mMenuAdapter = new MenuAdapter(mContext, MenuConstant.getMainMenus());
        mMenuAdapter.setClickListener(new MenuClickListenerImpl());
        mMenuRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        mMenuRecyclerView.setAdapter(mMenuAdapter);

        switchFragment(MenuConstant.MAIN_MENU_HOME);
        mMenuAdapter.setSelected(MenuConstant.MAIN_MENU_HOME);
    }

    private void switchFragment(String key) {
        Fragment fragment = getFragment(key);
        FragmentManager fragmentManager = getSupportFragmentManager();
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
        try {
            if (!fragment.isAdded()) {
                transaction.add(R.id.fl, fragment, key);
            } else {
                transaction.show(fragment);
            }
            transaction.commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*switch (key) {
            case MenuConstant.MAIN_MENU_HOME: {
                MainFragment mainFragment = (MainFragment) fragment;
                mainFragment.setPigFarmEntity(mPigFarmEntity, false);
                break;
            }
            case MenuConstant.MAIN_MENU_ALARM: {
                AlarmMainFragment alarmMainFragment = (AlarmMainFragment) fragment;
                alarmMainFragment.setPigFarmEntity(mPigFarmEntity, false);
                break;
            }
            case MenuConstant.MAIN_MENU_MONITOR: {
                MonitorFragment monitorFragment = (MonitorFragment) fragment;
                monitorFragment.setPigFarmEntity(mPigFarmEntity, false);
                break;
            }
        }*/
    }

    private Fragment getFragment(String key) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(key);
        if (fragment != null) {
            return fragment;
        }
        switch (key) {
            case MenuConstant.MAIN_MENU_HOME: {
                MainFragment mainFragment = new MainFragment();
                mainFragment.setPigFarmEntity(mPigFarmEntity, true);
                fragment = mainFragment;
                break;
            }
            case MenuConstant.MAIN_MENU_ALARM: {
                AlarmMainFragment alarmMainFragment = new AlarmMainFragment();
                alarmMainFragment.setPigFarmEntity(mPigFarmEntity, true);
                fragment = alarmMainFragment;
                break;
            }
            case MenuConstant.MAIN_MENU_MONITOR: {
                MonitorFragment monitorFragment = new MonitorFragment();
                monitorFragment.setPigFarmEntity(mPigFarmEntity, true);
                fragment = monitorFragment;
                break;
            }
            case MenuConstant.MAIN_MENU_MANAGE: {
                MgtMainFragment mgtFragment = new MgtMainFragment();
                mgtFragment.setMainMenuListener(show -> {
                    showMenusView(show);
                });
                fragment = mgtFragment;
                break;
            }
            case MenuConstant.MAIN_MENU_MINE:{
                fragment = new UserMainFragment();
                break;
            }
        }
        return fragment;
    }

    private void showMenusView(final boolean show) {
        mMotionLayout.transitionToState(show ? R.id.menu_start : R.id.menu_end);
    }

    // menu click
    private class MenuClickListenerImpl implements Function<MenuEntity> {

        @Override
        public void action(MenuEntity entity) {
            mMenuAdapter.setSelected(entity.key);
            switchFragment(entity.key);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        MgtMainFragment mgtMainFragment = (MgtMainFragment) fragmentManager.findFragmentByTag(MenuConstant.MAIN_MENU_MANAGE);
        if (mgtMainFragment != null
                && mgtMainFragment.isVisible()
                && mgtMainFragment.onBackPressed()) {

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMessage(int what, Message message) {
        switch (what) {
            case MsgConstant.MSG_WHAT_SHOW_PIGFARM_DIALOG: {
                if (mPigFarmSearchDialog == null) {
                    mPigFarmSearchDialog = new PigFarmSearchDialog();
                    mPigFarmSearchDialog.setOnDismissListener(dialog -> {
                        mPigFarmSearchDialog = null;
                    });
                }
                mPigFarmSearchDialog.showNowAllowingStateLoss(getSupportFragmentManager());
                break;
            }
            case MsgConstant.MSG_WHAT_PIGFARM_CHANGE: {
                mPigFarmEntity = (PigFarmEntity) message.obj;
                SharePreferences.getInstance().setSelectedPigFarm(mPigFarmEntity);
                break;
            }
            case MsgConstant.MSG_WHAT_PIGFARM_DELETE: {
                if (mPigFarmEntity == null) break;
                List<String> pigfarmIds = (List<String>) message.obj;
                for (String id : pigfarmIds) {
                    if (StringUtils.equals(id, mPigFarmEntity.getPigfarmId())) {
                        SharePreferences.getInstance().setSelectedPigFarm(null);
                        mPigFarmEntity = null;
                        break;
                    }
                }
                break;
            }
        }
    }
}