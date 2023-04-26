package com.wuzi.pig.module.management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.wuzi.pig.R;
import com.wuzi.pig.constant.PigFarmConstant;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function3;
import com.wuzi.pig.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PigFarmAdapter extends RecyclerView.Adapter<PigFarmAdapter.ViewHolder> {

    public final static int CLICK_EDIT = 100;
    public final static int CLICK_DELETE = 200;
    public final static int CLICK_ACTION = 300;

    private Context mContext;
    private Function3<View, PigFarmEntity, Integer> mEventListener;
    private List<PigFarmEntity> mList = new ArrayList<>();
    private Map<String, Object> mCheckedMap = new HashMap<>();
    private boolean isEdit = false;

    public PigFarmAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_pig_farm, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        @BindView(R.id.checkbox)
        AppCompatCheckBox mCheckBoxView;
        @BindView(R.id.name)
        AppCompatTextView mNameView;
        @BindView(R.id.edit)
        View mEditView;
        @BindView(R.id.arrow)
        View mArrowView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            mEditView.setOnClickListener(this);
            mCheckBoxView.setOnCheckedChangeListener(this);
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PigFarmEntity entity = mList.get(position);
            mNameView.setText(entity.getPigfarmName());

            mCheckBoxView.setVisibility(isEdit ? View.VISIBLE : View.GONE);
            mEditView.setVisibility(isEdit ? View.VISIBLE : View.GONE);
            mArrowView.setVisibility(!isEdit ? View.VISIBLE : View.GONE);

            mCheckBoxView.setChecked(mCheckedMap.containsKey(entity.getPigfarmId()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position == -1) return;
            PigFarmEntity entity = mList.get(position);
            switch (v.getId()) {
                case R.id.edit: {
                    if (mEventListener != null) {
                        mEventListener.action(v, entity, CLICK_EDIT);
                    }
                    break;
                }
                default:{
                    if (!isEdit && mEventListener != null) {
                        mEventListener.action(v, entity, CLICK_ACTION);
                    }
                    break;
                }
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!buttonView.isPressed()) return;
            int position = getAdapterPosition();
            if (position == -1) return;
            PigFarmEntity entity = mList.get(position);
            switch (buttonView.getId()) {
                case R.id.checkbox: {
                    if (mEventListener != null) {
                        mEventListener.action(buttonView, entity, CLICK_DELETE);
                    }
                    break;
                }
            }
        }
    }

    public void notifyDataSetChanged(List<PigFarmEntity> list) {
        mList.clear();
        mCheckedMap.clear();
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
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

    public void notifyItemChecked(PigFarmEntity entity, boolean checked) {
        if (checked) {
            mCheckedMap.put(entity.getPigfarmId(), entity);
        } else {
            mCheckedMap.remove(entity.getPigfarmId());
        }
        notifyItemChanged(entity);
    }

    public void notifyCheckedAll(boolean checked) {
        if (checked) {
            for (int i = 0; i < mList.size(); i++) {
                PigFarmEntity itemEntity = mList.get(i);
                if (mCheckedMap.size() >= PigFarmConstant.PIG_FARM_DELETE_ALL_MAX) {
                    break;
                }
                if (!mCheckedMap.containsKey(itemEntity.getPigfarmId())) {
                    mCheckedMap.put(itemEntity.getPigfarmId(), itemEntity);
                    notifyItemChanged(i);
                }
            }
        } else {
            List<String> ids = new ArrayList<>(mCheckedMap.keySet());
            for (String itemId: ids) {
                int position = -1;
                for (int i = 0; i < mList.size(); i++) {
                    PigFarmEntity itemEntity = mList.get(i);
                    if (StringUtils.equals(itemId, itemEntity.getPigfarmId())) {
                        position = i;
                        break;
                    }
                }
                mCheckedMap.remove(itemId);
                if (position != -1) {
                    notifyItemChanged(position);
                }
            }
        }
    }

    public void notifyItemRemovedByChecked() {
        for (String itemId: mCheckedMap.keySet()) {
            int position = -1;
            for (int i = 0; i < mList.size(); i++) {
                PigFarmEntity itemEntity = mList.get(i);
                if (StringUtils.equals(itemId, itemEntity.getPigfarmId())) {
                    position = i;
                    break;
                }
            }
            if (position != -1) {
                mList.remove(position);
                notifyItemRemoved(position);
            }
        }
        mCheckedMap.clear();
    }

    public List<PigFarmEntity> getList() {
        return mList;
    }

    public Map<String, Object> getCheckedMap() {
        return mCheckedMap;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEventListener(Function3<View, PigFarmEntity, Integer> eventListener) {
        mEventListener = eventListener;
    }
}
