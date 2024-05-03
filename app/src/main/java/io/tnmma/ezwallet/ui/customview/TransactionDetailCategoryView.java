package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import io.tnmma.ezwallet.data.model.TransactionDetailCategory;
import io.tnmma.ezwallet.databinding.ViewTransactionDetailCategoryBinding;

public class TransactionDetailCategoryView extends ConstraintLayout {

    private static final String TAG = TransactionDetailCategoryView.class.getSimpleName();

    private ViewTransactionDetailCategoryBinding binding;

    public TransactionDetailCategoryView(Context context) {
        super(context);
        init(context);
    }

    public TransactionDetailCategoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionDetailCategoryView(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TransactionDetailCategoryView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding =
                ViewTransactionDetailCategoryBinding.inflate(
                        LayoutInflater.from(context), this, true);
    }

    public void setViewModel(TransactionDetailCategory transactionDetailCategory) {
        binding.setViewModel(transactionDetailCategory);
        binding.setLifecycleOwner(ViewTreeLifecycleOwner.get(this));
    }
}
