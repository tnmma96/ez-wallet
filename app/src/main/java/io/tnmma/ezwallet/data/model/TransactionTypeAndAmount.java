package io.tnmma.ezwallet.data.model;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.util.AmountUtil;
import io.tnmma.ezwallet.util.ColorUtil;

public class TransactionTypeAndAmount {

    private static final String TAG = TransactionTypeAndAmount.class.getSimpleName();

    private TransactionType type;

    private double amount;

    public TransactionTypeAndAmount(TransactionType type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        return AmountUtil.format(amount);
    }

    public int getIconResId() {
        if (type == null) {
            return 0;
        }
        switch (type) {
            case INCOME:
                return R.drawable.icon_income;
            case EXPENSE:
                return R.drawable.icon_expense;
            case TRANSFER:
                return R.drawable.icon_transfer;
        }
        return 0;
    }

    public int getColor() {
        if (type == null) {
            return 0;
        }
        int colorResId = 0;
        switch (type) {
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
        return ColorUtil.getColorFromResId(colorResId);
    }
}
