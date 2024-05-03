package io.tnmma.ezwallet.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.DateType;

import kotlin.jvm.functions.Function1;

import java.time.LocalDate;

public class DateOption {

    private static final String TAG = DateOption.class.getSimpleName();

    private final MutableLiveData<DateType> dateTypeLiveData;

    private final LiveData<Integer> dateTypeLabelLiveData;

    private LocalDate startDate;

    private LocalDate endDate;

    public DateOption(DateType dateType, LocalDate startDate, LocalDate endDate) {
        dateTypeLiveData = new MutableLiveData<>(dateType);
        this.startDate = startDate;
        this.endDate = endDate;

        dateTypeLabelLiveData =
                Transformations.map(
                        dateTypeLiveData,
                        new Function1<DateType, Integer>() {
                            @Override
                            public Integer invoke(DateType dateType) {
                                return getLabelResId(dateType);
                            }
                        });
    }

    public DateType getDateType() {
        return dateTypeLiveData.getValue();
    }

    public LiveData<Integer> getDateTypeLabelLiveData() {
        return dateTypeLabelLiveData;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setDateType(DateType dateType) {
        dateTypeLiveData.setValue(dateType);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    private int getLabelResId(DateType dateType) {
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
