package io.tnmma.ezwallet.ui.screen.bottomsheet.selectsortoption;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.ConstantArrays;
import io.tnmma.ezwallet.base.constant.SortObject;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.SortOrderItem;
import io.tnmma.ezwallet.data.model.SortTypeItem;

import java.util.List;

public class SelectSortOptionViewModel extends ViewModel {

    private static final String TAG = SelectSortOptionViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final List<SortTypeItem> sortTypeList;

    private final List<SortOrderItem> sortOrderList;

    public static ViewModelProvider.Factory getFactory(
            SortObject sortObject, SortType sortType, SortOrder sortOrder) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SelectSortOptionViewModel(sortObject, sortType, sortOrder);
            }
        };
    }

    public SelectSortOptionViewModel(
            SortObject sortObject, SortType sortType, SortOrder sortOrder) {
        bottomAction = new BottomAction(BottomActionType.SET);
        sortTypeList = ConstantArrays.getSortTypeList(sortObject);
        setupSortTypeList(sortType);
        sortOrderList = ConstantArrays.getSortOrderList();
        setupSortOrderList(sortOrder);
    }

    private void setupSortTypeList(SortType sortType) {
        for (SortTypeItem item : sortTypeList) {
            item.setSelected(item.getSortType() == sortType);
        }
    }

    private void setupSortOrderList(SortOrder sortOrder) {
        for (SortOrderItem item : sortOrderList) {
            item.setSelected(item.getSortOrder() == sortOrder);
        }
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public List<SortTypeItem> getSortTypeList() {
        return sortTypeList;
    }

    public List<SortOrderItem> getSortOrderList() {
        return sortOrderList;
    }
}
