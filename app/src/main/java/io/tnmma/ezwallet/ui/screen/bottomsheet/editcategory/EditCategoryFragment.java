package io.tnmma.ezwallet.ui.screen.bottomsheet.editcategory;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.databinding.FragmentEditCategoryBinding;
import io.tnmma.ezwallet.ui.adapter.CategoryTypeAdapter;
import io.tnmma.ezwallet.ui.adapter.ColorAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;

public class EditCategoryFragment
        extends BaseBottomSheetFragment<FragmentEditCategoryBinding, EditCategoryViewModel> {

    private static final String TAG = EditCategoryFragment.class.getSimpleName();

    private CategoryTypeAdapter categoryTypeAdapter;

    private ColorAdapter colorAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_category;
    }

    @Override
    protected Class<EditCategoryViewModel> getViewModelClass() {
        return EditCategoryViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        Category category = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            category = (Category) arguments.getSerializable(ConstantKeys.ModelType.CATEGORY);
        }
        return EditCategoryViewModel.getFactory(category != null ? category : new Category());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        setupSoftInputMode();
        setupColorListUi();
        setupColorAdapter();
        observeSelectedColorFromAdapter();
        if (viewModel.showCategoryTypeList()) {
            setupCategoryTypeListUi();
            setupCategoryTypeAdapter();
        }
        listenForBottomActionClick();
        observeOnSaveEvent();
    }

    private void setupSoftInputMode() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void setupColorListUi() {
        int orientation = LinearLayout.HORIZONTAL;
        binding.fragmentEditCategoryListColor.setItemAnimator(null);
        binding.fragmentEditCategoryListColor.setHasFixedSize(true);
        binding.fragmentEditCategoryListColor.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentEditCategoryListColor.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentEditCategoryListColor.addItemDecoration(itemDecoration);
    }

    private void setupColorAdapter() {
        colorAdapter = new ColorAdapter();
        binding.fragmentEditCategoryListColor.setAdapter(colorAdapter);
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
            binding.fragmentEditCategoryListColor.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            ((CustomLinearLayoutManager)
                                            binding.fragmentEditCategoryListColor
                                                    .getLayoutManager())
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

    private void setupCategoryTypeListUi() {
        int orientation = LinearLayout.VERTICAL;
        binding.fragmentEditCategoryListType.setItemAnimator(null);
        binding.fragmentEditCategoryListType.setHasFixedSize(true);
        binding.fragmentEditCategoryListType.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentEditCategoryListType.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent);
        binding.fragmentEditCategoryListType.addItemDecoration(itemDecoration);
    }

    private void setupCategoryTypeAdapter() {
        categoryTypeAdapter = new CategoryTypeAdapter();
        binding.fragmentEditCategoryListType.setAdapter(categoryTypeAdapter);
        categoryTypeAdapter.submitList(viewModel.getCategoryTypeList());
    }

    private void listenForBottomActionClick() {
        binding.fragmentEditCategoryBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        viewModel.saveCategory(
                                binding.fragmentEditCategoryName.getText().toString(),
                                colorAdapter.getSelectedItem().getColor(),
                                viewModel.showCategoryTypeList()
                                        ? categoryTypeAdapter.getSelectedItem().getCategoryType()
                                        : null);
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
                                if (integer == ResultCode.Category.SAVE_SUCCEEDED) {
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
            case ResultCode.Category.EMPTY_ID:
                messageResId = R.string.error_category_empty_id;
                break;
            case ResultCode.Category.EMPTY_NAME:
                messageResId = R.string.error_category_empty_name;
                break;
            case ResultCode.Category.EMPTY_CATEGORY_TYPE:
                messageResId = R.string.error_category_empty_category_type;
                break;
            case ResultCode.Category.INVALID_COLOR:
                messageResId = R.string.error_category_invalid_color;
                break;
            case ResultCode.Category.EMPTY_CREATED_TIME:
                messageResId = R.string.error_category_empty_created_time;
                break;
            case ResultCode.Category.SAVE_FAILED:
                messageResId = R.string.error_category_save_failed;
                break;
        }
        showToast(messageResId);
    }
}
