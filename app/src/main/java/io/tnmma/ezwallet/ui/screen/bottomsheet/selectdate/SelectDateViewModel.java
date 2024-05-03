package io.tnmma.ezwallet.ui.screen.bottomsheet.selectdate;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.ConstantArrays;
import io.tnmma.ezwallet.base.constant.DateType;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.DateRange;
import io.tnmma.ezwallet.data.model.DateTypeItem;

import java.time.LocalDate;
import java.util.List;

public class SelectDateViewModel extends ViewModel {

    private static final String TAG = SelectDateViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final List<DateTypeItem> dateTypeList;

    private final DateRange dateRange;

    public static ViewModelProvider.Factory getFactory(
            DateType dateType, LocalDate startDate, LocalDate endDate) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SelectDateViewModel(dateType, startDate, endDate);
            }
        };
    }

    public SelectDateViewModel(DateType dateType, LocalDate startDate, LocalDate endDate) {
        bottomAction = new BottomAction(BottomActionType.SELECT);
        dateTypeList = ConstantArrays.getDateTypeList();
        setupDateTypeList(dateType);
        dateRange = new DateRange(dateType, startDate, endDate);
    }

    private void setupDateTypeList(DateType dateType) {
        for (DateTypeItem date : dateTypeList) {
            date.setSelected(date.getDateType() == dateType);
        }
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public List<DateTypeItem> getDateTypeList() {
        return dateTypeList;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateType(DateType dateType) {
        if (dateRange.getDateType() != dateType) {
            dateRange.setDateType(dateType);
        }
    }

    public void setStartDate(LocalDate startDate) {
        dateRange.setStartDate(startDate);
    }

    public void setEndDate(LocalDate endDate) {
        dateRange.setEndDate(endDate);
    }
}
