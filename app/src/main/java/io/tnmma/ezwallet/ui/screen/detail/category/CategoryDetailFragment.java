package io.tnmma.ezwallet.ui.screen.detail.category;

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
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.DateType;
import io.tnmma.ezwallet.base.constant.DetailScreenType;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.fragment.BaseFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.data.model.AmountTypeInfo;
import io.tnmma.ezwallet.data.model.CategoryAmountInfo;
import io.tnmma.ezwallet.data.model.DateWithTransactions;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.databinding.FragmentCategoryDetailBinding;
import io.tnmma.ezwallet.ui.adapter.AmountTypeInfoAdapter;
import io.tnmma.ezwallet.ui.adapter.CategoryAmountInfoAdapter;
import io.tnmma.ezwallet.ui.adapter.CategoryInfoAdapter;
import io.tnmma.ezwallet.ui.adapter.DateWithTransactionsAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;
import io.tnmma.ezwallet.ui.screen.bottomsheet.confirmdeletion.ConfirmDeletionFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.editcategory.EditCategoryFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selectdate.SelectDateFragment;

import java.time.LocalDate;
import java.util.List;

public class CategoryDetailFragment
        extends BaseFragment<FragmentCategoryDetailBinding, CategoryDetailViewModel> {

    private static final String TAG = CategoryDetailFragment.class.getSimpleName();

    private ConcatAdapter concatAdapter;

    private CategoryInfoAdapter infoAdapter;

    private AmountTypeInfoAdapter totalAmountAdapter;

    private CategoryAmountInfoAdapter amountAdapter;

    private DateWithTransactionsAdapter dateWithTransactionsAdapter;

    private SelectDateFragment selectDateFragment;

    private EditCategoryFragment editCategoryFragment;

    private ConfirmDeletionFragment confirmDeletionFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category_detail;
    }

    @Override
    protected Class<CategoryDetailViewModel> getViewModelClass() {
        return CategoryDetailViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        Category category = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            category = (Category) arguments.get(ConstantKeys.ModelType.CATEGORY);
        }
        return CategoryDetailViewModel.getFactory(category != null ? category : new Category());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setViewModel(viewModel);
        setupConcatListUi();
        setupConcatAdapter();
        observeCategoryLiveData();
        observeAmountTypeInfoLiveData();
        observeCategoryAmountInfoLiveData();
        observeTransactionListLiveData();
        listenForButtonDateOptionClick();
        listenForDateOptionResult();
        listenForBottomActionClick();
        listenForButtonDeleteClick();
        listenForConfirmDeletionResult();
        observeOnDeleteEvent();
    }

    private void setupConcatListUi() {
        int orientation = LinearLayout.VERTICAL;
        binding.fragmentCategoryDetailList.setItemAnimator(null);
        binding.fragmentCategoryDetailList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentCategoryDetailList.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_15)
                        .withColorResource(getContext(), R.color.transparent)
                        .withLastItemDecorated(true);
        binding.fragmentCategoryDetailList.addItemDecoration(itemDecoration);
    }

    private void setupConcatAdapter() {
        setupCategoryInfoAdapter();
        setupAmountTypeInfoAdapter();
        setupCategoryAmountAdapter();
        setupDateWithTransactionsAdapter();

        concatAdapter = new ConcatAdapter();
        concatAdapter.addAdapter(infoAdapter);
        concatAdapter.addAdapter(totalAmountAdapter);
        concatAdapter.addAdapter(amountAdapter);
        concatAdapter.addAdapter(dateWithTransactionsAdapter);
        binding.fragmentCategoryDetailList.setAdapter(concatAdapter);
    }

    private void setupCategoryInfoAdapter() {
        infoAdapter = new CategoryInfoAdapter();
    }

    private void setupAmountTypeInfoAdapter() {
        totalAmountAdapter = new AmountTypeInfoAdapter();
    }

    private void setupCategoryAmountAdapter() {
        amountAdapter = new CategoryAmountInfoAdapter();
        amountAdapter.setOnClickListener(
                new CategoryAmountInfoAdapter.OnClickListener() {
                    @Override
                    public void onButtonAddClick() {
                        TransactionWithDetails transaction = new TransactionWithDetails();
                        transaction.setCategory(viewModel.getCategory());
                        transaction.setTransactionType(
                                viewModel.getCategory().getCategoryType() == CategoryType.INCOME
                                        ? TransactionType.INCOME
                                        : TransactionType.EXPENSE);
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

    private void observeCategoryLiveData() {
        viewModel
                .getInfoLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<Category>() {
                            @Override
                            public void onChanged(Category category) {
                                infoAdapter.submitList(
                                        List.of(category),
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentCategoryDetailList.scrollToPosition(
                                                        0);
                                            }
                                        });
                            }
                        });
    }

    private void observeAmountTypeInfoLiveData() {
        viewModel
                .getTotalAmountLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<AmountTypeInfo>() {
                            @Override
                            public void onChanged(AmountTypeInfo amountTypeInfo) {
                                totalAmountAdapter.submitList(
                                        List.of(amountTypeInfo),
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentCategoryDetailList.scrollToPosition(
                                                        0);
                                            }
                                        });
                            }
                        });
    }

    private void observeCategoryAmountInfoLiveData() {
        viewModel
                .getAmountLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<CategoryAmountInfo>() {
                            @Override
                            public void onChanged(CategoryAmountInfo categoryAmountInfo) {
                                amountAdapter.submitList(
                                        List.of(categoryAmountInfo),
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentCategoryDetailList.scrollToPosition(
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
                                                binding.fragmentCategoryDetailList.scrollToPosition(
                                                        0);
                                            }
                                        });
                            }
                        });
    }

    private void listenForButtonDateOptionClick() {
        binding.fragmentCategoryDetailButtonDateOption.setOnClickListener(
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
        binding.fragmentCategoryDetailBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        closeScreen();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        openEditCategoryBottomSheet();
                    }
                });
    }

    private void openEditCategoryBottomSheet() {
        if (editCategoryFragment != null && editCategoryFragment.isAdded()) {
            return;
        }

        if (editCategoryFragment == null) {
            editCategoryFragment = new EditCategoryFragment();
        }
        Bundle arguments = new Bundle();
        arguments.putSerializable(ConstantKeys.ModelType.CATEGORY, viewModel.getCategory());
        editCategoryFragment.setArguments(arguments);
        editCategoryFragment.show(getChildFragmentManager(), null);
    }

    private void listenForButtonDeleteClick() {
        binding.fragmentCategoryDetailButtonDelete.setOnClickListener(
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
        arguments.putBoolean(ConstantKeys.ModelType.CATEGORY, true);
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
                                viewModel.deleteCategory();
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
                                if (integer == ResultCode.Category.DELETE_SUCCEEDED) {
                                    closeScreen();
                                } else if (integer == ResultCode.Category.DELETE_FAILED) {
                                    showToast(R.string.error_category_delete_failed);
                                }
                            }
                        });
    }

    private void closeScreen() {
        LocalBroadcastManager.getInstance(getContext())
                .sendBroadcast(new Intent(ConstantKeys.IntentAction.CLOSE_DETAIL_SCREEN));
    }
}
