package io.tnmma.ezwallet.ui.adapter;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.pojo.CategoryWithDetails;
import io.tnmma.ezwallet.databinding.ItemCategoryWithDetailsBinding;

import java.util.List;

public class CategoryWithDetailsAdapter
        extends BaseAdapter<CategoryWithDetails, ItemCategoryWithDetailsBinding> {

    private static final String TAG = CategoryWithDetailsAdapter.class.getSimpleName();

    private OnClickListener onClickListener;

    public CategoryWithDetailsAdapter() {
        super(CategoryWithDetails.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category_with_details;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemCategoryWithDetailsBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemCategoryWithDetailsBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null) {
                            onClickListener.onItemClick(
                                    getItem(viewHolder.getBindingAdapterPosition()));
                        }
                    }
                });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemCategoryWithDetailsBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemCategoryWithDetailsBinding> holder,
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
                            .itemCategoryWithDetailsName
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Category.COLOR:
                    int color = diffBundle.getInt(key);
                    holder.getBinding()
                            .itemCategoryWithDetailsBackgroundColor
                            .setBackgroundTintList(ColorStateList.valueOf(color));
                    break;
                case ConstantKeys.Category.ICON:
                    break;
                case ConstantKeys.Category.TOTAL_AMOUNT:
                    holder.getBinding()
                            .itemCategoryWithDetailsValueAmount
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.Category.FOREGROUND_COLOR:
                    int foregroundColor = diffBundle.getInt(key);
                    holder.getBinding().itemCategoryWithDetailsName.setTextColor(foregroundColor);
                    holder.getBinding().itemCategoryWithDetailsIcon.setColorFilter(foregroundColor);
            }
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {

        void onItemClick(CategoryWithDetails item);
    }
}
