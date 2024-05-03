package io.tnmma.ezwallet.data.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.util.AmountUtil;

public class CategoryAmountInfo {

    private static final String TAG = CategoryAmountInfo.class.getSimpleName();

    public static final DiffUtil.ItemCallback<CategoryAmountInfo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull CategoryAmountInfo oldItem, @NonNull CategoryAmountInfo newItem) {
                    return true;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull CategoryAmountInfo oldItem, @NonNull CategoryAmountInfo newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(
                        @NonNull CategoryAmountInfo oldItem, @NonNull CategoryAmountInfo newItem) {
                    Bundle diffBundle = new Bundle();
                    if (Math.abs(oldItem.amount - newItem.amount)
                            >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
                        diffBundle.putString(
                                ConstantKeys.CategoryAmountInfo.AMOUNT,
                                newItem.getFormattedAmount());
                    }
                    if (oldItem.transactionCount != newItem.transactionCount) {
                        diffBundle.putString(
                                ConstantKeys.CategoryAmountInfo.COUNT,
                                newItem.getFormattedTransactionCount());
                    }
                    return diffBundle.isEmpty() ? null : diffBundle;
                }
            };

    private final CategoryType categoryType;

    private double amount;

    private int transactionCount;

    public CategoryAmountInfo(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public double getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        return AmountUtil.format(amount);
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public String getFormattedTransactionCount() {
        return String.valueOf(transactionCount);
    }

    public void addTransactionAmount(double amount) {
        this.amount += amount;
        ++transactionCount;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        final CategoryAmountInfo categoryAmountInfo = (CategoryAmountInfo) obj;
        if (this.categoryType != categoryAmountInfo.categoryType) {
            return false;
        }
        if (Math.abs(this.amount - categoryAmountInfo.amount) >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
            return false;
        }
        if (this.transactionCount != categoryAmountInfo.transactionCount) {
            return false;
        }
        return true;
    }
}
