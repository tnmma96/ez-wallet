package io.tnmma.ezwallet.data.db.dao.write;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import io.tnmma.ezwallet.data.db.entity.AccountEntity;

import io.reactivex.rxjava3.core.Single;

import java.util.UUID;

@Dao
public interface WriteAccountDao {

    @Upsert
    Single<Long> saveAccount(AccountEntity account);

    @Query("DELETE FROM ivy_account WHERE id = :accountId")
    Single<Integer> deleteAccount(UUID accountId);
}
