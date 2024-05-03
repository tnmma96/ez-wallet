package io.tnmma.ezwallet.data.repository;

import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.data.db.IvyDatabase;
import io.tnmma.ezwallet.data.db.dao.read.ReadCategoryDao;
import io.tnmma.ezwallet.data.db.dao.write.WriteCategoryDao;
import io.tnmma.ezwallet.data.db.entity.CategoryEntity;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.data.pojo.CategorySelection;
import io.tnmma.ezwallet.data.pojo.CategoryWithDetails;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryRepository {

    private static final String TAG = CategoryRepository.class.getSimpleName();

    private final ReadCategoryDao readCategoryDao;

    private final WriteCategoryDao writeCategoryDao;

    public CategoryRepository() {
        readCategoryDao = IvyDatabase.getInstance().readCategoryDao();
        writeCategoryDao = IvyDatabase.getInstance().writeCategoryDao();
    }

    public Flowable<List<CategorySelection>> getCategorySelectionsByCategoryType(
            CategoryType categoryType) {
        if (categoryType == null) {
            return readCategoryDao
                    .getAllCategorySelections()
                    .onErrorReturn(
                            new Function<Throwable, List<CategorySelection>>() {
                                @Override
                                public List<CategorySelection> apply(Throwable throwable)
                                        throws Throwable {
                                    return new ArrayList<>();
                                }
                            });
        }
        return readCategoryDao
                .getAllCategorySelectionsByCategoryType(categoryType)
                .onErrorReturn(
                        new Function<Throwable, List<CategorySelection>>() {
                            @Override
                            public List<CategorySelection> apply(Throwable throwable)
                                    throws Throwable {
                                return new ArrayList<>();
                            }
                        });
    }

    public Flowable<List<CategoryWithDetails>> getAllCategoriesWithDetailsByCategoryType(
            CategoryType categoryType) {
        return readCategoryDao
                .getAllCategoriesWithDetailsByCategoryType(categoryType)
                .onErrorReturn(
                        new Function<Throwable, List<CategoryWithDetails>>() {
                            @Override
                            public List<CategoryWithDetails> apply(Throwable throwable)
                                    throws Throwable {
                                return new ArrayList<>();
                            }
                        })
                .doOnNext(
                        new Consumer<List<CategoryWithDetails>>() {
                            @Override
                            public void accept(List<CategoryWithDetails> categoryWithDetails)
                                    throws Throwable {
                                for (CategoryWithDetails category : categoryWithDetails) {
                                    category.computeAmount();
                                }
                            }
                        });
    }

    public Flowable<Category> getCategoryById(UUID categoryId) {
        return readCategoryDao
                .getCategoryById(categoryId)
                .onErrorReturn(
                        new Function<Throwable, Category>() {
                            @Override
                            public Category apply(Throwable throwable) throws Throwable {
                                return new Category();
                            }
                        });
    }

    public Single<Long> saveCategory(CategoryEntity category) {
        return writeCategoryDao.saveCategory(category).subscribeOn(Schedulers.io());
    }

    public Single<Integer> deleteCategory(UUID categoryId) {
        return writeCategoryDao.deleteCategoryById(categoryId).subscribeOn(Schedulers.io());
    }
}
