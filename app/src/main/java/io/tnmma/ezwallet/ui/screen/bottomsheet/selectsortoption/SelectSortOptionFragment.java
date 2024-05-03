package io.tnmma.ezwallet.ui.screen.bottomsheet.selectsortoption;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.SortObject;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.databinding.FragmentSelectSortOptionBinding;
import io.tnmma.ezwallet.ui.adapter.SortOrderAdapter;
import io.tnmma.ezwallet.ui.adapter.SortTypeAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;

public class SelectSortOptionFragment
        extends BaseBottomSheetFragment<
                FragmentSelectSortOptionBinding, SelectSortOptionViewModel> {

    private static final String TAG = SelectSortOptionFragment.class.getSimpleName();

    private SortTypeAdapter sortTypeAdapter;

    private SortOrderAdapter sortOrderAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_sort_option;
    }

    @Override
    protected Class<SelectSortOptionViewModel> getViewModelClass() {
        return SelectSortOptionViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        SortObject sortObject = null;
        SortType sortType = null;
        SortOrder sortOrder = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            sortObject = (SortObject) arguments.get(ConstantKeys.SortOption.OBJECT);
            sortType = (SortType) arguments.get(ConstantKeys.SortOption.TYPE);
            sortOrder = (SortOrder) arguments.get(ConstantKeys.SortOption.ORDER);
        }
        return SelectSortOptionViewModel.getFactory(sortObject, sortType, sortOrder);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        setupSortTypeListUi();
        setupSortTypeAdapter();
        setupSortOrderListUi();
        setupSortOrderAdapter();
        listenForBottomActionClick();
    }

    private void setupSortTypeListUi() {
        int orientation = LinearLayout.VERTICAL;
        binding.fragmentSelectSortOptionListType.setItemAnimator(null);
        binding.fragmentSelectSortOptionListType.setHasFixedSize(true);
        binding.fragmentSelectSortOptionListType.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentSelectSortOptionListType.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentSelectSortOptionListType.addItemDecoration(itemDecoration);
    }

    private void setupSortTypeAdapter() {
        sortTypeAdapter = new SortTypeAdapter();
        binding.fragmentSelectSortOptionListType.setAdapter(sortTypeAdapter);
        sortTypeAdapter.submitList(viewModel.getSortTypeList());
    }

    private void setupSortOrderListUi() {
        int orientation = LinearLayout.HORIZONTAL;
        binding.fragmentSelectSortOptionListOrder.setItemAnimator(null);
        binding.fragmentSelectSortOptionListOrder.setHasFixedSize(true);
        binding.fragmentSelectSortOptionListOrder.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentSelectSortOptionListOrder.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_15)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentSelectSortOptionListOrder.addItemDecoration(itemDecoration);
    }

    private void setupSortOrderAdapter() {
        sortOrderAdapter = new SortOrderAdapter();
        binding.fragmentSelectSortOptionListOrder.setAdapter(sortOrderAdapter);
        sortOrderAdapter.submitList(viewModel.getSortOrderList());
    }

    private void listenForBottomActionClick() {
        binding.fragmentSelectSortOptionBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        Bundle sortOptionBundle = new Bundle();
                        sortOptionBundle.putSerializable(
                                ConstantKeys.SortOption.TYPE,
                                sortTypeAdapter.getSelectedItem().getSortType());
                        sortOptionBundle.putSerializable(
                                ConstantKeys.SortOption.ORDER,
                                sortOrderAdapter.getSelectedItem().getSortOrder());
                        getParentFragmentManager()
                                .setFragmentResult(
                                        ConstantKeys.FragmentRequestKey.SORT_OPTION,
                                        sortOptionBundle);
                        dismiss();
                    }
                });
    }
}
