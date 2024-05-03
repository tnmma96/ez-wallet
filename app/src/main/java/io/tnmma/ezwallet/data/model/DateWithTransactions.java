package io.tnmma.ezwallet.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;

public class DateWithTransactions {

    private static final String TAG = DateWithTransactions.class.getSimpleName();

    public static final DiffUtil.ItemCallback<DateWithTransactions> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull DateWithTransactions oldItem,
                        @NonNull DateWithTransactions newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull DateWithTransactions oldItem,
                        @NonNull DateWithTransactions newItem) {
                    return false;
                }

                @Nullable
                @Override
                public Object getChangePayload(
                        @NonNull DateWithTransactions oldItem,
                        @NonNull DateWithTransactions newItem) {
                    return super.getChangePayload(oldItem, newItem);
                }
            };

    private final int viewType;

    private DateWithBalance date;

    private TransactionWithDetails transaction;

    public DateWithTransactions(DateWithBalance date) {
        this.date = date;
        viewType = ViewType.DATE;
    }

    public DateWithTransactions(TransactionWithDetails transaction) {
        this.transaction = transaction;
        viewType = ViewType.TRANSACTION;
    }

    public int getViewType() {
        return viewType;
    }

    public DateWithBalance getDate() {
        return date;
    }

    public TransactionWithDetails getTransaction() {
        return transaction;
    }

    public interface ViewType {

        int DATE = 1;

        int TRANSACTION = 2;
    }
}
