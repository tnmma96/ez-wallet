package io.tnmma.ezwallet.ui.screen.detail.category;

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
import io.tnmma.ezwallet.data.model.AmountTypeInfo;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.CategoryAmountInfo;
import io.tnmma.ezwallet.data.model.DateOption;
import io.tnmma.ezwallet.data.model.DateWithBalance;
import io.tnmma.ezwallet.data.model.DateWithTransactions;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.data.repository.CategoryRepository;
import io.tnmma.ezwallet.data.repository.TransactionRepository;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryDetailViewModel extends ViewModel {

    private static final String TAG = CategoryDetailViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final Category originalCategory;

    private final DateOption dateOption;

    private final CategoryRepository categoryRepository;

    private final TransactionRepository transactionRepository;

    private final LiveData<Category> infoLiveData;

    private final LiveData<AmountTypeInfo> totalAmountLiveData;

    private final MutableLiveData<CategoryAmountInfo> amountLiveData;

    private final MutableLiveData<List<DateWithTransactions>> dateWithTransactionsListLiveData;

    private Disposable getTransactionsDisposable;

    private final SingleLiveEvent<Integer> onDeleteEvent;

    public static ViewModelProvider.Factory getFactory(Category category) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryDetailViewModel(category);
            }
        };
    }

    public CategoryDetailViewModel(Category category) {
        bottomAction = new BottomAction(BottomActionType.EDIT);
        originalCategory = category;
        dateOption = new DateOption(DateType.ALL_TIME, null, null);
        categoryRepository = new CategoryRepository();
        transactionRepository = new TransactionRepository();

        infoLiveData = setupCategoryInfoLiveData();
        totalAmountLiveData = setupTotalAmountLiveData();
        amountLiveData = new MutableLiveData<>();
        dateWithTransactionsListLiveData = new MutableLiveData<>();
        setupGetTransactionsDisposable();

        onDeleteEvent = new SingleLiveEvent<>();
    }

    private LiveData<Category> setupCategoryInfoLiveData() {
        return LiveDataReactiveStreams.fromPublisher(
                categoryRepository.getCategoryById(originalCategory.getId()));
    }

    private LiveData<AmountTypeInfo> setupTotalAmountLiveData() {
        return LiveDataReactiveStreams.fromPublisher(
                transactionRepository
                        .getCategoryTotalAmount(originalCategory.getId())
                        .map(
                                new Function<Double, AmountTypeInfo>() {
                                    @Override
                                    public AmountTypeInfo apply(Double aDouble) throws Throwable {
                                        return new AmountTypeInfo(AmountType.AMOUNT, aDouble);
                                    }
                                }));
    }

    private void setupGetTransactionsDisposable() {
        if (getTransactionsDisposable != null) {
            getTransactionsDisposable.dispose();
        }
        getTransactionsDisposable =
                transactionRepository
                        .getTransactionsByCategoryIdInDateRange(
                                originalCategory.getId(),
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
        CategoryAmountInfo categoryAmountInfo =
                new CategoryAmountInfo(originalCategory.getCategoryType());
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
            categoryAmountInfo.addTransactionAmount(transaction.getAmount());
        }
        amountLiveData.postValue(categoryAmountInfo);
        dateWithTransactionsListLiveData.postValue(dateWithTransactionsList);
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public DateOption getDateOption() {
        return dateOption;
    }

    public LiveData<Category> getInfoLiveData() {
        return infoLiveData;
    }

    public Category getCategory() {
        return infoLiveData.getValue();
    }

    public LiveData<AmountTypeInfo> getTotalAmountLiveData() {
        return totalAmountLiveData;
    }

    public LiveData<CategoryAmountInfo> getAmountLiveData() {
        return amountLiveData;
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

    public void deleteCategory() {
        categoryRepository
                .deleteCategory(originalCategory.getId())
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
                                                ? ResultCode.Category.DELETE_SUCCEEDED
                                                : ResultCode.Category.DELETE_FAILED;
                                onDeleteEvent.postValue(resultCode);
                            }

                            @Override
                            public void onError(
                                    @io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Log.d(TAG, e.toString());
                                onDeleteEvent.postValue(ResultCode.Category.DELETE_FAILED);
                            }
                        });
    }
}
