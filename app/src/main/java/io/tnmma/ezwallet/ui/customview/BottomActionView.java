package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.databinding.ViewBottomActionBinding;

public class BottomActionView extends ConstraintLayout {

    private static final String TAG = BottomActionView.class.getSimpleName();

    private ViewBottomActionBinding binding;

    private OnActionClickListener onActionClickListener;

    public BottomActionView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BottomActionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomActionView(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BottomActionView(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding = ViewBottomActionBinding.inflate(LayoutInflater.from(context), this, true);
        setupActionClickListener();
    }

    // jumpDrawablesToCurrentState() is added to quickly show ripple
    //      before exiting a screen or bottom sheet
    // This works on SM-J410F with Android 8.1 (API 27)
    //      but does not work on Xiaomi Redmi Note 9S with Android 12 (API 31/32)
    private void setupActionClickListener() {
        binding.viewBottomActionButtonCancel.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.viewBottomActionButtonCancel.jumpDrawablesToCurrentState();
                        if (onActionClickListener != null) {
                            binding.viewBottomActionButtonCancel.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            onActionClickListener.onActionCancelClick();
                                        }
                                    });
                        }
                    }
                });
        binding.viewBottomActionButtonConfirm.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.viewBottomActionButtonConfirm.jumpDrawablesToCurrentState();
                        if (onActionClickListener != null) {
                            binding.viewBottomActionButtonConfirm.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            onActionClickListener.onActionConfirmClick();
                                        }
                                    });
                        }
                    }
                });
    }

    public void setViewModel(BottomAction bottomAction) {
        binding.setViewModel(bottomAction);
    }

    public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
    }

    public interface OnActionClickListener {

        void onActionCancelClick();

        void onActionConfirmClick();
    }
}
