package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.databinding.ViewTransactionAccountBinding;

public class TransactionAccountView extends LinearLayout {

    private static final String TAG = TransactionAccountView.class.getSimpleName();

    private ViewTransactionAccountBinding binding;

    public TransactionAccountView(Context context) {
        super(context);
        init(context);
    }

    public TransactionAccountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionAccountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TransactionAccountView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding = ViewTransactionAccountBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setViewModel(Account account) {
        binding.setViewModel(account);
    }
}
