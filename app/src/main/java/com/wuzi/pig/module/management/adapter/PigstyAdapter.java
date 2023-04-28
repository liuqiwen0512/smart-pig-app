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
import com.wuzi.pig.entity.PigstyEntity;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function3;
import com.wuzi.pig.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PigstyAdapter extends RecyclerView.Adapter<PigstyAdapter.ViewHolder> {

    public final static int CLICK_EDIT = 100;
    public final static int CLICK_DELETE = 200;
    public final static int CLICK_ACTION = 100;

    private Context mContext;
    private Function3<View, PigstyEntity, Integer> mEventListener;
    private List<PigstyEntity> mList = new ArrayList<>();
    private Map<String, Object> mCheckedMap = new HashMap<>();
    private boolean isEdit = false;

    public PigstyAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_pig_sty_edit, parent, false);
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

    public void notifyDataSetChanged(List<PigstyEntity> list) {
        mList.clear();
        mCheckedMap.clear();
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void notifyItemInserted(List<PigstyEntity> list) {
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
            notifyItemRangeInserted(mList.size(), list.size());
        }
    }

    public void notifyItemChanged(PigstyEntity entity) {
        for (int i = 0; i < mList.size(); i++) {
            PigstyEntity item = mList.get(i);
            if (StringUtils.equals(entity.getPigstyId(), item.getPigstyId())) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void notifyItemChecked(PigstyEntity entity, boolean checked) {
        if (checked) {
            mCheckedMap.put(entity.getPigstyId(), entity);
        } else {
            mCheckedMap.remove(entity.getPigstyId());
        }
        notifyItemChanged(entity);
    }

    public void notifyCheckedAll(boolean checked) {
        if (checked) {
            for (int i = 0; i < mList.size(); i++) {
                PigstyEntity itemEntity = mList.get(i);
                if (mCheckedMap.size() >= PigFarmConstant.PIG_FARM_DELETE_ALL_MAX) {
                    break;
                }
                if (!mCheckedMap.containsKey(itemEntity.getPigstyId())) {
                    mCheckedMap.put(itemEntity.getPigstyId(), itemEntity);
                    notifyItemChanged(i);
                }
            }
        } else {
            List<String> ids = new ArrayList<>(mCheckedMap.keySet());
            for (String itemId: ids) {
                int position = -1;
                for (int i = 0; i < mList.size(); i++) {
                    PigstyEntity itemEntity = mList.get(i);
                    if (StringUtils.equals(itemId, itemEntity.getPigstyId())) {
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
                PigstyEntity itemEntity = mList.get(i);
                if (StringUtils.equals(itemId, itemEntity.getPigstyId())) {
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

    public List<PigstyEntity> getList() {
        return mList;
    }

    public void setEventListener(Function3<View, PigstyEntity, Integer> eventListener) {
        mEventListener = eventListener;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        @BindView(R.id.checkbox)
        AppCompatCheckBox mCheckBoxView;
        @BindView(R.id.name)
        AppCompatTextView mNameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            mCheckBoxView.setOnCheckedChangeListener(this);
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PigstyEntity entity = mList.get(position);
            mNameView.setText(StringUtils.ASCII16ToString(entity.getPigstyName()));

            mCheckBoxView.setVisibility(isEdit ? View.VISIBLE : View.GONE);
            mCheckBoxView.setChecked(mCheckedMap.containsKey(entity.getPigstyId()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position == -1) return;
            PigstyEntity entity = mList.get(position);
            if (!isEdit && mEventListener != null) {
                mEventListener.action(v, entity, CLICK_ACTION);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!buttonView.isPressed()) return;
            int position = getAdapterPosition();
            if (position == -1) return;
            PigstyEntity entity = mList.get(position);
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
}
