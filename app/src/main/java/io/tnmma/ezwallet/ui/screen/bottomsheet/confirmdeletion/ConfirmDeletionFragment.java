package io.tnmma.ezwallet.ui.screen.bottomsheet.confirmdeletion;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.ModelType;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.databinding.FragmentConfirmDeletionBinding;
import io.tnmma.ezwallet.ui.customview.BottomActionView;

public class ConfirmDeletionFragment
        extends BaseBottomSheetFragment<FragmentConfirmDeletionBinding, ConfirmDeletionViewModel> {

    private static final String TAG = ConfirmDeletionFragment.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirm_deletion;
    }

    @Override
    protected Class<ConfirmDeletionViewModel> getViewModelClass() {
        return ConfirmDeletionViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        ModelType modelType = null;
        Bundle arguments = getArguments();
        if (arguments == null) {
            return ConfirmDeletionViewModel.getFactory(null);
        }

        if (arguments.containsKey(ConstantKeys.ModelType.ACCOUNT)) {
            modelType = ModelType.ACCOUNT;
        } else if (arguments.containsKey(ConstantKeys.ModelType.CATEGORY)) {
            modelType = ModelType.CATEGORY;
        } else if (arguments.containsKey(ConstantKeys.ModelType.TRANSACTION)) {
            modelType = ModelType.TRANSACTION;
        }
        return ConfirmDeletionViewModel.getFactory(modelType);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        listenForBottomActionClick();
    }

    private void listenForBottomActionClick() {
        binding.fragmentConfirmDeletionBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        getParentFragmentManager()
                                .setFragmentResult(
                                        ConstantKeys.FragmentRequestKey.CONFIRM_DELETION,
                                        new Bundle());
                        dismiss();
                    }
                });
    }
}
