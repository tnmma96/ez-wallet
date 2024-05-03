package io.tnmma.ezwallet.ui.screen.bottomsheet.editdescription;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.databinding.FragmentEditDescriptionBinding;
import io.tnmma.ezwallet.ui.customview.BottomActionView;

public class EditDescriptionFragment
        extends BaseBottomSheetFragment<FragmentEditDescriptionBinding, EditDescriptionViewModel> {

    private static final String TAG = EditDescriptionFragment.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_description;
    }

    @Override
    protected Class<EditDescriptionViewModel> getViewModelClass() {
        return EditDescriptionViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        return EditDescriptionViewModel.getFactory(
                getArguments() != null
                        ? getArguments().getString(ConstantKeys.Transaction.DESCRIPTION)
                        : null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMaxHeightPercent(90);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        listenForBottomActionClick();
    }

    private void listenForBottomActionClick() {
        binding.fragmentEditDescriptionBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        Bundle result = new Bundle();
                        result.putString(
                                ConstantKeys.Transaction.DESCRIPTION,
                                binding.fragmentEditDescriptionContent.getText().toString());
                        getParentFragmentManager()
                                .setFragmentResult(
                                        ConstantKeys.FragmentRequestKey.TRANSACTION_DESCRIPTION,
                                        result);
                        dismiss();
                    }
                });
    }
}
