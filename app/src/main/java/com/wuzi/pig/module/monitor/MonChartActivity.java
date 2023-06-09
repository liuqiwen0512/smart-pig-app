package com.wuzi.pig.module.monitor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseActivity;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.module.monitor.contract.MonChartContract;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StatusbarColorUtils;

import java.util.List;

public class MonChartActivity extends BaseActivity {

    private PigstyEntity mPigstyEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_monitor_chart;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        StatusBarUtils.immersive(getWindow());
        StatusbarColorUtils.setStatusBarDarkIcon(getWindow(), true);
        mPigstyEntity = getIntent().getParcelableExtra(MonChartContract.INTENT_EXTRA_PIGSTY);
        switchFragment(null);
    }

    private void switchFragment(String key) {
        /*MonPigstyChartPortraitFragment monPigstyChartPortraitFragment = new MonPigstyChartPortraitFragment();
        monPigstyChartPortraitFragment.setPigstyEntity(mPigstyEntity);  */
        MonPigstyChartMainFragment monPigstyChartMainFragment = new MonPigstyChartMainFragment();
        monPigstyChartMainFragment.setPigstyEntity(mPigstyEntity);
        Fragment fragment = monPigstyChartMainFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment item : fragments) {
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
    }
}
