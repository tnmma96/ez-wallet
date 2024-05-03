package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.databinding.ViewTransactionCategoryBinding;

public class TransactionCategoryView extends LinearLayout {

    private static final String TAG = TransactionCategoryView.class.getSimpleName();

    private ViewTransactionCategoryBinding binding;

    public TransactionCategoryView(Context context) {
        super(context);
        init(context);
    }

    public TransactionCategoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionCategoryView(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TransactionCategoryView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding = ViewTransactionCategoryBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setViewModel(Category category) {
        binding.setViewModel(category);
    }
}
