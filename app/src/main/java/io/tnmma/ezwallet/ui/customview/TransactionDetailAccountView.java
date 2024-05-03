package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import io.tnmma.ezwallet.data.model.TransactionDetailAccount;
import io.tnmma.ezwallet.databinding.ViewTransactionDetailAccountBinding;

public class TransactionDetailAccountView extends ConstraintLayout {

    private static final String TAG = TransactionDetailAccountView.class.getSimpleName();

    private ViewTransactionDetailAccountBinding binding;

    public TransactionDetailAccountView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TransactionDetailAccountView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionDetailAccountView(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TransactionDetailAccountView(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding =
                ViewTransactionDetailAccountBinding.inflate(
                        LayoutInflater.from(context), this, true);
    }

    public void setViewModel(TransactionDetailAccount transactionDetailAccount) {
        binding.setViewModel(transactionDetailAccount);
        binding.setLifecycleOwner(ViewTreeLifecycleOwner.get(this));
    }
}
