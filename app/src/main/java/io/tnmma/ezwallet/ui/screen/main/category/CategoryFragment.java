package io.tnmma.ezwallet.ui.screen.main.category;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.SortObject;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.base.fragment.BaseFragment;
import io.tnmma.ezwallet.data.model.SortOption;
import io.tnmma.ezwallet.databinding.FragmentCategoryBinding;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selectcategorytype.SelectCategoryTypeFragment;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selectsortoption.SelectSortOptionFragment;
import io.tnmma.ezwallet.ui.screen.main.category.categorybytype.CategoryByTypeFragment;

import java.util.HashMap;
import java.util.Map;

public class CategoryFragment extends BaseFragment<FragmentCategoryBinding, CategoryViewModel> {

    private static final String TAG = CategoryFragment.class.getSimpleName();

    private Map<CategoryType, CategoryByTypeFragment> fragmentMap;

    private CategoryByTypeFragment currentFragment;

    private SelectSortOptionFragment selectSortOptionFragment;

    private SelectCategoryTypeFragment selectCategoryTypeFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected Class<CategoryViewModel> getViewModelClass() {
        return CategoryViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        fragmentMap = new HashMap<>();
        observeCategoryTypeLiveData();
        listenForButtonCategoryTypeClick();
        listenForCategoryTypeResult();
        listenForButtonSortOptionClick();
        listenForSortOptionResult();
        viewModel.onSelectCategory(CategoryType.INCOME);
    }

    private void listenForButtonSortOptionClick() {
        binding.fragmentCategoryButtonSortOption.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSelectSortOptionBottomSheet();
                    }
                });
    }

    private void openSelectSortOptionBottomSheet() {
        if (selectSortOptionFragment != null && selectSortOptionFragment.isAdded()) {
            return;
        }

        if (selectSortOptionFragment == null) {
            selectSortOptionFragment = new SelectSortOptionFragment();
        }
        Bundle sortOptionArguments = new Bundle();
        sortOptionArguments.putSerializable(ConstantKeys.SortOption.OBJECT, SortObject.CATEGORY);
        sortOptionArguments.putSerializable(
                ConstantKeys.SortOption.TYPE, viewModel.getSortOption().getSortType());
        sortOptionArguments.putSerializable(
                ConstantKeys.SortOption.ORDER, viewModel.getSortOption().getSortOrder());
        selectSortOptionFragment.setArguments(sortOptionArguments);
        selectSortOptionFragment.show(getChildFragmentManager(), null);
    }

    private void listenForButtonCategoryTypeClick() {
        binding.fragmentCategoryButtonCategoryType.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSelectCategoryTypeBottomSheet();
                    }
                });
    }

    private void openSelectCategoryTypeBottomSheet() {
        if (selectCategoryTypeFragment != null && selectCategoryTypeFragment.isAdded()) {
            return;
        }

        if (selectCategoryTypeFragment == null) {
            selectCategoryTypeFragment = new SelectCategoryTypeFragment();
        }
        Bundle arguments = new Bundle();
        arguments.putSerializable(
                ConstantKeys.Category.TYPE, viewModel.getCurrentCategory().getValue());
        selectCategoryTypeFragment.setArguments(arguments);
        selectCategoryTypeFragment.show(getChildFragmentManager(), null);
    }

    private void listenForSortOptionResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.SORT_OPTION,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                updateSortOption(result);
                            }
                        });
    }

    private void updateSortOption(Bundle result) {
        SortType sortType = (SortType) result.get(ConstantKeys.SortOption.TYPE);
        SortOrder sortOrder = (SortOrder) result.get(ConstantKeys.SortOption.ORDER);
        if (sortType != null && sortOrder != null) {
            if (currentFragment != null) {
                currentFragment.setSortOption(sortType, sortOrder);
                viewModel.setSortOption(sortType, sortOrder);
            }
        }
    }

    private void listenForCategoryTypeResult() {
        getChildFragmentManager()
                .setFragmentResultListener(
                        ConstantKeys.FragmentRequestKey.CATEGORY_TYPE,
                        this,
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(
                                    @NonNull String requestKey, @NonNull Bundle result) {
                                CategoryType categoryType =
                                        (CategoryType) result.get(ConstantKeys.Category.TYPE);
                                viewModel.onSelectCategory(categoryType);
                            }
                        });
    }

    private void observeCategoryTypeLiveData() {
        viewModel
                .getCurrentCategory()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<CategoryType>() {
                            @Override
                            public void onChanged(CategoryType categoryType) {
                                switchTab(categoryType);
                                SortOption sortOption = currentFragment.getSortOption();
                                if (sortOption != null) {
                                    viewModel.setSortOption(
                                            sortOption.getSortType(), sortOption.getSortOrder());
                                }
                            }
                        });
    }

    private void switchTab(CategoryType categoryType) {
        if (categoryType == null) {
            return;
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        CategoryByTypeFragment fragment = fragmentMap.get(categoryType);
        if (fragment == null) {
            fragment = getFragmentByCategoryType(categoryType);
            fragmentMap.put(categoryType, fragment);
            transaction.add(binding.fragmentCategoryContainer.getId(), fragment).commit();
            getChildFragmentManager().executePendingTransactions();
        } else {
            transaction.show(fragment).commit();
        }
        currentFragment = fragment;
    }

    private CategoryByTypeFragment getFragmentByCategoryType(CategoryType categoryType) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ConstantKeys.Category.TYPE, categoryType);
        CategoryByTypeFragment fragment = new CategoryByTypeFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
}
