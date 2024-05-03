package io.tnmma.ezwallet.ui.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.CategoryAmountInfo;
import io.tnmma.ezwallet.databinding.ItemCategoryAmountInfoBinding;

import java.util.List;

public class CategoryAmountInfoAdapter
        extends BaseAdapter<CategoryAmountInfo, ItemCategoryAmountInfoBinding> {

    private static final String TAG = CategoryAmountInfoAdapter.class.getSimpleName();

    private OnClickListener onClickListener;

    public CategoryAmountInfoAdapter() {
        super(CategoryAmountInfo.DIFF_CALLBACK);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category_amount_info;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemCategoryAmountInfoBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemCategoryAmountInfoBinding> viewHolder =
                super.onCreateViewHolder(parent, viewType);
        viewHolder
                .getBinding()
                .itemCategoryAmountInfoButtonAdd
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onClickListener != null) {
                                    onClickListener.onButtonAddClick();
                                }
                            }
                        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemCategoryAmountInfoBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    @Override
    public void onBindViewHolder(
            @NonNull BaseViewHolder<ItemCategoryAmountInfoBinding> holder,
            int position,
            @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        Bundle diffBundle = (Bundle) payloads.get(0);
        for (String key : diffBundle.keySet()) {
            switch (key) {
                case ConstantKeys.CategoryAmountInfo.AMOUNT:
                    holder.getBinding()
                            .itemCategoryAmountInfoAmount
                            .setText(diffBundle.getString(key));
                    break;
                case ConstantKeys.CategoryAmountInfo.COUNT:
                    holder.getBinding()
                            .itemCategoryAmountInfoCount
                            .setText(diffBundle.getString(key));
                    break;
            }
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {

        void onButtonAddClick();
    }
}
