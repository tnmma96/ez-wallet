package io.tnmma.ezwallet.ui.screen.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.DetailScreenType;
import io.tnmma.ezwallet.base.constant.MenuType;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.fragment.BaseFragment;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.databinding.FragmentMainBinding;
import io.tnmma.ezwallet.ui.screen.bottomsheet.editaccount.EditAccountFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.editcategory.EditCategoryFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selecttransactiontype.SelectTransactionTypeFragment;
import io.tnmma.ezwallet.ui.screen.main.account.AccountFragment;
import io.tnmma.ezwallet.ui.screen.main.category.CategoryFragment;
import io.tnmma.ezwallet.ui.screen.main.home.HomeFragment;
import io.tnmma.ezwallet.ui.screen.main.user.UserFragment;

import java.util.HashMap;
import java.util.Map;

public class MainFragment extends BaseFragment<FragmentMainBinding, MainViewModel> {

    private static final String TAG = MainFragment.class.getSimpleName();

    private Map<MenuType, Fragment> fragmentMap;

    private Fragment currentFragment;

    private Map<MenuType, BottomSheetDialogFragment> bottomSheetMap;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        fragmentMap = new HashMap<>();
        bottomSheetMap = new HashMap<>();
        listenForMenuTypeLiveData();
        listenForButtonAddClick();
        listenForTransactionTypeResult();
        viewModel.onSelectMenu(MenuType.HOME);
    }

    private void listenForMenuTypeLiveData() {
        viewModel
                .getMenuTypeLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<MenuType>() {
                            @Override
                            public void onChanged(MenuType menuType) {
                                switchTab(menuType);
                            }
                        });
    }

    private void listenForButtonAddClick() {
        binding.fragmentMainButtonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openBottomSheet(viewModel.getCurrentMenuType());
                    }
                });
    }

    private void switchTab(MenuType menuType) {
        if (menuType == null) {
            return;
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        Fragment fragment = fragmentMap.get(menuType);
        if (fragment == null) {
            fragment = getFragmentByMenuType(menuType);
            fragmentMap.put(menuType, fragment);
            transaction.add(binding.fragmentMainContainer.getId(), fragment);
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
        currentFragment = fragment;
    }

    private Fragment getFragmentByMenuType(MenuType menuType) {
        switch (menuType) {
            case HOME:
                return new HomeFragment();
            case ACCOUNT:
                return new AccountFragment();
            case CATEGORY:
                return new CategoryFragment();
            case USER:
                return new UserFragment();
        }
        return new Fragment();
    }

    private void openBottomSheet(MenuType menuType) {
        if (menuType == null) {
            return;
        }

        BottomSheetDialogFragment bottomSheet = bottomSheetMap.get(menuType);
        if (bottomSheet != null && bottomSheet.isAdded()) {
            return;
        }

        if (bottomSheet == null) {
            bottomSheet = getBottomSheetByMenuType(menuType);
            bottomSheetMap.put(menuType, bottomSheet);
        }
        bottomSheet.show(getChildFragmentManager(), null);
    }

    private BottomSheetDialogFragment getBottomSheetByMenuType(MenuType menuType) {
        switch (menuType) {
            case HOME:
            case USER:
                return new SelectTransactionTypeFragment();
            case ACCOUNT:
                return new EditAccountFragment();
            case CATEGORY:
                return new EditCategoryFragment();
        }
        return new BottomSheetDialogFragment();
    }

    private void listenForTransactionTypeResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.TRANSACTION_TYPE,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                TransactionType transactionType =
                                        (TransactionType) result.get(ConstantKeys.Transaction.TYPE);
                                TransactionWithDetails transaction = new TransactionWithDetails();
                                transaction.setTransactionType(
                                        transactionType != null
                                                ? transactionType
                                                : TransactionType.INCOME);
                                openTransactionDetailScreen(transaction);
                            }
                        });
    }

    private void openTransactionDetailScreen(TransactionWithDetails transaction) {
        Bundle data = new Bundle();
        data.putSerializable(ConstantKeys.ModelType.TRANSACTION, transaction);
        Intent intent = new Intent(ConstantKeys.IntentAction.OPEN_DETAIL_SCREEN);
        intent.putExtra(ConstantKeys.DetailScreen.TYPE, DetailScreenType.TRANSACTION);
        intent.putExtra(ConstantKeys.DetailScreen.DATA, data);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public void handleBackPressEvent() {
        if (viewModel.getCurrentMenuType() == MenuType.HOME) {
            LocalBroadcastManager.getInstance(getContext())
                    .sendBroadcast(new Intent(ConstantKeys.IntentAction.CLOSE_APP));
        } else {
            viewModel.onSelectMenu(MenuType.HOME);
        }
    }
}
