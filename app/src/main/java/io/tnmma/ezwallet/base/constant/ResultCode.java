package io.tnmma.ezwallet.base.constant;

public interface ResultCode {

    interface Account {

        int EMPTY_ID = -10;

        int EMPTY_NAME = -20;

        int INVALID_COLOR = -30;

        int EMPTY_ICON = -40;

        int EMPTY_CREATED_TIME = -50;

        int SAVE_FAILED = -1000;

        int DELETE_FAILED = -10000;

        int VALID = 0;

        int SAVE_SUCCEEDED = 1000;

        int DELETE_SUCCEEDED = 10000;
    }

    interface Category {

        int EMPTY_ID = -10;

        int EMPTY_NAME = -20;

        int EMPTY_CATEGORY_TYPE = -30;

        int INVALID_COLOR = -40;

        int EMPTY_ICON = -50;

        int EMPTY_CREATED_TIME = -60;

        int SAVE_FAILED = -1000;

        int DELETE_FAILED = -10000;

        int VALID = 0;

        int SAVE_SUCCEEDED = 1000;

        int DELETE_SUCCEEDED = 10000;
    }

    interface Transaction {

        int EMPTY_ID = -10;

        int EMPTY_TRANSACTION_TYPE = -20;

        int EMPTY_SRC_ACCOUNT = -30;

        int EMPTY_DST_ACCOUNT = -40;

        int INCOME_EXPENSE_WITH_DST_ACCOUNT = -50;

        int TRANSFER_WITH_SAME_SRC_DST = -60;

        int TRANSFER_WITH_CATEGORY = -70;

        int INVALID_CATEGORY_TYPE = -80;

        int ZERO_AMOUNT = -90;

        int EMPTY_TITLE = -100;

        int EMPTY_DATE = -110;

        int EMPTY_CREATED_TIME = -120;

        int SAVE_FAILED = -1000;

        int DELETE_FAILED = -10000;

        int VALID = 0;

        int SAVE_SUCCEEDED = 1000;

        int DELETE_SUCCEEDED = 10000;
    }
}
