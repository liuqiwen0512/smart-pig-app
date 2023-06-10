package com.wuzi.pig.module.monitor;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MotionEvent;
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
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseFragment;
import com.wuzi.pig.constant.MonitorConstant;
import com.wuzi.pig.entity.ActivityListEntity;
import com.wuzi.pig.entity.ResponseListData;
import com.wuzi.pig.entity.TempListEntity;
import com.wuzi.pig.module.monitor.adapter.ChartEarTagAdapter;
import com.wuzi.pig.module.monitor.contract.MonChartContract;
import com.wuzi.pig.module.monitor.presenter.MonChartPresenter;
import com.wuzi.pig.module.monitor.tools.MonTools;
import com.wuzi.pig.net.factory.ResponseException;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.ToastUtils;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.fun.Function2;
import com.wuzi.pig.utils.listener.OnChartGestureListenerImpl;
import com.wuzi.pig.utils.tools.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MonPigstyChartLandscapeFragment extends BaseFragment<MonChartPresenter> implements MonChartContract.IView {

    @BindView(R.id.back)
    AppCompatTextView mBackView;
    @BindView(R.id.today_value)
    AppCompatTextView mTodayValueView;
    @BindView(R.id.today_nickname)
    AppCompatTextView mTodayNicknameView;
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

    private Function2<String, Object> mEventListener;
    private ChartEarTagAdapter mEarTagAdapter;
    private MonChartContract.UIEntity mUIEntity;
    private boolean mIsRefreshData;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_monitor_pigsty_chart_landscape;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        int statusBarHeight = StatusBarUtils.getStatusBarHeight(mContext);
        view.setPadding(statusBarHeight, 0, 0, 0);

        mEarTagAdapter = new ChartEarTagAdapter(mContext, ChartEarTagAdapter.TYPE_LANDSCAPE);
        mEarTagRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mEarTagRecyclerView.setAdapter(mEarTagAdapter);
        mEarTagAdapter.setClickListener(entity -> {
            String earTag = entity.earTag;
            boolean selected = !entity.selected;
            if (selected) {
                mUIEntity.unSelectedTagMap.remove(earTag);
            } else {
                mUIEntity.unSelectedTagMap.put(earTag, earTag);
            }
            mEarTagAdapter.notifyItemChanged(earTag, selected);
            if (mUIEntity.tempList != null) {
                List<TempListEntity> tempRows = mUIEntity.tempList.getRows();
                for (TempListEntity itemEntity : tempRows) {
                    if (StringUtils.equals(itemEntity.getEarTag(), earTag)) {
                        itemEntity.setSelected(selected);
                    }
                }
                if (mUIEntity.model == MonitorConstant.MODEL_TEMPERATURES) {
                    setTempChartData(mUIEntity.tempList);
                }
            }
            if (mUIEntity.activityList != null) {
                List<ActivityListEntity> activityRows = mUIEntity.activityList.getRows();
                for (ActivityListEntity itemEntity : activityRows) {
                    if (StringUtils.equals(itemEntity.getEarTag(), earTag)) {
                        itemEntity.setSelected(selected);
                    }
                }
                if (mUIEntity.model == MonitorConstant.MODEL_ACTIVITY) {
                    setActivityChartData(mUIEntity.activityList);
                }
            }
        });
        mEarTagRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int mMargin = UIUtils.dip2px(5);

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position != 0) {
                    outRect.top = mMargin;
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

        setUIEntity(mUIEntity);
        //mPresenter.getTemperatures(mQuery);
    }

    @OnClick({R.id.today_layout, R.id.screen_orientation, R.id.ear_tag_left, R.id.ear_tag_right, R.id.model_temp, R.id.model_activity})
    protected void onClickView(View v) {
        if (!TimeUtils.havePast200msec()) {
            return;
        }
        switch (v.getId()) {
            case R.id.today_layout: {
                if (mEventListener != null) {
                    mEventListener.action(MonPigstyChartMainFragment.EVENT_DATE, mUIEntity.calendar);
                }
                break;
            }
            case R.id.screen_orientation: {
                if (mEventListener != null) {
                    mEventListener.action(MonPigstyChartMainFragment.EVENT_PORTRAIT, null);
                }
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
                mUIEntity.model = MonitorConstant.MODEL_TEMPERATURES;
                setSelectorModel(mModelTempView);
                if (mUIEntity.tempList != null) {
                    setTempChartData(mUIEntity.tempList);
                } else if (mEventListener != null) {
                    mEventListener.action(MonPigstyChartMainFragment.EVENT_MODEL_TEMPERATURE, mUIEntity.calendar);
                }
                break;
            }
            case R.id.model_activity: {
                mUIEntity.model = MonitorConstant.MODEL_ACTIVITY;
                setSelectorModel(mModelActivityView);
                if (mUIEntity.activityList != null) {
                    setActivityChartData(mUIEntity.activityList);
                } else if (mEventListener != null) {
                    mEventListener.action(MonPigstyChartMainFragment.EVENT_MODEL_ACTIVITY, mUIEntity.calendar);
                }
                break;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mIsRefreshData) {
            mLineChartView.post(() -> {
                setUIEntity(mUIEntity);
            });
        }
    }

    public void setUIEntity(MonChartContract.UIEntity UIEntity) {
        mUIEntity = UIEntity;
        mIsRefreshData = !isVisible();
        if (mLineChartView == null) return;

        initLineChartView();

        switch (mUIEntity.model) {
            case MonitorConstant.MODEL_TEMPERATURES:
                setSelectorModel(mModelTempView);
                if (mUIEntity.tempList == null) {
                    performError(new ResponseException(ResponseException.ERROR.RESULT_CODE_201), MonChartContract.TAG_TEMPERATURES);
                } else {
                    setTempChartsMenus(mUIEntity.tempList);
                    setTempChartData(mUIEntity.tempList);
                }
                break;
            case MonitorConstant.MODEL_ACTIVITY:
                setSelectorModel(mModelActivityView);
                if (mUIEntity.activityList == null) {
                    performError(new ResponseException(ResponseException.ERROR.RESULT_CODE_201), MonChartContract.TAG_ACTIVITY);
                } else {
                    setActivityChartsMenus(mUIEntity.activityList);
                    setActivityChartData(mUIEntity.activityList);
                }
                break;
        }

        if (mBackView != null && mUIEntity.pigstyEntity != null) {
            mBackView.setText(StringUtils.ASCII16ToString(mUIEntity.pigstyEntity.getPigstyName()));
        }

        mTodayValueView.setText(MonTools.formatTime(mUIEntity.calendar));
        mTodayNicknameView.setText(MonTools.nicknameOfTime(mUIEntity.calendar));

        mEarTagRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                setChartMenusStatus();
                setEarScrollStatus();
            }
        });
    }

    public void setEventListener(Function2<String, Object> eventListener) {
        mEventListener = eventListener;
    }

    @Override
    public void performTemperatures(ResponseListData<TempListEntity> listData, int fromTag) {
        setTempChartsMenus(listData);
        setTempChartData(listData);
        mEarTagRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                setEarScrollStatus();
            }
        });
    }

    @Override
    public void performActivitys(ResponseListData<ActivityListEntity> listData, int fromTag) {
        setActivityChartsMenus(listData);
        setActivityChartData(listData);
        mEarTagRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                setEarScrollStatus();
            }
        });
    }

    @Override
    public void performError(ResponseException error, int fromTag) {
        if (error.code == ResponseException.ERROR.RESULT_CODE_201) {
            String model = "";
            if (fromTag == MonChartContract.TAG_TEMPERATURES) {
                model = getString(R.string.monitor_chart_model_temp);
            } else if (fromTag == MonChartContract.TAG_ACTIVITY) {
                model = getString(R.string.monitor_chart_model_activity);
            }
            mPigCountView.setText("在线生猪：0");
            mEarTagAdapter.setList(new ArrayList<>());
            mEarTagAdapter.notifyDataSetChanged();
            mLineChartView.setData(null);
            mLineChartView.setNoDataText("暂无" + model + "数据");
            mLineChartView.invalidate();
            setEarScrollStatus();
            setChartMenusStatus();
        }
        ToastUtils.show(error.getPromptMessage());
    }

    // earTag 滚动状态
    private void setEarScrollStatus() {
        int itemCount = mEarTagRecyclerView.getAdapter().getItemCount();
        LinearLayoutManager layoutManager = (LinearLayoutManager) mEarTagRecyclerView.getLayoutManager();
        int firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        mEarTagLeftView.setEnabled(itemCount != 0 && firstPosition != 0);
        mEarTagRightView.setEnabled(itemCount != 0 && lastPosition != itemCount - 1);
    }

    //切换图表类型- 温度、活跃度
    private void setSelectorModel(View view) {
        if (view == mModelTempView) {
            mChartTitleView.setText(R.string.monitor_chart_model_temp);
            mModelTempView.setSelected(true);
            mModelTempView.setTextSize(14);
            TextPaint tempPaint = mModelTempView.getPaint();
            tempPaint.setFakeBoldText(true);
            mModelTempView.setBackgroundResource(R.drawable.shape_monitor_chart_model_bg);

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
            mModelActivityView.setBackgroundResource(R.drawable.shape_monitor_chart_model_bg);

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

        mLineChartView.setVisibleXRangeMinimum(12);

        Legend legend = mLineChartView.getLegend();
        legend.setEnabled(false);

        Description description = new Description();
        description.setText("");
        mLineChartView.setDescription(description);

        mLineChartView.setOnChartGestureListener(new OnChartGestureListenerImpl() {
            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                setChartMenusStatus();
            }
        });
    }

    // 设置活跃度数据
    private void setActivityChartData(ResponseListData<ActivityListEntity> listData) {
        if (listData == null) return;
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
        if (listData == null) return;
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
        if (listData == null) return;
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
        mPigCountView.setText("在线生猪：" + listData.getTotal());
    }

    //活跃度 earTag
    private void setActivityChartsMenus(ResponseListData<ActivityListEntity> listData) {
        if (listData == null) return;
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
        mPigCountView.setText("在线生猪：" + listData.getTotal());
    }

    private int getColor(int position) {
        return mUIEntity.colors[position % mUIEntity.colors.length];
    }
}
