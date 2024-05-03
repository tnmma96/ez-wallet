package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import io.tnmma.ezwallet.data.model.DateRange;
import io.tnmma.ezwallet.databinding.ViewDateRangeBinding;

public class DateRangeView extends ConstraintLayout {

    private static final String TAG = DateRangeView.class.getSimpleName();

    private ViewDateRangeBinding binding;

    private OnDateClickListener onDateClickListener;

    public DateRangeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DateRangeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DateRangeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DateRangeView(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding = ViewDateRangeBinding.inflate(LayoutInflater.from(context), this, true);
        setupDateClickListener();
    }

    private void setupDateClickListener() {
        binding.viewDateRangeValueFrom.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDateClickListener.onStartDateClick();
                    }
                });
        binding.viewDateRangeValueTo.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDateClickListener.onEndDateClick();
                    }
                });
    }

    public void setViewModel(DateRange dateRange) {
        binding.setViewModel(dateRange);
        binding.setLifecycleOwner(ViewTreeLifecycleOwner.get(this));
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    public interface OnDateClickListener {

        void onStartDateClick();

        void onEndDateClick();
    }
}
