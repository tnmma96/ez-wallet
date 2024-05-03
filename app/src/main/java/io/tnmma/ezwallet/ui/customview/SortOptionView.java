package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import io.tnmma.ezwallet.data.model.SortOption;
import io.tnmma.ezwallet.databinding.ViewSortOptionBinding;

public class SortOptionView extends LinearLayout {

    private static final String TAG = SortOptionView.class.getSimpleName();

    private ViewSortOptionBinding binding;

    public SortOptionView(Context context) {
        super(context);
        init(context);
    }

    public SortOptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SortOptionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SortOptionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding = ViewSortOptionBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setViewModel(SortOption sortOption) {
        binding.setViewModel(sortOption);
        binding.setLifecycleOwner(ViewTreeLifecycleOwner.get(this));
    }
}
