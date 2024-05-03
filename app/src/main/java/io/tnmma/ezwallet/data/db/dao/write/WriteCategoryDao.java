package io.tnmma.ezwallet.data.db.dao.write;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import io.tnmma.ezwallet.data.db.entity.CategoryEntity;

import io.reactivex.rxjava3.core.Single;

import java.util.UUID;

@Dao
public interface WriteCategoryDao {

    @Upsert
    Single<Long> saveCategory(CategoryEntity category);

    @Query("DELETE FROM ivy_category WHERE id = :id")
    Single<Integer> deleteCategoryById(UUID id);
}
