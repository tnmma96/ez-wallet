package io.tnmma.ezwallet.data.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.util.ColorUtil;

public class TransactionTypeItem {

    private static final String TAG = TransactionTypeItem.class.getSimpleName();

    public static final DiffUtil.ItemCallback<TransactionTypeItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull TransactionTypeItem oldItem,
                        @NonNull TransactionTypeItem newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull TransactionTypeItem oldItem,
                        @NonNull TransactionTypeItem newItem) {
                    return false;
                }
            };

    private final TransactionType transactionType;

    private final MutableLiveData<Boolean> selectedLiveData;

    public TransactionTypeItem(TransactionType transactionType) {
        this.transactionType = transactionType;
        selectedLiveData = new MutableLiveData<>(false);
    }

    public TransactionType getTransactionType() {
        return transactionType;
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

    public int getIconResId() {
        if (transactionType == null) {
            return 0;
        }
        switch (transactionType) {
            case INCOME:
                return R.drawable.icon_income;
            case EXPENSE:
                return R.drawable.icon_expense;
            case TRANSFER:
                return R.drawable.icon_transfer;
        }
        return 0;
    }

    public int getLabelResId() {
        if (transactionType == null) {
            return 0;
        }
        switch (transactionType) {
            case INCOME:
                return R.string.label_transaction_type_income;
            case EXPENSE:
                return R.string.label_transaction_type_expense;
            case TRANSFER:
                return R.string.label_transaction_type_transfer;
        }
        return 0;
    }

    public int getColor() {
        if (transactionType == null) {
            return 0;
        }
        int colorResId = 0;
        switch (transactionType) {
            case INCOME:
                colorResId = R.color.ivy_green;
                break;
            case EXPENSE:
                colorResId = R.color.ivy_red;
                break;
            case TRANSFER:
                colorResId = R.color.ivy_purple;
                break;
        }
        return ColorUtil.getColorFromResId(colorResId);
    }
}
