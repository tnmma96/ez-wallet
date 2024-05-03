package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import io.tnmma.ezwallet.data.model.TransactionDetailDate;
import io.tnmma.ezwallet.databinding.ViewTransactionDetailDateBinding;

public class TransactionDetailDateView extends LinearLayout {

    private static final String TAG = TransactionDetailDateView.class.getSimpleName();

    private ViewTransactionDetailDateBinding binding;

    public TransactionDetailDateView(Context context) {
        super(context);
        init(context);
    }

    public TransactionDetailDateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionDetailDateView(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TransactionDetailDateView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding =
                ViewTransactionDetailDateBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setViewModel(TransactionDetailDate transactionDetailDate) {
        binding.setViewModel(transactionDetailDate);
        binding.setLifecycleOwner(ViewTreeLifecycleOwner.get(this));
    }
}
