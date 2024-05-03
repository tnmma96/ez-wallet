package io.tnmma.ezwallet.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.util.ColorUtil;
import io.tnmma.ezwallet.util.StringUtil;

import kotlin.jvm.functions.Function1;

import java.util.UUID;

public class TransactionDetailCategory {

    private static final String TAG = TransactionDetailCategory.class.getSimpleName();

    private final MutableLiveData<Category> categoryLiveData;

    private final LiveData<String> nameLiveData;

    private final LiveData<Integer> backgroundColorLiveData;

    private final LiveData<Integer> iconColorLiveData;

    private final LiveData<Integer> textColorLiveData;

    public TransactionDetailCategory(Category category) {
        categoryLiveData = new MutableLiveData<>(category);
        nameLiveData = setupNameLiveData();
        backgroundColorLiveData = setupBackgroundColorLiveData();
        iconColorLiveData = setupIconColorLiveData();
        textColorLiveData = setupTextColorLiveData();
    }

    private LiveData<String> setupNameLiveData() {
        return Transformations.map(
                categoryLiveData,
                new Function1<Category, String>() {
                    @Override
                    public String invoke(Category category) {
                        return category != null
                                ? category.getName()
                                : StringUtil.getStringFromResId(R.string.hint_select_category);
                    }
                });
    }

    private LiveData<Integer> setupBackgroundColorLiveData() {
        return Transformations.map(
                categoryLiveData,
                new Function1<Category, Integer>() {
                    @Override
                    public Integer invoke(Category category) {
                        return category != null
                                ? category.getColor()
                                : ColorUtil.getColorFromResId(R.color.ivy_medium_white);
                    }
                });
    }

    private LiveData<Integer> setupIconColorLiveData() {
        return Transformations.map(
                categoryLiveData,
                new Function1<Category, Integer>() {
                    @Override
                    public Integer invoke(Category category) {
                        return category != null
                                ? ColorUtil.getItemForegroundColor(category.getColor())
                                : ColorUtil.getColorFromResId(R.color.ivy_black);
                    }
                });
    }

    private LiveData<Integer> setupTextColorLiveData() {
        return Transformations.map(
                categoryLiveData,
                new Function1<Category, Integer>() {
                    @Override
                    public Integer invoke(Category category) {
                        return category != null
                                ? ColorUtil.getItemForegroundColor(category.getColor())
                                : ColorUtil.getColorFromResId(R.color.ivy_grey);
                    }
                });
    }

    public LiveData<Category> getCategoryLiveData() {
        return categoryLiveData;
    }

    public LiveData<String> getNameLiveData() {
        return nameLiveData;
    }

    public LiveData<Integer> getBackgroundColorLiveData() {
        return backgroundColorLiveData;
    }

    public LiveData<Integer> getIconColorLiveData() {
        return iconColorLiveData;
    }

    public LiveData<Integer> getTextColorLiveData() {
        return textColorLiveData;
    }

    public Category getCategory() {
        return categoryLiveData.getValue();
    }

    public UUID getCategoryId() {
        return categoryLiveData.getValue() != null ? categoryLiveData.getValue().getId() : null;
    }

    public CategoryType getCategoryType() {
        return categoryLiveData.getValue() != null
                ? categoryLiveData.getValue().getCategoryType()
                : null;
    }

    public void setCategory(Category category) {
        categoryLiveData.setValue(category);
    }

    public void removeCategory() {
        categoryLiveData.setValue(null);
    }
}
