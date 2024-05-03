package io.tnmma.ezwallet.ui.adapter;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.databinding.ItemCategoryInfoBinding;

import java.util.List;

public class CategoryInfoAdapter extends BaseAdapter<Category, ItemCategoryInfoBinding> {

    private static final String TAG = CategoryInfoAdapter.class.getSimpleName();

    public CategoryInfoAdapter() {
        super(Category.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category_info;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemCategoryInfoBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemCategoryInfoBinding> holder,
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
                    holder.getBinding().itemCategoryInfoName.setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Category.COLOR:
                    int color = diffBundle.getInt(key);
                    holder.getBinding()
                            .itemCategoryInfoBackgroundColor
                            .setBackgroundTintList(ColorStateList.valueOf(color));
                    break;
                case ConstantKeys.Category.ICON:
                    break;
                case ConstantKeys.Category.FOREGROUND_COLOR:
                    int foregroundColor = diffBundle.getInt(key);
                    holder.getBinding().itemCategoryInfoIcon.setColorFilter(foregroundColor);
                    holder.getBinding().itemCategoryInfoName.setTextColor(foregroundColor);
                    break;
            }
        }
    }
}
