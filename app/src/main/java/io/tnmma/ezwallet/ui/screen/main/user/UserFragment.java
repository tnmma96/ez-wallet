package io.tnmma.ezwallet.ui.screen.main.user;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.fragment.BaseFragment;
import io.tnmma.ezwallet.databinding.FragmentUserBinding;

public class UserFragment extends BaseFragment<FragmentUserBinding, UserViewModel> {

    private static final String TAG = UserFragment.class.getSimpleName();

    private int nightMode = AppCompatDelegate.MODE_NIGHT_NO;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    protected Class<UserViewModel> getViewModelClass() {
        return UserViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
