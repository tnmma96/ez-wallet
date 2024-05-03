package io.tnmma.ezwallet.data.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.util.AmountUtil;

public class AccountIncomeExpenseInfo {

    private static final String TAG = AccountIncomeExpenseInfo.class.getSimpleName();

    public static final DiffUtil.ItemCallback<AccountIncomeExpenseInfo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull AccountIncomeExpenseInfo oldItem,
                        @NonNull AccountIncomeExpenseInfo newItem) {
                    return true;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull AccountIncomeExpenseInfo oldItem,
                        @NonNull AccountIncomeExpenseInfo newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(
                        @NonNull AccountIncomeExpenseInfo oldItem,
                        @NonNull AccountIncomeExpenseInfo newItem) {
                    Bundle diffBundle = new Bundle();
                    if (Math.abs(oldItem.incomeAmount - newItem.incomeAmount)
                            >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
                        diffBundle.putString(
                                ConstantKeys.AccountIncomeExpenseInfo.INCOME_AMOUNT,
                                newItem.getFormattedIncomeAmount());
                    }
                    if (oldItem.incomeTransactionCount != newItem.incomeTransactionCount) {
                        diffBundle.putString(
                                ConstantKeys.AccountIncomeExpenseInfo.INCOME_COUNT,
                                newItem.getFormattedIncomeTransactionCount());
                    }
                    if (Math.abs(oldItem.expenseAmount - newItem.expenseAmount)
                            >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
                        diffBundle.putString(
                                ConstantKeys.AccountIncomeExpenseInfo.EXPENSE_AMOUNT,
                                newItem.getFormattedExpenseAmount());
                    }
                    if (oldItem.expenseTransactionCount != newItem.expenseTransactionCount) {
                        diffBundle.putString(
                                ConstantKeys.AccountIncomeExpenseInfo.EXPENSE_COUNT,
                                newItem.getFormattedExpenseTransactionCount());
                    }
                    return diffBundle.isEmpty() ? null : diffBundle;
                }
            };

    private double incomeAmount;

    private int incomeTransactionCount;

    private double expenseAmount;

    private int expenseTransactionCount;

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public String getFormattedIncomeAmount() {
        return AmountUtil.formatWithSuffixFromThousand(incomeAmount);
    }

    public int getIncomeTransactionCount() {
        return incomeTransactionCount;
    }

    public String getFormattedIncomeTransactionCount() {
        return String.valueOf(incomeTransactionCount);
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public String getFormattedExpenseAmount() {
        return AmountUtil.formatWithSuffixFromThousand(expenseAmount);
    }

    public int getExpenseTransactionCount() {
        return expenseTransactionCount;
    }

    public String getFormattedExpenseTransactionCount() {
        return String.valueOf(expenseTransactionCount);
    }

    public void addTransactionAmount(TransactionType transactionType, double amount) {
        switch (transactionType) {
            case INCOME:
                incomeAmount += amount;
                ++incomeTransactionCount;
                break;
            case EXPENSE:
                expenseAmount += amount;
                ++expenseTransactionCount;
                break;
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        final AccountIncomeExpenseInfo accountIncomeExpenseInfo = (AccountIncomeExpenseInfo) obj;
        if (Math.abs(this.incomeAmount - accountIncomeExpenseInfo.incomeAmount)
                >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
            return false;
        }
        if (this.incomeTransactionCount != accountIncomeExpenseInfo.incomeTransactionCount) {
            return false;
        }
        if (Math.abs(this.expenseAmount - accountIncomeExpenseInfo.expenseAmount)
                >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
            return false;
        }
        if (this.expenseTransactionCount != accountIncomeExpenseInfo.expenseTransactionCount) {
            return false;
        }
        return true;
    }
}
