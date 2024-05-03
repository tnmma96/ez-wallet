package io.tnmma.ezwallet.data.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.DateType;

public class DateTypeItem {

    public static final DiffUtil.ItemCallback<DateTypeItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull DateTypeItem oldItem, @NonNull DateTypeItem newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull DateTypeItem oldItem, @NonNull DateTypeItem newItem) {
                    return false;
                }
            };

    private final DateType dateType;

    private final MutableLiveData<Boolean> selectedLiveData;

    public DateTypeItem(DateType dateType) {
        this.dateType = dateType;
        selectedLiveData = new MutableLiveData<>(false);
    }

    public DateType getDateType() {
        return dateType;
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

    public int getLabelResId() {
        if (dateType == null) {
            return 0;
        }
        switch (dateType) {
            case TODAY:
                return R.string.label_date_type_today;
            case THIS_WEEK:
                return R.string.label_date_type_this_week;
            case THIS_MONTH:
                return R.string.label_date_type_this_month;
            case THIS_YEAR:
                return R.string.label_date_type_this_year;
            case ALL_TIME:
                return R.string.label_date_type_all_time;
            case CUSTOM_RANGE:
                return R.string.label_date_type_custom_range;
        }
        return 0;
    }
}
