package io.tnmma.ezwallet.ui.screen.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.tnmma.ezwallet.base.constant.AmountType;
import io.tnmma.ezwallet.base.constant.DateType;
import io.tnmma.ezwallet.data.model.AccountIncomeExpenseInfo;
import io.tnmma.ezwallet.data.model.AmountTypeInfo;
import io.tnmma.ezwallet.data.model.DateOption;
import io.tnmma.ezwallet.data.model.DateWithBalance;
import io.tnmma.ezwallet.data.model.DateWithTransactions;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.data.repository.TransactionRepository;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeViewModel extends ViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final DateOption dateOption;

    private final TransactionRepository transactionRepository;

    private final LiveData<AmountTypeInfo> currentBalanceLiveData;

    private final MutableLiveData<AccountIncomeExpenseInfo> incomeExpenseLiveData;

    private final MutableLiveData<List<DateWithTransactions>> dateWithTransactionsListLiveData;

    private Disposable getTransactionsDisposable;

    public HomeViewModel() {
        dateOption = new DateOption(DateType.ALL_TIME, null, null);
        transactionRepository = new TransactionRepository();
        currentBalanceLiveData = setupCurrentBalanceLiveData();
        incomeExpenseLiveData = new MutableLiveData<>();
        dateWithTransactionsListLiveData = new MutableLiveData<>();
        setupGetTransactionsDisposable();
    }

    private LiveData<AmountTypeInfo> setupCurrentBalanceLiveData() {
        return LiveDataReactiveStreams.fromPublisher(
                transactionRepository
                        .getCurrentBalance()
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
                        .getTransactionsInDateRange(
                                dateOption.getStartDate(), dateOption.getEndDate())
                        .doOnNext(
                                new Consumer<List<TransactionWithDetails>>() {
                                    @Override
                                    public void accept(
                                            List<TransactionWithDetails> transactionWithDetails) {
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

    public DateOption getDateOption() {
        return dateOption;
    }

    public LiveData<AmountTypeInfo> getCurrentBalanceLiveData() {
        return currentBalanceLiveData;
    }

    public LiveData<AccountIncomeExpenseInfo> getIncomeExpenseLiveData() {
        return incomeExpenseLiveData;
    }

    public LiveData<List<DateWithTransactions>> getDateWithTransactionsListLiveData() {
        return dateWithTransactionsListLiveData;
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
}
