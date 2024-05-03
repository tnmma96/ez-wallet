package io.tnmma.ezwallet.data.pojo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Ignore;
import androidx.room.Relation;

import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.data.db.entity.TransactionEntity;
import io.tnmma.ezwallet.util.AmountUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AccountWithDetails extends Account {

    private static final String TAG = AccountWithDetails.class.getSimpleName();

    private static final Comparator<AccountWithDetails> DEFAULT_COMPARATOR =
            Comparator.comparing(AccountWithDetails::getCreatedTime).reversed();

    public static final DiffUtil.ItemCallback<AccountWithDetails> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull AccountWithDetails oldItem, @NonNull AccountWithDetails newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull AccountWithDetails oldItem, @NonNull AccountWithDetails newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(
                        @NonNull AccountWithDetails oldItem, @NonNull AccountWithDetails newItem) {
                    Bundle diffBundle = new Bundle();
                    if (!Objects.equals(oldItem.getName(), newItem.getName())) {
                        diffBundle.putString(ConstantKeys.Account.NAME, newItem.getName());
                    }
                    if (oldItem.getColor() != newItem.getColor()) {
                        diffBundle.putInt(ConstantKeys.Account.COLOR, newItem.getColor());
                    }
                    if (!Objects.equals(oldItem.getIcon(), newItem.getIcon())) {
                        diffBundle.putString(ConstantKeys.Account.ICON, newItem.getIcon());
                    }
                    if (Math.abs(oldItem.currentBalance - newItem.currentBalance)
                            >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
                        diffBundle.putString(
                                ConstantKeys.Account.CURRENT_BALANCE,
                                newItem.getFormattedCurrentBalance());
                    }
                    if (Math.abs(oldItem.totalIncome - newItem.totalIncome)
                            >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
                        diffBundle.putString(
                                ConstantKeys.Account.TOTAL_INCOME,
                                newItem.getFormattedTotalIncome());
                    }
                    if (Math.abs(oldItem.totalExpense - newItem.totalExpense)
                            >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
                        diffBundle.putString(
                                ConstantKeys.Account.TOTAL_EXPENSE,
                                newItem.getFormattedTotalExpense());
                    }
                    if (oldItem.getForegroundColor() != newItem.getForegroundColor()) {
                        diffBundle.putInt(
                                ConstantKeys.Account.FOREGROUND_COLOR,
                                newItem.getForegroundColor());
                    }
                    return diffBundle.isEmpty() ? null : diffBundle;
                }
            };

    @Relation(
            parentColumn = "id",
            entityColumn = "src_account_id",
            entity = TransactionEntity.class)
    private List<Transaction> srcTransactions;

    @Relation(
            parentColumn = "id",
            entityColumn = "dst_account_id",
            entity = TransactionEntity.class)
    private List<Transaction> dstTransactions;

    @Ignore private Double currentBalance = 0.0;

    @Ignore private Double totalIncome = 0.0;

    @Ignore private Double totalExpense = 0.0;

    public void setSrcTransactions(List<Transaction> srcTransactions) {
        this.srcTransactions = srcTransactions;
    }

    public void setDstTransactions(List<Transaction> dstTransactions) {
        this.dstTransactions = dstTransactions;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public String getFormattedCurrentBalance() {
        return AmountUtil.format(currentBalance);
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public String getFormattedTotalIncome() {
        return AmountUtil.formatWithSuffixFromMillion(totalIncome);
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public String getFormattedTotalExpense() {
        return AmountUtil.formatWithSuffixFromMillion(totalExpense);
    }

    private void resetAmount() {
        currentBalance = 0.0;
        totalIncome = 0.0;
        totalExpense = 0.0;
    }

    public void computeAmount() {
        resetAmount();
        for (Transaction transaction : srcTransactions) {
            computeAmountFromTransaction(transaction);
        }
        for (Transaction transaction : dstTransactions) {
            computeAmountFromTransaction(transaction);
        }
    }

    private void computeAmountFromTransaction(Transaction transaction) {
        if (transaction == null || transaction.getTransactionType() == null) {
            return;
        }
        switch (transaction.getTransactionType()) {
            case INCOME:
                totalIncome += transaction.getAmount();
                currentBalance += transaction.getAmount();
                break;
            case EXPENSE:
                totalExpense += transaction.getAmount();
                currentBalance -= transaction.getAmount();
                break;
            case TRANSFER:
                if (Objects.equals(getId(), transaction.getSrcAccountId())) {
                    totalExpense += transaction.getAmount();
                    currentBalance -= transaction.getAmount();
                } else if (Objects.equals(getId(), transaction.getDstAccountId())) {
                    totalIncome += transaction.getAmount();
                    currentBalance += transaction.getAmount();
                }
                break;
        }
    }

    public static Comparator<AccountWithDetails> getAccountComparator(
            SortType sortType, SortOrder sortOrder) {
        if (sortType == null || sortOrder == null) {
            return Comparator.nullsLast(DEFAULT_COMPARATOR);
        }
        Comparator<AccountWithDetails> comparator;
        switch (sortType) {
            case CREATED_TIME:
                return sortOrder == SortOrder.DESCENDING
                        ? Comparator.nullsLast(DEFAULT_COMPARATOR)
                        : Comparator.nullsLast(DEFAULT_COMPARATOR.reversed());
            case NAME:
                comparator = Comparator.comparing(AccountWithDetails::getName);
                break;
            case BALANCE:
                comparator = Comparator.comparing(AccountWithDetails::getCurrentBalance);
                break;
            case INCOME:
                comparator = Comparator.comparing(AccountWithDetails::getTotalIncome);
                break;
            case EXPENSE:
                comparator = Comparator.comparing(AccountWithDetails::getTotalExpense);
                break;
            default:
                return Comparator.nullsLast(DEFAULT_COMPARATOR);
        }
        return Comparator.nullsLast(
                        sortOrder == SortOrder.ASCENDING ? comparator : comparator.reversed())
                .thenComparing(Comparator.nullsLast(DEFAULT_COMPARATOR));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        AccountWithDetails account = (AccountWithDetails) obj;
        if (Math.abs(this.currentBalance - account.currentBalance)
                >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
            return false;
        }
        if (Math.abs(this.totalIncome - account.totalIncome) >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
            return false;
        }
        if (Math.abs(this.totalExpense - account.totalExpense) >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
            return false;
        }
        return true;
    }
}
