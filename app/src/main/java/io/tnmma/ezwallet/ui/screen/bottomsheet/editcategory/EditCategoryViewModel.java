package io.tnmma.ezwallet.ui.screen.bottomsheet.editcategory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ConstantArrays;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.event.SingleLiveEvent;
import io.tnmma.ezwallet.data.db.entity.CategoryEntity;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.CategoryTypeItem;
import io.tnmma.ezwallet.data.model.ColorItem;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.repository.CategoryRepository;
import io.tnmma.ezwallet.util.ColorUtil;
import io.tnmma.ezwallet.util.DateTimeUtil;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

import kotlin.jvm.functions.Function1;

import java.util.List;
import java.util.UUID;

public class EditCategoryViewModel extends ViewModel {

    private static final String TAG = EditCategoryViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final Category originalCategory;

    private final CategoryRepository categoryRepository;

    private final List<ColorItem> colorList;

    private List<CategoryTypeItem> categoryTypeList;

    private final MutableLiveData<Integer> selectedBackgroundColorLiveData;

    private final LiveData<Integer> selectedForegroundColorLiveData;

    private final CategoryEntity newCategory;

    private final SingleLiveEvent<Integer> onSaveEvent;

    public static ViewModelProvider.Factory getFactory(Category category) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new EditCategoryViewModel(category);
            }
        };
    }

    public EditCategoryViewModel(Category category) {
        bottomAction = new BottomAction(BottomActionType.SAVE);
        originalCategory = category;
        categoryRepository = new CategoryRepository();

        colorList = ConstantArrays.getColorList();
        setupColorList();
        selectedBackgroundColorLiveData = new MutableLiveData<>(getOriginalColor());
        selectedForegroundColorLiveData = setupSelectedForegroundColorLiveData();

        if (showCategoryTypeList()) {
            categoryTypeList = ConstantArrays.getCategoryTypeList();
            setupCategoryTypeList();
        }

        newCategory = new CategoryEntity();
        onSaveEvent = new SingleLiveEvent<>();
    }

    private int getOriginalColor() {
        return originalCategory.getId() != null
                ? originalCategory.getColor()
                : colorList.get(0).getColor();
    }

    private void setupColorList() {
        int originalColor = getOriginalColor();
        for (ColorItem item : colorList) {
            item.setSelected(item.getColor() == originalColor);
        }
    }

    private void setupCategoryTypeList() {
        CategoryType originalCategoryType;
        if (originalCategory.getCategoryType() != null) {
            originalCategoryType = originalCategory.getCategoryType();
        } else {
            originalCategoryType = CategoryType.INCOME;
        }
        for (CategoryTypeItem item : categoryTypeList) {
            item.setSelected(item.getCategoryType() == originalCategoryType);
        }
    }

    private LiveData<Integer> setupSelectedForegroundColorLiveData() {
        return Transformations.map(
                selectedBackgroundColorLiveData,
                new Function1<Integer, Integer>() {
                    @Override
                    public Integer invoke(Integer integer) {
                        return ColorUtil.getItemForegroundColor(integer);
                    }
                });
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public int getTitleResId() {
        return originalCategory.getId() != null
                ? R.string.title_edit_category
                : R.string.title_create_category;
    }

    public String getOriginalCategoryName() {
        return originalCategory.getName();
    }

    public List<ColorItem> getColorList() {
        return colorList;
    }

    public boolean showCategoryTypeList() {
        return originalCategory.getId() == null;
    }

    public List<CategoryTypeItem> getCategoryTypeList() {
        return categoryTypeList;
    }

    public LiveData<Integer> getSelectedBackgroundColorLiveData() {
        return selectedBackgroundColorLiveData;
    }

    public LiveData<Integer> getSelectedForegroundColorLiveData() {
        return selectedForegroundColorLiveData;
    }

    public LiveData<Integer> getOnSaveEvent() {
        return onSaveEvent;
    }

    public void setBackgroundColor(int color) {
        selectedBackgroundColorLiveData.setValue(color);
    }

    public void saveCategory(String name, int color, CategoryType categoryType) {
        setupNewCategory(name, color, categoryType);
        int validateNewCategory = newCategory.validate();
        if (validateNewCategory != ResultCode.Category.VALID) {
            onSaveEvent.setValue(validateNewCategory);
            return;
        }

        categoryRepository
                .saveCategory(newCategory)
                .subscribe(
                        new SingleObserver<>() {
                            @Override
                            public void onSubscribe(
                                    @io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

                            @Override
                            public void onSuccess(
                                    @io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                                onSaveEvent.postValue(ResultCode.Category.SAVE_SUCCEEDED);
                            }

                            @Override
                            public void onError(
                                    @io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                onSaveEvent.postValue(ResultCode.Category.SAVE_FAILED);
                            }
                        });
    }

    private void setupNewCategory(String name, int color, CategoryType categoryType) {
        if (originalCategory.getId() != null) {
            newCategory.setId(originalCategory.getId());
            newCategory.setCategoryType(originalCategory.getCategoryType());
            newCategory.setIcon(originalCategory.getIcon());
            newCategory.setCreatedTime(originalCategory.getCreatedTime());
        } else {
            newCategory.setId(UUID.randomUUID());
            newCategory.setCategoryType(categoryType);
            newCategory.setCreatedTime(DateTimeUtil.getCurrentTime());
        }
        newCategory.setName(name);
        newCategory.setColor(color);
    }
}
