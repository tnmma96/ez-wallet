package io.tnmma.ezwallet.data.repository;

import io.tnmma.ezwallet.data.db.IvyDatabase;
import io.tnmma.ezwallet.data.db.dao.read.ReadAccountDao;
import io.tnmma.ezwallet.data.db.dao.write.WriteAccountDao;
import io.tnmma.ezwallet.data.db.entity.AccountEntity;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.AccountSelection;
import io.tnmma.ezwallet.data.pojo.AccountWithDetails;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountRepository {

    private static final String TAG = AccountRepository.class.getSimpleName();

    private final ReadAccountDao readAccountDao;

    private final WriteAccountDao writeAccountDao;

    public AccountRepository() {
        readAccountDao = IvyDatabase.getInstance().readAccountDao();
        writeAccountDao = IvyDatabase.getInstance().writeAccountDao();
    }

    public Flowable<List<Account>> getAllAccounts() {
        return readAccountDao
                .getAllAccounts()
                .onErrorReturn(
                        new Function<Throwable, List<Account>>() {
                            @Override
                            public List<Account> apply(Throwable throwable) throws Throwable {
                                return new ArrayList<>();
                            }
                        });
    }

    public Flowable<List<AccountSelection>> getAllAccountSelections() {
        return readAccountDao
                .getAllAccountSelections()
                .onErrorReturn(
                        new Function<Throwable, List<AccountSelection>>() {
                            @Override
                            public List<AccountSelection> apply(Throwable throwable)
                                    throws Throwable {
                                return new ArrayList<>();
                            }
                        });
    }

    public Flowable<List<AccountWithDetails>> getAllAccountsWithDetails() {
        return readAccountDao
                .getAllAccountsWithDetails()
                .onErrorReturn(
                        new Function<Throwable, List<AccountWithDetails>>() {
                            @Override
                            public List<AccountWithDetails> apply(Throwable throwable)
                                    throws Throwable {
                                return new ArrayList<>();
                            }
                        })
                .doOnNext(
                        new Consumer<List<AccountWithDetails>>() {
                            @Override
                            public void accept(List<AccountWithDetails> accountWithDetails)
                                    throws Throwable {
                                for (AccountWithDetails account : accountWithDetails) {
                                    account.computeAmount();
                                }
                            }
                        });
    }

    public Flowable<Account> getAccountById(UUID accountId) {
        return readAccountDao
                .getAccountById(accountId)
                .onErrorReturn(
                        new Function<Throwable, Account>() {
                            @Override
                            public Account apply(Throwable throwable) throws Throwable {
                                return new Account();
                            }
                        });
    }

    public Single<Long> saveAccount(AccountEntity account) {
        return writeAccountDao.saveAccount(account).subscribeOn(Schedulers.io());
    }

    public Single<Integer> deleteAccount(UUID accountId) {
        return writeAccountDao.deleteAccount(accountId).subscribeOn(Schedulers.io());
    }
}
