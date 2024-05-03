package io.tnmma.ezwallet.ui.screen.bottomsheet.editamount;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.AmountType;
import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.base.constant.KeyCode;
import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.util.AmountUtil;

public class EditAmountViewModel extends ViewModel {

    private static final String TAG = EditAmountViewModel.class.getSimpleName();

    private final BottomAction bottomAction;

    private final AmountType amountType;

    private final MutableLiveData<String> formattedAmount;

    public static ViewModelProvider.Factory getFactory(AmountType amountType, double amount) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new EditAmountViewModel(amountType, amount);
            }
        };
    }

    public EditAmountViewModel(AmountType amountType, double amount) {
        bottomAction = new BottomAction(BottomActionType.SET);
        this.amountType = amountType;
        formattedAmount = new MutableLiveData<>(AmountUtil.format(amount));
    }

    public boolean showKeyNegative() {
        return amountType == AmountType.BALANCE;
    }

    public BottomAction getBottomAction() {
        return bottomAction;
    }

    public AmountType getAmountType() {
        return amountType;
    }

    public LiveData<String> getFormattedAmount() {
        return formattedAmount;
    }

    public int getTitleResId() {
        if (amountType == null) {
            return 0;
        }
        return amountType == AmountType.AMOUNT
                ? R.string.title_edit_transaction_amount
                : R.string.title_edit_account_balance;
    }

    private void setDefaultAmount() {
        formattedAmount.setValue(String.valueOf(AmountUtil.DEFAULT_AMOUNT));
    }

    public void onKeyPressed(int keyCode) {
        if (keyCode >= KeyCode.KEY_0 && keyCode <= KeyCode.KEY_9) {
            handleDigitKey(keyCode);
            return;
        }
        switch (keyCode) {
            case KeyCode.KEY_DECIMAL:
                handleDecimalKey();
                break;
            case KeyCode.KEY_NEGATIVE:
                handleNegativeKey();
                break;
            case KeyCode.KEY_BACKSPACE:
                handleBackspaceKey();
                break;
            case KeyCode.KEY_CLEAR:
                handleClearKey();
                break;
        }
    }

    private void handleDigitKey(int keyCode) {
        String current = formattedAmount.getValue();
        if (keyCode == KeyCode.KEY_0 && current.equals("-0")) {
            return;
        }

        if (current.equals("0") && keyCode != KeyCode.KEY_0) {
            formattedAmount.setValue(String.valueOf((char) keyCode));
            return;
        }

        int decimalPosition = current.indexOf(KeyCode.KEY_DECIMAL);
        if (decimalPosition != -1) {
            if (decimalPosition > current.length() - 3) {
                formattedAmount.setValue(current + (char) keyCode);
            }
            return;
        }

        try {
            long wholePart = Long.parseLong(current.replace(",", "") + (char) keyCode);
            if (wholePart < AmountUtil.MIN_AMOUNT) {
                formattedAmount.setValue(AmountUtil.format(AmountUtil.MIN_AMOUNT));
            } else if (wholePart > AmountUtil.MAX_AMOUNT) {
                formattedAmount.setValue(AmountUtil.format(AmountUtil.MAX_AMOUNT));
            } else {
                formattedAmount.setValue(AmountUtil.format(wholePart));
            }
        } catch (NumberFormatException e) {
            setDefaultAmount();
        }
    }

    private void handleDecimalKey() {
        String current = formattedAmount.getValue();
        int decimalPosition = current.indexOf(KeyCode.KEY_DECIMAL);
        if (decimalPosition != -1) {
            formattedAmount.setValue(current.substring(0, decimalPosition));
        } else {
            formattedAmount.setValue(current + (char) KeyCode.KEY_DECIMAL);
        }
    }

    private void handleNegativeKey() {
        String current = formattedAmount.getValue();
        if (current.charAt(0) == KeyCode.KEY_NEGATIVE) {
            formattedAmount.setValue(current.substring(1));
        } else {
            formattedAmount.setValue((char) KeyCode.KEY_NEGATIVE + current);
        }
    }

    private void handleBackspaceKey() {
        String current = formattedAmount.getValue();
        if ((current.length() == 1 && current.charAt(0) != KeyCode.KEY_0)
                || (current.length() == 2 && current.charAt(0) == KeyCode.KEY_NEGATIVE)) {
            setDefaultAmount();
            return;
        }

        if (current.indexOf(KeyCode.KEY_DECIMAL) != -1) {
            formattedAmount.setValue(current.substring(0, current.length() - 1));
            return;
        }

        try {
            long wholePart =
                    Long.parseLong(current.substring(0, current.length() - 1).replace(",", ""));
            formattedAmount.setValue(AmountUtil.format(wholePart));
        } catch (NumberFormatException e) {
            setDefaultAmount();
        }
    }

    private void handleClearKey() {
        String current = formattedAmount.getValue().replace(",", "");
        if (current.equals("0")) {
            return;
        }
        setDefaultAmount();
    }

    private void padDecimal() {
        String current = removeNegativeSignIfZero(formattedAmount.getValue());
        int decimalPosition = current.indexOf(KeyCode.KEY_DECIMAL);
        if (decimalPosition == -1) {
            current = current + ".00";
        } else if (decimalPosition == current.length() - 1) {
            current = current + "00";
        } else if (decimalPosition == current.length() - 2) {
            current = current + "0";
        }
        formattedAmount.setValue(current);
    }

    private String removeNegativeSignIfZero(String value) {
        if (value.charAt(0) != KeyCode.KEY_NEGATIVE) {
            return value;
        }
        for (char c : value.toCharArray()) {
            if (c >= KeyCode.KEY_1 && c <= KeyCode.KEY_9) {
                return value;
            }
        }
        return value.substring(1);
    }

    public double getAmount() {
        padDecimal();
        try {
            return Double.parseDouble(formattedAmount.getValue().replace(",", ""));
        } catch (NumberFormatException | NullPointerException e) {
            return AmountUtil.DEFAULT_AMOUNT;
        }
    }
}
