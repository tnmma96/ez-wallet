package io.tnmma.ezwallet.ui.screen.main.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import io.tnmma.ezwallet.base.constant.SortOrder;
import io.tnmma.ezwallet.base.constant.SortType;
import io.tnmma.ezwallet.data.model.SortOption;
import io.tnmma.ezwallet.data.pojo.AccountWithDetails;
import io.tnmma.ezwallet.data.repository.AccountRepository;

import io.reactivex.rxjava3.functions.Consumer;

import java.util.List;
import java.util.stream.Collectors;

public class AccountViewModel extends ViewModel {

    private static final String TAG = AccountViewModel.class.getSimpleName();

    private final SortOption sortOption;

    private final AccountRepository accountRepository;

    private final MediatorLiveData<List<AccountWithDetails>> accountListLiveData;

    public AccountViewModel() {
        sortOption = new SortOption(SortType.CREATED_TIME, SortOrder.DESCENDING);
        accountRepository = new AccountRepository();
        accountListLiveData = new MediatorLiveData<>();
        setupAccountListLiveData();
    }

    private void setupAccountListLiveData() {
        accountListLiveData.addSource(
                getAccountListFromRepository(),
                new Observer<List<AccountWithDetails>>() {
                    @Override
                    public void onChanged(List<AccountWithDetails> accountWithDetails) {
                        accountListLiveData.setValue(accountWithDetails);
                    }
                });
    }

    private LiveData<List<AccountWithDetails>> getAccountListFromRepository() {
        return LiveDataReactiveStreams.fromPublisher(
                accountRepository
                        .getAllAccountsWithDetails()
                        .doOnNext(
                                new Consumer<List<AccountWithDetails>>() {
                                    @Override
                                    public void accept(
                                            List<AccountWithDetails> accountWithDetails) {
                                        sortOnNewAccountList(accountWithDetails);
                                    }
                                }));
    }

    private void sortOnNewAccountList(List<AccountWithDetails> accountList) {
        if (sortOption.getSortType() == SortType.CREATED_TIME
                && sortOption.getSortOrder() == SortOrder.DESCENDING) {
            return;
        }
        accountList.sort(
                AccountWithDetails.getAccountComparator(
                        sortOption.getSortType(), sortOption.getSortOrder()));
    }

    public SortOption getSortOption() {
        return sortOption;
    }

    public LiveData<List<AccountWithDetails>> getAccountListLiveData() {
        return accountListLiveData;
    }

    public void setSortOption(SortType sortType, SortOrder sortOrder) {
        if (sortOption.getSortType() == sortType && sortOption.getSortOrder() == sortOrder) {
            return;
        }

        sortOption.setSortType(sortType);
        sortOption.setSortOrder(sortOrder);
        sortOnNewSortOption(sortType, sortOrder);
    }

    private void sortOnNewSortOption(SortType sortType, SortOrder sortOrder) {
        new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                List<AccountWithDetails> newList =
                                        accountListLiveData.getValue().stream()
                                                .collect(Collectors.toList());
                                newList.sort(
                                        AccountWithDetails.getAccountComparator(
                                                sortType, sortOrder));
                                accountListLiveData.postValue(newList);
                            }
                        })
                .start();
    }
}
