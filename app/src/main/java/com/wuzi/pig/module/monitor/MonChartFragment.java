package com.wuzi.pig.module.monitor;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
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
    @BindView(R.id.chart)
    LineChart mLineChartView;

    private PigstyEntity mPigstyEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_monitor_chart;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);
        setPigstyEntity(mPigstyEntity);

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(Calendar.getInstance().getTime());
        MonChartContract.Query query = new MonChartContract.Query();
        query.pigstyId = mPigstyEntity.getPigstyId();
        query.beginTime = dateStr;
        query.endTime = dateStr;

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
        setLineData(listData);
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        ToastUtils.show(error.getPromptMessage());
    }

    public void setPigstyEntity(PigstyEntity pigstyEntity) {
        mPigstyEntity = pigstyEntity;
        if (mBackView != null && mPigstyEntity != null) {
            mBackView.setText(StringUtils.ASCII16ToString(mPigstyEntity.getPigstyName()));
        }
    }

    private void setLineData(ResponseListData<TempListEntity> listData) {

        int[] colors = new int[]{
                0xFF5c7a29,
                0xFFF7ACBC,
                0xFFEF5B9C,
                0xFFF47920,
                0xFF80752c
        };
        List<TempListEntity> rows = listData.getRows();
        List<ILineDataSet> lineDataSetList = new ArrayList<>();
        for (int j = 0; j < rows.size(); j++) {
            TempListEntity itemTemp = rows.get(j);
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
            //LineDataSet lineDataSet = new LineDataSet(entityList, StringUtils.ASCII16ToString(itemTemp.getEarTag()));
            //lineDataSet.setColor();
            int color = colors[j % colors.length];
            LineDataSet lineDataSet = new LineDataSet(entityList, itemTemp.getEarTag());
            lineDataSet.setColor(color);
            lineDataSet.setCircleColor(color);
            //lineDataSet.setValueTextSize(12);
            lineDataSet.setLineWidth(1);
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            /*lineDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return "";
                }
            });*/
            lineDataSetList.add(lineDataSet);

            LogUtils.e(TAG, " length : " + (j % colors.length) + "; color : " + color);
        }

        YAxis axisLeft = mLineChartView.getAxisLeft();
        axisLeft.setAxisMinimum(0);
        axisLeft.setAxisMaximum(60);
        axisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return ((int) value) + "°C";
            }
        });

        YAxis axisRight = mLineChartView.getAxisRight();
        axisRight.setEnabled(false);

        XAxis xAxis = mLineChartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineData lineData = new LineData(lineDataSetList);
        mLineChartView.setData(lineData);

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

        /*//缩放第一种方式
        Matrix matrix = new Matrix();
        //1f代表不缩放
        matrix.postScale(3f, 1f);
        mLineChartView.getViewPortHandler().refresh(matrix, mLineChartView, false);
        //重设所有缩放和拖动，使图表完全适合它的边界（完全缩小）。
        mLineChartView.fitScreen();*/
        //缩放第二种方式
        mLineChartView.getViewPortHandler().getMatrixTouch().postScale(10f, 1f);

        mLineChartView.invalidate();
    }
}
