package io.tnmma.ezwallet.ui.screen.main.category.categorybytype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.DetailScreenType;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.base.fragment.BaseFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.data.model.SortOption;
import io.tnmma.ezwallet.data.pojo.CategoryWithDetails;
import io.tnmma.ezwallet.databinding.FragmentCategoryByTypeBinding;
import io.tnmma.ezwallet.ui.adapter.CategoryWithDetailsAdapter;

import java.util.List;

public class CategoryByTypeFragment
        extends BaseFragment<FragmentCategoryByTypeBinding, CategoryByTypeViewModel> {

    private static final String TAG = CategoryByTypeFragment.class.getSimpleName();

    private CategoryWithDetailsAdapter categoryAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category_by_type;
    }

    @Override
    protected Class<CategoryByTypeViewModel> getViewModelClass() {
        return CategoryByTypeViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        CategoryType categoryType = null;
        if (getArguments() != null) {
            categoryType = (CategoryType) getArguments().get(ConstantKeys.Category.TYPE);
        }
        return CategoryByTypeViewModel.getFactory(categoryType);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);
        setupCategoryListUi();
        setupCategoryAdapter();
        observeCategoryListLiveData();
    }

    private void setupCategoryListUi() {
        int orientation = LinearLayout.VERTICAL;
        binding.fragmentCategoryByTypeList.setItemAnimator(null);
        binding.fragmentCategoryByTypeList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentCategoryByTypeList.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_15)
                        .withColorResource(getContext(), R.color.transparent)
                        .withLastItemDecorated(true);
        binding.fragmentCategoryByTypeList.addItemDecoration(itemDecoration);
    }

    private void setupCategoryAdapter() {
        categoryAdapter = new CategoryWithDetailsAdapter();
        categoryAdapter.setOnClickListener(new CategoryWithDetailsAdapter.OnClickListener() {
            @Override
            public void onItemClick(CategoryWithDetails item) {
                Bundle data = new Bundle();
                data.putSerializable(
                        ConstantKeys.ModelType.CATEGORY, item);
                Intent intent = new Intent(ConstantKeys.IntentAction.OPEN_DETAIL_SCREEN);
                intent.putExtra(ConstantKeys.DetailScreen.TYPE, DetailScreenType.CATEGORY);
                intent.putExtra(ConstantKeys.DetailScreen.DATA, data);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
        });
        binding.fragmentCategoryByTypeList.setAdapter(categoryAdapter);
    }

    private void observeCategoryListLiveData() {
        viewModel
                .getCategoryListLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<List<CategoryWithDetails>>() {
                            @Override
                            public void onChanged(List<CategoryWithDetails> categories) {
                                categoryAdapter.submitList(
                                        categories,
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentCategoryByTypeList.scrollToPosition(
                                                        0);
                                            }
                                        });
                            }
                        });
    }

    public void setSortOption(SortType sortType, SortOrder sortOrder) {
        viewModel.setSortOption(sortType, sortOrder);
    }

    public SortOption getSortOption() {
        return viewModel.getSortOption();
    }
}
