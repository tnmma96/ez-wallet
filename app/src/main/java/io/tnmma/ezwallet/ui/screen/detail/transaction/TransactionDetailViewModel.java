package io.tnmma.ezwallet.ui.screen.detail.transaction;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.event.SingleLiveEvent;
import io.tnmma.ezwallet.data.db.entity.TransactionEntity;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.TransactionDetailAccount;
import io.tnmma.ezwallet.data.model.TransactionDetailCategory;
import io.tnmma.ezwallet.data.model.TransactionDetailDate;
import io.tnmma.ezwallet.data.model.TransactionDetailDescription;
import io.tnmma.ezwallet.data.model.TransactionDetailType;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.data.repository.TransactionRepository;
import io.tnmma.ezwallet.util.AmountUtil;
import io.tnmma.ezwallet.util.DateTimeUtil;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

import kotlin.jvm.functions.Function1;

import java.time.LocalDate;
import java.util.UUID;

public class TransactionDetailViewModel extends ViewModel {

    private static final String TAG = TransactionDetailViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final TransactionWithDetails originalTransaction;

    private final TransactionRepository transactionRepository;

    private final TransactionDetailType transactionType;

    private final MutableLiveData<Double> amountLiveData;

    private final LiveData<String> formattedAmountLiveData;

    private final TransactionDetailAccount transactionSrcAccount;

    private final TransactionDetailAccount transactionDstAccount;

    private final TransactionDetailDate transactionDate;

    private final TransactionDetailCategory transactionCategory;

    private final TransactionDetailDescription transactionDescription;

    private final TransactionEntity newTransaction;

    private final SingleLiveEvent<Integer> onSaveEvent;

    private final SingleLiveEvent<Integer> onDeleteEvent;

    public static ViewModelProvider.Factory getFactory(TransactionWithDetails transaction) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TransactionDetailViewModel(transaction);
            }
        };
    }

    public TransactionDetailViewModel(TransactionWithDetails transaction) {
        bottomAction = new BottomAction(BottomActionType.SAVE);
        originalTransaction = transaction;
        transactionRepository = new TransactionRepository();

        transactionType = new TransactionDetailType(originalTransaction.getTransactionType());
        amountLiveData = new MutableLiveData<>(originalTransaction.getAmount());
        formattedAmountLiveData = setupFormattedAmountLiveData();
        transactionSrcAccount = new TransactionDetailAccount(originalTransaction.getSrcAccount());
        transactionDstAccount = new TransactionDetailAccount(originalTransaction.getDstAccount());
        transactionDate =
                new TransactionDetailDate(
                        originalTransaction.getDate() != null
                                ? originalTransaction.getDate()
                                : DateTimeUtil.getCurrentDate());
        transactionCategory = new TransactionDetailCategory(originalTransaction.getCategory());
        transactionDescription =
                new TransactionDetailDescription(originalTransaction.getDescription());

        newTransaction = new TransactionEntity();
        onSaveEvent = new SingleLiveEvent<>();
        onDeleteEvent = new SingleLiveEvent<>();
    }

    private LiveData<String> setupFormattedAmountLiveData() {
        return Transformations.map(
                amountLiveData,
                new Function1<Double, String>() {
                    @Override
                    public String invoke(Double aDouble) {
                        return AmountUtil.format(aDouble);
                    }
                });
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public boolean showButtonDelete() {
        return originalTransaction.getId() != null;
    }

    public String getOriginalTitle() {
        return originalTransaction.getTitle();
    }

    public LiveData<Double> getAmountLiveData() {
        return amountLiveData;
    }

    public TransactionDetailType getTransactionDetailType() {
        return transactionType;
    }

    public TransactionDetailDate getTransactionDetailDate() {
        return transactionDate;
    }

    public TransactionDetailDescription getTransactionDetailDescription() {
        return transactionDescription;
    }

    public TransactionDetailCategory getTransactionDetailCategory() {
        return transactionCategory;
    }

    public TransactionDetailAccount getTransactionSrcAccount() {
        return transactionSrcAccount;
    }

    public TransactionDetailAccount getTransactionDstAccount() {
        return transactionDstAccount;
    }

    public LiveData<TransactionType> getTransactionTypeLiveData() {
        return transactionType.getTransactionTypeLiveData();
    }

    public TransactionType getTransactionType() {
        return transactionType.getTransactionType();
    }

    public double getAmount() {
        return amountLiveData.getValue();
    }

    public Account getSrcAccount() {
        return transactionSrcAccount.getAccount();
    }

    public Account getDstAccount() {
        return transactionDstAccount.getAccount();
    }

    public LocalDate getDate() {
        return transactionDate.getDate();
    }

    public Category getCategory() {
        return transactionCategory.getCategory();
    }

    public String getDescription() {
        return transactionDescription.getDescription();
    }

    public LiveData<String> getFormattedAmountLiveData() {
        return formattedAmountLiveData;
    }

    public LiveData<Integer> getOnSaveEvent() {
        return onSaveEvent;
    }

    public LiveData<Integer> getOnDeleteEvent() {
        return onDeleteEvent;
    }

    public void setTransactionType(TransactionType type) {
        if (type == null || type == transactionType.getTransactionType()) {
            return;
        }
        transactionType.setTransactionType(type);
        transactionCategory.setCategory(null);
        transactionDstAccount.setAccount(null);
    }

    public void setAmount(double amount) {
        amountLiveData.setValue(amount);
    }

    public void setSrcAccount(Account account) {
        transactionSrcAccount.setAccount(account);
    }

    public void setDstAccount(Account account) {
        transactionDstAccount.setAccount(account);
    }

    public void setDescription(String description) {
        transactionDescription.setDescription(description);
    }

    public void setCategory(Category category) {
        transactionCategory.setCategory(category);
    }

    private void setupNewTransaction(String title) {
        if (originalTransaction.getId() != null) {
            newTransaction.setId(originalTransaction.getId());
        } else {
            newTransaction.setId(UUID.randomUUID());
        }
        newTransaction.setTransactionType(transactionType.getTransactionType());
        newTransaction.setSrcAccountId(transactionSrcAccount.getAccountId());
        newTransaction.setDstAccountId(transactionDstAccount.getAccountId());
        newTransaction.setCategoryId(transactionCategory.getCategoryId());
        newTransaction.setTitle(title);
        newTransaction.setAmount(amountLiveData.getValue());
        newTransaction.setDate(transactionDate.getDate());
        newTransaction.setDescription(transactionDescription.getDescription());
        newTransaction.setCreatedTime(DateTimeUtil.getCurrentTime());
    }

    public void saveTransaction(String title) {
        setupNewTransaction(title);
        int validate = newTransaction.validate(transactionCategory.getCategoryType());
        if (validate != ResultCode.Transaction.VALID) {
            onSaveEvent.setValue(validate);
            return;
        }

        transactionRepository
                .saveTransaction(newTransaction)
                .subscribe(
                        new SingleObserver<Long>() {
                            @Override
                            public void onSubscribe(
                                    @io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

                            @Override
                            public void onSuccess(
                                    @io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                                onSaveEvent.postValue(ResultCode.Transaction.SAVE_SUCCEEDED);
                            }

                            @Override
                            public void onError(
                                    @io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                onSaveEvent.postValue(ResultCode.Transaction.SAVE_FAILED);
                                Log.e(TAG, e.toString());
                            }
                        });
    }

    public void deleteTransaction() {
        transactionRepository
                .deleteTransaction(originalTransaction.getId())
                .subscribe(
                        new SingleObserver<>() {
                            @Override
                            public void onSubscribe(
                                    @io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

                            @Override
                            public void onSuccess(
                                    @io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                                onDeleteEvent.postValue(ResultCode.Transaction.DELETE_SUCCEEDED);
                            }

                            @Override
                            public void onError(
                                    @io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Log.e(TAG, e.toString());
                                onDeleteEvent.postValue(ResultCode.Transaction.DELETE_FAILED);
                            }
                        });
    }
}
