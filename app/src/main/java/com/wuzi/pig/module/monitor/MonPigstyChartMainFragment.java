package com.wuzi.pig.module.monitor;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.constant.MonitorConstant;
import com.wuzi.pig.entity.ActivityListEntity;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.ResponseListData;
import com.wuzi.pig.entity.TempListEntity;
import com.wuzi.pig.module.monitor.contract.MonChartContract;
import com.wuzi.pig.module.monitor.presenter.MonChartPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.fun.Function2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MonPigstyChartMainFragment extends BaseFragment<MonChartPresenter> implements MonChartContract.IView {

    public final static String FRAGMENT_LANDSCAPE = "f_landscape";
    public final static String FRAGMENT_PORTRAIT = "f_portrait";

    public final static String EVENT_MODEL_TEMPERATURE = "model_temperature";
    public final static String EVENT_MODEL_ACTIVITY = "model_activity";
    public final static String EVENT_PREVE_DAY = "preve_day";
    public final static String EVENT_NEXT_DAY = "next_day";
    public final static String EVENT_LANDSCAPE = "landscape";
    public final static String EVENT_PORTRAIT = "portrait";
    public final static String EVENT_DATE = "date";

    private Function2<String, Object> mEventListener = new EventListenerImpl();
    private MonDateDialog mDateDialog;
    private MonChartContract.UIEntity mUIEntity;
    private PigstyEntity mPigstyEntity;
    private MonChartContract.Query mQuery = new MonChartContract.Query();
    private String mCurrentFragmentTag = FRAGMENT_LANDSCAPE;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_monitor_pigsty_chart_main;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUIEntity = new MonChartContract.UIEntity();
        mUIEntity.model = MonitorConstant.MODEL_TEMPERATURES;
        mUIEntity.colors = MonitorConstant.CHART_COLORS;
        mUIEntity.pigstyEntity = mPigstyEntity;
        mUIEntity.calendar = Calendar.getInstance();

        String queryDate = getQueryDate(mUIEntity.calendar);
        mQuery = new MonChartContract.Query();
        mQuery.pigstyId = mPigstyEntity.getPigstyId();
        mQuery.beginTime = queryDate;
        mQuery.endTime = queryDate;

        switchFragment(FRAGMENT_PORTRAIT);
        mPresenter.getTemperatures(mQuery);
    }

    @Override
    public void performTemperatures(ResponseListData<TempListEntity> listData, int fromTag) {
        List<TempListEntity> rows = listData.getRows();
        for (TempListEntity itemEntity : rows) {
            itemEntity.setSelected(!mUIEntity.unSelectedTagMap.containsKey(itemEntity.getEarTag()));
        }
        mUIEntity.tempList = listData;
        Fragment fragment = getChildFragmentManager().findFragmentByTag(mCurrentFragmentTag);
        if (fragment != null) {
            ((MonChartContract.IView) fragment).performTemperatures(listData, fromTag);
        }
    }

    @Override
    public void performActivitys(ResponseListData<ActivityListEntity> listData, int fromTag) {
        List<ActivityListEntity> rows = listData.getRows();
        for (ActivityListEntity itemEntity : rows) {
            itemEntity.setSelected(!mUIEntity.unSelectedTagMap.containsKey(itemEntity.getEarTag()));
        }
        mUIEntity.activityList = listData;
        Fragment fragment = getChildFragmentManager().findFragmentByTag(mCurrentFragmentTag);
        if (fragment != null) {
            ((MonChartContract.IView) fragment).performActivitys(listData, fromTag);
        }
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        if (error.code == ResponseException.ERROR.RESULT_CODE_201) {
            Fragment fragment = getChildFragmentManager().findFragmentByTag(mCurrentFragmentTag);
            if (fragment != null) {
                ((MonChartContract.IView) fragment).performError(error, fromTag);
            }
        }
        ToastUtils.show(error.getPromptMessage());
    }

    public void setPigstyEntity(PigstyEntity pigstyEntity) {
        mPigstyEntity = pigstyEntity;
    }

    private String getQueryDate(Calendar instance) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(instance.getTime());
        return dateStr;
    }

    private void switchFragment(String key) {
        mCurrentFragmentTag = key;
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(key);
        if (fragment == null) {
            switch (key) {
                case FRAGMENT_PORTRAIT: {
                    MonPigstyChartPortraitFragment portraitFragment = new MonPigstyChartPortraitFragment();
                    fragment = portraitFragment;
                    setFragmentData(fragment);
                    break;
                }
                case FRAGMENT_LANDSCAPE: {
                    MonPigstyChartLandscapeFragment landscapeFragment = new MonPigstyChartLandscapeFragment();
                    fragment = landscapeFragment;
                    setFragmentData(fragment);
                    break;
                }
            }
        }

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

    private void setFragmentData(Fragment fragment) {
        if (fragment instanceof MonPigstyChartPortraitFragment) {
            MonPigstyChartPortraitFragment portraitFragment = (MonPigstyChartPortraitFragment) fragment;
            portraitFragment.setEventListener(mEventListener);
            portraitFragment.setUIEntity(mUIEntity);
        } else if (fragment instanceof MonPigstyChartLandscapeFragment) {
            MonPigstyChartLandscapeFragment landscapeFragment = (MonPigstyChartLandscapeFragment) fragment;
            landscapeFragment.setEventListener(mEventListener);
            landscapeFragment.setUIEntity(mUIEntity);
        }
    }

    private void requestChartData(Calendar calendar) {
        mUIEntity.resetChartData();
        mUIEntity.calendar = calendar;
        String queryDate = getQueryDate(mUIEntity.calendar);
        mQuery.beginTime = queryDate;
        mQuery.endTime = queryDate;
        if (mUIEntity.model == MonitorConstant.MODEL_TEMPERATURES) {
            mPresenter.getTemperatures(mQuery);
        } else if (mUIEntity.model == MonitorConstant.MODEL_ACTIVITY) {
            mPresenter.getMovements(mQuery);
        }
        setFragmentData(getChildFragmentManager().findFragmentByTag(mCurrentFragmentTag));
    }

    private class EventListenerImpl implements Function2<String, Object> {

        @Override
        public void action(String event, Object object) {
            switch (event) {
                case EVENT_MODEL_TEMPERATURE: {
                    mPresenter.getTemperatures(mQuery);
                    break;
                }
                case EVENT_MODEL_ACTIVITY: {
                    mPresenter.getMovements(mQuery);
                    break;
                }
                case EVENT_PREVE_DAY: {
                    mUIEntity.calendar.add(Calendar.DATE, -1);
                    requestChartData(mUIEntity.calendar);
                    break;
                }
                case EVENT_NEXT_DAY: {
                    mUIEntity.calendar.add(Calendar.DATE, 1);
                    requestChartData(mUIEntity.calendar);
                    break;
                }
                case EVENT_PORTRAIT: {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    switchFragment(FRAGMENT_PORTRAIT);
                    setFragmentData(getChildFragmentManager().findFragmentByTag(mCurrentFragmentTag));
                    break;
                }
                case EVENT_LANDSCAPE: {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    switchFragment(FRAGMENT_LANDSCAPE);
                    setFragmentData(getChildFragmentManager().findFragmentByTag(mCurrentFragmentTag));
                    break;
                }
                case EVENT_DATE: {
                    if (mDateDialog == null) {
                        mDateDialog = new MonDateDialog();
                        mDateDialog.setCurrentDate(mUIEntity.calendar);
                        mDateDialog.setOnDismissListener(dialog -> {
                            mDateDialog = null;
                        });
                        mDateDialog.setSubmitListener(calendar -> {
                            requestChartData(calendar);
                        });
                    }
                    mDateDialog.showNow(getParentFragmentManager());
                    break;
                }
            }
        }
    }

    private void setRequestedOrientation(int orientation) {
        Configuration config = this.getResources().getConfiguration();
        switch (orientation) {
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE: {
                if (Configuration.ORIENTATION_LANDSCAPE != config.orientation) {
                    getActivity().setRequestedOrientation(orientation);
                }
                break;
            }
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT: {
                if (Configuration.ORIENTATION_PORTRAIT != config.orientation) {
                    getActivity().setRequestedOrientation(orientation);
                }
                break;
            }
        }
    }
}
