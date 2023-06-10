package com.wuzi.pig.module.monitor.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function;
import com.wuzi.pig.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartEarTagAdapter extends RecyclerView.Adapter<ChartEarTagAdapter.ViewHolder> {

    public final static int TYPE_LANDSCAPE = 1;

    private Context mContext;
    private List<MenuEntity> mList = new ArrayList<>();
    private Function<MenuEntity> mClickListener;
    private int mType;

    public ChartEarTagAdapter(Context context) {
        this(context, 0);
    }

    public ChartEarTagAdapter(Context context, int type) {
        mContext = context;
        mType = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LANDSCAPE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_chart_landscape_ear_tag, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_chart_portrait_ear_tag, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return mType;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.bg)
        View mBgView;
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
            if (menuEntity.selected) {
                ViewCompat.setBackgroundTintList(mBgView, new ColorStateList(new int[][]{{}}, new int[]{menuEntity.color}));
            } else {
                ViewCompat.setBackgroundTintList(mBgView, new ColorStateList(new int[][]{{}}, new int[]{ResourcesCompat.getColor(mContext.getResources(), R.color.grey_df, null)}));
            }
            ViewCompat.setBackgroundTintList(mColorView, new ColorStateList(new int[][]{{}}, new int[]{menuEntity.color}));
            mNameView.setText(menuEntity.earTag);
            mNameView.setTextColor(menuEntity.selected ? menuEntity.color : ContextCompat.getColor(mContext, R.color.grey_df));
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

    public void notifyItemChanged(String earTag, boolean selected) {
        for (int i = 0; i < mList.size(); i++) {
            String itemEarTag = mList.get(i).earTag;
            if (StringUtils.equals(earTag, itemEarTag)) {
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
        public String earTag;
        public boolean selected = true;
    }
}
