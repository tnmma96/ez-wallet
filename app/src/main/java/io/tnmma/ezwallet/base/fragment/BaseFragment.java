package io.tnmma.ezwallet.base.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends ViewModel>
        extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    protected B binding;

    protected VM viewModel;

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
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
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
}
