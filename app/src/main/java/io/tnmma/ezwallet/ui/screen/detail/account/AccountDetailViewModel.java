package io.tnmma.ezwallet.ui.screen.detail.account;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.base.constant.AmountType;
import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.DateType;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.event.SingleLiveEvent;
import io.tnmma.ezwallet.data.model.AccountIncomeExpenseInfo;
import io.tnmma.ezwallet.data.model.AmountTypeInfo;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.DateOption;
import io.tnmma.ezwallet.data.model.DateWithBalance;
import io.tnmma.ezwallet.data.model.DateWithTransactions;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.data.repository.AccountRepository;
import io.tnmma.ezwallet.data.repository.TransactionRepository;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountDetailViewModel extends ViewModel {

    private static final String TAG = AccountDetailViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final Account originalAccount;

    private final DateOption dateOption;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final LiveData<Account> infoLiveData;

    private final LiveData<AmountTypeInfo> balanceLiveData;

    private final MutableLiveData<AccountIncomeExpenseInfo> incomeExpenseLiveData;

    private final MutableLiveData<List<DateWithTransactions>> dateWithTransactionsListLiveData;

    private Disposable getTransactionsDisposable;

    private final SingleLiveEvent<Integer> onDeleteEvent;

    public static ViewModelProvider.Factory getFactory(Account account) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AccountDetailViewModel(account);
            }
        };
    }

    public AccountDetailViewModel(Account account) {
        bottomAction = new BottomAction(BottomActionType.EDIT);
        originalAccount = account;
        dateOption = new DateOption(DateType.ALL_TIME, null, null);
        accountRepository = new AccountRepository();
        transactionRepository = new TransactionRepository();

        infoLiveData = setupInfoLiveData();
        balanceLiveData = setupBalanceLiveData();
        incomeExpenseLiveData = new MutableLiveData<>();
        dateWithTransactionsListLiveData = new MutableLiveData<>();
        setupGetTransactionsDisposable();

        onDeleteEvent = new SingleLiveEvent<>();
    }

    private LiveData<Account> setupInfoLiveData() {
        return LiveDataReactiveStreams.fromPublisher(
                accountRepository.getAccountById(originalAccount.getId()));
    }

    private LiveData<AmountTypeInfo> setupBalanceLiveData() {
        return LiveDataReactiveStreams.fromPublisher(
                transactionRepository
                        .getAccountCurrentBalance(originalAccount.getId())
                        .map(
                                new Function<Double, AmountTypeInfo>() {
                                    @Override
                                    public AmountTypeInfo apply(Double aDouble) throws Throwable {
                                        return new AmountTypeInfo(AmountType.BALANCE, aDouble);
                                    }
                                }));
    }

    private void setupGetTransactionsDisposable() {
        if (getTransactionsDisposable != null) {
            getTransactionsDisposable.dispose();
        }
        getTransactionsDisposable =
                transactionRepository
                        .getTransactionsByAccountIdInDateRange(
                                originalAccount.getId(),
                                dateOption.getStartDate(),
                                dateOption.getEndDate())
                        .doOnNext(
                                new Consumer<List<TransactionWithDetails>>() {
                                    @Override
                                    public void accept(
                                            List<TransactionWithDetails> transactionWithDetails)
                                            throws Throwable {
                                        handleNewTransactionList(transactionWithDetails);
                                    }
                                })
                        .subscribe();
    }

    private void handleNewTransactionList(List<TransactionWithDetails> transactionList) {
        AccountIncomeExpenseInfo accountIncomeExpenseInfo = new AccountIncomeExpenseInfo();
        List<DateWithTransactions> dateWithTransactionsList = new ArrayList<>();
        DateWithBalance currentDate = null;
        for (TransactionWithDetails transaction : transactionList) {
            if (currentDate == null
                    || !Objects.equals(currentDate.getDate(), transaction.getDate())) {
                currentDate = new DateWithBalance(transaction.getDate());
                dateWithTransactionsList.add(new DateWithTransactions(currentDate));
            }
            currentDate.addTransactionAmount(
                    transaction.getTransactionType(), transaction.getAmount());
            dateWithTransactionsList.add(new DateWithTransactions(transaction));
            accountIncomeExpenseInfo.addTransactionAmount(
                    transaction.getTransactionType(), transaction.getAmount());
        }
        incomeExpenseLiveData.postValue(accountIncomeExpenseInfo);
        dateWithTransactionsListLiveData.postValue(dateWithTransactionsList);
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public DateOption getDateOption() {
        return dateOption;
    }

    public LiveData<Account> getInfoLiveData() {
        return infoLiveData;
    }

    public Account getAccount() {
        return infoLiveData.getValue();
    }

    public LiveData<AmountTypeInfo> getBalanceLiveData() {
        return balanceLiveData;
    }

    public LiveData<AccountIncomeExpenseInfo> getIncomeExpenseLiveData() {
        return incomeExpenseLiveData;
    }

    public LiveData<List<DateWithTransactions>> getDateWithTransactionsListLiveData() {
        return dateWithTransactionsListLiveData;
    }

    public LiveData<Integer> getOnDeleteEvent() {
        return onDeleteEvent;
    }

    public void setDate(DateType dateType, LocalDate startDate, LocalDate endDate) {
        if (dateType != null && dateOption.getDateType() != dateType) {
            dateOption.setDateType(dateType);
        }
        if (Objects.equals(dateOption.getStartDate(), startDate)
                && Objects.equals(dateOption.getEndDate(), endDate)) {
            return;
        }
        dateOption.setStartDate(startDate);
        dateOption.setEndDate(endDate);
        setupGetTransactionsDisposable();
    }

    public void deleteAccount() {
        accountRepository
                .deleteAccount(originalAccount.getId())
                .subscribe(
                        new SingleObserver<>() {
                            @Override
                            public void onSubscribe(
                                    @io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

                            @Override
                            public void onSuccess(
                                    @io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                                int resultCode =
                                        integer > 0
                                                ? ResultCode.Account.DELETE_SUCCEEDED
                                                : ResultCode.Account.DELETE_FAILED;
                                onDeleteEvent.postValue(resultCode);
                            }

                            @Override
                            public void onError(
                                    @io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Log.d(TAG, e.toString());
                                onDeleteEvent.postValue(ResultCode.Account.DELETE_FAILED);
                            }
                        });
    }
}
