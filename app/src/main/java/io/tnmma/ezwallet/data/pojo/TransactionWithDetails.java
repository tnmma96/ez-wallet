package io.tnmma.ezwallet.data.pojo;

import androidx.room.Relation;

import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.data.db.entity.AccountEntity;
import io.tnmma.ezwallet.data.db.entity.CategoryEntity;
import io.tnmma.ezwallet.data.model.TransactionTypeAndAmount;

public class TransactionWithDetails extends Transaction {

    private static final String TAG = TransactionWithDetails.class.getSimpleName();

    @Relation(parentColumn = "src_account_id", entityColumn = "id", entity = AccountEntity.class)
    private Account srcAccount;

    @Relation(parentColumn = "dst_account_id", entityColumn = "id", entity = AccountEntity.class)
    private Account dstAccount;

    @Relation(parentColumn = "category_id", entityColumn = "id", entity = CategoryEntity.class)
    private Category category;

    public Account getSrcAccount() {
        return srcAccount;
    }

    public void setSrcAccount(Account srcAccount) {
        this.srcAccount = srcAccount;
    }

    public Account getDstAccount() {
        return dstAccount;
    }

    public void setDstAccount(Account dstAccount) {
        this.dstAccount = dstAccount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TransactionTypeAndAmount getTransactionTypeAndAmountModel() {
        return new TransactionTypeAndAmount(getTransactionType(), getAmount());
    }

    public boolean isTransferTransaction() {
        return getTransactionType() == TransactionType.TRANSFER;
    }

    public boolean showCategory() {
        return (!isTransferTransaction() && category != null);
    }
}
