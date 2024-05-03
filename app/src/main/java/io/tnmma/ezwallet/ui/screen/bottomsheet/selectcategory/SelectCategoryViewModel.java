package io.tnmma.ezwallet.ui.screen.bottomsheet.selectcategory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.CategorySelection;
import io.tnmma.ezwallet.data.repository.CategoryRepository;

import io.reactivex.rxjava3.functions.Consumer;

import java.util.List;
import java.util.Objects;

public class SelectCategoryViewModel extends ViewModel {

    private static final String TAG = SelectCategoryViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final CategoryType categoryType;

    private final CategoryRepository categoryRepository;

    private final LiveData<List<CategorySelection>> categoryListLiveData;

    private final MutableLiveData<Category> currentCategoryLiveData;

    public static ViewModelProvider.Factory getFactory(Category originalCategory) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SelectCategoryViewModel(originalCategory);
            }
        };
    }

    public SelectCategoryViewModel(Category originalCategory) {
        bottomAction = new BottomAction(BottomActionType.SELECT);
        this.categoryType = originalCategory.getCategoryType();
        categoryRepository = new CategoryRepository();
        currentCategoryLiveData =
                new MutableLiveData<>(originalCategory.getId() != null ? originalCategory : null);
        categoryListLiveData = getCategoryListFromRepository();
    }

    public LiveData<List<CategorySelection>> getCategoryListFromRepository() {
        return LiveDataReactiveStreams.fromPublisher(
                categoryRepository
                        .getCategorySelectionsByCategoryType(categoryType)
                        .doOnNext(
                                new Consumer<List<CategorySelection>>() {
                                    @Override
                                    public void accept(List<CategorySelection> categories)
                                            throws Throwable {
                                        setupCategoryList(categories);
                                    }
                                }));
    }

    private void setupCategoryList(List<CategorySelection> categoryList) {
        if (currentCategoryLiveData.getValue() == null) {
            return;
        }
        for (CategorySelection category : categoryList) {
            category.postSelected(
                    Objects.equals(category.getId(), currentCategoryLiveData.getValue().getId()));
        }
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public LiveData<List<CategorySelection>> getCategoryListLiveData() {
        return categoryListLiveData;
    }

    public Category getCurrentCategory() {
        return currentCategoryLiveData.getValue();
    }

    public void setCurrentCategory(Category category) {
        currentCategoryLiveData.setValue(category);
    }
}
