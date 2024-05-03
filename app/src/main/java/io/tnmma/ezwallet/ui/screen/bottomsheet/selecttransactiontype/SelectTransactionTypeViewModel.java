package io.tnmma.ezwallet.ui.screen.bottomsheet.selecttransactiontype;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.ConstantArrays;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.TransactionTypeItem;

import java.util.List;

public class SelectTransactionTypeViewModel extends ViewModel {

    private static final String TAG = SelectTransactionTypeViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final TransactionType originalTransactionType;

    private final List<TransactionTypeItem> transactionTypeList;

    public static ViewModelProvider.Factory getFactory(TransactionType transactionType) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SelectTransactionTypeViewModel(transactionType);
            }
        };
    }

    public SelectTransactionTypeViewModel(TransactionType transactionType) {
        bottomAction = new BottomAction(BottomActionType.SELECT);
        originalTransactionType = transactionType;
        transactionTypeList = ConstantArrays.getTransactionTypeList();
        setupTransactionTypeList(
                originalTransactionType != null ? originalTransactionType : TransactionType.INCOME);
    }

    private void setupTransactionTypeList(TransactionType transactionType) {
        for (TransactionTypeItem item : transactionTypeList) {
            item.setSelected(item.getTransactionType() == transactionType);
        }
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public int getTitleResId() {
        return (originalTransactionType != null
                ? R.string.title_select_transaction_type
                : R.string.title_create_transaction);
    }

    public List<TransactionTypeItem> getTransactionTypeList() {
        return transactionTypeList;
    }
}
