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

public class CategoryWithDetails extends Category {

    private static final String TAG = CategoryWithDetails.class.getSimpleName();

    private static final Comparator<CategoryWithDetails> DEFAULT_COMPARATOR =
            Comparator.comparing(CategoryWithDetails::getCreatedTime).reversed();

    public static final DiffUtil.ItemCallback<CategoryWithDetails> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull CategoryWithDetails oldItem,
                        @NonNull CategoryWithDetails newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull CategoryWithDetails oldItem,
                        @NonNull CategoryWithDetails newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(
                        @NonNull CategoryWithDetails oldItem,
                        @NonNull CategoryWithDetails newItem) {
                    Bundle diffBundle = new Bundle();
                    if (!Objects.equals(oldItem.getName(), newItem.getName())) {
                        diffBundle.putString(ConstantKeys.Category.NAME, newItem.getName());
                    }
                    if (oldItem.getColor() != newItem.getColor()) {
                        diffBundle.putInt(ConstantKeys.Category.COLOR, newItem.getColor());
                    }
                    if (!Objects.equals(oldItem.getIcon(), newItem.getIcon())) {
                        diffBundle.putString(ConstantKeys.Category.ICON, newItem.getIcon());
                    }
                    if (Math.abs(oldItem.totalAmount - newItem.totalAmount)
                            >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
                        diffBundle.putString(
                                ConstantKeys.Category.TOTAL_AMOUNT,
                                newItem.getFormattedTotalAmount());
                    }
                    if (oldItem.getForegroundColor() != newItem.getForegroundColor()) {
                        diffBundle.putInt(
                                ConstantKeys.Category.FOREGROUND_COLOR,
                                newItem.getForegroundColor());
                    }
                    return diffBundle.isEmpty() ? null : diffBundle;
                }
            };

    @Relation(parentColumn = "id", entityColumn = "category_id", entity = TransactionEntity.class)
    private List<Transaction> transactions;

    @Ignore private Double totalAmount = 0.0;

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getFormattedTotalAmount() {
        return AmountUtil.formatWithSuffixFromMillion(totalAmount);
    }

    private void resetAmount() {
        totalAmount = 0.0;
    }

    public void computeAmount() {
        resetAmount();
        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount();
        }
    }

    public static Comparator<CategoryWithDetails> getCategoryComparator(
            SortType sortType, SortOrder sortOrder) {
        if (sortType == null || sortOrder == null) {
            return Comparator.nullsLast(DEFAULT_COMPARATOR);
        }
        Comparator<CategoryWithDetails> comparator;
        switch (sortType) {
            case CREATED_TIME:
                return sortOrder == SortOrder.DESCENDING
                        ? Comparator.nullsLast(DEFAULT_COMPARATOR)
                        : Comparator.nullsLast(DEFAULT_COMPARATOR.reversed());
            case NAME:
                comparator = Comparator.comparing(CategoryWithDetails::getName);
                break;
            case AMOUNT:
                comparator = Comparator.comparing(CategoryWithDetails::getTotalAmount);
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
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        final CategoryWithDetails category = (CategoryWithDetails) obj;
        if (!Objects.equals(this.getId(), category.getId())) {
            return false;
        }
        if (!Objects.equals(this.getName(), category.getName())) {
            return false;
        }
        if (this.getCategoryType() != category.getCategoryType()) {
            return false;
        }
        if (this.getColor() != category.getColor()) {
            return false;
        }
        if (!Objects.equals(this.getIcon(), category.getIcon())) {
            return false;
        }
        if (!Objects.equals(this.getCreatedTime(), category.getCreatedTime())) {
            return false;
        }
        if (Math.abs(this.totalAmount - category.totalAmount) >= AmountUtil.SMALLEST_AMOUNT_DIFF) {
            return false;
        }
        return true;
    }
}
