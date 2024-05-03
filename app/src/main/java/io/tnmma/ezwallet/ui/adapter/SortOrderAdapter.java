package io.tnmma.ezwallet.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.SortOrderItem;
import io.tnmma.ezwallet.databinding.ItemSortOrderBinding;

public class SortOrderAdapter extends BaseAdapter<SortOrderItem, ItemSortOrderBinding> {

    private static final String TAG = SortOrderAdapter.class.getSimpleName();

    public SortOrderAdapter() {
        super(SortOrderItem.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_sort_order;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemSortOrderBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemSortOrderBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SortOrderItem item = getItem(viewHolder.getBindingAdapterPosition());
                        setSelectedItem(item.getSortOrder());
                    }
                });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemSortOrderBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    public SortOrderItem getSelectedItem() {
        for (SortOrderItem item : getCurrentList()) {
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    private void setSelectedItem(SortOrder sortOrder) {
        for (int i = 0; i < getItemCount(); ++i) {
            SortOrderItem item = getItem(i);
            if (item.isSelected() && item.getSortOrder() != sortOrder) {
                item.setSelected(false);
                notifyItemChanged(i);
            }
            if (!item.isSelected() && item.getSortOrder() == sortOrder) {
                item.setSelected(true);
                notifyItemChanged(i);
            }
        }
    }
}
