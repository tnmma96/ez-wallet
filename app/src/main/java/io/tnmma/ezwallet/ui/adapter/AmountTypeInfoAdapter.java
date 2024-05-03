package io.tnmma.ezwallet.ui.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.AmountTypeInfo;
import io.tnmma.ezwallet.databinding.ItemAmountTypeInfoBinding;

import java.util.List;

public class AmountTypeInfoAdapter extends BaseAdapter<AmountTypeInfo, ItemAmountTypeInfoBinding> {

    private static final String TAG = AmountTypeInfoAdapter.class.getSimpleName();

    public AmountTypeInfoAdapter() {
        super(AmountTypeInfo.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_amount_type_info;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAmountTypeInfoBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAmountTypeInfoBinding> holder,
            int position,
            @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Bundle diffBundle = (Bundle) payloads.get(0);
        for (String key : diffBundle.keySet()) {
            switch (key) {
                case ConstantKeys.AmountTypeInfo.AMOUNT:
                    holder.getBinding().itemAmountTypeInfoValue.setText(diffBundle.getString(key));
                    break;
            }
        }
    }
}
