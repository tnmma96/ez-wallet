package io.tnmma.ezwallet.ui;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.BindingAdapter;

import io.tnmma.ezwallet.data.model.BottomAction;
import io.tnmma.ezwallet.data.model.DateOption;
import io.tnmma.ezwallet.data.model.DateRange;
import io.tnmma.ezwallet.data.model.MenuTypeItem;
import io.tnmma.ezwallet.data.model.SortOption;
import io.tnmma.ezwallet.data.model.TransactionDetailAccount;
import io.tnmma.ezwallet.data.model.TransactionDetailCategory;
import io.tnmma.ezwallet.data.model.TransactionDetailDate;
import io.tnmma.ezwallet.data.model.TransactionDetailDescription;
import io.tnmma.ezwallet.data.model.TransactionDetailType;
import io.tnmma.ezwallet.data.model.TransactionTypeAndAmount;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.data.pojo.Category;
import io.tnmma.ezwallet.ui.customview.AmountKeyboardView;
import io.tnmma.ezwallet.ui.customview.BottomActionView;
import io.tnmma.ezwallet.ui.customview.DateOptionView;
import io.tnmma.ezwallet.ui.customview.DateRangeView;
import io.tnmma.ezwallet.ui.customview.MenuTypeView;
import io.tnmma.ezwallet.ui.customview.SortOptionView;
import io.tnmma.ezwallet.ui.customview.TransactionAccountView;
import io.tnmma.ezwallet.ui.customview.TransactionCategoryView;
import io.tnmma.ezwallet.ui.customview.TransactionDetailAccountView;
import io.tnmma.ezwallet.ui.customview.TransactionDetailCategoryView;
import io.tnmma.ezwallet.ui.customview.TransactionDetailDateView;
import io.tnmma.ezwallet.ui.customview.TransactionDetailDescriptionView;
import io.tnmma.ezwallet.ui.customview.TransactionDetailTypeView;
import io.tnmma.ezwallet.ui.customview.TransactionTypeAndAmountView;

public class IvyBindingAdapter {

    private static final String TAG = IvyBindingAdapter.class.getSimpleName();

    @BindingAdapter("app:tint")
    public static void setImageTintColor(@NonNull ImageView imageView, @ColorInt int color) {
        imageView.setColorFilter(color);
    }

    @BindingAdapter("android:textColor")
    public static void setTextColor(@NonNull TextView textView, @ColorInt int color) {
        textView.setTextColor(color);
    }

    @BindingAdapter("android:text")
    public static void setText(@NonNull TextView textView, @StringRes int stringResId) {
        textView.setText(stringResId);
    }

    @BindingAdapter("android:src")
    public static void setImage(@NonNull ImageView imageView, @DrawableRes int drawableResId) {
        imageView.setImageResource(drawableResId);
    }

    @BindingAdapter("android:maxWidth")
    public static void setMaxWidth(@NonNull TextView textView, int width) {
        textView.setMaxWidth(width);
    }

    @BindingAdapter("android:backgroundTint")
    public static void setBackgroundTintColor(@NonNull View view, @ColorInt int color) {
        view.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @BindingAdapter("menuType")
    public static void setMenuType(@NonNull MenuTypeView menuTypeView, MenuTypeItem menuTypeItem) {
        menuTypeView.setViewModel(menuTypeItem);
    }

    @BindingAdapter("bottomAction")
    public static void setBottomAction(
            @NonNull BottomActionView bottomActionView, BottomAction bottomAction) {
        bottomActionView.setViewModel(bottomAction);
    }

    @BindingAdapter("sortOption")
    public static void setSortOption(
            @NonNull SortOptionView sortOptionView, SortOption sortOption) {
        sortOptionView.setViewModel(sortOption);
    }

    @BindingAdapter("showKeyNegative")
    public static void setKeyboardShowKeyNegative(
            @NonNull AmountKeyboardView amountKeyboardView, boolean showKeyNegative) {
        amountKeyboardView.setShowKeyNegative(showKeyNegative);
    }

    @BindingAdapter("transactionAccount")
    public static void setTransactionAccount(
            @NonNull TransactionAccountView transactionAccountView, Account account) {
        transactionAccountView.setViewModel(account);
    }

    @BindingAdapter("transactionCategory")
    public static void setTransactionCategory(
            @NonNull TransactionCategoryView transactionCategoryView, Category category) {
        transactionCategoryView.setViewModel(category);
    }

    @BindingAdapter("transactionTypeAndAmount")
    public static void setTransactionTypeAndAmount(
            @NonNull TransactionTypeAndAmountView transactionTypeAndAmountView,
            TransactionTypeAndAmount transactionTypeAndAmount) {
        transactionTypeAndAmountView.setViewModel(transactionTypeAndAmount);
    }

    @BindingAdapter("dateOption")
    public static void setDateOption(
            @NonNull DateOptionView dateOptionView, DateOption dateOption) {
        dateOptionView.setViewModel(dateOption);
    }

    @BindingAdapter("dateRange")
    public static void setDateRange(@NonNull DateRangeView dateRangeView, DateRange dateRange) {
        dateRangeView.setViewModel(dateRange);
    }

    @BindingAdapter("transactionDetailType")
    public static void setTransactionDetailType(
            @NonNull TransactionDetailTypeView transactionDetailTypeView,
            TransactionDetailType transactionDetailType) {
        transactionDetailTypeView.setViewModel(transactionDetailType);
    }

    @BindingAdapter("transactionDetailAccount")
    public static void setTransactionDetailAccount(
            @NonNull TransactionDetailAccountView transactionDetailAccountView,
            TransactionDetailAccount transactionDetailAccount) {
        transactionDetailAccountView.setViewModel(transactionDetailAccount);
    }

    @BindingAdapter("transactionDetailDate")
    public static void setTransactionDetailDate(
            @NonNull TransactionDetailDateView transactionDetailDateView,
            TransactionDetailDate transactionDetailDate) {
        transactionDetailDateView.setViewModel(transactionDetailDate);
    }

    @BindingAdapter("transactionDetailCategory")
    public static void setTransactionDetailCategory(
            @NonNull TransactionDetailCategoryView transactionDetailCategoryView,
            TransactionDetailCategory transactionDetailCategory) {
        transactionDetailCategoryView.setViewModel(transactionDetailCategory);
    }

    @BindingAdapter("transactionDetailDescription")
    public static void setTransactionDetailDescription(
            @NonNull TransactionDetailDescriptionView transactionDetailDescriptionView,
            TransactionDetailDescription transactionDetailDescription) {
        transactionDetailDescriptionView.setViewModel(transactionDetailDescription);
    }
}
