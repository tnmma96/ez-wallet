package io.tnmma.ezwallet.data.db.dao.read;

import androidx.room.Dao;
import androidx.room.Query;

import io.tnmma.ezwallet.data.pojo.Transaction;
import io.tnmma.ezwallet.data.pojo.TransactionWithDetails;

import io.reactivex.rxjava3.core.Flowable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Dao
public interface ReadTransactionDao {

    @Query(
            "SELECT * FROM ivy_transaction "
                    + "WHERE transaction_type = 'INCOME' OR transaction_type = 'EXPENSE'")
    Flowable<List<Transaction>> getAllIncomeAndExpenseTransactions();

    @androidx.room.Transaction
    @Query(
            "SELECT * FROM ivy_transaction "
                    + "WHERE date >= :startDate AND date <= :endDate "
                    + "ORDER BY date DESC, created_time DESC")
    Flowable<List<TransactionWithDetails>> getTransactionsInDateRange(
            LocalDate startDate, LocalDate endDate);

    @Query(
            "SELECT * FROM ivy_transaction "
                    + "WHERE src_account_id = :accountId OR dst_account_id = :accountId")
    Flowable<List<Transaction>> getTransactionsByAccountId(UUID accountId);

    @androidx.room.Transaction
    @Query(
            "SELECT * FROM ivy_transaction "
                    + "WHERE (src_account_id = :accountId OR dst_account_id = :accountId) "
                    + "AND (date >= :startDate AND date <= :endDate) "
                    + "ORDER BY date DESC, created_time DESC")
    Flowable<List<TransactionWithDetails>> getTransactionsByAccountIdInDateRange(
            UUID accountId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT * FROM ivy_transaction WHERE category_id = :categoryId")
    Flowable<List<Transaction>> getTransactionsByCategoryId(UUID categoryId);

    @androidx.room.Transaction
    @Query(
            "SELECT * FROM ivy_transaction "
                    + "WHERE category_id = :categoryId "
                    + "AND (date >= :startDate AND date <= :endDate) "
                    + "ORDER BY date DESC, created_time DESC")
    Flowable<List<TransactionWithDetails>> getTransactionsByCategoryIdInDateRange(
            UUID categoryId, LocalDate startDate, LocalDate endDate);
}
