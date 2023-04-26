package com.wuzi.pig.module.monitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.entity.PigstyEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PigstyAdapter extends RecyclerView.Adapter<PigstyAdapter.ViewHolder> {

    private Context mContext;
    private List<PigstyEntity> mList = new ArrayList<>();
    private boolean isEdit = true;

    public PigstyAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_pigsty, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        AppCompatTextView mNameView;
        @BindView(R.id.pig_count)
        AppCompatTextView mPigCountView;
        @BindView(R.id.pig_temperature)
        AppCompatTextView mPigTemperatureView;
        @BindView(R.id.pig_liveness)
        AppCompatTextView mPigLivenessView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PigstyEntity entity = mList.get(position);
            mNameView.setText(entity.getName());
            mPigCountView.setText("在线：" + entity.getOnlineCount() + "头");
            mPigTemperatureView.setText("平均温度：" + entity.getTemperature() + "°C");
            mPigLivenessView.setText("平均活跃度：" + entity.getLiveness());
        }
    }

    public void setList(List<PigstyEntity> list) {
        mList = list;
        notifyDataSetChanged();
    }

}
