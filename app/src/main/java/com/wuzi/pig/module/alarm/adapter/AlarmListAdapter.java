package com.wuzi.pig.module.alarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.constant.AlarmConstant;
import com.wuzi.pig.entity.AlarmEntity;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    public final static int TYPE_TEMPERATURE = 100;
    public final static int TYPE_ACTIVITY = 200;
    public final static int TYPE_EAR_TAG = 300;
    public final static int TYPE_BASE_STATION = 400;
    public final static int TYPE_OUTAGE = 500;

    private Context mContext;
    private Function<AlarmEntity> mItemListener;
    private List<AlarmEntity> mList = new ArrayList<>();

    private String mCurrentType = AlarmConstant.TYPE_TEMPERATURE;

    public AlarmListAdapter(Context context, String type) {
        mContext = context;
        mCurrentType = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewType = getViewType();
        switch (viewType) {
            case TYPE_TEMPERATURE: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_alarm_temperature, parent, false);
                return new TemperatureViewHolder(view);
            }
            case TYPE_ACTIVITY: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_alarm_activity, parent, false);
                return new ActivityViewHolder(view);
            }
            case TYPE_EAR_TAG: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_alarm_activity, parent, false);
                return new ActivityViewHolder(view);
            }
            case TYPE_BASE_STATION: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_alarm_activity, parent, false);
                return new BaseStationViewHolder(view);
            }
            case TYPE_OUTAGE: {
                View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_alarm_activity, parent, false);
                return new OutageViewHolder(view);
            }
        }
        View view = new View(mContext);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TemperatureViewHolder extends ViewHolder {

        @BindView(R.id.id)
        AppCompatTextView mIdView;
        @BindView(R.id.time)
        AppCompatTextView mTimeView;
        @BindView(R.id.name)
        AppCompatTextView mNameView;
        @BindView(R.id.type)
        AppCompatTextView mTypeView;

        public TemperatureViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AlarmEntity entity = mList.get(position);
            itemView.setSelected(position % 2 == 0 ? false : true);
            mIdView.setText(entity.getEarTag());
            mTimeView.setText(entity.getCreateTime());
            mNameView.setText(entity.getPigstyName());
            mTypeView.setText(entity.getLevel());

            mTypeView.setSelected(StringUtils.equals(entity.getLevel(), AlarmConstant.TEMPERATURE_LOW));
        }
    }

    class ActivityViewHolder extends ViewHolder {

        @BindView(R.id.id)
        AppCompatTextView mIdView;
        @BindView(R.id.time)
        AppCompatTextView mTimeView;
        @BindView(R.id.name)
        AppCompatTextView mNameView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AlarmEntity entity = mList.get(position);
            itemView.setSelected(position % 2 == 0 ? false : true);
            mIdView.setText(entity.getEarTag());
            mTimeView.setText(entity.getCreateTime());
            mNameView.setText(entity.getPigstyName());
        }
    }

    class BaseStationViewHolder extends ViewHolder {

        @BindView(R.id.id)
        AppCompatTextView mIdView;
        @BindView(R.id.time)
        AppCompatTextView mTimeView;
        @BindView(R.id.name)
        AppCompatTextView mNameView;

        public BaseStationViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AlarmEntity entity = mList.get(position);
            itemView.setSelected(position % 2 == 0 ? false : true);
            mIdView.setText(entity.getPigstyName());
            mTimeView.setText(entity.getCreateTime());
            mNameView.setText(entity.getLevel());
        }
    }

    class OutageViewHolder extends ViewHolder {

        @BindView(R.id.id)
        AppCompatTextView mIdView;
        @BindView(R.id.time)
        AppCompatTextView mTimeView;
        @BindView(R.id.name)
        AppCompatTextView mNameView;

        public OutageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AlarmEntity entity = mList.get(position);
            itemView.setSelected(position % 2 == 0 ? false : true);
            mIdView.setText(entity.getPigstyName());
            mTimeView.setText(entity.getCreateTime());
            mNameView.setText(entity.getLevel());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        }
    }

    public void notifyDataSetChanged(List<AlarmEntity> list) {
        mList.clear();
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void notifyItemInserted(List<AlarmEntity> list) {
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
            notifyItemRangeInserted(mList.size(), list.size());
        }
    }

    public void setItemListener(Function<AlarmEntity> itemListener) {
        mItemListener = itemListener;
    }

    private int getViewType() {
        switch (mCurrentType) {
            case AlarmConstant.TYPE_TEMPERATURE:
                return TYPE_TEMPERATURE;
            case AlarmConstant.TYPE_ACTIVITY:
                return TYPE_ACTIVITY;
            case AlarmConstant.TYPE_EAR_TAG:
                return TYPE_EAR_TAG;
            case AlarmConstant.TYPE_BASE_STATION:
                return TYPE_BASE_STATION;
            case AlarmConstant.TYPE_OUTAGE:
                return TYPE_OUTAGE;
        }
        return 0;
    }
}
