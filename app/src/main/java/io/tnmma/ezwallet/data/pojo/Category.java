package io.tnmma.ezwallet.data.pojo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.data.db.entity.CategoryEntity;
import io.tnmma.ezwallet.util.ColorUtil;

import java.util.Objects;

public class Category extends CategoryEntity {

    private static final String TAG = Category.class.getSimpleName();

    public static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Category oldItem, @NonNull Category newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Category oldItem, @NonNull Category newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(
                        @NonNull Category oldItem, @NonNull Category newItem) {
                    Bundle diffBundle = new Bundle();
                    if (!Objects.equals(oldItem.getName(), newItem.getName())) {
                        diffBundle.putString(ConstantKeys.Category.NAME, newItem.getName());
                    }
                    if (oldItem.getColor() != newItem.getColor()) {
                        diffBundle.putInt(ConstantKeys.Category.COLOR, newItem.getColor());
                    }
                    if (!Objects.equals(oldItem.getIcon(), newItem.getIcon())) {
                        diffBundle.putString(ConstantKeys.Category.ICON, newItem.getIcon());
                    }
                    if (oldItem.getForegroundColor() != newItem.getForegroundColor()) {
                        diffBundle.putInt(
                                ConstantKeys.Category.FOREGROUND_COLOR,
                                newItem.getForegroundColor());
                    }
                    return diffBundle.isEmpty() ? null : diffBundle;
                }
            };

    public int getForegroundColor() {
        return ColorUtil.getItemForegroundColor(getColor());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        final Category category = (Category) obj;
        if (!Objects.equals(this.getId(), category.getId())) {
            return false;
        }
        if (!Objects.equals(this.getName(), category.getName())) {
            return false;
        }
        if (this.getCategoryType() != category.getCategoryType()) {
            return false;
        }
        if (this.getColor() != category.getColor()) {
            return false;
        }
        if (!Objects.equals(this.getIcon(), category.getIcon())) {
            return false;
        }
        if (!Objects.equals(this.getCreatedTime(), category.getCreatedTime())) {
            return false;
        }
        return true;
    }
}
