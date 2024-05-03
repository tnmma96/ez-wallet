package io.tnmma.ezwallet.data.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.SortType;

public class SortTypeItem {

    private static final String TAG = SortTypeItem.class.getSimpleName();

    public static final DiffUtil.ItemCallback<SortTypeItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull SortTypeItem oldItem, @NonNull SortTypeItem newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull SortTypeItem oldItem, @NonNull SortTypeItem newItem) {
                    return false;
                }
            };

    private final SortType sortType;

    private final MutableLiveData<Boolean> selectedLiveData;

    public SortTypeItem(SortType sortType) {
        this.sortType = sortType;
        selectedLiveData = new MutableLiveData<>(false);
    }

    public SortType getSortType() {
        return sortType;
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
        if (sortType == null) {
            return 0;
        }
        switch (sortType) {
            case CREATED_TIME:
                return R.drawable.icon_created_time;
            case NAME:
                return R.drawable.icon_name;
            case BALANCE:
                return R.drawable.icon_balance;
            case AMOUNT:
                return R.drawable.icon_amount;
            case INCOME:
                return R.drawable.icon_income;
            case EXPENSE:
                return R.drawable.icon_expense;
        }
        return 0;
    }

    public int getLabelResId() {
        if (sortType == null) {
            return 0;
        }
        switch (sortType) {
            case CREATED_TIME:
                return R.string.label_sort_type_created_time;
            case NAME:
                return R.string.label_sort_type_name;
            case BALANCE:
                return R.string.label_sort_type_balance;
            case AMOUNT:
                return R.string.label_sort_type_amount;
            case INCOME:
                return R.string.label_sort_type_income;
            case EXPENSE:
                return R.string.label_sort_type_expense;
        }
        return 0;
    }
}
