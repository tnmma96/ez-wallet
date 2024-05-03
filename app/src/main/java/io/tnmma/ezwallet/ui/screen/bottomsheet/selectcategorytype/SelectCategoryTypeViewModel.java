package io.tnmma.ezwallet.ui.screen.bottomsheet.selectcategorytype;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ConstantArrays;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.CategoryTypeItem;

import java.util.List;

public class SelectCategoryTypeViewModel extends ViewModel {

    private static final String TAG = SelectCategoryTypeViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final List<CategoryTypeItem> categoryTypeList;

    public static ViewModelProvider.Factory getFactory(CategoryType categoryType) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SelectCategoryTypeViewModel(categoryType);
            }
        };
    }

    public SelectCategoryTypeViewModel(CategoryType categoryType) {
        bottomAction = new BottomAction(BottomActionType.SELECT);
        categoryTypeList = ConstantArrays.getCategoryTypeList();
        setupCategoryTypeList(categoryType);
    }

    private void setupCategoryTypeList(CategoryType categoryType) {
        for (CategoryTypeItem item : categoryTypeList) {
            item.setSelected(item.getCategoryType() == categoryType);
        }
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public List<CategoryTypeItem> getCategoryTypeList() {
        return categoryTypeList;
    }
}
