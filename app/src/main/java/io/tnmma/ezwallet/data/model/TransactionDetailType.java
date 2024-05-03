package io.tnmma.ezwallet.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.util.ColorUtil;

import kotlin.jvm.functions.Function1;

public class TransactionDetailType {

    private static final String TAG = TransactionDetailType.class.getSimpleName();

    private final MutableLiveData<TransactionType> transactionTypeLiveData;

    private final LiveData<Integer> iconLiveData;

    private final LiveData<Integer> labelLiveData;

    private final LiveData<Integer> colorLiveData;

    public TransactionDetailType(TransactionType transactionType) {
        transactionTypeLiveData = new MutableLiveData<>(transactionType);
        iconLiveData = setupIconLiveData();
        labelLiveData = setupLabelLiveData();
        colorLiveData = setupColorLiveData();
    }

    private LiveData<Integer> setupIconLiveData() {
        return Transformations.map(
                transactionTypeLiveData,
                new Function1<TransactionType, Integer>() {
                    @Override
                    public Integer invoke(TransactionType transactionType) {
                        if (transactionType == null) {
                            return 0;
                        }
                        switch (transactionType) {
                            case INCOME:
                                return R.drawable.icon_income;
                            case EXPENSE:
                                return R.drawable.icon_expense;
                            case TRANSFER:
                                return R.drawable.icon_transfer;
                        }
                        return 0;
                    }
                });
    }

    private LiveData<Integer> setupLabelLiveData() {
        return Transformations.map(
                transactionTypeLiveData,
                new Function1<TransactionType, Integer>() {
                    @Override
                    public Integer invoke(TransactionType transactionType) {
                        if (transactionType == null) {
                            return 0;
                        }
                        switch (transactionType) {
                            case INCOME:
                                return R.string.label_transaction_type_income;
                            case EXPENSE:
                                return R.string.label_transaction_type_expense;
                            case TRANSFER:
                                return R.string.label_transaction_type_transfer;
                        }
                        return 0;
                    }
                });
    }

    private LiveData<Integer> setupColorLiveData() {
        return Transformations.map(
                transactionTypeLiveData,
                new Function1<TransactionType, Integer>() {
                    @Override
                    public Integer invoke(TransactionType transactionType) {
                        int colorResId = R.color.ivy_medium_white;
                        if (transactionType != null) {
                            switch (transactionType) {
                                case INCOME:
                                    colorResId = R.color.ivy_green;
                                    break;
                                case EXPENSE:
                                    colorResId = R.color.ivy_red;
                                    break;
                                case TRANSFER:
                                    colorResId = R.color.ivy_purple;
                                    break;
                            }
                        }
                        return ColorUtil.getColorFromResId(colorResId);
                    }
                });
    }

    public LiveData<TransactionType> getTransactionTypeLiveData() {
        return transactionTypeLiveData;
    }

    public TransactionType getTransactionType() {
        return transactionTypeLiveData.getValue();
    }

    public LiveData<Integer> getIconLiveData() {
        return iconLiveData;
    }

    public LiveData<Integer> getLabelLiveData() {
        return labelLiveData;
    }

    public LiveData<Integer> getColorLiveData() {
        return colorLiveData;
    }

    public void setTransactionType(TransactionType transactionType) {
        transactionTypeLiveData.setValue(transactionType);
    }
}
