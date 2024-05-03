package io.tnmma.ezwallet.ui.screen.bottomsheet.selectcategorytype;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.databinding.FragmentSelectCategoryTypeBinding;
import io.tnmma.ezwallet.ui.adapter.CategoryTypeAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;

public class SelectCategoryTypeFragment
        extends BaseBottomSheetFragment<
                FragmentSelectCategoryTypeBinding, SelectCategoryTypeViewModel> {

    private static final String TAG = SelectCategoryTypeFragment.class.getSimpleName();

    private CategoryTypeAdapter categoryTypeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_category_type;
    }

    @Override
    protected Class<SelectCategoryTypeViewModel> getViewModelClass() {
        return SelectCategoryTypeViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        Bundle arguments = getArguments();
        CategoryType categoryType = null;
        if (arguments != null) {
            categoryType = (CategoryType) arguments.get(ConstantKeys.Category.TYPE);
        }
        return SelectCategoryTypeViewModel.getFactory(categoryType);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        setupCategoryTypeListUi();
        setupCategoryTypeAdapter();
        listenForBottomActionClick();
    }

    private void setupCategoryTypeListUi() {
        int orientation = LinearLayout.VERTICAL;
        binding.fragmentSelectCategoryTypeList.setItemAnimator(null);
        binding.fragmentSelectCategoryTypeList.setHasFixedSize(true);
        binding.fragmentSelectCategoryTypeList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentSelectCategoryTypeList.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentSelectCategoryTypeList.addItemDecoration(itemDecoration);
    }

    private void setupCategoryTypeAdapter() {
        categoryTypeAdapter = new CategoryTypeAdapter();
        binding.fragmentSelectCategoryTypeList.setAdapter(categoryTypeAdapter);
        categoryTypeAdapter.submitList(viewModel.getCategoryTypeList());
    }

    private void listenForBottomActionClick() {
        binding.fragmentSelectCategoryTypeBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        Bundle result = new Bundle();
                        result.putSerializable(
                                ConstantKeys.Category.TYPE,
                                categoryTypeAdapter.getSelectedItem().getCategoryType());
                        getParentFragmentManager()
                                .setFragmentResult(
                                        ConstantKeys.FragmentRequestKey.CATEGORY_TYPE, result);
                        dismiss();
                    }
                });
    }
}
