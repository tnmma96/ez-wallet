package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import io.tnmma.ezwallet.data.model.MenuTypeItem;
import io.tnmma.ezwallet.databinding.ViewMenuTypeBinding;

public class MenuTypeView extends ConstraintLayout {

    private static final String TAG = MenuTypeView.class.getSimpleName();

    private ViewMenuTypeBinding binding;

    public MenuTypeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MenuTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MenuTypeView(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding = ViewMenuTypeBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setViewModel(MenuTypeItem menuTypeItem) {
        binding.setViewModel(menuTypeItem);
        binding.setLifecycleOwner(ViewTreeLifecycleOwner.get(this));
    }
}
