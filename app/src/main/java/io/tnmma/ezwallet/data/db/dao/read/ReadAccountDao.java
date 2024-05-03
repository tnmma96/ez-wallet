package io.tnmma.ezwallet.data.db.dao.read;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.AccountSelection;
import io.tnmma.ezwallet.data.pojo.AccountWithDetails;

import io.reactivex.rxjava3.core.Flowable;

import java.util.List;
import java.util.UUID;

@Dao
public interface ReadAccountDao {

    @Query("SELECT * FROM ivy_account ORDER BY created_time DESC")
    Flowable<List<Account>> getAllAccounts();

    @Query("SELECT * FROM ivy_account ORDER BY created_time DESC")
    Flowable<List<AccountSelection>> getAllAccountSelections();

    @Transaction
    @Query("SELECT * FROM ivy_account ORDER BY created_time DESC")
    Flowable<List<AccountWithDetails>> getAllAccountsWithDetails();

    @Query("SELECT * FROM ivy_account WHERE id = :accountId")
    Flowable<Account> getAccountById(UUID accountId);
}
