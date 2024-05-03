package io.tnmma.ezwallet.ui.screen.bottomsheet.editdescription;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.data.model.BottomAction;

public class EditDescriptionViewModel extends ViewModel {

    private static final String TAG = EditDescriptionViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final String originalDescription;

    public static ViewModelProvider.Factory getFactory(String description) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new EditDescriptionViewModel(description);
            }
        };
    }

    public EditDescriptionViewModel(String description) {
        bottomAction = new BottomAction(BottomActionType.SET);
        this.originalDescription = description;
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public String getOriginalDescription() {
        return originalDescription;
    }
}
