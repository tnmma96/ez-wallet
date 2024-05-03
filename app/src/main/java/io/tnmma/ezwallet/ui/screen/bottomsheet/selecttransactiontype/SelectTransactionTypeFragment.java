package io.tnmma.ezwallet.ui.screen.bottomsheet.selecttransactiontype;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.databinding.FragmentSelectTransactionTypeBinding;
import io.tnmma.ezwallet.ui.adapter.TransactionTypeAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;

public class SelectTransactionTypeFragment
        extends BaseBottomSheetFragment<
                FragmentSelectTransactionTypeBinding, SelectTransactionTypeViewModel> {

    private static final String TAG = SelectTransactionTypeFragment.class.getSimpleName();

    private TransactionTypeAdapter transactionTypeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_transaction_type;
    }

    @Override
    protected Class<SelectTransactionTypeViewModel> getViewModelClass() {
        return SelectTransactionTypeViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        TransactionType transactionType = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            transactionType = (TransactionType) arguments.get(ConstantKeys.Transaction.TYPE);
        }
        return SelectTransactionTypeViewModel.getFactory(transactionType);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        setupTransactionTypeListUi();
        setupTransactionTypeAdapter();
        listenForBottomActionClick();
    }

    private void setupTransactionTypeListUi() {
        int orientation = LinearLayout.VERTICAL;
        binding.fragmentSelectTransactionTypeList.setItemAnimator(null);
        binding.fragmentSelectTransactionTypeList.setHasFixedSize(true);
        binding.fragmentSelectTransactionTypeList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentSelectTransactionTypeList.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentSelectTransactionTypeList.addItemDecoration(itemDecoration);
    }

    private void setupTransactionTypeAdapter() {
        transactionTypeAdapter = new TransactionTypeAdapter();
        binding.fragmentSelectTransactionTypeList.setAdapter(transactionTypeAdapter);
        transactionTypeAdapter.submitList(viewModel.getTransactionTypeList());
    }

    private void listenForBottomActionClick() {
        binding.fragmentSelectTransactionTypeBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        Bundle result = new Bundle();
                        result.putSerializable(
                                ConstantKeys.Transaction.TYPE,
                                transactionTypeAdapter.getSelectedItem().getTransactionType());
                        getParentFragmentManager()
                                .setFragmentResult(
                                        ConstantKeys.FragmentRequestKey.TRANSACTION_TYPE, result);
                        dismiss();
                    }
                });
    }
}
