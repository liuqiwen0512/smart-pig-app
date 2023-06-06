package com.wuzi.pig.utils.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;

public class LineChart2 extends LineChart {
    public LineChart2(Context context) {
        super(context);
    }

    public LineChart2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new LineChartRenderer2(this, mAnimator, mViewPortHandler);
    }
}
