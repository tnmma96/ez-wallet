package io.tnmma.ezwallet.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TransactionDetailDescription {

    private static final String TAG = TransactionDetailDescription.class.getSimpleName();

    private final MutableLiveData<String> descriptionLiveData;

    public TransactionDetailDescription(String description) {
        descriptionLiveData = new MutableLiveData<>(description);
    }

    public LiveData<String> getDescriptionLiveData() {
        return descriptionLiveData;
    }

    public String getDescription() {
        return descriptionLiveData.getValue();
    }

    public void setDescription(String description) {
        if (description != null && description.trim().isEmpty()) {
            description = null;
        }
        descriptionLiveData.setValue(description);
    }
}
