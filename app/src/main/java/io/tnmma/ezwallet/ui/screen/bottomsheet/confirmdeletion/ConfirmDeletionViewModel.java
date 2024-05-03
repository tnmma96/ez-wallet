package io.tnmma.ezwallet.ui.screen.bottomsheet.confirmdeletion;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.ModelType;
import io.tnmma.ezwallet.data.model.BottomAction;

public class ConfirmDeletionViewModel extends ViewModel {

    private static final String TAG = ConfirmDeletionViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final ModelType modelType;

    public static ViewModelProvider.Factory getFactory(ModelType modelType) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ConfirmDeletionViewModel(modelType);
            }
        };
    }

    public ConfirmDeletionViewModel(ModelType modelType) {
        bottomAction = new BottomAction(BottomActionType.DELETE);
        this.modelType = modelType;
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public int getWarningResId() {
        if (modelType == null) {
            return 0;
        }
        switch (modelType) {
            case ACCOUNT:
                return R.string.warning_account_deletion;
            case CATEGORY:
                return R.string.warning_category_deletion;
            case TRANSACTION:
                return R.string.warning_transaction_deletion;
        }
        return 0;
    }
}
