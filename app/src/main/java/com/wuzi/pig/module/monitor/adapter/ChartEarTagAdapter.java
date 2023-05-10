package com.wuzi.pig.module.monitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.entity.TempListEntity;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartEarTagAdapter extends RecyclerView.Adapter<ChartEarTagAdapter.ViewHolder> {

    private Context mContext;
    private List<MenuEntity> mList = new ArrayList<>();
    private Function<MenuEntity> mClickListener;

    public ChartEarTagAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_chart_ear_tag, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.color)
        View mColorView;
        @BindView(R.id.name)
        AppCompatTextView mNameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MenuEntity menuEntity = mList.get(position);
            mColorView.setBackgroundColor(menuEntity.color);
            mNameView.setText(menuEntity.temp.getEarTag());
            mNameView.setTextColor(menuEntity.selected ? menuEntity.color : ContextCompat.getColor(mContext, R.color.grey_76));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mClickListener != null && position != -1) {
                MenuEntity menuEntity = mList.get(position);
                mClickListener.action(menuEntity);
            }
        }
    }

    public void setList(List<MenuEntity> list) {
        mList.clear();
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void notifyItemChanged(MenuEntity entity, boolean selected) {
        for (int i = 0; i < mList.size(); i++) {
            TempListEntity temp = entity.temp;
            TempListEntity itemTemp = mList.get(i).temp;
            if (StringUtils.equals(temp.getEarTag(), itemTemp.getEarTag())) {
                mList.get(i).selected = selected;
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void setClickListener(Function<MenuEntity> clickListener) {
        mClickListener = clickListener;
    }

    public static class MenuEntity {
        public int color;
        public TempListEntity temp;
        public boolean selected = true;
    }
}
