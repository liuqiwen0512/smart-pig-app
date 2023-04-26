package com.wuzi.pig.module.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.entity.MenuEntity;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context mContext;
    private Function<MenuEntity> mClickListener;
    private List<MenuEntity> mList = new ArrayList<>();
    private String mSelectedKey = null;

    public MenuAdapter(Context context, List<MenuEntity> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_main_menu, parent, false);
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

        @BindView(R.id.icon)
        ImageFilterView mIconView;
        @BindView(R.id.label)
        AppCompatTextView mLabelView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MenuEntity entity = mList.get(position);
            mIconView.setImageResource(entity.iconRes);
            mLabelView.setText(entity.label);

            boolean selected = StringUtils.equals(mSelectedKey, entity.key);
            mIconView.setSelected(selected);
            mLabelView.setSelected(selected);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mClickListener != null && position >= 0) {
                MenuEntity entity = mList.get(position);
                mClickListener.action(entity);
            }
        }
    }

    public void setClickListener(Function<MenuEntity> clickListener) {
        mClickListener = clickListener;
    }

    public void setSelected(String label) {
        mSelectedKey = label;
        notifyDataSetChanged();
    }

    public String getSelectedKey() {
        return mSelectedKey;
    }
}
