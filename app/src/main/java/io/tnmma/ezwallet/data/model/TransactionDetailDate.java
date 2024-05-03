package io.tnmma.ezwallet.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import io.tnmma.ezwallet.util.DateTimeUtil;

import kotlin.jvm.functions.Function1;

import java.time.LocalDate;

public class TransactionDetailDate {

    private static final String TAG = TransactionDetailDate.class.getSimpleName();

    private final MutableLiveData<LocalDate> dateLiveData;

    private final LiveData<String> formattedDateLiveData;

    public TransactionDetailDate(LocalDate date) {
        dateLiveData = new MutableLiveData<>(date);
        formattedDateLiveData = setupFormattedDateLiveData();
    }

    private LiveData<String> setupFormattedDateLiveData() {
        return Transformations.map(
                dateLiveData,
                new Function1<LocalDate, String>() {
                    @Override
                    public String invoke(LocalDate localDate) {
                        return DateTimeUtil.getFormattedDate(localDate, true);
                    }
                });
    }

    public LiveData<LocalDate> getDateLiveData() {
        return dateLiveData;
    }

    public LocalDate getDate() {
        return dateLiveData.getValue();
    }

    public LiveData<String> getFormattedDateLiveData() {
        return formattedDateLiveData;
    }

    public void setDate(LocalDate date) {
        dateLiveData.setValue(date);
    }
}
