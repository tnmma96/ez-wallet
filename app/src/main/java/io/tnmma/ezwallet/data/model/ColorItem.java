package io.tnmma.ezwallet.data.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;

import io.tnmma.ezwallet.util.ColorUtil;

public class ColorItem {

    private static final String TAG = ColorItem.class.getSimpleName();

    public static final DiffUtil.ItemCallback<ColorItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull ColorItem oldItem, @NonNull ColorItem newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull ColorItem oldItem, @NonNull ColorItem newItem) {
                    return false;
                }
            };

    private final int color;

    private final MutableLiveData<Boolean> selectedLiveData;

    public ColorItem(int color) {
        this.color = color;
        selectedLiveData = new MutableLiveData<>(false);
    }

    public int getColor() {
        return color;
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

    public int getForegroundColor() {
        return ColorUtil.getItemForegroundColor(color);
    }
}
