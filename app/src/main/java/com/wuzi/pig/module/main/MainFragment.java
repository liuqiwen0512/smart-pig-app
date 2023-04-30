package com.wuzi.pig.module.main;

import android.os.Bundle;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.constant.MsgConstant;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.entity.Statis72HourEntity;
import com.wuzi.pig.manager.MsgManager;
import com.wuzi.pig.module.main.contract.MainContract;
import com.wuzi.pig.module.main.presenter.MainPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.SpannableUtils;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.ui.view.GroupWrap;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment<MainPresenter> implements MainContract.IView {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.info_group)
    GroupWrap mInfoGroupView;
    @BindView(R.id.pig_farm_name)
    AppCompatTextView mPigFarmNameView;
    @BindView(R.id.pig_farm_title)
    AppCompatTextView mPigFarmTitleView;
    @BindView(R.id.pigsty_count_value)
    AppCompatTextView mPigstyCountValueView;
    @BindView(R.id.pig_statis_today_value)
    AppCompatTextView mPigStatisTodayValueView;
    @BindView(R.id.pig_statis_yesterday_value)
    AppCompatTextView mPigStatisYesterdayValueView;
    @BindView(R.id.pig_statis_before_yesterday_value)
    AppCompatTextView mPigStatisBeforeYesterdayValueView;
    @BindView(R.id.pig_statis_health_value)
    AppCompatTextView mPigStatisHealthValueView;
    @BindView(R.id.pig_statis_equipment_value)
    AppCompatTextView mPigStatisEquipmentValueView;

    private Map<String, String> mNetLoadedMap = new HashMap<>();
    private PigFarmEntity mPigFarmEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);

        int color = getResources().getColor(R.color.grey_3a);
        SpannableStringBuilder highlightSpannable = SpannableUtils.getHighlightSpannable("90808 头", "90808", color);
        SpannableStringBuilder boldSpannable = SpannableUtils.getBoldSpannable(highlightSpannable, "90808 头", "90808");
        SpannableStringBuilder sizeSpannable = SpannableUtils.getSizeSpannable(boldSpannable, "90808 头", "90808", 18);

        mPigStatisTodayValueView.setText(sizeSpannable);
        mPigStatisYesterdayValueView.setText(sizeSpannable);
        mPigStatisBeforeYesterdayValueView.setText(sizeSpannable);
        mPigStatisHealthValueView.setText(sizeSpannable);
        mPigStatisEquipmentValueView.setText(sizeSpannable);

        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mNetLoadedMap.clear();
                String pigfarmId = mPigFarmEntity.getPigfarmId();
                mPresenter.getPigstyCount(pigfarmId);
                mPresenter.getStatis72Hour(pigfarmId);
            }
        });

        setPigFarmEntity(mPigFarmEntity, true);
        if (mPigFarmEntity != null) {
            mRefreshLayout.autoRefresh();
        }
    }

    @OnClick({R.id.pig_farm_selection})
    protected void onClickView(View v) {
        switch (v.getId()) {
            case R.id.pig_farm_selection: {
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
    public void performPigstyCountSuccess(String count) {
        finishRefresh(String.valueOf(MainContract.TAG_PIGSTY_COUNT));
        String text = count + "个";
        setCountView(mPigstyCountValueView, text, text);
    }

    @Override
    public void performStatis72HourSuccess(Statis72HourEntity entity) {
        finishRefresh(String.valueOf(MainContract.TAG_STATIS_72_HOUR));
        String today = String.valueOf(entity.getToday());
        String yesterday = String.valueOf(entity.getYesterday());
        String befyesterday = String.valueOf(entity.getBefyesterday());
        String healths = String.valueOf(entity.getHealths());
        String equipments = String.valueOf(entity.getEquipments());
        setCountView(mPigStatisTodayValueView, today + " 头", today);
        setCountView(mPigStatisYesterdayValueView, yesterday + " 头", yesterday);
        setCountView(mPigStatisBeforeYesterdayValueView, befyesterday + " 头", befyesterday);
        setCountView(mPigStatisHealthValueView, healths + " 头", healths);
        setCountView(mPigStatisEquipmentValueView, equipments + " 头", equipments);
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        finishRefresh(String.valueOf(fromTag));
        if (fromTag == MainContract.TAG_PIGSTY_COUNT) {
            setCountView(mPigstyCountValueView, null, null);
        }
        if (fromTag == MainContract.TAG_STATIS_72_HOUR) {
            setCountView(mPigStatisTodayValueView, null, null);
            setCountView(mPigStatisYesterdayValueView, null, null);
            setCountView(mPigStatisBeforeYesterdayValueView, null, null);
            setCountView(mPigStatisHealthValueView, null, null);
            setCountView(mPigStatisEquipmentValueView, null, null);
        } else {
            ToastUtils.show(error.getMessage());
        }
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

    public void setPigFarmEntity(PigFarmEntity entity, boolean refresh) {
        if (!PigFarmEntity.equals(mPigFarmEntity, entity) || refresh) {
            if (mRefreshLayout != null) {
                if (entity == null) {
                    getView().setBackgroundResource(R.drawable.img_main_bg2);
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
}
