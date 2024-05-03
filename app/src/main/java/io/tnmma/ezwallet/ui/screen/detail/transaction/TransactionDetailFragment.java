package io.tnmma.ezwallet.ui.screen.detail.transaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.fragment.BaseFragment;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.databinding.FragmentTransactionDetailBinding;
import io.tnmma.ezwallet.ui.customview.BottomActionView;
import io.tnmma.ezwallet.ui.screen.bottomsheet.confirmdeletion.ConfirmDeletionFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.editamount.EditAmountFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.editdescription.EditDescriptionFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selectaccount.SelectAccountFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selectcategory.SelectCategoryFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selecttransactiontype.SelectTransactionTypeFragment;

import java.time.LocalDate;

public class TransactionDetailFragment
        extends BaseFragment<FragmentTransactionDetailBinding, TransactionDetailViewModel> {

    private static final String TAG = TransactionDetailFragment.class.getSimpleName();

    private SelectTransactionTypeFragment selectTransactionTypeFragment;

    private EditAmountFragment editAmountFragment;

    private DatePickerDialog datePickerDialog;

    private SelectAccountFragment selectAccountFragment;

    private SelectCategoryFragment selectCategoryFragment;

    private EditDescriptionFragment editDescriptionFragment;

    private ConfirmDeletionFragment confirmDeletionFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_transaction_detail;
    }

    @Override
    protected Class<TransactionDetailViewModel> getViewModelClass() {
        return TransactionDetailViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        TransactionWithDetails transaction = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            transaction =
                    (TransactionWithDetails)
                            arguments.getSerializable(ConstantKeys.ModelType.TRANSACTION);
        }
        return TransactionDetailViewModel.getFactory(
                transaction != null ? transaction : new TransactionWithDetails());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setViewModel(viewModel);
        listenForButtonTypeClick();
        listenForTypeResult();
        listenForAmountClick();
        listenForAmountResult();
        listenForAccountClick();
        listenForAccountResult();
        listenForDateClick();
        listenForCategoryClick();
        listenForCategoryResult();
        listenForDescriptionClick();
        listenForDescriptionResult();
        listenForBottomActionClick();
        observeOnSaveEvent();
        listenForButtonDeleteClick();
        listenForConfirmDeletionResult();
        observeOnDeleteEvent();
    }

    private void listenForButtonTypeClick() {
        binding.fragmentTransactionDetailButtonType.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSelectTransactionTypeBottomSheet();
                    }
                });
    }

    private void openSelectTransactionTypeBottomSheet() {
        if (selectTransactionTypeFragment != null && selectTransactionTypeFragment.isAdded()) {
            return;
        }

        if (selectTransactionTypeFragment == null) {
            selectTransactionTypeFragment = new SelectTransactionTypeFragment();
        }
        Bundle arguments = new Bundle();
        arguments.putSerializable(ConstantKeys.Transaction.TYPE, viewModel.getTransactionType());
        selectTransactionTypeFragment.setArguments(arguments);
        selectTransactionTypeFragment.show(getChildFragmentManager(), null);
    }

    private void listenForTypeResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.TRANSACTION_TYPE,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                viewModel.setTransactionType(
                                        (TransactionType)
                                                result.getSerializable(
                                                        ConstantKeys.Transaction.TYPE));
                            }
                        });
    }

    private void listenForAmountClick() {
        binding.fragmentTransactionDetailAmount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEditAmountBottomSheet();
                    }
                });
    }

    private void openEditAmountBottomSheet() {
        if (editAmountFragment != null && editAmountFragment.isAdded()) {
            return;
        }

        if (editAmountFragment == null) {
            editAmountFragment = new EditAmountFragment();
        }
        Bundle arguments = new Bundle();
        arguments.putDouble(ConstantKeys.Transaction.AMOUNT, viewModel.getAmount());
        editAmountFragment.setArguments(arguments);
        editAmountFragment.show(getChildFragmentManager(), null);
    }

    private void listenForAmountResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.TRANSACTION_AMOUNT,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                if (result.containsKey(ConstantKeys.Transaction.AMOUNT)) {
                                    viewModel.setAmount(
                                            result.getDouble(ConstantKeys.Transaction.AMOUNT));
                                }
                            }
                        });
    }

    private void listenForAccountClick() {
        binding.fragmentTransactionDetailSrcAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSelectAccountBottomSheet();
                    }
                });
        binding.fragmentTransactionDetailDstAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSelectAccountBottomSheet();
                    }
                });
    }

    private void openSelectAccountBottomSheet() {
        if (selectAccountFragment != null && selectAccountFragment.isAdded()) {
            return;
        }

        if (selectAccountFragment == null) {
            selectAccountFragment = new SelectAccountFragment();
        }
        Bundle arguments = new Bundle();
        arguments.putSerializable(ConstantKeys.Transaction.TYPE, viewModel.getTransactionType());
        arguments.putSerializable(ConstantKeys.Transaction.SRC_ACCOUNT, viewModel.getSrcAccount());
        if (viewModel.getTransactionType() == TransactionType.TRANSFER) {
            arguments.putSerializable(
                    ConstantKeys.Transaction.DST_ACCOUNT, viewModel.getDstAccount());
        }
        selectAccountFragment.setArguments(arguments);
        selectAccountFragment.show(getChildFragmentManager(), null);
    }

    private void listenForAccountResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.TRANSACTION_ACCOUNT,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                viewModel.setSrcAccount(
                                        (Account)
                                                result.getSerializable(
                                                        ConstantKeys.Transaction.SRC_ACCOUNT));
                                if (viewModel.getTransactionType() == TransactionType.TRANSFER) {
                                    viewModel.setDstAccount(
                                            (Account)
                                                    result.getSerializable(
                                                            ConstantKeys.Transaction.DST_ACCOUNT));
                                }
                            }
                        });
    }

    private void listenForDateClick() {
        binding.fragmentTransactionDetailDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDatePickerDialog();
                    }
                });
    }

    private void openDatePickerDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(
                                DatePicker view, int year, int month, int dayOfMonth) {
                            viewModel
                                    .getTransactionDetailDate()
                                    .setDate(LocalDate.of(year, month + 1, dayOfMonth));
                        }
                    });
        }

        LocalDate currentTransactionDate = viewModel.getDate();
        if (currentTransactionDate != null) {
            datePickerDialog.updateDate(
                    currentTransactionDate.getYear(),
                    currentTransactionDate.getMonthValue() - 1,
                    currentTransactionDate.getDayOfMonth());
        }
        datePickerDialog.show();
    }

    private void listenForCategoryClick() {
        binding.fragmentTransactionDetailCategory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSelectCategoryBottomSheet();
                    }
                });
    }

    private void openSelectCategoryBottomSheet() {
        if (viewModel.getTransactionType() == TransactionType.TRANSFER) {
            return;
        }
        if (selectCategoryFragment != null && selectCategoryFragment.isAdded()) {
            return;
        }

        if (selectCategoryFragment == null) {
            selectCategoryFragment = new SelectCategoryFragment();
        }
        Category category = new Category();
        if (viewModel.getCategory() != null) {
            category = viewModel.getCategory();
        } else {
            category.setCategoryType(
                    viewModel.getTransactionType() == TransactionType.INCOME
                            ? CategoryType.INCOME
                            : CategoryType.EXPENSE);
        }
        Bundle arguments = new Bundle();
        arguments.putSerializable(ConstantKeys.ModelType.CATEGORY, category);
        selectCategoryFragment.setArguments(arguments);
        selectCategoryFragment.show(getChildFragmentManager(), null);
    }

    private void listenForCategoryResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.TRANSACTION_CATEGORY,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                viewModel.setCategory(
                                        (Category)
                                                result.getSerializable(
                                                        ConstantKeys.ModelType.CATEGORY));
                            }
                        });
    }

    private void listenForDescriptionClick() {
        binding.fragmentTransactionDetailDescription.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEditDescriptionBottomSheet();
                    }
                });
    }

    private void openEditDescriptionBottomSheet() {
        if (editDescriptionFragment != null && editDescriptionFragment.isAdded()) {
            return;
        }

        if (editDescriptionFragment == null) {
            editDescriptionFragment = new EditDescriptionFragment();
        }
        Bundle arguments = new Bundle();
        arguments.putString(ConstantKeys.Transaction.DESCRIPTION, viewModel.getDescription());
        editDescriptionFragment.setArguments(arguments);
        editDescriptionFragment.show(getChildFragmentManager(), null);
    }

    private void listenForDescriptionResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.TRANSACTION_DESCRIPTION,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                viewModel.setDescription(
                                        result.getString(ConstantKeys.Transaction.DESCRIPTION));
                            }
                        });
    }

    private void listenForBottomActionClick() {
        binding.fragmentTransactionDetailBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        closeScreen();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        viewModel.saveTransaction(
                                binding.fragmentTransactionDetailTitle.getText().toString());
                    }
                });
    }

    private void observeOnSaveEvent() {
        viewModel
                .getOnSaveEvent()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                if (integer == ResultCode.Transaction.SAVE_SUCCEEDED) {
                                    closeScreen();
                                    return;
                                }
                                showError(integer);
                            }
                        });
    }

    private void listenForButtonDeleteClick() {
        binding.fragmentTransactionDetailButtonDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (confirmDeletionFragment != null && confirmDeletionFragment.isAdded()) {
                            return;
                        }

                        if (confirmDeletionFragment == null) {
                            confirmDeletionFragment = new ConfirmDeletionFragment();
                        }
                        Bundle arguments = new Bundle();
                        arguments.putBoolean(ConstantKeys.ModelType.TRANSACTION, true);
                        confirmDeletionFragment.setArguments(arguments);
                        confirmDeletionFragment.show(getChildFragmentManager(), null);
                    }
                });
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
                                viewModel.deleteTransaction();
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
                                if (integer == ResultCode.Transaction.DELETE_SUCCEEDED) {
                                    closeScreen();
                                } else if (integer == ResultCode.Transaction.DELETE_FAILED) {
                                    showError(integer);
                                }
                            }
                        });
    }

    private void showError(int resultCode) {
        int messageResId = 0;
        switch (resultCode) {
            case ResultCode.Transaction.EMPTY_ID:
                messageResId = R.string.error_transaction_empty_id;
                break;
            case ResultCode.Transaction.EMPTY_TRANSACTION_TYPE:
                messageResId = R.string.error_transaction_empty_transaction_type;
                break;
            case ResultCode.Transaction.EMPTY_SRC_ACCOUNT:
                messageResId = R.string.error_transaction_empty_src_account;
                break;
            case ResultCode.Transaction.EMPTY_DST_ACCOUNT:
                messageResId = R.string.error_transaction_empty_dst_account;
                break;
            case ResultCode.Transaction.INCOME_EXPENSE_WITH_DST_ACCOUNT:
                messageResId = R.string.error_transaction_income_expense_with_dst_account;
                break;
            case ResultCode.Transaction.TRANSFER_WITH_SAME_SRC_DST:
                messageResId = R.string.error_transaction_transfer_with_same_src_dst;
                break;
            case ResultCode.Transaction.TRANSFER_WITH_CATEGORY:
                messageResId = R.string.error_transaction_transfer_with_category;
                break;
            case ResultCode.Transaction.INVALID_CATEGORY_TYPE:
                messageResId = R.string.error_transaction_invalid_category_type;
                break;
            case ResultCode.Transaction.ZERO_AMOUNT:
                messageResId = R.string.error_transaction_zero_amount;
                break;
            case ResultCode.Transaction.EMPTY_TITLE:
                messageResId = R.string.error_transaction_empty_title;
                break;
            case ResultCode.Transaction.EMPTY_DATE:
                messageResId = R.string.error_transaction_empty_date;
                break;
            case ResultCode.Transaction.EMPTY_CREATED_TIME:
                messageResId = R.string.error_transaction_empty_created_time;
                break;
            case ResultCode.Transaction.SAVE_FAILED:
                messageResId = R.string.error_transaction_save_failed;
                break;
            case ResultCode.Transaction.DELETE_FAILED:
                messageResId = R.string.error_transaction_delete_failed;
                break;
        }
        showToast(messageResId);
    }

    private void closeScreen() {
        LocalBroadcastManager.getInstance(getContext())
                .sendBroadcast(new Intent(ConstantKeys.IntentAction.CLOSE_DETAIL_SCREEN));
    }
}
