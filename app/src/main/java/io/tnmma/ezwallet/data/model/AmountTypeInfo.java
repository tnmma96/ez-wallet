package io.tnmma.ezwallet.data.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.AmountType;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.util.AmountUtil;

public class AmountTypeInfo {

    private static final String TAG = AmountTypeInfo.class.getSimpleName();

    public static final DiffUtil.ItemCallback<AmountTypeInfo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull AmountTypeInfo oldItem, @NonNull AmountTypeInfo newItem) {
                    return true;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull AmountTypeInfo oldItem, @NonNull AmountTypeInfo newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(
                        @NonNull AmountTypeInfo oldItem, @NonNull AmountTypeInfo newItem) {
                    Bundle diffBundle = new Bundle();
                    if (Math.abs(oldItem.amount - newItem.amount)
                            >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
                        diffBundle.putString(
                                ConstantKeys.AmountTypeInfo.AMOUNT, newItem.getFormattedAmount());
                    }
                    return diffBundle.isEmpty() ? null : diffBundle;
                }
            };

    private final AmountType amountType;

    private final double amount;

    public AmountTypeInfo(AmountType amountType, double amount) {
        this.amountType = amountType;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        return AmountUtil.format(amount);
    }

    public int getIconResId() {
        if (amountType == null) {
            return 0;
        }
        return amountType == AmountType.AMOUNT
                ? R.drawable.icon_amount
                : R.drawable.icon_balance;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        AmountTypeInfo amountTypeInfo = (AmountTypeInfo) obj;
        if (this.amountType != amountTypeInfo.amountType) {
            return false;
        }
        if (this.amount != amountTypeInfo.amount) {
            return false;
        }
        return true;
    }
}
