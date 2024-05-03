package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import io.tnmma.ezwallet.data.model.TransactionTypeAndAmount;
import io.tnmma.ezwallet.databinding.ViewTransactionTypeAndAmountBinding;

public class TransactionTypeAndAmountView extends LinearLayout {

    private static final String TAG = TransactionTypeAndAmountView.class.getSimpleName();

    private ViewTransactionTypeAndAmountBinding binding;

    public TransactionTypeAndAmountView(Context context) {
        super(context);
        init(context);
    }

    public TransactionTypeAndAmountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionTypeAndAmountView(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TransactionTypeAndAmountView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding =
                ViewTransactionTypeAndAmountBinding.inflate(
                        LayoutInflater.from(context), this, true);
    }

    public void setViewModel(TransactionTypeAndAmount transactionTypeAndAmount) {
        binding.setViewModel(transactionTypeAndAmount);
    }
}
