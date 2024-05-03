package io.tnmma.ezwallet.data.db.dao.read;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.CategorySelection;
import io.tnmma.ezwallet.data.pojo.CategoryWithDetails;

import io.reactivex.rxjava3.core.Flowable;

import java.util.List;
import java.util.UUID;

@Dao
public interface ReadCategoryDao {

    @Query("SELECT * FROM ivy_category ORDER BY created_time DESC")
    Flowable<List<Category>> getAllCategories();

    @Query("SELECT * FROM ivy_category ORDER BY created_time DESC")
    Flowable<List<CategorySelection>> getAllCategorySelections();

    @Query(
            "SELECT * FROM ivy_category WHERE category_type = :categoryType ORDER BY created_time DESC")
    Flowable<List<Category>> getAllCategoriesByCategoryType(CategoryType categoryType);

    @Query(
            "SELECT * FROM ivy_category WHERE category_type = :categoryType ORDER BY created_time DESC")
    Flowable<List<CategorySelection>> getAllCategorySelectionsByCategoryType(CategoryType categoryType);

    @Transaction
    @Query(
            "SELECT * FROM ivy_category WHERE category_type = :categoryType ORDER BY created_time DESC")
    Flowable<List<CategoryWithDetails>> getAllCategoriesWithDetailsByCategoryType(
            CategoryType categoryType);

    @Query("SELECT * FROM ivy_category WHERE id = :categoryId")
    Flowable<Category> getCategoryById(UUID categoryId);
}
