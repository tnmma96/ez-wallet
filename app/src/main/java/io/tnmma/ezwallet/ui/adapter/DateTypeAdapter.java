package io.tnmma.ezwallet.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.DateType;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.DateTypeItem;
import io.tnmma.ezwallet.databinding.ItemDateTypeBinding;

public class DateTypeAdapter extends BaseAdapter<DateTypeItem, ItemDateTypeBinding> {

    private static final String TAG = DateTypeAdapter.class.getSimpleName();

    private final MutableLiveData<DateType> selectedDateTypeLiveData;

    public DateTypeAdapter() {
        super(DateTypeItem.DIFF_CALLBACK);
        selectedDateTypeLiveData = new MutableLiveData<>();
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_date_type;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemDateTypeBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemDateTypeBinding> viewHolder = super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTypeItem item = getItem(viewHolder.getBindingAdapterPosition());
                setSelectedItem(item.getDateType());
                if (selectedDateTypeLiveData.getValue() != item.getDateType()) {
                    selectedDateTypeLiveData.setValue(item.getDateType());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemDateTypeBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    public LiveData<DateType> getSelectedDateTypeLiveData() {
        return selectedDateTypeLiveData;
    }

    public DateTypeItem getSelectedItem() {
        for (DateTypeItem item : getCurrentList()) {
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    public int getSelectedPosition() {
        for (int i = 0; i < getItemCount(); ++i) {
            if (getItem(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void setSelectedItem(DateType dateType) {
        for (int i = 0; i < getItemCount(); ++i) {
            DateTypeItem item = getItem(i);
            if (item.isSelected() && item.getDateType() != dateType) {
                item.setSelected(false);
                notifyItemChanged(i);
            }
            if (!item.isSelected() && item.getDateType() == dateType) {
                item.setSelected(true);
                notifyItemChanged(i);
            }
        }
    }
}
