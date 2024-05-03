package io.tnmma.ezwallet.ui.screen.bottomsheet.editamount;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.AmountType;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.databinding.FragmentEditAmountBinding;
import io.tnmma.ezwallet.ui.customview.AmountKeyboardView;
import io.tnmma.ezwallet.ui.customview.BottomActionView;

public class EditAmountFragment
        extends BaseBottomSheetFragment<FragmentEditAmountBinding, EditAmountViewModel> {

    private static final String TAG = EditAmountFragment.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_amount;
    }

    @Override
    protected Class<EditAmountViewModel> getViewModelClass() {
        return EditAmountViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        double amount = 0.0;
        AmountType amountType = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(ConstantKeys.Account.CURRENT_BALANCE)) {
                amount = arguments.getDouble(ConstantKeys.Account.CURRENT_BALANCE);
                amountType = AmountType.BALANCE;
            } else if (arguments.containsKey(ConstantKeys.Transaction.AMOUNT)) {
                amount = arguments.getDouble(ConstantKeys.Transaction.AMOUNT);
                amountType = AmountType.AMOUNT;
            }
        }
        return EditAmountViewModel.getFactory(amountType, amount);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        listenForKeyboardClick();
        listenForBottomActionClick();
    }

    private void listenForKeyboardClick() {
        binding.fragmentEditAmountKeyboard.setOnKeyClickListener(
                new AmountKeyboardView.OnKeyClickListener() {
                    @Override
                    public void onKeyClick(int keyCode) {
                        viewModel.onKeyPressed(keyCode);
                    }
                });
    }

    private void listenForBottomActionClick() {
        binding.fragmentEditAmountBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        String requestKey =
                                viewModel.getAmountType() == AmountType.BALANCE
                                        ? ConstantKeys.FragmentRequestKey.ACCOUNT_CURRENT_BALANCE
                                        : ConstantKeys.FragmentRequestKey.TRANSACTION_AMOUNT;
                        Bundle result = new Bundle();
                        result.putDouble(
                                viewModel.getAmountType() == AmountType.BALANCE
                                        ? ConstantKeys.Account.CURRENT_BALANCE
                                        : ConstantKeys.Transaction.AMOUNT,
                                viewModel.getAmount());
                        getParentFragmentManager().setFragmentResult(requestKey, result);
                        dismiss();
                    }
                });
    }
}
