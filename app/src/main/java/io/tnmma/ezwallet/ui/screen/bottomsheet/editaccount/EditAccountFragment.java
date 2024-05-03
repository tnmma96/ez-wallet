package io.tnmma.ezwallet.ui.screen.bottomsheet.editaccount;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.databinding.FragmentEditAccountBinding;
import io.tnmma.ezwallet.ui.adapter.ColorAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;
import io.tnmma.ezwallet.ui.screen.bottomsheet.editamount.EditAmountFragment;

public class EditAccountFragment
        extends BaseBottomSheetFragment<FragmentEditAccountBinding, EditAccountViewModel> {

    private static final String TAG = EditAccountFragment.class.getSimpleName();

    private ColorAdapter colorAdapter;

    private EditAmountFragment editAmountFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_account;
    }

    @Override
    protected Class<EditAccountViewModel> getViewModelClass() {
        return EditAccountViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        Account account = null;
        double currentBalance = 0;
        Bundle arguments = getArguments();
        if (arguments != null) {
            account = (Account) arguments.getSerializable(ConstantKeys.ModelType.ACCOUNT);
            currentBalance = arguments.getDouble(ConstantKeys.Account.CURRENT_BALANCE);
        }
        return EditAccountViewModel.getFactory(
                account != null ? account : new Account(), currentBalance);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        setupSoftInputMode();
        setupColorListUi();
        setupColorAdapter();
        observeSelectedColorFromAdapter();
        listenForCurrentBalanceClick();
        listenForCurrentBalanceResult();
        listenForBottomActionClick();
        observeOnSaveEvent();
    }

    private void setupSoftInputMode() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void setupColorListUi() {
        int orientation = LinearLayout.HORIZONTAL;
        binding.fragmentEditAccountListColor.setItemAnimator(null);
        binding.fragmentEditAccountListColor.setHasFixedSize(true);
        binding.fragmentEditAccountListColor.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentEditAccountListColor.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentEditAccountListColor.addItemDecoration(itemDecoration);
    }

    private void setupColorAdapter() {
        colorAdapter = new ColorAdapter();
        binding.fragmentEditAccountListColor.setAdapter(colorAdapter);
        colorAdapter.submitList(
                viewModel.getColorList(),
                new Runnable() {
                    @Override
                    public void run() {
                        scrollColorListToSelectedPosition();
                    }
                });
    }

    private void scrollColorListToSelectedPosition() {
        int selectedPosition = colorAdapter.getSelectedPosition();
        if (selectedPosition > -1) {
            binding.fragmentEditAccountListColor.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            ((CustomLinearLayoutManager)
                                            binding.fragmentEditAccountListColor.getLayoutManager())
                                    .smoothScrollAndCenter(selectedPosition);
                        }
                    });
        }
    }

    private void observeSelectedColorFromAdapter() {
        colorAdapter
                .getSelectedColorLiveData()
                .observe(
                        this,
                        new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                viewModel.setBackgroundColor(integer);
                            }
                        });
    }

    private void listenForCurrentBalanceClick() {
        binding.fragmentEditAccountFormattedBalance.setOnClickListener(
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
        arguments.putDouble(ConstantKeys.Account.CURRENT_BALANCE, viewModel.getCurrentBalance());
        editAmountFragment.setArguments(arguments);
        editAmountFragment.show(getChildFragmentManager(), null);
    }

    private void listenForCurrentBalanceResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.ACCOUNT_CURRENT_BALANCE,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                viewModel.setCurrentBalance(
                                        result.getDouble(ConstantKeys.Account.CURRENT_BALANCE));
                            }
                        });
    }

    private void listenForBottomActionClick() {
        binding.fragmentEditAccountBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        viewModel.saveAccount(
                                binding.fragmentEditAccountName.getText().toString(),
                                colorAdapter.getSelectedItem().getColor());
                    }
                });
    }

    private void observeOnSaveEvent() {
        viewModel
                .getOnSaveEvent()
                .observe(
                        this,
                        new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                if (integer == ResultCode.Account.SAVE_SUCCEEDED) {
                                    dismiss();
                                    return;
                                }
                                showError(integer);
                            }
                        });
    }

    private void showError(int resultCode) {
        int messageResId = 0;
        switch (resultCode) {
            case ResultCode.Account.EMPTY_ID:
                messageResId = R.string.error_account_empty_id;
                break;
            case ResultCode.Account.EMPTY_NAME:
                messageResId = R.string.error_account_empty_name;
                break;
            case ResultCode.Account.INVALID_COLOR:
                messageResId = R.string.error_account_invalid_color;
                break;
            case ResultCode.Account.EMPTY_CREATED_TIME:
                messageResId = R.string.error_account_empty_created_time;
                break;
            case ResultCode.Account.SAVE_FAILED:
                messageResId = R.string.error_account_save_failed;
                break;
        }
        showToast(messageResId);
    }
}
