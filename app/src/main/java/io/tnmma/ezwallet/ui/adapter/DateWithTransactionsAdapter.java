package io.tnmma.ezwallet.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.ListAdapter;

import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.DateWithTransactions;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.databinding.ItemDateWithBalanceBinding;
import io.tnmma.ezwallet.databinding.ItemTransactionWithDetailsBinding;

import java.util.List;

public class DateWithTransactionsAdapter
        extends ListAdapter<DateWithTransactions, BaseViewHolder<? extends ViewDataBinding>> {

    private static final String TAG = DateWithTransactionsAdapter.class.getSimpleName();

    private final boolean isAccountAndCategoryClickable;

    private OnClickListener onClickListener;

    public DateWithTransactionsAdapter(boolean isAccountAndCategoryClickable) {
        super(DateWithTransactions.DIFF_CALLBACK);
        this.isAccountAndCategoryClickable = isAccountAndCategoryClickable;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @NonNull
    @Override
    public BaseViewHolder<? extends ViewDataBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DateWithTransactions.ViewType.DATE:
                return getDateViewHolder(parent);
            case DateWithTransactions.ViewType.TRANSACTION:
                return getTransactionViewHolder(parent);
        }
        return new BaseViewHolder<>(parent);
    }

    private DateWithBalanceViewHolder getDateViewHolder(ViewGroup parent) {
        ItemDateWithBalanceBinding dateBinding =
                ItemDateWithBalanceBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
        return new DateWithBalanceViewHolder(dateBinding);
    }

    private TransactionWithDetailsViewHolder getTransactionViewHolder(ViewGroup parent) {
        ItemTransactionWithDetailsBinding transactionBinding =
                ItemTransactionWithDetailsBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
        TransactionWithDetailsViewHolder viewHolder =
                new TransactionWithDetailsViewHolder(transactionBinding);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null) {
                            onClickListener.onTransactionClick(
                                    getItem(viewHolder.getBindingAdapterPosition())
                                            .getTransaction());
                        }
                    }
                });
        if (isAccountAndCategoryClickable) {
            listenForTransactionSrcAccountClick(viewHolder);
            listenForTransactionDstAccountClick(viewHolder);
            listenForTransactionCategoryClick(viewHolder);
        }
        return viewHolder;
    }

    private void listenForTransactionSrcAccountClick(TransactionWithDetailsViewHolder viewHolder) {
        viewHolder
                .getBinding()
                .itemTransactionWithDetailsSrcAccount
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onTransactionSrcAccountClick(viewHolder);
                            }
                        });
    }

    private void onTransactionSrcAccountClick(TransactionWithDetailsViewHolder viewHolder) {
        TransactionWithDetails transaction = getTransactionAt(viewHolder);
        if (transaction != null && transaction.getSrcAccount() != null && onClickListener != null) {
            onClickListener.onAccountClick(transaction.getSrcAccount());
        }
    }

    private void listenForTransactionDstAccountClick(TransactionWithDetailsViewHolder viewHolder) {
        viewHolder
                .getBinding()
                .itemTransactionWithDetailsDstAccount
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onTransactionDstAccountClick(viewHolder);
                            }
                        });
    }

    private void onTransactionDstAccountClick(TransactionWithDetailsViewHolder viewHolder) {
        TransactionWithDetails transaction = getTransactionAt(viewHolder);
        if (transaction != null && transaction.getDstAccount() != null && onClickListener != null) {
            onClickListener.onAccountClick(transaction.getDstAccount());
        }
    }

    private void listenForTransactionCategoryClick(TransactionWithDetailsViewHolder viewHolder) {
        viewHolder
                .getBinding()
                .itemTransactionWithDetailsCategory
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onTransactionCategoryClick(viewHolder);
                            }
                        });
    }

    private void onTransactionCategoryClick(TransactionWithDetailsViewHolder viewHolder) {
        TransactionWithDetails transaction = getTransactionAt(viewHolder);
        if (transaction != null && transaction.getCategory() != null && onClickListener != null) {
            onClickListener.onCategoryClick(transaction.getCategory());
        }
    }

    private TransactionWithDetails getTransactionAt(TransactionWithDetailsViewHolder viewHolder) {
        DateWithTransactions item = getItem(viewHolder.getBindingAdapterPosition());
        if (item.getViewType() == DateWithTransactions.ViewType.TRANSACTION) {
            return item.getTransaction();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        DateWithTransactions item = getItem(position);
        if (item.getViewType() == DateWithTransactions.ViewType.DATE) {
            ((DateWithBalanceViewHolder) holder).getBinding().setViewModel(item.getDate());
        } else if (item.getViewType() == DateWithTransactions.ViewType.TRANSACTION) {
            ((TransactionWithDetailsViewHolder) holder)
                    .getBinding()
                    .setViewModel(item.getTransaction());
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<? extends ViewDataBinding> holder,
            int position,
            @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public void setOnTransactionClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    static class DateWithBalanceViewHolder extends BaseViewHolder<ItemDateWithBalanceBinding> {

        public DateWithBalanceViewHolder(ItemDateWithBalanceBinding binding) {
            super(binding);
        }
    }

    static class TransactionWithDetailsViewHolder
            extends BaseViewHolder<ItemTransactionWithDetailsBinding> {

        public TransactionWithDetailsViewHolder(ItemTransactionWithDetailsBinding binding) {
            super(binding);
        }
    }

    public interface OnClickListener {

        void onTransactionClick(TransactionWithDetails transaction);

        default void onAccountClick(Account account) {}

        default void onCategoryClick(Category category) {}
    }
}
