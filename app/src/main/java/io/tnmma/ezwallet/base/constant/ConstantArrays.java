package io.tnmma.ezwallet.base.constant;

import io.tnmma.ezwallet.data.model.CategoryTypeItem;
import io.tnmma.ezwallet.data.model.ColorItem;
import io.tnmma.ezwallet.data.model.DateTypeItem;
import io.tnmma.ezwallet.data.model.MenuTypeItem;
import io.tnmma.ezwallet.data.model.SortOrderItem;
import io.tnmma.ezwallet.data.model.SortTypeItem;
import io.tnmma.ezwallet.data.model.TransactionTypeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConstantArrays {

    private static final String TAG = ConstantArrays.class.getSimpleName();

    private static final Map<MenuType, MenuTypeItem> MAIN_MENU_MAP;

    private static final List<TransactionTypeItem> TRANSACTION_TYPE_LIST;

    private static final List<CategoryTypeItem> CATEGORY_TYPE_LIST;

    private static final List<DateTypeItem> DATE_TYPE_LIST;

    private static final List<SortTypeItem> ACCOUNT_SORT_TYPE_LIST;

    private static final List<SortTypeItem> CATEGORY_SORT_TYPE_LIST;

    private static final List<SortOrderItem> SORT_ORDER_LIST;

    private static final List<ColorItem> COLOR_LIST;

    static {
        MAIN_MENU_MAP =
                Map.of(
                        MenuType.HOME, new MenuTypeItem(MenuType.HOME),
                        MenuType.ACCOUNT, new MenuTypeItem(MenuType.ACCOUNT),
                        MenuType.CATEGORY, new MenuTypeItem(MenuType.CATEGORY),
                        MenuType.USER, new MenuTypeItem(MenuType.USER));
    }

    static {
        TRANSACTION_TYPE_LIST =
                List.of(
                        new TransactionTypeItem(TransactionType.INCOME),
                        new TransactionTypeItem(TransactionType.EXPENSE),
                        new TransactionTypeItem(TransactionType.TRANSFER));
    }

    static {
        CATEGORY_TYPE_LIST =
                List.of(
                        new CategoryTypeItem(CategoryType.INCOME),
                        new CategoryTypeItem(CategoryType.EXPENSE));
    }

    static {
        DATE_TYPE_LIST =
                List.of(
                        new DateTypeItem(DateType.TODAY),
                        new DateTypeItem(DateType.THIS_WEEK),
                        new DateTypeItem(DateType.THIS_MONTH),
                        new DateTypeItem(DateType.THIS_YEAR),
                        new DateTypeItem(DateType.CUSTOM_RANGE),
                        new DateTypeItem(DateType.ALL_TIME));
    }

    static {
        ACCOUNT_SORT_TYPE_LIST =
                List.of(
                        new SortTypeItem(SortType.CREATED_TIME),
                        new SortTypeItem(SortType.NAME),
                        new SortTypeItem(SortType.BALANCE),
                        new SortTypeItem(SortType.INCOME),
                        new SortTypeItem(SortType.EXPENSE));
    }

    static {
        CATEGORY_SORT_TYPE_LIST =
                List.of(
                        new SortTypeItem(SortType.CREATED_TIME),
                        new SortTypeItem(SortType.NAME),
                        new SortTypeItem(SortType.AMOUNT));
    }

    static {
        SORT_ORDER_LIST =
                List.of(
                        new SortOrderItem(SortOrder.ASCENDING),
                        new SortOrderItem(SortOrder.DESCENDING));
    }

    static {
        COLOR_LIST =
                List.of(
                        new ColorItem(Color.BLUE_1),
                        new ColorItem(Color.BLUE_2),
                        new ColorItem(Color.BLUE_3),
                        new ColorItem(Color.GREEN_1),
                        new ColorItem(Color.GREEN_2),
                        new ColorItem(Color.GREEN_3),
                        new ColorItem(Color.GREEN_4),
                        new ColorItem(Color.RED_1),
                        new ColorItem(Color.RED_2),
                        new ColorItem(Color.RED_3),
                        new ColorItem(Color.PURPLE_1),
                        new ColorItem(Color.PURPLE_2),
                        new ColorItem(Color.PURPLE_3),
                        new ColorItem(Color.ORANGE_1),
                        new ColorItem(Color.ORANGE_2),
                        new ColorItem(Color.ORANGE_3),
                        new ColorItem(Color.YELLOW_1),
                        new ColorItem(Color.BLUE_1_LIGHT),
                        new ColorItem(Color.BLUE_2_LIGHT),
                        new ColorItem(Color.BLUE_3_LIGHT),
                        new ColorItem(Color.GREEN_1_LIGHT),
                        new ColorItem(Color.GREEN_2_LIGHT),
                        new ColorItem(Color.GREEN_3_LIGHT),
                        new ColorItem(Color.GREEN_4_LIGHT),
                        new ColorItem(Color.RED_1_LIGHT),
                        new ColorItem(Color.RED_2_LIGHT),
                        new ColorItem(Color.RED_3_LIGHT),
                        new ColorItem(Color.PURPLE_1_LIGHT),
                        new ColorItem(Color.PURPLE_2_LIGHT),
                        new ColorItem(Color.PURPLE_3_LIGHT),
                        new ColorItem(Color.ORANGE_1_LIGHT),
                        new ColorItem(Color.ORANGE_2_LIGHT),
                        new ColorItem(Color.ORANGE_3_LIGHT),
                        new ColorItem(Color.YELLOW_1_LIGHT),
                        new ColorItem(Color.BLUE_1_DARK),
                        new ColorItem(Color.BLUE_2_DARK),
                        new ColorItem(Color.BLUE_3_DARK),
                        new ColorItem(Color.GREEN_1_DARK),
                        new ColorItem(Color.GREEN_2_DARK),
                        new ColorItem(Color.GREEN_3_DARK),
                        new ColorItem(Color.GREEN_4_DARK),
                        new ColorItem(Color.RED_1_DARK),
                        new ColorItem(Color.RED_2_DARK),
                        new ColorItem(Color.RED_3_DARK),
                        new ColorItem(Color.PURPLE_1_DARK),
                        new ColorItem(Color.PURPLE_2_DARK),
                        new ColorItem(Color.PURPLE_3_DARK),
                        new ColorItem(Color.ORANGE_1_DARK),
                        new ColorItem(Color.ORANGE_2_DARK),
                        new ColorItem(Color.ORANGE_3_DARK),
                        new ColorItem(Color.YELLOW_1_DARK));
    }

    public static Map<MenuType, MenuTypeItem> getMainMenuMap() {
        return MAIN_MENU_MAP;
    }

    public static List<TransactionTypeItem> getTransactionTypeList() {
        return TRANSACTION_TYPE_LIST;
    }

    public static List<CategoryTypeItem> getCategoryTypeList() {
        return CATEGORY_TYPE_LIST;
    }

    public static List<DateTypeItem> getDateTypeList() {
        return DATE_TYPE_LIST;
    }

    public static List<SortTypeItem> getSortTypeList(SortObject sortObject) {
        if (sortObject != null) {
            switch (sortObject) {
                case ACCOUNT:
                    return ACCOUNT_SORT_TYPE_LIST;
                case CATEGORY:
                    return CATEGORY_SORT_TYPE_LIST;
            }
        }
        return new ArrayList<>();
    }

    public static List<SortOrderItem> getSortOrderList() {
        return SORT_ORDER_LIST;
    }

    public static List<ColorItem> getColorList() {
        return COLOR_LIST;
    }
}
