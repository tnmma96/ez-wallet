package io.tnmma.ezwallet.ui.adapter;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.databinding.ItemAccountInfoBinding;

import java.util.List;

public class AccountInfoAdapter extends BaseAdapter<Account, ItemAccountInfoBinding> {

    private static final String TAG = AccountInfoAdapter.class.getSimpleName();

    public AccountInfoAdapter() {
        super(Account.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_account_info;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAccountInfoBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemAccountInfoBinding> holder,
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
                    holder.getBinding().itemAccountInfoName.setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Account.COLOR:
                    int color = diffBundle.getInt(key);
                    holder.getBinding()
                            .itemAccountInfoBackgroundColor
                            .setBackgroundTintList(ColorStateList.valueOf(color));
                    break;
                case ConstantKeys.Account.ICON:
                    break;
                case ConstantKeys.Account.FOREGROUND_COLOR:
                    int foregroundColor = diffBundle.getInt(key);
                    holder.getBinding().itemAccountInfoIcon.setColorFilter(foregroundColor);
                    holder.getBinding().itemAccountInfoName.setTextColor(foregroundColor);
                    break;
            }
        }
    }
}
