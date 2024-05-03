package io.tnmma.ezwallet.ui.screen.bottomsheet.selectaccount;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.AccountSelection;
import io.tnmma.ezwallet.data.repository.AccountRepository;

import io.reactivex.rxjava3.functions.Consumer;

import java.util.List;
import java.util.Objects;

public class SelectAccountViewModel extends ViewModel {

    private static final String TAG = SelectAccountViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final TransactionType transactionType;

    private final AccountRepository accountRepository;

    private final LiveData<List<AccountSelection>> srcAccountListLiveData, dstAccountListLiveData;

    private final MutableLiveData<Account> currentSrcAccountLiveData, currentDstAccountLiveData;

    public static ViewModelProvider.Factory getFactory(
            TransactionType transactionType, Account srcAccount, Account dstAccount) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SelectAccountViewModel(transactionType, srcAccount, dstAccount);
            }
        };
    }

    public SelectAccountViewModel(
            TransactionType transactionType, Account srcAccount, Account dstAccount) {
        bottomAction = new BottomAction(BottomActionType.SELECT);
        this.transactionType = transactionType;
        accountRepository = new AccountRepository();

        currentSrcAccountLiveData = new MutableLiveData<>(srcAccount);
        srcAccountListLiveData = getAccountListFromRepository(true);

        currentDstAccountLiveData =
                isTransferTransaction() ? new MutableLiveData<>(dstAccount) : null;
        dstAccountListLiveData =
                isTransferTransaction() ? getAccountListFromRepository(false) : null;
    }

    private LiveData<List<AccountSelection>> getAccountListFromRepository(boolean isSrcAccount) {
        return LiveDataReactiveStreams.fromPublisher(
                accountRepository
                        .getAllAccountSelections()
                        .doOnNext(
                                new Consumer<List<AccountSelection>>() {
                                    @Override
                                    public void accept(List<AccountSelection> accounts) throws Throwable {
                                        setupAccountList(accounts, isSrcAccount);
                                    }
                                }));
    }

    private void setupAccountList(List<AccountSelection> accountList, boolean isSrcAccount) {
        if (isSrcAccount) {
            if (currentSrcAccountLiveData.getValue() == null) {
                return;
            }
            for (AccountSelection item : accountList) {
                item.postSelected(
                        Objects.equals(item.getId(), currentSrcAccountLiveData.getValue().getId()));
            }
        } else {
            if (currentDstAccountLiveData.getValue() == null) {
                return;
            }
            for (AccountSelection item : accountList) {
                item.postSelected(
                        Objects.equals(item.getId(), currentDstAccountLiveData.getValue().getId()));
            }
        }
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LiveData<List<AccountSelection>> getSrcAccountListLiveData() {
        return srcAccountListLiveData;
    }

    public LiveData<List<AccountSelection>> getDstAccountListLiveData() {
        return dstAccountListLiveData;
    }

    public Account getCurrentSrcAccount() {
        return currentSrcAccountLiveData.getValue();
    }

    public Account getCurrentDstAccount() {
        return currentDstAccountLiveData.getValue();
    }

    public boolean isTransferTransaction() {
        return transactionType == TransactionType.TRANSFER;
    }

    public void setSrcAccount(Account account) {
        currentSrcAccountLiveData.setValue(account);
    }

    public void setDstAccount(Account account) {
        currentDstAccountLiveData.setValue(account);
    }
}
