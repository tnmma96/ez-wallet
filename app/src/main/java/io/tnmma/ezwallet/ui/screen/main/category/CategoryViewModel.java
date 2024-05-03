package io.tnmma.ezwallet.ui.screen.main.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.data.model.SortOption;

public class CategoryViewModel extends ViewModel {

    private static final String TAG = CategoryViewModel.class.getSimpleName();

    private final SortOption sortOption;

    private final MutableLiveData<CategoryType> currentCategory;

    public CategoryViewModel() {
        sortOption = new SortOption(SortType.CREATED_TIME, SortOrder.DESCENDING);
        currentCategory = new MutableLiveData<>();
    }

    public SortOption getSortOption() {
        return sortOption;
    }

    public LiveData<CategoryType> getCurrentCategory() {
        return currentCategory;
    }

    public void setSortOption(SortType sortType, SortOrder sortOrder) {
        if (sortOption.getSortType() == sortType && sortOption.getSortOrder() == sortOrder) {
            return;
        }
        sortOption.setSortType(sortType);
        sortOption.setSortOrder(sortOrder);
    }

    public void onSelectCategory(CategoryType categoryType) {
        if (currentCategory.getValue() == categoryType) {
            return;
        }
        currentCategory.setValue(categoryType);
    }
}
