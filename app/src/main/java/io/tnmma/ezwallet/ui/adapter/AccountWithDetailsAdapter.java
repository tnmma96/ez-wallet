package io.tnmma.ezwallet.ui.adapter;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.pojo.AccountWithDetails;
import io.tnmma.ezwallet.databinding.ItemAccountWithDetailsBinding;

import java.util.List;

public class AccountWithDetailsAdapter
        extends BaseAdapter<AccountWithDetails, ItemAccountWithDetailsBinding> {

    private static final String TAG = AccountWithDetailsAdapter.class.getSimpleName();

    private OnClickListener onClickListener;

    public AccountWithDetailsAdapter() {
        super(AccountWithDetails.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_account_with_details;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemAccountWithDetailsBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemAccountWithDetailsBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null) {
                            onClickListener.onItemClick(
                                    getItem(viewHolder.getBindingAdapterPosition()));
                        }
                    }
                });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAccountWithDetailsBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAccountWithDetailsBinding> holder,
            int position,
            @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Bundle diffBundle = (Bundle) payloads.get(0);
        for (String key : diffBundle.keySet()) {
            switch (key) {
                case ConstantKeys.Account.NAME:
                    holder.getBinding()
                            .itemAccountWithDetailsName
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Account.COLOR:
                    int color = diffBundle.getInt(key);
                    holder.getBinding()
                            .itemAccountWithDetailsBackgroundColor
                            .setBackgroundTintList(ColorStateList.valueOf(color));
                    break;
                case ConstantKeys.Account.ICON:
                    break;
                case ConstantKeys.Account.CURRENT_BALANCE:
                    holder.getBinding()
                            .itemAccountWithDetailsCurrentBalance
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Account.TOTAL_INCOME:
                    holder.getBinding()
                            .itemAccountWithDetailsValueIncome
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Account.TOTAL_EXPENSE:
                    holder.getBinding()
                            .itemAccountWithDetailsValueExpense
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Account.FOREGROUND_COLOR:
                    int foregroundColor = diffBundle.getInt(key);
                    holder.getBinding().itemAccountWithDetailsIcon.setColorFilter(foregroundColor);
                    holder.getBinding().itemAccountWithDetailsName.setTextColor(foregroundColor);
                    holder.getBinding()
                            .itemAccountWithDetailsCurrentBalance
                            .setTextColor(foregroundColor);
                    break;
            }
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {

        void onItemClick(AccountWithDetails item);
    }
}
