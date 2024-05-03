package io.tnmma.ezwallet.data.pojo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Ignore;

import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.util.ColorUtil;

import java.util.Objects;

public class AccountSelection extends Account {

    private static final String TAG = AccountSelection.class.getSimpleName();

    public static final DiffUtil.ItemCallback<AccountSelection> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull AccountSelection oldItem, @NonNull AccountSelection newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull AccountSelection oldItem, @NonNull AccountSelection newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(
                        @NonNull AccountSelection oldItem, @NonNull AccountSelection newItem) {
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

    @Ignore private final transient MutableLiveData<Boolean> selectedLiveData;

    public AccountSelection() {
        selectedLiveData = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getSelectedLiveData() {
        return selectedLiveData;
    }

    public boolean isSelected() {
        return Boolean.TRUE.equals(selectedLiveData.getValue());
    }

    public void setSelected(boolean selected) {
        selectedLiveData.setValue(selected);
    }

    public void postSelected(boolean selected) {
        selectedLiveData.postValue(selected);
    }

    public int getForegroundColor() {
        return ColorUtil.getItemForegroundColor(getColor());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
