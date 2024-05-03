package io.tnmma.ezwallet.ui.screen.main.home;

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
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.fragment.BaseFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.data.model.AccountIncomeExpenseInfo;
import io.tnmma.ezwallet.data.model.AmountTypeInfo;
import io.tnmma.ezwallet.data.model.DateWithTransactions;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.databinding.FragmentHomeBinding;
import io.tnmma.ezwallet.ui.adapter.AccountIncomeExpenseInfoAdapter;
import io.tnmma.ezwallet.ui.adapter.AmountTypeInfoAdapter;
import io.tnmma.ezwallet.ui.adapter.DateWithTransactionsAdapter;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selectdate.SelectDateFragment;

import java.time.LocalDate;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private ConcatAdapter concatAdapter;

    private AmountTypeInfoAdapter currentBalanceAdapter;

    private AccountIncomeExpenseInfoAdapter incomeExpenseAdapter;

    private DateWithTransactionsAdapter dateWithTransactionsAdapter;

    private SelectDateFragment selectDateFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected Class<HomeViewModel> getViewModelClass() {
        return HomeViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);
        setupConcatListUi();
        setupConcatAdapter();
        observeCurrentBalanceLiveData();
        observeDateWithTransactionsLiveData();
        observeAccountIncomeExpenseLiveData();
        listenForButtonDateOptionClick();
        listenForDateOptionResult();
    }

    private void setupConcatListUi() {
        int orientation = LinearLayout.VERTICAL;
        binding.fragmentHomeList.setItemAnimator(null);
        binding.fragmentHomeList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentHomeList.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_15)
                        .withColorResource(getContext(), R.color.transparent)
                        .withLastItemDecorated(true);
        binding.fragmentHomeList.addItemDecoration(itemDecoration);
    }

    private void setupConcatAdapter() {
        setupBalanceAdapter();
        setupIncomeExpenseAdapter();
        setupDateWithTransactionsAdapter();

        concatAdapter = new ConcatAdapter();
        concatAdapter.addAdapter(currentBalanceAdapter);
        concatAdapter.addAdapter(incomeExpenseAdapter);
        concatAdapter.addAdapter(dateWithTransactionsAdapter);
        binding.fragmentHomeList.setAdapter(concatAdapter);
    }

    private void setupBalanceAdapter() {
        currentBalanceAdapter = new AmountTypeInfoAdapter();
    }

    private void setupIncomeExpenseAdapter() {
        incomeExpenseAdapter = new AccountIncomeExpenseInfoAdapter();
        incomeExpenseAdapter.setOnClickListener(
                new AccountIncomeExpenseInfoAdapter.OnClickListener() {
                    @Override
                    public void onButtonAddClick(TransactionType transactionType) {
                        TransactionWithDetails transaction = new TransactionWithDetails();
                        transaction.setTransactionType(transactionType);
                        Bundle data = new Bundle();
                        data.putSerializable(ConstantKeys.ModelType.TRANSACTION, transaction);
                        openDetailScreen(DetailScreenType.TRANSACTION, data);
                    }
                });
    }

    private void setupDateWithTransactionsAdapter() {
        dateWithTransactionsAdapter = new DateWithTransactionsAdapter(true);
        dateWithTransactionsAdapter.setOnTransactionClickListener(
                new DateWithTransactionsAdapter.OnClickListener() {
                    @Override
                    public void onTransactionClick(TransactionWithDetails transaction) {
                        Bundle data = new Bundle();
                        data.putSerializable(ConstantKeys.ModelType.TRANSACTION, transaction);
                        openDetailScreen(DetailScreenType.TRANSACTION, data);
                    }

                    @Override
                    public void onAccountClick(Account account) {
                        Bundle data = new Bundle();
                        data.putSerializable(ConstantKeys.ModelType.ACCOUNT, account);
                        openDetailScreen(DetailScreenType.ACCOUNT, data);
                    }

                    @Override
                    public void onCategoryClick(Category category) {
                        Bundle data = new Bundle();
                        data.putSerializable(ConstantKeys.ModelType.CATEGORY, category);
                        openDetailScreen(DetailScreenType.CATEGORY, data);
                    }
                });
    }

    private void openDetailScreen(DetailScreenType screenType, Bundle data) {
        Intent intent = new Intent(ConstantKeys.IntentAction.OPEN_DETAIL_SCREEN);
        intent.putExtra(ConstantKeys.DetailScreen.TYPE, screenType);
        intent.putExtra(ConstantKeys.DetailScreen.DATA, data);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    private void observeCurrentBalanceLiveData() {
        viewModel
                .getCurrentBalanceLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<AmountTypeInfo>() {
                            @Override
                            public void onChanged(AmountTypeInfo amountTypeInfo) {
                                currentBalanceAdapter.submitList(
                                        List.of(amountTypeInfo),
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentHomeList.scrollToPosition(0);
                                            }
                                        });
                            }
                        });
    }

    private void observeAccountIncomeExpenseLiveData() {
        viewModel
                .getIncomeExpenseLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<AccountIncomeExpenseInfo>() {
                            @Override
                            public void onChanged(
                                    AccountIncomeExpenseInfo accountIncomeExpenseInfo) {
                                incomeExpenseAdapter.submitList(
                                        List.of(accountIncomeExpenseInfo));
                            }
                        });
    }

    private void observeDateWithTransactionsLiveData() {
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
                                                binding.fragmentHomeList.scrollToPosition(0);
                                            }
                                        });
                            }
                        });
    }

    private void listenForButtonDateOptionClick() {
        binding.fragmentHomeButtonDateOption.setOnClickListener(
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
                                updateDateOption(result);
                            }
                        });
    }

    private void updateDateOption(Bundle result) {
        DateType dateType = (DateType) result.get(ConstantKeys.DateOption.TYPE);
        LocalDate startDate = (LocalDate) result.get(ConstantKeys.DateOption.START_DATE);
        LocalDate endDate = (LocalDate) result.get(ConstantKeys.DateOption.END_DATE);
        viewModel.setDate(dateType, startDate, endDate);
    }
}
