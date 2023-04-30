package com.wuzi.pig.module.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function3;
import com.wuzi.pig.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    public final static int CLICK_PIGSTY = 100;
    public final static int CLICK_TARGET = 200;

    private Context mContext;
    private Function3<View, PigFarmEntity, Integer> mEventListener;
    private List<PigFarmEntity> mList = new ArrayList<>();

    public SearchAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_search_pig_farm, parent, false);
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

        @BindView(R.id.name)
        AppCompatTextView mNameView;
        @BindView(R.id.arrow)
        View mArrowView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PigFarmEntity entity = mList.get(position);
            mNameView.setText(entity.getPigfarmName());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position == -1) return;
            PigFarmEntity entity = mList.get(position);
            mEventListener.action(v, entity, CLICK_TARGET);
        }
    }

    public void notifyDataSetChanged(List<PigFarmEntity> list) {
        mList.clear();
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void notifyItemInserted(List<PigFarmEntity> list) {
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
            notifyItemRangeInserted(mList.size(), list.size());
        }
    }

    public void notifyItemChanged(PigFarmEntity entity) {
        for (int i = 0; i < mList.size(); i++) {
            PigFarmEntity item = mList.get(i);
            if (StringUtils.equals(entity.getPigfarmId(), item.getPigfarmId())) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    public List<PigFarmEntity> getList() {
        return mList;
    }

    public void setEventListener(Function3<View, PigFarmEntity, Integer> eventListener) {
        mEventListener = eventListener;
    }
}
