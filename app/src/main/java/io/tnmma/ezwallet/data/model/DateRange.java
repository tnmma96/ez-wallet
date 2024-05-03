package io.tnmma.ezwallet.data.model;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.DateType;
import io.tnmma.ezwallet.util.DateTimeUtil;

import kotlin.jvm.functions.Function1;

import java.time.LocalDate;

public class DateRange {

    private static final String TAG = DateRange.class.getSimpleName();

    private final MutableLiveData<DateType> dateTypeLiveData;

    private final MediatorLiveData<LocalDate> startDateLiveData;

    private final MediatorLiveData<LocalDate> endDateLiveData;

    private final LiveData<String> formattedStartDateLiveData;

    private final LiveData<String> formattedEndDateLiveData;

    private final MediatorLiveData<Boolean> showButtonRemoveStartDateLiveData;

    private final MediatorLiveData<Boolean> showButtonRemoveEndDateLiveData;

    public DateRange(DateType dateType, LocalDate startDate, LocalDate endDate) {
        dateTypeLiveData = new MutableLiveData<>(dateType);
        startDateLiveData = new MediatorLiveData<>(startDate);
        endDateLiveData = new MediatorLiveData<>(endDate);

        formattedStartDateLiveData = setupFormattedStartDateLiveData();
        formattedEndDateLiveData = setupFormattedEndDateLiveData();

        showButtonRemoveStartDateLiveData =
                new MediatorLiveData<>(showButtonRemoveStartDate(getDateType(), getStartDate()));
        setupShowButtonRemoveStartDateObserver();
        showButtonRemoveEndDateLiveData =
                new MediatorLiveData<>(showButtonRemoveEndDate(getDateType(), getEndDate()));
        setupShowButtonRemoveEndDateObserver();
    }

    private LiveData<String> setupFormattedStartDateLiveData() {
        return Transformations.map(
                startDateLiveData,
                new Function1<LocalDate, String>() {
                    @Override
                    public String invoke(LocalDate localDate) {
                        return DateTimeUtil.getFormattedDate(localDate, true);
                    }
                });
    }

    private LiveData<String> setupFormattedEndDateLiveData() {
        return Transformations.map(
                endDateLiveData,
                new Function1<LocalDate, String>() {
                    @Override
                    public String invoke(LocalDate localDate) {
                        return DateTimeUtil.getFormattedDate(localDate, true);
                    }
                });
    }

    private void setupShowButtonRemoveStartDateObserver() {
        showButtonRemoveStartDateLiveData.addSource(
                dateTypeLiveData,
                new Observer<DateType>() {
                    @Override
                    public void onChanged(DateType dateType) {
                        showButtonRemoveStartDateLiveData.setValue(
                                showButtonRemoveStartDate(dateType, getStartDate()));
                    }
                });
        showButtonRemoveStartDateLiveData.addSource(
                startDateLiveData,
                new Observer<LocalDate>() {
                    @Override
                    public void onChanged(LocalDate startDate) {
                        showButtonRemoveStartDateLiveData.setValue(
                                showButtonRemoveStartDate(getDateType(), startDate));
                    }
                });
    }

    private void setupShowButtonRemoveEndDateObserver() {
        showButtonRemoveEndDateLiveData.addSource(
                dateTypeLiveData,
                new Observer<DateType>() {
                    @Override
                    public void onChanged(DateType dateType) {
                        showButtonRemoveEndDateLiveData.setValue(
                                showButtonRemoveEndDate(dateType, getEndDate()));
                    }
                });
        showButtonRemoveEndDateLiveData.addSource(
                endDateLiveData,
                new Observer<LocalDate>() {
                    @Override
                    public void onChanged(LocalDate endDate) {
                        showButtonRemoveEndDateLiveData.setValue(
                                showButtonRemoveEndDate(getDateType(), endDate));
                    }
                });
    }

    public LiveData<DateType> getDateTypeLiveData() {
        return dateTypeLiveData;
    }

    public LiveData<LocalDate> getStartDateLiveData() {
        return startDateLiveData;
    }

    public LiveData<LocalDate> getEndDateLiveData() {
        return endDateLiveData;
    }

    public LiveData<String> getFormattedStartDateLiveData() {
        return formattedStartDateLiveData;
    }

    public LiveData<String> getFormattedEndDateLiveData() {
        return formattedEndDateLiveData;
    }

    public DateType getDateType() {
        return dateTypeLiveData.getValue();
    }

    public LocalDate getStartDate() {
        return startDateLiveData.getValue();
    }

    public LocalDate getEndDate() {
        return endDateLiveData.getValue();
    }

    public LiveData<Boolean> getShowButtonRemoveStartDateLiveData() {
        return showButtonRemoveStartDateLiveData;
    }

    public LiveData<Boolean> getShowButtonRemoveEndDateLiveData() {
        return showButtonRemoveEndDateLiveData;
    }

    public void setDateType(DateType dateType) {
        dateTypeLiveData.setValue(dateType);
        setDateByDateType(dateType);
    }

    public void setStartDate(LocalDate startDate) {
        startDateLiveData.setValue(startDate);
    }

    public void setEndDate(LocalDate endDate) {
        endDateLiveData.setValue(endDate);
    }

    public void removeStartDate() {
        startDateLiveData.setValue(null);
    }

    public void removeEndDate() {
        endDateLiveData.setValue(null);
    }

    private void setDateByDateType(DateType dateType) {
        if (dateType == null) {
            setStartDate(null);
            setEndDate(null);
            return;
        }

        Pair<LocalDate, LocalDate> newDateRange;
        LocalDate newStartDate = null;
        LocalDate newEndDate = null;
        switch (dateType) {
            case TODAY:
            case THIS_WEEK:
            case THIS_MONTH:
            case THIS_YEAR:
                newDateRange = DateTimeUtil.getRange(dateType);
                newStartDate = newDateRange.first;
                newEndDate = newDateRange.second;
                break;
        }
        setStartDate(newStartDate);
        setEndDate(newEndDate);
    }

    public boolean showButtonRemoveStartDate(DateType dateType, LocalDate startDate) {
        return (dateType == DateType.CUSTOM_RANGE && startDate != null);
    }

    public boolean showButtonRemoveEndDate(DateType dateType, LocalDate endDate) {
        return (dateType == DateType.CUSTOM_RANGE && endDate != null);
    }
}
