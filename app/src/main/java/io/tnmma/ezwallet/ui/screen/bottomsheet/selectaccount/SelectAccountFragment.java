package io.tnmma.ezwallet.ui.screen.bottomsheet.selectaccount;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.AccountSelection;
import io.tnmma.ezwallet.databinding.FragmentSelectAccountBinding;
import io.tnmma.ezwallet.ui.adapter.AccountSelectionAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;
import io.tnmma.ezwallet.ui.screen.bottomsheet.editaccount.EditAccountFragment;

import java.util.List;
import java.util.Objects;

public class SelectAccountFragment
        extends BaseBottomSheetFragment<FragmentSelectAccountBinding, SelectAccountViewModel> {

    private static final String TAG = SelectAccountFragment.class.getSimpleName();

    private AccountSelectionAdapter srcAccountSelectionAdapter;

    private AccountSelectionAdapter dstAccountSelectionAdapter;

    private EditAccountFragment editAccountFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_account;
    }

    @Override
    protected Class<SelectAccountViewModel> getViewModelClass() {
        return SelectAccountViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        TransactionType transactionType = null;
        Account srcAccount = null, dstAccount = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            transactionType = (TransactionType) arguments.get(ConstantKeys.Transaction.TYPE);
            srcAccount = (Account) arguments.get(ConstantKeys.Transaction.SRC_ACCOUNT);
            dstAccount = (Account) arguments.get(ConstantKeys.Transaction.DST_ACCOUNT);
        }
        return SelectAccountViewModel.getFactory(transactionType, srcAccount, dstAccount);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        setupSoftInputMode();
        setupSrcAccountListUi();
        setupSrcAccountAdapter();
        observeSrcAccountListLiveData();
        observeSelectedSrcAccountFromAdapter();
        if (viewModel.isTransferTransaction()) {
            setupDstAccountListUi();
            setupDstAccountAdapter();
            observeDstAccountListLiveData();
            observeSelectedDstAccountFromAdapter();
        }
        listenForButtonAddClick();
        listenForBottomActionClick();
    }

    // Set soft input mode to adjustNothing so this fragment won't be affected by
    // the soft input mode in EditAccountFragment
    private void setupSoftInputMode() {
        getDialog()
                .getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void setupSrcAccountListUi() {
        int orientation = LinearLayout.HORIZONTAL;
        binding.fragmentSelectAccountListSrc.setItemAnimator(null);
        binding.fragmentSelectAccountListSrc.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentSelectAccountListSrc.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentSelectAccountListSrc.addItemDecoration(itemDecoration);
    }

    private void setupSrcAccountAdapter() {
        srcAccountSelectionAdapter = new AccountSelectionAdapter();
        binding.fragmentSelectAccountListSrc.setAdapter(srcAccountSelectionAdapter);
    }

    private void observeSrcAccountListLiveData() {
        viewModel
                .getSrcAccountListLiveData()
                .observe(
                        this,
                        new Observer<List<AccountSelection>>() {
                            @Override
                            public void onChanged(List<AccountSelection> accountList) {
                                srcAccountSelectionAdapter.submitList(
                                        accountList,
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollSrcAccountListToSelectedPosition();
                                            }
                                        });
                            }
                        });
    }

    private void scrollSrcAccountListToSelectedPosition() {
        int selectedPosition = srcAccountSelectionAdapter.getSelectedPosition();
        if (selectedPosition > -1) {
            binding.fragmentSelectAccountListSrc.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            binding.fragmentSelectAccountListSrc.scrollToPosition(selectedPosition);
                        }
                    });
        }
    }

    private void observeSelectedSrcAccountFromAdapter() {
        srcAccountSelectionAdapter
                .getSelectedAccountLiveData()
                .observe(
                        this,
                        new Observer<Account>() {
                            @Override
                            public void onChanged(Account account) {
                                viewModel.setSrcAccount(account);
                            }
                        });
    }

    private void setupDstAccountListUi() {
        int orientation = LinearLayout.HORIZONTAL;
        binding.fragmentSelectAccountListDst.setItemAnimator(null);
        binding.fragmentSelectAccountListDst.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentSelectAccountListDst.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentSelectAccountListDst.addItemDecoration(itemDecoration);
    }

    private void setupDstAccountAdapter() {
        dstAccountSelectionAdapter = new AccountSelectionAdapter();
        binding.fragmentSelectAccountListDst.setAdapter(dstAccountSelectionAdapter);
    }

    private void observeDstAccountListLiveData() {
        viewModel
                .getDstAccountListLiveData()
                .observe(
                        this,
                        new Observer<List<AccountSelection>>() {
                            @Override
                            public void onChanged(List<AccountSelection> accountList) {
                                dstAccountSelectionAdapter.submitList(
                                        accountList,
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollDstAccountListToSelectedPosition();
                                            }
                                        });
                            }
                        });
    }

    private void scrollDstAccountListToSelectedPosition() {
        int selectedPosition = dstAccountSelectionAdapter.getSelectedPosition();
        if (selectedPosition > -1) {
            binding.fragmentSelectAccountListDst.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            binding.fragmentSelectAccountListDst.scrollToPosition(selectedPosition);
                        }
                    });
        }
    }

    private void observeSelectedDstAccountFromAdapter() {
        dstAccountSelectionAdapter
                .getSelectedAccountLiveData()
                .observe(
                        this,
                        new Observer<Account>() {
                            @Override
                            public void onChanged(Account account) {
                                viewModel.setDstAccount(account);
                            }
                        });
    }

    private void listenForButtonAddClick() {
        binding.fragmentSelectAccountButtonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEditAccountBottomSheet();
                    }
                });
    }

    private void openEditAccountBottomSheet() {
        if (editAccountFragment != null && editAccountFragment.isAdded()) {
            return;
        }

        if (editAccountFragment == null) {
            editAccountFragment = new EditAccountFragment();
        }
        editAccountFragment.show(getChildFragmentManager(), null);
    }

    private void listenForBottomActionClick() {
        binding.fragmentSelectAccountBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        if (!validateConfirmButton()) {
                            return;
                        }
                        Bundle result = new Bundle();
                        result.putSerializable(
                                ConstantKeys.Transaction.SRC_ACCOUNT,
                                viewModel.getCurrentSrcAccount());
                        if (viewModel.isTransferTransaction()) {
                            result.putSerializable(
                                    ConstantKeys.Transaction.DST_ACCOUNT,
                                    viewModel.getCurrentDstAccount());
                        }
                        getParentFragmentManager()
                                .setFragmentResult(
                                        ConstantKeys.FragmentRequestKey.TRANSACTION_ACCOUNT,
                                        result);
                        dismiss();
                    }
                });
    }

    private boolean validateConfirmButton() {
        if (viewModel.getCurrentSrcAccount() == null) {
            showToast(R.string.error_transaction_empty_src_account);
            return false;
        }
        if (viewModel.isTransferTransaction()) {
            if (viewModel.getCurrentDstAccount() == null) {
                showToast(R.string.error_transaction_empty_dst_account);
                return false;
            } else if (Objects.equals(
                    viewModel.getCurrentSrcAccount().getId(),
                    viewModel.getCurrentDstAccount().getId())) {
                showToast(R.string.error_transaction_transfer_with_same_src_dst);
                return false;
            }
        }
        return true;
    }
}
