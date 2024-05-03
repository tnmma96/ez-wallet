package io.tnmma.ezwallet.data.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.util.ColorUtil;

public class CategoryTypeItem {

    private static final String TAG = CategoryTypeItem.class.getSimpleName();

    public static final DiffUtil.ItemCallback<CategoryTypeItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull CategoryTypeItem oldItem, @NonNull CategoryTypeItem newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull CategoryTypeItem oldItem, @NonNull CategoryTypeItem newItem) {
                    return false;
                }
            };

    private final CategoryType categoryType;

    private final MutableLiveData<Boolean> selectedLiveData;

    public CategoryTypeItem(CategoryType categoryType) {
        this.categoryType = categoryType;
        selectedLiveData = new MutableLiveData<>(false);
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public LiveData<Boolean> getSelectedLiveData() {
        return selectedLiveData;
    }

    public boolean isSelected() {
        return Boolean.TRUE.equals(selectedLiveData.getValue());
    }

    public void setSelected(boolean selected) {
        selectedLiveData.setValue(selected);
    }

    public int getIconResId() {
        if (categoryType == null) {
            return 0;
        }
        return categoryType == CategoryType.INCOME
                ? R.drawable.icon_income
                : R.drawable.icon_expense;
    }

    public int getLabelResId() {
        if (categoryType == null) {
            return 0;
        }
        return categoryType == CategoryType.INCOME
                ? R.string.label_category_type_income
                : R.string.label_category_type_expense;
    }

    public int getColor() {
        if (categoryType == null) {
            return 0;
        }
        int colorResId = categoryType == CategoryType.INCOME ? R.color.ivy_green : R.color.ivy_red;
        return ColorUtil.getColorFromResId(colorResId);
    }
}
