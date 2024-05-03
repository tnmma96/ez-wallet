package io.tnmma.ezwallet.data.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.SortOrder;

public class SortOrderItem {

    private static final String TAG = SortOrderItem.class.getSimpleName();

    public static final DiffUtil.ItemCallback<SortOrderItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull SortOrderItem oldItem, @NonNull SortOrderItem newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull SortOrderItem oldItem, @NonNull SortOrderItem newItem) {
                    return false;
                }
            };

    private final SortOrder sortOrder;

    private final MutableLiveData<Boolean> selectedLiveData;

    public SortOrderItem(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        selectedLiveData = new MutableLiveData<>(false);
    }

    public SortOrder getSortOrder() {
        return sortOrder;
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
        if (sortOrder == null) {
            return 0;
        }
        switch (sortOrder) {
            case ASCENDING:
                return R.drawable.icon_ascending;
            case DESCENDING:
                return R.drawable.icon_descending;
        }
        return 0;
    }
}
