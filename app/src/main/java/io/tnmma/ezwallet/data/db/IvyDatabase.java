package io.tnmma.ezwallet.data.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import io.tnmma.ezwallet.base.IvyApplication;
import io.tnmma.ezwallet.data.db.dao.read.ReadAccountDao;
import io.tnmma.ezwallet.data.db.dao.read.ReadCategoryDao;
import io.tnmma.ezwallet.data.db.dao.read.ReadTransactionDao;
import io.tnmma.ezwallet.data.db.dao.write.WriteAccountDao;
import io.tnmma.ezwallet.data.db.dao.write.WriteCategoryDao;
import io.tnmma.ezwallet.data.db.dao.write.WriteTransactionDao;
import io.tnmma.ezwallet.data.db.entity.AccountEntity;
import io.tnmma.ezwallet.data.db.entity.CategoryEntity;
import io.tnmma.ezwallet.data.db.entity.TransactionEntity;

@Database(
        entities = {AccountEntity.class, CategoryEntity.class, TransactionEntity.class},
        version = 1)
@TypeConverters({IvyTypeConverter.class})
public abstract class IvyDatabase extends RoomDatabase {

    private static final String DB_NAME = "ivy.db";

    private static volatile IvyDatabase instance;

    public abstract ReadAccountDao readAccountDao();

    public abstract ReadCategoryDao readCategoryDao();

    public abstract ReadTransactionDao readTransactionDao();

    public abstract WriteAccountDao writeAccountDao();

    public abstract WriteCategoryDao writeCategoryDao();

    public abstract WriteTransactionDao writeTransactionDao();

    public static IvyDatabase getInstance() {
        if (instance == null) {
            synchronized (IvyDatabase.class) {
                if (instance == null) {
                    instance =
                            Room.databaseBuilder(
                                            IvyApplication.getInstance().getAppContext(),
                                            IvyDatabase.class,
                                            DB_NAME)
                                    .build();
                }
            }
        }
        return instance;
    }
}
