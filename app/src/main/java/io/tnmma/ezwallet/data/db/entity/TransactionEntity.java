package io.tnmma.ezwallet.data.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.util.AmountUtil;
import io.tnmma.ezwallet.util.CommonUtil;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity(
        tableName = "ivy_transaction",
        foreignKeys = {
            @ForeignKey(
                    entity = AccountEntity.class,
                    parentColumns = {"id"},
                    childColumns = {"src_account_id"},
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(
                    entity = AccountEntity.class,
                    parentColumns = {"id"},
                    childColumns = {"dst_account_id"},
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(
                    entity = CategoryEntity.class,
                    parentColumns = {"id"},
                    childColumns = {"category_id"},
                    onDelete = ForeignKey.SET_NULL)
        },
        indices = {
            @Index(value = {"id"}),
            @Index(value = {"src_account_id"}),
            @Index(value = {"dst_account_id"}),
            @Index(value = {"category_id"})
        })
public class TransactionEntity implements Serializable {

    @NonNull @PrimaryKey private UUID id;

    @NonNull
    @ColumnInfo(name = "transaction_type")
    private TransactionType transactionType;

    @NonNull
    @ColumnInfo(name = "src_account_id")
    private UUID srcAccountId;

    @ColumnInfo(name = "dst_account_id")
    private UUID dstAccountId;

    @ColumnInfo(name = "category_id")
    private UUID categoryId;

    @NonNull private String title;

    private double amount;

    @NonNull private LocalDate date;

    private String description;

    @NonNull
    @ColumnInfo(name = "created_time")
    private Instant createdTime;

    @NonNull
    public UUID getId() {
        return id;
    }

    @NonNull
    public TransactionType getTransactionType() {
        return transactionType;
    }

    @NonNull
    public UUID getSrcAccountId() {
        return srcAccountId;
    }

    public UUID getDstAccountId() {
        return dstAccountId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public void setTransactionType(@NonNull TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setSrcAccountId(@NonNull UUID srcAccountId) {
        this.srcAccountId = srcAccountId;
    }

    public void setDstAccountId(UUID dstAccountId) {
        this.dstAccountId = dstAccountId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(@NonNull LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedTime(@NonNull Instant createdTime) {
        this.createdTime = createdTime;
    }

    public int validate(CategoryType categoryType) {
        if (id == null) {
            return ResultCode.Transaction.EMPTY_ID;
        }

        if (transactionType == null) {
            return ResultCode.Transaction.EMPTY_TRANSACTION_TYPE;
        }

        if (srcAccountId == null) {
            return ResultCode.Transaction.EMPTY_SRC_ACCOUNT;
        }

        if (transactionType != TransactionType.TRANSFER && dstAccountId != null) {
            return ResultCode.Transaction.INCOME_EXPENSE_WITH_DST_ACCOUNT;
        }

        if (transactionType == TransactionType.TRANSFER && dstAccountId == null) {
            return ResultCode.Transaction.EMPTY_DST_ACCOUNT;
        }

        if (transactionType == TransactionType.TRANSFER && categoryId != null) {
            return ResultCode.Transaction.TRANSFER_WITH_CATEGORY;
        }

        if (!validateCategoryType(categoryType)) {
            return ResultCode.Transaction.INVALID_CATEGORY_TYPE;
        }

        if (CommonUtil.isNullOrEmpty(title)) {
            return ResultCode.Transaction.EMPTY_TITLE;
        }

        if (amount < AmountUtil.SMALLEST_AMOUNT_DIFF) {
            return ResultCode.Transaction.ZERO_AMOUNT;
        }

        if (date == null) {
            return ResultCode.Transaction.EMPTY_DATE;
        }

        if (createdTime == null) {
            return ResultCode.Transaction.EMPTY_CREATED_TIME;
        }

        return ResultCode.Transaction.VALID;
    }

    public boolean validateCategoryType(CategoryType categoryType) {
        if (categoryType == null) {
            return true;
        }
        if (transactionType == TransactionType.INCOME && categoryType == CategoryType.INCOME) {
            return true;
        }
        if (transactionType == TransactionType.EXPENSE && categoryType == CategoryType.EXPENSE) {
            return true;
        }
        return false;
    }
}
