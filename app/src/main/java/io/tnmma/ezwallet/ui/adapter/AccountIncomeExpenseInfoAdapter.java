package io.tnmma.ezwallet.ui.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.AccountIncomeExpenseInfo;
import io.tnmma.ezwallet.databinding.ItemAccountIncomeExpenseInfoBinding;

import java.util.List;

public class AccountIncomeExpenseInfoAdapter
        extends BaseAdapter<AccountIncomeExpenseInfo, ItemAccountIncomeExpenseInfoBinding> {

    private static final String TAG = AccountIncomeExpenseInfoAdapter.class.getSimpleName();

    private OnClickListener onClickListener;

    public AccountIncomeExpenseInfoAdapter() {
        super(AccountIncomeExpenseInfo.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_account_income_expense_info;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemAccountIncomeExpenseInfoBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemAccountIncomeExpenseInfoBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder
                .getBinding()
                .itemAccountIncomeExpenseInfoButtonAddIncome
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onClickListener != null) {
                                    onClickListener.onButtonAddClick(TransactionType.INCOME);
                                }
                            }
                        });
        viewHolder
                .getBinding()
                .itemAccountIncomeExpenseInfoButtonAddExpense
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onClickListener != null) {
                                    onClickListener.onButtonAddClick(TransactionType.EXPENSE);
                                }
                            }
                        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAccountIncomeExpenseInfoBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAccountIncomeExpenseInfoBinding> holder,
            int position,
            @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Bundle diffBundle = (Bundle) payloads.get(0);
        for (String key : diffBundle.keySet()) {
            switch (key) {
                case ConstantKeys.AccountIncomeExpenseInfo.INCOME_AMOUNT:
                    holder.getBinding()
                            .itemAccountIncomeExpenseInfoAmountIncome
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.AccountIncomeExpenseInfo.INCOME_COUNT:
                    holder.getBinding()
                            .itemAccountIncomeExpenseInfoCountIncome
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.AccountIncomeExpenseInfo.EXPENSE_AMOUNT:
                    holder.getBinding()
                            .itemAccountIncomeExpenseInfoAmountExpense
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.AccountIncomeExpenseInfo.EXPENSE_COUNT:
                    holder.getBinding()
                            .itemAccountIncomeExpenseInfoCountExpense
                            .setText(diffBundle.getString(key));
                    break;
            }
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {

        void onButtonAddClick(TransactionType transactionType);
    }
}
