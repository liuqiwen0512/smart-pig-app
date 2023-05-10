package com.wuzi.pig.module.monitor;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.entity.ResponseListData;
import com.wuzi.pig.entity.TempListEntity;
import com.wuzi.pig.module.monitor.adapter.ChartEarTagAdapter;
import com.wuzi.pig.module.monitor.contract.MonChartContract;
import com.wuzi.pig.module.monitor.presenter.MonChartPresenter;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.LogUtils;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MonChartFragment extends BaseFragment<MonChartPresenter> implements MonChartContract.IView {

    @BindView(R.id.back)
    AppCompatTextView mBackView;
    @BindView(R.id.ear_tag_recycler)
    RecyclerView mEarTagRecyclerView;
    @BindView(R.id.chart)
    LineChart mLineChartView;

    private ResponseListData<TempListEntity> mTempList;
    private int[] mColors;
    private ChartEarTagAdapter mEarTagAdapter;
    private PigstyEntity mPigstyEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_monitor_chart;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);
        setPigstyEntity(mPigstyEntity);
        initLineChartView();

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(Calendar.getInstance().getTime());
        MonChartContract.Query query = new MonChartContract.Query();
        query.pigstyId = mPigstyEntity.getPigstyId();
        query.beginTime = dateStr;
        query.endTime = dateStr;

        mEarTagAdapter = new ChartEarTagAdapter(mContext);
        mEarTagRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mEarTagRecyclerView.setAdapter(mEarTagAdapter);
        mEarTagAdapter.setClickListener(entity -> {
            boolean selected = !entity.selected;
            mEarTagAdapter.notifyItemChanged(entity, selected);
            List<TempListEntity> rows = mTempList.getRows();
            for (TempListEntity itemEntity : rows) {
                if (StringUtils.equals(itemEntity.getEarTag(), entity.temp.getEarTag())) {
                    itemEntity.setSelected(selected);
                }
            }
            setLineData(mTempList, mColors);
        });

        mPresenter.getTemperatures(query);
    }

    @OnClick({R.id.back})
    protected void onClickView(View v) {
        switch (v.getId()) {
            case R.id.back: {
                getActivity().finish();
                break;
            }
        }
    }

    @Override
    public void performTemperatures(ResponseListData<TempListEntity> listData, int fromTag) {
        List<TempListEntity> rows = listData.getRows();
        for (TempListEntity itemEntity : rows) {
            itemEntity.setSelected(true);
        }
        mTempList = listData;
        mColors = getChartsColor(listData);
        setChartsMenus(listData, mColors);
        setLineData(listData, mColors);
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        //mLineChartView.setNoDataText("暂无数据");
        ToastUtils.show(error.getPromptMessage());
    }

    public void setPigstyEntity(PigstyEntity pigstyEntity) {
        mPigstyEntity = pigstyEntity;
        if (mBackView != null && mPigstyEntity != null) {
            mBackView.setText(StringUtils.ASCII16ToString(mPigstyEntity.getPigstyName()));
        }
    }

    private void initLineChartView() {
        //设置chart是否可以触摸
        mLineChartView.setTouchEnabled(true);
        //设置是否可以拖拽
        mLineChartView.setDragEnabled(true);
        //设置是否可以缩放 x和y，默认true
        mLineChartView.setScaleEnabled(false);
        //是否缩放X轴
        mLineChartView.setScaleXEnabled(true);
        //X轴缩放比例
        //mLineChartView.setScaleX(1.5f);
        //Y轴缩放比例
        //mLineChartView.setScaleY(1.5f);
        //是否缩放Y轴
        //mLineChartView.setScaleYEnabled(true);
        //设置是否可以通过双击屏幕放大图表。默认是true
        mLineChartView.setDoubleTapToZoomEnabled(false);

        //缩放
        mLineChartView.getViewPortHandler().getMatrixTouch().postScale(5f, 1f);

        Description description = new Description();
        description.setText("");
        mLineChartView.setDescription(description);

    }

    private void setLineData(ResponseListData<TempListEntity> listData, int[] colors) {
        List<TempListEntity> rows = listData.getRows();
        List<ILineDataSet> lineDataSetList = new ArrayList<>();
        for (int j = 0; j < rows.size(); j++) {
            TempListEntity itemTemp = rows.get(j);
            if (!itemTemp.isSelected()) {
                continue;
            }
            List<TempListEntity.ItemEntity> datas = itemTemp.getDatas();
            List<Entry> entityList = new ArrayList<>();
            float lastTemp = 0;
            for (int i = 0; i < datas.size(); i++) {
                TempListEntity.ItemEntity itemEntity = datas.get(i);
                if (itemEntity.getTemperature() > 0) {
                    lastTemp = itemEntity.getTemperature();
                }
                entityList.add(new Entry(i, lastTemp));
            }
            int color = colors[j % colors.length];
            LineDataSet lineDataSet = new LineDataSet(entityList, itemTemp.getEarTag());
            lineDataSet.setColor(color);
            lineDataSet.setCircleColor(color);
            //lineDataSet.setValueTextSize(12);
            lineDataSet.setLineWidth(1);
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSetList.add(lineDataSet);

            LogUtils.e(TAG, " length : " + (j % colors.length) + "; color : " + color);
        }

        YAxis axisLeft = mLineChartView.getAxisLeft();
        //axisLeft.setLabelCount(6, true);
        axisLeft.setAxisMinimum(25);
        axisLeft.setAxisMaximum(50);
        axisLeft.setGranularity(5);
        axisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return ((int) value) + "°C";
            }
        });

        YAxis axisRight = mLineChartView.getAxisRight();
        axisRight.setEnabled(false);

        List<TempListEntity.ItemEntity> tempDatas = rows.get(0).getDatas();
        XAxis xAxis = mLineChartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);//设置x轴的最小值
        xAxis.setAxisMaximum(tempDatas.size());//设置最大值
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (value > tempDatas.size()) {
                    return "";
                }
                TempListEntity.ItemEntity entity = tempDatas.get((int) value);
                String[] split = entity.getTime().split(" ");
                //return split[1] + "\n\r" + split[0];
                return split[1];
            }
        });

        LineData lineData = new LineData(lineDataSetList);
        mLineChartView.setData(lineData);
        mLineChartView.setVisibleXRangeMinimum(6);

        Legend legend = mLineChartView.getLegend();
        legend.setEnabled(false);

        mLineChartView.invalidate();
    }

    private int[] getChartsColor(ResponseListData<TempListEntity> listData) {
        int total = listData.getTotal();
        int[] colors = new int[total];
        int red = 254;
        int green = 0;
        int blue = 254;
        for (int i = 0; i < colors.length; i++) {
            if (i % 2 == 0) {
                red += i * 10;
            } else if (i % 3 == 0) {
                green += i * 100;
            } else {
                blue += i * 50;
            }
            colors[i] = Color.rgb(red % 255, green % 255, blue % 255);
        }
        return colors;
    }

    private void setChartsMenus(ResponseListData<TempListEntity> listData, int[] colors) {
        List<TempListEntity> rows = listData.getRows();
        List<ChartEarTagAdapter.MenuEntity> earTagMenuList = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            TempListEntity itemEntity = rows.get(i);
            ChartEarTagAdapter.MenuEntity menuEntity = new ChartEarTagAdapter.MenuEntity();
            menuEntity.color = colors[i];
            menuEntity.temp = itemEntity;
            earTagMenuList.add(menuEntity);
        }
        mEarTagAdapter.setList(earTagMenuList);
        mEarTagAdapter.notifyDataSetChanged();
    }

}
