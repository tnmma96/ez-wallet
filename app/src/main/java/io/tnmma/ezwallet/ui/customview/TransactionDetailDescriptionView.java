package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import io.tnmma.ezwallet.data.model.TransactionDetailDescription;
import io.tnmma.ezwallet.databinding.ViewTransactionDetailDescriptionBinding;

public class TransactionDetailDescriptionView extends LinearLayout {

    private static final String TAG = TransactionDetailDescriptionView.class.getSimpleName();

    private ViewTransactionDetailDescriptionBinding binding;

    public TransactionDetailDescriptionView(Context context) {
        super(context);
        init(context);
    }

    public TransactionDetailDescriptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionDetailDescriptionView(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TransactionDetailDescriptionView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding =
                ViewTransactionDetailDescriptionBinding.inflate(
                        LayoutInflater.from(context), this, true);
    }

    public void setViewModel(TransactionDetailDescription transactionDetailDescription) {
        binding.setViewModel(transactionDetailDescription);
        binding.setLifecycleOwner(ViewTreeLifecycleOwner.get(this));
    }
}
