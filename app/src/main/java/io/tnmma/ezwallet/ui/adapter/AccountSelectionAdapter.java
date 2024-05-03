package io.tnmma.ezwallet.ui.adapter;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.AccountSelection;
import io.tnmma.ezwallet.databinding.ItemAccountSelectionBinding;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AccountSelectionAdapter
        extends BaseAdapter<AccountSelection, ItemAccountSelectionBinding> {

    private static final String TAG = AccountSelectionAdapter.class.getSimpleName();

    private final MutableLiveData<Account> selectedAccountLiveData;

    public AccountSelectionAdapter() {
        super(AccountSelection.DIFF_CALLBACK);
        selectedAccountLiveData = new MutableLiveData<>();
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_account_selection;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemAccountSelectionBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemAccountSelectionBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AccountSelection item = getItem(viewHolder.getBindingAdapterPosition());
                        setSelectedItem(item.getId());
                        selectedAccountLiveData.setValue(item.isSelected() ? item : null);
                    }
                });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAccountSelectionBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAccountSelectionBinding> holder,
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
                    holder.getBinding().itemAccountSelectionName.setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Account.COLOR:
                    int color = diffBundle.getInt(key);
                    holder.getBinding()
                            .itemAccountSelectionBackgroundColor
                            .setBackgroundTintList(ColorStateList.valueOf(color));
                    break;
                case ConstantKeys.Account.ICON:
                    break;
                case ConstantKeys.Account.FOREGROUND_COLOR:
                    int foregroundColor = diffBundle.getInt(key);
                    holder.getBinding().itemAccountSelectionName.setTextColor(foregroundColor);
                    holder.getBinding().itemAccountSelectionIcon.setColorFilter(foregroundColor);
                    break;
            }
        }
    }

    public LiveData<Account> getSelectedAccountLiveData() {
        return selectedAccountLiveData;
    }

    public AccountSelection getSelectedItem() {
        for (AccountSelection item : getCurrentList()) {
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    public int getSelectedPosition() {
        for (int i = 0; i < getItemCount(); ++i) {
            if (getItem(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void setSelectedItem(UUID accountId) {
        for (int i = 0; i < getItemCount(); ++i) {
            AccountSelection item = getItem(i);
            if (item.isSelected() && !Objects.equals(item.getId(), accountId)) {
                item.setSelected(false);
                notifyItemChanged(i);
            }
            if (!item.isSelected() && Objects.equals(item.getId(), accountId)) {
                item.setSelected(true);
                notifyItemChanged(i);
            }
        }
    }
}
