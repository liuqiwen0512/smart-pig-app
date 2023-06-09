package com.wuzi.pig.module.monitor;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
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
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.entity.ActivityListEntity;
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
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.tools.TimeUtils;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MonChartFragment extends BaseFragment<MonChartPresenter> implements MonChartContract.IView {

    @BindView(R.id.back)
    AppCompatTextView mBackView;
    @BindView(R.id.today_value)
    AppCompatTextView mTodayValueView;
    @BindView(R.id.pig_count)
    AppCompatTextView mPigCountView;
    @BindView(R.id.ear_tag_left)
    ImageFilterView mEarTagLeftView;
    @BindView(R.id.ear_tag_right)
    ImageFilterView mEarTagRightView;
    @BindView(R.id.model_temp)
    AppCompatTextView mModelTempView;
    @BindView(R.id.model_activity)
    AppCompatTextView mModelActivityView;
    @BindView(R.id.chart_title)
    AppCompatTextView mChartTitleView;
    @BindView(R.id.ear_tag_recycler)
    RecyclerView mEarTagRecyclerView;
    @BindView(R.id.chart)
    LineChart mLineChartView;
    @BindView(R.id.chart_fold)
    View mChartFoldView;
    @BindView(R.id.chart_plus)
    View mChartPlusView;
    @BindView(R.id.chart_minus)
    View mChartMinusView;
    @BindView(R.id.chart_left)
    View mChartLeftView;
    @BindView(R.id.chart_right)
    View mChartRightView;
    @BindView(R.id.chart_fullscreen)
    View mChartFullscreenView;

    private HandlerImpl mHandler;
    private ResponseListData<TempListEntity> mTempList;
    private ResponseListData<ActivityListEntity> mActivityList;
    private Map<String, String> mUnSelectedTagMap = new HashMap<>();
    private int[] mColors = new int[]{0xFFe6194B, 0xFF3cb44b, 0xFFffe119, 0xFF4363d8, 0xFFf58231,
            0xFF911eb4, 0xFF42d4f4, 0xFFf032e6, 0xFFbfef45, 0xFFfabed4,
            0xFF469990, 0xFFdcbeff, 0xFF9A6324, 0xFFfffac8, 0xFF800000,
            0xFFaaffc3, 0xFF808000, 0xFFffd8b1, 0xFF000075, 0xFFa9a9a9, 0xFF000000
    };
    private ChartEarTagAdapter mEarTagAdapter;
    private PigstyEntity mPigstyEntity;
    private MonChartContract.Query mQuery;
    private Calendar mQueryCalendar = Calendar.getInstance();

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_monitor_chart;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.setPadding(mContext, view);
        setPigstyEntity(mPigstyEntity);
        initLineChartView();
        setChartMenusStatus();
        mHandler = new HandlerImpl(this);

        String queryDate = getQueryDate(mQueryCalendar);
        mQuery = new MonChartContract.Query();
        mQuery.pigstyId = mPigstyEntity.getPigstyId();
        mQuery.beginTime = queryDate;
        mQuery.endTime = queryDate;

        mEarTagAdapter = new ChartEarTagAdapter(mContext);
        mEarTagRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mEarTagRecyclerView.setAdapter(mEarTagAdapter);
        mEarTagAdapter.setClickListener(entity -> {
            String earTag = entity.earTag;
            boolean selected = !entity.selected;
            if (selected) {
                mUnSelectedTagMap.remove(earTag);
            } else {
                mUnSelectedTagMap.put(earTag, earTag);
            }
            mEarTagAdapter.notifyItemChanged(earTag, selected);
            List<TempListEntity> tempRows = mTempList.getRows();
            for (TempListEntity itemEntity : tempRows) {
                if (StringUtils.equals(itemEntity.getEarTag(), earTag)) {
                    itemEntity.setSelected(selected);
                }
            }
            if (mActivityList != null) {
                List<ActivityListEntity> activityRows = mActivityList.getRows();
                for (ActivityListEntity itemEntity : activityRows) {
                    if (StringUtils.equals(itemEntity.getEarTag(), earTag)) {
                        itemEntity.setSelected(selected);
                    }
                }
            }
            setChartData();
        });
        mEarTagRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int mMargin = UIUtils.dip2px(5);

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position != 0) {
                    outRect.left = mMargin;
                }
            }
        });
        mEarTagRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    setEarScrollStatus();
                }
            }
        });

        setToDayValue(mQueryCalendar);
        setEarScrollStatus();
        setSelectorModel(mModelTempView);

        mPresenter.getTemperatures(mQuery);
    }

    @OnClick({R.id.back, R.id.prev_day, R.id.next_day, R.id.ear_tag_left,
            R.id.ear_tag_right, R.id.model_temp, R.id.model_activity,
            R.id.chart_plus, R.id.chart_minus, R.id.chart_left, R.id.chart_right, R.id.chart_fullscreen})
    protected void onClickView(View v) {
        if (!TimeUtils.havePast200msec()) {
            return;
        }
        switch (v.getId()) {
            case R.id.back: {
                getActivity().finish();
                break;
            }
            case R.id.prev_day: {
                mQueryCalendar.add(Calendar.DATE, -1);
                String queryDate = getQueryDate(mQueryCalendar);
                mQuery.beginTime = queryDate;
                mQuery.endTime = queryDate;
                mTempList = null;
                mActivityList = null;
                setToDayValue(mQueryCalendar);
                setChartData();
                break;
            }
            case R.id.next_day: {
                mQueryCalendar.add(Calendar.DATE, 1);
                String queryDate = getQueryDate(mQueryCalendar);
                mQuery.beginTime = queryDate;
                mQuery.endTime = queryDate;
                mTempList = null;
                mActivityList = null;
                setToDayValue(mQueryCalendar);
                setChartData();
                break;
            }
            case R.id.ear_tag_left: {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mEarTagRecyclerView.getLayoutManager();
                int firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                mEarTagRecyclerView.smoothScrollToPosition(Math.max(firstPosition - 1, 0));
                break;
            }
            case R.id.ear_tag_right: {
                int itemCount = mEarTagRecyclerView.getAdapter().getItemCount();
                LinearLayoutManager layoutManager = (LinearLayoutManager) mEarTagRecyclerView.getLayoutManager();
                int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                mEarTagRecyclerView.smoothScrollToPosition(Math.min(lastPosition + 1, itemCount - 1));
                break;
            }
            case R.id.model_temp: {
                setSelectorModel(mModelTempView);
                setChartData();
                break;
            }
            case R.id.model_activity: {
                setSelectorModel(mModelActivityView);
                setChartData();
                break;
            }
            case R.id.chart_plus: {
                if (mLineChartView.isScaleXEnabled()) {
                    ViewPortHandler viewPortHandler = mLineChartView.getViewPortHandler();
                    float currentScaleX = Math.min(viewPortHandler.getScaleX() + 1.4f, viewPortHandler.getMaxScaleX());
                    mLineChartView.zoomAndCenterAnimated(currentScaleX, 1.0f,
                            mLineChartView.getLowestVisibleX(), 0,
                            YAxis.AxisDependency.LEFT, 200);
                    mChartPlusView.setEnabled(currentScaleX < viewPortHandler.getMaxScaleX());
                    mHandler.sendChangeChart();
                    LogUtils.e(TAG, " chart_plus - currentScaleX : " + currentScaleX);
                }
                break;
            }
            case R.id.chart_minus: {
                if (mLineChartView.isScaleXEnabled()) {
                    ViewPortHandler viewPortHandler = mLineChartView.getViewPortHandler();
                    float currentScaleX = Math.max(viewPortHandler.getScaleX() - 1.4f, viewPortHandler.getMinScaleX());
                    mLineChartView.zoomAndCenterAnimated(currentScaleX, 1.0f,
                            mLineChartView.getLowestVisibleX(), 0,
                            YAxis.AxisDependency.LEFT, 200);
                    mChartMinusView.setEnabled(currentScaleX > viewPortHandler.getMinScaleX());
                    mHandler.sendChangeChart();
                    LogUtils.e(TAG, " chart_minus - currentScaleX : " + currentScaleX);
                }
                break;
            }
            case R.id.chart_left: {
                XAxis xAxis = mLineChartView.getXAxis();
                float lowestVisibleX = mLineChartView.getLowestVisibleX();
                float visibleX = xAxis.mEntries[0];
                if (Math.abs(lowestVisibleX - visibleX) < 1) {
                    visibleX = xAxis.mEntries[1];
                }
                mLineChartView.moveViewToAnimated(visibleX, 0, YAxis.AxisDependency.LEFT, 200);
                mHandler.sendChangeChart();
                break;
            }
            case R.id.chart_right: {
                XAxis xAxis = mLineChartView.getXAxis();
                float lowestVisibleX = mLineChartView.getLowestVisibleX();
                float highestVisibleX = mLineChartView.getHighestVisibleX();
                float diff = Math.abs(highestVisibleX - xAxis.mEntries[xAxis.mEntryCount - 1]);
                float visibleX = Math.max(lowestVisibleX - diff, 0);
                if (diff < 0.05) {
                    float interval = Math.abs(xAxis.mEntries[1] - xAxis.mEntries[0]);
                    visibleX = Math.max(lowestVisibleX - interval, 0);
                }
                mLineChartView.moveViewToAnimated(visibleX, 0, YAxis.AxisDependency.RIGHT, 200);
                mHandler.sendChangeChart();
                break;
            }
            case R.id.chart_fullscreen: {

                break;
            }
        }
    }

    @Override
    public void performTemperatures(ResponseListData<TempListEntity> listData, int fromTag) {
        List<TempListEntity> rows = listData.getRows();
        for (TempListEntity itemEntity : rows) {
            itemEntity.setSelected(!mUnSelectedTagMap.containsKey(itemEntity.getEarTag()));
        }
        mPigCountView.setText("在线生猪：" + listData.getTotal());
        mTempList = listData;
        setTempChartsMenus(listData);
        mEarTagRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                setEarScrollStatus();
            }
        });
        setChartData();
    }

    @Override
    public void performActivitys(ResponseListData<ActivityListEntity> listData, int fromTag) {
        List<ActivityListEntity> rows = listData.getRows();
        for (ActivityListEntity itemEntity : rows) {
            itemEntity.setSelected(!mUnSelectedTagMap.containsKey(itemEntity.getEarTag()));
        }
        mPigCountView.setText("在线生猪：" + listData.getTotal());
        mActivityList = listData;
        setActivityChartsMenus(listData);
        mEarTagRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                setEarScrollStatus();
            }
        });
        setChartData();
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        if (error.code == ResponseException.ERROR.RESULT_CODE_201) {
            mPigCountView.setText("在线生猪：0");
            mLineChartView.setNoDataText("暂无数据");
        }
        ToastUtils.show(error.getPromptMessage());
    }

    public void setPigstyEntity(PigstyEntity pigstyEntity) {
        mPigstyEntity = pigstyEntity;
        if (mBackView != null && mPigstyEntity != null) {
            mBackView.setText(StringUtils.ASCII16ToString(mPigstyEntity.getPigstyName()));
        }
    }

    private void setToDayValue(Calendar instance) {
        int month = instance.get(Calendar.MONTH) + 1;
        int date = instance.get(Calendar.DAY_OF_MONTH);
        mTodayValueView.setText(month + "月" + date + "日");
    }

    private String getQueryDate(Calendar instance) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(instance.getTime());
        return dateStr;
    }

    // earTag 滚动状态
    private void setEarScrollStatus() {
        int itemCount = mEarTagRecyclerView.getAdapter().getItemCount();
        LinearLayoutManager layoutManager = (LinearLayoutManager) mEarTagRecyclerView.getLayoutManager();
        int firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        mEarTagLeftView.setEnabled(firstPosition != 0);
        mEarTagRightView.setEnabled(lastPosition != itemCount - 1);
    }

    // 请求图表数据
    private void setChartData() {
        if (mModelTempView.isSelected()) {
            if (mTempList == null) {
                mPresenter.getTemperatures(mQuery);
            } else {
                setTempChartData(mTempList);
            }
        } else {
            if (mActivityList == null) {
                mPresenter.getMovements(mQuery);
            } else {
                setActivityChartData(mActivityList);
            }
        }
    }

    //切换图表类型- 温度、活跃度
    private void setSelectorModel(View view) {
        if (view == mModelTempView) {
            mChartTitleView.setText(R.string.monitor_chart_model_temp);
            mModelTempView.setSelected(true);
            mModelTempView.setTextSize(14);
            TextPaint tempPaint = mModelTempView.getPaint();
            tempPaint.setFakeBoldText(true);
            mModelTempView.setBackgroundResource(R.drawable.img_monitor_temp_bg);

            mModelActivityView.setSelected(false);
            mModelActivityView.setTextSize(12);
            TextPaint activityPaint = mModelActivityView.getPaint();
            activityPaint.setFakeBoldText(false);
            mModelActivityView.setBackground(null);
        } else {
            mChartTitleView.setText(R.string.monitor_chart_activity_title);
            mModelActivityView.setSelected(true);
            mModelActivityView.setTextSize(14);
            TextPaint activityPaint = mModelActivityView.getPaint();
            activityPaint.setFakeBoldText(true);
            mModelActivityView.setBackgroundResource(R.drawable.img_monitor_activity_bg);

            mModelTempView.setSelected(false);
            mModelTempView.setTextSize(12);
            TextPaint tempPaint = mModelTempView.getPaint();
            tempPaint.setFakeBoldText(false);
            mModelTempView.setBackground(null);
        }
    }

    private void setChartMenusStatus() {
        float maxVisible = mLineChartView.getXAxis().getAxisMaximum();
        float lowestX = mLineChartView.getLowestVisibleX();
        float highestX = mLineChartView.getHighestVisibleX();
        ViewPortHandler viewPortHandler = mLineChartView.getViewPortHandler();
        mChartFoldView.setSelected(!viewPortHandler.isFullyZoomedOutX());
        mChartLeftView.setEnabled(highestX < maxVisible);
        mChartRightView.setEnabled(lowestX > 0);
        mChartPlusView.setEnabled(viewPortHandler.canZoomInMoreX());
        mChartMinusView.setEnabled(viewPortHandler.canZoomOutMoreX());
    }

    //初始化图表绘制
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

        //设置x轴
        mLineChartView.setExtraBottomOffset(10);
        XAxis xAxis = mLineChartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);//设置x轴的最小值
        xAxis.setAxisMaximum(143);//设置最大值
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextSize(12);
        xAxis.setDrawLabels(true);

        //缩放
        //mLineChartView.getViewPortHandler().getMatrixTouch().postScale(7.0f, 1f);
        //mLineChartView.fitScreen();

        mLineChartView.setVisibleXRangeMinimum(6);

        Legend legend = mLineChartView.getLegend();
        legend.setEnabled(false);

        Description description = new Description();
        description.setText("");
        mLineChartView.setDescription(description);
    }

    // 设置活跃度数据
    private void setActivityChartData(ResponseListData<ActivityListEntity> listData) {
        List<ActivityListEntity> rows = listData.getRows();
        List<ILineDataSet> lineDataSetList = new ArrayList<>();
        for (int j = 0; j < rows.size(); j++) {
            ActivityListEntity itemTemp = rows.get(j);
            if (!itemTemp.isSelected()) {
                continue;
            }
            List<ActivityListEntity.ItemEntity> datas = itemTemp.getDatas();
            List<Entry> entityList = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                ActivityListEntity.ItemEntity itemEntity = datas.get(i);
                entityList.add(new Entry(i, itemEntity.getMovement()));
            }
            int color = getColor(j);
            LineDataSet lineDataSet = new LineDataSet(entityList, ""/*itemTemp.getEarTag()*/);
            lineDataSet.setColor(color);
            lineDataSet.setCircleColor(color);
            lineDataSet.setLineWidth(1);
            lineDataSet.setValueTextSize(10);
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.setValueFormatter(new ValueFormatter() {
            });
            lineDataSetList.add(lineDataSet);
        }

        YAxis yAxisLeft = mLineChartView.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setAxisMaximum(2000);
        yAxisLeft.setGranularity(500);
        yAxisLeft.setTextSize(12);
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        YAxis axisRight = mLineChartView.getAxisRight();
        axisRight.setEnabled(false);

        List<ActivityListEntity.ItemEntity> tempDatas = rows.get(0).getDatas();
        XAxis xAxis = mLineChartView.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (value >= tempDatas.size()) {
                    return "";
                }
                ActivityListEntity.ItemEntity entity = tempDatas.get((int) value);
                String[] split = entity.getTime().split(" ");
                //return split[1] + "\n\r" + split[0];
                return split[1];
            }
        });

        LineData lineData = new LineData(lineDataSetList);
        mLineChartView.setData(lineData);

        mLineChartView.invalidate();
    }

    //设置温度数据
    private void setTempChartData(ResponseListData<TempListEntity> listData) {
        List<TempListEntity> rows = listData.getRows();
        List<ILineDataSet> lineDataSetList = new ArrayList<>();
        for (int j = 0; j < rows.size(); j++) {
            TempListEntity itemTemp = rows.get(j);
            if (!itemTemp.isSelected()) {
                continue;
            }
            List<TempListEntity.ItemEntity> datas = itemTemp.getDatas();
            List<Entry> entityList = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                TempListEntity.ItemEntity itemEntity = datas.get(i);
                entityList.add(new Entry(i, itemEntity.getTemperature()));
            }
            int color = getColor(j);
            LineDataSet lineDataSet = new LineDataSet(entityList, ""/*itemTemp.getEarTag()*/);
            lineDataSet.setColor(color);
            lineDataSet.setCircleColor(color);
            lineDataSet.setLineWidth(1);
            lineDataSet.setValueTextSize(10);
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.setValueFormatter(new ValueFormatter() {
            });
            lineDataSetList.add(lineDataSet);
        }

        YAxis yAxisLeft = mLineChartView.getAxisLeft();
        yAxisLeft.setAxisMinimum(25);
        yAxisLeft.setAxisMaximum(50);
        yAxisLeft.setGranularity(5);
        yAxisLeft.setTextSize(12);
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return ((int) value) + "°C";
            }
        });

        YAxis axisRight = mLineChartView.getAxisRight();
        axisRight.setEnabled(false);

        List<TempListEntity.ItemEntity> tempDatas = rows.get(0).getDatas();
        XAxis xAxis = mLineChartView.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                //return String.valueOf((int) value);
                if (value >= tempDatas.size()) {
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

        mLineChartView.invalidate();
    }

    //温度 earTag
    private void setTempChartsMenus(ResponseListData<TempListEntity> listData) {
        List<TempListEntity> rows = listData.getRows();
        List<ChartEarTagAdapter.MenuEntity> earTagMenuList = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            TempListEntity itemEntity = rows.get(i);
            ChartEarTagAdapter.MenuEntity menuEntity = new ChartEarTagAdapter.MenuEntity();
            menuEntity.color = getColor(i);
            menuEntity.earTag = itemEntity.getEarTag();
            earTagMenuList.add(menuEntity);
        }
        mEarTagAdapter.setList(earTagMenuList);
        mEarTagAdapter.notifyDataSetChanged();
    }

    //活跃度 earTag
    private void setActivityChartsMenus(ResponseListData<ActivityListEntity> listData) {
        List<ActivityListEntity> rows = listData.getRows();
        List<ChartEarTagAdapter.MenuEntity> earTagMenuList = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            ActivityListEntity itemEntity = rows.get(i);
            ChartEarTagAdapter.MenuEntity menuEntity = new ChartEarTagAdapter.MenuEntity();
            menuEntity.color = getColor(i);
            menuEntity.earTag = itemEntity.getEarTag();
            earTagMenuList.add(menuEntity);
        }
        mEarTagAdapter.setList(earTagMenuList);
        mEarTagAdapter.notifyDataSetChanged();
    }

    private int getColor(int position) {
        return mColors[position % mColors.length];
    }

    public static class HandlerImpl extends Handler {

        public final static int WHAT_CHART_CHANGE = 100;

        private WeakReference<MonChartFragment> mFragmentWeakRef = null;

        public HandlerImpl(MonChartFragment fragment) {
            mFragmentWeakRef = new WeakReference<MonChartFragment>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_CHART_CHANGE: {
                    MonChartFragment fragment = mFragmentWeakRef.get();
                    if (fragment != null) {
                        fragment.setChartMenusStatus();
                    }
                    break;
                }
            }
        }

        public void sendChangeChart() {
            removeMessages(WHAT_CHART_CHANGE);
            Message message = Message.obtain();
            message.what = WHAT_CHART_CHANGE;
            sendEmptyMessageDelayed(WHAT_CHART_CHANGE, 250);
        }
    }

}
