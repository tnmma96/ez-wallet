package io.tnmma.ezwallet.ui.screen.main.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.DetailScreenType;
import io.tnmma.ezwallet.base.constant.SortObject;
import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.base.fragment.BaseFragment;
import io.tnmma.ezwallet.base.recyclerview.CustomLinearLayoutManager;
import io.tnmma.ezwallet.base.recyclerview.CustomMaterialDividerItemDecoration;
import io.tnmma.ezwallet.data.pojo.AccountWithDetails;
import io.tnmma.ezwallet.databinding.FragmentAccountBinding;
import io.tnmma.ezwallet.ui.adapter.AccountWithDetailsAdapter;
import io.tnmma.ezwallet.ui.screen.bottomsheet.selectsortoption.SelectSortOptionFragment;

import java.util.List;

public class AccountFragment extends BaseFragment<FragmentAccountBinding, AccountViewModel> {

    private static final String TAG = AccountFragment.class.getSimpleName();

    private AccountWithDetailsAdapter accountAdapter;

    private SelectSortOptionFragment selectSortOptionFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    @Override
    protected Class<AccountViewModel> getViewModelClass() {
        return AccountViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(viewModel);

        setupAccountListUi();
        setupAccountListAdapter();
        observeAccountListLiveData();
        listenForButtonSortOptionClick();
        listenForSortOptionResult();
    }

    private void setupAccountListUi() {
        int orientation = LinearLayout.VERTICAL;
        binding.fragmentAccountList.setItemAnimator(null);
        binding.fragmentAccountList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.fragmentAccountList.setLayoutManager(
                new CustomLinearLayoutManager(getContext(), orientation, false));

        CustomMaterialDividerItemDecoration itemDecoration =
                new CustomMaterialDividerItemDecoration(getContext(), orientation)
                        .withThicknessResource(getContext(), R.dimen.recycler_view_divider_15)
                        .withColorResource(getContext(), R.color.transparent)
                        .withLastItemDecorated(true);
        binding.fragmentAccountList.addItemDecoration(itemDecoration);
    }

    private void setupAccountListAdapter() {
        accountAdapter = new AccountWithDetailsAdapter();
        accountAdapter.setOnClickListener(
                new AccountWithDetailsAdapter.OnClickListener() {
                    @Override
                    public void onItemClick(AccountWithDetails item) {
                        Bundle data = new Bundle();
                        data.putSerializable(ConstantKeys.ModelType.ACCOUNT, item);
                        Intent intent = new Intent(ConstantKeys.IntentAction.OPEN_DETAIL_SCREEN);
                        intent.putExtra(ConstantKeys.DetailScreen.TYPE, DetailScreenType.ACCOUNT);
                        intent.putExtra(ConstantKeys.DetailScreen.DATA, data);
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                    }
                });
        binding.fragmentAccountList.setAdapter(accountAdapter);
    }

    private void observeAccountListLiveData() {
        viewModel
                .getAccountListLiveData()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<List<AccountWithDetails>>() {
                            @Override
                            public void onChanged(List<AccountWithDetails> accounts) {
                                accountAdapter.submitList(
                                        accounts,
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.fragmentAccountList.scrollToPosition(0);
                                            }
                                        });
                            }
                        });
    }

    private void listenForButtonSortOptionClick() {
        binding.fragmentAccountButtonSortOption.setOnClickListener(
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
        Bundle arguments = new Bundle();
        arguments.putSerializable(ConstantKeys.SortOption.OBJECT, SortObject.ACCOUNT);
        arguments.putSerializable(
                ConstantKeys.SortOption.TYPE, viewModel.getSortOption().getSortType());
        arguments.putSerializable(
                ConstantKeys.SortOption.ORDER, viewModel.getSortOption().getSortOrder());
        selectSortOptionFragment.setArguments(arguments);
        selectSortOptionFragment.show(getChildFragmentManager(), null);
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
            viewModel.setSortOption(sortType, sortOrder);
        }
    }
}
