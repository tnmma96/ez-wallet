package io.tnmma.ezwallet.ui.screen.detail.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ConcatAdapter;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.DateType;
import io.tnmma.ezwallet.base.constant.DetailScreenType;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.fragment.BaseFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.data.model.AccountIncomeExpenseInfo;
import io.tnmma.ezwallet.data.model.AmountTypeInfo;
import io.tnmma.ezwallet.data.model.DateWithTransactions;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.databinding.FragmentAccountDetailBinding;
import io.tnmma.ezwallet.ui.adapter.AccountIncomeExpenseInfoAdapter;
import io.tnmma.ezwallet.ui.adapter.AccountInfoAdapter;
import io.tnmma.ezwallet.ui.adapter.AmountTypeInfoAdapter;
import io.tnmma.ezwallet.ui.adapter.DateWithTransactionsAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;
import io.tnmma.ezwallet.ui.screen.bottomsheet.confirmdeletion.ConfirmDeletionFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.editaccount.EditAccountFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selectdate.SelectDateFragment;

import java.time.LocalDate;
import java.util.List;

public class AccountDetailFragment
        extends BaseFragment<FragmentAccountDetailBinding, AccountDetailViewModel> {

    private static final String TAG = AccountDetailFragment.class.getSimpleName();

    private ConcatAdapter concatAdapter;

    private AccountInfoAdapter infoAdapter;

    private AmountTypeInfoAdapter balanceAdapter;

    private AccountIncomeExpenseInfoAdapter incomeExpenseAdapter;

    private DateWithTransactionsAdapter dateWithTransactionsAdapter;

    private SelectDateFragment selectDateFragment;

    private EditAccountFragment editAccountFragment;

    private ConfirmDeletionFragment confirmDeletionFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account_detail;
    }

    @Override
    protected Class<AccountDetailViewModel> getViewModelClass() {
        return AccountDetailViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        Account account = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            account = (Account) arguments.getSerializable(ConstantKeys.ModelType.ACCOUNT);
        }
        return AccountDetailViewModel.getFactory(account != null ? account : new Account());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        setupConcatListUi();
        setupConcatAdapter();
        observeAccountInfoLiveData();
        observeBalanceLiveData();
        observeIncomeExpenseLiveData();
        observeTransactionListLiveData();
        listenForButtonDateOptionClick();
        listenForDateOptionResult();
        listenForButtonDeleteClick();
        listenForConfirmDeletionResult();
        listenForBottomActionClick();
        observeOnDeleteEvent();
    }

    private void setupConcatListUi() {
        int orientation = LinearLayout.VERTICAL;
        binding.fragmentAccountDetailList.setItemAnimator(null);
        binding.fragmentAccountDetailList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentAccountDetailList.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_15)
                        .withColorResource(getContext(), R.color.transparent)
                        .withLastItemDecorated(true);
        binding.fragmentAccountDetailList.addItemDecoration(itemDecoration);
    }

    private void setupConcatAdapter() {
        setupAccountInfoAdapter();
        setupBalanceAdapter();
        setupIncomeExpenseAdapter();
        setupDateWithTransactionsAdapter();

        concatAdapter = new ConcatAdapter();
        concatAdapter.addAdapter(infoAdapter);
        concatAdapter.addAdapter(balanceAdapter);
        concatAdapter.addAdapter(incomeExpenseAdapter);
        concatAdapter.addAdapter(dateWithTransactionsAdapter);
        binding.fragmentAccountDetailList.setAdapter(concatAdapter);
    }

    private void setupAccountInfoAdapter() {
        infoAdapter = new AccountInfoAdapter();
    }

    private void setupBalanceAdapter() {
        balanceAdapter = new AmountTypeInfoAdapter();
    }

    private void setupIncomeExpenseAdapter() {
        incomeExpenseAdapter = new AccountIncomeExpenseInfoAdapter();
        incomeExpenseAdapter.setOnClickListener(
                new AccountIncomeExpenseInfoAdapter.OnClickListener() {
                    @Override
                    public void onButtonAddClick(TransactionType transactionType) {
                        TransactionWithDetails transaction = new TransactionWithDetails();
                        transaction.setTransactionType(transactionType);
                        transaction.setSrcAccount(viewModel.getAccount());
                        openTransactionDetailScreen(transaction);
                    }
                });
    }

    private void setupDateWithTransactionsAdapter() {
        dateWithTransactionsAdapter = new DateWithTransactionsAdapter(false);
        dateWithTransactionsAdapter.setOnTransactionClickListener(
                new DateWithTransactionsAdapter.OnClickListener() {
                    @Override
                    public void onTransactionClick(TransactionWithDetails transaction) {
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

    private void observeAccountInfoLiveData() {
        viewModel
                .getInfoLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<Account>() {
                            @Override
                            public void onChanged(Account account) {
                                infoAdapter.submitList(
                                        List.of(account),
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentAccountDetailList.scrollToPosition(
                                                        0);
                                            }
                                        });
                            }
                        });
    }

    private void observeBalanceLiveData() {
        viewModel
                .getBalanceLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<AmountTypeInfo>() {
                            @Override
                            public void onChanged(AmountTypeInfo amountTypeInfo) {
                                balanceAdapter.submitList(
                                        List.of(amountTypeInfo),
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentAccountDetailList.scrollToPosition(
                                                        0);
                                            }
                                        });
                            }
                        });
    }

    private void observeIncomeExpenseLiveData() {
        viewModel
                .getIncomeExpenseLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<AccountIncomeExpenseInfo>() {
                            @Override
                            public void onChanged(
                                    AccountIncomeExpenseInfo accountIncomeExpenseInfo) {
                                incomeExpenseAdapter.submitList(
                                        List.of(accountIncomeExpenseInfo),
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentAccountDetailList.scrollToPosition(
                                                        0);
                                            }
                                        });
                            }
                        });
    }

    private void observeTransactionListLiveData() {
        viewModel
                .getDateWithTransactionsListLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<List<DateWithTransactions>>() {
                            @Override
                            public void onChanged(List<DateWithTransactions> dateWithTransactions) {
                                dateWithTransactionsAdapter.submitList(
                                        dateWithTransactions,
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentAccountDetailList.scrollToPosition(
                                                        0);
                                            }
                                        });
                            }
                        });
    }

    private void listenForButtonDateOptionClick() {
        binding.fragmentAccountDetailButtonDateOption.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSelectDateBottomSheet();
                    }
                });
    }

    private void openSelectDateBottomSheet() {
        if (selectDateFragment != null && selectDateFragment.isAdded()) {
            return;
        }

        if (selectDateFragment == null) {
            selectDateFragment = new SelectDateFragment();
        }
        Bundle arguments = new Bundle();
        arguments.putSerializable(
                ConstantKeys.DateOption.TYPE, viewModel.getDateOption().getDateType());
        arguments.putSerializable(
                ConstantKeys.DateOption.START_DATE, viewModel.getDateOption().getStartDate());
        arguments.putSerializable(
                ConstantKeys.DateOption.END_DATE, viewModel.getDateOption().getEndDate());
        selectDateFragment.setArguments(arguments);
        selectDateFragment.show(getChildFragmentManager(), null);
    }

    private void listenForDateOptionResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.DATE_OPTION,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                DateType dateType =
                                        (DateType) result.get(ConstantKeys.DateOption.TYPE);
                                LocalDate startDate =
                                        (LocalDate) result.get(ConstantKeys.DateOption.START_DATE);
                                LocalDate endDate =
                                        (LocalDate) result.get(ConstantKeys.DateOption.END_DATE);
                                viewModel.setDate(dateType, startDate, endDate);
                            }
                        });
    }

    private void listenForBottomActionClick() {
        binding.fragmentAccountDetailBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        closeScreen();
                    }

                    @Override
                    public void onActionConfirmClick() {
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
        Bundle arguments = new Bundle();
        arguments.putSerializable(ConstantKeys.ModelType.ACCOUNT, viewModel.getAccount());
        arguments.putSerializable(
                ConstantKeys.Account.CURRENT_BALANCE,
                viewModel.getBalanceLiveData().getValue().getAmount());
        editAccountFragment.setArguments(arguments);
        editAccountFragment.show(getChildFragmentManager(), null);
    }

    private void listenForButtonDeleteClick() {
        binding.fragmentAccountDetailButtonDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openConfirmDeletionBottomSheet();
                    }
                });
    }

    private void openConfirmDeletionBottomSheet() {
        if (confirmDeletionFragment != null && confirmDeletionFragment.isAdded()) {
            return;
        }

        if (confirmDeletionFragment == null) {
            confirmDeletionFragment = new ConfirmDeletionFragment();
        }
        Bundle arguments = new Bundle();
        arguments.putBoolean(ConstantKeys.ModelType.ACCOUNT, true);
        confirmDeletionFragment.setArguments(arguments);
        confirmDeletionFragment.show(getChildFragmentManager(), null);
    }

    private void listenForConfirmDeletionResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.CONFIRM_DELETION,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                viewModel.deleteAccount();
                            }
                        });
    }

    private void observeOnDeleteEvent() {
        viewModel
                .getOnDeleteEvent()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                if (integer == ResultCode.Account.DELETE_SUCCEEDED) {
                                    closeScreen();
                                } else if (integer == ResultCode.Account.DELETE_FAILED) {
                                    showToast(R.string.error_account_delete_failed);
                                }
                            }
                        });
    }

    private void closeScreen() {
        LocalBroadcastManager.getInstance(getContext())
                .sendBroadcast(new Intent(ConstantKeys.IntentAction.CLOSE_DETAIL_SCREEN));
    }
}
