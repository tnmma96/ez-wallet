package io.tnmma.ezwallet.ui.screen.bottomsheet.editaccount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.ConstantArrays;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.base.event.SingleLiveEvent;
import io.tnmma.ezwallet.data.db.entity.AccountEntity;
import io.tnmma.ezwallet.data.db.entity.TransactionEntity;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.ColorItem;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.repository.AccountRepository;
import io.tnmma.ezwallet.data.repository.TransactionRepository;
import io.tnmma.ezwallet.util.AmountUtil;
import io.tnmma.ezwallet.util.ColorUtil;
import io.tnmma.ezwallet.util.DateTimeUtil;
import io.tnmma.ezwallet.util.StringUtil;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

import kotlin.jvm.functions.Function1;

import java.util.List;
import java.util.UUID;

public class EditAccountViewModel extends ViewModel {

    private static final String TAG = EditAccountViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final Account originalAccount;

    private final double originalCurrentBalance;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final List<ColorItem> colorList;

    private final MutableLiveData<Integer> selectedBackgroundColorLiveData;

    private final LiveData<Integer> selectedForegroundColorLiveData;

    private final MutableLiveData<Double> currentBalanceLiveData;

    private final LiveData<String> formattedCurrentBalanceLiveData;

    private final AccountEntity newAccount;

    private final TransactionEntity adjustBalanceTransaction;

    private final SingleLiveEvent<Integer> onSaveEvent;

    public static ViewModelProvider.Factory getFactory(Account account, double currentBalance) {
        return new ViewModelProvider.Factory() {
            @androidx.annotation.NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new EditAccountViewModel(account, currentBalance);
            }
        };
    }

    public EditAccountViewModel(Account account, double currentBalance) {
        bottomAction = new BottomAction(BottomActionType.SAVE);
        originalAccount = account;
        originalCurrentBalance = currentBalance;
        accountRepository = new AccountRepository();
        transactionRepository = new TransactionRepository();

        colorList = ConstantArrays.getColorList();
        setupColorList();
        selectedBackgroundColorLiveData = new MutableLiveData<>(getOriginalColor());
        selectedForegroundColorLiveData = setupSelectedForegroundColorLiveData();

        currentBalanceLiveData = new MutableLiveData<>(originalCurrentBalance);
        formattedCurrentBalanceLiveData = setupFormattedCurrentBalanceLiveData();

        newAccount = new AccountEntity();
        adjustBalanceTransaction = new TransactionEntity();
        onSaveEvent = new SingleLiveEvent<>();
    }

    private int getOriginalColor() {
        return originalAccount.getId() != null
                ? originalAccount.getColor()
                : colorList.get(0).getColor();
    }

    private void setupColorList() {
        int originalColor = getOriginalColor();
        for (ColorItem item : colorList) {
            item.setSelected(item.getColor() == originalColor);
        }
    }

    private LiveData<Integer> setupSelectedForegroundColorLiveData() {
        return Transformations.map(
                selectedBackgroundColorLiveData,
                new Function1<Integer, Integer>() {
                    @Override
                    public Integer invoke(Integer integer) {
                        return ColorUtil.getItemForegroundColor(integer);
                    }
                });
    }

    private LiveData<String> setupFormattedCurrentBalanceLiveData() {
        return Transformations.map(
                currentBalanceLiveData,
                new Function1<Double, String>() {
                    @Override
                    public String invoke(Double aDouble) {
                        return AmountUtil.format(aDouble);
                    }
                });
    }

    public int getTitleResId() {
        return originalAccount.getId() != null
                ? R.string.title_edit_account
                : R.string.title_create_account;
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public String getOriginalAccountName() {
        return originalAccount.getName();
    }

    public List<ColorItem> getColorList() {
        return colorList;
    }

    public LiveData<Integer> getSelectedBackgroundColorLiveData() {
        return selectedBackgroundColorLiveData;
    }

    public LiveData<Integer> getSelectedForegroundColorLiveData() {
        return selectedForegroundColorLiveData;
    }

    public double getCurrentBalance() {
        return currentBalanceLiveData.getValue();
    }

    public LiveData<String> getFormattedCurrentBalanceLiveData() {
        return formattedCurrentBalanceLiveData;
    }

    public LiveData<Integer> getOnSaveEvent() {
        return onSaveEvent;
    }

    public void setCurrentBalance(double currentBalance) {
        currentBalanceLiveData.setValue(currentBalance);
    }

    public void setBackgroundColor(int color) {
        selectedBackgroundColorLiveData.setValue(color);
    }

    private void setupNewAccount(String name, int color) {
        if (originalAccount.getId() != null) {
            newAccount.setId(originalAccount.getId());
            newAccount.setIcon(originalAccount.getIcon());
            newAccount.setCreatedTime(originalAccount.getCreatedTime());
        } else {
            newAccount.setId(UUID.randomUUID());
            newAccount.setCreatedTime(DateTimeUtil.getCurrentTime());
        }
        newAccount.setName(name);
        newAccount.setColor(color);
    }

    public void saveAccount(String name, int color) {
        setupNewAccount(name, color);
        int validateNewAccount = newAccount.validate();
        if (validateNewAccount != ResultCode.Account.VALID) {
            onSaveEvent.setValue(validateNewAccount);
            return;
        }

        accountRepository
                .saveAccount(newAccount)
                .subscribe(
                        new SingleObserver<>() {
                            @Override
                            public void onSubscribe(
                                    @io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

                            @Override
                            public void onSuccess(
                                    @io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                                saveAdjustBalanceTransaction(newAccount.getId());
                                onSaveEvent.postValue(ResultCode.Account.SAVE_SUCCEEDED);
                            }

                            @Override
                            public void onError(
                                    @io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                onSaveEvent.postValue(ResultCode.Account.SAVE_FAILED);
                            }
                        });
    }

    private TransactionEntity setupAdjustBalanceTransaction(UUID accountId) {
        double balanceDiff = currentBalanceLiveData.getValue() - originalCurrentBalance;
        if (Math.abs(currentBalanceLiveData.getValue() - originalCurrentBalance)
                < AmountUtil.SMALLEST_AMOUNT_DIFF) {
            return null;
        }

        adjustBalanceTransaction.setId(UUID.randomUUID());
        adjustBalanceTransaction.setTransactionType(
                balanceDiff > 0 ? TransactionType.INCOME : TransactionType.EXPENSE);
        adjustBalanceTransaction.setSrcAccountId(accountId);
        adjustBalanceTransaction.setAmount(Math.abs(balanceDiff));
        adjustBalanceTransaction.setTitle(
                StringUtil.getStringFromResId(R.string.title_transaction_adjust_balance));
        adjustBalanceTransaction.setDate(DateTimeUtil.getCurrentDate());
        adjustBalanceTransaction.setCreatedTime(DateTimeUtil.getCurrentTime());
        return adjustBalanceTransaction;
    }

    private void saveAdjustBalanceTransaction(UUID accountId) {
        TransactionEntity adjustBalanceTransaction = setupAdjustBalanceTransaction(accountId);
        if (adjustBalanceTransaction == null) {
            return;
        }

        transactionRepository
                .saveTransaction(adjustBalanceTransaction)
                .subscribe(
                        new SingleObserver<>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {}

                            @Override
                            public void onSuccess(@NonNull Long aLong) {}

                            @Override
                            public void onError(@NonNull Throwable e) {}
                        });
    }
}
