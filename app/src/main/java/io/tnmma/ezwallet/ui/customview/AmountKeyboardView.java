package io.tnmma.ezwallet.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;

import io.tnmma.ezwallet.databinding.ViewAmountKeyboardBinding;

public class AmountKeyboardView extends TableLayout {

    private static final String TAG = AmountKeyboardView.class.getSimpleName();

    private ViewAmountKeyboardBinding binding;

    private OnKeyClickListener onKeyClickListener;

    public AmountKeyboardView(Context context) {
        super(context);
        init(context);
    }

    public AmountKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        binding = ViewAmountKeyboardBinding.inflate(LayoutInflater.from(context), this, true);
        setupKeyClickListener();
    }

    private void setupKeyClickListener() {
        View[] keyViews =
                new View[] {
                    binding.viewAmountKeyboardKey0,
                    binding.viewAmountKeyboardKey1,
                    binding.viewAmountKeyboardKey2,
                    binding.viewAmountKeyboardKey3,
                    binding.viewAmountKeyboardKey4,
                    binding.viewAmountKeyboardKey5,
                    binding.viewAmountKeyboardKey6,
                    binding.viewAmountKeyboardKey7,
                    binding.viewAmountKeyboardKey8,
                    binding.viewAmountKeyboardKey9,
                    binding.viewAmountKeyboardKeyDecimal,
                    binding.viewAmountKeyboardKeyNegative,
                    binding.viewAmountKeyboardKeyBackspace,
                    binding.viewAmountKeyboardKeyClear
                };
        for (View key : keyViews) {
            key.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onKeyClickListener != null) {
                                onKeyClickListener.onKeyClick(
                                        key.getTag() != null ? (Integer) key.getTag() : 0);
                            }
                        }
                    });
        }
    }

    public void setShowKeyNegative(boolean showKeyNegative) {
        binding.viewAmountKeyboardKeyNegative.setVisibility(
                showKeyNegative ? View.VISIBLE : View.GONE);
    }

    public void setOnKeyClickListener(OnKeyClickListener onKeyClickListener) {
        this.onKeyClickListener = onKeyClickListener;
    }

    public interface OnKeyClickListener {
        void onKeyClick(int keyCode);
    }
}
