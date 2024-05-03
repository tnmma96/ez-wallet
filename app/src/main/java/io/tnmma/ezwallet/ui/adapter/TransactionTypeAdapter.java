package io.tnmma.ezwallet.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.TransactionTypeItem;
import io.tnmma.ezwallet.databinding.ItemTransactionTypeBinding;

public class TransactionTypeAdapter
        extends BaseAdapter<TransactionTypeItem, ItemTransactionTypeBinding> {

    private static final String TAG = TransactionTypeAdapter.class.getSimpleName();

    public TransactionTypeAdapter() {
        super(TransactionTypeItem.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_transaction_type;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemTransactionTypeBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemTransactionTypeBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TransactionTypeItem item = getItem(viewHolder.getBindingAdapterPosition());
                        setSelectedItem(item.getTransactionType());
                    }
                });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemTransactionTypeBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    public TransactionTypeItem getSelectedItem() {
        for (TransactionTypeItem item : getCurrentList()) {
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    private void setSelectedItem(TransactionType transactionType) {
        for (int i = 0; i < getItemCount(); ++i) {
            TransactionTypeItem item = getItem(i);
            if (item.isSelected() && item.getTransactionType() != transactionType) {
                item.setSelected(false);
                notifyItemChanged(i);
            }
            if (!item.isSelected() && item.getTransactionType() == transactionType) {
                item.setSelected(true);
                notifyItemChanged(i);
            }
        }
    }
}
