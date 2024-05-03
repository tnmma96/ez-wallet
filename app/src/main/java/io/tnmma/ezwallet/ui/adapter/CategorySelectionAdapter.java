package io.tnmma.ezwallet.ui.adapter;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.CategorySelection;
import io.tnmma.ezwallet.databinding.ItemCategorySelectionBinding;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CategorySelectionAdapter
        extends BaseAdapter<CategorySelection, ItemCategorySelectionBinding> {

    private static final String TAG = CategorySelectionAdapter.class.getSimpleName();

    private final MutableLiveData<Category> selectedCategoryLiveData;

    public CategorySelectionAdapter() {
        super(CategorySelection.DIFF_CALLBACK);
        selectedCategoryLiveData = new MutableLiveData<>();
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category_selection;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemCategorySelectionBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemCategorySelectionBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategorySelection item = getItem(viewHolder.getBindingAdapterPosition());
                        setSelectedItem(item.getId());
                        selectedCategoryLiveData.setValue(item.isSelected() ? item : null);
                    }
                });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemCategorySelectionBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemCategorySelectionBinding> holder,
            int position,
            @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Bundle diffBundle = (Bundle) payloads.get(0);
        for (String key : diffBundle.keySet()) {
            switch (key) {
                case ConstantKeys.Category.NAME:
                    holder.getBinding()
                            .itemCategorySelectionName
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Category.COLOR:
                    int color = diffBundle.getInt(key);
                    holder.getBinding()
                            .itemCategorySelectionBackgroundColor
                            .setBackgroundTintList(ColorStateList.valueOf(color));
                    break;
                case ConstantKeys.Category.ICON:
                    break;
                case ConstantKeys.Category.FOREGROUND_COLOR:
                    int foregroundColor = diffBundle.getInt(key);
                    holder.getBinding().itemCategorySelectionName.setTextColor(foregroundColor);
                    holder.getBinding().itemCategorySelectionIcon.setColorFilter(foregroundColor);
                    break;
            }
        }
    }

    public LiveData<Category> getSelectedCategoryLiveData() {
        return selectedCategoryLiveData;
    }

    public CategorySelection getSelectedItem() {
        for (CategorySelection item : getCurrentList()) {
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    public int getSelectedPosition() {
        for (int i = 0; i < getItemCount(); ++i) {
            if (getItem(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void setSelectedItem(UUID categoryId) {
        for (int i = 0; i < getItemCount(); ++i) {
            CategorySelection item = getItem(i);
            if (item.isSelected() && !Objects.equals(item.getId(), categoryId)) {
                item.setSelected(false);
                notifyItemChanged(i);
            }
            if (Objects.equals(item.getId(), categoryId)) {
                item.setSelected(!item.isSelected());
                notifyItemChanged(i);
            }
        }
    }
}
