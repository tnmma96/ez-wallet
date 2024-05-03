package io.tnmma.ezwallet.base.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseActivity<B extends ViewDataBinding, VM extends ViewModel>
        extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected B binding;

    protected VM viewModel;

    protected abstract int getLayoutId();

    protected abstract Class<VM> getViewModelClass();

    protected abstract ViewModelProvider.Factory getViewModelFactory();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        setupBinding();
    }

    private void setupViewModel() {
        ViewModelProvider.Factory factory = getViewModelFactory();
        if (factory != null) {
            viewModel = new ViewModelProvider(this, factory).get(getViewModelClass());
        } else {
            viewModel = new ViewModelProvider(this).get(getViewModelClass());
        }
    }

    private void setupBinding() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), getLayoutId(), null, false);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int stringResId) {
        try {
            showToast(getString(stringResId));
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, e.toString());
        }
    }
}
