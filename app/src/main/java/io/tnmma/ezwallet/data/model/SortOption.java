package io.tnmma.ezwallet.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;

import kotlin.jvm.functions.Function1;

public class SortOption {

    private static final String TAG = SortOption.class.getSimpleName();

    private final MutableLiveData<SortType> sortTypeLiveData;

    private final MutableLiveData<SortOrder> sortOrderLiveData;

    private final LiveData<Integer> sortTypeIconLiveData;

    private final LiveData<Integer> sortOrderIconLiveData;

    public SortOption(SortType sortType, SortOrder sortOrder) {
        sortTypeLiveData = new MutableLiveData<>(sortType);
        sortOrderLiveData = new MutableLiveData<>(sortOrder);
        sortTypeIconLiveData = setupSortTypeIconLiveData();
        sortOrderIconLiveData = setupSortOrderIconLiveData();
    }

    private LiveData<Integer> setupSortTypeIconLiveData() {
        return Transformations.map(
                sortTypeLiveData,
                new Function1<SortType, Integer>() {
                    @Override
                    public Integer invoke(SortType sortType) {
                        return getSortTypeIconResId(sortType);
                    }
                });
    }

    private LiveData<Integer> setupSortOrderIconLiveData() {
        return Transformations.map(
                sortOrderLiveData,
                new Function1<SortOrder, Integer>() {
                    @Override
                    public Integer invoke(SortOrder sortOrder) {
                        return getSortOrderIconResId(sortOrder);
                    }
                });
    }

    public void setSortType(SortType sortType) {
        sortTypeLiveData.setValue(sortType);
    }

    public void setSortOrder(SortOrder sortOrder) {
        sortOrderLiveData.setValue(sortOrder);
    }

    public SortType getSortType() {
        return sortTypeLiveData.getValue();
    }

    public SortOrder getSortOrder() {
        return sortOrderLiveData.getValue();
    }

    public LiveData<Integer> getSortTypeIconLiveData() {
        return sortTypeIconLiveData;
    }

    public LiveData<Integer> getSortOrderIconLiveData() {
        return sortOrderIconLiveData;
    }

    private int getSortTypeIconResId(SortType sortType) {
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

    private int getSortOrderIconResId(SortOrder sortOrder) {
        if (sortOrder == null) {
            return 0;
        }
        return sortOrder == SortOrder.ASCENDING
                ? R.drawable.icon_ascending
                : R.drawable.icon_descending;
    }
}
