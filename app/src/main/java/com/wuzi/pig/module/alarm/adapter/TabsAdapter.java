package com.wuzi.pig.module.alarm.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.constant.AlarmConstant;
import com.wuzi.pig.entity.OptionEntity;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabsAdapter extends RecyclerView.Adapter<TabsAdapter.ViewHolder> {

    private Context mContext;
    private Function<OptionEntity> mItemListener;
    private final OptionEntity[] mTabEntitys = AlarmConstant.PAGE_TABS;

    private String mSelectedTab = AlarmConstant.FRAGMENT_TEMPERATURE;

    public TabsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_alarm_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OptionEntity tabEntity = mTabEntitys[position];
        holder.mLabelView.setText(tabEntity.label);

        if (position == 0) {
            holder.mBgView.setImageResource(R.drawable.selector_alarm_tab_bg1);
        } else if (position == getItemCount() - 1) {
            holder.mBgView.setImageResource(R.drawable.selector_alarm_tab_bg3);
        } else {
            holder.mBgView.setImageResource(R.drawable.selector_alarm_tab_bg2);
        }

        boolean selected = StringUtils.equals(mSelectedTab, tabEntity.key);
        holder.mBgView.setSelected(selected);
        holder.mLabelView.setSelected(selected);
        holder.mLabelView.setTextSize(selected ? 14 : 12);
        TextPaint textPaint = holder.mLabelView.getPaint();
        textPaint.setFakeBoldText(selected);
    }

    @Override
    public int getItemCount() {
        return mTabEntitys.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.bg)
        ImageFilterView mBgView;
        @BindView(R.id.label)
        AppCompatTextView mLabelView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemListener != null) {
                int position = getAdapterPosition();
                if (position != -1) {
                    mItemListener.action(mTabEntitys[position]);
                }
            }
        }
    }

    public void notifyItemChanged(OptionEntity entity) {
        //TODO 去掉这个判断 notifyItemChanged无效
        if (StringUtils.equals(entity.key, mSelectedTab)) {
            return;
        }
        String oldSelectedTab = mSelectedTab;
        mSelectedTab = entity.key;
        for (int i = 0; i < mTabEntitys.length; i++) {
            OptionEntity itemEntity = mTabEntitys[i];
            if (StringUtils.equals(itemEntity.key, mSelectedTab)) {
                notifyItemChanged(i);
            }
            if (StringUtils.equals(itemEntity.key, oldSelectedTab)) {
                notifyItemChanged(i);
            }
        }
    }

    public void setItemListener(Function<OptionEntity> itemListener) {
        mItemListener = itemListener;
    }
}
