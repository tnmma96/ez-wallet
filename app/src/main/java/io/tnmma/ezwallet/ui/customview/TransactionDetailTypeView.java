package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import io.tnmma.ezwallet.data.model.TransactionDetailType;
import io.tnmma.ezwallet.databinding.ViewTransactionDetailTypeBinding;

public class TransactionDetailTypeView extends LinearLayout {

    private static final String TAG = TransactionDetailTypeView.class.getSimpleName();

    private ViewTransactionDetailTypeBinding binding;

    public TransactionDetailTypeView(Context context) {
        super(context);
        init(context);
    }

    public TransactionDetailTypeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionDetailTypeView(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TransactionDetailTypeView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding =
                ViewTransactionDetailTypeBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setViewModel(TransactionDetailType transactionDetailType) {
        binding.setViewModel(transactionDetailType);
        binding.setLifecycleOwner(ViewTreeLifecycleOwner.get(this));
    }
}
