package io.tnmma.ezwallet.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.SortTypeItem;
import io.tnmma.ezwallet.databinding.ItemSortOrderBinding;
import io.tnmma.ezwallet.databinding.ItemSortTypeBinding;

public class SortTypeAdapter extends BaseAdapter<SortTypeItem, ItemSortTypeBinding> {

    private static final String TAG = SortTypeAdapter.class.getSimpleName();

    public SortTypeAdapter() {
        super(SortTypeItem.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_sort_type;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemSortTypeBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemSortTypeBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SortTypeItem item = getItem(viewHolder.getBindingAdapterPosition());
                        setSelectedItem(item.getSortType());
                    }
                });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemSortTypeBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    public SortTypeItem getSelectedItem() {
        for (SortTypeItem item : getCurrentList()) {
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    private void setSelectedItem(SortType sortType) {
        for (int i = 0; i < getItemCount(); ++i) {
            SortTypeItem item = getItem(i);
            if (item.isSelected() && item.getSortType() != sortType) {
                item.setSelected(false);
                notifyItemChanged(i);
            }
            if (!item.isSelected() && item.getSortType() == sortType) {
                item.setSelected(true);
                notifyItemChanged(i);
            }
        }
    }
}
