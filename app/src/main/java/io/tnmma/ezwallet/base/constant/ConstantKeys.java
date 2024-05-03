package io.tnmma.ezwallet.base.constant;

public interface ConstantKeys {

    String MODEL_TYPE = "MODEL_TYPE";

    interface IntentAction {

        String OPEN_DETAIL_SCREEN = "OPEN_DETAIL_SCREEN";

        String CLOSE_DETAIL_SCREEN = "CLOSE_DETAIL_SCREEN";

        String CLOSE_APP = "CLOSE_APP";
    }

    interface FragmentRequestKey {

        String SORT_OPTION = "SORT_OPTION";

        String DATE_OPTION = "DATE_OPTION";

        String CATEGORY_TYPE = "CATEGORY_TYPE";

        String ACCOUNT_CURRENT_BALANCE = "ACCOUNT_CURRENT_BALANCE";

        String TRANSACTION_TYPE = "TRANSACTION_TYPE";

        String TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT";

        String TRANSACTION_ACCOUNT = "TRANSACTION_ACCOUNT";

        String TRANSACTION_CATEGORY = "TRANSACTION_CATEGORY";

        String TRANSACTION_DESCRIPTION = "TRANSACTION_DESCRIPTION";

        String CONFIRM_DELETION = "CONFIRM_DELETION";
    }

    interface ModelType {

        String ACCOUNT = "ACCOUNT";

        String CATEGORY = "CATEGORY";

        String TRANSACTION = "TRANSACTION";
    }

    interface Account {

        String ID = "ACCOUNT_ID";

        String NAME = "ACCOUNT_NAME";

        String COLOR = "ACCOUNT_COLOR";

        String ICON = "ACCOUNT_ICON";

        String CURRENT_BALANCE = "ACCOUNT_CURRENT_BALANCE";

        String TOTAL_INCOME = "ACCOUNT_TOTAL_INCOME";

        String TOTAL_EXPENSE = "ACCOUNT_TOTAL_EXPENSE";

        String FOREGROUND_COLOR = "ACCOUNT_FOREGROUND_COLOR";
    }

    interface Category {

        String ID = "CATEGORY_ID";

        String NAME = "CATEGORY_NAME";

        String TYPE = "CATEGORY_TYPE";

        String COLOR = "CATEGORY_COLOR";

        String ICON = "CATEGORY_ICON";

        String TOTAL_AMOUNT = "CATEGORY_TOTAL_AMOUNT";

        String FOREGROUND_COLOR = "CATEGORY_FOREGROUND_COLOR";
    }

    interface Transaction {

        String TYPE = "TRANSACTION_TYPE";

        String SRC_ACCOUNT = "TRANSACTION_SRC_ACCOUNT";

        String DST_ACCOUNT = "TRANSACTION_DST_ACCOUNT";

        String CATEGORY = "TRANSACTION_CATEGORY";

        String AMOUNT = "TRANSACTION_AMOUNT";

        String TITLE = "TRANSACTION_TITLE";

        String DATE = "TRANSACTION_DATE";

        String DESCRIPTION = "TRANSACTION_DESCRIPTION";
    }

    interface SortOption {

        String OBJECT = "SORT_OBJECT";

        String ORDER = "SORT_ORDER";

        String TYPE = "SORT_TYPE";
    }

    interface DateOption {

        String TYPE = "DATE_OPTION_TYPE";

        String START_DATE = "DATE_OPTION_START_DATE";

        String END_DATE = "DATE_OPTION_END_DATE";
    }

    interface AmountTypeInfo {

        String AMOUNT = "AMOUNT_TYPE_AMOUNT";
    }

    interface AccountIncomeExpenseInfo {

        String INCOME_AMOUNT = "ACCOUNT_INCOME_AMOUNT";

        String INCOME_COUNT = "ACCOUNT_INCOME_COUNT";

        String EXPENSE_AMOUNT = "ACCOUNT_EXPENSE_AMOUNT";

        String EXPENSE_COUNT = "ACCOUNT_EXPENSE_COUNT";
    }

    interface CategoryAmountInfo {

        String AMOUNT = "CATEGORY_AMOUNT";

        String COUNT = "CATEGORY_COUNT";
    }

    interface DetailScreen {

        String TYPE = "DETAIL_SCREEN_TYPE";

        String DATA = "DETAIL_SCREEN_DATA";
    }
}
