package io.tnmma.ezwallet.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.recyclerview.BaseAdapter;
import io.tnmma.ezwallet.base.recyclerview.BaseViewHolder;
import io.tnmma.ezwallet.data.model.ColorItem;
import io.tnmma.ezwallet.databinding.ItemColorBinding;

public class ColorAdapter extends BaseAdapter<ColorItem, ItemColorBinding> {

    private static final String TAG = ColorAdapter.class.getSimpleName();

    private final MutableLiveData<Integer> selectedColorLiveData;

    public ColorAdapter() {
        super(ColorItem.DIFF_CALLBACK);
        selectedColorLiveData = new MutableLiveData<>();
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_color;
    }

    @NonNull
    @Override
    public BaseViewHolder<ItemColorBinding> onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<ItemColorBinding> viewHolder = super.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ColorItem item = getItem(viewHolder.getBindingAdapterPosition());
                        setSelectedItem(item.getColor());
                        if (selectedColorLiveData.getValue() == null
                                || selectedColorLiveData.getValue() != item.getColor()) {
                            selectedColorLiveData.setValue(item.getColor());
                        }
                    }
                });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemColorBinding> holder, int position) {
        holder.getBinding().setViewModel(getItem(position));
    }

    public LiveData<Integer> getSelectedColorLiveData() {
        return selectedColorLiveData;
    }

    public int getSelectedPosition() {
        for (int i = 0; i < getItemCount(); ++i) {
            if (getItem(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public ColorItem getSelectedItem() {
        for (ColorItem item : getCurrentList()) {
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    public void setSelectedItem(int color) {
        for (int i = 0; i < getItemCount(); ++i) {
            ColorItem item = getItem(i);
            if (item.isSelected() && item.getColor() != color) {
                item.setSelected(false);
                notifyItemChanged(i);
            }
            if (!item.isSelected() && item.getColor() == color) {
                item.setSelected(true);
                notifyItemChanged(i);
            }
        }
    }
}
