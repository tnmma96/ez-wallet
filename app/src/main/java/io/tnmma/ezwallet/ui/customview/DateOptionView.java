package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import io.tnmma.ezwallet.data.model.DateOption;
import io.tnmma.ezwallet.databinding.ViewDateOptionBinding;

public class DateOptionView extends LinearLayout {

    private static final String TAG = DateOptionView.class.getSimpleName();

    private ViewDateOptionBinding binding;

    public DateOptionView(Context context) {
        super(context);
        init(context);
    }

    public DateOptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DateOptionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DateOptionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding = ViewDateOptionBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setViewModel(DateOption dateOption) {
        binding.setViewModel(dateOption);
        binding.setLifecycleOwner(ViewTreeLifecycleOwner.get(this));
    }
}
