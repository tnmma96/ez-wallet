package io.tnmma.ezwallet.data.repository;

import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.data.db.IvyDatabase;
import io.tnmma.ezwallet.data.db.dao.read.ReadTransactionDao;
import io.tnmma.ezwallet.data.db.dao.write.WriteTransactionDao;
import io.tnmma.ezwallet.data.db.entity.TransactionEntity;
import io.tnmma.ezwallet.data.pojo.Transaction;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;
import io.tnmma.ezwallet.util.DateTimeUtil;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TransactionRepository {

    private static final String TAG = TransactionRepository.class.getSimpleName();

    private final ReadTransactionDao readTransactionDao;

    private final WriteTransactionDao writeTransactionDao;

    public TransactionRepository() {
        readTransactionDao = IvyDatabase.getInstance().readTransactionDao();
        writeTransactionDao = IvyDatabase.getInstance().writeTransactionDao();
    }

    public Flowable<Double> getCurrentBalance() {
        return readTransactionDao
                .getAllIncomeAndExpenseTransactions()
                .onErrorReturn(
                        new Function<Throwable, List<Transaction>>() {
                            @Override
                            public List<Transaction> apply(Throwable throwable) throws Throwable {
                                return new ArrayList<>();
                            }
                        })
                .map(
                        new Function<List<Transaction>, Double>() {
                            @Override
                            public Double apply(List<Transaction> transactions) throws Throwable {
                                double balance = 0;
                                for (Transaction transaction : transactions) {
                                    if (transaction.getTransactionType()
                                            == TransactionType.INCOME) {
                                        balance += transaction.getAmount();
                                    } else if (transaction.getTransactionType()
                                            == TransactionType.EXPENSE) {
                                        balance -= transaction.getAmount();
                                    }
                                }
                                return balance;
                            }
                        });
    }

    public Flowable<List<TransactionWithDetails>> getTransactionsInDateRange(
            LocalDate startDate, LocalDate endDate) {
        return readTransactionDao
                .getTransactionsInDateRange(
                        startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                        endDate != null ? endDate : DateTimeUtil.MAX_DATE)
                .onErrorReturn(
                        new Function<Throwable, List<TransactionWithDetails>>() {
                            @Override
                            public List<TransactionWithDetails> apply(Throwable throwable)
                                    throws Throwable {
                                return new ArrayList<>();
                            }
                        });
    }

    public Flowable<Double> getAccountCurrentBalance(UUID accountId) {
        return readTransactionDao
                .getTransactionsByAccountId(accountId)
                .onErrorReturn(
                        new Function<Throwable, List<Transaction>>() {
                            @Override
                            public List<Transaction> apply(Throwable throwable) throws Throwable {
                                return new ArrayList<>();
                            }
                        })
                .map(
                        new Function<List<Transaction>, Double>() {
                            @Override
                            public Double apply(List<Transaction> transactions) throws Throwable {
                                double balance = 0;
                                for (Transaction transaction : transactions) {
                                    if (transaction.getTransactionType() == null) {
                                        continue;
                                    }
                                    switch (transaction.getTransactionType()) {
                                        case INCOME:
                                            balance += transaction.getAmount();
                                            break;
                                        case EXPENSE:
                                            balance -= transaction.getAmount();
                                            break;
                                        case TRANSFER:
                                            if (Objects.equals(
                                                    accountId, transaction.getSrcAccountId())) {
                                                balance -= transaction.getAmount();
                                            } else if (Objects.equals(
                                                    accountId, transaction.getDstAccountId())) {
                                                balance += transaction.getAmount();
                                            }
                                            break;
                                    }
                                }
                                return balance;
                            }
                        });
    }

    public Flowable<Double> getCategoryTotalAmount(UUID categoryId) {
        return readTransactionDao
                .getTransactionsByCategoryId(categoryId)
                .onErrorReturn(
                        new Function<Throwable, List<Transaction>>() {
                            @Override
                            public List<Transaction> apply(Throwable throwable) throws Throwable {
                                return new ArrayList<>();
                            }
                        })
                .map(
                        new Function<List<Transaction>, Double>() {
                            @Override
                            public Double apply(List<Transaction> transactions) throws Throwable {
                                double amount = 0;
                                for (Transaction transaction : transactions) {
                                    amount += transaction.getAmount();
                                }
                                return amount;
                            }
                        });
    }

    public Flowable<List<TransactionWithDetails>> getTransactionsByAccountIdInDateRange(
            UUID accountId, LocalDate startDate, LocalDate endDate) {
        return readTransactionDao
                .getTransactionsByAccountIdInDateRange(
                        accountId,
                        startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                        endDate != null ? endDate : DateTimeUtil.MAX_DATE)
                .onErrorReturn(
                        new Function<Throwable, List<TransactionWithDetails>>() {
                            @Override
                            public List<TransactionWithDetails> apply(Throwable throwable)
                                    throws Throwable {
                                return new ArrayList<>();
                            }
                        });
    }

    public Flowable<List<TransactionWithDetails>> getTransactionsByCategoryIdInDateRange(
            UUID categoryId, LocalDate startDate, LocalDate endDate) {
        return readTransactionDao
                .getTransactionsByCategoryIdInDateRange(
                        categoryId,
                        startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                        endDate != null ? endDate : DateTimeUtil.MAX_DATE)
                .onErrorReturn(
                        new Function<Throwable, List<TransactionWithDetails>>() {
                            @Override
                            public List<TransactionWithDetails> apply(Throwable throwable)
                                    throws Throwable {
                                return new ArrayList<>();
                            }
                        });
    }

    public Single<Long> saveTransaction(TransactionEntity transaction) {
        return writeTransactionDao.saveTransaction(transaction).subscribeOn(Schedulers.io());
    }

    public Single<Integer> deleteTransaction(UUID transactionId) {
        return writeTransactionDao.deleteTransaction(transactionId).subscribeOn(Schedulers.io());
    }
}
