package io.tnmma.ezwallet.data.pojo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.data.db.entity.AccountEntity;
import io.tnmma.ezwallet.util.ColorUtil;

import java.util.Objects;

public class Account extends AccountEntity {

    private static final String TAG = Account.class.getSimpleName();

    public static final DiffUtil.ItemCallback<Account> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Account oldItem, @NonNull Account newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Account oldItem, @NonNull Account newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(@NonNull Account oldItem, @NonNull Account newItem) {
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
                    if (oldItem.getForegroundColor() != newItem.getForegroundColor()) {
                        diffBundle.putInt(
                                ConstantKeys.Account.FOREGROUND_COLOR,
                                newItem.getForegroundColor());
                    }
                    return diffBundle.isEmpty() ? null : diffBundle;
                }
            };

    public int getForegroundColor() {
        return ColorUtil.getItemForegroundColor(getColor());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        final Account account = (Account) obj;
        if (!Objects.equals(this.getId(), account.getId())) {
            return false;
        }
        if (!Objects.equals(this.getName(), account.getName())) {
            return false;
        }
        if (this.getColor() != account.getColor()) {
            return false;
        }
        if (!Objects.equals(this.getIcon(), account.getIcon())) {
            return false;
        }
        if (!Objects.equals(this.getCreatedTime(), account.getCreatedTime())) {
            return false;
        }
        return true;
    }
}
