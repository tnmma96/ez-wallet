package io.tnmma.ezwallet.base.fragment;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.tnmma.ezwallet.R;

public abstract class BaseBottomSheetFragment<B extends ViewDataBinding, VM extends ViewModel>
        extends BottomSheetDialogFragment {

    private static final String TAG = BaseBottomSheetFragment.class.getSimpleName();

    protected B binding;

    protected VM viewModel;

    private int maxHeightPercent;

    protected abstract int getLayoutId();

    protected abstract Class<VM> getViewModelClass();

    protected abstract ViewModelProvider.Factory getViewModelFactory();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        ViewModelProvider.Factory factory = getViewModelFactory();
        if (factory != null) {
            viewModel = new ViewModelProvider(this, factory).get(getViewModelClass());
        } else {
            viewModel = new ViewModelProvider(this).get(getViewModelClass());
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        setupBinding(inflater, container);
        return binding.getRoot();
    }

    private void setupBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        binding.setLifecycleOwner(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupBottomSheetUi();
    }

    private void setupBottomSheetUi() {
        binding.getRoot()
                .setBackground(
                        AppCompatResources.getDrawable(
                                getContext(), R.drawable.background_base_bottom_sheet));

        View bottomSheet =
                ((BottomSheetDialog) getDialog())
                        .findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            BottomSheetBehavior.from(bottomSheet).setDraggable(false);
            // BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);

            if (maxHeightPercent > 0) {
                BottomSheetBehavior.from(bottomSheet)
                        .setMaxHeight(getActivityWindowHeight() * maxHeightPercent / 100);
            }
        }
    }

    protected int getActivityWindowHeight() {
        Rect rect = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.height();
    }

    protected void setMaxHeightPercent(int maxHeightPercent) {
        this.maxHeightPercent = maxHeightPercent;
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    protected void setupOutsideViewClickEvent() {
        View outsideView =
                ((BottomSheetDialog) getDialog())
                        .findViewById(com.google.android.material.R.id.touch_outside);
        if (outsideView != null) {
            outsideView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Outside view clicked");
                        }
                    });
        }
    }

    protected void setupBackPressEvent() {
        getDialog()
                .setOnKeyListener(
                        new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(
                                    DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK
                                        && event.getAction() == KeyEvent.ACTION_UP) {
                                    Log.d(TAG, "Back button released");
                                    return true;
                                }
                                return false;
                            }
                        });
    }

    protected void setupKeyboardShowEvent() {
        binding.getRoot()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                boolean keyboardShown =
                                        ViewCompat.getRootWindowInsets(binding.getRoot())
                                                .isVisible(WindowInsetsCompat.Type.ime());
                                Log.d(TAG, "keyboardShown = " + keyboardShown);
                            }
                        });
    }

    protected void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int stringResId) {
        try {
            showToast(getString(stringResId));
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean isDialogVisible1() {
        return getDialog() != null && getDialog().isShowing();
    }

    public boolean isDialogVisible2() {
        if (getDialog() == null || getDialog().getWindow() == null) {
            return false;
        }
        return getDialog().getWindow().getDecorView().getVisibility() == View.VISIBLE;
    }
}
