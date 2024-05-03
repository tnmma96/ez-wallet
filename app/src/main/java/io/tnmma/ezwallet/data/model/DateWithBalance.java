package io.tnmma.ezwallet.data.model;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.TransactionType;
import io.tnmma.ezwallet.util.AmountUtil;
import io.tnmma.ezwallet.util.ColorUtil;
import io.tnmma.ezwallet.util.DateTimeUtil;

import java.time.LocalDate;

public class DateWithBalance {

    private static final String TAG = DateWithBalance.class.getSimpleName();

    private final LocalDate date;

    private double balance;

    public DateWithBalance(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void addTransactionAmount(TransactionType transactionType, double amount) {
        if (transactionType == null) {
            return;
        }
        switch (transactionType) {
            case INCOME:
                balance += amount;
                break;
            case EXPENSE:
                balance -= amount;
                break;
        }
    }

    public String getFormattedDate() {
        return (date != null) ? DateTimeUtil.getFormattedDate(date, false) : null;
    }

    public String getFormattedDayOfWeek() {
        return (date != null) ? DateTimeUtil.getFormattedDayOfWeek(date) : null;
    }

    public String getFormattedBalance() {
        return AmountUtil.formatToBillion(balance);
    }

    public int getColor() {
        int colorResId = R.color.ivy_black;
        if (balance < 0) {
            colorResId = R.color.ivy_red;
        } else if (balance > 0) {
            colorResId = R.color.ivy_green;
        }
        return ColorUtil.getColorFromResId(colorResId);
    }
}
