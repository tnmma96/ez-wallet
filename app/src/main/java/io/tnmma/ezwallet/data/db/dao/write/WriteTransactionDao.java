package io.tnmma.ezwallet.data.db.dao.write;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import io.tnmma.ezwallet.data.db.entity.TransactionEntity;

import io.reactivex.rxjava3.core.Single;

import java.util.UUID;

@Dao
public interface WriteTransactionDao {

    @Upsert
    Single<Long> saveTransaction(TransactionEntity account);

    @Query("DELETE FROM ivy_transaction WHERE id = :transactionId")
    Single<Integer> deleteTransaction(UUID transactionId);
}
