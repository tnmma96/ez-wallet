package io.tnmma.ezwallet.ui.screen.main.category.categorybytype;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.data.model.SortOption;
import io.tnmma.ezwallet.data.pojo.CategoryWithDetails;
import io.tnmma.ezwallet.data.repository.CategoryRepository;

import io.reactivex.rxjava3.functions.Consumer;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryByTypeViewModel extends ViewModel {

    private static final String TAG = CategoryByTypeViewModel.class.getSimpleName();

    private final SortOption sortOption;

    private final CategoryType categoryType;

    private final CategoryRepository categoryRepository;

    private final MediatorLiveData<List<CategoryWithDetails>> categoryListLiveData;

    public static ViewModelProvider.Factory getFactory(CategoryType categoryType) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryByTypeViewModel(categoryType);
            }
        };
    }

    public CategoryByTypeViewModel(CategoryType categoryType) {
        sortOption = new SortOption(SortType.CREATED_TIME, SortOrder.DESCENDING);
        this.categoryType = categoryType;
        categoryRepository = new CategoryRepository();
        categoryListLiveData = new MediatorLiveData<>();
        setupCategoryList();
    }

    private void setupCategoryList() {
        categoryListLiveData.addSource(
                getCategoryListFromRepository(),
                new Observer<List<CategoryWithDetails>>() {
                    @Override
                    public void onChanged(List<CategoryWithDetails> categoryWithDetails) {
                        categoryListLiveData.setValue(categoryWithDetails);
                    }
                });
    }

    private LiveData<List<CategoryWithDetails>> getCategoryListFromRepository() {
        return LiveDataReactiveStreams.fromPublisher(
                categoryRepository
                        .getAllCategoriesWithDetailsByCategoryType(categoryType)
                        .doOnNext(
                                new Consumer<List<CategoryWithDetails>>() {
                                    @Override
                                    public void accept(
                                            List<CategoryWithDetails> categoryWithDetails) {
                                        sortOnNewCategoryList(categoryWithDetails);
                                    }
                                }));
    }

    private void sortOnNewCategoryList(List<CategoryWithDetails> categoryList) {
        if (sortOption.getSortType() == SortType.CREATED_TIME
                && sortOption.getSortOrder() == SortOrder.DESCENDING) {
            return;
        }
        categoryList.sort(
                CategoryWithDetails.getCategoryComparator(
                        sortOption.getSortType(), sortOption.getSortOrder()));
    }

    public SortOption getSortOption() {
        return sortOption;
    }

    public LiveData<List<CategoryWithDetails>> getCategoryListLiveData() {
        return categoryListLiveData;
    }

    public void setSortOption(SortType sortType, SortOrder sortOrder) {
        if (sortOption.getSortType() == sortType && sortOption.getSortOrder() == sortOrder) {
            return;
        }

        sortOption.setSortType(sortType);
        sortOption.setSortOrder(sortOrder);
        sortOnNewSortOption(sortType, sortOrder);
    }

    private void sortOnNewSortOption(SortType sortType, SortOrder sortOrder) {
        new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                List<CategoryWithDetails> newList =
                                        categoryListLiveData.getValue().stream()
                                                .collect(Collectors.toList());
                                newList.sort(
                                        CategoryWithDetails.getCategoryComparator(
                                                sortType, sortOrder));
                                categoryListLiveData.postValue(newList);
                            }
                        })
                .start();
    }
}
