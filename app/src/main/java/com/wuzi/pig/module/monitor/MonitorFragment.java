package com.wuzi.pig.module.monitor;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.entity.PigstyStatisEntity;
import com.wuzi.pig.module.monitor.adapter.PigstyAdapter;
import com.wuzi.pig.utils.SpannableUtils;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MonitorFragment extends BaseFragment {

    @BindView(R.id.pigsty_count)
    AppCompatTextView mPigstyCountView;
    @BindView(R.id.pig_count)
    AppCompatTextView mPigCountView;
    @BindView(R.id.pigsty_recycler)
    RecyclerView mPigstyRecyclerView;

    private PigstyAdapter mPigstyAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_monitor;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);

        int color = getResources().getColor(R.color.grey_3a);
        SpannableStringBuilder highlightSpannable = SpannableUtils.getHighlightSpannable("90808 头", "90808", color);
        SpannableStringBuilder boldSpannable = SpannableUtils.getBoldSpannable(highlightSpannable, "90808 头", "90808");
        SpannableStringBuilder sizeSpannable = SpannableUtils.getSizeSpannable(boldSpannable, "90808 头", "90808", 22);

        mPigstyCountView.setText(sizeSpannable);
        mPigCountView.setText(sizeSpannable);

        mPigstyAdapter = new PigstyAdapter(mContext);
        mPigstyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPigstyRecyclerView.setAdapter(mPigstyAdapter);
        mPigstyRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int mMargin = UIUtils.dip2px(18);
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = mMargin;
            }
        });

        List<PigstyStatisEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PigstyStatisEntity entity = new PigstyStatisEntity();
            entity.setName("猪栏" + (i + 1));
            entity.setOnlineCount(1490 + i);
            entity.setTemperature(35);
            entity.setLiveness(25);
            list.add(entity);
        }
        mPigstyAdapter.setList(list);
    }

    @OnClick({R.id.pig_farm_search})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.pig_farm_search:{
                break;
            }
        }
    }
}
