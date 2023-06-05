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
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.ui.view.GroupWrap;

import java.util.HashMap;
import java.util.List;
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
    @BindView(R.id.pig_farm_title_prompt)
    AppCompatTextView mPigFarmTitlePromptView;
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

    private Function<Integer> mClickListener;
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
        SpannableStringBuilder highlightSpannable = SpannableUtils.getHighlightSpannable("0 头", "0", color);
        SpannableStringBuilder boldSpannable = SpannableUtils.getBoldSpannable(highlightSpannable, "0 头", "0");
        SpannableStringBuilder sizeSpannable = SpannableUtils.getSizeSpannable(boldSpannable, "0 头", "0", 18);

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

    @OnClick({R.id.pig_farm_selection, R.id.pig_statis_health, R.id.pig_statis_equipment})
    protected void onClickView(View v) {
        switch (v.getId()) {
            case R.id.pig_farm_selection: {
                MsgManager.showPigFarmDialog();
                break;
            }
            case R.id.pig_statis_health: {
                if (mClickListener != null) {
                    mClickListener.action(0);
                }
                break;
            }
            case R.id.pig_statis_equipment: {
                if (mClickListener != null) {
                    mClickListener.action(2);
                }
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

    public void setClickListener(Function<Integer> clickListener) {
        mClickListener = clickListener;
    }

    public void setPigFarmEntity(PigFarmEntity newEntity, boolean refresh) {
        PigFarmEntity oldEntity = mPigFarmEntity;
        mPigFarmEntity = newEntity;
        if (!PigFarmEntity.equals(oldEntity, newEntity) || refresh) {
            if (mRefreshLayout != null) {
                if (newEntity == null) {
                    getView().setBackgroundResource(R.drawable.img_main_bg2);
                    mRefreshLayout.setEnableRefresh(false);
                    mInfoGroupView.setVisibility(View.INVISIBLE);
                    mPigFarmTitleView.setText(R.string.selection_pig_farm_default);
                    mPigFarmTitlePromptView.setVisibility(View.VISIBLE);
                    mPigFarmNameView.setText(R.string.selection_pig_farm_default);
                } else {
                    String pigfarmName = StringUtils.nullToString(newEntity.getPigfarmName());
                    getView().setBackgroundResource(R.drawable.img_main_bg);
                    mRefreshLayout.setEnableRefresh(true);
                    mInfoGroupView.setVisibility(View.VISIBLE);
                    mPigFarmTitleView.setText(pigfarmName);
                    mPigFarmTitlePromptView.setVisibility(View.GONE);
                    mPigFarmNameView.setText(pigfarmName);
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
}
