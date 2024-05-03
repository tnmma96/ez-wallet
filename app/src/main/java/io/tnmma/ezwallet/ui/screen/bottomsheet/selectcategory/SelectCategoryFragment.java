package io.tnmma.ezwallet.ui.screen.bottomsheet.selectcategory;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.fragment.BaseBottomSheetFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.CategorySelection;
import io.tnmma.ezwallet.databinding.FragmentSelectCategoryBinding;
import io.tnmma.ezwallet.ui.adapter.CategorySelectionAdapter;
import io.tnmma.ezwallet.ui.customview.BottomActionView;
import io.tnmma.ezwallet.ui.screen.bottomsheet.editcategory.EditCategoryFragment;

import java.util.List;

public class SelectCategoryFragment
        extends BaseBottomSheetFragment<FragmentSelectCategoryBinding, SelectCategoryViewModel> {

    private static final String TAG = SelectCategoryFragment.class.getSimpleName();

    private CategorySelectionAdapter categorySelectionAdapter;

    private EditCategoryFragment editCategoryFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_category;
    }

    @Override
    protected Class<SelectCategoryViewModel> getViewModelClass() {
        return SelectCategoryViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        Category originalCategory = new Category();
        Bundle arguments = getArguments();
        if (arguments != null) {
            originalCategory = (Category) arguments.get(ConstantKeys.ModelType.CATEGORY);
        }
        return SelectCategoryViewModel.getFactory(originalCategory);
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

        setupSoftInputMode();
        setupCategoryListUi();
        setupCategoryAdapter();
        observeCategoryListLiveData();
        observeSelectedCategoryFromAdapter();
        listenForButtonAddClick();
        listenForBottomActionClick();
    }

    // Set soft input mode to adjustNothing so this fragment won't be affected by
    // the soft input mode in EditAccountFragment
    private void setupSoftInputMode() {
        getDialog()
                .getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void setupCategoryListUi() {
        binding.fragmentSelectCategoryList.setMinimumHeight(getActivityWindowHeight() * 30 / 100);
        binding.fragmentSelectCategoryList.setItemAnimator(null);
        binding.fragmentSelectCategoryList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentSelectCategoryList.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(
                                getContext(), MaterialDividerItemDecoration.VERTICAL)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_10)
                        .withColorResource(getContext(), R.color.transparent)
                        .withLastItemDecorated(false);
        binding.fragmentSelectCategoryList.addItemDecoration(itemDecoration);
    }

    private void setupCategoryAdapter() {
        categorySelectionAdapter = new CategorySelectionAdapter();
        binding.fragmentSelectCategoryList.setAdapter(categorySelectionAdapter);
    }

    private void observeCategoryListLiveData() {
        viewModel
                .getCategoryListLiveData()
                .observe(
                        this,
                        new Observer<List<CategorySelection>>() {
                            @Override
                            public void onChanged(List<CategorySelection> categories) {
                                categorySelectionAdapter.submitList(
                                        categories,
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollToSelectedPosition();
                                            }
                                        });
                            }
                        });
    }

    private void scrollToSelectedPosition() {
        int selectedPosition = categorySelectionAdapter.getSelectedPosition();
        if (selectedPosition > -1) {
            binding.fragmentSelectCategoryList.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            ((CustomLinearLayoutManager)
                                            binding.fragmentSelectCategoryList.getLayoutManager())
                                    .smoothScrollAndCenter(selectedPosition);
                        }
                    });
        }
    }

    private void observeSelectedCategoryFromAdapter() {
        categorySelectionAdapter
                .getSelectedCategoryLiveData()
                .observe(
                        this,
                        new Observer<Category>() {
                            @Override
                            public void onChanged(Category category) {
                                viewModel.setCurrentCategory(category);
                            }
                        });
    }

    private void listenForButtonAddClick() {
        binding.fragmentSelectCategoryButtonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
        Category category = new Category();
        category.setCategoryType(viewModel.getCategoryType());
        Bundle arguments = new Bundle();
        arguments.putSerializable(ConstantKeys.ModelType.CATEGORY, category);
        editCategoryFragment.setArguments(arguments);
        editCategoryFragment.show(getChildFragmentManager(), null);
    }

    private void listenForBottomActionClick() {
        binding.fragmentSelectCategoryBottomAction.setOnActionClickListener(
                new BottomActionView.OnActionClickListener() {
                    @Override
                    public void onActionCancelClick() {
                        dismiss();
                    }

                    @Override
                    public void onActionConfirmClick() {
                        Bundle result = new Bundle();
                        result.putSerializable(
                                ConstantKeys.ModelType.CATEGORY, viewModel.getCurrentCategory());
                        getParentFragmentManager()
                                .setFragmentResult(
                                        ConstantKeys.FragmentRequestKey.TRANSACTION_CATEGORY,
                                        result);
                        dismiss();
                    }
                });
    }
}
