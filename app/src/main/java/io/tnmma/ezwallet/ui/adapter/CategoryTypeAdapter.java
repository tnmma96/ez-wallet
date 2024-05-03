package io.tnmma.ezwallet.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.CategoryTypeItem;
import io.tnmma.ezwallet.databinding.ItemCategoryTypeBinding;

public class CategoryTypeAdapter extends BaseAdapter<CategoryTypeItem, ItemCategoryTypeBinding> {

    private static final String TAG = CategoryTypeAdapter.class.getSimpleName();

    public CategoryTypeAdapter() {
        super(CategoryTypeItem.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category_type;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemCategoryTypeBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemCategoryTypeBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoryTypeItem item = getItem(viewHolder.getBindingAdapterPosition());
                        setSelectedItem(item.getCategoryType());
                    }
                });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemCategoryTypeBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    public CategoryTypeItem getSelectedItem() {
        for (CategoryTypeItem item : getCurrentList()) {
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    private void setSelectedItem(CategoryType categoryType) {
        for (int i = 0; i < getItemCount(); ++i) {
            CategoryTypeItem item = getItem(i);
            if (item.isSelected() && item.getCategoryType() != categoryType) {
                item.setSelected(false);
                notifyItemChanged(i);
            }
            if (!item.isSelected() && item.getCategoryType() == categoryType) {
                item.setSelected(true);
                notifyItemChanged(i);
            }
        }
    }
}
